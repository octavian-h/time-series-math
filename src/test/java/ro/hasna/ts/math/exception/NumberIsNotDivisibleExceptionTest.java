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
package ro.hasna.ts.math.exception;

import org.junit.Assert;
import org.junit.Test;
import ro.hasna.ts.math.representation.PiecewiseAggregateApproximation;

public class NumberIsNotDivisibleExceptionTest {

    @Test
    public void testGetFactor() throws Exception {
        try {
            PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(4);
            double[] v = {1, 2, 3, 4, 5};
            paa.transform(v);
        } catch (NumberIsNotDivisibleException e) {
            Assert.assertEquals(4, e.getFactor().intValue());
            Assert.assertEquals("5 is not divisible with 4", e.getMessage());
        }
    }
}