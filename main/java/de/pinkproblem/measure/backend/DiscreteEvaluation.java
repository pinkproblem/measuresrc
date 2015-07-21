package de.pinkproblem.measure.backend;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Iris on 20.07.2015.
 */
public class DiscreteEvaluation implements EvaluationStrategy {

    static final double sectionSize = Math.PI / 4; //-> 8 sections
    static final int numberOfSections = (int) Math.floor(Math.PI * 2 / sectionSize);

    ArrayList<Sample>[] samples;

    public DiscreteEvaluation() {
        samples = new ArrayList[numberOfSections];
        for (int i = 0; i < numberOfSections; i++) {
            samples[i] = new ArrayList<>();
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

        return maxAzimuth;
    }
}
