package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brevbaker.api.model.LetterMarkup

fun diffBrev(redigertBrev: Edit.Letter, rendret: LetterMarkup): Pair<List<DiffSegment>, List<DiffSegment>> =
    TextOnlyWordTokenizer().diff(old = rendret.toEdit(), new = redigertBrev)

data class ContentIndex(val blockIndex: Int, val contentIndex: Int)

data class DiffSegment(val index: ContentIndex, val startOffset: Int, val endOffset: Int)