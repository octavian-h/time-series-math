/**
 * Copyright (C) 2015 Octavian Hasna
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

import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

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

    public LongestCommonSubsequenceDistance(double epsilon, double radiusPercentage) {
        this.epsilon = epsilon;
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
        double d = 1 - computeLcs(a, b, n, radius) * 1.0 / n;
        if (d >= cutOffValue) {
            return Double.POSITIVE_INFINITY;
        }
        return d;
    }

    @Override
    public double compute(double[] a, double[] b, int n, double cutOffValue) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }

    private int computeLcs(double[] a, double b[], int n, int radius) {
        boolean equals = true;
        for (int i = 0; i < n && equals; i++) {
            if (!Precision.equals(a[i], b[i], epsilon)) {
                equals = false;
            }
        }
        if (equals) {
            return n;
        }

        int[] prev = new int[n];
        int[] current = new int[n];
        int start, end;
        for (int i = 0; i < n; i++) {
            start = FastMath.max(0, i - radius);
            end = FastMath.min(n - 1, i + radius);
            for (int j = start; j <= end; j++) {
                if (Precision.equals(a[i], b[j], epsilon)) {
                    if (j == 0) {
                        current[j] = 1;
                    } else {
                        current[j] = prev[j - 1] + 1;
                    }
                } else {
                    if (j == 0) {
                        current[j] = prev[j];
                    } else {
                        current[j] = FastMath.max(current[j - 1], prev[j]);
                    }
                }
            }

            System.arraycopy(current, 0, prev, 0, n);
        }

        return current[n - 1];
    }
}
