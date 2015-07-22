package ro.hasna.ts.math.distribution;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class NormalDistributionDividerTest {
    private NormalDistributionDivider divider;

    @Before
    public void setUp() throws Exception {
        divider = new NormalDistributionDivider();
    }

    @After
    public void tearDown() throws Exception {
        divider = null;
    }

    @Test
    public void testConstructor() throws Exception {
        divider = new NormalDistributionDivider(false);

        Assert.assertFalse(divider.isCachingEnabled());
    }

    @Test
    public void testGetBreakpoints1() throws Exception {
        double[] expected = {-0.4307272992954576, 0.4307272992954576};
        double[] v = divider.getBreakpoints(3);

        Assert.assertArrayEquals(expected, v, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testGetBreakpoints2() throws Exception {
        double[] expected = {-0.6744897501960816, 0, 0.6744897501960816};
        double[] v = divider.getBreakpoints(4);

        Assert.assertArrayEquals(expected, v, TimeSeriesPrecision.EPSILON);
    }
}