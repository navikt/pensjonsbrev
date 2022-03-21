package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.BaseLanguages
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LanguageSettings

fun languageSettings(init: LanguageSettingsBuilder.() -> Unit) =
    with(LanguageSettingsBuilder().apply(init)) {
        LanguageSettings(settings)
    }

@LetterTemplateMarker
class LanguageSettingsBuilder(val settings: MutableMap<String, List<Element<BaseLanguages>>> = mutableMapOf()) {
    fun setting(name: String, valueBuilder: TextOnlyScope<BaseLanguages, Nothing>.() -> Unit) {
        settings[name] = TextOnlyScope<BaseLanguages, Nothing>().apply(valueBuilder).children
    }

    fun setting(name: String, value: Element<BaseLanguages>) {
        settings[name] = listOf(value)
    }
}
