package com.example.exchange.entity;

public class ListingEntity extends BaseEntity {
    private String name;
    private String description;
    private String condition;
    private int quantity;
    private String imageUrl;
    private boolean visible;
    private int userId;
    private int categoryId;

    public ListingEntity(String name,
            String description,
            String condition,
            int quantity,
            String imageUrl,
            boolean visible,
            int userId,
            int categoryId) {
        super();
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.visible = visible;
        this.userId = userId;
        this.categoryId = categoryId;
    }

    public ListingEntity(int id,
            String name,
            String description,
            String condition,
            int quantity,
            String imageUrl,
            boolean visible,
            int userId,
            int categoryId) {
        super(id);
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.visible = visible;
        this.userId = userId;
        this.categoryId = categoryId;
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

    public boolean isVisible() {
        return visible;
    }

    public int getUserId() {
        return userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

}
