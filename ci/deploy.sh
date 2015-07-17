#!/usr/bin/env bash

COMMIT_MESSAGE="$(git log --format=%B -n 1 $TRAVIS_COMMIT)"

if [ "$TRAVIS_PULL_REQUEST" == "false" ] &&
   [ "$TRAVIS_TAG" == "" ] &&
   [[ "$COMMIT_MESSAGE" != "[maven-release-plugin]"* ]]; then

    if [[ "$COMMIT_MESSAGE" != *"[no-deploy]"* ]] &&
       [[ "$COMMIT_MESSAGE" != *"[start-release]"* ]]; then

        echo "Deploy Snapshot artifacts"
        mvn --batch-mode deploy -DskipTests=true --settings ci/maven-settings.xml

    elif [[ "$COMMIT_MESSAGE" == *"[start-release]"* ]]; then

        echo "Release artifacts"
        git branch -va
        git symbolic-ref HEAD
        git symbolic-ref HEAD refs/heads/master

        mvn --batch-mode release:prepare --settings ci/maven-settings.xml
        mvn --batch-mode release:perform --settings ci/maven-settings.xml

    else
        echo "Skip deploy/release"
    fi
else
   echo "Skip deploy/release"
fi
