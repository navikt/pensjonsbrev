package no.nav.pensjon.etterlatte

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.render.HTMLDocument
import no.nav.pensjon.brev.template.render.HTMLDocumentRenderer
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.template.toCode
import java.nio.file.Path
import java.util.Base64
import kotlin.io.path.Path

object TestTags {
    const val INTEGRATION_TEST = "integration-test"

    // For visual inspection of documents/design
    const val MANUAL_TEST = "manual-test"
}


const val PDF_BUILDER_URL = "http://localhost:8081"

fun writeTestPDF(pdfFileName: String, pdf: ByteArray, path: Path = Path.of("build", "test_pdf")) {
    val file = path.resolve("${pdfFileName.replace(" ", "_")}.pdf").toFile()
    file.parentFile.mkdirs()
    file.writeBytes(pdf)
    println("Test-file written to file:${"\\".repeat(3)}${file.absolutePath}".replace('\\', '/'))
}

private val laTeXCompilerService = LaTeXCompilerService(PDF_BUILDER_URL, maxRetries = 0)


fun <ParameterType : Any> Letter<ParameterType>.renderTestPDF(
    pdfFileName: String,
    path: Path = Path.of("build", "test_pdf"),
): Letter<ParameterType> {
    Letter2Markup.render(this)
        .let {
            runBlocking {
                laTeXCompilerService.producePDF(
                    PDFRequest(
                        it.letterMarkup,
                        it.attachments,
                        language.toCode(),
                        felles,
                        template.letterMetadata.brevtype
                    )
                )
            }.base64PDF
        }
        .also { writeTestPDF(pdfFileName, Base64.getDecoder().decode(it), path) }
    return this
}

fun writeTestHTML(letterName: String, htmlLetter: HTMLDocument, buildSubDir: String = "test_html") {
    val dir = Path("build/$buildSubDir/$letterName")
    dir.toFile().mkdirs()
    htmlLetter.files.forEach { it.writeTo(dir) }
    htmlLetter.files.firstOrNull { it.fileName == "index.html" }
        ?.also {
            println("""Test index-html written to file://${dir.resolve(it.fileName).toAbsolutePath()}""")
        }
}

fun <ParameterType : Any> Letter<ParameterType>.renderTestHtml(htmlFileName: String): Letter<ParameterType> {
    Letter2Markup.render(this)
        .let {
            HTMLDocumentRenderer.render(
                it.letterMarkup,
                it.attachments,
                language,
                felles,
                template.letterMetadata.brevtype
            )
        }
        .also { writeTestHTML(htmlFileName, it) }

    return this
}
