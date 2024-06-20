package no.nav.pensjon.brev.skribenten.db

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
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
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import org.assertj.core.api.Assertions.assertThat
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
        coEvery { renderLetter(any(), eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID), any(), any()) } returns ServiceResult.Ok(letter)
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
    fun `non existing brevredingering returns null`() {
        assertNull(service.hentBrev(99, ::mapBrev))
    }

    @Test
    fun `can create and fetch brevredigering`() = runBlocking {
        val saksbehandlerValg = GeneriskBrevData().apply { put("valg1", true) }
        val result =
            service.opprettBrev(callMock, sak, Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID, saksbehandlerValg, ::mapBrev).resultOrNull()!!

        assertEquals(result, service.hentBrev(result.info.id, ::mapBrev))
        assertEquals(result.info.brevkode, Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID)
        assertEquals(result.redigertBrev, letter.toEdit())
    }

    @Test
    fun `can update brevredigering`() = runBlocking {
        coEvery { penService.hentPesysBrevdata(any(), any(), any()) } returns
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
                            ), brevdata = EmptyBrevdata
                        ),
                        error = null
                    )
                )

        val saksbehandlerValg = GeneriskBrevData().apply { put("valg1", true) }
        val original =
            service.opprettBrev(callMock, sak, Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID, saksbehandlerValg, ::mapBrev).resultOrNull()!!

        val nyeValg = GeneriskBrevData().apply { put("valg2", true) }
        val oppdatert =
            service.oppdaterBrev(
                callMock,
                original.info.id,
                Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
                nyeValg,
                letter.toEdit(),
                ::mapBrev
            )
                .resultOrNull()!!

        assertEquals(oppdatert, service.hentBrev(original.info.id, ::mapBrev))
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
            brevbakerMock.renderLetter(
                any(),
                eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID),
                eq(GeneriskRedigerbarBrevdata(EmptyBrevdata, nyeValg)),
                any()
            )
        } returns ServiceResult.Ok(freshRender)

        val oppdatert =
            service.oppdaterBrev(
                callMock,
                original.info.id,
                Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
                nyeValg,
                letter.toEdit(),
                ::mapBrev
            )
                .resultOrNull()!!

        assertNotEquals(original.redigertBrev, oppdatert.redigertBrev)
        assertEquals(letter.toEdit().updateEditedLetter(freshRender), oppdatert.redigertBrev)
    }

    @Test
    fun `cannot update non-existing brevredigering`() = runBlocking {
        val saksbehandlerValg = GeneriskBrevData().apply { put("valg1", true) }
        val oppdatert =
            service.oppdaterBrev(callMock, 1099, Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID, saksbehandlerValg, letter.toEdit(), ::mapBrev)
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
            brevbakerMock.renderLetter(
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
            service.oppdaterBrev(callMock, 2098, Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID, saksbehandlerValg, letter.toEdit(), ::mapBrev)
        }

        assertThat(result).isInstanceOf(ServiceResult.Error::class.java)
        assertThat((result as ServiceResult.Error).statusCode).isEqualTo(HttpStatusCode.NotFound)
    }

    @Test
    fun `can delete brevredigering`(): Unit = runBlocking {
        val saksbehandlerValg = GeneriskBrevData().apply { put("valg1", true) }
        val result =
            service.opprettBrev(callMock, sak, Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID, saksbehandlerValg, ::mapBrev).resultOrNull()!!

        assertEquals(result, service.hentBrev(result.info.id, ::mapBrev))
        assertTrue(service.slettBrev(result.info.id))
        assertNull(service.hentBrev(result.info.id, ::mapBrev))
    }

    @Test
    fun `delete brevredigering returns false for non-existing brev`(): Unit = runBlocking {
        assertNull(service.hentBrev(1337, ::mapBrev))
        assertFalse(service.slettBrev(1337))
    }

}