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
package ro.hasna.ts.math.filter;

import org.apache.commons.math3.exception.DimensionMismatchException;

/**
 * Implements the moving average/mean filter.
 *
 * @since 1.0
 */
public class MovingAverageFilter implements Filter {
    private final int observations;
    private final boolean symmetric;
    private final double[] weights;

    /**
     * Creates a new instance of this class with the given number of observations to smooth from both sides.
     */
    public MovingAverageFilter(int observations) {
        this(observations, true, null);
    }

    /**
     * Creates a new instance of this class.
     *
     * @param observations the number of observations
     * @param symmetric    smooth from both sides
     * @param weights      the weights to be used for smoothing; it can be null
     * @throws DimensionMismatchException if the length of the weights is not equal to the number of observations
     *                                    or the size of the window (2 * observations + 1) if the smoothing is symmetric
     */
    public MovingAverageFilter(int observations, boolean symmetric, double[] weights) {
        this.observations = observations;
        this.symmetric = symmetric;
        if (weights != null) {
            if (!symmetric && weights.length != observations) {
                throw new DimensionMismatchException(weights.length, observations);
            }
            if (symmetric && weights.length != 2 * observations + 1) {
                throw new DimensionMismatchException(weights.length, 2 * observations + 1);
            }
        }

        this.weights = weights;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] filter(double[] values) {
        if (weights == null) {
            return filterWithoutWeights(values);
        } else {
            return filterWithWeights(values);
        }
    }

    private double[] filterWithoutWeights(double[] values) {
        int length = values.length;
        double[] result = new double[length];

        int windowSize;
        int index;
        if (symmetric) {
            windowSize = 2 * observations + 1;
            index = observations;
        } else {
            windowSize = observations;
            index = observations - 1;
        }
        // compute the sum of the first window
        double sum = 0;
        for (int i = 0; i < windowSize && i < length; i++) {
            sum += values[i];
        }

        // copy the beginning non smoothed values to the result
        System.arraycopy(values, 0, result, 0, index);

        for (int i = windowSize - 1; i < length; i++) {
            if (i != windowSize - 1) {
                sum += values[i];
                sum -= values[i - windowSize];
            }
            result[index] = sum / windowSize;
            index++;
        }
        if (symmetric) {
            // copy the end non smoothed values to the result
            System.arraycopy(values, index, result, index, length - index);
        }
        return result;
    }

    private double[] filterWithWeights(double[] values) {
        int length = values.length;
        double[] result = new double[length];

        int windowSize;
        int index;
        if (symmetric) {
            windowSize = 2 * observations + 1;
            index = observations;
        } else {
            windowSize = observations;
            index = observations - 1;
        }

        // copy the beginning non smoothed values to the result
        System.arraycopy(values, 0, result, 0, index);

        for (int i = 0; i < length - windowSize + 1; i++) {
            double sum = 0;
            for (int j = i; j < i + windowSize; j++) {
                sum += values[j] * weights[j - i];
            }

            result[index] = sum;
            index++;
        }
        if (symmetric) {
            // copy the end non smoothed values to the result
            System.arraycopy(values, index, result, index, length - index);
        }

        return result;
    }
}
