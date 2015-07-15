package ro.hasna.ts.math.exception;

import org.apache.commons.math3.exception.MathIllegalNumberException;
import org.apache.commons.math3.exception.util.Localizable;
import ro.hasna.ts.math.exception.util.TimeSeriesLocalizedFormats;

/**
 * Exception to be thrown when a number is not a factor of factor.
 *
 * @since 1.0
 */
public class NumberIsNotDivisibleException extends MathIllegalNumberException {
    /**
     * The factor for the number.
     */
    private final Integer factor;

    /**
     * Construct the exception.
     *
     * @param wrong  Value that is not divisible with the factor.
     * @param factor The factor.
     */
    public NumberIsNotDivisibleException(Number wrong,
                                         Integer factor) {
        this(TimeSeriesLocalizedFormats.NUMBER_NOT_DIVISIBLE_WITH, wrong, factor);
    }

    /**
     * Construct the exception with a specific context.
     *
     * @param specific Specific context pattern.
     * @param wrong    Value that is not divisible with the factor.
     * @param factor   The factor.
     */
    public NumberIsNotDivisibleException(Localizable specific,
                                         Number wrong,
                                         Integer factor) {
        super(specific, wrong, factor);
        this.factor = factor;
    }

    /**
     * @return the factor
     */
    public Integer getFactor() {
        return factor;
    }
}
