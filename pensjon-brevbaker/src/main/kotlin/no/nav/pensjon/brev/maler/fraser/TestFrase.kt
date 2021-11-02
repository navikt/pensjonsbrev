package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.*

data class PhraseInputDto(val test: String)

val testFrase = miniLetter(PhraseInputDto::class, languages(Language.Bokmal)) {
    paragraph {
        val test = argument().select(PhraseInputDto::test)
        textExpr(
            Language.Bokmal to "Hei på deg".expr() + test,
        )
    }
}