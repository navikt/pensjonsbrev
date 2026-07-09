package no.nav.brev.brevbaker.markup.elements

import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Block.FormText.Size
import no.nav.brev.brevbaker.markup.outline.Block.Table.ColumnAlignment
import no.nav.brev.brevbaker.markup.outline.Text

/** Bygg en frittstående [Block.Title2]. */
fun title2Element(id: Int = 0, content: ElementContentScope.() -> Unit): Block.Title2 =
    Block.Title2(id, elementContent(content))

/** Bygg en frittstående [Block.Title3]. */
fun title3Element(id: Int = 0, content: ElementContentScope.() -> Unit): Block.Title3 =
    Block.Title3(id, elementContent(content))

/** Bygg en frittstående [Block.Title4]. */
fun title4Element(id: Int = 0, content: ElementContentScope.() -> Unit): Block.Title4 =
    Block.Title4(id, elementContent(content))

/** Bygg en frittstående [Block.Paragraph]. */
fun paragraphElement(id: Int = 0, content: ElementContentScope.() -> Unit): Block.Paragraph =
    Block.Paragraph(id, elementContent(content))

/** Bygg et frittstående [Block.Item]. */
fun itemElement(id: Int = 0, content: ElementContentScope.() -> Unit): Block.Item =
    Block.Item(id, elementContent(content))

/** Bygg en frittstående [Block.ItemList]. */
fun itemListElement(id: Int = 0, build: ElementItemsScope.() -> Unit): Block.ItemList =
    Block.ItemList(id, ElementItemsScope().apply(build).build())

/** Bygg en frittstående [Block.NumberedList]. */
fun numberedListElement(id: Int = 0, build: ElementItemsScope.() -> Unit): Block.NumberedList =
    Block.NumberedList(id, ElementItemsScope().apply(build).build())

/** Bygg en frittstående [Block.Table.Cell]. */
fun cellElement(id: Int = 0, content: ElementContentScope.() -> Unit): Block.Table.Cell =
    Block.Table.Cell(id, elementContent(content))

/** Bygg en frittstående [Block.Table.Row]. */
fun rowElement(id: Int = 0, build: ElementRowScope.() -> Unit): Block.Table.Row =
    Block.Table.Row(id, ElementRowScope().apply(build).build())

/** Bygg en frittstående [Block.Table.ColumnSpec]. */
fun columnSpecElement(
    id: Int = 0,
    alignment: ColumnAlignment = ColumnAlignment.LEFT,
    span: Int = 1,
    cellId: Int = 0,
    content: ElementContentScope.() -> Unit,
): Block.Table.ColumnSpec =
    Block.Table.ColumnSpec(id, Block.Table.Cell(cellId, elementContent(content)), alignment, span)

/** Bygg en frittstående [Block.Table.Header]. */
fun headerElement(id: Int = 0, build: ElementHeaderScope.() -> Unit): Block.Table.Header =
    Block.Table.Header(id, ElementHeaderScope().apply(build).build())

/** Bygg en frittstående [Block.Table]. */
fun tableElement(id: Int = 0, build: ElementTableScope.() -> Unit): Block.Table =
    ElementTableScope(id).apply(build).build()

/** Bygg en frittstående [Block.FormText]. */
fun formTextElement(
    size: Size,
    id: Int = 0,
    vspace: Boolean = true,
    prompt: ElementContentScope.() -> Unit,
): Block.FormText = Block.FormText(id, elementContent(prompt), size, vspace)

/** Bygg en frittstående [Block.FormChoice.Choice]. */
fun choiceElement(id: Int = 0, content: ElementContentScope.() -> Unit): Block.FormChoice.Choice =
    Block.FormChoice.Choice(id, elementContent(content))

/** Bygg en frittstående [Block.FormChoice]. */
fun formChoiceElement(id: Int = 0, vspace: Boolean = true, build: ElementFormChoiceScope.() -> Unit): Block.FormChoice =
    ElementFormChoiceScope(id, vspace).apply(build).build()

@MarkupElementsDsl
class ElementItemsScope internal constructor() {
    private val items = mutableListOf<Block.Item>()

    fun item(id: Int = 0, content: ElementContentScope.() -> Unit) {
        items.add(Block.Item(id, elementContent(content)))
    }

    internal fun build(): List<Block.Item> = items.toList()
}

@MarkupElementsDsl
class ElementRowScope internal constructor() {
    private val cells = mutableListOf<Block.Table.Cell>()

    fun cell(id: Int = 0, content: ElementContentScope.() -> Unit) {
        cells.add(Block.Table.Cell(id, elementContent(content)))
    }

    internal fun build(): List<Block.Table.Cell> = cells.toList()
}

@MarkupElementsDsl
class ElementHeaderScope internal constructor() {
    private val colSpec = mutableListOf<Block.Table.ColumnSpec>()

    fun column(
        id: Int = 0,
        alignment: ColumnAlignment = ColumnAlignment.LEFT,
        span: Int = 1,
        cellId: Int = 0,
        content: ElementContentScope.() -> Unit,
    ) {
        colSpec.add(Block.Table.ColumnSpec(id, Block.Table.Cell(cellId, elementContent(content)), alignment, span))
    }

    internal fun build(): List<Block.Table.ColumnSpec> = colSpec.toList()
}

@MarkupElementsDsl
class ElementTableScope internal constructor(private val id: Int) {
    private var header: Block.Table.Header = Block.Table.Header(0, emptyList())
    private val rows = mutableListOf<Block.Table.Row>()

    fun header(id: Int = 0, build: ElementHeaderScope.() -> Unit) {
        header = Block.Table.Header(id, ElementHeaderScope().apply(build).build())
    }

    fun row(id: Int = 0, build: ElementRowScope.() -> Unit) {
        rows.add(Block.Table.Row(id, ElementRowScope().apply(build).build()))
    }

    internal fun build(): Block.Table = Block.Table(id, rows.toList(), header)
}

@MarkupElementsDsl
class ElementFormChoiceScope internal constructor(private val id: Int, private val vspace: Boolean) {
    private val prompt = mutableListOf<Text>()
    private val choices = mutableListOf<Block.FormChoice.Choice>()

    fun prompt(content: ElementContentScope.() -> Unit) {
        prompt.addAll(elementContent(content))
    }

    fun choice(id: Int = 0, content: ElementContentScope.() -> Unit) {
        choices.add(Block.FormChoice.Choice(id, elementContent(content)))
    }

    internal fun build(): Block.FormChoice = Block.FormChoice(id, prompt.toList(), choices.toList(), vspace)
}
