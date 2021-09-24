package no.nav.pensjon.brev.template.base

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import no.nav.pensjon.brev.template.LanguageSettings
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.RenderedLetter

@JsonIgnoreProperties("parameters")
sealed class BaseTemplate {
    val name: String = this::class.java.name

    abstract val languageSettings: LanguageSettings

    abstract fun render(letter: Letter<*>): RenderedLetter

}
