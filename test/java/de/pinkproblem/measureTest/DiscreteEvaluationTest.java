package de.pinkproblem.measureTest;

import org.junit.Test;

import de.pinkproblem.measure.backend.DiscreteEvaluation;
import de.pinkproblem.measure.backend.EvaluationStrategy;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Iris on 21.07.2015.
 */
public class DiscreteEvaluationTest {

    @org.junit.Test
    public void discreteEvaluationTest() {
        EvaluationStrategy ev = new DiscreteEvaluation();

        ev.addSample(-3.0, -60, 0);
        ev.addSample(-2.0, -80, 0);
        ev.addSample(-1.0, -80, 0);
        ev.addSample(-0.2, -80, 0);
        ev.addSample(3.0, -80, 0);
        ev.addSample(2.0, -80, 0);
        ev.addSample(1.0, -80, 0);
        ev.addSample(0.2, -80, 0);

        double res = ev.calculate();
        assertEquals(res, -2.748, 0.01);
    }

    //tests missing values for sections
    @Test
    public void discreteEvaluationTest2() {
        EvaluationStrategy ev = new DiscreteEvaluation();

        ev.addSample(-3.0, -60, 0);
        ev.addSample(3.0, -80, 0);
        ev.addSample(2.0, -80, 0);
        ev.addSample(1.0, -80, 0);
        ev.addSample(0.2, -80, 0);

        double res = ev.calculate();
        assertEquals(res, -2.748, 0.01);
    }

    //tests adding samples
    @org.junit.Test
    public void discreteEvaluationTest3() {
        EvaluationStrategy ev = new DiscreteEvaluation();

        ev.addSample(-3.0, -60, 0);
        ev.addSample(-2.0, -80, 0);
        ev.addSample(-1.0, -80, 0);
        ev.addSample(-0.2, -80, 0);
        ev.addSample(3.0, -80, 0);
        ev.addSample(2.0, -80, 0);
        ev.addSample(1.0, -80, 0);
        ev.addSample(0.2, -80, 0);

        double res = ev.calculate();
        assertEquals(res, -2.748, 0.01);

        ev.addSample(-2.0, -20, 0);
        res = ev.calculate();
        assertEquals(res, -1.963, 0.01);
    }
}
