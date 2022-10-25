plugins {
    val kotlinVersion: String by System.getProperties()
    val ktorVersion: String by System.getProperties()
    val kspVersion: String by System.getProperties()

    kotlin("jvm") version kotlinVersion apply false
    id("com.google.devtools.ksp") version "$kotlinVersion-$kspVersion" apply false
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    id("io.ktor.plugin") version ktorVersion apply false
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            // Create a token at https://github.com/settings/tokens/new with package.read
            // Then create a gradle.properties file in $HOME/.gradle with the following:
            // gpr.user=<your github username>
            // gpr.token=<the token>
            url = uri("https://maven.pkg.github.com/navikt/pensjonsbrev")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
