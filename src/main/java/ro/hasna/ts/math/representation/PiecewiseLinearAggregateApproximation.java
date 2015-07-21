package ro.hasna.ts.math.representation;

import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import ro.hasna.ts.math.exception.ArrayLengthIsNotDivisibleException;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.exception.UnsupportedStrategyException;
import ro.hasna.ts.math.representation.util.SegmentationStrategy;
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
public class PiecewiseLinearAggregateApproximation {
    private final int segments;
    private final SegmentationStrategy strategy;

    /**
     * Creates a new instance of this class with default strategy.
     *
     * @param segments the number of segments
     */
    public PiecewiseLinearAggregateApproximation(int segments) {
        this(segments, SegmentationStrategy.STRICT);
    }

    /**
     * Creates a new instance of this class with a given strategy.
     *
     * @param segments the number of segments
     * @param strategy the type of strategy to be applied to the sequence
     * @throws UnsupportedStrategyException is strategy is different than STRICT and IGNORE_REMAINING
     */
    public PiecewiseLinearAggregateApproximation(int segments, SegmentationStrategy strategy) {
        if (strategy != SegmentationStrategy.STRICT && strategy != SegmentationStrategy.IGNORE_REMAINING) {
            throw new UnsupportedStrategyException(strategy.name(), "PLAA");
        }
        this.strategy = strategy;
        this.segments = segments;
    }

    /**
     * Transform a given sequence of values using the algorithm PLAA.
     *
     * @param values the sequence of values
     * @return the result of the transformation
     */
    public MeanSlopePair[] transformToMeanSlopePairArray(double[] values) {
        int len = values.length;
        if (len < segments) {
            throw new ArrayLengthIsTooSmallException(len, segments, true);
        }

        int modulo = len % segments;
        if (modulo != 0 && strategy == SegmentationStrategy.STRICT) {
            throw new ArrayLengthIsNotDivisibleException(len, segments);
        }

        MeanSlopePair[] reducedValues = new MeanSlopePair[segments];
        int intervalSize = len / segments;

        double[] x = new double[intervalSize];
        for (int i = 0; i < intervalSize; i++) {
            x[i] = i + 1;
        }

        double variance = new Variance(true).evaluate(x);
        for (int i = 0; i < segments; i++) {
            double[] y = new double[intervalSize];
            System.arraycopy(values, i * intervalSize, y, 0, intervalSize);

            double covariance = new Covariance().covariance(x, y, true);
            double mean = new Mean().evaluate(y);

            reducedValues[i] = new MeanSlopePair(mean, covariance / variance);
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
