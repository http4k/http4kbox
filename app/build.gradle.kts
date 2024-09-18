
dependencies {
    api("org.http4k:http4k-core")
    api("org.http4k:http4k-config")
    api("org.http4k:http4k-htmx")
    api("org.http4k:http4k-multipart")
    api("org.http4k:http4k-template-rocker")
    api("org.http4k:http4k-connect-amazon-s3")
    api("dev.forkhandles:values4k")

    compileOnly("com.fizzed:rocker-compiler:1.3.0")
}

rocker {
    version.set("1.3.0")
    configurations {
        create("main") {
            templateDir.set(file("src/main/resources"))
            outputDir.set(file("src/main/generated/kotlin"))
            classDir.set(file("build/classes"))
            extendsModelClass.set("org.http4k.template.RockerViewModel")
        }
    }
}

