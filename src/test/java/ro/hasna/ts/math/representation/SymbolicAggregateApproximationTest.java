package ro.hasna.ts.math.representation;

import org.junit.Assert;
import org.junit.Test;
import ro.hasna.ts.math.normalization.Normalizer;
import ro.hasna.ts.math.normalization.ZNormalizer;
import ro.hasna.ts.math.representation.util.SegmentationStrategy;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class SymbolicAggregateApproximationTest {

    @Test
    public void testTransformToIntArray1() throws Exception {
        double[] list = new double[64];
        for (int i = 0; i < 32; i++) {
            list[i] = i;
        }
        for (int i = 32; i < 64; i++) {
            list[i] = 64 - i;
        }
        double[] breakpoints = {-0.4307272992954576, 0.4307272992954576};
        int[] expected = {0, 0, 1, 2, 2, 2, 1, 0, 0};

        SymbolicAggregateApproximation sax = new SymbolicAggregateApproximation(new PiecewiseAggregateApproximation(9, SegmentationStrategy.FRACTIONAL_PARTITION), new ZNormalizer(), breakpoints);
        int[] result = sax.transform(list);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testTransformToIntArray2() throws Exception {
        double[] list = new double[64];
        for (int i = 0; i < 32; i++) {
            list[i] = i;
        }
        for (int i = 32; i < 64; i++) {
            list[i] = 64 - i;
        }
        double[] breakpoints = {-0.4307272992954576, 0.4307272992954576};
        int[] expected = {0, 0, 1, 2, 2, 2, 1, 0};

        SymbolicAggregateApproximation sax = new SymbolicAggregateApproximation(8, breakpoints);
        int[] result = sax.transform(list);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testNormalisationAndPaaOrder() throws Exception {
        int segments = 16;
        int valuesPerSegment = 1024;
        int len = segments * valuesPerSegment;
        double[] list = new double[len];
        for (int i = 0; i < len; i++) {
            list[i] = i;
        }
        ZNormalizer normalizer = new ZNormalizer();
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(segments, SegmentationStrategy.STRICT);

        double[] first = normalizer.normalize(paa.transform(list));
        double[] second = paa.transform(normalizer.normalize(list));

        Assert.assertArrayEquals(first, second, 0.01);
    }

    @Test
    public void testGetters() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(4, SegmentationStrategy.STRICT);
        Normalizer normalizer = new ZNormalizer();
        double[] breakpoints = {-0.4307272992954576, 0.4307272992954576};

        SymbolicAggregateApproximation sax = new SymbolicAggregateApproximation(paa, normalizer, breakpoints);

        Assert.assertEquals(paa, sax.getPaa());
        Assert.assertEquals(normalizer, sax.getNormalizer());
        Assert.assertArrayEquals(breakpoints, sax.getBreakpoints(), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(3, sax.getAlphabetSize());
    }
}