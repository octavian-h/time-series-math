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
 * @since 0.10
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
        int n = b.length;
        int radius = (int) (n * radiusPercentage);
        int n1 = n + 1;
        double[] prev = new double[n1];
        double[] current = new double[n1];
        double[] db = new double[n];
        int start, end;
        double min, da, x, y, z;

        current[0] = 0;
        prev[0] = 0;
        for (int i = 1; i < n1; i++) {
            db[i - 1] = distance(b[i - 1], gap);
            prev[i] = prev[i - 1] + db[i - 1];
        }

        for (int i = 0; i < a.length; i++) {
            start = FastMath.max(0, i - radius);
            end = FastMath.min(n - 1, i + radius);

            da = distance(a[i], gap);
            current[0] = current[0] + da;
            min = Double.POSITIVE_INFINITY;

            for (int j = start; j <= end; j++) {
                x = prev[j + 1] + da;
                y = current[j] + db[j];
                z = prev[j] + distance(a[i], b[j]);

                current[j + 1] = FastMath.min(x, FastMath.min(y, z));
                if (current[j + 1] < min) {
                    min = current[j + 1];
                }
            }

            if (min > cutOffValue) {
                return Double.POSITIVE_INFINITY;
            }

            System.arraycopy(current, 0, prev, 0, n1);
        }

        if (current[n] > cutOffValue) {
            return Double.POSITIVE_INFINITY;
        }

        return current[n];
    }

    protected double distance(double a, double b) {
        return FastMath.abs(a - b);
    }
}
