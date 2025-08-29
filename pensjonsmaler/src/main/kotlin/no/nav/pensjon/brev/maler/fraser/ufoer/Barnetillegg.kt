package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.BarnetilleggFellesbarnSelectors.brukerBorMed
import no.nav.pensjon.brev.maler.fraser.common.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
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

                text(
                    bokmal { + "Vi har vedtatt at barnetillegget i uføretrygden din opphører fra " + virkningsDato +
                            " for " + ifElse(barnFlertall, "barna", "barn") + " født" },
                    nynorsk { + "Vi har stansa barnetillegget i uføretrygda di frå " + virkningsDato +
                            " for " + ifElse(barnFlertall, "barna", "barn") + " fødd" },
                    english { + "The child supplement in your disability benefit has been discontinued, effective as of " + virkningsDato +
                            ", for " + ifElse(barnFlertall, "children", "the child") + " born" },
                )
                includePhrase(Felles.TextOrList(foedselsdatoPaaBarnMedOpphoertBarnetillegg.map(LocalizedFormatter.DateFormat)))
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
                text(
                    bokmal { + "For å ha rett til barnetillegg må du forsørge barn under 18 år. Vi har vedtatt at barnetillegget i uføretrygden opphører fordi " +
                            ifElse(barnFlertall, "barna", "barnet") + " har fylt 18 år." },
                    nynorsk { + "For å ha rett til barnetillegg må du forsørgje barn under 18 år. Vi har stansa barnetillegget i uføretrygda fordi " +
                            ifElse(barnFlertall, "barna", "barnet") + " har fylt 18 år." },
                    english { + "To be eligible for child supplement, you must support children under 18 years of age. The child supplement in your disability benefit has been discontinued because your " +
                            ifElse(barnFlertall, "children have", "child has") + " turned 18 years of age." }
                )
            }
        }
    }

    // TBU2338
    data class InntektHarBetydningForSaerkullsbarnTillegg(
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val borMedSivilstand: Expression<BorMedSivilstand?>,
        val faarUtbetaltBarnetilleggSaerkullsbarn: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

            showIf(harBarnetilleggSaerkullsbarn and not(harBarnetilleggFellesbarn)) {
                paragraph {
                    text(
                        bokmal { + "Inntekten din har betydning for hva du får i barnetillegg. Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert." },
                        nynorsk { + "Inntekta di har noko å seie for kva du får i barnetillegg. Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert." },
                        english { + "Your income affects how much you receive in child supplement. If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced." }
                    )
                    showIf(faarUtbetaltBarnetilleggSaerkullsbarn) {
                        text(
                            bokmal { + " Denne grensen kaller vi for fribeløp." },
                            nynorsk { + " Denne grensa kallar vi for fribeløp." },
                            english { + " We call this limit the exemption amount." }
                        )
                    }
                    ifNotNull(borMedSivilstand) { borMedSivilstand ->
                        text(
                            bokmal { + " Inntekten til " + borMedSivilstand.bestemtForm() + " din har ikke betydning for størrelsen på barnetillegget." },
                            nynorsk { + " Inntekta til " + borMedSivilstand.bestemtForm() + " din har ikkje noko å seie for storleiken på barnetillegget." },
                            english { + " The income of your " + borMedSivilstand.bestemtForm() + " does not affect the size of your child supplement." },
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
        val borMedSivilstand: Expression<BorMedSivilstand>,
        val barnetilleggSaerkullsbarnGjelderFlereBarn: Expression<Boolean>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(harBarnetilleggFellesbarn) {
                paragraph {
                    text(
                        bokmal { + "Inntekten til deg og " + borMedSivilstand.bestemtForm() + " din har betydning for hva du får i barnetillegg. Er inntektene over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert." },
                        nynorsk { + "Inntekta til deg og " + borMedSivilstand.bestemtForm() + " din har noko å seie for kva du får i barnetillegg. Er den samla inntekta over grensa for å få utbetalt fullt barnetillegg, blir tillegget ditt redusert." },
                        english { + "The incomes of you and your " + borMedSivilstand.bestemtForm() + " affects how much you receive in child supplement. If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced." },
                    )
                    showIf(faarUtbetaltBarnetilleggFellesbarn) {
                        text(
                            bokmal { + " Denne grensen kaller vi for fribeløp." },
                            nynorsk { + " Denne grensa kallar vi for fribeløp." },
                            english { + " We call this limit the exemption amount." }
                        )
                    }
                    showIf(harBarnetilleggSaerkullsbarn) {
                        text(
                            bokmal { + " Inntekten til " + borMedSivilstand.bestemtForm() + " din har kun betydning for størrelsen på barnetillegget til "
                                    + ifElse(barnetilleggSaerkullsbarnGjelderFlereBarn, "barna", "barnet")
                                    + " som bor sammen med begge sine foreldre." },

                            nynorsk { + " Inntekta til " + borMedSivilstand.bestemtForm() + " din har berre betydning for storleiken på barnetillegget til "
                                    + ifElse(barnetilleggSaerkullsbarnGjelderFlereBarn, "barna", "barnet")
                                    + " som bur saman med begge foreldra sine." },

                            english { + " The income of your " + borMedSivilstand.bestemtForm() + " only affects the size of the child supplement for the children who live together with both parents." },
                        )
                    }
                }
            }
        }
    }


    // TBU3801
    data class BetydningAvInntektEndringer(
        val barnetilleggFellesbarn: Expression<BarnetilleggFellesbarn?>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(barnetilleggFellesbarn.notNull() or harBarnetilleggSaerkullsbarn) {
                paragraph {
                    ifNotNull(barnetilleggFellesbarn) { fellesTillegg ->
                        val sivilstand = fellesTillegg.brukerBorMed
                        text(
                            bokmal { + "Endringer i inntektene til deg og " + sivilstand.bestemtForm() + " din kan ha betydning for barnetillegget ditt. " },
                            nynorsk { + "Endringar i inntektene til deg og " + sivilstand.bestemtForm() + " di kan ha betydning for barnetillegget ditt. " },
                            english { + "Changes in your and your " + sivilstand.bestemtForm() + " income may affect your child supplement. " },
                        )
                    }.orShowIf(harBarnetilleggSaerkullsbarn) {
                        text(
                            bokmal { + "Endringer i inntekten din kan ha betydning for barnetillegget ditt. " },
                            nynorsk { + "Endringar i inntekta di kan ha betydning for barnetillegget ditt. " },
                            english { + "Changes in income may affect your child supplement. " }
                        )
                    }
                    text(
                        bokmal { + "Du kan enkelt melde fra om inntektsendringer under menyvalget " + quoted("uføretrygd") +" på ${Constants.NAV_URL}." },
                        nynorsk { + "Du kan enkelt melde frå om inntektsendringar under menyvalet " + quoted("uføretrygd") +" på ${Constants.NAV_URL}." },
                        english { + "You can easily report income changes under the menu option " + quoted("disability benefit") + " at ${Constants.NAV_URL}." }
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
        val borMedSivilstand: Expression<BorMedSivilstand>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val grunnbeloep = grunnbeloep.format()

            paragraph {
                text(
                    bokmal { + "Inntekten din er " + brukersInntektBruktiAvkortningFellesbarn.format() + " og inntekten til " + borMedSivilstand.bestemtForm() + " din er " + inntektAnnenForelderFellesbarn.format() + "." },
                    nynorsk { + "Inntekta di er " + brukersInntektBruktiAvkortningFellesbarn.format() + ", og inntekta til " + borMedSivilstand.bestemtForm() + " din er " + inntektAnnenForelderFellesbarn.format() + "." },
                    english { + "Your income is " + brukersInntektBruktiAvkortningFellesbarn.format() + " and your " + borMedSivilstand.bestemtForm() + "'s income is " + inntektAnnenForelderFellesbarn.format() + "." },
                )

                showIf(harBeloepFratrukketAnnenForelder) {
                    text(
                        bokmal { + " Folketrygdens grunnbeløp på inntil " + grunnbeloep + " er holdt utenfor inntekten til "
                                + borMedSivilstand.bestemtForm() + " din." },
                        nynorsk { + " Grunnbeløpet i folketrygda på inntil " + grunnbeloep + " er halde utanfor inntekta til "
                                + borMedSivilstand.bestemtForm() + " din." },
                        english { + " The national insurance basic amount of up to " + grunnbeloep + " has not been included in your "
                                + borMedSivilstand.bestemtForm() + "'s income." },
                    )
                }

                showIf(not(harBarnetilleggSaerkullsbarn) and not(harJusteringsbeloepFellesbarn) and faarUtbetaltBarnetilleggFellesBarn) {
                    text(
                        bokmal { + " Til sammen er inntektene " +
                                ifElse(harFradragFellesbarn, "høyere", "lavere") +
                                " enn fribeløpet ditt på " + fribeloepFellesbarn.format() + ". Barnetillegget ditt er derfor" +
                                ifElse(harFradragFellesbarn, "", " ikke") + " redusert ut fra inntekt." },

                        nynorsk { + " Til saman er inntektene " +
                                ifElse(harFradragFellesbarn, "høgare", "lågare") +
                                " enn fribeløpet ditt på " + fribeloepFellesbarn.format() + ". Barnetillegget ditt er derfor" +
                                ifElse(harFradragFellesbarn, "", " ikkje") +
                                " redusert ut frå inntekt." },

                        english { + " Together, the incomes are " +
                                ifElse(harFradragFellesbarn, "higher", "lower") +
                                " than your exemption amount of " + fribeloepFellesbarn.format() +
                                ". Therefore, your child supplement has" +
                                ifElse(harFradragFellesbarn, "", " not") +
                                " been reduced on the basis of your income." }
                    )
                }

                showIf(harJusteringsbeloepFellesbarn and not(harBarnetilleggSaerkullsbarn)) {
                    includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
                    text(
                        bokmal { + " Dette ble tatt hensyn til da vi endret barnetillegget." },
                        nynorsk { + " Dette har vi teke omsyn til når vi endra barnetillegget." },
                        english { + " This we took into account when we changed your child supplement." }
                    )
                    showIf(not(faarUtbetaltBarnetilleggFellesBarn)) {
                        text(
                            bokmal { + " Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                            nynorsk { + " Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                            english { + " You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year." }
                        )
                    }
                }
            }
        }
    }


    object DuHarFaattUtbetaltBarnetilleggTidligereIAar : ParagraphPhrase<LangBokmalNynorskEnglish>() {
        override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            text(
                bokmal { + "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover." },
                nynorsk { + "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover." },
                english { + "What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future." },
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

                    text(
                        bokmal { + "Inntekten din er " + inntektBruktiAvkortning + "." },
                        nynorsk { + "Inntekta di er " + inntektBruktiAvkortning + "." },
                        english { + "Your income is  " + inntektBruktiAvkortning + "." }
                    )
                    includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
                    text(
                        bokmal { + " Dette ble tatt hensyn til da vi endret barnetillegget." },
                        nynorsk { + " Dette har vi teke omsyn til når vi endra barnetillegget." },
                        english { + " We took this in to account when we changed your child supplement." },
                    )
                    showIf(not(faarUtbetaltBarnetilleggSaerkullsbarn)) {
                        text(
                            bokmal { + " Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                            nynorsk { + " Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                            english { + " You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year." }
                        )
                    }
                }
            }.orShowIf(faarUtbetaltBarnetilleggSaerkullsbarn) {
                paragraph {
                    text(
                        bokmal { + "Inntekten din på " + inntektBruktiAvkortning + " er " +
                                ifElse(hoeyereLavere, "lavere", "høyere") +
                                " enn fribeløpet ditt på " + fribeloep + ". Barnetillegget ditt er derfor " +
                                ifElse(inntektLavereEnnFribeloep, "ikke ", "") + "redusert ut fra inntekt." },

                        nynorsk { + "Inntekta di på " + inntektBruktiAvkortning + " er " +
                                ifElse(hoeyereLavere, "lågare", "høgare") +
                                " enn fribeløpet ditt på " + fribeloep + ". Barnetillegget ditt er derfor " +
                                ifElse(inntektLavereEnnFribeloep, "ikkje ", "") + "redusert ut frå inntekt." },

                        english { + "Your income of " + inntektBruktiAvkortning + " is " +
                                ifElse(hoeyereLavere, "lower", "higher") +
                                " than your exemption amount of " +
                                fribeloep + ". Therefore your child supplement has " +
                                ifElse(inntektLavereEnnFribeloep, "not ", "") +
                                "been reduced on the basis of your income." }
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
        val harTilleggForFlereSaerkullsbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val beloepBruttoFellesbarn: Expression<Kroner>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val beloepNettoFellesbarn: Expression<Kroner>,
        val fribeloepFellesbarn: Expression<Kroner>,
        val harJusteringsbeloepFellesbarn: Expression<Boolean>,
        val harTilleggForFlereFellesbarn: Expression<Boolean>,
        val borMed: Expression<BorMedSivilstand>,
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
                    text(
                        bokmal { + "Inntekten din er " +
                                ifElse(inntektOverFribeloepSaerkullsbarn, "høyere", "lavere") +
                                " enn " + fribeloepSaerkullsbarn.format() + ", som er fribeløpet for barnetillegget til " +
                                ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet") +
                                " som ikke bor sammen med begge foreldrene." },

                        nynorsk { + "Inntekta di er " +
                                ifElse(inntektOverFribeloepSaerkullsbarn, "høgare", "lågare") +
                                " enn " + fribeloepSaerkullsbarn.format() + ", som er fribeløpet for barnetillegget til " +
                                ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet") +
                                " som ikkje bur saman med begge foreldra." },

                        english { + "Your income is " +
                                ifElse(inntektOverFribeloepSaerkullsbarn, "higher", "lower") +
                                " higher than " + fribeloepSaerkullsbarn.format() +
                                ", which is the exemption amount for the child supplement for the " +
                                ifElse(harTilleggForFlereSaerkullsbarn, "children who do", "child who does") +
                                " not live together with both parents." }
                    )

                    showIf(harFradragForEnBarnetilleggYtelse) {
                        text(
                            bokmal { + " Dette barnetillegget er derfor" +
                                    ifElse(barnetilleggSaerkullsbarnIkkeRedusert, " ikke ", " ") +
                                    "redusert ut fra inntekt." },

                            nynorsk { + " Dette barnetillegget er derfor" +
                                    ifElse(barnetilleggSaerkullsbarnIkkeRedusert, " ikkje ", " ") +
                                    "redusert ut frå inntekt." },

                            english { + " Therefore, your child supplement has" +
                                    ifElse(barnetilleggSaerkullsbarnIkkeRedusert, " not ", " ") +
                                    "been reduced on the basis of your income." }
                        )
                    }
                    showIf(faarUtbetaltBarnetilleggFellesbarn and not(harJusteringsbeloepFellesbarn)) {

                        val ogsaa = not(harFradragForEnBarnetilleggYtelse)
                        val inntektErHoyereEnnFribeloepFellesBarn = samletInntektBruktiAvkortningFellesbarn.greaterThan(fribeloepFellesbarn)
                        text(
                            bokmal { + " Til sammen er " +
                                    ifElse(ogsaa, "også inntektene", "inntektene") +
                                    " til deg og " + borMed.bestemtForm() + " din " +
                                    ifElse(inntektErHoyereEnnFribeloepFellesBarn, "høyere", "lavere") +
                                    " enn " + fribeloepFellesbarn.format() + ", som er fribeløpet for barnetillegget til " +
                                    ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                                    " som bor med begge sine foreldre." },

                            nynorsk { + " Til saman er " +
                                    ifElse(ogsaa, "også inntektene", "inntektene") +
                                    " til deg og " + borMed.bestemtForm() + " din " +
                                    ifElse(inntektErHoyereEnnFribeloepFellesBarn, "høgare", "lagare") +
                                    " enn " + fribeloepFellesbarn.format() + ", som er fribeløpet for barnetillegget til " +
                                    ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                                    " som bur saman med begge foreldra sine." },

                            english { + " Together, your income and your " + borMed.bestemtForm() + "'s income is " +
                                    ifElse(inntektErHoyereEnnFribeloepFellesBarn, "higher", "lower") +
                                    " than " + fribeloepFellesbarn.format() + ", which is the exemption amount for the child supplement for the " +
                                    ifElse(harTilleggForFlereFellesbarn, "children who live", "child who lives") +
                                    " together with both parents." },
                        )

                        showIf(harFradragForEnBarnetilleggYtelse) {
                            text(
                                bokmal { + 
                                        " Dette barnetillegget er derfor" +
                                        ifElse(barnetilleggFellesbarnIkkeRedusert, " ikke ", " ") +
                                        "redusert ut fra inntekt." },

                                nynorsk { + " Dette barnetillegget er derfor" +
                                        ifElse(barnetilleggFellesbarnIkkeRedusert, " ikkje ", " ") +
                                        "redusert ut frå inntekt." },

                                english { + " Therefore, your child supplement has" +
                                        ifElse(barnetilleggFellesbarnIkkeRedusert, " not ", " ") +
                                        "been reduced on the basis of your income." }
                            )
                        }.orShow {
                            val ikkeRedusert =
                                barnetilleggFellesbarnIkkeRedusert and barnetilleggSaerkullsbarnIkkeRedusert
                            text(
                                bokmal { + " Barnetilleggene er derfor" +
                                        ifElse(ikkeRedusert, " ikke ", " ") +
                                        "redusert ut fra inntekt." },

                                nynorsk { + " Desse barnetillegga er derfor" +
                                        ifElse(ikkeRedusert, " ikkje ", " ") +
                                        "redusert ut frå inntekt." },

                                english { + " Therefore your child supplements have" +
                                        ifElse(ikkeRedusert, " not ", " ") +
                                        "been reduced on the basis of your income." }
                            )
                        }
                    }
                }
            }

            showIf(harJusteringsbeloepFellesbarn) {
                paragraph {
                    includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
                    text(
                        bokmal { + " Dette ble tatt hensyn til da vi endret barnetillegget for " +
                                ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                                " som bor med begge sine foreldre." },

                        nynorsk { + " Dette har vi teke omsyn til når vi endra barnetillegget for " +
                                ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                                " som bur saman med begge foreldra sine." },

                        english { + " We took this in to account when we changed your child supplement for the " +
                                ifElse(harTilleggForFlereFellesbarn, "children who live", "child who lives") +
                                " together with both parents." }
                    )
                    showIf(not(faarUtbetaltBarnetilleggFellesbarn)) {
                        text(
                            bokmal { + " Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året" },
                            nynorsk { + " Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                            english { + " You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year." }
                        )
                    }
                }
            }

            showIf(harJusteringsbeloepSaerkullsbarn) {
                paragraph {
                    includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
                    text(
                        bokmal { + " Dette ble tatt hensyn til da vi endret barnetillegget for " +
                                ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet") +
                                " som ikke bor sammen med begge foreldrene." },

                        nynorsk { + " Dette har vi teke omsyn til når vi endra barnetillegget for " +
                                ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet") +
                                " som ikkje bur saman med begge foreldra." },

                        english { + " We took this in to account when we changed your child supplement for the " +
                                ifElse(harTilleggForFlereSaerkullsbarn, "children who live", "child who lives") +
                                " together with both parents." }
                    )
                    showIf(not(faarUtbetaltBarnetilleggSaerkullsbarn)) {
                        text(
                            bokmal { + " Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                            nynorsk { + " Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                            english { + " You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year." }
                        )
                    }
                }
            }
        }
    }

    // TBU1286.1, TBU1286.2, TBU2490
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
                        text( // TBU2490
                            bokmal { + "Barnetillegget for " +
                                    ifElse(harFlereFellesBarn, "barna", "barnet") +
                                    " som bor med begge sine foreldre, blir ikke utbetalt fordi de samlede inntektene er høyere enn " +
                                    inntektstakFellesbarn.format() + ". Barnetillegget for " +
                                    ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                    " som ikke bor sammen med begge foreldrene, blir heller ikke utbetalt fordi inntekten din alene er høyere enn " +
                                    inntektstakSaerkullsbarn.format() + ". Inntektene er over grensen for å få utbetalt barnetillegg." },

                            nynorsk { + "Barnetillegget for " +
                                    ifElse(harFlereFellesBarn, "barna", "barnet") +
                                    " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei samla inntektene er høgare enn " +
                                    inntektstakFellesbarn.format() + ". Barnetillegget for " +
                                    ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                    " som ikkje bur saman med begge foreldra, blir heller ikkje utbetalt fordi inntekta di åleine er høgare enn " +
                                    inntektstakSaerkullsbarn.format() + ". Inntektene er over grensa for å få utbetalt barnetillegg." },

                            english { + "You will not receive child supplement for the " +
                                    ifElse(harFlereFellesBarn, "children", "child") +
                                    " who lives together with both parents because your total income is higher than " +
                                    inntektstakFellesbarn.format() + ". You will not receive child supplement for the " +
                                    ifElse(harFlereSaerkullsbarn, "children", "child") +
                                    " who do not live together with both parents because your income alone is higher than " +
                                    inntektstakSaerkullsbarn.format() + ". You will not receive child supplement because your income exceeds the income limit." }
                        )
                    }.orShowIf(not(saerkullUtbetalt) and saerkullInnvilget and not(fellesInnvilget)) {
                        text( // TBU1286.1
                            bokmal { + "Barnetillegget for " +
                                    ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                    " blir ikke utbetalt fordi du har en samlet inntekt som er høyere enn " +
                                    inntektstakFellesbarn.format() + ". Inntekten din er over grensen for å få utbetalt barnetillegg." },

                            nynorsk { + "Barnetillegget for " +
                                    ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                    " blir ikkje utbetalt fordi du har ei samla inntekt som er høgare enn " +
                                    inntektstakFellesbarn.format() + ". Inntekta di er over grensa for å få utbetalt barnetillegg." },

                            english { + "You will not receive child supplement for the " +
                                    ifElse(harFlereSaerkullsbarn, "children", "child") +
                                    " because your total income on its own is higher than " +
                                    inntektstakFellesbarn.format() + ". You will not receive child supplement because your income exceeds the income limit." }
                        )
                    }.orShowIf(not(saerkullUtbetalt) and saerkullInnvilget and fellesInnvilget) {
                        text( // TBU1286.1
                            bokmal { + "Barnetillegget for " +
                                    ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                    " som ikke bor sammen med begge foreldrene, blir ikke utbetalt fordi du alene har en samlet inntekt som er høyere enn " +
                                    inntektstakFellesbarn.format() + ". Inntekten din er over grensen for å få utbetalt barnetillegg." },

                            nynorsk { + "Barnetillegget for " +
                                    ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                    " som ikkje bur saman med begge foreldra sine, blir ikkje utbetalt fordi du åleine har ei samla inntekt som er høgare enn " +
                                    inntektstakFellesbarn.format() + ". Inntekta di er over grensa for å få utbetalt barnetillegg." },

                            english { + "You will not receive child supplement for the " +
                                    ifElse(harFlereSaerkullsbarn, "children who do", "child who does") +
                                    " not live together with both parents because your total income on its own is higher than " +
                                    inntektstakFellesbarn.format() + ". You will not receive child supplement because your income exceeds the income limit." }
                        )
                    }.orShowIf(not(fellesUtbetalt) and not(saerkullInnvilget) and fellesInnvilget) {
                        text(// TBU1286.2
                            bokmal { + "Barnetillegget for " +
                                    ifElse(harFlereFellesBarn, "barna", "barnet") +
                                    " blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " +
                                    inntektstakFellesbarn.format() + ". De samlede inntektene er over grensen for å få utbetalt barnetillegg." },

                            nynorsk { + "Barnetillegget for " +
                                    ifElse(harFlereFellesBarn, "barna", "barnet") +
                                    " blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " +
                                    inntektstakFellesbarn.format() + ". Dei samla inntektene er over grensa for å få utbetalt barnetillegg." },

                            english { + "You will not receive child supplement for the " +
                                    ifElse(harFlereFellesBarn, "children", "child") +
                                    " because your total income on its own is higher than " +
                                    inntektstakFellesbarn.format() + ". You will not receive child supplement because your combined incomes exceed the income limit." }
                        )
                    }.orShowIf(not(fellesUtbetalt) and saerkullInnvilget and fellesInnvilget) {
                        text(// TBU1286.2
                            bokmal { + "Barnetillegget for " +
                                    ifElse(harFlereFellesBarn, "barna", "barnet") +
                                    " som bor med begge sine foreldre, blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " +
                                    inntektstakFellesbarn.format() + ". De samlede inntektene er over grensen for å få utbetalt barnetillegg." },

                            nynorsk { + "Barnetillegget for " +
                                    ifElse(harFlereFellesBarn, "barna", "barnet") +
                                    " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " +
                                    inntektstakFellesbarn.format() + ". Dei samla inntektene er over grensa for å få utbetalt barnetillegg." },

                            english { + "You will not receive child supplement for the " +
                                    ifElse(harFlereFellesBarn, "children who live", "child who lives") +
                                    " together with both parents because your total income on its own is higher than " +
                                    inntektstakFellesbarn.format() + ". You will not receive child supplement because your combined incomes exceed the income limit." }
                        )
                    }
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
                        bokmal { + "Du kan lese mer om beregningen av barnetillegg i vedlegget " },
                        nynorsk { + "Du kan lese meir om berekninga av barnetillegg i vedlegget " },
                        english { + "Read more about how child supplements are calculated in the attachment called " }
                    )
                    text(
                        bokmal { + quoted("Opplysninger om beregningen") +"." },
                        nynorsk { + quoted("Opplysningar om berekninga") +"." },
                        english { + quoted("Information about calculations") +"." }
                    )
                }
            }
        }
    }
}



