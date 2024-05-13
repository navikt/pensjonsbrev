package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*

fun languageSettings(init: LanguageSettingsBuilder.() -> Unit) =
    with(LanguageSettingsBuilder().apply(init)) {
        LanguageSettings(settings)
    }

@LetterTemplateMarker
class LanguageSettingsBuilder(val settings: MutableMap<String, Element.OutlineContent.ParagraphContent.Text.Literal<BaseLanguages>> = mutableMapOf()) {
    fun setting(name: String, valueBuilder: () -> Element.OutlineContent.ParagraphContent.Text.Literal<BaseLanguages>) {
        settings[name] = valueBuilder()
    }

    fun setting(name: String, value: Element.OutlineContent.ParagraphContent.Text.Literal<BaseLanguages>) {
        settings[name] = value
    }
}
