import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("org.graalvm.buildtools.native") version "0.9.28"
}

dependencies {
    implementation(project(":app"))
    implementation("org.http4k:http4k-server-apache")
}

application {
    mainClass.set("http4kbox.NativeServerKt")
}

graalvmNative {
    toolchainDetection.set(true)
    binaries {
        named("main") {
            imageName.set("http4kbox")
            mainClass.set("http4kbox.NativeServerKt")
            useFatJar.set(true)
            buildArgs.add("-H:ResourceConfigurationFiles=/${project.projectDir}/src/main/resources/native/resource-config.json")
        }
    }
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set(project.name)
    archiveClassifier.set(null as String?)
    archiveVersion.set(null as String?)
    mergeServiceFiles()
}