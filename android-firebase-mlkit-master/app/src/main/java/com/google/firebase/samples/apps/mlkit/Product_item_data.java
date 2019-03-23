package com.google.firebase.samples.apps.mlkit;

public class Product_item_data {

    public String product_Image;
    public String product_name;
    public String product_price;
    public String product_decription;

    public Product_item_data() {
    }

    public Product_item_data(String product_Image, String product_name, String product_price, String product_decription) {
        this.product_Image = product_Image;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_decription = product_decription;
    }

    public String getProduct_Image() {
        return product_Image;
    }

    public void setProduct_Image(String product_Image) {
        this.product_Image = product_Image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_decription() {
        return product_decription;
    }

    public void setProduct_decription(String product_decription) {
        this.product_decription = product_decription;
    }
}
