package ro.hasna.ts.math.ml.distance;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.hasna.ts.math.representation.SymbolicAggregateApproximation;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

import java.util.Random;

/**
 * @since 1.0
 */
public class SaxEuclideanDistanceTest {
    private SaxEuclideanDistance distance;

    @Before
    public void setUp() throws Exception {
        distance = new SaxEuclideanDistance(new SymbolicAggregateApproximation(8, 8));
    }

    @After
    public void tearDown() throws Exception {
        distance = null;
    }

    @Test
    public void testTriangleInequality() throws Exception {
        int n = 128;
        double a[] = new double[n];
        double b[] = new double[n];
        double c[] = new double[n];

        for (int i = 0; i < n; i++) {
            a[i] = i;
            b[i] = n - i;
            c[i] = i * i;
        }

        double ab = distance.compute(a, b);
        double ba = distance.compute(b, a);
        double bc = distance.compute(b, c);
        double ac = distance.compute(a, c);

        Assert.assertEquals(ab, ba, TimeSeriesPrecision.EPSILON);
        Assert.assertTrue(ab + bc >= ac);
    }

    @Test
    public void testEquality() throws Exception {
        int n = 128;
        double a[] = new double[n];
        double b[] = new double[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            a[i] = i;
            b[i] = 100 + i + random.nextDouble();
        }

        double result = distance.compute(a, b);

        Assert.assertEquals(0, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testOverflow() throws Exception {
        int a[] = {0, 4, 5, 6};
        int b[] = {7, 2, 5, 1};

        double result = distance.compute(a, b, 128, 3);

        Assert.assertEquals(Double.POSITIVE_INFINITY, result, TimeSeriesPrecision.EPSILON);
    }
}