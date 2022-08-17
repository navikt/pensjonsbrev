package no.nav.pensjon.brev.template.base

import no.nav.pensjon.brev.template.LanguageSettings
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.RenderedLetter

abstract class DummyBase : BaseTemplate() {
    override fun render(letter: Letter<*>): RenderedLetter {
        throw NotImplementedError("Dummy")
    }

    override val languageSettings: LanguageSettings
        get() = throw NotImplementedError("Dummy")
}