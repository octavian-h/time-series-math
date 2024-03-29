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

import org.apache.commons.math3.util.FastMath;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TesparDzCodingTest {
    private TesparDzCoding tesparDzCoding;

    @Before
    public void setUp() {
        tesparDzCoding = new TesparDzCoding();
    }

    @After
    public void tearDown() {
        tesparDzCoding = null;
    }

    @Test
    public void testTransform() {
        double[] list = new double[300];
        for (int i = 0; i < 300; i++) {
            list[i] = FastMath.sin(i * 0.1);
            if (i % 7 == 0) {
                list[i] += 0.1;
            }
        }
        int[] expected = {1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 2};
        //27, 1, 27, 4, 24, 13, 6, 22

        int[] result = tesparDzCoding.transform(list);

        Assert.assertArrayEquals(expected, result);
    }
}