package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
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

    data class TBU2290(
        val oensketVirkningsDato: Expression<LocalDate>,
        val foedselsdatoPaaBarnetilleggOpphoert: Expression<LocalDate>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val virkningsDato = oensketVirkningsDato.format()
                val foedselsDato = foedselsdatoPaaBarnetilleggOpphoert.format()  // or childs name?
                // showIf antall foedselsDato = 1
                textExpr(
                    Bokmal to "Vi har vedtatt at barnetillegget i uføretrygden din opphører fra ".expr() + virkningsDato + " for barn født ".expr() + foedselsDato + ".".expr(),
                    Nynorsk to "Vi har stansa barnetillegget i uføretrygda di frå ".expr() + virkningsDato + " for barn fødd ".expr() + foedselsDato + ".".expr(),
                    English to "The child supplement in your disability benefit has been discontinued, effective as of ".expr() + virkningsDato + ", for child/children born ".expr() + foedselsDato + ".".expr()
                )
            }
        }
    }

    object TBU3920 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                textExpr(
                    Bokmal to "For å ha rett til barnetillegg må du forsørge barn under 18 år. Vi har vedtatt at barnetillegget i uføretrygden opphører fordi barnetbarna har fylt 18 år.".expr(),
                    Nynorsk to "For å ha rett til barnetillegg må du forsørgje barn under 18 år. Vi har stansa barnetillegget i uføretrygda fordi barnetbarna har fylt 18 år.".expr(),
                    English to "To be eligible for child supplement, you must support children under 18 years of age. The child supplement in your disability benefit has been discontinued because your  childchildren has(have) turned 18 years of age.".expr()
                )
            }
        }
    }

    object TBU3800 : OutlinePhrase<LangBokmalNynorskEnglish>() {
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

    data class TBU2338(
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
                        showIf(sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.GIFT_LEVER_ADSKILT)) {
                            text(
                                Bokmal to "ektefellen",
                                Nynorsk to "ektefellen",
                                English to "spouse"
                            )
                        }.orShowIf(sivilstand.isOneOf(Sivilstand.PARTNER, Sivilstand.PARTNER_LEVER_ADSKILT)) {
                            text(
                                Bokmal to "partneren",
                                Nynorsk to "partnaren",
                                English to "partner"
                            )
                        }.orShowIf(sivilstand.isOneOf(Sivilstand.SAMBOER1_5, Sivilstand.SAMBOER3_2)) {
                            text(
                                Bokmal to "samboeren",
                                Nynorsk to "sambuaren",
                                English to "cohabitant"
                            )
                        }
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


    data class TBU2339(
        val antallFellesbarnInnvilget: Expression<Int>,
        val beloepNettoFellesbarn: Expression<Kroner>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val sivilstand: Expression<Sivilstand>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val barnFlertall = antallFellesbarnInnvilget.greaterThan(1)

                text(
                    Bokmal to "Inntekten til deg og ",
                    Nynorsk to "Inntekta til deg og ",
                    English to "The incomes of you and your "
                )
                showIf(sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.GIFT_LEVER_ADSKILT)) {
                    text(
                        Bokmal to "ektefellen",
                        Nynorsk to "ektefellen",
                        English to "spouse"
                    )
                }.orShowIf(sivilstand.isOneOf(Sivilstand.PARTNER, Sivilstand.PARTNER_LEVER_ADSKILT)) {
                    text(
                        Bokmal to "partneren",
                        Nynorsk to "partnaren",
                        English to "partner"
                    )
                }.orShowIf(sivilstand.isOneOf(Sivilstand.SAMBOER1_5, Sivilstand.SAMBOER3_2)) {
                    text(
                        Bokmal to "samboeren",
                        Nynorsk to "sambuaren",
                        English to "cohabitant"
                    )
                }
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
                }
                showIf(harBarnetilleggFellesbarn and harBarnetilleggSaerkullsbarn) {
                    text(
                        Bokmal to " Inntekten til ",
                        Nynorsk to " Inntekta til ",
                        English to "The income of your "
                    )
                    showIf(sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.GIFT_LEVER_ADSKILT)) {
                        text(
                            Bokmal to "ektefellen",
                            Nynorsk to "ektefellen",
                            English to "spouse"
                        )
                    }.orShowIf(sivilstand.isOneOf(Sivilstand.PARTNER, Sivilstand.PARTNER_LEVER_ADSKILT)) {
                        text(
                            Bokmal to "partneren",
                            Nynorsk to "partnaren",
                            English to "partner"
                        )
                    }.orShowIf(sivilstand.isOneOf(Sivilstand.SAMBOER1_5, Sivilstand.SAMBOER3_2)) {
                        text(
                            Bokmal to "samboeren",
                            Nynorsk to "sambuaren",
                            English to "cohabitant"
                        )
                        textExpr(
                            Bokmal to " din har kun betydning for størrelsen på barnetillegget til ".expr() + ifElse(
                                barnFlertall,
                                ifTrue = "barna",
                                ifFalse = "barnet"
                            ) + " som bor sammen med begge sine foreldre.".expr(),
                            Nynorsk to " din har berre betydning for storleiken på barnetillegget til ".expr() + ifElse(
                                barnFlertall,
                                ifTrue = "barna",
                                ifFalse = "barnet"
                            ) + " som bur saman med begge foreldra sine.".expr(),
                            English to " only affects the size of the child supplement for the ".expr() + ifElse(
                                barnFlertall,
                                ifTrue = "children who live",
                                ifFalse = "child who lives"
                            ) + " together with both parents.".expr()
                        )
                    }
                }
            }
        }
    }


    data class TBU3801(
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val sivilstand: Expression<Sivilstand>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {

                showIf(harBarnetilleggFellesbarn and not(harBarnetilleggSaerkullsbarn) or (harBarnetilleggFellesbarn and harBarnetilleggSaerkullsbarn)) {
                    text(
                        Bokmal to "Endringer i inntektene til deg og ",
                        Nynorsk to "Endringar i inntektene til deg og ",
                        English to "Changes in your and your "
                    )
                    showIf(sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.GIFT_LEVER_ADSKILT)) {
                        text(
                            Bokmal to "ektefellen",
                            Nynorsk to "ektefellen",
                            English to "spouse"
                        )
                    }.orShowIf(sivilstand.isOneOf(Sivilstand.PARTNER, Sivilstand.PARTNER_LEVER_ADSKILT)) {
                        text(
                            Bokmal to "partneren",
                            Nynorsk to "partnaren",
                            English to "partner"
                        )
                    }.orShowIf(sivilstand.isOneOf(Sivilstand.SAMBOER1_5, Sivilstand.SAMBOER3_2)) {
                        text(
                            Bokmal to "samboeren",
                            Nynorsk to "sambuaren",
                            English to "cohabitant"
                        )
                    }
                    text(
                        Bokmal to " din kan ha betydning for barnetillegget ditt. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» på nav.no.",
                        Nynorsk to " di kan ha betydning for barnetillegget ditt. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» på nav.no.",
                        English to " income may affect your child supplement. You can easily report income changes under the menu option «disability benefit» at nav.no."
                    )
                }.orShowIf(not(harBarnetilleggFellesbarn) and harBarnetilleggSaerkullsbarn) {
                    text(
                        Bokmal to "Endringer i inntekten din kan ha betydning for barnetillegget ditt. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» på nav.no.",
                        Nynorsk to "Endringar i inntekta di kan ha betydning for barnetillegget ditt. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» på nav.no.",
                        English to ""
                    )
                }
            }
        }
    }


    data class TBU1284(
        val beloepFratrukketAnnenForeldersInntekt: Expression<Int>,
        val beloepNettoFellesbarn: Expression<Kroner>,
        val fradragFellesbarn: Expression<Kroner>,
        val fribeloepFellesbarn: Expression<Kroner>,
        val inntektAnnenForelderFellesbarn: Expression<Kroner>,
        val inntektBruktiAvkortningFellesbarn: Expression<Kroner>,
        val grunnbeloepFellesbarn: Expression<Kroner>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val justeringsbeloepFellesbarn: Expression<Kroner>,
        val sivilstand: Expression<Sivilstand>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val fribeloep = fribeloepFellesbarn.format()
                val inntektAnnenForelder = inntektAnnenForelderFellesbarn.format()
                val inntektBruktiAvkortning = inntektBruktiAvkortningFellesbarn.format()
                val grunnbeloep = grunnbeloepFellesbarn.format()
                val hoeyereLavere = inntektBruktiAvkortningFellesbarn.lessThanOrEqual(fribeloepFellesbarn)
                val ikke = fradragFellesbarn.equalTo(0)

                showIf(not(harBarnetilleggSaerkullsbarn)) {
                    textExpr(
                        Bokmal to "Inntekten din er ".expr() + inntektBruktiAvkortning + " kroner og inntekten til ".expr(),
                        Nynorsk to "Inntekta di er ".expr() + inntektBruktiAvkortning + " kroner, og inntekta til ".expr(),
                        English to "Your income is NOK ".expr() + inntektBruktiAvkortning + " and your".expr()
                    )
                    showIf(sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.GIFT_LEVER_ADSKILT)) {
                        text(
                            Bokmal to "ektefellen",
                            Nynorsk to "ektefellen",
                            English to "spouse"
                        )
                    }.orShowIf(sivilstand.isOneOf(Sivilstand.PARTNER, Sivilstand.PARTNER_LEVER_ADSKILT)) {
                        text(
                            Bokmal to "partneren",
                            Nynorsk to "partnaren",
                            English to "partner"
                        )
                    }.orShowIf(sivilstand.isOneOf(Sivilstand.SAMBOER1_5, Sivilstand.SAMBOER3_2)) {
                        text(
                            Bokmal to "samboeren",
                            Nynorsk to "sambuaren",
                            English to "cohabitant"
                        )
                        textExpr(
                            Bokmal to " din er ".expr() + inntektAnnenForelder + " kroner.".expr(),
                            Nynorsk to " din er ".expr() + inntektAnnenForelder + " kroner.".expr(),
                            English to " partner's income is NOK ".expr() + inntektAnnenForelder + "."
                        )
                    }
                    showIf(beloepFratrukketAnnenForeldersInntekt.greaterThan(0)) {
                        textExpr(
                            Bokmal to " Folketrygdens grunnbeløp på inntil ".expr() + grunnbeloep + " kroner er holdt utenfor inntekten til ".expr(),
                            Nynorsk to " Grunnbeløpet i folketrygda på inntil ".expr() + grunnbeloep + " kroner er halde utanfor inntekta til ".expr(),
                            English to " The national insurance basic amount of up to NOK ".expr() + grunnbeloep + " has not been included in your ".expr()
                        )
                    }
                    showIf(sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.GIFT_LEVER_ADSKILT)) {
                        text(
                            Bokmal to "ektefellen din.",
                            Nynorsk to "ektefellen di.",
                            English to "spouse."
                        )
                    }.orShowIf(sivilstand.isOneOf(Sivilstand.PARTNER, Sivilstand.PARTNER_LEVER_ADSKILT)) {
                        text(
                            Bokmal to "partneren din.",
                            Nynorsk to "partnaren di.",
                            English to "partner."
                        )
                    }.orShowIf(sivilstand.isOneOf(Sivilstand.SAMBOER1_5, Sivilstand.SAMBOER3_2)) {
                        text(
                            Bokmal to "samboeren din.",
                            Nynorsk to "sambuaren di",
                            English to "cohabitant."
                        )
                    }
                    showIf(
                        not(harBarnetilleggSaerkullsbarn) and justeringsbeloepFellesbarn.greaterThan(
                            0
                        ) and beloepNettoFellesbarn.greaterThan(0)
                    ) {
                        textExpr(
                            Bokmal to " Til sammen er inntektene ".expr() + ifElse(
                                hoeyereLavere,
                                ifTrue = "lavere",
                                ifFalse = "høyere"
                            ) + " enn fribeløpet ditt på ".expr() + fribeloep + " kroner. Barnetillegget ditt er derfor".expr() + ifElse(
                                ikke,
                                ifTrue = " ikke",
                                ifFalse = ""
                            ) + " redusert ut fra inntekt.".expr(),
                            Nynorsk to " Til saman er inntektene ".expr() + ifElse(
                                hoeyereLavere,
                                ifTrue = "lågare",
                                ifFalse = "høgare"
                            ) + " enn fribeløpet ditt på ".expr() + fribeloep + " kroner. Barnetillegget ditt er derfor".expr() + ifElse(
                                ikke,
                                ifTrue = " ikkje",
                                ifFalse = ""
                            ) + " redusert ut frå inntekt.".expr(),
                            English to " Together, the incomes are ".expr() + ifElse(
                                hoeyereLavere,
                                ifTrue = "lower",
                                ifFalse = "higher"
                            ) + " than your exemption amount of NOK ".expr() + fribeloep + ". Therefore, your child supplement has".expr() + ifElse(
                                ikke,
                                ifTrue = " not",
                                ifFalse = ""
                            ) + " been reduced on the basis of your income.".expr()
                        )
                    }
                    showIf(
                        not(harBarnetilleggSaerkullsbarn) and justeringsbeloepFellesbarn.greaterThan(0) and beloepNettoFellesbarn.greaterThan(
                            0
                        )
                    ) {
                        text(
                            Bokmal to " Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget.",
                            Nynorsk to " Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget.",
                            English to " What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took into account when we changed your child supplement."
                        )
                    }
                    showIf(
                        not(harBarnetilleggSaerkullsbarn) and justeringsbeloepFellesbarn.greaterThan(0) and beloepNettoFellesbarn.equalTo(
                            0
                        )
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
    }


    data class TBU1285(
        val beloepNettoSaerkullsbarn: Expression<Kroner>,
        val fribeloepSaerkullsbarn: Expression<Kroner>,
        val inntektBruktiAvkortningSaerkullsbarn: Expression<Kroner>,
        val justeringsbeloepSaerkullsbarn: Expression<Kroner>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val fribeloep = fribeloepSaerkullsbarn.format()
                val inntektBruktiAvkortning = inntektBruktiAvkortningSaerkullsbarn.format()
                val hoeyereLavere = inntektBruktiAvkortningSaerkullsbarn.lessThanOrEqual(fribeloepSaerkullsbarn)
                val ikke = inntektBruktiAvkortningSaerkullsbarn.lessThanOrEqual(fribeloepSaerkullsbarn)

                showIf(
                    beloepNettoSaerkullsbarn.greaterThan(0) and justeringsbeloepSaerkullsbarn.equalTo(0)
                ) {
                    textExpr(
                        Bokmal to "Inntekten din på ".expr() + inntektBruktiAvkortning + " kroner er ".expr() + ifElse(
                            hoeyereLavere,
                            ifTrue = "lavere",
                            ifFalse = "høyere"
                        ) + " enn fribeløpet ditt på ".expr()
                            + fribeloep + " kroner. Barnetillegget ditt er derfor ".expr() + ifElse(
                            ikke,
                            ifTrue = "ikke ",
                            ifFalse = ""
                        ) + "redusert ut fra inntekt.".expr(),
                        Nynorsk to "Inntekta di på ".expr() + inntektBruktiAvkortning + " kroner er ".expr() + ifElse(
                            hoeyereLavere,
                            ifTrue = "lågare",
                            ifFalse = "høgare"
                        ) + " enn fribeløpet ditt på ".expr()
                            + fribeloep + " kroner. Barnetillegget ditt er derfor ".expr() + ifElse(
                            ikke,
                            ifTrue = "ikkje ",
                            ifFalse = ""
                        ) + "redusert ut frå inntekt.".expr(),
                        English to "Your income of NOK ".expr() + inntektBruktiAvkortning + " is ".expr() + ifElse(
                            hoeyereLavere,
                            ifTrue = "lower",
                            ifFalse = "higher"
                        ) + " than your exemption amount of NOK ".expr()
                            + fribeloep + ". Therefore your child supplement has ".expr() + ifElse(
                            ikke,
                            ifTrue = "not ",
                            ifFalse = ""
                        ) + "not been reduced on the basis of your income.".expr()
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


    data class TBU1286Saerkullsbarn(
        val beloepNettoSaerkullsbarn: Expression<Kroner>,
        val fradragSaerkullsbarn: Expression<Kroner>,
        val fradragFellesbarn: Expression<Kroner>,
        val fribeloepSaerkullsbarn: Expression<Kroner>,
        val justeringsbeloepSaerkullsbarn: Expression<Kroner>,
        val antallSaerkullsbarnInnvilget: Expression<Int>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val fribeloep = fribeloepSaerkullsbarn.format()
                val barnFlertall = antallSaerkullsbarnInnvilget.greaterThan(1)
                // Bruker mottar barnetillegg for både saerkullsbarn og fellesbarn, men kun barnetillegget for saerkullsbarn er blitt redusert
                showIf(
                    beloepNettoSaerkullsbarn.notEqualTo(0) and justeringsbeloepSaerkullsbarn.equalTo(0)
                )
                {
                    textExpr(
                        Bokmal to "Inntekten din er høyere enn ".expr() + fribeloep + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertall,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som ikke bor sammen med begge foreldrene. Dette barnetillegget er derfor redusert ut fra inntekt.".expr(),
                        Nynorsk to "Inntekta di er høgare enn ".expr() + fribeloep + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertall,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som ikkje bur saman med begge foreldra. Dette barnetillegget er derfor redusert ut frå inntekt.".expr(),
                        English to "Your income is higher than NOK ".expr() + fribeloep + ", which is the exemption amount for the child supplement for the ".expr() + ifElse(
                            barnFlertall,
                            ifTrue = "children who do",
                            ifFalse = "child who does"
                        ) + " not live together with both parents. Therefore, your child supplement has been reduced on the basis of your income.".expr()
                    )
                    // Bruker mottar barnetillegg for både saerkullsbarn og fellesbarn, men kun barnetillegget for saerkullsbarn er ikke blitt redusert
                }.orShowIf(
                    beloepNettoSaerkullsbarn.notEqualTo(0) and justeringsbeloepSaerkullsbarn.equalTo(0) and fradragSaerkullsbarn.equalTo(
                        0
                    )
                        and fradragFellesbarn.greaterThan(0)
                ) {
                    textExpr(
                        Bokmal to "Inntekten din er lavere enn ".expr() + fribeloep + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertall,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som ikke bor sammen med begge foreldrene. Dette barnetillegget er derfor ikke redusert ut fra inntekt.".expr(),
                        Nynorsk to "Inntekta di er lågare enn ".expr() + fribeloep + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertall,
                            ifTrue = "barna",
                            ifFalse = "barnet "
                        ) + " som ikkje bur saman med begge foreldra. Dette barnetillegget er derfor ikkje redusert ut frå inntekt.".expr(),
                        English to "Your income is lower than NOK ".expr() + fribeloep + ", which is the exemption amount for the child supplement for the ".expr() + ifElse(
                            barnFlertall,
                            ifTrue = "children who do",
                            ifFalse = "child who does"
                        ) + " not live together with both parents. Therefore, your child supplement has not been reduced on the basis of your income.".expr()
                    )
                }
            }
        }
    }


    data class TBU1286Fellesbarn(
        val beloepNettoFellesbarn: Expression<Kroner>,
        val fradragSaerkullsbarn: Expression<Kroner>,
        val fradragFellesbarn: Expression<Kroner>,
        val fribeloepFellesbarn: Expression<Kroner>,
        val justeringsbeloepFellesbarn: Expression<Kroner>,
        val antallFellesbarnInnvilget: Expression<Int>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val fribeloep = fribeloepFellesbarn.format()
                val barnFlertall = antallFellesbarnInnvilget.greaterThan(1)

                // Bruker mottar barnetillegg for både saerkullsbarn og fellesbarn, men kun barnetillegget for fellesbarn er blitt redusert
                showIf(
                    beloepNettoFellesbarn.greaterThan(0) and justeringsbeloepFellesbarn.equalTo(0) and fradragFellesbarn.greaterThan(
                        0
                    )
                        and fradragSaerkullsbarn.equalTo(0)
                ) {
                    textExpr(
                        Bokmal to "Til sammen er inntektene til deg og sivilstandEPSBestemtForm  din høyere enn ".expr() + fribeloep + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertall,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som bor med begge sine foreldre. Dette barnetillegget er derfor redusert ut fra inntekt.".expr(),
                        Nynorsk to "Til saman er inntektene til deg og ektefellen/partnaren/sambuaren din høgare enn ".expr() + fribeloep + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertall,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som bur saman med begge foreldra sine. Dette barnetillegget er derfor redusert ut frå inntekt.".expr(),
                        English to "Together, your income and your spouse/partner/cohabiting partner's income is higher than NOK ".expr() + fribeloep + ", which is the exemption amount for the child supplement for the ".expr() + ifElse(
                            barnFlertall,
                            ifTrue = "children who live",
                            ifFalse = "child who lives"
                        ) + " together with both parents. Therefore, your child supplement has been reduced on the basis of your income.".expr()
                    )
                    // Bruker mottar barnetillegg for både saerkullsbarn og fellesbarn, men kun barnetillegget for fellesbarn er ikke blitt redusert
                }.orShowIf(
                    beloepNettoFellesbarn.greaterThan(0) and justeringsbeloepFellesbarn.equalTo(0) and fradragFellesbarn.equalTo(
                        0
                    )
                        and fradragSaerkullsbarn.greaterThan(0)
                ) {
                    textExpr(
                        Bokmal to "Til sammen er inntektene til deg og ektefellenpartnerensamboeren din lavere enn ".expr() + fribeloep + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertall,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som bor med begge sine foreldre.  Dette barnetillegget er derfor ikke redusert ut fra inntekt.".expr(),
                        Nynorsk to "Til saman er inntektene til deg og ektefellen/partnaren/sambuaren din lågare enn ".expr() + fribeloep + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertall,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som bur saman med begge foreldra sine. Dette barnetillegget er derfor ikke redusert ut frå inntekt.".expr(),
                        English to "Together, your income and your spouse/partner/cohabiting partner's income is lower than NOK ".expr() + fribeloep + ", which is the exemption amount for the child supplement for the ".expr() + ifElse(
                            barnFlertall,
                            ifTrue = "children who live",
                            ifFalse = "child who lives"
                        ) + " together with both parents. Therefore, your child supplement has not been reduced on the basis of your income.".expr()
                    )
                }
            }
        }
    }


    data class TBU1286SearkullsbarnFellesbarn(
        val beloepNettoSaerkullsbarn: Expression<Kroner>,
        val beloepNettoFellesbarn: Expression<Kroner>,
        val fradragSaerkullsbarn: Expression<Kroner>,
        val fradragFellesbarn: Expression<Kroner>,
        val fribeloepSaerkullsbarn: Expression<Kroner>,
        val fribeloepFellesbarn: Expression<Kroner>,
        val justeringsbeloepSaerkullsbarn: Expression<Kroner>,
        val justeringsbeloepFellesbarn: Expression<Kroner>,
        val antallSaerkullsbarnInnvilget: Expression<Int>,
        val antallFellesbarnInnvilget: Expression<Int>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val fribeloepSaerkullsbarn = fribeloepSaerkullsbarn.format()
                val fribeloepFellesbarn = fribeloepFellesbarn.format()
                val barnFlertallSaerkullsbarn = antallSaerkullsbarnInnvilget.greaterThan(1)
                val barnFlertallFellesbarn = antallFellesbarnInnvilget.greaterThan(1)

                // Bruker mottar barnetillegg for både saerkullsbarn og fellesbarn, barnetilleggene for baade fellesbarn og saerkullsbarn er blitt redusert
                showIf(
                    (fradragFellesbarn.greaterThan(0) and justeringsbeloepFellesbarn.equalTo(0) and fradragSaerkullsbarn.greaterThan(
                        0
                    ) and justeringsbeloepSaerkullsbarn.equalTo(
                        0
                    ))
                ) {
                    textExpr(
                        Bokmal to "Inntekten din er høyere enn ".expr() + fribeloepSaerkullsbarn + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertallSaerkullsbarn,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som ikke bor sammen med begge foreldrene. Til sammen er også inntektene til deg og ektefellenpartnerensamboeren din høyere enn ".expr()
                            + fribeloepFellesbarn + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertallFellesbarn,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som bor med begge sine foreldre. Barnetilleggene er derfor redusert ut fra inntekt.".expr(),
                        Nynorsk to "Inntekta di er høgare enn ".expr() + fribeloepSaerkullsbarn + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertallSaerkullsbarn,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som ikkje bur saman med begge foreldra. Til saman er også inntektene til deg og ektefellen/partnaren/sambuaren din høgare enn ".expr()
                            + fribeloepFellesbarn + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertallFellesbarn,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som bur saman med begge foreldra sine. Desse barnetillegga er derfor redusert ut frå inntekt.".expr(),
                        English to "Your income is higher than NOK ".expr() + fribeloepSaerkullsbarn + ", which is the exemption amount for the child supplement for the ".expr() + ifElse(
                            barnFlertallSaerkullsbarn,
                            ifTrue = "children who do",
                            ifFalse = "child who does"
                        ) + " not live together with both parents. Together, your income and your spouse/partner/cohabiting partner's income is higher than NOK ".expr()
                            + fribeloepFellesbarn + ", which is the exemption amount for the child supplement for the ".expr() + ifElse(
                            barnFlertallFellesbarn,
                            ifTrue = "children who live",
                            ifFalse = "child who lives"
                        ) + " together with both parents. Therefore, your child supplements have been reduced on the basis of your income.".expr()
                    ) // Bruker mottar barnetillegg for både saerkullsbarn og fellesbarn, barnetilleggene for baade fellesbarn og saerkullsbarn er ikke blitt redusert
                }.orShowIf(
                    beloepNettoFellesbarn.greaterThan(0) and fradragFellesbarn.equalTo(0) and beloepNettoSaerkullsbarn.greaterThan(
                        0
                    ) and fradragSaerkullsbarn.equalTo(
                        0
                    )
                ) {
                    textExpr(
                        Bokmal to "Inntekten din er lavere enn ".expr() + fribeloepSaerkullsbarn + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertallSaerkullsbarn,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som ikke bor sammen med begge foreldrene. Til sammen er også inntektene til deg og ektefellenpartnerensamboeren din lavere enn ".expr()
                            + fribeloepFellesbarn + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertallFellesbarn,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som bor med begge sine foreldre. Barnetilleggene er derfor ikke redusert ut fra inntekt.".expr(),
                        Nynorsk to "Inntekta di er lågare enn ".expr() + fribeloepSaerkullsbarn + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertallSaerkullsbarn,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som ikkje bur saman med begge foreldra. Til saman er også inntektene til deg og ektefellen/partnaren/sambuaren din lågare enn ".expr()
                            + fribeloepFellesbarn + " kroner, som er fribeløpet for barnetillegget til ".expr() + ifElse(
                            barnFlertallFellesbarn,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som bur saman med begge foreldra sine. Desse barnetillegga er derfor ikkje redusert ut frå inntekt.".expr(),
                        English to "Your income is lower than NOK ".expr() + fribeloepSaerkullsbarn + ", which is the exemption amount for the child supplement for the ".expr() + ifElse(
                            barnFlertallSaerkullsbarn,
                            ifTrue = "children who do",
                            ifFalse = "child who does"
                        ) + " who do not live together with both parents. Together, your income and your spouse/partner/cohabiting partner's income is lower than NOK ".expr()
                            + fribeloepFellesbarn + ", which is the exemption amount for the child supplement for the ".expr() + ifElse(
                            barnFlertallFellesbarn,
                            ifTrue = "children who live",
                            ifFalse = "child who lives"
                        ) + " who lives together with both parents. Therefore, your child supplements have not been reduced on the basis of your income.".expr()
                    )
                }
            }
        }
    }


    // Denne skal kun når bruker mottar barnetillegg for både særkullsbarn og fellesbarn og ingen av dem blir utbetalt
    data class TBU2490(
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
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som bor med begge sine foreldre, blir ikke utbetalt fordi de samlede inntektene er høyere enn ".expr()
                            + inntektstakFellesbarn.format() + " kroner. Barnetillegget for ".expr() + ifElse(
                            barnFlertallSaerkullsbarn,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som ikke bor sammen med begge foreldrene, blir heller ikke utbetalt fordi inntekten din alene er høyere enn ".expr()
                            + inntektstakSaerkullsbarn.format() + " kroner. Inntektene er over grensen for å få utbetalt barnetillegg.".expr(),
                        Nynorsk to "Barnetillegget for ".expr() + ifElse(
                            barnFlertallFellesbarn,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei samla inntektene er høgare enn ".expr()
                            + inntektstakFellesbarn.format() + " kroner. Barnetillegget for ".expr() + ifElse(
                            barnFlertallSaerkullsbarn,
                            ifTrue = "barna",
                            ifFalse = "barnet"
                        ) + " som ikkje bur saman med begge foreldra, blir heller ikkje utbetalt fordi inntekta di åleine er høgare enn ".expr()
                            + inntektstakSaerkullsbarn.format() + " kroner. Inntektene er over grensa for å få utbetalt barnetillegg.".expr(),
                        English to "You will not receive child supplement for the ".expr() + ifElse(
                            barnFlertallFellesbarn,
                            ifTrue = "children",
                            ifFalse = "child"
                        ) + " who lives together with both parents because your total income is higher than NOK ".expr()
                            + inntektstakFellesbarn.format() + ". You will not receive child supplement for the ".expr() + ifElse(
                            barnFlertallSaerkullsbarn,
                            ifTrue = "children",
                            ifFalse = "child"
                        ) + " who do not live together with both parents because your income alone is higher than NOK ".expr()
                            + inntektstakSaerkullsbarn.format() + ". You will not receive child supplement because your income exceeds the income limit.".expr()
                    )
                }
            }
        }
    }


    data class TBU1288(
        val harBarnetilleggFellesbarn: Expression<Boolean?>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean?>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {

                ifNotNull(
                    harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn,
                ) { harBarnetilleggFellesbarn, harBarnetilleggSaerkullsbarn ->

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
}





