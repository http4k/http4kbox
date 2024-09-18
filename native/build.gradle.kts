import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.graalvm.buildtools.gradle.tasks.BuildNativeImageTask

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
            buildArgs.add("--enable-http")
            buildArgs.add("--enable-url-protocols=https")
        }
    }
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("all")
        archiveClassifier.set(null as String?)
        archiveVersion.set(null as String?)
        mergeServiceFiles()
    }

    named("generateResourcesConfigFile") {
        dependsOn(named("shadowJar"))
    }

    named<BuildNativeImageTask>("nativeCompile") {
        classpathJar.set(File("./build/libs/all.jar"))
    }
}
