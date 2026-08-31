import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    id("java-library")
}

group = "no.nav.brev.brevbaker"
version = "0.0.1-SNAPSHOT"

dependencies {
    // Modulen eier bare serialiseringen av intern trafikk; modellene selv re-eksporteres ikke.
    // Konsumenter deklarerer dem selv.
    implementation(publishedLibs.brevbaker.api)
    implementation(publishedLibs.markup.model)
    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.databind)
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.datatype.jsr310) {
        because("we require deserialization/serialization of java.time.LocalDate")
    }
    implementation(libs.jackson.module.kotlin) {
        because("markup bruker value classes og default-verdier i konstruktører, som Jackson kun ser med kotlin-modulen")
    }

    testImplementation(libs.bundles.junit)
    // Den utvidede (id-eksplisitte) markup-DSL-en, brukt til å bygge testdata. Gatet med
    // @ExtendedMarkupDsl, som testkompileringen opter inn på nedenfor.
    testImplementation(publishedLibs.markup.dsl)

}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaTarget))
    }
}

tasks.named<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileTestKotlin") {
    compilerOptions {
        // Golden-fixturene bygger markup med eksplisitte id-er, akkurat som core gjør i produksjon.
        optIn.add("no.nav.brev.brevbaker.markup.dsl.extended.ExtendedMarkupDsl")
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
