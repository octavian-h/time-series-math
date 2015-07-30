package ro.hasna.ts.math.representation;

import org.junit.Assert;
import org.junit.Test;
import ro.hasna.ts.math.type.MeanLastPair;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class AdaptivePiecewiseConstantApproximationTest {

    @Test
    public void testTransform() throws Exception {
        AdaptivePiecewiseConstantApproximation apca = new AdaptivePiecewiseConstantApproximation(3);
        double[] v = {1, 1, 4, 5, 1, 0, 1, 2, 1};

        MeanLastPair[] result = apca.transform(v);

        Assert.assertEquals(1, result[0].getMean(), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(2, result[0].getLast());
        Assert.assertEquals(4.5, result[1].getMean(), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(4, result[1].getLast());
        Assert.assertEquals(1, result[2].getMean(), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(8, result[2].getLast());
    }
}