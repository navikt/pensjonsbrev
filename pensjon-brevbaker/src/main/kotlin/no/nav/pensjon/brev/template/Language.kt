package no.nav.pensjon.brev.template

import java.util.*

typealias BaseLanguages = LangBokmalNynorskEnglish
typealias LangBokmal = LanguageSupport.Single<Language.Bokmal>
typealias LangNynorsk = LanguageSupport.Single<Language.Nynorsk>
typealias LangEnglish = LanguageSupport.Single<Language.English>
typealias LangBokmalNynorsk = LanguageSupport.Double<Language.Bokmal, Language.Nynorsk>
typealias LangBokmalNynorskEnglish = LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>

data class LanguageSettings(val settings: Map<String, List<TextElement<BaseLanguages>>>) {

    fun writeLanguageSettings(writeSetting: (name: String, value: List<TextElement<BaseLanguages>>) -> Unit): Unit =
        settings.entries.forEach { writeSetting(it.key, it.value) }

}

sealed class Language : StableHash {
    val name: String = this::class.java.name

    override fun toString(): String {
        return this::class.qualifiedName!!
    }

    fun locale(): Locale =
        when (this) {
            Bokmal -> Locale.forLanguageTag("no")
            Nynorsk -> Locale.forLanguageTag("no")
            English -> Locale.UK
        }

    object Bokmal : Language(), StableHash by StableHash.of("Language.Bokmal")
    object Nynorsk : Language(), StableHash by StableHash.of("Language.Nynorsk")
    object English : Language(), StableHash by StableHash.of("Language.English")
}

interface LanguageSupport : StableHash {
    fun supports(language: Language): Boolean
    fun all(): Set<Language>

    interface Single<Lang1 : Language> : LanguageSupport
    interface Double<Lang1 : Language, Lang2 : Language> : Single<Lang1>
    interface Triple<Lang1 : Language, Lang2 : Language, Lang3 : Language> : Double<Lang1, Lang2>
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