import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.jlleitschuh.gradle.ktlint.tasks.KtLintCheckTask
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

plugins {
    kotlin("jvm") version libs.versions.kotlinVersion apply false
    alias(libs.plugins.ktlint)
}

allprojects {

    // Sikrer at alle jackson-* avhengigheter i hele prosjektet resolver til samme versjon,
    // uten at hver modul må deklarere platform(libs.jackson.bom) selv.
    configurations.matching { it.name in setOf("implementation", "testImplementation", "testFixturesImplementation") }
        .configureEach {
                project.dependencies.add(name, project.dependencies.platform(libs.jackson.bom))
        }

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
        systemProperties["junit.jupiter.execution.parallel.config.executor-service"] = "FORK_JOIN_POOL"
        systemProperties["junit.jupiter.execution.parallel.config.strategy"] = "dynamic"
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
        forkEvery = 100
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    // Version of the ktlint engine itself; declared once in gradle/libs.versions.toml so that both this
    // KtlintExtension config and the :ktlint-rules module's dependencies stay in sync.
    val ktlintEngineVersion =
        rootProject.extensions
            .getByType<VersionCatalogsExtension>()
            .named("libs")
            .findVersion("ktlintEngineVersion")
            .get()
            .requiredVersion

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set(ktlintEngineVersion)
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
    // Custom rules (e.g. requiring compile-time constant arguments to `ifNull`), see :ktlint-rules.
    // Excluded from the module itself to avoid a self-dependency.
    if (path != ":ktlint-rules") {
        dependencies {
            "ktlintRuleset"(project(":ktlint-rules"))
        }
    }

    tasks {
        register<Test>("integrationTest") {
            description = "Integration tests"
            outputs.doNotCacheIf("Output of this task is not cached") { true }
            outputs.upToDateWhen { false }
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            timeout = 15.minutes.toJavaDuration()
            systemProperties["junit.jupiter.execution.parallel.config.dynamic.factor"] = 0.5
            systemProperties["junit.jupiter.execution.parallel.config.executor-service"] = "FORK_JOIN_POOL"
            forkEvery = 0 // for å dele test-container uten å spinne opp ny.
            useJUnitPlatform {
                includeTags = setOf("integration-test")
            }
        }
        register<Test>("manualTest") {
            description = "Manual tests that require running services"
            outputs.doNotCacheIf("Output of this task is not cached") { true }
            outputs.upToDateWhen { false }
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            systemProperties["junit.jupiter.execution.parallel.config.dynamic.factor"] = 0.5
            forkEvery = 0 // for å dele test-container uten å spinne opp ny.
            useJUnitPlatform {
                includeTags = setOf("manual-test")
            }
        }
    }
}
