import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

// Version of the ktlint engine itself, must match `version.set(...)` in the root build.gradle.kts KtlintExtension config.
val ktlintEngineVersion = "1.8.0"

plugins {
    kotlin("jvm")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaTarget))
    }
}

dependencies {
    implementation("com.pinterest.ktlint:ktlint-cli-ruleset-core:$ktlintEngineVersion")
    implementation("com.pinterest.ktlint:ktlint-rule-engine-core:$ktlintEngineVersion")

    testImplementation(libs.bundles.junit)
    testImplementation("com.pinterest.ktlint:ktlint-test:$ktlintEngineVersion")
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.18")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
