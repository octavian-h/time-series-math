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
package ro.hasna.ts.math.ml.distance.util;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.junit.Assert;
import ro.hasna.ts.math.ml.distance.GenericDistanceMeasure;
import ro.hasna.ts.math.representation.GenericTransformer;
import ro.hasna.ts.math.util.TimeSeriesPrecision;


public class DistanceTester {
    private GenericDistanceMeasure<double[]> distance;
    private int vectorLength = 128;
    private double offset;
    private double cutOffValue;
    private double expectedResult;

    /**
     * Private constructor.
     */
    public DistanceTester() {
    }

    public <T> DistanceTester withGenericDistanceMeasure(final GenericDistanceMeasure<T[]> distance, final GenericTransformer<double[], T[]> transformer) {
        this.distance = new GenericDistanceMeasure<double[]>() {
            private static final long serialVersionUID = -7602963003446517879L;

            @Override
            public double compute(double[] a, double[] b) {
                T[] ta = transformer.transform(a);
                T[] tb = transformer.transform(b);

                return distance.compute(ta, tb);
            }

            @Override
            public double compute(double[] a, double[] b, double cutOffValue) {
                T[] ta = transformer.transform(a);
                T[] tb = transformer.transform(b);

                return distance.compute(ta, tb, cutOffValue);
            }
        };
        return this;
    }

    public DistanceTester withGenericDistanceMeasure(GenericDistanceMeasure<double[]> distance) {
        this.distance = distance;
        return this;
    }

    public DistanceTester withDistanceMeasure(final DistanceMeasure distance) {
        this.distance = new GenericDistanceMeasure<double[]>() {
            private static final long serialVersionUID = -7065467026544814688L;

            @Override
            public double compute(double[] a, double[] b) {
                return distance.compute(a, b);
            }

            @Override
            public double compute(double[] a, double[] b, double cutOffValue) {
                return distance.compute(a, b);
            }
        };
        return this;
    }

    public DistanceTester withVectorLength(int n) {
        this.vectorLength = n;
        return this;
    }

    public DistanceTester withOffset(double offset) {
        this.offset = offset;
        return this;
    }

    public DistanceTester withCutOffValue(double cutOffValue) {
        this.cutOffValue = cutOffValue;
        return this;
    }

    public DistanceTester withExpectedResult(double expectedResult) {
        this.expectedResult = expectedResult;
        return this;
    }

    public void testTriangleInequality() {
        double[] a = new double[vectorLength];
        double[] b = new double[vectorLength];
        double[] c = new double[vectorLength];

        for (int i = 0; i < vectorLength; i++) {
            a[i] = i;
            b[i] = vectorLength - i;
            c[i] = i * i;
        }

        double ab = distance.compute(a, b);
        double ba = distance.compute(b, a);
        double bc = distance.compute(b, c);
        double ac = distance.compute(a, c);

        Assert.assertEquals(ab, ba, TimeSeriesPrecision.EPSILON);
        Assert.assertTrue(ab + bc >= ac);
        Assert.assertTrue(ab + ac >= bc);
        Assert.assertTrue(ac + bc >= ab);
    }

    public void testEquality() {
        double[] a = new double[vectorLength];
        double[] b = new double[vectorLength];
        for (int i = 0; i < vectorLength; i++) {
            a[i] = i;
            b[i] = i;
        }

        double result = distance.compute(a, b);

        Assert.assertEquals(0, result, TimeSeriesPrecision.EPSILON);
    }

    public void testOverflowAdditive() {
        double[] a = new double[vectorLength];
        double[] b = new double[vectorLength];
        for (int i = 0; i < vectorLength; i++) {
            a[i] = i;
            b[i] = i + offset;
        }

        double result = distance.compute(a, b, cutOffValue);

        Assert.assertEquals(Double.POSITIVE_INFINITY, result, TimeSeriesPrecision.EPSILON);
    }

    public void testOverflowSquare() {
        double[] a = new double[vectorLength];
        double[] b = new double[vectorLength];
        for (int i = 0; i < vectorLength; i++) {
            a[i] = i;
            b[i] = i * i;
        }

        double result = distance.compute(a, b, cutOffValue);

        Assert.assertEquals(Double.POSITIVE_INFINITY, result, TimeSeriesPrecision.EPSILON);
    }

    public void testResultAdditive() {
        double[] a = new double[vectorLength];
        double[] b = new double[vectorLength];
        for (int i = 0; i < vectorLength; i++) {
            a[i] = i;
            b[i] = i + offset;
        }

        double result = distance.compute(a, b, cutOffValue);

        Assert.assertEquals(expectedResult, result, TimeSeriesPrecision.EPSILON);
    }

    public void testResultSquare() {
        double[] a = new double[vectorLength];
        double[] b = new double[vectorLength];
        for (int i = 0; i < vectorLength; i++) {
            a[i] = i;
            b[i] = i * i;
        }

        double result = distance.compute(a, b, cutOffValue);

        Assert.assertEquals(expectedResult, result, TimeSeriesPrecision.EPSILON);
    }
}
