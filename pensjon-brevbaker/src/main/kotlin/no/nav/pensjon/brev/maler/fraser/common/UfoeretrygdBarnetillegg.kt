package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.maler.fraser.Constants
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate


object UfoeretrygdBarnetillegg {
    // TODO: children plural
    // TBU2290
    data class VirkningsDatoForOpphoer(
        val oensketVirkningsDato: Expression<LocalDate>,
        val foedselsdatoPaaBarnetilleggOpphoert: Expression<LocalDate>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val virkningsDato = oensketVirkningsDato.format()
                val foedselsDato = foedselsdatoPaaBarnetilleggOpphoert.format()

                textExpr(
                    Bokmal to "Vi har vedtatt at barnetillegget i uføretrygden din opphører fra ".expr() + virkningsDato + " for barn født ".expr() + foedselsDato + ".".expr(),
                    Nynorsk to "Vi har stansa barnetillegget i uføretrygda di frå ".expr() + virkningsDato + " for barn fødd ".expr() + foedselsDato + ".".expr(),
                    English to "The child supplement in your disability benefit has been discontinued, effective as of ".expr() + virkningsDato + ", for child/children born ".expr() + foedselsDato + ".".expr()
                )
            }
        }
    }

    // TODO: children plural
    // TBU3920
    object BarnHarFylt18AAR : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Bokmal to "For å ha rett til barnetillegg må du forsørge barn under 18 år. Vi har vedtatt at barnetillegget i uføretrygden opphører fordi barnetbarna har fylt 18 år.".expr(),
                    Nynorsk to "For å ha rett til barnetillegg må du forsørgje barn under 18 år. Vi har stansa barnetillegget i uføretrygda fordi barnetbarna har fylt 18 år.".expr(),
                    English to "To be eligible for child supplement, you must support children under 18 years of age. The child supplement in your disability benefit has been discontinued because your childchildren has(have) turned 18 years of age.".expr()
                )
            }
        }
    }

    // TBU3800
    data class BetydningAvInntektOverskrift(
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Slik påvirker inntekt barnetillegget ditt",
                    Nynorsk to "Slik verkar inntekt inn på barnetillegget ditt",
                    English to "Income will affect your child supplement"
                )
            }
        }
    }

    // TBU2338
    data class InntektHarBetydningForSaerkullsbarnTillegg(
        val beloepNettoSaerkullsbarn: Expression<Kroner>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val sivilstand: Expression<Sivilstand>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {

                showIf(harBarnetilleggSaerkullsbarn and not(harBarnetilleggFellesbarn)) {
                    text(
                        Bokmal to "Inntekten din har betydning for hva du får i barnetillegg. Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert.",
                        Nynorsk to "Inntekta di har noko å seie for kva du får i barnetillegg. Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert.",
                        English to "Your income affects how much you receive in child supplement. If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced."
                    )
                    showIf(beloepNettoSaerkullsbarn.greaterThan(0)) {
                        text(
                            Bokmal to " Denne grensen kaller vi for fribeløp.",
                            Nynorsk to " Denne grensa kallar vi for fribeløp.",
                            English to " We call this limit the exemption amount."
                        )
                    }
                    showIf(sivilstand.isNotAnyOf(Sivilstand.ENSLIG)) {
                        text(
                            Bokmal to " Inntekten til ",
                            Nynorsk to " Inntekta til ",
                            English to " The income of your "
                        )

                        includePhrase(Felles.SivilstandEPSBestemtForm(sivilstand))

                        text(
                            Bokmal to " din har ikke betydning for størrelsen på barnetillegget.",
                            Nynorsk to " din har ikkje noko å seie for storleiken på barnetillegget.",
                            English to " does not affect the size of your child supplement."
                        )
                    }
                }
            }
        }
    }


    // TBU2339
    data class InntektHarBetydningForFellesbarnTillegg(
        val beloepNettoFellesbarn: Expression<Kroner>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val sivilstand: Expression<Sivilstand>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                showIf(harBarnetilleggFellesbarn) {
                    text(
                        Bokmal to "Inntekten til deg og ",
                        Nynorsk to "Inntekta til deg og ",
                        English to "The incomes of you and your "
                    )

                    includePhrase(Felles.SivilstandEPSBestemtForm(sivilstand))

                    text(
                        Bokmal to " din har betydning for hva du får i barnetillegg. Er inntektene over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert.",
                        Nynorsk to " din har noko å seie for kva du får i barnetillegg. Er den samla inntekta over grensa for å få utbetalt fullt barnetillegg, blir tillegget ditt redusert.",
                        English to " affects how much you receive in child supplement. If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced."
                    )
                    showIf(beloepNettoFellesbarn.greaterThan(0)) {
                        text(
                            Bokmal to " Denne grensen kaller vi for fribeløp.",
                            Nynorsk to " Denne grensa kallar vi for fribeløp.",
                            English to " We call this limit the exemption amount."
                        )
                    }.orShowIf(harBarnetilleggFellesbarn and harBarnetilleggSaerkullsbarn) {
                    text(
                        Bokmal to "Inntekten til ",
                        Nynorsk to "Inntekta til ",
                        English to "The income of your "
                    )

                    includePhrase(Felles.SivilstandEPSBestemtForm(sivilstand))

                    textExpr(
                        Bokmal to " din har kun betydning for størrelsen på barnetillegget til barna som bor sammen med begge sine foreldre.".expr(),
                        Nynorsk to " din har berre betydning for storleiken på barnetillegget til barna som bur saman med begge foreldra sine.".expr(),
                        English to " only affects the size of the child supplement for the children who live together with both parents.".expr()
                    )
                }
            }
        }
    }


    // TBU3801
    data class BetydningAvInntektEndringer(
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val sivilstand: Expression<Sivilstand>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                    showIf(harBarnetilleggFellesbarn) {
                        text(
                            Bokmal to "Endringer i inntektene til deg og ",
                            Nynorsk to "Endringar i inntektene til deg og ",
                            English to "Changes in your and your "
                        )

                        includePhrase(Felles.SivilstandEPSBestemtForm(sivilstand))

                        text(
                            Bokmal to " din kan ha betydning for barnetillegget ditt. ",
                            Nynorsk to " di kan ha betydning for barnetillegget ditt. ",
                            English to " income may affect your child supplement. "
                        )
                    }.orShowIf(harBarnetilleggSaerkullsbarn and not(harBarnetilleggFellesbarn)) {
                        text(
                            Bokmal to "Endringer i inntekten din kan ha betydning for barnetillegget ditt. ",
                            Nynorsk to "Endringar i inntekta di kan ha betydning for barnetillegget ditt. ",
                            English to "Changes in income may affect your child supplement. "
                        )
                    }
                    text(
                        Bokmal to "Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» på ${Constants.NAV_URL}.",
                        Nynorsk to "Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» på ${Constants.NAV_URL}.",
                        English to "You can easily report income changes under the menu option «disability benefit» at ${Constants.NAV_URL}."
                    )
                }
            }
        }


    // TBU1284
    data class InntektTilAvkortningFellesbarn(
        val beloepFratrukketAnnenForeldersInntekt: Expression<Kroner>,
        val beloepNettoFellesbarn: Expression<Kroner>,
        val fradragFellesbarn: Expression<Kroner>,
        val fribeloepFellesbarn: Expression<Kroner>,
        val inntektAnnenForelderFellesbarn: Expression<Kroner>,
        val inntektBruktiAvkortningFellesbarn: Expression<Kroner>,
        val grunnbeloep: Expression<Kroner>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val justeringsbeloepFellesbarn: Expression<Kroner>,
        val sivilstand: Expression<Sivilstand>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val fribeloep = fribeloepFellesbarn.format()
                val inntektAnnenForelder = inntektAnnenForelderFellesbarn.format()
                val inntektBruktiAvkortning = inntektBruktiAvkortningFellesbarn.format()
                val grunnbeloep = grunnbeloep.format()
                val inntektLavereEnnFribeloep = inntektBruktiAvkortningFellesbarn.lessThanOrEqual(fribeloepFellesbarn)
                val erIkkeRedusert = fradragFellesbarn.equalTo(0)
                val harNedjustertBarnetilleggFellesbarn = justeringsbeloepFellesbarn.greaterThan(0)
                val harBeloepFratrukketAnnenForelder = beloepFratrukketAnnenForeldersInntekt.greaterThan(0)

                showIf(not(harBarnetilleggSaerkullsbarn)) {
                    textExpr(
                        Bokmal to "Inntekten din er ".expr() + inntektBruktiAvkortning + " kroner og inntekten til ".expr(),
                        Nynorsk to "Inntekta di er ".expr() + inntektBruktiAvkortning + " kroner, og inntekta til ".expr(),
                        English to "Your income is NOK ".expr() + inntektBruktiAvkortning + " and your ".expr()
                    )

                    includePhrase(Felles.SivilstandEPSBestemtForm(sivilstand))

                    textExpr(
                        Bokmal to " din er ".expr() + inntektAnnenForelder + " kroner.".expr(),
                        Nynorsk to " din er ".expr() + inntektAnnenForelder + " kroner.".expr(),
                        English to "'s income is NOK ".expr() + inntektAnnenForelder + "."
                    )
                }
                showIf(harBeloepFratrukketAnnenForelder) {
                    textExpr(
                        Bokmal to " Folketrygdens grunnbeløp på inntil ".expr() + grunnbeloep + " kroner er holdt utenfor inntekten til ".expr(),
                        Nynorsk to " Grunnbeløpet i folketrygda på inntil ".expr() + grunnbeloep + " kroner er halde utanfor inntekta til ".expr(),
                        English to " The national insurance basic amount of up to NOK ".expr() + grunnbeloep + " has not been included in your ".expr()
                    )
                }

                includePhrase(Felles.SivilstandEPSBestemtForm(sivilstand))

                text(Bokmal to " din", Nynorsk to " din.",English to "'s income.")

                showIf(
                    not(harBarnetilleggSaerkullsbarn)
                        and harNedjustertBarnetilleggFellesbarn
                        and beloepNettoFellesbarn.greaterThan(0)
                ) {
                    textExpr(
                        Bokmal to " Til sammen er inntektene ".expr() +
                            ifElse(inntektLavereEnnFribeloep, "lavere", "høyere") +
                            " enn fribeløpet ditt på ".expr() + fribeloep + " kroner. Barnetillegget ditt er derfor".expr() +
                            ifElse(erIkkeRedusert, " ikke", "") + " redusert ut fra inntekt.".expr(),
                        Nynorsk to " Til saman er inntektene ".expr() +
                            ifElse(inntektLavereEnnFribeloep, "lågare", "høgare") +
                            " enn fribeløpet ditt på ".expr() + fribeloep + " kroner. Barnetillegget ditt er derfor".expr() +
                            ifElse(erIkkeRedusert, " ikkje", "") + " redusert ut frå inntekt.".expr(),
                        English to " Together, the incomes are ".expr() +
                            ifElse(inntektLavereEnnFribeloep, "lower", "higher") +
                            " than your exemption amount of NOK ".expr() + fribeloep + ". Therefore, your child supplement has".expr() +
                            ifElse(erIkkeRedusert, " not", "") + " been reduced on the basis of your income.".expr()
                    )
                }
                showIf(not(harBarnetilleggSaerkullsbarn)
                    and harNedjustertBarnetilleggFellesbarn
                    and beloepNettoFellesbarn.greaterThan(0)
                ) {
                    text(
                        Bokmal to " Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget.",
                        Nynorsk to " Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget.",
                        English to " What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took into account when we changed your child supplement."
                    )
                }
                showIf(
                    not(harBarnetilleggSaerkullsbarn)
                        and harNedjustertBarnetilleggFellesbarn
                        and beloepNettoFellesbarn.equalTo(0)
                ) {
                    text(
                        Bokmal to " Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                        Nynorsk to " Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                        English to " What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took into account when we changed your child supplement. You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
                    )
                }
            }
        }
    }


    // TBU1285
    data class InntektTilAvkortningSaerkullsbarn(
        val beloepNettoSaerkullsbarn: Expression<Kroner>,
        val fribeloepSaerkullsbarn: Expression<Kroner>,
        val inntektBruktIAvkortningSaerkullsbarn: Expression<Kroner>,
        val justeringsbeloepSaerkullsbarn: Expression<Kroner>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val fribeloep = fribeloepSaerkullsbarn.format()
                val inntektBruktiAvkortning = inntektBruktIAvkortningSaerkullsbarn.format()
                val hoeyereLavere = inntektBruktIAvkortningSaerkullsbarn.lessThanOrEqual(fribeloepSaerkullsbarn)
                val ikke = inntektBruktIAvkortningSaerkullsbarn.lessThanOrEqual(fribeloepSaerkullsbarn)

                showIf(
                    beloepNettoSaerkullsbarn.greaterThan(0) and justeringsbeloepSaerkullsbarn.equalTo(0)
                ) {
                    textExpr(
                        Bokmal to "Inntekten din på ".expr() + inntektBruktiAvkortning + " kroner er ".expr() +
                            ifElse(hoeyereLavere, "lavere", "høyere") +
                            " enn fribeløpet ditt på ".expr() + fribeloep + " kroner. Barnetillegget ditt er derfor ".expr() +
                            ifElse(ikke, "ikke ", "") +
                            "redusert ut fra inntekt.".expr(),
                        Nynorsk to "Inntekta di på ".expr() + inntektBruktiAvkortning + " kroner er ".expr() +
                            ifElse(hoeyereLavere, "lågare", "høgare") +
                            " enn fribeløpet ditt på ".expr() + fribeloep + " kroner. Barnetillegget ditt er derfor ".expr() +
                            ifElse(ikke, "ikkje ", "") +
                            "redusert ut frå inntekt.".expr(),
                        English to "Your income of NOK ".expr() + inntektBruktiAvkortning + " is ".expr() +
                            ifElse(hoeyereLavere, "lower", "higher") +
                            " than your exemption amount of NOK ".expr()
                            + fribeloep + ". Therefore your child supplement has ".expr() +
                            ifElse(ikke, "not ", "") + "been reduced on the basis of your income.".expr()
                    )
                    showIf(
                        beloepNettoSaerkullsbarn.greaterThan(0) and justeringsbeloepSaerkullsbarn.greaterThan(0)
                    ) {
                        textExpr(
                            Bokmal to "Inntekten din er ".expr() + inntektBruktiAvkortning + " kroner. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget.".expr(),
                            Nynorsk to "Inntekta di er ".expr() + inntektBruktiAvkortning + " kroner. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget.".expr(),
                            English to "Your income is NOK ".expr() + inntektBruktiAvkortning + ". What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took in to account when we changed your child supplement.".expr()
                        )
                    }
                    showIf(
                        beloepNettoSaerkullsbarn.equalTo(0) and justeringsbeloepSaerkullsbarn.greaterThan(0)
                    ) {
                        textExpr(
                            Bokmal to "Inntekten din er ".expr() + inntektBruktiAvkortning + " kroner. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.".expr(),
                            Nynorsk to "Inntekta di er ".expr() + inntektBruktiAvkortning + " kroner. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.".expr(),
                            English to "Your income is NOK ".expr() + inntektBruktiAvkortning + ". What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took in to account when we changed your child supplement. You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year.".expr()
                        )
                    }
                }
            }
        }
    }


    // TBU1286
    data class BarnetilleggReduksjonSaerkullsbarnFellesbarn(
        val beloepNettoSaerkullsbarn: Expression<Kroner>,
        val beloepBruttoSaerkullsbarn: Expression<Kroner>,
        val fradragSaerkullsbarn: Expression<Kroner>,
        val fradragFellesbarn: Expression<Kroner>,
        val fribeloepSaerkullsbarn: Expression<Kroner>,
        val justeringsbeloepSaerkullsbarn: Expression<Kroner>,
        val antallSaerkullsbarnInnvilget: Expression<Int>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val beloepBruttoFellesbarn: Expression<Kroner>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val beloepNettoFellesbarn: Expression<Kroner>,
        val fribeloepFellesbarn: Expression<Kroner>,
        val justeringsbeloepFellesbarn: Expression<Kroner>,
        val antallFellesbarnInnvilget: Expression<Int>,
        val sivilstand: Expression<Sivilstand>,
        val inntektBruktIAvkortningSaerkullsbarn: Expression<Kroner>,
        val inntektBruktiAvkortningFellesbarn: Expression<Kroner>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val faarUtbetaltBarnetilleggSaerkullsbarn = beloepNettoSaerkullsbarn.greaterThan(0)
            val harNedjustertBarnetilleggSaerkullsbarn = justeringsbeloepSaerkullsbarn.equalTo(0)
            val harFradragSaerkullsbarn = fradragSaerkullsbarn.greaterThan(0)

            val harFradragFellesbarn = fradragFellesbarn.greaterThan(0)
            val faarUtbetaltBarnetilleggFellesbarn = beloepNettoFellesbarn.greaterThan(0)
            val harNedjustertBarnetilleggFellesbarn = justeringsbeloepFellesbarn.equalTo(0)
            paragraph {
                val harFlereSaerkullsbarn = antallSaerkullsbarnInnvilget.greaterThan(1)
                showIf(
                    faarUtbetaltBarnetilleggSaerkullsbarn
                        and harBarnetilleggSaerkullsbarn
                        and harBarnetilleggFellesbarn
                        and harNedjustertBarnetilleggSaerkullsbarn
                ) {
                    val inntektOverFribeloep = inntektBruktIAvkortningSaerkullsbarn.greaterThan(fribeloepSaerkullsbarn)
                    textExpr(
                        Bokmal to "Inntekten din er ".expr() +
                            ifElse(inntektOverFribeloep, "høyere", "lavere") +
                            " enn ".expr() + fribeloepSaerkullsbarn.format() + " kroner, som er fribeløpet for barnetillegget til ".expr() +
                            ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                            " som ikke bor sammen med begge foreldrene. ".expr(),
                        Nynorsk to "Inntekta di er ".expr() + ifElse(inntektOverFribeloep, "høgare", "lågare") +
                            " enn ".expr() + fribeloepSaerkullsbarn.format() + " kroner, som er fribeløpet for barnetillegget til ".expr() +
                            ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                            " som ikkje bur saman med begge foreldra. ".expr(),
                        English to "Your income is ".expr() + ifElse(
                            inntektOverFribeloep,
                            "higher",
                            "lower"
                        ) + " higher than NOK ".expr() + fribeloepSaerkullsbarn.format() + ", which is the exemption amount for the child supplement for the ".expr() +
                            ifElse(harFlereSaerkullsbarn, "children who do", "child who does") +
                            " not live together with both parents. ".expr()
                    )
                    // har fradrag for enten særkull eller felles barn utbetaling.
                    // snakk om reduksjon / ikke når det kun er ett barnetillegg som er redusert.
                    showIf(
                        (harFradragSaerkullsbarn and not(harFradragFellesbarn))
                            or (not(harFradragSaerkullsbarn) and harFradragFellesbarn)
                    ) {
                        val ikkeRedusert = beloepNettoSaerkullsbarn.equalTo(beloepBruttoSaerkullsbarn)
                        textExpr(
                            Bokmal to "Dette barnetillegget er derfor ".expr() + ifElse(
                                ikkeRedusert,
                                "ikke redusert",
                                "redusert"
                            ) + " ut fra inntekt .".expr(),
                            Nynorsk to "Dette barnetillegget er derfor ".expr() + ifElse(
                                ikkeRedusert,
                                "ikkje redusert",
                                "redusert"
                            ) + " ut frå inntekt.".expr(),
                            English to "Therefore, your child supplement has ".expr() + ifElse(
                                ikkeRedusert,
                                "has not reduced",
                                "has been reduced"
                            ) + " on the basis of your income.".expr()
                        )
                    }
                    showIf(faarUtbetaltBarnetilleggFellesbarn and harNedjustertBarnetilleggFellesbarn) {
                        val ogsaa = (not(harFradragSaerkullsbarn) and not(harFradragFellesbarn)
                            or harFradragSaerkullsbarn and harFradragFellesbarn)
                        textExpr(
                            Bokmal to "Til sammen er ".expr() + ifElse(
                                ogsaa,
                                "også inntektene",
                                "inntektene"
                            ) + " til deg og ".expr(),
                            Nynorsk to "Til saman er ".expr() + ifElse(
                                ogsaa,
                                "også inntektene",
                                "inntektene"
                            ) + " til deg og ".expr(),
                            English to "Together, your income and your ".expr()
                        )

                        includePhrase(Felles.SivilstandEPSBestemtForm(sivilstand))

                        val fribeloepFellesbarn = fribeloepFellesbarn.format()
                        val harFlereFellesBarn = antallFellesbarnInnvilget.greaterThan(1)
                        val hoeyereLavere = inntektBruktiAvkortningFellesbarn.greaterThan(fribeloepFellesbarn)
                        textExpr(
                            Bokmal to " din ".expr() + ifElse(
                                hoeyereLavere,
                                "høyere",
                                "lavere"
                            ) + " enn ".expr() + fribeloepFellesbarn + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                                harFlereFellesBarn,
                                "barna",
                                "barnet"
                            ) + " som bor med begge sine foreldre. ".expr(),

                            Nynorsk to " din ".expr() + ifElse(
                                hoeyereLavere,
                                "høgare",
                                "lagare"
                            ) + " enn ".expr() + fribeloepFellesbarn + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                                harFlereFellesBarn,
                                "barna",
                                "barnet"
                            ) + " som bur saman med begge foreldra sine. ".expr(),

                            English to "'s income is ".expr() + ifElse(
                                hoeyereLavere,
                                "higher",
                                "lower"
                            ) + " than NOK ".expr() + fribeloepFellesbarn + ", which is the exemption amount for the child supplement for the ".expr() + ifElse(
                                harFlereFellesBarn,
                                "children who live",
                                "child who lives"
                            ) + " together with both parents. ".expr()
                        )
                        showIf(
                            ((harFradragSaerkullsbarn and not(harFradragFellesbarn))
                                or (not(harFradragSaerkullsbarn) and harFradragFellesbarn)
                                and beloepNettoFellesbarn.notEqualTo(0) and harNedjustertBarnetilleggFellesbarn)
                        ) {
                            val ikkeRedusert = beloepNettoFellesbarn.equalTo(beloepBruttoFellesbarn)
                            textExpr(
                                Bokmal to "Dette barnetillegget er derfor ".expr() +
                                    ifElse(ikkeRedusert,"ikke redusert","redusert") + " ut fra inntekt.".expr(),
                                Nynorsk to "Dette barnetillegget er derfor ".expr() +
                                    ifElse(ikkeRedusert,"ikkje redusert","redusert") + " ut frå inntekt.".expr(),
                                English to "Therefore, your child supplement has ".expr() +
                                    ifElse(ikkeRedusert,"not been reduced","been reduced") + " on the basis of your income.".expr()
                            )
                        }.orShowIf(
                            (not(harFradragSaerkullsbarn) and not(harFradragFellesbarn))
                                or (harFradragSaerkullsbarn and harFradragFellesbarn)
                                and beloepNettoSaerkullsbarn.notEqualTo(0) and beloepNettoFellesbarn.notEqualTo(0)
                                and harNedjustertBarnetilleggSaerkullsbarn
                                and harNedjustertBarnetilleggFellesbarn
                        ) {
                            val ikkeRedusert =beloepNettoFellesbarn.equalTo(beloepBruttoFellesbarn) and
                                beloepNettoSaerkullsbarn.equalTo(beloepBruttoSaerkullsbarn)
                            textExpr(
                                Bokmal to "Barnetilleggene er derfor ".expr() +
                                    ifElse(ikkeRedusert,"ikke redusert","redusert") + " ut fra inntekt.".expr(),
                                Nynorsk to "Desse barnetillegga er derfor ".expr() +
                                    ifElse(ikkeRedusert,"ikkje redusert","redusert") + " ut frå inntekt.".expr(),
                                English to "Therefore your child supplements have ".expr() +
                                    ifElse(ikkeRedusert,"not been reduced","have been reduced") + " on the basis of your income.".expr()
                            )
                        }
                    }
                }
            }

            showIf(
                justeringsbeloepFellesbarn.notEqualTo(0)
            ) {
                val barnFlertall = antallFellesbarnInnvilget.greaterThan(1)
                paragraph {
                    textExpr(
                        Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for ".expr() + ifElse(
                            barnFlertall,
                            "barna",
                            "barnet"
                        ) + " som bor med begge sine foreldre.".expr(),
                        Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for ".expr() + ifElse(
                            barnFlertall,
                            "barna",
                            "barnet"
                        ) + " som bur saman med begge foreldra sine.".expr(),
                        English to "What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took in to account when we changed your child supplement for the ".expr() + ifElse(
                            barnFlertall,
                            "children who live",
                            "child who lives"
                        ) + " together with both parents.".expr()
                    )
                    showIf(beloepNettoFellesbarn.equalTo(0)) {
                        text(
                            Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året",
                            Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                            English to "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
                        )
                    }
                }
            }

            showIf(justeringsbeloepSaerkullsbarn.notEqualTo(0)) {
                val barnFlertall = antallSaerkullsbarnInnvilget.greaterThan(1)
                textExpr(
                    Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for ".expr() + ifElse(
                        barnFlertall,
                        "barna",
                        "barnet"
                    ) + " som ikke bor sammen med begge foreldrene.".expr(),
                    Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for ".expr() + ifElse(
                        barnFlertall,
                        "barna",
                        "barnet"
                    ) + " som ikkje bur saman med begge foreldra. ".expr(),
                    English to "What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took in to account when we changed your child supplement for the ".expr() + ifElse(
                        barnFlertall,
                        "children who live",
                        "child who lives"
                    ) + " together with both parents.".expr()
                )
                showIf(beloepNettoSaerkullsbarn.equalTo(0)) {
                    text(
                        Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                        Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                        English to "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
                    )
                }
            }
        }
    }


    // TBU2490 Barnetilleggene for både særkullsbarn og fellesbarn er innvilget og ingen av dem blir utbetalt
    data class InnvilgetOgIkkeUtbetalt(
        val beloepNettoFellesbarn: Expression<Kroner>,
        val beloepNettoSaerkullsbarn: Expression<Kroner>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val inntektstakFellesbarn: Expression<Kroner>,
        val inntektstakSaerkullsbarn: Expression<Kroner>,
        val justeringsbeloepFellesbarn: Expression<Kroner>,
        val justeringsbeloepSaerkullsbarn: Expression<Kroner>,
        val antallSaerkullsbarnInnvilget: Expression<Int>,
        val antallFellesbarnInnvilget: Expression<Int>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val barnFlertallSaerkullsbarn = antallSaerkullsbarnInnvilget.greaterThan(1)
                val barnFlertallFellesbarn = antallFellesbarnInnvilget.greaterThan(1)

                showIf(
                    harBarnetilleggFellesbarn and harBarnetilleggSaerkullsbarn
                        and beloepNettoFellesbarn.equalTo(0) and beloepNettoSaerkullsbarn.equalTo(0)
                        and justeringsbeloepFellesbarn.equalTo(0) and justeringsbeloepSaerkullsbarn.equalTo(
                        0
                    )
                ) {
                    textExpr(
                        Bokmal to "Barnetillegget for ".expr() + ifElse(
                            barnFlertallFellesbarn,
                            "barna",
                            "barnet"
                        ) + " som bor med begge sine foreldre, blir ikke utbetalt fordi de samlede inntektene er høyere enn ".expr()
                            + inntektstakFellesbarn.format() + " kroner. Barnetillegget for ".expr() + ifElse(
                            barnFlertallSaerkullsbarn,
                            "barna",
                            "barnet"
                        ) + " som ikke bor sammen med begge foreldrene, blir heller ikke utbetalt fordi inntekten din alene er høyere enn ".expr()
                            + inntektstakSaerkullsbarn.format() + " kroner. Inntektene er over grensen for å få utbetalt barnetillegg.".expr(),
                        Nynorsk to "Barnetillegget for ".expr() + ifElse(
                            barnFlertallFellesbarn,
                            "barna",
                            "barnet"
                        ) + " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei samla inntektene er høgare enn ".expr()
                            + inntektstakFellesbarn.format() + " kroner. Barnetillegget for ".expr() + ifElse(
                            barnFlertallSaerkullsbarn,
                            "barna",
                            "barnet"
                        ) + " som ikkje bur saman med begge foreldra, blir heller ikkje utbetalt fordi inntekta di åleine er høgare enn ".expr()
                            + inntektstakSaerkullsbarn.format() + " kroner. Inntektene er over grensa for å få utbetalt barnetillegg.".expr(),
                        English to "You will not receive child supplement for the ".expr() + ifElse(
                            barnFlertallFellesbarn,
                            "children",
                            "child"
                        ) + " who lives together with both parents because your total income is higher than NOK ".expr()
                            + inntektstakFellesbarn.format() + ". You will not receive child supplement for the ".expr() + ifElse(
                            barnFlertallSaerkullsbarn,
                            "children",
                            "child"
                        ) + " who do not live together with both parents because your income alone is higher than NOK ".expr()
                            + inntektstakSaerkullsbarn.format() + ". You will not receive child supplement because your income exceeds the income limit.".expr()
                    )
                }
            }
        }
    }

    // TBU1288
    data class HenvisningTilVedleggOpplysningerOmBeregning(
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {

                showIf(harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn)
                {
                    text(
                        Bokmal to "Du kan lese mer om beregningen av barnetillegg i vedlegget «Opplysninger om beregningen».",
                        Nynorsk to "Du kan lese meir om berekninga av barnetillegg i vedlegget «Opplysningar om berekninga».",
                        English to "Read more about how child supplements are calculated in the attachment called «Information about calculations»."
                    )
                }
            }
        }
    }
}









