package ro.hasna.ts.math.representation;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import ro.hasna.ts.math.exception.NotAFactorNumberException;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Piecewise Aggregate Approximation (PAA) algorithm.
 * <p>
 * Reference:
 * Keogh E., Chakrabarti K., Pazzani M., Mehrotra S. (2001)
 * <i>Locally Adaptive Dimensionality Reduction for Indexing Large Time Series Databases </i>
 * </p>
 *
 * @since 1.0
 */
public class PiecewiseAggregateApproximation {
    private final PaaStrategy strategy;

    /**
     * Creates a new instance of this class with a given strategy.
     *
     * @param strategy the type of strategy to be applied to the sequence
     */
    public PiecewiseAggregateApproximation(PaaStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Transform a given sequence of values using the algorithm Piecewise Aggregate Approximation (PAA).
     *
     * @param sequence the sequence of values
     * @param segments the number of segments
     * @return the result of the transformation
     */
    public List<Double> transform(List<Double> sequence, int segments) {
        int len = sequence.size();
        if (segments > len) {
            throw new NumberIsTooLargeException(segments, len, true);
        }

        int modulo = len % segments;
        if (modulo != 0 && strategy == PaaStrategy.STRICT) {
            throw new NotAFactorNumberException(segments, len);
        }

        List<Double> reducedValues = new ArrayList<>(segments);
        if (modulo == 0 || strategy == PaaStrategy.IGNORE_REMAINING) {
            int intervalSize = len / segments;
            double sum = 0;
            for (int i = 0; i < len; i++) {
                sum += sequence.get(i);
                if ((i + 1) % intervalSize == 0) {
                    reducedValues.add(sum / intervalSize);
                    if (reducedValues.size() == segments) break;
                    sum = 0;
                }
            }

        } else if (strategy == PaaStrategy.MEAN_PADDING) {
            int intervalSize = len / segments + 1;
            double sum = 0;
            for (int i = 0; i < len; i++) {
                sum += sequence.get(i);
                if ((i + 1) % intervalSize == 0) {
                    reducedValues.add(sum / intervalSize);
                    sum = 0;
                }
            }
            modulo = len % intervalSize;
            reducedValues.add(sum / modulo);

        } else if (strategy == PaaStrategy.FRACTIONAL_PARTITION) {
            double intervalSize = len * 1.0 / segments;
            double sum = 0;
            int i = 0;
            double x = 1.0;
            int y = (int) (intervalSize - 1);
            double z = intervalSize - 1 - y;
            while (i < len) {
                if (!Precision.equals(x, 0, TimeSeriesPrecision.EPSILON)) {
                    sum += sequence.get(i) * x;
                }
                int k = i + 1 + y;
                for (int j = i + 1; j < k; j++) {
                    sum += sequence.get(j);
                }
                if (!Precision.equals(z, 0, TimeSeriesPrecision.EPSILON)) {
                    sum += sequence.get(k) * z;
                }

                reducedValues.add(sum / intervalSize);
                if (reducedValues.size() == segments) break;

                sum = 0;

                x = 1 - z;
                y = (int) (intervalSize - x);
                z = intervalSize - x - y;

                i = k;
            }
        }

        return reducedValues;
    }

    @Override
    public String toString() {
        return "PAA(" + strategy + ")";
    }
}
