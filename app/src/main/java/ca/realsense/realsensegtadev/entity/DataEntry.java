package ca.realsense.realsensegtadev.entity;

import ca.realsense.realsensegtadev.util.Constants;

public class DataEntry {

    private int dataType = Constants.DATA_TYPE_UNKNOWN;

    private int year = 0;
    private int month = 0;
    private String areaName = null;
    private String propertyType = null;

    private int sales = 0;
    private int averagePrice = 0;
    private int medianPrice = 0;
    private int newListings = 0;
    private int activeListings = 0;
    private int splp = 0;
    private int dom = 0;

    // periodical changes on each number. the period may be monthly or weekly
    private float salesPc = 0.0f;
    private float averagePricePc = 0.0f;
    private float medianPricePc = 0.0f;
    private float newListingsPc = 0.0f;
    private float activeListingsPc = 0.0f;
    private float splpPc = 0.0f;
    private float domPc = 0.0f;

    // annual changes on each number
    private float salesAc = 0.0f;
    private float averagePriceAc = 0.0f;
    private float medianPriceAc = 0.0f;
    private float newListingsAc = 0.0f;
    private float activeListingsAc = 0.0f;
    private float splpAc = 0.0f;
    private float domAc = 0.0f;

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(int averagePrice) {
        this.averagePrice = averagePrice;
    }

    public int getMedianPrice() {
        return medianPrice;
    }

    public void setMedianPrice(int medianPrice) {
        this.medianPrice = medianPrice;
    }

    public int getNewListings() {
        return newListings;
    }

    public void setNewListings(int newListings) {
        this.newListings = newListings;
    }

    public int getActiveListings() {
        return activeListings;
    }

    public void setActiveListings(int activeListings) {
        this.activeListings = activeListings;
    }

    public int getSplp() {
        return splp;
    }

    public void setSplp(int splp) {
        this.splp = splp;
    }

    public int getDom() {
        return dom;
    }

    public void setDom(int dom) {
        this.dom = dom;
    }

    public float getSalesPc() {
        return salesPc;
    }

    public void setSalesPc(float salesPc) {
        this.salesPc = salesPc;
    }

    public float getAveragePricePc() {
        return averagePricePc;
    }

    public void setAveragePricePc(float averagePricePc) {
        this.averagePricePc = averagePricePc;
    }

    public float getMedianPricePc() {
        return medianPricePc;
    }

    public void setMedianPricePc(float medianPricePc) {
        this.medianPricePc = medianPricePc;
    }

    public float getNewListingsPc() {
        return newListingsPc;
    }

    public void setNewListingsPc(float newListingsPc) {
        this.newListingsPc = newListingsPc;
    }

    public float getActiveListingsPc() {
        return activeListingsPc;
    }

    public void setActiveListingsPc(float activeListingsPc) {
        this.activeListingsPc = activeListingsPc;
    }

    public float getSplpPc() {
        return splpPc;
    }

    public void setSplpPc(float splpPc) {
        this.splpPc = splpPc;
    }

    public float getDomPc() {
        return domPc;
    }

    public void setDomPc(float domPc) {
        this.domPc = domPc;
    }

    public float getSalesAc() {
        return salesAc;
    }

    public void setSalesAc(float salesAc) {
        this.salesAc = salesAc;
    }

    public float getAveragePriceAc() {
        return averagePriceAc;
    }

    public void setAveragePriceAc(float averagePriceAc) {
        this.averagePriceAc = averagePriceAc;
    }

    public float getMedianPriceAc() {
        return medianPriceAc;
    }

    public void setMedianPriceAc(float medianPriceAc) {
        this.medianPriceAc = medianPriceAc;
    }

    public float getNewListingsAc() {
        return newListingsAc;
    }

    public void setNewListingsAc(float newListingsAc) {
        this.newListingsAc = newListingsAc;
    }

    public float getActiveListingsAc() {
        return activeListingsAc;
    }

    public void setActiveListingsAc(float activeListingsAc) {
        this.activeListingsAc = activeListingsAc;
    }

    public float getSplpAc() {
        return splpAc;
    }

    public void setSplpAc(float splpAc) {
        this.splpAc = splpAc;
    }

    public float getDomAc() {
        return domAc;
    }

    public void setDomAc(float domAc) {
        this.domAc = domAc;
    }
}
