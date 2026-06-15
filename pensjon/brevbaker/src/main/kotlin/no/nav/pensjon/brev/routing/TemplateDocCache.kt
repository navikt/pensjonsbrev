package no.nav.pensjon.brev.routing

import io.ktor.util.AttributeKey
import no.nav.pensjon.brev.api.TemplateResource
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.brevbakerJacksonObjectMapper
import no.nav.pensjon.brev.template.render.TemplateDocumentationRenderer
import no.nav.pensjon.brev.template.render.TemplateTextBlock
import no.nav.pensjon.brev.template.render.TemplateTextExtractor
import no.nav.pensjon.brev.template.toCode
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

// The documentation batch endpoint renders each template into searchable lines
// (and omits the large, per-language identical model specification), so a search
// client can build its index directly without re-flattening the documentation
// tree itself. Consumers that need the full documentation tree use
// /{kode}/doc/{language}; those that need the model specification use
// /{kode}/modelSpecification.
private val EMPTY_MODEL_SPECIFICATION = TemplateModelSpecification(types = emptyMap(), letterModelTypeName = null)

/** Carries the batch-documentation content fingerprint from the route to the
 *  ConditionalHeaders/CachingHeaders plugins, which turn it into `ETag`/`304`
 *  revalidation and `Cache-Control: no-cache`. */
@PublishedApi
internal val DocumentationETag = AttributeKey<String>("DocumentationETag")

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
 * when the set of enabled templates changes (Unleash toggles). Published via
 * [DocumentationETag] so the ConditionalHeaders plugin can serve it as the batch
 * endpoint's HTTP ETag and clients revalidate cheaply (304). */
private fun sha256Hex(value: String): String =
    MessageDigest.getInstance("SHA-256").digest(value.toByteArray()).joinToString("") { "%02x".format(it) }


@PublishedApi
internal class TemplateDocCache<Kode : Brevkode<Kode>>(private val resource: TemplateResource<Kode, *, *>) {
    private val objectMapper = brevbakerJacksonObjectMapper()

    // Per-brevkode content fingerprint. Cheap to retain and stable at runtime
    // (the documentation only changes between deploys), so it drives the ETag
    // without keeping the rendered object graph alive.
    private val hashes = ConcurrentHashMap<String, String>()

    /** The finished, gzipped JSON for the currently enabled set of templates,
     *  memoized by its [etag]. This is the only sizeable thing the cache retains. */
    private data class Payload(val etag: String, val gzipped: ByteArray)
    private val payload = AtomicReference<Payload?>()

    /** Renders one template (all languages) into searchable lines. The rendered
     *  objects are intentionally *not* retained; only the fingerprint is cached. */
    private fun render(brevkode: String): List<SearchableContent>? =
        resource.getTemplate(resource.kodeOf(brevkode))?.template?.let { template ->
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
                .also { content -> hashes[brevkode] = sha256Hex(content.toString()) }
        }

    private fun hashOf(brevkode: String): String? =
        hashes[brevkode] ?: render(brevkode)?.let { hashes[brevkode] }

    /** Content fingerprint of [gzippedJson]; cheap and stable while the content is
     *  unchanged. Served as the batch endpoint's HTTP ETag. */
    fun etag(): String =
        resource.listTemplatekeys().sorted()
            .joinToString("|") { "$it:${hashOf(it).orEmpty()}" }
            .let(::sha256Hex)

    /** Gzipped JSON array of searchable documentation for every enabled template
     *  in every language. Memoized per [etag]; rebuilt only when the content or
     *  the enabled-template set changes. */
    fun gzippedJson(): ByteArray {
        val etag = etag()
        payload.get()?.takeIf { it.etag == etag }?.let { return it.gzipped }
        val content = resource.listTemplatekeys().flatMap { render(it).orEmpty() }
        val gzipped = gzip(objectMapper.writeValueAsBytes(content))
        payload.set(Payload(etag, gzipped))
        return gzipped
    }

    /** Uncompressed JSON, for the rare client that does not accept gzip. */
    fun json(): ByteArray = gunzip(gzippedJson())

    private fun gzip(bytes: ByteArray): ByteArray =
        ByteArrayOutputStream().also { bos -> GZIPOutputStream(bos).use { it.write(bytes) } }.toByteArray()

    private fun gunzip(bytes: ByteArray): ByteArray =
        GZIPInputStream(ByteArrayInputStream(bytes)).use { it.readBytes() }
}
