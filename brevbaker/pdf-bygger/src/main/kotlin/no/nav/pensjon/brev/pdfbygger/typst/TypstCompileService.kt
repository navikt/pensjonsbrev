package no.nav.pensjon.brev.pdfbygger.typst

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse
import no.nav.pensjon.brev.template.render.DocumentFile
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import kotlin.io.path.createTempDirectory
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries


private const val DEFAULT_TYPST_TEMPLATE_DIR = "/app/typst"

class TypstCompileService(
    private val templateDir: Path = Path.of(DEFAULT_TYPST_TEMPLATE_DIR)
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private fun typstCommand(workingDir: Path) = listOf(
        "typst", "compile",
        "--root", workingDir.toString(),
        "--pdf-standard", "ua-1",
        "--ignore-system-fonts",
        "--font-path", "/usr/share/fonts/truetype/sourcesans3",
        "--font-path", "/usr/share/fonts/truetype/noto",
        "letter.typ",
        // Output PDF to stdout instead of writing to a file
        "-",
    )

    suspend fun createLetter(typstFiles: List<DocumentFile>): PDFCompilationResponse {
        val tmpDir = createTempDirectory(Path.of("/tmp"))

        return try {
            symlinkTemplateFiles(templateDir, tmpDir)

            // Write generated files (input.typ, letter.typ)
            typstFiles.forEach {
                tmpDir.resolve(it.fileName).toFile().writeText(it.content)
            }

            when (val result: Execution = executeCompileProcess(tmpDir)) {
                is Execution.Success ->
                    PDFCompilationResponse.Success(PDFCompilationOutput(result.pdfBytes))

                is Execution.Failure.Compilation ->
                    PDFCompilationResponse.Failure.Client(
                        reason = "PDF compilation failed",
                        output = null,
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

    private fun symlinkTemplateFiles(source: Path, destination: Path) {
        if (!source.exists()) {
            throw IllegalStateException("Template directory does not exist: $source")
        }

        source.listDirectoryEntries().forEach { entry ->
            Files.createSymbolicLink(destination.resolve(entry.fileName), entry)
        }
    }

    private suspend fun executeCompileProcess(workingDir: Path): Execution {
        return withContext(Dispatchers.IO) {
            var process: Process? = null
            try {
                process = ProcessBuilder(typstCommand(workingDir))
                    .directory(workingDir.toFile())
                    .start()

                // Read stdout (PDF bytes) and stderr (error messages) concurrently
                // to avoid deadlock when either buffer fills up
                val stdoutFuture = CompletableFuture.supplyAsync { process.inputStream.readAllBytes() }
                val stderrContent = String(process.errorStream.readAllBytes())
                val pdfBytes = stdoutFuture.await()

                process.onExit().await()

                if (process.exitValue() == 0) {
                    if (stderrContent.isNotBlank()) {
                        logger.warn("PDF-generering gikk bra, men ga følgende typst feil: $stderrContent")
                    }
                    Execution.Success(pdfBytes = pdfBytes)
                } else {
                    Execution.Failure.Compilation(error = stderrContent)
                }
            } catch (e: IOException) {
                Execution.Failure.Execution(e)
            } finally {
                process?.destroyForcibly()
            }
        }
    }

    private sealed class Execution {
        class Success(val pdfBytes: ByteArray) : Execution()
        sealed class Failure : Execution() {
            data class Compilation(val error: String) : Failure()
            data class Execution(val cause: Throwable) : Failure()
        }
    }
}