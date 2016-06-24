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
package ro.hasna.ts.math.ml.distance;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import ro.hasna.ts.math.exception.util.LocalizableMessages;

/**
 * Calculates the distance between two vectors using Longest Common Subsequence.
 * <p>
 * Reference:
 * Vlachos Michail, George Kollios and Dimitrios Gunopulos (2002)
 * <i>Discovering similar multidimensional trajectories</i>
 * </p>
 *
 * @since 1.0
 */
public class LongestCommonSubsequenceDistance implements GenericDistanceMeasure<double[]> {
    private static final long serialVersionUID = 4542559569313251930L;
    private final double epsilon;
    private final double radiusPercentage;

    /**
     * Creates a new instance of this class with
     *
     * @param epsilon          the maximum absolute difference between two values that are considered equal
     * @param radiusPercentage Sakoe-Chiba Band width used to constraint the searching window
     * @throws OutOfRangeException if radiusPercentage is outside the interval [0, 1]
     */
    public LongestCommonSubsequenceDistance(double epsilon, double radiusPercentage) {
        this.epsilon = epsilon;
        if (radiusPercentage < 0 || radiusPercentage > 1) {
            throw new OutOfRangeException(LocalizableMessages.OUT_OF_RANGE_BOTH_INCLUSIVE, radiusPercentage, 0, 1);
        }

        this.radiusPercentage = radiusPercentage;
    }

    @Override
    public double compute(double[] a, double[] b) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(double[] a, double[] b, double cutOffValue) {
        int n = a.length;
        int radius = (int) (n * radiusPercentage);
        double min = -1;
        if (cutOffValue < 1) {
            min = n * (1 - cutOffValue);
        }

        int lcs = computeLcs(a, b, n, radius, min);
        if (lcs == -1) {
            return Double.POSITIVE_INFINITY;
        }

        return 1 - lcs * 1.0 / n;
    }

    private int computeLcs(double[] a, double b[], int n, int radius, double min) {
        min = min - n + 1;

        int w = 2 * radius + 1;
        int[] prev = new int[w];
        int[] current = new int[w];
        int start, end, x, y, k = 0;
        for (int i = 0; i < n; i++) {
            k = FastMath.max(0, radius - i);
            start = FastMath.max(0, i - radius);
            end = FastMath.min(n - 1, i + radius);
            for (int j = start; j <= end; j++, k++) {
                if (Precision.equals(a[i], b[j], epsilon)) {
                    current[k] = prev[k] + 1;
                } else {
                    if (k - 1 >= 0) x = current[k - 1];
                    else x = 0;

                    if (k + 1 < w) y = prev[k + 1];
                    else y = 0;

                    current[k] = FastMath.max(x, y);
                }
            }

            if (current[k - 1] - i <= min) {
                return -1;
            }

            System.arraycopy(current, 0, prev, 0, w);
        }

        return current[k - 1];
    }
}
