package ro.hasna.ts.math.representation.mp;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;
import ro.hasna.ts.math.normalization.ZNormalizer;

import java.util.Arrays;

/**
 * @since 0.17
 */
public class MatrixProfileTest {
    @Test
    public void zeroStd() {
        double[] a = {200, 100, 50};
        double[] b = {1, 2, 3};

        ZNormalizer normalizer = new ZNormalizer();
        double[] aNormalize = normalizer.normalize(a);
        double[] bNormalize = normalizer.normalize(b);

        EuclideanDistance ed = new EuclideanDistance();
        double result = ed.compute(aNormalize, bNormalize);

        System.out.println(result);
    }

    @Test
    public void euclideanDistanceWithNormalization() {
        double[] t = {1, 2, 3, 4, 120, 71, 2, 2, 3, 5, 19};
        int window = 4;
        double[] q = new double[window];
        int n = t.length - window + 1;
        for (int m = 0; m < n; m++) {
            System.arraycopy(t, m, q, 0, window);

            double[] d = new double[n];

            double meanQ = new Mean().evaluate(q);
            double sdQ = new StandardDeviation(false).evaluate(q);

            for (int i = 0; i < n; i++) {
                Mean mean = new Mean();
                StandardDeviation sd = new StandardDeviation(false);
                double qt = 0;
                for (int j = 0; j < window; j++) {
                    int k = i + j;
                    qt += t[k] * q[j];
                    mean.increment(t[k]);
                    sd.increment(t[k]);
                }
                double meanT = mean.getResult();
                double sdT = sd.getResult();
                d[i] = 2 * window * (1 - (qt - window * meanT * meanQ) / (window * sdT * sdQ));
            }

            System.out.println(Arrays.toString(d));
        }
    }
}
