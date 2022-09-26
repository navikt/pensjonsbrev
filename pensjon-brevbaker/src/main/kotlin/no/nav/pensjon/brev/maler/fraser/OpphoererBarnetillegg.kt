package no.nav.pensjon.brev.maler.fraser

import io.ktor.http.*
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.OpphoererBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.UngUfoerAutoDto
import no.nav.pensjon.brev.maler.fraser.common.Felles
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


object OpphoerBarnetillegg {

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


    object TBU2223 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned.",
                    Nynorsk to "Uføretrygda blir framleis utbetalt seinast den 20. i kvar månad.",
                    English to "Your disability benefit will still be paid no later than the 20th of every month."
                )
            }
        }
    }

    object TBU1128 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "I dette brevet forklarer vi hvilke rettigheter og plikter du har. Det er derfor viktig at du leser hele brevet.",
                    Nynorsk to "I dette brevet forklarer vi kva rettar og plikter du har. Det er derfor viktig at du les heile brevet.",
                    English to "In this letter we will explain your rights and obligations. Therefore, it is important that you read the whole letter."
                )
            }
        }
    }

    //Use existing TBU1092 in <maler/fraser/vedtak/Vedtak.kt
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

    data class TBU4085(
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                showIf(harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn) {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12",
                        English to "The decision has been made pursuant to Section 12-15, 12-16 and 22-12 of the Norwegian National Insurance Act."
                    )
                }.orShow {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15, og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15, og 22-12.",
                        English to "The decision has been made pursuant to Section 12-15, and 22-12 of the Norwegian National Insurance Act."

                    )
                }
            }
        }
    }


    // TBU1174 in <maler/fraser/omregning/ufoeretrygd/Ufoeretrygd.kt
    data class TBU4086(
        val oensketVirkningsDato: Expression<LocalDate>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val dato = oensketVirkningsDato.format()
                showIf(harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn) {
                    textExpr(
                        Bokmal to "Barnetillegget ditt er blitt endret fra ".expr() + dato + ". Dette er måneden etter at barn barna har fylt 18 år. Dette kaller vi virkningstidspunktet.".expr(),
                        Nynorsk to "Barnetillegget ditt er endra frå ".expr() + dato + ". Dette er månaden etter at barnet barna  har fylt 18 år. Dette kallar vi verknadstidspunktet.".expr(),
                        English to "Your child supplement has been changed from ".expr() + dato + ". This is the month after the child children has have turned 18. This is called the effective date.".expr()
                    )
                } orShow {
                    textExpr(
                        Bokmal to "Barnetillegget ditt har opphørt fra ".expr() + dato + ". Dette er måneden etter at barnet barna har fylt 18 år. Dette kaller vi virkningstidspunktet.".expr(),
                        Nynorsk to "Barnetillegget ditt er stansa frå ".expr() + dato + ". Dette er månaden etter at barnet barna  har fylt 18 år. Dette kallar vi verknadstidspunktet.".expr(),
                        English to "Your child supplement has been discontinued from ".expr() + dato + ". This is the month after the child children has have turned 18. This is called the effective date.".expr()
                    )
                }
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
        val beloepNettoSaerkullsbarn: Expression<Kroner?>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val sivilstand: Expression<Sivilstand>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                ifNotNull(
                    beloepNettoSaerkullsbarn
                ) { beloepNettoSaerkullsbarn ->

                    val barnetilleggStoerelseEPS = sivilstand.isNotAnyOf(Sivilstand.ENSLIG, Sivilstand.SEPARERT)

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
    }

    data class TBU2339(
        val antallFellesbarnInnvilget: Expression<Int?>,
        val beloepNettoFellesbarn: Expression<Kroner?>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val sivilstand: Expression<Sivilstand>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {

                ifNotNull(
                    antallFellesbarnInnvilget,
                    beloepNettoFellesbarn,
                ) { antallFellesbarnInnvilget, beloepNettoFellesbarn ->

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
    }


    data class TBU3801(
        val harBarnetilleggFellesbarn: Expression<Boolean?>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean?>,
        val sivilstand: Expression<Sivilstand>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {

                ifNotNull(
                    harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn,
                ) { harBarnetilleggFellesbarn, harBarnetilleggSaerkullsbarn ->

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
    }

    data class TBU1284Fellesbarn(
        val beloepNettoFellesbarn: Expression<Kroner?>,
        val fribeloepFellesbarn: Expression<Kroner>,
        val inntektAnnenForelderFellesbarn: Expression<Kroner>,
        val inntektBruktiAvkortningFellesbarn: Expression<Kroner>,
        val grunnbeloepFellesbarn: Expression<Kroner>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val justeringsbeloepFellesbarn: Expression<Kroner>,
        val beloepFratrukketAnnenForeldersInntekt: Expression<Int>,
        val sivilstand: Expression<Sivilstand>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val fribeloep = fribeloepFellesbarn.format()
                val inntektAnnenForelder = inntektAnnenForelderFellesbarn.format()
                val inntektBruktiAvkortning = inntektBruktiAvkortningFellesbarn.format()
                val grunnbeloep = grunnbeloepFellesbarn.format()

                ifNotNull(
                    harBarnetilleggFellesbarn,
                    harBarnetilleggSaerkullsbarn,
                    beloepNettoFellesbarn,
                ) { harBarnetilleggFellesbarn, harBarnetilleggSaerkullsbarn, beloepNettoFellesbarn ->

                    val hoeyereLavere = inntektBruktiAvkortningFellesbarn.lessThanOrEqual(fribeloepFellesbarn)

                    showIf(harBarnetilleggFellesbarn and not(harBarnetilleggSaerkullsbarn)) {
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
                        showIf(inntektAnnenForelderFellesbarn.greaterThan(0)) {
                            textExpr(
                                Bokmal to "Folketrygdens grunnbeløp på inntil ".expr() + grunnbeloep + " kroner er holdt utenfor inntekten til ".expr(),
                                Nynorsk to "Grunnbeløpet i folketrygda på inntil ".expr() + grunnbeloep + " kroner er halde utanfor inntekta til ".expr(),
                                English to "The national insurance basic amount of up to NOK ".expr() + grunnbeloep + " has not been included in your ".expr()
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
                            harBarnetilleggFellesbarn and not(harBarnetilleggSaerkullsbarn) and justeringsbeloepFellesbarn.greaterThan(
                                0
                            ) and beloepNettoFellesbarn.greaterThan(0)
                        ) {
                            textExpr(
                                Bokmal to "Til sammen er inntektene ".expr() + ifElse(
                                    hoeyereLavere,
                                    ifTrue = "lavere",
                                    ifFalse = "høyere"
                                ) + " enn fribeløpet ditt på ".expr() + fribeloep + " kroner. Barnetillegget ditt er derfor ikke redusert ut fra inntekt.".expr(),
                                Nynorsk to "Til saman er inntektene ".expr() + ifElse(
                                    hoeyereLavere,
                                    ifTrue = "lågare",
                                    ifFalse = "høgare"
                                ) + " enn fribeløpet ditt på ".expr() + fribeloep + " kroner. Barnetillegget ditt er derfor ikkje redusert ut frå inntekt.".expr(),
                                English to "Together, the incomes are ".expr() + ifElse(
                                    hoeyereLavere,
                                    ifTrue = "lower",
                                    ifFalse = "higher"
                                ) + " than your exemption amount of NOK ".expr() + fribeloep + ". Therefore, your child supplement has not been reduced on the basis of your income.".expr()
                            )
                        }
                    }
                }
            }
        }
    }
}
/*

        ektefellenpartnerensamboeren din er ".expr()
        +inntektAnnenForelder + " kroner. Folketrygdens grunnbeløp på inntil ".expr()
        +grunnbeloep + " kroner er holdt utenfor inntekten til ektefellenpartnerensamboeren din. Til sammen er inntektene ".expr() + ifElse(
        hoeyereLavere,
        ifTrue = "lavere",
        ifFalse = "høyere"
        ) + " enn fribeløpet ditt på ".expr()
        +fribeloep + " kroner. Barnetillegget ditt er derfor ikke redusert ut fra inntekt.  Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget.  Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.".expr(),
        Nynorsk to "Inntekta di er ".expr() + inntektBruktiAvkortning + " kroner, og inntekta til ektefellen/partnaren/sambuaren din er ".expr()
        +inntektAnnenForelder + " kroner. Grunnbeløpet i folketrygda på inntil ".expr()
        +grunnbeloep + " kroner er halde utanfor inntekta til ektefellen/partnaren/sambuaren din. Til saman er inntektene ".expr() + ifElse(
        hoeyereLavere,
        ifTrue = "lågare",
        ifFalse = "høgare"
        ) + " enn fribeløpet ditt på ".expr()
        +fribeloep + " kroner. Barnetillegget ditt er derfor ikkje redusert ut frå inntekt. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.".expr(),
        English to "Your income is NOK ".expr() + inntektBruktiAvkortning + " and your spouse/partner/cohabiting partner's income is NOK ".expr()
        +inntektAnnenForelder + ". The national insurance basic amount of up to NOK ".expr()
        +grunnbeloep + " has not been included in your spouse/partner/cohabiting partner's income. Together, the incomes are ".expr() + ifElse(
        hoeyereLavere,
        ifTrue = "lower",
        ifFalse = "higher"
        ) + " than your exemption amount of NOK ".expr()
        +fribeloep + ". Therefore, your child supplement has not been reduced on the basis of your income. What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took into account when we changed your child supplement. You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year.".expr()
        )
    }.orShowIf(harBarnetilleggFellesbarn)
    {
        textExpr(
            Bokmal to "Inntekten din er ".expr() + inntektBruktiAvkortning + " kroner og inntekten til ektefellenpartnerensamboeren din er ".expr()
                + inntektBruktiAvkortning + " kroner. Folketrygdens grunnbeløp på inntil ".expr() + grunnbeloep + " kroner er holdt utenfor inntekten til ektefellenpartnerensamboeren din. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget.  Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.".expr(),
            Nynorsk to "Inntekta di er ".expr() + inntektBruktiAvkortning + " kroner, og inntekta til ektefellen/partnaren/sambuaren din er ".expr()
                + inntektAnnenForelder + " kroner. Grunnbeløpet i folketrygda på inntil ".expr()
                + grunnbeloep + " kroner er halde utanfor inntekta til ektefellen/partnaren/sambuaren din. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.".expr(),
            English to "Your income is NOK ".expr() + inntektBruktiAvkortning + " and your spouse/partner/cohabiting partner's income is NOK ".expr()
                + inntektAnnenForelder + ". The national insurance basic amount of up to NOK ".expr()
                + grunnbeloep + " has not been included in your spouse/partner/cohabiting partner's income. Therefore, your child supplement has not been reduced on the basis of your income. What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took into account when we changed your child supplement. You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year.".expr()
        )
    }
}
}
}

data class TBU1284FellesbarnSaerkullsbarn(
    val fribeloep: Expression<Kroner>,
    val inntektAnnenForelder: Expression<Kroner>,
    val inntektBruktiAvkortning: Expression<Kroner>,
    val grunnbeloep: Expression<Kroner>,
    val harBarnetilleggFellesbarn: Expression<Boolean>,
    val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
    val beloepFratrukketAnnenForeldersInntekt: Expression<Int>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            val fribeloepFellesbarn = fribeloep.format()
            val inntektAnnenForelderFellesbarn = inntektAnnenForelder.format()
            val inntektBruktiAvkortningFellesbarn = inntektBruktiAvkortning.format()
            val grunnbeloep = grunnbeloep.format()

            showIf(harBarnetilleggFellesbarn and not(harBarnetilleggSaerkullsbarn)) {
                textExpr(
                    Bokmal to "Inntekten din er ".expr() + inntektBruktiAvkortningFellesbarn + " kroner og inntekten til ektefellenpartnerensamboeren din er ".expr()
                        + inntektAnnenForelderFellesbarn + " kroner. Folketrygdens grunnbeløp på inntil ".expr()
                        + grunnbeloep + " kroner er holdt utenfor inntekten til ektefellenpartnerensamboeren din. Til sammen er inntektene høyere lavere enn fribeløpet ditt på ".expr()
                        + fribeloepFellesbarn + " kroner. Barnetillegget ditt er derfor ikke redusert ut fra inntekt.  Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget.  Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.".expr(),
                    Nynorsk to "Inntekta di er ".expr() + inntektBruktiAvkortningFellesbarn + " kroner, og inntekta til ektefellen/partnaren/sambuaren din er ".expr()
                        + inntektAnnenForelderFellesbarn + " kroner. Grunnbeløpet i folketrygda på inntil ".expr()
                        + grunnbeloep + " kroner er halde utanfor inntekta til ektefellen/partnaren/sambuaren din. Til saman er inntektene høgare/lågare enn fribeløpet ditt på ".expr()
                        + fribeloepFellesbarn + " kroner. Barnetillegget ditt er derfor ikkje redusert ut frå inntekt. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.".expr(),
                    English to "Your income is NOK ".expr() + inntektBruktiAvkortningFellesbarn + " and your spouse/partner/cohabiting partner's income is NOK ".expr()
                        + inntektAnnenForelderFellesbarn + ". The national insurance basic amount of up to NOK ".expr()
                        + grunnbeloep + " has not been included in your spouse/partner/cohabiting partner's income. Together, the incomes are higher/lower than your exemption amount of NOK ".expr()
                        + fribeloepFellesbarn + ". Therefore, your child supplement has not been reduced on the basis of your income. What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took into account when we changed your child supplement. You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year.".expr()
                )
            }.orShowIf(harBarnetilleggFellesbarn) {
                textExpr(
                    Bokmal to "Inntekten din er ".expr() + inntektBruktiAvkortningFellesbarn + " kroner og inntekten til ektefellenpartnerensamboeren din er ".expr()
                        + inntektBruktiAvkortningFellesbarn + " kroner. Folketrygdens grunnbeløp på inntil ".expr() + grunnbeloep + " kroner er holdt utenfor inntekten til ektefellenpartnerensamboeren din. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget.  Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.".expr(),
                    Nynorsk to "Inntekta di er ".expr() + inntektBruktiAvkortningFellesbarn + " kroner, og inntekta til ektefellen/partnaren/sambuaren din er ".expr()
                        + inntektAnnenForelderFellesbarn + " kroner. Grunnbeløpet i folketrygda på inntil ".expr()
                        + grunnbeloep + " kroner er halde utanfor inntekta til ektefellen/partnaren/sambuaren din. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.".expr(),
                    English to "Your income is NOK ".expr() + inntektBruktiAvkortningFellesbarn + " and your spouse/partner/cohabiting partner's income is NOK ".expr()
                        + inntektAnnenForelderFellesbarn + ". The national insurance basic amount of up to NOK ".expr()
                        + grunnbeloep + " has not been included in your spouse/partner/cohabiting partner's income. Therefore, your child supplement has not been reduced on the basis of your income. What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took into account when we changed your child supplement. You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year.".expr()
                )
            }
        }
    }
}

data class TBU1285(
    val fribeloepSaerkullsbarn: Expression<Kroner>,
    val inntektBruktiAvkortningSaerkullsbarn: Expression<Kroner>,
    val beloepNettoSaerkullsbarn: Expression<Kroner>,
    val justeringsbeloepSaerkullsbarn: Expression<Kroner>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            val fribeloep = fribeloepSaerkullsbarn.format()
            val inntektBruktiAvkortning = inntektBruktiAvkortningSaerkullsbarn.format()
            val hoeyereLavere = inntektBruktiAvkortningSaerkullsbarn.lessThanOrEqual(fribeloepSaerkullsbarn)
            val ikke = inntektBruktiAvkortningSaerkullsbarn.lessThanOrEqual(fribeloepSaerkullsbarn)

            showIf(beloepNettoSaerkullsbarn.greaterThan(0) and justeringsbeloepSaerkullsbarn.equalTo(0)) {
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
            }.orShowIf(beloepNettoSaerkullsbarn.equalTo(0) and justeringsbeloepSaerkullsbarn.equalTo(0)) {
                textExpr(
                    Bokmal to "Inntekten din er ".expr() + inntektBruktiAvkortning + " kroner. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.".expr(),
                    Nynorsk to "Inntekta di er ".expr() + inntektBruktiAvkortning + " kroner. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.".expr(),
                    English to "Your income is NOK ".expr() + inntektBruktiAvkortning + ". You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year.".expr()
                )
            }.orShowIf(
                beloepNettoSaerkullsbarn.equalTo(0) and justeringsbeloepSaerkullsbarn.greaterThan(
                    0
                )
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

data class TBU1286Saerkullsbarn(
    val beloepNettoSaerkullsbarn: Expression<Kroner>,
    val fradragSaerkullsbarn: Expression<Kroner>,
    val fradragFellesbarn: Expression<Kroner>,
    val fribeloepSaerkullsbarn: Expression<Kroner>,
    val justeringsbeloepSaerkullsbarn: Expression<Kroner>,
    val antallSaerkullsbarnInnvilget: Expression<Int?>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            val fribeloep = fribeloepSaerkullsbarn.format()

            ifNotNull(antallSaerkullsbarnInnvilget) { antallSaerkullsbarnInnvilget ->
                val barnFlertall = antallSaerkullsbarnInnvilget.greaterThan(1)

                // Bruker mottar barnetillegg for både saerkullsbarn og fellesbarn, men kun barnetillegget for saerkullsbarn er blitt redusert
                showIf(
                    beloepNettoSaerkullsbarn.notEqualTo(0) and justeringsbeloepSaerkullsbarn.equalTo(0) and fradragFellesbarn.greaterThan(
                        0
                    )
                        and fradragSaerkullsbarn.equalTo(0)
                ) {
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
}

data class TBU1286Fellesbarn(
    val beloepNettoFellesbarn: Expression<Kroner>,
    val fradragSaerkullsbarn: Expression<Kroner>,
    val fradragFellesbarn: Expression<Kroner>,
    val fribeloepFellesbarn: Expression<Kroner>,
    val justeringsbeloepFellesbarn: Expression<Kroner>,
    val antallFellesbarnInnvilget: Expression<Int?>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            val fribeloep = fribeloepFellesbarn.format()

            ifNotNull(antallFellesbarnInnvilget) { antallFellesbarnInnvilget ->
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
        val antallSaerkullsbarnInnvilget: Expression<Int?>,
        val antallFellesbarnInnvilget: Expression<Int?>,

        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val fribeloepSaerkullsbarn = fribeloepSaerkullsbarn.format()
                val fribeloepFellesbarn = fribeloepFellesbarn.format()

                ifNotNull(
                    antallSaerkullsbarnInnvilget,
                    antallFellesbarnInnvilget
                ) { antallSaerkullsbarnInnvilget, antallFellesbarnInnvilget ->
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


// TBU1286.1 in <maler/fraser/omregning/ufoeretrygd/Ufoeretrygd.kt
// TBU1286.2 in <maler/fraser/omregning/ufoeretrygd/Ufoeretrygd.kt

        data

        class TBU2490(

            val fellesbarnInntektstak: Expression<Kroner>,
            val saerkullsbarnInntektstak: Expression<Kroner>,

            ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                paragraph {
                    val inntektstakFellesbarn = fellesbarnInntektstak.format()
                    val inntektstakSaerkullsbarn = saerkullsbarnInntektstak.format()
                    textExpr(
                        Bokmal to "Barnetillegget for barnetbarna som bor med begge sine foreldre, blir ikke utbetalt fordi de samlede inntektene er høyere enn ".expr()
                            + inntektstakFellesbarn + " kroner. Barnetillegget for barnetbarna som ikke bor sammen med begge foreldrene, blir heller ikke utbetalt fordi inntekten din alene er høyere enn " + inntektstakSaerkullsbarn + " kroner. Inntektene er over grensen for å få utbetalt barnetillegg.".expr(),
                        Nynorsk to "Barnetillegget for barnet/barna som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei samla inntektene er høgare enn ".expr()
                            + inntektstakFellesbarn + " kroner. Barnetillegget for barnet/barna som ikkje bur saman med begge foreldra, blir heller ikkje utbetalt fordi inntekta di åleine er høgare enn " + inntektstakSaerkullsbarn + " kroner. Inntektene er over grensa for å få utbetalt barnetillegg.".expr(),
                        English to "You will not receive child supplement for the child/children who lives together with both parents because your total income is higher than NOK ".expr()
                            + inntektstakFellesbarn + ". You will not receive child supplement for the child/children who do not live together with both parents because your income alone is higher than NOK " + inntektstakSaerkullsbarn + ". You will not receive child supplement because your income exceeds the income limit.".expr()
                    )
                }
            }
        }

        object TBU1288 : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                paragraph {
                    text(
                        Bokmal to "Du kan lese mer om beregningen av barnetillegg i vedlegget «Opplysninger om beregningen».",
                        Nynorsk to "Du kan lese meir om berekninga av barnetillegg i vedlegget «Opplysningar om berekninga».",
                        English to "Read more about how child supplements are calculated in the attachment called «Information about calculations»."
                    )
                }
            }
        }

        object TBU2364 : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                paragraph {
                    text(
                        Bokmal to "Du må melde fra om eventuell inntekt",
                        Nynorsk to "Du må melde frå om eventuell inntekt",
                        English to "Report any income"
                    )
                }
            }
        }

        object TBU2365 : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                paragraph {
                    text(
                        Bokmal to "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd. Dette kan du gjøre under menyvalget «uføretrygd» når du logger deg inn på nav.no. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd ved siden av inntekten din.",
                        Nynorsk to "Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di. Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd. Dette kan du gjere under menyvalet «uføretrygd» når du logger deg inn på nav.no. Her kan du leggje inn kor mykje du forventar å tene i løpet av året. Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd ved sida av inntekta di.",
                        English to "If you are working or are planning to work, you must report any changes in your income. It is important that you report this as soon as possible, so that you receive the correct disability benefit payments. You can register your change in income under the option “uføretrygd” at nav.no. You can register how much you expect to earn in the calendar year. You will then be able to see how much disability benefit you will receive in addition to your income."
                    )
                }
            }
        }

        object TBU2212 : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                title1 {
                    text(
                        Bokmal to "Du må melde fra om endringer",
                        Nynorsk to "Du må melde frå om endringar",
                        English to "You must notify any changes"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Skjer det endringer, må du melde fra til oss med en gang. I vedlegget «Orientering om rettigheter og plikter» ser du hvilke endringer du må si fra om.",
                        Nynorsk to "Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget «Orientering om rettar og plikter» ser du kva endringar du må seie frå om.",
                        English to "You must notify us immediately of any changes in your situation. In the attachment “Information about rights and obligations” you will see which changes you must report."
                    )
                }
            }
        }

        object TBU2213 : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                title1 {
                    text(
                        Bokmal to "Du har rett til å klage",
                        Nynorsk to "Du har rett til å klage",
                        English to "You have the right of appeal"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du mottok vedtaket. I vedlegget «Orientering om rettigheter og plikter» får du vite mer om hvordan du går fram. Du finner skjema og informasjon på ${Constants.KLAGE_URL}.",
                        Nynorsk to "Viss du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. I vedlegget «Orientering om rettar og plikter» får du vite meir om korleis du går fram. Du finn skjema og informasjon på ${Constants.KLAGE_URL}.",
                        English to "If you believe the decision is wrong, you may appeal. The deadline for appeal is six weeks from the date you received the decision. In the attachment “Information about rights and obligations”, you can find out more about how to proceed. You will find forms and information at ${Constants.KLAGE_URL}."

                    )
                }
            }
        }

        // TBU1074 in <maler/fraser/common/Felles.kt
        object TBU2242 : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                paragraph {
                    text(
                        Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg «Orientering om rettigheter og plikter» for informasjon om hvordan du går fram.",
                        Nynorsk to "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg «Orientering om rettar og plikter» for informasjon om korleis du går fram.",
                        English to "You are entitled to see your case documents. Refer to the attachment “Rights and obligations” for information about how to proceed."
                    )
                }
            }
        }

// TBU1227 in <maler/fraser/omregning/ufoeretrygd/Ufoeretrygd.kt

        object TBU1228 : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                title1 {
                    text(
                        Bokmal to "Skattekort",
                        Nynorsk to "Skattekort",
                        English to "Tax card"
                    )

                }
                paragraph {
                    text(
                        Bokmal to "Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet ditt til NAV fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten. Du bør likevel sjekke at du har riktig skattekort. Skattekortet kan du endre på skatteetaten.no. Under menyvalget «uføretrygd» når du logger deg inn på nav.no, kan du se hvilket skattetrekk som er registrert hos NAV.",
                        Nynorsk to "Uføretrygd blir skattlagd som lønsinntekt. Du treng ikkje levere skattekortet ditt til NAV, fordi skatteopplysningane dine blir sende elektronisk frå Skatteetaten. Du bør likevel sjekke at du har rett skattekort. Skattekortet kan du endre på skatteetaten.no. Under menyvalet «uføretrygd» når du logger deg inn på nav.no, kan du sjå kva skattetrekk som er registrert hos NAV.",
                        English to "You do not need to submit your tax card to NAV because your tax details are sent electronically from the Norwegian Tax Administration. However, you should check that you have the correct tax card. You may change your tax card under skatteetaten.no. You may see your registered income tax rate under the option “uføretrygd” at nav.no."
                    )
                }
            }
        }

        object TBU3730 : OutlinePhrase<LangBokmalNynorskEnglish>() {
            override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
                title1 {
                    text(
                        Bokmal to "Skatt for deg som bor i utlandet",
                        Nynorsk to "Skatt for deg som bur i utlandet",
                        English to "Tax for people who live abroad"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på skatteetaten.no. Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor.",
                        Nynorsk to "Bur du i utlandet og betaler kjeldeskatt, finn du meir informasjon om kjeldeskatt på skatteetaten.no. Viss du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur.",
                        English to "You can find more information about withholding tax to Norway at skatteetaten.no. For information about taxation from your country of residence, you can contact the locale tax authorities."
                    )
                }
            }
        }
    }
}
} */

