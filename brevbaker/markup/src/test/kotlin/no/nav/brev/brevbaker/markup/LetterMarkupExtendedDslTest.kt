package no.nav.brev.brevbaker.markup

import kotlinx.serialization.json.Json
import no.nav.brev.brevbaker.markup.outline.EditBehaviour
import no.nav.brev.brevbaker.markup.outline.EditBehaviour.FRITEKST
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

class LetterMarkupExtendedDslTest {

    private fun fullLetter(): LetterMarkup {
        var next = 0
        fun id() = next++
        return letterMarkupExtended(
            saksinformasjon = saksinformasjon(
                gjelderNavn = "Ola Nordmann",
                gjelderPersonidentifikator = "12345678901",
                saksnummer = "9876543",
                dokumentDato = LocalDate.of(2026, 7, 9),
                annenMottakerNavn = "Kari Nordmann",
            ),
            signatur = signatur(
                hilsenTekst = "Med vennlig hilsen",
                navAvsenderEnhet = "NAV Familie- og pensjonsytelser",
                saksbehandlerNavn = "Sak S.Behandler",
                attesterendeSaksbehandlerNavn = "Att Esterer",
            ),
        ) {
            title1 { text(id(), "Vedtak om uføretrygd") }
            outline {
                title2(id()) { text(id(), "Innledning") }
                paragraph(id()) {
                    text(id(), "Du får ")
                    newLine(id())
                    variable(id(), "uføretrygd")
                    text(id(), ".")
                }
                itemList(id()) {
                    item(id()) { text(id(), "Punkt 1") }
                    item(id()) { text(id(), "Punkt 2") }
                    item(id()) { text(id(), "") }
                }
                numberedList(id()) {
                    item(id()) {
                        text(id(), "Steg 1")
                    }
                }
                table(id()) {
                    header(id()) {
                        column(id(), ColumnAlignment.LEFT) {
                            text(id(), "Kolonne ")
                            variable(id(), "A")
                        }
                        column(id(), ColumnAlignment.RIGHT, span = 2) { text(id(), "Kolonne B") }
                    }
                    row(id()) {
                        cell(id()) { text(id(), "A1") }
                        cell(id()) { text(id(), "B1") }
                    }
                }
                formText(id(), Size.LONG) { text(id(), "Skriv her") }
                formChoice(id()) {
                    prompt { text(id(), "Velg") }
                    choice(id()) { text(id(), "Ja") }
                    choice(id()) { text(id(), "Nei") }
                }
            }
        }
    }

    @Test
    fun `dsl builds a valid letter with all block and text types`() {
        val letter = fullLetter()

        assertEquals(2, letter.version)
        assertEquals("Ola Nordmann", letter.saksinformasjon.gjelderNavn)
        assertEquals("12345678901", letter.saksinformasjon.gjelderPersonidentifikator.value)
        assertEquals("9876543", letter.saksinformasjon.saksnummer.value)

        val blockClasses = letter.blocks.map { it::class }
        assertTrue(
            blockClasses.containsAll(
                listOf(
                    Block.Title2::class,
                    Block.Paragraph::class,
                    Block.ItemList::class,
                    Block.NumberedList::class,
                    Block.Table::class,
                    Block.FormText::class,
                    Block.FormChoice::class,
                )
            )
        )

        val paragraph = letter.blocks.filterIsInstance<Block.Paragraph>().single()
        val textClasses = paragraph.content.map { it::class }
        assertTrue(textClasses.containsAll(listOf(Text.Literal::class, Text.Variable::class, Text.NewLine::class)))
    }

    @Test
    fun `dsl carries the ids provided by the caller`() {
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
    fun `extended DSL supports font type and variables on content`() {
        var next = 0
        fun id() = next++
        val letter = letterMarkupExtended(
            saksinformasjon = saksinformasjon(
                gjelderNavn = "Ola Nordmann",
                gjelderPersonidentifikator = "12345678901",
                saksnummer = "9876543",
                dokumentDato = LocalDate.of(2026, 7, 9),
            ),
            signatur = signatur(
                hilsenTekst = "Hilsen",
                navAvsenderEnhet = "NAV",
            ),
        ) {
            title1 {
                text(id(), "Tittel fra builder ")
                variable(id(), "x")
            }
            outline {
                title2(id()) { text(id(), "Innledning") }
                title2(id()) {
                    text(id(), "Innledning ")
                    variable(id(), "x")
                }
                title3(id()) { text(id(), "Mellomtittel") }
                formText(id(), Size.SHORT) { text(id(), "TEXT") }
                title3(id()) {
                    text(id(), "Mellomtittel ")
                    variable(id(), "x")
                }
                title4(id()) { text(id(), "Detaljer") }
                title4(id()) {
                    text(id(), "Detaljer ")
                    variable(id(), "x")
                }
                paragraph(id()) { text(id(), "Ingress", fontType = FontType.BOLD) }
                paragraph(id()) {
                    text(id(), "Du får ")
                    variable(id(), "1000 Kr", editBehaviour = EditBehaviour.REDIGERBAR_DATA)
                    newLine(id())
                }
                itemList(id()) {
                    item(id()) { text(id(), "Punkt", fontType = FontType.BOLD) }
                    item(id()) {
                        text(id(), "Du får ")
                        variable(id(), "1000 Kr")
                    }
                }
                table(id()) {
                    header(id()) {
                        column(id()) { text(id(), "Kolonne") }
                        column(id()) {
                            text(id(), "Kolonne ")
                            variable(id(), "2")
                        }
                    }
                    row(id()) {
                        cell(id()) { text(id(), "Celle", fontType = FontType.BOLD) }
                        cell(id()) {
                            text(id(), "bla")
                            variable(id(), "2")
                        }
                    }
                }
                formText(id(), Size.SHORT) { text(id(), "Ledetekst", fontType = FontType.BOLD) }
                formText(id(), Size.LONG) {
                    text(id(), "Skriv ")
                    variable(id(), "her")
                }
                formChoice(id()) {
                    prompt { text(id(), "Velg") }
                    prompt {
                        text(id(), "Du får svar innen ")
                        variable(id(), "x")
                        text(id(), "uker.")
                    }
                    choice(id()) { text(id(), "Ja", fontType = FontType.BOLD) }
                    choice(id()) { text(id(), "Nei") }
                    choice(id()) {
                        text(id(), "kanskje")
                        variable(id(), "eller?", editBehaviour = FRITEKST)
                    }
                }
            }
        }

        val titleTexts = letter.title1.filterIsInstance<Text.Literal>().map { it.text }
        assertTrue(titleTexts.contains("Tittel fra builder "))

        val title2 = letter.blocks.filterIsInstance<Block.Title2>().first()
        assertEquals(FontType.PLAIN, title2.content.filterIsInstance<Text.Literal>().first().fontType)

        val paragraph = letter.blocks.filterIsInstance<Block.Paragraph>().first()
        assertEquals(FontType.BOLD, (paragraph.content.single() as Text.Literal).fontType)

        val item = letter.blocks.filterIsInstance<Block.ItemList>().single().items.first()
        assertEquals(FontType.BOLD, (item.content.single() as Text.Literal).fontType)

        val cell = letter.blocks.filterIsInstance<Block.Table>().single().rows.single().cells.first()
        assertEquals(FontType.BOLD, (cell.content.single() as Text.Literal).fontType)
    }

    @Test
    fun `pdfTittelExtended supports variables`() {
        var next = 0
        fun id() = next++
        val tittel = pdfTittelExtended {
            text(id(), "Vedtak for ")
            variable(id(), "Ola Nordmann")
        }

        assertEquals(listOf(Text.Literal::class, Text.Variable::class), tittel.title1.map { it::class })
    }

    @Test
    fun `letterMarkupWithDataUsage includes brevtype and data usage`() {
        val property = dataUsageProperty(typeName = "UngUfoerDto", propertyName = "totaltUfoerePerMnd")

        val letter = letterMarkupWithDataUsage(
            markup = letterMarkup(
                saksinformasjon = saksinformasjon(
                    gjelderNavn = "Ola Nordmann",
                    gjelderPersonidentifikator = "12345678901",
                    saksnummer = "9876543",
                    dokumentDato = LocalDate.of(2026, 7, 9),
                ),
                signatur = signatur(
                    hilsenTekst = "Med vennlig hilsen",
                    navAvsenderEnhet = "NAV",
                ),
            ) {
                title1("Vedtak")
                outline {
                    paragraph("Innhold")
                }
            },
            brevtype = Markup.Brevtype.VEDTAKSBREV,
            letterDataUsage = setOf(property),
        )

        assertEquals(Markup.Brevtype.VEDTAKSBREV, letter.brevtype)
        assertEquals(setOf(property), letter.letterDataUsage)
        assertEquals("Vedtak", (letter.markup.title1.single() as Text.Literal).text)
    }

    @Test
    fun `letterMarkupWithDataUsageExtended supports variable content and round-trips through json`() {
        var next = 0
        fun id() = next++
        val property = dataUsageProperty(typeName = "UngUfoerDto", propertyName = "belop")

        val letter = letterMarkupWithDataUsage(
            markup = letterMarkupExtended(
                saksinformasjon = saksinformasjon(
                    gjelderNavn = "Ola Nordmann",
                    gjelderPersonidentifikator = "12345678901",
                    saksnummer = "9876543",
                    dokumentDato = LocalDate.of(2026, 7, 9),
                ),
                signatur = signatur(
                    hilsenTekst = "Med vennlig hilsen",
                    navAvsenderEnhet = "NAV",
                ),
            ) {
                title1 { text(id(), "Orientering") }
                outline {
                    paragraph(id()) {
                        text(id(), "Beløp: ")
                        variable(id(), "1000 kr")
                    }
                }
            },
            brevtype = Markup.Brevtype.INFORMASJONSBREV,
            letterDataUsage = setOf(property),
        )

        val paragraph = letter.markup.blocks.single() as Block.Paragraph
        assertEquals(listOf(Text.Literal::class, Text.Variable::class), paragraph.content.map { it::class })

        val decoded = Json.decodeFromString(
            LetterMarkupWithDataUsage.serializer(),
            Json.encodeToString(LetterMarkupWithDataUsage.serializer(), letter),
        )
        assertEquals(letter, decoded)
    }

    @Test
    fun `attachmentExtended supports variable text`() {
        var next = 0
        fun id() = next++
        val vedlegg = attachmentExtended(inkluderSaksinformasjon = false) {
            title1 { text(id(), "Vedlegg 2") }
            outline {
                paragraph(id()) {
                    text(id(), "Sats ")
                    variable(id(), "2G")
                }
            }
        }

        assertEquals(false, vedlegg.inkluderSaksinformasjon)
        val paragraph = vedlegg.blocks.single() as Block.Paragraph
        val textClasses = paragraph.content.map { it::class }
        assertTrue(textClasses.containsAll(listOf(Text.Literal::class, Text.Variable::class)))
    }
}
