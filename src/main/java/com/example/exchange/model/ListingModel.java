package com.example.exchange.model;

public class ListingModel {
    private int id;
    private String name;
    private String description;
    private String condition;
    private int quantity;
    private String imageUrl;
    private int userId;
    private CategoryModel category;
    private int offers;
    private String offerStatus;
    private boolean visible = true;

    public ListingModel(int id,
            String name,
            String description,
            String condition,
            int quantity,
            String imageUrl,
            int userId,
            CategoryModel category,
            int offers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.category = category;
        this.offers = offers;

        this.offerStatus = quantity <= 0 ? "disabled" : "";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCondition() {
        return condition;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getUserId() {
        return userId;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public int getOffers() {
        return offers;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
