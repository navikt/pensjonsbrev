plugins {
    val kotlinVersion: String by System.getProperties()
    val ktorVersion: String by System.getProperties()
    val kspVersion: String by System.getProperties()

    kotlin("jvm") version kotlinVersion apply false
    id("com.google.devtools.ksp") version "$kotlinVersion-$kspVersion" apply false
    id("io.ktor.plugin") version ktorVersion apply false
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
            content {
                includeGroup("no.nav.pensjon.brev") // api-model
                includeGroup("no.nav.pensjon.brevbaker") // api-model-common
            }
        }
    }
}
