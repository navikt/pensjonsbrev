import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val javaTarget: String by System.getProperties()

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization") version libs.versions.kotlinVersion
}

group = "no.nav.pensjon.brev.brevoppskrift"
version = "0.0.1"

application {
    mainClass.set("no.nav.pensjon.brev.brevoppskrift.BrevoppskriftAppKt")
}

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
        useJUnitPlatform()
        systemProperty("authoring.repository", rootProject.projectDir.absolutePath)
        systemProperty("authoring.modelClasses", sourceSets.main.get().output.classesDirs.asPath)
    }
    build {
        dependsOn(installDist)
    }
}

dependencies {
    implementation(platform(libs.netty.bom))
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.di)
    implementation(ktorLibs.server.callId)
    implementation(ktorLibs.server.callLogging)
    implementation(libs.kotlinx.serialization.json)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.statusPages)
    implementation(ktorLibs.server.metrics.micrometer)
    implementation(ktorLibs.serialization.jackson)
    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.datatype.jsr310)
    implementation(platform(libs.log4j.bom))
    implementation(libs.bundles.logging)
    implementation(libs.micrometer.prometheus)
    implementation(libs.hikari.cp)
    implementation(libs.postgresql)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(ktorLibs.client.cio)
    implementation(ktorLibs.client.contentNegotiation)
    implementation(exposedLibs.core)
    implementation(exposedLibs.jdbc)
    implementation(exposedLibs.json)
    implementation(exposedLibs.java.time)

    testImplementation(libs.bundles.junit)
    testImplementation(ktorLibs.client.mock)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(ktorLibs.server.testHost) {
        exclude("org.jetbrains.kotlin", "kotlin-test")
    }
}

    // PSI and contract generation are build-only. Neither the compiler nor source checkout is needed at runtime.
    val authoringTools = sourceSets.create("authoringTools")
    configurations[authoringTools.implementationConfigurationName].extendsFrom(configurations.implementation.get())
    configurations[authoringTools.runtimeOnlyConfigurationName].extendsFrom(configurations.runtimeOnly.get())
    authoringTools.compileClasspath += sourceSets.main.get().output.classesDirs
    authoringTools.runtimeClasspath += sourceSets.main.get().output.classesDirs

    dependencies {
        add(authoringTools.implementationConfigurationName, "org.jetbrains.kotlin:kotlin-compiler-embeddable:${libs.versions.kotlinVersion.get()}")
        add(authoringTools.implementationConfigurationName, kotlin("reflect"))
        testImplementation(files(authoringTools.output.classesDirs))
        testRuntimeOnly(files(configurations[authoringTools.runtimeClasspathConfigurationName]))
    }

    val catalogueResources = layout.buildDirectory.dir("generated/authoring/resources")
    sourceSets.main {
        resources.srcDir(catalogueResources)
    }

    val generateSourceCatalogue by tasks.registering(JavaExec::class) {
        group = "build"
        description = "Import visible Kotlin templates from the build's Git commit into an immutable resource."
        dependsOn(tasks.named(authoringTools.classesTaskName))
        classpath = authoringTools.runtimeClasspath
        mainClass.set("no.nav.pensjon.brev.brevoppskrift.katalog.SourceCatalogueBuilderKt")
        args(rootProject.projectDir.absolutePath, catalogueResources.get().file("authoring/source-catalogue.json").asFile.absolutePath)
        outputs.dir(catalogueResources)
        // HEAD can change without any working-tree file changing. Always read the actual commit.
        outputs.upToDateWhen { false }
    }

    tasks.processResources {
        dependsOn(generateSourceCatalogue)
    }

    val generateAuthoringApi by tasks.registering(JavaExec::class) {
        group = "build"
        description = "Generate OpenAPI component schemas and TypeScript from serialized Kotlin properties and Jackson subtypes."
        dependsOn(tasks.named(authoringTools.classesTaskName))
        classpath = authoringTools.runtimeClasspath
        mainClass.set("no.nav.pensjon.brev.brevoppskrift.kotlin.ApiContractGeneratorKt")
        args(
            sourceSets.main.get().output.classesDirs.asPath,
            rootProject.file("brevoppskrift-web/frontend/src/api/brevoppskrift-api.generated.ts").absolutePath,
            layout.buildDirectory.file("generated/authoring/openapi.json").get().asFile.absolutePath,
        )
        inputs.files(sourceSets.main.get().output.classesDirs)
        outputs.file(rootProject.file("brevoppskrift-web/frontend/src/api/brevoppskrift-api.generated.ts"))
        outputs.file(layout.buildDirectory.file("generated/authoring/openapi.json"))
    }

    val authoringExamples = layout.buildDirectory.dir("generated/authoring/compile")
    val generateAuthoringCompilationExamples by tasks.registering(JavaExec::class) {
        group = "verification"
        dependsOn(tasks.named(authoringTools.classesTaskName))
        classpath = authoringTools.runtimeClasspath
        mainClass.set("no.nav.pensjon.brev.brevoppskrift.kotlin.GenerateCompilationExamplesKt")
        args(authoringExamples.get().asFile.absolutePath)
        outputs.dir(authoringExamples)
    }

    // Real target-module dependencies, including published api-model and DSL; no fake DSL stubs.
    // Verification sources stay inside this module's build directory and do not change target sources.
    val verifyGeneratedTemplates by tasks.registering {
        group = "verification"
        description = "Compile generated mal/DTO/render tests and verify DTO JSON/DSL behavior against the actual consumer."
        notCompatibleWithConfigurationCache("Cross-project verification classpaths use Kotlin classpath transforms. Ordinary backend builds remain cache-compatible.")
    }
    val verifyGeneratedDtoJson by tasks.registering(JavaExec::class) {
        group = "verification"
        description = "Round-trip compiled generated DTOs with the actual consumer mapper and evaluate Boolean choice defaults."
        dependsOn(generateAuthoringCompilationExamples)
        classpath = authoringTools.runtimeClasspath
        mainClass.set("no.nav.pensjon.brev.brevoppskrift.kotlin.VerifyGeneratedDtoJsonKt")
        val manifest = authoringExamples.map { it.file("dto-json-fixtures.json") }
        args(manifest.get().asFile.absolutePath)
        inputs.file(manifest)
        notCompatibleWithConfigurationCache("Loads compiled cross-project verification fixtures and the actual consumer classpath.")
    }
    verifyGeneratedTemplates.configure { dependsOn(verifyGeneratedDtoJson) }
    gradle.projectsEvaluated {
        // Publish dependency graphs and jars, not another project's live resolvable FileCollection.
        // Gradle 9 forbids resolving foreign configurations from a compilation task's project lock.
        fun verificationElements(target: Project, name: String, inherited: Configuration): Configuration =
            target.configurations.create(name) {
                isCanBeConsumed = true
                isCanBeResolved = false
                extendsFrom(inherited)
                outgoing.artifact(target.tasks.named<Jar>("jar"))
            }

        fun verificationClasspath(name: String, target: Project, elements: Configuration): Configuration =
            configurations.create(name) {
                isCanBeConsumed = false
                isCanBeResolved = true
                dependencies.add(project.dependencies.project(mapOf("path" to target.path, "configuration" to elements.name)))
            }

        val consumer = project(":pensjon:brevbaker")
        val consumerMain = consumer.extensions.getByType<SourceSetContainer>().getByName("main")
        val consumerElements = verificationElements(consumer, "authoringConsumerRuntimeElements",
            consumer.configurations.getByName(consumerMain.runtimeClasspathConfigurationName))
        val consumerClasspath = verificationClasspath("authoringConsumerRuntime", consumer, consumerElements)
        verifyGeneratedDtoJson.configure {
            classpath += consumerClasspath
        }
        mapOf("UFOERE" to ":ufoere:maler", "PENSJON" to ":pensjon:maler", "ALDER" to ":alder:maler",
            "PLANLEGGE_PENSJON" to ":planlegge-pensjon-maler").forEach { (module, targetPath) ->
            val target = project(targetPath)
            val targetSources = target.extensions.getByType<SourceSetContainer>()
            // The Kotlin plugin creates the compilation tasks and injects its compiler-options services.
            // Direct KotlinCompile construction is not supported by Kotlin 2.4.
            val generatedMain = sourceSets.create("generated${module}Main")
            val generatedTest = sourceSets.create("generated${module}Test")
            // Preserve producer provenance for every consumer, including ktlint's source-set tasks.
            // The root ktlint filter already excludes generated/; filtering alone does not remove
            // Gradle's requirement to declare the dependency on the producer of the source directory.
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
            val compileElements = verificationElements(target, "authoringVerificationCompileElements",
                target.configurations.getByName(targetTest.compileClasspathConfigurationName))
            val runtimeElements = verificationElements(target, "authoringVerificationRuntimeElements",
                target.configurations.getByName(targetTest.runtimeClasspathConfigurationName))
            dependencies.add(generatedMain.implementationConfigurationName,
                project.dependencies.project(mapOf("path" to target.path, "configuration" to compileElements.name)))
            dependencies.add(generatedTest.implementationConfigurationName,
                project.dependencies.project(mapOf("path" to target.path, "configuration" to compileElements.name)))
            generatedTest.compileClasspath += generatedMain.output.classesDirs
            val runtimeClasspath = verificationClasspath("generated${module}VerificationRuntime", target, runtimeElements)
            val mainCompilation = tasks.named<KotlinCompile>(generatedMain.getCompileTaskName("kotlin")) {
                dependsOn(generateAuthoringCompilationExamples, target.tasks.named("compileKotlin"))
                compilerOptions.jvmTarget.set(JvmTarget.fromTarget(javaTarget))
                notCompatibleWithConfigurationCache("Cross-project verification classpath transforms cannot currently be serialized by Kotlin/Gradle.")
            }
            val testCompilation = tasks.named<KotlinCompile>(generatedTest.getCompileTaskName("kotlin")) {
                dependsOn(mainCompilation)
                compilerOptions.jvmTarget.set(JvmTarget.fromTarget(javaTarget))
                notCompatibleWithConfigurationCache("Cross-project verification classpath transforms cannot currently be serialized by Kotlin/Gradle.")
            }
            verifyGeneratedDtoJson.configure {
                dependsOn(mainCompilation)
                classpath += runtimeClasspath + generatedMain.output.classesDirs
            }
            verifyGeneratedTemplates.configure { dependsOn(testCompilation) }
            val renderRegression = tasks.register<Test>("testGenerated${module}Templates") {
                group = "verification"
                description = "Run generated letter and attachment render regressions for $module."
                dependsOn(testCompilation)
                useJUnitPlatform()
                maxParallelForks = 1
                testClassesDirs = generatedTest.output.classesDirs
                classpath = generatedTest.output.classesDirs + generatedMain.output.classesDirs +
                    runtimeClasspath
                notCompatibleWithConfigurationCache("Runs compiled cross-project generated template fixtures.")
            }
            verifyGeneratedTemplates.configure { dependsOn(renderRegression) }
        }
}
