package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.Literal

fun languageSettings(from: LanguageSettings? = null, init: LanguageSettingsBuilder.() -> Unit) =
    with(LanguageSettingsBuilder(from?.settings?.toMutableMap() ?: mutableMapOf()).apply(init)) {
        LanguageSettings(settings)
    }

@LetterTemplateMarker
class LanguageSettingsBuilder(val settings: MutableMap<String, Literal<BaseLanguages>> = mutableMapOf()) {
    fun setting(name: String, valueBuilder: () -> Literal<BaseLanguages>) {
        settings[name] = valueBuilder()
    }

    fun setting(name: String, value: Literal<BaseLanguages>) {
        settings[name] = value
    }
}
