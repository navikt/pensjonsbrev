package no.nav.pensjon.brev.skribenten.letter.v2

import no.nav.brev.brevbaker.markup.outline.EditBehaviour
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.cell
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.columnSpec
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.header
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.item
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.itemList
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.literal
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.numberedList
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.paragraph
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.row
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.table
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.title2
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.title3
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.title4
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2.variable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UpdateEditedLetterV2Test {

    @Test
    fun `updates fields of editedLetter from renderedLetter`() {
        val firstRender = letter(title2(1, literal(1, "Noe tekst")))
        val originalEditedLetter = firstRender.toEdit().withSignaturAttestant("original attestant")
        val nextRender = letter(
            title2(1, literal(1, "Noe tekst")),
            title = "ny tittel11",
            saksinformasjon = MarkupV2.saksinformasjon(
                gjelderNavn = "ny gjelder",
                gjelderPersonidentifikator = "nytt fødselsnummer",
                saksnummer = "nytt saksnummer",
            ),
            signatur = MarkupV2.signatur(
                saksbehandlerNavn = "ny saksbehandlernavn",
                attesterendeSaksbehandlerNavn = "ny attesterendenavn",
                navAvsenderEnhet = "ny avsenderenhet",
            ),
        )

        val expected = nextRender.toEdit().copy(
            signatur = EditV2.Signatur(
                saksbehandlerNavn = originalEditedLetter.signatur.saksbehandlerNavn,
                attesterendeSaksbehandlerNavn = "original attestant",
                navAvsenderEnhet = "ny avsenderenhet",
            )
        )

        assertEquals(expected, originalEditedLetter.updateEditedLetter(nextRender))
    }

    @Test
    fun `updateSaksinformasjonOgSignatur takes saksinformasjon and avsenderenhet from rendered but keeps signatur names`() {
        val edited = editedLetter {
            paragraph(id = 1) { literal(id = 11, text = "tekst", editedText = "redigert") }
        }.withSignaturSaksbehandler("Redigert Saksbehandler").withSignaturAttestant("Redigert Attestant")
        val rendered = letter(
            paragraph(1, literal(11, "ny tekst")),
            saksinformasjon = MarkupV2.saksinformasjon(gjelderNavn = "ny gjelder", saksnummer = "999"),
            signatur = MarkupV2.signatur(saksbehandlerNavn = "Ny", attesterendeSaksbehandlerNavn = "Ny attestant", navAvsenderEnhet = "ny enhet"),
        )

        val expected = edited.copy(
            saksinformasjon = rendered.saksinformasjon.toEdit(),
            signatur = EditV2.Signatur("Redigert Saksbehandler", "Redigert Attestant", "ny enhet"),
        )

        assertEquals(expected, edited.updateSaksinformasjonOgSignatur(rendered))
    }

    @Test
    fun `no changes in editedLetter and no changes in nextLetter returns same letter`() {
        val rendered = letter(
            title2(1, literal(1, "En tittel "), variable(2, "for deg "), literal(3, "og meg!"))
        )
        val edited = rendered.toEdit()
        assertEquals(edited, edited.updateEditedLetter(rendered))
    }

    @Test
    fun `literals in editedLetter are not replaced`() {
        val next = letter(
            title2(1, literal(1, "En tittel "), variable(2, "for deg "), literal(3, "og meg!"))
        )
        val edited = editedLetter {
            title2(id = 1) {
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
            title2(1, literal(1, "En tittel "), variable(2, "for de andre "), literal(3, "og meg!"))
        )
        val edited = editedLetter {
            title2(id = 1) {
                literal(id = 1, text = "En tittel ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
        }

        assertEquals(rendered.toEdit(), edited.updateEditedLetter(rendered))
    }

    @Test
    fun `unedited literals no longer present in next render are removed but other edits are kept`() {
        val rendered = letter(title2(1, variable(2, "for deg ")))
        val edited = editedLetter {
            title2(id = 1) {
                literal(id = 1, text = "En tittel ", editedText = "Her har jeg redigert")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
        }

        val expected = editedLetter {
            title2(id = 1) {
                literal(id = 1, text = "En tittel ", editedText = "Her har jeg redigert")
                variable(id = 2, text = "for deg ")
            }
        }

        assertEquals(expected, edited.updateEditedLetter(rendered))
    }

    @Test
    fun `new edited content is kept`() {
        val next = letter(title2(1, variable(2, "for deg "), literal(3, "og meg!")))
        val edited = editedLetter {
            title2(id = 1) {
                literal(text = "", editedText = "Her har jeg lagt til ")
                literal(id = 1, text = "Var med i forrige rendring, men skal ikke være med i neste")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
                literal(text = "", editedText = " Også lagt til")
            }
        }

        val expected = editedLetter {
            title2(id = 1) {
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
            title2(1, variable(2, "for deg "), literal(3, "og meg!"), literal(4, " pluss noe mer tekst"))
        )
        val edited = editedLetter {
            title2(id = 1) {
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!", editedText = "og dine")
            }
        }

        val expected = editedLetter {
            title2(id = 1) {
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!", editedText = "og dine")
                literal(id = 4, text = " pluss noe mer tekst")
            }
        }

        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `new edited literal added before a variable should be kept before that variable`() {
        val next = letter(
            title2(1, variable(1, "En tittel "), variable(2, "for deg "), literal(3, "og meg!"))
        )
        val edited = editedLetter {
            title2(id = 1) {
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
            title2(1, variable(1, "En tittel "), variable(2, "for deg "), literal(3, "og meg!"), literal(4, "ny rendered literal"))
        )
        val edited = editedLetter {
            title2(id = 1) {
                variable(id = 1, text = "En tittel ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
                literal(text = "", editedText = "ny literal")
            }
        }
        val expected = editedLetter {
            title2(id = 1) {
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
        val next = letter(paragraph(1, literal(1, "Noe tekst"), variable(2, "en variabel")))
        val edited = editedLetter {
            title2(id = 1, originalType = EditV2.Block.Type.PARAGRAPH) {
                literal(id = 1, text = "Noe tekst")
                variable(id = 2, text = "en variabel")
            }
        }

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `an edited block where type is changed from paragraph to title2 keeps its type and fresh list block is added`() {
        val next = letter(
            paragraph(1, literal(1, "Noe tekst "), variable(2, "med en oppdatert variabel"), literal(3, " og noe mer tekst")),
            itemList(4, item(41, literal(411, "en punktliste"))),
        )
        val edited = editedLetter {
            title2(id = 1) {
                literal(id = 1, text = "Noe tekst", editedText = "Noe redigert tekst ")
                variable(id = 2, text = "med en variabel")
                literal(id = 3, text = " og noe mer tekst")
            }
        }
        val expected = editedLetter {
            title2(id = 1) {
                literal(id = 1, text = "Noe tekst ", editedText = "Noe redigert tekst ")
                variable(id = 2, text = "med en oppdatert variabel")
                literal(id = 3, text = " og noe mer tekst")
            }
            itemList(id = 4) {
                item(id = 41) { literal(id = 411, text = "en punktliste") }
            }
        }
        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `an edited block where type is changed from paragraph to title2 can merge`() {
        val next = letter(
            paragraph(
                1,
                literal(1, "Noe tekst "),
                variable(2, "med en oppdatert variabel"),
                literal(3, " og noe mer tekst"),
                literal(4, " pluss noe ny tekst"),
            )
        )
        val edited = editedLetter {
            title2(id = 1) {
                literal(id = 1, text = "Noe tekst ", editedText = "Noe redigert tekst ")
                variable(id = 2, text = "med en variabel")
                literal(id = 3, text = " og noe mer tekst")
            }
        }
        val expected = editedLetter {
            title2(id = 1) {
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
        val next = letter(title2(1, literal(1, "En tittel "), variable(2, "for deg "), literal(3, "og meg!")))
        val edited = editedLetter {
            paragraph {
                literal(text = "", editedText = "Noe ny tekst")
            }
            title2(id = 1) {
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
            title2(1, literal(1, "En tittel "), variable(2, "for deg "), literal(3, "og meg!")),
            title2(3, literal(1, "En ny tittel ")),
        )
        val edited = editedLetter {
            title2(id = 1) {
                literal(id = 1, text = "En tittel ", editedText = "Her har jeg redigert ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
            paragraph {
                literal(text = "Noe mer ny tekst")
            }
        }
        val expected = editedLetter {
            title2(id = 1) {
                literal(id = 1, text = "En tittel ", editedText = "Her har jeg redigert ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
            paragraph {
                literal(text = "Noe mer ny tekst")
            }
            title2(id = 3) {
                literal(id = 1, text = "En ny tittel ")
            }
        }

        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `keeps new edited blocks in positions relative to original blocks`() {
        val next = letter(
            title2(1, literal(1, "En tittel "), variable(2, "for deg "), literal(3, "og meg!")),
            title2(3, literal(1, "En ny tittel ")),
        )
        val edited = editedLetter {
            paragraph {
                literal(text = "", editedText = "første teksten")
            }
            title2(id = 1) {
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
            title2(id = 3) {
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
            title2(1, literal(1, "En tittel "), variable(2, "for deg "), literal(3, "og meg!")),
            title2(3, literal(1, "Dette er en ny block før id:4 ")),
            title2(4, literal(1, "En tittel ")),
        )
        val edited = editedLetter {
            title2(id = 1) {
                literal(id = 1, text = "En tittel ", editedText = "Her har jeg redigert ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
            paragraph {
                literal(text = "Noe ny redigert tekst")
            }
            title2(id = 4) {
                literal(id = 1, text = "En tittel ")
            }
        }
        val expected = editedLetter {
            title2(id = 1) {
                literal(id = 1, text = "En tittel ", editedText = "Her har jeg redigert ")
                variable(id = 2, text = "for deg ")
                literal(id = 3, text = "og meg!")
            }
            paragraph {
                literal(text = "Noe ny redigert tekst")
            }
            title2(id = 3) {
                literal(id = 1, text = "Dette er en ny block før id:4 ")
            }
            title2(id = 4) {
                literal(id = 1, text = "En tittel ")
            }
        }

        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `unedited content not present in next render are removed`() {
        val next = letter(
            paragraph(
                1,
                literal(11, "Første"),
                variable(12, "en variabel"),
                literal(13, "Andre"),
                variable(14, "andre variabel"),
                literal(15, "Tredje"),
            ),
            itemList(16, item(160, literal(161, "punkt 1"), literal(162, "punkt 2"), literal(163, "punkt 3"))),
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
            }
            itemList(id = 16) {
                item(id = 160) {
                    literal(id = 161, text = "punkt 1")
                    literal(id = 162, text = "punkt 2")
                    literal(id = 163, text = "punkt 3")
                }
            }
        }

        assertEquals(next.toEdit(), edited.updateEditedLetter(next))
    }

    @Test
    fun `content edited to empty string is kept after render`() {
        val next = letter(
            paragraph(1, literal(11, "lit1"), variable(12, "var1"), literal(13, "rediger til tom streng")),
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
            paragraph(1, literal(11, "lit1")),
            paragraph(2, literal(21, "lit2")),
            itemList(3, item(31, literal(311, "punkt"))),
        )
        val edited = editedLetter(deleted = setOf(2, 3)) {
            paragraph(id = 1) {
                literal(id = 11, text = "lit1")
            }
        }

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `deleted content are not included in updated letter`() {
        val next = letter(
            paragraph(1, literal(11, "1lit1"), literal(12, "1lit2")),
            title2(2, literal(21, "2lit1"), literal(22, "2lit2")),
            title3(3, literal(31, "3lit1"), literal(32, "3lit2")),
            title4(4, literal(41, "4lit1"), literal(42, "4lit2")),
        )
        val edited = editedLetter {
            paragraph(id = 1, deletedContent = setOf(12)) {
                literal(id = 11, text = "1lit1")
            }
            title2(id = 2, deletedContent = setOf(22)) {
                literal(id = 21, text = "2lit1")
            }
            title3(id = 3, deletedContent = setOf(32)) {
                literal(id = 31, text = "3lit1")
            }
            title4(id = 4, deletedContent = setOf(42)) {
                literal(id = 41, text = "4lit1")
            }
        }

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `deleted items are not included in updated letter`() {
        val next = letter(
            itemList(11, item(111, literal(1111, "item 1")), item(112, literal(1121, "item 2"))),
            numberedList(12, item(121, literal(1211, "item 1")), item(122, literal(1221, "item 2"))),
        )
        val edited = editedLetter {
            itemList(id = 11, deletedItems = setOf(111)) {
                item(id = 112) {
                    literal(id = 1121, text = "item 2")
                }
            }
            numberedList(id = 12, deletedItems = setOf(122)) {
                item(id = 121) {
                    literal(id = 1211, text = "item 1")
                }
            }
        }
        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `multiple new rendered items are included`() {
        val next = letter(
            paragraph(1, literal(11, "første"), literal(12, "andre"), literal(13, "tredje"), literal(14, "fjerde"))
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
            paragraph(
                1,
                literal(11, "en literal"),
                variable(12, "oppdatert variabel"),
                variable(1311, "oppdatert variabel 2"),
                variable(14111, "oppdatert variabel 3"),
                variable(14211, "oppdatert variabel 4"),
            )
        )
        val edited = editedLetter(deleted = setOf(1)) {
            paragraph {
                literal(id = 11, text = "en literal")
                variable(id = 12, text = "en variabel")
            }
            itemList(id = 13, parentId = 1) {
                item(id = 131) {
                    variable(id = 1311, text = "variabel 2")
                }
            }
            table(id = 14, parentId = 1) {
                header(id = 141) {
                    colSpec(id = 1411) {
                        variable(id = 14111, text = "variabel 3")
                    }
                }
                row(id = 142) {
                    cell(id = 1421) {
                        variable(id = 14211, text = "variabel 4")
                    }
                }
            }
            title2 {
                variable(id = 1311, text = "variabel 2")
            }
            title3 {
                variable(id = 14211, text = "variabel 4")
            }
        }
        val expected = editedLetter(deleted = setOf(1)) {
            paragraph {
                literal(id = 11, text = "en literal")
                variable(id = 12, text = "oppdatert variabel")
            }
            itemList(id = 13, parentId = 1) {
                item(id = 131) {
                    variable(id = 1311, text = "oppdatert variabel 2")
                }
            }
            table(id = 14, parentId = 1) {
                header(id = 141) {
                    colSpec(id = 1411) {
                        variable(id = 14111, text = "oppdatert variabel 3")
                    }
                }
                row(id = 142) {
                    cell(id = 1421) {
                        variable(id = 14211, text = "oppdatert variabel 4")
                    }
                }
            }
            title2 {
                variable(id = 1311, text = "oppdatert variabel 2")
            }
            title3 {
                variable(id = 14211, text = "oppdatert variabel 4")
            }
        }
        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `variables moved to a new block no longer present in rendered cannot be updated and is converted to literal`() {
        val next = letter(paragraph(1, literal(11, "en literal")))
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
            paragraph(1, literal(11, "en literal")),
            itemList(12, item(121, variable(1211, "oppdatert v1"))),
            paragraph(2, literal(21, "to literal")),
        )
        val edited = editedLetter(deleted = setOf(12)) {
            paragraph(id = 1) {
                literal(id = 11, text = "en literal")
            }
            paragraph(id = 2) {
                variable(id = 1211, text = "variable 1", parentId = 121)
                literal(id = 21, text = "to literal")
            }
        }
        val expected = editedLetter(deleted = setOf(12)) {
            paragraph(id = 1) {
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
    fun `variables moved from itemList to preceding block will be updated`() {
        val next = letter(
            paragraph(1, literal(11, "en literal")),
            itemList(12, item(121, variable(1211, "oppdatert v1"))),
        )
        val edited = editedLetter(deleted = setOf(12)) {
            paragraph(id = 1) {
                literal(id = 11, text = "en literal")
                variable(id = 1211, text = "variable 1", parentId = 121)
            }
        }
        val expected = editedLetter(deleted = setOf(12)) {
            paragraph(id = 1) {
                literal(id = 11, text = "en literal")
                variable(id = 1211, text = "oppdatert v1", parentId = 121)
            }
        }
        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `variables moved to another parent block no longer present will be updated if variable is present in rendered`() {
        val next = letter(paragraph(1, literal(11, "en literal"), variable(12, "oppdatert v1")))
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
            paragraph(1, literal(11, "lit1"), variable(12, "var2")),
            itemList(13, item(131, literal(1311, "punkt1"))),
            paragraph(2, literal(14, "lit2")),
        )
        val edited = editedLetter(deleted = setOf(1, 2)) {
            itemList(id = 13) {
                item {
                    literal(id = 11, text = "lit1", parentId = 1)
                    variable(id = 12, text = "var2", parentId = 1)
                }
                item(id = 131) {
                    literal(id = 1311, text = "punkt1")
                    literal(id = 14, text = "lit2", parentId = 2)
                }
            }
        }
        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `content moved to another block is kept`() {
        val next = letter(
            paragraph(1, literal(11, "lit1"), variable(12, "var2"), literal(15, "lit3")),
            itemList(13, item(131, literal(1311, "punkt1"))),
            paragraph(2, literal(14, "lit2")),
        )
        val edited = editedLetter {
            paragraph {
                literal(id = 11, text = "lit1", parentId = 1)
                variable(id = 12, text = "var2", parentId = 1)
            }
            paragraph(id = 1, deletedContent = setOf(11, 12)) {
                literal(id = 15, text = "lit3")
            }
            itemList(id = 13) {
                item(id = 131) {
                    literal(id = 1311, text = "punkt1")
                }
            }
            paragraph(id = 2) {
                literal(id = 14, text = "lit2")
            }
        }
        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `BrevdataEllerFritekst som begynner som fritekst og endres til variabel senere fordi saken oppdateres`() {
        val editedLetter1 = editedLetter {
            paragraph(id = 1) {
                literal(id = 11, text = "fast tekst")
                literal(id = 12, text = "fritekst", editedText = "fylt inn fritekst", editBehaviour = EditV2.EditBehaviour.FRITEKST)
            }
        }

        val brevdataVariabelHarEnVerdi = letter(
            paragraph(1, literal(11, "fast tekst oppdatert"), variable(12, "verdi fra brevdata"))
        )

        val expected = editedLetter {
            paragraph(id = 1) {
                literal(id = 11, text = "fast tekst oppdatert")
                literal(id = 12, text = "fritekst", editedText = "fylt inn fritekst", editBehaviour = EditV2.EditBehaviour.FRITEKST)
            }
        }

        assertEquals(expected, editedLetter1.updateEditedLetter(brevdataVariabelHarEnVerdi))
    }

    @Test
    fun `FRITEKST editBehaviour is preserved when fritekst is edited`() {
        val next = letter(
            paragraph(1, literal(11, "fast tekst "), literal(12, "fyll inn", editBehaviour = EditBehaviour.FRITEKST))
        )
        val edited = editedLetter {
            paragraph(id = 1) {
                literal(id = 11, text = "fast tekst ")
                literal(id = 12, text = "fyll inn", editedText = "utfylt tekst", editBehaviour = EditV2.EditBehaviour.FRITEKST)
            }
        }

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `list type switch is preserved when list content is not otherwise edited`() {
        val next = letter(
            itemList(10, item(100, literal(101, "punkt 1")), item(200, literal(201, "punkt 2"))),
        )
        val edited = editedLetter {
            numberedList(id = 10, originalType = EditV2.Block.Type.ITEM_LIST) {
                item(id = 100) { literal(id = 101, text = "punkt 1") }
                item(id = 200) { literal(id = 201, text = "punkt 2") }
            }
        }

        assertEquals(edited, edited.updateEditedLetter(next))
    }

    @Test
    fun `unswitched list type follows rendered version`() {
        val next = letter(numberedList(10, item(100, literal(101, "punkt 1"))))
        val edited = editedLetter {
            itemList(id = 10) {
                item(id = 100) { literal(id = 101, text = "punkt 1") }
            }
        }

        assertEquals(next.toEdit(), edited.updateEditedLetter(next))
    }

    @Test
    fun `list type switch is preserved when the list content is also edited`() {
        val next = letter(
            itemList(10, item(100, literal(101, "punkt 1 fra mal")), item(200, literal(201, "nytt punkt"))),
        )
        val edited = editedLetter {
            numberedList(id = 10, originalType = EditV2.Block.Type.ITEM_LIST) {
                item(id = 100) { literal(id = 101, text = "punkt 1 fra mal", editedText = "punkt 1 redigert") }
            }
        }
        val expected = editedLetter {
            numberedList(id = 10, originalType = EditV2.Block.Type.ITEM_LIST) {
                item(id = 100) { literal(id = 101, text = "punkt 1 fra mal", editedText = "punkt 1 redigert") }
                item(id = 200) { literal(id = 201, text = "nytt punkt") }
            }
        }

        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `paragraph split into paragraph and item list by saksbehandler survives merge`() {
        val next = letter(
            paragraph(
                1,
                literal(11, "Innledning. "),
                literal(12, "punkt a"),
                variable(13, "oppdatert verdi"),
                literal(14, "Avslutning."),
            ),
            paragraph(2, literal(21, "Neste avsnitt")),
        )
        val edited = editedLetter {
            paragraph(id = 1, deletedContent = setOf(12, 13, 14)) {
                literal(id = 11, text = "Innledning. ")
            }
            itemList(id = null, parentId = 1) {
                item {
                    literal(id = 12, text = "punkt a", parentId = 1)
                }
                item {
                    variable(id = 13, text = "gammel verdi", parentId = 1)
                }
            }
            paragraph(id = null, parentId = 1) {
                literal(id = 14, text = "Avslutning.", parentId = 1)
            }
            paragraph(id = 2) {
                literal(id = 21, text = "Neste avsnitt")
            }
        }
        val expected = editedLetter {
            paragraph(id = 1, deletedContent = setOf(12, 13, 14)) {
                literal(id = 11, text = "Innledning. ")
            }
            itemList(id = null, parentId = 1) {
                item {
                    literal(id = 12, text = "punkt a", parentId = 1)
                }
                item {
                    variable(id = 13, text = "oppdatert verdi", parentId = 1)
                }
            }
            paragraph(id = null, parentId = 1) {
                literal(id = 14, text = "Avslutning.", parentId = 1)
            }
            paragraph(id = 2) {
                literal(id = 21, text = "Neste avsnitt")
            }
        }

        val result = edited.updateEditedLetter(next)
        assertEquals(expected, result)
        assertEquals(expected, result.updateEditedLetter(next))
    }

    @Test
    fun `block whose type changed to another block category keeps edited content`() {
        val next = letter(
            itemList(4, item(41, literal(411, "punkt"), variable(412, "oppdatert"))),
        )
        val edited = editedLetter {
            paragraph(id = 4, originalType = EditV2.Block.Type.ITEM_LIST) {
                literal(id = 411, text = "punkt", parentId = 41)
                variable(id = 412, text = "gammel", parentId = 41)
            }
        }
        val expected = editedLetter {
            paragraph(id = 4, originalType = EditV2.Block.Type.ITEM_LIST) {
                literal(id = 411, text = "punkt", parentId = 41)
                variable(id = 412, text = "oppdatert", parentId = 41)
            }
        }

        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `edited list and table missing from template are kept and marked missingFromTemplate`() {
        val next = letter(paragraph(1, literal(11, "tekst")))
        val edited = editedLetter {
            paragraph(id = 1) { literal(id = 11, text = "tekst") }
            itemList(id = 2) {
                item(id = 21) { literal(id = 211, text = "punkt", editedText = "redigert punkt") }
            }
            table(id = 3) {
                header(id = 31) { colSpec(id = 311) { literal(id = 3111, text = "kolonne") } }
                row(id = 32) { cell(id = 321) { literal(id = 3211, text = "celle", editedText = "redigert celle") } }
            }
            itemList(id = 4) {
                item(id = 41) { literal(id = 411, text = "uendret punkt") }
            }
        }
        val expected = editedLetter {
            paragraph(id = 1) { literal(id = 11, text = "tekst") }
            itemList(id = 2, missingFromTemplate = true) {
                item(id = 21) { literal(id = 211, text = "punkt", editedText = "redigert punkt") }
            }
            table(id = 3, missingFromTemplate = true) {
                header(id = 31) { colSpec(id = 311) { literal(id = 3111, text = "kolonne") } }
                row(id = 32) { cell(id = 321) { literal(id = 3211, text = "celle", editedText = "redigert celle") } }
            }
        }

        assertEquals(expected, edited.updateEditedLetter(next))
    }

    @Test
    fun `deleted cell text does not reappear after updateEditedLetter`() {
        val rendered = letter(
            table(
                11,
                header(112, columnSpec(1121, literal(11211, "kolonne 1"))),
                row(111, cell(1111, literal(11111, "tekst1"), literal(11112, "tekst2"))),
            ),
        )
        val edited = editedLetter {
            table(id = 11) {
                header(id = 112) {
                    colSpec(id = 1121) { literal(id = 11211, text = "kolonne 1") }
                }
                row(id = 111) {
                    cell(id = 1111, deletedContent = setOf(11111)) {
                        literal(id = 11112, text = "tekst2")
                    }
                }
            }
        }
        assertEquals(edited, edited.updateEditedLetter(rendered))
    }

    @Test
    fun `deleted column does not reappear after updateEditedLetter`() {
        val rendered = letter(
            table(
                11,
                header(
                    112,
                    columnSpec(1121, literal(11211, "Kolonne 1")),
                    columnSpec(1122, literal(11221, "Kolonne 2")),
                ),
                row(
                    111,
                    cell(1111, literal(11111, "rad1kol1")),
                    cell(1112, literal(11121, "rad1kol2")),
                ),
            ),
        )
        val edited = editedLetter {
            table(id = 11) {
                header(id = 112, deletedColSpecs = setOf(1122)) {
                    colSpec(id = 1121) { literal(id = 11211, text = "Kolonne 1") }
                }
                row(id = 111, deletedCells = setOf(1112)) {
                    cell(id = 1111) { literal(id = 11111, text = "rad1kol1") }
                }
            }
        }
        assertEquals(edited, edited.updateEditedLetter(rendered))
    }

    @Test
    fun `deleted table row does not reappear and new rendered row is added`() {
        val rendered = letter(
            table(
                11,
                header(112, columnSpec(1121, literal(11211, "Kolonne 1"))),
                row(111, cell(1111, literal(11111, "rad1"))),
                row(113, cell(1131, literal(11311, "rad2"))),
                row(114, cell(1141, literal(11411, "rad3"))),
            ),
        )
        val edited = editedLetter {
            table(id = 11, deletedRows = setOf(113)) {
                header(id = 112) {
                    colSpec(id = 1121) { literal(id = 11211, text = "Kolonne 1") }
                }
                row(id = 111) {
                    cell(id = 1111) { literal(id = 11111, text = "rad1", editedText = "rad1 redigert") }
                }
            }
        }
        val expected = editedLetter {
            table(id = 11, deletedRows = setOf(113)) {
                header(id = 112) {
                    colSpec(id = 1121) { literal(id = 11211, text = "Kolonne 1") }
                }
                row(id = 111) {
                    cell(id = 1111) { literal(id = 11111, text = "rad1", editedText = "rad1 redigert") }
                }
                row(id = 114) {
                    cell(id = 1141) { literal(id = 11411, text = "rad3") }
                }
            }
        }
        assertEquals(expected, edited.updateEditedLetter(rendered))
    }

    @Test
    fun `deleting one occurrence of a duplicated variable is not resurrected by a later merge`() {
        val rendered = letter(
            title2(1, variable(2, "1000 kroner"), literal(3, " og "), variable(2, "1000 kroner"))
        )
        val edited = editedLetter {
            title2(id = 1, deletedContent = listOf(2)) {
                variable(id = 2, text = "1000 kroner")
                literal(id = 3, text = " og ")
            }
        }

        assertEquals(edited, edited.updateEditedLetter(rendered))
    }

    @Test
    fun `deleting the first occurrence of a duplicated variable preserves the remaining content order`() {
        val rendered = letter(
            title2(1, variable(2, "1000 kroner"), literal(3, " og "), variable(2, "1000 kroner"))
        )
        val edited = editedLetter {
            title2(id = 1, deletedContent = listOf(2)) {
                literal(id = 3, text = " og ")
                variable(id = 2, text = "1000 kroner")
            }
        }

        assertEquals(edited, edited.updateEditedLetter(rendered))
    }

    @Test
    fun `deletion marker survives when the kept duplicate disappears from the template`() {
        val edited = editedLetter {
            title2(id = 1, deletedContent = listOf(2)) {
                literal(id = 3, text = " og ")
                variable(id = 2, text = "1000 kroner")
            }
        }
        val rendered = letter(title2(1, variable(2, "1000 kroner"), literal(3, " og ")))
        val expected = editedLetter {
            title2(id = 1, deletedContent = listOf(2)) {
                literal(id = 3, text = " og ")
            }
        }

        val firstUpdate = edited.updateEditedLetter(rendered)
        assertEquals(expected, firstUpdate)
        assertEquals(expected, firstUpdate.updateEditedLetter(rendered))
    }

    @Test
    fun `deleting both occurrences of a duplicated variable removes it entirely`() {
        val rendered = letter(
            title2(1, variable(2, "1000 kroner"), literal(3, " og "), variable(2, "1000 kroner"))
        )
        val edited = editedLetter {
            title2(id = 1, deletedContent = listOf(2, 2)) {
                literal(id = 3, text = " og ")
            }
        }

        assertEquals(edited, edited.updateEditedLetter(rendered))
    }

    @Test
    fun `a duplicated variable removed entirely from the template is forgotten instead of piling up deletions`() {
        val edited = editedLetter {
            title2(id = 1, deletedContent = listOf(2)) {
                literal(id = 3, text = " og ")
            }
        }
        val rendered = letter(title2(1, literal(3, " og ")))

        val result = edited.updateEditedLetter(rendered)
        assertEquals(emptyList<Int>(), (result.blocks.single() as EditV2.Block.Title2).deletedContent)
    }
}
