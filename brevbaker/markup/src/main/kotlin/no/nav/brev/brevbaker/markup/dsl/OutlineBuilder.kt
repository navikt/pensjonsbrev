package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType
import kotlin.jvm.JvmName

@MarkupDsl
class OutlineBuilder<C : AbstractContentBuilder> internal constructor(
    private val ids: IdGenerator,
    private val contentFactory: ContentFactory<C>,
) {
    private val blocks = mutableListOf<Block>()

    /**
     * Legg til et avsnitt via DSL-blokk.
     *
     * ```
     * paragraph { text("Du får "); variable("uføretrygd"); text(".") }
     * ```
     */
    fun paragraph(content: C.() -> Unit) {
        blocks.add(Block.Paragraph(ids.next(), ids.content(contentFactory, content)))
    }

    /**
     * Legg til et avsnitt med ren tekst, valgfritt med [fontType].
     *
     * ```
     * paragraph("Dette er et avsnitt.")
     * ```
     */
    fun paragraph(text: String, fontType: FontType = FontType.PLAIN) {
        blocks.add(Block.Paragraph(ids.next(), listOf(Text.Literal(ids.next(), text, fontType))))
    }

    /**
     * Legg til en punktliste (kulepunkter).
     *
     * ```
     * itemList { item("Punkt 1"); item("Punkt 2") }
     * ```
     */
    fun itemList(build: ItemsBuilder<C>.() -> Unit) {
        blocks.add(Block.ItemList(ids.next(), ItemsBuilder(ids, contentFactory).apply(build).build()))
    }

    /**
     * Legg til en nummerert liste.
     *
     * ```
     * numberedList { item("Steg 1"); item("Steg 2") }
     * ```
     */
    fun numberedList(build: ItemsBuilder<C>.() -> Unit) {
        blocks.add(Block.NumberedList(ids.next(), ItemsBuilder(ids, contentFactory).apply(build).build()))
    }

    /**
     * Legg til en tabell med kolonneoverskrift og rader.
     *
     * En tabell må alltid ha:
     * - én `header { ... }` med minst én `column(...)`
     * - minst én `row { ... }`
     *
     * Hver rad må ha like mange celler som antall kolonner i header.
     *
     * Vanlige valg i tabeller:
     * - [ColumnAlignment.LEFT] eller [ColumnAlignment.RIGHT] per kolonne
     * - `span` for kolonner som skal dekke flere kolonner
     * - celleinnhold som ren tekst eller sammensatt tekst med `text(...)` og `variable(...)`
     *
     * ```
     * table {
     *     header {
     *         column("Ytelse")
     *         column("Beløp", alignment = ColumnAlignment.RIGHT)
     *     }
     *     row { cell("Uføretrygd"); cell("20 000 kr") }
     *     row { cell { text("Sum "); variable("maaned") }; cell("20 000 kr") }
     * }
     * ```
     */
    fun table(build: TableBuilder<C>.() -> Unit) {
        blocks.add(TableBuilder(ids, contentFactory).apply(build).build())
    }

    /**
     * Legg til et skjemafelt for fritekst med angitt [size] og valgfri [vspace] (luft rundt feltet).
     * Ledeteksten settes i DSL-blokken.
     * Se også shorthand-varianten `formText("...", Size.SHORT)`.
     *
     * ```
     * formText(Size.LONG) { text("Skriv her") }
     * ```
     */
    fun formText(size: Size, vspace: Boolean = true, prompt: C.() -> Unit) {
        blocks.add(Block.FormText(ids.next(), ids.content(contentFactory, prompt), size, vspace))
    }

    /**
     * Legg til et skjemafelt for fritekst med ren ledetekst og angitt [size].
     * Se også DSL-varianten `formText(Size.SHORT) { ... }`.
     *
     * ```
     * formText("Fyll inn dato", Size.SHORT)
     * ```
     */
    fun formText(text: String, size: Size, fontType: FontType = FontType.PLAIN, vspace: Boolean = true) {
        blocks.add(Block.FormText(ids.next(), listOf(Text.Literal(ids.next(), text, fontType)), size, vspace))
    }

    /**
     * Legg til et avkrysningsfelt med en ledetekst (`prompt`) og minst to valg (`choice`).
     *
     * ```
     * formChoice {
     *     prompt("Velg")
     *     choice("Ja")
     *     choice("Nei")
     * }
     * ```
     */
    fun formChoice(vspace: Boolean = true, build: FormChoiceBuilder<C>.() -> Unit) {
        val formChoiceBuilder = FormChoiceBuilder(ids, contentFactory)
        FormChoiceBuilder.setVspace(formChoiceBuilder, vspace)
        blocks.add(formChoiceBuilder.apply(build).build())
    }

    internal fun build(): List<Block> = blocks.toList()

    // Tittel/kolonne/prompt tilbys som extension-funksjoner (ikke medlemmer) slik at plain- og
    // variabel-varianten kan velges via overload-resolution på C. Disse companion-hjelperne gir
    // extensionene tilgang til privat tilstand uten å eksponere den på DSL-scopet.
    internal companion object {
        internal fun addTitle2(builder: OutlineBuilder<*>, content: List<Text>) {
            builder.blocks.add(Block.Title2(builder.ids.next(), content))
        }

        internal fun addTitle3(builder: OutlineBuilder<*>, content: List<Text>) {
            builder.blocks.add(Block.Title3(builder.ids.next(), content))
        }

        internal fun addTitle4(builder: OutlineBuilder<*>, content: List<Text>) {
            builder.blocks.add(Block.Title4(builder.ids.next(), content))
        }

        internal fun plainText(builder: OutlineBuilder<*>, text: String): List<Text> = builder.ids.plainText(text)

        internal fun plainText(builder: OutlineBuilder<*>, content: PlainTextBuilder.() -> Unit): List<Text> =
            builder.ids.plainText(content)

        internal fun plainExtendedText(
            builder: OutlineBuilder<*>,
            content: PlainExtendedTextBuilder.() -> Unit,
        ): List<Text> = builder.ids.plainExtendedText(content)
    }
}

/**
 * Legg til en nivå-2-overskrift som ren tekst. Gjelder både med og uten `variable`.
 * Se også DSL-variantene `title2 { text("...") }` og `title2 { text("..."); variable("...") }`.
 *
 * ```
 * title2("Innledning")
 * ```
 */
fun <C : AbstractContentBuilder> OutlineBuilder<C>.title2(text: String) =
    OutlineBuilder.addTitle2(this, OutlineBuilder.plainText(this, text))

/**
 * Legg til en nivå-2-overskrift via DSL-blokk (uten `variable`).
 * Se også shorthand-varianten `title2("...")`.
 *
 * ```
 * title2 { text("Innledning "); text("del 1") }
 * ```
 */
@JvmName("title2WithPlainTextBuilder")
fun OutlineBuilder<ContentBuilder>.title2(content: PlainTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle2(this, OutlineBuilder.plainText(this, content))

/**
 * Legg til en nivå-2-overskrift via DSL-blokk med `variable`.
 * Se også shorthand-varianten `title2("...")`.
 *
 * ```
 * title2 { text("Vedtak for "); variable("navn") }
 * ```
 */
@JvmName("title2WithPlainExtendedTextBuilder")
fun OutlineBuilder<ExtendedContentBuilder>.title2(content: PlainExtendedTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle2(this, OutlineBuilder.plainExtendedText(this, content))

/**
 * Legg til en nivå-3-overskrift som ren tekst. Gjelder både med og uten `variable`.
 * Se også DSL-variantene `title3 { text("...") }` og `title3 { text("..."); variable("...") }`.
 *
 * ```
 * title3("Mellomtittel")
 * ```
 */
fun <C : AbstractContentBuilder> OutlineBuilder<C>.title3(text: String) =
    OutlineBuilder.addTitle3(this, OutlineBuilder.plainText(this, text))

/**
 * Legg til en nivå-3-overskrift via DSL-blokk (uten `variable`).
 * Se også shorthand-varianten `title3("...")`.
 *
 * ```
 * title3 { text("Mellomtittel") }
 * ```
 */
@JvmName("title3WithPlainTextBuilder")
fun OutlineBuilder<ContentBuilder>.title3(content: PlainTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle3(this, OutlineBuilder.plainText(this, content))

/**
 * Legg til en nivå-3-overskrift via DSL-blokk med `variable`.
 * Se også shorthand-varianten `title3("...")`.
 *
 * ```
 * title3 { text("Mellomtittel "); variable("nr") }
 * ```
 */
@JvmName("title3WithPlainExtendedTextBuilder")
fun OutlineBuilder<ExtendedContentBuilder>.title3(content: PlainExtendedTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle3(this, OutlineBuilder.plainExtendedText(this, content))

/**
 * Legg til en nivå-4-overskrift som ren tekst. Gjelder både med og uten `variable`.
 * Se også DSL-variantene `title4 { text("...") }` og `title4 { text("..."); variable("...") }`.
 *
 * ```
 * title4("Detaljer")
 * ```
 */
fun <C : AbstractContentBuilder> OutlineBuilder<C>.title4(text: String) =
    OutlineBuilder.addTitle4(this, OutlineBuilder.plainText(this, text))

/**
 * Legg til en nivå-4-overskrift via DSL-blokk (uten `variable`).
 * Se også shorthand-varianten `title4("...")`.
 *
 * ```
 * title4 { text("Detaljer") }
 * ```
 */
@JvmName("title4WithPlainTextBuilder")
fun OutlineBuilder<ContentBuilder>.title4(content: PlainTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle4(this, OutlineBuilder.plainText(this, content))

/**
 * Legg til en nivå-4-overskrift via DSL-blokk med `variable`.
 * Se også shorthand-varianten `title4("...")`.
 *
 * ```
 * title4 { text("Detaljer "); variable("nr") }
 * ```
 */
@JvmName("title4WithPlainExtendedTextBuilder")
fun OutlineBuilder<ExtendedContentBuilder>.title4(content: PlainExtendedTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle4(this, OutlineBuilder.plainExtendedText(this, content))

@MarkupDsl
class ItemsBuilder<C : AbstractContentBuilder> internal constructor(
    private val ids: IdGenerator,
    private val contentFactory: ContentFactory<C>,
) {
    private val items = mutableListOf<Block.Item>()

    /**
     * Legg til et listepunkt via DSL-blokk.
     * Se også shorthand-varianten `item("...")`.
     *
     * ```
     * itemList { item { text("Du får "); variable("uføretrygd") } }
     * ```
     */
    fun item(content: C.() -> Unit) {
        items.add(Block.Item(ids.next(), ids.content(contentFactory, content)))
    }

    /**
     * Legg til et listepunkt med ren tekst, valgfritt med [fontType].
     * Se også DSL-varianten `item { ... }`.
     *
     * ```
     * itemList { item("Punkt 1") }
     * ```
     */
    fun item(text: String, fontType: FontType = FontType.PLAIN) {
        items.add(Block.Item(ids.next(), listOf(Text.Literal(ids.next(), text, fontType))))
    }

    internal fun build(): List<Block.Item> {
        require(items.isNotEmpty()) { "List has no items" }
        return items.toList()
    }
}

@MarkupDsl
class TableBuilder<C : AbstractContentBuilder> internal constructor(
    private val ids: IdGenerator,
    private val contentFactory: ContentFactory<C>,
) {
    private var header: Block.Table.Header? = null
    private val rows = mutableListOf<Block.Table.Row>()

    /**
     * Definer kolonnene i tabellen.
     *
     * Denne må brukes én gang per tabell, og må inneholde minst én `column(...)`.
     *
     * ```
     * header { column("Kolonne A"); column("Kolonne B", ColumnAlignment.RIGHT) }
     * ```
     */
    fun header(build: HeaderBuilder<C>.() -> Unit) {
        header = HeaderBuilder<C>(ids).apply(build).build()
    }

    /**
     * Legg til én rad i tabellen.
     *
     * En rad består av én eller flere `cell(...)`. Antall celler må være lik antall kolonner i
     * `header`.
     *
     * ```
     * row { cell("A1"); cell("B1") }
     * ```
     */
    fun row(build: RowBuilder<C>.() -> Unit) {
        rows.add(RowBuilder(ids, contentFactory).apply(build).build())
    }

    internal fun build(): Block.Table {
        val id = ids.next()
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
        return Block.Table(
            id = id,
            rows = rows.toList(),
            header = header,
        )
    }
}

@MarkupDsl
class HeaderBuilder<C : AbstractContentBuilder> internal constructor(
    private val ids: IdGenerator,
) {
    private val colSpec = mutableListOf<Block.Table.ColumnSpec>()

    internal fun build(): Block.Table.Header = Block.Table.Header(ids.next(), colSpec.toList())

    internal companion object {
        internal fun addColumn(builder: HeaderBuilder<*>, alignment: ColumnAlignment, span: Int, content: List<Text>) {
            require(span >= 1) { "Table column span must be at least 1, but was $span" }
            val cell = Block.Table.Cell(builder.ids.next(), content)
            builder.colSpec.add(Block.Table.ColumnSpec(builder.ids.next(), cell, alignment, span))
        }

        internal fun plainText(builder: HeaderBuilder<*>, text: String): List<Text> = builder.ids.plainText(text)

        internal fun plainText(builder: HeaderBuilder<*>, content: PlainTextBuilder.() -> Unit): List<Text> =
            builder.ids.plainText(content)

        internal fun plainExtendedText(
            builder: HeaderBuilder<*>,
            content: PlainExtendedTextBuilder.() -> Unit,
        ): List<Text> = builder.ids.plainExtendedText(content)
    }
}

/**
 * Legg til en kolonne med ren tekst i header.
 * Se også DSL-variantene `column { text("...") }` og `column { text("..."); variable("...") }`.
 *
 * Parametere:
 * - `text`: overskriftstekst for kolonnen
 * - `alignment`: justering av kolonneinnhold ([ColumnAlignment.LEFT] som standard)
 * - `span`: hvor mange kolonner overskriften skal dekke (`1` som standard)
 *
 * ```
 * header { column("Beløp", ColumnAlignment.RIGHT) }
 * ```
 */
fun <C : AbstractContentBuilder> HeaderBuilder<C>.column(
    text: String,
    alignment: ColumnAlignment = ColumnAlignment.LEFT,
    span: Int = 1,
) = HeaderBuilder.addColumn(this, alignment, span, HeaderBuilder.plainText(this, text))

/**
 * Legg til en kolonne i header med sammensatt tekst.
 * Se også shorthand-varianten `column("...")`.
 *
 * Parametere:
 * - `alignment`: justering av kolonneinnhold ([ColumnAlignment.LEFT] som standard)
 * - `span`: hvor mange kolonner overskriften skal dekke (`1` som standard)
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
) = HeaderBuilder.addColumn(this, alignment, span, HeaderBuilder.plainText(this, content))

/**
 * Legg til en kolonne i header med tekst som kan kombineres med `variable(...)`.
 * Se også shorthand-varianten `column("...")`.
 *
 * Parametere:
 * - `alignment`: justering av kolonneinnhold ([ColumnAlignment.LEFT] som standard)
 * - `span`: hvor mange kolonner overskriften skal dekke (`1` som standard)
 *
 * ```
 * header { column { text("Beløp "); variable("år") } }
 * ```
 */
@JvmName("columnWithPlainExtendedTextBuilder")
fun HeaderBuilder<ExtendedContentBuilder>.column(
    alignment: ColumnAlignment = ColumnAlignment.LEFT,
    span: Int = 1,
    content: PlainExtendedTextBuilder.() -> Unit,
) = HeaderBuilder.addColumn(this, alignment, span, HeaderBuilder.plainExtendedText(this, content))

@MarkupDsl
class RowBuilder<C : AbstractContentBuilder> internal constructor(
    private val ids: IdGenerator,
    private val contentFactory: ContentFactory<C>,
) {
    private val cells = mutableListOf<Block.Table.Cell>()

    /**
     * Legg til en celle med sammensatt tekst.
     * Se også shorthand-varianten `cell("...")`.
     *
     * ```
     * row { cell { text("Sum: "); variable("beløp") } }
     * ```
     */
    fun cell(content: C.() -> Unit) {
        cells.add(Block.Table.Cell(ids.next(), ids.content(contentFactory, content)))
    }

    /**
     * Legg til en celle med ren tekst.
     * Se også DSL-varianten `cell { ... }`.
     *
     * Parametere:
     * - `text`: tekst i cellen
     * - `fontType`: skriftstil ([FontType.PLAIN] som standard)
     *
     * ```
     * row { cell("A1"); cell("B1") }
     * ```
     */
    fun cell(text: String, fontType: FontType = FontType.PLAIN) {
        cells.add(Block.Table.Cell(ids.next(), listOf(Text.Literal(ids.next(), text, fontType))))
    }

    internal fun build(): Block.Table.Row = Block.Table.Row(ids.next(), cells.toList())
}

@MarkupDsl
class FormChoiceBuilder<C : AbstractContentBuilder> internal constructor(
    private val ids: IdGenerator,
    private val contentFactory: ContentFactory<C>,
) {
    private var vspace: Boolean = true
    private val prompt = mutableListOf<Text>()
    private val choices = mutableListOf<Block.FormChoice.Choice>()

    /**
     * Legg til et svaralternativ via DSL-blokk. Teksten må være ikke-tom.
     * Se også shorthand-varianten `choice("...")`.
     *
     * ```
     * formChoice { prompt("Velg"); choice { text("Ja, jeg samtykker") }; choice("Nei") }
     * ```
     */
    fun choice(content: C.() -> Unit) {
        val choiceContent = ids.content(contentFactory, content)
        require(choiceContent.any { it.text.isNotBlank() }) { "Form choice option text must be non-empty" }
        choices.add(Block.FormChoice.Choice(ids.next(), choiceContent))
    }

    /**
     * Legg til et svaralternativ med ren tekst, valgfritt med [fontType]. Teksten må være ikke-tom.
     * Se også DSL-varianten `choice { ... }`.
     *
     * ```
     * formChoice { prompt("Velg"); choice("Ja"); choice("Nei") }
     * ```
     */
    fun choice(text: String, fontType: FontType = FontType.PLAIN) {
        require(text.isNotBlank()) { "Form choice option text must be non-empty" }
        choices.add(Block.FormChoice.Choice(ids.next(), listOf(Text.Literal(ids.next(), text, fontType))))
    }

    internal fun build(): Block.FormChoice {
        require(prompt.any { it.text.isNotBlank() }) { "Form choice must have a non-empty prompt" }
        require(choices.size >= 2) { "Form choice must have at least two choices" }
        return Block.FormChoice(ids.next(), prompt.toList(), choices.toList(), vspace)
    }

    internal companion object {
        internal fun addPrompt(builder: FormChoiceBuilder<*>, content: List<Text>) {
            builder.prompt.addAll(content)
        }

        internal fun plainText(builder: FormChoiceBuilder<*>, text: String): List<Text> = builder.ids.plainText(text)

        internal fun plainExtendedText(
            builder: FormChoiceBuilder<*>,
            content: PlainExtendedTextBuilder.() -> Unit,
        ): List<Text> = builder.ids.plainExtendedText(content)

        internal fun setVspace(builder: FormChoiceBuilder<*>, value: Boolean) {
            builder.vspace = value
        }
    }
}

/**
 * Sett avkrysningsfeltets ledetekst som ren tekst. Gjelder både med og uten `variable`.
 * Se også DSL-varianten `prompt { text("..."); variable("...") }`.
 *
 * ```
 * formChoice { prompt("Ønsker du å klage?"); choice("Ja"); choice("Nei") }
 * ```
 */
fun <C : AbstractContentBuilder> FormChoiceBuilder<C>.prompt(text: String) =
    FormChoiceBuilder.addPrompt(this, FormChoiceBuilder.plainText(this, text))

/**
 * Sett avkrysningsfeltets ledetekst via DSL-blokk med `variable`.
 * Se også shorthand-varianten `prompt("...")`.
 *
 * ```
 * formChoice { prompt { text("Svar innen "); variable("frist") }; choice("Ja"); choice("Nei") }
 * ```
 */
fun FormChoiceBuilder<ExtendedContentBuilder>.prompt(content: PlainExtendedTextBuilder.() -> Unit) =
    FormChoiceBuilder.addPrompt(this, FormChoiceBuilder.plainExtendedText(this, content))
