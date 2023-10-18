package no.nav.pensjon.brev.pdfbygger

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.nio.file.Path
import java.util.*
import kotlin.io.path.createTempDirectory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

private const val COMPILATION_RUNS = 2

class LaTeXService(
    latexCommand: String,
    latexParallelism: Int,
    private val timeout: Duration,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val decoder = Base64.getDecoder()
    private val encoder = Base64.getEncoder()
    private val latexCommand = latexCommand.split(" ").filter { it.isNotBlank() } + "letter.tex"
    private val parallelismSemaphore = latexParallelism.takeIf { it > 0 }?.let { Semaphore(it) }

    suspend fun producePDF(latexFiles: Map<String, String>): PDFCompilationResponse {
        val tmpDir = createTempDirectory()

        latexFiles.forEach {
            tmpDir.resolve(it.key).toFile().apply {
                createNewFile()
                writeBytes(decoder.decode(it.value))
            }
        }

        return try {
            withTimeoutOrNull(timeout) {
                parallelismSemaphore?.withPermit {
                    createLetter(tmpDir)
                } ?: createLetter(tmpDir)
            } ?: PDFCompilationResponse.Failure.Timeout(reason = "Compilation timed out - spent more than: $timeout")
        } finally {
            tmpDir.toFile().deleteRecursively()
        }
    }

    private suspend fun createLetter(executionFolder: Path): PDFCompilationResponse =
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
        }

    private suspend fun compile(executionFolder: Path): Execution {
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

    private suspend fun executeCompileProcess(
        workingDir: Path,
        texFilename: String = "letter",
        output: Path = workingDir.resolve("process.out"),
        error: Path = workingDir.resolve("process.err"),
    ): Execution {
        return withContext(Dispatchers.IO) {
            var process: Process? = null
            try {
                process = ProcessBuilder(latexCommand)
                    .directory(workingDir.toFile())
                    .redirectOutput(ProcessBuilder.Redirect.appendTo(output.toFile()))
                    .redirectError(ProcessBuilder.Redirect.appendTo(error.toFile()))
                    .start()

                while (process.isAlive) {
                    delay(50.milliseconds)
                }

                if (process.exitValue() == 0) {
                    Execution.Success(pdf = workingDir.resolve("${File(texFilename).nameWithoutExtension}.pdf"))
                } else {
                    Execution.Failure.Compilation(output = output.toFile().readText(), error = error.toFile().readText())
                }
            } catch (e: IOException) {
                Execution.Failure.Execution(e)
            } finally {
                process?.destroyForcibly()
            }
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