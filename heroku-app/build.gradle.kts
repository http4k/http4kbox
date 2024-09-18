dependencies {
    implementation(project(":app"))
    implementation("org.http4k:http4k-server-apache")
}

application {
    mainClass.set("http4kbox.HerokuServerKt")
}

