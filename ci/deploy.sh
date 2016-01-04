#!/usr/bin/env bash
#
# Copyright (C) 2016-2015 Octavian Hasna
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

COMMIT_MESSAGE="$(git log --format=%B -n 1 ${TRAVIS_COMMIT})"

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

    else
        echo "Skip deploy/release"
    fi
else
   echo "Skip deploy/release"
fi
