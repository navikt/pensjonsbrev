package no.nav.pensjon.brev.skribenten.db

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.testcontainers.containers.PostgreSQLContainer
import java.time.Instant
import java.time.temporal.ChronoUnit

class MottakerTest {
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

    private val principal = NavIdent("abc")

    @Test
    fun `kan sette tss mottaker for brevredigering`() {
        val brevredigering = createBrevredigering()
        transaction {
            Mottaker.new(brevredigering.id.value) {
                samhandler("12345")
            }
        }
        val mottaker = transaction { Mottaker[brevredigering.id] }
        assertEquals("12345", mottaker.tssId)
        assertEquals(MottakerType.SAMHANDLER, mottaker.type)
    }

    @Test
    fun `kan sette norsk adresse for brevredigering`() {
        val brevredigering = createBrevredigering()
        transaction {
            Mottaker.new(brevredigering.id.value) {
                norskAdresse("a", "b", "c", "d", "e", "f")
            }
        }
        val mottaker = transaction { Mottaker[brevredigering.id] }
        assertEquals(MottakerType.NORSK_ADRESSE, mottaker.type)
        assertEquals("a", mottaker.navn)
        assertEquals("b", mottaker.postnummer)
        assertEquals("c", mottaker.poststed)
        assertEquals("d", mottaker.adresselinje1)
        assertEquals("e", mottaker.adresselinje2)
        assertEquals("f", mottaker.adresselinje3)
    }

    @Test
    fun `kan sette utenlandsk adresse for brevredigering`() {
        val brevredigering = createBrevredigering()
        transaction {
            Mottaker.new(brevredigering.id.value) {
                utenlandskAdresse("a", "b", "c", "d", "e", "f", "g")
            }
        }
        val mottaker = transaction { Mottaker[brevredigering.id] }
        assertEquals(MottakerType.UTENLANDSK_ADRESSE, mottaker.type)
        assertEquals("a", mottaker.navn)
        assertEquals("b", mottaker.postnummer)
        assertEquals("c", mottaker.poststed)
        assertEquals("d", mottaker.adresselinje1)
        assertEquals("e", mottaker.adresselinje2)
        assertEquals("f", mottaker.adresselinje3)
        assertEquals("g", mottaker.landkode)
    }

    @Test
    fun `kan ikke sette flere mottakere for brevredigering`() {
        val brevredigering = createBrevredigering()
        assertThrows<ExposedSQLException> {
            transaction {
                Mottaker.new(brevredigering.id.value) {
                    samhandler("12345")
                }
                Mottaker.new(brevredigering.id.value) {
                    samhandler("12345")
                }
            }
        }
    }

    @Test
    fun `kan endre mottaker type for brevredigering`() {
        val brevredigering = createBrevredigering()
        transaction {
            Mottaker.new(brevredigering.id.value) {
                norskAdresse("a", "b", "c", "d", "e", "f")
            }
        }
        transaction {
            Mottaker[brevredigering.id].samhandler("123456")
        }
        val mottaker = transaction { Mottaker[brevredigering.id] }
        assertEquals(MottakerType.SAMHANDLER, mottaker.type)
        assertEquals("123456", mottaker.tssId)
    }

    @Test
    fun `kan endre mottaker gjennom brevredigering`() {
        val brevredigeringId = createBrevredigering().id.value
        transaction {
            Mottaker.new(brevredigeringId) {
                samhandler("12345")
            }
        }
        transaction { Brevredigering[brevredigeringId].mottaker?.samhandler("abc") }
        val mottaker = transaction { Mottaker[brevredigeringId] }

        assertEquals(MottakerType.SAMHANDLER, mottaker.type)
        assertEquals("abc", mottaker.tssId)
    }

    private fun createBrevredigering() = transaction {
        Brevredigering.new {
            saksId = 123L
            opprettetAvNavIdent = principal
            this.brevkode = Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID
            this.spraak = LanguageCode.BOKMAL
            this.avsenderEnhetId = "1111"
            this.saksbehandlerValg = Api.GeneriskBrevdata()
            laastForRedigering = false
            distribusjonstype = Distribusjonstype.SENTRALPRINT
            redigeresAvNavIdent = principal
            opprettet = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            sistredigert = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            redigertBrev = Edit.Letter(
                "a",
                LetterMarkup.Sakspart("b", "c", "d", "e"),
                emptyList(),
                LetterMarkup.Signatur("f", "g", "h", "i", "j"),
                emptySet(),
            )
            sistRedigertAvNavIdent = principal
            signaturSignerende = "en signatur"
        }
    }
}