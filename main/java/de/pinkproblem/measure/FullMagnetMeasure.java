package de.pinkproblem.measure;

import android.os.SystemClock;

/**
 * Created by Iris on 30.06.2015.
 */
public class FullMagnetMeasure extends MeasureStrategy {

    MainActivity ma;
    long startMillis;

    public FullMagnetMeasure(MainActivity m) {
        super(m);
        ma = m;

        columnNames = new String[]{"Time", "Azimuth", "RSSI"};
        strategyTag = "magnet_full";

        startMillis = SystemClock.uptimeMillis();
    }

    @Override
    public String getLine(int rssi) {
        double time = (SystemClock.uptimeMillis() - startMillis) / 1000.0;
        return time + separator + ma.rotation + separator + rssi;
    }
}
