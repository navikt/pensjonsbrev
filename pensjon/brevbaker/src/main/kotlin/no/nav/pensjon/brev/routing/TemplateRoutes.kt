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
import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap

// The documentation batch endpoint renders each template into searchable lines
// (and omits the large, per-language identical model specification), so a search
// client can build its index directly without re-flattening the documentation
// tree itself. Consumers that need the full documentation tree use
// /{kode}/doc/{language}; those that need the model specification use
// /{kode}/modelSpecification.
val EMPTY_MODEL_SPECIFICATION = TemplateModelSpecification(types = emptyMap(), letterModelTypeName = null)

inline fun <reified Kode : Brevkode<Kode>, T : BrevTemplate<BrevbakerBrevdata, Kode>> Route.templateRoutes(resource: TemplateResource<Kode, T, *>) =
    route("/${resource.name}") {

        val docCache = TemplateDocCache(resource)

        get {
            if (call.request.queryParameters["includeMetadata"] == "true") {
                call.respond(resource.listTemplatesWithMetadata())
            } else {
                call.respond(resource.listTemplatekeys())
            }
        }

        get("/all") {
            call.respond(docCache.all())
        }

        get("/all/version") {
            call.respond(docCache.version())
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


/** One template's searchable lines for a single language, as returned by the batch
 * documentation endpoint. Each line is an ordered list of text/variable segments
 * (see [SearchLine]). */
data class SearchableContent(
    val brevkode: String,
    val language: LanguageCode,
    val lines: List<SearchLine>,
)

/** A content fingerprint for the batch documentation of a template resource.
 * Changes whenever the rendered documentation changes (e.g. a new deploy) or
 * when the set of enabled templates changes (Unleash toggles). Clients can poll
 * this cheaply and only re-fetch/-index the full corpus when [hash] changes. */
data class TemplateDocVersion(val hash: String)

private fun sha256Hex(value: String): String =
    MessageDigest.getInstance("SHA-256").digest(value.toByteArray()).joinToString("") { "%02x".format(it) }

/**
 * Renders and caches the searchable documentation for a template resource.
 *
 * A template's rendered documentation is static for the lifetime of the process
 * (templates are code), so each template is rendered once and memoised. Unleash
 * toggles only change *which* templates are visible; that is evaluated live via
 * [TemplateResource.listTemplatekeys] on every call, so toggling a template
 * in/out takes effect immediately.
 */
@PublishedApi
internal class TemplateDocCache<Kode : Brevkode<Kode>>(private val resource: TemplateResource<Kode, *, *>) {
    private data class Rendered(val content: List<SearchableContent>, val hash: String)

    private val cache = ConcurrentHashMap<String, Rendered>()

    private fun render(brevkode: String): Rendered? =
        cache[brevkode] ?: resource.getTemplate(resource.kodeOf(brevkode))?.template?.let { template ->
            template.language.all()
                .map { language ->
                    SearchableContent(
                        brevkode = brevkode,
                        language = language.toCode(),
                        lines = DocumentationTextExtractor.extract(
                            TemplateDocumentationRenderer.render(template, language, EMPTY_MODEL_SPECIFICATION),
                        ),
                    )
                }
                .let { content -> Rendered(content, sha256Hex(content.toString())) }
                .also { cache[brevkode] = it }
        }

    /** Searchable documentation for every enabled template in every language. */
    fun all(): List<SearchableContent> =
        resource.listTemplatekeys().flatMap { render(it)?.content.orEmpty() }

    /** Content fingerprint of [all]; cheap and stable while the content is unchanged. */
    fun version(): TemplateDocVersion =
        resource.listTemplatekeys().sorted()
            .joinToString("|") { "$it:${render(it)?.hash.orEmpty()}" }
            .let { TemplateDocVersion(sha256Hex(it)) }
}