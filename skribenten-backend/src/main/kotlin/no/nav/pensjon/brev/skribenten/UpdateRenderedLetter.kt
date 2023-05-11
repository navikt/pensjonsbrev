package no.nav.pensjon.brev.skribenten

import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter.*

fun updatedEditedLetter(editedLetter: EditedJsonLetter, renderedLetter: RenderedJsonLetter): RenderedJsonLetter =
    renderedLetter.copy(blocks = merge(editedLetter.letter.blocks, renderedLetter.blocks, editedLetter.deletedBlocks))

private fun merge(
    editedBlocks: List<Block>,
    renderedBlocks: List<Block>,
    deletedBlocks: Set<Int>,
): List<Block> = buildList {
    var remainingEdited = editedBlocks

    for ((renderedIndex, rendered) in renderedBlocks.filter { !deletedBlocks.contains(it.id) }.withIndex()) {
        val editedIndex = remainingEdited.indexOfFirst { it.id == rendered.id }
        val editedLastIndex = remainingEdited.indexOfLast { it.id == rendered.id }

        if (editedIndex >= 0) {
            // There is a corresponding edited block to the current rendered item, so we add all the preceding new edited blocks before it
            addAll(remainingEdited.subList(0, editedIndex).filter { it.isNew() })

            // If the rendered block is editable then merge edited with it, if not only add the rendered
            if (rendered.editable) {
                addAll(mergeBlocks(remainingEdited.subList(editedIndex, editedLastIndex + 1), rendered))
            } else {
                add(rendered)
            }

            // remove the processed edited blocks from remaining
            remainingEdited = remainingEdited.subList(editedLastIndex + 1, remainingEdited.size)
        } else {
            // There is no corresponding edited block to the current rendered.

            // If there is a next edited block that corresponds to a next rendered block add all new edited blocks preceding it,
            // or if there isn't one then we add all remaining new edited blocks. The reason we add all remaining new edited blocks
            // is that we want them to be "connected" to the previous non-new edited block such that any new blocks from rendered
            // appears after those.
            val renderedRest = renderedBlocks.subList(renderedIndex + 1, renderedBlocks.size).map { it.id }.toSet()
            val nextEditedIndex = remainingEdited.indexOfFirst { renderedRest.contains(it.id) }
                .takeIf { it >= 0 }
                ?: remainingEdited.size

            // add the new edited blocks and update remaining
            addAll(remainingEdited.subList(0, nextEditedIndex).filter { it.isNew() })
            remainingEdited = remainingEdited.subList(nextEditedIndex, remainingEdited.size)

            add(rendered)
        }
    }

    // Add any remaining new edited blocks
    addAll(remainingEdited.filter { it.isNew() })
}

private fun mergeBlocks(editedBlocks: List<Block>, renderedBlock: Block): List<Block> = buildList {
    var remainingRendered = renderedBlock.content()
    val allRenderedIds = remainingRendered.map { it.id }.toSet()

    // merge all edited blocks with the rendered block
    for ((editedIndex, edited) in editedBlocks.withIndex()) {
        val nextRenderedIndex = remainingRendered.indexOfFirst { r -> edited.content().none { r.id == it.id } }
            .takeIf { it >= 0 }
            ?: remainingRendered.size

        // merge the content of the current edited with the remaining rendered
        var merged = mergeContent(edited.content(), remainingRendered.subList(0, nextRenderedIndex), allRenderedIds)
        remainingRendered = remainingRendered.subList(nextRenderedIndex, remainingRendered.size)

        // If we're at the last edited block, add any remaining rendered content (that would be new rendered content)
        if (editedIndex == editedBlocks.size - 1) {
            merged = merged + remainingRendered
        }

        when (edited) {
            is Block.Title1 -> {
                // If we're able to keep the edited block as Title1.
                if (merged.all { it is ParagraphContent.Text }) {
                    add(edited.copy(content = merged.filterIsInstance<ParagraphContent.Text>()))
                } else {
                    add(Block.Paragraph(edited.id, true, merged))
                }
            }
            is Block.Paragraph -> {
                add(edited.copy(content = merged))
            }
        }
    }
}

private fun <T : ParagraphContent> mergeContent(editedContent: List<T>, renderedContent: List<T>, allRenderedIds: Set<Int>): List<T> =
    buildList {
        var remainingEdited = editedContent

        for ((renderedIndex, rendered) in renderedContent.withIndex()) {
            val editedIndex = remainingEdited.indexOfFirst { it.id == rendered.id }
            val editedLastIndex = remainingEdited.indexOfLast { it.id == rendered.id }
            if (editedIndex >= 0) {
                // There is a corresponding edited content to the current rendered item, so we add all remaining new edited content before it
                addAll(remainingEdited.subList(0, editedIndex).filter { it.isNew() || allRenderedIds.contains(it.id) })

                // If the rendered content is editable, add the edited or if not add the rendered
                if (rendered !is ParagraphContent.Text.Variable) {
                    addAll(remainingEdited.subList(editedIndex, editedLastIndex + 1))
                } else {
                    add(rendered)
                }

                // remove the processed edited content from remaining
                remainingEdited = remainingEdited.subList(editedLastIndex + 1, remainingEdited.size)
            } else {
                // There is no corresponding edited content to the current rendered.

                // If there is a next edited content that corresponds to a next rendered content add all new edited content before it,
                // or if there isn't one then we add all remaining new edited content.
                val renderedRest = renderedContent.subList(renderedIndex + 1, renderedContent.size)
                val nextEditedIndex = remainingEdited.indexOfFirst { e -> renderedRest.any { e.id == it.id } }.takeIf { it >= 0 }

                // add the new edited content and update remaining
                addAll(remainingEdited.subList(0, nextEditedIndex ?: remainingEdited.size).filter { it.isNew() })
                remainingEdited = remainingEdited.subList(nextEditedIndex ?: remainingEdited.size, remainingEdited.size)

                add(rendered)
            }
        }

        addAll(remainingEdited.filter { it.isNew() || allRenderedIds.contains(it.id) })
    }

private fun Block.isNew(): Boolean = id == -1
private fun ParagraphContent.isNew(): Boolean = id == -1

private fun Block.content(): List<ParagraphContent> =
    when (this) {
        is Block.Paragraph -> content
        is Block.Title1 -> content
    }
