package de.pinkproblem.measure.backend;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Iris on 23.07.2015.
 */
public class DiscreteEvaluationWithMin implements EvaluationStrategy {

    static final double sectionSize = Math.PI / 4; //-> 8 sections
    static final int numberOfSections = (int) Math.floor(Math.PI * 2 / sectionSize);

    ArrayList<Sample>[] samples;

    public DiscreteEvaluationWithMin() {
        samples = new ArrayList[numberOfSections];
        for (int i = 0; i < numberOfSections; i++) {
            samples[i] = new ArrayList<>();
        }
    }

    private static double getOpposing(double azimuth) {
        if (azimuth < 0) {
            return azimuth + Math.PI;
        } else {
            return azimuth - Math.PI;
        }
    }

    @Override
    public void addSample(Sample s) {
        int section = (int) Math.floor(s.getAzimuth() / sectionSize) + (numberOfSections / 2);
        samples[section].add(s);
    }

    @Override
    public void addSample(double azimuth, int rssi, long time) {
        addSample(new Sample(azimuth, rssi, time));
    }

    @Override
    public double calculate() {
        HashMap<Double, Double> averages = new HashMap<>();

        //put averages for all sections
        for (int i = 0; i < numberOfSections; i++) {
            if (samples[i].size() == 0) {
                continue;
            }

            int sum = 0;
            for (Sample sample : samples[i]) {
                sum += sample.getRssi();
            }

            double avg = sum / samples[i].size();
            double azimuth = (i - numberOfSections / 2) * sectionSize + sectionSize / 2;
            averages.put(azimuth, avg);
        }

        //find azimuth for max. average
        double maxAvg = -Double.MAX_VALUE;
        double maxAzimuth = 0;
        for (double az : averages.keySet()) {
            if (averages.get(az) > maxAvg) {
                maxAvg = averages.get(az);
                maxAzimuth = az;
            }
        }

        //find azimuth for min. average
        double minAvg = Double.MAX_VALUE;
        double minAzimuth = 0;
        for (double az : averages.keySet()) {
            if (averages.get(az) < minAvg) {
                minAvg = averages.get(az);
                minAzimuth = az;
            }
        }

        //average max and min
        double minAzimuthOp = getOpposing(minAzimuth);
        //averaging over the border of pi/minus pi:
        if (maxAzimuth - minAzimuthOp > Math.PI) {
            //project to opposite, average and project back
            double oppositeMax = getOpposing(maxAzimuth);
            double oppositeMin = getOpposing(minAzimuthOp);
            double oppositeAvg = getOpposing((oppositeMax + oppositeMin) / 2);

            return getOpposing(oppositeAvg);
        }
        return (maxAzimuth + minAzimuthOp) / 2;
    }
}
