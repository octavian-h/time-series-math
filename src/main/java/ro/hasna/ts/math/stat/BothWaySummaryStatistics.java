/*
 * Copyright 2015 Octavian Hasna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.hasna.ts.math.stat;

import org.apache.commons.math3.exception.InsufficientDataException;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import org.apache.commons.math3.util.FastMath;

import java.io.Serializable;

/**
 * Compute iterative statistics when adding or removing values.
 *
 * @since 0.17
 */
public class BothWaySummaryStatistics implements StatisticalSummary, Serializable, Cloneable {
    private static final long serialVersionUID = -8419769227216721345L;
    private final Max max;
    private final Min min;
    private long n;
    private double sum;
    private double sumSquares;
    private boolean removeMade;

    public BothWaySummaryStatistics() {
        n = 0;
        sum = 0;
        sumSquares = 0;
        max = new Max();
        min = new Min();
        removeMade = false;
    }

    public void addValue(double d) {
        sum += d;
        sumSquares += d * d;
        n++;
        if (!removeMade) {
            max.increment(d);
            min.increment(d);
        }
    }

    public void removeValue(double d) {
        if (n == 0) {
            throw new InsufficientDataException();
        }

        sum -= d;
        sumSquares -= d * d;
        n--;
        removeMade = true;
        max.clear();
        min.clear();
    }

    @Override
    public double getMean() {
        if (n == 0) return 0;
        return sum / n;
    }

    @Override
    public double getVariance() {
        if (n == 0) return Double.NaN;
        if (n == 1) return 0;
        double mean = getMean();
        return sumSquares / n - mean * mean;
    }

    @Override
    public double getStandardDeviation() {
        if (n == 0) return Double.NaN;
        if (n == 1) return 0;
        return FastMath.sqrt(getVariance());
    }

    /**
     * @return max value or Double.NaN if removeValue was called
     */
    @Override
    public double getMax() {
        return max.getResult();
    }

    /**
     * @return min value or Double.NaN if removeValue was called
     */
    @Override
    public double getMin() {
        return min.getResult();
    }

    @Override
    public long getN() {
        return n;
    }

    @Override
    public double getSum() {
        return sum;
    }

    @Override
    public BothWaySummaryStatistics clone() {
        BothWaySummaryStatistics copy = new BothWaySummaryStatistics();
        copy.n = n;
        copy.sum = sum;
        copy.sumSquares = sumSquares;
        return copy;
    }
}