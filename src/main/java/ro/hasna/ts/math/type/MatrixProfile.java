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
package ro.hasna.ts.math.type;

import lombok.Data;
import org.apache.commons.math3.exception.DimensionMismatchException;

@Data
public class MatrixProfile implements Cloneable {
    private final double[] profile;
    private final int[] indexProfile;

    public MatrixProfile(double[] profile, int[] indexProfile) {
        if (profile.length != indexProfile.length) {
            throw new DimensionMismatchException(profile.length, indexProfile.length);
        }

        this.profile = profile;
        this.indexProfile = indexProfile;
    }

    public MatrixProfile(int size) {
        profile = new double[size];
        indexProfile = new int[size];
        for (int j = 0; j < size; j++) {
            profile[j] = Double.POSITIVE_INFINITY;
            indexProfile[j] = -1;
        }
    }

    @Override
    public MatrixProfile clone() {
        int size = profile.length;
        MatrixProfile copy = new MatrixProfile(size);
        for (int i = 0; i < size; i++) {
            copy.profile[i] = profile[i];
            copy.indexProfile[i] = indexProfile[i];
        }
        return copy;
    }
}
