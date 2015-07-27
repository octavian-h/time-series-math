package ro.hasna.ts.math.representation;

import org.apache.commons.math3.util.FastMath;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @since 1.0
 */
public class TesparDzCodingTest {
    private TesparDzCoding tesparDzCoding;

    @Before
    public void setUp() throws Exception {
        tesparDzCoding = new TesparDzCoding();
    }

    @After
    public void tearDown() throws Exception {
        tesparDzCoding = null;
    }

    @Test
    public void testTransform() throws Exception {
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