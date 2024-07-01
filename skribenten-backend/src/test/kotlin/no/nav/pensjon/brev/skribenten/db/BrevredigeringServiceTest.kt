package no.nav.pensjon.brev.skribenten.db

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.routes.mapBrev
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import no.nav.pensjon.brev.skribenten.services.BrevdataResponse
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService
import no.nav.pensjon.brev.skribenten.services.GeneriskRedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.services.PenService
import no.nav.pensjon.brev.skribenten.services.ServiceResult
import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block.Paragraph
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.Literal
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.Variable
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import java.time.LocalDate
import java.util.UUID
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

    private val brevbakerMock = mockk<BrevbakerService> {
        coEvery { renderMarkup(any(), eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID), any(), any()) } returns ServiceResult.Ok(letter)
    }
    private val callMock = mockk<ApplicationCall> {
        every { principal() } returns mockk<UserPrincipal> {
            every { navIdent } returns "Agent Smith"
        }
    }

    private val penService: PenService = mockk()
    private val service: BrevredigeringService = BrevredigeringService(
        brevbakerService = brevbakerMock,
        penService = penService
    )

    private val sak = Pen.SakSelection(
        1234L,
        "12345678910",
        LocalDate.now().minusYears(42),
        Pen.SakType.ALDER,
        "rabbit"
    )

    @Test
    fun `non existing brevredingering returns null`() = runBlocking {
        assertThat(service.hentBrev(callMock, sak, 99, ::mapBrev)).isInstanceOfSatisfying(ServiceResult.Error::class.java) {
            assertThat(it.statusCode).isEqualTo(HttpStatusCode.NotFound)
        }
    }

    @Test
    fun `can create and fetch brevredigering`() = runBlocking {
        val saksbehandlerValg = GeneriskBrevData().apply { put("valg1", true) }
        val result = service.opprettBrev(callMock, sak, Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID, saksbehandlerValg, ::mapBrev)

        assertEquals(result, service.hentBrev(callMock, sak, result.resultOrNull()!!.info.id, ::mapBrev))
        assertEquals(result.resultOrNull()?.info?.brevkode, Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID)
        assertEquals(result.resultOrNull()?.redigertBrev, letter.toEdit())
    }

    @Test
    fun `can update brevredigering`() = runBlocking {
        coEvery { penService.hentPesysBrevdata(any(), eq(sak.saksId), eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID)) } returns
                ServiceResult.Ok(
                    BrevdataResponse(
                        data = BrevdataResponse.Data(
                            felles = Felles(
                                dokumentDato = LocalDate.now(),
                                saksnummer = "123",
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
                        error = null
                    )
                )

        val saksbehandlerValg = GeneriskBrevData().apply { put("valg1", true) }
        val original =
            service.opprettBrev(callMock, sak, Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID, saksbehandlerValg, ::mapBrev).resultOrNull()!!

        val nyeValg = GeneriskBrevData().apply { put("valg2", true) }
        val oppdatert = service.oppdaterBrev(
            callMock,
            sak,
            original.info.id,
            nyeValg,
            letter.toEdit(),
            ::mapBrev,
        ).resultOrNull()!!

        assertThat(service.hentBrev(callMock, sak, original.info.id, ::mapBrev))
            .isInstanceOfSatisfying(ServiceResult.Ok::class.java) {
                assertThat(it.result).isEqualTo(oppdatert)
            }

        assertNotEquals(original.saksbehandlerValg, oppdatert.saksbehandlerValg)
    }

    @Test
    fun `updates redigertBrev with fresh rendering from brevbaker`() = runBlocking {
        val saksbehandlerValg = GeneriskBrevData().apply { put("valg1", true) }
        val original =
            service.opprettBrev(callMock, sak, Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID, saksbehandlerValg, ::mapBrev).resultOrNull()!!

        val nyeValg = GeneriskBrevData().apply { put("valg2", true) }
        val freshRender = letter.copy(blocks = letter.blocks + Paragraph(2, true, listOf(Variable(21, "ny paragraph"))))
        coEvery {
            brevbakerMock.renderMarkup(
                any(),
                eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID),
                eq(GeneriskRedigerbarBrevdata(Api.GeneriskBrevdata(), nyeValg)),
                any()
            )
        } returns ServiceResult.Ok(freshRender)

        val oppdatert =
            service.oppdaterBrev(
                callMock,
                sak,
                original.info.id,
                nyeValg,
                letter.toEdit(),
                ::mapBrev,
            )
                .resultOrNull()!!

        assertNotEquals(original.redigertBrev, oppdatert.redigertBrev)
        assertEquals(letter.toEdit().updateEditedLetter(freshRender), oppdatert.redigertBrev)
    }

    @Test
    fun `cannot update non-existing brevredigering`() = runBlocking {
        val saksbehandlerValg = GeneriskBrevData().apply { put("valg1", true) }
        val oppdatert =
            service.oppdaterBrev(callMock, sak, 1099, saksbehandlerValg, letter.toEdit(), ::mapBrev)
                .resultOrNull()

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
                eq(GeneriskRedigerbarBrevdata(EmptyBrevdata, saksbehandlerValg)),
                any()
            )
        } coAnswers {
            delay(10.minutes)
            ServiceResult.Ok(letter)
        }


        val result = withTimeout(10.seconds) {
            service.oppdaterBrev(callMock, sak, 2098, saksbehandlerValg, letter.toEdit(), ::mapBrev)
        }

        assertThat(result).isInstanceOf(ServiceResult.Error::class.java)
        assertThat((result as ServiceResult.Error).statusCode).isEqualTo(HttpStatusCode.NotFound)
    }

    @Test
    fun `can delete brevredigering`(): Unit = runBlocking {
        val saksbehandlerValg = GeneriskBrevData().apply { put("valg1", true) }
        val result = service.opprettBrev(callMock, sak, Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID, saksbehandlerValg, ::mapBrev)

        assertEquals(result, service.hentBrev(callMock, sak, result.resultOrNull()!!.info.id, ::mapBrev))
        assertTrue(service.slettBrev(result.resultOrNull()!!.info.id))
        assertThat(service.hentBrev(callMock, sak, result.resultOrNull()!!.info.id, ::mapBrev))
            .isInstanceOfSatisfying(ServiceResult.Error::class.java) {
                assertThat(it.statusCode).isEqualTo(HttpStatusCode.NotFound)
            }
    }

    @Test
    fun `delete brevredigering returns false for non-existing brev`(): Unit = runBlocking {
        assertThat(service.hentBrev(callMock, sak, 1337, ::mapBrev))
            .isInstanceOfSatisfying(ServiceResult.Error::class.java) {
                assertThat(it.statusCode).isEqualTo(HttpStatusCode.NotFound)
            }

        assertFalse(service.slettBrev(1337))
    }

    @Test
    fun `gitt en ferdigstilling saa skal pdf lagres i DB med referanse til Brevredigering`(): Unit = runBlocking {

        val pdf = "nesten en pdf".encodeToByteArray()
        coEvery { penService.hentPesysBrevdata(any(), eq(sak.saksId), eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID)) } returns
                ServiceResult.Ok(
                    BrevdataResponse(
                        data = BrevdataResponse.Data(
                            felles = Felles(
                                dokumentDato = LocalDate.now(),
                                saksnummer = "123",
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
                        error = null
                    )
                )

        coEvery { brevbakerMock.renderPdf(any(), any(), any(), any(), any()) } returns ServiceResult.Ok(
            LetterResponse.V2(
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

        val saksbehandlersValg = GeneriskBrevData().apply { put("valg", true) }

        val freshRender = letter.copy(blocks = letter.blocks + Paragraph(2, true, listOf(Variable(21, "ny paragraph"))))
        coEvery {
            brevbakerMock.renderMarkup(
                any(),
                eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID),
                eq(GeneriskRedigerbarBrevdata(Api.GeneriskBrevdata(), saksbehandlersValg)),
                any()
            )
        } returns ServiceResult.Ok(freshRender)

        val unikCallMock = mockk<ApplicationCall> {
            every { principal() } returns mockk<UserPrincipal> {
                every { navIdent } returns "XYZ12345678"
            }
        }

        val result = service.opprettBrev(
            unikCallMock,
            sak,
            Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
            saksbehandlersValg,
            ::mapBrev
        ).resultOrNull()!!

        transaction {
            val brevredigering = Brevredigering.all().first { it.opprettetAvNavIdent == "XYZ12345678" }
            val document = Document.all().filter { it.brevredigering.id == brevredigering.id }

            assertEquals(0, brevredigering.document.count())
            assertEquals(0, document.count())
        }

        service.ferdigstill(callMock, result.info.id)

        transaction {
            val brevredigering = Brevredigering.all().first { it.opprettetAvNavIdent == "XYZ12345678" }
            val document = Document.all().filter { it.brevredigering.id == brevredigering.id }

            assertEquals(1, brevredigering.document.count())
            assertEquals(1, document.count())
            assertTrue(pdf.contentEquals(document.first().pdf.bytes))
        }
    }
}

