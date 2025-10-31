package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.callid.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.Pdl
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.Pen.BestillExstreamBrevResponse
import no.nav.pensjon.brev.skribenten.model.Pen.SendRedigerbartBrevRequest
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.HentSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.services.PenService.KravStoettetAvDatabyggerResult
import no.nav.pensjon.brev.skribenten.services.SafService.HentDokumenterResponse
import no.nav.pensjon.brevbaker.api.model.*

class NotYetStubbedException(message: String) : Exception(message)

fun notYetStubbed(description: String? = null): Nothing =
    throw NotYetStubbedException(description ?: "This method has not yet been stubbed in the test setup.")

open class FakeNavansattService(
    val harTilgangTilEnhet: Map<Pair<String, String>, Boolean> = emptyMap(),
    val navansatte: Map<String, String> = emptyMap(),
) : NavansattService {
    override suspend fun harTilgangTilEnhet(ansattId: String, enhetsId: String) =
        ServiceResult.Ok(harTilgangTilEnhet.getOrDefault(Pair(ansattId, enhetsId), false))

    override suspend fun hentNavansatt(ansattId: String): Navansatt? = navansatte[ansattId]?.let {
        Navansatt(
            emptyList(),
            it,
            it.split(' ').first(),
            it.split(' ').last(),
        )
    }

    override suspend fun hentNavAnsattEnhetListe(ansattId: String) = notYetStubbed()
}

open class FakeNorg2Service(val enheter: Map<String, NavEnhet> = mapOf()) : Norg2Service {
    override suspend fun getEnhet(enhetId: String) = enheter[enhetId]
}

open class FakeSamhandlerService(val navn: Map<String, String> = mapOf()) : SamhandlerService {
    override suspend fun hentSamhandlerNavn(idTSSEkstern: String) = navn[idTSSEkstern]
    override suspend fun finnSamhandler(requestDto: FinnSamhandlerRequestDto): FinnSamhandlerResponseDto = notYetStubbed()
    override suspend fun hentSamhandler(idTSSEkstern: String): HentSamhandlerResponseDto = notYetStubbed()
}

open class FakeBrevmetadataService(
    val eblanketter: List<BrevdataDto> = listOf(),
    val brevmaler: List<BrevdataDto> = listOf(),
    val maler: Map<String, BrevdataDto> = mapOf(),
) : BrevmetadataService {
    override suspend fun getAllBrev(): List<BrevdataDto> = brevmaler + eblanketter

    override suspend fun getBrevmalerForSakstype(sakstype: Sakstype) = brevmaler

    override suspend fun getEblanketter() = eblanketter

    override suspend fun getMal(brevkode: String) = maler[brevkode] ?: notYetStubbed()

}

open class FakeBrevbakerService(
    open var maler: List<TemplateDescription.Redigerbar> = listOf(),
    open var redigerbareMaler: MutableMap<RedigerbarBrevkode, TemplateDescription.Redigerbar> = mutableMapOf(),
) : BrevbakerService {
    override suspend fun getTemplates() = ServiceResult.Ok(maler)

    override suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart) = redigerbareMaler[brevkode]

    override suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart): ServiceResult<TemplateModelSpecification> = notYetStubbed()
    override suspend fun renderMarkup(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
    ): ServiceResult<LetterMarkupWithDataUsage> = notYetStubbed()
    override suspend fun renderPdf(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
        redigertBrev: LetterMarkup,
    ): ServiceResult<LetterResponse> = notYetStubbed()
}

private val objectMapper = jacksonObjectMapper()

fun <T> mockEngine(responseBody: T) = MockEngine {
    respond(
        content = responseBody?.let { objectMapper.writeValueAsString(it) } ?: "",
        status = responseBody?.let { HttpStatusCode.OK } ?: HttpStatusCode.NotFound,
        headers = headersOf("Content-Type", "application/json")
    )
}

/**
 * Helper for tests that need a HttpClient with a MockEngine and also need to run with a CallId and a Principal.
 */
fun <T> httpClientTest(responseBody: T, block: suspend (MockEngine) -> Unit) = runBlocking {
    withCallId("123") {
        withPrincipal(MockPrincipal(NavIdent("123"), "TestPrincipal")) {
            block(mockEngine(responseBody))
        }
    }
}

open class PenServiceStub : PenService {
    override suspend fun hentSak(saksId: String): ServiceResult<Pen.SakSelection> = notYetStubbed()
    override suspend fun bestillDoksysBrev(
        request: Api.BestillDoksysBrevRequest,
        enhetsId: String,
        saksId: Long
    ): ServiceResult<Pen.BestillDoksysBrevResponse> = notYetStubbed()
    override suspend fun bestillExstreamBrev(
        bestillExstreamBrevRequest: Pen.BestillExstreamBrevRequest,
    ): ServiceResult<BestillExstreamBrevResponse> = notYetStubbed()
    override suspend fun redigerDoksysBrev(journalpostId: String, dokumentId: String): ServiceResult<Pen.RedigerDokumentResponse> = notYetStubbed()
    override suspend fun redigerExstreamBrev(journalpostId: String): ServiceResult<Pen.RedigerDokumentResponse> = notYetStubbed()
    override suspend fun hentAvtaleland(): ServiceResult<List<Pen.Avtaleland>> = notYetStubbed()
    override suspend fun hentIsKravPaaGammeltRegelverk(vedtaksId: String): ServiceResult<Boolean> = notYetStubbed()
    override suspend fun hentIsKravStoettetAvDatabygger(vedtaksId: String): ServiceResult<KravStoettetAvDatabyggerResult> = notYetStubbed()
    override suspend fun hentPesysBrevdata(saksId: Long, vedtaksId: Long?, brevkode: Brevkode.Redigerbart, avsenderEnhetsId: String?): ServiceResult<BrevdataResponse.Data> = notYetStubbed()
    override suspend fun sendbrev(
        sendRedigerbartBrevRequest: SendRedigerbartBrevRequest,
        distribuer: Boolean,
    ): ServiceResult<Pen.BestillBrevResponse> = notYetStubbed()
}


open class PdlServiceStub : PdlService {
    override suspend fun hentAdressebeskyttelse(fnr: String, behandlingsnummer: Pdl.Behandlingsnummer?): ServiceResult<List<Pdl.Gradering>> = notYetStubbed()
}

open class SafServiceStub : SafService {
    override suspend fun waitForJournalpostStatusUnderArbeid(journalpostId: String): JournalpostLoadingResult = notYetStubbed()
    override suspend fun getFirstDocumentInJournal(journalpostId: String): ServiceResult<HentDokumenterResponse> = notYetStubbed()
    override suspend fun hentPdfForJournalpostId(journalpostId: String): ServiceResult<ByteArray> = notYetStubbed()
}