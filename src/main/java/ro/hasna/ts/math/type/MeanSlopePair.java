package ro.hasna.ts.math.type;

import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class MeanSlopePair extends Pair<Double, Double> {
    /**
     * Create an entry of type (mean, slope).
     *
     * @param mean  the mean value for the segment
     * @param slope the slope of the segment
     */
    public MeanSlopePair(Double mean, Double slope) {
        super(mean, slope);
    }

    public Double getMean() {
        return getFirst();
    }

    public Double getSlope() {
        return getSecond();
    }

    /**
     * Compare the specified object with this entry for equality.
     *
     * @param o Object.
     * @return {@code true} if the given object is also a map entry and
     * the two entries represent the same mapping.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        } else {
            MeanSlopePair msp = (MeanSlopePair) o;
            return Precision.equals(getFirst(), msp.getMean(), TimeSeriesPrecision.EPSILON) &&
                    Precision.equals(getSecond(), msp.getSlope(), TimeSeriesPrecision.EPSILON);
        }
    }
}
