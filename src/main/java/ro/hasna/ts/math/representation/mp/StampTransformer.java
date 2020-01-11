/*
 * Copyright 2015 Octavian Hasna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.hasna.ts.math.representation.mp;

import org.apache.commons.math3.complex.Complex;
import ro.hasna.ts.math.stat.BothWaySummaryStatistics;
import ro.hasna.ts.math.type.MatrixProfile;

import java.util.function.Predicate;

/**
 * Implements the STAMP algorithm to compute the self join matrix profile.
 * <p>
 * Reference:
 * Yeh C. C. M., Zhu Y., Ulanova L., Begum N., Ding Y., Dau H. A., Keogh E. (2016, December)
 * <i>Matrix profile I: all pairs similarity joins for time series: a unifying view that includes motifs, discords and shapelets</i>
 * </p>
 *
 * @since 0.17
 */
public class StampTransformer extends SelfJoinAbstractMatrixProfileTransformer {
    private static final long serialVersionUID = 5919724985496947961L;

    public StampTransformer(int window) {
        super(window);
    }

    public StampTransformer(int window, double exclusionZonePercentage, boolean useNormalization) {
        super(window, exclusionZonePercentage, useNormalization);
    }

    @Override
    protected MatrixProfile computeNormalizedMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
        int n = input.length - window + 1;
        MatrixProfile mp = new MatrixProfile(n);
        double[] distanceProfile = new double[n];

        int[] indices = generateRandomIndices(n);
        for (int i = 0; i < n; i++) {
            int index = indices[i];
            computeNormalizedDistanceProfileWithFft(input, index, input, skip, n, distanceProfile);
            updateMatrixProfileFromDistanceProfile(distanceProfile, n, index, mp, callback);
        }

        updateMatrixProfileWithSqrt(mp);
        return mp;
    }

    protected void updateMatrixProfileFromDistanceProfile(double[] distanceProfile, int n, int i, MatrixProfile mp, Predicate<MatrixProfile> callback) {
        for (int j = 0; j < n; j++) {
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

        executeCallback(callback, mp);
    }

    @Override
    protected MatrixProfile computeMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
        int n = input.length - window + 1;
        MatrixProfile mp = new MatrixProfile(n);
        double[] distanceProfile = new double[n];

        int[] indices = generateRandomIndices(n);
        for (int i = 0; i < n; i++) {
            int index = indices[i];
            computeDistanceProfileWithProductSums(input, index, input, skip, n, distanceProfile);
            updateMatrixProfileFromDistanceProfile(distanceProfile, n, index, mp, callback);
        }

        updateMatrixProfileWithSqrt(mp);
        return mp;
    }

    protected void computeDistanceProfileWithProductSums(double[] a, int i, double[] b, int skip, int nb, double[] distanceProfile) {
        for (int j = 0; j < nb; j++) {
            if (inExclusionZone(i, j, skip)) {
                distanceProfile[j] = Double.POSITIVE_INFINITY;
            } else {
                double distance = 0;
                for (int k = 0; k < window; k++) {
                    distance += (a[k + i] - b[k + j]) * (a[k + i] - b[k + j]);
                }
                distanceProfile[j] = distance;
            }
        }
    }

    protected void computeNormalizedDistanceProfileWithFft(double[] a, int i, double[] b, int skip, int nb, double[] distanceProfile) {
        BothWaySummaryStatistics first = new BothWaySummaryStatistics();
        BothWaySummaryStatistics second = new BothWaySummaryStatistics();
        for (int k = 0; k < window; k++) {
            first.addValue(a[k + i]);
            second.addValue(b[k]);
        }
        Complex[] transform = computeConvolutionUsingFft(a, i, b);
        for (int j = 0; j < nb; j++) {
            if (j > 0) {
                second.addValue(b[j + window - 1]);
                second.removeValue(b[j - 1]);
            }

            if (inExclusionZone(i, j, skip)) {
                distanceProfile[j] = Double.POSITIVE_INFINITY;
            } else {
                double productSum = transform[j + window - 1].getReal();
                distanceProfile[j] = computeNormalizedDistance(productSum, first, second);
            }
        }
    }
}
