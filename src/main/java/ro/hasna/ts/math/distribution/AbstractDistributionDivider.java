package ro.hasna.ts.math.distribution;

import org.apache.commons.math3.exception.NumberIsTooSmallException;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class used for caching breakpoints of a DistributionDivider implementation.
 *
 * @since 1.0
 */
public abstract class AbstractDistributionDivider implements DistributionDivider {
    private Map<Integer, double[]> breakpointsMap;
    private boolean cachingEnabled;

    public AbstractDistributionDivider() {
        this(true);
    }

    public AbstractDistributionDivider(boolean cachingEnabled) {
        this.cachingEnabled = cachingEnabled;
        this.breakpointsMap = new HashMap<>();
    }

    public AbstractDistributionDivider(boolean cachingEnabled, int initialCapacity) {
        this.cachingEnabled = cachingEnabled;
        this.breakpointsMap = new HashMap<>(initialCapacity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] getBreakpoints(int areas) {
        if (areas < 2) {
            throw new NumberIsTooSmallException(areas, 2, true);
        }

        double[] result = breakpointsMap.get(areas);
        if (result != null) {
            return result;
        }

        result = computeBreakpoints(areas);

        if (cachingEnabled) {
            breakpointsMap.put(areas, result);
        }

        return result;
    }

    protected abstract double[] computeBreakpoints(int areas);

    /**
     * Save breakpoints in the cache.
     *
     * @param breakpoints the values for dividing the distribution in equal areas of probability
     */
    public void saveBreakpoints(double[] breakpoints) {
        breakpointsMap.put(breakpoints.length + 1, breakpoints);
    }

    /**
     * @return the value of the caching flag
     */
    public boolean isCachingEnabled() {
        return cachingEnabled;
    }

    /**
     * Set the caching flag. If the value is true then the computed breakpoints
     * are saved to an internal cache to be retrieved faster.
     *
     * @param cachingEnabled the caching flag.
     */
    public void setCachingEnabled(boolean cachingEnabled) {
        this.cachingEnabled = cachingEnabled;
    }
}
