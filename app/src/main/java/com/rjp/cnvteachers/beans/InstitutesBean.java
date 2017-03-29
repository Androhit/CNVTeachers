package com.rjp.cnvteachers.beans;

/**
 * Created by rohit on 27/10/16.
 */
public class InstitutesBean
{
    private String chrDocNo,code,inst_link,api_link,db_name,name,logo_link,web_site,description;
    private int bitActive;

    public String getChrDocNo() {
        return chrDocNo;
    }

    public void setChrDocNo(String chrDocNo) {
        this.chrDocNo = chrDocNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInst_link() {
        return inst_link;
    }

    public void setInst_link(String inst_link) {
        this.inst_link = inst_link;
    }

    public String getApi_link() {
        return api_link;
    }

    public void setApi_link(String api_link) {
        this.api_link = api_link;
    }

    public String getDb_name() {
        return db_name;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
    }

    public String getLogo_link() {
        return logo_link;
    }

    public void setLogo_link(String logo_link) {
        this.logo_link = logo_link;
    }

    public String getWeb_site() {
        return web_site;
    }

    public void setWeb_site(String web_site) {
        this.web_site = web_site;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBitActive() {
        return bitActive;
    }

    public void setBitActive(int bitActive) {
        this.bitActive = bitActive;
    }
}
