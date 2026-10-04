#!/bin/sh
# Bootstrap helper: GitHub Actions uses Gradle's setup-gradle action.
# For local builds, install Gradle 8+ and run: gradle build
exec gradle "$@"
