import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    id("java-library")
    id("java-test-fixtures")
    alias(libs.plugins.ksp) apply true
    alias(libs.plugins.binary.compatibility.validator) apply true
}

group = "no.nav.brev.brevbaker"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

dependencies {
    api(project(":brevbaker-dsl"))
    api(libs.brevbaker.common)
    ksp(project(":template-model-generator"))
    implementation(libs.kotlinx.html)

    testImplementation(kotlin("test"))
    testImplementation(libs.hamkrest)

    testImplementation(testFixtures(project(":brevbaker-dsl")))

    testFixturesImplementation(libs.ktor.serialization.jackson)
    testFixturesImplementation(libs.ktor.client.cio)
    testFixturesImplementation(libs.ktor.client.content.negotiation)
    testFixturesImplementation(libs.ktor.client.encoding)
    testFixturesImplementation(libs.ktor.server.callId)

    testFixturesImplementation(libs.bundles.logging)

    testFixturesImplementation(libs.jackson.datatype.jsr310) {
        because("we require deserialization/serialization of java.time.LocalDate")
    }
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
    kotlin {
        jvmToolchain(apiModelJavaTarget.toInt())
        compileKotlin {
            compilerOptions.optIn.add("no.nav.brev.InternKonstruktoer")
        }
        compileTestKotlin {
            compilerOptions.optIn.add("no.nav.brev.InterneDataklasser")
            compilerOptions.optIn.add("no.nav.brev.InternKonstruktoer")
        }
        compileTestFixturesKotlin {
            compilerOptions.optIn.add("no.nav.brev.InterneDataklasser")
            compilerOptions.optIn.add("no.nav.brev.InternKonstruktoer")
        }
    }
    compileJava {
        targetCompatibility = apiModelJavaTarget
    }
    compileTestJava {
        targetCompatibility = apiModelJavaTarget
    }
}
