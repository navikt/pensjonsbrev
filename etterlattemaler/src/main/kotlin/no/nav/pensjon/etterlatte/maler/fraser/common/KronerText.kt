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
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner

data class KronerText(val kroner: Expression<Kroner>, val fontType: Element.OutlineContent.ParagraphContent.Text.FontType = Element.OutlineContent.ParagraphContent.Text.FontType.PLAIN) :
    TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        textExpr(
            Language.Bokmal to kroner.format() + " kr",
            Language.Nynorsk to kroner.format() + " kr",
            Language.English to "NOK ".expr() + kroner.format(),
            fontType,
        )
}