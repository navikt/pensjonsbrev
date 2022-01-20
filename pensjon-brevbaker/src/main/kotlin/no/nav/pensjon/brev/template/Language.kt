package no.nav.pensjon.brev.template

import java.util.*

typealias BaseLanguages = LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>

data class LanguageSettings(val settings: Map<String, List<Element<BaseLanguages>>>) {

    fun writeLanguageSettings(writeSetting: (name: String, value: List<Element<BaseLanguages>>) -> Unit): Unit =
        settings.entries.forEach { writeSetting(it.key, it.value) }

}

sealed class Language {
    val name: String = this::class.java.name

    override fun toString(): String {
        return this::class.qualifiedName!!
    }

    fun locale(): Locale =
        when (this) {
            Bokmal -> Locale.forLanguageTag("NB")
            Nynorsk -> Locale.forLanguageTag("NN")
            English -> Locale.UK
        }

    object Bokmal : Language()
    object Nynorsk : Language()
    object English : Language()
}

interface LanguageSupport {
    fun supports(language: Language): Boolean
    fun all(): Set<Language>

    interface Single<Lang1 : Language> : LanguageSupport
    interface Double<Lang1 : Language, Lang2 : Language> : Single<Lang1>
    interface Triple<Lang1 : Language, Lang2 : Language, Lang3 : Language> : Double<Lang1, Lang2>
}

sealed class LanguageCombination {

    data class Single<Lang : Language>(val first: Lang) : LanguageCombination(), LanguageSupport.Single<Lang> {
        override fun supports(language: Language): Boolean = language == first
        override fun all(): Set<Language> = setOf(first)
    }

    data class Double<Lang1 : Language, Lang2 : Language>(val first: Lang1, val second: Lang2) : LanguageCombination(), LanguageSupport.Double<Lang1, Lang2> {
        override fun supports(language: Language): Boolean = language == first || language == second
        override fun all(): Set<Language> = setOf(first, second)
    }

    data class Triple<Lang1 : Language, Lang2 : Language, Lang3 : Language>(
        val first: Lang1,
        val second: Lang2,
        val third: Lang3
    ) : LanguageCombination(), LanguageSupport.Triple<Lang1, Lang2, Lang3> {
        override fun supports(language: Language): Boolean =
            language == first || language == second || language == third
        override fun all(): Set<Language> = setOf(first, second, third)
    }
}