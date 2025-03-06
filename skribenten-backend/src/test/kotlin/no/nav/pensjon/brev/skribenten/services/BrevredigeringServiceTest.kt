package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import io.mockk.*
import kotlinx.coroutines.*
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.lesInnADGrupper
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.db.*
import no.nav.pensjon.brev.skribenten.isInstanceOfSatisfying
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.services.BrevredigeringException.*
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService.Companion.RESERVASJON_TIMEOUT
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl
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

    @BeforeAll
    fun startDb() {
        postgres.start()
        initDatabase(postgres.jdbcUrl, postgres.username, postgres.password)
    }

    @AfterAll
    fun stopDb() {
        postgres.stop()
    }

    private val letter = letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "red pill"))))
    private val stagetPDF = "nesten en pdf".encodeToByteArray()
    private val lettermetadata = LetterMetadata(
        displayTitle = "displayTitle",
        isSensitiv = false,
        distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
        brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
    )
    private val letterResponse = LetterResponse(file = stagetPDF, contentType = "pdf", letterMetadata = lettermetadata)
    private val templateDescription = TemplateDescription.Redigerbar(
        name = "template name",
        letterDataClass = "template letter data class",
        languages = listOf(LanguageCode.ENGLISH),
        metadata = lettermetadata,
        kategori = TemplateDescription.Brevkategori.INFORMASJONSBREV,
        brevkontekst = TemplateDescription.Brevkontekst.ALLE,
        sakstyper = Sakstype.all,
    )

    private val brevbakerMock: BrevbakerService = mockk<BrevbakerService>()
    private val principalNavIdent = NavIdent("Agent Smith")
    private val principalNavn = "Hugo Weaving"
    private val principalNavIdent2 = NavIdent("Morpheus")
    private val principalNavn2 = "Laurence Fishburne"
    private val principalNavEnhetId = "Nebuchadnezzar"

    private fun principalMock(ident: NavIdent = principalNavIdent) = MockPrincipal(ident, "Laurence Fishburne")

    private val sak = Pen.SakSelection(
        1234L,
        "12345678910",
        LocalDate.now().minusYears(42),
        Pen.SakType.ALDER,
        "rabbit"
    )

    @OptIn(InterneDataklasser::class)
    private val brevdataResponseData = BrevdataResponse.Data(
        felles = FellesImpl(
            dokumentDato = LocalDate.now(),
            saksnummer = sak.saksId.toString(),
            avsenderEnhet = NavEnhetImpl(
                nettside = "nav.no",
                navn = "en fantastisk enhet",
                telefonnummer = TelefonnummerImpl("12345678")
            ),
            bruker = BrukerImpl(
                foedselsnummer = FoedselsnummerImpl("12345678910"),
                fornavn = "Navn",
                mellomnavn = null,
                etternavn = "Navnesen"
            ),
            vergeNavn = null,
            signerendeSaksbehandlere = null,
        ), brevdata = Api.GeneriskBrevdata()
    )
    private val penService: PenService = mockk()
    private val navAnsattService = mockk<NavansattService> {
        coEvery { harTilgangTilEnhet(any(), any()) } returns ServiceResult.Ok(false)
        coEvery {
            harTilgangTilEnhet(
                eq(principalNavIdent.id),
                eq(principalNavEnhetId)
            )
        } returns ServiceResult.Ok(true)
        coEvery {
            harTilgangTilEnhet(
                eq(principalNavIdent2.id),
                eq(principalNavEnhetId)
            )
        } returns ServiceResult.Ok(true)
        coEvery { hentNavansatt(any()) } returns null
        coEvery { hentNavansatt(eq(principalNavIdent.id)) } returns Navansatt(
            emptyList(),
            principalNavn,
            "Hugo",
            "Weaving"
        )
        coEvery { hentNavansatt(eq(principalNavIdent2.id)) } returns Navansatt(
            emptyList(),
            principalNavn2,
            "Laurence",
            "Fishburne"
        )
    }
    private val samhandlerService = mockk<SamhandlerService>()

    private val brevredigeringService: BrevredigeringService = BrevredigeringService(
        brevbakerService = brevbakerMock,
        navansattService = navAnsattService,
        penService = penService,
    )

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
        } returns ServiceResult.Ok(letter)
        coEvery { brevbakerMock.getRedigerbarTemplate(any()) } returns templateDescription
        stagePdf(stagetPDF)

        coEvery {
            penService.hentPesysBrevdata(
                eq(sak.saksId),
                any(),
                any(),
                any()
            )
        } returns ServiceResult.Ok(brevdataResponseData)
        coEvery { penService.sendbrev(any(), any()) } returns ServiceResult.Ok(Pen.BestillBrevResponse(123, null))
    }

    @Test
    fun `non existing brevredingering returns null`(): Unit = runBlocking {
        assertThat(brevredigeringService.hentBrev(saksId = sak.saksId, brevId = 99)).isNull()
    }

    @Test
    fun `can create and fetch brevredigering`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val brev = opprettBrev(reserverForRedigering = true, saksbehandlerValg = saksbehandlerValg).resultOrNull()!!

        coVerify {
            brevbakerMock.renderMarkup(
                eq(Testbrevkoder.TESTBREV),
                eq(LanguageCode.ENGLISH),
                any(),
                any()
            )
        }
        clearMocks()

        assertEquals(
            brev.copy(info = brev.info.copy(sistReservert = null)),
            withPrincipal(principalMock()) {
                brevredigeringService.hentBrev(
                    saksId = sak.saksId,
                    brevId = brev.info.id,
                    reserverForRedigering = true,
                )?.resultOrNull()?.let { it.copy(info = it.info.copy(sistReservert = null)) }
            }
        )
        assertEquals(brev.info.brevkode, RedigerbarBrevkode(Testbrevkoder.TESTBREV.name))
        assertEquals(brev.redigertBrev, letter.toEdit())

        coVerify {
            brevbakerMock.renderMarkup(
                eq(RedigerbarBrevkode(Testbrevkoder.TESTBREV.name)),
                eq(LanguageCode.ENGLISH),
                any(),
                any()
            )
        }
    }

    @Test
    fun `can create brev for vedtak`(): Unit = runBlocking {
        val vedtaksId: Long = 5678

        val brev = opprettBrev(vedtaksId = vedtaksId).resultOrNull()!!
        assertThat(brev.info.vedtaksId).isEqualTo(vedtaksId)
        coVerify {
            penService.hentPesysBrevdata(
                eq(sak.saksId),
                eq(vedtaksId),
                eq(Testbrevkoder.TESTBREV),
                any()
            )
        }

        val hentet = brevredigeringService.hentBrev(brev.info.saksId, brev.info.id)?.resultOrNull()!!
        assertThat(hentet.info.vedtaksId).isEqualTo(vedtaksId)
        coVerify {
            penService.hentPesysBrevdata(
                eq(sak.saksId),
                eq(vedtaksId),
                eq(Testbrevkoder.TESTBREV),
                any()
            )
        }
    }

    @Test
    fun `brev must belong to provided saksId`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        withPrincipal(principalMock()) {
            assertThat(
                brevredigeringService.hentBrev(
                    saksId = sak.saksId + 1,
                    brevId = brev.info.id
                )
            ).isNull()
            assertThat(
                brevredigeringService.oppdaterBrev(
                    saksId = sak.saksId + 1,
                    brevId = brev.info.id,
                    nyeSaksbehandlerValg = brev.saksbehandlerValg,
                    nyttRedigertbrev = brev.redigertBrev,
                    signatur = brev.info.signaturSignerende,
                )
            ).isNull()
            assertThat(
                brevredigeringService.delvisOppdaterBrev(
                    saksId = sak.saksId + 1,
                    brevId = brev.info.id,
                    laastForRedigering = true,
                )
            ).isNull()
            assertThat(
                brevredigeringService.hentEllerOpprettPdf(
                    saksId = sak.saksId + 1,
                    brevId = brev.info.id
                )
            ).isNull()

            // Må generere pdf nå, slik at sendBrev ikke returnerer null pga. manglende dokument
            brevredigeringService.hentEllerOpprettPdf(saksId = sak.saksId, brevId = brev.info.id)
            assertThat(
                brevredigeringService.sendBrev(
                    saksId = sak.saksId + 1,
                    brevId = brev.info.id
                )
            ).isNull()
            assertThat(brevredigeringService.slettBrev(saksId = sak.saksId + 1, brevId = brev.info.id)).isFalse()
        }
    }

    @Test
    fun `cannot create brevredigering for a NavEnhet without access to it`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val result = withPrincipal(principalMock()) {
            brevredigeringService.opprettBrev(
                sak = sak,
                vedtaksId = null,
                brevkode = Testbrevkoder.TESTBREV,
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
        val oppdatert = withPrincipal(principalMock()) {
            brevredigeringService.oppdaterBrev(
                saksId = sak.saksId,
                brevId = original.info.id,
                nyeSaksbehandlerValg = nyeValg,
                nyttRedigertbrev = letter.toEdit(),
                signatur = "Malenia"
            )?.resultOrNull()!!
        }

        coVerify(exactly = 1) {
            brevbakerMock.renderMarkup(
                eq(RedigerbarBrevkode(Testbrevkoder.TESTBREV.name)),
                eq(LanguageCode.ENGLISH),
                any(),
                any()
            )
        }

        assertThat(brevredigeringService.hentBrev(saksId = sak.saksId, brevId = original.info.id))
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
        val freshRender = letter.copy(blocks = letter.blocks + ParagraphImpl(2, true, listOf(VariableImpl(21, "ny paragraph"))))
        coEvery {
            brevbakerMock.renderMarkup(
                eq(RedigerbarBrevkode(Testbrevkoder.TESTBREV.name)),
                any(),
                eq(GeneriskRedigerbarBrevdata(Api.GeneriskBrevdata(), nyeValg)),
                any()
            )
        } returns ServiceResult.Ok(freshRender)

        val oppdatert = withPrincipal(principalMock()) {
            brevredigeringService.oppdaterBrev(
                saksId = sak.saksId,
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
        val oppdatert = withPrincipal(principalMock()) {
            brevredigeringService.oppdaterBrev(
                saksId = sak.saksId,
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
                eq(Testbrevkoder.TESTBREV),
                any(),
                eq(GeneriskRedigerbarBrevdata(EmptyBrevdata, saksbehandlerValg)),
                any()
            )
        } coAnswers {
            delay(10.minutes)
            ServiceResult.Ok(letter)
        }


        val result = withTimeout(10.seconds) {
            withPrincipal(principalMock()) {
                brevredigeringService.oppdaterBrev(
                    saksId = sak.saksId,
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
            brevredigeringService.hentBrev(saksId = sak.saksId, brevId = brev.info.id)
                ?.resultOrNull()
        )
        assertThat(brevredigeringService.slettBrev(saksId = sak.saksId, brevId = brev.info.id)).isTrue()
        assertThat(brevredigeringService.hentBrev(saksId = sak.saksId, brevId = brev.info.id)).isNull()
    }

    @Test
    fun `delete brevredigering returns false for non-existing brev`(): Unit = runBlocking {
        assertThat(brevredigeringService.hentBrev(saksId = sak.saksId, brevId = 1337)).isNull()
        assertThat(brevredigeringService.slettBrev(saksId = sak.saksId, brevId = 1337)).isFalse()
    }

    @Test
    fun `hentPdf skal opprette et Document med referanse til Brevredigering`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        transaction {
            assertThat(Brevredigering[brev.info.id].document).isEmpty()
            assertThat(Document.find { DocumentTable.brevredigering.eq(brev.info.id) }).isEmpty()
        }

        assertThat(
            brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)?.resultOrNull()
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

        brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)
        transaction { assertThat(Document.find { DocumentTable.brevredigering eq brev.info.id }).hasSize(1) }

        brevredigeringService.slettBrev(saksId = sak.saksId, brevId = brev.info.id)
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
                brevredigeringService.hentEllerOpprettPdf(
                    sak.saksId,
                    brev.info.id
                )
            }
        }.toTypedArray())

        transaction {
            assertThat(Document.find { DocumentTable.brevredigering eq brev.info.id }).hasSize(1)
        }
    }

    @Test
    fun `Document redigertBrevHash er stabil`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)
        val firstHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

        transaction { Brevredigering[brev.info.id].document.first().delete() }
        brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)
        val secondHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

        assertThat(firstHash).isEqualTo(secondHash)
    }

    @Test
    fun `Document redigertBrevHash endres basert paa redigertBrev`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)
        val firstHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

        transaction {
            Brevredigering[brev.info.id].redigertBrev =
                letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "blue pill")))).toEdit()
        }
        brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)
        val secondHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

        assertThat(firstHash).isNotEqualTo(secondHash)
    }

    @Test
    fun `hentPdf rendrer ny pdf om den ikke eksisterer`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        val pdf = brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)

        assertThat(pdf?.resultOrNull()?.toString(Charsets.UTF_8)).isEqualTo(stagetPDF.toString(Charsets.UTF_8))
    }

    @Test
    fun `hentPdf rendrer ny pdf om den ikke er basert paa gjeldende redigertBrev`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        stagePdf("min første pdf".encodeToByteArray())
        brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)

        transaction {
            Brevredigering[brev.info.id].redigertBrev =
                letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "blue pill")))).toEdit()
        }

        stagePdf("min andre pdf".encodeToByteArray())
        val pdf = brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)?.resultOrNull()
            ?.toString(Charsets.UTF_8)

        assertEquals("min andre pdf", pdf)
    }

    @Test
    fun `hentPdf rendrer ikke ny pdf om den er basert paa gjeldende redigertBrev`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        stagePdf("min første pdf".encodeToByteArray())
        val first = brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)?.resultOrNull()

        stagePdf("min andre pdf".encodeToByteArray())
        val second = brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)?.resultOrNull()

        assertThat(first).isEqualTo(second)
    }

    @Test
    fun `attesterer hvis avsender har attestantrolle`(): Unit = runBlocking {
        Features.override(Features.attestant, true)
        clearMocks(brevbakerMock, penService)

        coEvery { penService.hentPesysBrevdata(any(), any(), any(), any()) } returns ServiceResult.Ok(brevdataResponseData)

        coEvery { brevbakerMock.renderPdf(any(), any(), any(), any(), any()) } returns ServiceResult.Ok(letterResponse)
        coEvery { brevbakerMock.renderMarkup(any(), any(), any(), any()) } returns ServiceResult.Ok(letter)
        val meta = templateDescription.copy(metadata = lettermetadata.copy(brevtype = LetterMetadata.Brevtype.VEDTAKSBREV))
        coEvery { brevbakerMock.getRedigerbarTemplate(any()) } returns meta

        ADGroups.init(lesInnADGrupper())
        val brev = opprettBrev(
            reserverForRedigering = false,
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) },
            vedtaksId = 1
        ).resultOrNull()!!

        val navident = NavIdent("A12345")
        withPrincipal(MockPrincipal(navident, "Peder Ås", mutableSetOf(ADGroups.attestant))) {
            brevredigeringService.oppdaterSignaturAttestant(brev.info.id, "Lars Holm")
            assertEquals("Lars Holm", brevredigeringService.hentSignaturAttestant(sak.saksId, brev.info.id)?.resultOrNull())
            brevredigeringService.delvisOppdaterBrev(
                saksId = sak.saksId,
                brevId = brev.info.id,
                laastForRedigering = true,
                distribusjonstype = Distribusjonstype.SENTRALPRINT
            )
            brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)!!
            brevredigeringService.attester(sak.saksId, brev.info.id)
        }

        coVerify {
            penService.hentPesysBrevdata(
                eq(sak.saksId),
                eq(1),
                eq(Testbrevkoder.TESTBREV),
                eq(principalNavEnhetId),
            )
        }
    }

    @Test
    fun `attesterer ikke hvis avsender ikke har attestantrolle`(): Unit = runBlocking {
        Features.override(Features.attestant, true)
        clearMocks(brevbakerMock, penService)

        coEvery { penService.hentPesysBrevdata(any(), any(), any(), any()) } returns ServiceResult.Ok(brevdataResponseData)

        coEvery { brevbakerMock.renderPdf(any(), any(), any(), any(), any()) } returns ServiceResult.Ok(letterResponse)
        coEvery { brevbakerMock.renderMarkup(any(), any(), any(), any()) } returns ServiceResult.Ok(letter)
        val meta = templateDescription.copy(metadata = lettermetadata.copy(brevtype = LetterMetadata.Brevtype.VEDTAKSBREV))
        coEvery { brevbakerMock.getRedigerbarTemplate(any()) } returns meta

        ADGroups.init(lesInnADGrupper())
        val brev = opprettBrev(
            reserverForRedigering = false,
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) },
            vedtaksId = 1
        ).resultOrNull()!!

        val navident = NavIdent("A12345")
        withPrincipal(MockPrincipal(navident, "Peder Ås", mutableSetOf())) {
            brevredigeringService.oppdaterSignaturAttestant(brev.info.id, "Lars Holm")
            brevredigeringService.delvisOppdaterBrev(
                saksId = sak.saksId,
                brevId = brev.info.id,
                laastForRedigering = true,
                distribusjonstype = Distribusjonstype.SENTRALPRINT
            )
            brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)!!
            assertThrows<HarIkkeAttestantrolleException> {
                brevredigeringService.attester(sak.saksId, brev.info.id)
            }
        }

        coVerify {
            penService.hentPesysBrevdata(any(), any(), any(), any())
        }
    }

    @Test
    fun `distribuerer sentralprint brev`(): Unit = runBlocking {
        clearMocks(brevbakerMock, penService)

        coEvery { penService.hentPesysBrevdata(any(), isNull(), any(), any()) } returns ServiceResult.Ok(brevdataResponseData)
        coEvery { penService.sendbrev(any(), any()) } returns ServiceResult.Ok(Pen.BestillBrevResponse(123, null))

        coEvery { brevbakerMock.renderPdf(any(), any(), any(), any(), any()) } returns ServiceResult.Ok(letterResponse)
        coEvery { brevbakerMock.renderMarkup(any(), any(), any(), any()) } returns ServiceResult.Ok(letter)
        coEvery { brevbakerMock.getRedigerbarTemplate(any()) } returns templateDescription

        val brev = opprettBrev(
            reserverForRedigering = false,
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) }
        ).resultOrNull()!!

        withPrincipal(principalMock()) {
            brevredigeringService.delvisOppdaterBrev(
                saksId = sak.saksId,
                brevId = brev.info.id,
                laastForRedigering = true,
                distribusjonstype = Distribusjonstype.SENTRALPRINT
            )
            brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)!!
            brevredigeringService.sendBrev(sak.saksId, brev.info.id)
        }

        coVerify {
            penService.hentPesysBrevdata(
                eq(sak.saksId),
                isNull(),
                eq(Testbrevkoder.TESTBREV),
                eq(principalNavEnhetId),
            )
        }
        coVerify {
            penService.sendbrev(
                eq(
                    Pen.SendRedigerbartBrevRequest(
                        templateDescription = templateDescription,
                        dokumentDato = LocalDate.now(),
                        saksId = 1234,
                        brevkode = RedigerbarBrevkode(Testbrevkoder.TESTBREV.name),
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

        coEvery { penService.hentPesysBrevdata(any(), isNull(), any(), any()) } returns ServiceResult.Ok(brevdataResponseData)
        coEvery { penService.sendbrev(any(), any()) } returns ServiceResult.Ok(Pen.BestillBrevResponse(123, null))

        coEvery { brevbakerMock.renderPdf(any(), any(), any(), any(), any()) } returns ServiceResult.Ok(letterResponse)
        coEvery { brevbakerMock.renderMarkup(any(), any(), any(), any()) } returns ServiceResult.Ok(letter)
        coEvery { brevbakerMock.getRedigerbarTemplate(any()) } returns templateDescription

        val brev = opprettBrev(
            reserverForRedigering = false,
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) }
        ).resultOrNull()!!

        withPrincipal(principalMock()) {
            brevredigeringService.delvisOppdaterBrev(
                saksId = sak.saksId,
                brevId = brev.info.id,
                laastForRedigering = true,
                distribusjonstype = Distribusjonstype.LOKALPRINT,
            )
            brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)!!
            brevredigeringService.sendBrev(sak.saksId, brev.info.id)
        }

        coVerify {
            penService.hentPesysBrevdata(
                eq(sak.saksId),
                isNull(),
                eq(Testbrevkoder.TESTBREV),
                eq(principalNavEnhetId),
            )
        }
        coVerify {
            penService.sendbrev(
                eq(
                    Pen.SendRedigerbartBrevRequest(
                        templateDescription = templateDescription,
                        dokumentDato = LocalDate.now(),
                        saksId = 1234,
                        brevkode = RedigerbarBrevkode(Testbrevkoder.TESTBREV.name),
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

        assertThat(brev.info.redigeresAv).isEqualTo(principalNavIdent)
        assertThat(brev.info.sistReservert).isBetween(Instant.now() - 10.minutes.toJavaDuration(), Instant.now())
        assertThat(transaction { Brevredigering[brev.info.id].redigeresAvNavIdent }).isEqualTo(principalNavIdent)
    }

    @Test
    fun `brev kan reserveres for redigering gjennom hent brev`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = false).resultOrNull()!!

        assertThat(brev.info.laastForRedigering).isFalse()
        assertThat(brev.info.redigeresAv).isNull()

        val hentetBrev = withPrincipal(principalMock()) {
            brevredigeringService.hentBrev(
                saksId = sak.saksId,
                brevId = brev.info.id,
                reserverForRedigering = true
            )?.resultOrNull()!!
        }

        assertThat(hentetBrev.info.laastForRedigering).isFalse()
        assertThat(hentetBrev.info.redigeresAv).isEqualTo(principalNavIdent)
    }

    @Test
    fun `allerede reservert brev kan ikke resereveres for redigering`() {
        runBlocking {
            val brev = opprettBrev(principalMock(principalNavIdent), reserverForRedigering = true).resultOrNull()!!

            assertThrows<KanIkkeReservereBrevredigeringException> {
                withPrincipal(principalMock(principalNavIdent2)) {
                    brevredigeringService.hentBrev(
                        saksId = sak.saksId,
                        brevId = brev.info.id,
                        reserverForRedigering = true
                    )?.resultOrNull()!!
                }
            }
            assertThat(transaction { Brevredigering[brev.info.id].redigeresAvNavIdent }).isEqualTo(principalNavIdent)
        }
    }

    @Test
    fun `kun en som vinner reservasjon av et brev`() {
        runBlocking {
            val brev = opprettBrev(principalMock(principalNavIdent), reserverForRedigering = false).resultOrNull()!!

            coEvery {
                brevbakerMock.renderMarkup(
                    eq(Testbrevkoder.TESTBREV),
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
                        withPrincipal(principalMock(NavIdent("id-$it"))) {
                            brevredigeringService.hentBrev(
                                saksId = sak.saksId,
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
            withPrincipal(principalMock(principalNavIdent2)) {
                brevredigeringService.oppdaterBrev(
                    saksId = sak.saksId,
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

        val pdf = brevredigeringService.hentEllerOpprettPdf(brev.info.saksId, brev.info.id)?.resultOrNull()
        assertThat(pdf).isNotNull()
        withPrincipal(principalMock()) {
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

        withPrincipal(principalMock()) {
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
    fun `kan ikke sende brev som ikke er markert klar til sending`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        brevredigeringService.hentEllerOpprettPdf(brev.info.saksId, brev.info.id)?.resultOrNull()

        assertThrows<BrevIkkeKlartTilSendingException> {
            brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)
        }
    }

    @Test
    fun `arkivert brev men ikke distribuert kan sendes`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        val pdf = brevredigeringService.hentEllerOpprettPdf(brev.info.saksId, brev.info.id)?.resultOrNull()
        assertThat(pdf).isNotNull()

        coEvery { penService.sendbrev(any(), any()) } returns ServiceResult.Ok(
            Pen.BestillBrevResponse(
                991,
                Pen.BestillBrevResponse.Error(null, "Distribuering feilet", null)
            )
        )
        withPrincipal(principalMock()) {
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
            Brevredigering[brev.info.id].sistReservert = Instant.now() - RESERVASJON_TIMEOUT - 1.seconds.toJavaDuration()
        }

        val hentetBrev = brevredigeringService.hentBrev(saksId = sak.saksId, brevId = brev.info.id)?.resultOrNull()!!

        assertThat(hentetBrev.info.redigeresAv).isNull()

        val hentetBrevMedReservasjon = withPrincipal(principalMock(principalNavIdent2)) {
            brevredigeringService.hentBrev(
                saksId = sak.saksId,
                brevId = brev.info.id,
                reserverForRedigering = true
            )?.resultOrNull()!!
        }
        assertThat(hentetBrevMedReservasjon.info.redigeresAv).isEqualTo(principalNavIdent2)
    }

    @Test
    fun `brev reservasjon kan fornyes`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!

        val forrigeReservasjon = Instant.now().minusSeconds(60).truncatedTo(ChronoUnit.MILLIS)
        transaction { Brevredigering[brev.info.id].sistReservert = forrigeReservasjon }

        withPrincipal(principalMock()) { brevredigeringService.fornyReservasjon(brev.info.id) }
        assertThat(transaction { Brevredigering[brev.info.id].sistReservert })
            .isAfter(forrigeReservasjon)
            .isBetween(Instant.now().minusSeconds(1), Instant.now().plusSeconds(1))
    }

    @Test
    fun `brev reservasjon kan frigis ved oppdatering`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!
        assertThat(brev.info.redigeresAv).isEqualTo(principalNavIdent)

        val oppdatertBrev = withPrincipal(principalMock()) {
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
        assertThat(brev.info.redigeresAv).isEqualTo(principalNavIdent)

        val oppdatertBrev = withPrincipal(principalMock()) {
            brevredigeringService.oppdaterBrev(
                saksId = brev.info.saksId,
                brevId = brev.info.id,
                nyeSaksbehandlerValg = brev.saksbehandlerValg,
                nyttRedigertbrev = null,
                signatur = brev.info.signaturSignerende,
                frigiReservasjon = false,
            )?.resultOrNull()
        }

        assertThat(oppdatertBrev?.info?.redigeresAv).isEqualTo(principalNavIdent)
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
        assertTrue(brevredigeringService.fjernOverstyrtMottaker(brev.info.id, sak.saksId))

        assertNull(brevredigeringService.hentBrev(sak.saksId, brev.info.id)?.resultOrNull()?.info?.mottaker)
        assertNull(transaction { Mottaker.findById(brev.info.id) })
        assertNull(transaction { Brevredigering[brev.info.id].mottaker })
    }

    @Test
    fun `kan oppdatere mottaker av brev`(): Unit = runBlocking {
        coEvery { samhandlerService.hentSamhandlerNavn(eq("1")) } returns "Samhandler En"

        val brev = opprettBrev(mottaker = Dto.Mottaker.samhandler("1")).resultOrNull()!!
        val nyMottaker = Dto.Mottaker.norskAdresse("a", "b", "c", "d", "e", "f")

        val oppdatert = withPrincipal(principalMock()) { brevredigeringService.delvisOppdaterBrev(sak.saksId, brev.info.id, mottaker = nyMottaker) }
        assertEquals(nyMottaker, oppdatert?.info?.mottaker)
    }

    @Test
    fun `kan sette annen mottaker for eksisterende brev`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        val nyMottaker = Dto.Mottaker.utenlandskAdresse("a", "b", "c", "d", "e", "f", "g")

        val oppdatert = withPrincipal(principalMock()) { brevredigeringService.delvisOppdaterBrev(sak.saksId, brev.info.id, mottaker = nyMottaker) }
        assertEquals(nyMottaker, oppdatert?.info?.mottaker)
    }

    @Test
    fun `brev distribueres til annen mottaker`(): Unit = runBlocking {
        coEvery { samhandlerService.hentSamhandlerNavn(eq("987")) } returns "Ni Åtte Syv"

        val mottaker = Dto.Mottaker.samhandler("987")
        val brev = opprettBrev(mottaker = mottaker).resultOrNull()!!
        brevredigeringService.hentEllerOpprettPdf(sak.saksId, brev.info.id)
        withPrincipal(principalMock()) {
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)
        }

        brevredigeringService.sendBrev(sak.saksId, brev.info.id)

        coVerify {
            penService.sendbrev(
                eq(
                    Pen.SendRedigerbartBrevRequest(
                        templateDescription = templateDescription,
                        dokumentDato = LocalDate.now(),
                        saksId = sak.saksId,
                        brevkode = RedigerbarBrevkode(Testbrevkoder.TESTBREV.name),
                        enhetId = principalNavEnhetId,
                        pdf = stagetPDF,
                        eksternReferanseId = "skribenten:${brev.info.id}",
                        mottaker = Pen.SendRedigerbartBrevRequest.Mottaker(Pen.SendRedigerbartBrevRequest.Mottaker.Type.TSS_ID, mottaker.tssId, null, null)
                    )
                ),
                eq(true)
            )
        }
    }

    @Test
    fun `kan endre signerende saksbehandler signatur`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        withPrincipal(principalMock()) { brevredigeringService.oppdaterSignatur(brev.info.id, "en ny signatur") }

        assertEquals("en ny signatur", transaction { Brevredigering[brev.info.id].signaturSignerende })
    }

    @Test
    fun `kan tilbakestille brev`(): Unit = runBlocking {
        val brev = opprettBrev(saksbehandlerValg = Api.GeneriskBrevdata().apply {
            put("ytelse", "uføre")
            put("inkluderAfpTekst", false)
        }).resultOrNull()!!

        withPrincipal(principalMock()) {
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

        val tilbakestilt = withPrincipal(principalMock()) { brevredigeringService.tilbakestill(brev.info.id)?.resultOrNull()!! }
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
                eq(Testbrevkoder.TESTBREV), any(), any(), any()
            )
        } returns ServiceResult.Ok(
            letter(
                ParagraphImpl(
                    1,
                    true,
                    listOf(LiteralImpl(12, "Vi har "), LiteralImpl(13, "dato", tags = setOf(ElementTags.FRITEKST)), LiteralImpl(14, " mottatt søknad."))
                )
            )
        )
        val brev = opprettBrev().resultOrNull()!!

        // sjekk at delvis oppdatering fungerer uten å sette brevet til låst
        withPrincipal(principalMock()) {
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = false)
            brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, distribusjonstype = Distribusjonstype.SENTRALPRINT)
        }

        assertThrows<BrevIkkeKlartTilSendingException> {
            withPrincipal(principalMock()) {
                brevredigeringService.delvisOppdaterBrev(brev.info.saksId, brev.info.id, laastForRedigering = true)
            }
        }
    }

    @Test
    fun `kan markere brev klar til sending om alle fritekst er fylt ut`(): Unit = runBlocking {
        coEvery {
            brevbakerMock.renderMarkup(
                eq(Testbrevkoder.TESTBREV), any(), any(), any()
            )
        } returns ServiceResult.Ok(
            letter(
                ParagraphImpl(
                    1,
                    true,
                    listOf(LiteralImpl(12, "Vi har "), LiteralImpl(13, "dato", tags = setOf(ElementTags.FRITEKST)), LiteralImpl(14, " mottatt søknad."))
                )
            )
        )
        val brev = opprettBrev().resultOrNull()!!

        withPrincipal(principalMock()) {
            brevredigeringService.oppdaterBrev(
                brev.info.saksId, brev.info.id, nyeSaksbehandlerValg = null, nyttRedigertbrev = brev.redigertBrev.copy(
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

    private suspend fun opprettBrev(
        principal: UserPrincipal = principalMock(),
        reserverForRedigering: Boolean = false,
        mottaker: Dto.Mottaker? = null,
        saksbehandlerValg: SaksbehandlerValg = SaksbehandlerValg().apply { put("valg", true) },
        vedtaksId: Long? = null,
    ) = withPrincipal(principal) {
        brevredigeringService.opprettBrev(
            sak = sak,
            vedtaksId = vedtaksId,
            brevkode = Testbrevkoder.TESTBREV,
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

    private fun <T> condition(description: String, predicate: Predicate<T>): Condition<T> =
        Condition(predicate, description)
}

fun TemplateDescription.Redigerbar.copy(metadata: LetterMetadata) = TemplateDescription.Redigerbar(
    name = this.name,
    letterDataClass = this.letterDataClass,
    languages = this.languages,
    metadata = metadata,
    kategori = this.kategori,
    brevkontekst = this.brevkontekst,
    sakstyper = this.sakstyper
)