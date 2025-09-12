package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import kotlinx.coroutines.*
import no.nav.brev.Landkode
import no.nav.brev.brevbaker.FellesFactory
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.db.*
import no.nav.pensjon.brev.skribenten.db.kryptering.KrypteringService
import no.nav.pensjon.brev.skribenten.initADGroups
import no.nav.pensjon.brev.skribenten.isInstanceOfSatisfying
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype.SENTRALPRINT
import no.nav.pensjon.brev.skribenten.services.BrevredigeringException.*
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService.Companion.RESERVASJON_TIMEOUT
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.SignaturImpl
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.testcontainers.containers.PostgreSQLContainer
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
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
    private val postgres = PostgreSQLContainer("postgres:15-alpine")

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
            isSensitiv = false,
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
            isSensitiv = false,
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

    private class BrevredigeringFakeBrevbakerService : FakeBrevbakerService() {
        lateinit var renderMarkupResultat: suspend ((f: Felles) -> ServiceResult<LetterMarkup>)
        lateinit var renderPdfResultat: ServiceResult<LetterResponse>
        lateinit var modelSpecificationResultat: ServiceResult<TemplateModelSpecification>
        override var redigerbareMaler: MutableMap<RedigerbarBrevkode, TemplateDescription.Redigerbar> = mutableMapOf()
        val renderMarkupKall = mutableListOf<Pair<Brevkode.Redigerbart, LanguageCode>>()
        val renderPdfKall = mutableListOf<LetterMarkup>()

        override suspend fun renderMarkup(
            brevkode: Brevkode.Redigerbart,
            spraak: LanguageCode,
            brevdata: RedigerbarBrevdata<*, *>,
            felles: Felles
        ): ServiceResult<LetterMarkupWithDataUsage> =
            renderMarkupResultat(felles)
                .also { renderMarkupKall.add(Pair(brevkode, spraak)) }
                .map { LetterMarkupWithDataUsageImpl(it, emptySet()) }

        override suspend fun renderPdf(
            brevkode: Brevkode.Redigerbart,
            spraak: LanguageCode,
            brevdata: RedigerbarBrevdata<*, *>,
            felles: Felles,
            redigertBrev: LetterMarkup
        ) = renderPdfResultat.also { renderPdfKall.add(redigertBrev) }

        override suspend fun getRedigerbarTemplate(brevkode: Brevkode.Redigerbart) = redigerbareMaler[brevkode]

        override suspend fun getModelSpecification(brevkode: Brevkode.Redigerbart) = modelSpecificationResultat
    }

    private val principalNavEnhetId = "Nebuchadnezzar"


    private val sak1 = Pen.SakSelection(
        1234L,
        "12345678910",
        LocalDate.now().minusYears(42),
        Pen.SakType.ALDER,
        "rabbit"
    )

    private val brevdataResponseData = BrevdataResponse.Data(
        felles = FellesFactory.lagFelles(
            dokumentDato = LocalDate.now(),
            saksnummer = sak1.saksId.toString()
        ),
        brevdata = Api.GeneriskBrevdata()
    )

    private val penService = FakePenService()

    class FakePenService(
        var saker: MutableMap<String, Pen.SakSelection> = mutableMapOf(),
        var pesysBrevdata: BrevdataResponse.Data? = null,
        var sendBrevResponse: ServiceResult<Pen.BestillBrevResponse>? = null,
    ) : PenServiceStub() {
        val utfoerteHentPesysBrevdataKall = mutableListOf<PesysBrevdatakallRequest>()

        data class PesysBrevdatakallRequest(
            val saksId: Long,
            val vedtaksId: Long?,
            val brevkode: Brevkode.Redigerbart,
            val avsenderEnhetsId: String?,
        )

        val utfoerteSendBrevKall = mutableListOf<Pair<Pen.SendRedigerbartBrevRequest, Boolean>>()

        override suspend fun hentSak(saksId: String): ServiceResult<Pen.SakSelection> =
            saker[saksId]?.let { ServiceResult.Ok(it) }
                ?: ServiceResult.Error("Sak finnes ikke", HttpStatusCode.NotFound)

        override suspend fun hentPesysBrevdata(
            saksId: Long,
            vedtaksId: Long?,
            brevkode: Brevkode.Redigerbart,
            avsenderEnhetsId: String?,
        ): ServiceResult<BrevdataResponse.Data> = ServiceResult.Ok(pesysBrevdata ?: notYetStubbed("Mangler pesysBrevdata stub")).also {
            utfoerteHentPesysBrevdataKall.add(PesysBrevdatakallRequest(saksId, vedtaksId, brevkode, avsenderEnhetsId))
        }

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
        samhandlerService = FakeSamhandlerService()
    )

    private val bestillBrevresponse = ServiceResult.Ok(Pen.BestillBrevResponse(123, null))

    @BeforeEach
    fun clearMocks() {
        brevbakerService.renderMarkupResultat = {
            val signatur = it.signerendeSaksbehandlere
            ServiceResult.Ok(
                letter.medSignatur(
                    saksbehandler = signatur?.saksbehandler,
                    attestant = signatur?.attesterendeSaksbehandler
                )
            )
        }

        brevbakerService.redigerbareMaler.put(Testbrevkoder.INFORMASJONSBREV, informasjonsbrev)
        brevbakerService.redigerbareMaler.put(Testbrevkoder.VEDTAKSBREV, vedtaksbrev)
        stagePdf(stagetPDF)

        stageSak(sak1)
        penService.sendBrevResponse = bestillBrevresponse
    }

    @Test
    fun `non existing brevredingering returns null`(): Unit = runBlocking {
        assertThat(brevredigeringService.hentBrev(saksId = sak1.saksId, brevId = 99)).isNull()
    }

    @Test
    fun `can create and fetch brevredigering`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val brev = opprettBrev(reserverForRedigering = true, saksbehandlerValg = saksbehandlerValg).resultOrNull()!!

        assertEquals(brevbakerService.renderMarkupKall.first(), Pair(Testbrevkoder.INFORMASJONSBREV, LanguageCode.ENGLISH))
        brevbakerService.renderMarkupKall.clear()

        assertEquals(
            brev.copy(info = brev.info.copy(sistReservert = null)),
            withPrincipal(saksbehandler1Principal) {
                brevredigeringService.hentBrev(
                    saksId = sak1.saksId,
                    brevId = brev.info.id,
                    reserverForRedigering = true,
                )?.resultOrNull()?.let { it.copy(info = it.info.copy(sistReservert = null)) }
            }
        )
        assertThat(brev.info.brevkode.kode()).isEqualTo(Testbrevkoder.INFORMASJONSBREV.kode())
        assertThat(brev.redigertBrev).isEqualTo(letter.toEdit())

        assertEquals(brevbakerService.renderMarkupKall.first(), Pair(Testbrevkoder.INFORMASJONSBREV, LanguageCode.ENGLISH))
    }

    @Test
    fun `can create brev for vedtak`(): Unit = runBlocking {
        val vedtaksId: Long = 5678

        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = vedtaksId).resultOrNull()!!
        assertThat(brev.info.vedtaksId).isEqualTo(vedtaksId)
        penService.verifyHentPesysBrevdata(sak1.saksId, vedtaksId, Testbrevkoder.VEDTAKSBREV, principalNavEnhetId)

        val hentet = brevredigeringService.hentBrev(brev.info.saksId, brev.info.id)?.resultOrNull()!!
        assertThat(hentet.info.vedtaksId).isEqualTo(vedtaksId)
        penService.verifyHentPesysBrevdata(sak1.saksId, vedtaksId, Testbrevkoder.VEDTAKSBREV, principalNavEnhetId)
    }

    @Test
    fun `brev must belong to provided saksId`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        withPrincipal(saksbehandler1Principal) {
            assertThat(
                brevredigeringService.hentBrev(
                    saksId = sak1.saksId + 1,
                    brevId = brev.info.id
                )
            ).isNull()
            assertThat(
                brevredigeringService.oppdaterBrev(
                    saksId = sak1.saksId + 1,
                    brevId = brev.info.id,
                    nyeSaksbehandlerValg = brev.saksbehandlerValg,
                    nyttRedigertbrev = brev.redigertBrev,
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
                    brevId = brev.info.id
                )
            ).isNull()

            // Må generere pdf nå, slik at sendBrev ikke returnerer null pga. manglende dokument
            brevredigeringService.hentEllerOpprettPdf(saksId = sak1.saksId, brevId = brev.info.id)
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
        val brev = opprettBrev().resultOrNull()!!

        assertThat(brev.info.status).isEqualTo(Dto.BrevStatus.KLADD)
    }

    @Test
    fun `status er KLAR om brev er laast`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
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
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = 1).resultOrNull()!!

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
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = 1).resultOrNull()!!

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(
                saksId = brev.info.saksId,
                brevId = brev.info.id,
                laastForRedigering = true
            )!!
        }
        val brevEtterAttestering = withPrincipal(attestantPrincipal) {
            brevredigeringService.attester(saksId = brev.info.saksId, brevId = brev.info.id, null, null)
                ?.resultOrNull()!!
        }
        assertThat(brevEtterAttestering.info.status).isEqualTo(Dto.BrevStatus.KLAR)
    }

    @Test
    fun `status er ARKIVERT om brev har journalpost`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        transaction { Brevredigering[brev.info.id].journalpostId = 123L }

        val oppdatertBrev = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentBrev(brev.info.saksId, brev.info.id)?.resultOrNull()!!
        }
        assertThat(oppdatertBrev.info.status).isEqualTo(Dto.BrevStatus.ARKIVERT)
    }

    @Test
    fun `cannot create brevredigering for a NavEnhet without access to it`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val result = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.opprettBrev(
                sak = sak1,
                vedtaksId = null,
                brevkode = Testbrevkoder.INFORMASJONSBREV,
                spraak = LanguageCode.ENGLISH,
                avsenderEnhetsId = "The Matrix",
                saksbehandlerValg = saksbehandlerValg
            )
        }
        assertThat(result).isInstanceOfSatisfying<ServiceResult.Error<*>> {
            assertThat(it.statusCode).isEqualTo(HttpStatusCode.Forbidden)
        }
    }

    @Test
    fun `can update brevredigering`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val original = opprettBrev(reserverForRedigering = true, saksbehandlerValg = saksbehandlerValg).resultOrNull()!!

        brevbakerService.renderMarkupKall.clear()

        val nyeValg = Api.GeneriskBrevdata().apply { put("valg2", true) }
        val oppdatert = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.oppdaterBrev(
                saksId = sak1.saksId,
                brevId = original.info.id,
                nyeSaksbehandlerValg = nyeValg,
                nyttRedigertbrev = letter.toEdit()
                    .let { it.copy(signatur = (it.signatur as SignaturImpl).copy(saksbehandlerNavn = "Malenia")) },
            )?.resultOrNull()!!
        }


        assertEquals(brevbakerService.renderMarkupKall.first(), Pair(Testbrevkoder.INFORMASJONSBREV, LanguageCode.ENGLISH))
        assertEquals(1, brevbakerService.renderMarkupKall.filter { it.first == Testbrevkoder.INFORMASJONSBREV && it.second == LanguageCode.ENGLISH }.size)

        assertThat(brevredigeringService.hentBrev(saksId = sak1.saksId, brevId = original.info.id))
            .isInstanceOfSatisfying<ServiceResult.Ok<*>> {
                assertThat(it.result).isEqualTo(oppdatert.copy(propertyUsage = null))
            }

        assertNotEquals(original.saksbehandlerValg, oppdatert.saksbehandlerValg)
        assertEquals("Malenia", oppdatert.redigertBrev.signatur.saksbehandlerNavn)
    }

    @Test
    fun `updates redigertBrev with fresh rendering from brevbaker`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val original = opprettBrev(saksbehandlerValg = saksbehandlerValg).resultOrNull()!!

        val nyeValg = Api.GeneriskBrevdata().apply { put("valg2", true) }
        val freshRender =
            letter.copy(blocks = letter.blocks + ParagraphImpl(2, true, listOf(VariableImpl(21, "ny paragraph"))))
        brevbakerService.renderMarkupResultat = { ServiceResult.Ok(freshRender) }

        val oppdatert = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.oppdaterBrev(
                saksId = sak1.saksId,
                brevId = original.info.id,
                nyeSaksbehandlerValg = nyeValg,
                nyttRedigertbrev = letter.toEdit(),
            )?.resultOrNull()!!
        }

        assertNotEquals(original.redigertBrev, oppdatert.redigertBrev)
        assertEquals(letter.toEdit().updateEditedLetter(freshRender), oppdatert.redigertBrev)
    }

    @Test
    fun `cannot update non-existing brevredigering`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val oppdatert = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.oppdaterBrev(
                saksId = sak1.saksId,
                brevId = 1099,
                nyeSaksbehandlerValg = saksbehandlerValg,
                nyttRedigertbrev = letter.toEdit(),
            )?.resultOrNull()
        }

        assertNull(oppdatert)
    }

    @Test
    fun `does not wait for brevbaker before answering with brevredigering does not exist`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply {
            put("valg1", true)
            put("noe unikt", UUID.randomUUID())
        }

        brevbakerService.renderMarkupResultat = {
            delay(10.minutes)
            ServiceResult.Ok(letter)
        }

        val result = withTimeout(10.seconds) {
            withPrincipal(saksbehandler1Principal) {
                brevredigeringService.oppdaterBrev(
                    saksId = sak1.saksId,
                    brevId = 2098,
                    nyeSaksbehandlerValg = saksbehandlerValg,
                    nyttRedigertbrev = letter.toEdit(),
                )
            }
        }

        assertThat(result).isNull()
    }

    @Test
    fun `can delete brevredigering`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        assertEquals(
            brev.copy(propertyUsage = null),
            brevredigeringService.hentBrev(saksId = sak1.saksId, brevId = brev.info.id)
                ?.resultOrNull()
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
        val brev = opprettBrev().resultOrNull()!!

        transaction {
            assertThat(Brevredigering[brev.info.id].document).isEmpty()
            assertThat(Document.find { DocumentTable.brevredigering.eq(brev.info.id) }).isEmpty()
        }

        assertThat(
            withPrincipal(saksbehandler1Principal) {
                brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)?.resultOrNull()
            }
        ).isEqualTo(stagetPDF)

        transaction {
            val brevredigering = Brevredigering[brev.info.id]
            assertThat(brevredigering.document).hasSize(1)
            assertThat(Document.find { DocumentTable.brevredigering.eq(brev.info.id) }).hasSize(1)
            assertThat(brevredigering.document.first().pdf).isEqualTo(stagetPDF)
        }
    }

    @Test
    fun `sletter relaterte documents ved sletting av brevredigering`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

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
        val brev = opprettBrev().resultOrNull()!!

        awaitAll(*(0..<10).map {
            async(Dispatchers.IO) {
                withPrincipal(saksbehandler1Principal) {
                    brevredigeringService.hentEllerOpprettPdf(
                        sak1.saksId,
                        brev.info.id
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
        val brev = opprettBrev().resultOrNull()!!

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
        val brev = opprettBrev().resultOrNull()!!

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
        val brev = opprettBrev().resultOrNull()!!

        withPrincipal(saksbehandler1Principal) {
            val pdf = brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)
            assertThat(pdf?.resultOrNull()?.toString(Charsets.UTF_8)).isEqualTo(stagetPDF.toString(Charsets.UTF_8))
        }
    }

    @Test
    fun `hentPdf rendrer ny pdf om den ikke er basert paa gjeldende redigertBrev`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        stagePdf("min første pdf".encodeToByteArray())

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)

            transaction {
                Brevredigering[brev.info.id].redigertBrev =
                    letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "blue pill")))).toEdit()
            }

            stagePdf("min andre pdf".encodeToByteArray())
            val pdf = brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)?.resultOrNull()
                ?.toString(Charsets.UTF_8)

            assertEquals("min andre pdf", pdf)
        }
    }

    @Test
    fun `hentPdf rendrer ikke ny pdf om den er basert paa gjeldende redigertBrev`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        withPrincipal(saksbehandler1Principal) {
            stagePdf("min første pdf".encodeToByteArray())
            val first = brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)?.resultOrNull()

            stagePdf("min andre pdf".encodeToByteArray())
            val second = brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)?.resultOrNull()

            assertThat(first).isEqualTo(second)
        }
    }

    @Test
    fun `attesterer hvis avsender har attestantrolle`(): Unit = runBlocking {
        Features.override(Features.attestant, true)

        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) },
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = 1,
        ).resultOrNull()!!

        val attesteringsResultat = withPrincipal(attestantPrincipal) {
            val oppdatert = brevredigeringService.oppdaterSignaturAttestant(brev.info.id, "Lars Holm")
            assertEquals(
                "Lars Holm",
                oppdatert?.resultOrNull()?.redigertBrev?.signatur?.attesterendeSaksbehandlerNavn,
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
        assertThat(attesteringsResultat?.resultOrNull()?.info?.attestertAv).isEqualTo(attestantPrincipal.navIdent)

        penService.verifyHentPesysBrevdata(sak1.saksId, 1, Testbrevkoder.VEDTAKSBREV, principalNavEnhetId)
    }

    @Test
    fun `attesterer ikke hvis avsender ikke har attestantrolle`(): Unit = runBlocking {
        Features.override(Features.attestant, true)

        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) },
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = 1,
        ).resultOrNull()!!

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
        Features.override(Features.attestant, true)

        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) },
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = 1,
        ).resultOrNull()!!
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
        Features.override(Features.attestant, true)
        brevbakerService.renderPdfKall.clear()

        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) },
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = 1,
        ).resultOrNull()!!
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
        penService.pesysBrevdata = brevdataResponseData
        penService.sendBrevResponse = ServiceResult.Ok(Pen.BestillBrevResponse(123, null))

        brevbakerService.renderPdfResultat = ServiceResult.Ok(letterResponse)
        brevbakerService.renderMarkupResultat = { ServiceResult.Ok(letter) }

        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) }
        ).resultOrNull()!!

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

        penService.verifySendBrev(Pen.SendRedigerbartBrevRequest(
            templateDescription = informasjonsbrev,
            dokumentDato = LocalDate.now(),
            saksId = 1234,
            brevkode = Testbrevkoder.INFORMASJONSBREV,
            enhetId = principalNavEnhetId,
            pdf = stagetPDF,
            eksternReferanseId = "skribenten:${brev.info.id}",
            mottaker = null,
        ), true)
    }

    @Test
    fun `distribuerer ikke lokalprint brev`(): Unit = runBlocking {
        penService.pesysBrevdata = brevdataResponseData
        penService.sendBrevResponse = ServiceResult.Ok(Pen.BestillBrevResponse(123, null))

        brevbakerService.renderPdfResultat = ServiceResult.Ok(letterResponse)
        brevbakerService.renderMarkupResultat = { ServiceResult.Ok(letter) }

        val brev = opprettBrev(
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) }
        ).resultOrNull()!!

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

        penService.verifySendBrev(Pen.SendRedigerbartBrevRequest(
            templateDescription = informasjonsbrev,
            dokumentDato = LocalDate.now(),
            saksId = 1234,
            brevkode = Testbrevkoder.INFORMASJONSBREV,
            enhetId = principalNavEnhetId,
            pdf = stagetPDF,
            eksternReferanseId = "skribenten:${brev.info.id}",
            mottaker = null,
        ), false)
    }

    @Test
    fun `brev kan reserveres for redigering gjennom opprett brev`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!

        assertThat(brev.info.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent)
        assertThat(brev.info.sistReservert).isBetween(Instant.now() - 10.minutes.toJavaDuration(), Instant.now())
        assertThat(transaction { Brevredigering[brev.info.id].redigeresAvNavIdent }).isEqualTo(saksbehandler1Principal.navIdent)
    }

    @Test
    fun `brev kan reserveres for redigering gjennom hent brev`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        assertThat(brev.info.laastForRedigering).isFalse()
        assertThat(brev.info.redigeresAv).isNull()

        val hentetBrev = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentBrev(
                saksId = sak1.saksId,
                brevId = brev.info.id,
                reserverForRedigering = true
            )?.resultOrNull()!!
        }

        assertThat(hentetBrev.info.laastForRedigering).isFalse()
        assertThat(hentetBrev.info.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent)
    }

    @Test
    fun `allerede reservert brev kan ikke resereveres for redigering`() {
        runBlocking {
            val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!

            assertThrows<KanIkkeReservereBrevredigeringException> {
                withPrincipal(saksbehandler2Principal) {
                    brevredigeringService.hentBrev(
                        saksId = sak1.saksId,
                        brevId = brev.info.id,
                        reserverForRedigering = true
                    )?.resultOrNull()!!
                }
            }
            assertThat(transaction { Brevredigering[brev.info.id].redigeresAvNavIdent }).isEqualTo(
                saksbehandler1Principal.navIdent
            )
        }
    }

    @Test
    fun `kun en som vinner reservasjon av et brev`() {
        runBlocking {
            val brev = opprettBrev().resultOrNull()!!

            brevbakerService.renderMarkupResultat = {
                delay(100)
                ServiceResult.Ok(letter)
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
            val redigeresFaktiskAv = transaction { Brevredigering[brev.info.id].redigeresAvNavIdent }!!

            assertThat(awaited).areExactly(1, condition("Vellykkede hentBrev med reservasjon") { it.isSuccess })
            assertThat(awaited).areExactly(
                awaited.size - 1,
                condition("Feilende hentBrev med reservasjon") { it.isFailure })
            assertThat(awaited).allMatch {
                it.isFailure || it.getOrNull()?.resultOrNull()?.info?.redigeresAv == redigeresFaktiskAv
            }
        }
    }

    @Test
    fun `brev kan ikke oppdateres av andre enn den som har reservert det for redigering`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!

        assertThrows<KanIkkeReservereBrevredigeringException> {
            withPrincipal(saksbehandler2Principal) {
                brevredigeringService.oppdaterBrev(
                    saksId = sak1.saksId,
                    brevId = brev.info.id,
                    nyeSaksbehandlerValg = brev.saksbehandlerValg,
                    nyttRedigertbrev = letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "blue pill")))).toEdit(),
                )
            }
        }
        transaction {
            assertThat(Brevredigering[brev.info.id].redigertBrev == brev.redigertBrev)
        }
    }

    @Test
    fun `brev kan ikke endres om det er arkivert`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!

        withPrincipal(saksbehandler1Principal) {
            val pdf = brevredigeringService.hentEllerOpprettPdf(brev.info.saksId, brev.info.id)?.resultOrNull()
            assertThat(pdf).isNotNull()
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)
        }

        penService.sendBrevResponse = ServiceResult.Ok(
            Pen.BestillBrevResponse(
                991,
                Pen.BestillBrevResponse.Error(null, "Distribuering feilet", null)
            )
        )

        brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)?.resultOrNull()
        assertThat(brevredigeringService.hentBrev(brev.info.saksId, brev.info.id)?.resultOrNull()).isNotNull()

        withPrincipal(saksbehandler1Principal) {
            assertThrows<ArkivertBrevException> {
                brevredigeringService.oppdaterBrev(
                    saksId = brev.info.saksId,
                    brevId = brev.info.id,
                    nyeSaksbehandlerValg = null,
                    nyttRedigertbrev = letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "blue pill")))).toEdit(),
                )
            }
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
    fun `saksbehandler kan ikke redigere brev som er laastForRedigering`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)

            assertThrows<BrevLaastForRedigeringException> {
                brevredigeringService.oppdaterBrev(
                    saksId = brev.info.saksId,
                    brevId = brev.info.id,
                    nyeSaksbehandlerValg = null,
                    nyttRedigertbrev = letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "blue pill")))).toEdit(),
                )
            }
        }
    }

    @Test
    fun `attestant kan redigere brev som er laastForRedigering`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)
        }

        withPrincipal(attestantPrincipal) {
            brevredigeringService.oppdaterBrev(
                saksId = brev.info.saksId,
                brevId = brev.info.id,
                nyeSaksbehandlerValg = null,
                nyttRedigertbrev = letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "blue pill")))).toEdit(),
            )
        }
    }

    @Test
    fun `kan ikke sende brev som ikke er markert klar til sending`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentEllerOpprettPdf(brev.info.saksId, brev.info.id)?.resultOrNull()
        }

        assertThrows<BrevIkkeKlartTilSendingException> {
            brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)
        }
    }

    @Test
    fun `arkivert brev men ikke distribuert kan sendes`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        withPrincipal(saksbehandler1Principal) {
            val pdf = brevredigeringService.hentEllerOpprettPdf(brev.info.saksId, brev.info.id)?.resultOrNull()
            assertThat(pdf).isNotNull()
        }

        penService.sendBrevResponse = ServiceResult.Ok(
            Pen.BestillBrevResponse(
                991,
                Pen.BestillBrevResponse.Error(null, "Distribuering feilet", null)
            )
        )

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)
        }
        brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)?.resultOrNull()!!
        assertThat(brevredigeringService.hentBrev(brev.info.saksId, brev.info.id)?.resultOrNull()).isNotNull()

        penService.sendBrevResponse = ServiceResult.Ok(Pen.BestillBrevResponse(991, null))

        brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)

        assertThat(transaction { Brevredigering.findById(brev.info.id) }).isNull()
    }

    @Test
    fun `brev reservasjon utloeper`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!

        transaction {
            Brevredigering[brev.info.id].sistReservert =
                Instant.now() - RESERVASJON_TIMEOUT - 1.seconds.toJavaDuration()
        }

        val hentetBrev = brevredigeringService.hentBrev(saksId = sak1.saksId, brevId = brev.info.id)?.resultOrNull()!!

        assertThat(hentetBrev.info.redigeresAv).isNull()

        val hentetBrevMedReservasjon = withPrincipal(saksbehandler2Principal) {
            brevredigeringService.hentBrev(
                saksId = sak1.saksId,
                brevId = brev.info.id,
                reserverForRedigering = true
            )?.resultOrNull()!!
        }
        assertThat(hentetBrevMedReservasjon.info.redigeresAv).isEqualTo(saksbehandler2Principal.navIdent)
    }

    @Test
    fun `brev reservasjon kan fornyes`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!

        val forrigeReservasjon = Instant.now().minusSeconds(60).truncatedTo(ChronoUnit.MILLIS)
        transaction { Brevredigering[brev.info.id].sistReservert = forrigeReservasjon }

        withPrincipal(saksbehandler1Principal) { brevredigeringService.fornyReservasjon(brev.info.id) }
        assertThat(transaction { Brevredigering[brev.info.id].sistReservert })
            .isAfter(forrigeReservasjon)
            .isBetween(Instant.now().minusSeconds(1), Instant.now().plusSeconds(1))
    }

    @Test
    fun `brev reservasjon kan frigis ved oppdatering`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!
        assertThat(brev.info.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent)

        val oppdatertBrev = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.oppdaterBrev(
                saksId = brev.info.saksId,
                brevId = brev.info.id,
                nyeSaksbehandlerValg = brev.saksbehandlerValg,
                nyttRedigertbrev = null,
                frigiReservasjon = true,
            )?.resultOrNull()
        }

        assertThat(oppdatertBrev?.info?.redigeresAv).isNull()
    }

    @Test
    fun `brev reservasjon frigis ikke ved oppdatering`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!
        assertThat(brev.info.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent)

        val oppdatertBrev = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.oppdaterBrev(
                saksId = brev.info.saksId,
                brevId = brev.info.id,
                nyeSaksbehandlerValg = brev.saksbehandlerValg,
                nyttRedigertbrev = null,
                frigiReservasjon = false,
            )?.resultOrNull()
        }

        assertThat(oppdatertBrev?.info?.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent)
    }

    @Test
    fun `oppdatering av redigertBrev endrer ogsaa redigertBrevHash`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!
        val hash1 = transaction { Brevredigering[brev.info.id].redigertBrevHash }
        assertThat(hash1.hexBytes).isEqualTo(WithEditLetterHash.hashBrev(letter.toEdit()))

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

        assertEquals(mottaker, brev.resultOrNull()?.info?.mottaker)
        assertEquals(mottaker.tssId, transaction { Mottaker[brev.resultOrNull()!!.info.id].tssId })
    }

    @Test
    fun `kan fjerne overstyrt mottaker av brev`(): Unit = runBlocking {
        val mottaker = Dto.Mottaker.samhandler("samhandlerId")
        val brev = opprettBrev(mottaker = mottaker).resultOrNull()!!
        assertTrue(brevredigeringService.fjernOverstyrtMottaker(brev.info.id, sak1.saksId))

        assertNull(brevredigeringService.hentBrev(sak1.saksId, brev.info.id)?.resultOrNull()?.info?.mottaker)
        assertNull(transaction { Mottaker.findById(brev.info.id) })
        assertNull(transaction { Brevredigering[brev.info.id].mottaker })
    }

    @Test
    fun `kan oppdatere mottaker av brev`(): Unit = runBlocking {
        val brev = opprettBrev(mottaker = Dto.Mottaker.samhandler("1")).resultOrNull()!!
        val nyMottaker = Dto.Mottaker.norskAdresse("a", "b", "c", "d", "e", "f")

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
        val brev = opprettBrev().resultOrNull()!!
        val nyMottaker = Dto.Mottaker.utenlandskAdresse("a", "b", "c", "d", "e", "f", Landkode("CY"))

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
        val brev = opprettBrev(mottaker = mottaker).resultOrNull()!!
        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)
        }

        brevredigeringService.sendBrev(sak1.saksId, brev.info.id)

        penService.verifySendBrev(Pen.SendRedigerbartBrevRequest(
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
        ), true)
    }

    @Test
    fun `kan endre signerende saksbehandler signatur`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
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
        }).resultOrNull()!!

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.oppdaterBrev(
                saksId = brev.info.saksId,
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
                frigiReservasjon = false,
            )?.resultOrNull()!!
        }

        brevbakerService.modelSpecificationResultat = ServiceResult.Ok(
            TemplateModelSpecification(
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
        )

        val tilbakestilt = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.tilbakestill(brev.info.id)?.resultOrNull()!!
        }
        assertThat(tilbakestilt.redigertBrev).isEqualTo(letter.toEdit())
        assertThat(tilbakestilt.saksbehandlerValg).isEqualTo(Api.GeneriskBrevdata().apply {
            put("ytelse", "uføre")
            put("inkluderAfpTekst", false)
            put("land", null)
        })
    }

    @Test
    fun `kan ikke markere brev klar til sending om ikke alle fritekst er fylt ut`(): Unit = runBlocking {
        brevbakerService.renderMarkupResultat = {
            ServiceResult.Ok(
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
            )
        }
        val brev = opprettBrev().resultOrNull()!!

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
            ServiceResult.Ok(
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
            )
        }
        val brev = opprettBrev().resultOrNull()!!

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.oppdaterBrev(
                brev.info.saksId, brev.info.id, nyeSaksbehandlerValg = null,
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
            )?.resultOrNull()!!
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)
        }
    }

    @Test
    fun `kan hente brev for flere saker`(): Unit = runBlocking {
        val sak2 = sak1.copy(saksId = sak1.saksId + 1).also { stageSak(it) }
        val sak3 = sak1.copy(saksId = sak2.saksId + 1).also { stageSak(it) }
        val forventedeBrev = listOf(
            opprettBrev(sak = sak1),
            opprettBrev(sak = sak1),
            opprettBrev(sak = sak2)
        ).map { it.resultOrNull()!!.info }.toSet()
        val ikkeForventetBrev = opprettBrev(sak = sak3).resultOrNull()!!.info

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
        brevbakerService.renderPdfResultat = ServiceResult.Ok(
            LetterResponse(
                file = pdf,
                contentType = ContentType.Application.Pdf.toString(),
                letterMetadata = LetterMetadata(
                    displayTitle = "En fin tittel",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
                )
            )
        )
    }

    private fun stageSak(sak: Pen.SakSelection) {
        penService.pesysBrevdata = brevdataResponseData
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
