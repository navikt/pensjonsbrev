package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
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

    suspend fun hentKontaktadresse(call: ApplicationCall, ident: String) =
        client.post(call, "brev/skribenten/hentKontaktadresse"){
            setBody(ident)
        } .toServiceResult<HentKontaktadresseResponse, String>()

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

    data class HentKontaktadresseResponse(val kontaktadresse: Kontaktadresse?, val error: Error? = null) {
        companion object {
            fun ok(kontaktadresse: Kontaktadresse) = HentKontaktadresseResponse(kontaktadresse)

            fun error(tekniskgrunn: String?, type: Error.ErrorType) =
                HentKontaktadresseResponse(null, Error(tekniskgrunn, type))
        }

        data class Error(val tekniskgrunn: String?, val type: ErrorType) {
            enum class ErrorType {
                PERSON_NOT_FOUND,
                ADDRESS_NOT_FOUND,
                ADDRESS_INCOMPLETE,
                GENERIC,
                TPS_CALL_FAILIURE,
            }
        }

        data class Kontaktadresse(
            val adresselinje1: String,
            val adresselinje2: String? = null,
            val adresselinje3: String? = null,
            val poststed: String? = null,
            val postnummer: String? = null,
            val land: String? = null,
            val landkode: String? = null
        )
    }

}

