/**
 * Copyright (C) 2016-2015 Octavian Hasna
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
package ro.hasna.ts.math.type;

import org.apache.commons.math3.util.Pair;

/**
 * @since 1.0
 */
public class SaxPair extends Pair<Integer, Integer> {

    /**
     * Create an entry of type (symbol, alphabetSize).
     *
     * @param symbol       the symbol value for the segment
     * @param alphabetSize the size of the alphabet used by the symbol
     */
    public SaxPair(int symbol, int alphabetSize) {
        super(symbol, alphabetSize);
    }

    public int getSymbol() {
        return getFirst();
    }

    public int getAlphabetSize() {
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
        if (!(o instanceof SaxPair)) {
            return false;
        } else {
            SaxPair saxPair = (SaxPair) o;
            return getFirst() == saxPair.getSymbol() && getSecond() == saxPair.getAlphabetSize();
        }
    }
}
