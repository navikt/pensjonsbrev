plugins {
    kotlin("jvm") version libs.versions.kotlinVersion apply false
    id("com.google.devtools.ksp") version libs.versions.kotlinKspVersion apply false
    id("io.ktor.plugin") version libs.versions.ktorVersion apply false
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
