package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService

@JsonIgnoreProperties(ignoreUnknown = true)
data class KontaktAdresseResponseDto(
    val adresseString: String,
    val adresselinjer: List<String>,
)

class PensjonPersonDataService(config: Config, authService: AzureADService) {

    private val pensjonPersondataURL = config.getString("url")
    private val scope = config.getString("scope")
    private val client = AzureADOnBehalfOfAuthorizedHttpClient(scope, authService) {
        defaultRequest {
            url(pensjonPersondataURL)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
            }
        }
    }

    suspend fun hentKontaktadresse(call: ApplicationCall, pid: String): ServiceResult<KontaktAdresseResponseDto, String> =
        client.get(call, "/api/adresse/kontaktadresse?checkForVerge=true") {
            parameter("checkForVerge", false)
            headers {
                header("pid", pid)
            }
        }.toServiceResult<KontaktAdresseResponseDto, String>()

}