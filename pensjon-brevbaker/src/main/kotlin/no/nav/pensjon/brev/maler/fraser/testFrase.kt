package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.textExpr

data class TestFraseDto(val test: String)

val testFrase = OutlinePhrase<LangBokmalNynorskEnglish, TestFraseDto> {
    paragraph {
        val input = it.select(TestFraseDto::test)
        textExpr(
            Language.Bokmal to "Hei på deg fra TestFrase: ".expr() + input,
            Language.Nynorsk to "Hei på deg frå TestFrase: ".expr() + input,
            Language.English to "Hey you, from TestFrase: ".expr() + input,
        )
    }
}