val javaTarget: String by System.getProperties()
val kotlinVersion: String by System.getProperties()
val kspVersion: String by System.getProperties()
val jupiterVersion: String by project
val hamkrestVersion: String by project

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
        kotlinOptions.jvmTarget = javaTarget
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = javaTarget
    }
    compileJava {
        targetCompatibility = javaTarget
    }
}

dependencies {
    compileOnly(kotlin("reflect"))
    implementation("com.google.devtools.ksp:symbol-processing-api:$kotlinVersion-$kspVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.5.0")
    testImplementation("com.natpryce:hamkrest:$hamkrestVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
}
