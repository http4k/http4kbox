#!/usr/bin/env bash

export $(cat http4kbox.env | xargs)

echo "http4kbox will run at: http://localhost:${HTTP_PORT}"
java -jar build/libs/local.jar
