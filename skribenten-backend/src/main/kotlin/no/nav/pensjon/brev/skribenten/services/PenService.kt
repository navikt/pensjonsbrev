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
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.OrderLetterRequest
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
    ): ServiceResult2<BestillDoksysBrevResponse> =
        client.post(call, "brev/skribenten/doksys/sak/${request.sakId}") {
            setBody(
                BestilDoksysBrevRequest(
                    sakId = request.sakId,
                    brevkode = request.brevkode,
                    mottaker = null, // TODO slett feltet fra pesys og sett mottaker der.
                    journalfoerendeEnhet = request.enhetsId,
                    sensitivePersonopplysninger = false, // TODO Undersøk om feltet har noen påvirkning på doksys, evt slett fra skribentencontroller i pesys
                    sprakKode = request.spraak,
                    vedtakId = null, //TODO set from request
                )
            )
            contentType(ContentType.Application.Json)
        }.toServiceResult2<BestillDoksysBrevResponse>()

    data class Avtaleland(val navn: String, val kode: String)

    data class BestillDoksysBrevResponse(val journalpostId: String?, val failure: FailureType? = null) {
        enum class FailureType {
            ADDRESS_NOT_FOUND,
            UNAUTHORIZED,
            PERSON_NOT_FOUND,
            UNEXPECTED_DOKSYS_ERROR,
            INTERNAL_SERVICE_CALL_FAILIURE,
            TPS_CALL_FAILIURE,
        }
    }

    suspend fun hentAvtaleland(call: ApplicationCall): ServiceResult<List<Avtaleland>, String> =
        client.get(call, "brev/skribenten/avtaleland").toServiceResult<List<Avtaleland>, String>()
}

