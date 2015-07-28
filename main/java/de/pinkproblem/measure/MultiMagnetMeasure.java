package de.pinkproblem.measure;

import de.pinkproblem.measure.backend.DiscreteEvaluation;
import de.pinkproblem.measure.backend.DiscreteEvaluationWithMin;
import de.pinkproblem.measure.backend.MovingAverageEvaluation;

/**
 * Created by Iris on 15.07.2015.
 */
public class MultiMagnetMeasure extends MeasureStrategy {

    static final String mac1 = "00:07:80:78:F5:88";
    static final String mac2 = "00:07:80:78:FA:5A";
    static final String mac3 = "00:07:80:1B:5C:7C";

    MainActivity main;

    DiscreteEvaluation discEv1;
    DiscreteEvaluation discEv2;
    DiscreteEvaluation discEv3;

    DiscreteEvaluationWithMin discMinEv1;
    DiscreteEvaluationWithMin discMinEv2;
    DiscreteEvaluationWithMin discMinEv3;

    MovingAverageEvaluation avgEv1;
    MovingAverageEvaluation avgEv2;
    MovingAverageEvaluation avgEv3;

    MovingAverageEvaluation avgEv051;
    MovingAverageEvaluation avgEv052;
    MovingAverageEvaluation avgEv053;

    public MultiMagnetMeasure(MainActivity m) {
        super(m);
        main = m;

        discEv1 = new DiscreteEvaluation();
        discEv2 = new DiscreteEvaluation();
        discEv3 = new DiscreteEvaluation();

        discMinEv1 = new DiscreteEvaluationWithMin();
        discMinEv2 = new DiscreteEvaluationWithMin();
        discMinEv3 = new DiscreteEvaluationWithMin();

        avgEv1 = new MovingAverageEvaluation();
        avgEv2 = new MovingAverageEvaluation();
        avgEv3 = new MovingAverageEvaluation();

        avgEv051 = new MovingAverageEvaluation(0.5);
        avgEv052 = new MovingAverageEvaluation(0.5);
        avgEv053 = new MovingAverageEvaluation(0.5);

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
            discEv1.addSample(rotation, rssi, 0);
            discMinEv1.addSample(rotation, rssi, 0);
            avgEv1.addSample(rotation, rssi, 0);
            avgEv051.addSample(rotation, rssi, 0);
            return rotation + separator + rssi;
        } else if (address.equals(mac2)) {
            discEv2.addSample(rotation, rssi, 0);
            discMinEv2.addSample(rotation, rssi, 0);
            avgEv2.addSample(rotation, rssi, 0);
            avgEv052.addSample(rotation, rssi, 0);
            return rotation + separator + separator + rssi;
        } else if (address.equals(mac3)) {
            discEv3.addSample(rotation, rssi, 0);
            discMinEv3.addSample(rotation, rssi, 0);
            avgEv3.addSample(rotation, rssi, 0);
            avgEv053.addSample(rotation, rssi, 0);
            return rotation + separator + separator + separator + rssi;
        }
        throw new IllegalStateException("None of the expected mac addresses");
    }

    @Override
    public String getLines() {
        String res = "\n" + "DiscreteEv" + separator + discEv1.calculate() + separator + discEv2
                .calculate()
                + separator + discEv3.calculate();
        res += "\n" + "DiscreteMinEv" + separator + discMinEv1.calculate() + separator + discMinEv2
                .calculate() + separator + discMinEv3.calculate();
        res += "\n" + "MovAvgEv0.3" + separator + avgEv1.calculate() + separator + avgEv2.calculate()
                + separator + avgEv3.calculate();
        res += "\n" + "MovAvgEv0.5" + separator + avgEv051.calculate()
                + separator + avgEv052.calculate() + separator + avgEv053.calculate();
        return res;
    }
}
