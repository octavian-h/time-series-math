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
