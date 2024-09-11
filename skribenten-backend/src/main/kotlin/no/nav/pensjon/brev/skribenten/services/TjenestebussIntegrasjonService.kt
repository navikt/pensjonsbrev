package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.*
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerAdresseResponseDto.FailureType.GENERISK
import org.slf4j.LoggerFactory

class TjenestebussIntegrasjonService(config: Config, authService: AzureADService) : ServiceStatus {

    private val tjenestebussIntegrasjonUrl = config.getString("url")
    private val tjenestebussIntegrasjonScope = config.getString("scope")
    private val logger = LoggerFactory.getLogger(TjenestebussIntegrasjonService::class.java)

    private val tjenestebussIntegrasjonClient =
        AzureADOnBehalfOfAuthorizedHttpClient(tjenestebussIntegrasjonScope, authService) {
            defaultRequest {
                url(tjenestebussIntegrasjonUrl)
            }
            install(ContentNegotiation) {
                jackson()
            }
        }

    suspend fun hentSamhandlerAdresse(
        call: ApplicationCall,
        idTSSEkstern: String,
    ): HentSamhandlerAdresseResponseDto =
        tjenestebussIntegrasjonClient.post(call, "/hentSamhandlerAdresse") {
            contentType(Json)
            accept(Json)
            setBody(HentSamhandlerAdresseRequestDto(idTSSEkstern))
        }.toServiceResult<HentSamhandlerAdresseResponseDto>()
            .catch { message, status ->
                logger.error("Feil ved henting av samhandler adresse fra tjenestebuss-integrasjon. Status: $status Melding: $message")
                HentSamhandlerAdresseResponseDto(null, GENERISK)
            }

    override val name = "Tjenestebuss-integrasjon"
    override suspend fun ping(call: ApplicationCall): ServiceResult<Boolean> =
        tjenestebussIntegrasjonClient.get(call, "/isReady").toServiceResult<String>().map { true }

    suspend fun status(call: ApplicationCall): ServiceResult<TjenestebussStatus> =
        tjenestebussIntegrasjonClient.get(call, "/status").toServiceResult()

}
