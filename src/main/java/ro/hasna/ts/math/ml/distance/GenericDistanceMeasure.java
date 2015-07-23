package ro.hasna.ts.math.ml.distance;

/**
 * Interface for generic distance measures of n-dimensional vectors.
 *
 * @since 1.0
 */
public interface GenericDistanceMeasure<T> {
    /**
     * Compute the distance between two n-dimensional vectors.
     * <p>
     * The two vectors are required to have the same dimension.
     *
     * @param a the first vector
     * @param b the second vector
     * @return the distance between the two vectors
     */
    double compute(T a, T b);

    /**
     * Compute the distance between two n-dimensional vectors.
     * <p>
     * The two vectors are required to have the same dimension.
     *
     * @param a      the first vector
     * @param b      the second vector
     * @param cutoff if the distance being calculated is above this value stop computing the remaining distance
     * @return the distance between the two vectors
     */
    double compute(T a, T b, double cutoff);
}
