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

@Data
public class FullMatrixProfile {
    private final MatrixProfile leftMatrixProfile;
    private final MatrixProfile rightMatrixProfile;

    public FullMatrixProfile(MatrixProfile leftMatrixProfile, MatrixProfile rightMatrixProfile) {
        this.leftMatrixProfile = leftMatrixProfile;
        this.rightMatrixProfile = rightMatrixProfile;
    }

    public FullMatrixProfile(int leftSize, int rightSize) {
        this(new MatrixProfile(leftSize), new MatrixProfile(rightSize));
    }
}
