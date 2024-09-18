#!/usr/bin/env bash

set -e

../gradlew :native:shadowJar :native:nativeCompile
