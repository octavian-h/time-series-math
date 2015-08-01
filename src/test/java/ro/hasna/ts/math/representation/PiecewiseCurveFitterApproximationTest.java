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
package ro.hasna.ts.math.representation;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.fitting.AbstractCurveFitter;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.junit.*;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.exception.ArrayLengthIsNotDivisibleException;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.representation.util.SegmentationStrategy;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class PiecewiseCurveFitterApproximationTest {
    private AbstractCurveFitter curveFitter;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static void assertMatrixEquals(double[][] expected, double[][] actual, double precision) {
        Assert.assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertArrayEquals(expected[i], actual[i], precision);
        }
    }

    @Before
    public void setUp() throws Exception {
        curveFitter = PolynomialCurveFitter.create(0).withMaxIterations(2);
    }

    @After
    public void tearDown() throws Exception {
        curveFitter = null;
    }

    @Test
    public void testConstructor() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("0 is smaller than the minimum (1)");

        new PiecewiseCurveFitterApproximation(0, curveFitter);
    }

    @Test
    public void testTransformMoreSegmentsThanValues() throws Exception {
        thrown.expect(ArrayLengthIsTooSmallException.class);
        thrown.expectMessage("3 is smaller than the minimum (4)");

        PiecewiseCurveFitterApproximation pcfa = new PiecewiseCurveFitterApproximation(4, SegmentationStrategy.FRACTIONAL_PARTITION, curveFitter);
        double[] v = {1, 2, 3};

        pcfa.transform(v);
    }

    @Test
    public void testTransformSegmentsNotDivisible() throws Exception {
        thrown.expect(ArrayLengthIsNotDivisibleException.class);
        thrown.expectMessage("5 is not divisible with 4");

        PiecewiseCurveFitterApproximation pcfa = new PiecewiseCurveFitterApproximation(4, curveFitter);
        double[] v = {1, 2, 3, 4, 5};

        pcfa.transform(v);
    }

    @Test
    public void testTransformStrict() throws Exception {
        PiecewiseCurveFitterApproximation pcfa = new PiecewiseCurveFitterApproximation(2, curveFitter);
        double[] v = {1, 2, 3, 4, 5, 6};
        double[][] expected = {{2.0}, {5.0}};

        double[][] result = pcfa.transform(v);

        assertMatrixEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformIgnoreRemaining() throws Exception {
        PiecewiseCurveFitterApproximation pcfa = new PiecewiseCurveFitterApproximation(2, SegmentationStrategy.IGNORE_REMAINING, curveFitter);
        double[] v = {1, 2, 3, 4, 5, 6, 7};
        double[][] expected = {{2.0}, {5.0}};

        double[][] result = pcfa.transform(v);

        assertMatrixEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformFractionalPartition1() throws Exception {
        PiecewiseCurveFitterApproximation pcfa = new PiecewiseCurveFitterApproximation(2, SegmentationStrategy.FRACTIONAL_PARTITION, curveFitter);
        double[] v = {1, 2, 3, 4, 5, 6, 7};
        double[][] expected = {{8 / 3.5}, {20 / 3.5}};

        double[][] result = pcfa.transform(v);

        assertMatrixEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformFractionalPartition2() throws Exception {
        PiecewiseCurveFitterApproximation pcfa = new PiecewiseCurveFitterApproximation(5, SegmentationStrategy.FRACTIONAL_PARTITION, curveFitter);
        double[] v = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        double[][] expected = {{24.0 / 13}, {57.0 / 13}, {91.0 / 13}, {125.0 / 13}, {158.0 / 13}};

        double[][] result = pcfa.transform(v);

        assertMatrixEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformFractionalPartition3() throws Exception {
        PiecewiseCurveFitterApproximation pcfa = new PiecewiseCurveFitterApproximation(13, SegmentationStrategy.FRACTIONAL_PARTITION, curveFitter);
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

    @Test
    public void testGetters1() throws Exception {
        PiecewiseCurveFitterApproximation pcfa = new PiecewiseCurveFitterApproximation(3, curveFitter);

        Assert.assertEquals(3, pcfa.getSegments());
        Assert.assertEquals(SegmentationStrategy.STRICT, pcfa.getStrategy());
        Assert.assertEquals(curveFitter, pcfa.getCurveFitter());
    }

    @Test
    public void testGetters2() throws Exception {
        PiecewiseCurveFitterApproximation pcfa = new PiecewiseCurveFitterApproximation(4, SegmentationStrategy.IGNORE_REMAINING, curveFitter);

        Assert.assertEquals(4, pcfa.getSegments());
        Assert.assertEquals(SegmentationStrategy.IGNORE_REMAINING, pcfa.getStrategy());
    }
}