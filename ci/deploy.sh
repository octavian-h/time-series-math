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

COMMIT_MESSAGE="$(git log --format=%s --max-count 1)"

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [[ "$COMMIT_MESSAGE" != *"[no-deploy]"* ]]; then
  openssl aes-256-cbc -K "$encrypted_e0cb3f1fc6c0_key" -iv "$encrypted_e0cb3f1fc6c0_iv" -in ci/private-key.gpg.enc -out private-key.gpg -d
  gpg --import private-key.gpg
  mvn deploy --batch-mode -DskipTests=true --settings ci/maven-settings.xml -P release
else
  echo "Skip deploy"
fi
