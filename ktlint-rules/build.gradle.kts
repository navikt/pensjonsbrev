import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
    kotlin("jvm")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaTarget))
    }
}

dependencies {
    implementation(libs.ktlint.cli.ruleset.core)
    implementation(libs.ktlint.rule.engine.core)

    testImplementation(libs.bundles.junit)
    testImplementation(libs.ktlint.test)
    testImplementation(libs.log4j.slf4j2.impl)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
