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
    api(libs.brevbaker.common)
    // Merk: markup:main deklareres ikke med koordinater her. Den kommer transitivt fra
    // api-model-common, og å ha både koordinatene og prosjekt-avhengigheten under gir GAV-kollisjon
    // ("Cannot select a variant by configuration name from no.nav.brev.brevbaker:markup").
    // Den utvidede (id-eksplisitte) markup-DSL-en, brukt av Letter2Markup. Ligger i markups
    // `apiInternal`-kildesett, som aldri publiseres, og hentes derfor som lokal jar.
    // `implementation`, ikke `api`: jar-en deler GAV med det publiserte markup-artefaktet, så hvis
    // den lekker transitivt kan ingen nedstrøms modul lenger deklarere libs.brevbaker.markup
    // ("Cannot select a variant by configuration name from no.nav.brev.brevbaker:markup").
    implementation(project(path = ":brevbaker:markup", configuration = "apiInternalElements"))
    implementation(project(":brevbaker:internal"))
    ksp(project(":brevbaker:template-model-generator"))
    kspTest(project(":brevbaker:template-model-generator"))
    implementation(libs.kotlinx.html)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.bundles.junit)


    testImplementation(testFixtures(project(":brevbaker:dsl")))
    testImplementation(testFixtures(project(":brevbaker:core")))

    testFixturesApi(libs.brevbaker.common)
    testFixturesImplementation(project(":brevbaker:internal"))
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