/**
 * Copyright (C) 2016-2015 Octavian Hasna
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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class DiscreteHaarWaveletTransformTest {
    private DiscreteHaarWaveletTransform waveletTransform;

    @Before
    public void setUp() throws Exception {
        waveletTransform = new DiscreteHaarWaveletTransform();
    }

    @After
    public void tearDown() throws Exception {
        waveletTransform = null;
    }

    @Test
    public void testTransform() throws Exception {
        double[] v = new double[]{1, 2, 1, 0, -1, -2, -1, 0};
        double[] result = waveletTransform.transform(v);
        double[] expected = new double[]{0, 8, 2, -2, -1, 1, 1, -1};

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }
}