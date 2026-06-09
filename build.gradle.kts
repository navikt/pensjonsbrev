import com.ncorti.ktfmt.gradle.tasks.KtfmtCheckTask
import com.ncorti.ktfmt.gradle.tasks.KtfmtFormatTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version libs.versions.kotlinVersion apply false
    alias(libs.plugins.ktfmt)
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
    tasks.withType<KotlinJvmCompile> {
        /*
        Denne er for å unngå unødige advarsler om https://youtrack.jetbrains.com/issue/KT-73255
        Vi bruker egentlig bare konstruktør-varianten, men vil egentlig helst holde oss til kotlin sin standardvariant
        Så når dette er blitt standarden i kotlin - som det skal bli - så kan vi skru av denne
         */
        compilerOptions { freeCompilerArgs = listOf("-Xannotation-default-target=param-property") }
    }
    tasks.withType<KtfmtCheckTask> {
        if (System.getenv("CI")?.toBoolean() != true) {
            dependsOn("ktfmtFormat")
        }
    }
    tasks.withType<KtfmtFormatTask> {
        source = project.fileTree(rootDir)
        include("**/.kt", "**/*.kts")
    }
    tasks.withType<Test> {
        testLogging {
            events(
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
                TestLogEvent.FAILED,
                TestLogEvent.STANDARD_ERROR,
            )
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
    apply(plugin = "com.ncorti.ktfmt.gradle")
    ktfmt {
        googleStyle()
        kotlinLangStyle()
        maxWidth = 160
    }
    tasks {
        register<Test>("integrationTest") {
            description = "Integration tests"
            outputs.doNotCacheIf("Output of this task is not cached") { true }
            outputs.upToDateWhen { false }
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            systemProperties["junit.jupiter.execution.parallel.config.dynamic.factor"] = 0.5
            forkEvery = 0 // for å dele test-container uten å spinne opp ny.
            useJUnitPlatform { includeTags = setOf("integration-test") }
        }
        register<Test>("manualTest") {
            description = "Manual tests that require running services"
            outputs.doNotCacheIf("Output of this task is not cached") { true }
            outputs.upToDateWhen { false }
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            systemProperties["junit.jupiter.execution.parallel.config.dynamic.factor"] = 0.5
            forkEvery = 0 // for å dele test-container uten å spinne opp ny.
            useJUnitPlatform { includeTags = setOf("manual-test") }
        }
    }
}
