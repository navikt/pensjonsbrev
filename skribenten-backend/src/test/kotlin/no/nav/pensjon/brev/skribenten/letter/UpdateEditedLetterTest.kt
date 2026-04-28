package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.Listetype
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.ElementTags
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.Title1Impl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.Title2Impl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.Title3Impl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.ItemListImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.ItemListImpl.ItemImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.SakspartImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.SignaturImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

class UpdateRenderedLetterTest {

    @Test
    fun `updates fields of editedLetter from renderedLetter`() {
        val firstRender = letter(Title1Impl(1, true, listOf(LiteralImpl(1, "Noe tekst"))))
        val originalEditedLetter = firstRender.toEdit().withSignaturAttestant("original attestant")
        val nextRender = firstRender.copy(
            title = listOf(LiteralImpl(1, "ny tittel11")),
            sakspart = SakspartImpl(
                gjelderNavn = "ny gjelder",
                gjelderFoedselsnummer = Foedselsnummer("nytt fødselsnummer"),
                annenMottakerNavn = null,
                saksnummer = "nytt saksnummer",
                dokumentDato = LocalDate.now(),
            ),
            signatur = SignaturImpl(
                hilsenTekst = "ny hilsenTekst",
                saksbehandlerNavn = "ny saksbehandlernavn",
                attesterendeSaksbehandlerNavn = "ny attesterendenavn",
                navAvsenderEnhet = "ny avsenderenhet"
            ),
        )

        // Signature names are preserved from edited letter, but template fields (hilsenTekst, navAvsenderEnhet) are synced from rendered
        val expected = nextRender.toEdit().copy(
            signatur = SignaturImpl(
                hilsenTekst = nextRender.signatur.hilsenTekst,
                saksbehandlerNavn = originalEditedLetter.signatur.saksbehandlerNavn,
                attesterendeSaksbehandlerNavn = originalEditedLetter.signatur.attesterendeSaksbehandlerNavn,
                navAvsenderEnhet = nextRender.signatur.navAvsenderEnhet,
            )
        )

        assertEquals(expected, originalEditedLetter.updateEditedLetter(nextRender))
    }

    @Test
    fun `no changes in editedLetter and no changes in nextLetter returns same letter`() {
        val rendered = letter(
            Title1Impl(
                1, true, listOf(
                    LiteralImpl(1, "En tittel "),
                    VariableImpl(2, "for deg "),
                    LiteralImpl(3, "og meg!")
                )
            )
        )
        val edited = rendered.toEdit()
        assertEquals(edited, edited.updateEditedLetter(rendered))
    }

    @Test
    fun `literals in editedLetter are not replaced`() {
        val next = letter(
            Title1Impl(
                1, true, listOf(
                    LiteralImpl(1, "En tittel "),
                    VariableImpl(2, "for deg "),
                    LiteralImpl(3, "og meg!")
                )
            )
        )
        val edited = editedLetter {
            title1(id = 1) {
                literal(id = 1, text = "En tittel ", editedText = "Her har jeg redigert")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
        }

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `variables in editedLetter are replaced `() {
        val rendered = letter(
            Title1Impl(
                1, true, listOf(
                    LiteralImpl(1, "En tittel "),
                    VariableImpl(2, "for de andre "),
                    LiteralImpl(3, "og meg!")
                )
            )
        )
        val edited = editedLetter {
            title1(id = 1) {
                literal(id = 1, text = "En tittel ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
        }

        assertEquals(rendered.toEdit(), edited.updateEditedLetter(rendered))
    }

    @Test
    fun `unedited literals no longer present in next render are removed but other edits are kept`() {
        val rendered = letter(
            Title1Impl(
                1, true, listOf(
                    VariableImpl(2, "for deg "),
                )
            )
        )
        val edited = editedLetter {
            title1(id = 1) {
                literal(id = 1, text = "En tittel ", editedText = "Her har jeg redigert")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
        }

        val expected = editedLetter {
            title1(id = 1) {
                literal(id = 1, text = "En tittel ", editedText = "Her har jeg redigert")
                variable(id = 2, text = "for deg ")
            }
        }

        assertEquals(expected, edited.updateEditedLetter(rendered))
    }

    @Test
    fun `new edited content is kept`() {
        val next = letter(
            Title1Impl(
                1, true, listOf(
                    VariableImpl(2, "for deg "),
                    LiteralImpl(3, "og meg!")
                )
            )
        )
        val edited = editedLetter {
            title1(id = 1) {
                literal(text = "", editedText = "Her har jeg lagt til ")
                literal(id = 1, text = "Var med i forrige rendring, men skal ikke være med i neste")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
                literal(text = "", editedText = " Også lagt til")
            }
        }

        val expected = editedLetter {
            title1(id = 1) {
                literal(text = "", editedText = "Her har jeg lagt til ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
                literal(text = "", editedText = " Også lagt til")
            }
        }

        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `new content from next is added`() {
        val next = letter(
            Title1Impl(
                1, true, listOf(
                    VariableImpl(2, "for deg "),
                    LiteralImpl(3, "og meg!"),
                    LiteralImpl(4, " pluss noe mer tekst")
                )
            )
        )
        val edited = editedLetter {
            title1(id = 1) {
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!", editedText = "og dine")
            }
        }

        val expected = editedLetter {
            title1(id = 1) {
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!", editedText = "og dine")
                literal(id = 4, text = " pluss noe mer tekst")
            }
        }

        assertEquals(expected, edited.updateEditedLetter(next))
    }


    @Test
    fun `should fail when edited content has different type than the rendered content with same ID`() {
        val next = letter(
            Title1Impl(
                1, true, listOf(
                    VariableImpl(1, "En tittel "),
                    VariableImpl(2, "for deg "),
                    LiteralImpl(3, "og meg!")
                )
            )
        )
        val edited = editedLetter {
            title1(id = 1) {
                literal(id = 1, text = "En tittel", editedText = "Her er det en ulovlig redigering")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
        }

        assertThrows<UpdateEditedLetterException> { edited.updateEditedLetter(next) }
    }


    @Test
    fun `new edited literal added before a variable should be kept before that variable`() {
        val next = letter(
            Title1Impl(
                1, true, listOf(
                    VariableImpl(1, "En tittel "),
                    VariableImpl(2, "for deg "),
                    LiteralImpl(3, "og meg!")
                )
            )
        )
        val edited = editedLetter {
            title1(id = 1) {
                literal(text = "", editedText = "ny literal")
                variable(id = 1, text = "En tittel ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
        }

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `new edited literal added after a rendered content should keep relative position`() {
        val next = letter(
            Title1Impl(
                1, true, listOf(
                    VariableImpl(1, "En tittel "),
                    VariableImpl(2, "for deg "),
                    LiteralImpl(3, "og meg!"),
                    LiteralImpl(4, "ny rendered literal"),
                )
            )
        )
        val edited = editedLetter {
            title1(id = 1) {
                variable(id = 1, text = "En tittel ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
                literal(text = "", editedText = "ny literal")
            }
        }
        val expected = editedLetter {
            title1(id = 1) {
                variable(id = 1, text = "En tittel ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
                literal(text = "", editedText = "ny literal")
                literal(id = 4, text = "ny rendered literal")
            }
        }

        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `can change type of unedited block`() {
        val next = letter(
            ParagraphImpl(
                1, true, listOf(
                    LiteralImpl(1, "Noe tekst"),
                    VariableImpl(2, "en variabel"),
                )
            )
        )
        val edited = editedLetter {
            title1(id = 1, originalType = Edit.Block.Type.PARAGRAPH) {
                literal(id = 1, text = "Noe tekst")
                variable(id = 2, text = "en variabel")
            }
        }

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `an edited block where type is changed from paragraph to title1 then any fresh nontext content is ignored`() {
        val next = letter(
            ParagraphImpl(
                1, true, listOf(
                    LiteralImpl(1, "Noe tekst "),
                    VariableImpl(2, "med en oppdatert variabel"),
                    LiteralImpl(3, " og noe mer tekst"),
                    ItemListImpl(4, listOf(ItemImpl(41, listOf(LiteralImpl(411, "en punktliste")))), Listetype.PUNKTLISTE),
                )
            )
        )
        val edited = editedLetter {
            title1(id = 1) {
                literal(id = 1, text = "Noe tekst", editedText = "Noe redigert tekst ")
                variable(id = 2, text = "med en variabel")
                literal(id = 3, text = " og noe mer tekst")
            }
        }
        val expected = editedLetter {
            title1(id = 1) {
                literal(id = 1, text = "Noe tekst ", editedText = "Noe redigert tekst ")
                variable(id = 2, text = "med en oppdatert variabel")
                literal(id = 3, text = " og noe mer tekst")
            }
        }
        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `an edited block where type is changed from paragraph to title1 can merge`() {
        val next = letter(
            ParagraphImpl(
                1, true, listOf(
                    LiteralImpl(1, "Noe tekst "),
                    VariableImpl(2, "med en oppdatert variabel"),
                    LiteralImpl(3, " og noe mer tekst"),
                    LiteralImpl(4, " pluss noe ny tekst"),
                )
            )
        )
        val edited = editedLetter {
            title1(id = 1) {
                literal(id = 1, text = "Noe tekst ", editedText = "Noe redigert tekst ")
                variable(id = 2, text = "med en variabel")
                literal(id = 3, text = " og noe mer tekst")
            }
        }
        val expected = editedLetter {
            title1(id = 1) {
                literal(id = 1, text = "Noe tekst ", editedText = "Noe redigert tekst ")
                variable(id = 2, text = "med en oppdatert variabel")
                literal(id = 3, text = " og noe mer tekst")
                literal(id = 4, text = " pluss noe ny tekst")
            }
        }
        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `a new edited block should be kept`() {
        val next = letter(
            Title1Impl(
                1, true, listOf(
                    LiteralImpl(1, "En tittel "),
                    VariableImpl(2, "for deg "),
                    LiteralImpl(3, "og meg!")
                )
            )
        )
        val edited = editedLetter {
            paragraph {
                literal(text = "", editedText = "Noe ny tekst")
            }
            title1(id = 1) {
                literal(id = 1, text = "En tittel ", editedText = "Her har jeg redigert ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
            paragraph {
                literal(text = "", editedText = "Noe mer ny tekst")
            }
        }

        assertEquals(edited, edited.updateEditedLetter(next))
    }


    @Test
    fun `new block from rendered should be added`() {
        val next = letter(
            Title1Impl(
                1, true, listOf(
                    LiteralImpl(1, "En tittel "),
                    VariableImpl(2, "for deg "),
                    LiteralImpl(3, "og meg!")
                )
            ),
            Title1Impl(
                3, true, listOf(
                    LiteralImpl(1, "En ny tittel "),
                )
            ),
        )
        val edited = editedLetter {
            title1(id = 1) {
                literal(id = 1, text = "En tittel ", editedText = "Her har jeg redigert ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
            paragraph {
                literal(text = "Noe mer ny tekst")
            }
        }
        val expected = editedLetter {
            title1(id = 1) {
                literal(id = 1, text = "En tittel ", editedText = "Her har jeg redigert ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
            paragraph {
                literal(text = "Noe mer ny tekst")
            }
            title1(id = 3) {
                literal(id = 1, text = "En ny tittel ")
            }
        }

        assertEquals(expected, edited.updateEditedLetter(next))
    }


    @Test
    fun `keeps new edited blocks in positions relative to original blocks`() {
        val next = letter(
            Title1Impl(
                1, true, listOf(
                    LiteralImpl(1, "En tittel "),
                    VariableImpl(2, "for deg "),
                    LiteralImpl(3, "og meg!")
                )
            ),
            Title1Impl(
                3, true, listOf(
                    LiteralImpl(1, "En ny tittel "),
                )
            ),
        )
        val edited = editedLetter {
            paragraph {
                literal(text = "", editedText = "første teksten")
            }
            title1(id = 1) {
                literal(id = 1, text = "En tittel ", editedText = "Her har jeg redigert ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
            paragraph {
                literal(text = "Noe mer ny tekst")
            }
            paragraph {
                literal(text = "enda mer tekst")
            }
            title1(id = 3) {
                literal(id = 1, text = "En ny tittel ")
            }
            paragraph {
                literal(text = "siste teksten")
            }
        }

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `new edited blocks belongs to the previous rendered block`() {
        val next = letter(
            Title1Impl(
                1, true, listOf(
                    LiteralImpl(1, "En tittel "),
                    VariableImpl(2, "for deg "),
                    LiteralImpl(3, "og meg!")
                )
            ),
            Title1Impl(
                3, true, listOf(
                    LiteralImpl(1, "Dette er en ny block før id:4 "),
                )
            ),
            Title1Impl(
                4, true, listOf(
                    LiteralImpl(1, "En tittel "),
                )
            ),
        )
        val edited = editedLetter {
            title1(id = 1) {
                literal(id = 1, text = "En tittel ", editedText = "Her har jeg redigert ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
            paragraph {
                literal(text = "Noe ny redigert tekst")
            }
            title1(id = 4) {
                literal(id = 1, text = "En tittel ")
            }
        }
        val expected = editedLetter {
            title1(id = 1) {
                literal(id = 1, text = "En tittel ", editedText = "Her har jeg redigert ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
            paragraph {
                literal(text = "Noe ny redigert tekst")
            }
            title1(id = 3) {
                literal(id = 1, text = "Dette er en ny block før id:4 ")
            }
            title1(id = 4) {
                literal(id = 1, text = "En tittel ")
            }
        }

        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `unedited content not present in next render are removed`() {
        val next = letter(
            ParagraphImpl(
                1, true, listOf(
                    LiteralImpl(11, "Første"),
                    VariableImpl(12, "en variabel"),
                    LiteralImpl(13, "Andre"),
                    VariableImpl(14, "andre variabel"),
                    LiteralImpl(15, "Tredje"),
                    ItemListImpl(
                        16, listOf(
                            ItemImpl(160, listOf(LiteralImpl(161, "punkt 1"), LiteralImpl(162, "punkt 2"), LiteralImpl(163, "punkt 3"))),
                        ),
                        Listetype.PUNKTLISTE,
                    ),
                )
            )
        )
        val edited = editedLetter {
            paragraph(id = 1) {
                literal(id = 11, text = "Første")
                variable(id = 12, text = "en variabel")
                literal(id = 13, text = "Andre")
                variable(id = 14, text = "andre variabel")
                literal(id = 25, text = "Tredje")
                variable(id = 27, text = "burde ikke bli med etter rendring")
                literal(id = 28, text = "burde heller ikke bli med etter rendring")
                itemList(id = 16) {
                    item(id = 160) {
                        literal(id = 161, text = "punkt 1")
                        literal(id = 162, text = "punkt 2")
                        literal(id = 163, text = "punkt 3")
                    }
                }
            }
        }

        assertEquals(next.toEdit(), edited.updateEditedLetter(next))
    }

    @Test
    fun `content edited to empty string is kept after render`() {
        val next = letter(
            ParagraphImpl(
                1, true,
                listOf(
                    LiteralImpl(11, "lit1"),
                    VariableImpl(12, "var1"),
                    LiteralImpl(13, "rediger til tom streng"),
                ),
            ),
        )
        val edited = editedLetter {
            paragraph(id = 1) {
                literal(id = 11, text = "lit1")
                variable(id = 12, text = "var1")
                literal(id = 13, text = "rediger til tom streng", editedText = "")
            }
        }

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `deleted blocks are not included in updated letter`() {
        val next = letter(
            ParagraphImpl(
                1, true,
                listOf(
                    LiteralImpl(11, "lit1"),
                ),
            ),
            ParagraphImpl(
                2, true,
                listOf(
                    LiteralImpl(21, "lit2"),
                ),
            ),
        )
        val edited = editedLetter(deleted = setOf(2)) {
            paragraph(id = 1) {
                literal(id = 11, text = "lit1")
            }
        }

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `deleted content are not included in updated letter`() {
        val next = letter(
            ParagraphImpl(
                1, true,
                listOf(
                    LiteralImpl(11, "1lit1"),
                    LiteralImpl(12, "1lit2"),
                ),
            ),
            Title1Impl(
                2, true, listOf(
                    LiteralImpl(21, "2lit1"),
                    LiteralImpl(22, "2lit2"),
                )
            ),
            Title2Impl(
                3, true, listOf(
                    LiteralImpl(31, "3lit1"),
                    LiteralImpl(32, "3lit2"),
                )
            ),
            Title3Impl(
                4, true, listOf(
                    LiteralImpl(41, "4lit1"),
                    LiteralImpl(42, "4lit2"),
                )
            )
        )
        val edited = editedLetter {
            paragraph(id = 1, deletedContent = setOf(12)) {
                literal(id = 11, text = "1lit1")
            }
            title1(id = 2, deletedContent = setOf(22)) {
                literal(id = 21, text = "2lit1")
            }
            title2(id = 3, deletedContent = setOf(32)) {
                literal(id = 31, text = "3lit1")
            }
            title3(id = 4, deletedContent = setOf(42)) {
                literal(id = 41, text = "4lit1")
            }
        }

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `deleted items are not included in updated letter`() {
        val next = letter(
            ParagraphImpl(
                1, true,
                listOf(
                    ItemListImpl(
                        11, listOf(
                            ItemImpl(111, listOf(LiteralImpl(1111, "item 1"))),
                            ItemImpl(112, listOf(LiteralImpl(1121, "item 2"))),
                        ),
                        Listetype.PUNKTLISTE,
                    ),
                ),
            ),
        )
        val edited = editedLetter {
            paragraph(id = 1) {
                itemList(id = 11, deletedItems = setOf(111)) {
                    item(id = 112) {
                        literal(id = 1121, text = "item 2")
                    }
                }
            }
        }
        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `multiple new rendered items are included`() {
        val next = letter(
            ParagraphImpl(
                1, true,
                listOf(
                    LiteralImpl(11, "første"),
                    LiteralImpl(12, "andre"),
                    LiteralImpl(13, "tredje"),
                    LiteralImpl(14, "fjerde"),
                )
            )
        )
        val edited = editedLetter {
            paragraph(id = 1) {
                literal(id = 14, text = "fjerde", editedText = "fjerdeEdited")
            }
        }
        val expected = editedLetter {
            paragraph(id = 1) {
                literal(id = 11, text = "første")
                literal(id = 12, text = "andre")
                literal(id = 13, text = "tredje")
                literal(id = 14, text = "fjerde", editedText = "fjerdeEdited")
            }
        }
        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `variables moved to a new block will be updated to new value from rendered`() {
        val next = letter(
            ParagraphImpl(
                1, true,
                listOf(
                    LiteralImpl(11, "en literal"),
                    VariableImpl(12, "oppdatert variabel"),
                    VariableImpl(1311, "oppdatert variabel 2"),
                    VariableImpl(141111, "oppdatert variabel 3"),
                    VariableImpl(14211, "oppdatert variabel 4"),
                )
            )
        )
        val edited = editedLetter(deleted = setOf(1)) {
            paragraph {
                literal(id = 11, text = "en literal")
                variable(id = 12, text = "en variabel")
                itemList(id = 13) {
                    item(id = 131) {
                        variable(id = 1311, text = "variabel 2")
                    }
                }
                table(id = 14) {
                    header(id = 141) {
                        colSpec(id = 1411, cellId = 14111) {
                            variable(id = 141111, text = "variabel 3")
                        }
                    }
                    row(id = 142) {
                        cell(id = 1421) {
                            variable(id = 14211, text = "variabel 4")
                        }
                    }
                }
            }
            title1 {
                variable(id = 1311, text = "variabel 2")
            }
            title2 {
                variable(id = 14211, text = "variabel 4")
            }
        }
        val expected = editedLetter(deleted = setOf(1)) {
            paragraph {
                literal(id = 11, text = "en literal")
                variable(id = 12, text = "oppdatert variabel")
                itemList(id = 13) {
                    item(id = 131) {
                        variable(id = 1311, text = "oppdatert variabel 2")
                    }
                }
                table(id = 14) {
                    header(id = 141) {
                        colSpec(id = 1411, cellId = 14111) {
                            variable(id = 141111, text = "oppdatert variabel 3")
                        }
                    }
                    row(id = 142) {
                        cell(id = 1421) {
                            variable(id = 14211, text = "oppdatert variabel 4")
                        }
                    }
                }
            }
            title1 {
                variable(id = 1311, text = "oppdatert variabel 2")
            }
            title2 {
                variable(id = 14211, text = "oppdatert variabel 4")
            }
        }
        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `variables moved to a new block no longer present in rendered cannot be updated and is converted to literal`() {
        val next = letter(
            ParagraphImpl(
                1, true,
                listOf(
                    LiteralImpl(11, "en literal"),
                )
            )
        )
        val edited = editedLetter(deleted = setOf(1)) {
            paragraph {
                literal(id = 11, text = "en literal")
                variable(id = 12, text = "en variabel")
            }
        }
        val expected = editedLetter(deleted = setOf(1)) {
            paragraph {
                literal(id = 11, text = "en literal")
                literal(id = 12, text = "en variabel")
            }
        }
        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `variables moved to another parent block will be updated`() {
        val next = letter(
            ParagraphImpl(
                1, true,
                listOf(
                    LiteralImpl(11, "en literal"),
                    ItemListImpl(12, listOf(ItemImpl(121, listOf(VariableImpl(1211, "oppdatert v1")))), Listetype.PUNKTLISTE),
                )
            ),
            ParagraphImpl(
                2, true,
                listOf(LiteralImpl(21, "to literal")),
            )
        )
        val edited = editedLetter {
            paragraph(id = 1, deletedContent = setOf(12)) {
                literal(id = 11, text = "en literal")
            }
            paragraph(id = 2) {
                variable(id = 1211, text = "variable 1", parentId = 121)
                literal(id = 21, text = "to literal")
            }
        }
        val expected = editedLetter {
            paragraph(id = 1, deletedContent = setOf(12)) {
                literal(id = 11, text = "en literal")
            }
            paragraph(id = 2) {
                variable(id = 1211, text = "oppdatert v1", parentId = 121)
                literal(id = 21, text = "to literal")
            }
        }
        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `variables moved from itemList to parent block will be updated`() {
        val next = letter(
            ParagraphImpl(
                1, true,
                listOf(
                    LiteralImpl(11, "en literal"),
                    ItemListImpl(12, listOf(ItemImpl(121, listOf(VariableImpl(1211, "oppdatert v1")))), Listetype.PUNKTLISTE),
                )
            )
        )
        val edited = editedLetter {
            paragraph(id = 1, deletedContent = setOf(12)) {
                literal(id = 11, text = "en literal")
                variable(id = 1211, text = "variable 1", parentId = 121)
            }
        }
        val expected = editedLetter {
            paragraph(id = 1, deletedContent = setOf(12)) {
                literal(id = 11, text = "en literal")
                variable(id = 1211, text = "oppdatert v1", parentId = 121)
            }
        }
        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `variables moved to another parent block no longer present will be updated if variable is present in rendered`() {
        val next = letter(
            ParagraphImpl(
                1, true,
                listOf(LiteralImpl(11, "en literal"), VariableImpl(12, "oppdatert v1")),
            ),
        )
        val edited = editedLetter {
            paragraph(id = 1) {
                literal(id = 11, text = "en literal")
                variable(id = 12, text = "variable 1")
            }
            paragraph(id = 2) {
                variable(id = 12, text = "variable 1")
                literal(id = 21, text = "hei", editedText = "heisann")
            }
        }
        val expected = editedLetter {
            paragraph(id = 1) {
                literal(id = 11, text = "en literal")
                variable(id = 12, text = "oppdatert v1")
            }
            paragraph(id = 2, missingFromTemplate = true) {
                variable(id = 12, text = "oppdatert v1")
                literal(id = 21, text = "hei", editedText = "heisann")
            }
        }
        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `content moved into an item list is kept`() {
        val next = letter(
            ParagraphImpl(
                1, true,
                listOf(
                    LiteralImpl(11, "lit1"),
                    VariableImpl(12, "var2"),
                    ItemListImpl(13, listOf(ItemImpl(131, listOf(LiteralImpl(1311, "punkt1")))), Listetype.PUNKTLISTE),
                    LiteralImpl(14, "lit2"),
                )
            )
        )
        val edited = editedLetter {
            paragraph(id = 1, deletedContent = setOf(11, 12, 14)) {
                itemList(id = 13) {
                    item {
                        literal(id = 11, text = "lit1", parentId = 1)
                        variable(id = 12, text = "var2", parentId = 1)
                    }
                    item(id = 131) {
                        literal(id = 1311, text = "punkt1")
                        literal(id = 14, text = "lit2", parentId = 1)
                    }
                }
            }
        }
        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `content moved to another block is kept`() {
        val next = letter(
            ParagraphImpl(
                1, true,
                listOf(LiteralImpl(11, "lit1"), VariableImpl(12, "var2"), ItemListImpl(13, listOf(ItemImpl(131, listOf(LiteralImpl(1311, "punkt1")))), Listetype.PUNKTLISTE), LiteralImpl(14, "lit2"))
            )
        )
        val edited = editedLetter {
            paragraph {
                literal(id = 11, text = "lit1", parentId = 1)
                variable(id = 12, text = "var2", parentId = 1)
            }
            paragraph(id = 1, deletedContent = setOf(11, 12)) {
                itemList(id = 13) {
                    item(id = 131) {
                        literal(id = 1311, text = "punkt1")
                    }
                }
                literal(id = 14, text = "lit2")
            }
        }
        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `BrevdataEllerFritekst som begynner som fritekst og endres til variabel senere fordi saken oppdateres`() {
        // Om et BrevdataEllerFritekst-expression i et brev ikke får brevdata ved første render, men får en verdi etter at saksbehandler har fylt inn fritekst så skal det ikke feile i Skribenten.
        val editedLetter1 = editedLetter(
            E_Paragraph(1, true, listOf(
                E_Literal(11, "fast tekst"),
                E_Literal(12, "fritekst", editedText = "fylt inn fritekst", tags = setOf(ElementTags.FRITEKST)),
            ))
        )

        val brevdataVariabelHarEnVerdi = letter(
            ParagraphImpl(1, true, listOf(
                LiteralImpl(11, "fast tekst oppdatert"),
                VariableImpl(12, "verdi fra brevdata"),
            ))
        )

        // Saksbehandlers redigering beholdes
        val expected = editedLetter(
            E_Paragraph(1, true, listOf(
                E_Literal(11, "fast tekst oppdatert"),
                E_Literal(12, "fritekst", editedText = "fylt inn fritekst", tags = setOf(ElementTags.FRITEKST)),
            ))
        )

        assertEquals(expected, editedLetter1.updateEditedLetter(brevdataVariabelHarEnVerdi))
    }

}