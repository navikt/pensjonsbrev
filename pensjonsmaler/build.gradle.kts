val apiModelVersion: String by project

plugins {
    kotlin("jvm") version "2.1.0"
}

group = "no.nav.etterlatte"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(kotlin("stdlib"))
    api(project(":brevbaker-api-model-mal"))
    api("no.nav.pensjon.brev:pensjon-brevbaker-api-model:$apiModelVersion")
}

tasks.test {
    useJUnitPlatform()
}