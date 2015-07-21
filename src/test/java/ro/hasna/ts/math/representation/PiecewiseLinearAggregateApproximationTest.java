package ro.hasna.ts.math.representation;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.exception.ArrayLengthIsNotDivisibleException;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.exception.UnsupportedStrategyException;
import ro.hasna.ts.math.representation.util.SegmentationStrategy;
import ro.hasna.ts.math.type.MeanSlopePair;

/**
 * @since 1.0
 */
public class PiecewiseLinearAggregateApproximationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testTransformMoreSegmentsThanValues() throws Exception {
        thrown.expect(ArrayLengthIsTooSmallException.class);
        thrown.expectMessage("3 is smaller than the minimum (4)");

        PiecewiseLinearAggregateApproximation plaa = new PiecewiseLinearAggregateApproximation(4, SegmentationStrategy.IGNORE_REMAINING);
        double[] v = {1, 2, 3};

        plaa.transformToMeanSlopePairArray(v);
    }

    @Test
    public void testTransformSegmentsNotDivisible() throws Exception {
        thrown.expect(ArrayLengthIsNotDivisibleException.class);
        thrown.expectMessage("5 is not divisible with 4");

        PiecewiseLinearAggregateApproximation plaa = new PiecewiseLinearAggregateApproximation(4);
        double[] v = {1, 2, 3, 4, 5};

        plaa.transformToMeanSlopePairArray(v);
    }

    @Test
    public void testTransformUnsupportedStrategy() throws Exception {
        thrown.expect(UnsupportedStrategyException.class);
        thrown.expectMessage("the strategy FRACTIONAL_PARTITION is not supported in PLAA");

        new PiecewiseLinearAggregateApproximation(4, SegmentationStrategy.FRACTIONAL_PARTITION);
    }

    @Test
    public void testTransformStrict() throws Exception {
        PiecewiseLinearAggregateApproximation plaa = new PiecewiseLinearAggregateApproximation(2);
        double[] v = {1, 2, 3, 3, 2, 1};
        MeanSlopePair[] expected = {new MeanSlopePair(2.0, 1.0), new MeanSlopePair(2.0, -1.0)};

        MeanSlopePair[] result = plaa.transformToMeanSlopePairArray(v);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testTransformIgnoreRemaining() throws Exception {
        PiecewiseLinearAggregateApproximation plaa = new PiecewiseLinearAggregateApproximation(2, SegmentationStrategy.IGNORE_REMAINING);
        double[] v = {1, 2, 3, 3, 2, 1, 0};
        MeanSlopePair[] expected = {new MeanSlopePair(2.0, 1.0), new MeanSlopePair(2.0, -1.0)};

        MeanSlopePair[] result = plaa.transformToMeanSlopePairArray(v);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testGetters1() throws Exception {
        PiecewiseLinearAggregateApproximation plaa = new PiecewiseLinearAggregateApproximation(3);

        Assert.assertEquals(3, plaa.getSegments());
        Assert.assertEquals(SegmentationStrategy.STRICT, plaa.getStrategy());
    }

    @Test
    public void testGetters2() throws Exception {
        PiecewiseLinearAggregateApproximation plaa = new PiecewiseLinearAggregateApproximation(4, SegmentationStrategy.IGNORE_REMAINING);

        Assert.assertEquals(4, plaa.getSegments());
        Assert.assertEquals(SegmentationStrategy.IGNORE_REMAINING, plaa.getStrategy());
    }
}