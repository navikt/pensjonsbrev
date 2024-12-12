package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.LetterMarkup.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block.Paragraph
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block.Title1
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList.Item
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.Literal
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.Variable
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.InstanceOfAssertFactories
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph as E_Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Title1 as E_Title1
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Title2 as E_Title2
import no.nav.pensjon.brev.skribenten.letter.Edit.Identifiable as E_Identifiable
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.ItemList as E_ItemList
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.ItemList.Item as E_Item
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Table as E_Table
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Table.Cell as E_Cell
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Table.ColumnSpec as E_ColumnSpec
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Table.Header as E_Header
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Table.Row as E_Row
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.FontType as E_FontType
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal as E_Literal
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable as E_Variable

class UpdateRenderedLetterTest {

    @Test
    fun `updates fields of editedLetter from renderedLetter`() {
        val rendered = letter(Title1(1, true, listOf(Literal(1, "Noe tekst"))))
        val next = rendered.copy(
            title = "ny tittel11",
            sakspart = Sakspart(
                "ny gjelder",
                "nytt fødselsnummer",
                "nytt saksnummer",
                "ny dato"
            ),
            signatur = Signatur("ny hilsenTekst", "ny saksbehandler rolle tekst", "ny saksbehandlernavn", "ny attesterendenavn", "ny avsenderenhet"),
        )

        assertEquals(next.toEdit(), rendered.toEdit().copy(deletedBlocks = setOf(-1)).updateEditedLetter(next))
    }

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
        assertEquals(edited, edited.updateEditedLetter(rendered))
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
                    E_Literal(1, "En tittel ", E_FontType.PLAIN, "Her har jeg redigert"),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN)
                )
            )
        )

        assertEquals(edited, edited.updateEditedLetter(next))
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
                    E_Literal(1, "En tittel ", E_FontType.PLAIN),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN)
                )
            )
        )

        assertEquals(rendered.toEdit(), edited.updateEditedLetter(rendered))
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
                    E_Literal(1, "En tittel ", E_FontType.PLAIN, "Her har jeg redigert"),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN)
                )
            )
        )

        val expected = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel ", E_FontType.PLAIN, "Her har jeg redigert"),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                )
            )
        )

        assertEquals(expected, edited.updateEditedLetter(rendered))
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
                    E_Literal(null, "", E_FontType.PLAIN, "Her har jeg lagt til "),
                    E_Literal(1, "Var med i forrige rendring, men skal ikke være med i neste", E_FontType.PLAIN),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN),
                    E_Literal(null, "", E_FontType.PLAIN, " Også lagt til"),
                )
            )
        )

        val expected = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Literal(null, "", E_FontType.PLAIN, "Her har jeg lagt til "),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN),
                    E_Literal(null, "", E_FontType.PLAIN, " Også lagt til"),
                )
            )
        )

        assertEquals(expected, edited.updateEditedLetter(next))
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
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN, "og dine"),
                )
            )
        )

        val expected = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN, "og dine"),
                    E_Literal(4, " pluss noe mer tekst", E_FontType.PLAIN),
                )
            )
        )

        assertEquals(expected, edited.updateEditedLetter(next))
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
                    E_Literal(1, "En tittel", E_FontType.PLAIN, "Her er det en ulovlig redigering"),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN)
                )
            )
        )

        assertThrows<UpdateEditedLetterException> { edited.updateEditedLetter(next) }
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
                    E_Literal(null, "", E_FontType.PLAIN, "ny literal"),
                    E_Variable(1, "En tittel ", E_FontType.PLAIN),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN)
                )
            )
        )

        assertEquals(edited, edited.updateEditedLetter(next))
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
                    E_Variable(1, "En tittel ", E_FontType.PLAIN),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN),
                    E_Literal(null, "", E_FontType.PLAIN, "ny literal"),
                )
            )
        )
        val expected = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Variable(1, "En tittel ", E_FontType.PLAIN),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN),
                    E_Literal(null, "", E_FontType.PLAIN, "ny literal"),
                    E_Literal(4, "ny rendered literal", E_FontType.PLAIN),
                )
            )
        )

        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `can change type of unedited block`() {
        val next = letter(
            Paragraph(
                1, true, listOf(
                    Literal(1, "Noe tekst"),
                    Variable(2, "en variabel"),
                )
            )
        )
        val edited = editedLetter(
            E_Title1(
                1, true,
                listOf(
                    E_Literal(1, "Noe tekst", E_FontType.PLAIN),
                    E_Variable(2, "en variabel", E_FontType.PLAIN),
                ),
                originalType = Edit.Block.Type.PARAGRAPH,
            )
        )

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `an edited block where type is changed from paragraph to title1 then any fresh nontext content is ignored`() {
        val next = letter(
            Paragraph(
                1, true, listOf(
                    Literal(1, "Noe tekst "),
                    Variable(2, "med en oppdatert variabel"),
                    Literal(3, " og noe mer tekst"),
                    ItemList(4, listOf(Item(41, listOf(Literal(411, "en punktliste"))))),
                )
            )
        )
        val edited = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "Noe tekst", E_FontType.PLAIN, "Noe redigert tekst "),
                    E_Variable(2, "med en variabel", E_FontType.PLAIN),
                    E_Literal(3, " og noe mer tekst", E_FontType.PLAIN),
                )
            )
        )
        val expected = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "Noe tekst ", E_FontType.PLAIN, "Noe redigert tekst "),
                    E_Variable(2, "med en oppdatert variabel", E_FontType.PLAIN),
                    E_Literal(3, " og noe mer tekst", E_FontType.PLAIN),
                )
            ),
        )
        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `an edited block where type is changed from paragraph to title1 can merge`() {
        val next = letter(
            Paragraph(
                1, true, listOf(
                    Literal(1, "Noe tekst "),
                    Variable(2, "med en oppdatert variabel"),
                    Literal(3, " og noe mer tekst"),
                    Literal(4, " pluss noe ny tekst"),
                )
            )
        )
        val edited = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "Noe tekst ", E_FontType.PLAIN, "Noe redigert tekst "),
                    E_Variable(2, "med en variabel", E_FontType.PLAIN),
                    E_Literal(3, " og noe mer tekst", E_FontType.PLAIN),
                )
            )
        )
        val expected = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "Noe tekst ", E_FontType.PLAIN, "Noe redigert tekst "),
                    E_Variable(2, "med en oppdatert variabel", E_FontType.PLAIN),
                    E_Literal(3, " og noe mer tekst", E_FontType.PLAIN),
                    E_Literal(4, " pluss noe ny tekst", E_FontType.PLAIN),
                )
            )
        )
        assertEquals(expected, edited.updateEditedLetter(next))
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
                    E_Literal(null, "", E_FontType.PLAIN, "Noe ny tekst")
                )
            ),
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel ", E_FontType.PLAIN, "Her har jeg redigert "),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN)
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "", E_FontType.PLAIN, "Noe mer ny tekst")
                )
            ),
        )

        assertEquals(edited, edited.updateEditedLetter(next))
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
                    E_Literal(1, "En tittel ", E_FontType.PLAIN, "Her har jeg redigert "),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN)
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "Noe mer ny tekst", E_FontType.PLAIN)
                )
            ),
        )
        val expected = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel ", E_FontType.PLAIN, "Her har jeg redigert "),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN)
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "Noe mer ny tekst", E_FontType.PLAIN)
                )
            ),
            E_Title1(
                3, true, listOf(
                    E_Literal(1, "En ny tittel ", E_FontType.PLAIN),
                )
            ),
        )

        assertEquals(expected, edited.updateEditedLetter(next))
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
                    E_Literal(null, "", E_FontType.PLAIN, "første teksten")
                )
            ),
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel ", E_FontType.PLAIN, "Her har jeg redigert "),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN)
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "Noe mer ny tekst", E_FontType.PLAIN)
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "enda mer tekst", E_FontType.PLAIN)
                )
            ),
            E_Title1(
                3, true, listOf(
                    E_Literal(1, "En ny tittel ", E_FontType.PLAIN),
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "siste teksten", E_FontType.PLAIN)
                )
            ),
        )

        assertEquals(edited, edited.updateEditedLetter(next))
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
                    E_Literal(1, "En tittel ", E_FontType.PLAIN, "Her har jeg redigert "),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN)
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "Noe ny redigert tekst", E_FontType.PLAIN)
                )
            ),
            E_Title1(
                4, true, listOf(
                    E_Literal(1, "En tittel ", E_FontType.PLAIN),
                )
            ),
        )
        val expected = editedLetter(
            E_Title1(
                1, true, listOf(
                    E_Literal(1, "En tittel ", E_FontType.PLAIN, "Her har jeg redigert "),
                    E_Variable(2, "for deg ", E_FontType.PLAIN),
                    E_Literal(3, "og meg!", E_FontType.PLAIN)
                )
            ),
            E_Paragraph(
                null, true, listOf(
                    E_Literal(null, "Noe ny redigert tekst", E_FontType.PLAIN)
                )
            ),
            E_Title1(
                3, true, listOf(
                    E_Literal(1, "Dette er en ny block før id:4 ", E_FontType.PLAIN),
                )
            ),
            E_Title1(
                4, true, listOf(
                    E_Literal(1, "En tittel ", E_FontType.PLAIN),
                )
            ),
        )

        assertEquals(expected, edited.updateEditedLetter(next))
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
                    E_Literal(11, "Første", E_FontType.PLAIN),
                    E_Variable(12, "en variabel", E_FontType.PLAIN),
                    E_Literal(13, "Andre", E_FontType.PLAIN),
                    E_Variable(14, "andre variabel", E_FontType.PLAIN),
                    E_Literal(25, "Tredje", E_FontType.PLAIN),
                    E_Variable(27, "burde ikke bli med etter rendring", E_FontType.PLAIN),
                    E_Literal(28, "burde heller ikke bli med etter rendring", E_FontType.PLAIN),
                    E_ItemList(
                        16, listOf(
                            E_Item(
                                160,
                                listOf(
                                    E_Literal(161, "punkt 1", E_FontType.PLAIN),
                                    E_Literal(162, "punkt 2", E_FontType.PLAIN),
                                    E_Literal(163, "punkt 3", E_FontType.PLAIN)
                                )
                            ),
                        )
                    ),
                )
            )
        )

        assertEquals(next.toEdit(), edited.updateEditedLetter(next))
    }

    @Test
    fun `content edited to empty string is kept after render`() {
        val next = letter(
            Paragraph(
                1, true,
                listOf(
                    Literal(11, "lit1"),
                    Variable(12, "var1"),
                    Literal(13, "rediger til tom streng"),
                ),
            ),
        )
        val edited = editedLetter(
            E_Paragraph(
                1, true,
                listOf(
                    E_Literal(11, "lit1", E_FontType.PLAIN),
                    E_Variable(12, "var1", E_FontType.PLAIN),
                    E_Literal(13, "rediger til tom streng", E_FontType.PLAIN, ""),
                ),
            ),
        )

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `deleted blocks are not included in updated letter`() {
        val next = letter(
            Paragraph(
                1, true,
                listOf(
                    Literal(11, "lit1"),
                ),
            ),
            Paragraph(
                2, true,
                listOf(
                    Literal(21, "lit2"),
                ),
            ),
        )
        val edited = editedLetter(
            E_Paragraph(
                1, true,
                listOf(
                    E_Literal(11, "lit1", E_FontType.PLAIN),
                ),
            ),
            deleted = setOf(2),
        )

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `deleted content are not included in updated letter`() {
        val next = letter(
            Paragraph(
                1, true,
                listOf(
                    Literal(11, "1lit1"),
                    Literal(12, "1lit2"),
                ),
            ),
            Title1(
                2, true, listOf(
                    Literal(21, "2lit1"),
                    Literal(22, "2lit2"),
                )
            ),
            Block.Title2(
                3, true, listOf(
                    Literal(31, "3lit1"),
                    Literal(32, "3lit2"),
                )
            ),
        )
        val edited = editedLetter(
            E_Paragraph(
                1, true, listOf(
                    E_Literal(11, "1lit1", E_FontType.PLAIN),
                ),
                deletedContent = setOf(12)
            ),
            E_Title1(
                2, true, listOf(
                    E_Literal(21, "2lit1", E_FontType.PLAIN),
                ),
                deletedContent = setOf(22)
            ),
            E_Title2(
                3, true, listOf(
                    E_Literal(31, "3lit1", E_FontType.PLAIN),
                ),
                deletedContent = setOf(32)
            ),
        )

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `deleted items are not included in updated letter`() {
        val next = letter(
            Paragraph(
                1, true,
                listOf(
                    ItemList(
                        11, listOf(
                            Item(111, listOf(Literal(1111, "item 1"))),
                            Item(112, listOf(Literal(1121, "item 2"))),
                        )
                    ),
                ),
            ),
        )
        val edited = editedLetter(
            E_Paragraph(
                1, true,
                listOf(
                    E_ItemList(
                        11,
                        listOf(
                            E_Item(112, listOf(E_Literal(1121, "item 2", E_FontType.PLAIN))),
                        ),
                        setOf(111),
                    ),
                ),
            ),
        )
        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `multiple new rendered items are included`() {
        val next = letter(
            Paragraph(
                1, true,
                listOf(
                    Literal(11, "første"),
                    Literal(12, "andre"),
                    Literal(13, "tredje"),
                    Literal(14, "fjerde"),
                )
            )
        )
        val edited = editedLetter(
            E_Paragraph(
                1, true,
                listOf(
                    E_Literal(14, "fjerde", E_FontType.PLAIN, "fjerdeEdited"),
                )
            )
        )
        val expected = editedLetter(
            E_Paragraph(
                1, true,
                listOf(
                    E_Literal(11, "første", E_FontType.PLAIN),
                    E_Literal(12, "andre", E_FontType.PLAIN),
                    E_Literal(13, "tredje", E_FontType.PLAIN),
                    E_Literal(14, "fjerde", E_FontType.PLAIN, "fjerdeEdited"),
                )
            )
        )
        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `variables moved to a new block will be updated to new value from rendered`() {
        val next = letter(
            Paragraph(
                1, true,
                listOf(
                    Literal(11, "en literal"),
                    Variable(12, "oppdatert variabel"),
                    Variable(1311, "oppdatert variabel 2"),
                    Variable(141111, "oppdatert variabel 3"),
                    Variable(14211, "oppdatert variabel 4"),
                )
            )
        )
        val edited = editedLetter(
            E_Paragraph(
                null, true,
                listOf(
                    E_Literal(11, "en literal", E_FontType.PLAIN),
                    E_Variable(12, "en variabel", E_FontType.PLAIN),
                    E_ItemList(13, items = listOf(E_Item(131, listOf(E_Variable(1311, "variabel 2", E_FontType.PLAIN))))),
                    E_Table(
                        id = 14,
                        header = E_Header(
                            141,
                            colSpec = listOf(
                                E_ColumnSpec(
                                    1411,
                                    E_Cell(14111, listOf(E_Variable(141111, "variabel 3", E_FontType.PLAIN))),
                                    Edit.ParagraphContent.Table.ColumnAlignment.LEFT,
                                    1
                                )
                            )
                        ),
                        rows = listOf(E_Row(142, listOf(E_Cell(1421, listOf(E_Variable(14211, "variabel 4", E_FontType.PLAIN)))))),
                    )
                )
            ),
            E_Title1(null, true, listOf(E_Variable(1311, "variabel 2", E_FontType.PLAIN))),
            E_Title2(null, true, listOf(E_Variable(14211, "variabel 4", E_FontType.PLAIN))),
            deleted = setOf(1),
        )
        val expected = editedLetter(
            E_Paragraph(
                null, true,
                listOf(
                    E_Literal(11, "en literal", E_FontType.PLAIN),
                    E_Variable(12, "oppdatert variabel", E_FontType.PLAIN),
                    E_ItemList(13, items = listOf(E_Item(131, listOf(E_Variable(1311, "oppdatert variabel 2", E_FontType.PLAIN))))),
                    E_Table(
                        id = 14,
                        header = E_Header(
                            141,
                            colSpec = listOf(
                                E_ColumnSpec(
                                    1411,
                                    E_Cell(14111, listOf(E_Variable(141111, "oppdatert variabel 3", E_FontType.PLAIN))),
                                    Edit.ParagraphContent.Table.ColumnAlignment.LEFT,
                                    1
                                )
                            )
                        ),
                        rows = listOf(E_Row(142, listOf(E_Cell(1421, listOf(E_Variable(14211, "oppdatert variabel 4", E_FontType.PLAIN)))))),
                    )
                )
            ),
            E_Title1(null, true, listOf(E_Variable(1311, "oppdatert variabel 2", E_FontType.PLAIN))),
            E_Title2(null, true, listOf(E_Variable(14211, "oppdatert variabel 4", E_FontType.PLAIN))),
            deleted = setOf(1),
        )
        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `variables moved to a new block no longer present in rendered cannot be updated and is converted to literal`() {
        val next = letter(
            Paragraph(
                1, true,
                listOf(
                    Literal(11, "en literal"),
                )
            )
        )
        val edited = editedLetter(
            E_Paragraph(
                null, true,
                listOf(
                    E_Literal(11, "en literal", E_FontType.PLAIN),
                    E_Variable(12, "en variabel", E_FontType.PLAIN),
                )
            ),
            deleted = setOf(1),
        )
        val expected = editedLetter(
            E_Paragraph(
                null, true,
                listOf(
                    E_Literal(11, "en literal", E_FontType.PLAIN),
                    E_Literal(12, "en variabel", E_FontType.PLAIN),
                )
            ),
            deleted = setOf(1),
        )
        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `content moved into an item list is kept`() {
        val next = letter(
            Paragraph(
                1, true,
                listOf(
                    Literal(11, "lit1"),
                    Variable(12, "var2"),
                    ItemList(13, listOf(Item(131, listOf(Literal(1311, "punkt1"))))),
                    Literal(14, "lit2"),
                )
            )
        )
        val edited = editedLetter(
            E_Paragraph(
                1, true,
                listOf(
                    E_ItemList(
                        13,
                        listOf(
                            E_Item(null, listOf(E_Literal(11, "lit1", parentId = 1), E_Variable(12, "var2", parentId = 1))),
                            E_Item(131, listOf(E_Literal(1311, "punkt1"), E_Literal(14, "lit2", parentId = 1)))
                        )
                    ),
                ),
                deletedContent = setOf(11, 12, 14)
            )
        )
        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `content moved to another block is kept`() {
        val next = letter(
            Paragraph(
                1, true,
                listOf(Literal(11, "lit1"), Variable(12, "var2"), ItemList(13, listOf(Item(131, listOf(Literal(1311, "punkt1"))))), Literal(14, "lit2"))
            )
        )
        val edited = editedLetter(
            E_Paragraph(
                null, true,
                listOf(E_Literal(11, "lit1", parentId = 1), E_Variable(12, "var2", parentId = 1))
            ),
            E_Paragraph(
                1, true,
                listOf(
                    E_ItemList(13, listOf(E_Item(131, listOf(E_Literal(1311, "punkt1"))))),
                    E_Literal(14, "lit2")
                ),
                deletedContent = setOf(11, 12)
            )
        )
        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `parentIds will be fixed for edited letters that does not have them`() {
        val next = letter(
            Paragraph(
                1, true,
                listOf(
                    Literal(11, "lit1"),
                    Variable(12, "var2"),
                    ItemList(13, listOf(Item(131, listOf(Literal(1311, "punkt1"))))),
                    Literal(14, "lit2"),
                    Table(
                        15,
                        listOf(Row(152, listOf(Cell(1521, listOf(Literal(15211, "cell 1")))))),
                        Header(151, listOf(ColumnSpec(1511, Cell(15111, listOf(Literal(151111, "title cell 1"))), ColumnAlignment.LEFT, 1))),
                    ),
                )
            )
        )
        val edited = editedLetter(
            E_Paragraph(
                1, true,
                listOf(
                    E_Literal(11, "lit1"),
                    E_Variable(12, "var2"),
                    E_ItemList(13, listOf(E_Item(131, listOf(E_Literal(1311, "punkt1"))))),
                    E_Literal(14, "lit2"),
                    E_Table(
                        15,
                        listOf(E_Row(152, listOf(E_Cell(1521, listOf(E_Literal(15211, "cell 1")))))),
                        E_Header(151, listOf(E_ColumnSpec(1511, E_Cell(15111, listOf(E_Literal(151111, "title cell 1"))), E_Table.ColumnAlignment.LEFT, 1))),
                    ),
                )
            ),
            fixParentIds = false,
        )

        assertThat(edited.identifiable.toList()).allSatisfy { assertThat(it).extracting(E_Identifiable::parentId).isNull() }

        val updated = edited.updateEditedLetter(next)
        assertThat(updated.identifiable.filter { it !is Edit.Block }.toList()).allSatisfy { assertThat(it).extracting(E_Identifiable::parentId, InstanceOfAssertFactories.INTEGER) }
        assertEquals(edited.fixParentIds(), updated)
    }
}