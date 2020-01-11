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
package ro.hasna.ts.math.type;

import org.apache.commons.math3.exception.NotPositiveException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.util.TimeSeriesPrecision;


public class TesparSymbolTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testConstructorException() throws Exception {
        thrown.expect(NotPositiveException.class);
        thrown.expectMessage("-5.5 is smaller than the minimum (0)");

        new TesparSymbol(10, 3, -5.5);
    }

    @Test
    public void testGetters() throws Exception {
        TesparSymbol symbol = new TesparSymbol(10, 3, 5.5);

        Assert.assertEquals(10, symbol.getDuration());
        Assert.assertEquals(3, symbol.getShape());
        Assert.assertEquals(5.5, symbol.getAmplitude(), TimeSeriesPrecision.EPSILON);
    }
}