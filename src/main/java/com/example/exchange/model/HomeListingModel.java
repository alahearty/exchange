package com.example.exchange.model;

import java.util.List;

public class HomeListingModel {
    private String selectedCategory;
    private List<CategoryModel> categories;
    private List<ListingModel> listings;

    public HomeListingModel(String selectedCategory, List<CategoryModel> categories, List<ListingModel> listings) {
        this.selectedCategory = selectedCategory;
        this.categories = categories;
        this.listings = listings;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public List<ListingModel> getListings() {
        return listings;
    }
}
