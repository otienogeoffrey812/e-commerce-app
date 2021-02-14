package com.example.slickkwear.Model;

public class Products {
    private String ProductUniqueID, ProductName, ProductPrice, ProductDescription, ProductImage, ProductCategory, ProductStatus, DateCreated, TimeCreated;

    public Products() {
    }

    public Products(String productUniqueID, String productName, String productPrice, String productDescription, String productImage, String productCategory, String productStatus, String dateCreated, String timeCreated) {
        ProductUniqueID = productUniqueID;
        ProductName = productName;
        ProductPrice = productPrice;
        ProductDescription = productDescription;
        ProductImage = productImage;
        ProductCategory = productCategory;
        ProductStatus = productStatus;
        DateCreated = dateCreated;
        TimeCreated = timeCreated;
    }

    public String getProductUniqueID() {
        return ProductUniqueID;
    }

    public void setProductUniqueID(String productUniqueID) {
        ProductUniqueID = productUniqueID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }

    public String getProductStatus() {
        return ProductStatus;
    }

    public void setProductStatus(String productStatus) {
        ProductStatus = productStatus;
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
