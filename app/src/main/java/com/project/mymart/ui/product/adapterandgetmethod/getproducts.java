package com.project.mymart.ui.product.adapterandgetmethod;

public class getproducts {

    String product_id, product_name, category_id, product_image, mrp, product_status;

    public getproducts(String product_id, String product_name, String category_id, String product_image, String mrp, String product_status) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.category_id = category_id;
        this.product_image = product_image;
        this.mrp = mrp;
        this.product_status = product_status;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getProduct_status() {
        return product_status;
    }

    public void setProduct_status(String product_status) {
        this.product_status = product_status;
    }
}
