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
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.render.*
import java.io.File
import java.util.*
import kotlin.io.path.Path

val BREVBAKER_URL = System.getenv("BREVBAKER_URL") ?: "http://localhost:8080"
const val PDF_BUILDER_URL = "http://localhost:8081"
object TestTags {
    const val PDF_BYGGER = "pdf-bygger"
}
val httpClient = HttpClient(CIO) {
    install(HttpTimeout){
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

fun writeTestPDF(pdfFileName: String, pdf: String) {
    val file = File("build/test_pdf/$pdfFileName.pdf")
    file.parentFile.mkdirs()
    file.writeBytes(Base64.getDecoder().decode(pdf))
    println("Test-file written to file:${"\\".repeat(3)}${file.absolutePath}".replace('\\', '/'))
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


fun LaTeXCompilerService.producePdfSync(latexLetter: RenderedLatexLetter) =
    runBlocking { producePDF(latexLetter, "fra-tester") }