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
import no.nav.pensjon.brev.api.*
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.TemplateModelSpecification
import no.nav.pensjon.brev.template.render.*
import no.nav.pensjon.etterlatte.etterlatteRouting

private val latexCompilerService = LaTeXCompilerService(requireEnv("PDF_BUILDER_URL"))
private val letterResource = LetterResource()

data class RedigerbarTemplateDescription(
    val description: TemplateDescription,
    val modelSpecification: TemplateModelSpecification,
)

fun Application.brevbakerRouting(authenticationNames: Array<String>) =
    routing {
        route("/templates") {

            route("/autobrev") {
                get {
                    call.respond(letterResource.templateResource.getAutoBrev())
                }

                get("/{kode}") {
                    val template = call.parameters
                        .getOrFail<Brevkode.AutoBrev>("kode")
                        .let { letterResource.templateResource.getAutoBrev(it) }
                        ?.description()

                    if (template == null) {
                        call.respond(HttpStatusCode.NotFound)
                    } else {
                        call.respond(template)
                    }
                }
            }

            route("/redigerbar") {
                get {
                    call.respond(letterResource.templateResource.getRedigerbareBrev())
                }

                get("/{kode}") {
                    val template = call.parameters.getOrFail<Brevkode.Redigerbar>("kode")
                        .let { letterResource.templateResource.getRedigerbartBrev(it) }

                    if (template == null) {
                        call.respond(HttpStatusCode.NotFound)
                    } else {
                        call.respond(RedigerbarTemplateDescription(template.description(), template.modelSpecification))
                    }
                }
            }
        }

        authenticate(*authenticationNames, optional = environment?.developmentMode ?: false) {
            route("/letter") {

                post("/autobrev") {
                    val letterRequest = call.receive<AutobrevRequest>()

                    val letter = letterResource.create(letterRequest)
                    val pdfBase64 = PensjonLatexRenderer.render(letter)
                        .let { latexCompilerService.producePDF(it, call.callId) }

                    call.respond(LetterResponse(pdfBase64.base64PDF, letter.template.letterMetadata))

                    Metrics.prometheusRegistry.counter(
                        "pensjon_brevbaker_letter_request_count",
                        listOf(Tag.of("brevkode", letterRequest.kode.name))
                    ).increment()
                }

                post("/redigerbar") {
                    val letterRequest = call.receive<RedigerbartbrevRequest>()

                    call.respond(PensjonJsonRenderer.render(letterResource.create(letterRequest)))
                }

            }
            get("/ping_authorized") {
                val principal = call.authentication.principal<JWTPrincipal>()
                call.respondText("Authorized as: ${principal?.subject}")
            }
            route("etterlatte") {
                etterlatteRouting(latexCompilerService)
            }
        }

        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

    }
