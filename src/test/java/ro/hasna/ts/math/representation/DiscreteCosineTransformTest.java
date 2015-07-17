package ro.hasna.ts.math.representation;

import org.apache.commons.math3.transform.FastCosineTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class DiscreteCosineTransformTest {

    @InjectMocks
    private DiscreteCosineTransform discreteCosineTransform;

    @Mock
    private FastCosineTransformer fastCosineTransformer;

    @Before
    public void setUp() throws Exception {
        Mockito.when(fastCosineTransformer.transform(Mockito.<double[]>any(), Mockito.<TransformType>any())).thenReturn(new double[]{0});
    }

    @After
    public void tearDown() throws Exception {
        discreteCosineTransform = null;
        fastCosineTransformer = null;
    }

    @Test
    public void testTransform() throws Exception {
        double[] v = {1, 2, 3, 4};
        discreteCosineTransform.transformToDoubleArray(v);

        Mockito.verify(fastCosineTransformer).transform(new double[]{1, 2, 3, 4, 0}, TransformType.FORWARD);
    }

    @Test
    public void testTransformPowerOfTwoPlusOne() throws Exception {
        double[] v = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        discreteCosineTransform.transformToDoubleArray(v);

        Mockito.verify(fastCosineTransformer).transform(v, TransformType.FORWARD);
    }

//    @Test
//    public void testTransformSineWave() throws Exception {
//        double signalFrequency = 100;
//        double amplitude = 30;
//        double displacement = 17;
//        double samplingFrequency = signalFrequency * 8; //it should be at least double (Shannon Theorem)
//        int len = 1000;
//        double[] v = new double[len];
//        for (int i = 0; i < len; i++) {
//            v[i] = amplitude * Math.sin(2 * Math.PI * signalFrequency * i / samplingFrequency) + displacement;
//        }
//
//        double[] result = new DiscreteCosineTransform().transformToDoubleArray(v);
//
//        double max = 0;
//        int pos = 0;
//        for (int i = 1; i < result.length; i++) {
//            if (max < result[i]) {
//                max = result[i];
//                pos = i;
//            }
//        }
//
//        int powerOfTwo = Integer.highestOneBit(len);
//        if (len != powerOfTwo) {
//            powerOfTwo = powerOfTwo << 1;
//        }
//
//        Assert.assertEquals(amplitude, max, TimeSeriesPrecision.EPSILON);
//        Assert.assertEquals(signalFrequency, pos * samplingFrequency / powerOfTwo, TimeSeriesPrecision.EPSILON);
//        Assert.assertEquals(displacement, result[0], TimeSeriesPrecision.EPSILON);
//    }
}