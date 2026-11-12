package no.nav.pensjon.brev.maler.ufore.fraser

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.ParagraphPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner


object Barnetillegg {

    // TBU2339
    data class InntektHarBetydningForFellesbarnTillegg(
        val faarUtbetaltBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val borMedSivilstand: Expression<BorMedSivilstand>,
        val barnetilleggSaerkullsbarnGjelderFlereBarn: Expression<Boolean>

    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(harBarnetilleggFellesbarn) {
                paragraph {
                    text(
                        bokmal { + "Inntekten til deg og " + borMedSivilstand.bestemtForm() + " din har betydning for hva du får i barnetillegg. Er inntektene over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert." },
                        nynorsk { + "Inntekta til deg og " + borMedSivilstand.bestemtForm() + " din har noko å seie for kva du får i barnetillegg. Er den samla inntekta over grensa for å få utbetalt fullt barnetillegg, blir tillegget ditt redusert." },
                    )
                    showIf(faarUtbetaltBarnetilleggFellesbarn) {
                        text(
                            bokmal { + " Denne grensen kaller vi for fribeløp." },
                            nynorsk { + " Denne grensa kallar vi for fribeløp." },
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
                        )
                    }
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

    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            val grunnbeloep = grunnbeloep.format()

            paragraph {
                text(
                    bokmal { + "Inntekten din er " + brukersInntektBruktiAvkortningFellesbarn.format() + " og inntekten til " + borMedSivilstand.bestemtForm() + " din er " + inntektAnnenForelderFellesbarn.format() + "." },
                    nynorsk { + "Inntekta di er " + brukersInntektBruktiAvkortningFellesbarn.format() + ", og inntekta til " + borMedSivilstand.bestemtForm() + " din er " + inntektAnnenForelderFellesbarn.format() + "." },
                )

                showIf(harBeloepFratrukketAnnenForelder) {
                    text(
                        bokmal { + " Folketrygdens grunnbeløp på inntil " + grunnbeloep + " er holdt utenfor inntekten til "
                                + borMedSivilstand.bestemtForm() + " din." },
                        nynorsk { + " Grunnbeløpet i folketrygda på inntil " + grunnbeloep + " er halde utanfor inntekta til "
                                + borMedSivilstand.bestemtForm() + " din." },
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
                    )
                }

                showIf(harJusteringsbeloepFellesbarn and not(harBarnetilleggSaerkullsbarn)) {
                    includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
                    text(
                        bokmal { + " Dette ble tatt hensyn til da vi endret barnetillegget." },
                        nynorsk { + " Dette har vi teke omsyn til når vi endra barnetillegget." },
                    )
                    showIf(not(faarUtbetaltBarnetilleggFellesBarn)) {
                        text(
                            bokmal { + " Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                            nynorsk { + " Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                        )
                    }
                }
            }
        }
    }


    object DuHarFaattUtbetaltBarnetilleggTidligereIAar : ParagraphPhrase<LangBokmalNynorsk>() {
        override fun ParagraphOnlyScope<LangBokmalNynorsk, Unit>.template() {
            text(
                bokmal { + "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover." },
                nynorsk { + "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover." },
            )
        }
    }

    // TBU1285
    data class InntektTilAvkortningSaerkullsbarn(
        val beloepNettoSaerkullsbarn: Expression<Kroner>,
        val fribeloepSaerkullsbarn: Expression<Kroner>,
        val inntektBruktIAvkortningSaerkullsbarn: Expression<Kroner>,
        val harJusteringsbeloepSaerkullsbarn: Expression<Boolean>

    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

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
                    )
                    includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
                    text(
                        bokmal { + " Dette ble tatt hensyn til da vi endret barnetillegget." },
                        nynorsk { + " Dette har vi teke omsyn til når vi endra barnetillegget." },
                    )
                    showIf(not(faarUtbetaltBarnetilleggSaerkullsbarn)) {
                        text(
                            bokmal { + " Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                            nynorsk { + " Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
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

        ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
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
                    )

                    showIf(harFradragForEnBarnetilleggYtelse) {
                        text(
                            bokmal { + " Dette barnetillegget er derfor" +
                                    ifElse(barnetilleggSaerkullsbarnIkkeRedusert, " ikke ", " ") +
                                    "redusert ut fra inntekt." },

                            nynorsk { + " Dette barnetillegget er derfor" +
                                    ifElse(barnetilleggSaerkullsbarnIkkeRedusert, " ikkje ", " ") +
                                    "redusert ut frå inntekt." },
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
                    )
                    showIf(not(faarUtbetaltBarnetilleggFellesbarn)) {
                        text(
                            bokmal { + " Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året" },
                            nynorsk { + " Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
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
                    )
                    showIf(not(faarUtbetaltBarnetilleggSaerkullsbarn)) {
                        text(
                            bokmal { + " Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                            nynorsk { + " Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
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
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
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
                        )
                    }
                }
            }
        }
    }
}



