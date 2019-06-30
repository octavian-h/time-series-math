package ro.hasna.ts.math.representation.mp;

import ro.hasna.ts.math.stat.BothWaySummaryStatistics;
import ro.hasna.ts.math.type.MatrixProfile;

import java.util.function.Predicate;


/**
 * Implements the STOMP algorithm to compute the self join matrix profile.
 * <p>
 * Reference:
 * Zhu Y., Zimmerman Z., Senobari N. S., Yeh C. C. M., Funning G., Mueen A., Keogh E. (2016)
 * <i>Exploiting a Novel Algorithm and GPUs to Break the One Hundred Million Barrier for Time Series Motifs and Joins</i>
 * </p>
 */
public class StompTransformer extends SelfJoinAbstractMatrixProfileTransformer {
    private static final long serialVersionUID = 2910211807207716085L;

    public StompTransformer(int window) {
        super(window);
    }

    public StompTransformer(int window, double exclusionZonePercentage, boolean useNormalization) {
        super(window, exclusionZonePercentage, useNormalization);
    }

    @Override
    protected MatrixProfile computeNormalizedMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
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

        computeFirstNormalizedDistanceProfile(input, first, input, second, skip, n, productSums, distanceProfile);
        updateMatrixProfileFromDistanceProfile(distanceProfile, 0, skip, n, mp, callback);
        for (int i = 1; i < n - skip; i++) {
            computeNextNormalizedDistanceProfile(input, skip, n, distanceProfile, productSums, first, second, i);
            updateMatrixProfileFromDistanceProfile(distanceProfile, i, skip, n, mp, callback);
        }
        updateMatrixProfileWithSqrt(mp);
        return mp;
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

    @Override
    protected MatrixProfile computeMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
        // O(N + w*N + N + N*N)
        int n = input.length - window + 1;
        MatrixProfile mp = new MatrixProfile(n);
        double[] distanceProfile = new double[n];

        computeFirstDistanceProfileWithProductSums(input, input, skip, n, distanceProfile);
        updateMatrixProfileFromDistanceProfile(distanceProfile, 0, skip, n, mp, callback);
        for (int i = 1; i < n - skip; i++) {
            computeNextDistanceProfile(input, n, skip, distanceProfile, i);
            updateMatrixProfileFromDistanceProfile(distanceProfile, i, skip, n, mp, callback);
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
}
