package ro.hasna.ts.math.representation.mp;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.type.MatrixProfile;

public class BruteForceMatrixProfile implements MatrixProfileTransformer {
    private final int window;
    private final double exclusionZonePercentage;

    public BruteForceMatrixProfile(int window) {
        this(window, 0.25);
    }

    public BruteForceMatrixProfile(int window, double exclusionZonePercentage) {
        if (window < 1) {
            throw new NumberIsTooSmallException(window, 1, true);
        }

        this.window = window;
        this.exclusionZonePercentage = exclusionZonePercentage;
    }

    @Override
    public MatrixProfile transform(double[] input) {
        int len = input.length;
        int skip = Math.max((int) (window * exclusionZonePercentage), 1);
        if (len < window + skip) {
            throw new ArrayLengthIsTooSmallException(len, window + skip, true);
        }

        int n = len - window + 1;
        double[] distanceProfile = new double[n];

        double[] profile = new double[n];
        int[] indexProfile = new int[n];
        for (int j = 0; j < n; j++) {
            profile[j] = Double.POSITIVE_INFINITY;
            indexProfile[j] = -1;
        }

        for (int i = 0; i < n - skip; i++) {
            if (i == 0){
                computeFirstDistanceProfile(input, n, skip, distanceProfile);
            } else {
                computeNextDistanceProfile(input, n, skip, distanceProfile, i);
            }

            for (int j = i + skip; j < n; j++) {
                // update horizontal line from upper triangle
                if (profile[j] > distanceProfile[j]) {
                    profile[j] = distanceProfile[j];
                    indexProfile[j] = i;
                }
                // update vertical line i from matrix profile
                if (profile[i] > distanceProfile[j]) {
                    profile[i] = distanceProfile[j];
                    indexProfile[i] = j;
                }
            }
        }

        return new MatrixProfile(profile, indexProfile);
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
            for (int k = j; k < j + window; k++) {
                distance += (input[k - j] - input[k]) * (input[k - j] - input[k]);
            }
            distanceProfile[j] = distance;
        }
    }

    @Override
    public MatrixProfile transform(double[] a, double[] b) {
        //TODO
        return null;
    }
}
