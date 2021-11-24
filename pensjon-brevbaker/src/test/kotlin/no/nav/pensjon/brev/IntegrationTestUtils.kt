package no.nav.pensjon.brev

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.api.model.LetterRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import java.io.File
import java.util.*

val BREVBAKER_URL = "http://localhost:8080"
val PDF_BUILDER_URL = "http://localhost:8081"
object TestTags {
    const val PDF_BYGGER = "pdf-bygger"
}
val httpClient = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = JacksonSerializer{
            registerModule(JavaTimeModule())
        }
    }
}

fun requestLetter(letterRequest: LetterRequest): LetterResponse {
    return runBlocking {
        httpClient.post("$BREVBAKER_URL/letter") {
            contentType(ContentType.Application.Json)
            body = letterRequest
        }
    }
}

fun writeTestPDF(pdfFileName: String, pdf: String) {
    val file = File("build/test_pdf/$pdfFileName.pdf")
    file.parentFile.mkdirs()
    file.writeBytes(Base64.getDecoder().decode(pdf))
    println("Test-file written to file:${"\\".repeat(3)}${file.absolutePath}".replace('\\', '/'))
}

fun LaTeXCompilerService.producePdfSync(pdfCompilationInput: PdfCompilationInput) =
    runBlocking { producePDF(pdfCompilationInput) }