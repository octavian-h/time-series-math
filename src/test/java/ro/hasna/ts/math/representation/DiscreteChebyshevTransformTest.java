package ro.hasna.ts.math.representation;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class DiscreteChebyshevTransformTest {
    private DiscreteChebyshevTransform discreteChebyshevTransform;

    @Before
    public void setUp() throws Exception {
        discreteChebyshevTransform = new DiscreteChebyshevTransform();
    }

    @After
    public void tearDown() throws Exception {
        discreteChebyshevTransform = null;
    }

    @Test
    public void testTransform() throws Exception {
        double[] v = {98, 0, 100}; // 99 * x^2 - x
        double[] expected = {49.5, -1, 49.5}; // 49.5 + (-1) * x + 49.5 * (2*x^2-1)
        double[] results = discreteChebyshevTransform.transform(v);

        Assert.assertArrayEquals(expected, results, TimeSeriesPrecision.EPSILON);
    }
}