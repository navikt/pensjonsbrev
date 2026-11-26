import com.github.gradle.node.npm.task.NpxTask

plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
    id("com.github.node-gradle.node")
}

val apiTypes = extensions.create<OpenApiTypesExtension>("openApiTypes")
val toolVersions = rootProject.layout.projectDirectory.file(".tool-versions")

ktor {
    openApi {
        enabled = true
        codeInferenceEnabled = true
    }
}

// Resolved eagerly: NodePlugin maps `version` through a lambda that captures its non-serializable Project.
// A lazy ValueSource here makes the configuration cache serialize that lambda, failing nodeSetup on reuse.
val nodeVersion: String = providers.fileContents(toolVersions).asText.get()
    .lineSequence().firstOrNull { it.startsWith("nodejs ") }
    ?.substringAfter("nodejs ")?.trim()?.takeIf { it.isNotBlank() }
    ?: throw GradleException("Missing 'nodejs <version>' in .tool-versions")

node {
    download.set(providers.environmentVariable("CI").map { !it.toBoolean() }.orElse(true))
    version.set(nodeVersion)
    nodeProjectDir.set(apiTypes.npmDirectory)
    npmInstallCommand.set("ci")
}

tasks.test {
    useJUnitPlatform {
        excludeTags("openapi-spec")
    }
}

val generateOpenApiSpec = tasks.register<Test>("generateOpenApiSpec") {
    description = "Export the OpenAPI contract from the application's OpenAPI tests."
    group = LifecycleBasePlugin.BUILD_GROUP
    mustRunAfter(tasks.test)
    maxParallelForks = 1
    useJUnitPlatform {
        includeTags("openapi-spec")
    }
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    outputs.file(layout.buildDirectory.file("openapi-spec.json"))
}

tasks.register<NpxTask>("generateApiTypes") {
    description = "Generate frontend TypeScript types from the application's OpenAPI contract."
    group = LifecycleBasePlugin.BUILD_GROUP
    dependsOn(generateOpenApiSpec, tasks.npmInstall)
    command.set("openapi-typescript")
    val specFile = layout.buildDirectory.file("openapi-spec.json")
    val outputFile = apiTypes.outputFile.get().asFile
    args.set(
        listOf(
            specFile.get().asFile.absolutePath,
            "--output", outputFile.absolutePath,
            "--root-types",
            "--root-types-no-schema-prefix",
        ),
    )
    inputs.file(specFile)
    inputs.files(apiTypes.npmDirectory.file("package.json"), apiTypes.npmDirectory.file("package-lock.json"))
    inputs.file(toolVersions)
    outputs.file(outputFile)
}
