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
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class PiecewiseAggregateApproximationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testConstructor() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("0 is smaller than the minimum (1)");

        new PiecewiseAggregateApproximation(0);
    }

    @Test
    public void testTransformMoreSegmentsThanValues() throws Exception {
        thrown.expect(ArrayLengthIsTooSmallException.class);
        thrown.expectMessage("3 is smaller than the minimum (4)");

        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(4);
        double[] v = {1, 2, 3};

        paa.transform(v);
    }

    @Test
    public void testTransformStrict() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(2);
        double[] v = {1, 2, 3, 4, 5, 6};
        double[] expected = {2.0, 5.0};

        double[] result = paa.transform(v);

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformFractionalPartition1() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(2);
        double[] v = {1, 2, 3, 4, 5, 6, 7};
        double[] expected = {8 / 3.5, 20 / 3.5};

        double[] result = paa.transform(v);

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformFractionalPartition2() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(5);
        double[] v = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        double[] expected = {24.0 / 13, 57.0 / 13, 91.0 / 13, 125.0 / 13, 158.0 / 13};

        double[] result = paa.transform(v);

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testTransformFractionalPartition3() throws Exception {
        PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(13);
        double[] v = new double[523];
        for (int i = 0; i < 523; i++) {
            v[i] = 1.0;
        }
        double[] expected = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        double[] result = paa.transform(v);

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }
}