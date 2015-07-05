package de.pinkproblem.measure;

import android.content.Context;
import android.hardware.SensorManager;

/**
 * Created by Iris on 26.06.2015.
 */
public class MagnetMeasure extends MeasureStrategy {

    MainActivity ma;

    public MagnetMeasure(Context c, MainActivity m) {
        super(c);

        ma = m;

        columnNames = new String[]{"RSSI", "Azimuth"};
        strategyTag = "rssi_azimuth";
    }

    @Override
    public String getLine(int rssi) {
        return rssi + "," + ma.rotation;
    }
}
