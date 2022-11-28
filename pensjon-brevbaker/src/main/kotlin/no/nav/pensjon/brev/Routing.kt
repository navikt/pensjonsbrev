package no.nav.pensjon.brev

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.micrometer.core.instrument.Tag
import no.nav.pensjon.brev.api.LetterResource
import no.nav.pensjon.brev.api.description
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.render.PensjonLatexRenderer

private val latexCompilerService = LaTeXCompilerService(requireEnv("PDF_BUILDER_URL"))
private val letterResource = LetterResource()

fun Application.brevbakerRouting(authenticationNames: Array<String>) =
    routing {

        get("/templates") {
            call.respond(letterResource.templateResource.getVedtaksbrev())
        }

        get("/templates/vedtaksbrev/{kode}") {
            val template = call.parameters
                .getOrFail<Brevkode.Vedtak>("kode")
                .let { letterResource.templateResource.getVedtaksbrev(it) }
                ?.description()

            if (template == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(template)
            }
        }

        get("/templates/redigerbar/{kode}") {
            val template = call.parameters.getOrFail<Brevkode.Redigerbar>("kode")
                .let { letterResource.templateResource.getRedigerbartBrev(it) }

            if (template == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(template)
            }
        }

        authenticate(*authenticationNames, optional = environment?.developmentMode ?: false) {
            post("/letter/vedtak") {
                val letterRequest = call.receive<VedtaksbrevRequest>()

                val letter = letterResource.create(letterRequest)
                val pdfBase64 = PensjonLatexRenderer.render(letter)
                    .let { latexCompilerService.producePDF(it, call.callId) }

                call.respond(LetterResponse(pdfBase64.base64PDF, letter.template.letterMetadata))

                Metrics.prometheusRegistry.counter("pensjon_brevbaker_letter_request_count",
                    listOf(Tag.of("brevkode", letterRequest.kode.name))).increment()
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

    }
