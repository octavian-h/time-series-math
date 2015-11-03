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
package ro.hasna.ts.math.ml.distance.util;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.junit.Assert;
import ro.hasna.ts.math.ml.distance.GenericDistanceMeasure;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class DistanceTester {
    private GenericDistanceMeasure<double[]> distance;
    private int vectorLength;
    private double offset;
    private double cutOffValue;
    private double expectedResult;

    /**
     * Private constructor.
     */
    public DistanceTester() {
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

            @Override
            public double compute(double[] a, double[] b, int n, double cutOffValue) {
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

    public void testTriangleInequality() throws Exception {
        int n = 128;
        double a[] = new double[n];
        double b[] = new double[n];
        double c[] = new double[n];

        for (int i = 0; i < n; i++) {
            a[i] = i;
            b[i] = n - i;
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

    public void testEquality() throws Exception {
        int n = 128;
        double a[] = new double[n];
        double b[] = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
            b[i] = i;
        }

        double result = distance.compute(a, b);

        Assert.assertEquals(0, result, TimeSeriesPrecision.EPSILON);
    }

    public void testOverflowAdditive() throws Exception {
        double a[] = new double[vectorLength];
        double b[] = new double[vectorLength];
        for (int i = 0; i < vectorLength; i++) {
            a[i] = i;
            b[i] = i + offset;
        }

        double result = distance.compute(a, b, cutOffValue);

        Assert.assertEquals(Double.POSITIVE_INFINITY, result, TimeSeriesPrecision.EPSILON);
    }

    public void testOverflowSquare() throws Exception {
        double a[] = new double[vectorLength];
        double b[] = new double[vectorLength];
        for (int i = 0; i < vectorLength; i++) {
            a[i] = i;
            b[i] = i * i;
        }

        double result = distance.compute(a, b, cutOffValue);

        Assert.assertEquals(Double.POSITIVE_INFINITY, result, TimeSeriesPrecision.EPSILON);
    }

    public void testResultAdditive() throws Exception {
        double a[] = new double[vectorLength];
        double b[] = new double[vectorLength];
        for (int i = 0; i < vectorLength; i++) {
            a[i] = i;
            b[i] = i + offset;
        }

        double result = distance.compute(a, b, cutOffValue);

        Assert.assertEquals(expectedResult, result, TimeSeriesPrecision.EPSILON);
    }

    public void testResultSquare() throws Exception {
        double a[] = new double[vectorLength];
        double b[] = new double[vectorLength];
        for (int i = 0; i < vectorLength; i++) {
            a[i] = i;
            b[i] = i * i;
        }

        double result = distance.compute(a, b, cutOffValue);

        Assert.assertEquals(expectedResult, result, TimeSeriesPrecision.EPSILON);
    }
}
