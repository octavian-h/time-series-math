/**
 * Copyright (C) 2015 Octavian Hasna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
