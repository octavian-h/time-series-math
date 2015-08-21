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

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * Calculates the distance between two vectors using Uniform Scaling.
 * <p>
 * Reference:
 * Yankov Dragomir, Eamonn Keogh, Jose Medina, Bill Chiu and Victor Zordan (2007)
 * <i>Detecting time series motifs under uniform scaling</i>
 * </p>
 *
 * @since 1.0
 */
public class UniformScalingDistance implements GenericDistanceMeasure<double[]> {
    private static final long serialVersionUID = -596315329786672046L;
    private final double minScalingFactor;
    private final double maxScalingFactor;
    private final int steps;
    private final GenericDistanceMeasure<double[]> distance;

    public UniformScalingDistance(double minScalingFactor, double maxScalingFactor, int steps) {
        this(minScalingFactor, maxScalingFactor, steps, new EuclideanDistanceAdapter());
    }

    public UniformScalingDistance(double minScalingFactor, double maxScalingFactor, int steps, GenericDistanceMeasure<double[]> distance) {
        if (minScalingFactor <= 0) {
            throw new NumberIsTooSmallException(minScalingFactor, 0, false);
        }
        this.minScalingFactor = minScalingFactor;

        if (maxScalingFactor < minScalingFactor) {
            throw new NumberIsTooSmallException(maxScalingFactor, minScalingFactor, true);
        }
        this.maxScalingFactor = maxScalingFactor;

        if (steps < 1) {
            throw new NumberIsTooSmallException(steps, 1, true);
        }
        this.steps = steps;

        this.distance = distance;
    }

    @Override
    public double compute(double[] a, double[] b) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(double[] a, double[] b, double cutOffValue) {
        double min = Double.POSITIVE_INFINITY;
        double[] aux = new double[a.length];
        if (steps == 1) {
            double scalingFactor = (minScalingFactor + maxScalingFactor) / 2;
            return computeDistance(a, b, aux, scalingFactor, cutOffValue);
        }

        double interval = (maxScalingFactor - minScalingFactor) / (steps - 1);
        if (Precision.equals(interval, 0.0, TimeSeriesPrecision.EPSILON)) {
            return computeDistance(a, b, aux, minScalingFactor, cutOffValue);
        }

        for (double i = minScalingFactor; i <= maxScalingFactor; i += interval) {
            double d = computeDistance(a, b, aux, i, cutOffValue);
            if (d < min) {
                min = d;
                if (Precision.equals(min, 0.0, TimeSeriesPrecision.EPSILON)) {
                    return min;
                }
            }
        }
        return min;
    }

    private double computeDistance(double[] a, double[] b, double[] aux, double scalingFactor, double cutOffValue) {
        if (Precision.equals(scalingFactor, 1.0, TimeSeriesPrecision.EPSILON)) {
            return distance.compute(a, b, cutOffValue);
        }

        if (scalingFactor > 1) {
            return computeDistance(b, a, aux, 1 / scalingFactor, cutOffValue);
        }

        int n = a.length;
        double j;
        int k;
        aux[0] = b[0];
        for (int i = 1; i < n; i++) {
            j = i * scalingFactor;
            k = (int) j;
            if (Precision.equals(j, k, TimeSeriesPrecision.EPSILON)) {
                aux[i] = b[k];
            } else {
                // linear interpolation between (k,b[k]) and (k+1,b[k+1])
                aux[i] = (b[k + 1] - b[k]) * j + (k + 1) * b[k] - k * b[k + 1];
            }
        }

        return distance.compute(a, aux, cutOffValue);
    }

    @Override
    public double compute(double[] a, double[] b, int n, double cutOffValue) {
        return compute(a, b, cutOffValue);
    }
}
