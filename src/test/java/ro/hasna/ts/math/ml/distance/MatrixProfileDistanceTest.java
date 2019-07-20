package ro.hasna.ts.math.ml.distance;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.util.FastMath;
import org.junit.*;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.ml.distance.util.DistanceTester;
import ro.hasna.ts.math.normalization.ZNormalizer;
import ro.hasna.ts.math.representation.mp.MatrixProfileTransformer;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

import java.util.Locale;
import java.util.Scanner;

/**
 * @author Octavian
 * @since 02.06.2019
 */
public class MatrixProfileDistanceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private MatrixProfileDistance distance;

    @Before
    public void setUp() throws Exception {
        distance = new MatrixProfileDistance(32);
    }

    @After
    public void tearDown() throws Exception {
        distance = null;
    }

    @Test
    public void testTriangleInequality() throws Exception {
        new DistanceTester().withGenericDistanceMeasure(distance)
                .testTriangleInequality();
    }

    @Test
    public void testEquality() throws Exception {
        new DistanceTester().withGenericDistanceMeasure(distance)
                .testEquality();
    }

    @Test
    public void testResult() throws Exception {
        double[] a = {1, 2, 3, 82, 69, 54};
        double[] b = {45, 78, 12, 98, 65, 32, 2, 2, 4};

        MatrixProfileDistance tmp = new MatrixProfileDistance(new MatrixProfileTransformer(3, 0, false), 0.05);
        double result = tmp.compute(a, b);

        Assert.assertEquals(FastMath.sqrt(2), result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testResultSmall() throws Exception {
        double[] a = {200, 100, 50};
        double[] b = {1, 2, 3};

        MatrixProfileDistance tmp = new MatrixProfileDistance(a.length);
        double result = tmp.compute(a, b);

        ZNormalizer normalizer = new ZNormalizer();
        double[] aNormalize = normalizer.normalize(a);
        double[] bNormalize = normalizer.normalize(b);

        EuclideanDistance ed = new EuclideanDistance();
        double expected = ed.compute(aNormalize, bNormalize);

        Assert.assertEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testResultLarge() throws Exception {
        MatrixProfileDistance mpd = new MatrixProfileDistance(64);
        int m = 128;

        Scanner dataScanner = new Scanner(getClass().getResourceAsStream("data-100k.txt")).useLocale(Locale.ENGLISH);
        double[] data = new double[m];
        double[] copy = new double[m];
        for (int i = 0; i < m && dataScanner.hasNextDouble(); i++) {
            data[i] = dataScanner.nextDouble();
        }

        Scanner queryScanner = new Scanner(getClass().getResourceAsStream("query-128.txt")).useLocale(Locale.ENGLISH);
        double[] query = new double[m];
        for (int i = 0; i < m && queryScanner.hasNextDouble(); i++) {
            query[i] = queryScanner.nextDouble();
        }

        double min = Double.POSITIVE_INFINITY;
        int posMin = 0;
        int n = 0;
//        long duration = 0;
        while (dataScanner.hasNextDouble()) {
            System.arraycopy(data, 1, copy, 0, m - 1);
            copy[m - 1] = dataScanner.nextDouble();

//            long start = System.currentTimeMillis();
            double d = mpd.compute(copy, query, min);
//            duration += System.currentTimeMillis() - start;

            if (d < min) {
                min = d;
                posMin = n;
            }

            data = copy;
            n++;
        }

//        System.out.println("duration=" + duration);

        Assert.assertEquals(48002, posMin);
        Assert.assertEquals(3.4811675327308373, min, TimeSeriesPrecision.EPSILON);
    }
}