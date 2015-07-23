package ro.hasna.ts.math.representation;

import java.io.Serializable;

/**
 * Interface for generic vector transformation.
 *
 * @since 1.0
 */
public interface GenericTransformer<I, O> extends Serializable {
    /**
     * Transform the input vector from type I into type O.
     *
     * @param input the input vector
     * @return the output vector
     */
    O transform(I input);
}
