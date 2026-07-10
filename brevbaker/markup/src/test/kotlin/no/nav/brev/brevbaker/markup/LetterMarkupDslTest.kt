package no.nav.brev.brevbaker.markup

import no.nav.brev.brevbaker.markup.ElementTags.FRITEKST
import no.nav.brev.brevbaker.markup.dsl.column
import no.nav.brev.brevbaker.markup.dsl.*
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LetterMarkupDslTest {

    private fun fullLetter(): LetterMarkup = letterMarkupExtended {
        title1 { text("Vedtak om uføretrygd") }
        saksinformasjon(
            gjelderNavn = "Ola Nordmann",
            gjelderFoedselsnummer = "12345678901",
            saksnummer = "9876543",
            dokumentDato = LocalDate.of(2026, 7, 9),
            annenMottakerNavn = "Kari Nordmann",
        )
        outline {
            title2 { text("Innledning") }
            paragraph {
                text("Du får ")
                newLine()
                variable("uføretrygd")
                text(".")
            }
            itemList {
                item { text("Punkt 1") }
                item { text("Punkt 2") }
                item("")
            }
            numberedList {
                item {
                    text("Steg 1")
                }
            }
            table {
                header {
                    column(ColumnAlignment.LEFT) {
                        text("Kolonne ")
                        variable("A")
                    }
                    column(ColumnAlignment.RIGHT, span = 2) { text("Kolonne B") }
                }
                row {
                    cell { text("A1") }
                    cell { text("B1") }
                }
            }
            formText(Size.LONG) { text("Skriv her") }
            formChoice {
                prompt { text("Velg") }
                choice { text("Ja") }
                choice { text("Nei") }
            }
        }
        signatur(
            hilsenTekst = "Med vennlig hilsen",
            navAvsenderEnhet = "NAV Familie- og pensjonsytelser",
            saksbehandlerNavn = "Sak S.Behandler",
            attesterendeSaksbehandlerNavn = "Att Esterer",
        )
    }

    @Test
    fun `dsl builds a valid letter with all block and text types`() {
        val letter = fullLetter()

        assertEquals(2, letter.version)
        assertEquals("Ola Nordmann", letter.saksinformasjon.gjelderNavn)
        assertEquals("12345678901", letter.saksinformasjon.gjelderFoedselsnummer.value)
        assertEquals("9876543", letter.saksinformasjon.saksnummer.saksnummer)

        val types = letter.blocks.map { it.type }
        assertTrue(
            types.containsAll(
                listOf(
                    Block.Type.TITLE2,
                    Block.Type.PARAGRAPH,
                    Block.Type.ITEM_LIST,
                    Block.Type.NUMBERED_LIST,
                    Block.Type.TABLE,
                    Block.Type.FORM_TEXT,
                    Block.Type.FORM_CHOICE,
                )
            )
        )

        val paragraph = letter.blocks.filterIsInstance<Block.Paragraph>().single()
        val textTypes = paragraph.content.map { it.type }
        assertTrue(textTypes.containsAll(listOf(Text.Type.LITERAL, Text.Type.VARIABLE, Text.Type.NEW_LINE)))
    }

    @Test
    fun `dsl assigns unique ids`() {
        val letter = fullLetter()
        val ids = mutableListOf<Int>()
        letter.title1.forEach { ids.add(it.id) }
        letter.blocks.forEach { block ->
            ids.add(block.id)
            when (block) {
                is Block.Title2 -> block.content.forEach { ids.add(it.id) }
                is Block.Paragraph -> block.content.forEach { ids.add(it.id) }
                else -> {}
            }
        }
        assertEquals(ids.size, ids.toSet().size, "ids should be unique")
    }

    @Test
    fun `letter round-trips through json`() {
        val letter = fullLetter()
        val json = letter.toJson()
        val decoded = decodeLetterMarkup(json)
        assertEquals(letter, decoded)
    }

    @Test
    fun `base letterMarkup builds without variables`() {
        val letter = letterMarkup {
            saksinformasjon(
                gjelderNavn = "Ola Nordmann",
                gjelderFoedselsnummer = "12345678901",
                saksnummer = "9876543",
                dokumentDato = LocalDate.of(2026, 7, 9),
            )
            title1("Tittel")
            title1 { text("Tittel fra builder") }
            outline {
                title2("Title 2")
                title2 { text("Title 2 builder") }
                title3("Title 3")
                title3 { text("Title 3 builder") }
                title4("Title 4")
                title4 { text("Title 4 builder") }

                paragraph("Paragraph literal", fontType = FontType.BOLD)
                paragraph {
                    text("Kun litteral tekst.")
                    newLine()
                    text("Ny linje.")
                }

                itemList {
                    item("Punkt 1", fontType = FontType.BOLD)
                    item { text("Punkt 2 fra builder") }
                }
                numberedList {
                    item("Steg 1")
                    item { text("Steg 2 fra builder") }
                }

                table {
                    header {
                        column("Kolonne 1")
                        column(alignment = ColumnAlignment.RIGHT) { text("Kolonne 2 builder") }
                    }

                    row {
                        cell("A1", fontType = FontType.BOLD)
                        cell { text("B1") }
                    }
                }

                formText("Ledetekst", Size.SHORT, fontType = FontType.BOLD)
                formText(Size.LONG) { text("Ledetekst fra builder") }
                formChoice(vspace = false) {
                    prompt("Velg")
                    choice("Ja", fontType = FontType.BOLD)
                    choice { text("Nei fra builder") }
                }
            }
            signatur(
                hilsenTekst = "Med vennlig hilsen",
                navAvsenderEnhet = "NAV",
                saksbehandlerNavn = "Sak S.Behandler",
            )
        }

        val types = letter.blocks.map { it.type }
        assertTrue(types.containsAll(Block.Type.entries))
    }

    @Test
    fun `extended DSL supports shorthand string methods with font type on content`() {
        val letter = letterMarkupExtended {
            title1("Vedtak")
            title1 {
                text("Tittel fra builder ")
                variable("x")
            }
            saksinformasjon(
                gjelderNavn = "Ola Nordmann",
                gjelderFoedselsnummer = "12345678901",
                saksnummer = "9876543",
                dokumentDato = LocalDate.of(2026, 7, 9),
            )
            outline {
                title2("Innledning")
                title2 {
                    text("Innledning ")
                    variable("x")
                }
                title3("Mellomtittel")
                formText("TEXT", Size.SHORT)
                title3 {
                    text("Mellomtittel ")
                    variable("x")
                }
                title4("Detaljer")
                title4 {
                    text("Detaljer ")
                    variable("x")
                }
                paragraph("Ingress", fontType = FontType.BOLD)
                paragraph {
                    text("Du får ")
                    variable("1000 Kr", tags = setOf(ElementTags.REDIGERBAR_DATA))
                    newLine()
                }
                itemList {
                    item("Punkt", fontType = FontType.BOLD)
                    item {
                        text("Du får ")
                        variable("1000 Kr")
                    }
                }
                table {
                    header {
                        column("Kolonne")
                        column {
                            text("Kolonne ")
                            variable("2")
                        }
                    }
                    row {
                        cell("Celle", fontType = FontType.BOLD)
                        cell {
                            text("bla")
                            variable("2")
                        }
                    }
                }
                formText("Ledetekst", Size.SHORT, fontType = FontType.BOLD)
                formText(Size.LONG) {
                    text("Skriv ")
                    variable("her")
                }
                formChoice {
                    prompt("Velg")
                    prompt {
                        text("Du får svar innen ")
                        variable("x")
                        text("uker.")
                    }
                    choice("Ja", fontType = FontType.BOLD)
                    choice("Nei")
                    choice {
                        text("kanskje")
                        variable("eller?", tags = setOf(FRITEKST))
                    }
                }
            }
            signatur(
                hilsenTekst = "Hilsen",
                navAvsenderEnhet = "NAV",
            )
        }

        val titleTexts = letter.title1.filterIsInstance<Text.Literal>().map { it.text }
        assertTrue(titleTexts.contains("Tittel fra builder "))
        letter.blocks.filterIsInstance<Block.Title2>().firstOrNull()?.let {
            it.content.map { el ->
                when(el) {
                    is Text.Literal -> TODO()
                    is Text.NewLine -> TODO()
                    is Text.Variable -> TODO()
                }
            }
        }

        val title2 = letter.blocks.filterIsInstance<Block.Title2>().first()
        assertEquals(FontType.PLAIN, title2.content.filterIsInstance<Text.Literal>().first().fontType)

        val paragraph = letter.blocks.filterIsInstance<Block.Paragraph>().first()
        assertEquals(FontType.BOLD, (paragraph.content.single() as Text.Literal).fontType)

        val item = letter.blocks.filterIsInstance<Block.ItemList>().single().items.first()
        assertEquals(FontType.BOLD, (item.content.single() as Text.Literal).fontType)

        val cell = letter.blocks.filterIsInstance<Block.Table>().single().rows.single().cells.first()
        assertEquals(FontType.BOLD, (cell.text.single() as Text.Literal).fontType)
    }
}
