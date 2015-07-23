package de.pinkproblem.measureTest;

import org.junit.Test;

import de.pinkproblem.measure.backend.DiscreteEvaluationWithMin;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Iris on 23.07.2015.
 */
public class DiscreteWithMinTest {

    @Test
    public void minEvTest1() {
        DiscreteEvaluationWithMin ev = new DiscreteEvaluationWithMin();

        ev.addSample(-3.0, -90, 0);
        ev.addSample(1.0, -60, 0);

        assertEquals(ev.calculate(), 0.785, 0.01);
    }

    //tests a max around pi/minus pi
    @Test
    public void minEvTest2() {
        DiscreteEvaluationWithMin ev = new DiscreteEvaluationWithMin();

        ev.addSample(3.0, -60, 0);
        ev.addSample(0.3, -90, 0);

        assertTrue(ev.calculate() - Math.PI < 0.01 || ev.calculate() + Math.PI < 0.01);
    }
}
