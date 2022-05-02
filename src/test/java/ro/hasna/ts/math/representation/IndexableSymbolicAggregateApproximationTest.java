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

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.distribution.NormalDistributionDivider;
import ro.hasna.ts.math.normalization.ZNormalizer;
import ro.hasna.ts.math.type.SaxPair;

import java.util.Random;


public class IndexableSymbolicAggregateApproximationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testConstructorException() {
        thrown.expect(DimensionMismatchException.class);
        thrown.expectMessage("5 != 4");

        new IndexableSymbolicAggregateApproximation(4, new int[5]);
    }

    @Test
    public void testTransformToSaxPairArray1() {
        double[] list = new double[64];
        for (int i = 0; i < 32; i++) {
            list[i] = i;
        }
        for (int i = 32; i < 64; i++) {
            list[i] = 64 - i;
        }
        int[] alphabetSizes = new int[9];
        for (int i = 0; i < 9; i++) {
            alphabetSizes[i] = 3;
        }
        SaxPair[] expected = new SaxPair[9];
        expected[0] = new SaxPair(0, 3);
        expected[1] = new SaxPair(0, 3);
        expected[2] = new SaxPair(1, 3);
        expected[3] = new SaxPair(2, 3);
        expected[4] = new SaxPair(2, 3);
        expected[5] = new SaxPair(2, 3);
        expected[6] = new SaxPair(1, 3);
        expected[7] = new SaxPair(0, 3);
        expected[8] = new SaxPair(0, 3);

        IndexableSymbolicAggregateApproximation isax = new IndexableSymbolicAggregateApproximation(
                new PiecewiseAggregateApproximation(9),
                new ZNormalizer(),
                alphabetSizes,
                new NormalDistributionDivider());
        SaxPair[] result = isax.transform(list);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testTransformToSaxPairArray2() {
        double[] list = new double[64];
        for (int i = 0; i < 32; i++) {
            list[i] = i;
        }
        for (int i = 32; i < 64; i++) {
            list[i] = 64 - i;
        }
        int[] alphabetSizes = new int[8];
        for (int i = 0; i < 8; i++) {
            alphabetSizes[i] = 3;
        }
        SaxPair[] expected = new SaxPair[8];
        expected[0] = new SaxPair(0, 3);
        expected[1] = new SaxPair(0, 3);
        expected[2] = new SaxPair(1, 3);
        expected[3] = new SaxPair(2, 3);
        expected[4] = new SaxPair(2, 3);
        expected[5] = new SaxPair(2, 3);
        expected[6] = new SaxPair(1, 3);
        expected[7] = new SaxPair(0, 3);

        IndexableSymbolicAggregateApproximation isax = new IndexableSymbolicAggregateApproximation(8, alphabetSizes);
        SaxPair[] result = isax.transform(list);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testTransformToSaxPairArray3() {
        double[] list = new double[64];
        Random random = new Random();
        for (int i = 0; i < 26; i++) {
            list[i] = random.nextDouble();
        }
        for (int i = 26; i < 40; i++) {
            list[i] = 20 + random.nextDouble();
        }
        for (int i = 40; i < 64; i++) {
            list[i] = random.nextDouble();
        }
        int[] alphabetSizes = {2, 2, 2, 4, 4, 2, 2, 2};
        SaxPair[] expected = new SaxPair[8];
        expected[0] = new SaxPair(0, 2);
        expected[1] = new SaxPair(0, 2);
        expected[2] = new SaxPair(0, 2);
        expected[3] = new SaxPair(3, 4);
        expected[4] = new SaxPair(3, 4);
        expected[5] = new SaxPair(0, 2);
        expected[6] = new SaxPair(0, 2);
        expected[7] = new SaxPair(0, 2);

        IndexableSymbolicAggregateApproximation isax = new IndexableSymbolicAggregateApproximation(8, alphabetSizes);
        SaxPair[] result = isax.transform(list);

        Assert.assertArrayEquals(expected, result);
    }
}