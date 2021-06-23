val ktorVersion = "1.6.0"
val kotlinVersion = "1.5.10"

plugins {
    kotlin("jvm") version "1.5.10"
    application
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

repositories {
    mavenLocal()
    mavenCentral()
}

group="no.nav.pensjon.brev.pdfbygger"
version="0.0.1-SNAPSHOT"

sourceSets {
    main {
        java.srcDir("/src/main/kotlin")
    }
    test {

    }
}

tasks {
    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-metrics:$ktorVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-gson:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}
sourceSets["main"].resources.srcDirs("resources")

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

kotlin.sourceSets["main"].kotlin.srcDirs("resources")
kotlin.sourceSets["test"].kotlin.srcDirs("resources") // Split into it's own folder if necessary



application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}