import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val markupJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
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

dependencies {
    api(libs.kotlinx.serialization.json)

    testImplementation(libs.bundles.junit)
}

// The `apiInternal` source set is an internal-API layer that may use markup's `internal` seams
// (builders, IdGenerator, markupJson, internal constructors). It is kept as a dedicated source set
// — rather than folded into `main` — so those seams do NOT become part of markup's published public
// API (the binary-compatibility validator and the published jar only cover `main`).
//
// It gains `internal` access to `main` through Kotlin compilation association (`associateWith`)
sourceSets.create("apiInternal")

kotlin {
    val mainCompilation = target.compilations.getByName("main")
    val apiInternalCompilation = target.compilations.getByName("apiInternal")
    apiInternalCompilation.associateWith(mainCompilation)

    // Tests exercise the apiInternal DSL, so the test compilation needs it (and transitively main)
    target.compilations.getByName("test").associateWith(apiInternalCompilation)
}

// Expose the compiled `apiInternal` classes as a consumable configuration so sibling modules (core)
// can build the renderer against markup's internal-API layer WITHOUT publishing those seams. The
// `main` classes/deps are consumed through the normal project dependency; this only adds the
// apiInternal jar on top.
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
kotlin { abiValidation() }
