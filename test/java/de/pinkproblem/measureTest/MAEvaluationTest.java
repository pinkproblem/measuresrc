package de.pinkproblem.measureTest;

import org.junit.Test;

import de.pinkproblem.measure.backend.MovingAverageEvaluation;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Iris on 22.07.2015.
 * <p/>
 * All test cases assume a rangeFactor of 0.3.
 */
public class MAEvaluationTest {

    @Test
    public void movingAverageTest1() {
        MovingAverageEvaluation ev = new MovingAverageEvaluation();

        ev.addSample(1.0, -60, 0);

        assertEquals(ev.calculate(), 1.0);
    }

    @Test
    public void movingAverageTest2() {
        MovingAverageEvaluation ev = new MovingAverageEvaluation();

        ev.addSample(0.6, -60, 0);
        ev.addSample(0.7, -60, 0);
        ev.addSample(0.8, -60, 0);
        ev.addSample(0.9, -50, 0);
        ev.addSample(1.0, -50, 0);
        ev.addSample(1.1, -50, 0);
        ev.addSample(1.2, -60, 0);
        ev.addSample(1.3, -60, 0);
        ev.addSample(1.4, -60, 0);
        ev.addSample(1.5, -60, 0);

        assertEquals(ev.calculate(), 1.0);
    }

    @Test
    public void movingAverageTest3() {
        MovingAverageEvaluation ev = new MovingAverageEvaluation();

        ev.addSample(-3.0, -80, 0);
        ev.addSample(-2.5, -100, 0);
        ev.addSample(-2.0, -90, 0);
        ev.addSample(-1.5, -80, 0);
        ev.addSample(-1.1, -60, 0);
        ev.addSample(-1.0, -70, 0);
        ev.addSample(-0.9, -60, 0);
        ev.addSample(0.0, -80, 0);
        ev.addSample(0.5, -80, 0);
        ev.addSample(1.0, -90, 0);
        ev.addSample(1.5, -80, 0);
        ev.addSample(2.0, -100, 0);
        ev.addSample(3.0, -100, 0);

        assertEquals(ev.calculate(), -1.0);
    }
}
