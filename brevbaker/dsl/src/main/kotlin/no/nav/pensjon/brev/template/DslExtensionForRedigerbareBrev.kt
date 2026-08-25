package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

sealed interface DslExtensionForRedigerbareBrev {
    fun <LetterData : RedigerbarBrevdata<*, *>> TemplateGlobalScope<LetterData>.fritekst(
        beskrivelse: String,
    ): Fritekst =
        beskrivelse.takeIf { it.trim().isNotEmpty() }
            ?.let { Fritekst(it) }
            ?: throw IllegalArgumentException("Fritekstfelt må ha initiell tekst for at vi ikke skal lure bruker.")

    fun <LetterData : RedigerbarBrevdata<*, *>> TemplateGlobalScope<LetterData>.redigerbarData(
        variabel: StringExpression,
    ): RedigerbarData = RedigerbarData(variabel)

    fun <Lang : LanguageSupport> ParagraphOnlyScope<Lang, out RedigerbarBrevdata<*, *>>.includePhrase(
        phrase: RedigerbarParagraphPhrase<Lang>,
    ) {
        phrase.apply(this)
    }

    fun <Lang : LanguageSupport> OutlineOnlyScope<Lang, out RedigerbarBrevdata<*, *>>.includePhrase(
        phrase: RedigerbarOutlinePhrase<Lang>,
    ) {
        phrase.apply(this)
    }
}