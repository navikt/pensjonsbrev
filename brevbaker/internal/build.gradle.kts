import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
    id("java-library")
    id("java-test-fixtures")
}

group = "no.nav.brev.brevbaker"
version = "0.0.1-SNAPSHOT"

// Dette er den interne delingsmodulen mellom brevbaker, skribenten og pdf-bygger.
//
// Den skal ALDRI publiseres: den er bevisst uten `maven-publish` og uten `abiValidation`, slik at
// interne konsepter ikke kan lekke ut i et eksternt kontrakts-artefakt slik de har gjort i
// api-model-common tidligere.
//
// Modulen konsumerer api-model-common og markup via *publiserte* koordinater (ikke project-deps), og
// re-eksporterer dem med `api(...)`. Det betyr at brevbaker, skribenten og pdf-bygger kun ser API
// som faktisk er publisert. Ved lokale endringer i disse to modulene må de først publiseres til
// mavenLocal:
//
//   ./gradlew :brevbaker:api-model-common:publishToMavenLocal :brevbaker:markup:publishToMavenLocal
dependencies {
    api(libs.brevbaker.common)
    api(libs.brevbaker.markup)

    // Serialiseringen for all intern kommunikasjon bor her, ikke i markup (som skal være uten
    // avhengigheter) og ikke spredt utover konsumentene.
    // TODO: midlertidig – serialiseringen skal over på Jackson (se plan, steg internal-jackson-markup).
    api(libs.kotlinx.serialization.json)

    api(libs.jackson.databind)
    api(libs.jackson.annotations)
    implementation(libs.jackson.datatype.jsr310) {
        because("we require deserialization/serialization of java.time.LocalDate")
    }

    testImplementation(libs.bundles.junit)

    testFixturesApi(libs.testcontainers.core)
    testFixturesImplementation(libs.bundles.logging)
    testFixturesImplementation(libs.ktor.client.cio)
    testFixturesImplementation(libs.ktor.client.content.negotiation)
    testFixturesImplementation(libs.ktor.serialization.jackson)
    testFixturesImplementation(libs.jackson.datatype.jsr310)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaTarget))
        // Modulen er den eneste tiltenkte brukeren av markups interne konstruksjons-seams.
        optIn.add("no.nav.brev.brevbaker.markup.MarkupInternalApi")
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
