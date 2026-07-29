package no.nav.brev.brevbaker.markup

import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Text
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CleanMarkupTest {

    private val saksinformasjon = Saksinformasjon(
        gjelderNavn = "Ola Nordmann",
        gjelderPersonidentifikator = Markup.Personidentifikator("12345678901"),
        annenMottakerNavn = null,
        saksnummer = Markup.Saksnummer("9876543"),
        dokumentDato = LocalDate.of(2026, 7, 9),
    )
    private val signatur = Signatur(hilsenTekst = "Med vennlig hilsen", saksbehandlerSignatur = null, navAvsenderEnhet = "NAV")

    private var idCounter = 0
    private fun nextId() = idCounter++

    private fun literal(text: String) = Text.Literal(nextId(), text)
    private fun newLine() = Text.NewLine(nextId())

    private fun letter(vararg blocks: Block): LetterMarkup =
        LetterMarkup(title1 = emptyList(), saksinformasjon = saksinformasjon, blocks = blocks.toList(), signatur = signatur)

    private fun title2(vararg content: Text) = Block.Title2(nextId(), content.toList())
    private fun title3(vararg content: Text) = Block.Title3(nextId(), content.toList())
    private fun paragraph(vararg content: Text) = Block.Paragraph(nextId(), content.toList())

    @Test
    fun `tom tittel blir fjernet`() {
        val markup = letter(title2(literal("   "), newLine(), literal("      ")))
        assertThat(markup.clean().blocks).isEmpty()
    }

    @Test
    fun `tittel med tekst blir ikke fjernet men newlines blir fjernet`() {
        val cleansed = letter(title2(literal("aaa"), newLine(), literal("Innhold"))).clean()

        assertThat(cleansed.blocks).isNotEmpty()
        assertThat(cleansed.blocks[0])
            .isInstanceOfSatisfying(Block.Title2::class.java) { title2 ->
                assertThat(title2.content).allSatisfy { assertThat(it).isNotInstanceOf(Text.NewLine::class.java) }
            }
    }

    @Test
    fun `to tomme titler etter hverandre blir fjernet`() {
        val markup = letter(
            title2(literal("   "), newLine(), literal("      ")),
            title3(literal(" ")),
        )
        assertThat(markup.clean().blocks).isEmpty()
    }

    @Test
    fun `tomme paragraphs fjernes`() {
        val markup = letter(
            paragraph(literal("   "), newLine(), literal("      ")),
            paragraph(literal(" ")),
            paragraph(literal("Innhold")),
        )
        val cleansed = markup.clean()
        assertThat(cleansed.blocks).hasSize(1)
        assertThat(cleansed.blocks[0])
            .isInstanceOfSatisfying(Block.Paragraph::class.java) { paragraph ->
                assertThat(paragraph.content).anySatisfy { assertThat(it.text).isEqualTo("Innhold") }
            }
    }

    @Test
    fun `newLine maa ha noe innhold forran seg for aa beholdes`() {
        val cleansed = letter(
            paragraph(newLine(), literal("Innhold etter newLine")),
            paragraph(literal(""), newLine(), literal("      "), newLine(), literal("Innhold etter newLine")),
        ).clean()
        assertThat(cleansed.blocks).hasSize(2)
        assertThat(cleansed.blocks[0])
            .isInstanceOfSatisfying(Block.Paragraph::class.java) { paragraph ->
                assertThat(paragraph.content).hasSize(1)
                assertThat(paragraph.content).allSatisfy { assertThat(it).isNotInstanceOf(Text.NewLine::class.java) }
            }
        assertThat(cleansed.blocks[1])
            .isInstanceOfSatisfying(Block.Paragraph::class.java) { paragraph ->
                assertThat(paragraph.content).hasSize(3)
                assertThat(paragraph.content).allSatisfy { assertThat(it).isNotInstanceOf(Text.NewLine::class.java) }
            }
    }

    @Test
    fun `newline kan ikke starte i en blokk`() {
        val cleansed = letter(
            paragraph(),
            paragraph(newLine(), literal("Innhold etter newLine")),
        ).clean()
        assertThat(cleansed.blocks).hasSize(1)
        assertThat(cleansed.blocks[0])
            .isInstanceOfSatisfying(Block.Paragraph::class.java) { paragraph ->
                assertThat(paragraph.content).hasSize(1)
                assertThat(paragraph.content).allSatisfy { assertThat(it).isNotInstanceOf(Text.NewLine::class.java) }
            }
    }

    @Test
    fun `tom liste fjernes`() {
        val cleansed = letter(
            paragraph(literal("Før list")),
            Block.ItemList(nextId(), emptyList()),
            paragraph(literal("Etter list")),
        ).clean()
        assertThat(cleansed.blocks).hasSize(2)
        assertThat(cleansed.blocks).noneMatch { it is Block.ItemList || it is Block.NumberedList }
    }

    @Test
    fun `liste med innhold beholdes som egen blokk`() {
        val cleansed = letter(
            paragraph(literal("Før list")),
            Block.ItemList(
                nextId(),
                listOf(
                    Block.Item(nextId(), listOf(literal("Punkt 1"))),
                    Block.Item(nextId(), listOf(literal("Punkt 2"))),
                ),
            ),
            paragraph(literal("Etter list")),
        ).clean()
        assertThat(cleansed.blocks).hasSize(3)
        assertThat(cleansed.blocks[1]).isInstanceOf(Block.ItemList::class.java)
    }

    @Test
    fun `tom tabell fjernes`() {
        val cleansed = letter(
            paragraph(literal("Før table")),
            Block.Table(nextId(), rows = emptyList(), header = Block.Table.Header(nextId(), emptyList())),
            paragraph(literal("Etter table")),
        ).clean()
        assertThat(cleansed.blocks).hasSize(2)
        assertThat(cleansed.blocks).noneMatch { it is Block.Table }
    }

    @Test
    fun `tom formChoice fjernes ikke`() {
        val cleansed = letter(
            paragraph(literal("Før form")),
            Block.FormChoice(nextId(), prompt = emptyList(), choices = emptyList(), vspace = true),
            paragraph(literal("Etter form")),
        ).clean()
        assertThat(cleansed.blocks).hasSize(3)
        assertThat(cleansed.blocks[1]).isInstanceOf(Block.FormChoice::class.java)
    }

    @Test
    fun `formChoice med prompt og choices beholdes uendret`() {
        val cleansed = letter(
            Block.FormChoice(
                nextId(),
                prompt = listOf(literal("Velg et alternativ")),
                choices = listOf(
                    Block.FormChoice.Choice(nextId(), listOf(literal("Ja"))),
                    Block.FormChoice.Choice(nextId(), listOf(literal("Nei"))),
                ),
                vspace = true,
            ),
        ).clean()
        assertThat(cleansed.blocks).hasSize(1)
        assertThat(cleansed.blocks[0])
            .isInstanceOfSatisfying(Block.FormChoice::class.java) { formChoice ->
                assertThat(formChoice.prompt).hasSize(1)
                assertThat(formChoice.choices).hasSize(2)
            }
    }

    @Test
    fun `tom formText fjernes ikke`() {
        val cleansed = letter(
            paragraph(literal("Før form")),
            Block.FormText(nextId(), content = emptyList(), size = Size.LONG, vspace = true),
            paragraph(literal("Etter form")),
        ).clean()
        assertThat(cleansed.blocks).hasSize(3)
        assertThat(cleansed.blocks[1]).isInstanceOf(Block.FormText::class.java)
    }

    @Test
    fun `formText med prompt beholdes uendret`() {
        val cleansed = letter(
            Block.FormText(nextId(), content = listOf(literal("Skriv inn navn")), size = Size.LONG, vspace = true),
        ).clean()
        assertThat(cleansed.blocks).hasSize(1)
        assertThat(cleansed.blocks[0])
            .isInstanceOfSatisfying(Block.FormText::class.java) { formText ->
                assertThat(formText.content).hasSize(1)
                assertThat(formText.size).isEqualTo(Size.LONG)
            }
    }

    @Test
    fun `renser ogsaa vedlegg`() {
        val cleansed = Attachment(
            title1 = listOf(literal("Tittel1")),
            blocks = listOf(
                title2(literal("")),
                paragraph(literal("Innhold")),
            ),
            inkluderSaksinformasjon = false,
        ).clean()
        assertThat(cleansed.blocks).hasSize(1)
        assertThat(cleansed.blocks[0])
            .isInstanceOfSatisfying(Block.Paragraph::class.java) { paragraph ->
                assertThat(paragraph.content).hasSize(1)
                assertThat(paragraph.content).allSatisfy { assertThat(it).isInstanceOf(Text.Literal::class.java) }
            }
    }
}
