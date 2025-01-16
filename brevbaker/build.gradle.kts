import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

group = "no.nav.brev.brevbaker"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    api(project(":brevbaker-api-model-mal"))
    ksp(project(":template-model-generator"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}


kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(apiModelJavaTarget))
    }
    sourceSets {
        main {
            kotlin.srcDir("build/generated/ksp/main/kotlin")
        }
        test {
            kotlin.srcDir("build/generated/ksp/test/kotlin")
        }
    }
}
tasks {
    compileJava {
        targetCompatibility = apiModelJavaTarget
    }
    compileTestJava {
        targetCompatibility = apiModelJavaTarget
    }
}
