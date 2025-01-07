package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import java.util.*

typealias BaseLanguages = LangBokmalNynorskEnglish
typealias LangBokmal = LanguageSupport.Single<Language.Bokmal>
typealias LangNynorsk = LanguageSupport.Single<Language.Nynorsk>
typealias LangEnglish = LanguageSupport.Single<Language.English>
typealias LangBokmalNynorsk = LanguageSupport.Double<Language.Bokmal, Language.Nynorsk>
typealias LangBokmalEnglish = LanguageSupport.Double<Language.Bokmal, Language.English>
typealias LangBokmalNynorskEnglish = LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>

data class LanguageSettings(val settings: Map<String, Element.OutlineContent.ParagraphContent.Text.Literal<BaseLanguages>>) {
    class MissingLanguageSettingException(msg: String) : Exception(msg)

    fun writeLanguageSettings(language: Language, writeSetting: (name: String, value: String) -> Unit): Unit =
        settings.keys.forEach { name -> writeSetting(name, getSetting(language, name)) }

    fun getSetting(language: Language, setting: String): String =
        settings[setting]?.text(language)
            ?: throw MissingLanguageSettingException(setting)
}

fun <Lang1 : Language, Lang2 : Language, LetterData : Any> OutlineOnlyScope<LanguageSupport.Double<Lang1, Lang2>, LetterData>.includePhrase(phrase: OutlinePhrase<out LanguageSupport.Triple<Lang1, *, Lang2>>) {
    // Det er trygt å caste her fordi receiver og phrase begge har Lang1 og Lang2.
    @Suppress("UNCHECKED_CAST")
    (phrase as OutlinePhrase<LanguageSupport.Double<Lang1, Lang2>>).apply(this)
}