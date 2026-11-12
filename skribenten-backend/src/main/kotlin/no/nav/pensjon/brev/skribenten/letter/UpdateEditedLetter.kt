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
 *
 * If [previousRender] is provided, blocks in the fresh rendering without a corresponding occurrence in it are marked with [Edit.Block.Status.NEW_FROM_TEMPLATE].
 */
fun Edit.Letter.updateEditedLetter(renderedLetter: LetterMarkup, previousRender: Edit.Letter? = null): Edit.Letter =
    renderedLetter.toEdit().let { rendered ->
        UpdateEditedLetter(rendered.variablesValueMap()).mergeLetter(this, rendered, previousRender)
    }


/**
 * If [previousRender] is provided, blocks in the fresh rendering without a corresponding occurrence in it are marked with [Edit.Block.Status.NEW_FROM_TEMPLATE].
 */
fun Edit.Attachment.updateEditedAttachment(renderedAttachment: LetterMarkup.Attachment, previousRender: Edit.Attachment? = null): Edit.Attachment =
    renderedAttachment.toEdit().let { rendered ->
        UpdateEditedLetter(rendered.variablesValueMap()).mergeAttachment(this, rendered, previousRender)
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

    fun mergeLetter(edited: Edit.Letter, rendered: Edit.Letter, previousRender: Edit.Letter? = null): Edit.Letter =
        edited.copy(
            title = mergeTitle(edited.title, rendered.title),
            sakspart = rendered.sakspart,
            signatur = mergeSignatur(edited.signatur, rendered.signatur),
            blocks = mergeList(
                parent = null,
                edited = edited.blocks,
                rendered = rendered.blocks,
                deleted = edited.deletedBlocks,
                merge = ::mergeBlock,
                updateVariables = ::updateVariableValues,
                setStatus = ::setStatus,
                previousRendered = previousRender?.blocks,
                isNewFromTemplate = { it.status == Edit.Block.Status.NEW_FROM_TEMPLATE },
            ),
            deletedBlocks = prunedDeleted(edited.deletedBlocks, edited.blocks, rendered.blocks),
        )

    fun mergeAttachment(edited: Edit.Attachment, rendered: Edit.Attachment, previousRender: Edit.Attachment? = null): Edit.Attachment =
        edited.copy(
            title = mergeTitle(edited.title, rendered.title),
            blocks = mergeList(
                parent = null,
                edited = edited.blocks,
                rendered = rendered.blocks,
                deleted = edited.deletedBlocks,
                merge = ::mergeBlock,
                updateVariables = ::updateVariableValues,
                setStatus = ::setStatus,
                previousRendered = previousRender?.blocks,
                isNewFromTemplate = { it.status == Edit.Block.Status.NEW_FROM_TEMPLATE },
            ),
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
        val lengths = longestCommonSubsequenceLengths(editedIds, renderedIds)

        return collectMatchedRenderedIndices(editedIds, renderedWithIds, lengths)
    }

    private fun <T> longestCommonSubsequenceLengths(editedIds: List<T>, renderedIds: List<T>): Array<IntArray> {
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
        return lengths
    }

    private fun <T> collectMatchedRenderedIndices(
        editedIds: List<T>,
        renderedWithIds: List<Pair<Int, T>>,
        lengths: Array<IntArray>,
    ): Set<Int> =
        buildSet {
            var editedIndex = 0
            var renderedIndex = 0
            while (editedIndex < editedIds.size && renderedIndex < renderedWithIds.size) {
                if (editedIds[editedIndex] == renderedWithIds[renderedIndex].second) {
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
     *
     * If [previousRendered] is provided, every result element taken from [rendered] without a corresponding occurrence with the same id in [previousRendered]
     * (i.e. newly added to the template since the previous render) is marked with [setStatus]. The mark is kept for edited elements
     * already marked according to [isNewFromTemplate], so that it survives subsequent merges until Saksbehandler has handled it.
     */
    private fun <E : Edit.Identifiable> mergeList(
        parent: Edit.Identifiable?,
        edited: List<E>,
        rendered: List<E>,
        deleted: List<Int>,
        merge: (E, E) -> E,
        updateVariables: ((E) -> E),
        setStatus: (E, Edit.Block.Status) -> E = { element, _ -> element },
        previousRendered: List<E>? = null,
        isNewFromTemplate: ((E) -> Boolean) = { false },
    ): List<E> = buildList {
        val renderedWithNewMarkers = if (previousRendered != null) {
            val previousIds = previousRendered.map { it.id }
            val renderedIds = rendered.map { it.id }
            val lengths = longestCommonSubsequenceLengths(previousIds, renderedIds)
            val matchedIndices = collectMatchedRenderedIndices(previousIds, renderedIds.mapIndexed { index, id -> index to id }, lengths)
            rendered.mapIndexed { index, element ->
                if (index in matchedIndices) {
                    element
                } else {
                    setStatus(element, Edit.Block.Status.NEW_FROM_TEMPLATE)
                }
            }
        } else {
            rendered
        }

        fun markNewFromTemplate(result: E, renderedElement: E, editedElement: E? = null): E =
            if ((editedElement != null && isNewFromTemplate(editedElement)) || isNewFromTemplate(renderedElement)) {
                setStatus(result, Edit.Block.Status.NEW_FROM_TEMPLATE)
            } else {
                result
            }

        // A queue of unprocessed rendered elements. `deleted` is a multiset (one entry per actually
        // deleted occurrence, not a "this id is deleted" flag). The LCS alignment identifies which
        // occurrences are still represented by `edited`; deletion counts are consumed only from
        // the unmatched occurrences, preserving the surrounding content order.
        val remainingRendered = if (deleted.isEmpty()) {
            renderedWithNewMarkers.filter { it.id != null }.toMutableList()
        } else {
            val deletedCounts = deleted.groupingBy { it }.eachCount().toMutableMap()
            val matchedRenderedIndices = matchedRenderedIndices(edited, rendered)
            renderedWithNewMarkers.mapIndexedNotNull { index, element ->
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
                    repeat((0 until renderedIndex).count()) {
                        val renderedElement = remainingRendered.removeFirst()
                        add(markNewFromTemplate(renderedElement, renderedElement))
                    }

                    // If the currentEdited element actually has any edits we merge them, otherwise we simply pick the rendered one.
                    val renderedElement = remainingRendered.removeFirst()
                    val result = if (currentEdited.isEdited()) merge(currentEdited, renderedElement) else renderedElement
                    add(markNewFromTemplate(result, renderedElement, currentEdited))
                } else if (currentEdited.isEdited()) {
                    // The currentEdited element is not present in the fresh render, but it is edited by the Saksbehandler.
                    // We include it so that no potentially important text is lost.
                    add(setStatus(updateVariables(currentEdited), Edit.Block.Status.MISSING_FROM_TEMPLATE))
                } else if (currentEdited.parentId != parent?.id) {
                    // The currentEdited element is moved to another parent, and thus cannot currently be tracked.
                    // But it is moved by intent, so we do not wish to remove it.
                    add(updateVariables(currentEdited))
                }
            }
        }
        remainingRendered.forEach { add(markNewFromTemplate(it, it)) }
    }

    private fun mergeTitle(edited: Edit.Title, rendered: Edit.Title): Edit.Title =
        edited.copy(
            text = mergeList(null, edited.text, rendered.text, edited.deletedContent, ::mergeTextContent, ::updateVariableValues),
            deletedContent = prunedDeleted(edited.deletedContent, edited.text, rendered.text)
        )

    private fun mergeBlock(edited: Edit.Block, rendered: Edit.Block): Edit.Block =
        when (edited) {
            is Edit.Block.Paragraph -> edited.copy(
                content = mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeParagraphContent, ::updateVariableValues),
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
            is Edit.Block.Title1 -> mergeList(parent, editedContent, rendered.content, deleted, ::mergeTextContent, ::updateVariableValues)
            is Edit.Block.Title2 -> mergeList(parent, editedContent, rendered.content, deleted, ::mergeTextContent, ::updateVariableValues)
            is Edit.Block.Title3 -> mergeList(parent, editedContent, rendered.content, deleted, ::mergeTextContent, ::updateVariableValues)
            is Edit.Block.Paragraph -> mergeList(parent, editedContent, rendered.textContent(), deleted, ::mergeTextContent, ::updateVariableValues)
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
                        items = mergeList(edited, edited.items, rendered.items, edited.deletedItems, ::mergeItems, ::updateVariableValues),
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
                        rows = mergeList(edited, edited.rows, rendered.rows, edited.deletedRows, ::mergeRows, ::updateVariableValues),
                        deletedRows = prunedDeleted(edited.deletedRows, edited.rows, rendered.rows),
                    )
                } else {
                    throw UpdateEditedLetterException("Cannot merge ${edited.type} with ${rendered.type}: $edited - $rendered")
                }
        }

    private fun mergeTableHeader(edited: Edit.ParagraphContent.Table.Header, rendered: Edit.ParagraphContent.Table.Header): Edit.ParagraphContent.Table.Header =
        edited.copy(
            colSpec = mergeList(edited, edited.colSpec, rendered.colSpec, edited.deletedColSpecs, ::mergeColumnSpec, ::updateVariableValues),
            deletedColSpecs = prunedDeleted(edited.deletedColSpecs, edited.colSpec, rendered.colSpec),
        )

    private fun mergeColumnSpec(
        edited: Edit.ParagraphContent.Table.ColumnSpec,
        rendered: Edit.ParagraphContent.Table.ColumnSpec,
    ): Edit.ParagraphContent.Table.ColumnSpec =
        edited.copy(headerContent = mergeCell(edited.headerContent, rendered.headerContent))

    private fun mergeCell(edited: Edit.ParagraphContent.Table.Cell, rendered: Edit.ParagraphContent.Table.Cell): Edit.ParagraphContent.Table.Cell =
        edited.copy(
            text = mergeList(edited, edited.text, rendered.text, edited.deletedContent, ::mergeTextContent, ::updateVariableValues),
            deletedContent = prunedDeleted(edited.deletedContent, edited.text, rendered.text),
        )

    private fun mergeRows(edited: Edit.ParagraphContent.Table.Row, rendered: Edit.ParagraphContent.Table.Row): Edit.ParagraphContent.Table.Row =
        edited.copy(
            cells = mergeList(edited, edited.cells, rendered.cells, edited.deletedCells, ::mergeCell, ::updateVariableValues),
            deletedCells = prunedDeleted(edited.deletedCells, edited.cells, rendered.cells),
        )

    private fun mergeItems(edited: Edit.ParagraphContent.ItemList.Item, rendered: Edit.ParagraphContent.ItemList.Item): Edit.ParagraphContent.ItemList.Item =
        edited.copy(
            content = mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeTextContent, ::updateVariableValues),
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

    private fun setStatus(block: Edit.Block, status: Edit.Block.Status): Edit.Block = when (block) {
        is Edit.Block.Title1 -> block.copy(status = status)
        is Edit.Block.Title2 -> block.copy(status = status)
        is Edit.Block.Title3 -> block.copy(status = status)
        is Edit.Block.Paragraph -> block.copy(status = status)
    }

}
