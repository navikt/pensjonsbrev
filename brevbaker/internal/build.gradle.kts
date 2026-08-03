import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    id("java-library")
}

group = "no.nav.brev.brevbaker"
version = "0.0.1-SNAPSHOT"

dependencies {
    // Modulen eier bare serialiseringen av markup og api-model-common; modellene selv re-eksporteres
    // ikke. Konsumenter deklarerer dem selv med versjonene fra libs.versions.toml.
    implementation(libs.brevbaker.common)
    implementation(libs.brevbaker.markup)
    api(libs.jackson.databind)
    api(libs.jackson.annotations)
    api(libs.jackson.datatype.jsr310) {
        because("we require deserialization/serialization of java.time.LocalDate")
    }
    api(libs.jackson.module.kotlin) {
        because("markup bruker value classes og default-verdier i konstruktører, som Jackson kun ser med kotlin-modulen")
    }

    testImplementation(libs.bundles.junit)
    // Den utvidede (id-eksplisitte) markup-DSL-en. Ligger i markups `apiInternal`-kildesett, som har
    // friend-tilgang til markups internals og aldri publiseres, og hentes derfor som lokal jar.
    testImplementation(project(path = ":brevbaker:markup", configuration = "apiInternalElements"))

}

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
    test {
        useJUnitPlatform()
    }
}
