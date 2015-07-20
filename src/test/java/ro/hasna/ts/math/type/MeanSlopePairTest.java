package ro.hasna.ts.math.type;

import org.junit.Assert;
import org.junit.Test;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class MeanSlopePairTest {

    @Test
    public void testGetters() throws Exception {
        MeanSlopePair pair = new MeanSlopePair(10.0, 20.0);

        Assert.assertEquals(10.0, pair.getMean(), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(20.0, pair.getSlope(), TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testEquals() throws Exception {
        MeanSlopePair pair = new MeanSlopePair(10.0, 20.0);

        Assert.assertEquals(false, pair.equals(null));
        Assert.assertEquals(true, pair.equals(pair));
        Assert.assertEquals(true, pair.equals(new MeanSlopePair(10.0, 20.0)));
        Assert.assertEquals(false, pair.equals(new MeanSlopePair(10.0, 21.0)));
    }
}