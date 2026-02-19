package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Pid
import org.slf4j.LoggerFactory

@JsonIgnoreProperties(ignoreUnknown = true)
data class KontaktAdresseResponseDto(
    val adresseString: String,
    val adresselinjer: List<String>,
    val type: Adressetype,
){
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
class PensjonPersonDataServiceException(message: String) : ServiceException(message)

class PensjonPersonDataService(config: Config, authService: AuthService, clientEngine: HttpClientEngine = CIO.create()): ServiceStatus {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val pensjonPersondataURL = config.getString("url")
    private val scope = config.getString("scope")
    private val client = HttpClient(clientEngine) {
        defaultRequest {
            url(pensjonPersondataURL)
        }
        installRetry(logger)
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
            }
        }
        callIdAndOnBehalfOfClient(scope, authService)
    }

    suspend fun hentKontaktadresse(pid: Pid): KontaktAdresseResponseDto? {
        val response = client.get("/api/adresse/kontaktadresse") {
            parameter("checkForVerge", true)
            headers {
                header("pid", pid.value)
            }
        }

        return when {
            response.status.isSuccess() -> response.body()
            response.status == HttpStatusCode.NotFound -> null
            else -> throw PensjonPersonDataServiceException("Feil ved henting av kontaktadresse fra Pensjon Persondata: ${response.status}, ${response.bodyAsText()}")
        }
    }

    override suspend fun ping() =
        ping("Pensjon Persondata") { client.get("/actuator/health/liveness") }
}