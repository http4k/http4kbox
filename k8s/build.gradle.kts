dependencies {
    implementation(project(":app"))
    implementation("org.http4k:http4k-server-jetty")
}

application {
    mainClass.set("http4kbox.K8sServerKt")
}

tasks {
    shadowJar {
        archiveBaseName.set(project.name)
        archiveClassifier = null
        archiveVersion = null
        mergeServiceFiles()
        dependsOn(distTar, distZip)
        isZip64 = true
    }
}