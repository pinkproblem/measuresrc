package de.pinkproblem.measure;

import android.content.Context;

/**
 * Created by Iris on 26.06.2015.
 */
public class MagnetMeasure extends MeasureStrategy {

    public MagnetMeasure(Context c){
        super(c);

        columnNames = new String[]{"RSSI", "Azimuth"};
        strategyTag = "rssi_azimuth";
    }

    @Override
    public String getLine(int rssi) {
        return rssi+";";
    }
}
