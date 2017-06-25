# http4k + Heroku + Travis 

[![build status](https://travis-ci.org/http4k/http4k-heroku-travis-example-app.svg?branch=master)](https://travis-ci.org/http4k/http4k-heroku-travis-example-app.svg?branch=master)

This application gets deployed to Heroku on every push to GitHub

## Pre-requisites

You need to have installed:
* [Heroku Toolbelt](https://devcenter.heroku.com/articles/heroku-cli#download-and-install)
* [Travis CLI](https://github.com/travis-ci/travis.rb#installation)

## Getting Started

* Fork this repo
* Configure [Travis](https://travis-ci.org) to build the new repo
* Create your Heroku app:

```bash
heroku apps:create my-awesome-app
```

* Update the `app` entry in .travis.yml
* Update the deployment credentials

```bash
travis encrypt $(heroku auth:token) --add deploy.api_key
```

* Commit and push your changes to GitHub:

```bash
git commit -am"Update travis config"
git push origin master
```

This will automatically trigger a new build and deployment of your app.

## Running it locally

```bash
./gradlew stage
heroku local web
```

The app will be available on [http://localhost:5000](http://localhost:5000)

## Deploying it manually

```bash
git push heroku master
```
