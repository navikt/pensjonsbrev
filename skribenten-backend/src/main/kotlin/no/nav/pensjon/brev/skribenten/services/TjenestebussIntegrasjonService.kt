package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerAdresseRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerAdresseResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerAdresseResponseDto.FailureType.GENERISK
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.TjenestebussStatus
import org.slf4j.LoggerFactory

class TjenestebussIntegrasjonService(config: Config, authService: AzureADService) : ServiceStatus {
    private val tjenestebussIntegrasjonUrl = config.getString("url")
    private val tjenestebussIntegrasjonScope = config.getString("scope")
    private val logger = LoggerFactory.getLogger(TjenestebussIntegrasjonService::class.java)

    private val tjenestebussIntegrasjonClient =
        HttpClient(CIO) {
            defaultRequest {
                url(tjenestebussIntegrasjonUrl)
            }
            install(ContentNegotiation) {
                jackson()
            }
            callIdAndOnBehalfOfClient(tjenestebussIntegrasjonScope, authService)
        }

    suspend fun hentSamhandlerAdresse(idTSSEkstern: String): HentSamhandlerAdresseResponseDto =
        tjenestebussIntegrasjonClient.post("/hentSamhandlerAdresse") {
            contentType(Json)
            accept(Json)
            setBody(HentSamhandlerAdresseRequestDto(idTSSEkstern))
        }.toServiceResult<HentSamhandlerAdresseResponseDto>()
            .catch { message, status ->
                logger.error("Feil ved henting av samhandler adresse fra tjenestebuss-integrasjon. Status: $status Melding: $message")
                HentSamhandlerAdresseResponseDto(null, GENERISK)
            }

    override val name = "Tjenestebuss-integrasjon"

    override suspend fun ping(): ServiceResult<Boolean> =
        tjenestebussIntegrasjonClient.get("/isReady").toServiceResult<String>().map { true }

    suspend fun status(): ServiceResult<TjenestebussStatus> =
        tjenestebussIntegrasjonClient.get("/status").toServiceResult()
}
