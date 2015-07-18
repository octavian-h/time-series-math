package ro.hasna.ts.math.filter;

/**
 * Interface for all time series filter algorithms.
 *
 * @since 1.0
 */
public interface Filter {
    /**
     * Filter the sequence of values.
     *
     * @param values the sequence of values
     * @return the filtered sequence
     */
    double[] filter(double[] values);
}
