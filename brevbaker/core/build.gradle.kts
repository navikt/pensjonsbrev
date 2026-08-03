import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
    id("java-library")
    id("java-test-fixtures")
    alias(libs.plugins.ksp) apply true
}

group = "no.nav.brev.brevbaker"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
        content {
            includeGroup("org.jetbrains.kotlinx")
        }
    }
}

dependencies {
    api(project(":brevbaker:dsl"))
    api(project(":brevbaker:brevbaker-api"))
    // Den utvidede (id-eksplisitte) markup-DSL-en, brukt av Letter2Markup. Den bor i samme modul som
    // den vanlige DSL-en og er gatet med @ExtendedMarkupDsl, som denne modulen opter inn på under.
    api(project(":brevbaker:markup-dsl"))
    // Kontrakten mot pdf-bygger. Core bygger LetterPDFRequest selv, så :brevbaker:pdf-bygger-dsl
    // trengs ikke her — den DSL-en finnes for eksterne konsumenter.
    api(project(":brevbaker:pdf-bygger-api"))
    implementation(project(":brevbaker:jackson"))
    ksp(project(":brevbaker:template-model-generator"))
    kspTest(project(":brevbaker:template-model-generator"))
    implementation(libs.kotlinx.html)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.bundles.junit)


    testImplementation(testFixtures(project(":brevbaker:dsl")))
    testImplementation(testFixtures(project(":brevbaker:core")))

    testFixturesApi(project(":brevbaker:brevbaker-api"))
    testFixturesImplementation(project(":brevbaker:jackson"))
    // Testfixturene bygger PDF-forespørsler slik en ekstern konsument ville gjort det.
    testFixturesImplementation(project(":brevbaker:pdf-bygger-dsl"))
    testFixturesImplementation(libs.ktor.serialization.jackson)
    testFixturesImplementation(libs.ktor.client.cio)
    testFixturesImplementation(libs.ktor.client.content.negotiation)
    testFixturesImplementation(libs.ktor.server.callId)

    testFixturesImplementation(testFixtures(project(":brevbaker:dsl")))
    testFixturesImplementation(libs.bundles.logging)
    testFixturesImplementation(libs.bundles.junit)
    testFixturesApi(libs.testcontainers.core)

    testFixturesImplementation(libs.jackson.datatype.jsr310) {
        because("we require deserialization/serialization of java.time.LocalDate")
    }
}

tasks.test {
    useJUnitPlatform()
}

sourceSets {
    main {
        resources.srcDir(rootProject.layout.projectDirectory.dir("resources"))
    }
}


kotlin {
    compilerOptions {
        // Letter2Markup eier id-tildelingen og er nettopp den kalleren den utvidede DSL-en finnes for.
        optIn.add("no.nav.brev.brevbaker.markup.dsl.extended.ExtendedMarkupDsl")
        // BrevbakerPDF bygger LetterPDFRequest direkte i stedet for å dra inn pdf-bygger-DSL-en.
        optIn.add("no.nav.brev.brevbaker.markup.MarkupModelApi")
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

java {
    targetCompatibility = JavaVersion.toVersion(apiModelJavaTarget)
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
            compilerOptions.optIn.add("no.nav.pensjon.brev.template.BrevbakerDSLInternal")
        }
        compileTestFixturesKotlin {
            compilerOptions.optIn.add("no.nav.brev.InterneDataklasser")
            compilerOptions.optIn.add("no.nav.brev.InternKonstruktoer")
        }
    }
}

@OptIn(org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class)
kotlin {
    abiValidation {
        filters {
            exclude {
                annotatedWith.add("no.nav.brev.InterneDataklasser")
                annotatedWith.add("no.nav.brev.InternKonstruktoer")
            }
        }
    }
}