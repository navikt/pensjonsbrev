package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import java.time.LocalDate

class PenService(config: Config, authService: AzureADService) {
    private val penUrl = config.getString("url")
    private val penScope = config.getString("scope")

    private val client = AzureADOnBehalfOfAuthorizedHttpClient(penScope, authService) {
        defaultRequest {
            url(penUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
    }

    suspend fun hentAvtaleland(call: ApplicationCall): ServiceResult<List<Avtaleland>, String> =
        client.get(call, "brev/skribenten/avtaleland").toServiceResult<List<Avtaleland>, String>()

    suspend fun hentSak(call: ApplicationCall, sakId: String): ServiceResult<SakSelection, PenError> =
        fetchSak(call, sakId).map {
            SakSelection(
                sakId = it.sakId,
                foedselsnr = it.foedselsnr,
                foedselsdato = it.foedselsdato,
                sakType = it.sakType,
            )
        }

    private suspend fun fetchSak(call: ApplicationCall, sakId: String): ServiceResult<Sak, PenError> =
        client.get(call, "brev/skribenten/sak/$sakId").toServiceResult<Sak, PenError>()

    data class PenError(val feilmelding: String)

    data class Sak(
        val sakId: Long,
        val foedselsnr: String,
        val foedselsdato: LocalDate,
        val sakType: SakType,
    )

    enum class SakType { AFP, AFP_PRIVAT, ALDER, BARNEP, FAM_PL, GAM_YRK, GENRL, GJENLEV, GRBL, KRIGSP, OMSORG, UFOREP, }
    data class SakSelection(
        val sakId: Long,
        val foedselsnr: String,
        val foedselsdato: LocalDate,
        val sakType: SakType,
    )

    data class Avtaleland(val navn: String, val kode: String)

}

