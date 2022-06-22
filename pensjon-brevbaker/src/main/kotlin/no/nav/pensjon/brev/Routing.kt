package no.nav.pensjon.brev

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.prometheus.client.exporter.common.TextFormat
import no.nav.pensjon.brev.api.LetterResource
import no.nav.pensjon.brev.api.description
import no.nav.pensjon.brev.api.model.LetterRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput

private val latexCompilerService = LaTeXCompilerService(requireEnv("PDF_BUILDER_URL"))
private val letterResource = LetterResource()
fun Application.brevbakerRouting(authenticationNames: Array<String>) =
    routing {

        get("/templates") {
            call.respond(letterResource.templateResource.getTemplates())
        }

        get("/templates/{name}") {
            val template = letterResource.templateResource.getTemplate(call.parameters["name"]!!)?.description()
            if (template == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(template)
            }
        }

        authenticate(*authenticationNames, optional = environment?.developmentMode ?: false) {
            post("/letter") {
                val letterRequest = call.receive<LetterRequest>()

                val letter = letterResource.create(letterRequest)
                val pdfBase64 = PdfCompilationInput(letter.render().base64EncodedFiles())
                    .let { latexCompilerService.producePDF(it, call.callId) }

                call.respond(LetterResponse(pdfBase64.base64PDF, letter.template.letterMetadata))
            }

            get("/ping_authorized") {
                val principal = call.authentication.principal as JWTPrincipal
                call.respondText("Authorized as: ${principal.subject}")
            }

        }

        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/metrics") {
            call.respondTextWriter(ContentType.parse(TextFormat.CONTENT_TYPE_004)) {
                Metrics.writeMetrics004(this, Metrics.prometheusRegistry)
            }
        }

    }
