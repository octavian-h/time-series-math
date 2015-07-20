package ro.hasna.ts.math.exception;

import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import ro.hasna.ts.math.exception.util.TimeSeriesLocalizedFormats;

/**
 * Exception to be thrown when a strategy is invalid for a given algorithm.
 *
 * @since 1.0
 */
public class UnsupportedStrategyException extends MathUnsupportedOperationException {
    private String strategy;
    private String algorithm;

    /**
     * Default constructor.
     */
    public UnsupportedStrategyException(String strategy, String algorithm) {
        super(TimeSeriesLocalizedFormats.UNSUPPORTED_STRATEGY, strategy, algorithm);
        this.strategy = strategy;
        this.algorithm = algorithm;
    }

    public String getStrategy() {
        return strategy;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}
