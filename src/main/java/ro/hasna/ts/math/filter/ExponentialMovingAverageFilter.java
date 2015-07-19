package ro.hasna.ts.math.filter;

import org.apache.commons.math3.exception.OutOfRangeException;
import ro.hasna.ts.math.exception.util.TimeSeriesLocalizedFormats;

/**
 * Implements the exponential moving average filter (exponential smoothing).
 *
 * @since 1.0
 */
public class ExponentialMovingAverageFilter implements Filter {
    private final double smoothingFactor;

    public ExponentialMovingAverageFilter(double smoothingFactor) {
        if (smoothingFactor <= 0 || smoothingFactor >= 1) {
            throw new OutOfRangeException(TimeSeriesLocalizedFormats.OUT_OF_RANGE_BOTH, smoothingFactor, 0, 1);
        }
        this.smoothingFactor = smoothingFactor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] filter(double[] values) {
        int length = values.length;
        double[] result = new double[length];
        result[0] = values[0] * smoothingFactor;
        double k = 1 - smoothingFactor;
        for (int i = 1; i < length; i++) {
            result[i] = values[i] * smoothingFactor + result[i - 1] * k;
        }
        return result;
    }
}
