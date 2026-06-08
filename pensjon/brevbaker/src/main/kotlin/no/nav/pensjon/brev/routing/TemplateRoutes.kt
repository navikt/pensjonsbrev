package no.nav.pensjon.brev.routing

import io.ktor.http.*
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.*
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
import no.nav.pensjon.brev.template.render.TemplateTextExtractor
import no.nav.pensjon.brev.template.render.TemplateTextBlock
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
            val etag = "\"${docCache.etag()}\""
            call.response.header(HttpHeaders.ETag, etag)
            // `no-cache` (not `no-store`): clients may cache the body but must
            // revalidate with If-None-Match, so unchanged corpora cost a cheap 304.
            call.response.header(HttpHeaders.CacheControl, "no-cache")
            if (call.request.header(HttpHeaders.IfNoneMatch)?.let { ifNoneMatchAllows(it, etag) } == true) {
                call.respond(HttpStatusCode.NotModified)
            } else {
                call.respond(docCache.all())
            }
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
 * (see [TemplateTextBlock]). */
data class SearchableContent(
    val brevkode: String,
    val language: LanguageCode,
    val lines: List<TemplateTextBlock>,
)

/** A content fingerprint for the batch documentation of a template resource.
 * Changes whenever the rendered documentation changes (e.g. a new deploy) or
 * when the set of enabled templates changes (Unleash toggles). Served as the
 * HTTP ETag of the batch endpoint so clients revalidate cheaply (304) and only
 * re-fetch/-index the full corpus when it actually changes. */
private fun sha256Hex(value: String): String =
    MessageDigest.getInstance("SHA-256").digest(value.toByteArray()).joinToString("") { "%02x".format(it) }

/** Whether an `If-None-Match` header value matches [etag] (or is the `*` wildcard). */
@PublishedApi
internal fun ifNoneMatchAllows(ifNoneMatch: String, etag: String): Boolean =
    ifNoneMatch.split(",").map { it.trim() }.any { it == etag || it == "*" }


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
                        lines = TemplateTextExtractor.extract(
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

    /** Content fingerprint of [all]; cheap and stable while the content is
     *  unchanged. Served as the batch endpoint's HTTP ETag. */
    fun etag(): String =
        resource.listTemplatekeys().sorted()
            .joinToString("|") { "$it:${render(it)?.hash.orEmpty()}" }
            .let(::sha256Hex)
}