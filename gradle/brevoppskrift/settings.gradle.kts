pluginManagement {
    includeBuild("../settings")
}

plugins {
    id("dependency-catalogs")
}

rootProject.name = "brevoppskrift-build-logic"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}
