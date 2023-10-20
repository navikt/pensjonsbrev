package no.nav.pensjon.brev.skribenten.services
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService

class KrrService(config: Config, authService: AzureADService) {
    private val krrUrl = config.getString("url")
    private val krrScope = config.getString("scope")

    private val client = AzureADOnBehalfOfAuthorizedHttpClient(krrScope, authService) {
        defaultRequest {
            url(krrUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
            }
        }
    }

    data class KontaktinfoResponse(
        val personident: Pid,
        val aktiv: Boolean,
        val kanVarsles: Boolean,
        val reservert: Boolean,
        val epostadresse: String? = null,
        val mobiltelefonnummer: String? = null,
        val sikkerDigitalPostkasse: SikkerDigitalPostkasse? = null,
        val spraak: SprakCode? = null
    ) {
        data class Pid(val pid: String)
    }

    suspend fun getPreferredLocale(call: ApplicationCall, pid: String) {
        client.get(call, "/rest/v1/person"){
            headers{
                accept(ContentType.Application.Json)
                header("Nav-Personident", pid)
            }

        }
    }



}