package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.*
import kotlinx.coroutines.*
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.db.Brevredigering
import no.nav.pensjon.brev.skribenten.db.Document
import no.nav.pensjon.brev.skribenten.db.DocumentTable
import no.nav.pensjon.brev.skribenten.db.initDatabase
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block.Paragraph
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.Literal
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.Variable
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.testcontainers.containers.PostgreSQLContainer
import java.time.Instant
import java.time.LocalDate
import java.util.*
import java.util.function.Predicate
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class BrevredigeringServiceTest {
    private class GeneriskBrevData : LinkedHashMap<String, Any>(), BrevbakerBrevdata

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

    private val brevbakerMock: BrevbakerService = mockk<BrevbakerService>()
    private val principalNavIdent = "Agent Smith"
    private val principalNavIdent2 = "Morpheus"
    private val principalNavEnhetId = "Nebuchadnezzar"
    private fun callMock(ident: String = principalNavIdent) = mockk<ApplicationCall> {
        every { principal() } returns mockk<UserPrincipal> {
            every { navIdent } returns ident
        }
    }

    private val sak = Pen.SakSelection(
        1234L,
        "12345678910",
        LocalDate.now().minusYears(42),
        Pen.SakType.ALDER,
        "rabbit"
    )

    private val penService: PenService = mockk {
        coEvery { hentPesysBrevdata(any(), eq(sak.saksId), eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID), any()) } returns
                ServiceResult.Ok(
                    BrevdataResponse.Data(
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
                    ),
                )
    }
    private val navAnsattService = mockk<NavansattService> {
        coEvery { harTilgangTilEnhet(any(), any(), any()) } returns ServiceResult.Ok(false)
        coEvery { harTilgangTilEnhet(any(), eq(principalNavIdent), eq(principalNavEnhetId)) } returns ServiceResult.Ok(true)
        coEvery { harTilgangTilEnhet(any(), eq(principalNavIdent2), eq(principalNavEnhetId)) } returns ServiceResult.Ok(true)
    }
    private val brevredigeringService: BrevredigeringService = BrevredigeringService(
        brevbakerService = brevbakerMock,
        penService = penService,
        navansattService = navAnsattService,
    )

    @BeforeEach
    fun clearMocks() {
        clearMocks(brevbakerMock)
        coEvery { brevbakerMock.renderMarkup(any(), eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID), any(), any(), any()) } returns ServiceResult.Ok(
            letter
        )
    }

    @Test
    fun `non existing brevredingering returns null`(): Unit = runBlocking {
        assertThat(brevredigeringService.hentBrev(callMock(), 99)).isNull()
    }

    @Test
    fun `can create and fetch brevredigering`() = runBlocking {
        val saksbehandlerValg = GeneriskBrevData().apply { put("valg1", true) }
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

        assertEquals(brev, brevredigeringService.hentBrev(callMock(), brev.info.id, reserverForRedigering = true)?.resultOrNull())
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
    fun `cannot create brevredigering for a NavEnhet without access to it`(): Unit = runBlocking {
        val saksbehandlerValg = GeneriskBrevData().apply { put("valg1", true) }
        val result = brevredigeringService.opprettBrev(
            callMock(),
            sak,
            Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
            LanguageCode.ENGLISH,
            "The Matrix",
            saksbehandlerValg
        )
        assertThat(result).isInstanceOfSatisfying(ServiceResult.Error::class.java) {
            assertThat(it.statusCode).isEqualTo(HttpStatusCode.Forbidden)
        }
    }

    @Test
    fun `can update brevredigering`() = runBlocking {
        val saksbehandlerValg = GeneriskBrevData().apply { put("valg1", true) }
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

        val nyeValg = GeneriskBrevData().apply { put("valg2", true) }
        val oppdatert = brevredigeringService.oppdaterBrev(
            callMock(),
            original.info.id,
            nyeValg,
            letter.toEdit(),
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

        assertThat(brevredigeringService.hentBrev(callMock(), original.info.id))
            .isInstanceOfSatisfying(ServiceResult.Ok::class.java) {
                assertThat(it.result).isEqualTo(oppdatert)
            }

        assertNotEquals(original.saksbehandlerValg, oppdatert.saksbehandlerValg)
    }

    @Test
    fun `updates redigertBrev with fresh rendering from brevbaker`() = runBlocking {
        val saksbehandlerValg = GeneriskBrevData().apply { put("valg1", true) }
        val original =
            brevredigeringService.opprettBrev(
                callMock(),
                sak,
                Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
                LanguageCode.ENGLISH,
                principalNavEnhetId,
                saksbehandlerValg
            ).resultOrNull()!!

        val nyeValg = GeneriskBrevData().apply { put("valg2", true) }
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
                callMock(),
                original.info.id,
                nyeValg,
                letter.toEdit(),
            )?.resultOrNull()!!

        assertNotEquals(original.redigertBrev, oppdatert.redigertBrev)
        assertEquals(letter.toEdit().updateEditedLetter(freshRender), oppdatert.redigertBrev)
    }

    @Test
    fun `cannot update non-existing brevredigering`() = runBlocking {
        val saksbehandlerValg = GeneriskBrevData().apply { put("valg1", true) }
        val oppdatert =
            brevredigeringService.oppdaterBrev(callMock(), 1099, saksbehandlerValg, letter.toEdit())
                ?.resultOrNull()

        assertNull(oppdatert)
    }

    @Test
    fun `does not wait for brevbaker before answering with brevredigering does not exist`(): Unit = runBlocking {
        val saksbehandlerValg = GeneriskBrevData().apply {
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
            brevredigeringService.oppdaterBrev(callMock(), 2098, saksbehandlerValg, letter.toEdit())
        }

        assertThat(result).isNull()
    }

    @Test
    fun `can delete brevredigering`(): Unit = runBlocking {
        val saksbehandlerValg = GeneriskBrevData().apply { put("valg1", true) }
        val result =
            brevredigeringService.opprettBrev(
                callMock(),
                sak,
                Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
                LanguageCode.ENGLISH,
                principalNavEnhetId,
                saksbehandlerValg
            )

        assertEquals(result, brevredigeringService.hentBrev(callMock(), result.resultOrNull()!!.info.id))
        assertTrue(brevredigeringService.slettBrev(result.resultOrNull()!!.info.id))
        assertThat(brevredigeringService.hentBrev(callMock(), result.resultOrNull()!!.info.id)).isNull()
    }

    @Test
    fun `delete brevredigering returns false for non-existing brev`(): Unit = runBlocking {
        assertThat(brevredigeringService.hentBrev(callMock(), 1337)).isNull()
        assertFalse(brevredigeringService.slettBrev(1337))
    }

    @Test
    fun `hentPdf skal opprette et Document med referanse til Brevredigering`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        transaction {
            assertThat(Brevredigering[brev.info.id].document).isEmpty()
            assertThat(Document.find { DocumentTable.brevredigering.eq(brev.info.id) }).isEmpty()
        }

        val pdf = "nesten en pdf".encodeToByteArray()
        stagePdf(pdf)

        assertThat(brevredigeringService.hentEllerOpprettPdf(callMock(), brev.info.id)?.resultOrNull()).isEqualTo(pdf)

        transaction {
            val brevredigering = Brevredigering[brev.info.id]
            assertThat(brevredigering.document).hasSize(1)
            assertThat(Document.find { DocumentTable.brevredigering.eq(brev.info.id) }).hasSize(1)
            assertThat(brevredigering.document.first().pdf.bytes).isEqualTo(pdf)
        }
    }

    @Test
    fun `sletter relaterte documents ved sletting av brevredigering`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        stagePdf("a real life pdf".encodeToByteArray())

        brevredigeringService.hentEllerOpprettPdf(callMock(), brev.info.id)
        transaction { assertThat(Document.find { DocumentTable.brevredigering eq brev.info.id }).hasSize(1) }

        brevredigeringService.slettBrev(brev.info.id)
        transaction {
            assertThat(Brevredigering.findById(brev.info.id)).isNull()
            assertThat(Document.find { DocumentTable.brevredigering eq brev.info.id }).hasSize(0)
        }
    }

    @Test
    fun `dobbel oppretting av pdf er ikke mulig`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        stagePdf("a real life pdf".encodeToByteArray())
        awaitAll(*(0..<10).map { async(Dispatchers.IO) { brevredigeringService.hentEllerOpprettPdf(callMock(), brev.info.id) } }.toTypedArray())

        transaction {
            assertThat(Document.find { DocumentTable.brevredigering eq brev.info.id }).hasSize(1)
        }
    }

    @Test
    fun `Document redigertBrevHash er stabil`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        stagePdf("min første pdf".encodeToByteArray())

        brevredigeringService.hentEllerOpprettPdf(callMock(), brev.info.id)
        val firstHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

        transaction { Brevredigering[brev.info.id].document.first().delete() }
        brevredigeringService.hentEllerOpprettPdf(callMock(), brev.info.id)
        val secondHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

        assertThat(firstHash).isEqualTo(secondHash)
    }

    @Test
    fun `Document redigertBrevHash endres basert paa redigertBrev`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!

        stagePdf("min første pdf".encodeToByteArray())
        brevredigeringService.hentEllerOpprettPdf(callMock(), brev.info.id)
        val firstHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

        transaction { Brevredigering[brev.info.id].redigertBrev = letter(Paragraph(1, true, listOf(Literal(1, "blue pill")))).toEdit() }
        brevredigeringService.hentEllerOpprettPdf(callMock(), brev.info.id)
        val secondHash = transaction { Brevredigering[brev.info.id].document.first().redigertBrevHash }

        assertThat(firstHash).isNotEqualTo(secondHash)
    }

    @Test
    fun `hentPdf rendrer ny pdf om den ikke eksisterer`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        stagePdf("what a cool party".encodeToByteArray())

        val pdf = brevredigeringService.hentEllerOpprettPdf(callMock(), brev.info.id)

        assertThat(pdf?.resultOrNull()?.toString(Charsets.UTF_8)).isEqualTo("what a cool party")
    }

    @Test
    fun `hentPdf rendrer ny pdf om den ikke er basert paa gjeldende redigertBrev`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        stagePdf("min første pdf".encodeToByteArray())
        brevredigeringService.hentEllerOpprettPdf(callMock(), brev.info.id)

        transaction { Brevredigering[brev.info.id].redigertBrev = letter(Paragraph(1, true, listOf(Literal(1, "blue pill")))).toEdit() }

        stagePdf("min andre pdf".encodeToByteArray())
        val pdf = brevredigeringService.hentEllerOpprettPdf(callMock(), brev.info.id)?.resultOrNull()?.toString(Charsets.UTF_8)

        assertEquals("min andre pdf", pdf)
    }

    @Test
    fun `hentPdf rendrer ikke ny pdf om den er basert paa gjeldende redigertBrev`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        stagePdf("min første pdf".encodeToByteArray())
        val first = brevredigeringService.hentEllerOpprettPdf(callMock(), brev.info.id)

        stagePdf("min første pdf".encodeToByteArray())
        val second = brevredigeringService.hentEllerOpprettPdf(callMock(), brev.info.id)

        assertThat(first).isNotEqualTo(second)
    }

    @Test
    fun `status er kladd om brev ikke er laast og ikke redigeres`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        assertThat(brev.info.status).isEqualTo(Api.BrevStatus.Kladd)
    }

    @Test
    fun `status er Klar om brev er laast`(): Unit = runBlocking {
        val brev = opprettBrev().resultOrNull()!!
        val oppdatert = brevredigeringService.delvisOppdaterBrev(brev.info.id, Api.DelvisOppdaterBrevRequest(laastForRedigering = true))
        assertThat(oppdatert?.info?.status).isEqualTo(Api.BrevStatus.Klar)
    }

    @Test
    fun `status er UnderRedigering om brev ikke er laast og er reservert for redigering`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!
        assertThat(brev.info.status).isEqualTo(Api.BrevStatus.UnderRedigering(principalNavIdent))
    }

    @Test
    fun `brev kan reserveres for redigering gjennom opprett brev`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!

        assertThat(brev.info.redigeresAv).isEqualTo(principalNavIdent)
        assertThat(transaction { Brevredigering[brev.info.id].redigeresAvNavIdent }).isEqualTo(principalNavIdent)
    }

    @Test
    fun `brev kan reserveres for redigering gjennom hent brev`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = false).resultOrNull()!!

        assertThat(brev.info.redigeresAv).isNull()
        assertThat(transaction { Brevredigering[brev.info.id].redigeresAvNavIdent }).isNull()

        val hentetBrev = brevredigeringService.hentBrev(callMock(), brev.info.id, reserverForRedigering = true)?.resultOrNull()!!
        assertThat(hentetBrev.info.redigeresAv).isEqualTo(principalNavIdent)
        assertThat(transaction { Brevredigering[hentetBrev.info.id].redigeresAvNavIdent }).isEqualTo(principalNavIdent)
    }

    @Test
    fun `allerede reservert brev kan ikke resereveres for redigering`() {
        runBlocking {
            val brev = opprettBrev(callMock(principalNavIdent), reserverForRedigering = true).resultOrNull()!!

            assertThrows<KanIkkeReservereBrevredigeringException> {
                brevredigeringService.hentBrev(callMock(principalNavIdent2), brev.info.id, reserverForRedigering = true)?.resultOrNull()!!
            }
            assertThat(transaction { Brevredigering[brev.info.id].redigeresAvNavIdent }).isEqualTo(principalNavIdent)
        }
    }

    @Test
    fun `kun en som vinner reservasjon av et brev`() {
        runBlocking {
            val brev = opprettBrev(callMock(principalNavIdent), reserverForRedigering = false).resultOrNull()!!

            coEvery { brevbakerMock.renderMarkup(any(), eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID), any(), any(), any()) } coAnswers {
                delay(100)
                ServiceResult.Ok(letter)
            }

            val hentBrev = (0..10).map {
                async(Dispatchers.IO) {
                    runCatching { brevredigeringService.hentBrev(callMock("id-$it"), brev.info.id, reserverForRedigering = true) }
                }
            }
            val awaited = hentBrev.awaitAll()
            val redigeresFaktiskAv = transaction { Brevredigering[brev.info.id].redigeresAvNavIdent }

            assertThat(awaited).areExactly(1, condition("Vellykkede hentBrev med reservasjon") { it.isSuccess })
            assertThat(awaited).areExactly(awaited.size - 1, condition("Feilende hentBrev med reservasjon") { it.isFailure })
            assertThat(awaited).allMatch { it.isFailure || it.getOrNull()?.resultOrNull()?.info?.redigeresAv == redigeresFaktiskAv }
        }
    }

    @Test
    fun `brev kan ikke oppdateres av andre enn den som har reservert det for redigering`(): Unit = runBlocking {
        val brev = opprettBrev(reserverForRedigering = true).resultOrNull()!!

        assertThrows<KanIkkeReservereBrevredigeringException> {
            brevredigeringService.oppdaterBrev(
                callMock(principalNavIdent2),
                brev.info.id,
                brev.saksbehandlerValg,
                letter(Paragraph(1, true, listOf(Literal(1, "blue pill")))).toEdit()
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
            Brevredigering[brev.info.id].sistReservert = Instant.now().minus(BrevredigeringService.RESERVASJON_TIMEOUT.plusSeconds(10))
        }

        val hentetBrev = brevredigeringService.hentBrev(callMock(), brev.info.id, reserverForRedigering = false)?.resultOrNull()!!
        assertThat(hentetBrev.info.redigeresAv).isNull()

        val hentetBrevMedReservasjon = brevredigeringService.hentBrev(callMock(principalNavIdent2), brev.info.id, reserverForRedigering = true)?.resultOrNull()!!
        assertThat(hentetBrevMedReservasjon.info.redigeresAv).isEqualTo(principalNavIdent2)
    }

    private suspend fun opprettBrev(call: ApplicationCall = callMock(), reserverForRedigering: Boolean = false) = brevredigeringService.opprettBrev(
        call = call,
        sak = sak,
        brevkode = Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
        spraak = LanguageCode.ENGLISH,
        avsenderEnhetsId = principalNavEnhetId,
        saksbehandlerValg = GeneriskBrevData().apply { put("valg", true) },
        reserverForRedigering = reserverForRedigering
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

    private fun <T> condition(description: String, predicate: Predicate<T>): org.assertj.core.api.Condition<T> =
        org.assertj.core.api.Condition(predicate, description)
}

