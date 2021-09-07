package no.nav.pensjon.brev.template.base

import no.nav.pensjon.brev.template.LanguageSettings
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.RenderedLetter

abstract class DummyBase : BaseTemplate() {
    override fun render(letter: Letter): RenderedLetter {
        TODO("Not yet implemented")
    }

    override val languageSettings: LanguageSettings
        get() = TODO("Not yet implemented")
}