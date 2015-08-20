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

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class UniformScalingDistanceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private UniformScalingDistance distance;

    @Before
    public void setUp() throws Exception {
        distance = new UniformScalingDistance(0.5, 1.5, 10);
    }

    @After
    public void tearDown() throws Exception {
        distance = null;
    }

    @Test
    public void testMinScalingBelowZero() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("0 is smaller than, or equal to, the minimum (0)");

        new UniformScalingDistance(0, 1.5, 4);
    }

    @Test
    public void testMaxScalingBelowMinScaling() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("0 is smaller than the minimum (1.5)");

        new UniformScalingDistance(1.5, 0, 4);
    }

    @Test
    public void testStepsBelowOne() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("0 is smaller than the minimum (1)");

        new UniformScalingDistance(0.1, 1.5, 0);
    }

    @Test
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

    @Test
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

    @Test
    public void testOverflow() throws Exception {
        int n = 128;
        double a[] = new double[n];
        double b[] = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
            b[i] = i + 100;
        }

        double result = distance.compute(a, b, 99);

        Assert.assertEquals(Double.POSITIVE_INFINITY, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testResult() throws Exception {
        int n = 100;
        double a[] = new double[n];
        double b[] = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = i * 0.7 + 1;
            b[i] = i;
        }

        double result = distance.compute(a, b, 11);

        Assert.assertEquals(10, result, TimeSeriesPrecision.EPSILON);
    }
}