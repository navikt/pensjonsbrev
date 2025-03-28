package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

object OmsorgEgenerklaeringTittel : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            Bokmal to "Du må sende oss egenerklæring om pleie- og omsorgsarbeid",
            Nynorsk to "Du må sende oss eigenmelding om pleie- og omsorgsarbeid",
            English to "Personal declaration about the circumstances of care",
        )
    }

}

data class OmsorgEgenerklaeringOutline(
    val aarEgenerklaringOmsorgspoeng: Expression<String>,
    val aarInnvilgetOmsorgspoeng: Expression<String>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to
                        "Vi trenger en bekreftelse på at du har utført pleie- og omsorgsarbeid i ".expr()
                        + aarEgenerklaringOmsorgspoeng
                        + " år. Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.",

                Nynorsk to
                        "Vi treng ei stadfesting på at du har utført pleie- og omsorgsarbeid i ".expr()
                        + aarEgenerklaringOmsorgspoeng
                        + " år. Du må difor nytte det vedlagde skjemaet og sende til oss innan fire veker.",

                English to
                        "We need you to confirm that you have provided nursing and care work in ".expr()
                        + aarEgenerklaringOmsorgspoeng
                        + " years. Therefore, it is required that you complete the enclosed form and return it to Nav within four weeks.",
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Du har fått godkjent pensjonsopptjening for ".expr() + aarInnvilgetOmsorgspoeng + " år.",
                Nynorsk to "Du har fått godkjend pensjonsopptening for ".expr() + aarInnvilgetOmsorgspoeng + " år.",
                English to "You have accumulated pensionable earnings for ".expr() + aarInnvilgetOmsorgspoeng + " years.",
            )
        }
        includePhrase(Felles.HarDuSpoersmaal.omsorg)
    }
}