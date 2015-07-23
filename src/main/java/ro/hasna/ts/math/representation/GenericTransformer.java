package ro.hasna.ts.math.representation;

/**
 * Interface for generic vector transformation.
 *
 * @since 1.0
 */
public interface GenericTransformer<I, O> {
    /**
     * Transform the input vector from type I into type O.
     *
     * @param input the input vector
     * @return the output vector
     */
    O transform(I input);
}
