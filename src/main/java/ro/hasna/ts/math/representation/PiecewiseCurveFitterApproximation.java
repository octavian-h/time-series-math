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
package ro.hasna.ts.math.representation;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.fitting.AbstractCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.util.Precision;
import ro.hasna.ts.math.exception.ArrayLengthIsNotDivisibleException;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.representation.util.SegmentationStrategy;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements Piecewise Approximation using a curve fitting function (ex: linear = PLA, quadratic = PQA etc.).
 * An optimised implementation for the mean function is the class {@code PiecewiseAggregateApproximation}.
 *
 * @since 1.0
 */
public class PiecewiseCurveFitterApproximation implements GenericTransformer<double[], double[][]> {
    private static final long serialVersionUID = -410430777798956046L;
    private final int segments;
    private final SegmentationStrategy strategy;
    private final AbstractCurveFitter curveFitter;

    /**
     * Creates a new instance of this class with default strategy.
     *
     * @param segments    the number of segments
     * @param curveFitter the curve fitting function
     */
    public PiecewiseCurveFitterApproximation(int segments, AbstractCurveFitter curveFitter) {
        this(segments, SegmentationStrategy.STRICT, curveFitter);
    }

    /**
     * Creates a new instance of this class with a given strategy.
     *
     * @param segments    the number of segments
     * @param strategy    the type of strategy to be applied to the sequence
     * @param curveFitter the curve fitting function
     * @throws NumberIsTooSmallException if segments lower than 1
     */
    public PiecewiseCurveFitterApproximation(int segments, SegmentationStrategy strategy, AbstractCurveFitter curveFitter) {
        if (segments < 1) {
            throw new NumberIsTooSmallException(segments, 1, true);
        }

        this.segments = segments;
        this.strategy = strategy;
        this.curveFitter = curveFitter;
    }

    @Override
    public double[][] transform(double[] values) {
        int len = values.length;
        if (len < segments) {
            throw new ArrayLengthIsTooSmallException(len, segments, true);
        }

        int modulo = len % segments;
        if (modulo != 0 && strategy == SegmentationStrategy.STRICT) {
            throw new ArrayLengthIsNotDivisibleException(len, segments);
        }

        double[][] reducedValues = new double[segments][];
        if (modulo == 0 || strategy == SegmentationStrategy.IGNORE_REMAINING) {
            int segmentSize = len / segments;
            List<WeightedObservedPoint> segment = new ArrayList<>(segmentSize);
            for (int i = 0; i < segmentSize; i++) {
                segment.add(null);
            }

            int n = 0;
            for (int i = 0; i < len; i++) {
                int index = i % segmentSize;
                segment.set(index, new WeightedObservedPoint(1, index, values[i]));
                if (index + 1 == segmentSize) {
                    reducedValues[n++] = curveFitter.fit(segment);
                    if (n == segments) break;
                }
            }

        } else if (strategy == SegmentationStrategy.FRACTIONAL_PARTITION) {
            double segmentSize = len * 1.0 / segments;
            int maxSegmentSize = (int) segmentSize + 2;
            int i = 0;
            double x = 1.0;
            int y = (int) (segmentSize - 1);
            double z = segmentSize - 1 - y;
            int n = 0;
            while (i < len) {
                int m = 0;
                List<WeightedObservedPoint> segment = new ArrayList<>(maxSegmentSize);

                if (!Precision.equals(x, 0, TimeSeriesPrecision.EPSILON)) {
                    segment.add(new WeightedObservedPoint(x, m++, values[i]));
                }
                int k = i + 1 + y;
                for (int j = i + 1; j < k; j++) {
                    segment.add(new WeightedObservedPoint(1, m++, values[j]));
                }
                if (!Precision.equals(z, 0, TimeSeriesPrecision.EPSILON)) {
                    segment.add(new WeightedObservedPoint(z, m, values[k]));
                }

                reducedValues[n++] = curveFitter.fit(segment);
                if (n == segments) break;

                x = 1 - z;
                y = (int) (segmentSize - x);
                z = segmentSize - x - y;

                i = k;
            }
        }

        return reducedValues;
    }

    public int getSegments() {
        return segments;
    }
}
