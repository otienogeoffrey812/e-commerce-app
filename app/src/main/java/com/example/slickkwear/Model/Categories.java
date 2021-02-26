package com.example.slickkwear.Model;

public class Categories {
    private String CategoryUniqueID, CategoryName, CategoryImage, CategoryStatus,CategoryDeleted, DateCreated, TimeCreated;

    public Categories() {
    }

    public Categories(String categoryUniqueID, String categoryName, String categoryImage, String categoryStatus, String categoryDeleted, String dateCreated, String timeCreated) {
        CategoryUniqueID = categoryUniqueID;
        CategoryName = categoryName;
        CategoryImage = categoryImage;
        CategoryStatus = categoryStatus;
        CategoryDeleted = categoryDeleted;
        DateCreated = dateCreated;
        TimeCreated = timeCreated;
    }

    public String getCategoryUniqueID() {
        return CategoryUniqueID;
    }

    public void setCategoryUniqueID(String categoryUniqueID) {
        CategoryUniqueID = categoryUniqueID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }

    public String getCategoryStatus() {
        return CategoryStatus;
    }

    public void setCategoryStatus(String categoryStatus) {
        CategoryStatus = categoryStatus;
    }

    public String getCategoryDeleted() {
        return CategoryDeleted;
    }

    public void setCategoryDeleted(String categoryDeleted) {
        CategoryDeleted = categoryDeleted;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getTimeCreated() {
        return TimeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        TimeCreated = timeCreated;
    }
}
