package no.nav.pensjon.brev.routing

import io.ktor.http.*
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.api.TemplateResource
import no.nav.pensjon.brev.api.model.maler.AutomatiskBrevkode
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.api.toLanguage
import no.nav.pensjon.brev.template.BrevTemplate
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.TemplateModelSpecificationFactory
import no.nav.pensjon.brev.template.render.DocumentationTextExtractor
import no.nav.pensjon.brev.template.render.SearchLine
import no.nav.pensjon.brev.template.render.TemplateDocumentationRenderer
import no.nav.pensjon.brev.template.toCode
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification

// The documentation batch endpoint renders each template into searchable lines
// (and omits the large, per-language identical model specification), so a search
// client can build its index directly without re-flattening the documentation
// tree itself. Consumers that need the full documentation tree use
// /{kode}/doc/{language}; those that need the model specification use
// /{kode}/modelSpecification.
val EMPTY_MODEL_SPECIFICATION = TemplateModelSpecification(types = emptyMap(), letterModelTypeName = null)

/** One template's searchable lines for a single language, as returned by the batch
 * documentation endpoint. Each line is an ordered list of text/variable segments
 * (see [SearchLine]). */
data class SearchableContent(
    val brevkode: String,
    val language: LanguageCode,
    val lines: List<SearchLine>,
)

inline fun <reified Kode : Brevkode<Kode>, T : BrevTemplate<BrevbakerBrevdata, Kode>> Route.templateRoutes(resource: TemplateResource<Kode, T, *>) =
    route("/${resource.name}") {

        get {
            if (call.request.queryParameters["includeMetadata"] == "true") {
                call.respond(resource.listTemplatesWithMetadata())
            } else {
                call.respond(resource.listTemplatekeys())
            }
        }

        // Batch documentation for every template in every supported language, so a
        // client (e.g. brevoppskrift's full-text index) can build its index with a
        // single request instead of one request per template per language.
        get("/all") {
            val entries = resource.listTemplatekeys().flatMap { key ->
                val template = resource.getTemplate(resource.kodeOf(key))?.template ?: return@flatMap emptyList()
                template.language.all().map { language ->
                    SearchableContent(
                        brevkode = key,
                        language = language.toCode(),
                        lines = DocumentationTextExtractor.extract(
                            TemplateDocumentationRenderer.render(template, language, EMPTY_MODEL_SPECIFICATION),
                        ),
                    )
                }
            }
            call.respond(entries)
        }

        route("/{kode}") {
            get {
                val template = call.kode(resource)
                    .let { resource.getTemplate(it) }
                    ?.description()

                if (template != null) {
                    call.respond(template)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            get("/doc/{language}") {
                val language = call.parameters.getOrFail<LanguageCode>("language").toLanguage()

                val template = call.kode(resource)
                    .let { resource.getTemplate(it)?.template }
                    ?.takeIf { it.language.supports(language) }

                if (template != null) {
                    call.respond(TemplateDocumentationRenderer.render(template, language, template.modelSpecification()))
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            get("/modelSpecification") {
                val template = call.kode(resource)
                    .let { resource.getTemplate(it)?.template }

                if (template != null) {
                    call.respond(template.modelSpecification())
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }

fun LetterTemplate<*, *>.modelSpecification() = TemplateModelSpecificationFactory(this.letterDataType).build()

// TODO: Med riktig typing burde heile denne metoden vera unødvendig
fun <Kode : Brevkode<Kode>> ApplicationCall.kode(resource: TemplateResource<Kode, *, *>): Kode = parameters.getOrFail<String>("kode")
    .let { resource.kodeOf(it) }

fun <Kode: Brevkode<Kode>> TemplateResource<Kode,*,*>.kodeOf(kode: String): Kode =
    if (name == "autobrev") {
        AutomatiskBrevkode(kode)
    } else {
        RedigerbarBrevkode(kode)
    } as Kode