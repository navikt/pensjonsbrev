package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.TextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner

data class KronerText(val kroner: Expression<Kroner>, val fontType: Element.OutlineContent.ParagraphContent.Text.FontType = Element.OutlineContent.ParagraphContent.Text.FontType.PLAIN) :
    TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            bokmal { +kroner.format(false) + " kr" },
            nynorsk { +kroner.format(false) + " kr" },
            english { +"NOK " + kroner.format(false) },
            fontType,
        )
}