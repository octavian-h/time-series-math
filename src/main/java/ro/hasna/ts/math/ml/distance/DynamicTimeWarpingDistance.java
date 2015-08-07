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
import ro.hasna.ts.math.normalization.Normalizer;
import ro.hasna.ts.math.normalization.ZNormalizer;

import java.util.Comparator;
import java.util.PriorityQueue;

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
    private final Normalizer normalizer;

    public DynamicTimeWarpingDistance(int radius) {
        this(radius, new ZNormalizer());
    }

    /**
     * Creates a new instance of this class with
     *
     * @param radius     Sakoe-Chiba Band width used to constraint the warping window
     * @param normalizer the normalizer (it can be null if the values were normalized)
     */
    public DynamicTimeWarpingDistance(int radius, Normalizer normalizer) {
        this.radius = radius;
        this.normalizer = normalizer;
    }

    @Override
    public double compute(double[] a, double[] b) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(double[] a, double[] b, double cutOffValue) {
        if (normalizer != null) {
            a = normalizer.normalize(a);
            b = normalizer.normalize(b);
        }
        double transformedCutoff = cutOffValue * cutOffValue;

        if (computeModifiedKimLowerBound(a, b, transformedCutoff) == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }

        if (computeKeoghLowerBound(a, b, transformedCutoff) == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }

        return computeFullDtw(a, b, cutOffValue);
    }

    /**
     * <p>
     * Reference:
     * Daniel Lemire (2008)
     * <i>Faster Sequential Search with a Two-Pass Dynamic-Time-Warping Lower Bound</i>
     * </p>
     *
     * @return LB_Improved value
     */
    protected double computeLemireLowerBound() {
        //TODO
        return 0;
    }

    /**
     * <p>
     * Reference:
     * Eamonn Keogh and Chotirat Ann Ratanamahatana (2004)
     * <i>Exact indexing of dynamic time warping</i>
     * </p>
     *
     * @return LB_Keogh value
     */
    protected double computeKeoghLowerBound(double[] a, double[] b, double transformedCutoff) {
        int n = a.length;
        double d = 0;
        PriorityQueue<Double> minQueue = new PriorityQueue<>(radius);
        PriorityQueue<Double> maxQueue = new PriorityQueue<>(radius, new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return o2.compareTo(o1);
            }
        });
        for (int i = 0; i < radius && i < n; i++) {
            minQueue.add(a[i]);
            maxQueue.add(a[i]);
        }
        for (int i = 0; i < n; i++) {
            double min = minQueue.peek();
            double max = maxQueue.peek();
            if (b[i] > max) {
                d += (b[i] - max) * (b[i] - max);
            } else if (b[i] < min) {
                d += (b[i] - min) * (b[i] - min);
            }

            if (d > transformedCutoff) {
                return Double.POSITIVE_INFINITY;
            }

            minQueue.remove(a[i]);
            maxQueue.remove(a[i]);
            int j = i + radius;
            if (j < n) {
                minQueue.add(a[j]);
                maxQueue.add(a[j]);
            }
        }

        return FastMath.sqrt(d);
    }

    /**
     * <p>
     * Reference:
     * Kim Sang-Wook, Sanghyun Park and Wesley W. Chu (2001)
     * <i>An index-based approach for similarity search supporting time warping in large sequence databases</i>
     * </p>
     * <p>
     * Reference:
     * Rakthanmanon Thanawin, et al. (2013)
     * <i>Addressing big data time series: Mining trillions of time series subsequences under dynamic time warping</i>
     * </p>
     *
     * @return modified LB_Kim value
     */
    protected double computeModifiedKimLowerBound(double[] a, double[] b, double transformedCutoff) {
        int n = a.length;
        double d = (a[0] - b[0]) * (a[0] - b[0]) + (a[n - 1] - b[n - 1]) * (a[n - 1] - b[n - 1]);
        if (d > transformedCutoff) {
            return Double.POSITIVE_INFINITY;
        }

        return FastMath.sqrt(d);
    }

    protected double computeFullDtw(double[] a, double[] b, double transformedCutoff) {
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
                double aux = a[i - 1] - b[j - 1];
                d[1][j] = aux * aux + FastMath.min(d[0][j], FastMath.min(d[1][j - 1], d[0][j - 1]));
                if (d[1][j] > transformedCutoff) {
                    return Double.POSITIVE_INFINITY;
                }
            }
            for (int j = 0; j <= m; j++) {
                d[0][j] = d[1][j];
                d[1][j] = Double.POSITIVE_INFINITY;
            }
        }

        return FastMath.sqrt(d[0][m]);
    }

    @Override
    public double compute(double[] a, double[] b, int n, double cutOffValue) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }
}
