import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation(project(":app"))
    implementation("org.http4k:http4k-server-undertow")
    implementation("org.http4k:http4k-client-apache")
}

application {
    mainClass.set("http4kbox.LocalServerKt")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set(project.name)
    archiveClassifier.set(null as String?)
    archiveVersion.set(null as String?)
    mergeServiceFiles()
}