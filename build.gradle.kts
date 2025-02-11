import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    val kotlinVersion: String by System.getProperties()
    val ktorVersion: String by System.getProperties()
    val kspVersion: String by System.getProperties()

    kotlin("jvm") version kotlinVersion apply false
    id("com.google.devtools.ksp") version "$kotlinVersion-$kspVersion" apply false
    id("io.ktor.plugin") version ktorVersion apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
            content {
                includeGroup("no.nav.pensjon.brev") // api-model
                includeGroup("no.nav.pensjon.brevbaker") // api-model-common
            }
        }
    }
}

tasks {
    withType<KotlinCompile>().configureEach {
        dependsOn("ktlintFormat")
    }

    task("build") {
        dependsOn("copyPreCommitHook")
    }

    register<Copy>("copyPreCommitHook") {
        from(".scripts/pre-commit")
        into(".git/hooks")
        filePermissions {
            user {
                execute = true
            }
        }
        doFirst {
            println("Installing git hooks...")
        }
        doLast {
            println("Git hooks installed successfully.")
        }
        description = "Copy pre-commit hook to .git/hooks"
        group = "git hooks"
        outputs.upToDateWhen { false }
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        coloredOutput.set(true)
        reporters {
            reporter(ReporterType.CHECKSTYLE)
            reporter(ReporterType.JSON)
            reporter(ReporterType.HTML)
        }
        filter {
            exclude { element ->
                val path = element.file.path
                path.contains("generated/") || path.contains("build.gradle.kts")
            }
        }
    }
}