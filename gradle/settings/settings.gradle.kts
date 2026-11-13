rootProject.name = "settings-plugins"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}
