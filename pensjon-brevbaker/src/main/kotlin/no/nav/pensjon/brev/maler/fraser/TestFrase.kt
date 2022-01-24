package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.*

data class TestFraseDto(val test: String)

object TestFrase : Phrase<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, TestFraseDto> {
    override val elements = phrase {
        paragraph {
            val input = argument().select(TestFraseDto::test)
            textExpr(
                Language.Bokmal to "Hei på deg fra TestFrase: ".expr() + input,
                Language.Nynorsk to "Hei på deg frå TestFrase: ".expr() + input,
                Language.English to "Hey you, from TestFrase: ".expr() + input,
            )
        }
    }
}