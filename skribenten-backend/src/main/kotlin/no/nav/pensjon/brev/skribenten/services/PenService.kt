package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.routes.OrderLetterRequest
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

    data class PenError(val feilmelding: String)

    data class PenPersonDto(
        val fodselsdato: LocalDate,
        val fnr: String,
    )
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


    private suspend fun fetchSak(call: ApplicationCall, sakId: String): ServiceResult<Sak, PenError> =
        client.get(call, "brev/skribenten/sak/$sakId").toServiceResult<Sak, PenError>()

    suspend fun hentSak(call: ApplicationCall, sakId: String): ServiceResult<SakSelection, PenError> =
        fetchSak(call, sakId).map {
            SakSelection(
                sakId = it.sakId,
                foedselsnr = it.foedselsnr,
                foedselsdato = it.foedselsdato,
                sakType = it.sakType,
            )
        }

    data class BestilDoksysBrevRequest(
        val sakId: Long,
        val brevkode: String,
        val mottaker: String?,
        val journalfoerendeEnhet: String?,
        val sensitivePersonopplysninger: Boolean?,
        val sprakKode: SpraakKode?,
        val vedtakId: Long?,
    )

    suspend fun bestillDoksysBrev(
        call: ApplicationCall,
        request: OrderLetterRequest,
    ): ServiceResult<BestillDoksysBrevResponse, BestillDoksysBrevResponse> =
        client.post(call, "brev/skribenten/doksys/sak/${request.sakId}") {
            setBody(
                BestilDoksysBrevRequest(
                    sakId = request.sakId,
                    brevkode = request.brevkode,
                    mottaker = null, // TODO
                    journalfoerendeEnhet = request.enhetsId,
                    sensitivePersonopplysninger = false, // TODO valg fra saksbehandler
                    sprakKode = request.spraak,
                    vedtakId = 42806043, //TODO fyll inn fra query param
                )
            )
            contentType(ContentType.Application.Json)
        }.toServiceResult<BestillDoksysBrevResponse, BestillDoksysBrevResponse>()
    data class Avtaleland(val navn: String, val kode: String)

    data class BestillDoksysBrevResponse(val journalpostId: String?, val error: Error? = null) {
        companion object {
            fun ok(journalpostId: String) =
                BestillDoksysBrevResponse(journalpostId)

            fun error(tekniskgrunn: String?, type: Error.ErrorType) =
                BestillDoksysBrevResponse(null, Error(tekniskgrunn, type))
        }

        data class Error(val tekniskgrunn: String?, val type: ErrorType) {
            enum class ErrorType {
                ADDRESS_NOT_FOUND,
                UNAUTHORIZED,
                PERSON_NOT_FOUND,
                UNEXPECTED_DOKSYS_ERROR,
                INTERNAL_SERVICE_CALL_FAILIURE,
                TPS_CALL_FAILIURE,
            }
        }
    }

    suspend fun hentAvtaleland(call: ApplicationCall): ServiceResult<List<Avtaleland>, String> =
        client.get(call, "brev/skribenten/avtaleland").toServiceResult<List<Avtaleland>, String>()
}

