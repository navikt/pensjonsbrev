import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val markupJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
    alias(libs.plugins.binary.compatibility.validator) apply true
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
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.bundles.junit)
    testImplementation(libs.kotlinx.serialization.json)
}

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
}
