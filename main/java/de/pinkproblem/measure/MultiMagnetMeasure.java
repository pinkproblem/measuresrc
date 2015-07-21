package de.pinkproblem.measure;

/**
 * Created by Iris on 15.07.2015.
 */
public class MultiMagnetMeasure extends MeasureStrategy {

    MainActivity main;

    static final String mac1 = "00:07:80:78:F5:88";
    static final String mac2 = "00:07:80:78:FA:5A";
    static final String mac3 = "00:07:80:1B:5C:7C";

    public MultiMagnetMeasure(MainActivity m) {
        super(m);
        main = m;

        columnNames = new String[]{"Azimuth", mac1, mac2, mac3};
        strategyTag = "multi_magnet";
    }

    @Override
    public String getLine(int rssi) {
        return "";
    }

    @Override
    public String getLine(String address, int rssi) {
        double rotation = main.rotation;
        if (address.equals(mac1)) {
            return rotation + separator + rssi;
        } else if (address.equals(mac2)) {
            return rotation + separator + separator + rssi;
        } else if (address.equals(mac3)) {
            return rotation + separator + separator + separator + rssi;
        }
        throw new IllegalStateException("None of the expected mac addresses");
    }
}
