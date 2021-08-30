package no.nav.pensjon.brev

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.pensjon.brev.api.LetterRequest
import no.nav.pensjon.brev.api.LetterResource
import no.nav.pensjon.brev.api.TemplateResource
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private val latexCompilerService = LaTeXCompilerService()
private val base64Decoder = Base64.getDecoder()

@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }


    routing {

        get("/templates") {
            call.respond(TemplateResource.getTemplates())
        }

        get("/templates/{name}") {
            val template = TemplateResource.getTemplate(call.parameters["name"]!!)
            if (template == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(template)
            }
        }

        post("/letter") {
            val letterRequest = call.receive<LetterRequest>()

            val pdfBase64 = LetterResource.create(letterRequest).render()
                .let { PdfCompilationInput(it.base64EncodedFiles()) }
                .let { latexCompilerService.producePDF(it) }

            call.respondBytes(base64Decoder.decode(pdfBase64), ContentType.Application.Pdf)
        }
    }
}

