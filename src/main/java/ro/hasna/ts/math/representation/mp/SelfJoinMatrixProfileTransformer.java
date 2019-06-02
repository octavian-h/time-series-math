package ro.hasna.ts.math.representation.mp;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.FastMath;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.representation.GenericTransformer;
import ro.hasna.ts.math.stat.BothWaySummaryStatistics;
import ro.hasna.ts.math.type.MatrixProfile;

/**
 * Implements the STOMP algorithm to compute the self join matrix profile.
 * <p>
 * Reference:
 * Zhu Y., Zimmerman Z., Senobari N. S., Yeh C. C. M., Funning G., Mueen A., Keogh E. (2016)
 * <i>Exploiting a Novel Algorithm and GPUs to Break the One Hundred Million Barrier for Time Series Motifs and Joins</i>
 * </p>
 */
public class SelfJoinMatrixProfileTransformer implements GenericTransformer<double[], MatrixProfile> {
    private static final long serialVersionUID = 4273395812927663256L;
    private final int window;
    private final double exclusionZonePercentage;
    private final boolean useNormalization;

    /**
     * Creates a new instance of this class with normalization enabled and an exclusion zone of 25%.
     *
     * @param window the length of the window
     * @throws NumberIsTooSmallException if window is lower than 4
     */
    public SelfJoinMatrixProfileTransformer(int window) {
        this(window, 0.25, true);
    }

    /**
     * @param window                  the length of the window
     * @param exclusionZonePercentage percentage of window that should be excluded at distance profile computing
     * @param useNormalization        flag to use Z-Normalization
     * @throws NumberIsTooSmallException if window or window * exclusionZonePercentage is lower than 1
     */
    public SelfJoinMatrixProfileTransformer(int window, double exclusionZonePercentage, boolean useNormalization) {
        this.useNormalization = useNormalization;
        if (window < 1) {
            throw new NumberIsTooSmallException(window, 1, true);
        }
        int skip = (int) (window * exclusionZonePercentage);
        if (skip < 1) {
            int minWindow = (int) Math.ceil(1 / exclusionZonePercentage);
            throw new NumberIsTooSmallException(window, minWindow, true);
        }

        this.window = window;
        this.exclusionZonePercentage = exclusionZonePercentage;
    }

    @Override
    public MatrixProfile transform(double[] input) {
        int len = input.length;
        int skip = (int) (window * exclusionZonePercentage);
        if (len < window + skip) {
            throw new ArrayLengthIsTooSmallException(len, window + skip, true);
        }

        if (useNormalization) {
            return computeNormalizedMatrixProfile(input, skip);
        }
        return computeMatrixProfile(input, skip);
    }

    private MatrixProfile computeNormalizedMatrixProfile(double[] input, int skip) {
        // O(N + w + w*N + N + N*N + N)
        int n = input.length - window + 1;
        MatrixProfile mp = new MatrixProfile(n);
        double[] distanceProfile = new double[n];

        double[] productSums = new double[n];
        BothWaySummaryStatistics first = new BothWaySummaryStatistics();
        BothWaySummaryStatistics second = new BothWaySummaryStatistics();
        for (int i = 0; i < window; i++) {
            first.addValue(input[i]);
            second.addValue(input[i + skip]);
        }

        computeFirstNormalizedDistanceProfile(input, skip, n, distanceProfile, productSums, first, second);
        updateMatrixProfileFromDistanceProfile(distanceProfile, n, skip, 0, mp);
        for (int i = 1; i < n - skip; i++) {
            computeNextNormalizedDistanceProfile(input, skip, n, distanceProfile, productSums, first, second, i);
            updateMatrixProfileFromDistanceProfile(distanceProfile, n, skip, i, mp);
        }
        updateMatrixProfileWithSqrt(mp);
        return mp;
    }

    /**
     * When this is finished second will contain statistics for last sliding window
     */
    private void computeFirstNormalizedDistanceProfile(double[] input, int skip, int n, double[] distanceProfile, double[] productSums, BothWaySummaryStatistics first, BothWaySummaryStatistics second) {
        for (int j = skip; j < n; j++) {
            if (j > skip) {
                second.addValue(input[j + window - 1]);
                second.removeValue(input[j - 1]);
            }
            double productSum = 0;
            for (int k = 0; k < window; k++) {
                productSum += input[k] * input[k + j];
            }
            productSums[j] = productSum;
            distanceProfile[j] = computeNormalizedDistance(productSums[j], first, second);
        }
    }

    /**
     * First is updated, second is not updated.
     */
    private void computeNextNormalizedDistanceProfile(double[] input, int skip, int n, double[] distanceProfile, double[] productSums, BothWaySummaryStatistics first, BothWaySummaryStatistics second, int i) {
        first.addValue(input[i + window - 1]);
        first.removeValue(input[i - 1]);
        BothWaySummaryStatistics secondClone = second.clone();

        for (int j = n - 1; j >= i + skip; j--) {
            if (j < n - 1) {
                secondClone.removeValue(input[j + window]);
                secondClone.addValue(input[j]);
            }
            double prev = input[i - 1] * input[j - 1];
            double next = input[i + window - 1] * input[j + window - 1];
            productSums[j] = productSums[j - 1] - prev + next;
            distanceProfile[j] = computeNormalizedDistance(productSums[j], first, secondClone);
        }
    }

    private double computeNormalizedDistance(double productSum, BothWaySummaryStatistics first, BothWaySummaryStatistics second) {
        return 2.0 * window * (1 - (productSum - window * first.getMean() * second.getMean()) / (window * first.getStandardDeviation() * second.getStandardDeviation()));
    }

    private MatrixProfile computeMatrixProfile(double[] input, int skip) {
        // O(N + w*N + N + N*N)
        int n = input.length - window + 1;
        MatrixProfile mp = new MatrixProfile(n);
        double[] distanceProfile = new double[n];

        computeFirstDistanceProfile(input, n, skip, distanceProfile);
        updateMatrixProfileFromDistanceProfile(distanceProfile, n, skip, 0, mp);
        for (int i = 1; i < n - skip; i++) {
            computeNextDistanceProfile(input, n, skip, distanceProfile, i);
            updateMatrixProfileFromDistanceProfile(distanceProfile, n, skip, i, mp);
        }
        updateMatrixProfileWithSqrt(mp);
        return mp;
    }

    private void computeNextDistanceProfile(double[] input, int n, int skip, double[] prevDistanceProfile, int i) {
        for (int j = n - 1; j >= i + skip; j--) {
            double prev = input[i - 1] - input[j - 1];
            double next = input[i + window - 1] - input[j + window - 1];
            prevDistanceProfile[j] = prevDistanceProfile[j - 1] - prev * prev + next * next;
        }
    }

    private void computeFirstDistanceProfile(double[] input, int n, int skip, double[] distanceProfile) {
        for (int j = skip; j < n; j++) {
            double distance = 0;
            for (int k = 0; k < window; k++) {
                distance += (input[k] - input[k + j]) * (input[k] - input[k + j]);
            }
            distanceProfile[j] = distance;
        }
    }

    private void updateMatrixProfileFromDistanceProfile(double[] distanceProfile, int n, int skip, int i, MatrixProfile mp) {
        for (int j = i + skip; j < n; j++) {
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

    private void updateMatrixProfileWithSqrt(MatrixProfile mp) {
        double[] profile = mp.getProfile();
        for (int i = 0; i < profile.length; i++) {
            profile[i] = FastMath.sqrt(profile[i]);
        }
    }
}
