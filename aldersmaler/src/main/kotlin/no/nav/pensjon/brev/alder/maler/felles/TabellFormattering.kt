package no.nav.pensjon.brev.alder.maler.felles

import no.nav.pensjon.brev.alder.model.GarantipensjonSatsType
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner

data class KronerText(
    val kroner: Expression<Kroner>,
    val fontType: FontType = FontType.PLAIN
) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { + kroner.format(false) + " kr" },
            nynorsk { + kroner.format(false) + " kr" },
            english { + "NOK " + kroner.format(false) },
            fontType,
        )
}

data class AntallAarText(
    val aar: Expression<Int>,
    val fontType: FontType = FontType.PLAIN
) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { + aar.format() + " år" },
            nynorsk { + aar.format() + " år" },
            english { + aar.format() + ifElse(aar.greaterThan(1), " years", " year") },
            fontType,
        )
}

data class AntallMaanederText(val maaneder: Expression<Int>, val fontType: FontType = FontType.PLAIN) :
    TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val maanedText = maaneder.format()
        showIf(maaneder.greaterThan(1)) {
            text(
                bokmal { + maanedText + " måneder" },
                nynorsk { + maanedText + " månadar" },
                english { + maanedText + " months" },
            )
        }.orShow {
            text(
                bokmal { + maanedText + " måned" },
                nynorsk { + maanedText + " månad" },
                english { + maanedText + " month" },
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
        eval(teller.format() + "/" + nevner.format())
}


object Ja : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            bokmal { + "Ja" },
            nynorsk { + "Ja" },
            english { + "Yes" },
        )
    }
}

object Nei : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            bokmal { + "Nei" },
            nynorsk { + "Nei" },
            english { + "No" },
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
                bokmal { + "høy sats" },
                nynorsk { + "høg sats" },
                english { + "high rate" },
            )
        }.orShowIf(ordinaer) {
            text(
                bokmal { + "ordinær sats" },
                nynorsk { + "ordinær sats" },
                english { + "ordinary rate" },
            )
        }

    }

}