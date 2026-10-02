@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.skribenten.letter

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl

class UpdateEditedLetterException(message: String) : RuntimeException(message)

/**
 * Update a letter edited in Skribenten (originally rendered by brevbaker) with a fresh
 * rendering from brevbaker. In the new rendering from brevbaker we may receive elements (blocks/content)
 * that wasn't present in previous renders, e.g. if Saksbehandler has modified the template options (saksbehandlerValg) or
 * if Sak-data has changed in pesys. Or elements (blocks/content) present in previous renders may no longer be
 * present.
 */
fun Edit.Letter.updateEditedLetter(renderedLetter: LetterMarkup): Edit.Letter =
    renderedLetter.toEdit().let { rendered ->
        UpdateEditedLetter(rendered.variablesValueMap()).mergeLetter(this, rendered)
    }


fun Edit.Attachment.updateEditedAttachment(renderedAttachment: LetterMarkup.Attachment): Edit.Attachment =
    renderedAttachment.toEdit().let { rendered ->
        UpdateEditedLetter(rendered.variablesValueMap()).mergeAttachment(this, rendered)
    }

fun Edit.Letter.updateSakspartOgSignatur(renderedLetter: LetterMarkup): Edit.Letter =
    copy(
        sakspart = renderedLetter.sakspart,
        signatur = mergeSignatur(edited = signatur, rendered = renderedLetter.signatur),
    )

private fun mergeSignatur(edited: LetterMarkup.Signatur, rendered: LetterMarkup.Signatur): LetterMarkup.Signatur =
    LetterMarkupImpl.SignaturImpl(
        // Template-controlled fields: always use rendered values
        hilsenTekst = rendered.hilsenTekst,
        navAvsenderEnhet = rendered.navAvsenderEnhet,
        // User-editable fields: preserve edited values
        saksbehandlerNavn = edited.saksbehandlerNavn,
        attesterendeSaksbehandlerNavn = edited.attesterendeSaksbehandlerNavn,
    )

class UpdateEditedLetter(private val variableValues: Map<Int, String>) {

    fun mergeLetter(edited: Edit.Letter, rendered: Edit.Letter): Edit.Letter =
        edited.copy(
            title = mergeTitle(edited.title, rendered.title),
            sakspart = rendered.sakspart,
            signatur = mergeSignatur(edited.signatur, rendered.signatur),
            blocks = mergeList(null, edited.blocks, rendered.blocks, edited.deletedBlocks, ::mergeBlock, ::updateVariableValues, ::setMissing),
            deletedBlocks = prunedDeleted(edited.deletedBlocks, edited.blocks, rendered.blocks),
        )

    fun mergeAttachment(edited: Edit.Attachment, rendered: Edit.Attachment): Edit.Attachment =
        edited.copy(
            title = mergeTitle(edited.title, rendered.title),
            blocks = mergeList(null, edited.blocks, rendered.blocks, edited.deletedBlocks, ::mergeBlock, ::updateVariableValues, ::setMissing),
            deletedBlocks = prunedDeleted(edited.deletedBlocks, edited.blocks, rendered.blocks),
            includeSakspart = rendered.includeSakspart,
        )

    /**
     * Caps a multiset of deleted ids (one entry per deleted occurrence, see [mergeList]) at the
     * number of rendered occurrences of that id that are not aligned with [edited]. This uses the
     * same alignment as [mergeList], so an edited occurrence that is no longer in the template
     * cannot use up capacity belonging to a deleted occurrence that is still rendered.
     */
    private fun prunedDeleted(deleted: List<Int>, edited: List<Edit.Identifiable>, rendered: List<Edit.Identifiable>): List<Int> {
        if (deleted.isEmpty()) return emptyList()

        val matchedRenderedIndices = matchedRenderedIndices(edited, rendered)
        val unmatchedRenderedCounts = rendered
            .filterIndexed { index, element -> element.id != null && index !in matchedRenderedIndices }
            .groupingBy { it.id }
            .eachCount()
        val deletedCounts = deleted.groupingBy { it }.eachCount()

        return deletedCounts.flatMap { (id, deletedCount) ->
            List(minOf(deletedCount, unmatchedRenderedCounts[id] ?: 0)) { id }
        }
    }

    /**
     * Returns the indices in [rendered] that form a longest common subsequence with the template
     * elements still present in [edited]. A deletion marker for a duplicated id is then applied
     * only to rendered occurrences outside this alignment. That preserves whether the caseworker
     * removed the first or the second occurrence in e.g. `[X, separator, X]`.
     */
    private fun matchedRenderedIndices(edited: List<Edit.Identifiable>, rendered: List<Edit.Identifiable>): Set<Int> {
        val editedIds = edited.mapNotNull { it.id }
        val renderedWithIds = rendered.mapIndexedNotNull { index, element -> element.id?.let { index to it } }
        val renderedIds = renderedWithIds.map { it.second }
        val lengths = Array(editedIds.size + 1) { IntArray(renderedIds.size + 1) }

        for (editedIndex in editedIds.indices.reversed()) {
            for (renderedIndex in renderedIds.indices.reversed()) {
                lengths[editedIndex][renderedIndex] =
                    if (editedIds[editedIndex] == renderedIds[renderedIndex]) {
                        1 + lengths[editedIndex + 1][renderedIndex + 1]
                    } else {
                        maxOf(lengths[editedIndex + 1][renderedIndex], lengths[editedIndex][renderedIndex + 1])
                    }
            }
        }

        return buildSet {
            var editedIndex = 0
            var renderedIndex = 0
            while (editedIndex < editedIds.size && renderedIndex < renderedIds.size) {
                if (editedIds[editedIndex] == renderedIds[renderedIndex]) {
                    add(renderedWithIds[renderedIndex].first)
                    editedIndex++
                    renderedIndex++
                } else if (lengths[editedIndex + 1][renderedIndex] > lengths[editedIndex][renderedIndex + 1]) {
                    editedIndex++
                } else {
                    renderedIndex++
                }
            }
        }
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
        parent: Edit.Identifiable?,
        edited: List<E>,
        rendered: List<E>,
        deleted: List<Int>,
        merge: (E, E) -> E,
        updateVariables: ((E) -> E),
        setMissingFromTemplate: ((E) -> E),
    ): List<E> = buildList {
        // A queue of unprocessed rendered elements. `deleted` is a multiset (one entry per actually
        // deleted occurrence, not a "this id is deleted" flag). The LCS alignment identifies which
        // occurrences are still represented by `edited`; deletion counts are consumed only from
        // the unmatched occurrences, preserving the surrounding content order.
        val remainingRendered = if (deleted.isEmpty()) {
            rendered.filter { it.id != null }.toMutableList()
        } else {
            val deletedCounts = deleted.groupingBy { it }.eachCount().toMutableMap()
            val matchedRenderedIndices = matchedRenderedIndices(edited, rendered)
            rendered.mapIndexedNotNull { index, element ->
                val id = element.id ?: return@mapIndexedNotNull null
                val remaining = deletedCounts[id]
                if (index !in matchedRenderedIndices && remaining != null && remaining > 0) {
                    deletedCounts[id] = remaining - 1
                    null
                } else {
                    element
                }
            }.toMutableList()
        }

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
                    repeat((0 until renderedIndex).count()) { add(remainingRendered.removeFirst()) }

                    // If the currentEdited element actually has any edits we merge them, otherwise we simply pick the rendered one.
                    if (currentEdited.isEdited()) {
                        add(merge(currentEdited, remainingRendered.removeFirst()))
                    } else {
                        add(remainingRendered.removeFirst())
                    }
                } else if (currentEdited.isEdited()) {
                    // The currentEdited element is not present in the fresh render, but it is edited by the Saksbehandler.
                    // We include it so that no potentially important text is lost.
                    add(setMissingFromTemplate(updateVariables(currentEdited)))
                } else if (currentEdited.parentId != parent?.id) {
                    // The currentEdited element is moved to another parent, and thus cannot currently be tracked.
                    // But it is moved by intent, so we do not wish to remove it.
                    add(updateVariables(currentEdited))
                }
            }
        }
        addAll(remainingRendered)
    }

    private fun mergeTitle(edited: Edit.Title, rendered: Edit.Title): Edit.Title =
        edited.copy(
            text = mergeList(null, edited.text, rendered.text, edited.deletedContent, ::mergeTextContent, ::updateVariableValues, ::setMissing),
            deletedContent = prunedDeleted(edited.deletedContent, edited.text, rendered.text)
        )

    private fun mergeBlock(edited: Edit.Block, rendered: Edit.Block): Edit.Block =
        when (edited) {
            is Edit.Block.Paragraph -> edited.copy(
                content = mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeParagraphContent, ::updateVariableValues, ::setMissing),
                deletedContent = prunedDeleted(edited.deletedContent, edited.content, rendered.content),
            )

            is Edit.Block.Title1 -> edited.copy(
                content = mergeListText(edited, edited.content, rendered, edited.deletedContent),
                deletedContent = prunedDeleted(edited.deletedContent, edited.content, rendered.textContent()),
            )

            is Edit.Block.Title2 -> edited.copy(
                content = mergeListText(edited, edited.content, rendered, edited.deletedContent),
                deletedContent = prunedDeleted(edited.deletedContent, edited.content, rendered.textContent()),
            )

            is Edit.Block.Title3 -> edited.copy(
                content = mergeListText(edited, edited.content, rendered, edited.deletedContent),
                deletedContent = prunedDeleted(edited.deletedContent, edited.content, rendered.textContent()),
            )

        }

    private fun Edit.Block.textContent(): List<Edit.ParagraphContent.Text> =
        when (this) {
            is Edit.Block.Title1 -> content
            is Edit.Block.Title2 -> content
            is Edit.Block.Title3 -> content
            is Edit.Block.Paragraph -> content.filterIsInstance<Edit.ParagraphContent.Text>()
        }

    private fun mergeListText(
        parent: Edit.Identifiable,
        editedContent: List<Edit.ParagraphContent.Text>,
        rendered: Edit.Block,
        deleted: List<Int>,
    ): List<Edit.ParagraphContent.Text> =
        when (rendered) {
            is Edit.Block.Title1 -> mergeList(parent, editedContent, rendered.content, deleted, ::mergeTextContent, ::updateVariableValues, ::setMissing)
            is Edit.Block.Title2 -> mergeList(parent, editedContent, rendered.content, deleted, ::mergeTextContent, ::updateVariableValues, ::setMissing)
            is Edit.Block.Title3 -> mergeList(parent, editedContent, rendered.content, deleted, ::mergeTextContent, ::updateVariableValues, ::setMissing)
            is Edit.Block.Paragraph -> mergeList(
                parent,
                editedContent,
                rendered.content.filterIsInstance<Edit.ParagraphContent.Text>(),
                deleted,
                ::mergeTextContent,
                ::updateVariableValues,
                ::setMissing
            )
        }

    private fun mergeTextContent(edited: Edit.ParagraphContent.Text, rendered: Edit.ParagraphContent.Text): Edit.ParagraphContent.Text =
        when (edited) {
            is Edit.ParagraphContent.Text.Literal -> when (rendered) {
                is Edit.ParagraphContent.Text.Literal -> rendered.copy(editedText = edited.editedText, editedFontType = edited.editedFontType)
                is Edit.ParagraphContent.Text.Variable -> if (edited.editedText != null) edited else rendered
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
                        items = mergeList(edited, edited.items, rendered.items, edited.deletedItems, ::mergeItems, ::updateVariableValues, ::setMissing),
                        deletedItems = prunedDeleted(edited.deletedItems, edited.items, rendered.items),
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
                        rows = mergeList(edited, edited.rows, rendered.rows, edited.deletedRows, ::mergeRows, ::updateVariableValues, ::setMissing),
                        deletedRows = prunedDeleted(edited.deletedRows, edited.rows, rendered.rows),
                    )
                } else {
                    throw UpdateEditedLetterException("Cannot merge ${edited.type} with ${rendered.type}: $edited - $rendered")
                }
        }

    private fun mergeTableHeader(edited: Edit.ParagraphContent.Table.Header, rendered: Edit.ParagraphContent.Table.Header): Edit.ParagraphContent.Table.Header =
        edited.copy(
            colSpec = mergeList(edited, edited.colSpec, rendered.colSpec, edited.deletedColSpecs, ::mergeColumnSpec, ::updateVariableValues, ::setMissing),
            deletedColSpecs = prunedDeleted(edited.deletedColSpecs, edited.colSpec, rendered.colSpec),
        )

    private fun mergeColumnSpec(
        edited: Edit.ParagraphContent.Table.ColumnSpec,
        rendered: Edit.ParagraphContent.Table.ColumnSpec,
    ): Edit.ParagraphContent.Table.ColumnSpec =
        edited.copy(headerContent = mergeCell(edited.headerContent, rendered.headerContent))

    private fun mergeCell(edited: Edit.ParagraphContent.Table.Cell, rendered: Edit.ParagraphContent.Table.Cell): Edit.ParagraphContent.Table.Cell =
        edited.copy(
            text = mergeList(edited, edited.text, rendered.text, edited.deletedContent, ::mergeTextContent, ::updateVariableValues, ::setMissing),
            deletedContent = prunedDeleted(edited.deletedContent, edited.text, rendered.text),
        )

    private fun mergeRows(edited: Edit.ParagraphContent.Table.Row, rendered: Edit.ParagraphContent.Table.Row): Edit.ParagraphContent.Table.Row =
        edited.copy(
            cells = mergeList(edited, edited.cells, rendered.cells, edited.deletedCells, ::mergeCell, ::updateVariableValues, ::setMissing),
            deletedCells = prunedDeleted(edited.deletedCells, edited.cells, rendered.cells),
        )

    private fun mergeItems(edited: Edit.ParagraphContent.ItemList.Item, rendered: Edit.ParagraphContent.ItemList.Item): Edit.ParagraphContent.ItemList.Item =
        edited.copy(
            content = mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeTextContent, ::updateVariableValues, ::setMissing),
            deletedContent = prunedDeleted(edited.deletedContent, edited.content, rendered.content),
        )

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
            header = table.header.copy(colSpec = table.header.colSpec.map(::updateVariableValues)),
            rows = table.rows.map(::updateVariableValues),
        )

    private fun updateVariableValues(columnSpec: Edit.ParagraphContent.Table.ColumnSpec): Edit.ParagraphContent.Table.ColumnSpec =
        columnSpec.copy(headerContent = updateVariableValues(columnSpec.headerContent))

    private fun updateVariableValues(row: Edit.ParagraphContent.Table.Row): Edit.ParagraphContent.Table.Row =
        row.copy(cells = row.cells.map(::updateVariableValues))

    private fun updateVariableValues(cell: Edit.ParagraphContent.Table.Cell): Edit.ParagraphContent.Table.Cell =
        cell.copy(text = cell.text.map(::updateVariableValues))

    private fun setMissing(block: Edit.Block): Edit.Block = when (block) {
        is Edit.Block.Title1 -> block.copy(missingFromTemplate = true)
        is Edit.Block.Title2 -> block.copy(missingFromTemplate = true)
        is Edit.Block.Title3 -> block.copy(missingFromTemplate = true)
        is Edit.Block.Paragraph -> block.copy(missingFromTemplate = true)
    }

    private fun setMissing(content: Edit.ParagraphContent): Edit.ParagraphContent = content

    private fun setMissing(text: Edit.ParagraphContent.Text): Edit.ParagraphContent.Text = text

    private fun setMissing(item: Edit.ParagraphContent.ItemList.Item): Edit.ParagraphContent.ItemList.Item = item

    private fun setMissing(columnSpec: Edit.ParagraphContent.Table.ColumnSpec): Edit.ParagraphContent.Table.ColumnSpec = columnSpec

    private fun setMissing(row: Edit.ParagraphContent.Table.Row): Edit.ParagraphContent.Table.Row = row

    private fun setMissing(cell: Edit.ParagraphContent.Table.Cell): Edit.ParagraphContent.Table.Cell = cell
}
