import org.gradle.api.JavaVersion.VERSION_21
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version "2.0.20"
    java
    application
    id("com.gradleup.shadow") version "8.3.1"
    id("nu.studer.rocker") version "3.0.4"
}

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}


allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "application")
    apply(plugin = "com.gradleup.shadow")
    apply(plugin = "nu.studer.rocker")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(platform("org.http4k:http4k-bom:${project.properties["http4k_version"]}"))
        implementation(platform("org.http4k:http4k-connect-bom:${project.properties["http4k_connect_version"]}"))
        implementation(platform("dev.forkhandles:forkhandles-bom:${project.properties["forkhandles_version"]}"))
        testApi(platform("org.junit:junit-bom:${project.properties["junit_version"]}"))
        testApi("org.http4k:http4k-testing-hamkrest")
        testApi("org.http4k:http4k-connect-amazon-s3-fake")
        testApi("org.junit.jupiter:junit-jupiter-api")
        testApi("org.junit.jupiter:junit-jupiter-engine")
        testApi("org.jetbrains.kotlin:kotlin-reflect:${project.properties["kotlin_version"]}")
    }


    tasks {
        withType<KotlinJvmCompile>().configureEach {
            compilerOptions {
                allWarningsAsErrors = false
                jvmTarget.set(JVM_21)
            }
        }

        withType<Test> {
            useJUnitPlatform()
        }

        java {
            sourceCompatibility = VERSION_21
            targetCompatibility = VERSION_21
        }
    }
}

tasks.register("stage") {
    dependsOn("installDist")
}

dependencies {
    implementation(project(":heroku-app"))
}