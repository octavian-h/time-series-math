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

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.ml.distance.util.DistanceTester;
import ro.hasna.ts.math.util.TimeSeriesPrecision;


public class UniformScalingDistanceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private UniformScalingDistance distance;

    @Before
    public void setUp() {
        distance = new UniformScalingDistance(0.5, 1.5, 11);
    }

    @After
    public void tearDown() {
        distance = null;
    }

    @Test
    public void testMinScalingBelowZero() {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("0 is smaller than, or equal to, the minimum (0)");

        new UniformScalingDistance(0, 1.5, 4);
    }

    @Test
    public void testMaxScalingBelowMinScaling() {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("0 is smaller than the minimum (1.5)");

        new UniformScalingDistance(1.5, 0, 4);
    }

    @Test
    public void testStepsBelowOne() {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("0 is smaller than the minimum (1)");

        new UniformScalingDistance(0.1, 1.5, 0);
    }

    @Test
    public void testTriangleInequality() throws Exception {
        new DistanceTester().withGenericDistanceMeasure(distance)
                .testTriangleInequality();
    }

    @Test
    public void testEquality() throws Exception {
        new DistanceTester().withGenericDistanceMeasure(distance)
                .testEquality();
    }

    @Test
    public void testOverflow() throws Exception {
        new DistanceTester().withGenericDistanceMeasure(distance)
                .withVectorLength(128)
                .withOffset(100)
                .withCutOffValue(99)
                .testOverflowAdditive();
    }

    @Test
    public void testResult() {
        int n = 100;
        double[] a = new double[n];
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = i * 0.7 + 1;
            b[i] = i;
        }

        double result = distance.compute(a, b, 11);

        Assert.assertEquals(10, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testResult2() {
        int n = 100;
        double[] a = new double[n];
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = i * 1.3 + 1;
            b[i] = i;
        }

        double result = distance.compute(a, b, 11);

        Assert.assertEquals(10, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testResult3() {
        UniformScalingDistance dist = new UniformScalingDistance(1.5, 2.5, 1);
        double[] a = {1, 2, 3, 4, 5, 6};
        double[] b = {1, 1.5, 2, 2.5, 3, 3.5};

        double result = dist.compute(a, b, 11);

        Assert.assertEquals(0, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testResult4() {
        UniformScalingDistance dist = new UniformScalingDistance(2, 2, 10);
        double[] a = {1, 2, 3, 4, 5, 6};
        double[] b = {1, 1.5, 2, 2.5, 3, 3.5};

        double result = dist.compute(a, b, 11);

        Assert.assertEquals(0, result, TimeSeriesPrecision.EPSILON);
    }
}