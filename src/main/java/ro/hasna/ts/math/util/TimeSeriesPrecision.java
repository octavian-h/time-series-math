package ro.hasna.ts.math.util;

import org.apache.commons.math3.util.FastMath;

/**
 * Methods and constants used for comparisons.
 *
 * @see org.apache.commons.math3.util.Precision
 * @since 1.0
 */
public class TimeSeriesPrecision {
    public static final double EPSILON = FastMath.pow(2, -30);

    /**
     * Private constructor.
     */
    private TimeSeriesPrecision() {
    }
}
