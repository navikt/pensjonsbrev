package no.nav.pensjon.brev

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import java.io.File
import java.util.*

val BREVBAKER_URL = System.getenv("BREVBAKER_URL") ?: "http://localhost:8080"
val PDF_BUILDER_URL = "http://localhost:8081"
object TestTags {
    const val PDF_BYGGER = "pdf-bygger"
}
val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
        }
    }
}

fun requestLetter(letterRequest: VedtaksbrevRequest): LetterResponse =
    runBlocking {
        httpClient.post("$BREVBAKER_URL/letter/vedtak") {
            contentType(ContentType.Application.Json)
            setBody(letterRequest)
        }.body()
    }

@Deprecated("Erstattet med requestLetter(letterRequest: VedtaksbrevRequest")
fun requestLetter(letterRequest: LetterRequest): LetterResponse {
    return runBlocking {
        httpClient.post("$BREVBAKER_URL/letter") {
            contentType(ContentType.Application.Json)
            setBody(letterRequest)
        }.body()
    }
}

fun requestTemplates(): Set<Brevkode.Vedtak> = runBlocking {
    httpClient.get("$BREVBAKER_URL/templates").body()
}

fun writeTestPDF(pdfFileName: String, pdf: String) {
    val file = File("build/test_pdf/$pdfFileName.pdf")
    file.parentFile.mkdirs()
    file.writeBytes(Base64.getDecoder().decode(pdf))
    println("Test-file written to file:${"\\".repeat(3)}${file.absolutePath}".replace('\\', '/'))
}

fun LaTeXCompilerService.producePdfSync(pdfCompilationInput: PdfCompilationInput) =
    runBlocking { producePDF(pdfCompilationInput, "fra-tester") }