package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.LetterMarkup

fun createSplitDiffSegments(redigertBrev: Edit.Letter, rendret: LetterMarkup): Pair<List<DiffSegment>, List<DiffSegment>> {
    val tokenizer = TextOnlyWordTokenizer()
    val rendretTokens = tokenizer.tokenize(rendret.toEdit()).toList()
    val redigertTokens = tokenizer.tokenize(redigertBrev).toList()
    val editScript = shortestEditScript(rendretTokens, redigertTokens)

    return Pair(
        tokenizer.generateDiffSegments(TokensWithEdits(redigertTokens, editScript.inserts)),
        tokenizer.generateDiffSegments(TokensWithEdits(rendretTokens, editScript.deletes)),
    )
}

data class ContentIndex(val blockIndex: Int, val contentIndex: Int)

data class DiffSegment(val index: ContentIndex, val startOffset: Int, val endOffset: Int)