#!/usr/bin/env bash

set -e

../gradlew shadowJar

docker build -t daviddenton/http4kbox-native -t daviddenton/http4kbox .