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
    private double epsilon;
    private int delta;

    public LongestCommonSubsequenceDistance(double epsilon, int delta) {
        this.epsilon = epsilon;
        this.delta = delta;
    }

    @Override
    public double compute(double[] a, double[] b) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(double[] a, double[] b, double cutOffValue) {
        int na = a.length;
        int nb = b.length;
        double[][] m = new double[na][nb];
        for (int i = 0; i < na; i++) {
            for (int j = 0; j < nb; j++) {
                m[i][j] = -1;
            }
        }
        return lcss(a, na - 1, b, nb - 1, m, cutOffValue);
    }

    private double lcss(double[] a, int na, double[] b, int nb, double[][] m, double cutOffValue) {
        if (na < 0 || nb < 0)
            return 0d;

        if (FastMath.abs(a[na] - b[nb]) < epsilon && FastMath.abs(na - nb) <= delta) {
            if (na < 1 || nb < 1)
                return 1d;

            if (m[na - 1][nb - 1] == -1) {
                double d = lcss(a, na - 1, b, nb - 1, m, cutOffValue) + 1;
                if (d >= cutOffValue) {
                    return Double.POSITIVE_INFINITY;
                }
                m[na - 1][nb - 1] = d;
            }

            return m[na - 1][nb - 1];
        }

        if (na < 1 || nb < 1)
            return 0d;

        double d1;
        if (m[na - 1][nb] == -1) {
            d1 = lcss(a, na - 1, b, nb, m, cutOffValue);
            if (d1 >= cutOffValue) {
                return Double.POSITIVE_INFINITY;
            }
            m[na - 1][nb] = d1;
        } else {
            d1 = m[na - 1][nb];
        }

        double d2;
        if (m[na][nb - 1] == -1) {
            d2 = lcss(a, na, b, nb - 1, m, cutOffValue);
            if (d2 >= cutOffValue) {
                return Double.POSITIVE_INFINITY;
            }
            m[na - 1][nb] = d2;
        } else {
            d2 = m[na][nb - 1];
        }

        return FastMath.max(d1, d2);
    }

    @Override
    public double compute(double[] a, double[] b, int n, double cutOffValue) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }
}
