package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.Cache
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerResponseDto
import org.slf4j.LoggerFactory

class SamhandlerService(configSamhandlerProxy: Config, authService: AzureADService): ServiceStatus {
    private val samhandlerProxyUrl = configSamhandlerProxy.getString("url")
    private val samhandlerProxyScope = configSamhandlerProxy.getString("scope")

    private val samhandlerProxyClient =
        AzureADOnBehalfOfAuthorizedHttpClient(samhandlerProxyScope, authService) {
            defaultRequest {
                url(samhandlerProxyUrl)
            }
            install(ContentNegotiation) {
                jackson {
                    disable(FAIL_ON_UNKNOWN_PROPERTIES)
                }
            }
        }

    private val logger = LoggerFactory.getLogger(SamhandlerService::class.java)

    suspend fun finnSamhandler(requestDto: FinnSamhandlerRequestDto): FinnSamhandlerResponseDto =
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

    suspend fun hentSamhandler(idTSSEkstern: String): HentSamhandlerResponseDto =
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

    private val samhandlerNavnCache = Cache<String, String>()
    suspend fun hentSamhandlerNavn(idTSSEkstern: String): String? = samhandlerNavnCache.cached(idTSSEkstern) {
        hentSamhandler(idTSSEkstern).success?.navn
    }

    override val name = "SamhandlerService"
    override suspend fun ping(): ServiceResult<Boolean> =
        samhandlerProxyClient.get("/api/samhandler/ping").toServiceResult<String>().map { true }

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
        val avdelingsnr: String?
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