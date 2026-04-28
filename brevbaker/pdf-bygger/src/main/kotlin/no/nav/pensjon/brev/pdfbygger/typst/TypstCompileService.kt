package no.nav.pensjon.brev.pdfbygger.typst

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Path


private const val DEFAULT_TYPST_TEMPLATE_DIR = "/app/typst"
private const val MAX_CONCURRENT_COMPILES_ENV = "PDF_BYGGER_MAX_CONCURRENT_COMPILES"

private fun defaultMaxConcurrentCompiles(): Int =
    System.getenv(MAX_CONCURRENT_COMPILES_ENV)?.toIntOrNull()?.takeIf { it > 0 }
        ?: Runtime.getRuntime().availableProcessors().coerceAtLeast(1)

open class TypstCompileService(
    private val templateDir: Path = Path.of(DEFAULT_TYPST_TEMPLATE_DIR),
    maxConcurrentCompiles: Int = defaultMaxConcurrentCompiles(),
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Begrenser hvor mange `typst compile`-subprosesser som kjører samtidig per pod.
     *
     * Uten en slik grense kan en byge med samtidige requests fyre opp et høyt antall
     * CPU-tunge subprosesser som thrash'er om CPU. Det gjør at hver enkelt request blir
     * tregere, og at Ktor/Netty blir CPU-sultet og ikke rekker å svare på readiness-proben
     * innen timeout – kubelet markerer da poden NotReady, ingress fjerner den fra rotasjon,
     * og lasten flyttes til andre pods. Resultatet er flapping og ujevn lastfordeling.
     *
     * Default settes til antall tilgjengelige vCPU, og kan
     * overstyres via env var [MAX_CONCURRENT_COMPILES_ENV]
     */
    private val compileSemaphore = Semaphore(permits = maxConcurrentCompiles).also {
        logger.info("TypstCompileService startet med maxConcurrentCompiles=$maxConcurrentCompiles")
    }

    private fun typstCommand() = listOf(
        "typst", "compile",
        "--root", templateDir.toString(),
        "--pdf-standard", "a-3a",
        "--ignore-system-fonts",
        "--font-path", "/app/typst/fonts/truetype/sourcesans3",
        "--font-path", "/usr/share/fonts/truetype/noto",
        // Read input from stdin
        "-",
        // Output PDF to stdout
        "-",
    )

    open suspend fun createLetter(writeLetter: (TypstFileWriter) -> Unit): PDFCompilationResponse {
        return compileSemaphore.withPermit {
            when (val result: Execution = executeCompileProcess(writeLetter)) {
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
                    PDFCompilationResponse.Failure.Server(reason = "Compilation process execution failed: ${typstCommand()}")
                }
            }
        }
    }

    private suspend fun executeCompileProcess(writeLetter: (TypstFileWriter) -> Unit): Execution {
        return withContext(Dispatchers.IO) {
            var process: Process? = null
            try {
                process = ProcessBuilder(typstCommand())
                    .directory(templateDir.toFile())
                    .start()

                // Write letter content directly to stdin
                process.outputStream.writer(Charsets.UTF_8).use { writeLetter(TypstFileWriter(it)) }

                // Read stdout (PDF bytes) and stderr (error messages) concurrently
                // to avoid deadlock when either buffer fills up
                val stdoutDeferred = async(Dispatchers.IO) { process.inputStream.readAllBytes() }
                val stderrContent = String(process.errorStream.readAllBytes(), Charsets.UTF_8)
                val pdfBytes = stdoutDeferred.await()

                // Both streams fully drained means the process has already exited;
                // waitFor() returns immediately without scheduling async work.
                val exitCode = process.waitFor()

                if (exitCode == 0) {
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