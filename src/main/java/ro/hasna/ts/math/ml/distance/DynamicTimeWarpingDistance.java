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
 * Calculates the distance between two vectors using Dynamic Time Warping.
 * <p>
 * Reference:
 * Wikipedia https://en.wikipedia.org/wiki/Dynamic_time_warping
 * </p>
 *
 * @since 1.0
 */
public class DynamicTimeWarpingDistance implements GenericDistanceMeasure<double[]> {
    private static final long serialVersionUID = 1154818905340336905L;
    private final int radius;

    public DynamicTimeWarpingDistance(int radius) {
        this.radius = radius;
    }

    @Override
    public double compute(double[] a, double[] b) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(double[] a, double[] b, double cutOffValue) {
        //TODO compute lower bounds
        return computeFullDtw(a, b, cutOffValue);
    }

    protected double computeFullDtw(double[] a, double[] b, double cutOffValue) {
        int n = a.length;
        int m = b.length;
        double[][] d = new double[2][m + 1];

        int w = FastMath.max(radius, FastMath.abs(n - m));
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j <= m; j++) {
                d[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        d[0][0] = 0;

        for (int i = 1; i <= n; i++) {
            int start = FastMath.max(1, i - w);
            int end = FastMath.min(m, i + w);
            for (int j = start; j <= end; j++) {
                double aux = FastMath.abs(a[i - 1] - b[j - 1]);
                d[1][j] = aux + FastMath.min(d[0][j], FastMath.min(d[1][j - 1], d[0][j - 1]));
                if (d[1][j] > cutOffValue) {
                    return Double.POSITIVE_INFINITY;
                }
            }
            for (int j = 0; j <= m; j++) {
                d[0][j] = d[1][j];
                d[1][j] = Double.POSITIVE_INFINITY;
            }
        }

        return d[0][m];
    }

    @Override
    public double compute(double[] a, double[] b, int n, double cutOffValue) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }
}
