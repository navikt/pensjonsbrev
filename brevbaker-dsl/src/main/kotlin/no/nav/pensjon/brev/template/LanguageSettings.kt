package no.nav.pensjon.brev.template

import java.util.Objects

class LanguageSettings internal constructor(val settings: Map<String, Element.OutlineContent.ParagraphContent.Text.Literal<BaseLanguages>>) {
    class MissingLanguageSettingException(msg: String) : Exception(msg)

    fun writeLanguageSettings(language: Language, writeSetting: (name: String, value: String) -> Unit): Unit =
        settings.keys.forEach { name -> writeSetting(name, getSetting(language, name)) }

    fun getSetting(language: Language, setting: String): String =
        settings[setting]?.text(language)
            ?: throw MissingLanguageSettingException(setting)

    override fun equals(other: Any?): Boolean {
        if (other !is LanguageSettings) return false
        return settings == other.settings
    }
    override fun hashCode(): Int = Objects.hash(settings)
    override fun toString() = "LanguageSettings(settings=$settings)"
}
