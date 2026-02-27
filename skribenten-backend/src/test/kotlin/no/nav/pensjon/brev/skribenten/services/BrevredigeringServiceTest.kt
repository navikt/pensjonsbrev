package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import kotlinx.coroutines.runBlocking
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
import no.nav.pensjon.brev.skribenten.db.kryptering.KrypteringService
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.serialize.Sakstype
import no.nav.pensjon.brev.skribenten.usecase.*
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.BrevFelles.Bruker
import no.nav.pensjon.brevbaker.api.model.BrevFelles.Felles
import no.nav.pensjon.brevbaker.api.model.BrevFelles.NavEnhet
import no.nav.pensjon.brevbaker.api.model.BrevFelles.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Pid
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Telefonnummer
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.SignaturImpl
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.LocalDate

@OptIn(InternKonstruktoer::class)
class BrevredigeringServiceTest {

    @BeforeAll
    fun initDb() {
        SharedPostgres.subscribeAndEnsureDatabaseInitialized(this)
    }

    @AfterAll
    fun kansellerDbAvhengighet() {
        SharedPostgres.cancelSubscription(this)
    }


    companion object {
        init {
            KrypteringService.init("ZBn9yGLDluLZVVGXKZxvnPun3kPQ2ccF")
            initADGroups()
        }
    }

    private val saksbehandler1Principal =
        MockPrincipal(NavIdent("Agent Smith"), "Hugo Weaving", setOf(ADGroups.pensjonSaksbehandler))
    private val saksbehandler2Principal =
        MockPrincipal(NavIdent("Morpheus"), "Laurence Fishburne", setOf(ADGroups.pensjonSaksbehandler))

    private val letter = letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "red pill"))))
        .medSignatur(saksbehandler = saksbehandler1Principal.fullName, attestant = null)

    private val stagetPDF = "nesten en pdf".encodeToByteArray()

    private val informasjonsbrev = TemplateDescription.Redigerbar(
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
    private val vedtaksbrev = TemplateDescription.Redigerbar(
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
        sakstyper = setOf(TemplateDescription.Redigerbar.Sakstype("S1"), TemplateDescription.Redigerbar.Sakstype("S2")),
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
    private val brevbakerService = BrevredigeringFakeBrevbakerService()

    class BrevredigeringFakeBrevbakerService : FakeBrevbakerService() {
        lateinit var renderMarkupResultat: suspend ((f: Felles) -> LetterMarkup)
        lateinit var renderPdfResultat: LetterResponse
        var modelSpecificationResultat: TemplateModelSpecification? = null
        override var redigerbareMaler: MutableMap<RedigerbarBrevkode, TemplateDescription.Redigerbar> = mutableMapOf()
        val renderMarkupKall = mutableListOf<Pair<Brevkode.Redigerbart, LanguageCode>>()
        val renderPdfKall = mutableListOf<LetterMarkup>()

        override suspend fun renderMarkup(
            brevkode: Brevkode.Redigerbart,
            spraak: LanguageCode,
            brevdata: RedigerbarBrevdata<*, *>,
            felles: Felles
        ): LetterMarkupWithDataUsage =
            renderMarkupResultat(felles)
                .also { renderMarkupKall.add(Pair(brevkode, spraak)) }
                .let { LetterMarkupWithDataUsageImpl(it, emptySet(), if (brevkode == Testbrevkoder.VEDTAKSBREV) LetterMetadata.Brevtype.VEDTAKSBREV else LetterMetadata.Brevtype.INFORMASJONSBREV) }

        override suspend fun renderPdf(
            brevkode: Brevkode.Redigerbart,
            spraak: LanguageCode,
            brevdata: RedigerbarBrevdata<*, *>,
            felles: Felles,
            redigertBrev: LetterMarkup,
            alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>
        ) = renderPdfResultat.also { renderPdfKall.add(redigertBrev) }

        override suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart) = redigerbareMaler[brevkode]
        override suspend fun getAlltidValgbareVedlegg(brevId: BrevId) = notYetStubbed()

        override suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart) = modelSpecificationResultat
    }

    private val principalNavEnhetId = EnhetId("9876")


    private val sak1 = Pen.SakSelection(
        saksId = SaksId(1234L),
        foedselsdato = LocalDate.now().minusYears(42),
        navn = Pen.SakSelection.Navn("a", "b", "c"),
        sakType = Sakstype("ALDER"),
        pid = Pid("12345678910")
    )

    private val brevdataResponseData = BrevdataResponse.Data(
        felles = Felles(
            dokumentDato = LocalDate.now(),
            saksnummer = sak1.saksId.toString(),
            avsenderEnhet =
                NavEnhet(
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

    private val penService = FakePenService()

    class FakePenService(
        var saker: MutableMap<SaksId, Pen.SakSelection> = mutableMapOf(),
        var pesysBrevdata: BrevdataResponse.Data? = null,
        var sendBrevResponse: Pen.BestillBrevResponse? = null,
    ) : PenServiceStub() {
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

    private val navAnsattService = FakeNavansattService(
        harTilgangTilEnhet = mapOf(
            Pair(saksbehandler1Principal.navIdent.id, principalNavEnhetId) to true,
            Pair(saksbehandler2Principal.navIdent.id, principalNavEnhetId) to true
        ),
        navansatte = mapOf(
            saksbehandler1Principal.navIdent.id to saksbehandler1Principal.fullName,
            saksbehandler2Principal.navIdent.id to saksbehandler2Principal.fullName
        )
    )

    private val brevredigeringService: BrevredigeringService = BrevredigeringService()

    private val brevredigeringFacade = BrevredigeringFacadeFactory.create(
        brevbakerService = brevbakerService,
        penService = penService,
        samhandlerService = FakeSamhandlerService(),
        navansattService = navAnsattService,
        p1Service = FakeP1Service()
    )

    private val bestillBrevresponse = Pen.BestillBrevResponse(JournalpostId(123), null)

    @BeforeEach
    fun clearMocks() {
        brevbakerService.renderMarkupResultat = {
            letter.medSignatur(
                saksbehandler = it.signerendeSaksbehandlere?.saksbehandler,
                attestant = it.signerendeSaksbehandlere?.attesterendeSaksbehandler
            )
        }

        brevbakerService.redigerbareMaler[Testbrevkoder.INFORMASJONSBREV] = informasjonsbrev
        brevbakerService.redigerbareMaler[Testbrevkoder.VEDTAKSBREV] = vedtaksbrev
        brevbakerService.redigerbareMaler[Testbrevkoder.VARSELBREV] = varselbrevIVedtakskontekst
        stagePdf(stagetPDF)

        penService.pesysBrevdata = brevdataResponseData
        penService.sendBrevResponse = bestillBrevresponse
    }


    @Test
    fun `can delete brevredigering`(): Unit = runBlocking {
        val brev = opprettBrev()

        assertEquals(
            brev.copy(propertyUsage = null),
            hentBrev(brev.info.id)
        )
        assertThat(brevredigeringService.slettBrev(saksId = sak1.saksId, brevId = brev.info.id)).isTrue()
        assertThat(hentBrev(brev.info.id)).isNull()
    }

    @Test
    fun `delete brevredigering returns false for non-existing brev`(): Unit = runBlocking {
        assertThat(hentBrev(brevId = BrevId(1337))).isNull()
        assertThat(brevredigeringService.slettBrev(saksId = sak1.saksId, brevId = BrevId(1337))).isFalse()
    }

    @Test
    fun `kan hente brev for flere saker`(): Unit = runBlocking {
        val sak2 = sak1.copy(saksId = SaksId(sak1.saksId.id + 1))
        val sak3 = sak1.copy(saksId = SaksId(sak2.saksId.id + 1))
        val forventedeBrev = listOf(
            opprettBrev(sak = sak1),
            opprettBrev(sak = sak1),
            opprettBrev(sak = sak2)
        ).map { it.info }.toSet()
        val ikkeForventetBrev = opprettBrev(sak = sak3).info

        val resultat = brevredigeringService.hentBrevForAlleSaker(setOf(sak1.saksId, sak2.saksId)).toSet()
        assertThat(resultat).containsAll(forventedeBrev)
        assertThat(resultat).doesNotContain(ikkeForventetBrev)
    }

    private suspend fun opprettBrev(
        principal: UserPrincipal = saksbehandler1Principal,
        reserverForRedigering: Boolean = false,
        mottaker: Dto.Mottaker? = null,
        saksbehandlerValg: SaksbehandlerValg = SaksbehandlerValg().apply { put("valg", true) },
        brevkode: Brevkode.Redigerbart = Testbrevkoder.INFORMASJONSBREV,
        vedtaksId: VedtaksId? = null,
        sak: Pen.SakSelection = sak1,
    ) = withPrincipal(principal) {
        val result = brevredigeringFacade.opprettBrev(
            OpprettBrevHandlerImpl.Request(
                saksId = sak.saksId,
                vedtaksId = vedtaksId,
                brevkode = brevkode,
                spraak = LanguageCode.ENGLISH,
                avsenderEnhetsId = principalNavEnhetId,
                saksbehandlerValg = saksbehandlerValg,
                reserverForRedigering = reserverForRedigering,
                mottaker = mottaker,
            )
        )
        when (result) {
            is Outcome.Failure -> error("Kunne ikke opprette brev: ${result.error}")
            is Outcome.Success -> result.value
        }
    }

    private suspend fun hentBrev(
        brevId: BrevId,
        reserverForRedigering: Boolean = false,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Dto.Brevredigering? {
        val result = withPrincipal(principal) {
            brevredigeringFacade.hentBrev(HentBrevHandler.Request(brevId = brevId, reserverForRedigering = reserverForRedigering))
        }
        return when (result) {
            is Outcome.Failure -> error("Kunne ikke hente brev: ${result.error}")
            is Outcome.Success -> result.value
            null -> null
        }
    }

    private fun stagePdf(pdf: ByteArray) {
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

    private fun LetterMarkupImpl.medSignatur(saksbehandler: String?, attestant: String?) =
        copy(
            signatur = (signatur as SignaturImpl).copy(
                saksbehandlerNavn = saksbehandler,
                attesterendeSaksbehandlerNavn = attestant
            )
        )
}
