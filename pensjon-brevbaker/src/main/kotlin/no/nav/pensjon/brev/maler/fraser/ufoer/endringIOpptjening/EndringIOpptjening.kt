package no.nav.pensjon.brev.maler.fraser.ufoer.endringIOpptjening

import java.time.LocalDate
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

object EndringIOpptjening {
    // TBU2220
    object OpplysningerFraSkatteetaten : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Bokmal to "Vi har mottatt nye opplysninger fra Skatteetaten, og har derfor beregnet uføretrygden din på nytt.",
                    Nynorsk to "Vi har mottatt nye opplysningar fra Skatteetaten, og har difor berekna uføretrygda di på nytt.",
                    English to "We have received new information from the Norwegian tax administration «Skatteetaten», and we have therefore recalculated your disability benefit."
                )
            }
    }


    // TBU2221, TBU2222
    data class BetydningForUfoeretrygden(
        val harBeloepOekt: Expression<Boolean>,
        val harBeloepRedusert: Expression<Boolean>,
        val virkningsDato: Expression<LocalDate>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                showIf(harBeloepOekt or harBeloepRedusert) {
                    textExpr(
                        Bokmal to "Utbetalingen er endret med virkning fra ".expr() + virkningsDato.format() + ".".expr(),
                        Nynorsk to "Utbetalinga er endra med virkning frå ".expr() + virkningsDato.format() + ".".expr(),
                        English to "The payment is changed with effect from ".expr() + virkningsDato.format() + ".".expr()
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før.".expr(),
                        Nynorsk to "Dette får ikkje betydning for uføretrygda di, og du vil få utbetalt det same som før.".expr(),
                        English to "This will not affect your disability benefit, and you will receive the same payment as before.".expr()
                    )
                }
            }
        }
    }

    // TBU2224
    object UfoerInntektListe : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Uføretrygden blir beregnet ut fra inntektsårene før du ble ufør. Vi beregner uføretrygden på nytt dersom du får endringer i",
                    Nynorsk to "Uføretrygda blir berekna ut frå inntektsårene før du ble ufør. Vi bereknar uføretrygda på nytt dersom du får endringar i",
                    English to "The calculation of disability benefits is based upon your income in the years before the disibilty. We recalculate the disibility benefit if you have changes in"
                )
                list {
                    item {
                        text(
                            Bokmal to "pensjonsgivende inntekt",
                            Nynorsk to "pensjonsgivende inntekt",
                            English to "pensionable income"
                        )
                    }
                    item {
                        text(
                            Bokmal to "omsorgspoeng",
                            Nynorsk to "omsorgspoeng",
                            English to "acquired care rights"
                        )
                    }
                    item {
                        text(
                            Bokmal to "år med inntekt i utlandet",
                            Nynorsk to "år med inntekt i utlandet",
                            English to "income in another country"
                        )
                    }
                }
            }
        }
    }


    // TBU2225, TBU2226
    data class EndringIOpptjeningTilUfoeretrygd(
        val harBeloepOekt: Expression<Boolean>,
        val harBeloepRedusert: Expression<Boolean>,
        val virkningsDato: Expression<LocalDate>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                showIf(harBeloepOekt) {
                    textExpr(
                        Bokmal to "Du har fått en endring i opptjeningen din før du ble ufør. Dette gir deg en økt uføretrygd fra ".expr() + virkningsDato.format() + ". Du vil derfor motta en etterbetaling fra NAV.".expr(),
                        Nynorsk to "Du har fått ein endring i oppteninga di før du ble ufør. Dette gir deg ein auka uføretrygd frå ".expr() + virkningsDato.format() + ". Du vil difor motta ein etterbetaling frå NAV.".expr(),
                        English to "You have had a change in earnings before you became eligible for disability benefit. This gives you an increase in disability benefit from ".expr() + virkningsDato.format() + ". You will therefore receive an arrears payment from NAV.".expr()
                    )
                }.orShowIf(harBeloepRedusert) {
                    textExpr(
                        Bokmal to "Du har fått en endring i opptjeningen din før du ble ufør. Dette gir deg en redusert uføretrygd fra ".expr() + virkningsDato.format() + ".".expr(),
                        Nynorsk to "Du har fått ein endring i oppteninga di før du ble ufør. Dette gir deg ein redusert uføretrygd frå ".expr() + virkningsDato.format() + ".".expr(),
                        English to "You have had a change in earnings before you became eligible for disability benefit. This gives you a reduction in disability benefit from ".expr() + virkningsDato.format() + ".".expr()
                    )
                }
            }
        }
    }

    // TBU3224
    data class EtterbetalingAvUfoeretrygd(
        val harBeloepOekt: Expression<Boolean>,
        val ufoeregrad: Expression<Int>,
        val utbetalingsgrad: Expression<Int>,
        val virkningsDato: Expression<LocalDate>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(harBeloepOekt and utbetalingsgrad.equalTo(ufoeregrad)) {
                title1 {
                    text(
                        Bokmal to "Etterbetaling av uføretrygd",
                        Nynorsk to "Etterbetaling av uføretrygd",
                        English to "Disability benefit paid in arrears"
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Du får etterbetalt uføretrygd fra ".expr() + virkningsDato.format() + ". Beløpet blir vanligvis utbetalt i løpet av sju virkedager. Det kan bli beregnet fradrag i etterbetalingen for skatt og ytelser du har mottatt fra NAV eller andre, som for eksempel tjenestepensjonsordninger. I disse tilfellene kan etterbetalingen bli forsinket med inntil ni uker. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen.".expr(),
                        Nynorsk to "Du får etterbetalt uføretrygd frå ".expr() + virkningsDato.format() + ". Beløpet blir vanlegvis utbetalt i løpet av sju verkedagar. Det kan bli berekna fradrag i etterbetalinga for skatt og ytingar du har mottatt frå NAV eller andre, som for eksempel tjenestepensjonsordninga. I desse tilfella kan etterbetalinga bli forseinka med inntil ni uka. Fradrag i etterbetallinga vil gå fram av utbetalingsmeldinga.".expr(),
                        English to "You will receive disbability benefit paid in arrears from ".expr() + virkningsDato.format() + ". The payment is usually made within a week. The arrears payment can include deductutions for tax and benefits you have received from NAV or others, for example occupational pension schemes. In these cases the payment in arrears can be delayed upto nine weeks. Any deductions will be listed on the payment notification.".expr()
                    )
                }
            }
        }
    }

    // TBU2530
    data class TilbakekrevingAvUfoeretrygd(
        val harBeloepRedusert: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

            showIf(harBeloepRedusert) {
                title1 {
                    text(
                        Bokmal to "Tilbakekreving av uføretrygd",
                        Nynorsk to "Tilbakekreving av uføretrygd",
                        English to "Repayment of disability benefit"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Fordi uføretrygden din er redusert tilbake i tid, betyr dette at du har fått utbetalt for mye i uføretrygd. Du får et eget brev med varsel om eventuell tilbakekreving av det feilutbetalte beløpet.",
                        Nynorsk to "Fordi uføretrygda di er redusert tilbake i tid, betyr dette at du har fått utbetalt for mykje i uføretrygd. Du får eit eget brev med varsel om eventuell tilbakekrevjing av det feilutbetalte beløpet.",
                        English to "Your disability benefit was reduced in the past, this means you have been paid too much disability benefit. You will recieve a letter notifying you of the demand for the repayment of the overpayment of disability benefit."
                    )
                }
            }
        }
    }
}

