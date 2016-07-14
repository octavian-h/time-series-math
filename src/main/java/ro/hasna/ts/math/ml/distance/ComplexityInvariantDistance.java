/*
 * Copyright 2016 Octavian Hasna
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

import org.apache.commons.math3.util.FastMath;

/**
 * Apply a complexity factor to the base distance.
 * <p>
 * Reference:
 * Gustavo E.A.P.A. Batista, Xiaoyue Wang and Eamonn J. Keogh (2011)
 * <i>A Complexity-Invariant Distance Measure for Time Series</i>
 * </p>
 *
 * @since 1.0
 */
public class ComplexityInvariantDistance implements GenericDistanceMeasure<double[]> {
    private static final long serialVersionUID = 8279756267007620217L;
    private final GenericDistanceMeasure<double[]> baseDistance;

    public ComplexityInvariantDistance(GenericDistanceMeasure<double[]> baseDistance) {
        this.baseDistance = baseDistance;
    }

    @Override
    public double compute(double[] a, double[] b) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(double[] a, double[] b, double cutoffValue) {
        double complexityFactor = getComplexityFactor(a, b);
        double transformedCutoff = cutoffValue / complexityFactor;
        double d = baseDistance.compute(a, b, transformedCutoff);
        if (d >= transformedCutoff) {
            return Double.POSITIVE_INFINITY;
        }

        return d * complexityFactor;
    }

    // Overrides these methods for using other norms

    protected double getComplexityFactor(double[] a, double[] b) {
        double lengthA = getSeriesLength(a);
        double lengthB = getSeriesLength(b);
        if (lengthA > lengthB) {
            return lengthA / lengthB;
        }

        return lengthB / lengthA;
    }

    protected double getSeriesLength(double[] v) {
        double sum = 0;
        for (int i = 0; i < v.length - 1; i++) {
            sum += (v[i + 1] - v[i]) * (v[i + 1] - v[i]);
        }
        return FastMath.sqrt(sum);
    }

    public GenericDistanceMeasure<double[]> getBaseDistance() {
        return baseDistance;
    }
}
