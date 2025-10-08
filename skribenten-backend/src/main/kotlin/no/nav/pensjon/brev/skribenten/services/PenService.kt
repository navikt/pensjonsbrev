package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.Pen.BestillExstreamBrevResponse
import no.nav.pensjon.brev.skribenten.model.Pen.SendRedigerbartBrevRequest
import no.nav.pensjon.brevbaker.api.model.Felles
import org.slf4j.LoggerFactory
import java.time.LocalDate
import kotlin.jvm.java

private val logger = LoggerFactory.getLogger(PenServiceHttp::class.java)

interface PenService {
    suspend fun hentSak(saksId: String): ServiceResult<Pen.SakSelection>
    suspend fun bestillDoksysBrev(
        request: Api.BestillDoksysBrevRequest,
        enhetsId: String,
        saksId: Long
    ): ServiceResult<Pen.BestillDoksysBrevResponse>
    suspend fun bestillExstreamBrev(
        bestillExstreamBrevRequest: Pen.BestillExstreamBrevRequest,
    ): ServiceResult<BestillExstreamBrevResponse>
    suspend fun redigerDoksysBrev(journalpostId: String, dokumentId: String): ServiceResult<Pen.RedigerDokumentResponse>
    suspend fun redigerExstreamBrev(journalpostId: String): ServiceResult<Pen.RedigerDokumentResponse>
    suspend fun hentAvtaleland(): ServiceResult<List<Pen.Avtaleland>>
    suspend fun hentIsKravPaaGammeltRegelverk(vedtaksId: String): ServiceResult<Boolean>
    suspend fun hentIsKravStoettetAvDatabygger(vedtaksId: String): ServiceResult<KravStoettetAvDatabyggerResult>
    suspend fun hentPesysBrevdata(saksId: Long, vedtaksId: Long?, brevkode: Brevkode.Redigerbart, avsenderEnhetsId: String?): ServiceResult<BrevdataResponse.Data>
    suspend fun sendbrev(
        sendRedigerbartBrevRequest: SendRedigerbartBrevRequest,
        distribuer: Boolean,
    ): ServiceResult<Pen.BestillBrevResponse>

    data class KravStoettetAvDatabyggerResult(
        val kravStoettet: Map<String, Boolean> = emptyMap()
    )
}

class PenServiceHttp(config: Config, authService: AuthService) : PenService, ServiceStatus {
    private val penUrl = config.getString("url")
    private val penScope = config.getString("scope")

    private val client = HttpClient(CIO) {
        defaultRequest {
            url(penUrl)
        }
        installRetry(logger, shouldNotRetry = { method,_ -> method != HttpMethod.Get } )
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

    override suspend fun hentSak(saksId: String): ServiceResult<Pen.SakSelection> =
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

    override suspend fun bestillDoksysBrev(
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

    override suspend fun bestillExstreamBrev(
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

    override suspend fun redigerDoksysBrev(journalpostId: String, dokumentId: String): ServiceResult<Pen.RedigerDokumentResponse> =
        client.get("brev/dokument/metaforce/$journalpostId/$dokumentId")
            .toServiceResult(::handlePenErrorResponse)

    override suspend fun redigerExstreamBrev(journalpostId: String): ServiceResult<Pen.RedigerDokumentResponse> =
        client.get("brev/dokument/exstream/$journalpostId")
            .toServiceResult(::handlePenErrorResponse)

    override suspend fun hentAvtaleland(): ServiceResult<List<Pen.Avtaleland>> =
        client.get("brev/skribenten/avtaleland").toServiceResult(::handlePenErrorResponse)

    override val name = "PEN"
    override suspend fun ping(): ServiceResult<Boolean> =
        client.get("/pen/actuator/health/readiness")
            .toServiceResult<String>()
            .map { true }

    override suspend fun hentIsKravPaaGammeltRegelverk(vedtaksId: String): ServiceResult<Boolean> =
        client.get("brev/skribenten/vedtak/$vedtaksId/isKravPaaGammeltRegelverk").toServiceResult<Boolean>(::handlePenErrorResponse)

    override suspend fun hentIsKravStoettetAvDatabygger(vedtaksId: String): ServiceResult<PenService.KravStoettetAvDatabyggerResult> =
        client.get("brev/skribenten/vedtak/$vedtaksId/isKravStoettetAvDatabygger").toServiceResult<PenService.KravStoettetAvDatabyggerResult>(::handlePenErrorResponse)

    override suspend fun hentPesysBrevdata(saksId: Long, vedtaksId: Long?, brevkode: Brevkode.Redigerbart, avsenderEnhetsId: String?): ServiceResult<BrevdataResponse.Data> =
        client.get("brev/skribenten/sak/$saksId/brevdata/${brevkode.kode()}") {
            if (avsenderEnhetsId != null) {
                url {
                    parameters.append("enhetsId", avsenderEnhetsId)
                    vedtaksId?.let{ parameters.append("vedtaksId", it.toString()) }
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

    override suspend fun sendbrev(
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
