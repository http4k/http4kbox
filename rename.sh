#!/bin/bash

set -e
set -o errexit
set -o pipefail
set -o nounset

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

NEW_NAME=${1:-}

if [ -z ${NEW_NAME} ]; then
    echo "Usage: $0 <new-name>"
    exit 1
fi

sed -i '' "s/rootProject.name = '.*'/rootProject.name = '${NEW_NAME}'/g" ${DIR}/settings.gradle
sed -i '' "s/app: .*/app: ${NEW_NAME}/g" ${DIR}/.travis.yml
sed -i '' "s@build/install/.*/bin/.* \$PORT@build/install/${NEW_NAME}/bin/${NEW_NAME} \$PORT@g" ${DIR}/Procfile