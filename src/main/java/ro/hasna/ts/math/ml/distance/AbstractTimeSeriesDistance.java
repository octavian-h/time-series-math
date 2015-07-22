package ro.hasna.ts.math.ml.distance;

/**
 * @since 1.0
 */
public abstract class AbstractTimeSeriesDistance {
    protected int initialVectorLength;

    public int getInitialVectorLength() {
        return initialVectorLength;
    }

    public void setInitialVectorLength(int initialVectorLength) {
        this.initialVectorLength = initialVectorLength;
    }
}
