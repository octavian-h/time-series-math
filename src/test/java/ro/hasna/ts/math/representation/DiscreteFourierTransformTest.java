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
import org.apache.commons.math3.transform.DftNormalization;
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

import java.util.Arrays;
import java.util.stream.Collectors;


@RunWith(MockitoJUnitRunner.class)
public class DiscreteFourierTransformTest {

    @InjectMocks
    private DiscreteFourierTransform discreteFourierTransform;

    @Mock
    private FastFourierTransformer fastFourierTransformer;

    @Before
    public void setUp() {
        Mockito.when(fastFourierTransformer.transform(Mockito.<double[]>any(), Mockito.any())).thenReturn(new Complex[]{new Complex(0)});
    }

    @After
    public void tearDown() {
        discreteFourierTransform = null;
        fastFourierTransformer = null;
    }

    @Test
    public void testTransform() {
        double[] v = {1, 2, 3};
        discreteFourierTransform.transform(v);

        Mockito.verify(fastFourierTransformer).transform(new double[]{1, 2, 3, 0}, TransformType.FORWARD);
    }

    @Test
    public void testTransformPowerOfTwo() {
        double[] v = {1, 2, 3, 4};
        discreteFourierTransform.transform(v);

        Mockito.verify(fastFourierTransformer).transform(v, TransformType.FORWARD);
    }

    @Test
    public void reverseTransform() {
        double[] v = {1, 2, 2, 5, 50, 25, 18, 0};
        double[] v2 = {4, 3, 2, 1, 0, 0, 0, 0};
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] transformB = fft.transform(v, TransformType.FORWARD);
        Complex[] transformA = fft.transform(v2, TransformType.FORWARD);
        for (int i = 0; i < 8; i++) {
            transformA[i] = transformA[i].multiply(transformB[i]);
        }
        Complex[] reverse = fft.transform(transformA, TransformType.INVERSE);
        System.out.println(Arrays.toString(reverse));
        System.out.println(Arrays.stream(reverse).map(Complex::abs).collect(Collectors.toList()));
    }

    @Test
    public void testTransformSineWave() {
        double signalFrequency = 100;
        double amplitude = 30;
        double displacement = 17;
        double samplingFrequency = signalFrequency * 8; //it should be at least double (Shannon Theorem)
        int len = 1000;
        double[] v = new double[len];
        for (int i = 0; i < len; i++) {
            v[i] = amplitude * Math.sin(2 * Math.PI * signalFrequency * i / samplingFrequency) + displacement;
        }

        double[] result = new DiscreteFourierTransform().transform(v);

        double max = 0;
        int pos = 0;
        for (int i = 1; i < result.length; i++) {
            if (max < result[i]) {
                max = result[i];
                pos = i;
            }
        }

        int powerOfTwo = Integer.highestOneBit(len);
        if (len != powerOfTwo) {
            powerOfTwo = powerOfTwo << 1;
        }

        Assert.assertEquals(amplitude, max, TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(signalFrequency, pos * samplingFrequency / powerOfTwo, TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(displacement, result[0], TimeSeriesPrecision.EPSILON);
    }
}
