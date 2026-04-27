package no.nav.pensjon.brev.skribenten.letter

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.pensjon.brev.skribenten.letter.ApplyEditScriptTest.Difference.Unchanged
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Type.PARAGRAPH
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Type.TITLE1
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Type.TITLE2
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Type.TITLE3
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Variable
import no.nav.pensjon.brev.skribenten.letter.TextOnlyWordDiff.Token
import no.nav.pensjon.brev.skribenten.serialize.LetterMarkupJacksonModule
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
        Paragraph(2, true, listOf(Literal(1, "noe tekst "), Variable(2, " og noe mer veldig dyrt")))
    )
    val letter2 = editedLetter(
        Paragraph(null, true, listOf(Literal(null, "jadda"))),
        Paragraph(2, true, listOf(Literal(1, "noe tekst "), Variable(2, " og noe mer kjempe billig")))
    )

    private val tokenizer = TextOnlyWordDiff()

    fun List<Token>.reconstructBlocks(): List<LetterMarkup.Block> = reconstructEditLetter(toMutableList())

    @JvmName("reconstructEditLetter-internal")
    fun reconstructEditLetter(remaining: MutableList<Token>): List<LetterMarkup.Block> = buildList {
        while (remaining.isNotEmpty()) {
            val current = remaining.removeFirst()
            if (current is Token.Block) {
                add(createBlock(current, remaining))
            } else {
                throw IllegalStateException("Found editable that is not a BlockId at the top level: $current")
            }
        }
    }

    fun createBlock(block: Token.Block, remaining: MutableList<Token>): LetterMarkup.Block {
        val id = block.id ?: 0

        return when (block.type) {
            TITLE1 -> Title1Impl(id = id, content = collectTextContent(remaining))
            TITLE2 -> Title2Impl(id, true, collectTextContent(remaining))
            TITLE3 -> Title3Impl(id, true, collectTextContent(remaining))
            PARAGRAPH -> ParagraphImpl(id, true, collectParagraphContent(remaining))
        }
    }

    private fun collectParagraphContent(remaining: MutableList<Token>): List<LetterMarkup.ParagraphContent> =
        collectTextContent(remaining)

    private fun collectTextContent(remaining: MutableList<Token>): List<LetterMarkup.ParagraphContent.Text> = buildList {
        while (remaining.firstOrNull() is Token.Content) {
            val content = remaining.removeFirst() as Token.Content
            val id = content.id ?: 0
            val font = remaining.removeFirst() as? Token.ContentFont
                ?: throw IllegalStateException("Expected ContentType after Content for content ${content.id}, but found ${remaining.firstOrNull()}")

            val text = remaining.removeWhile<Token.ContentText>().joinToString(" ") { it.char }

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
        println(tokenizer.tokenize(letter1).toList().reconstructBlocks())
        assertEquals(letter1.toMarkup().blocks, tokenizer.tokenize(letter1).toList().reconstructBlocks())
    }

    @Test
    fun `can apply edit script correctly`() {
        val editscript = shortestEditScript(tokenizer.tokenize(letter1), tokenizer.tokenize(letter2)).all.toMutableList()
        val appliedScript = tokenizer.tokenize(letter1).toList().applyEditScript(editscript)

        println("EDIT SCRIPT")
        editscript.forEach { println(it) }

        assertEquals(tokenizer.tokenize(letter2).toList(), appliedScript)
        assertEquals(letter2.toMarkup().blocks, appliedScript.reconstructBlocks())

    }

    @Test
    fun `can generate diff from editScript`() {
        val editscript = shortestEditScript(tokenizer.tokenize(letter1), tokenizer.tokenize(letter2)).all.toMutableList()
        val diff = tokenizer.tokenize(letter1).toList().generateDiff(editscript)

        println("EDIT SCRIPT")
        println("===========")
        editscript.forEach { println(it) }
        println()
        println("Diff")
        println("===========")
        diff.forEach { println(it) }

        println("Diff highlight")
        println("==============")
        constructDiff(diff.toMutableList())

//        assertEquals(letter2.editables.toList(), appliedScript)
//        assertEquals(letter2.toMarkup().blocks, appliedScript.reconstructBlocks())

    }

    @Test
    fun `can generate delete DiffSegments`() {
        val diff = diffBrev(letter2, letter1.toMarkup())

        jacksonObjectMapper().apply {
            registerModule(JavaTimeModule())
            registerModule(LetterMarkupJacksonModule)
            // TODO pretty print

        }.writerWithDefaultPrettyPrinter()
            .writeValueAsString(letter1.toMarkup().blocks)
            .also { println(it) }

        println()
        println("Diff highlight")
        println("==============")
        diff.second.forEach { println(it) }

    }

    @Test
    fun `can generate insert DiffSegments`() {
        val diff = diffBrev(letter2, letter1.toMarkup())

        jacksonObjectMapper().apply {
            registerModule(JavaTimeModule())
            registerModule(LetterMarkupJacksonModule)
            // TODO pretty print

        }.writerWithDefaultPrettyPrinter()
            .writeValueAsString(letter2.blocks)
            .also { println(it) }

        println()
        println("Diff highlight")
        println("==============")
        diff.first.forEach { println(it) }

    }

    inline fun <reified F> MutableList<in F>.removeWhile(crossinline predicate: (F) -> Boolean = { true }): List<F> =
        generateSequence { (firstOrNull() as? F)?.takeIf(predicate)?.also { removeFirst() } }.toList()


    fun Iterable<Token>.applyEditScript(script: Iterable<EditOperation<Token>>): List<Token> {
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

    fun Iterable<Token>.generateDiff(script: Iterable<EditOperation<Token>>): List<Difference<Token>> {
        val result = map { Unchanged(it) }.toMutableList<Difference<Token>>()
        var offset = 0
        script.forEach { op ->
            when (op) {
                is EditOperation.Insert -> {
                    result.add(op.position, Difference.Inserted(op.value))
                    offset++
                }

                is EditOperation.Delete -> {
                    result[op.position + offset] = Difference.Deleted(op.value)
//                    offset--
                }
            }
        }
        return result
    }

    sealed class Difference<T : Token>(val editable: T) {

        class Inserted<T : Token>(editable: T) : Difference<T>(editable)
        class Deleted<T : Token>(editable: T) : Difference<T>(editable)
        class Unchanged<T : Token>(editable: T) : Difference<T>(editable)

        override fun toString(): String {
            return "${this::class.simpleName}(editable=$editable)"
        }
    }

//    fun List<Difference<Editable>>.constructDiff() = constructDiff(toMutableList())

    fun constructDiff(remaining: MutableList<Difference<Token>>) {
        var blockIndex = 0
        while (remaining.isNotEmpty()) {
            val current = remaining.removeFirst()
            if (current.editable is Token.Block) {
                when (current) {
                    is Difference.Inserted<*> -> {
                        val next = remaining.firstOrNull()
                        if (next is Difference.Deleted<*> && next.editable is Token.Block) {
                            remaining.removeFirst()
                        }
                    }

                    is Difference.Deleted<*> -> {
                        val next = remaining.firstOrNull()
                        if (next is Difference.Inserted<*> && next.editable is Token.Block) {
                            remaining.removeFirst()
                        }
                    }

                    is Unchanged<*> -> Unit
                }

                consumeBlock(blockIndex++, current.editable, remaining)
            } else {
                throw IllegalStateException("Found editable that is not a BlockId at the top level: $current")
            }
        }
    }

    fun consumeBlock(index: Int, block: Token.Block, remaining: MutableList<Difference<Token>>) {
        consumeTextContent(index, remaining)
//        return when (block.type) {
//            TITLE1 -> Title1Impl(id = id, content = collectTextContent(remaining))
//            TITLE2 -> Title2Impl(id, true, collectTextContent(remaining))
//            TITLE3 -> Title3Impl(id, true, collectTextContent(remaining))
//            PARAGRAPH -> ParagraphImpl(id, true, collectParagraphContent(remaining))
//        }
    }

    fun consumeTextContent(blockIndex: Int, remaining: MutableList<Difference<Token>>) {
        var index = 0
        while (remaining.firstOrNull()?.editable is Token.Content) {
            val currentIndex = ContentIndex(blockIndex = blockIndex, contentIndex = index++)
            // Dette er ikke godt nok, det kan være kombinasjon av insert og delete
            val content = remaining.removeFirst()
            assert(content.editable is Token.Content)
            assert(remaining.removeFirst().editable is Token.ContentFont)

            var currentDiff: Diff? = null
            var offset = 0
            while (remaining.firstOrNull()?.editable is Token.ContentText) {
                val current = remaining.removeFirst()
                assert(current.editable is Token.ContentText)

                // TODO Problem med offset logikken her
                if (current is Difference.Inserted && currentDiff is ApplyEditScriptTest.Diff.Insert) {
                    currentDiff = currentDiff.copy(endOffset = ++offset)
                } else if (current is Difference.Deleted && currentDiff is ApplyEditScriptTest.Diff.Delete) {
                    currentDiff = currentDiff.copy(text = currentDiff.text + (current.editable as Token.ContentText).char)
                } else {
                    if (currentDiff != null) {
                        println(currentDiff)
                    }

                    currentDiff = when (current) {
                        is Difference.Deleted<*> -> Diff.Delete(index = currentIndex, offset = offset, text = (current.editable as Token.ContentText).char.toString())
                        is Difference.Inserted<*> -> Diff.Insert(index = currentIndex, startOffset = offset++, endOffset = offset)
                        is Unchanged<*> -> {
                            offset++
                            null
                        }
                    }
                }
            }
            if (currentDiff != null) {
                println(currentDiff)
            }
        }
    }

    data class ContentIndex(val blockIndex: Int, val contentIndex: Int)

    sealed class Diff {
        data class Insert(val index: ContentIndex, val startOffset: Int, val endOffset: Int) : Diff()
        data class Delete(val index: ContentIndex, val offset: Int, val text: String) : Diff()
    }
}