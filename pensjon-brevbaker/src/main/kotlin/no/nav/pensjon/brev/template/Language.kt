package no.nav.pensjon.brev.template

import com.fasterxml.jackson.annotation.JsonCreator
import java.io.PrintWriter

//TODO: lag unit test som verifiserer at BaseLanguages inkluderer alle Language
typealias BaseLanguages = LanguageCombination.Triple<Language.Bokmal, Language.Nynorsk, Language.English>

abstract class LanguageSettings(val settings: Map<String, Element.Text.Literal<BaseLanguages>>) {

    abstract fun asWritableLanguageSetting(key: String, value: String): String

    fun writeLanguageSettings(language: Language, printWriter: PrintWriter): Unit =
        settings.entries.map { asWritableLanguageSetting(it.key, it.value.text(language)) }
            .forEach { printWriter.println(it) }


    class LatexCommands(vararg settings: Pair<String, Element.Text.Literal<BaseLanguages>>) : LanguageSettings(settings.toMap()) {
        override fun asWritableLanguageSetting(key: String, value: String): String {
            return """\newcommand{\felt$key}{$value}"""
        }
    }
}

sealed class Language {
    val name: String = this::class.java.name

    override fun toString(): String {
        return this::class.qualifiedName!!
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