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
    UpdateEditedLetter(this, renderedLetter).build()


// TODO: Fjern `parentId = edited.parentId ?: rendered.parentId` fra alle `edited.copy` kall. De er kun lagt til for å oppdatere redigerte brev i databasen.

class UpdateEditedLetter(private val edited: Edit.Letter, rendered: LetterMarkup) {
    private val rendered = rendered.toEdit()
    private val variableValues = this.rendered.variablesValueMap()

    fun build(): Edit.Letter =
        edited.copy(
            title = rendered.title,
            sakspart = rendered.sakspart,
            signatur = rendered.signatur,
            blocks = mergeList(null, edited.blocks, rendered.blocks, edited.deletedBlocks, ::mergeBlock, ::updateVariableValues),
            deletedBlocks = edited.deletedBlocks.filter { id -> rendered.blocks.any { it.id == id } }.toSet(),
        )

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
        parent: Edit.Identifiable?,
        edited: List<E>,
        rendered: List<E>,
        deleted: Set<Int>,
        merge: (E, E) -> E,
        updateVariables: ((E) -> E),
    ): List<E> = buildList {
        // A queue of unprocessed rendered elements
        val remainingRendered = rendered.filter { it.id != null && !deleted.contains(it.id) }.toMutableList()

        // We zip-merge the two lists with edited as basis, then we pick matching elements of remainingRendered.
        edited.forEach { currentEdited ->
            // If the currentEdited element is new, i.e. was added manually by Saksbehandler.
            if (currentEdited.isNew()) {
                add(updateVariables(currentEdited))
            } else {
                val renderedIndex = remainingRendered.indexOfFirst { it.id == currentEdited.id }

                if (renderedIndex >= 0) {
                    // The currentEdited element is present in the fresh render.

                    // We add any new elements from the fresh render that precedes currentEdited in the fresh render.
                    (0 until renderedIndex).forEach { add(remainingRendered.removeFirst()) }

                    // If the currentEdited element actually has any edits we merge them, otherwise we simply pick the rendered one.
                    if (currentEdited.isEdited()) {
                        add(merge(currentEdited, remainingRendered.removeFirst()))
                    } else {
                        add(remainingRendered.removeFirst())
                    }
                } else if (currentEdited.isEdited()) {
                    // The currentEdited element is not present in the fresh render, but it is edited by the Saksbehandler.
                    // We include it so that no potentially important text is lost.
                    // TODO: dette elementet er ikke lenger med i rendring, vurdere om vi skal annotere det på et vis eller noe (slik at det kan vises til saksbehandler).
                    add(updateVariables(currentEdited))
                } else if (currentEdited.parentId != parent?.id) {
                    // The currentEdited element is moved to another parent, and thus cannot currently be tracked.
                    // But it is moved by intent, so we do not wish to remove it.
                    add(updateVariables(currentEdited))
                }
            }
        }
        addAll(remainingRendered)
    }

    private fun mergeBlock(edited: Edit.Block, rendered: Edit.Block): Edit.Block =
        when (edited) {
            is Edit.Block.Paragraph -> edited.copy(
                content = mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeParagraphContent, ::updateVariableValues),
                parentId = edited.parentId ?: rendered.parentId
            )

            is Edit.Block.Title1 -> edited.copy(
                content = mergeListText(edited, edited.content, rendered, edited.deletedContent),
                parentId = edited.parentId ?: rendered.parentId
            )

            is Edit.Block.Title2 -> edited.copy(
                content = mergeListText(edited, edited.content, rendered, edited.deletedContent),
                parentId = edited.parentId ?: rendered.parentId
            )
        }

    private fun mergeListText(
        parent: Edit.Identifiable,
        editedContent: List<Edit.ParagraphContent.Text>,
        rendered: Edit.Block,
        deleted: Set<Int>,
    ): List<Edit.ParagraphContent.Text> =
        when (rendered) {
            is Edit.Block.Title1 -> mergeList(parent, editedContent, rendered.content, deleted, ::mergeTextContent, ::updateVariableValues)
            is Edit.Block.Title2 -> mergeList(parent, editedContent, rendered.content, deleted, ::mergeTextContent, ::updateVariableValues)
            is Edit.Block.Paragraph -> mergeList(
                parent,
                editedContent,
                rendered.content.filterIsInstance<Edit.ParagraphContent.Text>(),
                deleted,
                ::mergeTextContent,
                ::updateVariableValues,
            )
        }

    private fun mergeTextContent(edited: Edit.ParagraphContent.Text, rendered: Edit.ParagraphContent.Text): Edit.ParagraphContent.Text =
        when (edited) {
            is Edit.ParagraphContent.Text.Literal -> when (rendered) {
                is Edit.ParagraphContent.Text.Literal -> rendered.copy(editedText = edited.editedText, editedFontType = edited.editedFontType)
                is Edit.ParagraphContent.Text.Variable -> throw UpdateEditedLetterException("Edited literal and rendered variable has same ID, cannot merge: $edited - $rendered")
                is Edit.ParagraphContent.Text.NewLine -> throw UpdateEditedLetterException("Edited literal and rendered newLine has same ID, cannot merge: $edited - $rendered")
            }
            is Edit.ParagraphContent.Text.Variable -> throw UpdateEditedLetterException("Variable should never be considered edited: $edited")
            is Edit.ParagraphContent.Text.NewLine -> when (rendered) {
                is Edit.ParagraphContent.Text.NewLine -> edited
                is Edit.ParagraphContent.Text.Literal -> throw UpdateEditedLetterException("Edited newLine and rendered literal has same ID, cannot merge: $edited - $rendered")
                is Edit.ParagraphContent.Text.Variable -> throw UpdateEditedLetterException("Edited newLine and rendered variable has same ID, cannot merge: $edited - $rendered")
            }
        }

    private fun mergeParagraphContent(edited: Edit.ParagraphContent, rendered: Edit.ParagraphContent): Edit.ParagraphContent =
        when (edited) {
            is Edit.ParagraphContent.ItemList ->
                if (rendered is Edit.ParagraphContent.ItemList) {
                    edited.copy(
                        items = mergeList(edited, edited.items, rendered.items, edited.deletedItems, ::mergeItems, ::updateVariableValues),
                        parentId = edited.parentId ?: rendered.parentId
                    )
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
                        rows = mergeList(edited, edited.rows, rendered.rows, rendered.deletedRows, ::mergeRows, ::updateVariableValues),
                        parentId = edited.parentId ?: rendered.parentId,
                    )
                } else {
                    throw UpdateEditedLetterException("Cannot merge ${edited.type} with ${rendered.type}: $edited - $rendered")
                }
        }

    private fun mergeTableHeader(edited: Edit.ParagraphContent.Table.Header, rendered: Edit.ParagraphContent.Table.Header): Edit.ParagraphContent.Table.Header =
        edited.copy(
            colSpec = mergeList(edited, edited.colSpec, rendered.colSpec, emptySet(), ::mergeColumnSpec, ::updateVariableValues),
            parentId = edited.parentId ?: rendered.parentId
        )

    private fun mergeColumnSpec(
        edited: Edit.ParagraphContent.Table.ColumnSpec,
        rendered: Edit.ParagraphContent.Table.ColumnSpec,
    ): Edit.ParagraphContent.Table.ColumnSpec =
        edited.copy(headerContent = mergeCell(edited.headerContent, rendered.headerContent), parentId = edited.parentId ?: rendered.parentId)

    private fun mergeCell(edited: Edit.ParagraphContent.Table.Cell, rendered: Edit.ParagraphContent.Table.Cell): Edit.ParagraphContent.Table.Cell =
        edited.copy(
            text = mergeList(edited, edited.text, rendered.text, emptySet(), ::mergeTextContent, ::updateVariableValues),
            parentId = edited.parentId ?: rendered.parentId
        )

    private fun mergeRows(edited: Edit.ParagraphContent.Table.Row, rendered: Edit.ParagraphContent.Table.Row): Edit.ParagraphContent.Table.Row =
        edited.copy(
            cells = mergeList(edited, edited.cells, rendered.cells, emptySet(), ::mergeCell, ::updateVariableValues),
            parentId = edited.parentId ?: rendered.parentId
        )

    private fun mergeItems(edited: Edit.ParagraphContent.ItemList.Item, rendered: Edit.ParagraphContent.ItemList.Item): Edit.ParagraphContent.ItemList.Item =
        edited.copy(
            content = mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeTextContent, ::updateVariableValues),
            parentId = edited.parentId ?: rendered.parentId
        )

    private fun updateVariableValues(edited: Edit.Block): Edit.Block =
        when (edited) {
            is Edit.Block.Title1 -> edited.copy(content = edited.content.map { updateVariableValues(it) })
            is Edit.Block.Title2 -> edited.copy(content = edited.content.map { updateVariableValues(it) })
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
                ?: Edit.ParagraphContent.Text.Literal(content.id, content.text, content.fontType, parentId = content.parentId)

            is Edit.ParagraphContent.Text.NewLine -> content
        }

    private fun updateVariableValues(itemList: Edit.ParagraphContent.ItemList): Edit.ParagraphContent.ItemList =
        itemList.copy(items = itemList.items.map(::updateVariableValues))

    private fun updateVariableValues(item: Edit.ParagraphContent.ItemList.Item): Edit.ParagraphContent.ItemList.Item =
        item.copy(content = item.content.map(::updateVariableValues))

    private fun updateVariableValues(table: Edit.ParagraphContent.Table): Edit.ParagraphContent.Table =
        table.copy(
            header = table.header.copy(colSpec = table.header.colSpec.map(::updateVariableValues)),
            rows = table.rows.map(::updateVariableValues),
        )

    private fun updateVariableValues(columnSpec: Edit.ParagraphContent.Table.ColumnSpec): Edit.ParagraphContent.Table.ColumnSpec =
        columnSpec.copy(headerContent = updateVariableValues(columnSpec.headerContent))

    private fun updateVariableValues(row: Edit.ParagraphContent.Table.Row): Edit.ParagraphContent.Table.Row =
        row.copy(cells = row.cells.map(::updateVariableValues))

    private fun updateVariableValues(cell: Edit.ParagraphContent.Table.Cell): Edit.ParagraphContent.Table.Cell =
        cell.copy(text = cell.text.map(::updateVariableValues))

}
