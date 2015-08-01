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
import org.apache.commons.math3.util.FastMath;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.type.MeanLastPair;

/**
 * Implements the Adaptive Piecewise Constant Approximation (APCA) algorithm.
 * <p>
 * Reference:
 * Chakrabarti K., Keogh E., Mehrotra S., Pazzani M. (2002)
 * <i>Locally Adaptive Dimensionality Reduction for Indexing Large Time Series Databases</i>
 * </p>
 *
 * @since 1.0
 */
public class AdaptivePiecewiseConstantApproximation implements GenericTransformer<double[], MeanLastPair[]> {
    private static final long serialVersionUID = 5071554004881637993L;
    private final int segments;

    /**
     * Creates a new instance of this class.
     *
     * @param segments the number of segments
     * @throws NumberIsTooSmallException if segments < 1
     */
    public AdaptivePiecewiseConstantApproximation(int segments) {
        if (segments < 1) {
            throw new NumberIsTooSmallException(segments, 1, true);
        }

        this.segments = segments;
    }

    @Override
    public MeanLastPair[] transform(double[] values) {
        int length = values.length;
        if (length < 2 * segments) {
            throw new ArrayLengthIsTooSmallException(length, 2 * segments, true);
        }
        int numberOfSegments = length / 2;

        // create segments with two values
        Segment first = createSegments(values, length);

        if (numberOfSegments > segments) {
            // compute error by unifying current segment with the next segment
            computeRepresentationErrors(values, first);
        }

        // unify concurrent segments with minimum error
        while (numberOfSegments > segments) {
            Segment minSegment = getSegmentWithMinError(first);
            deleteSubsequentSegment(minSegment);
            updateErrorForThePreviousSegment(values, minSegment);
            updateErrorForMinSegment(values, minSegment);

            numberOfSegments--;
        }

        return getMeanLastPairs(first, numberOfSegments);
    }

    private Segment createSegments(double[] values, int length) {
        Segment first = null, last = null;
        for (int i = 0; i < length - 1; i += 2) {
            double mean = (values[i] + values[i + 1]) / 2;
            Segment segment = new Segment(i, i + 2, mean);
            if (first == null) {
                first = segment;
                last = first;
            } else {
                last.next = segment;
                segment.prev = last;
                last = last.next;
            }
        }
        return first;
    }

    private void computeRepresentationErrors(double[] values, Segment first) {
        Segment current = first;
        while (current.next != null) {
            double mean = getUnifiedMean(current, current.next);
            double error = 0.0;
            for (int i = current.start; i < current.next.end; i++) {
                error += FastMath.abs(values[i] - mean);
            }
            current.error = error;
            current = current.next;
        }
    }

    private Segment getSegmentWithMinError(Segment first) {
        Segment minSegment = first;
        Segment current = first;
        while (current.next != null) {
            if (current.error < minSegment.error) {
                minSegment = current;
            }
            if (minSegment.error == 0) {
                break;
            }
            current = current.next;
        }
        return minSegment;
    }

    private void deleteSubsequentSegment(Segment minSegment) {
        Segment toBeDeleted = minSegment.next;
        minSegment.mean = getUnifiedMean(minSegment, toBeDeleted);
        minSegment.end = toBeDeleted.end;
        minSegment.next = toBeDeleted.next;
        if (toBeDeleted.next != null) {
            toBeDeleted.next.prev = minSegment;
        }
    }

    private void updateErrorForMinSegment(double[] values, Segment minSegment) {
        if (minSegment.next != null) {
            double mean = getUnifiedMean(minSegment, minSegment.next);
            double error = 0.0;
            for (int i = minSegment.start; i < minSegment.next.end; i++) {
                error += FastMath.abs(values[i] - mean);
            }
            minSegment.error = error;
        } else {
            minSegment.error = Double.POSITIVE_INFINITY;
        }
    }

    private void updateErrorForThePreviousSegment(double[] values, Segment minSegment) {
        if (minSegment.prev != null) {
            double mean = getUnifiedMean(minSegment.prev, minSegment);
            double error = 0.0;
            for (int i = minSegment.prev.start; i < minSegment.end; i++) {
                error += FastMath.abs(values[i] - mean);
            }
            minSegment.prev.error = error;
        }
    }

    private MeanLastPair[] getMeanLastPairs(Segment first, int numberOfSegments) {
        MeanLastPair[] result = new MeanLastPair[numberOfSegments];
        int i = 0;
        while (first != null) {
            result[i] = new MeanLastPair(first.mean, first.end);
            first = first.next;
            i++;
        }
        return result;
    }

    private double getUnifiedMean(Segment first, Segment second) {
        return (first.mean * (first.end - first.start)
                + second.mean * (second.end - second.start))
                / (second.end - first.start);
    }

    private static class Segment {
        int start; //inclusive
        int end; //exclusive
        double mean;
        double error;
        Segment next;
        Segment prev;

        Segment(int start, int end, double mean) {
            this.start = start;
            this.end = end;
            this.mean = mean;
            this.error = Double.POSITIVE_INFINITY;
        }
    }
}
