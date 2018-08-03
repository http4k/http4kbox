#!/usr/bin/env bash

source http4kbox.env

echo "http4kbox will run at: http://localhost:${HTTP_PORT}"
docker-compose up
