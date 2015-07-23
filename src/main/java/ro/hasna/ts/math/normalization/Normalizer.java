package ro.hasna.ts.math.normalization;

import java.io.Serializable;

/**
 * Interface for all normalization algorithms.
 *
 * @since 1.0
 */
public interface Normalizer extends Serializable {
    /**
     * Normalize the sequence of values.
     * NOTE: The length of the output array should be equal to the input array.
     *
     * @param values the sequence of values
     * @return the normalized sequence
     */
    double[] normalize(double[] values);
}
