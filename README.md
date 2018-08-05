# http4kbox - http4k app deployed in multiple modes 

This is a simple Dropbox clone built with [**http4k**](https://http4k.org) which uses S3 as a backing store, implemented in<100 lines of code (when imports are excluded). It is designed to demo that http4k apps run identically in the following modes:

1. As a Kotlin function - see [app](./app)
1. In a deployed local server (Undertow) - see [local](./local)
1. As a cloud-based app deployed to Heroku through a fully CD pipeline run on Travis - see [cloud](./cloud)
1. As a function deployed in a Serverless environment (AWS Lambda) - see [serverless](./serverless)
1. As a native binary running under GraalVM - see [native](./native)

It uses the following [**http4k**](https://http4k.org) modules and features:

- http4k core `http4k-core`
- Jetty server module `http4k-server-jetty`
- Apache HTTP client `http4k-client-apache`
- AWS `http4k-aws` <-- This replaces the Java AWS SDK.
- Handlebars templating `http4k-template-handlebars`
- http4k multipart forms `http4k-multipart`

## In action:

<img src="https://github.com/daviddenton/http4k-demo-s3box/raw/master/screenshot.png"/>
