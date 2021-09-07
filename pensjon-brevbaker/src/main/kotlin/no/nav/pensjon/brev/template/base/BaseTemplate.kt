package no.nav.pensjon.brev.template.base

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import no.nav.pensjon.brev.template.*

@JsonIgnoreProperties("parameters")
sealed class BaseTemplate {
    val name: String = this::class.java.name

    abstract val parameters: Set<TemplateParameter>
    abstract val languageSettings: LanguageSettings

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
