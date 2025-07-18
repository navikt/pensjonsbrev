package no.nav.pensjon.brev.pdfbygger.latex

import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.pensjon.brev.pdfbygger.PDFCompilationResponse
import no.nav.pensjon.brev.template.render.DocumentFile
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.createTempDirectory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

const val LATEX_CONFIG_PATH = "pdfBygger.latex"
private const val COMPILATION_RUNS = 2

class LatexCompileService(
    latexCommand: String,
    private val compileTimeout: Duration,
    private val tmpBaseDir: Path? = Path.of("/app/tmp")
) {
    constructor(config: ApplicationConfig) : this(
        latexCommand = config.propertyOrNull("latexCommand")?.getString()
            ?: "xelatex --interaction=nonstopmode -halt-on-error",
        compileTimeout = config.propertyOrNull("compileTimeout")?.getString()?.let { Duration.parse(it) }
            ?: 300.seconds,
        tmpBaseDir = Path.of(config.property("compileTmpDir").getString())
    )

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val latexCommand = latexCommand.split(" ").filter { it.isNotBlank() } + "letter.tex"

    suspend fun createLetter(latexFiles: List<DocumentFile>): PDFCompilationResponse {
        val tmpDir = createTempDirectory(tmpBaseDir)

        return try {
            latexFiles.forEach {
                tmpDir.resolve(it.fileName).toFile().apply {
                    createNewFile()
                    writeText(it.content)
                }
            }

            when (val result: Execution = compile(tmpDir)) {
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
                    logger.error("latexCommand failed", result.cause)
                    PDFCompilationResponse.Failure.Server(reason = "Compilation process execution failed: $latexCommand")
                }

                is Execution.Failure.Timeout ->
                    PDFCompilationResponse.Failure.Timeout("Compilation timed out in ${result.timeout}: completed ${result.completedRuns} runs")
            }
        } finally {
            tmpDir.toFile().deleteRecursively()
        }
    }

    private suspend fun compile(executionFolder: Path): Execution {
        var runs = 0
        return withTimeoutOrNull(compileTimeout) {
            var result: Execution = executeCompileProcess(executionFolder)
            runs++

            repeat(COMPILATION_RUNS - 1) {
                if (result is Execution.Success) {
                    result = executeCompileProcess(executionFolder)
                    runs++
                } else {
                    return@repeat
                }
            }
            return@withTimeoutOrNull result
        } ?: Execution.Failure.Timeout(completedRuns = runs, timeout = compileTimeout)
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
                    .apply { environment()["TEXINPUTS"] = ".:/app/pensjonsbrev_latex//:" }
                    .start()

                while (process.isAlive) {
                    delay(50.milliseconds)
                }

                if (process.exitValue() == 0) {
                    Execution.Success(pdf = workingDir.resolve("${File(texFilename).nameWithoutExtension}.pdf"))
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
}

private sealed class Execution {
    data class Success(val pdf: Path) : Execution()
    sealed class Failure : Execution() {
        data class Compilation(val output: String, val error: String) : Failure()
        data class Execution(val cause: Throwable) : Failure()
        data class Timeout(val completedRuns: Int, val timeout: Duration) : Failure()
    }
}