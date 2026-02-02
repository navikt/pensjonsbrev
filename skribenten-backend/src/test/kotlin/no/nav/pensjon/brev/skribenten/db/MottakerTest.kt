package no.nav.pensjon.brev.skribenten.db

import no.nav.pensjon.brev.skribenten.SharedPostgres
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.db.kryptering.KrypteringService
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.Mottaker
import no.nav.pensjon.brev.skribenten.domain.MottakerType
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import java.time.LocalDate

class MottakerTest {

    @BeforeAll
    fun startDb() {
        SharedPostgres.subscribeAndEnsureDatabaseInitialized(this)
        KrypteringService.init("ZBn9yGLDluLZVVGXKZxvnPun3kPQ2ccF")
    }

    @AfterAll
    fun kansellerDbAvhengighet() {
        SharedPostgres.cancelSubscription(this)
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
        transaction { BrevredigeringEntity[brevredigeringId].mottaker?.tssId = "abc" }
        val mottaker = transaction { Mottaker[brevredigeringId] }

        assertEquals(MottakerType.SAMHANDLER, mottaker.type)
        assertEquals("abc", mottaker.tssId)
    }

    private fun createBrevredigering() = transaction {
        BrevredigeringEntity.opprettBrev(
            saksId = 123L,
            opprettetAv = principal,
            brevkode = Testbrevkoder.TESTBREV,
            spraak = LanguageCode.BOKMAL,
            avsenderEnhetId = "1111",
            saksbehandlerValg = Api.GeneriskBrevdata(),
            distribusjonstype = Distribusjonstype.SENTRALPRINT,
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
            ),
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
            vedtaksId = null,
            timestamp = Instant.now(),
        )
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