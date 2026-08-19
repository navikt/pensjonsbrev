package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

sealed interface DslExtensionForRedigerbareBrev {
    fun <LetterData : RedigerbarBrevdata<out SaksbehandlerValgBrevdata, out FagsystemBrevdata>> TemplateGlobalScope<LetterData>.fritekst(
        beskrivelse: String,
    ): Fritekst =
        beskrivelse.takeIf { it.trim().isNotEmpty() }
            ?.let { Fritekst(it) }
            ?: throw IllegalArgumentException("Fritekstfelt må ha initiell tekst for at vi ikke skal lure bruker.")

    fun <LetterData : RedigerbarBrevdata<out SaksbehandlerValgBrevdata, out FagsystemBrevdata>> TemplateGlobalScope<LetterData>.redigerbarData(
        variabel: StringExpression,
    ): RedigerbarData = RedigerbarData(variabel)

    fun <Lang : LanguageSupport> ParagraphOnlyScope<Lang, out RedigerbarBrevdata<out SaksbehandlerValgBrevdata, out FagsystemBrevdata>>.includePhrase(
        phrase: RedigerbarParagraphPhrase<Lang>,
    ) {
        phrase.apply(this)
    }

    fun <Lang : LanguageSupport> OutlineOnlyScope<Lang, out RedigerbarBrevdata<out SaksbehandlerValgBrevdata, out FagsystemBrevdata>>.includePhrase(
        phrase: RedigerbarOutlinePhrase<Lang>,
    ) {
        phrase.apply(this)
    }
}