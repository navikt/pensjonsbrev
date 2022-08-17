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

abstract class TextOnlyPhrase2<Lang : LanguageSupport> {
    abstract fun TextOnlyScope<Lang, Unit>.template()
    fun apply(scope: TextOnlyScope<in Lang, *>) {
        TextOnlyScope<Lang, Unit>().apply { template() }.also { scope.addAll(it.children) }
    }
}

abstract class ParagraphPhrase<Lang : LanguageSupport> {
    abstract fun ParagraphScope<Lang, Unit>.template()
    fun apply(scope: ParagraphScope<in Lang, *>) {
        ParagraphScope<Lang, Unit>().apply { template() }.also { scope.addAll(it.children) }
    }
}

abstract class OutlinePhrase<Lang : LanguageSupport> {
    abstract fun OutlineScope<Lang, Unit>.template()
    fun apply(scope: OutlineScope<in Lang, *>) {
        OutlineScope<Lang, Unit>().apply { template() }.also { scope.addAll(it.children) }
    }
}
