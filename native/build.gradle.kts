import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation(project(":app"))
    implementation("org.http4k:http4k-server-apache")
}

application {
    mainClass.set("http4kbox.NativeServerKt")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set(project.name)
    archiveClassifier.set(null as String?)
    archiveVersion.set(null as String?)
    mergeServiceFiles()
}