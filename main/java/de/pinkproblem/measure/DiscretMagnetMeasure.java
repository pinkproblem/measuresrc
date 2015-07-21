package de.pinkproblem.measure;

/**
 * Created by Iris on 05.07.2015.
 */
public class DiscretMagnetMeasure extends MeasureStrategy {

    MainActivity main;
    static final double sectionSize = Math.PI / 4; //-> 8 sections

    public DiscretMagnetMeasure(MainActivity m) {
        super(m);
        main = m;

        columnNames = new String[]{"Azimuth", "RSSI"};
        strategyTag = "discret_magnet";
    }

    @Override
    public String getLine(int rssi) {
        double rotation = main.rotation;
        int section = (int) Math.floor(rotation / sectionSize);
        rotation = section * sectionSize;

        return rotation + separator + rssi;
    }
}
