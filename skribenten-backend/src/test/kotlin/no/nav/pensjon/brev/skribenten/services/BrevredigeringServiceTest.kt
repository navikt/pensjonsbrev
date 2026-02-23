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
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.serialize.Sakstype
import no.nav.pensjon.brev.skribenten.services.BrevredigeringException.*
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService.Companion.RESERVASJON_TIMEOUT
import no.nav.pensjon.brev.skribenten.usecase.*
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.SignaturImpl
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.NavEnhet
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.Instant
import java.time.LocalDate
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph as E_Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType as E_FontType
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal as E_Literal

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
    private val attestantPrincipal =
        MockPrincipal(NavIdent("A12345"), "Peder Ås", mutableSetOf(ADGroups.pensjonSaksbehandler, ADGroups.attestant))

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
    private val letterResponse =
        LetterResponse(file = stagetPDF, contentType = "pdf", letterMetadata = informasjonsbrev.metadata)

    private val brevbakerService = BrevredigeringFakeBrevbakerService()

    class BrevredigeringFakeBrevbakerService : FakeBrevbakerService() {
        lateinit var renderMarkupResultat: suspend ((f: Felles) -> LetterMarkup)
        lateinit var renderPdfResultat: LetterResponse
        lateinit var modelSpecificationResultat: TemplateModelSpecification
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
        foedselsnr = Foedselsnummer("12345678910"),
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

    private val brevredigeringService: BrevredigeringService = BrevredigeringService(
        brevbakerService = brevbakerService,
        navansattService = navAnsattService,
        penService = penService
    )

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
    fun `status er ARKIVERT om brev har journalpost`(): Unit = runBlocking {
        val brev = opprettBrev()
        transaction { BrevredigeringEntity[brev.info.id].journalpostId = JournalpostId(123L) }

        val oppdatertBrev = hentBrev(brev.info.id)
        assertThat(oppdatertBrev?.info?.status).isEqualTo(Dto.BrevStatus.ARKIVERT)
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
    fun `kan ikke distribuere vedtaksbrev som ikke er attestert`(): Unit = runBlocking {
        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) },
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1),
        )
        withPrincipal(saksbehandler1Principal) {
            assertThat(brevredigeringFacade.hentPDF(HentEllerOpprettPdfHandler.Request(brevId = brev.info.id))).isSuccess()
            assertThat(brevredigeringFacade.veksleKlarStatus(VeksleKlarStatusHandler.Request(brevId = brev.info.id, klar = true)))
                .isSuccess()

            assertThrows<BrevIkkeKlartTilSendingException> {
                brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)
            }
        }
    }

    @Test
    fun `kan distribuere vedtaksbrev som er attestert`(): Unit = runBlocking {
        brevbakerService.renderPdfKall.clear()

        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) },
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1),
        )
        withPrincipal(saksbehandler1Principal) {
            assertThat(brevredigeringFacade.veksleKlarStatus(VeksleKlarStatusHandler.Request(brevId = brev.info.id, klar = true)))
                .isSuccess()
        }

        withPrincipal(attestantPrincipal) {
            assertThat(brevredigeringFacade.attesterBrev(AttesterBrevHandler.Request(brev.info.id, frigiReservasjon = true))).isSuccess()
            assertThat(brevredigeringFacade.hentPDF(HentEllerOpprettPdfHandler.Request(brevId = brev.info.id))).isSuccess()
            assertThat(brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)).isEqualTo(bestillBrevresponse)
        }
        assertEquals(attestantPrincipal.fullName, brevbakerService.renderPdfKall.first().signatur.attesterendeSaksbehandlerNavn)
    }

    @Test
    fun `distribuerer sentralprint brev`(): Unit = runBlocking {
        penService.sendBrevResponse = Pen.BestillBrevResponse(JournalpostId(123), null)

        brevbakerService.renderPdfResultat = letterResponse
        brevbakerService.renderMarkupResultat = { letter }

        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) }
        )

        withPrincipal(saksbehandler1Principal) {
            assertThat(brevredigeringFacade.veksleKlarStatus(VeksleKlarStatusHandler.Request(brevId = brev.info.id, klar = true)))
                .isSuccess()
            assertThat(brevredigeringFacade.hentPDF(HentEllerOpprettPdfHandler.Request(brevId = brev.info.id))).isSuccess()
            brevredigeringService.sendBrev(sak1.saksId, brev.info.id)
        }

        penService.verifyHentPesysBrevdata(sak1.saksId, null, Testbrevkoder.INFORMASJONSBREV, principalNavEnhetId)

        penService.verifySendBrev(
            Pen.SendRedigerbartBrevRequest(
                templateDescription = informasjonsbrev,
                dokumentDato = LocalDate.now(),
                saksId = SaksId(1234),
                brevkode = Testbrevkoder.INFORMASJONSBREV,
                enhetId = principalNavEnhetId,
                pdf = stagetPDF,
                eksternReferanseId = "skribenten:${brev.info.id.id}",
                mottaker = null,
            ), true
        )
    }

    @Test
    fun `distribuerer ikke lokalprint brev`(): Unit = runBlocking {
        penService.sendBrevResponse = Pen.BestillBrevResponse(JournalpostId(123), null)

        brevbakerService.renderPdfResultat = letterResponse
        brevbakerService.renderMarkupResultat = { letter }

        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) }
        )

        withPrincipal(saksbehandler1Principal) {
            assertThat(brevredigeringFacade.veksleKlarStatus(VeksleKlarStatusHandler.Request(brevId = brev.info.id, klar = true)))
                .isSuccess()
            assertThat(
                brevredigeringFacade.endreDistribusjonstype(
                    EndreDistribusjonstypeHandler.Request(
                        brevId = brev.info.id,
                        type = Distribusjonstype.LOKALPRINT
                    )
                )
            ).isSuccess()
            assertThat(brevredigeringFacade.hentPDF(HentEllerOpprettPdfHandler.Request(brevId = brev.info.id))).isSuccess()
            brevredigeringService.sendBrev(sak1.saksId, brev.info.id)
        }

        penService.verifyHentPesysBrevdata(sak1.saksId, null, Testbrevkoder.INFORMASJONSBREV, principalNavEnhetId)

        penService.verifySendBrev(
            Pen.SendRedigerbartBrevRequest(
                templateDescription = informasjonsbrev,
                dokumentDato = LocalDate.now(),
                saksId = SaksId(1234),
                brevkode = Testbrevkoder.INFORMASJONSBREV,
                enhetId = principalNavEnhetId,
                pdf = stagetPDF,
                eksternReferanseId = "skribenten:${brev.info.id.id}",
                mottaker = null,
            ), false
        )
    }

    @Test
    fun `brev kan ikke endres om det er arkivert`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true)

        withPrincipal(saksbehandler1Principal) {
            assertThat(brevredigeringFacade.hentPDF(HentEllerOpprettPdfHandler.Request(brevId = brev.info.id))).isSuccess()

            assertThat(brevredigeringFacade.veksleKlarStatus(VeksleKlarStatusHandler.Request(brevId = brev.info.id, klar = true)))
                .isSuccess()
        }

        penService.sendBrevResponse = Pen.BestillBrevResponse(
            JournalpostId(991),
            Pen.BestillBrevResponse.Error(null, "Distribuering feilet", null)
        )

        brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)
        assertThat(hentBrev(brev.info.id)).isNotNull()

        withPrincipal(saksbehandler1Principal) {
            assertThrows<ArkivertBrevException> { brevredigeringService.tilbakestill(brev.info.id) }
        }
    }

    @Test
    fun `kan ikke sende brev som ikke er markert klar til sending`(): Unit = runBlocking {
        val brev = opprettBrev()
        withPrincipal(saksbehandler1Principal) {
            assertThat(brevredigeringFacade.hentPDF(HentEllerOpprettPdfHandler.Request(brevId = brev.info.id))).isSuccess()
        }

        assertThrows<BrevIkkeKlartTilSendingException> {
            brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)
        }
    }

    @Test
    suspend fun `kan ikke sende brev hvor pdf har annen hash enn siste brevredigering`() {
        val brev = opprettBrev()

        withPrincipal(saksbehandler1Principal) {
            assertThat(brevredigeringFacade.hentPDF(HentEllerOpprettPdfHandler.Request(brevId = brev.info.id))).isSuccess()
            brevredigeringFacade.oppdaterBrev(
                OppdaterBrevHandler.Request(
                    brevId = brev.info.id,
                    nyttRedigertbrev = brev.redigertBrev.withSignatur(saksbehandler = "en ny signatur")
                )
            )
            assertThat(brevredigeringFacade.veksleKlarStatus(VeksleKlarStatusHandler.Request(brevId = brev.info.id, klar = true)))
                .isSuccess()
        }
        // verifiser forskjellig hash
        transaction {
            val redigering = BrevredigeringEntity[brev.info.id]
            assertThat(redigering.redigertBrevHash).isNotEqualTo(redigering.document!!.redigertBrevHash)
        }

        withPrincipal(saksbehandler1Principal) {
            assertThrows<NyereVersjonFinsException> {
                brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)
            }
        }
    }

    @Test
    fun `arkivert brev men ikke distribuert kan sendes`(): Unit = runBlocking {
        val brev = opprettBrev()

        withPrincipal(saksbehandler1Principal) {
            assertThat(brevredigeringFacade.hentPDF(HentEllerOpprettPdfHandler.Request(brevId = brev.info.id))).isSuccess()
        }

        penService.sendBrevResponse = Pen.BestillBrevResponse(
            JournalpostId(991),
            Pen.BestillBrevResponse.Error(null, "Distribuering feilet", null)
        )

        assertThat(
            withPrincipal(saksbehandler1Principal) {
                brevredigeringFacade.veksleKlarStatus(VeksleKlarStatusHandler.Request(brevId = brev.info.id, klar = true))
            }
        ).isSuccess()

        brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)
        assertThat(hentBrev(brev.info.id)).isNotNull()

        penService.sendBrevResponse = Pen.BestillBrevResponse(JournalpostId(991), null)

        brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)

        assertThat(transaction { BrevredigeringEntity.findById(brev.info.id) }).isNull()
    }

    @Test
    fun `brev reservasjon utloeper`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true)

        transaction {
            BrevredigeringEntity[brev.info.id].sistReservert =
                Instant.now() - RESERVASJON_TIMEOUT - 1.seconds.toJavaDuration()
        }

        val hentetBrev = hentBrev(brev.info.id)

        assertThat(hentetBrev?.info?.redigeresAv).isNull()

        val hentetBrevMedReservasjon = hentBrev(
            brevId = brev.info.id,
            reserverForRedigering = true,
            principal = saksbehandler2Principal,
        )
        assertThat(hentetBrevMedReservasjon?.info?.redigeresAv).isEqualTo(saksbehandler2Principal.navIdent)
    }

    @Test
    fun `brev distribueres til annen mottaker`(): Unit = runBlocking {
        val mottaker = Dto.Mottaker.samhandler("987")
        val brev = opprettBrev(mottaker = mottaker)
        withPrincipal(saksbehandler1Principal) {
            assertThat(brevredigeringFacade.hentPDF(HentEllerOpprettPdfHandler.Request(brevId = brev.info.id))).isSuccess()
            assertThat(brevredigeringFacade.veksleKlarStatus(VeksleKlarStatusHandler.Request(brevId = brev.info.id, klar = true)))
                .isSuccess()
        }

        brevredigeringService.sendBrev(sak1.saksId, brev.info.id)

        penService.verifySendBrev(
            Pen.SendRedigerbartBrevRequest(
                templateDescription = informasjonsbrev,
                dokumentDato = LocalDate.now(),
                saksId = sak1.saksId,
                brevkode = Testbrevkoder.INFORMASJONSBREV,
                enhetId = principalNavEnhetId,
                pdf = stagetPDF,
                eksternReferanseId = "skribenten:${brev.info.id.id}",
                mottaker = Pen.SendRedigerbartBrevRequest.Mottaker(
                    Pen.SendRedigerbartBrevRequest.Mottaker.Type.TSS_ID,
                    mottaker.tssId,
                    null,
                    null
                )
            ), true
        )
    }

    @Test
    fun `kan tilbakestille brev`(): Unit = runBlocking {
        val brev = opprettBrev(saksbehandlerValg = Api.GeneriskBrevdata().apply {
            put("ytelse", "uføre")
            put("inkluderAfpTekst", false)
        })

        withPrincipal(saksbehandler1Principal) {
            brevredigeringFacade.oppdaterBrev(
                OppdaterBrevHandler.Request(
                    brevId = brev.info.id,
                    nyeSaksbehandlerValg = Api.GeneriskBrevdata().apply {
                        put("ytelse", "uføre")
                        put("inkluderAfpTekst", true)
                        put("land", "Spania")
                    },
                    nyttRedigertbrev = brev.redigertBrev.copy(
                        blocks = brev.redigertBrev.blocks + E_Paragraph(
                            null,
                            true,
                            listOf(E_Literal(null, "", E_FontType.PLAIN, "and blue pill"))
                        )
                    ),
                )
            )
        }

        brevbakerService.modelSpecificationResultat = TemplateModelSpecification(
            types = mapOf(
                "BrevData1" to mapOf(
                    "saksbehandlerValg" to FieldType.Object(false, "SaksbehandlerValg1"),
                ),
                "SaksbehandlerValg1" to mapOf(
                    "ytelse" to FieldType.Scalar(false, FieldType.Scalar.Kind.STRING),
                    "land" to FieldType.Scalar(true, FieldType.Scalar.Kind.STRING),
                    "inkluderAfpTekst" to FieldType.Scalar(false, FieldType.Scalar.Kind.BOOLEAN),
                ),
            ),
            letterModelTypeName = "BrevData1",
        )

        val tilbakestilt = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.tilbakestill(brev.info.id)
        }
        assertThat(tilbakestilt?.redigertBrev).isEqualTo(letter.toEdit())
        assertThat(tilbakestilt?.saksbehandlerValg).isEqualTo(Api.GeneriskBrevdata().apply {
            put("ytelse", "uføre")
            put("inkluderAfpTekst", false)
            put("land", null)
        })
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
