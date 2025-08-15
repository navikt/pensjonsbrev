package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.api.model.GarantipensjonSatsType
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.dsl.universalText
import no.nav.pensjon.brevbaker.api.model.Kroner

data class KronerText(
    val kroner: Expression<Kroner>,
    val fontType: FontType = FontType.PLAIN
) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        textExpr(
            Bokmal to kroner.format() + " kr",
            Nynorsk to kroner.format() + " kr",
            English to "NOK ".expr() + kroner.format(),
            fontType,
        )
}

data class AntallAarText(
    val aar: Expression<Int>,
    val fontType: FontType = FontType.PLAIN
) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        textExpr(
            Bokmal to aar.format() + " år",
            Nynorsk to aar.format() + " år",
            English to aar.format() + ifElse(aar.greaterThan(1), " years", " year"),
            fontType,
        )
}

data class AntallMaanederText(val maaneder: Expression<Int>, val fontType: FontType = FontType.PLAIN) :
    TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val maanedText = maaneder.format()
        showIf(maaneder.greaterThan(1)) {
            textExpr(
                Bokmal to maanedText + " måneder",
                Nynorsk to maanedText + " månadar",
                English to maanedText + " months",
            )
        }.orShow {
            textExpr(
                Bokmal to maanedText + " måned",
                Nynorsk to maanedText + " månad",
                English to maanedText + " month",
            )
        }
    }
}

data class BroekText(
    val teller: Expression<Int>,
    val nevner: Expression<Int>,
    val fontType: FontType = FontType.PLAIN
) :TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        universalText(teller.format() + "/" + nevner.format())
}


object Ja : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            Bokmal to "Ja",
            Nynorsk to "Ja",
            English to "Yes",
        )
    }
}

object Nei : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            Bokmal to "Nei",
            Nynorsk to "Nei",
            English to "No",
        )
    }
}

data class GarantipensjonSatsTypeText(
    val satsType: Expression<GarantipensjonSatsType>,
) : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val hoysats = satsType.equalTo(GarantipensjonSatsType.HOY)
        val ordinaer = satsType.equalTo(GarantipensjonSatsType.ORDINAER)
        showIf(hoysats) {
            text(
                Bokmal to "høy sats",
                Nynorsk to "høg sats",
                English to "high rate",
            )
        }.orShowIf(ordinaer) {
            text(
                Bokmal to "ordinær sats",
                Nynorsk to "ordinær sats",
                English to "ordinary rate",
            )
        }

    }

}