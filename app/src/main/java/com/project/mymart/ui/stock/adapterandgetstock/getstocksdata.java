package com.project.mymart.ui.stock.adapterandgetstock;

public class getstocksdata {

    String stock_id, stockproduct_name,supplier_id,category_id,quantity,sale_rate,supplier_rate,stock_date,update_date;

    public getstocksdata(String stock_id, String stockproduct_name, String supplier_id, String category_id, String quantity, String sale_rate, String supplier_rate, String stock_date, String update_date) {
        this.stock_id = stock_id;
        this.stockproduct_name = stockproduct_name;
        this.supplier_id = supplier_id;
        this.category_id = category_id;
        this.quantity = quantity;
        this.sale_rate = sale_rate;
        this.supplier_rate = supplier_rate;
        this.stock_date = stock_date;
        this.update_date = update_date;
    }

    public String getStock_id() {
        return stock_id;
    }

    public void setStock_id(String stock_id) {
        this.stock_id = stock_id;
    }

    public String getStockproduct_name() {
        return stockproduct_name;
    }

    public void setStockproduct_name(String stockproduct_name) {
        this.stockproduct_name = stockproduct_name;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSale_rate() {
        return sale_rate;
    }

    public void setSale_rate(String sale_rate) {
        this.sale_rate = sale_rate;
    }

    public String getSupplier_rate() {
        return supplier_rate;
    }

    public void setSupplier_rate(String supplier_rate) {
        this.supplier_rate = supplier_rate;
    }

    public String getStock_date() {
        return stock_date;
    }

    public void setStock_date(String stock_date) {
        this.stock_date = stock_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }
}
