package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

sealed interface DslExtensionForRedigerbareBrev {
    fun TemplateGlobalScope<*>.fritekst(beskrivelse: String): Fritekst = beskrivelse.takeIf { it.trim().isNotEmpty() }
        ?.let { Fritekst(it) }
        ?: throw IllegalArgumentException("Fritekstfelt må ha initiell tekst for at vi ikke skal lure bruker.")

    fun TemplateGlobalScope<*>.redigerbarData(variabel: StringExpression): RedigerbarData = RedigerbarData(variabel)

    fun <Lang : LanguageSupport> ParagraphOnlyScope<Lang, *>.includePhrase(phrase: RedigerbarParagraphPhrase<Lang>) {
        phrase.apply(this)
    }

    fun <Lang : LanguageSupport> OutlineOnlyScope<Lang, *>.includePhrase(phrase: RedigerbarOutlinePhrase<Lang>) {
        phrase.apply(this)
    }
}