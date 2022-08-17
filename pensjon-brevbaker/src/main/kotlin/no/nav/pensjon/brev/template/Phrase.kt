package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.OutlineScope
import no.nav.pensjon.brev.template.dsl.ParagraphScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope

class TextOnlyPhrase<Lang : LanguageSupport, PhraseData : Any>(private val phraseBody: TextOnlyScope<Lang, Unit>.(arg: Expression<PhraseData>) -> Unit) : HasModel<PhraseData> {
    fun apply(scope: TextOnlyScope<in Lang, *>, data: Expression<PhraseData>) {
        TextOnlyScope<Lang, Unit>().apply {
            phraseBody(data)
        }.also { scope.addAll(it.children) }
    }
}

class ParagraphPhrase<Lang : LanguageSupport, PhraseData : Any>(private val phraseBody: ParagraphScope<Lang, Unit>.(arg: Expression<PhraseData>) -> Unit) : HasModel<PhraseData> {
    fun apply(scope: ParagraphScope<in Lang, *>, data: Expression<PhraseData>) {
        ParagraphScope<Lang, Unit>().apply {
            phraseBody(data)
        }.also { scope.addAll(it.children) }
    }
}

abstract class OutlinePhrase<Lang : LanguageSupport> {
    abstract fun OutlineScope<Lang, Unit>.template()
    fun apply(scope: OutlineScope<in Lang, *>) {
        OutlineScope<Lang, Unit>().apply { template() }.also { scope.addAll(it.children) }
    }
}
