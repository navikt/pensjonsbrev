package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown
import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown.*
import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown.Block.Paragraph
import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown.Block.Title1
import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown.ParagraphContent.ItemList
import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown.ParagraphContent.ItemList.Item
import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown.ParagraphContent.Text.Literal
import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown.ParagraphContent.Text.Variable
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Title1 as E_Title1
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph as E_Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.ItemList as E_ItemList
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.ItemList.Item as E_Item
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal as E_Literal
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable as E_Variable

class UpdateRenderedLetterTest {

    @Test
    fun `no changes in editedLetter and no changes in nextLetter returns same letter`() {
        val rendered = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "En tittel "),
                    Variable(2, "for deg "),
                    Literal(3, "og meg!")
                )
            )
        )
        val edited = rendered.toEdit()
        assertEquals(edited, edited.updatedEditedLetter(rendered))
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
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel ", "Her har jeg redigert"),
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!")
                )
            )
        )

        assertEquals(edited, edited.updatedEditedLetter(next))
    }

    @Test
    fun `variables in editedLetter are replaced `() {
        val rendered = letter(
            Title1(
                1, true, listOf(
                    Literal(1, "En tittel "),
                    Variable(2, "for de andre "),
                    Literal(3, "og meg!")
                )
            )
        )
        val edited = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel "),
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!")
                )
            )
        )

        assertEquals(rendered.toEdit(), edited.updatedEditedLetter(rendered))
    }

    @Test
    fun `unedited literals no longer present in next render are removed but other edits are kept`() {
        val rendered = letter(
            Title1(
                1, true, listOf(
                    Variable(2, "for deg "),
                )
            )
        )
        val edited = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel ", "Her har jeg redigert"),
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!")
                )
            )
        )

        val expected = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel ", "Her har jeg redigert"),
                    E_Variable(2, "for deg "),
                )
            )
        )

        assertEquals(expected, edited.updatedEditedLetter(rendered))
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
            E_Title1(
                1, true, listOf(
                    E_Literal(null, "", "Her har jeg lagt til "),
                    E_Literal(1, "Var med i forrige rendring, men skal ikke være med i neste"),
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!"),
                    E_Literal(null, "", " Også lagt til"),
                )
            )
        )

        val expected = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Literal(null, "", "Her har jeg lagt til "),
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!"),
                    E_Literal(null, "", " Også lagt til"),
                )
            )
        )

        assertEquals(expected, edited.updatedEditedLetter(next))
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
            E_Title1(
                1, true, listOf(
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!", "og dine"),
                )
            )
        )

        val expected = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!", "og dine"),
                    E_Literal(4, " pluss noe mer tekst"),
                )
            )
        )

        assertEquals(expected, edited.updatedEditedLetter(next))
    }

    @Test
    fun `should fail when edited content has different type than the rendered content with same ID`() {
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
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel", "Her er det en ulovlig redigering"),
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!")
                )
            )
        )

        assertFailsWith<UpdateEditedLetterException> { edited.updatedEditedLetter(next) }
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
            E_Title1(
                1, true, listOf(
                    E_Literal(null, "", "ny literal"),
                    E_Variable(1, "En tittel "),
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!")
                )
            )
        )

        assertEquals(edited, edited.updatedEditedLetter(next))
    }

    @Test
    fun `new edited literal added after a rendered content should keep relative position`() {
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
            E_Title1(
                1, true, listOf(
                    E_Variable(1, "En tittel "),
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!"),
                    E_Literal(null, "", "ny literal"),
                )
            )
        )
        val expected = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Variable(1, "En tittel "),
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!"),
                    E_Literal(null, "", "ny literal"),
                    E_Literal(4, "ny rendered literal"),
                )
            )
        )

        assertEquals(expected, edited.updatedEditedLetter(next))
    }

    @Test
    fun `an edited block where type is changed from paragraph to title1 fail`() {
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
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "Noe tekst", "Noe redigert tekst "),
                    E_Variable(2, "med en variabel"),
                    E_Literal(3, " og noe mer tekst"),
                )
            )
        )
        assertFailsWith<UpdateEditedLetterException> { edited.updatedEditedLetter(next) }
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
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "", "Noe ny tekst")
                )
            ),
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel ", "Her har jeg redigert "),
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!")
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "", "Noe mer ny tekst")
                )
            ),
        )

        assertEquals(edited, edited.updatedEditedLetter(next))
    }


    @Test
    fun `new block from rendered should be added`() {
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
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel ", "Her har jeg redigert "),
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!")
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "Noe mer ny tekst")
                )
            ),
        )
        val expected = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel ", "Her har jeg redigert "),
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!")
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "Noe mer ny tekst")
                )
            ),
            E_Title1(
                3, true, listOf(
                    E_Literal(1, "En ny tittel "),
                )
            ),
        )

        assertEquals(expected, edited.updatedEditedLetter(next))
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
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "", "første teksten")
                )
            ),
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel ", "Her har jeg redigert "),
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!")
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "Noe mer ny tekst")
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "enda mer tekst")
                )
            ),
            E_Title1(
                3, true, listOf(
                    E_Literal(1, "En ny tittel "),
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "siste teksten")
                )
            ),
        )

        assertEquals(edited, edited.updatedEditedLetter(next))
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
                    Literal(1, "En tittel "),
                )
            ),
        )
        val edited = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel ", "Her har jeg redigert "),
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!")
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "Noe ny redigert tekst")
                )
            ),
            E_Title1(
                4, true, listOf(
                    E_Literal(1, "En tittel "),
                )
            ),
        )
        val expected = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel ", "Her har jeg redigert "),
                    E_Variable(2, "for deg "),
                    E_Literal(3, "og meg!")
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "Noe ny redigert tekst")
                )
            ),
            E_Title1(
                3, true, listOf(
                    E_Literal(1, "Dette er en ny block før id:4 "),
                )
            ),
            E_Title1(
                4, true, listOf(
                    E_Literal(1, "En tittel "),
                )
            ),
        )

        assertEquals(expected, edited.updatedEditedLetter(next))
    }

    @Test
    fun `unedited content not present in next render are removed`() {
        val next = letter(
            Paragraph(
                1, true, listOf(
                    Literal(11, "Første"),
                    Variable(12, "en variabel"),
                    Literal(13, "Andre"),
                    Variable(14, "andre variabel"),
                    Literal(15, "Tredje"),
                    ItemList(
                        16, listOf(
                            Item(160, listOf(Literal(161, "punkt 1"), Literal(162, "punkt 2"), Literal(163, "punkt 3"))),
                        )
                    ),
                )
            )
        )
        val edited = editedLetter(
            E_Paragraph(
                1, true, listOf(
                    E_Literal(11, "Første"),
                    E_Variable(12, "en variabel"),
                    E_Literal(13, "Andre"),
                    E_Variable(14, "andre variabel"),
                    E_Literal(25, "Tredje"),
                    E_Variable(27, "burde ikke bli med etter rendring"),
                    E_Literal(28, "burde heller ikke bli med etter rendring"),
                    E_ItemList(
                        16, listOf(
                            E_Item(160, listOf(E_Literal(161, "punkt 1"), E_Literal(162, "punkt 2"), E_Literal(163, "punkt 3"))),
                        )
                    ),
                )
            )
        )

        assertEquals(next.toEdit(), edited.updatedEditedLetter(next))
    }

    private fun letter(vararg blocks: Block) =
        RenderedLetterMarkdown(
            title = "En tittel",
            sakspart = Sakspart("Test Testeson", "1234568910", "1234", "20.12.2022"),
            blocks = blocks.toList(),
            signatur = Signatur("Med vennlig hilsen", "Saksbehandler", "Kjersti Saksbehandler", null, "NAV Familie- og pensjonsytelser Porsgrunn")
        )

    private fun editedLetter(vararg blocks: Edit.Block, deleted: Set<Int> = emptySet()): Edit.Letter =
        Edit.Letter(
            blocks.toList().toList(),
            deleted
        )

}