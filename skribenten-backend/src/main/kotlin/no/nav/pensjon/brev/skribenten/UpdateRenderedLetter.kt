package no.nav.pensjon.brev.skribenten

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.RenderedJsonLetter.*

fun updatedEditedLetter(editedLetter: RenderedJsonLetter, nextLetter: RenderedJsonLetter): RenderedJsonLetter {
    val editedBlocks = editedLetter.blocks.associateBy { it.id }
    return nextLetter.copy(blocks = nextLetter.blocks.map { next ->
        editedBlocks[next.id]?.let { edited -> mergeBlocks(edited, next) } ?: next
    })
}

private fun mergeBlocks(edited: Block, next: Block): Block {
    val editedContent = edited.content.associateBy { it.id }

    return next.copy(content = next.content.map {
        if (it.type == Content.Type.VARIABLE) {
            it
        } else {
            editedContent[it.id] ?: it
        }
    })
}