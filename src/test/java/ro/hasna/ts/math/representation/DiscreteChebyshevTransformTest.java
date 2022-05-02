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

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ro.hasna.ts.math.util.TimeSeriesPrecision;


@RunWith(MockitoJUnitRunner.class)
public class DiscreteChebyshevTransformTest {

    @InjectMocks
    private DiscreteChebyshevTransform discreteChebyshevTransform;

    @Mock
    private FastFourierTransformer fastFourierTransformer;

    @Before
    public void setUp() {
        Mockito.when(fastFourierTransformer.transform(Mockito.<double[]>any(), Mockito.any())).thenReturn(new Complex[]{new Complex(0)});
    }

    @After
    public void tearDown() {
        discreteChebyshevTransform = null;
        fastFourierTransformer = null;
    }

    @Test
    public void testTransform() {
        double[] v = {1, 2, 3};
        discreteChebyshevTransform.transform(v);

        Mockito.verify(fastFourierTransformer).transform(new double[]{1, 2, 3, 2}, TransformType.FORWARD);
    }

    @Test
    public void testTransform2() {
        double[] v = {1, 2, 3, 4};
        discreteChebyshevTransform.transform(v);

        Mockito.verify(fastFourierTransformer).transform(new double[]{1, 2, 3, 4, 3, 2, 0, 0}, TransformType.FORWARD);
    }

    @Test
    public void testTransformSmallVector() {
        double[] v = {1, 2};
        discreteChebyshevTransform.transform(v);

        Mockito.verify(fastFourierTransformer, Mockito.never()).transform(Mockito.<double[]>any(), Mockito.any());
    }

    @Test
    public void testTransformConcrete() {
        double[] v = {98, 100}; // 99 * x^2 - x
        double[] expected = {99, -1};
        double[] result = new DiscreteChebyshevTransform().transform(v);

        Assert.assertArrayEquals(expected, result, TimeSeriesPrecision.EPSILON);
    }
}