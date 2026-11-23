import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    kotlin("jvm")
}

val sourceSets = extensions.getByType<SourceSetContainer>()
val mainSourceSet = sourceSets.named("main").get()
val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
val authoringGitExecutable = providers.gradleProperty("authoringGitExecutable").orElse("/usr/bin/git")

sourceSets {
    create("authoringTools") {
        compileClasspath += mainSourceSet.output.classesDirs
        runtimeClasspath += mainSourceSet.output.classesDirs
    }
}
val authoringTools = sourceSets.named("authoringTools").get()

configurations {
    named(authoringTools.implementationConfigurationName) {
        extendsFrom(configurations.named("implementation").get())
    }
    named(authoringTools.runtimeOnlyConfigurationName) {
        extendsFrom(configurations.named("runtimeOnly").get())
    }
}

dependencies {
    (authoringTools.implementationConfigurationName)(libs.findLibrary("kotlin-compiler-embeddable").get())
    "testImplementation"(files(authoringTools.output.classesDirs))
    "testRuntimeOnly"(files(configurations.named(authoringTools.runtimeClasspathConfigurationName)))
}

tasks.named<Test>("test") {
    systemProperty("authoring.repository", rootProject.projectDir.absolutePath)
    systemProperty("authoring.gitExecutable", authoringGitExecutable.get())
}

val catalogueResources = layout.buildDirectory.dir("generated/authoring/resources")
mainSourceSet.resources.srcDir(catalogueResources)

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

tasks.named<ProcessResources>("processResources") {
    dependsOn(generateSourceCatalogue)
}
