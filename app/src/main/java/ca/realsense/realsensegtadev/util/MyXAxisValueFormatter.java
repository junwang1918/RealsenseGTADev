package ca.realsense.realsensegtadev.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import java.util.List;

/**
 * Created by joewang on 2017-08-31.
 */

public class MyXAxisValueFormatter implements IAxisValueFormatter {

    private List<String> mValues;

    public MyXAxisValueFormatter(List<String> values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)


        //if (mValues.size() > (int) value) {

        String str = mValues.get((int) value);

        if(str.length() > 2){
            str = CommonUtils.getMonthYear(str);
        }
        return str;

        //return mValues.get((int) value);



        //} else
        //    return null;

    }



    /**
     * this is only needed if numbers are returned, else return 0
     */
    public int getDecimalDigits() {
        return 0;
    }
}
