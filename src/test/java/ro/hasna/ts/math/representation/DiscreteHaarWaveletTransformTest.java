package ro.hasna.ts.math.representation;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class DiscreteHaarWaveletTransformTest {
    private DiscreteHaarWaveletTransform waveletTransform;

    @Before
    public void setUp() throws Exception {
        waveletTransform = new DiscreteHaarWaveletTransform();
    }

    @After
    public void tearDown() throws Exception {
        waveletTransform = null;
    }

    @Test
    public void testTransform() throws Exception {
        double[] v = new double[]{1, 2, 1, 0, -1, -2, -1, 0};
        double[] results = waveletTransform.transform(v);
        double[] expected = new double[]{0, 8, 2, -2, -1, 1, 1, -1};

        Assert.assertArrayEquals(expected, results, TimeSeriesPrecision.EPSILON);
    }
}