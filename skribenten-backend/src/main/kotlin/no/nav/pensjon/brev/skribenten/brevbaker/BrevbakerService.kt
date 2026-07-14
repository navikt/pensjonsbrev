package no.nav.pensjon.brev.skribenten.brevbaker

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import no.nav.pensjon.brev.skribenten.OboClientConfig
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.utils.io.core.Closeable
import kotlinx.io.EOFException
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.SkribentenConfig
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.common.Cache
import no.nav.pensjon.brev.skribenten.common.Cacheomraade
import no.nav.pensjon.brev.skribenten.common.cached
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.serialize.LetterMarkupJacksonModule
import no.nav.pensjon.brev.skribenten.serialize.TemplateModelSpecificationMixins
import no.nav.pensjon.brev.skribenten.serialize.registerMixin
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brev.skribenten.services.HttpClientFactory.lagHttpClient
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.seconds

class BrevbakerServiceException(msg: String) : ServiceException(msg)

// TODO: Del opp i to interfaces, ett for det som skal håndteres av fagsystem, og ett for brevbaker.
interface BrevbakerService {

    suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart): TemplateModelSpecification?

    suspend fun renderMarkup(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: BrevbakerFelles,
    ): LetterMarkupWithDataUsage

    suspend fun renderPdf(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: BrevbakerFelles,
        redigertBrev: LetterMarkup,
        alltidValgbareVedlegg: List<AlltidValgbartVedleggBrevkode>,
        redigerteVedlegg: Map<VedleggId, LetterMarkup.Attachment> = emptyMap(),
        medPDFVedlegg: Boolean,
    ): LetterResponse

    suspend fun hentRedigerbareVedleggTitler(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: BrevbakerFelles,
    ): RedigerbareVedleggTitler?

    /**
     * Lettvekts-sjekk som ikke krever brevdata: forteller om malen i det hele tatt har redigerbare
     * vedlegg. Lar oss unngå tunge pesysdata-kall i [hentRedigerbareVedleggTitler] når svaret uansett blir tomt.
     */
    suspend fun harRedigerbareVedlegg(brevkode: Brevkode.Redigerbart): Boolean

    suspend fun renderRedigerbartVedlegg(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: BrevbakerFelles,
        vedleggId: VedleggId,
    ): LetterMarkup.Attachment?

    suspend fun getTemplates(): List<TemplateDescription.Redigerbar>?
    suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart): TemplateDescription.Redigerbar?
    suspend fun getAlltidValgbareVedlegg(): Set<AlltidValgbartVedleggBrevkode>
}

class BrevbakerServiceHttp(config: OboClientConfig, authService: AuthService, val cache: Cache) : BrevbakerService, ServiceStatus, Closeable {
    private val logger = LoggerFactory.getLogger(BrevbakerServiceHttp::class.java)!!

    @Suppress("unused") // Brukes av ktor-di
    constructor(config: SkribentenConfig, authService: AuthService, cache: Cache): this(config.services.brevbaker, authService, cache)

    private val brevbakerUrl = config.url
    private val scope = config.scope
    private val client = lagHttpClient {
        defaultRequest {
            url(brevbakerUrl)
        }
        installRetry(logger, shouldNotRetry = { method, url, cause -> method == HttpMethod.Post && url.segments.last() == "pdf" && cause?.unwrapCancellationException() !is EOFException })
        engine {
            requestTimeout = 60.seconds.inWholeMilliseconds
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                registerModule(LetterMarkupJacksonModule)
                registerMixin(TemplateModelSpecificationMixins)
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            }
        }
        callIdAndOnBehalfOfClient(scope, authService)
    }

    /**
     * Get model specification for a template.
     */
    override suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart): TemplateModelSpecification? {
        val response = client.get("/templates/redigerbar/${brevkode.kode()}/modelSpecification")

        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.NotFound -> null
            else -> throw BrevbakerServiceException(
                response.bodyAsText().takeIf { it.isNotBlank() }
                    ?: "Ukjent feil oppstod ved henting av modelSpecification for brevkode: $brevkode"
            )
        }

    }

    override suspend fun renderMarkup(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: BrevbakerFelles,
    ): LetterMarkupWithDataUsage {
        val response = client.post("/letter/redigerbar/markup-usage") {
            contentType(ContentType.Application.Json)
            setBody(
                BestillBrevRequest(
                    kode = brevkode,
                    letterData = brevdata,
                    felles = felles,
                    language = spraak,
                )
            )
        }

        return if (response.status.isSuccess()) {
            response.body()
        } else {
            throw BrevbakerServiceException(
                response.bodyAsText().takeIf { it.isNotBlank() }?.let { "${response.status}: $it" }
                    ?: "Ukjent feil oppstod ved generering av markup for brevkode: $brevkode"
            )
        }
    }

    override suspend fun renderPdf(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: BrevbakerFelles,
        redigertBrev: LetterMarkup,
        alltidValgbareVedlegg: List<AlltidValgbartVedleggBrevkode>,
        redigerteVedlegg: Map<VedleggId, LetterMarkup.Attachment>,
        medPDFVedlegg: Boolean,
    ): LetterResponse {
        val response = client.post("/letter/redigerbar/pdf") {
            contentType(ContentType.Application.Json)
            setBody(
                BestillRedigertBrevRequest(
                    kode = brevkode,
                    letterData = brevdata,
                    felles = felles,
                    language = spraak,
                    letterMarkup = redigertBrev,
                    alltidValgbareVedlegg = alltidValgbareVedlegg,
                    redigerteVedlegg = redigerteVedlegg,
                    medPDFVedlegg = medPDFVedlegg
                )
            )
        }

        return if (response.status.isSuccess()) {
            response.body()
        } else {
            throw BrevbakerServiceException(
                response.bodyAsText().takeIf { it.isNotBlank() }
                    ?: "Ukjent feil oppstod ved generering av PDF for brevkode: $brevkode"
            )
        }
    }

    override suspend fun hentRedigerbareVedleggTitler(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: BrevbakerFelles,
    ): RedigerbareVedleggTitler? {
        val response = client.post("/letter/redigerbar/redigerbare-vedlegg/titler") {
            contentType(ContentType.Application.Json)
            setBody(
                BestillBrevRequest(
                    kode = brevkode,
                    letterData = brevdata,
                    felles = felles,
                    language = spraak,
                )
            )
        }

        return when {
            response.status.isSuccess() -> response.body<RedigerbareVedleggTitler>()
            response.status == HttpStatusCode.NotFound -> null
            else -> throw BrevbakerServiceException(
                response.bodyAsText().takeIf { it.isNotBlank() }?.let { "${response.status}: $it" }
                    ?: "Ukjent feil oppstod ved generering av redigerbare vedlegg for brevkode: $brevkode"
            )
        }
    }

    override suspend fun harRedigerbareVedlegg(brevkode: Brevkode.Redigerbart): Boolean =
        cache.cached(Cacheomraade.HAR_REDIGERBARE_VEDLEGG, brevkode) {
            val response = client.get("/templates/redigerbar/${brevkode.kode()}/har-redigerbare-vedlegg")

            when {
                response.status.isSuccess() -> response.body<Boolean>()
                response.status == HttpStatusCode.NotFound -> false
                else -> throw BrevbakerServiceException(
                    response.bodyAsText().takeIf { it.isNotBlank() }?.let { "${response.status}: $it" }
                        ?: "Ukjent feil oppstod ved sjekk av redigerbare vedlegg for brevkode: $brevkode"
                )
            }
        }

    override suspend fun renderRedigerbartVedlegg(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: BrevbakerFelles,
        vedleggId: VedleggId,
    ): LetterMarkup.Attachment? {
        val response = client.post("/letter/redigerbar/redigerbare-vedlegg/${vedleggId.id}") {
            contentType(ContentType.Application.Json)
            setBody(
                BestillBrevRequest(
                    kode = brevkode,
                    letterData = brevdata,
                    felles = felles,
                    language = spraak,
                )
            )
        }

        return when {
            response.status == HttpStatusCode.NotFound -> null
            response.status.isSuccess() -> response.body()
            else -> throw BrevbakerServiceException(
                response.bodyAsText().takeIf { it.isNotBlank() }?.let { "${response.status}: $it" }
                    ?: "Ukjent feil oppstod ved generering av redigerbart vedlegg for brevkode: $brevkode"
            )
        }
    }

    override suspend fun getTemplates(): List<TemplateDescription.Redigerbar>? {
        val response = client.get("/templates/redigerbar") {
            url {
                parameters.append("includeMetadata", "true")
            }
        }
        return if (response.status.isSuccess()) {
             response.body()
        } else {
            logger.error("Feilet ved henting av maler fra Brevbaker: ${response.status.value} - ${response.bodyAsText()}")
            null
        }
    }

    override suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart): TemplateDescription.Redigerbar? =
        cache.cached(Cacheomraade.REDIGERBAR_MAL, brevkode) {
            val response = client.get("/templates/redigerbar/${brevkode.kode()}")

            if (response.status.isSuccess()) {
                 response.body()
            } else if (response.status == HttpStatusCode.NotFound) {
                null
            } else {
                logger.error("Feilet ved henting av templateDescription for $brevkode: ${response.status.value} - ${response.bodyAsText()}")
                null
            }
        }

    override suspend fun getAlltidValgbareVedlegg(): Set<AlltidValgbartVedleggBrevkode> =
        cache.cached(Cacheomraade.ALLTID_VALGBARE_VEDLEGG, "alltidValgbareVedlegg") {
            val response = client.get("/letter/redigerbar/alltidValgbareVedlegg")

            if (response.status.isSuccess()) {
                response.body()
            } else {
                throw BrevbakerServiceException(
                    response.bodyAsText().takeIf { it.isNotBlank() }
                        ?: "Ukjent feil oppstod ved henting av alltid valgbare vedlegg for brev"
                )
            }
        }

    override suspend fun ping() = ping("Brevbaker") { client.get("/ping_authorized") }
    override fun close() { client.close() }
}

