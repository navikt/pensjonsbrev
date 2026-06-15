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

@PublishedApi
internal val DocumentationETag = AttributeKey<String>("DocumentationETag")

data class SearchableContent(
    val brevkode: String,
    val language: LanguageCode,
    val lines: List<TemplateTextBlock>,
)

private fun sha256Hex(value: String): String =
    MessageDigest.getInstance("SHA-256").digest(value.toByteArray()).joinToString("") { "%02x".format(it) }


@PublishedApi
internal class TemplateDocCache<Kode : Brevkode<Kode>>(private val resource: TemplateResource<Kode, *, *>) {
    private val objectMapper = brevbakerJacksonObjectMapper()

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
                            TemplateDocumentationRenderer.render(template, language, TemplateModelSpecification(types = emptyMap(), letterModelTypeName = null)),
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
