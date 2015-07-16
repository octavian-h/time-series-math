package ro.hasna.ts.math.exception;

import org.apache.commons.math3.exception.util.Localizable;

/**
 * @since 1.0
 */
public class ArrayLengthIsNotDivisibleException extends NumberIsNotDivisibleException {
    /**
     * Construct the exception.
     *
     * @param wrong  Value that is not divisible with the factor.
     * @param factor The factor.
     */
    public ArrayLengthIsNotDivisibleException(Number wrong, Integer factor) {
        super(wrong, factor);
    }

    /**
     * Construct the exception with a specific context.
     *
     * @param specific Specific context pattern.
     * @param wrong    Value that is not divisible with the factor.
     * @param factor   The factor.
     */
    public ArrayLengthIsNotDivisibleException(Localizable specific, Number wrong, Integer factor) {
        super(specific, wrong, factor);
    }
}
