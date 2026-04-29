@file:OptIn(InternKonstruktoer::class)

package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import io.ktor.http.*
import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.*
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevbaker.RenderService
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringFacade
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringFacadeFactory
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.db.kryptering.KrypteringService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevdataResponse
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.P1Service
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.serialize.Sakstype
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles.*
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles.NavEnhet
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Pid
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Telefonnummer
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.SignaturImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsageImpl
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.opentest4j.AssertionFailedError
import java.time.LocalDate
import kotlin.collections.get

/**
 * Har valgt å gjøre det på denne måten underveis for å understøtte steg-for-steg refaktorering med blanding mellom ny og gammel stil.
 * Felles fixtures tror jeg kommer til å være nyttig uansett, så det er spesielt service-mocks jeg ikke er sikker på.
 * Jeg er også usikker på om sluttresultatet tester helt ned mot database eller ei.
 */
abstract class BrevredigeringHandlerTestBase {

    @BeforeAll
    fun startDbOnce() {
        SharedPostgres.subscribeAndEnsureDatabaseInitialized(this)
    }

    @AfterAll
    fun kansellerDbAvhengighet() {
        SharedPostgres.cancelSubscription(this)
    }

    @BeforeEach
    fun clearMocks() {
        brevbakerService.renderMarkupKall.clear()
        brevbakerService.renderMarkupResultat = {
            letter.medSignatur(
                saksbehandler = it.signerendeSaksbehandlere?.saksbehandler,
                attestant = it.signerendeSaksbehandlere?.attesterendeSaksbehandler
            ).medAnnenMottakerNavn(it.annenMottakerNavn)
        }

        brevbakerService.redigerbareMaler[Testbrevkoder.INFORMASJONSBREV] = informasjonsbrev
        brevbakerService.redigerbareMaler[Testbrevkoder.VEDTAKSBREV] = vedtaksbrev
        brevbakerService.redigerbareMaler[Testbrevkoder.VARSELBREV] = varselbrevIVedtakskontekst
        stagePdf(stagetPDF)

        penService.pesysBrevdata = brevdataResponseData
        penService.sendBrevResponse = bestillBrevresponse
    }


    private val navAnsattService = FakeNavansattService(
        harTilgangTilEnhet = mapOf(
            Pair(saksbehandler1Principal.navIdent.id, PRINCIPAL_NAVENHET_ID) to true,
            Pair(saksbehandler2Principal.navIdent.id, PRINCIPAL_NAVENHET_ID) to true,
            Pair(attestant1Principal.navIdent.id, PRINCIPAL_NAVENHET_ID) to true,
            Pair(attestant2Principal.navIdent.id, PRINCIPAL_NAVENHET_ID) to true,
        ),
        navansatte = mapOf(
            saksbehandler1Principal.navIdent.id to saksbehandler1Principal.fullName,
            saksbehandler2Principal.navIdent.id to saksbehandler2Principal.fullName,
            attestant1Principal.navIdent.id to attestant1Principal.fullName,
            attestant2Principal.navIdent.id to attestant2Principal.fullName,
        )
    )

    protected val brevbakerService = BrevredigeringFakeBrevbakerService()
    protected val penService = FakePenClient()
    protected val samhandlerService = FakeSamhandlerService(mapOf("samhandler1" to "Sam Handler AS"))

    protected val brevredigeringFacade = createFacade()

    protected companion object Fixtures {
        init {
            KrypteringService.init("ZBn9yGLDluLZVVGXKZxvnPun3kPQ2ccF")
            initADGroups()
        }

        val PRINCIPAL_NAVENHET_ID = EnhetId("1234")
        val saksbehandler1Principal = MockPrincipal(NavIdent("Agent Smith"), "Hugo Weaving", setOf(ADGroups.pensjonSaksbehandler))
        val saksbehandler2Principal = MockPrincipal(NavIdent("Morpheus"), "Laurence Fishburne", setOf(ADGroups.pensjonSaksbehandler))
        val attestant1Principal = MockPrincipal(NavIdent("Key Maker"), "Randall Kim", mutableSetOf(ADGroups.pensjonSaksbehandler, ADGroups.attestant))
        val attestant2Principal = MockPrincipal(NavIdent("The Oracle"), "Gloria Foster", mutableSetOf(ADGroups.pensjonSaksbehandler, ADGroups.attestant))

        val sak1 = Pen.SakSelection(
            saksId = SaksId(1234L),
            foedselsdato = LocalDate.now().minusYears(42),
            navn = Pen.SakSelection.Navn("a", "b", "c"),
            sakType = Sakstype("ALDER"),
            pid = Pid("12345678910")
        )

        val letter = letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "red pill"))))
            .medSignatur(saksbehandler = saksbehandler1Principal.fullName, attestant = null)

        val informasjonsbrev = TemplateDescription.Redigerbar(
            name = Testbrevkoder.INFORMASJONSBREV.kode(),
            letterDataClass = "template letter data class",
            languages = listOf(LanguageCode.ENGLISH),
            metadata = LetterMetadata(
                displayTitle = "Et informasjonsbrev",
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
            ),
            kategori = TemplateDescription.Redigerbar.Brevkategori("INFORMASJONSBREV"),
            brevkontekst = TemplateDescription.Brevkontekst.ALLE,
            sakstyper = setOf(TemplateDescription.Redigerbar.Sakstype("S1"), TemplateDescription.Redigerbar.Sakstype("S2")),
        )

        val vedtaksbrev = TemplateDescription.Redigerbar(
            name = Testbrevkoder.VEDTAKSBREV.kode(),
            letterDataClass = "template letter data class",
            languages = listOf(LanguageCode.ENGLISH),
            metadata = LetterMetadata(
                displayTitle = "Et vedtaksbrev",
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            ),
            kategori = TemplateDescription.Redigerbar.Brevkategori("UFOEREPENSJON"),
            brevkontekst = TemplateDescription.Brevkontekst.VEDTAK,
            sakstyper = setOf(TemplateDescription.Redigerbar.Sakstype("S1"), TemplateDescription.Redigerbar.Sakstype("S2"))
        )

        private val varselbrevIVedtakskontekst = TemplateDescription.Redigerbar(
            name = Testbrevkoder.VARSELBREV.kode(),
            letterDataClass = "template letter data class",
            languages = listOf(LanguageCode.ENGLISH),
            metadata = LetterMetadata(
                displayTitle = "Et varselbrev",
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
            ),
            kategori = TemplateDescription.Redigerbar.Brevkategori("VARSEL"),
            brevkontekst = TemplateDescription.Brevkontekst.VEDTAK,
            sakstyper = setOf(TemplateDescription.Redigerbar.Sakstype("S1"), TemplateDescription.Redigerbar.Sakstype("S2")),
        )

        val stagetPDF = "nesten en pdf".encodeToByteArray()

        val brevdataResponseData = BrevdataResponse.Data(
            felles = BrevbakerFelles(
                dokumentDato = LocalDate.now(),
                saksnummer = sak1.saksId.toString(),
                avsenderEnhet = NavEnhet(
                    nettside = "nav.no",
                    navn = "Nav Familie- og pensjonsytelser Porsgrunn",
                    telefonnummer = Telefonnummer("55553334"),
                ),
                bruker = Bruker(
                    fornavn = "Test",
                    mellomnavn = "\"bruker\"",
                    etternavn = "Testerson",
                    foedselsnummer = Foedselsnummer("01019878910"),
                ),
                signerendeSaksbehandlere = SignerendeSaksbehandlere(
                    saksbehandler = "Ole Saksbehandler",
                    attesterendeSaksbehandler = "Per Attesterende"
                ),
                annenMottakerNavn = null,
            ),
            brevdata = Api.GeneriskBrevdata()
        )

        val bestillBrevresponse = Pen.BestillBrevResponse(JournalpostId(123), null)

        fun LetterMarkupImpl.medSignatur(saksbehandler: String?, attestant: String?) =
            copy(
                signatur = (signatur as SignaturImpl).copy(
                    saksbehandlerNavn = saksbehandler,
                    attesterendeSaksbehandlerNavn = attestant
                )
            )

        fun LetterMarkupImpl.medAnnenMottakerNavn(navn: String?) =
            copy(
                sakspart = (sakspart as LetterMarkupImpl.SakspartImpl).copy(
                    annenMottakerNavn = navn,
                )
            )
    }

    protected fun createFacade(p1Service: P1Service = FakeP1Service()): BrevredigeringFacade = BrevredigeringFacadeFactory.create(
        brevService = BrevService(penService, LegacyBrevServiceStub()),
        brevdataService = BrevdataService(penService, samhandlerService),
        brevmalService = BrevmalService(brevbakerService, penService, FakeBrevmetadataService()),
        navansattService = navAnsattService,
        p1Service = p1Service,
        renderService = RenderService(brevbakerService),
    )

    protected suspend fun opprettBrev(
        facade: BrevredigeringFacade = brevredigeringFacade,
        principal: UserPrincipal = saksbehandler1Principal,
        reserverForRedigering: Boolean = false,
        mottaker: Dto.Mottaker? = null,
        saksbehandlerValg: SaksbehandlerValg = SaksbehandlerValg().apply { put("valg", true) },
        brevkode: Brevkode.Redigerbart = Testbrevkoder.INFORMASJONSBREV,
        vedtaksId: VedtaksId? = null,
        sak: Pen.SakSelection = sak1,
        avsenderEnhetsId: EnhetId = PRINCIPAL_NAVENHET_ID,
    ): Outcome<Dto.Brevredigering, BrevredigeringError> = withPrincipal(principal) {
        facade.opprettBrev(
            OpprettBrevHandlerImpl.Request(
                saksId = sak.saksId,
                vedtaksId = vedtaksId,
                brevkode = brevkode,
                spraak = LanguageCode.ENGLISH,
                avsenderEnhetsId = avsenderEnhetsId,
                saksbehandlerValg = saksbehandlerValg,
                reserverForRedigering = reserverForRedigering,
                mottaker = mottaker,
            )
        )
    }

    protected suspend fun oppdaterBrev(
        brevId: BrevId,
        nyeSaksbehandlerValg: SaksbehandlerValg? = null,
        nyttRedigertbrev: Edit.Letter? = null,
        frigiReservasjon: Boolean = false,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Dto.Brevredigering, BrevredigeringError>? = withPrincipal(principal) {
        brevredigeringFacade.oppdaterBrev(
            OppdaterBrevHandler.Request(
                brevId = brevId,
                nyeSaksbehandlerValg = nyeSaksbehandlerValg,
                nyttRedigertbrev = nyttRedigertbrev,
                frigiReservasjon = frigiReservasjon
            )
        )
    }

    protected suspend fun hentBrev(
        brevId: BrevId,
        reserverForRedigering: Boolean = false,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Dto.Brevredigering, BrevredigeringError>? = withPrincipal(principal) {
        brevredigeringFacade.hentBrev(
            HentBrevHandler.Request(
                brevId = brevId,
                reserverForRedigering = reserverForRedigering,
            )
        )
    }

    protected suspend fun slettBrev(
        brev: Dto.Brevredigering,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Unit, BrevredigeringError>? = slettBrev(brev.info.id, principal)

    protected suspend fun slettBrev(
        brevId: BrevId,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Unit, BrevredigeringError>? = withPrincipal(principal) {
        brevredigeringFacade.slettBrev(SlettBrevHandler.Request(brevId = brevId))
    }

    protected suspend fun attester(
        brev: Dto.Brevredigering,
        attestant: UserPrincipal = attestant1Principal,
        frigiReservasjon: Boolean = false,
        nyeSaksbehandlerValg: SaksbehandlerValg? = null,
        nyttRedigertbrev: Edit.Letter? = null,
    ) = withPrincipal(attestant) {
        brevredigeringFacade.attesterBrev(
            AttesterBrevHandler.Request(
                brevId = brev.info.id,
                frigiReservasjon = frigiReservasjon,
                nyeSaksbehandlerValg = nyeSaksbehandlerValg,
                nyttRedigertbrev = nyttRedigertbrev,
            )
        )
    }

    protected suspend fun veksleKlarStatus(
        brev: Dto.Brevredigering,
        klar: Boolean,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Dto.BrevInfo, BrevredigeringError>? = withPrincipal(principal) {
        brevredigeringFacade.veksleKlarStatus(
            VeksleKlarStatusHandler.Request(
                brevId = brev.info.id,
                klar = klar
            )
        )
    }

    protected suspend fun arkiverBrev(
        brev: Dto.Brevredigering,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Dto.SendBrevResult, BrevredigeringError>? = withPrincipal(principal) {
        assertThat(hentEllerOpprettPdf(brev, principal = principal)).isSuccess()
        assertThat(veksleKlarStatus(brev, true, principal = principal)).isSuccess()

        penService.sendBrevResponse = Pen.BestillBrevResponse(
            bestillBrevresponse.journalpostId,
            Pen.BestillBrevResponse.Error(null, "Distribuering feilet", null)
        )
        val result = sendBrev(brev, saksbehandler1Principal)
        penService.sendBrevResponse = bestillBrevresponse

        result
    }

    protected suspend fun hentEllerOpprettPdf(
        brev: Dto.Brevredigering,
        principal: UserPrincipal = saksbehandler1Principal,
        facade: BrevredigeringFacade = brevredigeringFacade,
    ): Outcome<Dto.HentDocumentResult, BrevredigeringError>? =
        withPrincipal(principal) {
            facade.hentPDF(HentEllerOpprettPdfHandler.Request(brevId = brev.info.id))
        }

    protected suspend fun endreDistribusjonstype(
        brevId: BrevId,
        nyDistribusjonstype: Distribusjonstype,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Dto.BrevInfo, BrevredigeringError>? = withPrincipal(principal) {
        brevredigeringFacade.endreDistribusjonstype(
            EndreDistribusjonstypeHandler.Request(
                brevId = brevId,
                type = nyDistribusjonstype,
            )
        )
    }

    protected suspend fun sendBrev(brev: Dto.Brevredigering, principal: UserPrincipal = saksbehandler1Principal): Outcome<Dto.SendBrevResult, BrevredigeringError>? =
        withPrincipal(principal) {
            brevredigeringFacade.sendBrev(SendBrevHandler.Request(brevId = brev.info.id))
        }

    protected fun stagePdf(pdf: ByteArray) {
        brevbakerService.renderPdfResultat = LetterResponse(
            file = pdf,
            contentType = ContentType.Application.Pdf.toString(),
            letterMetadata = LetterMetadata(
                displayTitle = "En fin tittel",
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
            )
        )
    }

    protected class ResultFailure(error: BrevredigeringError) :
        AssertionFailedError(null, Outcome.Success::class.java, error::class.java)

    protected fun <T> Outcome<T, BrevredigeringError>?.resultOrFail(): T = when (this) {
        is Outcome.Success -> value
        is Outcome.Failure -> throw ResultFailure(error)
        null -> throw AssertionError("Resultat var null")
    }

    protected class BrevredigeringFakeBrevbakerService : FakeBrevbakerService() {
        lateinit var renderMarkupResultat: suspend ((f: BrevbakerFelles) -> LetterMarkup)
        lateinit var renderPdfResultat: LetterResponse
        var modelSpecificationResultat: TemplateModelSpecification? = null
        override var redigerbareMaler: MutableMap<RedigerbarBrevkode, TemplateDescription.Redigerbar> = mutableMapOf()
        val renderMarkupKall = mutableListOf<Pair<Brevkode.Redigerbart, LanguageCode>>()
        val renderPdfKall = mutableListOf<LetterMarkup>()

        override suspend fun renderMarkup(
            brevkode: Brevkode.Redigerbart,
            spraak: LanguageCode,
            brevdata: RedigerbarBrevdata<*, *>,
            felles: BrevbakerFelles
        ): LetterMarkupWithDataUsage =
            renderMarkupResultat(felles)
                .also { renderMarkupKall.add(Pair(brevkode, spraak)) }
                .let { LetterMarkupWithDataUsageImpl(it, emptySet(), if (brevkode == Testbrevkoder.VEDTAKSBREV) LetterMetadata.Brevtype.VEDTAKSBREV else LetterMetadata.Brevtype.INFORMASJONSBREV) }

        override suspend fun renderPdf(
            brevkode: Brevkode.Redigerbart,
            spraak: LanguageCode,
            brevdata: RedigerbarBrevdata<*, *>,
            felles: BrevbakerFelles,
            redigertBrev: LetterMarkup,
            alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>
        ) = renderPdfResultat.also { renderPdfKall.add(redigertBrev) }

        override suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart) = redigerbareMaler[brevkode]
        override suspend fun getAlltidValgbareVedlegg(brevId: BrevId) = notYetStubbed()

        override suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart) = modelSpecificationResultat
    }

    protected class FakePenClient(
        var saker: MutableMap<SaksId, Pen.SakSelection> = mutableMapOf(),
        var pesysBrevdata: BrevdataResponse.Data? = null,
        var sendBrevResponse: Pen.BestillBrevResponse? = null,
    ) : PenClientStub() {
        val utfoerteHentPesysBrevdataKall = mutableListOf<PesysBrevdatakallRequest>()

        data class PesysBrevdatakallRequest(
            val saksId: SaksId,
            val vedtaksId: VedtaksId?,
            val brevkode: Brevkode.Redigerbart,
            val avsenderEnhetsId: EnhetId?,
        )

        val utfoerteSendBrevKall = mutableListOf<Pair<Pen.SendRedigerbartBrevRequest, Boolean>>()

        override suspend fun hentSak(saksId: SaksId): Pen.SakSelection? = saker[saksId]

        override suspend fun hentPesysBrevdata(saksId: SaksId, vedtaksId: VedtaksId?, brevkode: Brevkode.Redigerbart, avsenderEnhetsId: EnhetId): BrevdataResponse.Data =
            pesysBrevdata.also {
                utfoerteHentPesysBrevdataKall.add(PesysBrevdatakallRequest(saksId, vedtaksId, brevkode, avsenderEnhetsId))
            } ?: notYetStubbed("Mangler pesysBrevdata stub")

        override suspend fun sendbrev(sendRedigerbartBrevRequest: Pen.SendRedigerbartBrevRequest, distribuer: Boolean) =
            sendBrevResponse?.also {
                utfoerteSendBrevKall.add(Pair(sendRedigerbartBrevRequest, distribuer))
            } ?: notYetStubbed("Mangler sendBrevResponse stub")

        fun verifyHentPesysBrevdata(
            saksId: SaksId,
            vedtaksId: VedtaksId?,
            brevkode: Brevkode.Redigerbart,
            avsenderEnhetsId: EnhetId?,
        ) {
            assertThat(utfoerteHentPesysBrevdataKall.distinct()).contains(PesysBrevdatakallRequest(saksId, vedtaksId, brevkode, avsenderEnhetsId))
        }

        fun verifySendBrev(
            sendRedigerbartBrevRequest: Pen.SendRedigerbartBrevRequest, distribuer: Boolean
        ) {
            assertThat(utfoerteSendBrevKall.distinct()).contains(Pair(sendRedigerbartBrevRequest, distribuer))
        }
    }
}