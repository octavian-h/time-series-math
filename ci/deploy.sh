#!/usr/bin/env bash
#
# Copyright 2015 Octavian Hasna
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

COMMIT_MESSAGE="$(git log --format=%B -n 1 "${TRAVIS_COMMIT}")"

if [ "$TRAVIS_PULL_REQUEST" == "false" ] &&
   [ "$TRAVIS_TAG" == "" ] &&
   [[ "$COMMIT_MESSAGE" != "[maven-release-plugin]"* ]]; then

    if [[ "$COMMIT_MESSAGE" != *"[no-deploy]"* ]] &&
       [[ "$COMMIT_MESSAGE" != *"[start-release]"* ]]; then

        echo "Deploy Snapshot artifacts"
        mvn --batch-mode deploy -DskipTests=true --settings ci/maven-settings.xml

    elif [[ "$COMMIT_MESSAGE" == *"[start-release]"* ]]; then

        echo "Release artifacts"
        # point HEAD to master branch (this is needed by the maven release plugin)
        git symbolic-ref HEAD refs/heads/master

        mvn --batch-mode release:prepare --settings ci/maven-settings.xml
        mvn --batch-mode release:perform --settings ci/maven-settings.xml

        TAG="$(git describe --tags --abbrev=0)"
        PACKAGE="${TAG%-*}" # delete the shortest substring from the end that starts with "-" (inclusive)
        VERSION="${TAG##*-}" # delete the longest substring from the begging that ends with "-" (inclusive)

        echo "Sync artifact $PACKAGE:$VERSION to Maven Central"
        curl --request POST \
             --url "https://api.bintray.com/maven_central_sync/octavian-h/maven/$PACKAGE/versions/$VERSION" \
             --user octavian-h:"$BINTRAY_API_KEY" \
             --header "content-type: application/json" \
             --data "{\"username\": \"$SONATYPE_USER\",\"password\": \"$SONATYPE_TOKEN\",\"close\": \"1\"}"

    else
        echo "Skip deploy/release"
    fi
else
   echo "Skip deploy/release"
fi
