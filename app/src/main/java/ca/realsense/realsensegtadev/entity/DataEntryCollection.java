package ca.realsense.realsensegtadev.entity;

import java.util.TreeMap;

import ca.realsense.realsensegtadev.util.Constants;

public class DataEntryCollection {

    // monthly or weekly, for future use
    private int dataType = Constants.DATA_TYPE_UNKNOWN;

    private String key = null;

    private Integer year = null;

    private String areaName = null;

    private String propertyType = null;

    // use Integer for sorting purpose. it depends on the data. it may be sorted by key or one of values
    private TreeMap<Integer, DataEntry> dataEntryMap = new TreeMap<Integer, DataEntry>();

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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

    public TreeMap<Integer, DataEntry> getDataEntryMap() {
        return dataEntryMap;
    }

    public void setDataEntryMap(TreeMap<Integer, DataEntry> dataEntryMap) {
        this.dataEntryMap = dataEntryMap;
    }
}