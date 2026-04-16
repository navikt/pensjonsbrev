package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Type.PARAGRAPH
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Type.TITLE1
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Type.TITLE2
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Type.TITLE3
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.Title1Impl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.Title2Impl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.Title3Impl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApplyEditScriptTest {

    val letter1 = editedLetter(
        Paragraph(2, true, listOf(Literal(1, "noe tekst "), Variable(2, " og noe mera")))
    )
    val letter2 = editedLetter(
        Paragraph(null, true, listOf(Literal(null, "jadda"))),
        Paragraph(2, true, listOf(Literal(1, "noe tekst "), Variable(2, " og noe merb")))
    )

    fun List<Editable>.reconstructBlocks(): List<LetterMarkup.Block> = reconstructEditLetter(toMutableList())

    @JvmName("reconstructEditLetter-internal")
    fun reconstructEditLetter(remaining: MutableList<Editable>): List<LetterMarkup.Block> = buildList {
        while (remaining.isNotEmpty()) {
            val current = remaining.removeFirst()
            if (current is Editable.Block) {
                add(createBlock(current, remaining))
            } else {
                throw IllegalStateException("Found editable that is not a BlockId at the top level: $current")
            }
        }
    }

    fun createBlock(block: Editable.Block, remaining: MutableList<Editable>): LetterMarkup.Block {
        val id = block.id ?: 0

        return when (block.type) {
            TITLE1 -> Title1Impl(id = id, content = collectTextContent(remaining))
            TITLE2 -> Title2Impl(id, true, collectTextContent(remaining))
            TITLE3 -> Title3Impl(id, true, collectTextContent(remaining))
            PARAGRAPH -> ParagraphImpl(id, true, collectParagraphContent(remaining))
        }
    }

    private fun collectParagraphContent(remaining: MutableList<Editable>): List<LetterMarkup.ParagraphContent> =
        collectTextContent(remaining)

    private fun collectTextContent(remaining: MutableList<Editable>): List<LetterMarkup.ParagraphContent.Text> = buildList {
        while (remaining.firstOrNull() is Editable.Content) {
            val content = remaining.removeFirst() as Editable.Content
            val id = content.id ?: 0
            val font = remaining.removeFirst() as? Editable.ContentFont
                ?: throw IllegalStateException("Expected ContentType after Content for content ${content.id}, but found ${remaining.firstOrNull()}")

            val text = remaining.removeWhile<Editable.ContentText>().joinToString("") { "${it.char}" }

            add(
                when (content.type) {
                    Edit.ParagraphContent.Type.ITEM_LIST -> TODO()
                    Edit.ParagraphContent.Type.LITERAL -> LiteralImpl(id = id, fontType = font.type.toMarkup(), text = text)
                    Edit.ParagraphContent.Type.VARIABLE -> LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl(id = id, text = text)
                    Edit.ParagraphContent.Type.TABLE -> TODO()
                    Edit.ParagraphContent.Type.NEW_LINE -> TODO()
                }
            )
        }
    }

    @Test
    fun `can reconstruct edited letter`() {
        println(letter1.editables.toList().reconstructBlocks())
        assertEquals(letter1.toMarkup().blocks, letter1.editables.toList().reconstructBlocks())
    }

    @Test
    fun `can apply edit script correctly`() {
        val editscript = shortestEditScript(letter1.editables, letter2.editables).toMutableList()
        val appliedScript = letter1.editables.toList().applyEditScript(editscript)

        println("EDIT SCRIPT")
        editscript.forEach { println(it) }

        assertEquals(letter2.editables.toList(), appliedScript)
        assertEquals(letter2.toMarkup().blocks, appliedScript.reconstructBlocks())

    }

    inline fun <reified F> MutableList<in F>.removeWhile(crossinline predicate: (F) -> Boolean = { true }): List<F> =
        generateSequence { (firstOrNull() as? F)?.takeIf(predicate)?.also { removeFirst() } }.toList()


    fun Iterable<Editable>.applyEditScript(script: Iterable<EditOperation<Editable>>): List<Editable> {
        val result = toMutableList()
        var offset = 0
        script.forEach { op ->
            when (op) {
                is EditOperation.Insert -> {
                    result.add(op.position, op.value)
                    offset++
                }
                is EditOperation.Delete -> {
                    result.removeAt(op.position + offset)
                    offset--
                }
            }
        }
        return result
    }
}