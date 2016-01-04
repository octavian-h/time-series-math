/**
 * Copyright (C) 2016-2015 Octavian Hasna
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

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.FastMath;
import ro.hasna.ts.math.representation.AdaptivePiecewiseConstantApproximation;
import ro.hasna.ts.math.type.MeanLastPair;

/**
 * Calculates the L<sub>2</sub> (Euclidean) distance between two vectors using the APCA representation.
 * NOTE: there is no guarantee that the distance satisfy the triangle inequality
 *
 * @since 1.0
 */
public class ApcaEuclideanDistance implements DistanceMeasure, GenericDistanceMeasure<MeanLastPair[]> {
    private static final long serialVersionUID = -5281044546933045617L;
    private final AdaptivePiecewiseConstantApproximation apca;

    public ApcaEuclideanDistance(AdaptivePiecewiseConstantApproximation apca) {
        this.apca = apca;
    }

    @Override
    public double compute(double[] a, double[] b) {
        MeanLastPair[] mlpA = apca.transform(a);
        MeanLastPair[] mlpB = apca.transform(b);
        int n = a.length;

        return compute(mlpA, mlpB, n, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(MeanLastPair[] a, MeanLastPair[] b) {
        return compute(a, b, a.length, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(MeanLastPair[] a, MeanLastPair[] b, double cutOffValue) {
        return compute(a, b, a.length, cutOffValue);
    }

    @Override
    public double compute(MeanLastPair[] a, MeanLastPair[] b, int n, double cutoff) {
        double sum = 0.0;
        int w = a.length;
        double transformedCutoff = cutoff * cutoff;
        int i = 0;
        int j = 0;
        int pos = 0;
        while (i < w && j < w) {
            double aux = a[i].getMean() - b[j].getMean();
            aux = aux * aux;
            if (a[i].getLast() == b[j].getLast()) {
                aux = aux * (a[i].getLast() - pos);
                pos = a[i].getLast();
                i++;
                j++;
            } else if (a[i].getLast() < b[j].getLast()) {
                aux = aux * (a[i].getLast() - pos);
                pos = a[i].getLast();
                i++;
            } else {
                aux = aux * (b[j].getLast() - pos);
                pos = b[j].getLast();
                j++;
            }

            sum += aux;

            if (sum >= transformedCutoff) {
                return Double.POSITIVE_INFINITY;
            }
        }
        return FastMath.sqrt(sum);
    }
}
