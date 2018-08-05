# http4kbox in a GraalVM

Requires:
- Docker + docker-compose

Steps to get this running:
1. You will need an S3 bucket created with write access.
1. Make a copy of `http4kbox.env.example` named `http4kbox.env` and complete.
1. Run `build.sh` - this builds the app JAR in gradle and then builds a Docker image containing the native binary.
1. Run `run.sh` - this runs 2 containes:
    1. A http->https proxy - this is required because GraalVM currently does not support https. 
    2. The app, which will run by default at [http://localhost:10000].