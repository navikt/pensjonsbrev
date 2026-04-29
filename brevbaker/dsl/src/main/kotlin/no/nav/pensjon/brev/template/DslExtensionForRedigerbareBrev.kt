package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
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

    fun <Lang : LanguageSupport, LetterData : RedigerbarBrevdata<out SaksbehandlerValgBrevdata, out FagsystemBrevdata>> OutlineOnlyScope<Lang, LetterData>.includePhrase(phrase: RedigerbarOutlinePhrase<Lang>) {
        phrase.apply(this)
    }
}