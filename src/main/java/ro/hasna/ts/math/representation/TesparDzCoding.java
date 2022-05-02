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
package ro.hasna.ts.math.representation;

import org.apache.commons.math3.util.FastMath;
import ro.hasna.ts.math.type.TesparSymbol;

/**
 * Implements the Time Encoding Signal Processing and Recognition (TESPAR) coding method.
 * <p>
 * Reference:
 * King R.A., Phipps T.C. (1998)
 * <i>Shannon, TESPAR And Approximation Strategies</i>
 * </p>
 *
 * @since 0.7
 */
public class TesparDzCoding implements GenericTransformer<double[], int[]> {
    private static final long serialVersionUID = 7734158131264856074L;

    @Override
    public int[] transform(double[] values) {
        TesparSymbol[] symbols = getTesparSymbols(values);
        return getHistogram(symbols);
    }

    private int[] getHistogram(TesparSymbol[] symbols) {
        int[] result = new int[27];
        for (int i = 1; i < symbols.length; i++) {
            TesparSymbol currentSymbol = symbols[i];
            TesparSymbol prevSymbol = symbols[i - 1];

            int code = 14;
            if (currentSymbol.getAmplitude() > prevSymbol.getAmplitude()) {
                code -= 1;
            } else if (currentSymbol.getAmplitude() < prevSymbol.getAmplitude()) {
                code += 1;
            }
            if (currentSymbol.getShape() > prevSymbol.getShape()) {
                code -= 3;
            } else if (currentSymbol.getShape() < prevSymbol.getShape()) {
                code += 3;
            }
            if (currentSymbol.getDuration() > prevSymbol.getDuration()) {
                code -= 9;
            } else if (currentSymbol.getDuration() < prevSymbol.getDuration()) {
                code += 9;
            }
            result[code - 1]++;
        }
        return result;
    }

    private TesparSymbol[] getTesparSymbols(double[] values) {
        int n = 0;
        for (int i = 1; i < values.length; i++) {
            if (values[i - 1] != values[i] &&
                    ((values[i - 1] <= 0 && values[i] >= 0) || (values[i - 1] >= 0 && values[i] <= 0))) {
                n++;
            }
        }
        TesparSymbol[] result = new TesparSymbol[n];
        double amplitude = values[0];
        int start = 0;
        int shape = 0;
        n = 0;
        for (int i = 1; i < values.length; i++) {
            if (values[i - 1] != values[i] &&
                    ((values[i - 1] <= 0 && values[i] >= 0) || (values[i - 1] >= 0 && values[i] <= 0))) {
                result[n] = new TesparSymbol(i - start, shape, FastMath.abs(amplitude));
                start = i;
                shape = 0;
                amplitude = values[i];
                n++;
            } else {
                if (i < values.length - 1 && isShape(values[i - 1], values[i], values[i + 1])) {
                    shape++;
                }
                if (values[i] > 0 && values[i] > amplitude) {
                    amplitude = values[i];
                }
                if (values[i] < 0 && values[i] < amplitude) {
                    amplitude = values[i];
                }
            }
        }

        return result;
    }

    private boolean isShape(double a, double b, double c) {
        /* true if is local minimum for positive values or
        local maximum for negative values otherwise false */
        if (b > 0 && c > 0 && a > b && b < c) {
            return true;
        }
        return b < 0 && c < 0 && a < b && b > c;
    }
}
