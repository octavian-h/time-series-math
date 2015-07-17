#!/usr/bin/env bash

COMMIT_MESSAGE="$(git log --format=%B -n 1 $TRAVIS_COMMIT)"

if [ "$TRAVIS_PULL_REQUEST" == "false" ] &&
   [ "$TRAVIS_TAG" == "" ] &&
   [[ "$COMMIT_MESSAGE" != *"[no-deploy]"* ]] &&
   [[ "$COMMIT_MESSAGE" != "[maven-release-plugin]"* ]] &&
   [[ "$COMMIT_MESSAGE" != *"[start-release]"* ]]; then

   echo "Deploy Snapshot artifacts"
   mvn deploy -DskipTests=true --settings ci/maven-settings.xml
else
   echo "Skip deploy"
fi
