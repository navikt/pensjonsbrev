package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import kotlinx.coroutines.*
import no.nav.brev.Landkode
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.copy
import no.nav.pensjon.brev.skribenten.db.*
import no.nav.pensjon.brev.skribenten.db.kryptering.KrypteringService
import no.nav.pensjon.brev.skribenten.domain.Brevredigering
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.initADGroups
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype.SENTRALPRINT
import no.nav.pensjon.brev.skribenten.services.BrevredigeringException.*
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService.Companion.RESERVASJON_TIMEOUT
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.usecase.UpdateLetterHandler
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.SignaturImpl
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.NavEnhet
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.testcontainers.postgresql.PostgreSQLContainer
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.function.Predicate
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph as E_Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType as E_FontType
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal as E_Literal

class BrevredigeringServiceTest {
    private val postgres = PostgreSQLContainer("postgres:17-alpine")

    init {
        KrypteringService.init("ZBn9yGLDluLZVVGXKZxvnPun3kPQ2ccF")
        initADGroups()
    }

    @BeforeAll
    fun startDb() {
        postgres.start()
        initDatabase(postgres.jdbcUrl, postgres.username, postgres.password)
    }

    @AfterAll
    fun stopDb() {
        postgres.stop()
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
        kategori = TemplateDescription.Brevkategori.INFORMASJONSBREV,
        brevkontekst = TemplateDescription.Brevkontekst.ALLE,
        sakstyper = Sakstype.all,
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
        kategori = TemplateDescription.Brevkategori.UFOEREPENSJON,
        brevkontekst = TemplateDescription.Brevkontekst.ALLE,
        sakstyper = Sakstype.all,
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
                .let { LetterMarkupWithDataUsageImpl(it, emptySet()) }

        override suspend fun renderPdf(
            brevkode: Brevkode.Redigerbart,
            spraak: LanguageCode,
            brevdata: RedigerbarBrevdata<*, *>,
            felles: Felles,
            redigertBrev: LetterMarkup,
            alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>
        ) = renderPdfResultat.also { renderPdfKall.add(redigertBrev) }

        override suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart) = redigerbareMaler[brevkode]
        override suspend fun getAlltidValgbareVedlegg(brevId: Long) = notYetStubbed()

        override suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart) = modelSpecificationResultat
    }

    private val principalNavEnhetId = "Nebuchadnezzar"


    private val sak1 = Pen.SakSelection(
        saksId = 1234L,
        foedselsnr = "12345678910",
        foedselsdato = LocalDate.now().minusYears(42),
        navn = Pen.SakSelection.Navn("a", "b", "c"),
        sakType = Pen.SakType.ALDER,
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
        var saker: MutableMap<String, Pen.SakSelection> = mutableMapOf(),
        var pesysBrevdata: BrevdataResponse.Data? = null,
        var sendBrevResponse: Pen.BestillBrevResponse? = null,
    ) : PenServiceStub() {
        val utfoerteHentPesysBrevdataKall = mutableListOf<PesysBrevdatakallRequest>()

        data class PesysBrevdatakallRequest(
            val saksId: Long,
            val vedtaksId: Long?,
            val brevkode: Brevkode.Redigerbart,
            val avsenderEnhetsId: String?,
        )

        val utfoerteSendBrevKall = mutableListOf<Pair<Pen.SendRedigerbartBrevRequest, Boolean>>()

        override suspend fun hentSak(saksId: String): Pen.SakSelection? = saker[saksId]

        override suspend fun hentPesysBrevdata(saksId: Long, vedtaksId: Long?, brevkode: Brevkode.Redigerbart, avsenderEnhetsId: String?): BrevdataResponse.Data =
            pesysBrevdata.also {
                utfoerteHentPesysBrevdataKall.add(PesysBrevdatakallRequest(saksId, vedtaksId, brevkode, avsenderEnhetsId))
            } ?: notYetStubbed("Mangler pesysBrevdata stub")

        override suspend fun sendbrev(sendRedigerbartBrevRequest: Pen.SendRedigerbartBrevRequest, distribuer: Boolean) =
            sendBrevResponse?.also {
                utfoerteSendBrevKall.add(Pair(sendRedigerbartBrevRequest, distribuer))
            } ?: notYetStubbed("Mangler sendBrevResponse stub")

        fun verifyHentPesysBrevdata(
            saksId: Long,
            vedtaksId: Long?,
            brevkode: Brevkode.Redigerbart,
            avsenderEnhetsId: String?,
        ) {
            assertContains(utfoerteHentPesysBrevdataKall.distinct(), PesysBrevdatakallRequest(saksId, vedtaksId, brevkode, avsenderEnhetsId))
        }

        fun verifySendBrev(
            sendRedigerbartBrevRequest: Pen.SendRedigerbartBrevRequest, distribuer: Boolean
        ) {
            assertContains(utfoerteSendBrevKall.distinct(), Pair(sendRedigerbartBrevRequest, distribuer))
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
        penService = penService,
        samhandlerService = FakeSamhandlerService(),
        p1Service = FakeP1Service()
    )

    private val brevredigeringFacade = BrevredigeringFacade(
        renderService = RenderService(brevbakerService),
        brevdataService = BrevdataService(penService),
        redigerBrevPolicy = RedigerBrevPolicy(),
        brevreservasjonPolicy = BrevreservasjonPolicy(),
    )

    private val bestillBrevresponse = Pen.BestillBrevResponse(123, null)

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
        stagePdf(stagetPDF)

        penService.pesysBrevdata = brevdataResponseData
        penService.sendBrevResponse = bestillBrevresponse
    }

    @Test
    fun `non existing brevredingering returns null`(): Unit = runBlocking {
        assertThat(brevredigeringService.hentBrev(saksId = sak1.saksId, brevId = 99)).isNull()
    }

    @Test
    fun `can create and fetch brevredigering`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val brev = opprettBrev(reserverForRedigering = true, saksbehandlerValg = saksbehandlerValg)

        assertEquals(brevbakerService.renderMarkupKall.first(), Pair(Testbrevkoder.INFORMASJONSBREV, LanguageCode.ENGLISH))
        brevbakerService.renderMarkupKall.clear()

        assertEquals(
            brev.copy(info = brev.info.copy(sistReservert = null)),
            withPrincipal(saksbehandler1Principal) {
                brevredigeringService.hentBrev(
                    saksId = sak1.saksId,
                    brevId = brev.info.id,
                    reserverForRedigering = true,
                )?.let { it.copy(info = it.info.copy(sistReservert = null)) }
            }
        )
        assertThat(brev.info.brevkode.kode()).isEqualTo(Testbrevkoder.INFORMASJONSBREV.kode())
        assertThat(brev.redigertBrev).isEqualTo(letter.toEdit())

        assertEquals(brevbakerService.renderMarkupKall.first(), Pair(Testbrevkoder.INFORMASJONSBREV, LanguageCode.ENGLISH))
    }

    @Test
    fun `can create brev for vedtak`(): Unit = runBlocking {
        val vedtaksId: Long = 5678

        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = vedtaksId)
        assertThat(brev.info.vedtaksId).isEqualTo(vedtaksId)
        penService.verifyHentPesysBrevdata(sak1.saksId, vedtaksId, Testbrevkoder.VEDTAKSBREV, principalNavEnhetId)

        val hentet = brevredigeringService.hentBrev(brev.info.saksId, brev.info.id)
        assertThat(hentet?.info?.vedtaksId).isEqualTo(vedtaksId)
        penService.verifyHentPesysBrevdata(sak1.saksId, vedtaksId, Testbrevkoder.VEDTAKSBREV, principalNavEnhetId)
    }

    @Test
    fun `brev must belong to provided saksId`(): Unit = runBlocking {
        val brev = opprettBrev()
        withPrincipal(saksbehandler1Principal) {
            assertThat(
                brevredigeringService.hentBrev(
                    saksId = sak1.saksId + 1,
                    brevId = brev.info.id
                )
            ).isNull()
            assertThat(
                brevredigeringService.delvisOppdaterBrev(
                    saksId = sak1.saksId + 1,
                    brevId = brev.info.id,
                    laastForRedigering = true,
                )
            ).isNull()
            assertThat(
                brevredigeringService.hentEllerOpprettPdf(
                    saksId = sak1.saksId + 1,
                    brevId = brev.info.id,
                )
            ).isNull()

            // Må generere pdf nå, slik at sendBrev ikke returnerer null pga. manglende dokument
            brevredigeringService.hentEllerOpprettPdf(
                saksId = sak1.saksId,
                brevId = brev.info.id,
            )
            assertThat(
                brevredigeringService.sendBrev(
                    saksId = sak1.saksId + 1,
                    brevId = brev.info.id
                )
            ).isNull()
            assertThat(brevredigeringService.slettBrev(saksId = sak1.saksId + 1, brevId = brev.info.id)).isFalse()
        }
    }

    @Test
    fun `vedtaksbrev maa ha vedtaksId`(): Unit = runBlocking {
        assertThrows<VedtaksbrevKreverVedtaksId> {
            opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV)
        }
    }

    @Test
    fun `status er KLADD for et nytt brev`(): Unit = runBlocking {
        val brev = opprettBrev()

        assertThat(brev.info.status).isEqualTo(Dto.BrevStatus.KLADD)
    }

    @Test
    fun `status er KLAR om brev er laast`(): Unit = runBlocking {
        val brev = opprettBrev()
        val brevEtterLaas = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(
                saksId = brev.info.saksId,
                brevId = brev.info.id,
                laastForRedigering = true
            )!!
        }

        assertThat(brevEtterLaas.info.status).isEqualTo(Dto.BrevStatus.KLAR)
    }

    @Test
    fun `status er ATTESTERING om vedtaksbrev er laast`(): Unit = runBlocking {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = 1)

        val brevEtterLaas = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(
                saksId = brev.info.saksId,
                brevId = brev.info.id,
                laastForRedigering = true
            )!!
        }
        assertThat(brevEtterLaas.info.status).isEqualTo(Dto.BrevStatus.ATTESTERING)
    }

    @Test
    fun `status er KLAR om vedtaksbrev er laast og det er attestert`(): Unit = runBlocking {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = 1)

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(
                saksId = brev.info.saksId,
                brevId = brev.info.id,
                laastForRedigering = true
            )!!
        }
        val brevEtterAttestering = withPrincipal(attestantPrincipal) {
            brevredigeringService.attester(saksId = brev.info.saksId, brevId = brev.info.id, null, null)
        }
        assertThat(brevEtterAttestering?.info?.status).isEqualTo(Dto.BrevStatus.KLAR)
    }

    @Test
    fun `attestering fjernes om brevet laases opp igjen`(): Unit = runBlocking {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = 1)

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(
                saksId = brev.info.saksId,
                brevId = brev.info.id,
                laastForRedigering = true
            )!!
        }
        withPrincipal(attestantPrincipal) {
            brevredigeringService.attester(saksId = brev.info.saksId, brevId = brev.info.id, null, null, frigiReservasjon = true)
        }
        withPrincipal(saksbehandler1Principal) {
            val brevEtterOpplaasing = brevredigeringService.delvisOppdaterBrev(
                saksId = brev.info.saksId,
                brevId = brev.info.id,
                laastForRedigering = false
            )!!
            assertThat(brevEtterOpplaasing.info.status).isEqualTo(Dto.BrevStatus.KLADD)
            val brevEtterLaasingIgjen = brevredigeringService.delvisOppdaterBrev(
                saksId = brev.info.saksId,
                brevId = brev.info.id,
                laastForRedigering = true
            )!!
            assertThat(brevEtterLaasingIgjen.info.status).isEqualTo(Dto.BrevStatus.ATTESTERING)
        }
        assertThat(transaction { Brevredigering[brev.info.id].attestertAvNavIdent }).isNull()
    }

    @Test
    fun `status er ARKIVERT om brev har journalpost`(): Unit = runBlocking {
        val brev = opprettBrev()
        transaction { Brevredigering[brev.info.id].journalpostId = 123L }

        val oppdatertBrev = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentBrev(brev.info.saksId, brev.info.id)
        }
        assertThat(oppdatertBrev?.info?.status).isEqualTo(Dto.BrevStatus.ARKIVERT)
    }

    @Test
    fun `cannot create brevredigering for a NavEnhet without access to it`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        assertThrows<IkkeTilgangTilEnhetException> {
            withPrincipal(saksbehandler1Principal) {
                brevredigeringService.opprettBrev(
                    sak = sak1,
                    vedtaksId = null,
                    brevkode = Testbrevkoder.INFORMASJONSBREV,
                    spraak = LanguageCode.ENGLISH,
                    avsenderEnhetsId = "The Matrix",
                    saksbehandlerValg = saksbehandlerValg
                )
            }
        }
    }

    @Test
    fun `can delete brevredigering`(): Unit = runBlocking {
        val brev = opprettBrev()

        assertEquals(
            brev.copy(propertyUsage = null),
            brevredigeringService.hentBrev(saksId = sak1.saksId, brevId = brev.info.id)
        )
        assertThat(brevredigeringService.slettBrev(saksId = sak1.saksId, brevId = brev.info.id)).isTrue()
        assertThat(brevredigeringService.hentBrev(saksId = sak1.saksId, brevId = brev.info.id)).isNull()
    }

    @Test
    fun `delete brevredigering returns false for non-existing brev`(): Unit = runBlocking {
        assertThat(brevredigeringService.hentBrev(saksId = sak1.saksId, brevId = 1337)).isNull()
        assertThat(brevredigeringService.slettBrev(saksId = sak1.saksId, brevId = 1337)).isFalse()
    }

    @Test
    fun `hentPdf skal opprette et Document med referanse til Brevredigering`(): Unit = runBlocking {
        val brev = opprettBrev()

        transaction {
            assertThat(Brevredigering[brev.info.id].document).isEmpty()
            assertThat(Document.find { DocumentTable.brevredigering.eq(brev.info.id) }).isEmpty()
        }

        assertThat(
            withPrincipal(saksbehandler1Principal) {
                brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)
            }
        ).isEqualTo(Api.PdfResponse(pdf = stagetPDF, rendretBrevErEndret = false))

        transaction {
            val brevredigering = Brevredigering[brev.info.id]
            assertThat(brevredigering.document).hasSize(1)
            assertThat(Document.find { DocumentTable.brevredigering.eq(brev.info.id) }).hasSize(1)
            assertThat(brevredigering.document.first().pdf).isEqualTo(stagetPDF)
        }
    }

    @Test
    fun `sletter relaterte documents ved sletting av brevredigering`(): Unit = runBlocking {
        val brev = opprettBrev()

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)
        }
        transaction { assertThat(Document.find { DocumentTable.brevredigering eq brev.info.id }).hasSize(1) }

        brevredigeringService.slettBrev(saksId = sak1.saksId, brevId = brev.info.id)
        transaction {
            assertThat(Brevredigering.findById(brev.info.id)).isNull()
            assertThat(Document.find { DocumentTable.brevredigering eq brev.info.id }).hasSize(0)
        }
    }

    @Test
    fun `dobbel oppretting av pdf er ikke mulig`(): Unit = runBlocking {
        val brev = opprettBrev()

        awaitAll(*(0..<10).map {
            async(Dispatchers.IO) {
                withPrincipal(saksbehandler1Principal) {
                    brevredigeringService.hentEllerOpprettPdf(
                        sak1.saksId,
                        brev.info.id,
                    )
                }
            }
        }.toTypedArray())
        transaction {
            assertThat(Document.find { DocumentTable.brevredigering eq brev.info.id }).hasSize(1)
        }
    }

    @Test
    fun `Document redigertBrevHash er stabil`(): Unit = runBlocking {
        val brev = opprettBrev()

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)
            val firstHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

            transaction { Brevredigering[brev.info.id].document.first().delete() }
            brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)
            val secondHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

            assertThat(firstHash).isEqualTo(secondHash)
        }
    }

    @Test
    fun `Document redigertBrevHash endres basert paa redigertBrev`(): Unit = runBlocking {
        val brev = opprettBrev()

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)
        }
        val firstHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

        transaction {
            Brevredigering[brev.info.id].redigertBrev =
                letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "blue pill")))).toEdit()
        }
        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)
        }
        val secondHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

        assertThat(firstHash).isNotEqualTo(secondHash)
    }

    @Test
    fun `hentPdf rendrer ny pdf om den ikke eksisterer`(): Unit = runBlocking {
        val brev = opprettBrev()

        withPrincipal(saksbehandler1Principal) {
            val response = brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)
            assertThat(response).isEqualTo(Api.PdfResponse(stagetPDF, rendretBrevErEndret = false))
        }
    }

    @Test
    fun `hentPdf rendrer ny pdf om den ikke er basert paa gjeldende redigertBrev`(): Unit = runBlocking {
        val brev = opprettBrev()
        stagePdf("min første pdf".encodeToByteArray())

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)

            transaction {
                Brevredigering[brev.info.id].redigertBrev =
                    letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "blue pill")))).toEdit()
            }

            stagePdf("min andre pdf".encodeToByteArray())
            val pdf = brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)

            assertThat(pdf).isEqualTo(Api.PdfResponse("min andre pdf".encodeToByteArray(), rendretBrevErEndret = true))
        }
    }

    @Test
    fun `hentPdf rendrer ny pdf om pesysdata er endra`(): Unit = runBlocking {
        val brev = opprettBrev()
        withPrincipal(saksbehandler1Principal) {
            stagePdf("min første pdf".encodeToByteArray())
            val first = brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)

            stagePdf("min andre pdf".encodeToByteArray())
            penService.pesysBrevdata = brevdataResponseData.copy(brevdata = Api.GeneriskBrevdata().also { it["a"] = "b" })
            val second = brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)

            assertThat(first).isNotEqualTo(second)
            assertThat(second).isEqualTo(Api.PdfResponse(pdf = "min andre pdf".encodeToByteArray(), rendretBrevErEndret = false))
        }
    }

    @Test
    fun `hentPdf informerer om at rendretBrev er endret pga pesysdata`(): Unit = runBlocking {
        val brev = opprettBrev()
        withPrincipal(saksbehandler1Principal) {
            stagePdf("min første pdf".encodeToByteArray())
            val first = brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)

            stagePdf("min andre pdf".encodeToByteArray())
            brevbakerService.renderMarkupResultat = {
                letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "red pill"), LiteralImpl(99, "new text"))))
                    .medSignatur(
                        saksbehandler = it.signerendeSaksbehandlere?.saksbehandler,
                        attestant = it.signerendeSaksbehandlere?.attesterendeSaksbehandler
                    )
            }
            penService.pesysBrevdata =
                brevdataResponseData.copy(brevdata = Api.GeneriskBrevdata().also { it["a"] = "b" })
            val second = brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)

            assertThat(first).isNotEqualTo(second)
            assertThat(second).isEqualTo(Api.PdfResponse(pdf = "min andre pdf".encodeToByteArray(), rendretBrevErEndret = true))
        }
    }

    @Test
    fun `hentPdf rendrer ny pdf om dokumentdato er endra`(): Unit = runBlocking {
        val brev = opprettBrev()
        withPrincipal(saksbehandler1Principal) {
            stagePdf("min første pdf".encodeToByteArray())
            brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)

            stagePdf("min andre pdf".encodeToByteArray())
            penService.pesysBrevdata = brevdataResponseData.copy(
                felles = brevdataResponseData.felles.copy(
                    dokumentDato = LocalDate.now().plusDays(2),
                    saksnummer = sak1.saksId.toString(),
                )
            )
            val second = brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)

            assertThat(brevbakerService.renderPdfKall.last().sakspart.dokumentDato).isEqualTo(penService.pesysBrevdata!!.felles.dokumentDato)
            assertThat(second).isEqualTo(Api.PdfResponse(pdf = "min andre pdf".encodeToByteArray(), rendretBrevErEndret = false))
        }
    }

    @Test
    fun `hentPdf rendrer ikke ny pdf om den er basert paa gjeldende redigertBrev`(): Unit = runBlocking {
        val brev = opprettBrev()
        withPrincipal(saksbehandler1Principal) {
            stagePdf("min første pdf".encodeToByteArray())
            val first = brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)

            stagePdf("min andre pdf".encodeToByteArray())
            val second = brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)

            assertThat(first).isEqualTo(second)
        }
    }

    @Test
    fun `attesterer hvis avsender har attestantrolle`(): Unit = runBlocking {
        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) },
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = 1,
        )

        val attesteringsResultat = withPrincipal(attestantPrincipal) {
            val oppdatert = brevredigeringService.oppdaterSignaturAttestant(brev.info.id, "Lars Holm")
            assertEquals(
                "Lars Holm",
                oppdatert?.redigertBrev?.signatur?.attesterendeSaksbehandlerNavn,
            )
            brevredigeringService.delvisOppdaterBrev(
                saksId = sak1.saksId,
                brevId = brev.info.id,
                laastForRedigering = true,
                distribusjonstype = SENTRALPRINT
            )
            brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)!!
            brevredigeringService.attester(sak1.saksId, brev.info.id, null, null, true)
        }
        assertThat(attesteringsResultat?.info?.attestertAv).isEqualTo(attestantPrincipal.navIdent)

        penService.verifyHentPesysBrevdata(sak1.saksId, 1, Testbrevkoder.VEDTAKSBREV, principalNavEnhetId)
    }

    @Test
    fun `attesterer ikke hvis avsender ikke har attestantrolle`(): Unit = runBlocking {
        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) },
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = 1,
        )

        withPrincipal(MockPrincipal(NavIdent("A12345"), "Peder Ås", mutableSetOf())) {
            brevredigeringService.oppdaterSignaturAttestant(brev.info.id, "Lars Holm")
            brevredigeringService.delvisOppdaterBrev(
                saksId = sak1.saksId,
                brevId = brev.info.id,
                laastForRedigering = true,
                distribusjonstype = SENTRALPRINT
            )
            brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)!!
            assertThrows<HarIkkeAttestantrolleException> {
                brevredigeringService.attester(sak1.saksId, brev.info.id, null, null, true)
            }
        }

        penService.verifyHentPesysBrevdata(sak1.saksId, 1, Testbrevkoder.VEDTAKSBREV, principalNavEnhetId)
    }

    @Test
    fun `kan ikke distribuere vedtaksbrev som ikke er attestert`(): Unit = runBlocking {
        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) },
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = 1,
        )
        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentEllerOpprettPdf(brev.info.saksId, brev.info.id)
            brevredigeringService.delvisOppdaterBrev(
                brev.info.saksId,
                brev.info.id,
                laastForRedigering = true,
                distribusjonstype = SENTRALPRINT
            )

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
            vedtaksId = 1,
        )
        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(
                brev.info.saksId,
                brev.info.id,
                laastForRedigering = true,
                distribusjonstype = SENTRALPRINT
            )
        }

        withPrincipal(attestantPrincipal) {
            brevredigeringService.attester(sak1.saksId, brev.info.id, null, null, true)
            brevredigeringService.hentEllerOpprettPdf(brev.info.saksId, brev.info.id)
            assertThat(brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)).isEqualTo(bestillBrevresponse)
        }
        assertEquals(brevbakerService.renderPdfKall.first().signatur.attesterendeSaksbehandlerNavn, attestantPrincipal.fullName)
    }

    @Test
    fun `distribuerer sentralprint brev`(): Unit = runBlocking {
        penService.sendBrevResponse = Pen.BestillBrevResponse(123, null)

        brevbakerService.renderPdfResultat = letterResponse
        brevbakerService.renderMarkupResultat = { letter }

        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) }
        )

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(
                saksId = sak1.saksId,
                brevId = brev.info.id,
                laastForRedigering = true,
                distribusjonstype = SENTRALPRINT
            )
            brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)!!
            brevredigeringService.sendBrev(sak1.saksId, brev.info.id)
        }

        penService.verifyHentPesysBrevdata(sak1.saksId, null, Testbrevkoder.INFORMASJONSBREV, principalNavEnhetId)

        penService.verifySendBrev(
            Pen.SendRedigerbartBrevRequest(
                templateDescription = informasjonsbrev,
                dokumentDato = LocalDate.now(),
                saksId = 1234,
                brevkode = Testbrevkoder.INFORMASJONSBREV,
                enhetId = principalNavEnhetId,
                pdf = stagetPDF,
                eksternReferanseId = "skribenten:${brev.info.id}",
                mottaker = null,
            ), true
        )
    }

    @Test
    fun `distribuerer ikke lokalprint brev`(): Unit = runBlocking {
        penService.sendBrevResponse = Pen.BestillBrevResponse(123, null)

        brevbakerService.renderPdfResultat = letterResponse
        brevbakerService.renderMarkupResultat = { letter }

        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) }
        )

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(
                saksId = sak1.saksId,
                brevId = brev.info.id,
                laastForRedigering = true,
                distribusjonstype = Distribusjonstype.LOKALPRINT,
            )
            brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)!!
            brevredigeringService.sendBrev(sak1.saksId, brev.info.id)
        }

        penService.verifyHentPesysBrevdata(sak1.saksId, null, Testbrevkoder.INFORMASJONSBREV, principalNavEnhetId)

        penService.verifySendBrev(
            Pen.SendRedigerbartBrevRequest(
                templateDescription = informasjonsbrev,
                dokumentDato = LocalDate.now(),
                saksId = 1234,
                brevkode = Testbrevkoder.INFORMASJONSBREV,
                enhetId = principalNavEnhetId,
                pdf = stagetPDF,
                eksternReferanseId = "skribenten:${brev.info.id}",
                mottaker = null,
            ), false
        )
    }

    @Test
    fun `brev kan reserveres for redigering gjennom opprett brev`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true)

        assertThat(brev.info.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent)
        assertThat(brev.info.sistReservert).isBetween(Instant.now() - 10.minutes.toJavaDuration(), Instant.now())
        assertThat(transaction { Brevredigering[brev.info.id].redigeresAv }).isEqualTo(saksbehandler1Principal.navIdent)
    }

    @Test
    fun `brev kan reserveres for redigering gjennom hent brev`(): Unit = runBlocking {
        val brev = opprettBrev()

        assertThat(brev.info.laastForRedigering).isFalse()
        assertThat(brev.info.redigeresAv).isNull()

        val hentetBrev = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentBrev(
                saksId = sak1.saksId,
                brevId = brev.info.id,
                reserverForRedigering = true
            )
        }

        assertThat(hentetBrev?.info?.laastForRedigering).isFalse()
        assertThat(hentetBrev?.info?.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent)
    }

    @Test
    fun `allerede reservert brev kan ikke resereveres for redigering`() {
        runBlocking {
            val brev = opprettBrev(reserverForRedigering = true)

            assertThrows<KanIkkeReservereBrevredigeringException> {
                withPrincipal(saksbehandler2Principal) {
                    brevredigeringService.hentBrev(
                        saksId = sak1.saksId,
                        brevId = brev.info.id,
                        reserverForRedigering = true
                    )
                }
            }
            assertThat(transaction { Brevredigering[brev.info.id].redigeresAv }).isEqualTo(
                saksbehandler1Principal.navIdent
            )
        }
    }

    @Test
    fun `kun en som vinner reservasjon av et brev`() {
        runBlocking {
            val brev = opprettBrev()

            brevbakerService.renderMarkupResultat = {
                delay(100)
                letter
            }

            val hentBrev = (0..10).map {
                async(Dispatchers.IO) {
                    runCatching {
                        withPrincipal(MockPrincipal(NavIdent("id-$it"), "saksbehandler-id-$it")) {
                            brevredigeringService.hentBrev(
                                saksId = sak1.saksId,
                                brevId = brev.info.id,
                                reserverForRedigering = true
                            )
                        }
                    }
                }
            }
            val awaited = hentBrev.awaitAll()
            val redigeresFaktiskAv = transaction { Brevredigering[brev.info.id].redigeresAv }!!

            assertThat(awaited).areExactly(1, condition("Vellykkede hentBrev med reservasjon") { it.isSuccess })
            assertThat(awaited).areExactly(
                awaited.size - 1,
                condition("Feilende hentBrev med reservasjon") { it.isFailure })
            assertThat(awaited).allMatch {
                it.isFailure || it.getOrNull()?.info?.redigeresAv == redigeresFaktiskAv
            }
        }
    }

    @Test
    fun `brev kan ikke endres om det er arkivert`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true)

        withPrincipal(saksbehandler1Principal) {
            val pdf = brevredigeringService.hentEllerOpprettPdf(brev.info.saksId, brev.info.id)
            assertThat(pdf).isNotNull()
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)
        }

        penService.sendBrevResponse = Pen.BestillBrevResponse(
            991,
            Pen.BestillBrevResponse.Error(null, "Distribuering feilet", null)
        )

        brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)
        assertThat(brevredigeringService.hentBrev(brev.info.saksId, brev.info.id)).isNotNull()

        withPrincipal(saksbehandler1Principal) {
            assertThrows<ArkivertBrevException> {
                brevredigeringService.delvisOppdaterBrev(
                    brev.info.saksId,
                    brev.info.id,
                    false,
                    Distribusjonstype.LOKALPRINT
                )
            }
            assertThrows<ArkivertBrevException> { brevredigeringService.oppdaterSignatur(brev.info.id, "ny signatur") }
            assertThrows<ArkivertBrevException> { brevredigeringService.tilbakestill(brev.info.id) }
        }
    }

    @Test
    fun `kan ikke sende brev som ikke er markert klar til sending`(): Unit = runBlocking {
        val brev = opprettBrev()
        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentEllerOpprettPdf(brev.info.saksId, brev.info.id)
        }

        assertThrows<BrevIkkeKlartTilSendingException> {
            brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)
        }
    }

    @Test
    fun `kan ikke sende brev hvor pdf har annen hash enn siste brevredigering`() {
        runBlocking {
            val brev = opprettBrev()

            withPrincipal(saksbehandler1Principal) {
                brevredigeringService.hentEllerOpprettPdf(brev.info.saksId, brev.info.id)
                brevredigeringService.oppdaterSignatur(brev.info.id, "en ny signatur")
                brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)
            }
            // verifiser forskjellig hash
            transaction {
                val redigering = Brevredigering[brev.info.id]
                assertThat(redigering.redigertBrevHash).isNotEqualTo(redigering.document.first().redigertBrevHash)
            }

            withPrincipal(saksbehandler1Principal) {
                assertThrows<NyereVersjonFinsException> {
                    brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)
                }
            }
        }
    }

    @Test
    fun `arkivert brev men ikke distribuert kan sendes`(): Unit = runBlocking {
        val brev = opprettBrev()

        withPrincipal(saksbehandler1Principal) {
            val pdf = brevredigeringService.hentEllerOpprettPdf(brev.info.saksId, brev.info.id)
            assertThat(pdf).isNotNull()
        }

        penService.sendBrevResponse = Pen.BestillBrevResponse(
            991,
            Pen.BestillBrevResponse.Error(null, "Distribuering feilet", null)
        )

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)
        }
        brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)
        assertThat(brevredigeringService.hentBrev(brev.info.saksId, brev.info.id)).isNotNull()

        penService.sendBrevResponse = Pen.BestillBrevResponse(991, null)

        brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)

        assertThat(transaction { Brevredigering.findById(brev.info.id) }).isNull()
    }

    @Test
    fun `brev reservasjon utloeper`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true)

        transaction {
            Brevredigering[brev.info.id].sistReservert =
                Instant.now() - RESERVASJON_TIMEOUT - 1.seconds.toJavaDuration()
        }

        val hentetBrev = brevredigeringService.hentBrev(saksId = sak1.saksId, brevId = brev.info.id)

        assertThat(hentetBrev?.info?.redigeresAv).isNull()

        val hentetBrevMedReservasjon = withPrincipal(saksbehandler2Principal) {
            brevredigeringService.hentBrev(
                saksId = sak1.saksId,
                brevId = brev.info.id,
                reserverForRedigering = true
            )
        }
        assertThat(hentetBrevMedReservasjon?.info?.redigeresAv).isEqualTo(saksbehandler2Principal.navIdent)
    }

    @Test
    fun `brev reservasjon kan fornyes`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true)

        val forrigeReservasjon = Instant.now().minusSeconds(60).truncatedTo(ChronoUnit.MILLIS)
        transaction { Brevredigering[brev.info.id].sistReservert = forrigeReservasjon }

        withPrincipal(saksbehandler1Principal) { brevredigeringService.fornyReservasjon(brev.info.id) }
        assertThat(transaction { Brevredigering[brev.info.id].sistReservert })
            .isAfter(forrigeReservasjon)
            .isBetween(Instant.now().minusSeconds(1), Instant.now().plusSeconds(1))
    }

    @Test
    fun `oppdatering av redigertBrev endrer ogsaa redigertBrevHash`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true)
        val hash1 = transaction { Brevredigering[brev.info.id].redigertBrevHash }
        assertThat(hash1.hexBytes).isEqualTo(WithHash.hash(letter.toEdit()))

        transaction {
            Brevredigering[brev.info.id].redigertBrev =
                letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "blue pill")))).toEdit()
        }

        val hash2 = transaction { Brevredigering[brev.info.id].redigertBrevHash }
        assertThat(hash2).isNotEqualTo(hash1)
    }

    @Test
    fun `kan overstyre mottaker av brev`(): Unit = runBlocking {
        val mottaker = Dto.Mottaker.samhandler("samhandlerId")
        val brev = opprettBrev(mottaker = mottaker)

        assertEquals(mottaker, brev.info.mottaker)
        assertEquals(mottaker.tssId, transaction { Mottaker[brev.info.id].tssId })
    }

    @Test
    fun `kan fjerne overstyrt mottaker av brev`(): Unit = runBlocking {
        val mottaker = Dto.Mottaker.samhandler("samhandlerId")
        val brev = opprettBrev(mottaker = mottaker)
        assertTrue(brevredigeringService.fjernOverstyrtMottaker(brev.info.id, sak1.saksId))

        assertNull(brevredigeringService.hentBrev(sak1.saksId, brev.info.id)?.info?.mottaker)
        assertNull(transaction { Mottaker.findById(brev.info.id) })
        assertNull(transaction { Brevredigering[brev.info.id].mottaker })
    }

    @Test
    fun `kan oppdatere mottaker av brev`(): Unit = runBlocking {
        val brev = opprettBrev(mottaker = Dto.Mottaker.samhandler("1"))
        val nyMottaker = Dto.Mottaker.norskAdresse("a", NorskPostnummer("1234"), "c", "d", "e", "f", Dto.Mottaker.ManueltAdressertTil.IKKE_RELEVANT)

        val oppdatert = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(
                sak1.saksId,
                brev.info.id,
                mottaker = nyMottaker
            )
        }
        assertEquals(nyMottaker, oppdatert?.info?.mottaker)
    }

    @Test
    fun `kan sette annen mottaker for eksisterende brev`(): Unit = runBlocking {
        val brev = opprettBrev(
            mottaker = Dto.Mottaker.utenlandskAdresse(
                navn = "a",
                adresselinje1 = "d",
                adresselinje2 = "e",
                adresselinje3 = "f",
                landkode = Landkode("CY"),
                manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.BRUKER
            )
        )
        val nyMottaker = Dto.Mottaker.utenlandskAdresse(
            navn = "a",
            adresselinje1 = "b",
            adresselinje2 = "c",
            adresselinje3 = "d",
            landkode = Landkode("CY"),
            manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.BRUKER
        )

        val oppdatert = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(
                sak1.saksId,
                brev.info.id,
                mottaker = nyMottaker
            )
        }
        assertEquals(nyMottaker, oppdatert?.info?.mottaker)
    }

    @Test
    fun `brev distribueres til annen mottaker`(): Unit = runBlocking {
        val mottaker = Dto.Mottaker.samhandler("987")
        val brev = opprettBrev(mottaker = mottaker)
        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)
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
                eksternReferanseId = "skribenten:${brev.info.id}",
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
    fun `kan endre signerende saksbehandler signatur`(): Unit = runBlocking {
        val brev = opprettBrev()
        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.oppdaterSignatur(
                brev.info.id,
                "en ny signatur"
            )
        }

        assertEquals(
            "en ny signatur",
            transaction { Brevredigering[brev.info.id].redigertBrev.signatur.saksbehandlerNavn })
    }

    @Test
    fun `kan tilbakestille brev`(): Unit = runBlocking {
        val brev = opprettBrev(saksbehandlerValg = Api.GeneriskBrevdata().apply {
            put("ytelse", "uføre")
            put("inkluderAfpTekst", false)
        })

        withPrincipal(saksbehandler1Principal) {
            brevredigeringFacade.oppdaterBrev(
                UpdateLetterHandler.Request(
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
    fun `kan ikke markere brev klar til sending om ikke alle fritekst er fylt ut`(): Unit = runBlocking {
        brevbakerService.renderMarkupResultat = {
            letter(
                ParagraphImpl(
                    1,
                    true,
                    listOf(
                        LiteralImpl(12, "Vi har "),
                        LiteralImpl(13, "dato", tags = setOf(ElementTags.FRITEKST)),
                        LiteralImpl(14, " mottatt søknad.")
                    )
                )
            )
        }
        val brev = opprettBrev()

        // sjekk at delvis oppdatering fungerer uten å sette brevet til låst
        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = false)
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, distribusjonstype = SENTRALPRINT)
        }

        assertThrows<BrevIkkeKlartTilSendingException> {
            withPrincipal(saksbehandler1Principal) {
                brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)
            }
        }
    }

    @Test
    fun `kan markere brev klar til sending om alle fritekst er fylt ut`(): Unit = runBlocking {
        brevbakerService.renderMarkupResultat = {
            letter(
                ParagraphImpl(
                    1,
                    true,
                    listOf(
                        LiteralImpl(12, "Vi har "),
                        LiteralImpl(13, "dato", tags = setOf(ElementTags.FRITEKST)),
                        LiteralImpl(14, " mottatt søknad.")
                    )
                )
            )
        }
        val brev = opprettBrev()

        withPrincipal(saksbehandler1Principal) {
            brevredigeringFacade.oppdaterBrev(
                UpdateLetterHandler.Request(
                    brevId = brev.info.id,
                    nyttRedigertbrev = brev.redigertBrev.copy(
                        blocks = listOf(
                            E_Paragraph(
                                1,
                                true,
                                listOf(
                                    E_Literal(12, "Vi har "),
                                    E_Literal(13, "dato", tags = setOf(ElementTags.FRITEKST), editedText = "redigert"),
                                    E_Literal(14, " mottatt søknad.")
                                )
                            )
                        )
                    ),
                )
            )
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)
        }
    }

    @Test
    fun `kan hente brev for flere saker`(): Unit = runBlocking {
        val sak2 = sak1.copy(saksId = sak1.saksId + 1)
        val sak3 = sak1.copy(saksId = sak2.saksId + 1)
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
        vedtaksId: Long? = null,
        sak: Pen.SakSelection = sak1,
    ) = withPrincipal(principal) {
        brevredigeringService.opprettBrev(
            sak = sak,
            vedtaksId = vedtaksId,
            brevkode = brevkode,
            spraak = LanguageCode.ENGLISH,
            avsenderEnhetsId = principalNavEnhetId,
            saksbehandlerValg = saksbehandlerValg,
            reserverForRedigering = reserverForRedigering,
            mottaker = mottaker,
        )
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

    private fun <T> condition(description: String, predicate: Predicate<T>): Condition<T> =
        Condition(predicate, description)

    private fun LetterMarkupImpl.medSignatur(saksbehandler: String?, attestant: String?) =
        copy(
            signatur = (signatur as SignaturImpl).copy(
                saksbehandlerNavn = saksbehandler,
                attesterendeSaksbehandlerNavn = attestant
            )
        )
}
