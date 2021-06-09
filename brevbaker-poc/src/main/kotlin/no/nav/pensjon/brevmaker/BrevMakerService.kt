package no.nav.pensjon.brevmaker

import org.springframework.stereotype.Service
import java.io.File
import java.util.concurrent.TimeUnit

@Service
class BrevMakerService {
    fun lagBrev(letterTemplate: LetterTemplate, fields: Map<String, String>): ByteArray {
        writeFieldsToTexParameters(fields, letterTemplate )
        """pdflatex ${letterTemplate.letterId}.tex""".runCommand()
        val brevPdf = File("${letterTemplate.letterId}.pdf")
        return brevPdf.readBytes()
    }

    private fun writeFieldsToTexParameters(fields: Map<String, String>, letterTemplate: LetterTemplate) {
        File("params.tex").writeText(letterTemplate.mapFields(fields))
    }

    fun String.runCommand() {
        ProcessBuilder(*split(" ").toTypedArray())
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
            .waitFor(60, TimeUnit.MINUTES)
    }
}