package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.io.EOFException
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.Cache
import no.nav.pensjon.brev.skribenten.Cacheomraade
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.cached
import no.nav.pensjon.brev.skribenten.serialize.BrevkodeJacksonModule
import no.nav.pensjon.brev.skribenten.serialize.LetterMarkupJacksonModule
import no.nav.pensjon.brev.skribenten.serialize.SakstypeModule
import no.nav.pensjon.brev.skribenten.serialize.TemplateModelSpecificationJacksonModule
import no.nav.pensjon.brevbaker.api.model.*
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.seconds

class BrevbakerServiceException(msg: String) : ServiceException(msg)

interface BrevbakerService {
    suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart): TemplateModelSpecification?
    suspend fun renderMarkup(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
    ): LetterMarkupWithDataUsage
    suspend fun renderPdf(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
        redigertBrev: LetterMarkup,
        alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>
    ): LetterResponse
    suspend fun getTemplates(): List<TemplateDescription.Redigerbar>?
    suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart): TemplateDescription.Redigerbar?
    suspend fun getAlltidValgbareVedlegg(brevId: Long): Set<AlltidValgbartVedleggKode>
}

class BrevbakerServiceHttp(config: Config, authService: AuthService, val cache: Cache) : BrevbakerService, ServiceStatus {
    private val logger = LoggerFactory.getLogger(BrevredigeringService::class.java)!!

    private val brevbakerUrl = config.getString("url")
    private val scope = config.getString("scope")
    private val client = HttpClient(CIO) {
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
                registerModule(BrevkodeJacksonModule)
                registerModule(SakstypeModule)
                registerModule(TemplateModelSpecificationJacksonModule)
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
        felles: Felles,
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
                response.bodyAsText().takeIf { it.isNotBlank() }
                    ?: "Ukjent feil oppstod ved generering av markup for brevkode: $brevkode"
            )
        }
    }

    override suspend fun renderPdf(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
        redigertBrev: LetterMarkup,
        alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>,
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

    override suspend fun getAlltidValgbareVedlegg(brevId: Long): Set<AlltidValgbartVedleggKode> =
        cache.cached(Cacheomraade.ALLTID_VALGBARE_VEDLEGG, brevId) {
            val response = client.get("/letter/redigerbar/alltidValgbareVedlegg")

            if (response.status.isSuccess()) {
                response.body()
            } else {
                throw BrevbakerServiceException(
                    response.bodyAsText().takeIf { it.isNotBlank() }
                        ?: "Ukjent feil oppstod ved henting av alltid valgbare vedlegg for brev $brevId"
                )
            }
        }

    override suspend fun ping() = ping("Brevbaker") { client.get("/ping_authorized") }
}

