package ro.hasna.ts.math.representation.mp;

import ro.hasna.ts.math.stat.BothWaySummaryStatistics;
import ro.hasna.ts.math.type.MatrixProfile;

import java.util.function.Predicate;

/**
 * Implements the SCRIMP algorithm to compute the self join matrix profile.
 */
public class ScrimpTransformer extends SelfJoinAbstractMatrixProfileTransformer {
    private static final long serialVersionUID = -7360923839319598742L;

    public ScrimpTransformer(int window) {
        super(window);
    }

    public ScrimpTransformer(int window, double exclusionZonePercentage, boolean useNormalization) {
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

        int numDiagonals = n - skip - 1;
        int[] indices = generateRandomIndices(numDiagonals);
        for (int i = 0; i < numDiagonals; i++) {
            int indexB = indices[i] + skip;
            computeNextNormalizedDistanceProfile(input, input, indexB, n, productSums[indexB], distanceProfile);
            updateMatrixProfileFromDiagonalDistanceProfile(distanceProfile, indexB, n, mp, callback);
        }

        updateMatrixProfileWithSqrt(mp);
        return mp;
    }

    protected void computeNextNormalizedDistanceProfile(double[] a, double[] b, int indexB, int n, double prevProductSum, double[] distanceProfile) {
        BothWaySummaryStatistics first = new BothWaySummaryStatistics();
        int indexA = 0;
        BothWaySummaryStatistics second = new BothWaySummaryStatistics();
        for (int k = 0; k < window; k++) {
            first.addValue(a[indexA + k]);
            second.addValue(b[indexB + k]);
        }

        while (indexB < n - 1) {
            first.removeValue(a[indexA]);
            second.removeValue(b[indexB]);
            first.addValue(a[indexA + window]);
            second.addValue(b[indexB + window]);
            prevProductSum = prevProductSum - a[indexA] * b[indexB] + a[indexA + window] * b[indexB + window];

            indexA++;
            indexB++;

            distanceProfile[indexB] = computeNormalizedDistance(prevProductSum, first, second);
        }
    }

    protected void updateMatrixProfileFromDiagonalDistanceProfile(double[] distanceProfile, int indexB, int n, MatrixProfile mp, Predicate<MatrixProfile> callback) {
        int indexA = 0;
        while (indexB < n - 1) {
            indexA++;
            indexB++;

            // upper diagonal
            if (mp.getProfile()[indexB] > distanceProfile[indexB]) {
                mp.getProfile()[indexB] = distanceProfile[indexB];
                mp.getIndexProfile()[indexB] = indexA;
            }

            // lower diagonal
            if (mp.getProfile()[indexA] > distanceProfile[indexB]) {
                mp.getProfile()[indexA] = distanceProfile[indexB];
                mp.getIndexProfile()[indexA] = indexB;
            }
        }

        executeCallback(callback, mp);
    }

    @Override
    protected MatrixProfile computeMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
        int n = input.length - window + 1;
        MatrixProfile mp = new MatrixProfile(n);
        double[] firstDistanceProfile = new double[n];
        double[] distanceProfile = new double[n];

        computeFirstDistanceProfileWithProductSums(input, input, skip, n, firstDistanceProfile);
        updateMatrixProfileFromDistanceProfile(firstDistanceProfile, 0, skip, n, mp, callback);

        int numDiagonals = n - skip - 1;
        int[] indices = generateRandomIndices(numDiagonals);
        for (int i = 0; i < numDiagonals; i++) {
            int indexB = indices[i] + skip;
            computeNextDistanceProfile(input, input, indexB, n, firstDistanceProfile[indexB], distanceProfile);
            updateMatrixProfileFromDiagonalDistanceProfile(distanceProfile, indexB, n, mp, callback);
        }

        updateMatrixProfileWithSqrt(mp);
        return mp;
    }

    protected void computeNextDistanceProfile(double[] a, double[] b, int indexB, int n, double prevDistance, double[] distanceProfile) {
        int indexA = 0;
        while (indexB < n - 1) {
            double prev = a[indexA] - b[indexB];
            double next = a[indexA + window] - b[indexB + window];
            prevDistance = prevDistance - prev * prev + next * next;

            indexA++;
            indexB++;

            distanceProfile[indexB] = prevDistance;
        }
    }
}
