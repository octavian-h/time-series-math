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
import org.apache.commons.math3.util.Precision;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * Calculates the distance between two vectors using Edit Distance on Real Sequence.
 * <p>
 * Reference:
 * Wagner Robert A. and Michael J. Fischer (1974)
 * <i>The string-to-string correction problem</i>
 * Chen Lei and Raymond Ng (2004)
 * <i>On the marriage of lp-norms and edit distance</i>
 * </p>
 *
 * @since 1.0
 */
public class RealSequenceEditDistance implements GenericDistanceMeasure<double[]> {
    private static final long serialVersionUID = -373272813771443967L;
    private final double epsilon;
    private final double radiusPercentage;

    public RealSequenceEditDistance() {
        this(TimeSeriesPrecision.EPSILON, 1.0);
    }

    public RealSequenceEditDistance(double epsilon, double radiusPercentage) {
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
        double d = computeEd(a, b, n, radius);
        if (d >= cutOffValue) {
            return Double.POSITIVE_INFINITY;
        }
        return d;
    }

    private double computeEd(double[] a, double[] b, int n, int radius) {
        boolean equals = true;
        for (int i = 0; i < n && equals; i++) {
            if (!Precision.equals(a[i], b[i], epsilon)) {
                equals = false;
            }
        }
        if (equals) {
            return 0;
        }

        double[] prev = new double[n];
        double[] current = new double[n];
        int start, end;
        double x, y, z;

        for (int i = 0; i < n; i++) {
            prev[i] = i + 1;
        }

        for (int i = 0; i < n; i++) {
            start = FastMath.max(0, i - radius);
            end = FastMath.min(n - 1, i + radius);
            for (int j = start; j <= end; j++) {
                if (Precision.equals(a[i], b[j], epsilon)) {
                    if (j == 0) {
                        current[j] = i;
                    } else {
                        current[j] = prev[j - 1];
                    }
                } else {
                    x = prev[j] + 1;
                    if (j == 0) {
                        y = i + 1;
                        z = i + 1;
                    } else {
                        y = current[j - 1] + 1;
                        z = prev[j - 1] + 1;
                    }
                    current[j] = FastMath.min(x, FastMath.min(y, z));
                }
            }

            System.arraycopy(current, 0, prev, 0, n);
        }

        return current[n - 1];
    }
}
