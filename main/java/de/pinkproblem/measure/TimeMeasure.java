package de.pinkproblem.measure;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Iris on 29.06.2015.
 */
public class TimeMeasure extends MeasureStrategy {

    long startMillis;

    public TimeMeasure(Context c) {
        super(c);

        columnNames = new String[]{"Time", "RSSI"};
        strategyTag = "time_rssi";

        startMillis = SystemClock.uptimeMillis();
    }

    @Override
    public String getLine(int rssi) {
        double time = (SystemClock.uptimeMillis() - startMillis) / 1000.0;
        return time + "," + rssi;
    }
}
