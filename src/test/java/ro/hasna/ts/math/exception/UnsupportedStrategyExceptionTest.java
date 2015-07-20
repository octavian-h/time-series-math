package ro.hasna.ts.math.exception;

import org.junit.Assert;
import org.junit.Test;
import ro.hasna.ts.math.representation.PiecewiseLinearAggregateApproximation;
import ro.hasna.ts.math.representation.util.PaaStrategy;

/**
 * @since 1.0
 */
public class UnsupportedStrategyExceptionTest {

    @Test
    public void testGetters() throws Exception {
        try {
            new PiecewiseLinearAggregateApproximation(4, PaaStrategy.MEAN_PADDING);
        } catch (UnsupportedStrategyException e) {
            Assert.assertEquals("MEAN_PADDING", e.getStrategy());
            Assert.assertEquals("PLAA", e.getAlgorithm());
            Assert.assertEquals("the strategy MEAN_PADDING is not supported in PLAA", e.getMessage());
        }
    }
}