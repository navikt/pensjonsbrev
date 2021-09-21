package no.nav.pensjon.brev.template

import com.fasterxml.jackson.annotation.JsonCreator
import java.io.PrintWriter
import java.util.*

//TODO: lag unit test som verifiserer at BaseLanguages inkluderer alle Language
typealias BaseLanguages = LanguageCombination.Triple<Language.Bokmal, Language.Nynorsk, Language.English>

class LanguageSettings(val settings: Map<String, List<Element<BaseLanguages>>>) {

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

    companion object {
        @JsonCreator
        @JvmStatic
        fun creator(name: String?): Language? {
            return Language::class.sealedSubclasses
                .mapNotNull { it.objectInstance }
                .firstOrNull { it.name == name }
        }
    }

    object Bokmal : Language()
    object Nynorsk : Language()
    object English : Language()
}

sealed class LanguageCombination {
    abstract fun supports(language: Language): Boolean

    data class Single<Lang : Language>(val first: Lang) : LanguageCombination() {
        override fun supports(language: Language): Boolean = language == first
    }

    data class Double<Lang1 : Language, Lang2 : Language>(val first: Lang1, val second: Lang2) : LanguageCombination() {
        override fun supports(language: Language): Boolean = language == first || language == second
    }

    data class Triple<Lang1 : Language, Lang2 : Language, Lang3 : Language>(
        val first: Lang1,
        val second: Lang2,
        val third: Lang3
    ) : LanguageCombination() {
        override fun supports(language: Language): Boolean =
            language == first || language == second || language == third
    }
}