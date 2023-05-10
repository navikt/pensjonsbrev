package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate


object Barnetillegg {
    // TBU2290
    data class VirkningsDatoForOpphoer(
        val oensketVirkningsDato: Expression<LocalDate>,
        val foedselsdatoPaaBarnMedOpphoertBarnetillegg: Expression<List<LocalDate>>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val virkningsDato = oensketVirkningsDato.format()
                val barnFlertall = foedselsdatoPaaBarnMedOpphoertBarnetillegg.size().greaterThan(1)

                textExpr(
                    Bokmal to "Vi har vedtatt at barnetillegget i uføretrygden din opphører fra ".expr() + virkningsDato +
                            " for " + ifElse(barnFlertall, "barna", "barn") + " født",
                    Nynorsk to "Vi har stansa barnetillegget i uføretrygda di frå ".expr() + virkningsDato +
                            " for " + ifElse(barnFlertall, "barna", "barn") + " fødd",
                    English to "The child supplement in your disability benefit has been discontinued, effective as of ".expr() + virkningsDato +
                            ", for " + ifElse(barnFlertall, "children", "the child") + " born",
                )
                includePhrase(Felles.TextOrList(foedselsdatoPaaBarnMedOpphoertBarnetillegg.map(BinaryOperation.LocalizedDateFormat)))
            }
        }
    }

    // TBU3920
    data class BarnHarFylt18AAR(
        val opphoertBarnetilleggFlereBarn: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val barnFlertall = opphoertBarnetilleggFlereBarn
                textExpr(
                    Bokmal to "For å ha rett til barnetillegg må du forsørge barn under 18 år. Vi har vedtatt at barnetillegget i uføretrygden opphører fordi ".expr() +
                            ifElse(barnFlertall, "barna", "barnet") + " har fylt 18 år.",
                    Nynorsk to "For å ha rett til barnetillegg må du forsørgje barn under 18 år. Vi har stansa barnetillegget i uføretrygda fordi ".expr() +
                            ifElse(barnFlertall, "barna", "barnet") + " har fylt 18 år.",
                    English to "To be eligible for child supplement, you must support children under 18 years of age. The child supplement in your disability benefit has been discontinued because your ".expr() +
                            ifElse(barnFlertall, "children have", "child has") + " turned 18 years of age."
                )
            }
        }
    }

    // TBU2338
    data class InntektHarBetydningForSaerkullsbarnTillegg(
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val sivilstand: Expression<Sivilstand>,
        val faarUtbetaltBarnetilleggSaerkullsbarn: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

            showIf(harBarnetilleggSaerkullsbarn and not(harBarnetilleggFellesbarn)) {
                paragraph {
                    text(
                        Bokmal to "Inntekten din har betydning for hva du får i barnetillegg. Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert.",
                        Nynorsk to "Inntekta di har noko å seie for kva du får i barnetillegg. Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert.",
                        English to "Your income affects how much you receive in child supplement. If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced."
                    )
                    showIf(faarUtbetaltBarnetilleggSaerkullsbarn) {
                        text(
                            Bokmal to " Denne grensen kaller vi for fribeløp.",
                            Nynorsk to " Denne grensa kallar vi for fribeløp.",
                            English to " We call this limit the exemption amount."
                        )
                    }
                    showIf(sivilstand.isNotAnyOf(Sivilstand.ENSLIG, Sivilstand.ENKE, Sivilstand.SEPARERT)) {
                        textExpr(
                            Bokmal to " Inntekten til ".expr() + sivilstand.bestemtForm() + " din har ikke betydning for størrelsen på barnetillegget.",
                            Nynorsk to " Inntekta til ".expr() + sivilstand.bestemtForm() + " din har ikkje noko å seie for storleiken på barnetillegget.",
                            English to " The income of your ".expr() + sivilstand.bestemtForm() + " does not affect the amount of your child supplement.",
                        )
                    }
                }
            }
        }
    }


    // TBU2339
    data class InntektHarBetydningForFellesbarnTillegg(
        val faarUtbetaltBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val harFlereFellesbarn: Expression<Boolean>,
        val sivilstand: Expression<Sivilstand>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(harBarnetilleggFellesbarn) {
                paragraph {
                    textExpr(
                        Bokmal to "Inntekten til deg og ".expr() + sivilstand.bestemtForm() + " din har betydning for hva du får i barnetillegg. Er inntektene over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert.",
                        Nynorsk to "Inntekta til deg og ".expr() + sivilstand.bestemtForm() + " din har noko å seie for kva du får i barnetillegg. Er den samla inntekta over grensa for å få utbetalt fullt barnetillegg, blir tillegget ditt redusert.",
                        English to "The incomes of you and your ".expr() + sivilstand.bestemtForm() + " influence how much you receive in child supplement. If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced.",
                    )
                    showIf(faarUtbetaltBarnetilleggFellesbarn) {
                        text(
                            Bokmal to " Denne grensen kaller vi for fribeløp.",
                            Nynorsk to " Denne grensa kallar vi for fribeløp.",
                            English to " We call this limit the exemption amount."
                        )
                    }
                    showIf(harBarnetilleggSaerkullsbarn) {
                        textExpr(
                            Bokmal to " Inntekten til ".expr() + sivilstand.bestemtForm() + " din har kun betydning for størrelsen på barnetillegget til ".expr() +
                                    ifElse(
                                        harFlereFellesbarn,
                                        "barna",
                                        "barnet"
                                    ) + " som bor sammen med begge sine foreldre.".expr(),
                            Nynorsk to " Inntekta til ".expr() + sivilstand.bestemtForm() + " din har berre betydning for storleiken på barnetillegget til ".expr() +
                                    ifElse(
                                        harFlereFellesbarn,
                                        ifTrue = "barna",
                                        ifFalse = "barnet"
                                    ) + " som bur saman med begge foreldra sine.".expr(),
                            English to " The income of your ".expr() + sivilstand.bestemtForm() + " only affects the amount of child supplement for the ".expr() +
                                    ifElse(
                                        harFlereFellesbarn,
                                        ifTrue = "children who live",
                                        ifFalse = "child who lives"
                                    ) + " together with both parents.",
                        )
                    }
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
            showIf(harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn) {
                paragraph {
                    showIf(harBarnetilleggFellesbarn) {
                        textExpr(
                            Bokmal to "Endringer i inntektene til deg og ".expr() + sivilstand.bestemtForm() + " din kan ha betydning for barnetillegget ditt. ",
                            Nynorsk to "Endringar i inntektene til deg og ".expr() + sivilstand.bestemtForm() + " di kan ha betydning for barnetillegget ditt. ",
                            English to "Changes in your and your ".expr() + sivilstand.bestemtForm() + "'s income may affect your child supplement. ",
                        )
                    }.orShowIf(harBarnetilleggSaerkullsbarn) {
                        text(
                            Bokmal to "Endringer i inntekten din kan ha betydning for barnetillegget ditt. ",
                            Nynorsk to "Endringar i inntekta di kan ha betydning for barnetillegget ditt. ",
                            English to "Changes in income may affect your child supplement. "
                        )
                    }
                    text(
                        Bokmal to "Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» på ${Constants.NAV_URL}.",
                        Nynorsk to "Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» på ${Constants.NAV_URL}.",
                        English to "You can report income changes under the menu option “disability benefit” at ${Constants.NAV_URL}."
                    )
                }
            }
        }
    }


    // TBU1284
    data class InntektTilAvkortningFellesbarn(
        val harBeloepFratrukketAnnenForelder: Expression<Boolean>,
        val faarUtbetaltBarnetilleggFellesBarn: Expression<Boolean>,
        val harFradragFellesbarn: Expression<Boolean>,
        val fribeloepFellesbarn: Expression<Kroner>,
        val grunnbeloep: Expression<Kroner>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val inntektAnnenForelderFellesbarn: Expression<Kroner>,
        val brukersInntektBruktiAvkortningFellesbarn: Expression<Kroner>,
        val harJusteringsbeloepFellesbarn: Expression<Boolean>,
        val sivilstand: Expression<Sivilstand>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val grunnbeloep = grunnbeloep.format()

            paragraph {
                textExpr(
                    Bokmal to "Inntekten din er ".expr() + brukersInntektBruktiAvkortningFellesbarn.format() + " kroner og inntekten til " + sivilstand.bestemtForm() + " din er " + inntektAnnenForelderFellesbarn.format() + " kroner.",
                    Nynorsk to "Inntekta di er ".expr() + brukersInntektBruktiAvkortningFellesbarn.format() + " kroner, og inntekta til " + sivilstand.bestemtForm() + " din er " + inntektAnnenForelderFellesbarn.format() + " kroner.",
                    English to "Your income is NOK ".expr() + brukersInntektBruktiAvkortningFellesbarn.format() + " and your " + sivilstand.bestemtForm() + "'s income is NOK " + inntektAnnenForelderFellesbarn.format() + ".",
                )

                showIf(harBeloepFratrukketAnnenForelder) {
                    textExpr(
                        Bokmal to " Folketrygdens grunnbeløp på inntil ".expr() + grunnbeloep + " kroner er holdt utenfor inntekten til "
                                + sivilstand.bestemtForm() + " din.",
                        Nynorsk to " Grunnbeløpet i folketrygda på inntil ".expr() + grunnbeloep + " kroner er halde utanfor inntekta til "
                                + sivilstand.bestemtForm() + " din.",
                        English to " The national insurance basic amount of up to NOK ".expr() + grunnbeloep + " has not been included in your "
                                + sivilstand.bestemtForm() + "'s income.",
                    )
                }

                showIf(not(harBarnetilleggSaerkullsbarn) and not(harJusteringsbeloepFellesbarn) and faarUtbetaltBarnetilleggFellesBarn) {
                    textExpr(
                        Bokmal to " Til sammen er inntektene ".expr() +
                                ifElse(harFradragFellesbarn, "høyere", "lavere") +
                                " enn fribeløpet ditt på " + fribeloepFellesbarn.format() + " kroner. Barnetillegget ditt er derfor" +
                                ifElse(harFradragFellesbarn, "", " ikke") + " redusert ut fra inntekt.",

                        Nynorsk to " Til saman er inntektene ".expr() +
                                ifElse(harFradragFellesbarn, "høgare", "lågare") +
                                " enn fribeløpet ditt på " + fribeloepFellesbarn.format() + " kroner. Barnetillegget ditt er derfor" +
                                ifElse(harFradragFellesbarn, "", " ikkje") +
                                " redusert ut frå inntekt.",

                        English to " Together, the incomes are ".expr() +
                                ifElse(harFradragFellesbarn, "higher", "lower") +
                                " than your exemption amount of NOK " + fribeloepFellesbarn.format() +
                                ". Therefore, your child supplement has" +
                                ifElse(harFradragFellesbarn, "", " not") +
                                " been reduced on the basis of your income."
                    )
                }

                showIf(harJusteringsbeloepFellesbarn and not(harBarnetilleggSaerkullsbarn)) {
                    includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
                    text(
                        Bokmal to " Dette ble tatt hensyn til da vi endret barnetillegget.",
                        Nynorsk to " Dette har vi teke omsyn til når vi endra barnetillegget.",
                        English to " This we took into account when we changed your child supplement."
                    )
                    showIf(not(faarUtbetaltBarnetilleggFellesBarn)) {
                        text(
                            Bokmal to " Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                            Nynorsk to " Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                            English to " You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
                        )
                    }
                }
            }
        }
    }


    object DuHarFaattUtbetaltBarnetilleggTidligereIAar : ParagraphPhrase<LangBokmalNynorskEnglish>() {
        override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover.",
                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover.",
                English to "What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future.",
            )
        }
    }

    // TBU1285
    data class InntektTilAvkortningSaerkullsbarn(
        val beloepNettoSaerkullsbarn: Expression<Kroner>,
        val fribeloepSaerkullsbarn: Expression<Kroner>,
        val inntektBruktIAvkortningSaerkullsbarn: Expression<Kroner>,
        val harJusteringsbeloepSaerkullsbarn: Expression<Boolean>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

            val fribeloep = fribeloepSaerkullsbarn.format()
            val inntektBruktiAvkortning = inntektBruktIAvkortningSaerkullsbarn.format()
            val hoeyereLavere = inntektBruktIAvkortningSaerkullsbarn.lessThanOrEqual(fribeloepSaerkullsbarn)
            val inntektLavereEnnFribeloep =
                inntektBruktIAvkortningSaerkullsbarn.lessThanOrEqual(fribeloepSaerkullsbarn)

            val faarUtbetaltBarnetilleggSaerkullsbarn = beloepNettoSaerkullsbarn.greaterThan(0)
            showIf(harJusteringsbeloepSaerkullsbarn) {
                paragraph {

                    textExpr(
                        Bokmal to "Inntekten din er ".expr() + inntektBruktiAvkortning + " kroner.",
                        Nynorsk to "Inntekta di er ".expr() + inntektBruktiAvkortning + " kroner.",
                        English to "Your income is NOK ".expr() + inntektBruktiAvkortning + "."
                    )
                    includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
                    text(
                        Bokmal to " Dette ble tatt hensyn til da vi endret barnetillegget.",
                        Nynorsk to " Dette har vi teke omsyn til når vi endra barnetillegget.",
                        English to " We took this in to account when we changed your child supplement.",
                    )
                    showIf(not(faarUtbetaltBarnetilleggSaerkullsbarn)) {
                        text(
                            Bokmal to " Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                            Nynorsk to " Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                            English to " You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
                        )
                    }
                }
            }.orShowIf(faarUtbetaltBarnetilleggSaerkullsbarn) {
                paragraph {
                    textExpr(
                        Bokmal to "Inntekten din på ".expr() + inntektBruktiAvkortning + " kroner er " +
                                ifElse(hoeyereLavere, "lavere", "høyere") +
                                " enn fribeløpet ditt på " + fribeloep + " kroner. Barnetillegget ditt er derfor " +
                                ifElse(inntektLavereEnnFribeloep, "ikke ", "") + "redusert ut fra inntekt.",

                        Nynorsk to "Inntekta di på ".expr() + inntektBruktiAvkortning + " kroner er " +
                                ifElse(hoeyereLavere, "lågare", "høgare") +
                                " enn fribeløpet ditt på " + fribeloep + " kroner. Barnetillegget ditt er derfor " +
                                ifElse(inntektLavereEnnFribeloep, "ikkje ", "") + "redusert ut frå inntekt.",

                        English to "Your income of NOK ".expr() + inntektBruktiAvkortning + " is " +
                                ifElse(hoeyereLavere, "lower", "higher") +
                                " than your exemption amount of NOK " +
                                fribeloep + ". Therefore your child supplement has " +
                                ifElse(inntektLavereEnnFribeloep, "not ", "") +
                                "been reduced on the basis of your income."
                    )
                }
            }
        }
    }


    // TBU1286
    data class BarnetilleggReduksjonSaerkullsbarnFellesbarn(
        val beloepNettoSaerkullsbarn: Expression<Kroner>,
        val beloepBruttoSaerkullsbarn: Expression<Kroner>,
        val harFradragSaerkullsbarn: Expression<Boolean>,
        val harFradragFellesbarn: Expression<Boolean>,
        val fribeloepSaerkullsbarn: Expression<Kroner>,
        val harJusteringsbeloepSaerkullsbarn: Expression<Boolean>,
        val harFlereSaerkullsbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val beloepBruttoFellesbarn: Expression<Kroner>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val beloepNettoFellesbarn: Expression<Kroner>,
        val fribeloepFellesbarn: Expression<Kroner>,
        val harJusteringsbeloepFellesbarn: Expression<Boolean>,
        val harFlereFellesbarn: Expression<Boolean>,
        val sivilstand: Expression<Sivilstand>,
        val inntektBruktIAvkortningSaerkullsbarn: Expression<Kroner>,
        val samletInntektBruktiAvkortningFellesbarn: Expression<Kroner>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val faarUtbetaltBarnetilleggSaerkullsbarn = beloepNettoSaerkullsbarn.greaterThan(0)
            val barnetilleggSaerkullsbarnIkkeRedusert = beloepNettoSaerkullsbarn.equalTo(beloepBruttoSaerkullsbarn)
            val inntektOverFribeloepSaerkullsbarn =
                inntektBruktIAvkortningSaerkullsbarn.greaterThan(fribeloepSaerkullsbarn)

            val faarUtbetaltBarnetilleggFellesbarn = beloepNettoFellesbarn.greaterThan(0)
            val barnetilleggFellesbarnIkkeRedusert = beloepNettoFellesbarn.equalTo(beloepBruttoFellesbarn)

            val harFradragForEnBarnetilleggYtelse = (harFradragSaerkullsbarn and not(harFradragFellesbarn)) or
                    (not(harFradragSaerkullsbarn) and harFradragFellesbarn)

            paragraph {
                showIf(faarUtbetaltBarnetilleggSaerkullsbarn and not(harJusteringsbeloepSaerkullsbarn)) {
                    textExpr(
                        Bokmal to "Inntekten din er ".expr() +
                                ifElse(inntektOverFribeloepSaerkullsbarn, "høyere", "lavere") +
                                " enn ".expr() + fribeloepSaerkullsbarn.format() + " kroner, som er fribeløpet for barnetillegget til ".expr() +
                                ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                " som ikke bor sammen med begge foreldrene. ".expr(),

                        Nynorsk to "Inntekta di er ".expr() +
                                ifElse(inntektOverFribeloepSaerkullsbarn, "høgare", "lågare") +
                                " enn ".expr() + fribeloepSaerkullsbarn.format() + " kroner, som er fribeløpet for barnetillegget til ".expr() +
                                ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                " som ikkje bur saman med begge foreldra. ".expr(),

                        English to "Your income is ".expr() +
                                ifElse(inntektOverFribeloepSaerkullsbarn, "higher", "lower") +
                                " higher than NOK ".expr() + fribeloepSaerkullsbarn.format() +
                                ", which is the exemption amount for the child supplement for the ".expr() +
                                ifElse(harFlereSaerkullsbarn, "children who do", "child who does") +
                                " not live together with both parents. ".expr()
                    )

                    showIf(harFradragForEnBarnetilleggYtelse) {
                        textExpr(
                            Bokmal to " Dette barnetillegget er derfor".expr() +
                                    ifElse(barnetilleggSaerkullsbarnIkkeRedusert, " ikke ", " ") +
                                    "redusert ut fra inntekt.",

                            Nynorsk to " Dette barnetillegget er derfor".expr() +
                                    ifElse(barnetilleggSaerkullsbarnIkkeRedusert, " ikkje ", " ") +
                                    "redusert ut frå inntekt.",

                            English to " Therefore, your child supplement has".expr() +
                                    ifElse(barnetilleggSaerkullsbarnIkkeRedusert, " not ", " ") +
                                    "been reduced on the basis of your income."
                        )
                    }
                    showIf(faarUtbetaltBarnetilleggFellesbarn and not(harJusteringsbeloepFellesbarn)) {

                        val ogsaa = not(harFradragForEnBarnetilleggYtelse)
                        val inntektErHoyereEnnFribeloepFellesBarn =
                            samletInntektBruktiAvkortningFellesbarn.greaterThan(fribeloepFellesbarn)
                        textExpr(
                            Bokmal to " Til sammen er ".expr() +
                                    ifElse(ogsaa, "også inntektene", "inntektene") +
                                    " til deg og " + sivilstand.bestemtForm() + " din " +
                                    ifElse(inntektErHoyereEnnFribeloepFellesBarn, "høyere", "lavere") +

                                    " enn ".expr() + fribeloepFellesbarn.format() + " kroner, som er fribeløpet for barnetillegget til ".expr() +
                                    ifElse(harFlereFellesbarn, "barna", "barnet") +
                                    " som bor med begge sine foreldre. ".expr(),

                            Nynorsk to " Til saman er ".expr() +
                                    ifElse(ogsaa, "også inntektene", "inntektene") +
                                    " til deg og " + sivilstand.bestemtForm() + " din " +
                                    ifElse(inntektErHoyereEnnFribeloepFellesBarn, "høgare", "lagare") +

                                    " enn ".expr() + fribeloepFellesbarn.format() + " kroner, som er fribeløpet for barnetillegget til ".expr() +
                                    ifElse(harFlereFellesbarn, "barna", "barnet") +
                                    " som bur saman med begge foreldra sine. ".expr(),

                            English to " Together, your income and your ".expr() + sivilstand.bestemtForm() + "'s income is " +
                                    ifElse(inntektErHoyereEnnFribeloepFellesBarn, "higher", "lower") +
                                    " than NOK ".expr() + fribeloepFellesbarn.format() + ", which is the exemption amount for the child supplement for the ".expr() +
                                    ifElse(harFlereFellesbarn, "children who live", "child who lives") +
                                    " together with both parents. ".expr(),
                        )

                        showIf(harFradragForEnBarnetilleggYtelse) {
                            textExpr(
                                Bokmal to
                                        " Dette barnetillegget er derfor".expr() +
                                        ifElse(barnetilleggFellesbarnIkkeRedusert, " ikke ", " ") +
                                        "redusert ut fra inntekt.",

                                Nynorsk to " Dette barnetillegget er derfor".expr() +
                                        ifElse(barnetilleggFellesbarnIkkeRedusert, " ikkje ", " ") +
                                        "redusert ut frå inntekt.",

                                English to " Therefore, your child supplement has".expr() +
                                        ifElse(barnetilleggFellesbarnIkkeRedusert, " not ", " ") +
                                        "been reduced on the basis of your income."
                            )
                        }.orShow {
                            val ikkeRedusert =
                                barnetilleggFellesbarnIkkeRedusert and barnetilleggSaerkullsbarnIkkeRedusert
                            textExpr(
                                Bokmal to " Barnetilleggene er derfor".expr() +
                                        ifElse(ikkeRedusert, " ikke ", " ") +
                                        "redusert ut fra inntekt.",

                                Nynorsk to " Desse barnetillegga er derfor".expr() +
                                        ifElse(ikkeRedusert, " ikkje ", " ") +
                                        "redusert ut frå inntekt.",

                                English to " Therefore your child supplements have".expr() +
                                        ifElse(ikkeRedusert, " not ", " ") +
                                        "been reduced on the basis of your income."
                            )
                        }
                    }
                }
            }

            showIf(harJusteringsbeloepFellesbarn) {
                paragraph {
                    includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
                    textExpr(
                        Bokmal to " Dette ble tatt hensyn til da vi endret barnetillegget for ".expr() +
                                ifElse(harFlereFellesbarn, "barna", "barnet") +
                                " som bor med begge sine foreldre.".expr(),

                        Nynorsk to " Dette har vi teke omsyn til når vi endra barnetillegget for ".expr() +
                                ifElse(harFlereFellesbarn, "barna", "barnet") +
                                " som bur saman med begge foreldra sine.".expr(),

                        English to " We took this in to account when we changed your child supplement for the ".expr() +
                                ifElse(harFlereFellesbarn, "children who live", "child who lives") +
                                " together with both parents.".expr()
                    )
                    showIf(not(faarUtbetaltBarnetilleggFellesbarn)) {
                        text(
                            Bokmal to " Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året",
                            Nynorsk to " Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                            English to " You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
                        )
                    }
                }
            }

            showIf(harJusteringsbeloepSaerkullsbarn) {
                paragraph {
                    includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
                    textExpr(
                        Bokmal to " Dette ble tatt hensyn til da vi endret barnetillegget for ".expr() +
                                ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                " som ikke bor sammen med begge foreldrene.".expr(),
                        Nynorsk to " Dette har vi teke omsyn til når vi endra barnetillegget for ".expr() +
                                ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                " som ikkje bur saman med begge foreldra. ".expr(),
                        English to " We took this in to account when we changed your child supplement for the ".expr() +
                                ifElse(harFlereSaerkullsbarn, "children who live", "child who lives") +
                                " together with both parents.".expr()
                    )
                    showIf(not(faarUtbetaltBarnetilleggSaerkullsbarn)) {
                        text(
                            Bokmal to " Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                            Nynorsk to " Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                            English to " You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
                        )
                    }
                }
            }
        }
    }

    // TBU1286.1, TBU1286.2
    data class BarnetilleggIkkeUtbetalt(
        val fellesInnvilget: Expression<Boolean>,
        val fellesUtbetalt: Expression<Boolean>,
        val harFlereFellesBarn: Expression<Boolean>,
        val harFlereSaerkullsbarn: Expression<Boolean>,
        val inntektstakFellesbarn: Expression<Kroner>,
        val inntektstakSaerkullsbarn: Expression<Kroner>,
        val saerkullInnvilget: Expression<Boolean>,
        val saerkullUtbetalt: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {

        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf((saerkullInnvilget or fellesInnvilget) and (not(saerkullUtbetalt) or not(fellesUtbetalt))) {
                paragraph {
                    showIf(not(saerkullUtbetalt) and not(fellesUtbetalt) and fellesInnvilget and saerkullInnvilget) {
                        textExpr( // TBU2490
                            Bokmal to "Barnetillegget for ".expr() +
                                    ifElse(harFlereFellesBarn, "barna", "barnet") +
                                    " som bor med begge sine foreldre, blir ikke utbetalt fordi de samlede inntektene er høyere enn " +
                                    inntektstakFellesbarn.format() + " kroner. Barnetillegget for " +
                                    ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                    " som ikke bor sammen med begge foreldrene, blir heller ikke utbetalt fordi inntekten din alene er høyere enn " +
                                    inntektstakSaerkullsbarn.format() + " kroner. Inntektene er over grensen for å få utbetalt barnetillegg.",

                            Nynorsk to "Barnetillegget for ".expr() +
                                    ifElse(harFlereFellesBarn, "barna", "barnet") +
                                    " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei samla inntektene er høgare enn " +
                                    inntektstakFellesbarn.format() + " kroner. Barnetillegget for " +
                                    ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                    " som ikkje bur saman med begge foreldra, blir heller ikkje utbetalt fordi inntekta di åleine er høgare enn " +
                                    inntektstakSaerkullsbarn.format() + " kroner. Inntektene er over grensa for å få utbetalt barnetillegg.",

                            English to "You will not receive child supplement for the ".expr() +
                                    ifElse(harFlereFellesBarn, "children", "child") +
                                    " who lives together with both parents because your total income is higher than NOK " +
                                    inntektstakFellesbarn.format() + ". You will not receive child supplement for the " +
                                    ifElse(harFlereSaerkullsbarn, "children", "child") +
                                    " who do not live together with both parents because your income alone is higher than NOK " +
                                    inntektstakSaerkullsbarn.format() + ". You will not receive child supplement because your income exceeds the income limit."
                        )
                    }.orShowIf(not(saerkullUtbetalt) and saerkullInnvilget and not(fellesInnvilget)) {
                        textExpr( // TBU1286.1
                            Bokmal to "Barnetillegget for ".expr() +
                                    ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                    " blir ikke utbetalt fordi du har en samlet inntekt som er høyere enn " +
                                    inntektstakFellesbarn.format() + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg.",

                            Nynorsk to "Barnetillegget for ".expr() +
                                    ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                    " blir ikkje utbetalt fordi du har ei samla inntekt som er høgare enn " +
                                    inntektstakFellesbarn.format() + " kroner. Inntekta di er over grensa for å få utbetalt barnetillegg.",

                            English to "You will not receive child supplement for the ".expr() +
                                    ifElse(harFlereSaerkullsbarn, "children", "child") +
                                    " because your total income on its own is higher than NOK " +
                                    inntektstakFellesbarn.format() + ". You will not receive child supplement because your income exceeds the income limit."
                        )
                    }.orShowIf(not(saerkullUtbetalt) and saerkullInnvilget and fellesInnvilget) {
                        textExpr( // TBU1286.1
                            Bokmal to "Barnetillegget for ".expr() +
                                    ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                    " som ikke bor sammen med begge foreldrene, blir ikke utbetalt fordi du alene har en samlet inntekt som er høyere enn " +
                                    inntektstakFellesbarn.format() + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg.",

                            Nynorsk to "Barnetillegget for ".expr() +
                                    ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                    " som ikkje bur saman med begge foreldra sine, blir ikkje utbetalt fordi du åleine har ei samla inntekt som er høgare enn " +
                                    inntektstakFellesbarn.format() + " kroner. Inntekta di er over grensa for å få utbetalt barnetillegg.",

                            English to "You will not receive child supplement for the ".expr() +
                                    ifElse(harFlereSaerkullsbarn, "children who do", "child who does") +
                                    " not live together with both parents because your total income on its own is higher than NOK " +
                                    inntektstakFellesbarn.format() + ". You will not receive child supplement because your income exceeds the income limit."
                        )
                    }.orShowIf(not(fellesUtbetalt) and not(saerkullInnvilget) and fellesInnvilget) {
                        textExpr(// TBU1286.2
                            Bokmal to "Barnetillegget for ".expr() +
                                    ifElse(harFlereFellesBarn, "barna", "barnet") +
                                    " blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " +
                                    inntektstakFellesbarn.format() + " kroner. De samlede inntektene er over grensen for å få utbetalt barnetillegg.",

                            Nynorsk to "Barnetillegget for ".expr() +
                                    ifElse(harFlereFellesBarn, "barna", "barnet") +
                                    " blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " +
                                    inntektstakFellesbarn.format() + " kroner. Dei samla inntektene er over grensa for å få utbetalt barnetillegg.",

                            English to "You will not receive child supplement for the ".expr() +
                                    ifElse(harFlereFellesBarn, "children", "child") +
                                    " because your total income on its own is higher than NOK " +
                                    inntektstakFellesbarn.format() + ". You will not receive child supplement because your combined incomes exceed the income limit."
                        )
                    }.orShowIf(not(fellesUtbetalt) and saerkullInnvilget and fellesInnvilget) {
                        textExpr(// TBU1286.2
                            Bokmal to "Barnetillegget for ".expr() +
                                    ifElse(harFlereFellesBarn, "barna", "barnet") +
                                    " som bor med begge sine foreldre, blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " +
                                    inntektstakFellesbarn.format() + " kroner. De samlede inntektene er over grensen for å få utbetalt barnetillegg.",

                            Nynorsk to "Barnetillegget for ".expr() +
                                    ifElse(harFlereFellesBarn, "barna", "barnet") +
                                    " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " +
                                    inntektstakFellesbarn.format() + " kroner. Dei samla inntektene er over grensa for å få utbetalt barnetillegg.",

                            English to "You will not receive child supplement for the ".expr() +
                                    ifElse(harFlereFellesBarn, "children who live", "child who lives") +
                                    " together with both parents because your total income on its own is higher than NOK " +
                                    inntektstakFellesbarn.format() + ". You will not receive child supplement because your combined incomes exceed the income limit."
                        )
                    }
                }
            }
        }
    }

    // TBU2490
    data class InnvilgetOgIkkeUtbetalt(
        val fellesInnvilget: Expression<Boolean>,
        val fellesUtbetalt: Expression<Boolean>,
        val harFlereFellesbarn: Expression<Boolean>,
        val harFlereSaerkullsbarn: Expression<Boolean>,
        val inntektstakFellesbarn: Expression<Kroner>,
        val inntektstakSaerkullsbarn: Expression<Kroner>,
        val saerkullInnvilget: Expression<Boolean>,
        val saerkullUtbetalt: Expression<Boolean>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                showIf(
                    not(saerkullUtbetalt) and not(fellesUtbetalt) and fellesInnvilget and saerkullInnvilget
                ) {
                    textExpr(
                        Bokmal to "Barnetillegget for ".expr() +
                                ifElse(harFlereFellesbarn, "barna", "barnet") +
                                " som bor med begge sine foreldre, blir ikke utbetalt fordi de samlede inntektene er høyere enn " +
                                inntektstakFellesbarn.format() + " kroner. Barnetillegget for " +
                                ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                " som ikke bor sammen med begge foreldrene, blir heller ikke utbetalt fordi inntekten din alene er høyere enn " +
                                inntektstakSaerkullsbarn.format() + " kroner. Inntektene er over grensen for å få utbetalt barnetillegg.",

                        Nynorsk to "Barnetillegget for ".expr() +
                                ifElse(harFlereFellesbarn, "barna", "barnet") +
                                " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei samla inntektene er høgare enn " +
                                inntektstakFellesbarn.format() + " kroner. Barnetillegget for " +
                                ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                " som ikkje bur saman med begge foreldra, blir heller ikkje utbetalt fordi inntekta di åleine er høgare enn " +
                                inntektstakSaerkullsbarn.format() + " kroner. Inntektene er over grensa for å få utbetalt barnetillegg.",

                        English to "You will not receive child supplement for the ".expr() +
                                ifElse(harFlereFellesbarn, "children", "child") +
                                " who lives together with both parents because your total income is higher than NOK " +
                                inntektstakFellesbarn.format() + ". You will not receive child supplement for the " +
                                ifElse(harFlereSaerkullsbarn, "children", "child") +
                                " who do not live together with both parents because your income alone is higher than NOK " +
                                inntektstakSaerkullsbarn.format() + ". You will not receive child supplement because your income exceeds the income limit."
                    )
                }.orShowIf(not(saerkullUtbetalt) and saerkullInnvilget and not(fellesInnvilget)) {
                    textExpr( // TBU1286.1
                        Bokmal to "Barnetillegget for ".expr() +
                                ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                " blir ikke utbetalt fordi du har en samlet inntekt som er høyere enn " +
                                inntektstakFellesbarn.format() + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg.",

                        Nynorsk to "Barnetillegget for ".expr() +
                                ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                " blir ikkje utbetalt fordi du har ei samla inntekt som er høgare enn " +
                                inntektstakFellesbarn.format() + " kroner. Inntekta di er over grensa for å få utbetalt barnetillegg.",

                        English to "You will not receive child supplement for the ".expr() +
                                ifElse(harFlereSaerkullsbarn, "children", "child") +
                                " because your total income on its own is higher than NOK " +
                                inntektstakFellesbarn.format() + ". You will not receive child supplement because your income exceeds the income limit."
                    )
                }.orShowIf(not(saerkullUtbetalt) and saerkullInnvilget and fellesInnvilget) {
                    textExpr( // TBU1286.1
                        Bokmal to "Barnetillegget for ".expr() +
                                ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                " som ikke bor sammen med begge foreldrene, blir ikke utbetalt fordi du alene har en samlet inntekt som er høyere enn " +
                                inntektstakFellesbarn.format() + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg.",

                        Nynorsk to "Barnetillegget for ".expr() +
                                ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                " som ikkje bur saman med begge foreldra sine, blir ikkje utbetalt fordi du åleine har ei samla inntekt som er høgare enn " +
                                inntektstakFellesbarn.format() + " kroner. Inntekta di er over grensa for å få utbetalt barnetillegg.",

                        English to "You will not receive child supplement for the ".expr() +
                                ifElse(harFlereSaerkullsbarn, "children who do", "child who does") +
                                " not live together with both parents because your total income on its own is higher than NOK " +
                                inntektstakFellesbarn.format() + ". You will not receive child supplement because your income exceeds the income limit."
                    )
                }.orShowIf(not(fellesUtbetalt) and not(saerkullInnvilget) and fellesInnvilget) {
                    textExpr(// TBU1286.2
                        Bokmal to "Barnetillegget for ".expr() +
                                ifElse(harFlereFellesbarn, "barna", "barnet") +
                                " blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " +
                                inntektstakFellesbarn.format() + " kroner. De samlede inntektene er over grensen for å få utbetalt barnetillegg.",

                        Nynorsk to "Barnetillegget for ".expr() +
                                ifElse(harFlereFellesbarn, "barna", "barnet") +
                                " blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " +
                                inntektstakFellesbarn.format() + " kroner. Dei samla inntektene er over grensa for å få utbetalt barnetillegg.",

                        English to "You will not receive child supplement for the ".expr() +
                                ifElse(harFlereFellesbarn, "children", "child") +
                                " because your total income on its own is higher than NOK " +
                                inntektstakFellesbarn.format() + ". You will not receive child supplement because your combined incomes exceed the income limit."
                    )
                }.orShowIf(not(fellesUtbetalt) and saerkullInnvilget and fellesInnvilget) {
                    textExpr(// TBU1286.2
                        Bokmal to "Barnetillegget for ".expr() +
                                ifElse(harFlereFellesbarn, "barna", "barnet") +
                                " som bor med begge sine foreldre, blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " +
                                inntektstakFellesbarn.format() + " kroner. De samlede inntektene er over grensen for å få utbetalt barnetillegg.",

                        Nynorsk to "Barnetillegget for ".expr() +
                                ifElse(harFlereFellesbarn, "barna", "barnet") +
                                " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " +
                                inntektstakFellesbarn.format() + " kroner. Dei samla inntektene er over grensa for å få utbetalt barnetillegg.",

                        English to "You will not receive child supplement for the ".expr() +
                                ifElse(harFlereFellesbarn, "children who live", "child who lives") +
                                " together with both parents because your total income on its own is higher than NOK " +
                                inntektstakFellesbarn.format() + ". You will not receive child supplement because your combined incomes exceed the income limit."
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
            showIf(harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn) {
                paragraph {
                    text(
                        Bokmal to "Du kan lese mer om beregningen av barnetillegg i vedlegget «Opplysninger om beregningen».",
                        Nynorsk to "Du kan lese meir om berekninga av barnetillegg i vedlegget «Opplysningar om berekninga».",
                        English to "Read more about how child supplements are calculated in the attachment called “Information about calculations”."
                    )
                }
            }
        }
    }

    // TBU601V
    data class BarnetilleggInntektsavkortning(
        val kravAarsakType: Expression<KravAarsakType>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Slik reduserer vi barnetillegget ut fra inntekt",
                    Nynorsk to "Slik reduserer vi barnetillegget ut frå inntekt",
                    English to "This is how the reduction of your child supplement is calculated"
                )
            }
            paragraph {
                text(
                    Bokmal to "Størrelsen på barnetillegget er avhengig av inntekt.",
                    Nynorsk to "Storleiken på barnetillegget er avhengig av inntekt.",
                    English to "The amount of child supplement depends on your income."
                )
            }
            paragraph {
                text(
                    Bokmal to "Barnetillegget blir redusert ut fra personinntekt. Dette kan for eksempel være:",
                    Nynorsk to "Barnetillegget kan bli redusert ut frå personinntekt. Dette kan til dømes være:",
                    English to "The child supplement is reduced on the basis of personal income. This can be for example:"
                )
                list {
                    item {
                        text(
                            Bokmal to "uføretrygd",
                            Nynorsk to "uføretrygd",
                            English to "disability benefits",
                        )
                    }
                    item {
                        text(
                            Bokmal to "arbeidsinntekt",
                            Nynorsk to "arbeidsinntekt",
                            English to "income from employment",
                        )
                    }
                    item {
                        text(
                            Bokmal to "næringsinntekt",
                            Nynorsk to "næringsinntekt",
                            English to "income from self-employment",
                        )
                    }
                    item {
                        text(
                            Bokmal to "inntekt fra utlandet",
                            Nynorsk to "inntekt frå utlandet",
                            English to "income from abroad"
                        )
                    }
                    item {
                        text(
                            Bokmal to "ytelser/pensjon fra Norge",
                            Nynorsk to "ytingar/pensjon frå Noreg",
                            English to "payments/pensions from Norway",
                        )
                    }
                    item {
                        text(
                            Bokmal to "pensjon fra utlandet",
                            Nynorsk to "pensjon frå utlandet",
                            English to "pensions from abroad",
                        )
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "Du kan lese mer om personinntekt på skatteetaten.no.",
                    Nynorsk to "Du kan lese meir om personinntekt på skatteetaten.no.",
                    English to "You can read more about personal income at skatteetaten.no."
                )
            }

            // TBU602V
            paragraph {
                textExpr(
                    Bokmal to "Det er inntekten ".expr(),
                    Nynorsk to "".expr(),
                    English to "".expr()
                )
            }
        }
    }
}






