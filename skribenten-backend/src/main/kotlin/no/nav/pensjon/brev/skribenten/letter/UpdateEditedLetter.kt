package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.LetterMarkup

class UpdateEditedLetterException(message: String) : RuntimeException(message)

/**
 * Update a letter edited in Skribenten (originally rendered by brevbaker) with a fresh
 * rendering from brevbaker. In the new rendering from brevbaker we may receive elements (blocks/content)
 * that wasn't present in previous renders, e.g. if Saksbehandler has modified the template options (saksbehandlerValg) or
 * if Sak-data has changed in pesys. Or elements (blocks/content) present in previous renders may no longer be
 * present.
 */
fun Edit.Letter.updateEditedLetter(renderedLetter: LetterMarkup): Edit.Letter =
    renderedLetter.toEdit().let { renderedAsEdit ->
        val variableValues = VariableValuesVisitor(renderedAsEdit).build()
        copy(
            title = renderedLetter.title,
            sakspart = renderedLetter.sakspart,
            signatur = renderedLetter.signatur,
            blocks = mergeList(blocks, renderedAsEdit.blocks, deletedBlocks, ::mergeBlock) { updateVariableValues(it, variableValues) },
            deletedBlocks = deletedBlocks.filter { id -> renderedLetter.blocks.any { it.id == id } }.toSet(),
        )
    }

/**
 * Merges a list of [edited] elements with a list of freshly [rendered] elements.
 * For each edited element we attempt to find a corresponding rendered element, and if we find it they are merged using the provided [merge]-function.
 * Both lists may contain elements not present in the other, and we try to zip the lists together based on elements present in both. The edited-list
 * may contain new elements, e.g. a new paragraph not present in the template or elements no longer present in the fresh render. While the rendered-list
 * may contain elements that weren't included in the previous render.
 *
 * Example 1: No edits other than new elements.
 * ```
 *    edited:   [A, C, New1, E, New2]
 *    rendered: [A, B, D, E]
 *    result:   [A, New1, D, E, New2]
 * ```
 *
 * Example 2: Edited elements marked with ', e.g. A'.
 * ```
 *    edited    [A, B', D]
 *    rendered: [A, B, C, D]
 *    result:   [A, B', C, D]
 * ```
 */
private fun <E : Edit.Identifiable> mergeList(
    edited: List<E>,
    rendered: List<E>,
    deleted: Set<Int>,
    merge: (E, E) -> E,
    updateVariables: ((E) -> E)? = null,
): List<E> = buildList {
    // A queue of unprocessed rendered elements
    val remainingRendered = rendered.filter { it.id != null && !deleted.contains(it.id) }.toMutableList()

    // We zip-merge the two lists with edited as basis, then we pick matching elements of remainingRendered.
    edited.forEach { currentEdited ->
        // If the currentEdited element is new, i.e. was added manually by Saksbehandler.
        if (currentEdited.isNew()) {
            add(updateVariables?.invoke(currentEdited) ?: currentEdited)
        } else {
            val renderedIndex = remainingRendered.indexOfFirst { it.id == currentEdited.id }

            if (renderedIndex >= 0) {
                // The currentEdited element is present in the fresh render.

                // We add any fresh elements from the fresh render that precedes currentEdited in the fresh render.
                for (ind in 0 until renderedIndex) {
                    add(remainingRendered.removeFirst())
                }

                // If the currentEdited element actually has any edits we merge them, otherwise we simply pick the rendered one.
                if (currentEdited.isEdited()) {
                    add(merge(currentEdited, remainingRendered.removeFirst()))
                } else {
                    add(remainingRendered.removeFirst())
                }
            } else if (currentEdited.isEdited()) {
                // The currentEdited element is not present in the fresh render, but it is edited by the Saksbehandler.
                // We include it so that no potentially important text is lost.
                // TODO dette elementet er ikke lenger med i rendring, vurdere om vi skal annotere det på et vis eller noe (slik at det kan vises til saksbehandler).
                add(currentEdited)
            }
        }
    }
    addAll(remainingRendered)
}

private fun mergeBlock(edited: Edit.Block, rendered: Edit.Block): Edit.Block =
    when (edited) {
        is Edit.Block.Paragraph -> edited.copy(content = mergeList(edited.content, rendered.content, edited.deletedContent, ::mergeParagraphContent))
        is Edit.Block.Title1 -> edited.copy(content = mergeListText(edited.content, rendered, edited.deletedContent))
        is Edit.Block.Title2 -> edited.copy(content = mergeListText(edited.content, rendered, edited.deletedContent))
    }

private fun mergeListText(edited: List<Edit.ParagraphContent.Text>, rendered: Edit.Block, deleted: Set<Int>): List<Edit.ParagraphContent.Text> =
    when (rendered) {
        is Edit.Block.Title1 -> mergeList(edited, rendered.content, deleted, ::mergeTextContent)
        is Edit.Block.Title2 -> mergeList(edited, rendered.content, deleted, ::mergeTextContent)
        is Edit.Block.Paragraph -> mergeList(edited, rendered.content.filterIsInstance<Edit.ParagraphContent.Text>(), deleted, ::mergeTextContent)
    }

private fun mergeTextContent(edited: Edit.ParagraphContent.Text, rendered: Edit.ParagraphContent.Text): Edit.ParagraphContent.Text =
    when (edited) {
        is Edit.ParagraphContent.Text.Literal -> when (rendered) {
            is Edit.ParagraphContent.Text.Literal -> rendered.copy(editedText = edited.editedText, editedFontType = edited.editedFontType)
            is Edit.ParagraphContent.Text.Variable -> throw UpdateEditedLetterException("Edited literal and rendered variable has same ID, cannot merge: $edited - $rendered")
        }

        is Edit.ParagraphContent.Text.Variable -> throw UpdateEditedLetterException("Variable should never be considered edited: $edited")
    }

private fun mergeParagraphContent(edited: Edit.ParagraphContent, rendered: Edit.ParagraphContent): Edit.ParagraphContent =
    when (edited) {
        is Edit.ParagraphContent.ItemList ->
            if (rendered is Edit.ParagraphContent.ItemList) {
                edited.copy(items = mergeList(edited.items, rendered.items, edited.deletedItems, ::mergeItems))
            } else {
                throw UpdateEditedLetterException("Cannot merge ${edited.type} with ${rendered.type}: $edited - $rendered")
            }

        is Edit.ParagraphContent.Text ->
            if (rendered is Edit.ParagraphContent.Text) {
                mergeTextContent(edited, rendered)
            } else {
                throw UpdateEditedLetterException("Cannot merge ${edited.type} with ${rendered.type}: $edited - $rendered")
            }

        is Edit.ParagraphContent.Table ->
            if (rendered is Edit.ParagraphContent.Table) {
                edited.copy(
                    header = mergeTableHeader(edited.header, rendered.header),
                    rows = mergeList(edited.rows, rendered.rows, rendered.deletedRows, ::mergeRows)
                )
            } else {
                throw UpdateEditedLetterException("Cannot merge ${edited.type} with ${rendered.type}: $edited - $rendered")
            }
    }

private fun mergeTableHeader(edited: Edit.ParagraphContent.Table.Header, rendered: Edit.ParagraphContent.Table.Header): Edit.ParagraphContent.Table.Header =
    edited.copy(colSpec = mergeList(edited.colSpec, rendered.colSpec, emptySet(), ::mergeColumnSpec))

private fun mergeColumnSpec(edited: Edit.ParagraphContent.Table.ColumnSpec, rendered: Edit.ParagraphContent.Table.ColumnSpec): Edit.ParagraphContent.Table.ColumnSpec =
    edited.copy(headerContent = mergeCell(edited.headerContent, rendered.headerContent))

private fun mergeCell(edited: Edit.ParagraphContent.Table.Cell, rendered: Edit.ParagraphContent.Table.Cell): Edit.ParagraphContent.Table.Cell =
    edited.copy(text = mergeList(edited.text, rendered.text, emptySet(), ::mergeTextContent))

private fun mergeRows(edited: Edit.ParagraphContent.Table.Row, rendered: Edit.ParagraphContent.Table.Row): Edit.ParagraphContent.Table.Row =
    edited.copy(cells = mergeList(edited.cells, rendered.cells, emptySet(), ::mergeCell))

private fun mergeItems(edited: Edit.ParagraphContent.ItemList.Item, rendered: Edit.ParagraphContent.ItemList.Item): Edit.ParagraphContent.ItemList.Item =
    edited.copy(content = mergeList(edited.content, rendered.content, emptySet(), ::mergeTextContent))

private fun updateVariableValues(edited: Edit.Block, variableValues: Map<Int, String>): Edit.Block =
    when (edited) {
        is Edit.Block.Title1 -> edited.copy(content = edited.content.map { updateVariableValues(it, variableValues) })
        is Edit.Block.Title2 -> edited.copy(content = edited.content.map { updateVariableValues(it, variableValues) })
        is Edit.Block.Paragraph -> edited.copy(content = edited.content.map { updateVariableValues(it, variableValues) })
    }

private fun updateVariableValues(content: Edit.ParagraphContent, variableValues: Map<Int, String>): Edit.ParagraphContent =
    when (content) {
        is Edit.ParagraphContent.ItemList -> updateVariableValues(content, variableValues)
        is Edit.ParagraphContent.Table -> updateVariableValues(content, variableValues)
        is Edit.ParagraphContent.Text -> updateVariableValues(content, variableValues)
    }

private fun updateVariableValues(content: Edit.ParagraphContent.Text, variableValues: Map<Int, String>): Edit.ParagraphContent.Text =
    when (content) {
        is Edit.ParagraphContent.Text.Literal -> content
        is Edit.ParagraphContent.Text.Variable -> variableValues[content.id]
            ?.let { content.copy(text = it) }
            ?: Edit.ParagraphContent.Text.Literal(content.id, content.text, content.fontType)
    }

private fun updateVariableValues(itemList: Edit.ParagraphContent.ItemList, variableValues: Map<Int, String>): Edit.ParagraphContent.ItemList =
    itemList.copy(items = itemList.items.map { item -> item.copy(content = item.content.map { updateVariableValues(it, variableValues) }) })

private fun updateVariableValues(table: Edit.ParagraphContent.Table, variableValues: Map<Int, String>): Edit.ParagraphContent.Table =
    table.copy(
        header = table.header.copy(colSpec = table.header.colSpec.map { it.copy(headerContent = updateVariableValues(it.headerContent, variableValues)) }),
        rows = table.rows.map { updateVariableValues(it, variableValues) },
    )

private fun updateVariableValues(row: Edit.ParagraphContent.Table.Row, variableValues: Map<Int, String>): Edit.ParagraphContent.Table.Row =
    row.copy(cells = row.cells.map { updateVariableValues(it, variableValues) })

private fun updateVariableValues(cell: Edit.ParagraphContent.Table.Cell, variableValues: Map<Int, String>): Edit.ParagraphContent.Table.Cell =
    cell.copy(text = cell.text.map { updateVariableValues(it, variableValues) })