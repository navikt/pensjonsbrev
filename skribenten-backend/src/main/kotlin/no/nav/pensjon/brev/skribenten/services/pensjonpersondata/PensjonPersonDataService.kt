package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService



data class AdresseResponse(
    val type : Adressetype,
    val adresseString : String,
    val adresselinjer : List<String>,
) {
    enum class Adressetype{
        VEGADRESSE,
        POSTBOKSADRESSE,
        MATRIKKELADRESSE,
        POSTADRESSE_I_FRITT_FORMAT,
        UTENLANDSK_ADRESSE,
        UTENLANDSK_ADRESSE_I_FRITT_FORMAT,
        UKJENT_BOSTED,
        REGOPPSLAG_ADRESSE,
        SAMHANDLER_POSTADRESSE
    }
}
class PensjonPersonDataService(config: Config, authService: AzureADService) {

    private val url = config.getString("url")
    private val scope = config.getString("scope")
    private val client = AzureADOnBehalfOfAuthorizedHttpClient(scope, authService) {
        defaultRequest {
            url(this@PensjonPersonDataService.url)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
            }
        }
    }


    suspend fun hentAdresse(call: ApplicationCall, pid: String) =
        client.get(call, "/api/adresse/kontaktadresse") {
            parameter("checkForVerge", false)
            headers {
                header("pid", pid)
            }
        }.toServiceResult<AdresseResponse, String >()
}