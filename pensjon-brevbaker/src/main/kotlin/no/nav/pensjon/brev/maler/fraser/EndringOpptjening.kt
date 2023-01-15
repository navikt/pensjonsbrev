package no.nav.pensjon.brev.maler.fraser

import java.time.LocalDate
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

object EndringOpptjening {
    // TBU2220
    object OpplysningerFraSkatteetaten : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                text(
                    Bokmal to "Vi har mottatt nye opplysninger fra Skatteetaten, og har derfor beregnet uføretrygden din på nytt.",
                    Nynorsk to "",
                    English to ""
                )
            }
    }


    // TBU2221, TBU2222
    data class BetydningForUfoeretrygden(
        val ufoerBeloepOekt: Expression<Boolean>,
        val ufoerBeloepRedusert: Expression<Boolean>,
        val virkningsDato: Expression<LocalDate>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                showIf(ufoerBeloepOekt or ufoerBeloepRedusert) {
                    textExpr(
                        Bokmal to "Utbetalingen er endret med virkning fra ".expr() + virkningsDato.format() + ".".expr(),
                        Nynorsk to "".expr(),
                        English to "".expr()
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før.".expr(),
                        Nynorsk to "".expr(),
                        English to "".expr()
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
                    Nynorsk to "",
                    English to "The calculation of disability benefits is based upon your income in the years before the disibilty. We recalculate the disibility benefit if you have changes in"
                )
                list {
                    item {

                        text(
                            Bokmal to "pensjonsgivende inntekt",
                            Nynorsk to "pensjonsgivende inntekt",
                            English to "pensionable income"
                        )
                        text(
                            Bokmal to "omsorgspoeng",
                            Nynorsk to "omsorgspoeng",
                            English to "acquired care rights"
                        )
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

        // TBU2225, TBU226
        data class EndringIOpptjeningTilUfoeretrygd(
            val ufoerBeloepOekt: Expression<Boolean>,
            val ufoerBeloepRedusert: Expression<Boolean>,
            val virkningsDato: Expression<LocalDate>,
        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                paragraph {
                    showIf(ufoerBeloepOekt) {
                        textExpr(
                            Bokmal to "Du har fått en endring i opptjeningen din før du ble ufør. Dette gir deg en økt uføretrygd fra ".expr() + virkningsDato.format() + ". Du vil derfor motta en etterbetaling fra NAV.".expr(),
                            Nynorsk to "".expr(),
                            English to "".expr()
                        )
                    }.orShow {
                        textExpr(
                            Bokmal to "Du har fått en endring i opptjeningen din før du ble ufør. Dette gir deg en redusert uføretrygd fra ".expr() + virkningsDato.format() + ".".expr(),
                            Nynorsk to "".expr(),
                            English to "".expr()
                        )
                    }
                }
            }
        }
    }
