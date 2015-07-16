package ro.hasna.ts.math.distribution;

import org.junit.Assert;
import org.junit.Test;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class UniformDistributionDividerTest {

    @Test
    public void testGetBreakpoints1() throws Exception {
        UniformDistributionDivider divider = new UniformDistributionDivider(-1, 1);
        double[] expected = {-1.0 / 3, 1.0 / 3};
        double[] v = divider.getBreakpoints(3);

        Assert.assertArrayEquals(expected, v, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testGetBreakpoints2() throws Exception {
        UniformDistributionDivider divider = new UniformDistributionDivider(-1, 1);
        double[] expected = {-1.0 / 2, 0, 1.0 / 2};
        double[] v = divider.getBreakpoints(4);

        Assert.assertArrayEquals(expected, v, TimeSeriesPrecision.EPSILON);
    }
}