package ro.hasna.ts.math.representation;

import org.apache.commons.math3.util.FastMath;
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
        Segment first = null, last = null;
        int length = values.length / 2;
        for (int i = 0; i < values.length - 1; i += 2) {
            double mean = (values[i] + values[i + 1]) / 2;
            Segment segment = new Segment(null, i, i + 2, mean, null);
            if (first == null) {
                first = segment;
                last = first;
            } else {
                last.next = segment;
                segment.prev = last;
                last = last.next;
            }
        }

        while (length > segments) {
            Segment minSegment = null;
            double minError = Double.POSITIVE_INFINITY;

            Segment current = first;
            while (current.next != null) {
                double mean = (current.mean * (current.end - current.start) +
                        current.next.mean * (current.next.end - current.next.start)) /
                        (current.next.end - current.start);
                // TODO use some approximation
                double error = 0.0;
                for (int i = current.start; i < current.next.end; i++) {
                    error += FastMath.abs(values[i] - mean);
                }
                if (error < minError) {
                    minError = error;
                    minSegment = current;
                }
                if (minError == 0) {
                    break;
                }
                current = current.next;
            }

            minSegment.mean = (minSegment.mean * (minSegment.end - minSegment.start) +
                    minSegment.next.mean * (minSegment.next.end - minSegment.next.start)) /
                    (minSegment.next.end - minSegment.start);
            minSegment.end = minSegment.next.end;

            Segment toBeDeleted = minSegment.next;
            minSegment.next = toBeDeleted.next;
            if (toBeDeleted.next != null) {
                toBeDeleted.next.prev = minSegment;
            }
            length--;
        }

        MeanLastPair[] result = new MeanLastPair[length];
        int i = 0;
        while (first != null) {
            result[i] = new MeanLastPair(first.mean, first.end - 1);
            first = first.next;
            i++;
        }

        return result;
    }

    private static class Segment {
        int start; //inclusive
        int end; //exclusive
        double mean;
        Segment next;
        Segment prev;

        Segment(Segment prev, int start, int end, double mean, Segment next) {
            this.start = start;
            this.end = end;
            this.mean = mean;
            this.next = next;
            this.prev = prev;
        }
    }
}
