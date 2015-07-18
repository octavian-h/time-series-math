package ro.hasna.ts.math.filter;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class MovingAverageFilterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testException1() throws Exception {
        thrown.expect(DimensionMismatchException.class);
        thrown.expectMessage("0 != 5");

        new MovingAverageFilter(2, true, new double[]{});
    }

    @Test
    public void testException2() throws Exception {
        thrown.expect(DimensionMismatchException.class);
        thrown.expectMessage("0 != 2");

        new MovingAverageFilter(2, false, new double[]{});
    }

    @Test
    public void testFilterSymmetricWithoutWeights() throws Exception {
        double[] v = {1, 1, 2, 2, 3, 3, 4, 4, 5};
        double[] expected = {1, 1, 1.8, 2.2, 2.8, 3.2, 3.8, 4, 5};

        Filter filter = new MovingAverageFilter(2);
        double[] result = filter.filter(v);

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testFilterSymmetricWithWeights() throws Exception {
        double[] v = {1, 1, 2, 2, 3, 3, 4, 4, 5};
        double[] w = {0.05, 0.1, 0.7, 0.1, 0.05};
        double[] expected = {1, 1, 1.9, 2.1, 2.9, 3.1, 3.9, 4, 5};

        Filter filter = new MovingAverageFilter(2, true, w);
        double[] result = filter.filter(v);

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testFilterNonSymmetricWithoutWeights() throws Exception {
        double[] v = {1, 1, 2, 2, 3, 3, 4, 4, 5};
        double[] expected = {1, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5};

        Filter filter = new MovingAverageFilter(2, false, null);
        double[] result = filter.filter(v);

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testFilterNonSymmetricWithWeights() throws Exception {
        double[] v = {1, 1, 2, 2, 3, 3, 4, 4, 5};
        double[] w = {0.4, 0.6};
        double[] expected = {1, 1, 1.6, 2, 2.6, 3, 3.6, 4, 4.6};

        Filter filter = new MovingAverageFilter(2, false, w);
        double[] result = filter.filter(v);

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }
}