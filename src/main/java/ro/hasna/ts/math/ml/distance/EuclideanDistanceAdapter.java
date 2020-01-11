/*
 * Copyright 2015 Octavian Hasna
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

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.util.FastMath;

/**
 * Adapter class for {@link EuclideanDistance} that implements {@link GenericDistanceMeasure}.
 *
 * @since 0.9
 */
public class EuclideanDistanceAdapter extends EuclideanDistance implements GenericDistanceMeasure<double[]> {
    private static final long serialVersionUID = 2803606298141242772L;

    @Override
    public double compute(double[] a, double[] b, double cutOffValue) {
        double sum = 0.0;
        double transformedCutoff = cutOffValue * cutOffValue;
        for (int i = 0; i < a.length; i++) {
            double aux = a[i] - b[i];
            sum += aux * aux;
            if (sum >= transformedCutoff) {
                return Double.POSITIVE_INFINITY;
            }
        }
        return FastMath.sqrt(sum);
    }
}
