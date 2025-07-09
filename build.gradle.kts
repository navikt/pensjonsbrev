import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

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
    tasks.withType<KotlinJvmCompile>{
        /*
        Denne er for å unngå unødige advarsler om https://youtrack.jetbrains.com/issue/KT-73255
        Vi bruker egentlig bare konstruktør-varianten, men vil egentlig helst holde oss til kotlin sin standardvariant
        Så når dette er blitt standarden i kotlin - som det skal bli - så kan vi skru av denne
         */
        compilerOptions {
            freeCompilerArgs = listOf("-Xannotation-default-target=param-property")
        }
    }
}