import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "no.nav.pensjon.ufoere.brev"

base {
    // Alle api-model-modulene havner i samme lib/-katalog i distribusjonen til pensjon:brevbaker,
    // og filnavnet der er artifactId + versjon — gruppen er ikke med. Uten unike navn kolliderer de
    // saa snart to av dem staar paa samme versjon.
    archivesName.set("ufoere-api-model")
}

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    api(libs.brevdata)
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
            artifactId = "ufoere-api-model"
            from(components["java"])
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(apiModelJavaTarget))
    }
}

tasks {
    register("verifyPackages") {
        inputs.files(fileTree("src/main/kotlin").matching { include("**/*.kt") })
        doLast {
            inputs.files.forEach { file ->
                val text = file.readText()
                val pkg = Regex("""package\s+([a-zA-Z0-9\._]+)""")
                    .find(text)?.groupValues?.get(1)

                val requiredPrefix = "no.nav.pensjon.brev.ufore.api.model"
                if (pkg == null) {
                    throw GradleException("File $file is missing package directive!")
                } else if (!pkg.startsWith(requiredPrefix)) {
                    throw GradleException("Invalid package: $pkg in file $file. Package should start with $requiredPrefix to avoid runtime class conflict.")
                }
            }
        }
    }
    compileJava {
        dependsOn("verifyPackages")
        targetCompatibility = apiModelJavaTarget
    }
    compileTestJava {
        targetCompatibility = apiModelJavaTarget
    }
}
