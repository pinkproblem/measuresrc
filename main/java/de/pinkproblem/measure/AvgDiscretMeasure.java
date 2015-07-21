package de.pinkproblem.measure;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Iris on 12.07.2015.
 */
public class AvgDiscretMeasure extends MeasureStrategy {

    MainActivity main;
    static final double sectionSize = Math.PI / 4; //-> 8 sections
    static final int numberOfSections = 8;

    ArrayList<Integer>[] measurements;

    public AvgDiscretMeasure(MainActivity m) {
        super(m);
        main = m;

        measurements = new ArrayList[numberOfSections];
        for (int i = 0; i < numberOfSections; i++) {
            measurements[i] = new ArrayList<>();
        }

        columnNames = new String[]{"Azimuth", "AvgRSSI", "Median", "TopAvg", "#values"};
        strategyTag = "avg_discret_magnet";
    }

    @Override
    public String getLine(int rssi) {
        double rotation = main.rotation;
        int section = (int) Math.floor(rotation / sectionSize);
        rotation = section * sectionSize;

        measurements[section + numberOfSections / 2].add(rssi);
        return "";
    }

    @Override
    public String getLines() {
        String res = "";

        for (int i = 0; i < numberOfSections; i++) {
            double avg = getAverage(i);
            if (avg == 0) {
                continue;
            }
            int median = getMedian(i);
            double topavg = getTopAverage(i);
            double rotation = (i - numberOfSections / 2) * sectionSize + sectionSize / 2;

            res += rotation + separator + avg + separator + median + separator + topavg + separator + measurements[i].size() + "\n";
        }
        return res;
    }

    @Override
    public double getResult() {
        double max = 0;
        int maxIndex = 0;

        for (int i = 0; i < numberOfSections; i++) {
            double avg = getAverage(i);
            if (avg > max && avg != 0) {
                max = avg;
                maxIndex = i;
            }
        }
        return (maxIndex - numberOfSections / 2) * sectionSize;
    }

    private double getAverage(int i) {
        ArrayList<Integer> sectionList = measurements[i];

        if (sectionList.size() == 0) {
            return 0;
        }
        double sum = 0;
        for (Integer rssi : sectionList) {
            sum += rssi;
        }
        return sum / sectionList.size();
    }

    private int getMedian(int i) {
        ArrayList<Integer> sectionList = measurements[i];

        if (sectionList.size() == 0) {
            return 0;
        }
        Collections.sort(sectionList);

        return sectionList.get(sectionList.size() / 2);
    }

    private double getTopAverage(int i) {
        ArrayList<Integer> sectionList = measurements[i];

        if (sectionList.size() == 0) {
            return 0;
        } else if (sectionList.size() == 1) {
            return sectionList.get(0);
        }
        Collections.sort(sectionList);
        Collections.reverse(sectionList);

        double sum = 0;

        for (int j = 0; j < sectionList.size() / 2; j++) {
            sum += sectionList.get(j);
        }

        return sum / (sectionList.size() / 2);
    }
}
