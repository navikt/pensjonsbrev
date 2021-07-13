package no.nav.pensjon.brev.pdfbygger

import java.io.File
import java.lang.IllegalStateException
import java.nio.file.Path
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory

class LaTeXService {
    private val decoder = Base64.getDecoder()
    private val encoder = Base64.getEncoder()

    fun producePDF(latexFiles: Map<String, String>): PDFCompilationOutput {
        val tmpDir = createTempDirectory(Path.of("pdfcompilation").toAbsolutePath())

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


        //Run twice to resolve references such as number of pages
        """xelatex letter.tex""".runCommand(executionFolder)
        """xelatex letter.tex""".runCommand(executionFolder)

        val letterPath = executionFolder.resolve("letter.pdf")
        val logPath = executionFolder.resolve("letter.pdf")

        val letterPDF = File(letterPath.absolutePathString())
        return if (letterPDF.exists()) {
            PDFCompilationOutput(pdf = encoder.encodeToString(letterPDF.readBytes()))
        } else {
            val letterCompilerLog = File(logPath.absolutePathString())
            if (!letterCompilerLog.exists()) {
                throw IllegalStateException("pdflatex compilation did not return log file or letter file")
            }
            PDFCompilationOutput( buildLog = letterCompilerLog.readText())
        }
    }

    fun String.runCommand(directory: Path) {
        ProcessBuilder(*split(" ").toTypedArray())
            .directory(File(directory.absolutePathString()))
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
            .waitFor(30, TimeUnit.SECONDS)
    }
}