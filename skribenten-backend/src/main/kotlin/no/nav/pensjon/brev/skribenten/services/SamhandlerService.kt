package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.Cache
import no.nav.pensjon.brev.skribenten.Cacheomraade
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.cached
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.HentSamhandlerAdresseResponseDto
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.HentSamhandlerAdresseResponseDto.FailureType.GENERISK
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.HentSamhandlerResponseDto
import org.slf4j.LoggerFactory
import kotlin.collections.flatMap

interface SamhandlerService {
    suspend fun finnSamhandler(requestDto: FinnSamhandlerRequestDto): FinnSamhandlerResponseDto
    suspend fun hentSamhandler(idTSSEkstern: String): HentSamhandlerResponseDto
    suspend fun hentSamhandlerNavn(idTSSEkstern: String): String?
    suspend fun hentSamhandlerAdresse(idTSSEkstern: String): HentSamhandlerAdresseResponseDto
}

class SamhandlerServiceHttp(configSamhandlerProxy: Config, authService: AuthService, private val cache: Cache) : SamhandlerService, ServiceStatus {
    private val samhandlerProxyUrl = configSamhandlerProxy.getString("url")
    private val samhandlerProxyScope = configSamhandlerProxy.getString("scope")

    private val samhandlerProxyClient = HttpClient(CIO) {
        defaultRequest {
            url(samhandlerProxyUrl)
        }
        install(ContentNegotiation) {
            jackson {
                disable(FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
        callIdAndOnBehalfOfClient(samhandlerProxyScope, authService)
    }

    private val logger = LoggerFactory.getLogger(SamhandlerServiceHttp::class.java)

    override suspend fun finnSamhandler(requestDto: FinnSamhandlerRequestDto): FinnSamhandlerResponseDto =
        samhandlerProxyClient.post("/api/samhandler/finnSamhandler") {
            contentType(Json)
            accept(Json)
            setBody(lagRequest(requestDto))
        }.toServiceResult<FinnSamhandlerResponse>()
            .map { it.toFinnSamhandlerResponseDto() }
            .catch { message, status ->
                logger.error("Feil ved samhandler søk. Status: $status Melding: $message")
                FinnSamhandlerResponseDto("Feil ved henting av samhandler")
            }

    override suspend fun hentSamhandler(idTSSEkstern: String): HentSamhandlerResponseDto =
        samhandlerProxyClient.get("/api/samhandler/hentSamhandlerEnkel/") {
            url {
                appendPathSegments(idTSSEkstern)
            }
            contentType(Json)
            accept(Json)
        }.toServiceResult<SamhandlerEnkel>()
            .map { it.toHentSamhandlerResponseDto() }
            .catch { message, status ->
                logger.error("Feil ved henting av samhandler fra tjenestebuss-integrasjon. Status: $status Melding: $message")
                HentSamhandlerResponseDto(null, HentSamhandlerResponseDto.FailureType.GENERISK)
            }

    override suspend fun hentSamhandlerNavn(idTSSEkstern: String): String? = cache.cached(Cacheomraade.SAMHANDLER, idTSSEkstern) {
        hentSamhandler(idTSSEkstern).success?.navn
    }

    override suspend fun hentSamhandlerAdresse(idTSSEkstern: String) = cache.cached(Cacheomraade.SAMHANDLER_ADRESSE, idTSSEkstern) {
        samhandlerProxyClient.get("/api/samhandler/hentSamhandlerPostadresse/") {
            url {
                appendPathSegments(idTSSEkstern)
            }
            contentType(Json)
            accept(Json)
        }.let { response ->
            response
                .takeIf { it.status.isSuccess() }
                ?.body<HentSamhandlerAdresseResponseDto>()
                ?: HentSamhandlerAdresseResponseDto(null, GENERISK).also {
                    logger.error("Feil ved henting av samhandler adresse fra tjenestebuss-integrasjon. Status: ${response.status}, melding: ${response.bodyAsText()}")
                }
        }
    }

    override suspend fun ping() =
        ping("SamhandlerService") { samhandlerProxyClient.get("/api/samhandler/ping") }

    private fun lagRequest(requestDto: FinnSamhandlerRequestDto) =
        when (requestDto) {
            is FinnSamhandlerRequestDto.DirekteOppslag -> {
                Soek(
                    navn = null,
                    idType = requestDto.identtype,
                    offentligId = requestDto.id,
                    samhandlerType = requestDto.samhandlerType.name,
                )
            }

            is FinnSamhandlerRequestDto.Organisasjonsnavn -> {
                Soek(
                    navn = requestDto.navn,
                    idType = null,
                    offentligId = null,
                    samhandlerType = requestDto.samhandlerType.name,
                )
            }

            is FinnSamhandlerRequestDto.Personnavn -> {
                Soek(
                    "${requestDto.etternavn} ${requestDto.fornavn}",
                    null,
                    null,
                    requestDto.samhandlerType.name,
                )
            }
        }

    data class Soek(
        val navn: String?,
        val idType: String?,
        val offentligId: String?,
        val samhandlerType: String?,
    )

    data class FinnSamhandlerResponse(
        val samhandlerList: List<Samhandler>,
    )

    private fun FinnSamhandlerResponse.toFinnSamhandlerResponseDto() =
        FinnSamhandlerResponseDto(
            samhandlere = samhandlerList.flatMap { samhandler ->
                samhandler.avdelinger.map { avdeling ->
                    FinnSamhandlerResponseDto.Samhandler(
                        navn = avdeling.avdelingNavn.takeIf { !it.isNullOrBlank() } ?: samhandler.navn,
                        samhandlerType = samhandler.samhandlerType,
                        offentligId = samhandler.offentligId,
                        idType = samhandler.idType,
                        idTSSEkstern = avdeling.idTSSEkstern
                    )
                }
            }
        )

    data class SamhandlerEnkel(
        val navn: String,
        val samhandlerType: String,
        val offentligId: String,
        val idType: String,
    )

    data class Samhandler(
        val navn: String,
        val samhandlerType: String,
        val offentligId: String,
        val idType: String,
        val avdelinger: List<Avdeling>,
    )

    data class Avdeling(
        val idTSSEkstern: String,
        val avdelingNavn: String?,
        val avdelingType: String?,
        val avdelingsnr: String?,
    )

    private fun SamhandlerEnkel.toHentSamhandlerResponseDto() =
        HentSamhandlerResponseDto(
            success = HentSamhandlerResponseDto.Success(
                navn = navn,
                samhandlerType = samhandlerType,
                offentligId = offentligId,
                idType = idType,
            ),
            failure = null
        )
}