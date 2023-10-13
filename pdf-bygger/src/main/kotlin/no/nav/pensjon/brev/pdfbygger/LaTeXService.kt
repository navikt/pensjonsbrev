package no.nav.pensjon.brev.pdfbygger

import org.slf4j.Logger
import java.io.File
import java.nio.file.Path
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.Long
import kotlin.String
import kotlin.Throwable
import kotlin.apply
import kotlin.getOrElse
import kotlin.io.path.createTempDirectory
import kotlin.let
import kotlin.runCatching
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private const val COMPILATION_RUNS = 2

class LaTeXService(
    private val logger: Logger,
    private val latexCommand: String = "xelatex --interaction=nonstopmode -halt-on-error",
    private val timeout: Duration = 60.seconds,
) {
    private val decoder = Base64.getDecoder()
    private val encoder = Base64.getEncoder()

    fun producePDF(latexFiles: Map<String, String>): PDFCompilationResponse {
        val tmpDir = createTempDirectory()

        latexFiles.forEach {
            tmpDir.resolve(it.key).toFile().apply {
                createNewFile()
                writeBytes(decoder.decode(it.value))
            }
        }

        return try {
            createLetter(tmpDir)
        } finally {
            tmpDir.toFile().deleteRecursively()
        }
    }

    private fun createLetter(executionFolder: Path): PDFCompilationResponse =
        when (val result: Execution = compile(executionFolder)) {
            is Execution.Success ->
                result.pdf.toFile().readBytes()
                    .let { encoder.encodeToString(it) }
                    .let { PDFCompilationResponse.Base64PDF(it) }

            is Execution.Failure.Compilation ->
                PDFCompilationResponse.Failure.Client(reason = "PDF compilation failed", output = result.output, error = result.error)

            is Execution.Failure.Execution -> {
                logger.error("latexCommand failed", result.cause)
                PDFCompilationResponse.Failure.Server(reason = "Compilation process execution failed: $latexCommand")
            }

            is Execution.Failure.Timeout ->
                PDFCompilationResponse.Failure.Timeout(reason = "Compilation timed out - spent more than: ${result.timeout} ${result.unit}")

        }

    private fun compile(executionFolder: Path): Execution {
        var result: Execution = executeCompileProcess(executionFolder)
        repeat(COMPILATION_RUNS - 1) {
            if (result is Execution.Success) {
                result = executeCompileProcess(executionFolder)
            } else {
                return@repeat
            }
        }
        return result
    }

    private fun executeCompileProcess(
        workingDir: Path,
        texFilename: String = "letter",
        output: Path = workingDir.resolve("process.out"),
        error: Path = workingDir.resolve("process.err"),
    ): Execution =
        runCatching {
            ProcessBuilder(*("$latexCommand $texFilename.tex").split(" ").toTypedArray())
                .directory(workingDir.toFile())
                .redirectOutput(ProcessBuilder.Redirect.appendTo(output.toFile()))
                .redirectError(ProcessBuilder.Redirect.appendTo(error.toFile()))
                .start()
                .let {
                    if (!it.waitFor(timeout.inWholeMilliseconds, TimeUnit.MILLISECONDS)) {
                        it.destroy()
                        Execution.Failure.Timeout(timeout.inWholeMilliseconds, TimeUnit.MILLISECONDS)
                    } else if (it.exitValue() == 0) {
                        Execution.Success(pdf = workingDir.resolve("${File(texFilename).nameWithoutExtension}.pdf"))
                    } else {
                        Execution.Failure.Compilation(output = output.toFile().readText(), error = error.toFile().readText())
                    }
                }
        }.getOrElse { Execution.Failure.Execution(it) }
}

private sealed class Execution {
    data class Success(val pdf: Path) : Execution()
    sealed class Failure : Execution() {
        data class Timeout(val timeout: Long, val unit: TimeUnit) : Failure()
        data class Compilation(val output: String, val error: String) : Failure()
        data class Execution(val cause: Throwable) : Failure()
    }
}