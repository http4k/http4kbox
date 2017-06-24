# http4k + Heroku + Travis 

[![build status](https://travis-ci.org/http4k/http4k-heroku-travis-example-app.svg?branch=master)](https://travis-ci.org/http4k/http4k-heroku-travis-example-app.svg?branch=master)

This application gets deployed to Heroku on every push to GitHub

## Pre-requisites

You need to have a Heroku account and the 
[Heroku Toolbelt](https://devcenter.heroku.com/articles/heroku-cli#download-and-install) to run some of the commands
 described below.

## Getting Started

Create and deploy:

```bash
heroku apps:create my-awesome-app
git push heroku master
```

## Running it locally

Run:

```bash
./gradlew stage
heroku local web
```

The app will be available on [http://localhost:5000](http://localhost:5000)

## Deploying using TravisCI

* Fork this repo 
* Change the `heroku` section in .travis.yml to use your app and api_key
* Add Travis to your new repo

