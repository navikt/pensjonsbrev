package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable
import org.junit.jupiter.api.Test


class EditScriptTest {

    val a = editedLetter(
        Paragraph(2, true, listOf(Literal(1, "noe tekst "), Variable(2, " og noe mera")))
    )
    val b = editedLetter(
        Paragraph(1, true, listOf(Literal(1, "jadda"))),
        Paragraph(2, true, listOf(Literal(1, "noe tekst "), Variable(2, " og noe merb")))
    )

    @Test
    fun testDiff3() {
        shortestEditScript(a.editables, b.editables).forEach { println(it) }
    }

    @Test
    fun applyEditScript() {
        val editscript = shortestEditScript(a.editables, b.editables)
    }

    fun List<EditOperation.Insert<Editable>>.reduceEditScript() {
        val queue = toMutableList()

        if (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            when (current.insert) {
                is Editable.Block -> newBlock(current.insert, queue)
                is Editable.ContentFont -> TODO()
                is Editable.ContentId -> TODO()
                is Editable.ContentText -> TODO()
                is Editable.BlockId -> TODO()
//                is Editable.BlockStart -> TODO()
                is Editable.BlockType -> TODO()
            }
        }
    }

    fun newBlock(block: Editable.Block, remaining: MutableList<EditOperation.Insert<Editable>>) {
        while (remaining.isNotEmpty() && remaining.first().insert.index.block == block.index.block) {
            val current = remaining.removeFirst()
            when (current.insert) {
                is Editable.ContentId -> TODO()
                is Editable.ContentFont -> TODO()
                is Editable.ContentText -> TODO()
                is Editable.Block -> TODO()
                is Editable.BlockId -> TODO()
                is Editable.BlockType -> TODO()
            }
        }
    }
}