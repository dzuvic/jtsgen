#!/usr/bin/env bash

# check if snapshot can be deployed to maven central
# adapted from  https://github.com/JakeWharton/butterknife/blob/master/.buildscript/deploy_snapshot.sh

set -e

TSGEN_REPO="dzuvic/jtsgen"
JDK="oraclejdk8"
BRANCH="master"
GRADLE_PROJECT_VERSION="$( ./gradlew properties | grep "^version:" | cut -f2 -d":" )"


if   [ -z "${OSSRH_USER}" ] || [-z "${OSSRH_PASS}" ] ; then
  echo "Skipping snapshot deployment: credentials not set"
elif [[ "${GRADLE_PROJECT_VERSION}" != *"SNAPSHOT"* ]] ; then
  echo "skipping snapshot deployment: Not a snapshot version. Version is '${GRADLE_PROJECT_VERSION}'"
elif [ "${TRAVIS_REPO_SLUG}" != "${TSGEN_REPO}" ]; then
  echo "Skipping snapshot deployment: Expected '${TSGEN_REPO}' but was '${TRAVIS_REPO_SLUG}'. This might be a fork."
elif [ "${TRAVIS_JDK_VERSION}" != "${JDK}" ]; then
  echo "Skipping snapshot deployment: wrong JDK. Expected '$JDK' but was '$TRAVIS_JDK_VERSION'."
elif [ "${TRAVIS_PULL_REQUEST}" != "false" ]; then
  echo "Skipping snapshot deployment: was pull request."
elif [ "${TRAVIS_BRANCH}" != "${BRANCH}" ]; then
  echo "Skipping snapshot deployment: wrong branch. Expected '${BRANCH}' but was '${TRAVIS_BRANCH}'."
else
  echo "Start deploying snapshot version ${GRADLE_PROJECT_VERSION}"
  ./gradlew uploadArchives && echo "Snapshot deployed!"
fi