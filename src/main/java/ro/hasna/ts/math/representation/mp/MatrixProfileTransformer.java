package ro.hasna.ts.math.representation.mp;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.FastMath;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.stat.BothWaySummaryStatistics;
import ro.hasna.ts.math.type.FullMatrixProfile;
import ro.hasna.ts.math.type.MatrixProfile;

import java.io.Serializable;

/**
 * Implements the STOMP algorithm to compute the matrix profile.
 * <p>
 * Reference:
 * Zhu Y., Zimmerman Z., Senobari N. S., Yeh C. C. M., Funning G., Mueen A., Keogh E. (2016)
 * <i>Exploiting a Novel Algorithm and GPUs to Break the One Hundred Million Barrier for Time Series Motifs and Joins</i>
 * </p>
 */
public class MatrixProfileTransformer implements Serializable {
    private static final long serialVersionUID = 1675705026272406220L;
    private final int window;
    private final boolean useNormalization;

    /**
     * Creates a new instance of this class with normalization enabled.
     *
     * @param window the length of the window
     * @throws NumberIsTooSmallException if window is lower than 1
     */
    public MatrixProfileTransformer(int window) {
        this(window, true);
    }

    /**
     * @param window           the length of the window
     * @param useNormalization flag to use Z-Normalization
     * @throws NumberIsTooSmallException if window is lower than 1
     */
    public MatrixProfileTransformer(int window, boolean useNormalization) {
        this.useNormalization = useNormalization;
        if (window < 1) {
            throw new NumberIsTooSmallException(window, 1, true);
        }

        this.window = window;
    }

    public MatrixProfile transform(double[] a, double[] b) {
        if (Math.min(a.length, b.length) < window) {
            throw new ArrayLengthIsTooSmallException(Math.min(a.length, b.length), window, true);
        }

        if (useNormalization) {
            return computeNormalizedFullMatrixProfile(a, b).getLeftMatrixProfile();
        }
        return computeFullMatrixProfile(a, b).getLeftMatrixProfile();
    }

    public FullMatrixProfile fullJoinTransform(double[] a, double[] b) {
        if (Math.min(a.length, b.length) < window) {
            throw new ArrayLengthIsTooSmallException(Math.min(a.length, b.length), window, true);
        }

        if (useNormalization) {
            return computeNormalizedFullMatrixProfile(a, b);
        }
        return computeFullMatrixProfile(a, b);
    }

    private FullMatrixProfile computeNormalizedFullMatrixProfile(double[] a, double[] b) {
        int na = a.length - window + 1;
        int nb = b.length - window + 1;
        FullMatrixProfile fmp = new FullMatrixProfile(nb, na);
        double[] distanceProfile = new double[nb];

        double[] productSums = new double[nb];
        BothWaySummaryStatistics first = new BothWaySummaryStatistics();
        BothWaySummaryStatistics second = new BothWaySummaryStatistics();
        for (int i = 0; i < window; i++) {
            first.addValue(a[i]);
            second.addValue(b[i]);
        }

        MatrixProfileUtil.computeFirstNormalizedDistanceProfile(a, b, 0, nb, window, distanceProfile, productSums, first, second);
        updateMatrixProfileFromDistanceProfile(distanceProfile, nb, 0, fmp);
        for (int i = 1; i < na; i++) {
            computeNextNormalizedDistanceProfile(a, b, nb, distanceProfile, productSums, first, second, i);
            updateMatrixProfileFromDistanceProfile(distanceProfile, nb, i, fmp);
        }
        sqrtMatrixProfile(fmp);
        return fmp;
    }

    /**
     * First is updated, second is not updated.
     */
    private void computeNextNormalizedDistanceProfile(double[] a, double[] b, int nb, double[] distanceProfile, double[] productSums, BothWaySummaryStatistics first, BothWaySummaryStatistics second, int i) {
        first.addValue(a[i + window - 1]);
        first.removeValue(a[i - 1]);
        BothWaySummaryStatistics secondClone = second.clone();

        for (int j = nb - 1; j >= 1; j--) {
            if (j < nb - 1) {
                secondClone.removeValue(b[j + window]);
                secondClone.addValue(b[j]);
            }
            double prev = a[i - 1] * b[j - 1];
            double next = a[i + window - 1] * b[j + window - 1];
            productSums[j] = productSums[j - 1] - prev + next;
            distanceProfile[j] = MatrixProfileUtil.computeNormalizedDistance(window, productSums[j], first, secondClone);
        }

        secondClone.removeValue(b[window]);
        secondClone.addValue(b[0]);
        double productSum = 0;
        for (int k = 0; k < window; k++) {
            productSum += a[k + i] * b[k];
        }
        productSums[0] = productSum;
        distanceProfile[0] = MatrixProfileUtil.computeNormalizedDistance(window, productSums[0], first, secondClone);
    }

    private FullMatrixProfile computeFullMatrixProfile(double[] a, double[] b) {
        int na = a.length - window + 1;
        int nb = b.length - window + 1;
        FullMatrixProfile fmp = new FullMatrixProfile(nb, na);
        double[] distanceProfile = new double[nb];

        MatrixProfileUtil.computeFirstDistanceProfile(a, b, 0, nb, window, distanceProfile);
        updateMatrixProfileFromDistanceProfile(distanceProfile, nb, 0, fmp);
        for (int i = 1; i < na; i++) {
            computeNextDistanceProfile(a, b, nb, distanceProfile, i);
            updateMatrixProfileFromDistanceProfile(distanceProfile, nb, i, fmp);
        }
        sqrtMatrixProfile(fmp);
        return fmp;
    }

    private void computeNextDistanceProfile(double[] a, double[] b, int nb, double[] prevDistanceProfile, int i) {
        for (int j = nb - 1; j >= 1; j--) {
            double prev = a[i - 1] - b[j - 1];
            double next = a[i + window - 1] - b[j + window - 1];
            prevDistanceProfile[j] = prevDistanceProfile[j - 1] - prev * prev + next * next;
        }

        double distance = 0;
        for (int k = 0; k < window; k++) {
            distance += (a[k + i] - b[k]) * (a[k + i] - b[k]);
        }
        prevDistanceProfile[0] = distance;
    }

    private void updateMatrixProfileFromDistanceProfile(double[] distanceProfile, int nb, int i, FullMatrixProfile mp) {
        double[] leftProfile = mp.getLeftMatrixProfile().getProfile();
        int[] leftIndexProfile = mp.getLeftMatrixProfile().getIndexProfile();
        double[] rightProfile = mp.getRightMatrixProfile().getProfile();
        int[] rightIndexProfile = mp.getRightMatrixProfile().getIndexProfile();
        for (int j = 0; j < nb; j++) {
            // update horizontal line
            if (leftProfile[j] > distanceProfile[j]) {
                leftProfile[j] = distanceProfile[j];
                leftIndexProfile[j] = i;
            }
            // update vertical line
            if (rightProfile[i] > distanceProfile[j]) {
                rightProfile[i] = distanceProfile[j];
                rightIndexProfile[i] = j;
            }
        }
    }

    private void sqrtMatrixProfile(FullMatrixProfile fmp) {
        double[] leftProfile = fmp.getLeftMatrixProfile().getProfile();
        for (int i = 0; i < leftProfile.length; i++) {
            leftProfile[i] = FastMath.sqrt(leftProfile[i]);
        }
        double[] rightProfile = fmp.getRightMatrixProfile().getProfile();
        for (int i = 0; i < rightProfile.length; i++) {
            rightProfile[i] = FastMath.sqrt(rightProfile[i]);
        }
    }
}
