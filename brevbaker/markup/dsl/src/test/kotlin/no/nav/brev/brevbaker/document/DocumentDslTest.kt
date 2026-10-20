@file:Suppress("DEPRECATION")

package no.nav.brev.brevbaker.document

import no.nav.brev.brevbaker.document.dsl.document
import no.nav.brev.brevbaker.document.dsl.documentMottaker
import no.nav.brev.brevbaker.document.dsl.documentSaksinformasjon
import no.nav.brev.brevbaker.document.dsl.documentSaksnummer
import no.nav.brev.brevbaker.markup.dsl.paragraph
import no.nav.brev.brevbaker.markup.dsl.title2
import no.nav.brev.brevbaker.markup.outline.Block
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

class DocumentDslTest {

    @Test
    fun `bygger dokument med innhold uten outline-nivaa`() {
        val dokument = document(tittel = "Min tittel") {
            title2("Overskrift")
            paragraph("Et avsnitt.")
        }

        assertEquals("Min tittel", dokument.tittel)
        assertEquals(2, dokument.blocks.size)
        assertTrue(dokument.blocks[0] is Block.Title2)
        assertTrue(dokument.blocks[1] is Block.Paragraph)
    }

    @Test
    fun `alle elementer er valgfrie bortsett fra tittel`() {
        val dokument = document(tittel = "Min tittel") { paragraph("Innhold") }

        assertTrue(dokument.visTittel)
        assertTrue(dokument.visLogo)
        assertNull(dokument.gjelderNavn)
        assertNull(dokument.gjelderPersonidentifikator)
        assertNull(dokument.annenMottakerNavn)
        assertNull(dokument.saksnummer)
        assertNull(dokument.dokumentDato)
    }

    @Test
    fun `saksinformasjonsfeltene settes uavhengig av hverandre`() {
        val dokument = document(
            tittel = "Min tittel",
            gjelderNavn = "Ola Nordmann",
            gjelderPersonidentifikator = "12345678901",
            dokumentDato = LocalDate.of(2020, 1, 1),
        ) { paragraph("Innhold") }

        assertEquals("Ola Nordmann", dokument.gjelderNavn)
        assertEquals("12345678901", dokument.gjelderPersonidentifikator?.value)
        assertNull(dokument.annenMottakerNavn)
        assertNull(dokument.saksnummer)
    }

    @Test
    fun `saksnummer baerer footervalget`() {
        val dokument = document(
            tittel = "Min tittel",
            saksnummer = documentSaksnummer("9876543", visFooter = true),
        ) { paragraph("Innhold") }

        assertEquals("9876543", dokument.saksnummer?.saksnummer?.value)
        assertTrue(dokument.saksnummer?.visFooter == true)
    }

    @Test
    fun `footer er av som standard`() {
        assertFalse(documentSaksnummer("9876543").visFooter)
    }

    @Test
    fun `tittel maa vaere satt`() {
        assertThrows<IllegalArgumentException> {
            document(tittel = " ") { paragraph("Innhold") }
        }
    }

    @Test
    fun `navn kan vaere like langt som verdens lengste navn`() {
        val dokument = document(
            tittel = "Min tittel",
            gjelderNavn = "a".repeat(747),
            annenMottakerNavn = "b".repeat(747),
        ) { paragraph("Innhold") }

        assertEquals(747, dokument.gjelderNavn?.length)
        assertEquals(747, dokument.annenMottakerNavn?.length)
    }

    @Test
    fun `navn lengre enn verdens lengste navn avvises`() {
        assertThrows<IllegalArgumentException> {
            document(tittel = "Min tittel", gjelderNavn = "a".repeat(748)) { paragraph("Innhold") }
        }
        assertThrows<IllegalArgumentException> {
            document(tittel = "Min tittel", annenMottakerNavn = "a".repeat(748)) { paragraph("Innhold") }
        }
    }

    @Test
    fun `for lang tittel avvises`() {
        document(tittel = "a".repeat(300)) { paragraph("Innhold") }
        assertThrows<IllegalArgumentException> {
            document(tittel = "a".repeat(301)) { paragraph("Innhold") }
        }
    }

    @Test
    fun `for lang personidentifikator avvises`() {
        document(tittel = "Min tittel", gjelderPersonidentifikator = "12345678901") { paragraph("Innhold") }
        assertThrows<IllegalArgumentException> {
            document(tittel = "Min tittel", gjelderPersonidentifikator = "123456789012") { paragraph("Innhold") }
        }
    }

    @Test
    fun `for langt saksnummer avvises`() {
        documentSaksnummer("1".repeat(20))
        assertThrows<IllegalArgumentException> {
            documentSaksnummer("1".repeat(21))
        }
    }

    @Test
    fun `dokumentet maa ha innhold`() {
        assertThrows<IllegalArgumentException> {
            document(tittel = "Min tittel") { }
        }
    }

    @Test
    fun `clean fjerner tomme blokker`() {
        val dokument = document(tittel = "Min tittel") {
            paragraph("Innhold")
            title2("")
        }.clean()

        assertEquals(1, dokument.blocks.size)
    }

    @Test
    fun `legacy saksinformasjon bygges som foer`() {
        val dokument = document(
            tittel = "Min tittel",
            saksinformasjon = documentSaksinformasjon(
                saksnummer = "9876543",
                visFooter = true,
                mottaker = documentMottaker(gjelderNavn = "Ola Nordmann", gjelderPersonidentifikator = "12345678901"),
            ),
        ) { paragraph("Innhold") }

        assertEquals("9876543", dokument.saksinformasjon?.saksnummer?.value)
        assertEquals("Ola Nordmann", dokument.saksinformasjon?.mottaker?.gjelderNavn)
        assertNull(dokument.gjelderNavn)
        assertNull(dokument.saksnummer)
    }
}
