import com.github.gradle.node.npm.task.NpxTask

plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
    id("com.github.node-gradle.node")
}

ktor {
    openApi {
        enabled = true
        codeInferenceEnabled = true
    }
}

node {
    download.set(System.getenv("CI")?.toBoolean() != true)
    version.set(
        rootProject.file(".tool-versions").readLines()
            .first { it.startsWith("nodejs ") }.substringAfter("nodejs ").trim(),
    )
    nodeProjectDir.set(project.file("openapi"))
    npmInstallCommand.set("ci")
}

tasks.test {
    useJUnitPlatform {
        excludeTags("openapi-spec")
    }
}

val generateOpenApiSpec = tasks.register<Test>("generateOpenApiSpec") {
    description = "Export and verify the OpenAPI contract from the running Brevoppskrift application."
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
    description = "Generate frontend TypeScript types from the verified Brevoppskrift OpenAPI contract."
    group = LifecycleBasePlugin.BUILD_GROUP
    dependsOn(generateOpenApiSpec, tasks.npmInstall)
    command.set("openapi-typescript")
    val specFile = layout.buildDirectory.file("openapi-spec.json")
    val outputFile = rootProject.file("brevoppskrift-web/frontend/src/verksted/api-types.ts")
    args.set(
        listOf(
            specFile.get().asFile.absolutePath,
            "--output", outputFile.absolutePath,
            "--root-types",
            "--root-types-no-schema-prefix",
        ),
    )
    inputs.file(specFile)
    inputs.files(project.file("openapi/package.json"), project.file("openapi/package-lock.json"))
    inputs.file(rootProject.file(".tool-versions"))
    outputs.file(outputFile)
}
