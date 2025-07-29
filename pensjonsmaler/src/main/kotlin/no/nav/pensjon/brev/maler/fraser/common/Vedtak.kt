package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.maler.fraser.common.Constants.DITT_NAV
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate


object Vedtak {

    // vedtakOverskriftPesys_001
    object Overskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {

        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            title1 {
                text(
                    Bokmal to "Vedtak",
                    Nynorsk to "Vedtak",
                    English to "Decision",
                )
            }
    }

    /**
     * TBU1092
     */
    object BegrunnelseOverskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {

        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            title1 {
                text(
                    Bokmal to "Begrunnelse for vedtaket",
                    Nynorsk to "Grunngiving for vedtaket",
                    English to "Grounds for the decision",
                )
            }
    }

    data class Etterbetaling(val virkDatoFom: Expression<LocalDate>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Etterbetaling",
                    Nynorsk to "Etterbetaling",
                    English to "Retroactive payment"
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du får etterbetalt pensjon fra ".expr() + virkDatoFom.format() + ". Etterbetalingen vil vanligvis bli utbetalt i løpet av syv virkedager. Vi kan trekke fra skatt og ytelser du har fått fra for eksempel Nav eller tjenestepensjonsordninger. Derfor kan etterbetalingen din bli forsinket. Tjenestepensjonsordninger har ni ukers frist på å kreve trekk i etterbetalingen. Du kan sjekke eventuelle fradrag i utbetalingsmeldingen på $DITT_NAV.",
                    Nynorsk to "Du får etterbetalt pensjon frå ".expr() + virkDatoFom.format() + ". Etterbetalinga blir vanlegvis betalt ut i løpet av sju yrkedagar. Vi kan trekke frå skatt og ytingar du har fått frå for eksempel Nav eller tenestepensjonsordningar. Derfor kan etterbetalinga di bli forsinka. Tenestepensjonsordninga har ni veker frist på å krevje trekk i etterbetalinga. Du kan sjekke eventuelle frådrag i utbetalingsmeldinga på $DITT_NAV.",
                    English to "You will receive retroactive pension payments from ".expr() + virkDatoFom.format() +". The retroactive payments will normally be made in the course of seven working days. We can make deductions for tax and benefits you have received, for example, from Nav or occupational pension schemes. Therefore, your retroactive payment may be delayed. Occupational pension schemes have a deadline of nine weeks to demand a deduction from the retroactive payments. You can check if there are any deductions from the payment notice at $DITT_NAV."
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis etterbetalingen gjelder tidligere år, trekker vi skatt etter skatteetatens standardsatser.",
                    Nynorsk to "Dersom etterbetalinga gjeld tidlegare år, vil vi trekkje skatt etter standardsatsane til skatteetaten.",
                    English to "If the retroactive payment refers to earlier years, we will deduct tax at the Tax Administration's standard rates."
                )
            }
        }
    }

    object TrygdetidOverskrift: OutlinePhrase<LangBokmalNynorskEnglish>(){
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 { includePhrase(TrygdetidText) }
        }
    }

    object TrygdetidText: PlainTextOnlyPhrase<LangBokmalNynorskEnglish> (){
        override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(Bokmal to "Trygdetid", Nynorsk to "Trygdetid", English to "Period of national insurance coverage")
        }

    }
}