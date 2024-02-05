package no.nav.pensjon.brev.skribenten

import no.nav.pensjon.brev.skribenten.routes.EditedJsonLetter
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter.*
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter.Block.Paragraph
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter.Block.Title1
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter.ParagraphContent.ItemList
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter.ParagraphContent.ItemList.Item
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter.ParagraphContent.Text.Literal
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter.ParagraphContent.Text.Variable
import org.junit.Test
import kotlin.test.assertEquals

class UpdateRenderedLetterTest {

    @Test
    fun `no changes in editedLetter and no changes in nextLetter returns same letter`() {
        val edited = editedLetter(
            Title1(
                1, true, listOf(
                    Literal(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )
        assertEquals(edited.letter, updatedEditedLetter(edited, edited.letter))
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
        val edited = editedLetter(
            Title1(
                1, true, listOf(
                    Literal(1, "Her har jeg redigert "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )

        assertEquals(edited.letter, updatedEditedLetter(edited, next))
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
        val edited = editedLetter(
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
        val edited = editedLetter(
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
        val edited = editedLetter(
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
        val edited = editedLetter(
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
        val edited = editedLetter(
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
        val edited = editedLetter(
            Title1(
                1, true, listOf(
                    Literal(-1, "ny literal"),
                    Variable(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )

        assertEquals(edited.letter, updatedEditedLetter(edited, next))
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
        val edited = editedLetter(
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
        val edited = editedLetter(
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
        val edited = editedLetter(
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
        val edited = editedLetter(
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
        val edited = editedLetter(
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
        val edited = editedLetter(
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

        assertEquals(edited.letter, updatedEditedLetter(edited, next))
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
        val edited = editedLetter(
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
        val edited = editedLetter(
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

        assertEquals(edited.letter, updatedEditedLetter(edited, next))
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
        val edited = editedLetter(
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

    @Test
    fun `an edited block can be split into multiple blocks and the updates should be merged into the correct blocks`() {
        val next = letter(
            Paragraph(1, true, listOf(
                Literal(1, "Første tekst"),
                Variable(2, "Oppdatert variable"),
                Literal(3, "Andre tekst"),
                Variable(4, "variable lagt til"),
            ))
        )
        val edited = editedLetter(
            Paragraph(1, true, listOf(
                Literal(1, "Første tekst med redigering"),
                Variable(2, "variable"),
            )),
            Paragraph(1, true, listOf(
                Literal(3, "Andre tekst med redigering"),
            )),
        )
        val expected = letter(
            Paragraph(1, true, listOf(
                Literal(1, "Første tekst med redigering"),
                Variable(2, "Oppdatert variable"),
            )),
            Paragraph(1, true, listOf(
                Literal(3, "Andre tekst med redigering"),
                Variable(4, "variable lagt til"),
            )),
        )

        assertEquals(expected, updatedEditedLetter(edited, next))
    }

    @Test
    fun `an edited block split into multiple blocks should not be overwritten`() {
        val next = letter(
            Paragraph(1, true, listOf(
                Variable(1, "Oppdatert variable"),
                Literal(2, "Første tekst"),
                Literal(3, "Andre tekst"),
                Variable(4, "variable lagt til"),
            ))
        )
        val edited = editedLetter(
            Paragraph(1, true, listOf(
                Variable(1, "variable"),
                Literal(2, "Første "),
            )),
            Paragraph(1, true, listOf(
                Literal(2, "tekst med redigering"),
                Literal(3, "Andre tekst med redigering"),
            )),
        )
        val expected = letter(
            Paragraph(1, true, listOf(
                Variable(1, "Oppdatert variable"),
                Literal(2, "Første "),
            )),
            Paragraph(1, true, listOf(
                Literal(2, "tekst med redigering"),
                Literal(3, "Andre tekst med redigering"),
                Variable(4, "variable lagt til"),
            )),
        )

        assertEquals(expected, updatedEditedLetter(edited, next))
    }

    @Test
    fun `an edited block split into multiple blocks should not be overwritten when it is last in block`() {
        val next = letter(
            Paragraph(1, true, listOf(
                Literal(2, "Første tekst"),
            ))
        )
        val edited = editedLetter(
            Paragraph(1, true, listOf(
                Literal(2, "Første "),
            )),
            Paragraph(1, true, listOf(
                Literal(2, "tekst med redigering"),
            )),
        )
        val expected = letter(
            Paragraph(1, true, listOf(
                Literal(2, "Første "),
            )),
            Paragraph(1, true, listOf(
                Literal(2, "tekst med redigering"),
            )),
        )

        assertEquals(expected, updatedEditedLetter(edited, next))
    }

    @Test
    fun `a new edited block in between a split rendered one should be kept`() {
        val next = letter(
            Paragraph(1, true, listOf(
                Literal(2, "Første tekst"),
            ))
        )
        val edited = editedLetter(
            Paragraph(1, true, listOf(
                Literal(2, "Første "),
            )),
            Paragraph(-1, true, listOf(
               Literal(-1, "Noe ny tekst")
            )),
            Paragraph(1, true, listOf(
                Literal(2, "tekst med redigering"),
            )),
        )
        val expected = letter(
            Paragraph(1, true, listOf(
                Literal(2, "Første "),
            )),
            Paragraph(-1, true, listOf(
                Literal(-1, "Noe ny tekst")
            )),
            Paragraph(1, true, listOf(
                Literal(2, "tekst med redigering"),
            )),
        )

        assertEquals(expected, updatedEditedLetter(edited, next))
    }

    @Test
    fun `deleted blocks should not reappear`() {
        val next = letter(
            Paragraph(1, true, listOf(
                Literal(1, "Første"),
            )),
            Paragraph(2, true, listOf(
                Literal(2, "Andre"),
            )),
            Paragraph(3, true, listOf(
                Literal(3, "Tredje"),
            )),
        )
        val edited = editedLetter(
            Paragraph(1, true, listOf(
                Literal(1, "Første"),
            )),
            Paragraph(3, true, listOf(
                Literal(3, "Tredje"),
            )),
            deleted = setOf(2)
        )

        assertEquals(edited.letter, updatedEditedLetter(edited, next))
    }

    private fun letter(vararg blocks: Block) =
        RenderedJsonLetter(
            title = "En tittel",
            sakspart = Sakspart("Test Testeson", "1234568910", "1234", "20.12.2022"),
            blocks = blocks.toList(),
            signatur = Signatur("Med vennlig hilsen", "Saksbehandler", "Kjersti Saksbehandler", null, "NAV Familie- og pensjonsytelser Porsgrunn")
        )

    private fun editedLetter(vararg blocks: Block, deleted: Set<Int> = emptySet()): EditedJsonLetter =
        EditedJsonLetter(
            letter(*blocks),
            deleted
        )
}