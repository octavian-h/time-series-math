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

import org.apache.commons.math3.util.FastMath;

/**
 * Calculates the distance between two vectors using Edit Distance with Real Penalty.
 * <p>
 * Reference:
 * Chen Lei, M. Tamer Özsu and Vincent Oria (2005)
 * <i>Robust and fast similarity search for moving object trajectories</i>
 * </p>
 *
 * @since 1.0
 */
public class RealPenaltyEditDistance implements GenericDistanceMeasure<double[]> {
    private static final long serialVersionUID = 7419503714378886631L;
    private final double gap;
    private final double radiusPercentage;

    public RealPenaltyEditDistance() {
        this(0.0, 1.0);
    }

    public RealPenaltyEditDistance(double gap, double radiusPercentage) {
        this.gap = gap;
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
        double d = computeEd(a, b, n, radius);
        if (d >= cutOffValue) {
            return Double.POSITIVE_INFINITY;
        }
        return d;
    }

    private double computeEd(double[] a, double[] b, int n, int radius) {
        double[] prev = new double[n];
        double[] current = new double[n];
        double[] aux = new double[n];
        int start, end;
        double x, y, z;

        prev[0] = distance(b[0], gap);
        aux[0] = distance(a[0], gap);
        for (int i = 1; i < n; i++) {
            prev[i] = prev[i - 1] + distance(b[i], gap);
            aux[i] = aux[i - 1] + distance(a[i], gap);
        }

        for (int i = 0; i < n; i++) {
            start = FastMath.max(0, i - radius);
            end = FastMath.min(n - 1, i + radius);
            for (int j = start; j <= end; j++) {
                x = prev[j] + distance(a[i], gap);
                y = distance(b[j], gap);
                z = distance(a[i], b[j]);
                if (j != 0) {
                    y += current[j - 1];
                    z += prev[j - 1];
                } else {
                    y += aux[i];
                    if (i != 0) {
                        z += aux[i - 1];
                    }
                }
                current[j] = FastMath.min(x, FastMath.min(y, z));
            }

            System.arraycopy(current, 0, prev, 0, n);
        }

        return current[n - 1];
    }

    @Override
    public double compute(double[] a, double[] b, int n, double cutOffValue) {
        return compute(a, b, cutOffValue);
    }

    protected double distance(double a, double b) {
        return FastMath.abs(a - b);
    }
}
