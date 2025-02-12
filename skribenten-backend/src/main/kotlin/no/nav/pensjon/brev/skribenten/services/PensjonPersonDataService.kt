package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.auth.AzureADService

@JsonIgnoreProperties(ignoreUnknown = true)
data class KontaktAdresseResponseDto(
    val adresseString: String,
    val adresselinjer: List<String>,
    val type: Adressetype,
) {
    @Suppress("unused")
    enum class Adressetype {
        MATRIKKELADRESSE,
        POSTADRESSE_I_FRITT_FORMAT,
        POSTBOKSADRESSE,
        REGOPPSLAG_ADRESSE,
        UKJENT_BOSTED,
        UTENLANDSK_ADRESSE,
        UTENLANDSK_ADRESSE_I_FRITT_FORMAT,
        VEGADRESSE,
        VERGE_PERSON_POSTADRESSE,
        VERGE_SAMHANDLER_POSTADRESSE,
    }
}

class PensjonPersonDataService(config: Config, authService: AzureADService, clientEngine: HttpClientEngine = CIO.create()) : ServiceStatus {
    private val pensjonPersondataURL = config.getString("url")
    private val scope = config.getString("scope")
    private val client =
        HttpClient(clientEngine) {
            defaultRequest {
                url(pensjonPersondataURL)
            }
            install(ContentNegotiation) {
                jackson {
                    registerModule(JavaTimeModule())
                }
            }
            callIdAndOnBehalfOfClient(scope, authService)
        }

    suspend fun hentKontaktadresse(pid: String): ServiceResult<KontaktAdresseResponseDto?> =
        client.get("/api/adresse/kontaktadresse") {
            parameter("checkForVerge", true)
            headers {
                header("pid", pid)
            }
        }.toServiceResult<KontaktAdresseResponseDto?> {
            when (it.status) {
                HttpStatusCode.NotFound -> ServiceResult.Ok(null)
                else -> ServiceResult.Error(it.bodyAsText(), it.status)
            }
        }

    override val name = "Pensjon PersonData"

    override suspend fun ping(): ServiceResult<Boolean> =
        client.get("/actuator/health/liveness")
            .toServiceResult<String>()
            .map { true }
}
