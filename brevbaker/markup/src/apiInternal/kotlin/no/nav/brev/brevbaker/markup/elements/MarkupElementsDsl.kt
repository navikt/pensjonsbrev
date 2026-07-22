package no.nav.brev.brevbaker.markup.elements

import no.nav.brev.brevbaker.markup.outline.EditBehaviour
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType

/**
 * DSL-markør for den frittstående element-byggeren. Egen markør (ikke [no.nav.brev.brevbaker.markup.dsl])
 * slik at det ikke oppstår forveksling mellom brev-DSL-en og element-byggeren beregnet på enhetstester.
 */
@DslMarker
annotation class MarkupElementsDsl

/**
 * Bygg ett enkelt markup-element frittstående. Byggeren returnerer den konkrete typen til elementet du
 * konstruerer, f.eks. `val text = markupElement { literal("...") }` gir en [Text.Literal] og
 * `markupElement { table(...) }` gir en [Block.Table].
 *
 * Alle funksjonene ligger på samme nivå. Sammensatte elementer bygges ved å sende inn barna som argumenter,
 * f.eks. `markupElement { paragraph(literal("a"), literal("b")) }`. Alle id-er defaulter til 0.
 */
fun <T> markupElement(build: MarkupElementScope.() -> T): T = MarkupElementScope().build()

/**
 * Flat scope med byggefunksjoner for hvert enkelt outline-element. Hver funksjon returnerer det konstruerte
 * elementet slik at det kan sendes inn som barn til andre elementer.
 */
@MarkupElementsDsl
class MarkupElementScope internal constructor() {

    // --- Text ---

    /** Fast tekst. Eks: `markupElement { literal("Hei") }` */
    fun literal(text: String, id: Int = 0, fontType: FontType = FontType.PLAIN, editBehaviour: EditBehaviour? = null): Text.Literal =
        Text.Literal(id, text, fontType, editBehaviour)

    /** En variabel (datafelt). Eks: `markupElement { variable("navn") }` */
    fun variable(text: String, id: Int = 0, fontType: FontType = FontType.PLAIN, editBehaviour: EditBehaviour? = null): Text.Variable =
        Text.Variable(id, text, fontType, editBehaviour)

    /** Et linjeskift. Eks: `markupElement { newLine() }` */
    fun newLine(id: Int = 0): Text.NewLine = Text.NewLine(id)

    // --- Titles & paragraph ---

    /** Nivå-2-overskrift. Eks: `markupElement { title2(literal("Innledning")) }` */
    fun title2(vararg content: Text, id: Int = 0): Block.Title2 = Block.Title2(id, content.toList())

    /** Nivå-3-overskrift. Eks: `markupElement { title3(literal("Mellomtittel")) }` */
    fun title3(vararg content: Text, id: Int = 0): Block.Title3 = Block.Title3(id, content.toList())

    /** Nivå-4-overskrift. Eks: `markupElement { title4(literal("Detaljer")) }` */
    fun title4(vararg content: Text, id: Int = 0): Block.Title4 = Block.Title4(id, content.toList())

    /** Et avsnitt. Eks: `markupElement { paragraph(literal("Du får "), variable("uføretrygd")) }` */
    fun paragraph(vararg content: Text, id: Int = 0): Block.Paragraph = Block.Paragraph(id, content.toList())

    // --- Lists ---

    /** Et listepunkt. Eks: `markupElement { item(literal("Punkt 1")) }` */
    fun item(vararg content: Text, id: Int = 0): Block.Item = Block.Item(id, content.toList())

    /** En punktliste. Eks: `markupElement { itemList(item(literal("a")), item(literal("b"))) }` */
    fun itemList(vararg items: Block.Item, id: Int = 0): Block.ItemList = Block.ItemList(id, items.toList())

    /** En nummerert liste. Eks: `markupElement { numberedList(item(literal("Steg 1"))) }` */
    fun numberedList(vararg items: Block.Item, id: Int = 0): Block.NumberedList = Block.NumberedList(id, items.toList())

    // --- Table ---

    /** En tabellcelle. Eks: `markupElement { cell(literal("A1")) }` */
    fun cell(vararg content: Text, id: Int = 0): Block.Table.Cell = Block.Table.Cell(id, content.toList())

    /** En kolonnespesifikasjon (overskriftstekst + [alignment]/[span]). Eks: `columnSpec(literal("Beløp"), ColumnAlignment.RIGHT)` */
    fun columnSpec(
        vararg content: Text,
        alignment: ColumnAlignment = ColumnAlignment.LEFT,
        span: Int = 1,
        id: Int = 0,
    ): Block.Table.ColumnSpec = Block.Table.ColumnSpec(id, content.toList(), alignment, span)

    /** En tabelloverskrift. Eks: `header(columnSpec(literal("A")), columnSpec(literal("B")))` */
    fun header(vararg colSpec: Block.Table.ColumnSpec, id: Int = 0): Block.Table.Header =
        Block.Table.Header(id, colSpec.toList())

    /** En tabellrad. Eks: `row(cell(literal("a1")), cell(literal("b1")))` */
    fun row(vararg cells: Block.Table.Cell, id: Int = 0): Block.Table.Row = Block.Table.Row(id, cells.toList())

    /** En tabell (overskrift + rader). Eks: `table(header(columnSpec(literal("A"))), row(cell(literal("a1"))))` */
    fun table(header: Block.Table.Header, vararg rows: Block.Table.Row, id: Int = 0): Block.Table =
        Block.Table(id, rows.toList(), header)

    // --- Forms ---

    /** Et fritekst-skjemafelt med angitt [size]. Eks: `formText(Size.LONG, literal("Skriv her"))` */
    fun formText(size: Size, vararg prompt: Text, vspace: Boolean = true, id: Int = 0): Block.FormText =
        Block.FormText(id, prompt.toList(), size, vspace)

    /** Et svaralternativ til et avkrysningsfelt. Eks: `choice(literal("Ja"))` */
    fun choice(vararg content: Text, id: Int = 0): Block.FormChoice.Choice =
        Block.FormChoice.Choice(id, content.toList())

    /** Et avkrysningsfelt (ledetekst + valg). Eks: `formChoice(listOf(literal("Velg")), choice(literal("Ja")), choice(literal("Nei")))` */
    fun formChoice(
        prompt: List<Text> = emptyList(),
        vararg choices: Block.FormChoice.Choice,
        vspace: Boolean = true,
        id: Int = 0,
    ): Block.FormChoice = Block.FormChoice(id, prompt, choices.toList(), vspace)
}
