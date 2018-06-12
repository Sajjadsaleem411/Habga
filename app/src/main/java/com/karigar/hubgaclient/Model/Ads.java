
package com.karigar.hubgaclient.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ads {

    @SerializedName("AdsID")
    @Expose
    private Integer adsID;
    @SerializedName("expiry")
    @Expose
    private String expiry;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("advertisment")
    @Expose
    private String advertisment;
    @SerializedName("catID")
    @Expose
    private Integer catID;
    @SerializedName("vendorid")
    @Expose
    private Integer vendorid;
    @SerializedName("serviceId")
    @Expose
    private Integer serviceId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("days")
    @Expose
    private Integer days;
    @SerializedName("flag")
    @Expose
    private Integer flag;

    public Integer getAdsID() {
        return adsID;
    }

    public void setAdsID(Integer adsID) {
        this.adsID = adsID;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getAdvertisment() {
        return advertisment;
    }

    public void setAdvertisment(String advertisment) {
        this.advertisment = advertisment;
    }

    public Integer getCatID() {
        return catID;
    }

    public void setCatID(Integer catID) {
        this.catID = catID;
    }

    public Integer getVendorid() {
        return vendorid;
    }

    public void setVendorid(Integer vendorid) {
        this.vendorid = vendorid;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

}
