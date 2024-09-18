dependencies {
    api(platform("org.http4k:http4k-bom:${project.properties["http4k_version"]}"))
    api(platform("org.http4k:http4k-connect-bom:${project.properties["http4k_connect_version"]}"))
    api("org.http4k:http4k-aws")
    api("org.http4k:http4k-core")
    api("org.http4k:http4k-config")
    api("org.http4k:http4k-connect-amazon-s3")
    api("org.http4k:http4k-multipart")
    api("org.http4k:http4k-template-handlebars")
}
