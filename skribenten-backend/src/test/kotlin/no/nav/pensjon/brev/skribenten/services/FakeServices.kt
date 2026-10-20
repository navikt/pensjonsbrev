package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.callid.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevbaker.BrevbakerService
import no.nav.pensjon.brev.skribenten.brevredigering.domain.TssId
import no.nav.pensjon.brev.skribenten.fagsystem.Behandlingsnummer
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.*
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenClient.KravStoettetAvDatabyggerResult
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.model.Pen.BestillExstreamBrevResponse
import no.nav.pensjon.brev.skribenten.model.Pen.SendRedigerbartBrevRequest
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.HentSamhandlerAdresseResponseDto
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.HentSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.model.Sakstype
import no.nav.pensjon.brev.skribenten.vedlegg.P1RedigerbarDto
import no.nav.pensjon.brev.skribenten.services.SafService.HentDokumenterResponse
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Pid
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId

class NotYetStubbedException(message: String) : Exception(message)

fun notYetStubbed(description: String? = null): Nothing =
    throw NotYetStubbedException(description ?: "This method has not yet been stubbed in the test setup.")

open class FakeNavansattService(
    val harTilgangTilEnhet: Map<Pair<NavIdent, EnhetId>, Boolean> = emptyMap(),
    val navansatte: Map<NavIdent, String> = emptyMap(),
) : NavansattService {
    override suspend fun harTilgangTilEnhet(ansattId: NavIdent, enhetsId: EnhetId) =
        harTilgangTilEnhet.getOrDefault(Pair(ansattId, enhetsId), false)

    override suspend fun hentNavansatt(ansattId: NavIdent): Navansatt? = navansatte[ansattId]?.let {
        Navansatt(
            emptyList(),
            it,
            it.split(' ').first(),
            it.split(' ').last(),
        )
    }

    override suspend fun hentNavAnsattEnhetListe(ansattId: NavIdent) = notYetStubbed()
}

open class FakeNorg2Service(val enheter: Map<String, NavEnhet> = mapOf()) : Norg2Service {
    override suspend fun getEnhet(enhetId: EnhetId) = enheter[enhetId.value] ?: throw IllegalStateException("Enhet $enhetId ikke funnet i FakeNorg2Service")
}

open class FakeSamhandlerService(
    val navn: Map<TssId, String> = mapOf(),
    val typer: Map<TssId, String> = mapOf(),
    val idTyper: Map<TssId, String> = mapOf(),
    val offentligIder: Map<TssId, String> = mapOf(),
) :
    SamhandlerService {
    override suspend fun hentSamhandler(idTSSEkstern: TssId): HentSamhandlerResponseDto =
        if (listOf(navn, typer, idTyper, offentligIder).none { idTSSEkstern in it }) {
            HentSamhandlerResponseDto(null, HentSamhandlerResponseDto.FailureType.IKKE_FUNNET)
        } else {
            HentSamhandlerResponseDto(
                success = HentSamhandlerResponseDto.Success(
                    navn = navn[idTSSEkstern] ?: "",
                    samhandlerType = typer[idTSSEkstern] ?: "",
                    offentligId = offentligIder[idTSSEkstern] ?: "",
                    idType = idTyper[idTSSEkstern] ?: "",
                ),
                failure = null,
            )
        }

    override suspend fun hentSamhandlerNavn(idTSSEkstern: TssId) = navn[idTSSEkstern]
    override suspend fun hentSamhandlerType(idTSSEkstern: TssId) = typer[idTSSEkstern]
    override suspend fun finnSamhandler(requestDto: FinnSamhandlerRequestDto): FinnSamhandlerResponseDto = notYetStubbed()
    override suspend fun hentSamhandlerAdresse(idTSSEkstern: TssId): HentSamhandlerAdresseResponseDto = notYetStubbed()
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
    override suspend fun getTemplates() = maler

    override suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart) = redigerbareMaler[brevkode]
    override suspend fun getAlltidValgbareVedlegg(brevkode: Brevkode.Redigerbart): Set<AlltidValgbartVedleggBrevkode> = notYetStubbed()

    override suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart): TemplateModelSpecification? = notYetStubbed()
    override suspend fun renderMarkup(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        felles: BrevbakerFelles,
        fagsystemBrevdata: FagsystemBrevdata,
        saksbehandlervalg: SaksbehandlervalgIDSL,
    ): LetterMarkupWithDataUsage = notYetStubbed()
    override suspend fun renderPdf(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        fagsystemBrevdata: FagsystemBrevdata,
        saksbehandlervalg: SaksbehandlervalgIDSL,
        felles: BrevbakerFelles,
        redigertBrev: LetterMarkup,
        alltidValgbareVedlegg: List<AlltidValgbartVedleggBrevkode>,
        redigerteVedlegg: Map<VedleggId, LetterMarkup.Attachment>,
        pdfVedlegg: List<PDFVedleggTittel>,
    ): LetterResponse = notYetStubbed()
    override suspend fun hentRedigerbareVedleggTitler(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        fagsystemBrevdata: FagsystemBrevdata,
        saksbehandlervalg: SaksbehandlervalgIDSL,
        felles: BrevbakerFelles,
    ): RedigerbareVedleggTitler = notYetStubbed()
    override suspend fun harRedigerbareVedlegg(brevkode: Brevkode.Redigerbart): Boolean = notYetStubbed()
    override suspend fun renderRedigerbartVedlegg(
        brevkode: Brevkode.Redigerbart,
        spraak: LanguageCode,
        fagsystemBrevdata: FagsystemBrevdata,
        saksbehandlervalg: SaksbehandlervalgIDSL,
        felles: BrevbakerFelles,
        vedleggId: VedleggId,
    ): LetterMarkup.Attachment? = notYetStubbed()
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

open class PenClientStub : PenClient {
    override suspend fun hentSak(saksId: SaksId): Pen.SakSelection? = notYetStubbed()
    override suspend fun bestillExstreamBrev(bestillExstreamBrevRequest: Pen.BestillExstreamBrevRequest): BestillExstreamBrevResponse = notYetStubbed()
    override suspend fun redigerExstreamBrev(journalpostId: JournalpostId): Pen.RedigerDokumentResponse = notYetStubbed()
    override suspend fun hentAvtaleland(): List<Pen.Avtaleland> = notYetStubbed()
    override suspend fun hentIsKravPaaGammeltRegelverk(vedtaksId: VedtaksId): Boolean? = notYetStubbed()
    override suspend fun hentIsKravStoettetAvDatabygger(vedtaksId: VedtaksId): KravStoettetAvDatabyggerResult = notYetStubbed()
    override suspend fun hentPesysBrevdata(saksId: SaksId, vedtaksId: VedtaksId?, brevkode: Brevkode.Redigerbart, avsenderEnhetsId: EnhetId): BrevdataResponse.Data = notYetStubbed()
    override suspend fun sendbrev(sendRedigerbartBrevRequest: SendRedigerbartBrevRequest, distribuer: Boolean): Pen.BestillBrevResponse = notYetStubbed()
    override suspend fun hentP1VedleggData(saksId: SaksId, spraak: LanguageCode): P1RedigerbarDto = notYetStubbed()
}


open class PdlServiceStub : PdlService {
    override suspend fun hentAdressebeskyttelse(ident: Pid, behandlingsnumre: List<Behandlingsnummer>): List<Pdl.Gradering>? = notYetStubbed()
    override suspend fun hentBrukerContext(ident: Pid, behandlingsnumre: List<Behandlingsnummer>): Pdl.PersonContext? =
        notYetStubbed()
}

open class SafServiceStub : SafService {
    override suspend fun waitForJournalpostStatusUnderArbeid(journalpostId: JournalpostId): JournalpostLoadingResult = notYetStubbed()
    override suspend fun getFirstDocumentInJournal(journalpostId: JournalpostId): HentDokumenterResponse = notYetStubbed()
    override suspend fun hentPdfForJournalpostId(journalpostId: JournalpostId): ByteArray = notYetStubbed()
}

open class LegacyBrevServiceStub : LegacyBrevService {
    override suspend fun bestillOgRedigerExstreamBrev(gjelderPid: Pid, request: Api.BestillExstreamBrevRequest, saksId: SaksId): Api.BestillOgRedigerBrevResponse = notYetStubbed()
    override suspend fun bestillOgRedigerEblankett(gjelderPid: Pid, request: Api.BestillEblankettRequest, saksId: SaksId): Api.BestillOgRedigerBrevResponse = notYetStubbed()
}

open class PensjonPersonDataServiceStub : PensjonPersonDataService {
    override suspend fun hentKontaktadresse(pid: Pid): KontaktAdresseResponseDto? = null
}