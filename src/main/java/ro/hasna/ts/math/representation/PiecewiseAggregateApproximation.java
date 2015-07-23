package ro.hasna.ts.math.representation;

import org.apache.commons.math3.util.Precision;
import ro.hasna.ts.math.exception.ArrayLengthIsNotDivisibleException;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.representation.util.SegmentationStrategy;
import ro.hasna.ts.math.type.SaxPair;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

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
public class PiecewiseAggregateApproximation implements GenericTransformer<double[], double[]>{
    private final int segments;
    private final SegmentationStrategy strategy;

    /**
     * Creates a new instance of this class with default strategy.
     *
     * @param segments the number of segments
     */
    public PiecewiseAggregateApproximation(int segments) {
        this(segments, SegmentationStrategy.STRICT);
    }

    /**
     * Creates a new instance of this class with a given strategy.
     *
     * @param segments the number of segments
     * @param strategy the type of strategy to be applied to the sequence
     */
    public PiecewiseAggregateApproximation(int segments, SegmentationStrategy strategy) {
        this.strategy = strategy;
        this.segments = segments;
    }

    /**
     * Transform a given sequence of values using the algorithm PAA.
     *
     * @param values the sequence of values
     * @return the result of the transformation
     */
    public double[] transform(double[] values) {
        int len = values.length;
        if (len < segments) {
            throw new ArrayLengthIsTooSmallException(len, segments, true);
        }

        int modulo = len % segments;
        if (modulo != 0 && strategy == SegmentationStrategy.STRICT) {
            throw new ArrayLengthIsNotDivisibleException(len, segments);
        }

        double[] reducedValues = new double[segments];
        if (modulo == 0 || strategy == SegmentationStrategy.IGNORE_REMAINING) {
            int intervalSize = len / segments;
            double sum = 0;
            int n = 0;
            for (int i = 0; i < len; i++) {
                sum += values[i];
                if ((i + 1) % intervalSize == 0) {
                    reducedValues[n++] = sum / intervalSize;
                    if (n == segments) break;
                    sum = 0;
                }
            }

        } else if (strategy == SegmentationStrategy.MEAN_PADDING) {
            int intervalSize = len / segments + 1;
            double sum = 0;
            int n = 0;
            for (int i = 0; i < len; i++) {
                sum += values[i];
                if ((i + 1) % intervalSize == 0) {
                    reducedValues[n++] = sum / intervalSize;
                    sum = 0;
                }
            }
            modulo = len % intervalSize;
            reducedValues[n] = sum / modulo;

        } else if (strategy == SegmentationStrategy.FRACTIONAL_PARTITION) {
            double intervalSize = len * 1.0 / segments;
            double sum = 0;
            int i = 0;
            double x = 1.0;
            int y = (int) (intervalSize - 1);
            double z = intervalSize - 1 - y;
            int n = 0;
            while (i < len) {
                if (!Precision.equals(x, 0, TimeSeriesPrecision.EPSILON)) {
                    sum += values[i] * x;
                }
                int k = i + 1 + y;
                for (int j = i + 1; j < k; j++) {
                    sum += values[j];
                }
                if (!Precision.equals(z, 0, TimeSeriesPrecision.EPSILON)) {
                    sum += values[k] * z;
                }

                reducedValues[n++] = sum / intervalSize;
                if (n == segments) break;

                sum = 0;

                x = 1 - z;
                y = (int) (intervalSize - x);
                z = intervalSize - x - y;

                i = k;
            }
        }

        return reducedValues;
    }

    public int getSegments() {
        return segments;
    }

    public SegmentationStrategy getStrategy() {
        return strategy;
    }
}
