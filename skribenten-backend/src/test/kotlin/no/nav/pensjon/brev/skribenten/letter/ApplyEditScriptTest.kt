package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Type.PARAGRAPH
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable
import org.junit.jupiter.api.Test

class ApplyEditScriptTest {

    val a = editedLetter(
        Paragraph(2, true, listOf(Literal(1, "noe tekst "), Variable(2, " og noe mera")))
    )
    val b = editedLetter(
        Paragraph(null, true, listOf(Literal(null, "jadda"))),
        Paragraph(2, true, listOf(Literal(1, "noe tekst "), Variable(2, " og noe merb")))
    )

    @Test
    fun applyEditScript() {
        val editscript = shortestEditScript(a.editables, b.editables).toMutableList()

        val newBlocks = buildList {
            fun currentBlockIndex() = this.size

            a.blocks.forEachIndexed { blockIndex, block ->
                val peek = editscript.firstOrNull()
                if (peek is EditOperation.Insert && peek.insert is Editable.Block && peek.insert.index.block == currentBlockIndex()) {
                    // Remove the peeked value
                    editscript.removeFirst()

                    add(newBlock(peek.insert, editscript.removeWhile { it.value.index.block == currentBlockIndex() }))
                }
            }
        }
    }

    inline fun <T, reified F : T> MutableList<T>.removeWhile(predicate: (F) -> Boolean) : MutableList<F> {
        val removed = mutableListOf<F>()

        while (isNotEmpty() && first() is F && predicate(first() as F)) {
            add(removeFirst())
        }

        return removed
    }

    fun newBlock(block: Editable.Block, remaining: MutableList<EditOperation.Insert<Editable>>): Edit.Block {
        val blockContent = remaining.map { it.insert }.toMutableList()

        val content = buildList<Edit.ParagraphContent> {
            while (remaining.isNotEmpty()) {
                val current = blockContent.removeFirst()

                if (current is Editable.ContentId) {
                    add(
                        Literal(
                            id = current.id,
                            fontType = (blockContent.removeFirst() as Editable.ContentFont).fontType,
                            text = blockContent.removeWhile<_, Editable.ContentText> { it.index.content == current.index.content }.joinToString(separator = "") { it.char.toString() }
                        )
                    )
                }
            }
        }
        return when (block.type) {
            PARAGRAPH -> Paragraph(block.id, true, content)
            else -> error("Unsupported block type")
        }
    }
}