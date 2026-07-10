package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import no.nav.brev.brevbaker.markup.outline.Text

@MarkupDsl
class OutlineBuilder<C : AbstractContentBuilder> internal constructor(
    private val ids: IdGenerator,
    private val contentFactory: ContentFactory<C>,
) {
    private val blocks = mutableListOf<Block>()

    fun title2(content: C.() -> Unit) {
        blocks.add(Block.Title2(ids.next(), ids.content(contentFactory, content)))
    }

    fun title3(content: C.() -> Unit) {
        blocks.add(Block.Title3(ids.next(), ids.content(contentFactory, content)))
    }

    fun title4(content: C.() -> Unit) {
        blocks.add(Block.Title4(ids.next(), ids.content(contentFactory, content)))
    }

    fun paragraph(content: C.() -> Unit) {
        blocks.add(Block.Paragraph(ids.next(), ids.content(contentFactory, content)))
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

    fun formChoice(vspace: Boolean = true, build: FormChoiceBuilder<C>.() -> Unit) {
        val formChoiceBuilder = FormChoiceBuilder(ids, contentFactory)
        FormChoiceBuilder.setVspace(formChoiceBuilder, vspace)
        blocks.add(formChoiceBuilder.apply(build).build())
    }

    internal fun build(): List<Block> = blocks.toList()

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

        internal fun plainVariableText(
            builder: OutlineBuilder<*>,
            content: PlainVariableTextBuilder.() -> Unit,
        ): List<Text> = builder.ids.plainVariableText(content)
    }
}

fun OutlineBuilder<ContentBuilder>.title2(text: String) =
    OutlineBuilder.addTitle2(this, OutlineBuilder.plainText(this, text))

fun OutlineBuilder<VariableContentBuilder>.title2(content: PlainVariableTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle2(this, OutlineBuilder.plainVariableText(this, content))

fun OutlineBuilder<ContentBuilder>.title3(text: String) =
    OutlineBuilder.addTitle3(this, OutlineBuilder.plainText(this, text))

fun OutlineBuilder<VariableContentBuilder>.title3(content: PlainVariableTextBuilder.() -> Unit) =
    OutlineBuilder.addTitle3(this, OutlineBuilder.plainVariableText(this, content))

fun OutlineBuilder<ContentBuilder>.title4(text: String) =
    OutlineBuilder.addTitle4(this, OutlineBuilder.plainText(this, text))

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

        internal fun plainVariableText(
            builder: HeaderBuilder<*>,
            content: PlainVariableTextBuilder.() -> Unit,
        ): List<Text> = builder.ids.plainVariableText(content)
    }
}

/** Kolonneoverskrift som ren tekst ([letterMarkup] uten variabler). */
fun HeaderBuilder<ContentBuilder>.column(
    text: String,
    alignment: ColumnAlignment = ColumnAlignment.LEFT,
    span: Int = 1,
) = HeaderBuilder.addColumn(this, alignment, span, HeaderBuilder.plainText(this, text))

/** Kolonneoverskrift som ren tekst med `variable` ([lettermarkupExtended]). */
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

    fun cell(text: String) {
        cells.add(Block.Table.Cell(ids.next(), ids.plainText(text)))
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
fun FormChoiceBuilder<ContentBuilder>.prompt(text: String) =
    FormChoiceBuilder.addPrompt(this, FormChoiceBuilder.plainText(this, text))

/** Form-choice-ledetekst som ren tekst med `variable` ([lettermarkupExtended]). */
fun FormChoiceBuilder<VariableContentBuilder>.prompt(content: PlainVariableTextBuilder.() -> Unit) =
    FormChoiceBuilder.addPrompt(this, FormChoiceBuilder.plainVariableText(this, content))
