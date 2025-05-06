plugins {
    kotlin("jvm") version libs.versions.kotlinVersion apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.binary.compatibility.validator) apply false
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
