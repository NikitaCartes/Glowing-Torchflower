pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.6"
}

stonecutter {
    create(rootProject) {
        versions("26.2-fabric" to "26.2").buildscript("build.fabric.gradle.kts")
        versions("26.2-neoforge" to "26.2").buildscript("build.neoforge.gradle.kts")
        vcsVersion = "26.2-fabric"
    }
}
