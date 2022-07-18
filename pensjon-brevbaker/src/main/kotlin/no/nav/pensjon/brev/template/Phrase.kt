package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.OutlineScope
import no.nav.pensjon.brev.template.dsl.ParagraphScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope

class TextOnlyPhrase<Lang : LanguageSupport, PhraseData>(private val phraseBody: TextOnlyScope<Lang, Unit>.(arg: Expression<PhraseData>) -> Unit) {
    fun apply(scope: TextOnlyScope<in Lang, *>, data: Expression<PhraseData>) {
        TextOnlyScope<Lang, Unit>().apply {
            phraseBody(data)
        }.also { scope.addAll(it.children) }
    }
}

class ParagraphPhrase<Lang : LanguageSupport, PhraseData>(private val phraseBody: ParagraphScope<Lang, Unit>.(arg: Expression<PhraseData>) -> Unit) {
    fun apply(scope: ParagraphScope<in Lang, *>, data: Expression<PhraseData>) {
        ParagraphScope<Lang, Unit>().apply {
            phraseBody(data)
        }.also { scope.addAll(it.children) }
    }
}

class OutlinePhrase<Lang : LanguageSupport, PhraseData>(private val phraseBody: OutlineScope<Lang, Unit>.(arg: Expression<PhraseData>) -> Unit) {
    fun apply(scope: OutlineScope<in Lang, *>, data: Expression<PhraseData>) {
        OutlineScope<Lang, Unit>().apply {
            phraseBody(data)
        }.also { scope.addAll(it.children) }
    }
}
