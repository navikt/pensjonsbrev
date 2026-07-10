package no.nav.brev.brevbaker.markup

import no.nav.brev.brevbaker.markup.ElementTags.FRITEKST
import no.nav.brev.brevbaker.markup.dsl.attachment
import no.nav.brev.brevbaker.markup.dsl.column
import no.nav.brev.brevbaker.markup.dsl.letterMarkup
import no.nav.brev.brevbaker.markup.dsl.lettermarkupExtended
import no.nav.brev.brevbaker.markup.dsl.prompt
import no.nav.brev.brevbaker.markup.dsl.title1
import no.nav.brev.brevbaker.markup.dsl.title2
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

    private fun fullLetter(): LetterMarkupV2 = lettermarkupExtended {
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
                variable("uføretrygd")
                text(".")
                newLine()
            }
            itemList {
                item { text("Punkt 1") }
                item { text("Punkt 2") }
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
        assertTrue(types.containsAll(listOf(
            Block.Type.TITLE2,
            Block.Type.PARAGRAPH,
            Block.Type.ITEM_LIST,
            Block.Type.NUMBERED_LIST,
            Block.Type.TABLE,
            Block.Type.FORM_TEXT,
            Block.Type.FORM_CHOICE,
        )))

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
        val json = MarkupJson.encodeToString(LetterMarkupV2.serializer(), letter)
        val decoded = MarkupJson.decodeFromString(LetterMarkupV2.serializer(), json)
        assertEquals(letter, decoded)
    }

    @Test
    fun `base letterMarkup builds without variables`() {
        val text = listOf("a", "b")
        val letter = letterMarkup {
            saksinformasjon(
                gjelderNavn = "Ola Nordmann",
                gjelderFoedselsnummer = "12345678901",
                saksnummer = "9876543",
                dokumentDato = LocalDate.of(2026, 7, 9),
            )
            title1("Tittel")
            outline {
                title2("")
                text.forEach {
                    paragraph(it)
                }
                title2 {
                    text("", FontType.BOLD)
                    newLine()
                }
                paragraph {
                    text("Kun litteral tekst.")
                    if(1==2) {
                        text("the end is nigh!")
                    } else {
                        text("phew")
                    }
                }

                paragraph("Literally just the usual literal.")
                table {
                    header {
                        column(alignment = ColumnAlignment.LEFT, span = 2) {
                            text("Bla!")
                        }
                        column("Bla!", alignment = ColumnAlignment.LEFT, span = 2)
                        column("Bla!")
                        column("Bla!")
                    }

                    row {
                        cell {
                            text("mø")
                        }
                        cell {
                            text("mø")
                        }
                        cell {
                            text("mø")
                        }
                        cell("mø")
                    }
                }
            }
            signatur(
                hilsenTekst = "Med vennlig hilsen",
                navAvsenderEnhet = "NAV",
                saksbehandlerNavn = "Sak S.Behandler",
            )
            attachment(false) {
                title1 ("Vedlegg er niz")
                outline {
                    table{
                        header { column("mø") }
                        row {
                            cell("mø")
                        }
                    }
                }
            }
        }

        val paragraph = letter.blocks.filterIsInstance<Block.Paragraph>().single()
        assertEquals(listOf(Text.Type.LITERAL), paragraph.content.map { it.type })
    }

    @Test
    fun `extended DSL supports shorthand string methods with font type on content`() {
        val letter = lettermarkupExtended {
            title1("Vedtak")
            title1 {
                text("something")
                variable("something")
            }
            saksinformasjon(
                gjelderNavn = "Ola Nordmann",
                gjelderFoedselsnummer = "12345678901",
                saksnummer = "9876543",
                dokumentDato = LocalDate.of(2026, 7, 9),
            )
            outline {
                title2("Innledning")
                paragraph("Ingress", fontType = FontType.BOLD)
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
                        column("Kolonne 2")
                    }
                    row {
                        cell("Celle", fontType = FontType.BOLD)
                        cell {
                            text("bla")
                        }
                    }
                }
                formText(Size.SHORT, "Ledetekst", fontType = FontType.BOLD)
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

        assertEquals("Vedtak", (letter.title1.single() as Text.Literal).text)

        val title2 = letter.blocks.filterIsInstance<Block.Title2>().single()
        assertEquals(FontType.PLAIN, (title2.content.single() as Text.Literal).fontType)

        val paragraph = letter.blocks.filterIsInstance<Block.Paragraph>().single()
        assertEquals(FontType.BOLD, (paragraph.content.single() as Text.Literal).fontType)

        val item = letter.blocks.filterIsInstance<Block.ItemList>().single().items.first()
        assertEquals(FontType.BOLD, (item.content.single() as Text.Literal).fontType)

        val cell = letter.blocks.filterIsInstance<Block.Table>().single().rows.single().cells.first()
        assertEquals(FontType.BOLD, (cell.text.single() as Text.Literal).fontType)
    }
}
