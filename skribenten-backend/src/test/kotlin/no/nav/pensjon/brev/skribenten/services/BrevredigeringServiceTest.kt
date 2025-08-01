package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import io.mockk.*
import kotlinx.coroutines.*
import no.nav.brev.brevbaker.FellesFactory
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.db.*
import no.nav.pensjon.brev.skribenten.initADGroups
import no.nav.pensjon.brev.skribenten.isInstanceOfSatisfying
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype.SENTRALPRINT
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.services.BrevredigeringException.*
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService.Companion.RESERVASJON_TIMEOUT
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.SignaturImpl
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType
import org.apache.commons.codec.binary.Hex
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
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph as E_Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType as E_FontType
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal as E_Literal

class BrevredigeringServiceTest {
    private val postgres = PostgreSQLContainer("postgres:15-alpine")

    init {
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

    private val saksbehandler1Principal = MockPrincipal(NavIdent("Agent Smith"), "Hugo Weaving", setOf(ADGroups.pensjonSaksbehandler))
    private val saksbehandler2Principal = MockPrincipal(NavIdent("Morpheus"), "Laurence Fishburne", setOf(ADGroups.pensjonSaksbehandler))
    private val attestantPrincipal = MockPrincipal(NavIdent("A12345"), "Peder Ås", mutableSetOf(ADGroups.pensjonSaksbehandler, ADGroups.attestant))

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

    private val brevbakerMock: BrevbakerService = mockk<BrevbakerService>()
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

    private val penService: PenService = mockk()
    private val navAnsattService = mockk<NavansattService> {
        coEvery { harTilgangTilEnhet(any(), any()) } returns ServiceResult.Ok(false)
        coEvery {
            harTilgangTilEnhet(
                eq(saksbehandler1Principal.navIdent.id),
                eq(principalNavEnhetId)
            )
        } returns ServiceResult.Ok(true)
        coEvery {
            harTilgangTilEnhet(
                eq(saksbehandler2Principal.navIdent.id),
                eq(principalNavEnhetId)
            )
        } returns ServiceResult.Ok(true)
        coEvery { hentNavansatt(any()) } returns null
        coEvery { hentNavansatt(eq(saksbehandler1Principal.navIdent.id)) } returns Navansatt(
            emptyList(),
            saksbehandler1Principal.fullName + "navansatt",
            saksbehandler1Principal.fullName.split(' ').first(),
            saksbehandler1Principal.fullName.split(' ').last(),
        )
        coEvery { hentNavansatt(eq(saksbehandler2Principal.navIdent.id)) } returns Navansatt(
            emptyList(),
            saksbehandler2Principal.fullName + "navansatt",
            saksbehandler2Principal.fullName.split(' ').first(),
            saksbehandler2Principal.fullName.split(' ').last(),
        )
    }
    private val samhandlerService = mockk<SamhandlerService>()

    private val brevredigeringService: BrevredigeringService = BrevredigeringService(
        brevbakerService = brevbakerMock,
        navansattService = navAnsattService,
        penService = penService,
    )

    private val bestillBrevresponse = ServiceResult.Ok(Pen.BestillBrevResponse(123, null))

    @BeforeEach
    fun clearMocks() {
        clearMocks(brevbakerMock, penService)
        coEvery {
            brevbakerMock.renderMarkup(
                any(),
                any(),
                any(),
                any()
            )
        } answers {
            val signatur = arg<Felles>(3).signerendeSaksbehandlere
            ServiceResult.Ok(
            letter.medSignatur(saksbehandler = signatur?.saksbehandler, attestant = signatur?.attesterendeSaksbehandler)
            )
        }
        coEvery { brevbakerMock.getRedigerbarTemplate(eq(Testbrevkoder.INFORMASJONSBREV)) } returns informasjonsbrev
        coEvery { brevbakerMock.getRedigerbarTemplate(eq(Testbrevkoder.VEDTAKSBREV)) } returns vedtaksbrev
        stagePdf(stagetPDF)

        stageSak(sak1)
        coEvery { penService.sendbrev(any(), any()) } returns bestillBrevresponse
    }

    @Test
    fun `non existing brevredingering returns null`(): Unit = runBlocking {
        assertThat(brevredigeringService.hentBrev(saksId = sak1.saksId, brevId = 99)).isNull()
    }

    @Test
    fun `can create and fetch brevredigering`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val brev = opprettBrev(reserverForRedigering = true, saksbehandlerValg = saksbehandlerValg).resultOrNull()!!

        coVerify {
            brevbakerMock.renderMarkup(
                eq(Testbrevkoder.INFORMASJONSBREV),
                eq(LanguageCode.ENGLISH),
                any(),
                any()
            )
        }
        clearMocks()

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

        coVerify {
            brevbakerMock.renderMarkup(
                eq(Testbrevkoder.INFORMASJONSBREV),
                eq(LanguageCode.ENGLISH),
                any(),
                any()
            )
        }
    }

    @Test
    fun `can create brev for vedtak`(): Unit = runBlocking {
        val vedtaksId: Long = 5678

        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = vedtaksId).resultOrNull()!!
        assertThat(brev.info.vedtaksId).isEqualTo(vedtaksId)
        coVerify {
            penService.hentPesysBrevdata(
                eq(sak1.saksId),
                eq(vedtaksId),
                eq(Testbrevkoder.VEDTAKSBREV),
                any()
            )
        }

        val hentet = brevredigeringService.hentBrev(brev.info.saksId, brev.info.id)?.resultOrNull()!!
        assertThat(hentet.info.vedtaksId).isEqualTo(vedtaksId)
        coVerify {
            penService.hentPesysBrevdata(
                eq(sak1.saksId),
                eq(vedtaksId),
                eq(Testbrevkoder.VEDTAKSBREV),
                any()
            )
        }
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
                    signatur = brev.info.signaturSignerende,
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
            brevredigeringService.delvisOppdaterBrev(saksId = brev.info.saksId, brevId = brev.info.id, laastForRedigering = true)!!
        }

        assertThat(brevEtterLaas.info.status).isEqualTo(Dto.BrevStatus.KLAR)
    }

    @Test
    fun `status er ATTESTERING om vedtaksbrev er laast`(): Unit = runBlocking {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = 1).resultOrNull()!!

        val brevEtterLaas = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(saksId = brev.info.saksId, brevId = brev.info.id, laastForRedigering = true)!!
        }
        assertThat(brevEtterLaas.info.status).isEqualTo(Dto.BrevStatus.ATTESTERING)
    }

    @Test
    fun `status er KLAR om vedtaksbrev er laast og det er attestert`(): Unit = runBlocking {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = 1).resultOrNull()!!

        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.delvisOppdaterBrev(saksId = brev.info.saksId, brevId = brev.info.id, laastForRedigering = true)!!
        }
        val brevEtterAttestering = withPrincipal(attestantPrincipal) {
            brevredigeringService.attester(saksId = brev.info.saksId, brevId = brev.info.id, null, null, null)?.resultOrNull()!!
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

        clearMocks()

        val nyeValg = Api.GeneriskBrevdata().apply { put("valg2", true) }
        val oppdatert = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.oppdaterBrev(
                saksId = sak1.saksId,
                brevId = original.info.id,
                nyeSaksbehandlerValg = nyeValg,
                nyttRedigertbrev = letter.toEdit(),
                signatur = "Malenia"
            )?.resultOrNull()!!
        }

        coVerify(exactly = 1) {
            brevbakerMock.renderMarkup(
                eq(Testbrevkoder.INFORMASJONSBREV),
                eq(LanguageCode.ENGLISH),
                any(),
                any()
            )
        }

        assertThat(brevredigeringService.hentBrev(saksId = sak1.saksId, brevId = original.info.id))
            .isInstanceOfSatisfying<ServiceResult.Ok<*>> {
                assertThat(it.result).isEqualTo(oppdatert)
            }

        assertNotEquals(original.saksbehandlerValg, oppdatert.saksbehandlerValg)
        assertEquals("Malenia", oppdatert.info.signaturSignerende)
    }

    @Test
    fun `updates redigertBrev with fresh rendering from brevbaker`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val original = opprettBrev(saksbehandlerValg = saksbehandlerValg).resultOrNull()!!

        val nyeValg = Api.GeneriskBrevdata().apply { put("valg2", true) }
        val freshRender =
            letter.copy(blocks = letter.blocks + ParagraphImpl(2, true, listOf(VariableImpl(21, "ny paragraph"))))
        coEvery {
            brevbakerMock.renderMarkup(
                eq(Testbrevkoder.INFORMASJONSBREV),
                any(),
                eq(GeneriskRedigerbarBrevdata(Api.GeneriskBrevdata(), nyeValg)),
                any()
            )
        } returns ServiceResult.Ok(freshRender)

        val oppdatert = withPrincipal(saksbehandler1Principal) {
            brevredigeringService.oppdaterBrev(
                saksId = sak1.saksId,
                brevId = original.info.id,
                nyeSaksbehandlerValg = nyeValg,
                nyttRedigertbrev = letter.toEdit(),
                signatur = original.info.signaturSignerende
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
                signatur = letter.signatur.saksbehandlerNavn
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

        coEvery {
            brevbakerMock.renderMarkup(
                eq(Testbrevkoder.INFORMASJONSBREV),
                any(),
                eq(GeneriskRedigerbarBrevdata(EmptyBrevdata, saksbehandlerValg)),
                any()
            )
        } coAnswers {
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
                    signatur = letter.signatur.saksbehandlerNavn,
                )
            }
        }

        assertThat(result).isNull()
    }

    @Test
    fun `can delete brevredigering`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        assertEquals(
            brev,
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
            assertThat(brevredigering.document.first().pdf.bytes).isEqualTo(stagetPDF)
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
            brevredigeringService.attester(sak1.saksId, brev.info.id, null, null, null, true)
        }
        assertThat(attesteringsResultat?.resultOrNull()?.info?.attestertAv).isEqualTo(attestantPrincipal.navIdent)

        coVerify {
            penService.hentPesysBrevdata(
                eq(sak1.saksId),
                eq(1),
                eq(Testbrevkoder.VEDTAKSBREV),
                eq(principalNavEnhetId),
            )
        }
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
                brevredigeringService.attester(sak1.saksId, brev.info.id, null, null, null, true)
            }
        }

        coVerify {
            penService.hentPesysBrevdata(any(), any(), any(), any())
        }
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
            brevredigeringService.attester(sak1.saksId, brev.info.id, null, null, null, true)
            brevredigeringService.hentEllerOpprettPdf(brev.info.saksId, brev.info.id)
            assertThat(brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)).isEqualTo(bestillBrevresponse)
        }
        coVerify {
            brevbakerMock.renderPdf(
                any(),
                any(),
                any(),
                any(),
                match { it.signatur.attesterendeSaksbehandlerNavn == attestantPrincipal.fullName },
            )
        }
    }

    @Test
    fun `distribuerer sentralprint brev`(): Unit = runBlocking {
        clearMocks(brevbakerMock, penService)

        coEvery { penService.hentPesysBrevdata(any(), isNull(), any(), any()) } returns ServiceResult.Ok(
            brevdataResponseData
        )
        coEvery { penService.sendbrev(any(), any()) } returns ServiceResult.Ok(Pen.BestillBrevResponse(123, null))

        coEvery { brevbakerMock.renderPdf(any(), any(), any(), any(), any()) } returns ServiceResult.Ok(letterResponse)
        coEvery { brevbakerMock.renderMarkup(any(), any(), any(), any()) } returns ServiceResult.Ok(letter)
        coEvery { brevbakerMock.getRedigerbarTemplate(any()) } returns informasjonsbrev

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

        coVerify {
            penService.hentPesysBrevdata(
                eq(sak1.saksId),
                isNull(),
                eq(Testbrevkoder.INFORMASJONSBREV),
                eq(principalNavEnhetId),
            )
        }
        coVerify {
            penService.sendbrev(
                eq(
                    Pen.SendRedigerbartBrevRequest(
                        templateDescription = informasjonsbrev,
                        dokumentDato = LocalDate.now(),
                        saksId = 1234,
                        brevkode = Testbrevkoder.INFORMASJONSBREV,
                        enhetId = principalNavEnhetId,
                        pdf = stagetPDF,
                        eksternReferanseId = "skribenten:${brev.info.id}",
                        mottaker = null,
                    )
                ),
                distribuer = eq(true)
            )
        }
    }

    @Test
    fun `distribuerer ikke lokalprint brev`(): Unit = runBlocking {
        clearMocks(brevbakerMock, penService)

        coEvery { penService.hentPesysBrevdata(any(), isNull(), any(), any()) } returns ServiceResult.Ok(
            brevdataResponseData
        )
        coEvery { penService.sendbrev(any(), any()) } returns ServiceResult.Ok(Pen.BestillBrevResponse(123, null))

        coEvery { brevbakerMock.renderPdf(any(), any(), any(), any(), any()) } returns ServiceResult.Ok(letterResponse)
        coEvery { brevbakerMock.renderMarkup(any(), any(), any(), any()) } returns ServiceResult.Ok(letter)
        coEvery { brevbakerMock.getRedigerbarTemplate(any()) } returns informasjonsbrev

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

        coVerify {
            penService.hentPesysBrevdata(
                eq(sak1.saksId),
                isNull(),
                eq(Testbrevkoder.INFORMASJONSBREV),
                eq(principalNavEnhetId),
            )
        }
        coVerify {
            penService.sendbrev(
                eq(
                    Pen.SendRedigerbartBrevRequest(
                        templateDescription = informasjonsbrev,
                        dokumentDato = LocalDate.now(),
                        saksId = 1234,
                        brevkode = Testbrevkoder.INFORMASJONSBREV,
                        enhetId = principalNavEnhetId,
                        pdf = stagetPDF,
                        eksternReferanseId = "skribenten:${brev.info.id}",
                        mottaker = null,
                    )
                ),
                distribuer = eq(false)
            )
        }
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

            coEvery {
                brevbakerMock.renderMarkup(
                    eq(Testbrevkoder.INFORMASJONSBREV),
                    any(),
                    any(),
                    any()
                )
            } coAnswers {
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
                    signatur = brev.info.signaturSignerende,
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

        coEvery { penService.sendbrev(any(), any()) } returns ServiceResult.Ok(
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
                    signatur = brev.info.signaturSignerende,
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
                    signatur = brev.info.signaturSignerende,
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
                signatur = brev.info.signaturSignerende,
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

        coEvery { penService.sendbrev(any(), any()) } returns ServiceResult.Ok(
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

        coEvery { penService.sendbrev(any(), any()) } returns ServiceResult.Ok(Pen.BestillBrevResponse(991, null))
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
                signatur = brev.info.signaturSignerende,
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
                signatur = brev.info.signaturSignerende,
                frigiReservasjon = false,
            )?.resultOrNull()
        }

        assertThat(oppdatertBrev?.info?.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent)
    }

    @Test
    fun `oppdatering av redigertBrev endrer ogsaa redigertBrevHash`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!
        val hash1 = transaction { Brevredigering[brev.info.id].redigertBrevHash }
        assertThat(hash1.hex).isEqualTo(Hex.encodeHexString(WithEditLetterHash.hashBrev(letter.toEdit())))

        transaction {
            Brevredigering[brev.info.id].redigertBrev =
                letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "blue pill")))).toEdit()
        }

        val hash2 = transaction { Brevredigering[brev.info.id].redigertBrevHash }
        assertThat(hash2).isNotEqualTo(hash1)
    }

    @Test
    fun `kan overstyre mottaker av brev`(): Unit = runBlocking {
        coEvery { samhandlerService.hentSamhandlerNavn(eq("samhandlerId")) } returns "hei"

        val mottaker = Dto.Mottaker.samhandler("samhandlerId")
        val brev = opprettBrev(mottaker = mottaker)

        assertEquals(mottaker, brev.resultOrNull()?.info?.mottaker)
        assertEquals(mottaker.tssId, transaction { Mottaker[brev.resultOrNull()!!.info.id].tssId })
    }

    @Test
    fun `kan fjerne overstyrt mottaker av brev`(): Unit = runBlocking {
        coEvery { samhandlerService.hentSamhandlerNavn(eq("samhandlerId")) } returns "hei"
        val mottaker = Dto.Mottaker.samhandler("samhandlerId")
        val brev = opprettBrev(mottaker = mottaker).resultOrNull()!!
        assertTrue(brevredigeringService.fjernOverstyrtMottaker(brev.info.id, sak1.saksId))

        assertNull(brevredigeringService.hentBrev(sak1.saksId, brev.info.id)?.resultOrNull()?.info?.mottaker)
        assertNull(transaction { Mottaker.findById(brev.info.id) })
        assertNull(transaction { Brevredigering[brev.info.id].mottaker })
    }

    @Test
    fun `kan oppdatere mottaker av brev`(): Unit = runBlocking {
        coEvery { samhandlerService.hentSamhandlerNavn(eq("1")) } returns "Samhandler En"

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
        coEvery { samhandlerService.hentSamhandlerNavn(eq("987")) } returns "Ni Åtte Syv"

        val mottaker = Dto.Mottaker.samhandler("987")
        val brev = opprettBrev(mottaker = mottaker).resultOrNull()!!
        withPrincipal(saksbehandler1Principal) {
            brevredigeringService.hentEllerOpprettPdf(sak1.saksId, brev.info.id)
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)
        }

        brevredigeringService.sendBrev(sak1.saksId, brev.info.id)

        coVerify {
            penService.sendbrev(
                eq(
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
                    )
                ),
                eq(true)
            )
        }
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

        assertEquals("en ny signatur", transaction { Brevredigering[brev.info.id].signaturSignerende })
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
                signatur = brev.info.signaturSignerende,
                frigiReservasjon = false,
            )?.resultOrNull()!!
        }

        coEvery { brevbakerMock.getModelSpecification(eq(brev.info.brevkode)) } returns ServiceResult.Ok(
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
        coEvery {
            brevbakerMock.renderMarkup(
                eq(Testbrevkoder.INFORMASJONSBREV), any(), any(), any()
            )
        } returns ServiceResult.Ok(
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
        coEvery {
            brevbakerMock.renderMarkup(
                eq(Testbrevkoder.INFORMASJONSBREV), any(), any(), any()
            )
        } returns ServiceResult.Ok(
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
                signatur = brev.info.signaturSignerende,
            )?.resultOrNull()!!
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)
        }
    }

    @Test
    fun `kan hente brev for flere saker`(): Unit = runBlocking {
        val sak2 = sak1.copy(saksId = sak1.saksId + 1).also { stageSak(it) }
        val sak3 = sak1.copy(saksId = sak2.saksId + 1).also { stageSak(it) }
        val forventedeBrev = listOf(opprettBrev(sak = sak1), opprettBrev(sak = sak1), opprettBrev(sak = sak2)).map { it.resultOrNull()!!.info }.toSet()
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
        coEvery { brevbakerMock.renderPdf(any(), any(), any(), any(), any()) } returns ServiceResult.Ok(
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
        coEvery {
            penService.hentPesysBrevdata(
                eq(sak.saksId),
                any(),
                any(),
                any()
            )
        } returns ServiceResult.Ok(brevdataResponseData)
    }

    private fun <T> condition(description: String, predicate: Predicate<T>): Condition<T> =
        Condition(predicate, description)

    private fun LetterMarkupImpl.medSignatur(saksbehandler: String?, attestant: String?) =
        copy(signatur = (signatur as SignaturImpl).copy(saksbehandlerNavn = saksbehandler, attesterendeSaksbehandlerNavn = attestant))
}
