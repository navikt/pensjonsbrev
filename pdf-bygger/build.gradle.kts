import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    application
    alias(libs.plugins.ktor) apply true
}

group="no.nav.pensjon.brev"
version="0.0.1-SNAPSHOT"

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaTarget))
    }
}

tasks {
    compileJava {
        targetCompatibility = javaTarget
    }
    compileTestJava {
        targetCompatibility = javaTarget
    }

}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.bundles.logging)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.callId)
    implementation(libs.ktor.server.callLogging)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.compression.jvm)
    implementation(libs.kafka.streams)
    implementation(libs.connect.runtime)

    implementation(libs.bundles.metrics)

    implementation(project(":brevbaker-dsl"))
    implementation(libs.brevbaker.common)

    implementation(libs.jackson.datatype.jsr310) {
        because("we require deserialization/serialization of java.time.LocalDate")
    }

    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.hamkrest)
    testImplementation(libs.ktor.server.test.host)

    testImplementation(project(":brevbaker"))
    testImplementation(testFixtures(project(":brevbaker")))
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

ktor {
    fatJar {
        archiveFileName.set("${project.name}.jar")
    }
}

tasks {
    // Dette føles meningsløst å møtte gjøre, men rocksdb-biblioteket som kommer transitivt med kafka-streams trekker med seg alle disse binærfilene som vi ikke vil ha med i imaget - der vil vi kun ha for plattformen vi kjører på, altså linux/amd64.
    // Dette er delvis henta fra https://robjohnson.dev/posts/thin-jars/
    // Vi trigger denne oppgava fra GitHub Actions-arbeidsflyten, men den kan fint kjøres lokalt også - men den er tilpassa å fjerne alt unntatt amd64, så da er du avhengig av å ha rett plattform.
    task<Exec>("rydd") {
        if (file("./build/libs/pdf-bygger.jar").exists()) {
            commandLine("zip", "--delete", "./build/libs/pdf-bygger.jar",
                "librocksdbjni-linux32-musl.so", "librocksdbjni-linux32.so", "librocksdbjni-linux64.so", "librocksdbjni-linux-ppc64le.so", "librocksdbjni-linux-ppc64le-musl.so", "librocksdbjni-linux-aarch64.so", "librocksdbjni-linux-aarch64-musl.so", "librocksdbjni-linux-s390x.so", "librocksdbjni-linux-s390x-musl.so", "librocksdbjni-win64.dll", "librocksdbjni-osx-arm64.jnilib", "librocksdbjni-osx-x86_64.jnilib")
                .also { it.setIgnoreExitValue(true) }
        } else {
            commandLine("echo", "pdf-bygger-jar fins ikke, gjør ingenting")
        }
    }
}