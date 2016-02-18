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
package ro.hasna.ts.math.representation;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import ro.hasna.ts.math.exception.ArrayLengthIsNotDivisibleException;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.type.MeanSlopePair;

/**
 * Implements the Piecewise Linear Aggregate Approximation (PLAA) algorithm.
 * <p>
 * Reference:
 * Hung N. Q. V., Anh D. T. (2008)
 * <i>An Improvement of PAA for Dimensionality Reduction in Large Time Series Databases</i>
 * </p>
 *
 * @since 1.0
 */
public class PiecewiseLinearAggregateApproximation implements GenericTransformer<double[], MeanSlopePair[]> {
    private static final long serialVersionUID = -4073250977010141095L;
    private final int segments;

    /**
     * Creates a new instance of this class.
     *
     * @param segments the number of segments
     * @throws NumberIsTooSmallException if segments lower than 1
     */
    public PiecewiseLinearAggregateApproximation(int segments) {
        if (segments < 1) {
            throw new NumberIsTooSmallException(segments, 1, true);
        }

        this.segments = segments;
    }

    /**
     * Transform a given sequence of values using the algorithm PLAA.
     *
     * @param values the sequence of values
     * @return the result of the transformation
     */
    public MeanSlopePair[] transform(double[] values) {
        int len = values.length;
        if (len < segments) {
            throw new ArrayLengthIsTooSmallException(len, segments, true);
        }

        int modulo = len % segments;
        if (modulo != 0) {
            throw new ArrayLengthIsNotDivisibleException(len, segments);
        }

        MeanSlopePair[] reducedValues = new MeanSlopePair[segments];
        int segmentSize = len / segments;

        double[] x = new double[segmentSize];
        for (int i = 0; i < segmentSize; i++) {
            x[i] = i + 1;
        }

        double variance = new Variance(true).evaluate(x);
        for (int i = 0; i < segments; i++) {
            double[] y = new double[segmentSize];
            System.arraycopy(values, i * segmentSize, y, 0, segmentSize);

            double covariance = new Covariance().covariance(x, y, true);
            double mean = new Mean().evaluate(y);

            reducedValues[i] = new MeanSlopePair(mean, covariance / variance);
        }

        return reducedValues;
    }
}
