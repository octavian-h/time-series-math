package ro.hasna.ts.math.exception;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.Localizable;

/**
 * @author Octavian
 * @since 15.07.2015
 */
public class ArrayLengthIsTooSmallException extends NumberIsTooSmallException {
    /**
     * Construct the exception.
     *
     * @param wrong          Value that is smaller than the minimum.
     * @param min            Minimum.
     * @param boundIsAllowed Whether {@code min} is included in the allowed range.
     */
    public ArrayLengthIsTooSmallException(Number wrong, Number min, boolean boundIsAllowed) {
        super(wrong, min, boundIsAllowed);
    }

    /**
     * Construct the exception with a specific context.
     *
     * @param specific       Specific context pattern.
     * @param wrong          Value that is smaller than the minimum.
     * @param min            Minimum.
     * @param boundIsAllowed Whether {@code min} is included in the allowed range.
     */
    public ArrayLengthIsTooSmallException(Localizable specific, Number wrong, Number min, boolean boundIsAllowed) {
        super(specific, wrong, min, boundIsAllowed);
    }
}
