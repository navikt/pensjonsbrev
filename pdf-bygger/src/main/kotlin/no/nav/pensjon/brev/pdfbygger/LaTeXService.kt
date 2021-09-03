package no.nav.pensjon.brev.pdfbygger

import java.io.File
import java.nio.file.Path
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.IllegalStateException
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory

class LaTeXService {
    private val decoder = Base64.getDecoder()
    private val encoder = Base64.getEncoder()

    fun producePDF(latexFiles: Map<String, String>): PDFCompilationOutput {
        val tmpDir = createTempDirectory()

        latexFiles.forEach {
            val file = File(tmpDir.resolve(it.key).absolutePathString())
            file.createNewFile()
            file.writeBytes(decoder.decode(it.value))
        }

        val compiledPDF: PDFCompilationOutput
        try {
            compiledPDF = createLetter(tmpDir)
        } catch (e: Exception) {
            return PDFCompilationOutput(buildLog =
            """Exception while trying to compile letter:
                ${e.message}""".trimMargin()
            )
        } finally {
            File(tmpDir.absolutePathString()).deleteRecursively()
        }
        return compiledPDF
    }

    private fun createLetter(executionFolder: Path): PDFCompilationOutput {
        val letterPath = executionFolder.resolve("letter.pdf")
        val logPath = executionFolder.resolve("letter.log")

        //Run twice to resolve references such as number of pages
        runCompilationCommand(executionFolder, logPath)
        runCompilationCommand(executionFolder, logPath)


        val letterPDF = File(letterPath.absolutePathString())
        return if (letterPDF.exists()) {
            PDFCompilationOutput(pdf = encoder.encodeToString(letterPDF.readBytes()))
        } else {
            val letterCompilerLog = File(logPath.absolutePathString())
            if (!letterCompilerLog.exists()) {
                throw IllegalStateException("pdflatex compilation did not return log file or letter file")
            }
            throw PdfCompilationException( letterCompilerLog.readText())
        }
    }

    private fun runCompilationCommand(executionFolder: Path, logPath: Path) {
        val process =
            ProcessBuilder(*"xelatex -interaction=nonstopmode -halt-on-error letter.tex".split(" ").toTypedArray())
                .directory(File(executionFolder.absolutePathString()))
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()

        val timedOut = !process.waitFor(30, TimeUnit.SECONDS)
        if (timedOut) {
            throw IllegalStateException("pdf compilation timed out")
        }

        if (process.exitValue() != 0) {
            val letterCompilerLog = File(logPath.absolutePathString())
            if (!letterCompilerLog.exists()) {
                throw IllegalStateException("pdflatex compilation did not return log file or letter file")
            }
            throw PdfCompilationException( letterCompilerLog.readText())
        }
    }

}