package com.example.exchange.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.exchange.dao.RecommendationDao;
import com.example.exchange.helper.ResponseT;
import com.example.exchange.model.ListingModel;

import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.AssociationRules;
import weka.associations.Item;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Hybrid recommender combining:
 * 1. Content-based: User's preferred categories from offer history
 * 2. Weka Apriori: Association rules (category X -> category Y) from all offers
 * 3. Popularity: Fallback to popular items when no preferences
 */
public class RecommendationService {

    private static final int MAX_RECOMMENDATIONS = 12;

    private final RecommendationDao recommendationDao;

    private Map<String, List<String>> categoryRules;
    private long rulesLastBuilt = 0;
    private static final long RULES_CACHE_MS = 60_000;

    public RecommendationService() {
        this.recommendationDao = new RecommendationDao();
    }

    public List<ListingModel> getRecommendations(Integer userId) {
        if (userId == null) {
            return getPopularRecommendations(-1);
        }
        return getPersonalizedRecommendations(userId);
    }

    public List<ListingModel> getPopularRecommendations(int excludeUserId) {
        ResponseT<List<ListingModel>> resp = recommendationDao.getPopularListingsExceptUser(excludeUserId, MAX_RECOMMENDATIONS);
        return resp.getSucceeded() && resp.getData() != null ? resp.getData() : new ArrayList<>();
    }

    private List<ListingModel> getPersonalizedRecommendations(int userId) {
        Set<Integer> seenIds = new HashSet<>();
        List<ListingModel> results = new ArrayList<>();
        ResponseT<List<String>> prefsResp = recommendationDao.getPreferredCategoriesByUserId(userId);
        List<String> prefs = (prefsResp.getSucceeded() && prefsResp.getData() != null) ? prefsResp.getData() : new ArrayList<>();

        // 1. Content-based: preferred categories
        if (!prefs.isEmpty()) {
            ResponseT<List<ListingModel>> catResp = recommendationDao.getListingsByCategoriesExceptUser(prefs, userId, MAX_RECOMMENDATIONS);
            if (catResp.getSucceeded() && catResp.getData() != null) {
                for (ListingModel m : catResp.getData()) {
                    if (results.size() >= MAX_RECOMMENDATIONS) break;
                    if (seenIds.add(m.getId())) results.add(m);
                }
            }
        }

        // 2. Weka association rules
        if (results.size() < MAX_RECOMMENDATIONS) {
            Map<String, List<String>> rules = buildCategoryRulesWithWeka();
            for (String userCat : prefs) {
                List<String> targetCats = rules.get(userCat.toLowerCase());
                if (targetCats != null && !targetCats.isEmpty()) {
                    ResponseT<List<ListingModel>> ruleResp = recommendationDao.getListingsByCategoriesExceptUser(targetCats, userId, MAX_RECOMMENDATIONS - results.size());
                    if (ruleResp.getSucceeded() && ruleResp.getData() != null) {
                        for (ListingModel m : ruleResp.getData()) {
                            if (results.size() >= MAX_RECOMMENDATIONS) break;
                            if (seenIds.add(m.getId())) results.add(m);
                        }
                    }
                }
            }
        }

        // 3. Popularity fallback
        if (results.size() < MAX_RECOMMENDATIONS) {
            ResponseT<List<ListingModel>> popResp = recommendationDao.getPopularListingsExceptUser(userId, MAX_RECOMMENDATIONS);
            if (popResp.getSucceeded() && popResp.getData() != null) {
                for (ListingModel m : popResp.getData()) {
                    if (results.size() >= MAX_RECOMMENDATIONS) break;
                    if (seenIds.add(m.getId())) results.add(m);
                }
            }
        }

        return results;
    }

    private Map<String, List<String>> buildCategoryRulesWithWeka() {
        if (categoryRules != null && System.currentTimeMillis() - rulesLastBuilt < RULES_CACHE_MS) {
            return categoryRules;
        }

        ResponseT<List<String[]>> pairsResp = recommendationDao.getCategoryPairsFromOffers();
        if (!pairsResp.getSucceeded() || pairsResp.getData() == null || pairsResp.getData().size() < 5) {
            return categoryRules != null ? categoryRules : new HashMap<>();
        }

        try {
            Set<String> allCats = new HashSet<>();
            for (String[] p : pairsResp.getData()) {
                if (p[0] != null) allCats.add(p[0].toLowerCase());
                if (p[1] != null) allCats.add(p[1].toLowerCase());
            }
            ArrayList<String> catList = new ArrayList<>(allCats);
            Attribute offerorAttr = new Attribute("offerorCategory", catList);
            Attribute offereeAttr = new Attribute("offereeCategory", catList);

            ArrayList<Attribute> attrs = new ArrayList<>();
            attrs.add(offerorAttr);
            attrs.add(offereeAttr);

            Instances data = new Instances("CategoryPairs", attrs, 0);

            for (String[] p : pairsResp.getData()) {
                if (p[0] != null && p[1] != null && catList.contains(p[0].toLowerCase()) && catList.contains(p[1].toLowerCase())) {
                    Instance inst = new DenseInstance(2);
                    inst.setValue(offerorAttr, p[0].toLowerCase());
                    inst.setValue(offereeAttr, p[1].toLowerCase());
                    data.add(inst);
                }
            }

            if (data.numInstances() < 5) return categoryRules != null ? categoryRules : new HashMap<>();

            Apriori apriori = new Apriori();
            apriori.setMinMetric(0.2);
            apriori.setLowerBoundMinSupport(0.05);
            apriori.setUpperBoundMinSupport(1.0);
            apriori.setNumRules(30);
            apriori.buildAssociations(data);

            Map<String, List<String>> rules = new HashMap<>();
            AssociationRules ar = apriori.getAssociationRules();
            if (ar != null) {
                for (AssociationRule rule : ar.getRules()) {
                    String premise = extractCatFromRule(rule.getPremise());
                    String consequence = extractCatFromRule(rule.getConsequence());
                    if (premise != null && consequence != null && !premise.equals(consequence)) {
                        rules.computeIfAbsent(premise, k -> new ArrayList<>()).add(consequence);
                    }
                }
            }
            categoryRules = rules;
            rulesLastBuilt = System.currentTimeMillis();
            return categoryRules;
        } catch (Exception e) {
            return categoryRules != null ? categoryRules : new HashMap<>();
        }
    }

    private String extractCatFromRule(Collection<Item> items) {
        if (items == null || items.isEmpty()) return null;
        Item first = items.iterator().next();
        if (first != null) {
            String val = first.getItemValueAsString();
            return val != null ? val.toLowerCase() : null;
        }
        return null;
    }
}
