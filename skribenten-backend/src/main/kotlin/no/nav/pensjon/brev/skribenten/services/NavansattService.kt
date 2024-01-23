package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.jackson.*

class NavansattService(config: Config) {

    private val navansattUrl = config.getString("url")

    val client = HttpClient(CIO) {
        defaultRequest {
            url(navansattUrl)
        }
        install(ContentNegotiation) {
            jackson()
        }
    }

    suspend fun hentNavAnsattEnhetListe(ansattId: String): ServiceResult<List<NAVEnhet>, String> {
        return client.get("navansatt/{$ansattId}/enheter").toServiceResult<List<NAVEnhet>, String>()
    }

}


@JsonIgnoreProperties(ignoreUnknown = true)
data class NAVEnhet(
 val enhetNr: String,
 val navn: String,
)
