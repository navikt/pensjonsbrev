package no.nav.pensjon.brev.skribenten.letter

import no.nav.pensjon.brev.skribenten.common.diff.Change
import no.nav.pensjon.brev.skribenten.common.diff.shortestEditScript
import no.nav.pensjon.brev.skribenten.letter.EditLetterWordTokenizer.Token

class EditLetterWordDiff {

    private val tokenizer = EditLetterWordTokenizer()

    fun tokenize(letter: Edit.Letter): List<Token> = tokenizer.tokenize(letter)

    fun diff(old: Edit.Letter, new: Edit.Letter): SplitDiff =
        tokenizer.parseTokens(shortestEditScript(tokenizer.tokenize(old), tokenizer.tokenize(new)), SplitDiffProducer())

    fun unifiedDiff(old: Edit.Letter, new: Edit.Letter): UnifiedDiff =
        tokenizer.parseTokens(shortestEditScript(tokenizer.tokenize(old), tokenizer.tokenize(new)), UnifiedDiffProducer())

    private class SplitDiffProducer : DiffProducer<SplitDiff> {
        private val inserts = mutableListOf<DiffSegment>()
        private val deletes = mutableListOf<DiffSegment>()

        override fun textSegment(insertIndex: ContentIndex, deleteIndex: ContentIndex, change: Change<DiffProducer.TextSegment>) {
            when (change) {
                is Change.Delete -> deletes.add(DiffSegment(deleteIndex, change.old.startOffset, change.old.endOffset))
                is Change.Insert -> inserts.add(DiffSegment(insertIndex, change.new.startOffset, change.new.endOffset))
                is Change.Replace -> {
                    inserts.add(DiffSegment(insertIndex, change.new.startOffset, change.new.endOffset))
                    deletes.add(DiffSegment(deleteIndex, change.old.startOffset, change.old.endOffset))
                }
            }
        }

        override fun build() = SplitDiff(inserts.toList(), deletes.toList())
    }

    private class UnifiedDiffProducer : DiffProducer<UnifiedDiff> {
        private val inserts = mutableListOf<DiffSegment>()
        private val deletes = mutableListOf<UnifiedDeleteSegment>()

        // Deletes use insertIndex (not deleteIndex) since it reflects the position in the merged (unified) view,
        // unlike deleteIndex which is the position in the old document and drifts whenever entirely deleted
        // content precedes it.
        override fun textSegment(insertIndex: ContentIndex, deleteIndex: ContentIndex, change: Change<DiffProducer.TextSegment>) {
            when (change) {
                is Change.Delete -> deletes.add(UnifiedDeleteSegment(insertIndex, change.old.startOffset, change.old.endOffset, change.old.text))
                is Change.Insert -> inserts.add(DiffSegment(insertIndex, change.new.startOffset, change.new.endOffset))
                is Change.Replace -> {
                    inserts.add(DiffSegment(insertIndex, change.new.startOffset, change.new.endOffset))
                    deletes.add(UnifiedDeleteSegment(insertIndex, change.old.startOffset, change.old.endOffset, change.old.text))
                }
            }
        }

        override fun build() = UnifiedDiff(inserts.toList(), deletes.toList())
    }
}
