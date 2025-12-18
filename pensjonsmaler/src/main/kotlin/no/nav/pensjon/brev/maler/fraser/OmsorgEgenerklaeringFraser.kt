package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

object OmsorgEgenerklaeringTittel : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            bokmal { + "Du må sende oss egenerklæring om pleie- og omsorgsarbeid" },
            nynorsk { + "Du må sende oss eigenmelding om pleie- og omsorgsarbeid" },
            english { + "Personal declaration about the circumstances of care" },
        )
    }

}

data class OmsorgEgenerklaeringOutline(
    val aarEgenerklaringOmsorgspoeng: Expression<String>,
    val aarInnvilgetOmsorgspoeng: Expression<String>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { +
                        "Vi trenger en bekreftelse på at du har utført pleie- og omsorgsarbeid i "
                        + aarEgenerklaringOmsorgspoeng
                        + ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker." },

                nynorsk { +
                        "Vi treng ei stadfesting på at du har utført pleie- og omsorgsarbeid i "
                        + aarEgenerklaringOmsorgspoeng
                        + ". Du må difor nytte det vedlagde skjemaet og sende til oss innan fire veker." },

                english { +
                        "We need you to confirm that you have provided nursing and care work in "
                        + aarEgenerklaringOmsorgspoeng
                        + ". Therefore, it is required that you complete the enclosed form and return it to Nav within four weeks." },
            )
        }
        paragraph {
            text(
                bokmal { + "Du har fått godkjent pensjonsopptjening for " + aarInnvilgetOmsorgspoeng + "." },
                nynorsk { + "Du har fått godkjend pensjonsopptening for " + aarInnvilgetOmsorgspoeng + "." },
                english { + "You have accumulated pensionable earnings for " + aarInnvilgetOmsorgspoeng + "." },
            )
        }
        includePhrase(Felles.HarDuSpoersmaal.omsorg)
    }
}