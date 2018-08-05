#!/usr/bin/env bash

source http4kbox.env

echo "http4kbox will run at: http://localhost:${HTTP_PORT}"
java -jar build/libs/local.jar
