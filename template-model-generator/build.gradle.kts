val kotlinVersion: String by System.getProperties()
val kspVersion: String by System.getProperties()
val jupiterVersion: String by project
val hamkrestVersion: String by project
val logbackVersion: String by project

plugins {
    kotlin("jvm")
}

group = "no.nav.pensjon.brev"
version = "0.0.1-SNAPSHOT"

tasks {
    test {
        useJUnitPlatform()
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}

dependencies {
    compileOnly(kotlin("reflect"))
    implementation("com.google.devtools.ksp:symbol-processing-api:$kotlinVersion-$kspVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.5.0")
    testImplementation("com.natpryce:hamkrest:$hamkrestVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
}
