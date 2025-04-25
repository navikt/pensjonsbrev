package no.nav.pensjon.brev.template.dsl.helpers

import com.google.devtools.ksp.impl.KotlinSymbolProcessing
import com.google.devtools.ksp.processing.KSPJvmConfig
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSNode
import io.github.classgraph.ClassGraph
import java.io.File
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createTempDirectory
import kotlin.io.path.deleteRecursively

data class KotlinSourceFile(val name: String, val content: String)
data class SourceGenerationResult(val exitCode: KotlinSymbolProcessing.ExitCode, val generatedSources: List<KotlinSourceFile>, val messages: List<String>)

fun KotlinSourceFile.generateSelectors(): SourceGenerationResult =
    listOf(this).generateSelectors()

@OptIn(ExperimentalPathApi::class)
fun List<KotlinSourceFile>.generateSelectors(): SourceGenerationResult =
    let { sources ->
        val tmpDir = createTempDirectory("template-model-generator")

        return try {
            generateSelectors(sources, tmpDir.toFile())
        } finally {
            tmpDir.deleteRecursively()
        }
    }

private val javaVersion by lazy { System.getProperty("java.version") }
private val hostClassPath by lazy {
    ClassGraph()
        .removeTemporaryFilesAfterScan()
        .classpathFiles
        .distinctBy(File::getAbsolutePath)
}

private fun generateSelectors(sources: List<KotlinSourceFile>, workingDir: File): SourceGenerationResult {
    val sourceDir = workingDir.resolve("src").also { it.mkdirs() }
    val outputDir = workingDir.resolve("out")
    val logger = ListLogger()

    sources.forEach { src -> sourceDir.resolve(src.name).writeText(src.content) }

    val kspConfig = KSPJvmConfig.Builder().apply {
        moduleName = "main"
        projectBaseDir = workingDir
        sourceRoots = listOf(sourceDir)
        outputBaseDir = outputDir
        cachesDir = workingDir.resolve("out/cache")
        classOutputDir = workingDir.resolve("out/classes")
        kotlinOutputDir = workingDir.resolve("out/generated-kotlin")
        javaOutputDir = workingDir.resolve("out/java")
        resourceOutputDir = workingDir.resolve("out/resources")
        jvmTarget = javaVersion
        languageVersion = KotlinVersion.CURRENT.toString()
        apiVersion = KotlinVersion.CURRENT.toString()
        libraries = hostClassPath
    }.build()

    val result = SourceGenerationResult(
        exitCode = KotlinSymbolProcessing(kspConfig, listOf(TemplateModelHelpersAnnotationProcessorProvider()), logger).execute(),
        generatedSources = outputDir.walk()
            .filter { it.isFile && it.name.endsWith(".kt") }
            .map { KotlinSourceFile(it.name, it.readText()) }
            .toList(),
        messages = logger.messages,
    )
    return result
}

private class ListLogger : KSPLogger {
    private val _messages = mutableListOf<String>()
    val messages: List<String> = _messages

    override fun error(message: String, symbol: KSNode?) {
        _messages.add(message)
    }

    override fun exception(e: Throwable) {}

    override fun info(message: String, symbol: KSNode?) {
        _messages.add(message)
    }

    override fun logging(message: String, symbol: KSNode?) {
        _messages.add(message)
    }

    override fun warn(message: String, symbol: KSNode?) {
        _messages.add(message)
    }
}