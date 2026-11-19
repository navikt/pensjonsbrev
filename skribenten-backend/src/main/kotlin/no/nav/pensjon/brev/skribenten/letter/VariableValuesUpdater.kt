package no.nav.pensjon.brev.skribenten.letter

import kotlin.collections.get

class VariableValuesUpdater(val variableValues: Map<Int, String>) {
    constructor(variableValues: Edit.Letter) : this(variableValues.variablesValueMap())
    constructor(variableValues: Edit.Attachment) : this(variableValues.variablesValueMap())

    private fun updateVariableValues(edited: Edit.Block): Edit.Block =
        when (edited) {
            is Edit.Block.Title1 -> edited.copy(content = edited.content.map { updateVariableValues(it) })
            is Edit.Block.Title2 -> edited.copy(content = edited.content.map { updateVariableValues(it) })
            is Edit.Block.Title3 -> edited.copy(content = edited.content.map { updateVariableValues(it) })
            is Edit.Block.Paragraph -> edited.copy(content = edited.content.map { updateVariableValues(it) })
        }

    private fun updateVariableValues(content: Edit.ParagraphContent): Edit.ParagraphContent =
        when (content) {
            is Edit.ParagraphContent.ItemList -> updateVariableValues(content)
            is Edit.ParagraphContent.Table -> updateVariableValues(content)
            is Edit.ParagraphContent.Text -> updateVariableValues(content)
        }

    private fun updateVariableValues(content: Edit.ParagraphContent.Text): Edit.ParagraphContent.Text =
        when (content) {
            is Edit.ParagraphContent.Text.Literal -> content
            is Edit.ParagraphContent.Text.Variable -> variableValues[content.id]
                ?.let { content.copy(text = it) }
                ?: Edit.ParagraphContent.Text.Literal(
                    id = content.id,
                    text = content.text,
                    fontType = content.fontType,
                    parentId = content.parentId
                )

            is Edit.ParagraphContent.Text.NewLine -> content
        }

    private fun updateVariableValues(itemList: Edit.ParagraphContent.ItemList): Edit.ParagraphContent.ItemList =
        itemList.copy(items = itemList.items.map(::updateVariableValues))

    private fun updateVariableValues(item: Edit.ParagraphContent.ItemList.Item): Edit.ParagraphContent.ItemList.Item =
        item.copy(content = item.content.map(::updateVariableValues))

    private fun updateVariableValues(table: Edit.ParagraphContent.Table): Edit.ParagraphContent.Table =
        table.copy(
            header = updateVariableValues(table.header),
            rows = table.rows.map(::updateVariableValues),
        )

    private fun updateVariableValues(header: Edit.ParagraphContent.Table.Header): Edit.ParagraphContent.Table.Header =
        header.copy(colSpec = header.colSpec.map(::updateVariableValues))

    private fun updateVariableValues(columnSpec: Edit.ParagraphContent.Table.ColumnSpec): Edit.ParagraphContent.Table.ColumnSpec =
        columnSpec.copy(headerContent = updateVariableValues(columnSpec.headerContent))

    private fun updateVariableValues(row: Edit.ParagraphContent.Table.Row): Edit.ParagraphContent.Table.Row =
        row.copy(cells = row.cells.map(::updateVariableValues))

    private fun updateVariableValues(cell: Edit.ParagraphContent.Table.Cell): Edit.ParagraphContent.Table.Cell =
        cell.copy(text = cell.text.map(::updateVariableValues))

    @Suppress("UNCHECKED_CAST")
    fun <E : Edit.Identifiable> withUpdatedVariables(element: E): E =
        when (element) {
            is Edit.Block -> updateVariableValues(element)
            is Edit.ParagraphContent -> updateVariableValues(element)
            is Edit.ParagraphContent.ItemList.Item -> updateVariableValues(element)
            is Edit.ParagraphContent.Table.Header -> updateVariableValues(element)
            is Edit.ParagraphContent.Table.ColumnSpec -> updateVariableValues(element)
            is Edit.ParagraphContent.Table.Row -> updateVariableValues(element)
            is Edit.ParagraphContent.Table.Cell -> updateVariableValues(element)
        } as E

}