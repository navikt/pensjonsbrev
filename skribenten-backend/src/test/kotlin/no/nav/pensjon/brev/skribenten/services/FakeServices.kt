package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.callid.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.db.P1Data
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.JournalpostId
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.Pdl
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.Pen.BestillExstreamBrevResponse
import no.nav.pensjon.brev.skribenten.model.Pen.SendRedigerbartBrevRequest
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.HentSamhandlerAdresseResponseDto
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.HentSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.services.PenService.KravStoettetAvDatabyggerResult
import no.nav.pensjon.brev.skribenten.services.SafService.HentDokumenterResponse
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Pid

class NotYetStubbedException(message: String) : Exception(message)

fun notYetStubbed(description: String? = null): Nothing =
    throw NotYetStubbedException(description ?: "This method has not yet been stubbed in the test setup.")

open class FakeNavansattService(
    val harTilgangTilEnhet: Map<Pair<String, EnhetId>, Boolean> = emptyMap(),
    val navansatte: Map<String, String> = emptyMap(),
) : NavansattService {
    override suspend fun harTilgangTilEnhet(ansattId: String, enhetsId: EnhetId) =
        harTilgangTilEnhet.getOrDefault(Pair(ansattId, enhetsId), false)

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
    override suspend fun getEnhet(enhetId: EnhetId) = enheter[enhetId.value] ?: throw IllegalStateException("Enhet $enhetId ikke funnet i FakeNorg2Service")
}

open class FakeSamhandlerService(val navn: Map<String, String> = mapOf()) : SamhandlerService {
    override suspend fun hentSamhandlerNavn(idTSSEkstern: String) = navn[idTSSEkstern]
    override suspend fun finnSamhandler(requestDto: FinnSamhandlerRequestDto): FinnSamhandlerResponseDto = notYetStubbed()
    override suspend fun hentSamhandler(idTSSEkstern: String): HentSamhandlerResponseDto = notYetStubbed()
    override suspend fun hentSamhandlerAdresse(idTSSEkstern: String): HentSamhandlerAdresseResponseDto = notYetStubbed()
}

open class FakeP1Service: P1Service {
    override suspend fun lagreP1Data(
        p1DataInput: Api.GeneriskBrevdata,
        brevId: BrevId,
        saksId: SaksId,
    ): P1Data? = notYetStubbed()

    override suspend fun hentP1Data(
        brevId: BrevId,
        saksId: SaksId,
    ): Api.GeneriskBrevdata? = notYetStubbed()

    override suspend fun patchMedP1DataOmP1(
        brevdataResponse: BrevdataResponse.Data,
        brevkode: Brevkode.Redigerbart,
        brevId: BrevId?,
        saksId: SaksId,
    ): BrevdataResponse.Data = brevdataResponse
}

open class FakeBrevmetadataService(
    val eblanketter: List<BrevdataDto> = listOf(),
    val brevmaler: List<BrevdataDto> = listOf(),
    val maler: Map<String, BrevdataDto> = mapOf(),
) : BrevmetadataService {
    override suspend fun getAllBrev(): List<BrevdataDto> = brevmaler + eblanketter

    override suspend fun getBrevmalerForSakstype(sakstype: ISakstype) = brevmaler

    override suspend fun getEblanketter() = eblanketter

    override suspend fun getMal(brevkode: String) = maler[brevkode] ?: notYetStubbed()

}

open class FakeBrevbakerService(
    open var maler: List<TemplateDescription.Redigerbar> = listOf(),
    open var redigerbareMaler: MutableMap<RedigerbarBrevkode, TemplateDescription.Redigerbar> = mutableMapOf(),
) : BrevbakerService {
    override suspend fun getTemplates() = maler

    override suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart) = redigerbareMaler[brevkode]
    override suspend fun getAlltidValgbareVedlegg(brevId: BrevId) = notYetStubbed()

    override suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart): TemplateModelSpecification = notYetStubbed()
    override suspend fun renderMarkup(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
    ): LetterMarkupWithDataUsage = notYetStubbed()
    override suspend fun renderPdf(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        brevdata: RedigerbarBrevdata<*, *>,
        felles: Felles,
        redigertBrev: LetterMarkup,
        alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>,
    ): LetterResponse = notYetStubbed()
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
    override suspend fun hentSak(saksId: SaksId): Pen.SakSelection? = notYetStubbed()
    override suspend fun bestillExstreamBrev(bestillExstreamBrevRequest: Pen.BestillExstreamBrevRequest): BestillExstreamBrevResponse = notYetStubbed()
    override suspend fun redigerExstreamBrev(journalpostId: JournalpostId): Pen.RedigerDokumentResponse = notYetStubbed()
    override suspend fun hentAvtaleland(): List<Pen.Avtaleland> = notYetStubbed()
    override suspend fun hentIsKravPaaGammeltRegelverk(vedtaksId: VedtaksId): Boolean? = notYetStubbed()
    override suspend fun hentIsKravStoettetAvDatabygger(vedtaksId: VedtaksId): KravStoettetAvDatabyggerResult = notYetStubbed()
    override suspend fun hentPesysBrevdata(saksId: SaksId, vedtaksId: VedtaksId?, brevkode: Brevkode.Redigerbart, avsenderEnhetsId: EnhetId): BrevdataResponse.Data = notYetStubbed()
    override suspend fun sendbrev(sendRedigerbartBrevRequest: SendRedigerbartBrevRequest, distribuer: Boolean): Pen.BestillBrevResponse = notYetStubbed()
    override suspend fun hentP1VedleggData(saksId: SaksId, spraak: LanguageCode): Api.GeneriskBrevdata = notYetStubbed()
}


open class PdlServiceStub : PdlService {
    override suspend fun hentAdressebeskyttelse(ident: Pid, behandlingsnummer: Pdl.Behandlingsnummer?): List<Pdl.Gradering> = notYetStubbed()
    override suspend fun hentBrukerContext(ident: Pid, behandlingsnummer: Pdl.Behandlingsnummer?): Pdl.PersonContext = notYetStubbed()
}

open class SafServiceStub : SafService {
    override suspend fun waitForJournalpostStatusUnderArbeid(journalpostId: JournalpostId): JournalpostLoadingResult = notYetStubbed()
    override suspend fun getFirstDocumentInJournal(journalpostId: JournalpostId): HentDokumenterResponse = notYetStubbed()
    override suspend fun hentPdfForJournalpostId(journalpostId: JournalpostId): ByteArray = notYetStubbed()
}