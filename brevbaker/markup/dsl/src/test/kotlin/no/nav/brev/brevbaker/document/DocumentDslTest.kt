package no.nav.brev.brevbaker.document

import no.nav.brev.brevbaker.document.dsl.document
import no.nav.brev.brevbaker.document.dsl.documentSaksinformasjon
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

    private val saksinformasjon = documentSaksinformasjon(
        gjelderNavn = "Ola Nordmann",
        gjelderPersonidentifikator = "12345678901",
        saksnummer = "9876543",
    )

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
        assertNull(dokument.saksinformasjon)
        assertNull(dokument.dokumentDato)
        assertFalse(dokument.visFooter)
    }

    @Test
    fun `visFooter uten saksinformasjon er ugyldig`() {
        assertThrows<IllegalArgumentException> {
            document(tittel = "Min tittel", visFooter = true) { paragraph("Innhold") }
        }
    }

    @Test
    fun `visFooter med saksinformasjon er gyldig`() {
        val dokument = document(
            tittel = "Min tittel",
            saksinformasjon = saksinformasjon,
            dokumentDato = LocalDate.of(2020, 1, 1),
            visFooter = true,
        ) { paragraph("Innhold") }

        assertTrue(dokument.visFooter)
        assertEquals("9876543", dokument.saksinformasjon?.saksnummer?.value)
    }

    @Test
    fun `tittel maa vaere satt`() {
        assertThrows<IllegalArgumentException> {
            document(tittel = " ") { paragraph("Innhold") }
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
}
