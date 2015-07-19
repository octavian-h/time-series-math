package ro.hasna.ts.math.filter;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @since 1.0
 */
public class ExponentialMovingAverageFilterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testConstructor1() throws Exception {
        thrown.expect(OutOfRangeException.class);
        thrown.expectMessage("2 out of (0, 1) range");

        new ExponentialMovingAverageFilter(2);
    }

    @Test
    public void testConstructor2() throws Exception {
        thrown.expect(OutOfRangeException.class);
        thrown.expectMessage("0 out of (0, 1) range");

        new ExponentialMovingAverageFilter(0);
    }

    @Test
    public void testConstructor3() throws Exception {
        thrown.expect(OutOfRangeException.class);
        thrown.expectMessage("1 out of (0, 1) range");

        new ExponentialMovingAverageFilter(1);
    }

    @Test
    public void testFilter() throws Exception {
        double[] v = {1, 1, 2, 2, 3, 3, 4, 4, 5};
        double[] expected = {0.8, 0.96, 1.79, 1.95, 2.79, 2.95, 3.79, 3.95, 4.79};

        Filter filter = new ExponentialMovingAverageFilter(0.8);
        double[] result = filter.filter(v);

        Assert.assertArrayEquals(expected, result, 0.01);
    }
}