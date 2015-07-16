package ro.hasna.ts.math.representation;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.exception.ArrayLengthIsNotDivisibleException;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.representation.util.PaaStrategy;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class PiecewiseAggregateApproximationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testTransformMoreSegmentsThanValues() throws Exception {
        thrown.expect(ArrayLengthIsTooSmallException.class);
        thrown.expectMessage("3 is smaller than the minimum (4)");

        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(4, PaaStrategy.FRACTIONAL_PARTITION);
        double[] v = {1, 2, 3};

        paa.transformToDoubleArray(v);
    }

    @Test
    public void testTransformSegmentsNotDivisible() throws Exception {
        thrown.expect(ArrayLengthIsNotDivisibleException.class);
        thrown.expectMessage("5 is not divisible with 4");

        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(4);
        double[] v = {1, 2, 3, 4, 5};

        paa.transformToDoubleArray(v);
    }

    @Test
    public void testTransformStrict() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(2);
        double[] v = {1, 2, 3, 4, 5, 6};
        double[] expected = {2.0, 5.0};

        double[] result = paa.transformToDoubleArray(v);

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformIgnoreRemaining() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(2, PaaStrategy.IGNORE_REMAINING);
        double[] v = {1, 2, 3, 4, 5, 6, 7};
        double[] expected = {2.0, 5.0};

        double[] result = paa.transformToDoubleArray(v);

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformMeanPadding() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(2, PaaStrategy.MEAN_PADDING);
        double[] v = {1, 2, 3, 4, 5, 6, 7};
        double[] expected = {2.5, 6.0};

        double[] result = paa.transformToDoubleArray(v);

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformFractionalPartition1() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(2, PaaStrategy.FRACTIONAL_PARTITION);
        double[] v = {1, 2, 3, 4, 5, 6, 7};
        double[] expected = {8 / 3.5, 20 / 3.5};

        double[] result = paa.transformToDoubleArray(v);

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformFractionalPartition2() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(5, PaaStrategy.FRACTIONAL_PARTITION);
        double[] v = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        double[] expected = {24.0 / 13, 57.0 / 13, 91.0 / 13, 125.0 / 13, 158.0 / 13};

        double[] result = paa.transformToDoubleArray(v);

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformFractionalPartition3() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(13, PaaStrategy.FRACTIONAL_PARTITION);
        double[] v = new double[523];
        for (int i = 0; i < 523; i++) {
            v[i] = 1.0;
        }
        double[] expected = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        double[] result = paa.transformToDoubleArray(v);

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testGetters1() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(3);

        Assert.assertEquals(3, paa.getSegments());
        Assert.assertEquals(PaaStrategy.STRICT, paa.getStrategy());
    }

    @Test
    public void testGetters2() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(4, PaaStrategy.MEAN_PADDING);

        Assert.assertEquals(4, paa.getSegments());
        Assert.assertEquals(PaaStrategy.MEAN_PADDING, paa.getStrategy());
    }

    @Test
    public void testToString1() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(5);

        Assert.assertEquals("PAA(5,STRICT)", paa.toString());
    }

    @Test
    public void testToString2() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(6, PaaStrategy.IGNORE_REMAINING);

        Assert.assertEquals("PAA(6,IGNORE_REMAINING)", paa.toString());
    }
}