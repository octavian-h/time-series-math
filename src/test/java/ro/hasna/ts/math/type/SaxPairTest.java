package ro.hasna.ts.math.type;

import org.junit.Assert;
import org.junit.Test;

/**
 * @since 1.0
 */
public class SaxPairTest {

    @Test
    public void testGetters() throws Exception {
        SaxPair pair = new SaxPair(10, 20);

        Assert.assertEquals(10, pair.getSymbol());
        Assert.assertEquals(20, pair.getAlphabetSize());
    }

    @Test
    public void testEquals() throws Exception {
        SaxPair pair = new SaxPair(10, 20);

        Assert.assertEquals(false, pair.equals(null));
        Assert.assertEquals(true, pair.equals(pair));
        Assert.assertEquals(true, pair.equals(new SaxPair(10, 20)));
        Assert.assertEquals(false, pair.equals(new SaxPair(10, 21)));
    }
}