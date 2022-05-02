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

import org.apache.commons.math3.transform.FastCosineTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DiscreteCosineTransformTest {

    @InjectMocks
    private DiscreteCosineTransform discreteCosineTransform;

    @Mock
    private FastCosineTransformer fastCosineTransformer;

    @Before
    public void setUp() {
        Mockito.when(fastCosineTransformer.transform(Mockito.any(), Mockito.any())).thenReturn(new double[]{0});
    }

    @After
    public void tearDown() {
        discreteCosineTransform = null;
        fastCosineTransformer = null;
    }

    @Test
    public void testTransform() {
        double[] v = {1, 2, 3, 4, 5, 6};
        discreteCosineTransform.transform(v);

        Mockito.verify(fastCosineTransformer).transform(new double[]{1, 2, 3, 4, 5, 6, 0, 0, 0}, TransformType.FORWARD);
    }

    @Test
    public void testTransformPowerOfTwoPlusOne() {
        double[] v = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        discreteCosineTransform.transform(v);

        Mockito.verify(fastCosineTransformer).transform(v, TransformType.FORWARD);
    }
}