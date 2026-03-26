package no.nav.pensjon.brev.pdfbygger.typst

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse
import no.nav.pensjon.brev.template.render.DocumentFile
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.createTempDirectory
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

private const val DEFAULT_TYPST_TEMPLATE_DIR = "/app/typst"

class TypstCompileService(
    private val templateDir: Path = Path.of(DEFAULT_TYPST_TEMPLATE_DIR)
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private fun typstCommand(workingDir: Path) = listOf(
        "typst", "compile",
        "--root", workingDir.toString(),
        "letter.typ"
    )

    suspend fun createLetter(typstFiles: List<DocumentFile>): PDFCompilationResponse {
        val tmpDir = createTempDirectory(Path.of("/tmp"))

        return try {
            // Copy template files from the template directory to the temp directory
            copyTemplateFiles(templateDir, tmpDir)

            // Write generated files (input.typ, letter.typ)
            typstFiles.forEach {
                tmpDir.resolve(it.fileName).toFile().apply {
                    createNewFile()
                    writeText(it.content)
                }
            }

            when (val result: Execution = executeCompileProcess(tmpDir)) {
                is Execution.Success -> {
                    result.pdf.toFile().readBytes()
                        .let { PDFCompilationResponse.Success(PDFCompilationOutput(it)) }
                }

                is Execution.Failure.Compilation ->
                    PDFCompilationResponse.Failure.Client(
                        reason = "PDF compilation failed",
                        output = result.output,
                        error = result.error
                    )

                is Execution.Failure.Execution -> {
                    logger.error("typst command failed", result.cause)
                    PDFCompilationResponse.Failure.Server(reason = "Compilation process execution failed: ${typstCommand(tmpDir)}")
                }
            }
        } finally {
            tmpDir.toFile().deleteRecursively()
        }
    }

    /**
     * Recursively copy all template files from the source directory to the destination directory.
     */
    private fun copyTemplateFiles(source: Path, destination: Path) {
        if (!source.exists()) {
            logger.warn("Template directory does not exist: $source")
            return
        }

        Files.walk(source).use { paths ->
            paths.forEach { sourcePath ->
                val relativePath = source.relativize(sourcePath)
                val targetPath = destination.resolve(relativePath)

                if (sourcePath.isDirectory()) {
                    Files.createDirectories(targetPath)
                } else {
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING)
                }
            }
        }
    }

    private suspend fun executeCompileProcess(
        workingDir: Path,
        typstFileName: String = "letter",
        output: Path = workingDir.resolve("process.out"),
        error: Path = workingDir.resolve("process.err"),
    ): Execution {
        return withContext(Dispatchers.IO) {
            var process: Process? = null
            try {
                process = ProcessBuilder(typstCommand(workingDir))
                    .directory(workingDir.toFile())
                    .redirectOutput(ProcessBuilder.Redirect.appendTo(output.toFile()))
                    .redirectError(ProcessBuilder.Redirect.appendTo(error.toFile()))
                    .start()

                process.onExit().await()

                if (process.exitValue() == 0) {
                    val errors = error.toFile().readText()
                    if (errors.isNotBlank()) {
                        logger.warn("PDF-generering gikk bra, men ga følgende typst feil: $errors")
                    }
                    Execution.Success(pdf = workingDir.resolve("${File(typstFileName).nameWithoutExtension}.pdf"))
                } else {
                    Execution.Failure.Compilation(
                        output = output.toFile().readText(),
                        error = error.toFile().readText()
                    )
                }
            } catch (e: IOException) {
                Execution.Failure.Execution(e)
            } finally {
                process?.destroyForcibly()
            }
        }
    }

    private sealed class Execution {
        data class Success(val pdf: Path) : Execution()
        sealed class Failure : Execution() {
            data class Compilation(val output: String, val error: String) : Failure()
            data class Execution(val cause: Throwable) : Failure()
        }
    }
}