package com.example.exchange.model;

public class InterestModel {
    private int interestId;
    private ListingModel offeror;
    private ListingModel offeree;
    private int offerorQuantity;
    private int offereeQuantity;
    private String offereeStatus;

    public InterestModel(int interestId, int offerorQuantity, int offereeQuantity,
            String offereeStatus, ListingModel offeror, ListingModel offeree) {
        this.interestId = interestId;
        this.offeror = offeror;
        this.offeree = offeree;
        this.offerorQuantity = offerorQuantity;
        this.offereeQuantity = offereeQuantity;
        this.offereeStatus = offereeStatus;
    }

    public int getInterestId() {
        return interestId;
    }

    public ListingModel getOfferor() {
        return offeror;
    }

    public ListingModel getOfferee() {
        return offeree;
    }

    public int getOfferorQuantity() {
        return offerorQuantity;
    }

    public int getOffereeQuantity() {
        return offereeQuantity;
    }

    public String getOffereeStatus() {
        return offereeStatus;
    }
}
