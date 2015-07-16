package ro.hasna.ts.math.distribution;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

import java.util.Map;

/**
 * @since 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class NormalDistributionDividerTest {

    @Mock
    private Map<Integer, double[]> breakpoints;

    @InjectMocks
    private NormalDistributionDivider divider;

    @Test
    public void testGetBreakpointsWithCaching() throws Exception {
        divider.setCachingEnabled(true);
        double[] v = divider.getBreakpoints(1000);

        Mockito.verify(breakpoints).put(1000, v);
    }

    @Test
    public void testGetBreakpointsWithoutCaching() throws Exception {
        divider.setCachingEnabled(false);
        double[] v = divider.getBreakpoints(1000);

        Mockito.verify(breakpoints, Mockito.never()).put(1000, v);
    }

    @Test
    public void testGetBreakpoints1() throws Exception {
        NormalDistributionDivider divider = new NormalDistributionDivider();
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

    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallNumberOfAreas() throws Exception {
        divider.getBreakpoints(1);
    }

    @Test
    public void testIsCachingEnabled1() throws Exception {
        NormalDistributionDivider divider = new NormalDistributionDivider();

        Assert.assertEquals(true, divider.isCachingEnabled());
    }

    @Test
    public void testIsCachingEnabled2() throws Exception {
        NormalDistributionDivider divider = new NormalDistributionDivider(false);

        Assert.assertEquals(false, divider.isCachingEnabled());
    }
}