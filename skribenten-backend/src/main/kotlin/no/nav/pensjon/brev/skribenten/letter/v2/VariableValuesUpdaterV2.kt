package no.nav.pensjon.brev.skribenten.letter.v2

import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Block
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Block.Table
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Item
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Text

internal class VariableValuesUpdaterV2(private val variableValues: Map<Int, String>) {
    constructor(variableValues: EditV2.Letter) : this(variableValues.variablesValueMap())
    constructor(variableValues: EditV2.Attachment) : this(variableValues.variablesValueMap())

    private fun updateVariableValues(block: Block): Block =
        when (block) {
            is Block.Title2 -> block.copy(content = block.content.map(::updateVariableValues))
            is Block.Title3 -> block.copy(content = block.content.map(::updateVariableValues))
            is Block.Title4 -> block.copy(content = block.content.map(::updateVariableValues))
            is Block.Paragraph -> block.copy(content = block.content.map(::updateVariableValues))
            is Block.ItemList -> block.copy(items = block.items.map(::updateVariableValues))
            is Block.NumberedList -> block.copy(items = block.items.map(::updateVariableValues))
            is Table -> updateVariableValues(block)
        }

    private fun updateVariableValues(text: Text): Text =
        when (text) {
            is Text.Literal -> text
            is Text.Variable -> variableValues[text.id]
                ?.let { text.copy(text = it) }
                ?: Text.Literal(
                    id = text.id,
                    text = text.text,
                    fontType = text.fontType,
                    parentId = text.parentId,
                )

            is Text.NewLine -> text
        }

    private fun updateVariableValues(item: Item): Item =
        item.copy(content = item.content.map(::updateVariableValues))

    private fun updateVariableValues(table: Table): Table =
        table.copy(
            header = updateVariableValues(table.header),
            rows = table.rows.map(::updateVariableValues),
        )

    private fun updateVariableValues(header: Table.Header): Table.Header =
        header.copy(colSpec = header.colSpec.map(::updateVariableValues))

    private fun updateVariableValues(columnSpec: Table.ColumnSpec): Table.ColumnSpec =
        columnSpec.copy(content = columnSpec.content.map(::updateVariableValues))

    private fun updateVariableValues(row: Table.Row): Table.Row =
        row.copy(cells = row.cells.map(::updateVariableValues))

    private fun updateVariableValues(cell: Table.Cell): Table.Cell =
        cell.copy(content = cell.content.map(::updateVariableValues))

    @Suppress("UNCHECKED_CAST")
    fun <E : EditV2.Identifiable> withUpdatedVariables(element: E): E =
        when (element) {
            is Block -> updateVariableValues(element)
            is Text -> updateVariableValues(element)
            is Item -> updateVariableValues(element)
            is Table.Header -> updateVariableValues(element)
            is Table.ColumnSpec -> updateVariableValues(element)
            is Table.Row -> updateVariableValues(element)
            is Table.Cell -> updateVariableValues(element)
        } as E
}
