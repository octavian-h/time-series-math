package ro.hasna.ts.math.type;

import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class MeanLastPair extends Pair<Double, Integer> {
    /**
     * Create an entry of type (mean, last).
     *
     * @param mean the mean value for the segment
     * @param last the position from the last element of the segment
     */
    public MeanLastPair(double mean, int last) {
        super(mean, last);
    }

    public double getMean() {
        return getFirst();
    }

    public int getLast() {
        return getSecond();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MeanLastPair)) {
            return false;
        } else {
            MeanLastPair mlp = (MeanLastPair) o;
            return Precision.equals(getFirst(), mlp.getMean(), TimeSeriesPrecision.EPSILON) &&
                    getSecond() == mlp.getLast();
        }
    }
}