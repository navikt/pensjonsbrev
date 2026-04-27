package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.LetterMarkup

fun createSplitDiffSegments(redigertBrev: Edit.Letter, rendret: LetterMarkup): Pair<List<DiffSegment>, List<DiffSegment>> {
    val tokenizer = TextOnlyWordTokenizer()

    return tokenizer.tokenize(rendret.toEdit()).toList().let { rendretEditables ->
        val redigertBrevEditables = tokenizer.tokenize(redigertBrev).toList()
        val editScript = shortestEditScript(rendretEditables, redigertBrevEditables)

        Pair(
            redigertBrevEditables.generateDiffSegments(editScript.filterIsInstance<EditOperation.Insert<TextOnlyWordTokenizer.Token>>()),
            rendretEditables.generateDiffSegments(editScript.filterIsInstance<EditOperation.Delete<TextOnlyWordTokenizer.Token>>())
        )
    }
}

data class ContentIndex(val blockIndex: Int, val contentIndex: Int)

data class DiffSegment(val index: ContentIndex, val startOffset: Int, val endOffset: Int)

private fun List<TextOnlyWordTokenizer.Token>.generateDiffSegments(edits: List<EditOperation<TextOnlyWordTokenizer.Token>>): List<DiffSegment> = buildList {
    val editscript = EditscriptConsumer(this@generateDiffSegments, edits)

    var blockIndex = 0
    while (editscript.hasNext) {
        // Her slukes en edit operation, men det virker kun relevant å markere tekst inni selve blokken.
        val (current) = editscript.consume()
        require(current is TextOnlyWordTokenizer.Token.Block) { "Found editable that is not a Block at the top level: $current" }

        addAll(consumeBlock(blockIndex++, editscript))
    }
}

private fun consumeBlock(
    blockIndex: Int,
    editscript: EditscriptConsumer<TextOnlyWordTokenizer.Token>,
): List<DiffSegment> = buildList {
    var contentIndex = 0
    while (editscript.peekLetter() is TextOnlyWordTokenizer.Token.Content) {
        val contentIndex = ContentIndex(blockIndex = blockIndex, contentIndex = contentIndex++)

        val (content) = editscript.consume()
        require(content is TextOnlyWordTokenizer.Token.Content) { "Found editable that is not a Content: $content" }
        require(editscript.consume().first is TextOnlyWordTokenizer.Token.ContentFont) { "Found editable that is not a ContentFont" }

        var currentDiff: DiffSegment? = null
        var text = ""
        while (editscript.peekLetter() is TextOnlyWordTokenizer.Token.ContentText) {
            val (current, textEdit) = editscript.consume()
            require(current is TextOnlyWordTokenizer.Token.ContentText)

            val toAppend = " ${current.char}"
            if (textEdit != null) {
                if (currentDiff == null) {
                    currentDiff = DiffSegment(index = contentIndex, startOffset = text.length, endOffset = text.length + toAppend.length)
                } else if (currentDiff.endOffset == text.length) {
                    currentDiff = currentDiff.copy(endOffset = text.length + toAppend.length)
                } else {
                    add(currentDiff)
                    currentDiff = DiffSegment(index = contentIndex, startOffset = text.length, endOffset = text.length + toAppend.length)
                }
            }
            text += toAppend
        }
        if (currentDiff != null) {
            add(currentDiff)
        }
    }
}

class EditscriptConsumer<T : Any>(val letter: List<T>, editscript: List<EditOperation<T>>) {
    init {
        require(editscript.distinctBy { it.position }.size == editscript.size) { "Expected editscript to only have unique position references" }
    }
    private val editscript = editscript.associateBy { it.position }
    private var currentIndex = 0
    val hasNext: Boolean get() = currentIndex < letter.size

    fun peekLetter(): T? = letter.getOrNull(currentIndex)
    fun consume(): Pair<T, EditOperation<T>?> = Pair(letter[currentIndex], editscript[currentIndex++]).also {
        require(it.second == null || it.second?.value == it.first) { "Expected edit operation value to match letter at position ${currentIndex - 1}, but was ${it.second?.value} and ${it.first}" }
    }
}