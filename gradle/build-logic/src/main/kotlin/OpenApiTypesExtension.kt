import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty

abstract class OpenApiTypesExtension {
    abstract val npmDirectory: DirectoryProperty
    abstract val outputFile: RegularFileProperty
}
