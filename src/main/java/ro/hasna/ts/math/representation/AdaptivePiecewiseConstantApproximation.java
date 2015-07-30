package ro.hasna.ts.math.representation;

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
    private int segments;

    public AdaptivePiecewiseConstantApproximation(int segments) {
        this.segments = segments;
    }

    @Override
    public MeanLastPair[] transform(double[] values) {
        int length = values.length;
        if (length < 2) {
            throw new ArrayLengthIsTooSmallException(length, 2, true);
        }

        // create segments with two values
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

        assert first != null;

        // compute error by unifying current segment with the next segment
        int initialSegments = length / 2;
        if (initialSegments > segments) {
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

        // unify concurrent segments with minimum error
        while (initialSegments > segments) {
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

            Segment toBeDeleted = minSegment.next;
            minSegment.mean = getUnifiedMean(minSegment, toBeDeleted);
            minSegment.end = toBeDeleted.end;
            minSegment.next = toBeDeleted.next;
            if (toBeDeleted.next != null) {
                toBeDeleted.next.prev = minSegment;
            }

            // update error for the previous segment
            if (minSegment.prev != null) {
                double mean = getUnifiedMean(minSegment.prev, minSegment);
                double error = 0.0;
                for (int i = minSegment.prev.start; i < minSegment.end; i++) {
                    error += FastMath.abs(values[i] - mean);
                }
                minSegment.prev.error = error;
            }

            // update error for the minSegment
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

            initialSegments--;
        }

        MeanLastPair[] result = new MeanLastPair[initialSegments];
        int i = 0;
        while (first != null) {
            result[i] = new MeanLastPair(first.mean, first.end - 1);
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
