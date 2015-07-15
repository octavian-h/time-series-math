package ro.hasna.ts.math.distribution;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements the equal areas of probability for Unit Normal Distribution.
 *
 * @since 1.0
 */
public class NormalDistributionDivider implements DistributionDivider {
    public static final int PRE_COMPUTED_BREAKPOINTS = 256;
    private static final String RESOURCE_NAME = "normal-distribution-breakpoints.csv";
    private Map<Integer, double[]> breakpoints;
    private boolean cachingEnabled;

    /**
     * Creates a new instance of this class with caching enable.
     */
    public NormalDistributionDivider() {
        this(true);
    }

    /**
     * Creates a new instance of this class
     *
     * @param cachingEnabled flag to enable or not the caching
     */
    public NormalDistributionDivider(boolean cachingEnabled) {
        this.cachingEnabled = cachingEnabled;
        try {
            readBreakpoints();
        } catch (IOException e) {
            throw new MathInternalError(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] getBreakpoints(int areas) {
        if (areas < 2) {
            throw new NumberIsTooSmallException(areas, 2, true);
        }

        double[] result = breakpoints.get(areas);
        if (result != null) {
            return result;
        }

        NormalDistribution normalDistribution = new NormalDistribution();
        int len = areas - 1;
        result = new double[len];
        double searchArea = 1.0 / areas;
        for (int i = 0; i < len; i++) {
            result[i] = normalDistribution.inverseCumulativeProbability(searchArea * (i + 1));
        }

        if (cachingEnabled) {
            breakpoints.put(areas, result);
        }

        return result;
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

    // Utility functions

    private void readBreakpoints() throws IOException {
        breakpoints = new HashMap<>(PRE_COMPUTED_BREAKPOINTS);
        InputStream stream = getClass().getResourceAsStream(RESOURCE_NAME);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            String read = br.readLine();

            int n = 1;
            while (read != null) {
                String[] v = read.split(";");
                double[] list = new double[n];
                for (int i = 0; i < v.length; i++) {
                    list[i] = Double.parseDouble(v[i]);
                }
                breakpoints.put(++n, list);

                read = br.readLine();
            }
        }
    }
}
