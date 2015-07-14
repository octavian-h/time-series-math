package ro.hasna.ts.math.exception;

import org.apache.commons.math3.exception.MathIllegalNumberException;
import org.apache.commons.math3.exception.util.Localizable;
import ro.hasna.ts.math.exception.util.TimeSeriesLocalizedFormats;

/**
 * Exception to be thrown when a number is not a factor of numerator.
 *
 * @since 1.0
 */
public class NotAFactorNumberException extends MathIllegalNumberException {
    /**
     * The numerator.
     */
    private final Integer numerator;

    /**
     * Construct the exception.
     *
     * @param wrong     Value that is not divisible with numerator.
     * @param numerator The numerator.
     */
    public NotAFactorNumberException(Number wrong,
                                     Integer numerator) {
        this(TimeSeriesLocalizedFormats.NUMBER_NOT_A_FACTOR, wrong, numerator);
    }

    /**
     * Construct the exception with a specific context.
     *
     * @param specific  Specific context pattern.
     * @param wrong     Value that is not divisible with numerator.
     * @param numerator The numerator.
     */
    public NotAFactorNumberException(Localizable specific,
                                     Number wrong,
                                     Integer numerator) {
        super(specific, wrong, numerator);
        this.numerator = numerator;
    }

    /**
     * @return the numerator
     */
    public Integer getNumerator() {
        return numerator;
    }
}
