package ca.realsense.realsensegtadev.data;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.realsense.realsensegtadev.entity.DataEntryCollection;
import ca.realsense.realsensegtadev.entity.DataEntry;

public class DataService {

    private static DataService instance = new DataService();

    private DataService(){}

    public static DataService getInstance(){
        return instance;
    }

    // retrieve data, multiple lines, each line is for a year, for a chart
    public List<DataEntryCollection> retrieveChartData(DataSnapshot dataSnapshot, String areaPropertyType, int yearToReport) {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        List<DataEntryCollection> dataList = new ArrayList<DataEntryCollection>();

        for (int yInt = yearToReport; yInt > 0; yInt--) {

            int processingYear = currentYear - yInt + 1;

            DataEntryCollection dataEntryCollection = retrieveDataEntryCollection(dataSnapshot, areaPropertyType, processingYear);

            if (null != dataEntryCollection) {
                dataList.add(dataEntryCollection);
            }
        }

        return dataList;
    }

    // retrieve data, multiple lines, each line is for a area, each line contains multiple years, for a chart
    public List<DataEntryCollection> retrieveChartData(DataSnapshot dataSnapshot, List<String> areaPropertyTypeList, int yearToReport) {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        List<DataEntryCollection> dataList = new ArrayList<DataEntryCollection>();

        for(String areaPropertyType : areaPropertyTypeList){

            DataEntryCollection dataEntryCollection = new DataEntryCollection();

            for (int yInt = yearToReport; yInt > 0; yInt--) {

                int processingYear = currentYear - yInt + 1;

                DataEntryCollection annualDataEntryCollection = retrieveDataEntryCollection(dataSnapshot, areaPropertyType, processingYear);

                if(null == dataEntryCollection.getAreaName()){
                    dataEntryCollection.setAreaName(annualDataEntryCollection.getAreaName());
                }

                if(null == dataEntryCollection.getPropertyType()){
                    dataEntryCollection.setPropertyType(annualDataEntryCollection.getPropertyType());
                }

                dataEntryCollection.getDataEntryMap().putAll(annualDataEntryCollection.getDataEntryMap());
            }

            dataList.add(dataEntryCollection);
        }

        return dataList;
    }

    // retrieve all available data for a specified area, property type, for a specified year
    public DataEntryCollection retrieveDataEntryCollection(DataSnapshot dataSnapshot, String areaPropertyType, int year) {

        // construct the data key for a data collection
        String DataEntryCollectionKey = areaPropertyType + "_" + year;
        // the returning data collection
        DataEntryCollection dataEntryCollection = null;

        // retrieve a snapshot of the collection
        DataSnapshot annualDataChild = dataSnapshot.child(DataEntryCollectionKey);

        // if the data is available
        if(annualDataChild.exists()){

            // initialize the collection
            dataEntryCollection = new DataEntryCollection();

            // initialize collection level properties
            dataEntryCollection.setKey(DataEntryCollectionKey);
            dataEntryCollection.setYear(year);

            String titleAreaName = (String)annualDataChild.child("area_name").getValue();
            String titlePropertyName = (String) annualDataChild.child("property_type").getValue();

            dataEntryCollection.setAreaName(titleAreaName);
            dataEntryCollection.setPropertyType(titlePropertyName);

            // fill the collection with data entries
            for (DataSnapshot monthlyDataChild : annualDataChild.child("monthly_data").getChildren()) {

                String key = monthlyDataChild.getKey();

                String areaName = (String) monthlyDataChild.child("area_name").getValue();
                String propertyType = (String) monthlyDataChild.child("property_type").getValue();
                Long yearLong = (Long) monthlyDataChild.child("year").getValue();
                Long monthLong = (Long) monthlyDataChild.child("month").getValue();

                Long averagePriceLong = (Long) monthlyDataChild.child("average_price").getValue();
                Long medianPriceLong = (Long) monthlyDataChild.child("median_price").getValue();
                Long salesLong = (Long) monthlyDataChild.child("sales").getValue();
                Long newListingsLong = (Long) monthlyDataChild.child("new_listings").getValue();
                Long activeListingsLong = (Long) monthlyDataChild.child("active_listings").getValue();
                Long slRatioLong = (Long) monthlyDataChild.child("average_splp").getValue();
                Long domLong = (Long) monthlyDataChild.child("average_dom").getValue();

                DataEntry dataEntry = new DataEntry();
                dataEntry.setAreaName(areaName);
                dataEntry.setPropertyType(propertyType);
                dataEntry.setYear(yearLong.intValue());
                dataEntry.setMonth(monthLong.intValue());
                dataEntry.setAveragePrice(averagePriceLong.intValue());
                dataEntry.setMedianPrice(medianPriceLong.intValue());
                dataEntry.setSales(salesLong.intValue());
                dataEntry.setNewListings(newListingsLong.intValue());
                dataEntry.setActiveListings(activeListingsLong.intValue());
                dataEntry.setSplp(slRatioLong.intValue());
                dataEntry.setDom(domLong.intValue());

                // sorted by month
                //dataEntryCollection.getDataEntryMap().put(Integer.valueOf(dataEntry.getMonth()), dataEntry);

                // sorted by key, year_month
                dataEntryCollection.getDataEntryMap().put(Integer.valueOf(key), dataEntry);

            }
        }

        return dataEntryCollection;
    }

}
