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
        blocks.add(FormChoiceBuilder(ids, contentFactory).withVspace(vspace).apply(build).build())
    }

    internal fun build(): List<Block> = blocks.toList()
}

@MarkupDsl
class ItemsBuilder<C : AbstractContentBuilder> internal constructor(
    private val ids: IdGenerator,
    private val contentFactory: ContentFactory<C>,
) {
    private val items = mutableListOf<Block.Item>()

    fun item(content: C.() -> Unit) {
        items.add(Block.Item(ids.next(), ids.content(contentFactory, content)))
    }

    internal fun build(): List<Block.Item> = items.toList()
}

@MarkupDsl
class TableBuilder<C : AbstractContentBuilder> internal constructor(
    private val ids: IdGenerator,
    private val contentFactory: ContentFactory<C>,
) {
    private var header: Block.Table.Header? = null
    private val rows = mutableListOf<Block.Table.Row>()

    fun header(build: HeaderBuilder<C>.() -> Unit) {
        header = HeaderBuilder(ids, contentFactory).apply(build).build()
    }

    fun row(build: RowBuilder<C>.() -> Unit) {
        rows.add(RowBuilder(ids, contentFactory).apply(build).build())
    }

    internal fun build(): Block.Table {
        val id = ids.next()
        val header = requireNotNull(header) { "Table must have a header" }
        val columnCount = header.colSpec.size
        rows.forEachIndexed { index, row ->
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
    private val contentFactory: ContentFactory<C>,
) {
    private val colSpec = mutableListOf<Block.Table.ColumnSpec>()

    fun column(alignment: ColumnAlignment = ColumnAlignment.LEFT, span: Int = 1, content: C.() -> Unit) {
        val cell = Block.Table.Cell(ids.next(), ids.content(contentFactory, content))
        colSpec.add(Block.Table.ColumnSpec(ids.next(), cell, alignment, span))
    }

    internal fun build(): Block.Table.Header = Block.Table.Header(ids.next(), colSpec.toList())
}

@MarkupDsl
class RowBuilder<C : AbstractContentBuilder> internal constructor(
    private val ids: IdGenerator,
    private val contentFactory: ContentFactory<C>,
) {
    private val cells = mutableListOf<Block.Table.Cell>()

    fun cell(content: C.() -> Unit) {
        cells.add(Block.Table.Cell(ids.next(), ids.content(contentFactory, content)))
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

    fun prompt(content: C.() -> Unit) {
        prompt.addAll(ids.content(contentFactory, content))
    }

    fun choice(content: C.() -> Unit) {
        choices.add(Block.FormChoice.Choice(ids.next(), ids.content(contentFactory, content)))
    }

    internal fun withVspace(value: Boolean): FormChoiceBuilder<C> {
        vspace = value
        return this
    }

    internal fun build(): Block.FormChoice =
        Block.FormChoice(ids.next(), prompt.toList(), choices.toList(), vspace)
}
