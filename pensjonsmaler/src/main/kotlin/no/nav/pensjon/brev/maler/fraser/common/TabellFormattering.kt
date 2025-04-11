package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner

data class KronerText(val kroner: Expression<Kroner>, val fontType: Element.OutlineContent.ParagraphContent.Text.FontType = Element.OutlineContent.ParagraphContent.Text.FontType.PLAIN) :
    TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        textExpr(
            Bokmal to kroner.format() + " kr",
            Nynorsk to kroner.format() + " kr",
            English to "NOK ".expr() + kroner.format(),
            fontType,
        )
}

data class AntallAarText(val aar: Expression<Int>, val fontType: Element.OutlineContent.ParagraphContent.Text.FontType = Element.OutlineContent.ParagraphContent.Text.FontType.PLAIN) :
    TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        textExpr(
            Bokmal to aar.format() + " år",
            Nynorsk to aar.format() + " år",
            English to aar.format() + ifElse(aar.greaterThan(1), " years", " year" ),
            fontType,
        )
}

data class AntallMaanederText(val maaneder: Expression<Int>, val fontType: Element.OutlineContent.ParagraphContent.Text.FontType = Element.OutlineContent.ParagraphContent.Text.FontType.PLAIN) :
    TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val maanedText = maaneder.format()
        showIf(maaneder.greaterThan(1)) {
            textExpr(
                Bokmal to maanedText + " måneder",
                Nynorsk to maanedText + " måneder",
                English to maanedText + " months",
            )
        }.orShow {
            textExpr(
                Bokmal to maanedText + " måned",
                Nynorsk to maanedText + " måned",
                English to maanedText + " month",
            )
        }
    }
}


object Ja: TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            Bokmal to "Ja",
            Nynorsk to "Ja",
            English to "Yes",
        )
    }
}

object Nei: TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            Bokmal to "Nei",
            Nynorsk to "Nei",
            English to "No",
        )
    }
}