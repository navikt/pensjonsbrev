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

    fun paragraph(content: C.() -> Unit) {
        blocks.add(Block.Paragraph(ids.next(), ids.content(contentFactory, content)))
    }

    fun paragraph(text: String, fontType: FontType = FontType.PLAIN) {
        blocks.add(Block.Paragraph(ids.next(), listOf(Text.Literal(ids.next(), text, fontType))))
    }

    fun itemList(build: ItemsBuilder<C>.() -> Unit) {
        blocks.add(Block.ItemList(ids.next(), ItemsBuilder(ids, contentFactory).apply(build).build()))
    }

    fun numberedList(build: ItemsBuilder<C>.() -> Unit) {
        blocks.add(Block.NumberedList(ids.next(), ItemsBuilder(ids, contentFactory).apply(build).build()))
    }

    fun table(build: TableBuilder<C>.() -> Unit) {
        blocks.add(TableBuilder(ids, contentFactory).apply(build).build())
    }

    fun formText(size: Size, vspace: Boolean = true, prompt: C.() -> Unit) {
        blocks.add(Block.FormText(ids.next(), ids.content(contentFactory, prompt), size, vspace))
    }

    fun formText(text: String, size: Size, fontType: FontType = FontType.PLAIN, vspace: Boolean = true) {
        blocks.add(Block.FormText(ids.next(), listOf(Text.Literal(ids.next(), text, fontType)), size, vspace))
    }

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

        internal fun plainVariableText(
            builder: OutlineBuilder<*>,
            content: PlainVariableTextBuilder.() -> Unit,
        ): List<Text> = builder.ids.plainVariableText(content)
    }
}

fun <C : AbstractContentBuilder> OutlineBuilder<C>.title2(text: String) =
    OutlineBuilder.addTitle2(this, OutlineBuilder.plainText(this, text))

@JvmName("title2WithPlainTextBuilder")
fun OutlineBuilder<ContentBuilder>.title2(content: PlainTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle2(this, OutlineBuilder.plainText(this, content))

@JvmName("title2WithPlainVariableTextBuilder")
fun OutlineBuilder<VariableContentBuilder>.title2(content: PlainVariableTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle2(this, OutlineBuilder.plainVariableText(this, content))

fun <C : AbstractContentBuilder> OutlineBuilder<C>.title3(text: String) =
    OutlineBuilder.addTitle3(this, OutlineBuilder.plainText(this, text))

@JvmName("title3WithPlainTextBuilder")
fun OutlineBuilder<ContentBuilder>.title3(content: PlainTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle3(this, OutlineBuilder.plainText(this, content))

@JvmName("title3WithPlainVariableTextBuilder")
fun OutlineBuilder<VariableContentBuilder>.title3(content: PlainVariableTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle3(this, OutlineBuilder.plainVariableText(this, content))

fun <C : AbstractContentBuilder> OutlineBuilder<C>.title4(text: String) =
    OutlineBuilder.addTitle4(this, OutlineBuilder.plainText(this, text))

@JvmName("title4WithPlainTextBuilder")
fun OutlineBuilder<ContentBuilder>.title4(content: PlainTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle4(this, OutlineBuilder.plainText(this, content))

@JvmName("title4WithPlainVariableTextBuilder")
fun OutlineBuilder<VariableContentBuilder>.title4(content: PlainVariableTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle4(this, OutlineBuilder.plainVariableText(this, content))

@MarkupDsl
class ItemsBuilder<C : AbstractContentBuilder> internal constructor(
    private val ids: IdGenerator,
    private val contentFactory: ContentFactory<C>,
) {
    private val items = mutableListOf<Block.Item>()

    fun item(content: C.() -> Unit) {
        items.add(Block.Item(ids.next(), ids.content(contentFactory, content)))
    }

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

    fun header(build: HeaderBuilder<C>.() -> Unit) {
        header = HeaderBuilder<C>(ids).apply(build).build()
    }

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

        internal fun plainVariableText(
            builder: HeaderBuilder<*>,
            content: PlainVariableTextBuilder.() -> Unit,
        ): List<Text> = builder.ids.plainVariableText(content)
    }
}

/** Kolonneoverskrift som ren tekst ([letterMarkup] uten variabler). */
fun <C : AbstractContentBuilder> HeaderBuilder<C>.column(
    text: String,
    alignment: ColumnAlignment = ColumnAlignment.LEFT,
    span: Int = 1,
) = HeaderBuilder.addColumn(this, alignment, span, HeaderBuilder.plainText(this, text))

/** Kolonneoverskrift via builder uten `variable` ([letterMarkup]). */
@JvmName("columnWithPlainTextBuilder")
fun HeaderBuilder<ContentBuilder>.column(
    alignment: ColumnAlignment = ColumnAlignment.LEFT,
    span: Int = 1,
    content: PlainTextBuilder.() -> Unit,
) = HeaderBuilder.addColumn(this, alignment, span, HeaderBuilder.plainText(this, content))

/** Kolonneoverskrift som ren tekst med `variable` ([letterMarkupWithVariables]). */
@JvmName("columnWithPlainVariableTextBuilder")
fun HeaderBuilder<VariableContentBuilder>.column(
    alignment: ColumnAlignment = ColumnAlignment.LEFT,
    span: Int = 1,
    content: PlainVariableTextBuilder.() -> Unit,
) = HeaderBuilder.addColumn(this, alignment, span, HeaderBuilder.plainVariableText(this, content))

@MarkupDsl
class RowBuilder<C : AbstractContentBuilder> internal constructor(
    private val ids: IdGenerator,
    private val contentFactory: ContentFactory<C>,
) {
    private val cells = mutableListOf<Block.Table.Cell>()

    fun cell(content: C.() -> Unit) {
        cells.add(Block.Table.Cell(ids.next(), ids.content(contentFactory, content)))
    }

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

    fun choice(content: C.() -> Unit) {
        val choiceContent = ids.content(contentFactory, content)
        require(choiceContent.any { it.text.isNotBlank() }) { "Form choice option text must be non-empty" }
        choices.add(Block.FormChoice.Choice(ids.next(), choiceContent))
    }

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

        internal fun plainVariableText(
            builder: FormChoiceBuilder<*>,
            content: PlainVariableTextBuilder.() -> Unit,
        ): List<Text> = builder.ids.plainVariableText(content)

        internal fun setVspace(builder: FormChoiceBuilder<*>, value: Boolean) {
            builder.vspace = value
        }
    }
}

/** Form-choice-ledetekst som ren tekst ([letterMarkup] uten variabler). */
fun <C : AbstractContentBuilder> FormChoiceBuilder<C>.prompt(text: String) =
    FormChoiceBuilder.addPrompt(this, FormChoiceBuilder.plainText(this, text))

/** Form-choice-ledetekst som ren tekst med `variable` ([letterMarkupWithVariables]). */
fun FormChoiceBuilder<VariableContentBuilder>.prompt(content: PlainVariableTextBuilder.() -> Unit) =
    FormChoiceBuilder.addPrompt(this, FormChoiceBuilder.plainVariableText(this, content))
