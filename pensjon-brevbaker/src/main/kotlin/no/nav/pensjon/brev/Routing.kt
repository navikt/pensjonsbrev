package no.nav.pensjon.brev

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.prometheus.client.exporter.common.TextFormat
import no.nav.pensjon.brev.api.LetterResource
import no.nav.pensjon.brev.api.TemplateResource
import no.nav.pensjon.brev.api.description
import no.nav.pensjon.brev.api.model.LetterRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PdfCompilationInput

private val latexCompilerService = LaTeXCompilerService(requireEnv("PDF_BUILDER_URL"))

fun Application.brevbakerRouting(authenticationNames: Array<String>, deploymentEnvironment: String) =
    routing {

        get("/templates") {
            call.respond(TemplateResource.getTemplates())
        }

        get("/templates/{name}") {
            val template = TemplateResource.getTemplate(call.parameters["name"]!!)?.description()
            if (template == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(template)
            }
        }

        authenticate(*authenticationNames, optional = environment.developmentMode) {
            post("/letter") {
                val letterRequest = call.receive<LetterRequest>()

                val letterResource = LetterResource.create(letterRequest)
                val pdfBase64 = letterResource.render()
                    .let { PdfCompilationInput(it.base64EncodedFiles()) }
                    .let { latexCompilerService.producePDF(it) }
                call.respond(LetterResponse(pdfBase64.base64PDF, letterResource.template.letterMetadata))
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
