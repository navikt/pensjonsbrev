package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

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

