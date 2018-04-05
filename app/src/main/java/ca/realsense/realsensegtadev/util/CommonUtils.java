package ca.realsense.realsensegtadev.util;

import android.graphics.Color;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.realsense.realsensegtadev.R;

/**
 * Created by joewang on 2017-08-31.
 */

public class CommonUtils {

    public static final int yearsToReport = 2;

    public static final String WATCH_LIST_PREFS_NAME = "watch_list_prefs_name";
    public static final String WATCH_LIST = "watch_list";

    private static List<String> reportKeys = new ArrayList<String>();

    public static final String TOP_REGION = "region";
    public static final String INTENT_EXTRA_KEY_REGION_KEY = "regionKey";
    public static final String INTENT_EXTRA_KEY_REGION_NAME = "regionName";

    public static final String INTENT_EXTRA_KEY_ACTION = "action";
    public static final String INTENT_EXTRA_VALUE_SINGLE = "single";
    public static final String INTENT_EXTRA_VALUE_COMPARISON = "comparison";

    public static final String INTENT_EXTRA_KEY_SINGLE_REPORT_KEY = "reportKey";
    public static final String INTENT_EXTRA_KEY_REPORT_KEYS = "reportKeys";

    public static final int TIME_WINDOW_THREE_MONTH = 3;
    public static final int TIME_WINDOW_SIX_MONTH = 6;
    public static final int TIME_WINDOW_ONE_YEAR = 12;
    public static final int TIME_WINDOW_MAX = 0;

    public static final int DATA_TYPE_AVERAGE_PRICE = 1;
    public static final int DATA_TYPE_MEDIAN_PRICE = 2;
    public static final int DATA_TYPE_SALES = 3;
    public static final int DATA_TYPE_NEW_LISTINGS = 4;
    public static final int DATA_TYPE_ACTIVE_LISTINGS = 5;
    public static final int DATA_TYPE_DOM = 6;
    public static final int DATA_TYPE_SPLP = 7;


    public static final int CC1 = Color.rgb(169, 50, 38);
    public static final int CC2 = Color.rgb(36, 113, 163);
    public static final int CC3 = Color.rgb(214, 137, 16);
    public static final int CC4 = Color.rgb(34, 153, 84);
    public static final int CC5 = Color.rgb(125, 60, 152);

    public static final int[] COMP_COLORS = {CC1,CC2,CC3,CC4,CC5};

    public static final long LINE_CHART_LINE_WIDTH = 4L;
    public static final long LINE_CHART_CIRCLE_SIZE = 4L;
    public static final Map propertyTypeIconMap = new HashMap();
    private static final String[] MONTH_NAMES = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    private static Map<String, String> regionKeyNameMap = new HashMap<String, String>();
    private static Map<String, String> propertyTypeKeyNameMap = new HashMap<String, String>();

    static {
        propertyTypeIconMap.put("detached", Integer.valueOf(R.drawable.realsenselogo));
        propertyTypeIconMap.put("semi", Integer.valueOf(R.drawable.realsenselogo));
        propertyTypeIconMap.put("freehold_town", Integer.valueOf(R.drawable.realsenselogo));
        propertyTypeIconMap.put("condo_town", Integer.valueOf(R.drawable.realsenselogo));
        propertyTypeIconMap.put("condo", Integer.valueOf(R.drawable.realsenselogo));
        propertyTypeIconMap.put("link", Integer.valueOf(R.drawable.realsenselogo));

        regionKeyNameMap.put("markham", "Markham");
        propertyTypeKeyNameMap.put("detached", "Detached");



    }

    public static String getMonthYear(int month, int year){
        return MONTH_NAMES[month-1] + " " + year;
    }

    public static String getMonthYear(String timeKey){
        String year = timeKey.substring(0,4);
        String month = timeKey.substring(4,6);
        int monthInt = Integer.parseInt(month);

        return MONTH_NAMES[monthInt-1] + "/" + year;
    }


    public static String getTimeKeyByLongValue(Long yearLong, Long monthLong) {
        StringBuffer sb = new StringBuffer();
        sb.append(yearLong.toString());
        if (monthLong.intValue() < 10) {
            sb.append("0");
        }
        sb.append(monthLong.toString());

        String timeKey = sb.toString();

        return timeKey;
    }


    public static String getReportKey(String regionKey, String propertyTypeKey) {
        StringBuffer sb = new StringBuffer();
        sb.append(regionKey).append("_").append(propertyTypeKey);
        return sb.toString();
    }

    public static void addRegionKeyName(String regionKey, String regionName) {
        if (!regionKeyNameMap.containsKey(regionKey)) {
            regionKeyNameMap.put(regionKey, regionName);
        }
    }

    public static String getRegionName(String regionKey) {
        return regionKeyNameMap.get(regionKey);
    }

    public static void addPropertyTypeKeyName(String propertyTypeKey, String propertyTypeName) {
        if (!propertyTypeKeyNameMap.containsKey(propertyTypeKey)) {
            propertyTypeKeyNameMap.put(propertyTypeKey, propertyTypeName);
        }
    }

    public static String getPropertyTypeName(String propertyTypeKey) {
        return propertyTypeKeyNameMap.get(propertyTypeKey);
    }

    public static int getPropertyTypeIcon(String propertyTypeKey) {
        Integer value = (Integer) propertyTypeIconMap.get(propertyTypeKey);
        return value.intValue();
    }

    public static int getDefaultIcon(){
        Integer value = (Integer) propertyTypeIconMap.get("detached");
        return value.intValue();
    }

    public static String getRegionNamePropertyName(String regionName, String propertyTypeName) {
        return propertyTypeName + " at " + regionName;
    }

    public static String getRegionNameFromReportKey(String reportKey) {
        String[] ss = reportKey.split("_");
        int size = ss.length;
        String propertyTypeKey = ss[size - 1];
        int index = reportKey.length() - propertyTypeKey.length() - 1;
        String regionKey = reportKey.substring(0, index);
        String regionName = getRegionName(regionKey);
        return regionName;
    }

    public static String formatPrice(int price) {
        DecimalFormat df = new DecimalFormat("$###,###.##");
        return df.format((long) price);
    }

    public static List<String> getReportKeys(){
        return reportKeys;
    }

    public static void addReportKey(String aReportKey){
        if(!reportKeys.contains(aReportKey)){
            reportKeys.add(aReportKey);
        }
    }

    public static void removeComparisonKey(int position){

        if(position > -1){
            reportKeys.remove(position);
        }
    }




    public static List<String> getReportEntryKeyList(String areaPropertyType, String year){
        List<String> reportEntryKeyList = new ArrayList<String>();

        for (int i = 0; i < 12; i++) {
            StringBuffer sb = new StringBuffer();
            sb.append(areaPropertyType).append("_").append(year);

            if (i < 9) {
                sb.append("0" + (i + 1));
            } else {
                sb.append("" + (i + 1));
            }

            reportEntryKeyList.add(sb.toString());
        }
        return reportEntryKeyList;
    }

}
