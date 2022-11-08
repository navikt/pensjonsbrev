package no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.maler.InnvilgetBarnetilleggSelectors.antallBarn
import no.nav.pensjon.brev.api.model.maler.InnvilgetBarnetilleggSelectors.inntektstak
import no.nav.pensjon.brev.api.model.maler.InnvilgetBarnetilleggSelectors.utbetalt_safe
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brev.maler.fraser.Constants
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import java.time.LocalDate

object Ufoeretrygd {
    /**
     * TBU3007
     */
    data class UngUfoer20aar_001(val kravVirkningFraOgMed: Expression<LocalDate>) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
            paragraph {
                val formatertDato = kravVirkningFraOgMed.format()
                textExpr(
                    Bokmal to "Vi har økt uføretrygden din fra ".expr() + formatertDato + " fordi du fyller 20 år. Du vil nå få utbetalt uføretrygd med rettighet som ung ufør.".expr(),
                    Nynorsk to "Vi har auka uføretrygda di frå ".expr() + formatertDato + " fordi du fyller 20 år. Du får no utbetalt uføretrygd med rett som ung ufør.".expr(),
                )
            }
    }

    /**
     * TBU1120, TBU1121, TBU1122, TBU1254, TBU1253, TBU1123
     */
    data class Beloep(
        val perMaaned: Expression<Kroner>,
        val ektefelle: Expression<Boolean>,
        val ufoeretrygd: Expression<Boolean>,
        val gjenlevende: Expression<Boolean>,
        val fellesbarn: Expression<Boolean>,
        val saerkullsbarn: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                val kroner = perMaaned.format()
                showIf(ufoeretrygd) {
                    showIf(not(fellesbarn) and not(saerkullsbarn) and not(ektefelle) and not(gjenlevende)) {
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd per månad før skatt.".expr(),
                            English to "Your monthly disability benefit payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }.orShowIf((fellesbarn or saerkullsbarn) and not(gjenlevende) and not(ektefelle)) {
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd og barnetillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd og barnetillegg per månad før skatt.".expr(),
                            English to "Your monthly disability benefit and child supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }.orShowIf(not(fellesbarn) and not(saerkullsbarn) and not(ektefelle) and gjenlevende) {
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd og gjenlevendetillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd og attlevandetillegg per månad før skatt.".expr(),
                            English to "Your monthly disability benefit and survivor's supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }.orShowIf((fellesbarn or saerkullsbarn) and ektefelle and not(gjenlevende)) {
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd, barne- og ektefelletillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd, barne- og ektefelletillegg per månad før skatt.".expr(),
                            English to "Your monthly disability benefit, child supplement and survivor's supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }.orShowIf(not(fellesbarn) and not(saerkullsbarn) and ektefelle and not(gjenlevende)) {
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd og ektefelletillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd og ektefelletillegg per månad før skatt.".expr(),
                            English to "Your monthly disability benefit and spouse supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }.orShowIf((fellesbarn or saerkullsbarn) and not(ektefelle) and gjenlevende) {
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i uføretrygd, barne- og gjenlevendetillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i uføretrygd, barne- og attlevandetillegg per månad før skatt.".expr(),
                            English to "Your monthly disability benefit, child supplement and spouse supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }
                }.orShow {
                    showIf((fellesbarn or saerkullsbarn) and not(ektefelle) and not(gjenlevende)) {
                        // TBU4082
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i barnetillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i barnetillegg per månad før skatt.".expr(),
                            English to "Your monthly child supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }.orShowIf((fellesbarn or saerkullsbarn) and ektefelle and not(gjenlevende)) {
                        // TBU4083
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i barne- og ektefelletillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i barne- og ektefelletillegg per månad før skatt.".expr(),
                            English to "Your monthly child supplement and spouse supplement  payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }.orShowIf(not(fellesbarn or saerkullsbarn) and not(gjenlevende) and (ektefelle)) {
                        // TBU4084
                        textExpr(
                            Bokmal to "Du får ".expr() + kroner + " kroner i ektefelletillegg per måned før skatt.".expr(),
                            Nynorsk to "Du får ".expr() + kroner + " kroner i ektefelletillegg per månad før skatt.".expr(),
                            English to "Your monthly spouse supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                        )
                    }
                }
            }
    }

    /**
     * TBU1286.1, TBU1286.2
     */
    data class BarnetilleggIkkeUtbetalt(
        val saerkullsbarn: Expression<UngUfoerAutoDto.InnvilgetBarnetillegg?>,
        val fellesbarn: Expression<UngUfoerAutoDto.InnvilgetBarnetillegg?>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            paragraph {
                val saerkullInnvilget = saerkullsbarn.notNull()
                val saerkullUtbetalt = saerkullsbarn.utbetalt_safe.ifNull(false)
                val fellesInnvilget = fellesbarn.notNull()
                val fellesUtbetalt = fellesbarn.utbetalt_safe.ifNull(false)


                ifNotNull(saerkullsbarn) { saerkullsbarn ->
                    val barnFlertall = saerkullsbarn.antallBarn.greaterThan(1)
                    val inntektstak = saerkullsbarn.inntektstak.format()

                    showIf(saerkullInnvilget and not(saerkullUtbetalt) and fellesUtbetalt and fellesInnvilget) {
                        textExpr(
                            Bokmal to "Barnetillegget for ".expr() + ifElse(
                                barnFlertall,
                                "barna",
                                "barnet"
                            ) + " som ikke bor sammen med begge foreldrene, blir ikke utbetalt fordi du alene har en samlet inntekt som er høyere enn " +
                                inntektstak + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg.".expr(),
                            Nynorsk to "Barnetillegget for ".expr() + ifElse(
                                barnFlertall,
                                "barna",
                                "barnet"
                            ) + " som ikkje bur saman med begge foreldra sine, blir ikkje utbetalt fordi du åleine har ei samla inntekt som er høgare enn " +
                                inntektstak + " kroner. Inntekta di er over grensa for å få utbetalt barnetillegg.".expr(),
                            English to "You will not receive child supplement for the ".expr() + ifElse(
                                barnFlertall,
                                "children",
                                "child"
                            ) + " who do not live together with both parents because your total income on its own is higher than NOK " +
                                inntektstak + ". You will not receive child supplement because your income exceeds the income limit.".expr()
                        )

                    }.orShowIf(saerkullInnvilget and not(saerkullUtbetalt) and not(fellesInnvilget)) {
                        textExpr(
                            Bokmal to "Barnetillegget for ".expr() + ifElse(
                                barnFlertall,
                                "barna",
                                "barnet"
                            ) + " som bor med begge sine foreldre, blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " +
                                inntektstak + " kroner. De samlede inntektene er over grensen for å få utbetalt barnetillegg. ".expr(),
                            Nynorsk to "Barnetillegget for ".expr() + ifElse(
                                barnFlertall,
                                "barna",
                                "barnet"
                            ) + " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " +
                                inntektstak + " kroner. Dei samla inntektene er over grensa for å få utbetalt barnetillegg.".expr(),
                            English to "You will not receive child supplement for the ".expr() + ifElse(
                                barnFlertall,
                                "children",
                                "child"
                            ) + " who do not live together with both parents because your total income on its own is higher than NOK " +
                                inntektstak + ". You will not receive child supplement because your income exceeds the income limit.".expr()
                        )
                    }
                }

                ifNotNull(fellesbarn) { fellesbarn ->
                    val barnFlertall = fellesbarn.antallBarn.greaterThan(1)
                    val inntektstak = fellesbarn.inntektstak.format()

                    showIf(fellesInnvilget and not(fellesUtbetalt) and saerkullUtbetalt and saerkullInnvilget) {
                        textExpr(
                            Bokmal to "Barnetillegget for ".expr() + ifElse(
                                barnFlertall,
                                "barna",
                                "barnet"
                            ) + " som bor med begge sine foreldre, blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " +
                                inntektstak + " kroner. De samlede inntektene er over grensen for å få utbetalt barnetillegg.".expr(),

                            Nynorsk to "Barnetillegget for ".expr() + ifElse(
                                barnFlertall,
                                "barna",
                                "barnet"
                            ) + " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " +
                                inntektstak + " kroner. Dei samla inntektene er over grensa for å få utbetalt barnetillegg.".expr(),
                            English to "You will not receive child supplement for the ".expr() + ifElse(
                                barnFlertall,
                                "children",
                                "child"
                            ) + " who lives together with both parents because your total income on its own is higher than NOK " +
                                inntektstak + ". You will not receive child supplement because your combined incomes exceed the income limit.".expr()
                        )

                    }.orShowIf(fellesInnvilget and not(fellesUtbetalt) and not(saerkullInnvilget)) {
                        textExpr(
                            Bokmal to "Barnetillegget for ".expr() + ifElse(
                                barnFlertall,
                                "barna",
                                "barnet"
                            ) + " som bor med begge sine foreldre, blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " +
                                inntektstak + " kroner. De samlede inntektene er over grensen for å få utbetalt barnetillegg.".expr(),
                            Nynorsk to "Barnetillegget for ".expr() + ifElse(
                                barnFlertall,
                                "barna",
                                "barnet"
                            ) + " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " +
                                inntektstak + " kroner. Dei samla inntektene er over grensa for å få utbetalt barnetillegg.".expr(),
                            English to "You will not receive child supplement for the ".expr() + ifElse(
                                barnFlertall,
                                "children",
                                "child"
                            ) + " who lives together with both parents because your total income on its own is higher than NOK " +
                                inntektstak + ". You will not receive child supplement because your combined incomes exceed the income limit.".expr()
                        )
                    }
                }
            }
    }


    /**
     * TBU3008, TBU3009, TBU3010, endrMYUngUfoer20Aar_001
     */

    data class EndringMinsteYtelseUngUfoerVed20aar(val minsteytelseVedVirkSats: Expression<Double>) : OutlinePhrase<LangBokmalNynorsk>() {

        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Du har tidligere fått innvilget uføretrygd som ung ufør. Fra og med den måneden du fyller 20 år har du rett til høyre minsteytelse.",
                    Nynorsk to "Du har tidlegare innvilga rett som ung ufør i uføretrygda di. Frå og med månaden du fyller 20 år har du rett til høgare minsteyting.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Sivilstanden din avgjør hva du kan få i minsteytelse som ung ufør:",
                    Nynorsk to "Sivilstanden din avgjer kva du kan få i minsteyting som ung ufør:",
                )
                list {
                    item {
                        text(
                            Bokmal to "Er du enslig, er minste årlige uføretrygd 2,91 ganger folketrygdens grunnbeløp.",
                            Nynorsk to "Er du einsleg, er minste årlege uføretrygd 2,91 gangar grunnbeløpet i folketrygda."
                        )
                    }
                    item {
                        text(
                            Bokmal to "Lever du sammen med en ektefelle eller samboer, er minste årlige ytelse 2,66 ganger folketrygdens grunnbeløp.",
                            Nynorsk to "Lever du saman med ein ektefelle eller sambuar, er minste årlege yting 2,66 gangar grunnbeløpet i folketrygda."
                        )
                    }
                }
            }

            paragraph {
                val satsFormatert = minsteytelseVedVirkSats.format()
                textExpr(
                    Bokmal to "Du får derfor en årlig ytelse som utgjør ".expr()
                            + satsFormatert + " ganger grunnbeløpet.",

                    Nynorsk to "Du får derfor ei årleg yting som utgjer ".expr()
                            + satsFormatert + " gangar grunnbeløpet.",
                )
            }
        }
    }

    /**
     * TBU3011
     */
    object HjemmelSivilstand : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-13 og § 22-12.",
                    Nynorsk to "Vedtaket har vi gjort etter folketrygdlova § 12-13 og § 22-12.",
                )
            }
    }

    /**
     * TBU1174
     */
    object VirkningFomOverskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            title1 {
                text(
                    Bokmal to "Dette er virkningstidspunktet ditt",
                    Nynorsk to "Dette er verknadstidspunktet ditt",
                    English to "This is your effective date",
                    )
            }
    }

    /**
     * TBU2529x
     */
    data class VirkningFraOgMed(val kravVirkningFraOgMed: Expression<LocalDate>) :
        OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
            paragraph {
                val dato = kravVirkningFraOgMed.format()
                textExpr(
                    Bokmal to "Uføretrygden din er endret fra ".expr() + dato + ". Dette kaller vi virkningstidspunktet. Du vil derfor få en ny utbetaling fra og med måneden vilkåret er oppfylt.".expr(),
                    Nynorsk to "Uføretrygda di er endra frå ".expr() + dato + ". Dette kallar vi verknadstidspunktet. Du vil derfor få ny utbetaling frå og med månaden vilkåret er oppfylt.".expr(),
                )
            }
    }

    /**
     * TBU1227, sjekkUtbetalingeneOverskrift_001, sjekkUtbetalingeneUT_001
     */
    object SjekkUtbetalingene : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Sjekk utbetalingene dine",
                    Nynorsk to "Sjekk utbetalingane dine",
                    English to "Information about your payments",
                )
            }

            paragraph {
                text(
                    Bokmal to "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. " +
                        "Du kan se alle utbetalingene du har mottatt på ${Constants.DITT_NAV}. Her kan du også endre kontonummeret ditt.",

                    Nynorsk to "Du får uføretrygd utbetalt den 20. kvar månad, eller seinast siste yrkedag før denne datoen. " +
                        "Du kan sjå alle utbetalingar du har fått på ${Constants.DITT_NAV}. Her kan du også endre kontonummeret ditt.",

                    English to "Your disability benefit will be paid on the 20th of each month or no later than the last business day before this date. " +
                        "To see all the payments you have received, go to: ${Constants.DITT_NAV}. You may also change your account number here.",
                )
            }
        }
    }
}



