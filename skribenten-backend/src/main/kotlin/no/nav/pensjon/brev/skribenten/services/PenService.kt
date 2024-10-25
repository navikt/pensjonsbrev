package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.Pen.BestillExstreamBrevResponse
import no.nav.pensjon.brev.skribenten.model.Pen.SendRedigerbartBrevRequest
import no.nav.pensjon.brevbaker.api.model.Felles
import org.slf4j.LoggerFactory
import java.time.LocalDate

private val logger = LoggerFactory.getLogger(PenService::class.java)

class PenService(config: Config, authService: AzureADService) : ServiceStatus {
    private val penUrl = config.getString("url")
    private val penScope = config.getString("scope")

    private val client = HttpClient(CIO) {
        defaultRequest {
            url(penUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
        callIdAndOnBehalfOfClient(penScope, authService)
    }

    private suspend fun <R> handlePenErrorResponse(response: HttpResponse): ServiceResult<R> =
        if (response.status == HttpStatusCode.InternalServerError) {
            logger.error("En feil oppstod i kall til PEN: ${response.bodyAsText()}")
            ServiceResult.Error("Ukjent feil oppstod i kall til PEN", HttpStatusCode.InternalServerError)
        } else {
            ServiceResult.Error(response.bodyAsText(), response.status)
        }

    private suspend fun fetchSak(saksId: String): ServiceResult<SakResponseDto> =
        client.get("brev/skribenten/sak/$saksId").toServiceResult(::handlePenErrorResponse)

    suspend fun hentSak(saksId: String): ServiceResult<Pen.SakSelection> =
        when (val sak = fetchSak(saksId)) {
            is ServiceResult.Error -> ServiceResult.Error(sak.error, sak.statusCode)
            is ServiceResult.Ok ->
                if (sak.result.enhetId.isNullOrBlank()) {
                    ServiceResult.Error("Sak er ikke tilordnet enhet", HttpStatusCode.BadRequest)
                } else {
                    ServiceResult.Ok(
                        Pen.SakSelection(
                            saksId = sak.result.saksId,
                            foedselsnr = sak.result.foedselsnr,
                            foedselsdato = sak.result.foedselsdato,
                            sakType = sak.result.sakType,
                            enhetId = sak.result.enhetId
                        )
                    )
                }
        }

    suspend fun bestillDoksysBrev(
        request: Api.BestillDoksysBrevRequest,
        enhetsId: String,
        saksId: Long
    ): ServiceResult<Pen.BestillDoksysBrevResponse> =
        client.post("brev/skribenten/doksys/sak/$saksId") {
            setBody(
                BestillDoksysBrevRequest(
                    saksId = saksId,
                    brevkode = request.brevkode,
                    journalfoerendeEnhet = enhetsId,
                    sprakKode = request.spraak,
                    vedtaksId = request.vedtaksId,
                )
            )
            contentType(ContentType.Application.Json)
        }.toServiceResult(::handlePenErrorResponse)

    suspend fun bestillExstreamBrev(
        bestillExstreamBrevRequest: Pen.BestillExstreamBrevRequest,
    ): ServiceResult<BestillExstreamBrevResponse> =
        client.post("brev/pjoark030/bestillbrev") {
            setBody(bestillExstreamBrevRequest)
            contentType(ContentType.Application.Json)
        }.toServiceResult {
            it.body<BestillExstreamBrevResponse.Error>().let { error ->
                ServiceResult.Error("${error.type}: ${error.message}", HttpStatusCode.InternalServerError)
            }
        }

    suspend fun redigerDoksysBrev(journalpostId: String, dokumentId: String): ServiceResult<Pen.RedigerDokumentResponse> =
        client.get("brev/dokument/metaforce/$journalpostId/$dokumentId")
            .toServiceResult(::handlePenErrorResponse)

    suspend fun redigerExstreamBrev(journalpostId: String): ServiceResult<Pen.RedigerDokumentResponse> =
        client.get("brev/dokument/exstream/$journalpostId")
            .toServiceResult(::handlePenErrorResponse)

    suspend fun hentAvtaleland(): ServiceResult<List<Pen.Avtaleland>> =
        client.get("brev/skribenten/avtaleland").toServiceResult(::handlePenErrorResponse)

    override val name = "PEN"
    override suspend fun ping(): ServiceResult<Boolean> =
        client.get("/pen/actuator/health/readiness")
            .toServiceResult<String>()
            .map { true }

    suspend fun hentIsKravPaaGammeltRegelverk(vedtaksId: String): ServiceResult<Boolean> =
        client.get("brev/skribenten/vedtak/$vedtaksId/isKravPaaGammeltRegelverk").toServiceResult<Boolean>(::handlePenErrorResponse)

    suspend fun hentPesysBrevdata(saksId: Long, brevkode: Brevkode.Redigerbar, avsenderEnhetsId: String?): ServiceResult<BrevdataResponse.Data> =
        client.get("brev/skribenten/sak/$saksId/brevdata/${brevkode.name}") {
            if (avsenderEnhetsId != null) {
                url {
                    parameters.append("enhetsId", avsenderEnhetsId)
                }
            }
        }.toServiceResult<BrevdataResponse>(::handlePenErrorResponse)
            .then {
                if (it.error != null) {
                    ServiceResult.Error(it.error, HttpStatusCode.InternalServerError)
                } else if (it.data != null) {
                    ServiceResult.Ok(it.data)
                } else {
                    ServiceResult.Error("Fikk hverken data eller feilmelding fra Pesys", HttpStatusCode.InternalServerError)
                }
            }

    suspend fun sendbrev(
        sendRedigerbartBrevRequest: SendRedigerbartBrevRequest,
        distribuer: Boolean,
    ): ServiceResult<Pen.BestillBrevResponse> =
        client.post("brev/skribenten/sendbrev") {
            setBody(sendRedigerbartBrevRequest)
            contentType(ContentType.Application.Json)
            url { parameters.append("distribuer", distribuer.toString()) }
        }.toServiceResult<Pen.BestillBrevResponse>(::handlePenErrorResponse)


    private data class BestillDoksysBrevRequest(
        val saksId: Long,
        val brevkode: String,
        val journalfoerendeEnhet: String?,
        val sprakKode: SpraakKode?,
        val vedtaksId: Long?,
    )

    private data class SakResponseDto(
        val saksId: Long,
        val foedselsnr: String,
        val foedselsdato: LocalDate,
        val sakType: Pen.SakType,
        val enhetId: String?,
    )
}

data class BrevdataResponse(val data: Data?, val error: String? = null) {
    data class Data(val felles: Felles, val brevdata: Api.GeneriskBrevdata)
}
