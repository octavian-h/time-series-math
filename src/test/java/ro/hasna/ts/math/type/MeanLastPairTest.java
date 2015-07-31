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
package ro.hasna.ts.math.type;

import org.junit.Assert;
import org.junit.Test;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class MeanLastPairTest {

    @Test
    public void testGetters() throws Exception {
        MeanLastPair pair = new MeanLastPair(10.0, 20);

        Assert.assertEquals(10.0, pair.getMean(), TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(20, pair.getLast());
    }

    @Test
    public void testEquals() throws Exception {
        MeanLastPair pair = new MeanLastPair(10.0, 20);

        Assert.assertEquals(false, pair.equals(null));
        Assert.assertEquals(true, pair.equals(pair));
        Assert.assertEquals(true, pair.equals(new MeanLastPair(10.0, 20)));
        Assert.assertEquals(false, pair.equals(new MeanLastPair(10.0, 21)));
    }
}