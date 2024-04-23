package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillDoksysBrevRequest
import no.nav.pensjon.brev.skribenten.services.PdlService.Behandlingsnummer
import no.nav.pensjon.brev.skribenten.services.PdlService.Behandlingsnummer.B222
import no.nav.pensjon.brev.skribenten.services.PdlService.Behandlingsnummer.B255
import no.nav.pensjon.brev.skribenten.services.PdlService.Behandlingsnummer.B280
import no.nav.pensjon.brev.skribenten.services.PdlService.Behandlingsnummer.B359
import org.slf4j.LoggerFactory
import java.time.LocalDate

private val logger = LoggerFactory.getLogger(PenService::class.java)

class PenService(config: Config, authService: AzureADService) : ServiceStatus {
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

    data class SakResponseDto(
        val saksId: Long,
        val foedselsnr: String,
        val foedselsdato: LocalDate,
        val sakType: SakType,
        val enhetId: String?,
    )

    enum class SakType(val behandlingsnummer: Behandlingsnummer?) {
        AFP(null),
        AFP_PRIVAT(null),
        ALDER(B280),
        BARNEP(B359),
        FAM_PL(null),
        GAM_YRK(null),
        GENRL(null),
        GJENLEV(B222),
        GRBL(null),
        KRIGSP(null),
        OMSORG(null),
        UFOREP(B255);
    }
    data class SakSelection(
        val saksId: Long,
        val foedselsnr: String,
        val foedselsdato: LocalDate,
        val sakType: SakType,
        val enhetId: String,
    )

    private suspend fun <R> handlePenErrorResponse(response: HttpResponse): ServiceResult<R> =
        if (response.status == HttpStatusCode.InternalServerError) {
            logger.error("En feil oppstod ved henting av sak fra Pesys: ${response.bodyAsText()}")
            ServiceResult.Error("Ukjent feil oppstod ved henting av sak fra Pesys", HttpStatusCode.InternalServerError)
        } else {
            ServiceResult.Error(response.bodyAsText(), response.status)
        }

    private suspend fun fetchSak(call: ApplicationCall, saksId: String): ServiceResult<SakResponseDto> =
        client.get(call, "brev/skribenten/sak/$saksId").toServiceResult(::handlePenErrorResponse)

    suspend fun hentSak(call: ApplicationCall, saksId: String): ServiceResult<SakSelection> =
        when (val sak = fetchSak(call, saksId)) {
            is ServiceResult.Error -> ServiceResult.Error(sak.error, sak.statusCode)
            is ServiceResult.Ok ->
                if (sak.result.enhetId.isNullOrBlank()) {
                    ServiceResult.Error("Sak er ikke tilordnet enhet", HttpStatusCode.BadRequest)
                } else {
                    ServiceResult.Ok(
                        SakSelection(
                            saksId = sak.result.saksId,
                            foedselsnr = sak.result.foedselsnr,
                            foedselsdato = sak.result.foedselsdato,
                            sakType = sak.result.sakType,
                            enhetId = sak.result.enhetId
                        )
                    )
                }
        }

    data class BestilDoksysBrevRequest(
        val saksId: Long,
        val brevkode: String,
        val journalfoerendeEnhet: String?,
        val sprakKode: SpraakKode?,
        val vedtaksId: Long?,
    )

    suspend fun bestillDoksysBrev(
        call: ApplicationCall,
        request: BestillDoksysBrevRequest,
        enhetsId: String,
        saksId: Long
    ): ServiceResult<BestillDoksysBrevResponse> =
        client.post(call, "brev/skribenten/doksys/sak/$saksId") {
            setBody(
                BestilDoksysBrevRequest(
                    saksId = saksId,
                    brevkode = request.brevkode,
                    journalfoerendeEnhet = enhetsId,
                    sprakKode = request.spraak,
                    vedtaksId = request.vedtaksId,
                )
            )
            contentType(ContentType.Application.Json)
        }.toServiceResult(::handlePenErrorResponse)

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

    data class PenSakTilgang(
        val saksId: String,
        val idForEnheterMedTilgang: List<String>,
    )

    suspend fun hentAvtaleland(call: ApplicationCall): ServiceResult<List<Avtaleland>> =
        client.get(call, "brev/skribenten/avtaleland").toServiceResult(::handlePenErrorResponse)

    suspend fun hentSaktilganger(call: ApplicationCall, saksId: String): ServiceResult<PenSakTilgang> =
        client.get(call, "sak/gyldigetilganger?saksid=${saksId}").toServiceResult(::handlePenErrorResponse)

    override val name = "PEN"
    override suspend fun ping(call: ApplicationCall): ServiceResult<Boolean> =
        client.get(call, "/pen/actuator/health/readiness")
            .toServiceResult<String>()
            .map { true }

    suspend fun hentIsKravPaaGammeltRegelverk(call: ApplicationCall, vedtaksId: String): ServiceResult<Boolean> =
        client.get(call, "brev/skribenten/vedtak/$vedtaksId/isKravPaaGammeltRegelverk").toServiceResult<Boolean>(::handlePenErrorResponse)
}

