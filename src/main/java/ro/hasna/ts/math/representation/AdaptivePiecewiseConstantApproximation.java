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
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.type.MeanLastPair;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

import java.util.Set;
import java.util.TreeSet;

/**
 * Implements the Adaptive Piecewise Constant Approximation (APCA) algorithm.
 * <p>
 * Reference:
 * Chakrabarti K., Keogh E., Mehrotra S., Pazzani M. (2002)
 * <i>Locally Adaptive Dimensionality Reduction for Indexing Large Time Series Databases</i>
 * </p>
 *
 * @since 0.8
 */
public class AdaptivePiecewiseConstantApproximation implements GenericTransformer<double[], MeanLastPair[]> {
    private static final long serialVersionUID = 5071554004881637993L;
    private final int segments;
    private final boolean approximateError;

    /**
     * Creates a new instance of this class with approximation enabled.
     *
     * @param segments the number of segments
     * @throws NumberIsTooSmallException if segments lower than 1
     */
    public AdaptivePiecewiseConstantApproximation(int segments) {
        this(segments, true);
    }

    /**
     * Creates a new instance of this class with a flag for using approximation.
     *
     * @param segments         the number of segments
     * @param approximateError compute the error of unified segments using approximation
     * @throws NumberIsTooSmallException if segments lower than 1
     */
    public AdaptivePiecewiseConstantApproximation(int segments, boolean approximateError) {
        if (segments < 1) {
            throw new NumberIsTooSmallException(segments, 1, true);
        }

        this.segments = segments;
        this.approximateError = approximateError;
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
            TreeSet<Segment> set = createSegmentsSet(values, first);


            // unify consecutive segments with minimum error
            while (numberOfSegments > segments) {
                Segment minSegment = set.pollFirst();
                minSegment.mean = getUnifiedMean(minSegment, minSegment.next);
                minSegment.error = getUnifiedError(minSegment, minSegment.next, values, minSegment.mean);
                minSegment.end = minSegment.next.end;

                deleteSubsequentSegment(minSegment, set);

                if (minSegment.next != null) {
                    double mean = getUnifiedMean(minSegment, minSegment.next);
                    minSegment.errorWithNext = getUnifiedError(minSegment, minSegment.next, values, mean);
                    set.add(minSegment);
                }

                if (minSegment.prev != null) {
                    set.remove(minSegment.prev);

                    double mean = getUnifiedMean(minSegment.prev, minSegment);
                    minSegment.prev.errorWithNext = getUnifiedError(minSegment.prev, minSegment, values, mean);

                    set.add(minSegment.prev);
                }

                numberOfSegments--;
            }
        }

        return getMeanLastPairs(first, numberOfSegments);
    }

    private Segment createSegments(double[] values, int length) {
        Segment first = null, last = null;
        for (int i = 0; i < length - 1; i += 2) {
            double mean = (values[i] + values[i + 1]) / 2;
            Segment segment = new Segment(i, i + 2, mean, 2 * FastMath.abs(values[i] - mean));
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

    private TreeSet<Segment> createSegmentsSet(double[] values, Segment first) {
        TreeSet<Segment> map = new TreeSet<>((s1, s2) ->
                Precision.compareTo(s1.errorWithNext, s2.errorWithNext, TimeSeriesPrecision.EPSILON));

        Segment current = first;
        while (current.next != null) {
            double mean = getUnifiedMean(current, current.next);
            current.errorWithNext = getUnifiedError(current, current.next, values, mean);
            map.add(current);
            current = current.next;
        }
        return map;
    }

    private void deleteSubsequentSegment(Segment segment, Set<Segment> set) {
        Segment toBeDeleted = segment.next;
        segment.next = toBeDeleted.next;
        if (toBeDeleted.next != null) {
            toBeDeleted.next.prev = segment;
        }
        set.remove(toBeDeleted);
    }

    private MeanLastPair[] getMeanLastPairs(Segment segment, int numberOfSegments) {
        MeanLastPair[] result = new MeanLastPair[numberOfSegments];
        for (int i = 0; i < numberOfSegments && segment != null; i++) {
            result[i] = new MeanLastPair(segment.mean, segment.end);
            segment = segment.next;
        }
        return result;
    }

    private double getUnifiedApproximatedError(Segment first, Segment second, double mean) {
        return first.error + second.error + 2 * FastMath.abs(first.mean - mean) * (first.end - first.start);
    }

    private double getUnifiedError(Segment first, Segment second, double[] values, double mean) {
        if (Precision.equals(mean, first.mean, TimeSeriesPrecision.EPSILON)) {
            return first.error + second.error;
        }

        if (approximateError) {
            return getUnifiedApproximatedError(first, second, mean);
        }

        double error = 0.0;
        for (int i = first.start; i < second.end; i++) {
            error += FastMath.abs(values[i] - mean);
        }
        return error;
    }

    private double getUnifiedMean(Segment first, Segment second) {
        return (first.mean * (first.end - first.start) + second.mean * (second.end - second.start))
                / (second.end - first.start);
    }

    private static class Segment {
        int start; //inclusive
        int end; //exclusive
        double mean;
        double error;
        double errorWithNext;
        Segment next;
        Segment prev;

        Segment(int start, int end, double mean, double error) {
            this.start = start;
            this.end = end;
            this.mean = mean;
            this.error = error;
            this.errorWithNext = Double.POSITIVE_INFINITY;
        }
    }
}
