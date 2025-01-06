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

sealed class LanguageCombination {

    data class Single<Lang : Language>(val first: Lang) : LanguageCombination(), LanguageSupport.Single<Lang>, StableHash by StableHash.of(first) {
        override fun supports(language: Language): Boolean = language == first
        override fun all(): Set<Language> = setOf(first)
    }

    data class Double<Lang1 : Language, Lang2 : Language>(
        val first: Lang1,
        val second: Lang2,
    ) : LanguageCombination(), LanguageSupport.Double<Lang1, Lang2>, StableHash by StableHash.of(first, second) {
        override fun supports(language: Language): Boolean = language == first || language == second
        override fun all(): Set<Language> = setOf(first, second)
    }

    data class Triple<Lang1 : Language, Lang2 : Language, Lang3 : Language>(
        val first: Lang1,
        val second: Lang2,
        val third: Lang3
    ) : LanguageCombination(), LanguageSupport.Triple<Lang1, Lang2, Lang3>, StableHash by StableHash.of(first, second, third) {
        override fun supports(language: Language): Boolean =
            language == first || language == second || language == third
        override fun all(): Set<Language> = setOf(first, second, third)
    }
}

fun <Lang1 : Language, Lang2 : Language, LetterData : Any> OutlineOnlyScope<LanguageSupport.Double<Lang1, Lang2>, LetterData>.includePhrase(phrase: OutlinePhrase<out LanguageSupport.Triple<Lang1, *, Lang2>>) {
    // Det er trygt å caste her fordi receiver og phrase begge har Lang1 og Lang2.
    @Suppress("UNCHECKED_CAST")
    (phrase as OutlinePhrase<LanguageSupport.Double<Lang1, Lang2>>).apply(this)
}