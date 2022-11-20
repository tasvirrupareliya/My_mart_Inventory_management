package com.project.mymart.ui.reports.adapterandget;

public class get_salesdata {

    String order_date,client_name,client_contact,grand_total;

    public get_salesdata(String order_date, String client_name, String client_contact, String grand_total) {
        this.order_date = order_date;
        this.client_name = client_name;
        this.client_contact = client_contact;
        this.grand_total = grand_total;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_contact() {
        return client_contact;
    }

    public void setClient_contact(String client_contact) {
        this.client_contact = client_contact;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }
}
