package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment

@MarkupDsl
class OutlineBuilder internal constructor(private val ids: IdGenerator) {
    private val blocks = mutableListOf<Block>()

    fun title2(content: ContentBuilder.() -> Unit) {
        blocks.add(Block.Title2(ids.next(), ids.content(content)))
    }

    fun title3(content: ContentBuilder.() -> Unit) {
        blocks.add(Block.Title3(ids.next(), ids.content(content)))
    }

    fun title4(content: ContentBuilder.() -> Unit) {
        blocks.add(Block.Title4(ids.next(), ids.content(content)))
    }

    fun paragraph(content: ContentBuilder.() -> Unit) {
        blocks.add(Block.Paragraph(ids.next(), ids.content(content)))
    }

    fun itemList(build: ItemsBuilder.() -> Unit) {
        blocks.add(Block.ItemList(ids.next(), ItemsBuilder(ids).apply(build).build()))
    }

    fun numberedList(build: ItemsBuilder.() -> Unit) {
        blocks.add(Block.NumberedList(ids.next(), ItemsBuilder(ids).apply(build).build()))
    }

    fun table(build: TableBuilder.() -> Unit) {
        blocks.add(TableBuilder(ids).apply(build).build())
    }

    fun formText(size: Size, vspace: Boolean = true, prompt: ContentBuilder.() -> Unit) {
        blocks.add(Block.FormText(ids.next(), ids.content(prompt), size, vspace))
    }

    fun formChoice(vspace: Boolean = true, build: FormChoiceBuilder.() -> Unit) {
        blocks.add(FormChoiceBuilder(ids).withVspace(vspace).apply(build).build())
    }

    internal fun build(): List<Block> = blocks.toList()
}

@MarkupDsl
class ItemsBuilder internal constructor(private val ids: IdGenerator) {
    private val items = mutableListOf<Block.Item>()

    fun item(content: ContentBuilder.() -> Unit) {
        items.add(Block.Item(ids.next(), ids.content(content)))
    }

    internal fun build(): List<Block.Item> = items.toList()
}

@MarkupDsl
class TableBuilder internal constructor(private val ids: IdGenerator) {
    private var header: Block.Table.Header? = null
    private val rows = mutableListOf<Block.Table.Row>()

    fun header(build: HeaderBuilder.() -> Unit) {
        header = HeaderBuilder(ids).apply(build).build()
    }

    fun row(build: RowBuilder.() -> Unit) {
        rows.add(RowBuilder(ids).apply(build).build())
    }

    internal fun build(): Block.Table {
        val id = ids.next()
        return Block.Table(
            id = id,
            rows = rows.toList(),
            header = requireNotNull(header) { "Table must have a header" },
        )
    }
}

@MarkupDsl
class HeaderBuilder internal constructor(private val ids: IdGenerator) {
    private val colSpec = mutableListOf<Block.Table.ColumnSpec>()

    fun column(alignment: ColumnAlignment = ColumnAlignment.LEFT, span: Int = 1, content: ContentBuilder.() -> Unit) {
        val cell = Block.Table.Cell(ids.next(), ids.content(content))
        colSpec.add(Block.Table.ColumnSpec(ids.next(), cell, alignment, span))
    }

    internal fun build(): Block.Table.Header = Block.Table.Header(ids.next(), colSpec.toList())
}

@MarkupDsl
class RowBuilder internal constructor(private val ids: IdGenerator) {
    private val cells = mutableListOf<Block.Table.Cell>()

    fun cell(content: ContentBuilder.() -> Unit) {
        cells.add(Block.Table.Cell(ids.next(), ids.content(content)))
    }

    internal fun build(): Block.Table.Row = Block.Table.Row(ids.next(), cells.toList())
}

@MarkupDsl
class FormChoiceBuilder internal constructor(private val ids: IdGenerator) {
    private var vspace: Boolean = true
    private val prompt = mutableListOf<no.nav.brev.brevbaker.markup.outline.Text>()
    private val choices = mutableListOf<Block.FormChoice.Choice>()

    fun prompt(content: ContentBuilder.() -> Unit) {
        prompt.addAll(ids.content(content))
    }

    fun choice(content: ContentBuilder.() -> Unit) {
        choices.add(Block.FormChoice.Choice(ids.next(), ids.content(content)))
    }

    internal fun withVspace(value: Boolean): FormChoiceBuilder {
        vspace = value
        return this
    }

    internal fun build(): Block.FormChoice =
        Block.FormChoice(ids.next(), prompt.toList(), choices.toList(), vspace)
}
