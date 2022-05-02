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
package ro.hasna.ts.math.representation;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.fitting.AbstractCurveFitter;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.junit.*;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.util.TimeSeriesPrecision;


public class PiecewiseCurveFitterApproximationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AbstractCurveFitter curveFitter;

    private static void assertMatrixEquals(double[][] expected, double[][] actual, double precision) {
        Assert.assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertArrayEquals(expected[i], actual[i], precision);
        }
    }

    @Before
    public void setUp() {
        curveFitter = PolynomialCurveFitter.create(0).withMaxIterations(2);
    }

    @After
    public void tearDown() {
        curveFitter = null;
    }

    @Test
    public void testConstructor() {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("0 is smaller than the minimum (1)");

        new PiecewiseCurveFitterApproximation(0, curveFitter);
    }

    @Test
    public void testTransformMoreSegmentsThanValues() {
        thrown.expect(ArrayLengthIsTooSmallException.class);
        thrown.expectMessage("3 is smaller than the minimum (4)");

        PiecewiseCurveFitterApproximation pcfa = new PiecewiseCurveFitterApproximation(4, curveFitter);
        double[] v = {1, 2, 3};

        pcfa.transform(v);
    }

    @Test
    public void testTransformStrict() {
        PiecewiseCurveFitterApproximation pcfa = new PiecewiseCurveFitterApproximation(2, curveFitter);
        double[] v = {1, 2, 3, 4, 5, 6};
        double[][] expected = {{2.0}, {5.0}};

        double[][] result = pcfa.transform(v);

        assertMatrixEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformFractionalPartition1() {
        PiecewiseCurveFitterApproximation pcfa = new PiecewiseCurveFitterApproximation(2, curveFitter);
        double[] v = {1, 2, 3, 4, 5, 6, 7};
        double[][] expected = {{8 / 3.5}, {20 / 3.5}};

        double[][] result = pcfa.transform(v);

        assertMatrixEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformFractionalPartition2() {
        PiecewiseCurveFitterApproximation pcfa = new PiecewiseCurveFitterApproximation(5, curveFitter);
        double[] v = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        double[][] expected = {{24.0 / 13}, {57.0 / 13}, {91.0 / 13}, {125.0 / 13}, {158.0 / 13}};

        double[][] result = pcfa.transform(v);

        assertMatrixEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformFractionalPartition3() {
        PiecewiseCurveFitterApproximation pcfa = new PiecewiseCurveFitterApproximation(13, curveFitter);
        double[] v = new double[523];
        for (int i = 0; i < 523; i++) {
            v[i] = 1.0;
        }
        double[][] expected = new double[13][1];
        for (int i = 0; i < 13; i++) {
            expected[i][0] = 1;
        }

        double[][] result = pcfa.transform(v);

        assertMatrixEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }
}