import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val markupJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "no.nav.brev.brevbaker"

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

// Markup skal være helt uten avhengigheter: den er den offentlige modellen/DSL-en for pdf-bygger, og
// konsumenter skal ikke arve verken et serialiseringsbibliotek eller genererte serializers gjennom den.
// All serialisering ligger i brevbaker:internal (se MarkupJacksonModule).
dependencies {
    testImplementation(libs.bundles.junit)
}

// Kildesettet `apiInternal` er et internt API-lag som får bruke markups `internal` seams (byggernes
// tilstand, `build()`, interne konstruktører). Det holdes som et eget kildesett — ikke foldet inn i
// `main` — nettopp for at de seamsene IKKE skal bli en del av markups publiserte offentlige API:
// både ABI-validatoren og den publiserte jar-en dekker kun `main`.
//
// Tilgangen til `main`s internals kommer fra Kotlin-kompileringsassosiasjon (`associateWith`).
sourceSets.create("apiInternal")

kotlin {
    val mainCompilation = target.compilations.getByName("main")
    val apiInternalCompilation = target.compilations.getByName("apiInternal")
    apiInternalCompilation.associateWith(mainCompilation)

    // Testene bruker apiInternal-DSL-en, så test-kompileringen trenger den (og transitivt main).
    target.compilations.getByName("test").associateWith(apiInternalCompilation)
}

// Overlever `apiInternal`s kompilerte klasser til `brevbaker:internal` over prosjektgrensen.
//
// `apiInternal` ser `main`s internals via friend-kompilering (`associateWith`), som kun virker
// innenfor prosjektet; konsumenten trenger bare de resulterende klassene. En vanlig
// prosjektavhengighet leverer kun `main`, så vi eksponerer `apiInternal`s output som en egen
// jar/konsumerbar konfigurasjon som aldri publiseres. Da holdes seamsene helt utenfor det publiserte
// artefaktet.
val apiInternalJar = tasks.register<Jar>("apiInternalJar") {
    archiveClassifier.set("api-internal")
    from(sourceSets["apiInternal"].output)
}

configurations.create("apiInternalElements") {
    isCanBeConsumed = true
    isCanBeResolved = false
}

artifacts.add("apiInternalElements", apiInternalJar)

tasks.test {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/navikt/pensjonsbrev")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
            pom {
                name.set("brevbaker-markup")
                description.set("Markup-modell og DSL for å bygge og serialisere Nav-brev.")
                url.set("https://github.com/navikt/pensjonsbrev")
                scm {
                    url.set("https://github.com/navikt/pensjonsbrev")
                    connection.set("scm:git:https://github.com/navikt/pensjonsbrev.git")
                }
            }
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(markupJavaTarget))
    }
}
tasks {
    compileJava {
        targetCompatibility = markupJavaTarget
    }
    compileTestJava {
        targetCompatibility = markupJavaTarget
    }
    named<JavaCompile>("compileApiInternalJava") {
        targetCompatibility = markupJavaTarget
    }
}

@OptIn(org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class)
kotlin {
    abiValidation {}
}
