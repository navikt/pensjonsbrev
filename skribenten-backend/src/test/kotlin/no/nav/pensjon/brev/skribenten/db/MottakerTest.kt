package no.nav.pensjon.brev.skribenten.db

import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.db.kryptering.KrypteringService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.NorskPostnummer
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.testcontainers.postgresql.PostgreSQLContainer
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class MottakerTest {
    private val postgres = PostgreSQLContainer("postgres:17-alpine")

    @BeforeAll
    fun startDb() {
        KrypteringService.init("ZBn9yGLDluLZVVGXKZxvnPun3kPQ2ccF")
        postgres.start()
        initDatabase(postgres.jdbcUrl, postgres.username, postgres.password)
    }

    @AfterAll
    fun stopDb() {
        postgres.stop()
    }

    private val principal = NavIdent("abc")

    @Test
    fun `kan sette tss mottaker for brevredigering`() {
        val brevredigering = createBrevredigering()
        transaction {
            Mottaker.new(brevredigering.id.value) {
                type = MottakerType.SAMHANDLER
                tssId = "12345"
                manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.IKKE_RELEVANT
            }
        }
        val mottaker = transaction { Mottaker[brevredigering.id] }
        assertEquals("12345", mottaker.tssId)
        assertEquals(MottakerType.SAMHANDLER, mottaker.type)
    }

    @Test
    fun `kan ikke sette flere mottakere for brevredigering`() {
        val brevredigering = createBrevredigering()
        assertThrows<ExposedSQLException> {
            transaction {
                Mottaker.new(brevredigering.id.value) {
                    type = MottakerType.SAMHANDLER
                    tssId = "12345"
                    manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.IKKE_RELEVANT
                }
                Mottaker.new(brevredigering.id.value) {
                    type = MottakerType.SAMHANDLER
                    tssId = "123456"
                    manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.IKKE_RELEVANT
                }
            }
        }
    }

    @Test
    fun `kan endre mottaker gjennom brevredigering`() {
        val brevredigeringId = createBrevredigering().id.value
        transaction {
            Mottaker.new(brevredigeringId) {
                type = MottakerType.SAMHANDLER
                tssId = "12345"
                manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.IKKE_RELEVANT
            }
        }
        transaction { Brevredigering[brevredigeringId].mottaker?.tssId = "abc" }
        val mottaker = transaction { Mottaker[brevredigeringId] }

        assertEquals(MottakerType.SAMHANDLER, mottaker.type)
        assertEquals("abc", mottaker.tssId)
    }

    private fun createBrevredigering() = transaction {
        Brevredigering.new {
            saksId = 123L
            opprettetAvNavIdent = principal
            this.brevkode = Testbrevkoder.TESTBREV
            this.spraak = LanguageCode.BOKMAL
            this.avsenderEnhetId = "1111"
            this.saksbehandlerValg = Api.GeneriskBrevdata()
            laastForRedigering = false
            distribusjonstype = Distribusjonstype.SENTRALPRINT
            redigeresAvNavIdent = principal
            opprettet = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            redigertBrev = Edit.Letter(
                Edit.Title(listOf(Edit.ParagraphContent.Text.Literal(null, "a"))),
                LetterMarkupImpl.SakspartImpl(
                    gjelderNavn = "b",
                    gjelderFoedselsnummer = Foedselsnummer("c"),
                    annenMottakerNavn = null,
                    saksnummer = "d",
                    dokumentDato = LocalDate.now(),
                ),
                emptyList(),
                LetterMarkupImpl.SignaturImpl(
                    hilsenTekst = "f",
                    saksbehandlerNavn = "en signatur",
                    attesterendeSaksbehandlerNavn = "i",
                    navAvsenderEnhet = "j",
                ),
                emptySet(),
            )
            sistRedigertAvNavIdent = principal
        }
    }

    @Test
    fun `gir feilmelding for norsk adresse med femsifra postnummer`() {
        assertThrows<IllegalArgumentException> {
            Dto.Mottaker.norskAdresse(
                navn = "Peder Ås",
                postnummer = NorskPostnummer("12345"),
                poststed = "Lillevik",
                adresselinje1 = null,
                adresselinje2 = null,
                adresselinje3 = null,
                manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.IKKE_RELEVANT
            )
        }
    }

    @Test
    fun `gir feilmelding for norsk adresse med tresifra postnummer`() {
        assertThrows<IllegalArgumentException> {
            Dto.Mottaker.norskAdresse(
                navn = "Peder Ås",
                postnummer = NorskPostnummer("123"),
                poststed = "Lillevik",
                adresselinje1 = null,
                adresselinje2 = null,
                adresselinje3 = null,
                manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.IKKE_RELEVANT
            )
        }
    }

    @Test
    fun `takler norsk adresse med firesifra postnummer`() {
        Dto.Mottaker.norskAdresse(
            navn = "Peder Ås",
            postnummer = NorskPostnummer("1234"),
            poststed = "Lillevik",
            adresselinje1 = null,
            adresselinje2 = null,
            adresselinje3 = null,
            manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.IKKE_RELEVANT
        )
    }
}