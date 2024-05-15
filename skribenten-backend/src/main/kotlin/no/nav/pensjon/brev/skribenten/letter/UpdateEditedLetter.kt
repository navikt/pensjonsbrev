package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.LetterMarkup

class UpdateEditedLetterException(message: String) : RuntimeException(message)

/**
 * Update a letter edited in Skribenten (originally rendered by brevbaker) with a fresh
 * rendering from brevbaker. In the new rendering from brevbaker we may receive elements (blocks/content)
 * that wasn't present in previous renders, e.g. if Saksbehandler has modified the template options or
 * if Sak-data has changed in pesys. Or elements (blocks/content) present in previous renders may no longer be
 * present.
 */
fun Edit.Letter.updatedEditedLetter(renderedLetter: LetterMarkup): Edit.Letter =
    copy(blocks = mergeList(blocks, renderedLetter.blocks.toEdit(), ::mergeBlock, deletedBlocks))

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
private fun <E : Edit.Identifiable> mergeList(edited: List<E>, rendered: List<E>, merge: (E, E) -> E, deleted: Set<Int>): List<E> =
    buildList {
        // A queue of unprocessed rendered elements
        val remainingRendered = rendered.filter { it.id != null && !deleted.contains(it.id) }.toMutableList()

        // We zip-merge the two lists with edited as basis, then we pick matching elements of remainingRendered.
        edited.forEach { currentEdited ->
            // If the currentEdited element is new, i.e. was added manually by Saksbehandler.
            if (currentEdited.isNew()) {
                add(currentEdited)
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


private fun mergeBlock(edited: Edit.Block, rendered: Edit.Block): Edit.Block {
    return when (edited) {
        is Edit.Block.Paragraph -> edited.copy(content = mergeList(edited.content, rendered.content, ::mergeParagraphContent, edited.deletedContent))

        is Edit.Block.Title1 -> when (rendered) {
            is Edit.Block.Title1 -> edited.copy(content = mergeList(edited.content, rendered.content, ::mergeTextContent, edited.deletedContent))
            is Edit.Block.Title2 -> edited.copy(content = mergeList(edited.content, rendered.content, ::mergeTextContent, edited.deletedContent))
            is Edit.Block.Paragraph -> throw UpdateEditedLetterException("Cannot merge a title1 block with a paragraph block: $edited - $rendered")
        }

        is Edit.Block.Title2 -> when (rendered) {
            is Edit.Block.Title1 -> edited.copy(content = mergeList(edited.content, rendered.content, ::mergeTextContent, edited.deletedContent))
            is Edit.Block.Title2 -> edited.copy(content = mergeList(edited.content, rendered.content, ::mergeTextContent, edited.deletedContent))
            is Edit.Block.Paragraph -> throw UpdateEditedLetterException("Cannot merge a title2 block with a paragraph block: $edited - $rendered")
        }
    }
}

private fun mergeTextContent(edited: Edit.ParagraphContent.Text, rendered: Edit.ParagraphContent.Text): Edit.ParagraphContent.Text =
    when (edited) {
        is Edit.ParagraphContent.Text.Literal -> when (rendered) {
            is Edit.ParagraphContent.Text.Literal -> rendered.copy(editedText = edited.editedText)
            is Edit.ParagraphContent.Text.Variable -> throw UpdateEditedLetterException("Edited literal and rendered variable has same ID, cannot merge: $edited - $rendered")
        }

        is Edit.ParagraphContent.Text.Variable -> throw UpdateEditedLetterException("Variable should never be considered edited: $edited")
    }

private fun mergeParagraphContent(edited: Edit.ParagraphContent, rendered: Edit.ParagraphContent): Edit.ParagraphContent =
    when (edited) {
        is Edit.ParagraphContent.ItemList ->
            if (rendered is Edit.ParagraphContent.ItemList) {
                edited.copy(items = mergeList(edited.items, rendered.items, ::mergeItems, edited.deletedItems))
            } else {
                throw UpdateEditedLetterException("Cannot merge ${edited.type} with ${rendered.type}: $edited - $rendered")
            }

        is Edit.ParagraphContent.Text ->
            if (rendered is Edit.ParagraphContent.Text) {
                mergeTextContent(edited, rendered)
            } else {
                throw UpdateEditedLetterException("Cannot merge ${edited.type} with ${rendered.type}: $edited - $rendered")
            }

        is Edit.ParagraphContent.Table -> TODO("Tables are not yet supported by Skribenten")
    }

private fun mergeItems(edited: Edit.ParagraphContent.ItemList.Item, rendered: Edit.ParagraphContent.ItemList.Item): Edit.ParagraphContent.ItemList.Item =
    edited.copy(content = mergeList(edited.content, rendered.content, ::mergeTextContent, emptySet()))

