package no.nav.pensjon.brev.pdfbygger

import java.io.File
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.TimeUnit

class LaTeXService(private val rootPath: String) {

    fun producePDF(latexFiles: Map<String, ByteArray>): PDFCompilationOutput {
        val tmpDir = File(rootPath + UUID.randomUUID().toString() + '/')
        tmpDir.mkdirs()

        latexFiles.forEach {
            val file = File("""${tmpDir.absolutePath}\${it.key}""")
            file.createNewFile()
            file.writeBytes(it.value)
        }

        val compiledPDF: PDFCompilationOutput
        try {
            compiledPDF = createLetter(tmpDir.absolutePath)
        } catch (e: Exception) {
            return PDFCompilationOutput(buildLog =
            """Exception while trying to compile letter:
                ${e.message}""".trimMargin()
            )
        } finally {
            tmpDir.deleteRecursively()
        }
        return compiledPDF
    }

    fun createLetter(executionFolder: String): PDFCompilationOutput {

        """cd $executionFolder && pdflatex letter.tex""".runCommand()

        val letterPDF = File("$executionFolder\\letter.pdf")
        return if (letterPDF.exists()) {
            PDFCompilationOutput(pdf = letterPDF.readBytes())
        } else {
            val letterCompilerLog = File("$executionFolder\\letter.log")
            if (!letterCompilerLog.exists()) {
                throw IllegalStateException("pdflatex compilation did not return log file or letter file")
            }
            PDFCompilationOutput( buildLog = letterCompilerLog.readText())
        }
    }

    fun String.runCommand() {
        ProcessBuilder(*split(" ").toTypedArray())
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
            .waitFor(30, TimeUnit.SECONDS)
    }
}