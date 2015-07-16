package ro.hasna.ts.math.exception.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

/**
 * @since 1.0
 */
public class TimeSeriesLocalizedFormatsTest {

    @Test
    public void testGetSourceString() throws Exception {
        TimeSeriesLocalizedFormats msg = TimeSeriesLocalizedFormats.NUMBER_NOT_DIVISIBLE_WITH;

        Assert.assertEquals(msg.getSourceString(), msg.getLocalizedString(Locale.ENGLISH));
    }
}