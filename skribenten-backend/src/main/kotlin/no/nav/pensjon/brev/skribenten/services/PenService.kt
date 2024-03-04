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

    data class Sak(
        val sakId: Long,
        val foedselsnr: String,
        val foedselsdato: LocalDate,
        val sakType: SakType,
        val enhetId: String,
    )

    enum class SakType { AFP, AFP_PRIVAT, ALDER, BARNEP, FAM_PL, GAM_YRK, GENRL, GJENLEV, GRBL, KRIGSP, OMSORG, UFOREP, }
    data class SakSelection(
        val sakId: Long,
        val foedselsnr: String,
        val foedselsdato: LocalDate,
        val sakType: SakType,
        val enhetId: String?,
    )

    private suspend fun <R> handlePenErrorResponse(response: HttpResponse): ServiceResult<R> =
        if (response.status == HttpStatusCode.InternalServerError) {
            logger.error("En feil oppstod ved henting av sak fra Pesys: ${response.bodyAsText()}")
            ServiceResult.Error("Ukjent feil oppstod ved henting av sak fra Pesys", HttpStatusCode.InternalServerError)
        } else {
            ServiceResult.Error(response.bodyAsText(), response.status)
        }

    private suspend fun fetchSak(call: ApplicationCall, sakId: String): ServiceResult<Sak> =
        client.get(call, "brev/skribenten/sak/$sakId").toServiceResult(::handlePenErrorResponse)

    suspend fun hentSak(call: ApplicationCall, sakId: String): ServiceResult<SakSelection> =
        fetchSak(call, sakId).map {
            SakSelection(
                sakId = it.sakId,
                foedselsnr = it.foedselsnr,
                foedselsdato = it.foedselsdato,
                sakType = it.sakType,
                enhetId = it.enhetId
            )
        }

    data class BestilDoksysBrevRequest(
        val sakId: Long,
        val brevkode: String,
        val journalfoerendeEnhet: String?,
        val sprakKode: SpraakKode?,
        val vedtaksId: Long?,
    )

    suspend fun bestillDoksysBrev(call: ApplicationCall, request: BestillDoksysBrevRequest, enhetsId: String, sakId: Long): ServiceResult<BestillDoksysBrevResponse> =
        client.post(call, "brev/skribenten/doksys/sak/$sakId") {
            setBody(
                BestilDoksysBrevRequest(
                    sakId = sakId,
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

    suspend fun hentAvtaleland(call: ApplicationCall): ServiceResult<List<Avtaleland>> =
        client.get(call, "brev/skribenten/avtaleland").toServiceResult(::handlePenErrorResponse)

    override val name = "PEN"
    override suspend fun ping(call: ApplicationCall): ServiceResult<Boolean> =
        client.get(call, "/pen/actuator/health/readiness")
            .toServiceResult<String>()
            .map { true }

}

