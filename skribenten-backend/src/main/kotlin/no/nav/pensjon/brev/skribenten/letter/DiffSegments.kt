package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.LetterMarkup

fun createSplitDiffSegments(redigertBrev: Edit.Letter, rendret: LetterMarkup): Pair<List<DiffSegment>, List<DiffSegment>> {
    val tokenizer = TextOnlyWordTokenizer()
    val editScript = shortestEditScript(
        old = tokenizer.tokenize(rendret.toEdit()).toList(),
        new = tokenizer.tokenize(redigertBrev).toList(),
    )
    return tokenizer.generateDiffSegments(editScript)
}

data class ContentIndex(val blockIndex: Int, val contentIndex: Int)

data class DiffSegment(val index: ContentIndex, val startOffset: Int, val endOffset: Int)