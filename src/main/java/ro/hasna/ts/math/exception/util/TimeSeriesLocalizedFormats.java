/**
 * Copyright (C) 2015 Octavian Hasna
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
package ro.hasna.ts.math.exception.util;

import org.apache.commons.math3.exception.util.Localizable;

import java.util.Locale;

/**
 * Enumeration for localized messages formats used in exceptions messages.
 *
 * @see org.apache.commons.math3.exception.util.LocalizedFormats
 * @since 1.0
 */
public enum TimeSeriesLocalizedFormats implements Localizable {
    NUMBER_NOT_DIVISIBLE_WITH("{0} is not divisible with {1}"),
    OUT_OF_RANGE_BOTH("{0} out of ({1}, {2}) range"),
    UNSUPPORTED_STRATEGY("the strategy {0} is not supported in {1}");

    /**
     * Source English format.
     */
    private final String sourceFormat;

    /**
     * Simple constructor.
     *
     * @param sourceFormat source English format to use when no localized version is available
     */
    TimeSeriesLocalizedFormats(final String sourceFormat) {
        this.sourceFormat = sourceFormat;
    }

    @Override
    public String getSourceString() {
        return sourceFormat;
    }

    @Override
    public String getLocalizedString(Locale locale) {
        return sourceFormat;
    }
}
