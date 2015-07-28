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

    @Test
    public void minEvTest3() {
        DiscreteEvaluationWithMin ev = new DiscreteEvaluationWithMin();

        ev.addSample(-2.513, -89, 0);
        ev.addSample(-2.347, -86, 0);
        ev.addSample(-1.192, -93, 0);
        ev.addSample(-0.586, -94, 0);
        ev.addSample(-0.379, -92, 0);
        ev.addSample(-0.158, -91, 0);
        ev.addSample(0.095, -97, 0);
        ev.addSample(0.307, -92, 0);
        ev.addSample(0.575, -88, 0);
        ev.addSample(0.725, -90, 0);
        ev.addSample(1.29, -90, 0);
        ev.addSample(1.62, -92, 0);
        ev.addSample(1.964, -86, 0);
        ev.addSample(2.012, -90, 0);
        ev.addSample(2.813, -86, 0);

        assertEquals(ev.calculate(), 2.356, 0.01);
    }

    @Test
    public void minEvTest4() {
        DiscreteEvaluationWithMin ev = new DiscreteEvaluationWithMin();

        ev.addSample(-2.69340229034423, -97, 0);
        ev.addSample(-0.436552762985229, -87, 0);
        ev.addSample(-0.185954064130783, -87, 0);
        ev.addSample(0.562045872211456, -92, 0);
        ev.addSample(1.43030881881713, -91, 0);
        ev.addSample(1.76841592788696, -96, 0);

        assertEquals(ev.calculate(), 0, 0.01);
    }
}
