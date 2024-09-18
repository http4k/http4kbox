# http4kbox in a GraalVM

NOTE: Currently, work is underway to make GraalVm support the `javax.crypto` packages, so currently the 
http4kbox application will not run because it requires them to sign AWS requests. This is actively being worked on by the GraalVm team, but is not in the most recent release (`graal 1.0.0-rc6`). The scripts will correctly build and run the Docker image containing the binary, but it will fail at runtime.

Requires:
- Docker + docker-compose

Steps to get this running:
1. You will need an S3 bucket created with write access.
1. Run `build.sh` - this builds the native image
1. Make a copy of `http4kbox.env.example` named `http4kbox.env` and complete the required values.
1. Run `run.sh`