dependencies {
    api(project(":app"))
    api("org.http4k:http4k-server-helidon")
    api("org.http4k:http4k-client-helidon")
}

application {
    mainClass.set("http4kbox.HerokuServerKt")
}

