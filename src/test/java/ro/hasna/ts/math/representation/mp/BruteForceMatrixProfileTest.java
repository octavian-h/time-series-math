package ro.hasna.ts.math.representation.mp;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Assert;
import org.junit.Test;
import ro.hasna.ts.math.type.MatrixProfile;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

import java.util.Arrays;

/**
 * @author ohasna
 * @since 22.05.2019
 */
public class BruteForceMatrixProfileTest {

    @Test
    public void euclideanDistanceWithNormalization() {
        double[] t = new double[]{1, 2, 3, 77, 100, 50};
        double[] q = new double[]{4, 56, 80};
        double[] d = new double[t.length - q.length + 1];
        double[] d2 = new double[t.length - q.length + 1];

        double meanQ = new Mean().evaluate(q);
        double sdQ = new StandardDeviation(false).evaluate(q);

        for (int i = 0; i < t.length - q.length + 1; i++) {
            Mean mean = new Mean();
            StandardDeviation sd = new StandardDeviation(false);
            double qt = 0;
            for (int j = 0; j < q.length; j++) {
                int k = i + j;
                qt += t[k] * q[j];
                mean.increment(t[k]);
                sd.increment(t[k]);
            }
            double meanT = mean.getResult();
            double sdT = sd.getResult();
            d[i] = 2 * q.length * (1 - (qt - q.length * meanT * meanQ) / (q.length * sdT * sdQ));
            d2[i] = 0;
            for (int j = 0; j < q.length; j++) {
                int k = i + j;
                double tNormalised = (t[k] - meanT) / sdT;
                double qNormalised = (q[j] - meanQ) / sdQ;
                d2[i] += (tNormalised - qNormalised) * (tNormalised - qNormalised);
            }
            d2[i] = d2[i];

        }

        System.out.println(Arrays.toString(d));
        System.out.println(Arrays.toString(d2));
    }

    @Test
    public void testTransform() throws Exception {
        BruteForceMatrixProfile bfmp = new BruteForceMatrixProfile(3);
        double[] v = {1, 2, 3, 50, 20, 71, 2, 2, 3, 15, 19};


        MatrixProfile transform = bfmp.transform(v);
        double[] expectedMp = new double[]{1, 1106, 1054, 3034, 1054, 4762, 1, 145, 161};
        int[] expectedIp = new int[]{6, 8, 4, 1, 2, 6, 0, 6, 7};

        Assert.assertArrayEquals(expectedMp, transform.getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getIndexProfile());
    }
}