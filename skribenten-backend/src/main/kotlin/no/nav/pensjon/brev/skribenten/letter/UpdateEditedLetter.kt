package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.RenderedLetterMarkdown

class UpdateEditedLetterException(message: String) : RuntimeException(message)

fun Edit.Letter.updatedEditedLetter(renderedLetter: RenderedLetterMarkdown): Edit.Letter =
    Edit.Letter(
        blocks = mergeList(blocks, renderedLetter.blocks.toEdit(), ::mergeBlock),
        deletedBlocks = emptySet()
    )

private fun <E : Edit.Identifiable> mergeList(edited: List<E>, rendered: List<E>, merge: (E, E) -> E): List<E> =
    buildList {
        val remainingRendered = rendered.toMutableList()

        edited.forEach { currentEdited ->
            if (currentEdited.isNew()) {
                add(currentEdited)
            } else if (currentEdited.isEdited()) {
                val renderedIndex = remainingRendered.indexOfFirst { it.id == currentEdited.id }

                if (renderedIndex < 0) {
                    // TODO dette elementet er ikke lenger med i rendring, vurdere om vi skal annotere det på et vis eller noe.
                    add(currentEdited)
                } else {
                    for (ind in 0 until renderedIndex) {
                        add(remainingRendered.removeFirst())
                    }

                    val currentRendered = remainingRendered.removeFirst()
                    add(merge(currentEdited, currentRendered))
                }
            } else {
                val renderedIndex = remainingRendered.indexOfFirst { it.id == currentEdited.id }

                // if currentEdited element exists in the new rendering we keep it and add any potential fresh rendered items in between,
                // otherwise we ignore it.
                if (renderedIndex >= 0) {
                    // det er kommet noen nye content elementer før gjeldende i edited, vi legger de til.
                    for (ind in 0..renderedIndex) {
                        add(remainingRendered.removeFirst())
                    }
                }
            }
        }
        addAll(remainingRendered)
    }


private fun mergeBlock(edited: Edit.Block, rendered: Edit.Block): Edit.Block {
    return when (edited) {
        is Edit.Block.Paragraph -> edited.copy(content = mergeList(edited.content, rendered.content, ::mergeParagraphContent))

        is Edit.Block.Title1 -> when (rendered) {
            is Edit.Block.Title1 -> edited.copy(content = mergeList(edited.content, rendered.content, ::mergeTextContent))
            is Edit.Block.Title2 -> edited.copy(content = mergeList(edited.content, rendered.content, ::mergeTextContent))
            is Edit.Block.Paragraph -> throw UpdateEditedLetterException("Cannot merge a title1 block with a paragraph block: $edited - $rendered")
        }

        is Edit.Block.Title2 -> when (rendered) {
            is Edit.Block.Title1 -> edited.copy(content = mergeList(edited.content, rendered.content, ::mergeTextContent))
            is Edit.Block.Title2 -> edited.copy(content = mergeList(edited.content, rendered.content, ::mergeTextContent))
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
                edited.copy(items = mergeList(edited.items, rendered.items, ::mergeItems))
            } else {
                throw UpdateEditedLetterException("Cannot merge ${edited.type} with ${rendered.type}: $edited - $rendered")
            }

        is Edit.ParagraphContent.Text ->
            if (rendered is Edit.ParagraphContent.Text) {
                mergeTextContent(edited, rendered)
            } else {
                throw UpdateEditedLetterException("Cannot merge ${edited.type} with ${rendered.type}: $edited - $rendered")
            }
    }

private fun mergeItems(edited: Edit.ParagraphContent.ItemList.Item, rendered: Edit.ParagraphContent.ItemList.Item): Edit.ParagraphContent.ItemList.Item =
    edited.copy(content = mergeList(edited.content, rendered.content, ::mergeTextContent))

