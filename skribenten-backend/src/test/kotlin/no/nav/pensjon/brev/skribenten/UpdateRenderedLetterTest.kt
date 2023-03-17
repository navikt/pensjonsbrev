package no.nav.pensjon.brev.skribenten

import no.nav.pensjon.brev.api.model.RenderedJsonLetter
import no.nav.pensjon.brev.api.model.RenderedJsonLetter.*
import no.nav.pensjon.brev.api.model.RenderedJsonLetter.Block.*
import no.nav.pensjon.brev.api.model.RenderedJsonLetter.ParagraphContent.ItemList
import no.nav.pensjon.brev.api.model.RenderedJsonLetter.ParagraphContent.ItemList.Item
import no.nav.pensjon.brev.api.model.RenderedJsonLetter.ParagraphContent.Text.*
import org.junit.Test
import kotlin.test.assertEquals

class UpdateRenderedLetterTest {

    @Test
    fun `no changes in editedLetter and no changes in nextLetter returns same letter`() {
        val edited = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )
        val next = edited.copy()
        assertEquals(edited, updatedEditedLetter(edited, next))
    }

    @Test
    fun `literals in editedLetter are not replaced`() {
        val next = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )
        val edited = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "Her har jeg redigert "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )

        assertEquals(edited, updatedEditedLetter(edited, next))
    }

    @Test
    fun `variables in editedLetter are replaced `() {
        val next = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "En tittel "),
                    Variable(2, "for de andre "),
                    Literal(3, "og meg!")
                )
            )
        )
        val edited = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )

        assertEquals(next, updatedEditedLetter(edited, next))
    }

    @Test
    fun `literals no longer present in next render are removed but other edits are kept`() {
        val next = letter(
            Title1(
                1, true, listOf(
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )
        val edited = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "Her har jeg redigert "),
                    Variable(2, "for deg "),
                    Literal(3, "og dine!")
                )
            )
        )

        val expected = letter(
            Title1(
                1, true, listOf(
                    Variable(2, "for deg "),
                    Literal(3, "og dine!")
                )
            )
        )

        assertEquals(expected, updatedEditedLetter(edited, next))
    }

    @Test
    fun `new edited content is kept`() {
        val next = letter(
            Title1(
                1, true, listOf(
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )
        val edited = letter(
            Title1(
                1, true, listOf(
                    Literal(-1, "Her har jeg lagt til "),
                    Variable(2, "for deg "),
                    Literal(3, "og dine!"),
                    Literal(-1, " Også lagt til"),
                )
            )
        )

        val expected = letter(
            Title1(
                1, true, listOf(
                    Literal(-1, "Her har jeg lagt til "),
                    Variable(2, "for deg "),
                    Literal(3, "og dine!"),
                    Literal(-1, " Også lagt til"),
                )
            )
        )

        assertEquals(expected, updatedEditedLetter(edited, next))
    }

    @Test
    fun `new content from next is added`() {
        val next = letter(
            Title1(
                1, true, listOf(
                    Variable(2, "for deg "),
                    Literal(3, "og meg!"),
                    Literal(4, " pluss noe mer tekst")
                )
            )
        )
        val edited = letter(
            Title1(
                1, true, listOf(
                    Variable(2, "for deg "),
                    Literal(3, "og meg!"),
                )
            )
        )

        val expected = letter(
            Title1(
                1, true, listOf(
                    Variable(2, "for deg "),
                    Literal(3, "og meg!"),
                    Literal(4, " pluss noe mer tekst"),
                )
            )
        )

        assertEquals(expected, updatedEditedLetter(edited, next))
    }

    @Test
    fun `should replace edited content when it has different type than the next content with same ID`() {
        // The ID of a content element is hashcode, and it should not be possible with the same ID.
        val next = letter(
            Title1(
                1, true, listOf(
                    Variable(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )
        val edited = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "Her har jeg redigert "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )

        assertEquals(next, updatedEditedLetter(edited, next))
    }

    @Test
    fun `new edited literal added before a variable should be kept before that variable`() {
        val next = letter(
            Title1(
                1, true, listOf(
                    Variable(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )
        val edited = letter(
            Title1(
                1, true, listOf(
                    Literal(-1, "ny literal"),
                    Variable(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )

        assertEquals(edited, updatedEditedLetter(edited, next))
    }

    @Test
    fun `new edited literal added after a rendered content should be keep relative position`() {
        val next = letter(
            Title1(
                1, true, listOf(
                    Variable(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!"),
                    Literal(4, "ny rendered literal"),
                )
            )
        )
        val edited = letter(
            Title1(
                1, true, listOf(
                    Variable(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!"),
                    Literal(-1, "ny literal"),
                )
            )
        )
        val expected = letter(
            Title1(
                1, true, listOf(
                    Variable(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!"),
                    Literal(-1, "ny literal"),
                    Literal(4, "ny rendered literal"),
                )
            )
        )

        assertEquals(expected, updatedEditedLetter(edited, next))
    }

    @Test
    fun `an edited block where type is changed from paragraph to title1 will merge and be kept as title1 if next only has text content`() {
        val next = letter(
            Paragraph(
                1, true, listOf(
                    Literal(1, "Noe tekst "),
                    Variable(2, "med en oppdatert variabel"),
                    Literal(3, " og noe mer tekst"),
                )
            )
        )
        val edited = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "Noe redigert tekst "),
                    Variable(2, "med en variabel"),
                    Literal(3, " og noe mer tekst"),
                )
            )
        )
        val expected = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "Noe redigert tekst "),
                    Variable(2, "med en oppdatert variabel"),
                    Literal(3, " og noe mer tekst")
                )
            )
        )

        assertEquals(expected, updatedEditedLetter(edited, next))
    }

    @Test
    fun `an edited block where type is changed from paragraph to title1 will be changed back to paragraph if next has non text content`() {
        val next = letter(
            Paragraph(
                1, true, listOf(
                    Literal(1, "Noe tekst "),
                    Variable(2, "med en oppdatert variabel"),
                    Literal(3, " og noe mer tekst"),
                    ItemList(4, items = listOf(Item(listOf(Literal(1, "et punkt"))))),
                )
            )
        )
        val edited = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "Noe redigert tekst "),
                    Variable(2, "med en variabel"),
                    Literal(3, " og noe mer tekst"),
                )
            )
        )
        val expected = letter(
            Paragraph(
                1, true, listOf(
                    Literal(1, "Noe redigert tekst "),
                    Variable(2, "med en oppdatert variabel"),
                    Literal(3, " og noe mer tekst"),
                    ItemList(4, items = listOf(Item(listOf(Literal(1, "et punkt"))))),
                )
            )
        )

        assertEquals(expected, updatedEditedLetter(edited, next))
    }

    @Test
    fun `an edited block where type is changed from title1 to paragraph will be merged but kept as paragraph`() {
        val next = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "Noe tekst "),
                    Variable(2, "med en oppdatert variabel"),
                    Literal(3, " og noe mer tekst"),
                )
            )
        )
        val edited = letter(
            Paragraph(
                1, true, listOf(
                    Literal(1, "Noe redigert tekst "),
                    Variable(2, "med en variabel"),
                    Literal(3, " og noe mer tekst"),
                )
            )
        )
        val expected = letter(
            Paragraph(
                1, true, listOf(
                    Literal(1, "Noe redigert tekst "),
                    Variable(2, "med en oppdatert variabel"),
                    Literal(3, " og noe mer tekst"),
                )
            )
        )

        assertEquals(expected, updatedEditedLetter(edited, next))
    }


    @Test
    fun `a non-editable block should be replaced with next despite any edits`() {
        val next = letter(
            Title1(
                1, false, listOf(
                    Literal(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )
        val edited = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "Her har jeg redigert "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )

        assertEquals(next, updatedEditedLetter(edited, next))
    }

    @Test
    fun `a new edited block should be kept`() {
        val next = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )
        val edited = letter(
            Paragraph(
                -1, true, listOf(
                    Literal(-1, "Noe ny tekst")
                )
            ),
            Title1(
                1, true, listOf(
                    Literal(1, "Her har jeg redigert "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            ),
            Paragraph(
                -1, true, listOf(
                    Literal(-1, "Noe mer ny tekst")
                )
            ),
        )

        assertEquals(edited, updatedEditedLetter(edited, next))
    }

    @Test
    fun `new block from next should be added`() {
        val next = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            ),
            Title1(
                3, true, listOf(
                    Literal(1, "En ny tittel "),
                )
            ),
        )
        val edited = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "Her har jeg redigert "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            ),
            Paragraph(
                -1, true, listOf(
                    Literal(-1, "Noe mer ny tekst")
                )
            ),
        )
        val expected = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "Her har jeg redigert "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            ),
            Paragraph(
                -1, true, listOf(
                    Literal(-1, "Noe mer ny tekst")
                )
            ),
            Title1(
                3, true, listOf(
                    Literal(1, "En ny tittel "),
                )
            ),
        )

        assertEquals(expected, updatedEditedLetter(edited, next))
    }

    @Test
    fun `keeps new edited blocks in positions relative to original blocks`() {
        val next = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            ),
            Title1(
                3, true, listOf(
                    Literal(1, "En ny tittel "),
                )
            ),
        )
        val edited = letter(
            Paragraph(
                -1, true, listOf(
                    Literal(-1, "første teksten")
                )
            ),
            Title1(
                1, true, listOf(
                    Literal(1, "Her har jeg redigert "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            ),
            Paragraph(
                -1, true, listOf(
                    Literal(-1, "Noe mer ny tekst")
                )
            ),
            Paragraph(
                -1, true, listOf(
                    Literal(-1, "enda mer tekst")
                )
            ),
            Title1(
                3, true, listOf(
                    Literal(1, "En ny tittel "),
                )
            ),
            Paragraph(
                -1, true, listOf(
                    Literal(-1, "siste teksten")
                )
            ),
        )

        assertEquals(edited, updatedEditedLetter(edited, next))
    }

    @Test
    fun `new edited blocks belongs to the previous rendered block`() {
        val next = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            ),
            Title1(
                3, true, listOf(
                    Literal(1, "Dette er en ny block før id:4 "),
                )
            ),
            Title1(
                4, true, listOf(
                    Literal(1, "En ny tittel "),
                )
            ),
        )
        val edited = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "Her har jeg redigert "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            ),
            Paragraph(
                -1, true, listOf(
                    Literal(-1, "Noe mer ny tekst")
                )
            ),
            Title1(
                4, true, listOf(
                    Literal(1, "En ny tittel "),
                )
            ),
        )
        val expected = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "Her har jeg redigert "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            ),
            Paragraph(
                -1, true, listOf(
                    Literal(-1, "Noe mer ny tekst")
                )
            ),
            Title1(
                3, true, listOf(
                    Literal(1, "Dette er en ny block før id:4 "),
                )
            ),
            Title1(
                4, true, listOf(
                    Literal(1, "En ny tittel "),
                )
            ),
        )

        assertEquals(expected, updatedEditedLetter(edited, next))
    }

    private fun letter(vararg blocks: Block) =
        RenderedJsonLetter(
            title = "En tittel",
            sakspart = Sakspart("Test Testeson", "1234568910", "1234", "20.12.2022"),
            blocks = blocks.toList(),
            signatur = Signatur("Med vennlig hilsen", "Saksbehandler", "Kjersti Saksbehandler", null, "NAV Familie- og pensjonsytelser Porsgrunn")
        )
}