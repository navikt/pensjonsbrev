import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val alderApiModelVersion = 47

val apiModelJavaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    alias(libs.plugins.ksp) apply true
}

group = "no.nav.pensjon.brev"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly(kotlin("stdlib"))
    implementation(project(":brevbaker"))
    ksp(project(":template-model-generator"))
    api("no.nav.pensjon.brev:alder-brevbaker-api-model:${alderApiModelVersion}")


    testImplementation(libs.bundles.junit)
    testImplementation(testFixtures(project(":brevbaker")))
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
    register("verifyPackages") {
        notCompatibleWithConfigurationCache("Uses script references")
        val files = fileTree("src/main/kotlin").matching { include("**/*.kt") }
        doLast {
            files.forEach { file ->
                val text = file.readText()
                val pkg = Regex("""package\s+([a-zA-Z0-9\._]+)""")
                    .find(text)?.groupValues?.get(1)

                val requiredPrefix = "no.nav.pensjon.brev.alder"
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


tasks {
    test {
        useJUnitPlatform {
            excludeTags = setOf("integration-test", "manual-test")
        }
    }

    val test by testing.suites.existing(JvmTestSuite::class)
    register<Test>("integrationTest") {
        testClassesDirs = files(test.map { it.sources.output.classesDirs })
        classpath = files(test.map { it.sources.runtimeClasspath })
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        useJUnitPlatform {
            includeTags = setOf("integration-test")
        }
    }
    register<Test>("manualTest") {
        testClassesDirs = files(test.map { it.sources.output.classesDirs })
        classpath = files(test.map { it.sources.runtimeClasspath })
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        useJUnitPlatform {
            includeTags = setOf("manual-test")
        }
    }
}