package ro.hasna.ts.math.type;

import org.junit.Assert;
import org.junit.Test;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class MeanLastPairTest {

    @Test
    public void testGetters() throws Exception {
        MeanLastPair pair = new MeanLastPair(10.0, 20);

        Assert.assertEquals(10.0, pair.getMean(), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(20, pair.getLast());
    }

    @Test
    public void testEquals() throws Exception {
        MeanLastPair pair = new MeanLastPair(10.0, 20);

        Assert.assertEquals(false, pair.equals(null));
        Assert.assertEquals(true, pair.equals(pair));
        Assert.assertEquals(true, pair.equals(new MeanLastPair(10.0, 20)));
        Assert.assertEquals(false, pair.equals(new MeanLastPair(10.0, 21)));
    }
}