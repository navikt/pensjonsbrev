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

        override fun textSegment(change: Change<DiffProducer.TextSegment>) {
            when (change) {
                is Change.Delete -> deletes.add(DiffSegment(change.old.index, change.old.startOffset, change.old.endOffset))
                is Change.Insert -> inserts.add(DiffSegment(change.new.index, change.new.startOffset, change.new.endOffset))
                is Change.Replace -> {
                    inserts.add(DiffSegment(change.new.index, change.new.startOffset, change.new.endOffset))
                    deletes.add(DiffSegment(change.old.index, change.old.startOffset, change.old.endOffset))
                }
            }
        }

        override fun build() = SplitDiff(inserts.toList(), deletes.toList())
    }

    private class UnifiedDiffProducer : DiffProducer<UnifiedDiff> {
        private val inserts = mutableListOf<DiffSegment>()
        private val deletes = mutableListOf<UnifiedDeleteSegment>()

        override fun textSegment(change: Change<DiffProducer.TextSegment>) {
            when (change) {
                is Change.Delete -> deletes.add(UnifiedDeleteSegment(change.old.index, change.old.startOffset, change.old.endOffset, change.old.text))
                is Change.Insert -> inserts.add(DiffSegment(change.new.index, change.new.startOffset, change.new.endOffset))
                is Change.Replace -> {
                    inserts.add(DiffSegment(change.new.index, change.new.startOffset, change.new.endOffset))
                    deletes.add(UnifiedDeleteSegment(change.old.index, change.old.startOffset, change.old.endOffset, change.old.text))
                }
            }
        }

        override fun build() = UnifiedDiff(inserts.toList(), deletes.toList())
    }
}
