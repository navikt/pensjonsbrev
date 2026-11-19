import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val javaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version libs.versions.kotlinVersion
}

group = "no.nav.pensjon.brev.brevoppskrift"
version = "0.0.1"

val authoringGitExecutable = providers.gradleProperty("authoringGitExecutable").orElse("/usr/bin/git")

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaTarget))
    }
}

tasks {
    compileJava {
        targetCompatibility = javaTarget
    }
    compileTestJava {
        targetCompatibility = javaTarget
    }
    test {
        maxParallelForks = 1
        systemProperty("authoring.repository", rootProject.projectDir.absolutePath)
        systemProperty("authoring.gitExecutable", authoringGitExecutable.get())
        useJUnitPlatform()
    }
}

sourceSets {
    create("authoringTools") {
        compileClasspath += sourceSets.main.get().output.classesDirs
        runtimeClasspath += sourceSets.main.get().output.classesDirs
    }
}
val authoringTools = sourceSets.named("authoringTools").get()

configurations {
    named(authoringTools.implementationConfigurationName) {
        extendsFrom(configurations.implementation.get())
    }
    named(authoringTools.runtimeOnlyConfigurationName) {
        extendsFrom(configurations.runtimeOnly.get())
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.datatype.jsr310)

    implementation(exposedLibs.core)
    implementation(exposedLibs.jdbc)
    implementation(exposedLibs.json)
    implementation(exposedLibs.java.time)
    implementation(libs.hikari.cp)
    implementation(libs.postgresql)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.micrometer.prometheus)

    testImplementation(libs.bundles.junit)
    testImplementation(libs.testcontainers.postgresql)

    "authoringToolsImplementation"(libs.kotlin.compiler.embeddable)
    testImplementation(files(authoringTools.output.classesDirs))
    testRuntimeOnly(files(configurations.named(authoringTools.runtimeClasspathConfigurationName)))
}

val catalogueResources = layout.buildDirectory.dir("generated/authoring/resources")
sourceSets.main {
    resources.srcDir(catalogueResources)
}

val generateSourceCatalogue = tasks.register<JavaExec>("generateSourceCatalogue") {
    group = "build"
    description = "Import visible Kotlin templates from the build's Git commit into an immutable resource."
    dependsOn(tasks.named(authoringTools.classesTaskName))
    classpath = authoringTools.runtimeClasspath
    mainClass.set("no.nav.pensjon.brev.brevoppskrift.katalog.SourceCatalogueBuilderKt")
    systemProperty("authoring.gitExecutable", authoringGitExecutable.get())
    args(rootProject.projectDir.absolutePath, catalogueResources.get().file("authoring/source-catalogue.json").asFile.absolutePath)
    outputs.dir(catalogueResources)
    outputs.upToDateWhen { false }
}

tasks.processResources {
    dependsOn(generateSourceCatalogue)
}

val authoringExamples = layout.buildDirectory.dir("generated/authoring/compile")
val generateAuthoringCompilationExamples = tasks.register<JavaExec>("generateAuthoringCompilationExamples") {
    group = "verification"
    dependsOn(tasks.named(authoringTools.classesTaskName))
    classpath = authoringTools.runtimeClasspath
    mainClass.set("no.nav.pensjon.brev.brevoppskrift.kotlin.GenerateCompilationExamplesKt")
    args(authoringExamples.get().asFile.absolutePath)
    outputs.dir(authoringExamples)
}

val verifyGeneratedTemplates = tasks.register("verifyGeneratedTemplates") {
    group = "verification"
    description = "Compile generated templates, DTOs and render tests against the actual target modules."
    notCompatibleWithConfigurationCache("Cross-project verification classpaths use Kotlin classpath transforms.")
}
val verifyGeneratedDtoJson = tasks.register<JavaExec>("verifyGeneratedDtoJson") {
    group = "verification"
    description = "Verify generated DTO JSON, choice defaults and attachment bindings with the actual consumer."
    dependsOn(generateAuthoringCompilationExamples)
    classpath = authoringTools.runtimeClasspath
    mainClass.set("no.nav.pensjon.brev.brevoppskrift.kotlin.VerifyGeneratedDtoJsonKt")
    val manifest = authoringExamples.map { it.file("dto-json-fixtures.json") }
    args(manifest.get().asFile.absolutePath)
    inputs.file(manifest)
    notCompatibleWithConfigurationCache("Loads compiled cross-project fixtures and the actual consumer classpath.")
}
verifyGeneratedTemplates.configure {
    dependsOn(verifyGeneratedDtoJson)
}

gradle.projectsEvaluated {
    // Expose dependency graphs and JARs; Gradle 9 forbids resolving another project's live configuration.
    fun verificationElements(target: Project, name: String, inherited: Configuration): Configuration =
        target.configurations.create(name) {
            isCanBeConsumed = true
            isCanBeResolved = false
            extendsFrom(inherited)
            outgoing.artifact(target.tasks.named<Jar>("jar"))
        }

    fun verificationClasspath(name: String, target: Project, elements: Configuration): Configuration {
        val classpath = configurations.create(name) {
            isCanBeConsumed = false
            isCanBeResolved = true
        }
        dependencies {
            name(project(mapOf("path" to target.path, "configuration" to elements.name)))
        }
        return classpath
    }

    val consumer = project(":pensjon:brevbaker")
    val consumerMain = consumer.extensions.getByType<SourceSetContainer>().getByName("main")
    val consumerElements = verificationElements(
        consumer, "authoringConsumerRuntimeElements",
        consumer.configurations.getByName(consumerMain.runtimeClasspathConfigurationName),
    )
    val consumerClasspath = verificationClasspath("authoringConsumerRuntime", consumer, consumerElements)
    verifyGeneratedDtoJson.configure {
        classpath += consumerClasspath
    }

    mapOf(
        "UFOERE" to ":ufoere:maler",
        "PENSJON" to ":pensjon:maler",
        "ALDER" to ":alder:maler",
        "PLANLEGGE_PENSJON" to ":planlegge-pensjon-maler",
    ).forEach { entry ->
        val module = entry.key
        val target = project(entry.value)
        val targetSources = target.extensions.getByType<SourceSetContainer>()
        // The Kotlin plugin must create compilation tasks and inject its compiler-options services.
        sourceSets {
            create("generated${module}Main")
            create("generated${module}Test")
        }
        val generatedMain = sourceSets.named("generated${module}Main").get()
        val generatedTest = sourceSets.named("generated${module}Test").get()
        val mainSources = files(authoringExamples.map { it.dir("$module/main") })
            .builtBy(generateAuthoringCompilationExamples)
        val testSources = files(authoringExamples.map { it.dir("$module/test") })
            .builtBy(generateAuthoringCompilationExamples)
        kotlin.sourceSets.named(generatedMain.name) {
            kotlin.srcDir(mainSources)
        }
        kotlin.sourceSets.named(generatedTest.name) {
            kotlin.srcDir(testSources)
        }
        val targetTest = targetSources.getByName("test")
        val compileElements = verificationElements(
            target, "authoringVerificationCompileElements",
            target.configurations.getByName(targetTest.compileClasspathConfigurationName),
        )
        val runtimeElements = verificationElements(
            target, "authoringVerificationRuntimeElements",
            target.configurations.getByName(targetTest.runtimeClasspathConfigurationName),
        )
        dependencies {
            (generatedMain.implementationConfigurationName)(
                project(mapOf("path" to target.path, "configuration" to compileElements.name)),
            )
            (generatedTest.implementationConfigurationName)(
                project(mapOf("path" to target.path, "configuration" to compileElements.name)),
            )
        }
        generatedTest.compileClasspath += generatedMain.output.classesDirs
        val runtimeClasspath = verificationClasspath("generated${module}VerificationRuntime", target, runtimeElements)
        val mainCompilation = tasks.named<KotlinCompile>(generatedMain.getCompileTaskName("kotlin")) {
            dependsOn(generateAuthoringCompilationExamples, target.tasks.named("compileKotlin"))
            compilerOptions.jvmTarget.set(JvmTarget.fromTarget(javaTarget))
            notCompatibleWithConfigurationCache("Cross-project Kotlin classpath transforms cannot currently be serialized.")
        }
        val testCompilation = tasks.named<KotlinCompile>(generatedTest.getCompileTaskName("kotlin")) {
            dependsOn(mainCompilation)
            compilerOptions.jvmTarget.set(JvmTarget.fromTarget(javaTarget))
            notCompatibleWithConfigurationCache("Cross-project Kotlin classpath transforms cannot currently be serialized.")
        }
        verifyGeneratedDtoJson.configure {
            dependsOn(mainCompilation)
            classpath += runtimeClasspath + generatedMain.output.classesDirs
        }
        val renderRegression = tasks.register<Test>("testGenerated${module}Templates") {
            group = "verification"
            description = "Run generated letter and attachment render regressions for $module."
            dependsOn(testCompilation)
            useJUnitPlatform()
            maxParallelForks = 1
            testClassesDirs = generatedTest.output.classesDirs
            classpath = generatedTest.output.classesDirs + generatedMain.output.classesDirs + runtimeClasspath
            notCompatibleWithConfigurationCache("Runs compiled cross-project generated template fixtures.")
        }
        verifyGeneratedTemplates.configure {
            dependsOn(testCompilation, renderRegression)
        }
    }
}
