pluginManagement {
    includeBuild("../settings")
}

plugins {
    id("dependency-catalogs")
}

rootProject.name = "pensjonsbrev-build-logic"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}
