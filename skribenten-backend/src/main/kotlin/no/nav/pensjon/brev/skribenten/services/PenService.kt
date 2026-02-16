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
import no.nav.pensjon.brev.api.model.ISakstype
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.JournalpostId
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.Pen.BestillExstreamBrevResponse
import no.nav.pensjon.brev.skribenten.model.Pen.SendRedigerbartBrevRequest
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brev.skribenten.serialize.SakstypeModule
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.slf4j.LoggerFactory
import java.time.LocalDate
import kotlin.jvm.java

private val logger = LoggerFactory.getLogger(PenServiceHttp::class.java)

interface PenService {
    suspend fun hentSak(saksId: SaksId): Pen.SakSelection?
    suspend fun bestillExstreamBrev(bestillExstreamBrevRequest: Pen.BestillExstreamBrevRequest): BestillExstreamBrevResponse
    suspend fun redigerExstreamBrev(journalpostId: JournalpostId): Pen.RedigerDokumentResponse?
    suspend fun hentAvtaleland(): List<Pen.Avtaleland>
    suspend fun hentIsKravPaaGammeltRegelverk(vedtaksId: VedtaksId): Boolean?
    suspend fun hentIsKravStoettetAvDatabygger(vedtaksId: VedtaksId): KravStoettetAvDatabyggerResult?
    suspend fun hentPesysBrevdata(saksId: SaksId, vedtaksId: VedtaksId?, brevkode: Brevkode.Redigerbart, avsenderEnhetsId: EnhetId): BrevdataResponse.Data
    suspend fun hentP1VedleggData(saksId: SaksId, spraak: LanguageCode): Api.GeneriskBrevdata
    suspend fun sendbrev(sendRedigerbartBrevRequest: SendRedigerbartBrevRequest, distribuer: Boolean): Pen.BestillBrevResponse

    data class KravStoettetAvDatabyggerResult(
        val kravStoettet: Map<String, Boolean> = emptyMap()
    )
}

class PenServiceException(message: String) : ServiceException(message)
class PenDataException(val feil: BrevExceptionDto) : ServiceException("${feil.tittel}: ${feil.melding}", status = HttpStatusCode.UnprocessableEntity)

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
                registerModule(SakstypeModule)
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
        callIdAndOnBehalfOfClient(penScope, authService)
    }

    private suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T? =
        when {
            status.isSuccess() -> body()
            status == HttpStatusCode.NotFound -> null
            else -> throw PenServiceException("Feil ved kall til PEN: ${bodyAsText()}")
        }

    override suspend fun hentSak(saksId: SaksId): Pen.SakSelection? =
        client.get("brev/skribenten/sak/${saksId.id}").bodyOrThrow<SakResponseDto>()?.let {
            Pen.SakSelection(
                saksId = it.saksId,
                foedselsnr = it.foedselsnr,
                foedselsdato = it.foedselsdato,
                navn = with(it.navn) { Pen.SakSelection.Navn(fornavn, mellomnavn, etternavn) },
                sakType = it.sakType,
            )
        }

    override suspend fun bestillExstreamBrev(bestillExstreamBrevRequest: Pen.BestillExstreamBrevRequest): BestillExstreamBrevResponse {
        val response = client.post("brev/pjoark030/bestillbrev") {
            setBody(bestillExstreamBrevRequest)
            contentType(ContentType.Application.Json)
        }

        return if (response.status.isSuccess()) {
            response.body()
        } else {
            val error = response.body<BestillExstreamBrevResponse.Error>()
            if (error.type == "AdresseIkkeRegistrert") {
                throw PenDataException(BrevExceptionDto(
                    tittel = "Adresse ikke registrert",
                    melding = error.message ?: "",
                ))
            } else {
                throw PenServiceException(error.let {
                    "Feil ved bestilling av exstreambrev - ${it.type}: ${it.message}"
                })
            }
        }
    }

    override suspend fun redigerExstreamBrev(journalpostId: JournalpostId): Pen.RedigerDokumentResponse? =
        client.get("brev/dokument/exstream/${journalpostId.id}")
            .bodyOrThrow()

    override suspend fun hentAvtaleland(): List<Pen.Avtaleland> =
        client.get("brev/skribenten/avtaleland").bodyOrThrow() ?: emptyList()

    override suspend fun ping() =
        ping("PEN") { client.get("/pen/actuator/health/readiness") }

    override suspend fun hentIsKravPaaGammeltRegelverk(vedtaksId: VedtaksId): Boolean? =
        client.get("brev/skribenten/vedtak/${vedtaksId.id}/isKravPaaGammeltRegelverk")
            .bodyOrThrow()

    override suspend fun hentIsKravStoettetAvDatabygger(vedtaksId: VedtaksId): PenService.KravStoettetAvDatabyggerResult? =
        client.get("brev/skribenten/vedtak/${vedtaksId.id}/isKravStoettetAvDatabygger").bodyOrThrow()

    override suspend fun hentPesysBrevdata(
        saksId: SaksId,
        vedtaksId: VedtaksId?,
        brevkode: Brevkode.Redigerbart,
        avsenderEnhetsId: EnhetId
    ): BrevdataResponse.Data =
        client.get("brev/skribenten/sak/${saksId.id}/brevdata/${brevkode.kode()}") {
            mapOf(
                "enhetsId" to avsenderEnhetsId.value,
                "vedtaksId" to vedtaksId?.id?.toString(),
            )
                .filter { it.value != null }
                .takeIf { it.isNotEmpty() }
                ?.let { params ->
                    url {
                        params.forEach { (key, value) -> parameters.append(key, value!!) }
                    }
                }
        }.brevdataOrThrow(saksId = saksId, vedtaksId = vedtaksId)

    override suspend fun hentP1VedleggData(saksId: SaksId, spraak: LanguageCode): P1VedleggDataResponse =
        client.get("brev/skribenten/sak/${saksId.id}/p1data") {
            url {
                parameters.append("spraak", spraak.name)
            }
        }.brevdataOrThrow(saksId = saksId)

    private suspend inline fun <reified Data : Any> HttpResponse.brevdataOrThrow(saksId: SaksId, vedtaksId: VedtaksId? = null): Data =
        when {
            status.isSuccess() -> body<BrevdataResponseWrapper<Data>>().data
            status == HttpStatusCode.UnprocessableEntity -> throw PenDataException(body<BrevdataFeilResponse>().feil)
            else -> throw PenServiceException("Feil ved kall til PEN: ${status.value} - ${bodyAsText()}. Saksid: ${saksId.id} ${vedtaksId?.let { ", vedtaksId: ${it.id}" }}")
        }

    override suspend fun sendbrev(sendRedigerbartBrevRequest: SendRedigerbartBrevRequest, distribuer: Boolean): Pen.BestillBrevResponse =
        client.post("brev/skribenten/sendbrev") {
            setBody(sendRedigerbartBrevRequest)
            contentType(ContentType.Application.Json)
            url { parameters.append("distribuer", distribuer.toString()) }
        }.bodyOrThrow()!!


    private data class SakResponseDto(
        val saksId: SaksId,
        val foedselsnr: Foedselsnummer,
        val foedselsdato: LocalDate,
        val navn: Navn,
        val sakType: ISakstype,
        val enhetId: String?,
    ) {
        data class Navn(val fornavn: String, val mellomnavn: String?, val etternavn: String)
    }
}

data class BrevdataFeilResponse(val feil: BrevExceptionDto)
data class BrevdataResponseWrapper<T : Any>(val data: T)

typealias P1VedleggDataResponse = Api.GeneriskBrevdata
object BrevdataResponse {
    data class Data(val felles: Felles, val brevdata: Api.GeneriskBrevdata)
}
