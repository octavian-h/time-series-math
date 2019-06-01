package ro.hasna.ts.math.representation.mp;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Pair;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.representation.GenericTransformer;
import ro.hasna.ts.math.stat.BothWaySummaryStatistics;
import ro.hasna.ts.math.type.MatrixProfile;

/**
 * Implements the STOMP algorithm to compute the (left join) matrix profile.
 * <p>
 * Reference:
 * Zhu Y., Zimmerman Z., Senobari N. S., Yeh C. C. M., Funning G., Mueen A., Keogh E. (2016)
 * <i>Exploiting a Novel Algorithm and GPUs to Break the One Hundred Million Barrier for Time Series Motifs and Joins</i>
 * </p>
 */
public class LeftJoinMatrixProfileTransformer implements GenericTransformer<Pair<double[], double[]>, MatrixProfile> {
    private static final long serialVersionUID = 1675705026272406220L;
    private final int window;
    private final boolean useNormalization;

    /**
     * Creates a new instance of this class with normalization enabled.
     *
     * @param window the length of the window
     * @throws NumberIsTooSmallException if window is lower than 1
     */
    public LeftJoinMatrixProfileTransformer(int window) {
        this(window, true);
    }

    /**
     * @param window           the length of the window
     * @param useNormalization flag to use Z-Normalization
     * @throws NumberIsTooSmallException if window is lower than 1
     */
    public LeftJoinMatrixProfileTransformer(int window, boolean useNormalization) {
        this.useNormalization = useNormalization;
        if (window < 1) {
            throw new NumberIsTooSmallException(window, 1, true);
        }

        this.window = window;
    }

    @Override
    public MatrixProfile transform(Pair<double[], double[]> input) {
        return transform(input.getFirst(), input.getSecond());
    }

    public MatrixProfile transform(double[] a, double[] b) {
        if (Math.min(a.length, b.length) < window) {
            throw new ArrayLengthIsTooSmallException(Math.min(a.length, b.length), window, true);
        }

        if (useNormalization) {
            return computeNormalizedMatrixProfile(a, b);
        }
        return computeMatrixProfile(a, b);
    }

    private MatrixProfile computeNormalizedMatrixProfile(double[] a, double[] b) {
        int na = a.length - window + 1;
        int nb = b.length - window + 1;
        MatrixProfile mp = new MatrixProfile(nb);
        double[] distanceProfile = new double[nb];

        double[] productSums = new double[nb];
        BothWaySummaryStatistics first = new BothWaySummaryStatistics();
        BothWaySummaryStatistics second = new BothWaySummaryStatistics();
        for (int i = 0; i < window; i++) {
            first.addValue(a[i]);
            second.addValue(b[i]);
        }

        computeFirstNormalizedDistanceProfile(a, b, nb, distanceProfile, productSums, first, second);
        updateMatrixProfileFromDistanceProfile(distanceProfile, nb, 0, mp);
        for (int i = 1; i < na; i++) {
            computeNextNormalizedDistanceProfile(a, b, nb, distanceProfile, productSums, first, second, i);
            updateMatrixProfileFromDistanceProfile(distanceProfile, nb, i, mp);
        }

        for (int i = 0; i < nb; i++) {
            mp.getProfile()[i] = FastMath.sqrt(mp.getProfile()[i]);
        }
        return mp;
    }

    /**
     * When this is finished second will contain statistics for last sliding window
     */
    private void computeFirstNormalizedDistanceProfile(double[] a, double[] b, int nb, double[] distanceProfile, double[] productSums, BothWaySummaryStatistics first, BothWaySummaryStatistics second) {
        for (int j = 0; j < nb; j++) {
            if (j > 0) {
                second.addValue(b[j + window - 1]);
                second.removeValue(b[j - 1]);
            }
            double productSum = 0;
            for (int k = j; k < j + window; k++) {
                productSum += a[k - j] * b[k];
            }
            productSums[j] = productSum;
            distanceProfile[j] = computeNormalizedDistance(productSums[j], first, second);
        }
    }

    /**
     * First is updated, second is not updated.
     */
    private void computeNextNormalizedDistanceProfile(double[] a, double[] b, int nb, double[] distanceProfile, double[] productSums, BothWaySummaryStatistics first, BothWaySummaryStatistics second, int i) {
        first.addValue(a[i + window - 1]);
        first.removeValue(a[i - 1]);
        BothWaySummaryStatistics secondClone = second.clone();

        for (int j = nb - 1; j >= i; j--) {
            if (j < nb - 1) {
                secondClone.removeValue(b[j + window]);
                secondClone.addValue(b[j]);
            }
            double prev = a[i - 1] * b[j - 1];
            double next = a[i + window - 1] * b[j + window - 1];
            productSums[j] = productSums[j - 1] - prev + next;
            distanceProfile[j] = computeNormalizedDistance(productSums[j], first, secondClone);
        }
    }

    private double computeNormalizedDistance(double productSum, BothWaySummaryStatistics first, BothWaySummaryStatistics second) {
        return 2.0 * window * (1 - (productSum - window * first.getMean() * second.getMean()) / (window * first.getStandardDeviation() * second.getStandardDeviation()));
    }

    private MatrixProfile computeMatrixProfile(double[] a, double[] b) {
        int na = a.length - window + 1;
        int nb = b.length - window + 1;
        MatrixProfile mp = new MatrixProfile(nb);
        double[] distanceProfile = new double[nb];

        computeFirstDistanceProfile(a, b, nb, distanceProfile);
        updateMatrixProfileFromDistanceProfile(distanceProfile, nb, 0, mp);
        for (int i = 1; i < na; i++) {
            computeNextDistanceProfile(a, b, nb, distanceProfile, i);
            updateMatrixProfileFromDistanceProfile(distanceProfile, nb, 0, mp);
        }
        return mp;
    }

    private void computeFirstDistanceProfile(double[] a, double[] b, int nb, double[] distanceProfile) {
        for (int j = 0; j < nb; j++) {
            double distance = 0;
            for (int k = j; k < j + window; k++) {
                distance += (a[k - j] - b[k]) * (a[k - j] - b[k]);
            }
            distanceProfile[j] = distance;
        }
    }

    private void computeNextDistanceProfile(double[] a, double[] b, int nb, double[] prevDistanceProfile, int i) {
        for (int j = nb - 1; j >= i; j--) {
            double prev = a[i - 1] - b[j - 1];
            double next = a[i + window - 1] - b[j + window - 1];
            prevDistanceProfile[j] = prevDistanceProfile[j - 1] - prev * prev + next * next;
        }
    }

    private void updateMatrixProfileFromDistanceProfile(double[] distanceProfile, int nb, int i, MatrixProfile mp) {
        for (int j = i; j < nb; j++) {
            // update horizontal line from upper triangle
            if (mp.getProfile()[j] > distanceProfile[j]) {
                mp.getProfile()[j] = distanceProfile[j];
                mp.getIndexProfile()[j] = i;
            }
            // update vertical line i from matrix profile
            if (mp.getProfile()[i] > distanceProfile[j]) {
                mp.getProfile()[i] = distanceProfile[j];
                mp.getIndexProfile()[i] = j;
            }
        }
    }
}
