plugins {
    val kotlinVersion: String by System.getProperties()
    val kspVersion: String by System.getProperties()

    kotlin("jvm") version kotlinVersion apply false
    id("com.google.devtools.ksp") version "$kotlinVersion-$kspVersion" apply false
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
}