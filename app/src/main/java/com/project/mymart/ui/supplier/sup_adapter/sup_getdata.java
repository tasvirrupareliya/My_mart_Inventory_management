package com.project.mymart.ui.supplier.sup_adapter;

public class sup_getdata {

    String supplier_id, companyname, website, first_name, last_name, contact, email, address_line1, address_line2, state, city, pincode, username;

    public sup_getdata(String supplier_id, String companyname, String website, String first_name, String last_name, String contact, String email, String address_line1, String address_line2, String state, String city, String pincode, String username) {
        this.supplier_id = supplier_id;
        this.companyname = companyname;
        this.website = website;
        this.first_name = first_name;
        this.last_name = last_name;
        this.contact = contact;
        this.email = email;
        this.address_line1 = address_line1;
        this.address_line2 = address_line2;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
        this.username = username;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress_line1() {
        return address_line1;
    }

    public void setAddress_line1(String address_line1) {
        this.address_line1 = address_line1;
    }

    public String getAddress_line2() {
        return address_line2;
    }

    public void setAddress_line2(String address_line2) {
        this.address_line2 = address_line2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
