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
import ro.hasna.ts.math.exception.ArrayLengthIsNotDivisibleException;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.type.MeanSlopePair;


public class PiecewiseLinearAggregateApproximationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testConstructor() {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("0 is smaller than the minimum (1)");

        new PiecewiseLinearAggregateApproximation(0);
    }

    @Test
    public void testTransformMoreSegmentsThanValues() {
        thrown.expect(ArrayLengthIsTooSmallException.class);
        thrown.expectMessage("3 is smaller than the minimum (4)");

        PiecewiseLinearAggregateApproximation plaa = new PiecewiseLinearAggregateApproximation(4);
        double[] v = {1, 2, 3};

        plaa.transform(v);
    }

    @Test
    public void testTransformSegmentsNotDivisible() {
        thrown.expect(ArrayLengthIsNotDivisibleException.class);
        thrown.expectMessage("5 is not divisible with 4");

        PiecewiseLinearAggregateApproximation plaa = new PiecewiseLinearAggregateApproximation(4);
        double[] v = {1, 2, 3, 4, 5};

        plaa.transform(v);
    }

    @Test
    public void testTransformStrict() {
        PiecewiseLinearAggregateApproximation plaa = new PiecewiseLinearAggregateApproximation(2);
        double[] v = {1, 2, 3, 3, 2, 1};
        MeanSlopePair[] expected = {new MeanSlopePair(2.0, 1.0), new MeanSlopePair(2.0, -1.0)};

        MeanSlopePair[] result = plaa.transform(v);

        Assert.assertArrayEquals(expected, result);
    }
}