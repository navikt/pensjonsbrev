package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType
import kotlin.jvm.JvmName

/**
 * Builder for blokk-innholdet i et brev/vedlegg. Blokk-funksjonene (paragraph, itemList, table osv.)
 * tilbys som extension-funksjoner.
 */
@MarkupDsl
class OutlineBuilder<C : AbstractContentBuilder> internal constructor(
    internal val contentFactory: ContentFactory<C>,
) {
    internal val blocks = mutableListOf<Block>()

    internal fun build(): List<Block> = blocks.toList()
}

/**
 * Legg til et avsnitt via DSL-blokk.
 *
 * ```
 * paragraph { text("Dette er et avsnitt.") }
 * ```
 */
fun OutlineBuilder<ContentBuilder>.paragraph(content: ContentBuilder.() -> Unit) {
    blocks.add(Block.Paragraph(0, contentFactory.content(content)))
}

/**
 * Legg til et avsnitt med ren tekst, valgfritt med [fontType].
 *
 * ```
 * paragraph("Dette er et avsnitt.")
 * ```
 */
fun OutlineBuilder<ContentBuilder>.paragraph(text: String, fontType: FontType = FontType.PLAIN) {
    blocks.add(Block.Paragraph(0, listOf(Text.Literal(0, text, fontType))))
}

/**
 * Legg til en punktliste (kulepunkter).
 *
 * ```
 * itemList { item("Punkt 1"); item("Punkt 2") }
 * ```
 */
fun OutlineBuilder<ContentBuilder>.itemList(build: ItemsBuilder<ContentBuilder>.() -> Unit) {
    blocks.add(Block.ItemList(0, ItemsBuilder(contentFactory).apply(build).build()))
}

/**
 * Legg til en nummerert liste.
 *
 * ```
 * numberedList { item("Steg 1"); item("Steg 2") }
 * ```
 */
fun OutlineBuilder<ContentBuilder>.numberedList(build: ItemsBuilder<ContentBuilder>.() -> Unit) {
    blocks.add(Block.NumberedList(0, ItemsBuilder(contentFactory).apply(build).build()))
}

/**
 * Legg til en tabell med kolonneoverskrift og rader.
 *
 * En tabell må alltid ha én `header { ... }` med minst én `column(...)` og minst én `row { ... }`.
 * Hver rad må ha like mange celler som antall kolonner i header.
 *
 * ```
 * table {
 *     header { column("Ytelse"); column("Beløp", alignment = ColumnAlignment.RIGHT) }
 *     row { cell("Uføretrygd"); cell("20 000 kr") }
 * }
 * ```
 */
fun OutlineBuilder<ContentBuilder>.table(build: TableBuilder<ContentBuilder>.() -> Unit) {
    blocks.add(TableBuilder(contentFactory).apply(build).build(0))
}

/**
 * Legg til et skjemafelt for fritekst med angitt [size] og valgfri [vspace] (luft rundt feltet).
 *
 * ```
 * formText(Size.LONG) { text("Skriv her") }
 * ```
 */
fun OutlineBuilder<ContentBuilder>.formText(size: Size, vspace: Boolean = true, prompt: ContentBuilder.() -> Unit) {
    blocks.add(Block.FormText(0, contentFactory.content(prompt), size, vspace))
}

/**
 * Legg til et skjemafelt for fritekst med ren ledetekst og angitt [size].
 *
 * ```
 * formText("Fyll inn dato", Size.SHORT)
 * ```
 */
fun OutlineBuilder<ContentBuilder>.formText(text: String, size: Size, fontType: FontType = FontType.PLAIN, vspace: Boolean = true) {
    blocks.add(Block.FormText(0, listOf(Text.Literal(0, text, fontType)), size, vspace))
}

/**
 * Legg til et avkrysningsfelt med en ledetekst (`prompt`) og minst to valg (`choice`).
 *
 * ```
 * formChoice { prompt("Velg"); choice("Ja"); choice("Nei") }
 * ```
 */
fun OutlineBuilder<ContentBuilder>.formChoice(vspace: Boolean = true, build: FormChoiceBuilder<ContentBuilder>.() -> Unit) {
    val builder = FormChoiceBuilder(contentFactory)
    builder.vspace = vspace
    blocks.add(builder.apply(build).build(0))
}

/**
 * Legg til en nivå-2-overskrift som ren tekst.
 *
 * ```
 * title2("Innledning")
 * ```
 */
fun OutlineBuilder<ContentBuilder>.title2(text: String) {
    blocks.add(Block.Title2(0, plainText(text)))
}

/**
 * Legg til en nivå-2-overskrift via DSL-blokk.
 *
 * ```
 * title2 { text("Innledning "); text("del 1") }
 * ```
 */
@JvmName("title2WithPlainTextBuilder")
fun OutlineBuilder<ContentBuilder>.title2(content: PlainTextBuilder.() -> Unit) {
    blocks.add(Block.Title2(0, plainText(content)))
}

/**
 * Legg til en nivå-3-overskrift som ren tekst.
 *
 * ```
 * title3("Mellomtittel")
 * ```
 */
fun OutlineBuilder<ContentBuilder>.title3(text: String) {
    blocks.add(Block.Title3(0, plainText(text)))
}

/**
 * Legg til en nivå-3-overskrift via DSL-blokk.
 *
 * ```
 * title3 { text("Mellomtittel") }
 * ```
 */
@JvmName("title3WithPlainTextBuilder")
fun OutlineBuilder<ContentBuilder>.title3(content: PlainTextBuilder.() -> Unit) {
    blocks.add(Block.Title3(0, plainText(content)))
}

/**
 * Legg til en nivå-4-overskrift som ren tekst.
 *
 * ```
 * title4("Detaljer")
 * ```
 */
fun OutlineBuilder<ContentBuilder>.title4(text: String) {
    blocks.add(Block.Title4(0, plainText(text)))
}

/**
 * Legg til en nivå-4-overskrift via DSL-blokk.
 *
 * ```
 * title4 { text("Detaljer") }
 * ```
 */
@JvmName("title4WithPlainTextBuilder")
fun OutlineBuilder<ContentBuilder>.title4(content: PlainTextBuilder.() -> Unit) {
    blocks.add(Block.Title4(0, plainText(content)))
}

/**
 * Builder for listepunkter. [item] tilbys som extension-funksjon.
 */
@MarkupDsl
class ItemsBuilder<C : AbstractContentBuilder> internal constructor(
    internal val contentFactory: ContentFactory<C>,
) {
    internal val items = mutableListOf<Block.Item>()

    internal fun build(): List<Block.Item> {
        require(items.isNotEmpty()) { "List has no items" }
        return items.toList()
    }
}

/**
 * Legg til et listepunkt via DSL-blokk.
 *
 * ```
 * itemList { item { text("Punkt 1") } }
 * ```
 */
fun ItemsBuilder<ContentBuilder>.item(content: ContentBuilder.() -> Unit) {
    items.add(Block.Item(0, contentFactory.content(content)))
}

/**
 * Legg til et listepunkt med ren tekst, valgfritt med [fontType].
 *
 * ```
 * itemList { item("Punkt 1") }
 * ```
 */
fun ItemsBuilder<ContentBuilder>.item(text: String, fontType: FontType = FontType.PLAIN) {
    items.add(Block.Item(0, listOf(Text.Literal(0, text, fontType))))
}

/**
 * Builder for en tabell. [header] og [row] tilbys som extension-funksjoner.
 */
@MarkupDsl
class TableBuilder<C : AbstractContentBuilder> internal constructor(
    internal val contentFactory: ContentFactory<C>,
) {
    internal var header: Block.Table.Header? = null
    internal val rows = mutableListOf<Block.Table.Row>()

    internal fun build(id: Int): Block.Table {
        val header = requireNotNull(header) { "Table must have a header" }
        require(header.colSpec.isNotEmpty()) { "Table column specification needs at least one column" }
        require(rows.isNotEmpty()) { "A table must have at least one row" }
        val columnCount = header.colSpec.size
        rows.forEachIndexed { index, row ->
            require(row.cells.isNotEmpty()) { "Table row $index needs at least one cell" }
            require(row.cells.size == columnCount) {
                "Table row $index has ${row.cells.size} cell(s), but the header defines $columnCount column(s)"
            }
        }
        return Block.Table(id = id, rows = rows.toList(), header = header)
    }
}

/**
 * Definer kolonnene i tabellen. Må brukes én gang per tabell, med minst én `column(...)`.
 *
 * ```
 * header { column("Kolonne A"); column("Kolonne B", ColumnAlignment.RIGHT) }
 * ```
 */
fun TableBuilder<ContentBuilder>.header(build: HeaderBuilder<ContentBuilder>.() -> Unit) {
    header = HeaderBuilder<ContentBuilder>().apply(build).build(0)
}

/**
 * Legg til én rad i tabellen. Antall celler må være lik antall kolonner i `header`.
 *
 * ```
 * row { cell("A1"); cell("B1") }
 * ```
 */
fun TableBuilder<ContentBuilder>.row(build: RowBuilder<ContentBuilder>.() -> Unit) {
    rows.add(RowBuilder(contentFactory).apply(build).build(0))
}

/**
 * Builder for kolonner i en tabelloverskrift. [column] tilbys som extension-funksjon.
 */
@MarkupDsl
class HeaderBuilder<C : AbstractContentBuilder> internal constructor() {
    internal val colSpec = mutableListOf<Block.Table.ColumnSpec>()

    internal fun build(id: Int): Block.Table.Header = Block.Table.Header(id, colSpec.toList())
}

/**
 * Legg til en kolonne med ren tekst i header.
 *
 * ```
 * header { column("Beløp", ColumnAlignment.RIGHT) }
 * ```
 */
fun HeaderBuilder<ContentBuilder>.column(
    text: String,
    alignment: ColumnAlignment = ColumnAlignment.LEFT,
    span: Int = 1,
) {
    require(span >= 1) { "Table column span must be at least 1, but was $span" }
    colSpec.add(Block.Table.ColumnSpec(0, plainText(text), alignment, span))
}

/**
 * Legg til en kolonne i header med sammensatt tekst.
 *
 * ```
 * header { column(alignment = ColumnAlignment.RIGHT) { text("Beløp") } }
 * ```
 */
@JvmName("columnWithPlainTextBuilder")
fun HeaderBuilder<ContentBuilder>.column(
    alignment: ColumnAlignment = ColumnAlignment.LEFT,
    span: Int = 1,
    content: PlainTextBuilder.() -> Unit,
) {
    require(span >= 1) { "Table column span must be at least 1, but was $span" }
    colSpec.add(Block.Table.ColumnSpec(0, plainText(content), alignment, span))
}

/**
 * Builder for en tabellrad. [cell] tilbys som extension-funksjon.
 */
@MarkupDsl
class RowBuilder<C : AbstractContentBuilder> internal constructor(
    internal val contentFactory: ContentFactory<C>,
) {
    internal val cells = mutableListOf<Block.Table.Cell>()

    internal fun build(id: Int): Block.Table.Row = Block.Table.Row(id, cells.toList())
}

/**
 * Legg til en celle med sammensatt tekst.
 *
 * ```
 * row { cell { text("Sum: ") } }
 * ```
 */
fun RowBuilder<ContentBuilder>.cell(content: ContentBuilder.() -> Unit) {
    cells.add(Block.Table.Cell(0, contentFactory.content(content)))
}

/**
 * Legg til en celle med ren tekst, valgfritt med [fontType].
 *
 * ```
 * row { cell("A1"); cell("B1") }
 * ```
 */
fun RowBuilder<ContentBuilder>.cell(text: String, fontType: FontType = FontType.PLAIN) {
    cells.add(Block.Table.Cell(0, listOf(Text.Literal(0, text, fontType))))
}

/**
 * Builder for et avkrysningsfelt. [prompt] og [choice] tilbys som extension-funksjoner.
 */
@MarkupDsl
class FormChoiceBuilder<C : AbstractContentBuilder> internal constructor(
    internal val contentFactory: ContentFactory<C>,
) {
    internal var vspace: Boolean = true
    internal val prompt = mutableListOf<Text>()
    internal val choices = mutableListOf<Block.FormChoice.Choice>()

    internal fun build(id: Int): Block.FormChoice {
        require(prompt.any { it.text.isNotBlank() }) { "Form choice must have a non-empty prompt" }
        require(choices.size >= 2) { "Form choice must have at least two choices" }
        return Block.FormChoice(id, prompt.toList(), choices.toList(), vspace)
    }
}

/**
 * Legg til et svaralternativ via DSL-blokk. Teksten må være ikke-tom.
 *
 * ```
 * formChoice { prompt("Velg"); choice { text("Ja, jeg samtykker") }; choice("Nei") }
 * ```
 */
fun FormChoiceBuilder<ContentBuilder>.choice(content: ContentBuilder.() -> Unit) {
    val choiceContent = contentFactory.content(content)
    require(choiceContent.any { it.text.isNotBlank() }) { "Form choice option text must be non-empty" }
    choices.add(Block.FormChoice.Choice(0, choiceContent))
}

/**
 * Legg til et svaralternativ med ren tekst, valgfritt med [fontType]. Teksten må være ikke-tom.
 *
 * ```
 * formChoice { prompt("Velg"); choice("Ja"); choice("Nei") }
 * ```
 */
fun FormChoiceBuilder<ContentBuilder>.choice(text: String, fontType: FontType = FontType.PLAIN) {
    require(text.isNotBlank()) { "Form choice option text must be non-empty" }
    choices.add(Block.FormChoice.Choice(0, listOf(Text.Literal(0, text, fontType))))
}

/**
 * Sett avkrysningsfeltets ledetekst som ren tekst.
 *
 * ```
 * formChoice { prompt("Ønsker du å klage?"); choice("Ja"); choice("Nei") }
 * ```
 */
fun FormChoiceBuilder<ContentBuilder>.prompt(text: String) {
    prompt.addAll(plainText(text))
}
