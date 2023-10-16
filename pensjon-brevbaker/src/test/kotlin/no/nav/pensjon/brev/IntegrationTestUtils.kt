package no.nav.pensjon.brev

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.api.model.AutobrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.render.RenderedHtmlLetter
import java.nio.file.Path
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.render.PensjonHTMLRenderer
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer
import java.util.*
import kotlin.io.path.Path

val BREVBAKER_URL = System.getenv("BREVBAKER_URL") ?: "http://localhost:8080"
const val PDF_BUILDER_URL = "http://localhost:8081"

object TestTags {
    const val INTEGRATION_TEST = "integration-test"
    // For visual inspection of documents/design
    const val MANUAL_TEST = "manual-test"
}

val httpClient = HttpClient(CIO) {
    install(HttpTimeout) {
        requestTimeoutMillis = 40000
    }
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
        }
    }
}

fun requestLetter(letterRequest: AutobrevRequest): LetterResponse =
    runBlocking {
        httpClient.post("$BREVBAKER_URL/letter/autobrev") {
            contentType(ContentType.Application.Json)
            setBody(letterRequest)
        }.body()
    }

fun requestTemplates(): Set<Brevkode.AutoBrev> = runBlocking {
    httpClient.get("$BREVBAKER_URL/templates/autobrev").body()
}

fun writeTestPDF(pdfFileName: String, pdf: String, path: Path = Path.of("build", "test_pdf")) {
    val file = path.resolve("$pdfFileName.pdf").toFile()
    file.parentFile.mkdirs()
    file.writeBytes(Base64.getDecoder().decode(pdf))
    println("Test-file written to file:${"\\".repeat(3)}${file.absolutePath}".replace('\\', '/'))
}

fun <ParameterType : Any> Letter<ParameterType>.renderTestPDF(pdfFileName: String): Letter<ParameterType> {
    PensjonLatexRenderer.render(this)
        .let { runBlocking { LaTeXCompilerService(PDF_BUILDER_URL, withRetry = false).producePDF(it, "test").base64PDF } }
        .also { writeTestPDF(pdfFileName, it) }
    return this
}

fun writeTestHTML(letterName: String, htmlLetter: RenderedHtmlLetter, buildSubDir: String = "test_html") {
    val dir = Path("build/$buildSubDir/$letterName")
    dir.toFile().mkdirs()
    htmlLetter.files.forEach { it.writeTo(dir) }
    htmlLetter.files.firstOrNull { it.fileName == "index.html" }
        ?.also {
            println("""Test index-html written to file://${dir.resolve(it.fileName).toAbsolutePath()}""")
        }
}

fun <ParameterType : Any> Letter<ParameterType>.renderTestHtml(htmlFileName: String): Letter<ParameterType> {
    writeTestHTML(htmlFileName, PensjonHTMLRenderer.render(this))
    return this
}