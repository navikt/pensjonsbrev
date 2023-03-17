package no.nav.pensjon.brev.skribenten

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.RenderedJsonLetter.*

fun updatedEditedLetter(editedLetter: RenderedJsonLetter, renderedLetter: RenderedJsonLetter): RenderedJsonLetter =
    renderedLetter.copy(blocks = mergeLists(editedLetter.blocks, renderedLetter.blocks, Block::id, { id == -1 }, { editable }, ::mergeBlocks))

private fun mergeBlocks(edited: Block, rendered: Block): Block {
    return when (edited) {
        is Block.Title1 -> {
            when (rendered) {
                is Block.Title1 -> edited.copy(content = mergeContent(edited.content, rendered.content))
                is Block.Paragraph -> {
                    if (rendered.content.all { it is ParagraphContent.Text }) {
                        edited.copy(content = mergeContent(edited.content, rendered.content.filterIsInstance<ParagraphContent.Text>()))
                    } else {
                        rendered.copy(content = mergeContent(edited.content, rendered.content))
                    }
                }
            }
        }

        is Block.Paragraph -> edited.copy(content = mergeContent(edited.content, rendered.content()))
    }
}

private fun <T : ParagraphContent> mergeContent(editedContent: List<T>, renderedContent: List<T>): List<T> =
    mergeLists(editedContent, renderedContent, ParagraphContent::id, { id == -1 }, { this !is ParagraphContent.Text.Variable }, { e, _ -> e })

private fun <T> mergeLists(
    editedItems: List<T>,
    renderedItems: List<T>,
    getId: T.() -> Int,
    isNew: T.() -> Boolean,
    isEditable: T.() -> Boolean,
    mergeItems: (T, T) -> T,
): List<T> = buildList {
    var remainingEdited = editedItems

    for ((renderedIndex, rendered) in renderedItems.withIndex()) {
        val editedIndex = remainingEdited.indexOfFirst { it.getId() == rendered.getId() }
        if (editedIndex >= 0) {
            // There is a corresponding edited item to the current rendered item, so we add all remaining new edited items before it
            addAll(remainingEdited.subList(0, editedIndex).filter { it.isNew() })

            // If the rendered item is editable merge edited with it, if not only add the rendered
            if (rendered.isEditable()) {
                add(mergeItems(remainingEdited[editedIndex], rendered))
            } else {
                add(rendered)
            }

            // remove the processed edited item from remaining
            remainingEdited = remainingEdited.subList(editedIndex + 1, remainingEdited.size)
        } else {
            // There is no corresponding edited item to the current rendered.

            // If there is a next edited item that corresponds to a next rendered item add all new edited items before it,
            // or if there isn't one then we add all remaining new edited items.
            val renderedRest = renderedItems.subList(renderedIndex + 1, renderedItems.size)
            val nextEditedIndex = remainingEdited.indexOfFirst { e -> renderedRest.any { e.getId() == it.getId() } }.takeIf { it >= 0 }

            // add the new edited blocks and update remaining
            addAll(remainingEdited.subList(0, nextEditedIndex ?: remainingEdited.size).filter { it.isNew() })
            remainingEdited = remainingEdited.subList(nextEditedIndex ?: remainingEdited.size, remainingEdited.size)

            add(rendered)
        }

    }

    addAll(remainingEdited.filter { it.isNew() })
}

private fun Block.content(): List<ParagraphContent> =
    when (this) {
        is Block.Paragraph -> content
        is Block.Title1 -> content
    }
