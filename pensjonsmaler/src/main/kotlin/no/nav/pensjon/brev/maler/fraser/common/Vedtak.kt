package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.DITT_NAV
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_PENSJONIST_URL
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
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

    object EndringKanHaBetydningForSkatt : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            // skattAPendring_001
            title1 {
                text(
                    Bokmal to "Endring av alderspensjon kan ha betydning for skatt",
                    Nynorsk to "Endring av alderspensjon kan ha betyding for skatt",
                    English to "The change in your retirement pension may affect how much tax you pay"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du bør kontrollere om skattekortet ditt er riktig når alderspensjonen blir endret. Dette kan du gjøre selv på $SKATTEETATEN_PENSJONIST_URL. Der får du også mer informasjon om skattekort for pensjonister.",
                    Nynorsk to "Du bør kontrollere om skattekortet ditt er riktig når alderspensjonen blir endra. Dette kan du gjere sjølv på $SKATTEETATEN_PENSJONIST_URL. Der får du også meir informasjon om skattekort for pensjonistar.",
                    English to "When your retirement pension has been changed, you should check if your tax deduction card is correctly calculated. You can change your tax card by logging on to $SKATTEETATEN_PENSJONIST_URL. There you will find more information regarding tax deduction card for pensioners."
                )
            }
            paragraph {
                text(
                    Bokmal to "På $DIN_PENSJON_URL får du vite hva du betaler i skatt. Her kan du også legge inn ekstra skattetrekk om du ønsker det. Dersom du endrer skattetrekket vil dette gjelde fra måneden etter at vi har fått beskjed.",
                    Nynorsk to "På $DIN_PENSJON_URL får du vite kva du betaler i skatt. Her kan du også leggje inn tilleggsskatt om du ønskjer det. Dersom du endrar skattetrekket, vil dette gjelde frå månaden etter at vi har fått beskjed.",
                    English to "At $DIN_PENSJON_URL you can see how much tax you are paying. Here you can also add surtax, if you want. If you change your income tax rate, this will be applied from the month after we have been notified of the change."
                )
            }
        }
    }
}