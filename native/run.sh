#!/usr/bin/env bash

set -e

export $(cat  ./http4kbox.env | xargs) && ./build/native/nativeCompile/http4kbox