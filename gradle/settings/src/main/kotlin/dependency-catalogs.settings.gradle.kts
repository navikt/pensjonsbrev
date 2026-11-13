import java.util.Properties

val versions = requireNotNull(javaClass.getResourceAsStream("/catalog-versions.properties"))
    .use { input -> Properties().apply { load(input) } }

dependencyResolutionManagement {
    repositories {
        mavenCentral {
            content {
                includeModule("io.ktor", "ktor-version-catalog")
                includeModule("org.jetbrains.exposed", "exposed-version-catalog")
            }
        }
    }
    versionCatalogs {
        create("ktorLibs") {
            from("io.ktor:ktor-version-catalog:${versions.getProperty("ktorVersion")}")
        }
        create("exposedLibs") {
            from("org.jetbrains.exposed:exposed-version-catalog:${versions.getProperty("exposedVersion")}")
        }
    }
}
