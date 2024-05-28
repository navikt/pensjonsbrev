package no.nav.pensjon.brev.api

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.charsets.*
import io.micrometer.core.instrument.Tag
import no.nav.pensjon.brev.Metrics
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.PDFCompilationOutput
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.jacksonObjectMapper
import no.nav.pensjon.brev.template.render.*
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import java.util.*

private val objectMapper = jacksonObjectMapper()

class TemplateResourceV2<Kode : Enum<Kode>, T : BrevTemplate<*, Kode>>(
    val name: String,
    templates: Set<T>,
    private val laTeXCompilerService: LaTeXCompilerService,
) {
    val templates: Map<Kode, T> = templates.associateBy { it.kode }

    fun createLetter(brevkode: Kode, bestillBrevRequest: BestillBrevRequest): Letter<BrevbakerBrevdata> {
        val template = templates[brevkode]?.template ?: throw NotFoundException("Template '${brevkode}' doesn't exist")

        if (!template.language.supports(bestillBrevRequest.language.toLanguage())) {
            throw BadRequestException("Template '${brevkode}' doesn't support language: ${template.language}")
        }

        return Letter(
            template = template,
            argument = parseArgument(bestillBrevRequest.letterData, template),
            language = bestillBrevRequest.language.toLanguage(),
            felles = bestillBrevRequest.felles,
        )
    }

    suspend fun renderPDF(letter: Letter<BrevbakerBrevdata>, call: ApplicationCall): PDFCompilationOutput =
        Letter2Markup.render(letter)
            .let { LatexDocumentRenderer.render(it.letterMarkup, it.attachments, letter) }
            .let { laTeXCompilerService.producePDF(it, call.callId) }

    fun renderHTML(letter: Letter<BrevbakerBrevdata>): HTMLDocument =
        Letter2Markup.render(letter)
            .let { HTMLDocumentRenderer.render(it.letterMarkup, it.attachments, letter) }

    private fun parseArgument(letterData: Any, template: LetterTemplate<*, out BrevbakerBrevdata>): BrevbakerBrevdata =
        try {
            objectMapper.convertValue(letterData, template.letterDataType.java)
        } catch (e: IllegalArgumentException) {
            throw ParseLetterDataException("Could not deserialize letterData: ${e.message}", e)
        }
}

inline fun <reified Kode : Enum<Kode>, T : BrevTemplate<*, Kode>> Routing.templateRoutes(resourceV2: TemplateResourceV2<Kode, T>) =
    route("/v2") {
        route("/templates/${resourceV2.name}") {
            get { call.respond(resourceV2.templates.keys) }

            route("/{kode}") {
                get {
                    // TODO: Finn ut om description har unødvendig informasjon, eller om den er dekkende
                    val template = call.parameters.getOrFail<Kode>("kode")
                        .let { resourceV2.templates[it] }
                        ?.template
                        ?.description()

                    if (template != null) {
                        call.respond(template)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }

                get("/doc/{language}") {
                    val language = call.parameters.getOrFail<LanguageCode>("language").toLanguage()

                    val template = call.parameters.getOrFail<Kode>("kode")
                        .let { resourceV2.templates[it]?.template }
                        ?.takeIf { it.language.supports(language) }

                    if (template != null) {
                        call.respond(TemplateDocumentationRenderer.render(template, language))
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }

                get("/modelSpecification") {
                    val template = call.parameters.getOrFail<Kode>("kode")
                        .let { resourceV2.templates[it]?.template }

                    if (template != null) {
                        call.respond(template.modelSpecification)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }

                route("/letter") {

                    post<BestillBrevRequest>("/pdf") { brevbestilling ->
                        val brevkode = call.parameters.getOrFail<Kode>("kode")
                        val letter = resourceV2.createLetter(brevkode, brevbestilling)
                        val pdf = resourceV2.renderPDF(letter, call)

                        call.respond(
                            LetterResponse.V2(
                                file = Base64.getDecoder().decode(pdf.base64PDF),
                                contentType = ContentType.Application.Pdf.toString(),
                                letterMetadata = letter.template.letterMetadata
                            )
                        )

                        Metrics.prometheusRegistry.counter(
                            "pensjon_brevbaker_letter_request_count",
                            listOf(Tag.of("brevkode", brevkode.name))
                        ).increment()
                    }

                    post<BestillBrevRequest>("/html") { brevbestilling ->
                        val brevkode = call.parameters.getOrFail<Kode>("kode")
                        val letter = resourceV2.createLetter(brevkode, brevbestilling)
                        val html = resourceV2.renderHTML(letter)

                        call.respond(
                            LetterResponse.V2(
                                file = html.files.first().content.toByteArray(Charsets.UTF_8),
                                contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8).toString(),
                                letterMetadata = letter.template.letterMetadata
                            )
                        )

                        Metrics.prometheusRegistry.counter(
                            "pensjon_brevbaker_letter_request_count",
                            listOf(Tag.of("brevkode", brevkode.name))
                        ).increment()
                    }
                }
            }
        }
    }

