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
package ro.hasna.ts.math.normalization;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.util.TimeSeriesPrecision;


public class MinMaxNormalizerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testConstructor() {
        thrown.expect(NumberIsTooLargeException.class);
        thrown.expectMessage("4 is larger than the maximum (3)");

        new MinMaxNormalizer(4, 3);
    }

    @Test
    public void testNormalizeDefaultConstructor() {
        MinMaxNormalizer normalizer = new MinMaxNormalizer();
        double[] v = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] expected = {0.0, 0.25, 0.5, 0.75, 1.0};

        double[] out = normalizer.normalize(v);

        Assert.assertArrayEquals(expected, out, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testNormalize() {
        MinMaxNormalizer normalizer = new MinMaxNormalizer(-1, 1);
        double[] v = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] expected = {-1.0, -0.5, 0.0, 0.5, 1.0};

        double[] out = normalizer.normalize(v);

        Assert.assertArrayEquals(expected, out, TimeSeriesPrecision.EPSILON);
    }
}