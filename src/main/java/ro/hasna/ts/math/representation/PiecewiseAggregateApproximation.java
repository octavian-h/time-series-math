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
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;

/**
 * Implements the Piecewise Aggregate Approximation (PAA) algorithm.
 * <p>
 * Reference:
 * Keogh E., Chakrabarti K., Pazzani M., Mehrotra S. (2001)
 * <i>Locally Adaptive Dimensionality Reduction for Indexing Large Time Series Databases </i>
 * </p>
 *
 * @since 1.0
 */
public class PiecewiseAggregateApproximation implements GenericTransformer<double[], double[]> {
    private static final long serialVersionUID = -8199587096227874425L;
    private final int segments;

    /**
     * Creates a new instance of this class.
     *
     * @param segments the number of segments
     * @throws NumberIsTooSmallException if segments lower than 1
     */
    public PiecewiseAggregateApproximation(int segments) {
        if (segments < 1) {
            throw new NumberIsTooSmallException(segments, 1, true);
        }

        this.segments = segments;
    }

    @Override
    public double[] transform(double[] values) {
        int len = values.length;
        if (len < segments) {
            throw new ArrayLengthIsTooSmallException(len, segments, true);
        }

        int modulo = len % segments;
        double[] reducedValues = new double[segments];
        if (modulo == 0) {
            int segmentSize = len / segments;
            double sum = 0;
            int n = 0;
            for (int i = 0; i < len; i++) {
                sum += values[i];
                if ((i + 1) % segmentSize == 0) {
                    reducedValues[n++] = sum / segmentSize;
                    if (n == segments) break;
                    sum = 0;
                }
            }
        } else {
            double segmentSize = len * 1.0 / segments;
            int k = 0;
            double sum = 0;
            for (int i = 0; i < segments - 1; i++) {
                double x = (i + 1) * segmentSize - 1;

                for (; k < x; k++) {
                    sum += values[k];
                }

                double delta = x - (int) x;
                sum += delta * values[k];
                reducedValues[i] = sum / segmentSize;

                sum = (1 - delta) * values[k];
                k++;
            }

            for (; k < len; k++) {
                sum += values[k];
            }
            reducedValues[segments - 1] = sum / segmentSize;
        }

        return reducedValues;
    }

    public int getSegments() {
        return segments;
    }
}
