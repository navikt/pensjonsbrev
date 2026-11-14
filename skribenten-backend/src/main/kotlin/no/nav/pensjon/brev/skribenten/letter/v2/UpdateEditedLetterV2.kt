package no.nav.pensjon.brev.skribenten.letter.v2

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.pensjon.brev.skribenten.letter.UpdateEditedLetterException
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Block
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Block.Table
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Item
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2.Text

internal class UpdateEditedLetterV2(private val variableValues: Map<Int, String>) {

    companion object {
        fun merge(edited: EditV2.Letter, rendered: LetterMarkup): EditV2.Letter =
            EditV2Markup.toEdit(rendered).let { UpdateEditedLetterV2(it.variablesValueMap()).mergeLetter(edited, it) }

        fun merge(edited: EditV2.Attachment, rendered: Attachment): EditV2.Attachment =
            EditV2Markup.toEdit(rendered).let { UpdateEditedLetterV2(it.variablesValueMap()).mergeAttachment(edited, it) }

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

    private fun mergeLetter(edited: EditV2.Letter, rendered: EditV2.Letter): EditV2.Letter =
        edited.copy(
            title = mergeTitle(edited.title, rendered.title),
            saksinformasjon = rendered.saksinformasjon,
            signatur = mergeSignatur(edited.signatur, rendered.signatur),
            blocks = mergeList(null, edited.blocks, rendered.blocks, edited.deletedBlocks, ::mergeBlock, ::updateVariableValues, ::setMissing),
            deletedBlocks = prunedDeleted(edited.deletedBlocks, edited.blocks, rendered.blocks),
        )

    private fun mergeAttachment(edited: EditV2.Attachment, rendered: EditV2.Attachment): EditV2.Attachment =
        edited.copy(
            title = mergeTitle(edited.title, rendered.title),
            blocks = mergeList(null, edited.blocks, rendered.blocks, edited.deletedBlocks, ::mergeBlock, ::updateVariableValues, ::setMissing),
            deletedBlocks = prunedDeleted(edited.deletedBlocks, edited.blocks, rendered.blocks),
            inkluderSaksinformasjon = rendered.inkluderSaksinformasjon,
        )

    private fun prunedDeleted(deleted: List<Int>, edited: List<EditV2.Identifiable>, rendered: List<EditV2.Identifiable>): List<Int> {
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

    private fun matchedRenderedIndices(edited: List<EditV2.Identifiable>, rendered: List<EditV2.Identifiable>): Set<Int> {
        val editedIds = edited.mapNotNull { it.id }
        val renderedWithIds = rendered.mapIndexedNotNull { index, element -> element.id?.let { index to it } }
        val renderedIds = renderedWithIds.map { it.second }
        val lengths = longestCommonSubsequenceLengths(editedIds, renderedIds)

        return collectMatchedRenderedIndices(editedIds, renderedWithIds, lengths)
    }

    private fun longestCommonSubsequenceLengths(editedIds: List<Int>, renderedIds: List<Int>): Array<IntArray> {
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

    private fun collectMatchedRenderedIndices(
        editedIds: List<Int>,
        renderedWithIds: List<Pair<Int, Int>>,
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

    private fun <E : EditV2.Identifiable> mergeList(
        parent: EditV2.Identifiable?,
        edited: List<E>,
        rendered: List<E>,
        deleted: List<Int>,
        merge: (E, E) -> E,
        updateVariables: (E) -> E,
        setMissingFromTemplate: (E) -> E,
    ): List<E> = buildList {
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

        edited.forEach { currentEdited ->
            if (currentEdited.isNew()) {
                add(updateVariables(currentEdited))
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
                    add(setMissingFromTemplate(updateVariables(currentEdited)))
                } else if (currentEdited.parentId != parent?.id) {
                    add(updateVariables(currentEdited))
                }
            }
        }
        addAll(remainingRendered)
    }

    private fun mergeTitle(edited: EditV2.Title, rendered: EditV2.Title): EditV2.Title =
        edited.copy(
            text = mergeList(null, edited.text, rendered.text, edited.deletedContent, ::mergeText, ::updateVariableValues, ::setMissing),
            deletedContent = prunedDeleted(edited.deletedContent, edited.text, rendered.text),
        )

    private fun mergeBlock(edited: Block, rendered: Block): Block =
        when (edited) {
            is Block.TextBlock ->
                if (rendered is Block.TextBlock) {
                    edited.withContent(
                        content = mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeText, ::updateVariableValues, ::setMissing),
                        deletedContent = prunedDeleted(edited.deletedContent, edited.content, rendered.content),
                    )
                } else {
                    updateVariableValues(edited)
                }

            is Block.ListBlock ->
                if (rendered is Block.ListBlock) {
                    edited.withItems(
                        items = mergeList(edited, edited.items, rendered.items, edited.deletedItems, ::mergeItem, ::updateVariableValues, ::setMissing),
                        deletedItems = prunedDeleted(edited.deletedItems, edited.items, rendered.items),
                    )
                } else {
                    updateVariableValues(edited)
                }

            is Table ->
                if (rendered is Table) {
                    edited.copy(
                        header = mergeTableHeader(edited.header, rendered.header),
                        rows = mergeList(edited, edited.rows, rendered.rows, edited.deletedRows, ::mergeRow, ::updateVariableValues, ::setMissing),
                        deletedRows = prunedDeleted(edited.deletedRows, edited.rows, rendered.rows),
                    )
                } else {
                    updateVariableValues(edited)
                }
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
        edited.copy(
            content = mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeText, ::updateVariableValues, ::setMissing),
            deletedContent = prunedDeleted(edited.deletedContent, edited.content, rendered.content),
        )

    private fun mergeTableHeader(edited: Table.Header, rendered: Table.Header): Table.Header =
        edited.copy(
            colSpec = mergeList(edited, edited.colSpec, rendered.colSpec, edited.deletedColSpecs, ::mergeColumnSpec, ::updateVariableValues, ::setMissing),
            deletedColSpecs = prunedDeleted(edited.deletedColSpecs, edited.colSpec, rendered.colSpec),
        )

    private fun mergeColumnSpec(edited: Table.ColumnSpec, rendered: Table.ColumnSpec): Table.ColumnSpec =
        edited.copy(
            content = mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeText, ::updateVariableValues, ::setMissing),
            deletedContent = prunedDeleted(edited.deletedContent, edited.content, rendered.content),
        )

    private fun mergeRow(edited: Table.Row, rendered: Table.Row): Table.Row =
        edited.copy(
            cells = mergeList(edited, edited.cells, rendered.cells, edited.deletedCells, ::mergeCell, ::updateVariableValues, ::setMissing),
            deletedCells = prunedDeleted(edited.deletedCells, edited.cells, rendered.cells),
        )

    private fun mergeCell(edited: Table.Cell, rendered: Table.Cell): Table.Cell =
        edited.copy(
            content = mergeList(edited, edited.content, rendered.content, edited.deletedContent, ::mergeText, ::updateVariableValues, ::setMissing),
            deletedContent = prunedDeleted(edited.deletedContent, edited.content, rendered.content),
        )

    private fun updateVariableValues(block: Block): Block =
        when (block) {
            is Block.TextBlock -> block.withContent(block.content.map(::updateVariableValues), block.deletedContent)
            is Block.ListBlock -> block.withItems(block.items.map(::updateVariableValues), block.deletedItems)
            is Table -> block.copy(
                header = block.header.copy(colSpec = block.header.colSpec.map(::updateVariableValues)),
                rows = block.rows.map(::updateVariableValues),
            )
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

    private fun updateVariableValues(columnSpec: Table.ColumnSpec): Table.ColumnSpec =
        columnSpec.copy(content = columnSpec.content.map(::updateVariableValues))

    private fun updateVariableValues(row: Table.Row): Table.Row =
        row.copy(cells = row.cells.map(::updateVariableValues))

    private fun updateVariableValues(cell: Table.Cell): Table.Cell =
        cell.copy(content = cell.content.map(::updateVariableValues))

    private fun setMissing(block: Block): Block =
        when (block) {
            is Block.Title2 -> block.copy(missingFromTemplate = true)
            is Block.Title3 -> block.copy(missingFromTemplate = true)
            is Block.Title4 -> block.copy(missingFromTemplate = true)
            is Block.Paragraph -> block.copy(missingFromTemplate = true)
            is Block.ItemList -> block.copy(missingFromTemplate = true)
            is Block.NumberedList -> block.copy(missingFromTemplate = true)
            is Table -> block.copy(missingFromTemplate = true)
        }

    private fun setMissing(text: Text): Text = text

    private fun setMissing(item: Item): Item = item

    private fun setMissing(columnSpec: Table.ColumnSpec): Table.ColumnSpec = columnSpec

    private fun setMissing(row: Table.Row): Table.Row = row

    private fun setMissing(cell: Table.Cell): Table.Cell = cell
}
