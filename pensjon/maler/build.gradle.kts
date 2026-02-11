import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val apiModelVersion = 337

val apiModelJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    alias(libs.plugins.ksp) apply true
}

group = "no.nav.pensjon.brev"
version = "0.0.1-SNAPSHOT"
base.archivesName.set("pensjon-maler")

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(project(":brevbaker:core"))
    ksp(project(":brevbaker:template-model-generator"))
    api("no.nav.pensjon.brev:api-model:$apiModelVersion")

    testImplementation(libs.bundles.junit)
    testImplementation(testFixtures(project(":brevbaker:core")))
}

tasks.test {
    useJUnitPlatform()
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

tasks {
    compileJava {
        targetCompatibility = apiModelJavaTarget
    }
    compileTestJava {
        targetCompatibility = apiModelJavaTarget
    }
}


tasks {
    test {
        useJUnitPlatform {
            excludeTags = setOf("integration-test", "manual-test")
        }
    }

    val test by testing.suites.existing(JvmTestSuite::class)
    named<Test>("integrationTest") {
        testClassesDirs = files(test.map { it.sources.output.classesDirs })
        classpath = files(test.map { it.sources.runtimeClasspath })
    }
    named<Test>("manualTest") {
        testClassesDirs = files(test.map { it.sources.output.classesDirs })
        classpath = files(test.map { it.sources.runtimeClasspath })
    }
}