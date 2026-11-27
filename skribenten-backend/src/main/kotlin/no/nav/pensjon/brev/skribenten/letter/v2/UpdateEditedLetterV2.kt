package no.nav.pensjon.brev.skribenten.letter.v2

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.pensjon.brev.skribenten.letter.UpdateEditedLetterException
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Block
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Block.Table
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Item
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Text

internal class UpdateEditedLetterV2(private val variableValues: VariableValuesUpdaterV2) {

    companion object {
        fun merge(edited: EditV2.Letter, rendered: LetterMarkup, previousRender: EditV2.Letter? = null): EditV2.Letter =
            EditV2Markup.toEdit(rendered).let { UpdateEditedLetterV2(VariableValuesUpdaterV2(it)).mergeLetter(edited, it, previousRender) }

        fun merge(edited: EditV2.Attachment, rendered: Attachment): EditV2.Attachment =
            EditV2Markup.toEdit(rendered).let { UpdateEditedLetterV2(VariableValuesUpdaterV2(it)).mergeAttachment(edited, it) }

        fun updateSaksinformasjonOgSignatur(edited: EditV2.Letter, rendered: LetterMarkup): EditV2.Letter =
            edited.copy(
                saksinformasjon = EditV2Markup.toEdit(rendered.saksinformasjon),
                signatur = mergeSignatur(edited = edited.signatur, rendered = EditV2Markup.toEdit(rendered.signatur)),
            )

        private fun mergeSignatur(edited: EditV2.Signatur, rendered: EditV2.Signatur): EditV2.Signatur =
            EditV2.Signatur(
                saksbehandlerNavn = edited.saksbehandlerNavn,
                attesterendeSaksbehandlerNavn = edited.attesterendeSaksbehandlerNavn,
                navAvsenderEnhet = rendered.navAvsenderEnhet,
            )
    }

    private fun mergeLetter(edited: EditV2.Letter, rendered: EditV2.Letter, previousRender: EditV2.Letter?): EditV2.Letter =
        mergeList(null, edited.blocks, withNewInRendered(rendered.blocks, previousRender), edited.deletedBlocks, ::mergeBlock).let { merged ->
            edited.copy(
                title = mergeTitle(edited.title, rendered.title),
                saksinformasjon = rendered.saksinformasjon,
                signatur = mergeSignatur(edited.signatur, rendered.signatur),
                blocks = merged.items,
                deletedBlocks = merged.deleted,
            )
        }

    private fun mergeAttachment(edited: EditV2.Attachment, rendered: EditV2.Attachment): EditV2.Attachment =
        mergeList(null, edited.blocks, rendered.blocks, edited.deletedBlocks, ::mergeBlock).let { merged ->
            edited.copy(
                title = mergeTitle(edited.title, rendered.title),
                blocks = merged.items,
                deletedBlocks = merged.deleted,
                inkluderSaksinformasjon = rendered.inkluderSaksinformasjon,
            )
        }

    private data class Merged<E>(val items: List<E>, val deleted: List<Int>)

    private fun <E : EditV2.Identifiable> mergeList(
        parent: EditV2.Identifiable?,
        edited: List<E>,
        rendered: List<E>,
        deleted: List<Int>,
        merge: (E, E) -> E,
    ): Merged<E> {
        val aligned = ListAlignmentV2.align(edited, rendered, deleted)
        return Merged(mergeAligned(parent, edited, aligned.remainingRendered, merge), aligned.deleted)
    }

    private fun <E : EditV2.Identifiable> mergeAligned(
        parent: EditV2.Identifiable?,
        edited: List<E>,
        rendered: List<E>,
        merge: (E, E) -> E,
    ): List<E> = buildList {
        val remainingRendered = rendered.toMutableList()

        edited.forEach { currentEdited ->
            if (currentEdited.isNew()) {
                add(variableValues.withUpdatedVariables(currentEdited))
            } else {
                val renderedIndex = remainingRendered.indexOfFirst { it.id == currentEdited.id }

                if (renderedIndex >= 0) {
                    repeat(renderedIndex) { add(remainingRendered.removeFirst()) }

                    if (currentEdited.isEdited()) {
                        add(merge(currentEdited, remainingRendered.removeFirst()))
                    } else {
                        add(remainingRendered.removeFirst())
                    }
                } else if (currentEdited.isEdited()) {
                    add(withDecision(variableValues.withUpdatedVariables(currentEdited), Block.DecisionRequest.missingFromRenderedPending))
                } else if (currentEdited.parentId != parent?.id) {
                    add(variableValues.withUpdatedVariables(currentEdited))
                }
            }
        }
        addAll(remainingRendered)
    }

    private fun mergeTitle(edited: EditV2.Title, rendered: EditV2.Title): EditV2.Title =
        mergeList(null, edited.text, rendered.text, edited.deletedContent, ::mergeText).let { merged ->
            edited.copy(text = merged.items, deletedContent = merged.deleted)
        }

    private fun mergeBlock(edited: Block, rendered: Block): Block =
        when (edited) {
            is Block.TextBlock ->
                if (rendered is Block.TextBlock) {
                    mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeText).let { merged ->
                        edited.withContent(merged.items, merged.deleted)
                    }
                } else {
                    variableValues.withUpdatedVariables(edited)
                }

            is Block.ListBlock ->
                if (rendered is Block.ListBlock) {
                    mergeList(edited, edited.items, rendered.items, edited.deletedItems, ::mergeItem).let { merged ->
                        edited.withItems(merged.items, merged.deleted)
                    }
                } else {
                    variableValues.withUpdatedVariables(edited)
                }

            is Table ->
                if (rendered is Table) {
                    mergeList(edited, edited.rows, rendered.rows, edited.deletedRows, ::mergeRow).let { merged ->
                        edited.copy(header = mergeTableHeader(edited.header, rendered.header), rows = merged.items, deletedRows = merged.deleted)
                    }
                } else {
                    variableValues.withUpdatedVariables(edited)
                }
        }.withDecision(mergeDecision(edited, rendered))

    // A block missing from the template keeps its decision request until Saksbehandler decides to keep or remove it.
    private fun mergeDecision(edited: Block, rendered: Block): Block.DecisionRequest? =
        edited.decision.takeIf { it?.type == Block.DecisionRequest.Type.MISSING_FROM_RENDERED && it.status == Block.DecisionRequest.Decision.PENDING }
            ?: rendered.decision

    /**
     * Marks blocks in the fresh render that were not present in [previousRender] with a pending
     * [Block.DecisionRequest.Type.NEW_IN_RENDERED] decision request.
     * Ids may occur several times in a render, so each occurrence in [previousRender] only accounts for one block.
     * Occurrences aligned with [previousRender] (see [ListAlignmentV2.matchedRenderedIndices]) are reserved first, so that
     * a duplicate inserted before an existing occurrence is the one marked. Remaining occurrences are then consumed
     * by reordered blocks in render order. Without a [previousRender] no blocks are marked.
     */
    private fun withNewInRendered(rendered: List<Block>, previousRender: EditV2.Letter?): List<Block> {
        if (previousRender == null) return rendered
        val matchedIndices = ListAlignmentV2.matchedRenderedIndices(previousRender.blocks, rendered)
        val remainingOccurrences = previousRender.blocks.mapNotNull { it.id }.groupingBy { it }.eachCount().toMutableMap()
        matchedIndices.forEach { index -> rendered[index].id?.let { id -> remainingOccurrences[id] = remainingOccurrences.getValue(id) - 1 } }

        return rendered.mapIndexed { index, block ->
            val id = block.id
            if (id == null || index in matchedIndices) return@mapIndexed block
            val remaining = remainingOccurrences[id] ?: 0
            if (remaining > 0) {
                remainingOccurrences[id] = remaining - 1
                block
            } else {
                block.withDecision(Block.DecisionRequest.newInRenderedPending)
            }
        }
    }

    private fun Block.withDecision(decision: Block.DecisionRequest?): Block =
        when (this) {
            is Block.Title2 -> copy(decision = decision)
            is Block.Title3 -> copy(decision = decision)
            is Block.Title4 -> copy(decision = decision)
            is Block.Paragraph -> copy(decision = decision)
            is Block.ItemList -> copy(decision = decision)
            is Block.NumberedList -> copy(decision = decision)
            is Table -> copy(decision = decision)
        }

    private fun Block.TextBlock.withContent(content: List<Text>, deletedContent: List<Int>): Block.TextBlock =
        when (this) {
            is Block.Title2 -> copy(content = content, deletedContent = deletedContent)
            is Block.Title3 -> copy(content = content, deletedContent = deletedContent)
            is Block.Title4 -> copy(content = content, deletedContent = deletedContent)
            is Block.Paragraph -> copy(content = content, deletedContent = deletedContent)
        }

    private fun Block.ListBlock.withItems(items: List<Item>, deletedItems: List<Int>): Block.ListBlock =
        when (this) {
            is Block.ItemList -> copy(items = items, deletedItems = deletedItems)
            is Block.NumberedList -> copy(items = items, deletedItems = deletedItems)
        }

    private fun mergeText(edited: Text, rendered: Text): Text =
        when (edited) {
            is Text.Literal -> when (rendered) {
                is Text.Literal -> rendered.copy(editedText = edited.editedText, editedFontType = edited.editedFontType)
                is Text.Variable -> if (edited.editedText != null) edited else rendered
                is Text.NewLine -> throw UpdateEditedLetterException("Edited literal and rendered newLine has same ID, cannot merge: $edited - $rendered")
            }

            is Text.Variable -> throw UpdateEditedLetterException("Variable should never be considered edited: $edited")
            is Text.NewLine -> when (rendered) {
                is Text.NewLine -> edited
                is Text.Literal -> throw UpdateEditedLetterException("Edited newLine and rendered literal has same ID, cannot merge: $edited - $rendered")
                is Text.Variable -> throw UpdateEditedLetterException("Edited newLine and rendered variable has same ID, cannot merge: $edited - $rendered")
            }
        }

    private fun mergeItem(edited: Item, rendered: Item): Item =
        mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeText).let { merged ->
            edited.copy(content = merged.items, deletedContent = merged.deleted)
        }

    private fun mergeTableHeader(edited: Table.Header, rendered: Table.Header): Table.Header =
        mergeList(edited, edited.colSpec, rendered.colSpec, edited.deletedColSpecs, ::mergeColumnSpec).let { merged ->
            edited.copy(colSpec = merged.items, deletedColSpecs = merged.deleted)
        }

    private fun mergeColumnSpec(edited: Table.ColumnSpec, rendered: Table.ColumnSpec): Table.ColumnSpec =
        mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeText).let { merged ->
            edited.copy(content = merged.items, deletedContent = merged.deleted)
        }

    private fun mergeRow(edited: Table.Row, rendered: Table.Row): Table.Row =
        mergeList(edited, edited.cells, rendered.cells, edited.deletedCells, ::mergeCell).let { merged ->
            edited.copy(cells = merged.items, deletedCells = merged.deleted)
        }

    private fun mergeCell(edited: Table.Cell, rendered: Table.Cell): Table.Cell =
        mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeText).let { merged ->
            edited.copy(content = merged.items, deletedContent = merged.deleted)
        }

    @Suppress("UNCHECKED_CAST")
    private fun <E : EditV2.Identifiable> withDecision(element: E, decision: Block.DecisionRequest): E =
        when (element) {
            is Block.Title2 -> element.copy(decision = decision) as E
            is Block.Title3 -> element.copy(decision = decision) as E
            is Block.Title4 -> element.copy(decision = decision) as E
            is Block.Paragraph -> element.copy(decision = decision) as E
            is Block.ItemList -> element.copy(decision = decision) as E
            is Block.NumberedList -> element.copy(decision = decision) as E
            is Table -> element.copy(decision = decision) as E
            is Text,
            is Item,
            is Table.Header,
            is Table.ColumnSpec,
            is Table.Row,
            is Table.Cell,
                -> element
        }
}
