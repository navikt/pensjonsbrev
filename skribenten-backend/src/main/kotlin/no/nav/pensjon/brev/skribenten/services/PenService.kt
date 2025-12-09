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
import io.ktor.http.isSuccess
import io.ktor.serialization.jackson.jackson
import no.nav.brev.BrevExceptionDto
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.Pen.BestillExstreamBrevResponse
import no.nav.pensjon.brev.skribenten.model.Pen.SendRedigerbartBrevRequest
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.slf4j.LoggerFactory
import java.time.LocalDate
import kotlin.jvm.java

private val logger = LoggerFactory.getLogger(PenServiceHttp::class.java)

interface PenService {
    suspend fun hentSak(saksId: String): Pen.SakSelection?
    suspend fun bestillDoksysBrev(request: Api.BestillDoksysBrevRequest, enhetsId: String, saksId: Long): Pen.BestillDoksysBrevResponse
    suspend fun bestillExstreamBrev(bestillExstreamBrevRequest: Pen.BestillExstreamBrevRequest): BestillExstreamBrevResponse
    suspend fun redigerDoksysBrev(journalpostId: String, dokumentId: String): Pen.RedigerDokumentResponse?
    suspend fun redigerExstreamBrev(journalpostId: String): Pen.RedigerDokumentResponse?
    suspend fun hentAvtaleland(): List<Pen.Avtaleland>
    suspend fun hentIsKravPaaGammeltRegelverk(vedtaksId: String): Boolean?
    suspend fun hentIsKravStoettetAvDatabygger(vedtaksId: String): ServiceResult<KravStoettetAvDatabyggerResult>
    suspend fun hentPesysBrevdata(saksId: Long, vedtaksId: Long?, brevkode: Brevkode.Redigerbart, avsenderEnhetsId: String?): ServiceResult<BrevdataResponse.Data>
    suspend fun sendbrev(
        sendRedigerbartBrevRequest: SendRedigerbartBrevRequest,
        distribuer: Boolean,
    ): ServiceResult<Pen.BestillBrevResponse>

    data class KravStoettetAvDatabyggerResult(
        val kravStoettet: Map<String, Boolean> = emptyMap()
    )

    suspend fun hentP1VedleggData(saksId: Long, spraak: LanguageCode): ServiceResult<Api.GeneriskBrevdata>
}

class PenServiceException(message: String) : ServiceException(message)

class PenServiceHttp(config: Config, authService: AuthService) : PenService, ServiceStatus {
    private val penUrl = config.getString("url")
    private val penScope = config.getString("scope")

    private val client = HttpClient(CIO) {
        defaultRequest {
            url(penUrl)
        }
        installRetry(logger, shouldNotRetry = { method, _, _ -> method != HttpMethod.Get })
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

    private suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T? =
        when {
            status.isSuccess() -> body()
            status == HttpStatusCode.NotFound -> null
            else -> throw PenServiceException("Feil ved kall til PEN: ${bodyAsText()}")
        }

    override suspend fun hentSak(saksId: String): Pen.SakSelection? =
        client.get("brev/skribenten/sak/$saksId").bodyOrThrow<SakResponseDto>()?.let {
            Pen.SakSelection(
                saksId = it.saksId,
                foedselsnr = it.foedselsnr,
                foedselsdato = it.foedselsdato,
                navn = with(it.navn) { Pen.SakSelection.Navn(fornavn, mellomnavn, etternavn) },
                sakType = it.sakType,
            )
        }

    override suspend fun bestillDoksysBrev(
        request: Api.BestillDoksysBrevRequest,
        enhetsId: String,
        saksId: Long
    ): Pen.BestillDoksysBrevResponse {
        val response = client.post("brev/skribenten/doksys/sak/$saksId") {
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
        }

        return if (response.status.isSuccess()) {
            response.body()
        } else {
            val errorBody = response.bodyAsText()
            logger.error("En feil oppstod i kall til PEN: $errorBody")
            throw PenServiceException("Feil ved bestilling av doksysbrev: $errorBody")
        }
    }

    override suspend fun bestillExstreamBrev(bestillExstreamBrevRequest: Pen.BestillExstreamBrevRequest): BestillExstreamBrevResponse {
        val response = client.post("brev/pjoark030/bestillbrev") {
            setBody(bestillExstreamBrevRequest)
            contentType(ContentType.Application.Json)
        }

        return if (response.status.isSuccess()) {
            response.body()
        } else {
            val error = response.body<BestillExstreamBrevResponse.Error>().let {
                "Feil ved bestilling av exstreambrev - ${it.type}: ${it.message}"
            }
            logger.info(error)
            throw PenServiceException(error)
        }
    }

    override suspend fun redigerDoksysBrev(journalpostId: String, dokumentId: String): Pen.RedigerDokumentResponse? =
        client.get("brev/dokument/metaforce/$journalpostId/$dokumentId")
            .bodyOrThrow()

    override suspend fun redigerExstreamBrev(journalpostId: String): Pen.RedigerDokumentResponse? =
        client.get("brev/dokument/exstream/$journalpostId")
            .bodyOrThrow()

    override suspend fun hentAvtaleland(): List<Pen.Avtaleland> =
        client.get("brev/skribenten/avtaleland").bodyOrThrow() ?: emptyList()

    override val name = "PEN"
    override suspend fun ping(): ServiceResult<Boolean> =
        client.get("/pen/actuator/health/readiness")
            .toServiceResult<String>()
            .map { true }

    override suspend fun hentIsKravPaaGammeltRegelverk(vedtaksId: String): Boolean? =
        client.get("brev/skribenten/vedtak/$vedtaksId/isKravPaaGammeltRegelverk")
            .bodyOrThrow()

    override suspend fun hentIsKravStoettetAvDatabygger(vedtaksId: String): ServiceResult<PenService.KravStoettetAvDatabyggerResult> =
        client.get("brev/skribenten/vedtak/$vedtaksId/isKravStoettetAvDatabygger")
            .toServiceResult<PenService.KravStoettetAvDatabyggerResult>(::handlePenErrorResponse)

    override suspend fun hentPesysBrevdata(
        saksId: Long,
        vedtaksId: Long?,
        brevkode: Brevkode.Redigerbart,
        avsenderEnhetsId: String?
    ): ServiceResult<BrevdataResponse.Data> =
        client.get("brev/skribenten/sak/$saksId/brevdata/${brevkode.kode()}") {
            if (avsenderEnhetsId != null) {
                url {
                    parameters.append("enhetsId", avsenderEnhetsId)
                    vedtaksId?.let { parameters.append("vedtaksId", it.toString()) }
                }
            }
        }.toServiceResult<BrevdataResponse>(::handlePenErrorBrevdataResponse)
            .then {
                if (it.data != null) {
                    ServiceResult.Ok(it.data)
                } else {
                    ServiceResult.Error("Fikk hverken data eller feilmelding fra Pesys", HttpStatusCode.InternalServerError)
                }
            }

    data class P1VedleggDataResponse(val data: Api.GeneriskBrevdata?, val feil: BrevExceptionDto? = null)

    override suspend fun hentP1VedleggData(saksId: Long, spraak: LanguageCode): ServiceResult<Api.GeneriskBrevdata> {
        val response = client.get("brev/skribenten/sak/$saksId/p1data") {
            url {
                parameters.append("spraak", spraak.name)
            }
        }
        return if (response.status.isSuccess()) {
            ServiceResult.Ok(response.body<P1VedleggDataResponse>().data!!)
        } else {
            val feil = response.body<P1VedleggDataResponse?>()?.feil
            logger.error("En feil oppstod i kall til PEN: ${feil?.tittel}: ${feil?.melding}")
            ServiceResult.Error(feil?.melding ?: "Ukjent feil oppstod i kall til PEN", response.status, feil?.tittel)
        }
    }


    private suspend fun handlePenErrorBrevdataResponse(response: HttpResponse): ServiceResult<BrevdataResponse> {
        val body = response.body<BrevdataResponse>()
        return if (response.status == HttpStatusCode.InternalServerError) {
            logger.error("En feil oppstod i kall til PEN: ${body.feil?.let { "${it.tittel}: ${it.melding}" }}")
            ServiceResult.Error("Ukjent feil oppstod i kall til PEN",  HttpStatusCode.InternalServerError, body.feil?.tittel)
        } else {
            ServiceResult.Error(body.feil?.melding ?: "Ukjent feil oppstod i kall til PEN", response.status, body.feil?.tittel)
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
        val navn: Navn,
        val sakType: Pen.SakType,
        val enhetId: String?,
    ) {
        data class Navn(val fornavn: String, val mellomnavn: String?, val etternavn: String)
    }
}

data class BrevdataResponse(val data: Data?, val feil: BrevExceptionDto? = null) {
    data class Data(val felles: Felles, val brevdata: Api.GeneriskBrevdata)
}
