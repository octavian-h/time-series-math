package ro.hasna.ts.math.ml.distance;

import java.io.Serializable;

/**
 * Interface for generic distance measures of n-dimensional vectors.
 *
 * @since 1.0
 */
public interface GenericDistanceMeasure<T> extends Serializable {
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
     * @param a           the first vector
     * @param b           the second vector
     * @param cutOffValue if the distance being calculated is above this value
     *                    stop computing the remaining distance
     * @return the distance between the two vectors
     */
    double compute(T a, T b, double cutOffValue);

    /**
     * Compute the distance between two n-dimensional vectors.
     * <p>
     * The two vectors are required to have the same dimension.
     *
     * @param a           the first vector
     * @param b           the second vector
     * @param cutOffValue if the distance being calculated is above this value
     *                    stop computing the remaining distance
     * @param n           the vector length before transformation
     * @return the distance between the two vectors
     */
    double compute(T a, T b, int n, double cutOffValue);
}
