package ro.hasna.ts.math.exception;

import org.junit.Assert;
import org.junit.Test;
import ro.hasna.ts.math.representation.PiecewiseAggregateApproximation;

/**
 * @since 1.0
 */
public class NumberIsNotDivisibleExceptionTest {

    @Test
    public void testGetFactor() throws Exception {
        try {
            PiecewiseAggregateApproximation paa = new PiecewiseAggregateApproximation(4);
            double[] v = {1, 2, 3, 4, 5};
            paa.transform(v);
        } catch (NumberIsNotDivisibleException e) {
            Assert.assertEquals(4, e.getFactor().intValue());
            Assert.assertEquals("5 is not divisible with 4", e.getMessage());
        }
    }
}