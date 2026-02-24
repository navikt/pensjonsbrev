import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.jlleitschuh.gradle.ktlint.tasks.KtLintCheckTask

plugins {
    kotlin("jvm") version libs.versions.kotlinVersion apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.binary.compatibility.validator) apply false
    alias(libs.plugins.ktlint)
}

allprojects {

    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
            content {
                // api-model
                includeGroup("no.nav.pensjon.alder.brev")
                includeGroup("no.nav.pensjon.ufoere.brev")
                includeGroup("no.nav.pensjon.brev")
                // api-model-common
                includeGroup("no.nav.brev.brevbaker")
            }
        }
    }
    tasks.withType<KotlinJvmCompile>{
        /*
        Denne er for å unngå unødige advarsler om https://youtrack.jetbrains.com/issue/KT-73255
        Vi bruker egentlig bare konstruktør-varianten, men vil egentlig helst holde oss til kotlin sin standardvariant
        Så når dette er blitt standarden i kotlin - som det skal bli - så kan vi skru av denne
         */
        compilerOptions {
            freeCompilerArgs = listOf("-Xannotation-default-target=param-property")
        }
    }
    tasks.withType<KtLintCheckTask> {
        if (System.getenv("CI")?.toBoolean() != true) {
            dependsOn("ktlintFormat")
        }
    }
    tasks.withType<Test>{
        testLogging {
            events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED, TestLogEvent.STANDARD_ERROR)
            exceptionFormat = TestExceptionFormat.FULL
        }
        systemProperties["junit.jupiter.execution.parallel.enabled"] = true
        systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
        systemProperties["junit.jupiter.execution.parallel.mode.classes.default"] = "concurrent"
        systemProperties["junit.jupiter.execution.parallel.config.strategy"] = "dynamic"
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
        forkEvery = 100
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        outputToConsole.set(true)
        reporters {
            reporter(ReporterType.JSON)
        }
        filter {
            exclude { element ->
                val path = element.file.path
                path.contains("generated/") || path.contains("build.gradle.kts")
            }
        }
    }

    tasks {
        register<Test>("integrationTest") {
            outputs.doNotCacheIf("Output of this task is not cached") { true }
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            systemProperties["junit.jupiter.execution.parallel.config.dynamic.factor"] = 0.5
            useJUnitPlatform {
                includeTags = setOf("integration-test")
            }
        }
        register<Test>("manualTest") {
            outputs.doNotCacheIf("Output of this task is not cached") { true }
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            systemProperties["junit.jupiter.execution.parallel.config.dynamic.factor"] = 0.5
            useJUnitPlatform {
                includeTags = setOf("manual-test")
            }
        }
    }
}
