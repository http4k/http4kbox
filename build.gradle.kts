import org.gradle.api.JavaVersion.VERSION_11
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version "2.0.20"
    java
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

    repositories {
        mavenCentral()
    }


    tasks {
        withType<KotlinJvmCompile>().configureEach {
            compilerOptions {
                allWarningsAsErrors = false
                jvmTarget.set(JVM_11)
            }
        }

        withType<Test> {
            useJUnitPlatform()
        }

        java {
            sourceCompatibility = VERSION_11
            targetCompatibility = VERSION_11
        }
    }
}
