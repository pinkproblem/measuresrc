package de.pinkproblem.measure;

import java.util.ArrayList;

/**
 * Created by Iris on 09.07.2015.
 */
public class ExpMeasure extends MeasureStrategy {

    MainActivity main;
    ArrayList<Integer> measurements;

    public ExpMeasure(MainActivity m) {
        super(m);
        main = m;
        measurements = new ArrayList<>();

        columnNames = new String[]{"Azimuth", "RSSI", "Smooth"};
        strategyTag = "exp_smoothed";
    }

    @Override
    public String getHeaderLine() {
        measurements = new ArrayList<>();
        return super.getHeaderLine();
    }

    @Override
    public String getLine(int rssi) {
        double smoothValue = getSmoothValue(rssi);

        return main.rotation + separator + rssi + separator + smoothValue;
    }

    private double getSmoothValue(int rssi) {
        double weightSum = 0;
        double rssiSum = 0;
        measurements.add(rssi);

        for (int i = 0; i < measurements.size(); i++) {
            weightSum += weight(measurements.size(), i);
            rssiSum += measurements.get(i) * weight(measurements.size(), i);
        }

        return rssiSum / weightSum;
    }

    private double weight(int now, int sampleTime) {
        return Math.pow(0.5, (now - sampleTime));
    }
}
