package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*

fun languageSettings(init: LanguageSettingsBuilder.() -> Unit) =
    with(LanguageSettingsBuilder().apply(init)) {
        LanguageSettings(settings)
    }

@LetterTemplateMarker
class LanguageSettingsBuilder(val settings: MutableMap<String, List<TextElement<BaseLanguages>>> = mutableMapOf()) {
    fun setting(name: String, valueBuilder: TextOnlyScope<BaseLanguages, Nothing>.() -> Unit) {
        settings[name] = TextOnlyScope<BaseLanguages, Nothing>().apply(valueBuilder).elements
    }

    fun setting(name: String, value: TextElement<BaseLanguages>) {
        settings[name] = listOf(value)
    }
}
