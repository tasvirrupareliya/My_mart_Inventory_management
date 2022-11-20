package com.project.mymart.ui.category.addcategory_adapter;

public class get_setmethod {

    String cname, cstatus, cid;

    public get_setmethod(String cid, String cname, String cstatus) {
        this.cid = cid;
        this.cname = cname;
        this.cstatus = cstatus;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCstatus() {
        return cstatus;
    }

    public void setCstatus(String cstatus) {
        this.cstatus = cstatus;
    }
}
