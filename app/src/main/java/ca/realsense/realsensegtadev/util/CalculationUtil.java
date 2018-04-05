package ca.realsense.realsensegtadev.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.realsense.realsensegtadev.entity.DataEntryCollection;
import ca.realsense.realsensegtadev.entity.DataEntry;

/**
 * Created by joewang on 2017-11-15.
 */

public class CalculationUtil {

    // calculate change rates on each number of current year data
    // two calculation types, so far: changes comparing with previous month, changes comparing with same month of previous year
    public static void calculateRatesForCurrentYear(DataEntryCollection currentYearData, DataEntryCollection lastYearData) {

        // those maps are sorted by key, month, since the implementation is TreeMap
        Map<Integer, DataEntry> currentYearReportEntryMap = currentYearData.getDataEntryMap();
        Map<Integer, DataEntry> lastYearReportEntryMap = lastYearData.getDataEntryMap();

        List<DataEntry> currentYearDataEntryList = new ArrayList<DataEntry>();
        List<DataEntry> lastYearDataEntryList = new ArrayList<DataEntry>();

        // list keeps the original sorting
        currentYearDataEntryList.addAll(currentYearReportEntryMap.values());
        lastYearDataEntryList.addAll(lastYearReportEntryMap.values());

        // current year data may have data for less than 12 months
        for (int month = 0; month < currentYearDataEntryList.size(); month++) {

            // last month report entry is going to be calculated
            DataEntry dataEntry = currentYearDataEntryList.get(month);

            // the report entry before the above report entry
            DataEntry previousEntry = null;

            // when month is 0, it means the first month of current year, so the previous month should be the last element of the last year data list
            if (month == 0) {
                previousEntry = lastYearDataEntryList.get(11);
            } else { // after the first month, the previous month should be from the current year
                previousEntry = currentYearDataEntryList.get(month - 1);
            }

            // calculate rates based on Month-to-Month
            // calculate changes using a specified month data against previous month data
            float averagePricePc = ((float) dataEntry.getAveragePrice() - (float) previousEntry.getAveragePrice()) / (float) previousEntry.getAveragePrice();
            averagePricePc = (float) Math.round(averagePricePc * 10000) / 100;
            dataEntry.setAveragePricePc(averagePricePc);

            float medianPricePc = ((float) dataEntry.getMedianPrice() - (float) previousEntry.getMedianPrice()) / (float) previousEntry.getMedianPrice();
            medianPricePc = Math.round(medianPricePc * 10000f) / 100;
            dataEntry.setMedianPricePc(medianPricePc);

            float salesPc = ((float) dataEntry.getSales() - (float) previousEntry.getSales()) / (float) previousEntry.getSales();
            salesPc = Math.round(salesPc * 10000f) / 100;
            dataEntry.setSalesPc(salesPc);

            float activeListingPc = ((float) dataEntry.getActiveListings() - (float) previousEntry.getActiveListings()) / (float) previousEntry.getActiveListings();
            activeListingPc = Math.round(activeListingPc * 10000f) / 100;
            dataEntry.setActiveListingsPc(activeListingPc);

            float domPc = ((float) dataEntry.getDom() - (float) previousEntry.getDom()) / (float) previousEntry.getDom();
            domPc = Math.round(domPc * 10000f) / 100;
            dataEntry.setDomPc(domPc);

            float newListingsPc = ((float) dataEntry.getNewListings() - (float) previousEntry.getNewListings()) / (float) previousEntry.getNewListings();
            newListingsPc = Math.round(newListingsPc * 10000f) / 100;
            dataEntry.setNewListingsPc(newListingsPc);

            float slRatioPc = ((float) dataEntry.getSplp() - (float) previousEntry.getSplp()) / (float) previousEntry.getSplp();
            slRatioPc = Math.round(slRatioPc * 10000f) / 100;
            dataEntry.setSplpPc(slRatioPc);

            // calculate rates based on Year-to-Year
            // calculate changes using a specified month data against the data of the same month of last year
            DataEntry sameMonthLastYearEntry = lastYearDataEntryList.get(month);

            float averagePriceAc = ((float) dataEntry.getAveragePrice() - (float) sameMonthLastYearEntry.getAveragePrice()) / (float) sameMonthLastYearEntry.getAveragePrice();
            averagePriceAc = (float) Math.round(averagePriceAc * 10000) / 100;
            dataEntry.setAveragePriceAc(averagePriceAc);

            float medianPriceAc = ((float) dataEntry.getMedianPrice() - (float) sameMonthLastYearEntry.getMedianPrice()) / (float) sameMonthLastYearEntry.getMedianPrice();
            medianPriceAc = (float) Math.round(medianPriceAc * 10000) / 100;
            dataEntry.setMedianPriceAc(medianPriceAc);


            float salesAc = ((float) dataEntry.getSales() - (float) sameMonthLastYearEntry.getSales()) / (float) sameMonthLastYearEntry.getSales();
            salesAc = (float) Math.round(salesAc * 10000) / 100;
            dataEntry.setSalesAc(salesAc);

            float activeListingPriceAc = ((float) dataEntry.getActiveListings() - (float) sameMonthLastYearEntry.getActiveListings()) / (float) sameMonthLastYearEntry.getActiveListings();
            activeListingPriceAc = (float) Math.round(activeListingPriceAc * 10000) / 100;
            dataEntry.setActiveListingsAc(activeListingPriceAc);

            float domAc = ((float) dataEntry.getDom() - (float) sameMonthLastYearEntry.getDom()) / (float) sameMonthLastYearEntry.getDom();
            domAc = (float) Math.round(domAc * 10000) / 100;
            dataEntry.setDomAc(domAc);

            float newListingAc = ((float) dataEntry.getNewListings() - (float) sameMonthLastYearEntry.getNewListings()) / (float) sameMonthLastYearEntry.getNewListings();
            newListingAc = (float) Math.round(newListingAc * 10000) / 100;
            dataEntry.setNewListingsAc(newListingAc);

            float splpAc = ((float) dataEntry.getSplp() - (float) sameMonthLastYearEntry.getSplp()) / (float) sameMonthLastYearEntry.getSplp();
            splpAc = (float) Math.round(splpAc * 10000) / 100;
            dataEntry.setSplpAc(splpAc);

        }

    }

}
