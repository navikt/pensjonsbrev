package no.nav.pensjon.brev.template

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.OutputStream
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.full.memberProperties

@JsonIgnoreProperties("parameters")
abstract class BaseTemplate {
    val name: String = this::class.java.name

    abstract val parameters: Set<TemplateParameter>
    abstract fun render(letter: Letter): RenderedLetter

    companion object {
        @JsonCreator
        @JvmStatic
        fun creator(name: String?): BaseTemplate? {
            return BaseTemplate::class.sealedSubclasses
                .mapNotNull { it.objectInstance }
                .firstOrNull { it.name == name }
        }
    }

}

interface LanguageSettings
inline fun <reified T : LanguageSettings> T.toMap(): Map<String, String> =
    T::class.memberProperties
        .associate { it.name to it.get(this).toString() }


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
