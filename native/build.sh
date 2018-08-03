#!/usr/bin/env bash
../gradlew shadowJar

docker build -t daviddenton/http4kbox-native -t daviddenton/http4kbox .