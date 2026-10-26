package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.LetterMarkup

/**
 * Updates the values of variables already present in the letter with values from a fresh rendering,
 * without merging any structural changes (added/removed blocks or content) from the rendering.
 * Variables not present in the fresh rendering keep their previous value.
 */
fun Edit.Letter.updateVariables(renderedLetter: LetterMarkup): Edit.Letter =
    UpdateVariables(renderedLetter.toEdit().allVariablesValueMap()).updateLetter(this)

/**
 * IDs of variables in this letter (including title, lists and tables) that have no value in the fresh rendering,
 * i.e. variables whose value cannot be updated.
 */
fun Edit.Letter.variablesMissingIn(renderedLetter: LetterMarkup): Set<Int> =
    allVariablesValueMap().keys - renderedLetter.toEdit().allVariablesValueMap().keys

private fun Edit.Letter.allVariablesValueMap(): Map<Int, String> =
    variablesValueMap() + title.variablesValueMap()

/**
 * Same as [Edit.Letter.updateVariables], but for an edited attachment.
 */
fun Edit.Attachment.updateVariables(renderedAttachment: LetterMarkup.Attachment): Edit.Attachment =
    renderedAttachment.toEdit().let { rendered ->
        UpdateVariables(rendered.variablesValueMap() + rendered.title.variablesValueMap()).updateAttachment(this)
    }

private fun Edit.Title.variablesValueMap(): Map<Int, String> =
    text.filterIsInstance<Edit.ParagraphContent.Text.Variable>().mapNotNull { v -> v.id?.let { it to v.text } }.toMap()


internal class UpdateVariables(
    private val variableValues: Map<Int, String>,
    private val missingVariableAsLiteral: Boolean = false,
) {
    fun updateLetter(letter: Edit.Letter): Edit.Letter =
        letter.copy(
            title = update(letter.title),
            blocks = letter.blocks.map(::update),
        )

    fun updateAttachment(attachment: Edit.Attachment): Edit.Attachment =
        attachment.copy(
            title = update(attachment.title),
            blocks = attachment.blocks.map(::update),
        )

    fun update(title: Edit.Title): Edit.Title =
        title.copy(text = title.text.map(::update))

    fun update(block: Edit.Block): Edit.Block =
        when (block) {
            is Edit.Block.Title1 -> block.copy(content = block.content.map(::update))
            is Edit.Block.Title2 -> block.copy(content = block.content.map(::update))
            is Edit.Block.Title3 -> block.copy(content = block.content.map(::update))
            is Edit.Block.Paragraph -> block.copy(content = block.content.map(::update))
        }

    fun update(content: Edit.ParagraphContent): Edit.ParagraphContent =
        when (content) {
            is Edit.ParagraphContent.Text -> update(content)
            is Edit.ParagraphContent.ItemList -> update(content)
            is Edit.ParagraphContent.Table -> update(content)
        }

    fun update(itemList: Edit.ParagraphContent.ItemList): Edit.ParagraphContent.ItemList =
        itemList.copy(items = itemList.items.map(::update))

    fun update(item: Edit.ParagraphContent.ItemList.Item): Edit.ParagraphContent.ItemList.Item =
        item.copy(content = item.content.map(::update))

    fun update(table: Edit.ParagraphContent.Table): Edit.ParagraphContent.Table =
        table.copy(
            header = table.header.copy(colSpec = table.header.colSpec.map(::update)),
            rows = table.rows.map(::update),
        )

    fun update(columnSpec: Edit.ParagraphContent.Table.ColumnSpec): Edit.ParagraphContent.Table.ColumnSpec =
        columnSpec.copy(headerContent = update(columnSpec.headerContent))

    fun update(row: Edit.ParagraphContent.Table.Row): Edit.ParagraphContent.Table.Row =
        row.copy(cells = row.cells.map(::update))

    fun update(cell: Edit.ParagraphContent.Table.Cell): Edit.ParagraphContent.Table.Cell =
        cell.copy(text = cell.text.map(::update))

    fun update(text: Edit.ParagraphContent.Text): Edit.ParagraphContent.Text =
        when (text) {
            is Edit.ParagraphContent.Text.Variable -> text.id?.let(variableValues::get)?.let { text.copy(text = it) }
                ?: if (missingVariableAsLiteral) {
                    Edit.ParagraphContent.Text.Literal(
                        id = text.id,
                        text = text.text,
                        fontType = text.fontType,
                        parentId = text.parentId,
                    )
                } else {
                    text
                }
            is Edit.ParagraphContent.Text.Literal, is Edit.ParagraphContent.Text.NewLine -> text
        }
}