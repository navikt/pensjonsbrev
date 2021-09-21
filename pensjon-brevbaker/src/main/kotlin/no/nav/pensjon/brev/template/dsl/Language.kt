package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.BaseLanguages
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LanguageSettings
import no.nav.pensjon.brev.template.TextOnlyBuilder

fun languageSettings(init: LanguageSettingsBuilder.() -> Unit) =
    with(LanguageSettingsBuilder().apply(init)) {
        LanguageSettings(settings)
    }

class LanguageSettingsBuilder(val settings: MutableMap<String, List<Element<BaseLanguages>>> = mutableMapOf()) {
    fun setting(name: String, valueBuilder: TextOnlyBuilder<BaseLanguages, Nothing>.() -> Unit) {
        with(TextOnlyBuilder<BaseLanguages, Nothing>().apply(valueBuilder)) {
            settings[name] = children
        }
    }

    fun setting(name: String, value: Element<BaseLanguages>) {
        settings[name] = listOf(value)
    }
}