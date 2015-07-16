package ro.hasna.ts.math.normalization;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 * Implements the ZNormalizer algorithm that use mean and standard deviation for data normalization.
 * This algorithm is also called Standard Score, Z-Values, Z-Scores, Normal Scores and Standardized Variables.
 *
 * @since 1.0
 */
public class ZNormalizer implements Normalizer {
    private final Mean mean;
    private final StandardDeviation standardDeviation;

    /**
     * Creates a new instance of this class.
     */
    public ZNormalizer() {
        this(new Mean(), new StandardDeviation(false));
    }

    /**
     * Creates a new instance of this class with the given mean and standard deviation algorithms.
     *
     * @param mean              the mean
     * @param standardDeviation the standard deviation
     */
    public ZNormalizer(final Mean mean, final StandardDeviation standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] normalize(double[] values) {
        double m = mean.evaluate(values);
        double sd = standardDeviation.evaluate(values, m);

        int length = values.length;
        double[] normalizedValues = new double[length];
        for (int i = 0; i < length; i++) {
            normalizedValues[i] = (values[i] - m) / sd;
        }
        return normalizedValues;
    }
}
