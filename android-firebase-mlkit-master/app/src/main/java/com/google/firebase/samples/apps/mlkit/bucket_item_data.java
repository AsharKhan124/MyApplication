package com.google.firebase.samples.apps.mlkit;

public class bucket_item_data {
    public String product_code;
    public String product_price;

    public bucket_item_data() {
    }

    public bucket_item_data(String product_code, String product_price) {
        this.product_code = product_code;
        this.product_price = product_price;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }
}
