package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.*
import kotlinx.coroutines.*
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.db.*
import no.nav.pensjon.brev.skribenten.isInstanceOfSatisfying
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService.Companion.RESERVASJON_TIMEOUT
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block.Paragraph
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.Literal
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.Variable
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType
import org.apache.commons.codec.binary.Hex
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
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

    private val letter = letter(Paragraph(1, true, listOf(Literal(1, "red pill"))))
    private val stagetPDF = "nesten en pdf".encodeToByteArray()
    private val lettermetadata = LetterMetadata(
        displayTitle = "displayTitle",
        isSensitiv = false,
        distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
        brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
    )
    private val letterResponse = LetterResponse(file = stagetPDF, contentType = "pdf", letterMetadata = lettermetadata)
    private val templateDescription = TemplateDescription(
        name = "template name",
        letterDataClass = "template letter data class",
        languages = listOf(LanguageCode.ENGLISH),
        metadata = lettermetadata,
        kategori = null
    )

    private val brevbakerMock: BrevbakerService = mockk<BrevbakerService>()
    private val principalNavIdent = NavIdent("Agent Smith")
    private val principalNavn = "Hugo Weaving"
    private val principalNavIdent2 = NavIdent("Morpheus")
    private val principalNavn2 = "Laurence Fishburne"
    private val principalNavEnhetId = "Nebuchadnezzar"
    private fun callMock(ident: NavIdent = principalNavIdent) = mockk<ApplicationCall> {
        every { principal() } returns mockk<UserPrincipal> {
            every { navIdent() } returns ident
            every { navIdent } returns ident.id
            every { fullName } returns "Laurence Fishburne"
        }
    }

    private val sak = Pen.SakSelection(
        1234L,
        "12345678910",
        LocalDate.now().minusYears(42),
        Pen.SakType.ALDER,
        "rabbit"
    )

    private val brevdataResponseData = BrevdataResponse.Data(
        felles = Felles(
            dokumentDato = LocalDate.now(),
            saksnummer = sak.saksId.toString(),
            avsenderEnhet = NAVEnhet(
                nettside = "nav.no",
                navn = "en fantastisk enhet",
                telefonnummer = Telefonnummer("12345678")
            ),
            bruker = Bruker(
                foedselsnummer = Foedselsnummer("12345678910"),
                fornavn = "Navn",
                mellomnavn = null,
                etternavn = "Navnesen"
            ),
            vergeNavn = null,
            signerendeSaksbehandlere = null,
        ), brevdata = Api.GeneriskBrevdata()
    )
    private val penService: PenService = mockk {
        coEvery {
            hentPesysBrevdata(
                any(),
                eq(sak.saksId),
                eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID),
                any()
            )
        } returns ServiceResult.Ok(brevdataResponseData)
        coEvery { sendbrev(any(), any(), any()) } returns ServiceResult.Ok(Pen.BestillBrevResponse(123, null))
    }
    private val navAnsattService = mockk<NavansattService> {
        coEvery { harTilgangTilEnhet(any(), any(), any()) } returns ServiceResult.Ok(false)
        coEvery {
            harTilgangTilEnhet(
                any(),
                eq(principalNavIdent.id),
                eq(principalNavEnhetId)
            )
        } returns ServiceResult.Ok(true)
        coEvery {
            harTilgangTilEnhet(
                any(),
                eq(principalNavIdent2.id),
                eq(principalNavEnhetId)
            )
        } returns ServiceResult.Ok(true)
        coEvery { hentNavansatt(any(), any()) } returns null
        coEvery { hentNavansatt(any(), eq(principalNavIdent.id)) } returns Navansatt(
            emptyList(),
            principalNavn,
            "Hugo",
            "Weaving"
        )
        coEvery { hentNavansatt(any(), eq(principalNavIdent2.id)) } returns Navansatt(
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

    private fun brevredigeringService(
        brevbakerMock: BrevbakerService = this.brevbakerMock,
        penService: PenService = this.penService,
        navansattService: NavansattService = this.navAnsattService,
    ): BrevredigeringService = BrevredigeringService(
        brevbakerService = brevbakerMock,
        navansattService = navansattService,
        penService = penService,
    )

    @BeforeEach
    fun clearMocks() {
        clearMocks(brevbakerMock)
        coEvery {
            brevbakerMock.renderMarkup(
                any(),
                eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID),
                any(),
                any(),
                any()
            )
        } returns ServiceResult.Ok(letter)
        coEvery { brevbakerMock.getRedigerbarTemplate(any(), any()) } returns templateDescription
        stagePdf(stagetPDF)
    }

    @Test
    fun `non existing brevredingering returns null`(): Unit = runBlocking {
        assertThat(brevredigeringService.hentBrev(call = callMock(), saksId = sak.saksId, brevId = 99)).isNull()
    }

    @Test
    fun `can create and fetch brevredigering`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val brev =
            brevredigeringService.opprettBrev(
                call = callMock(),
                sak = sak,
                brevkode = Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
                spraak = LanguageCode.ENGLISH,
                avsenderEnhetsId = principalNavEnhetId,
                saksbehandlerValg = saksbehandlerValg,
                reserverForRedigering = true,
            ).resultOrNull()!!

        coVerify {
            brevbakerMock.renderMarkup(
                any(),
                eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID),
                eq(LanguageCode.ENGLISH),
                any(),
                any()
            )
        }
        clearMocks()

        assertEquals(
            brev.copy(info = brev.info.copy(sistReservert = null)),
            brevredigeringService.hentBrev(
                call = callMock(),
                saksId = sak.saksId,
                brevId = brev.info.id,
                reserverForRedigering = true,
            )?.resultOrNull()?.let { it.copy(info = it.info.copy(sistReservert = null)) }
        )
        assertEquals(brev.info.brevkode, Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID)
        assertEquals(brev.redigertBrev, letter.toEdit())

        coVerify {
            brevbakerMock.renderMarkup(
                any(),
                eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID),
                eq(LanguageCode.ENGLISH),
                any(),
                any()
            )
        }
    }

    @Test
    fun `brev must belong to provided saksId`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        assertThat(
            brevredigeringService.hentBrev(
                call = callMock(),
                saksId = sak.saksId + 1,
                brevId = brev.info.id
            )
        ).isNull()
        assertThat(
            brevredigeringService.oppdaterBrev(
                call = callMock(),
                saksId = sak.saksId + 1,
                brevId = brev.info.id,
                nyeSaksbehandlerValg = brev.saksbehandlerValg,
                nyttRedigertbrev = brev.redigertBrev
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
                call = callMock(),
                saksId = sak.saksId + 1,
                brevId = brev.info.id
            )
        ).isNull()

        // Må generere pdf nå, slik at sendBrev ikke returnerer null pga. manglende dokument
        brevredigeringService.hentEllerOpprettPdf(call = callMock(), saksId = sak.saksId, brevId = brev.info.id)
        assertThat(
            brevredigeringService.sendBrev(
                call = callMock(),
                saksId = sak.saksId + 1,
                brevId = brev.info.id
            )
        ).isNull()
        assertThat(brevredigeringService.slettBrev(saksId = sak.saksId + 1, brevId = brev.info.id)).isFalse()
    }

    @Test
    fun `cannot create brevredigering for a NavEnhet without access to it`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val result = brevredigeringService.opprettBrev(
            callMock(),
            sak,
            Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
            LanguageCode.ENGLISH,
            "The Matrix",
            saksbehandlerValg
        )
        assertThat(result).isInstanceOfSatisfying<ServiceResult.Error<*>> {
            assertThat(it.statusCode).isEqualTo(HttpStatusCode.Forbidden)
        }
    }

    @Test
    fun `can update brevredigering`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val original =
            brevredigeringService.opprettBrev(
                call = callMock(),
                sak = sak,
                brevkode = Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
                spraak = LanguageCode.ENGLISH,
                avsenderEnhetsId = principalNavEnhetId,
                saksbehandlerValg = saksbehandlerValg,
                reserverForRedigering = true,
            ).resultOrNull()!!

        clearMocks()

        val nyeValg = Api.GeneriskBrevdata().apply { put("valg2", true) }
        val oppdatert = brevredigeringService.oppdaterBrev(
            call = callMock(),
            saksId = sak.saksId,
            brevId = original.info.id,
            nyeSaksbehandlerValg = nyeValg,
            nyttRedigertbrev = letter.toEdit(),
        )?.resultOrNull()!!

        coVerify(exactly = 1) {
            brevbakerMock.renderMarkup(
                any(),
                eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID),
                eq(LanguageCode.ENGLISH),
                any(),
                any()
            )
        }

        assertThat(brevredigeringService.hentBrev(call = callMock(), saksId = sak.saksId, brevId = original.info.id))
            .isInstanceOfSatisfying<ServiceResult.Ok<*>> {
                assertThat(it.result).isEqualTo(oppdatert)
            }

        assertNotEquals(original.saksbehandlerValg, oppdatert.saksbehandlerValg)
    }

    @Test
    fun `updates redigertBrev with fresh rendering from brevbaker`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val original =
            brevredigeringService.opprettBrev(
                callMock(),
                sak,
                Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
                LanguageCode.ENGLISH,
                principalNavEnhetId,
                saksbehandlerValg
            ).resultOrNull()!!

        val nyeValg = Api.GeneriskBrevdata().apply { put("valg2", true) }
        val freshRender = letter.copy(blocks = letter.blocks + Paragraph(2, true, listOf(Variable(21, "ny paragraph"))))
        coEvery {
            brevbakerMock.renderMarkup(
                any(),
                eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID),
                any(),
                eq(GeneriskRedigerbarBrevdata(Api.GeneriskBrevdata(), nyeValg)),
                any()
            )
        } returns ServiceResult.Ok(freshRender)

        val oppdatert =
            brevredigeringService.oppdaterBrev(
                call = callMock(),
                saksId = sak.saksId,
                brevId = original.info.id,
                nyeSaksbehandlerValg = nyeValg,
                nyttRedigertbrev = letter.toEdit(),
            )?.resultOrNull()!!

        assertNotEquals(original.redigertBrev, oppdatert.redigertBrev)
        assertEquals(letter.toEdit().updateEditedLetter(freshRender), oppdatert.redigertBrev)
    }

    @Test
    fun `cannot update non-existing brevredigering`(): Unit = runBlocking {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val oppdatert =
            brevredigeringService.oppdaterBrev(
                call = callMock(),
                saksId = sak.saksId,
                brevId = 1099,
                nyeSaksbehandlerValg = saksbehandlerValg,
                nyttRedigertbrev = letter.toEdit()
            )?.resultOrNull()

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
                any(),
                eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID),
                any(),
                eq(GeneriskRedigerbarBrevdata(EmptyBrevdata, saksbehandlerValg)),
                any()
            )
        } coAnswers {
            delay(10.minutes)
            ServiceResult.Ok(letter)
        }


        val result = withTimeout(10.seconds) {
            brevredigeringService.oppdaterBrev(
                call = callMock(),
                saksId = sak.saksId,
                brevId = 2098,
                nyeSaksbehandlerValg = saksbehandlerValg,
                nyttRedigertbrev = letter.toEdit()
            )
        }

        assertThat(result).isNull()
    }

    @Test
    fun `can delete brevredigering`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        assertEquals(
            brev,
            brevredigeringService.hentBrev(call = callMock(), saksId = sak.saksId, brevId = brev.info.id)
                ?.resultOrNull()
        )
        assertTrue(brevredigeringService.slettBrev(saksId = sak.saksId, brevId = brev.info.id))
        assertThat(
            brevredigeringService.hentBrev(
                call = callMock(),
                saksId = sak.saksId,
                brevId = brev.info.id
            )
        ).isNull()
    }

    @Test
    fun `delete brevredigering returns false for non-existing brev`(): Unit = runBlocking {
        assertThat(brevredigeringService.hentBrev(call = callMock(), saksId = sak.saksId, brevId = 1337)).isNull()
        assertFalse(brevredigeringService.slettBrev(saksId = sak.saksId, brevId = 1337))
    }

    @Test
    fun `hentPdf skal opprette et Document med referanse til Brevredigering`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        transaction {
            assertThat(Brevredigering[brev.info.id].document).isEmpty()
            assertThat(Document.find { DocumentTable.brevredigering.eq(brev.info.id) }).isEmpty()
        }

        assertThat(
            brevredigeringService.hentEllerOpprettPdf(callMock(), sak.saksId, brev.info.id)?.resultOrNull()
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

        brevredigeringService.hentEllerOpprettPdf(callMock(), sak.saksId, brev.info.id)
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
                    callMock(),
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

        brevredigeringService.hentEllerOpprettPdf(callMock(), sak.saksId, brev.info.id)
        val firstHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

        transaction { Brevredigering[brev.info.id].document.first().delete() }
        brevredigeringService.hentEllerOpprettPdf(callMock(), sak.saksId, brev.info.id)
        val secondHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

        assertThat(firstHash).isEqualTo(secondHash)
    }

    @Test
    fun `Document redigertBrevHash endres basert paa redigertBrev`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        brevredigeringService.hentEllerOpprettPdf(callMock(), sak.saksId, brev.info.id)
        val firstHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

        transaction {
            Brevredigering[brev.info.id].redigertBrev =
                letter(Paragraph(1, true, listOf(Literal(1, "blue pill")))).toEdit()
        }
        brevredigeringService.hentEllerOpprettPdf(callMock(), sak.saksId, brev.info.id)
        val secondHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

        assertThat(firstHash).isNotEqualTo(secondHash)
    }

    @Test
    fun `hentPdf rendrer ny pdf om den ikke eksisterer`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        val pdf = brevredigeringService.hentEllerOpprettPdf(callMock(), sak.saksId, brev.info.id)

        assertThat(pdf?.resultOrNull()?.toString(Charsets.UTF_8)).isEqualTo(stagetPDF.toString(Charsets.UTF_8))
    }

    @Test
    fun `hentPdf rendrer ny pdf om den ikke er basert paa gjeldende redigertBrev`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        stagePdf("min første pdf".encodeToByteArray())
        brevredigeringService.hentEllerOpprettPdf(callMock(), sak.saksId, brev.info.id)

        transaction {
            Brevredigering[brev.info.id].redigertBrev =
                letter(Paragraph(1, true, listOf(Literal(1, "blue pill")))).toEdit()
        }

        stagePdf("min andre pdf".encodeToByteArray())
        val pdf = brevredigeringService.hentEllerOpprettPdf(callMock(), sak.saksId, brev.info.id)?.resultOrNull()
            ?.toString(Charsets.UTF_8)

        assertEquals("min andre pdf", pdf)
    }

    @Test
    fun `hentPdf rendrer ikke ny pdf om den er basert paa gjeldende redigertBrev`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        stagePdf("min første pdf".encodeToByteArray())
        val first = brevredigeringService.hentEllerOpprettPdf(callMock(), sak.saksId, brev.info.id)?.resultOrNull()

        stagePdf("min andre pdf".encodeToByteArray())
        val second = brevredigeringService.hentEllerOpprettPdf(callMock(), sak.saksId, brev.info.id)?.resultOrNull()

        assertThat(first).isEqualTo(second)
    }

    @Test
    fun `distribuerer brev`(): Unit = runBlocking {
        val caller = callMock()
        val mockedPenService = mockk<PenService> {
            coEvery { hentPesysBrevdata(any(), any(), any(), any()) } returns ServiceResult.Ok(brevdataResponseData)
            coEvery { sendbrev(any(), any(), any()) } returns ServiceResult.Ok(Pen.BestillBrevResponse(123, null))
        }
        val mockedBrevbaker = mockk<BrevbakerService> {
            coEvery { renderPdf(any(), any(), any(), any(), any(), any()) } returns ServiceResult.Ok(letterResponse)
            coEvery { renderMarkup(any(), any(), any(), any(), any()) } returns ServiceResult.Ok(letter)
            coEvery { getRedigerbarTemplate(any(), any()) } returns templateDescription
        }
        val service = brevredigeringService(penService = mockedPenService, brevbakerMock = mockedBrevbaker)

        val brev = service.opprettBrev(
            call = caller,
            sak = sak,
            brevkode = Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
            spraak = LanguageCode.ENGLISH,
            avsenderEnhetsId = null,
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) },
            reserverForRedigering = false
        ).resultOrNull()!!

        service.delvisOppdaterBrev(
            saksId = sak.saksId,
            brevId = brev.info.id,
            laastForRedigering = true,
            distribusjonstype = Distribusjonstype.SENTRALPRINT
        )
        service.hentEllerOpprettPdf(caller, sak.saksId, brev.info.id)!!
        service.sendBrev(caller, sak.saksId, brev.info.id)

        coVerify {
            mockedPenService.hentPesysBrevdata(
                any(),
                eq(sak.saksId),
                eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID),
                null,
            )
        }
        coVerify {
            mockedPenService.sendbrev(
                eq(caller),
                eq(
                    Pen.SendRedigerbartBrevRequest(
                        templateDescription = templateDescription,
                        dokumentDato = LocalDate.now(),
                        saksId = 1234,
                        brevkode = Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
                        enhetId = null,
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
    fun `distribuerer ikke brev`(): Unit = runBlocking {
        val caller = callMock()
        val mockedPenService = mockk<PenService> {
            coEvery { hentPesysBrevdata(any(), any(), any(), any()) } returns ServiceResult.Ok(brevdataResponseData)
            coEvery { sendbrev(any(), any(), any()) } returns ServiceResult.Ok(Pen.BestillBrevResponse(123, null))
        }
        val mockedBrevbaker = mockk<BrevbakerService> {
            coEvery { renderPdf(any(), any(), any(), any(), any(), any()) } returns ServiceResult.Ok(letterResponse)
            coEvery { renderMarkup(any(), any(), any(), any(), any()) } returns ServiceResult.Ok(letter)
            coEvery { getRedigerbarTemplate(any(), any()) } returns templateDescription
        }
        val service = brevredigeringService(penService = mockedPenService, brevbakerMock = mockedBrevbaker)

        val brev = service.opprettBrev(
            call = caller,
            sak = sak,
            brevkode = Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
            spraak = LanguageCode.ENGLISH,
            avsenderEnhetsId = null,
            saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg", true) },
            reserverForRedigering = false
        ).resultOrNull()!!

        service.delvisOppdaterBrev(
            saksId = sak.saksId,
            brevId = brev.info.id,
            laastForRedigering = true,
            distribusjonstype = Distribusjonstype.LOKALPRINT,
        )
        service.hentEllerOpprettPdf(caller, sak.saksId, brev.info.id)!!
        service.sendBrev(caller, sak.saksId, brev.info.id)

        coVerify {
            mockedPenService.hentPesysBrevdata(
                any(),
                eq(sak.saksId),
                eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID),
                null,
            )
        }
        coVerify {
            mockedPenService.sendbrev(
                caller,
                Pen.SendRedigerbartBrevRequest(
                    templateDescription = templateDescription,
                    dokumentDato = LocalDate.now(),
                    saksId = 1234,
                    brevkode = Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
                    enhetId = null,
                    pdf = stagetPDF,
                    eksternReferanseId = "skribenten:${brev.info.id}",
                    mottaker = null,
                ),
                distribuer = false
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

        val hentetBrev = brevredigeringService.hentBrev(
            call = callMock(),
            saksId = sak.saksId,
            brevId = brev.info.id,
            reserverForRedigering = true
        )?.resultOrNull()!!

        assertThat(hentetBrev.info.laastForRedigering).isFalse()
        assertThat(hentetBrev.info.redigeresAv).isEqualTo(principalNavIdent)
    }

    @Test
    fun `allerede reservert brev kan ikke resereveres for redigering`() {
        runBlocking {
            val brev = opprettBrev(callMock(principalNavIdent), reserverForRedigering = true).resultOrNull()!!

            assertThrows<KanIkkeReservereBrevredigeringException> {
                brevredigeringService.hentBrev(
                    call = callMock(principalNavIdent2),
                    saksId = sak.saksId,
                    brevId = brev.info.id,
                    reserverForRedigering = true
                )
                    ?.resultOrNull()!!
            }
            assertThat(transaction { Brevredigering[brev.info.id].redigeresAvNavIdent }).isEqualTo(principalNavIdent)
        }
    }

    @Test
    fun `kun en som vinner reservasjon av et brev`() {
        runBlocking {
            val brev = opprettBrev(callMock(principalNavIdent), reserverForRedigering = false).resultOrNull()!!

            coEvery {
                brevbakerMock.renderMarkup(
                    any(),
                    eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID),
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
                        brevredigeringService.hentBrev(
                            call = callMock(NavIdent("id-$it")),
                            saksId = sak.saksId,
                            brevId = brev.info.id,
                            reserverForRedigering = true
                        )
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
            brevredigeringService.oppdaterBrev(
                call = callMock(principalNavIdent2),
                saksId = sak.saksId,
                brevId = brev.info.id,
                nyeSaksbehandlerValg = brev.saksbehandlerValg,
                nyttRedigertbrev = letter(Paragraph(1, true, listOf(Literal(1, "blue pill")))).toEdit()
            )
        }
        transaction {
            assertThat(Brevredigering[brev.info.id].redigertBrev == brev.redigertBrev)
        }
    }

    @Test
    fun `brev reservasjon utloeper`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!

        transaction {
            Brevredigering[brev.info.id].sistReservert = Instant.now() - RESERVASJON_TIMEOUT - 1.seconds.toJavaDuration()
        }

        val hentetBrev = brevredigeringService.hentBrev(call = callMock(), saksId = sak.saksId, brevId = brev.info.id)
            ?.resultOrNull()!!

        assertThat(hentetBrev.info.redigeresAv).isNull()

        val hentetBrevMedReservasjon = brevredigeringService.hentBrev(
            call = callMock(principalNavIdent2),
            saksId = sak.saksId,
            brevId = brev.info.id,
            reserverForRedigering = true
        )?.resultOrNull()!!
        assertThat(hentetBrevMedReservasjon.info.redigeresAv).isEqualTo(principalNavIdent2)
    }

    @Test
    fun `brev reservasjon kan fornyes`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!

        val forrigeReservasjon = Instant.now().minusSeconds(60).truncatedTo(ChronoUnit.MILLIS)
        transaction { Brevredigering[brev.info.id].sistReservert = forrigeReservasjon }

        brevredigeringService.fornyReservasjon(callMock(), brev.info.id)
        assertThat(transaction { Brevredigering[brev.info.id].sistReservert })
            .isAfter(forrigeReservasjon)
            .isBetween(Instant.now().minusSeconds(1), Instant.now().plusSeconds(1))
    }

    @Test
    fun `brev reservasjon kan frigis ved oppdatering`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!
        assertThat(brev.info.redigeresAv).isEqualTo(principalNavIdent)

        val oppdatertBrev = brevredigeringService.oppdaterBrev(
            call = callMock(),
            saksId = brev.info.saksId,
            brevId = brev.info.id,
            nyeSaksbehandlerValg = brev.saksbehandlerValg,
            nyttRedigertbrev = null,
            frigiReservasjon = true,
        )?.resultOrNull()

        assertThat(oppdatertBrev?.info?.redigeresAv).isNull()
    }

    @Test
    fun `brev reservasjon frigis ikke ved oppdatering`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!
        assertThat(brev.info.redigeresAv).isEqualTo(principalNavIdent)

        val oppdatertBrev = brevredigeringService.oppdaterBrev(
            call = callMock(),
            saksId = brev.info.saksId,
            brevId = brev.info.id,
            nyeSaksbehandlerValg = brev.saksbehandlerValg,
            nyttRedigertbrev = null,
            frigiReservasjon = false,
        )?.resultOrNull()

        assertThat(oppdatertBrev?.info?.redigeresAv).isEqualTo(principalNavIdent)
    }

    @Test
    fun `oppdatering av redigertBrev endrer ogsaa redigertBrevHash`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!
        val hash1 = transaction { Brevredigering[brev.info.id].redigertBrevHash }
        assertThat(hash1.hex).isEqualTo(Hex.encodeHexString(WithEditLetterHash.hashBrev(letter.toEdit())))

        transaction {
            Brevredigering[brev.info.id].redigertBrev =
                letter(Paragraph(1, true, listOf(Literal(1, "blue pill")))).toEdit()
        }

        val hash2 = transaction { Brevredigering[brev.info.id].redigertBrevHash }
        assertThat(hash2).isNotEqualTo(hash1)
    }

    @Test
    fun `kan overstyre mottaker av brev`(): Unit = runBlocking {
        coEvery { samhandlerService.hentSamhandlerNavn(any(), eq("samhandlerId")) } returns "hei"

        val mottaker = Dto.Mottaker.samhandler("samhandlerId")
        val brev = opprettBrev(mottaker = mottaker)

        assertEquals(mottaker, brev.resultOrNull()?.info?.mottaker)
        assertEquals(mottaker.tssId, transaction { Mottaker[brev.resultOrNull()!!.info.id].tssId })
    }

    @Test
    fun `kan fjerne overstyrt mottaker av brev`(): Unit = runBlocking {
        coEvery { samhandlerService.hentSamhandlerNavn(any(), eq("samhandlerId")) } returns "hei"
        val mottaker = Dto.Mottaker.samhandler("samhandlerId")
        val brev = opprettBrev(mottaker = mottaker).resultOrNull()!!
        assertTrue(brevredigeringService.fjernOverstyrtMottaker(brev.info.id, sak.saksId))

        assertNull(brevredigeringService.hentBrev(callMock(), sak.saksId, brev.info.id)?.resultOrNull()?.info?.mottaker)
        assertNull(transaction { Mottaker.findById(brev.info.id) })
        assertNull(transaction { Brevredigering[brev.info.id].mottaker })
    }

    @Test
    fun `kan oppdatere mottaker av brev`(): Unit = runBlocking {
        coEvery { samhandlerService.hentSamhandlerNavn(any(), eq("1")) } returns "Samhandler En"

        val brev = opprettBrev(mottaker = Dto.Mottaker.samhandler("1")).resultOrNull()!!
        val nyMottaker = Dto.Mottaker.norskAdresse("a", "b", "c", "d", "e", "f")

        val oppdatert = brevredigeringService.delvisOppdaterBrev(sak.saksId, brev.info.id, mottaker = nyMottaker)
        assertEquals(nyMottaker, oppdatert?.info?.mottaker)
    }

    @Test
    fun `kan sette annen mottaker for eksisterende brev`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        val nyMottaker = Dto.Mottaker.utenlandskAdresse("a", "b", "c", "d", "e", "f", "g")

        val oppdatert = brevredigeringService.delvisOppdaterBrev(sak.saksId, brev.info.id, mottaker = nyMottaker)
        assertEquals(nyMottaker, oppdatert?.info?.mottaker)
    }

    @Test
    fun `brev distribueres til annen mottaker`(): Unit = runBlocking {
        coEvery { samhandlerService.hentSamhandlerNavn(any(), eq("987")) } returns "Ni Åtte Syv"

        val mottaker = Dto.Mottaker.samhandler("987")
        val brev = opprettBrev(mottaker = mottaker).resultOrNull()!!

        brevredigeringService.hentEllerOpprettPdf(callMock(), sak.saksId, brev.info.id)
        brevredigeringService.sendBrev(callMock(), sak.saksId, brev.info.id)

        coVerify {
            penService.sendbrev(
                any(),
                eq(
                    Pen.SendRedigerbartBrevRequest(
                        templateDescription = templateDescription,
                        dokumentDato = LocalDate.now(),
                        saksId = sak.saksId,
                        brevkode = Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
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
        brevredigeringService.oppdaterSignatur(callMock(), brev.info.id, "en ny signatur")

        assertEquals("en ny signatur", transaction { Brevredigering[brev.info.id].signaturSignerende })
    }

    @Test
    fun `kan tilbakestille brev`(): Unit = runBlocking {
        val brev = opprettBrev(saksbehandlerValg = Api.GeneriskBrevdata().apply {
            put("ytelse", "uføre")
            put("inkluderAfpTekst", false)
        }).resultOrNull()!!

        brevredigeringService.oppdaterBrev(
            call = callMock(),
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
                    listOf(E_Literal(null, "", "and blue pill"))
                )
            ),
            frigiReservasjon = false,
        )?.resultOrNull()!!

        coEvery { brevbakerMock.getModelSpecification(any(), eq(brev.info.brevkode)) } returns ServiceResult.Ok(TemplateModelSpecification(
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
        ))

        val tilbakestilt = brevredigeringService.tilbakestill(callMock(), brev.info.id)?.resultOrNull()!!
        assertThat(tilbakestilt.redigertBrev).isEqualTo(letter.toEdit())
        assertThat(tilbakestilt.saksbehandlerValg).isEqualTo(Api.GeneriskBrevdata().apply {
            put("ytelse", "uføre")
            put("inkluderAfpTekst", false)
            put("land", null)
        })
    }

    private suspend fun opprettBrev(
        call: ApplicationCall = callMock(),
        reserverForRedigering: Boolean = false,
        mottaker: Dto.Mottaker? = null,
        saksbehandlerValg: SaksbehandlerValg = SaksbehandlerValg().apply { put("valg", true) },
    ) =
        brevredigeringService.opprettBrev(
            call = call,
            sak = sak,
            brevkode = Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
            spraak = LanguageCode.ENGLISH,
            avsenderEnhetsId = principalNavEnhetId,
            saksbehandlerValg = saksbehandlerValg,
            reserverForRedigering = reserverForRedigering,
            mottaker = mottaker,
        )

    private fun stagePdf(pdf: ByteArray) {
        coEvery { brevbakerMock.renderPdf(any(), any(), any(), any(), any(), any()) } returns ServiceResult.Ok(
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

