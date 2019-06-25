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
package ro.hasna.ts.math.exception.util;

import org.apache.commons.math3.exception.util.DummyLocalizable;
import org.apache.commons.math3.exception.util.Localizable;

/**
 * Class for localized messages formats used in exceptions messages.
 *
 * @see org.apache.commons.math3.exception.util.LocalizedFormats
 * @since 1.0
 */
public class LocalizableMessages {
    public static final Localizable NUMBER_NOT_DIVISIBLE_WITH = new DummyLocalizable("{0} is not divisible with {1}");
    public static final Localizable OUT_OF_RANGE_BOTH_EXCLUSIVE = new DummyLocalizable("{0} out of ({1}, {2}) range");
    public static final Localizable OUT_OF_RANGE_BOTH_INCLUSIVE = new DummyLocalizable("{0} out of [{1}, {2}] range");
    public static final Localizable OPERATION_WAS_CANCELLED = new DummyLocalizable("The operation was cancelled.");

    private LocalizableMessages() {
    }
}
