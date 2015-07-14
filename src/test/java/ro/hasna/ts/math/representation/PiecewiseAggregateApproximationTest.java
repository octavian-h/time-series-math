package ro.hasna.ts.math.representation;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.exception.NotAFactorNumberException;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Octavian
 * @since 14.07.2015
 */
public class PiecewiseAggregateApproximationTest {

    public final static double BIGGER_EPSILON = FastMath.pow(2, -10);
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testTransformMoreSegmentsThanValues() throws Exception {
        thrown.expect(NumberIsTooLargeException.class);
        thrown.expectMessage("4 is larger than the maximum (3)");

        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(PaaStrategy.FRACTIONAL_PARTITION);
        List<Double> list = Arrays.asList(1d, 2d, 3d);

        paa.transform(list, 4);
    }

    @Test
    public void testTransformSegmentsNotDivisible() throws Exception {
        thrown.expect(NotAFactorNumberException.class);
        thrown.expectMessage("3 is not a factor for 4");

        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(PaaStrategy.STRICT);
        List<Double> list = Arrays.asList(1.0, 2.0, 3.0, 4.0);

        paa.transform(list, 3);
    }

    @Test
    public void testTransformStrict() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(PaaStrategy.STRICT);
        List<Double> list = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);

        List<Double> result = paa.transform(list, 2);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(2.0, result.get(0), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(5.0, result.get(1), TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformIgnoreRemaining() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(PaaStrategy.IGNORE_REMAINING);
        List<Double> list = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);

        List<Double> result = paa.transform(list, 2);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(2.0, result.get(0), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(5.0, result.get(1), TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformMeanPadding() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(PaaStrategy.MEAN_PADDING);
        List<Double> list = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);

        List<Double> result = paa.transform(list, 2);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(2.5, result.get(0), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(6.0, result.get(1), TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformFractionalPartition() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(PaaStrategy.FRACTIONAL_PARTITION);
        List<Double> list = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);

        List<Double> result = paa.transform(list, 2);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(8 / 3.5, result.get(0), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(20 / 3.5, result.get(1), TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformFractionalPartition2() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(PaaStrategy.FRACTIONAL_PARTITION);
        List<Double> list = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0);

        List<Double> result = paa.transform(list, 5);
        Assert.assertEquals(5, result.size());
        Assert.assertEquals(24.0 / 13, result.get(0), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(57.0 / 13, result.get(1), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(91.0 / 13, result.get(2), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(125.0 / 13, result.get(3), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(158.0 / 13, result.get(4), TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformFractionalPartition3() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(PaaStrategy.FRACTIONAL_PARTITION);
        List<Double> list = new ArrayList<>(470);
        for (int i = 0; i < 470; i++) {
            list.add(1.0);
        }

        List<Double> result = paa.transform(list, 20);
        Assert.assertEquals(20, result.size());
        for (Double value : result) {
            Assert.assertEquals(1.0, value, TimeSeriesPrecision.EPSILON);
        }
    }

    @Test
    public void testTransformFractionalPartition4() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(PaaStrategy.FRACTIONAL_PARTITION);
        List<Double> list = new ArrayList<>(96);
        for (int i = 0; i < 96; i++) {
            list.add(1.0);
        }

        List<Double> result = paa.transform(list, 20);
        Assert.assertEquals(20, result.size());
        for (Double value : result) {
            Assert.assertEquals(1.0, value, TimeSeriesPrecision.EPSILON);
        }
    }
}