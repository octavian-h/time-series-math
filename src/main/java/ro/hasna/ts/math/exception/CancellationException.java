package ro.hasna.ts.math.exception;

import org.apache.commons.math3.exception.MathIllegalStateException;
import ro.hasna.ts.math.exception.util.LocalizableMessages;

/**
 * @author Octavian
 * @since 25.06.2019
 */
public class CancellationException extends MathIllegalStateException {
    private static final long serialVersionUID = 8488577710717564366L;

    public CancellationException() {
        super(LocalizableMessages.OPERATION_WAS_CANCELLED);
    }
}
