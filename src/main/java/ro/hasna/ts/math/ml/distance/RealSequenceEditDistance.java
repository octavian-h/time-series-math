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
 * Chen Lei (2005)
 * <i>Similarity search over time series and trajectory data</i>
 * </p>
 *
 * @since 0.10
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
        if (equals(a, b)) return 0;

        int n = b.length;
        int radius = (int) (n * radiusPercentage);
        int n1 = n + 1;
        double[] prev = new double[n1];
        double[] current = new double[n1];
        int start, end;
        double min, x, y, z;

        for (int i = 0; i < n1; i++) {
            prev[i] = i;
        }

        for (int i = 0; i < a.length; i++) {
            start = FastMath.max(0, i - radius);
            end = FastMath.min(n - 1, i + radius);

            current[0] = i + 1;
            min = Double.POSITIVE_INFINITY;

            for (int j = start; j <= end; j++) {
                x = prev[j + 1] + 1;
                y = current[j] + 1;
                z = prev[j];

                if (!Precision.equals(a[i], b[j], epsilon)) {
                    z++;
                }

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

    private boolean equals(double[] a, double[] b) {
        boolean equals = a.length == b.length;
        for (int i = 0; i < a.length && equals; i++) {
            if (!Precision.equals(a[i], b[i], epsilon)) {
                equals = false;
            }
        }
        return equals;
    }
}
