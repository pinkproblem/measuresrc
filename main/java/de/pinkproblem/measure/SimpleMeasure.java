package de.pinkproblem.measure;

import android.content.Context;

/**
 * Created by Iris on 23.06.2015.
 */
public class SimpleMeasure extends MeasureStrategy {

    public SimpleMeasure(Context c){
        super(c);
        columnNames = new String[]{"RSSI"};
        strategyTag = "rssi_only";
    }

    @Override
    public String getLine(int rssi) {
        return ""+rssi;
    }
}
