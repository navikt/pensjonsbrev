package no.nav.pensjon.brev.maler.ufore.fraser

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Grunnlag
import no.nav.pensjon.brev.maler.legacy.hoyereLavere
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut
import no.nav.pensjon.brev.maler.legacy.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall
import no.nav.pensjon.brev.maler.legacy.sumUtBtErHoyereEnnGradertOIFU
import no.nav.pensjon.brev.maler.legacy.ut_virkningstidpunktar
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumutbt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid
import no.nav.pensjon.brev.maler.ufore.extensions.bokmalUbestemtEntall
import no.nav.pensjon.brev.maler.ufore.extensions.nynorskBestemtEntall
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.ParagraphPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate


object Barnetillegg {

    data class InntektensBetydningForBarnetillegg(
        val harSaerkullstillegg: Expression<Boolean>,
        val harFellesbarnstillegg: Expression<Boolean>,
        val nettoSaerkullstillegg: Expression<Kroner>,
        val nettoFellesbarnstillegg: Expression<Kroner>,
        val visAtEpsInntektIkkePaavirkerTillegget: Expression<Boolean>,
        val fellesbarnOmtale: Expression<String>,
        val epsBokmal: Expression<String>,
        val epsNynorsk: Expression<String>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            val nettoBarnetillegg = ifElse(harFellesbarnstillegg, nettoFellesbarnstillegg, nettoSaerkullstillegg)

            showIf(harSaerkullstillegg or harFellesbarnstillegg) {
                paragraph {
                    text(
                        bokmal {
                            +"Inntekten " + ifElse(harFellesbarnstillegg, "til deg og din ".expr() + epsBokmal, "din".expr()) +
                                " har betydning for hva du får i barnetillegg. Er " +
                                ifElse(harFellesbarnstillegg, "inntektene", "inntekten din") +
                                " over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. "
                        },
                        nynorsk {
                            +"Inntekta " + ifElse(harFellesbarnstillegg, "til deg og ".expr() + epsNynorsk + " din", "di".expr()) +
                                " har noko å seie for kva du får i barnetillegg. Er " +
                                ifElse(harFellesbarnstillegg, "den samla inntekta", "inntekta di") +
                                " over grensa for å få utbetalt fullt barnetillegg, blir tillegget" +
                                ifElse(harFellesbarnstillegg, " ditt", "") + " redusert. "
                        },
                    )
                    showIf(nettoBarnetillegg.notEqualTo(0)) {
                        text(
                            bokmal { +"Denne grensen kaller vi for fribeløp. " },
                            nynorsk { +"Denne grensa kallar vi for fribeløp. " },
                        )
                    }
                    showIf(not(harFellesbarnstillegg) and visAtEpsInntektIkkePaavirkerTillegget) {
                        text(
                            bokmal { +"Inntekten til din " + epsBokmal + " har ikke betydning for størrelsen på barnetillegget." },
                            nynorsk { +"Inntekta til " + epsNynorsk + " din har ikkje noko å seie for storleiken på barnetillegget. " },
                        )
                    }
                    showIf(harFellesbarnstillegg and harSaerkullstillegg) {
                        text(
                            bokmal { +"Inntekten til din " + epsBokmal + " har kun betydning for størrelsen på barnetillegget til " + fellesbarnOmtale + " som bor sammen med begge sine foreldre." },
                            nynorsk { +"Inntekta til " + epsNynorsk + " din har berre betydning for storleiken på barnetillegget til " + fellesbarnOmtale + " som bur saman med begge foreldra sine. " },
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

    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(harBarnetilleggFellesbarn) {
                paragraph {
                    text(
                        bokmal { +"Inntekten til deg og " + borMedSivilstand.bestemtForm() + " din har betydning for hva du får i barnetillegg. Er inntektene over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert." },
                        nynorsk { +"Inntekta til deg og " + borMedSivilstand.bestemtForm() + " din har noko å seie for kva du får i barnetillegg. Er den samla inntekta over grensa for å få utbetalt fullt barnetillegg, blir tillegget ditt redusert." },
                    )
                    showIf(faarUtbetaltBarnetilleggFellesbarn) {
                        text(
                            bokmal { +" Denne grensen kaller vi for fribeløp." },
                            nynorsk { +" Denne grensa kallar vi for fribeløp." },
                        )
                    }
                    showIf(harBarnetilleggSaerkullsbarn) {
                        text(
                            bokmal {
                                +" Inntekten til " + borMedSivilstand.bestemtForm() + " din har kun betydning for størrelsen på barnetillegget til "
                                +ifElse(barnetilleggSaerkullsbarnGjelderFlereBarn, "barna", "barnet")
                                +" som bor sammen med begge sine foreldre."
                            },

                            nynorsk {
                                +" Inntekta til " + borMedSivilstand.bestemtForm() + " din har berre betydning for storleiken på barnetillegget til "
                                +ifElse(barnetilleggSaerkullsbarnGjelderFlereBarn, "barna", "barnet")
                                +" som bur saman med begge foreldra sine."
                            },
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
                    bokmal { +"Inntekten din er " + brukersInntektBruktiAvkortningFellesbarn.format() + " og inntekten til " + borMedSivilstand.bestemtForm() + " din er " + inntektAnnenForelderFellesbarn.format() + "." },
                    nynorsk { +"Inntekta di er " + brukersInntektBruktiAvkortningFellesbarn.format() + ", og inntekta til " + borMedSivilstand.bestemtForm() + " din er " + inntektAnnenForelderFellesbarn.format() + "." },
                )

                showIf(harBeloepFratrukketAnnenForelder) {
                    text(
                        bokmal {
                            +" Folketrygdens grunnbeløp på inntil " + grunnbeloep + " er holdt utenfor inntekten til "
                            +borMedSivilstand.bestemtForm() + " din."
                        },
                        nynorsk {
                            +" Grunnbeløpet i folketrygda på inntil " + grunnbeloep + " er halde utanfor inntekta til "
                            +borMedSivilstand.bestemtForm() + " din."
                        },
                    )
                }

                showIf(not(harBarnetilleggSaerkullsbarn) and not(harJusteringsbeloepFellesbarn) and faarUtbetaltBarnetilleggFellesBarn) {
                    text(
                        bokmal {
                            +" Til sammen er inntektene " +
                                    ifElse(harFradragFellesbarn, "høyere", "lavere") +
                                    " enn fribeløpet ditt på " + fribeloepFellesbarn.format() + ". Barnetillegget ditt er derfor" +
                                    ifElse(harFradragFellesbarn, "", " ikke") + " redusert ut fra inntekt."
                        },

                        nynorsk {
                            +" Til saman er inntektene " +
                                    ifElse(harFradragFellesbarn, "høgare", "lågare") +
                                    " enn fribeløpet ditt på " + fribeloepFellesbarn.format() + ". Barnetillegget ditt er derfor" +
                                    ifElse(harFradragFellesbarn, "", " ikkje") +
                                    " redusert ut frå inntekt."
                        },
                    )
                }

                showIf(harJusteringsbeloepFellesbarn and not(harBarnetilleggSaerkullsbarn)) {
                    includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
                    text(
                        bokmal { +" Dette ble tatt hensyn til da vi endret barnetillegget." },
                        nynorsk { +" Dette har vi teke omsyn til når vi endra barnetillegget." },
                    )
                    showIf(not(faarUtbetaltBarnetilleggFellesBarn)) {
                        text(
                            bokmal { +" Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                            nynorsk { +" Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                        )
                    }
                }
            }
        }
    }

    object BarnetilleggPavirkesAvUtlandsopphold : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Barnetillegget påvirkes av utlandsopphold" },
                    nynorsk { +"Barnetillegget vert påverka av utanlandsopphald" },
                )
            }
            paragraph {
                text(
                    bokmal { +"For å ha rett til barnetillegg" },
                    nynorsk { +"For å ha rett til barnetillegg" },
                )
                list {
                    item {
                        text(
                            bokmal { +"må du enten bo i Norge, innenfor EØS-området eller i et annet land Norge har trygdeavtale med" },
                            nynorsk { +"må du enten bu i Noreg, innanfor EØS-området eller i eit anna land Noreg har trygdeavtale med" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"må også barnet være bosatt og oppholde seg i Norge, innenfor EØS-området eller et annet land Norge har trygdeavtale med" },
                            nynorsk { +"må også barn vere busett og opphalde seg i Noreg, innanfor EØS-området eller eit anna land Noreg har trygdeavtale med" },
                        )
                    }
                }
                text(
                    bokmal { +"Dette går frem av folketrygdloven § 12-15." },
                    nynorsk { +"Dette går fram av folketrygdlova § 12-15." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du planlegger å flytte eller oppholde deg i et annet land, må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til barnetillegg. Dette gjelder også hvis barnet du forsørger skal oppholde seg i et annet land. " },
                    nynorsk { +"Om du planlegg å flytte eller opphalde deg i eit anna land må du kontakte oss slik at vi kan ta stilling til om du fortsatt har rett til barnetillegg. Dette gjeld også om barnet du forsørgjer skal opphalde seg i eit anna land. " },
                )
            }
        }
    }


    object DuHarFaattUtbetaltBarnetilleggTidligereIAar : ParagraphPhrase<LangBokmalNynorsk>() {
        override fun ParagraphOnlyScope<LangBokmalNynorsk, Unit>.template() {
            text(
                bokmal { +"Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover." },
                nynorsk { +"Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover." },
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
                        bokmal { +"Inntekten din er " + inntektBruktiAvkortning + "." },
                        nynorsk { +"Inntekta di er " + inntektBruktiAvkortning + "." },
                    )
                    includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
                    text(
                        bokmal { +" Dette ble tatt hensyn til da vi endret barnetillegget." },
                        nynorsk { +" Dette har vi teke omsyn til når vi endra barnetillegget." },
                    )
                    showIf(not(faarUtbetaltBarnetilleggSaerkullsbarn)) {
                        text(
                            bokmal { +" Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                            nynorsk { +" Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                        )
                    }
                }
            }.orShowIf(faarUtbetaltBarnetilleggSaerkullsbarn) {
                paragraph {
                    text(
                        bokmal {
                            +"Inntekten din på " + inntektBruktiAvkortning + " er " +
                                    ifElse(hoeyereLavere, "lavere", "høyere") +
                                    " enn fribeløpet ditt på " + fribeloep + ". Barnetillegget ditt er derfor " +
                                    ifElse(inntektLavereEnnFribeloep, "ikke ", "") + "redusert ut fra inntekt."
                        },

                        nynorsk {
                            +"Inntekta di på " + inntektBruktiAvkortning + " er " +
                                    ifElse(hoeyereLavere, "lågare", "høgare") +
                                    " enn fribeløpet ditt på " + fribeloep + ". Barnetillegget ditt er derfor " +
                                    ifElse(inntektLavereEnnFribeloep, "ikkje ", "") + "redusert ut frå inntekt."
                        },
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
                        bokmal {
                            +"Inntekten din er " +
                                    ifElse(inntektOverFribeloepSaerkullsbarn, "høyere", "lavere") +
                                    " enn " + fribeloepSaerkullsbarn.format() + ", som er fribeløpet for barnetillegget til " +
                                    ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet") +
                                    " som ikke bor sammen med begge foreldrene."
                        },

                        nynorsk {
                            +"Inntekta di er " +
                                    ifElse(inntektOverFribeloepSaerkullsbarn, "høgare", "lågare") +
                                    " enn " + fribeloepSaerkullsbarn.format() + ", som er fribeløpet for barnetillegget til " +
                                    ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet") +
                                    " som ikkje bur saman med begge foreldra."
                        },
                    )

                    showIf(harFradragForEnBarnetilleggYtelse) {
                        text(
                            bokmal {
                                +" Dette barnetillegget er derfor" +
                                        ifElse(barnetilleggSaerkullsbarnIkkeRedusert, " ikke ", " ") +
                                        "redusert ut fra inntekt."
                            },

                            nynorsk {
                                +" Dette barnetillegget er derfor" +
                                        ifElse(barnetilleggSaerkullsbarnIkkeRedusert, " ikkje ", " ") +
                                        "redusert ut frå inntekt."
                            },
                        )
                    }
                    showIf(faarUtbetaltBarnetilleggFellesbarn and not(harJusteringsbeloepFellesbarn)) {

                        val ogsaa = not(harFradragForEnBarnetilleggYtelse)
                        val inntektErHoyereEnnFribeloepFellesBarn = samletInntektBruktiAvkortningFellesbarn.greaterThan(fribeloepFellesbarn)
                        text(
                            bokmal {
                                +" Til sammen er " +
                                        ifElse(ogsaa, "også inntektene", "inntektene") +
                                        " til deg og " + borMed.bestemtForm() + " din " +
                                        ifElse(inntektErHoyereEnnFribeloepFellesBarn, "høyere", "lavere") +
                                        " enn " + fribeloepFellesbarn.format() + ", som er fribeløpet for barnetillegget til " +
                                        ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                                        " som bor med begge sine foreldre."
                            },

                            nynorsk {
                                +" Til saman er " +
                                        ifElse(ogsaa, "også inntektene", "inntektene") +
                                        " til deg og " + borMed.bestemtForm() + " din " +
                                        ifElse(inntektErHoyereEnnFribeloepFellesBarn, "høgare", "lagare") +
                                        " enn " + fribeloepFellesbarn.format() + ", som er fribeløpet for barnetillegget til " +
                                        ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                                        " som bur saman med begge foreldra sine."
                            },
                        )

                        showIf(harFradragForEnBarnetilleggYtelse) {
                            text(
                                bokmal {
                                    +
                                    " Dette barnetillegget er derfor" +
                                            ifElse(barnetilleggFellesbarnIkkeRedusert, " ikke ", " ") +
                                            "redusert ut fra inntekt."
                                },

                                nynorsk {
                                    +" Dette barnetillegget er derfor" +
                                            ifElse(barnetilleggFellesbarnIkkeRedusert, " ikkje ", " ") +
                                            "redusert ut frå inntekt."
                                },
                            )
                        }.orShow {
                            val ikkeRedusert =
                                barnetilleggFellesbarnIkkeRedusert and barnetilleggSaerkullsbarnIkkeRedusert
                            text(
                                bokmal {
                                    +" Barnetilleggene er derfor" +
                                            ifElse(ikkeRedusert, " ikke ", " ") +
                                            "redusert ut fra inntekt."
                                },

                                nynorsk {
                                    +" Desse barnetillegga er derfor" +
                                            ifElse(ikkeRedusert, " ikkje ", " ") +
                                            "redusert ut frå inntekt."
                                },
                            )
                        }
                    }
                }
            }

            showIf(harJusteringsbeloepFellesbarn) {
                paragraph {
                    includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
                    text(
                        bokmal {
                            +" Dette ble tatt hensyn til da vi endret barnetillegget for " +
                                    ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                                    " som bor med begge sine foreldre."
                        },

                        nynorsk {
                            +" Dette har vi teke omsyn til når vi endra barnetillegget for " +
                                    ifElse(harTilleggForFlereFellesbarn, "barna", "barnet") +
                                    " som bur saman med begge foreldra sine."
                        },
                    )
                    showIf(not(faarUtbetaltBarnetilleggFellesbarn)) {
                        text(
                            bokmal { +" Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året" },
                            nynorsk { +" Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
                        )
                    }
                }
            }

            showIf(harJusteringsbeloepSaerkullsbarn) {
                paragraph {
                    includePhrase(DuHarFaattUtbetaltBarnetilleggTidligereIAar)
                    text(
                        bokmal {
                            +" Dette ble tatt hensyn til da vi endret barnetillegget for " +
                                    ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet") +
                                    " som ikke bor sammen med begge foreldrene."
                        },

                        nynorsk {
                            +" Dette har vi teke omsyn til når vi endra barnetillegget for " +
                                    ifElse(harTilleggForFlereSaerkullsbarn, "barna", "barnet") +
                                    " som ikkje bur saman med begge foreldra."
                        },
                    )
                    showIf(not(faarUtbetaltBarnetilleggSaerkullsbarn)) {
                        text(
                            bokmal { +" Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                            nynorsk { +" Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året." },
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
                        text(
                            // TBU2490
                            bokmal {
                                +"Barnetillegget for " +
                                        ifElse(harFlereFellesBarn, "barna", "barnet") +
                                        " som bor med begge sine foreldre, blir ikke utbetalt fordi de samlede inntektene er høyere enn " +
                                        inntektstakFellesbarn.format() + ". Barnetillegget for " +
                                        ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                        " som ikke bor sammen med begge foreldrene, blir heller ikke utbetalt fordi inntekten din alene er høyere enn " +
                                        inntektstakSaerkullsbarn.format() + ". Inntektene er over grensen for å få utbetalt barnetillegg."
                            },

                            nynorsk {
                                +"Barnetillegget for " +
                                        ifElse(harFlereFellesBarn, "barna", "barnet") +
                                        " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei samla inntektene er høgare enn " +
                                        inntektstakFellesbarn.format() + ". Barnetillegget for " +
                                        ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                        " som ikkje bur saman med begge foreldra, blir heller ikkje utbetalt fordi inntekta di åleine er høgare enn " +
                                        inntektstakSaerkullsbarn.format() + ". Inntektene er over grensa for å få utbetalt barnetillegg."
                            },
                        )
                    }.orShowIf(not(saerkullUtbetalt) and saerkullInnvilget and not(fellesInnvilget)) {
                        text(
                            // TBU1286.1
                            bokmal {
                                +"Barnetillegget for " +
                                        ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                        " blir ikke utbetalt fordi du har en samlet inntekt som er høyere enn " +
                                        inntektstakFellesbarn.format() + ". Inntekten din er over grensen for å få utbetalt barnetillegg."
                            },

                            nynorsk {
                                +"Barnetillegget for " +
                                        ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                        " blir ikkje utbetalt fordi du har ei samla inntekt som er høgare enn " +
                                        inntektstakFellesbarn.format() + ". Inntekta di er over grensa for å få utbetalt barnetillegg."
                            },
                        )
                    }.orShowIf(not(saerkullUtbetalt) and saerkullInnvilget and fellesInnvilget) {
                        text(
                            // TBU1286.1
                            bokmal {
                                +"Barnetillegget for " +
                                        ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                        " som ikke bor sammen med begge foreldrene, blir ikke utbetalt fordi du alene har en samlet inntekt som er høyere enn " +
                                        inntektstakFellesbarn.format() + ". Inntekten din er over grensen for å få utbetalt barnetillegg."
                            },

                            nynorsk {
                                +"Barnetillegget for " +
                                        ifElse(harFlereSaerkullsbarn, "barna", "barnet") +
                                        " som ikkje bur saman med begge foreldra sine, blir ikkje utbetalt fordi du åleine har ei samla inntekt som er høgare enn " +
                                        inntektstakFellesbarn.format() + ". Inntekta di er over grensa for å få utbetalt barnetillegg."
                            },
                        )
                    }.orShowIf(not(fellesUtbetalt) and not(saerkullInnvilget) and fellesInnvilget) {
                        text(
// TBU1286.2
                            bokmal {
                                +"Barnetillegget for " +
                                        ifElse(harFlereFellesBarn, "barna", "barnet") +
                                        " blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " +
                                        inntektstakFellesbarn.format() + ". De samlede inntektene er over grensen for å få utbetalt barnetillegg."
                            },

                            nynorsk {
                                +"Barnetillegget for " +
                                        ifElse(harFlereFellesBarn, "barna", "barnet") +
                                        " blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " +
                                        inntektstakFellesbarn.format() + ". Dei samla inntektene er over grensa for å få utbetalt barnetillegg."
                            },
                        )
                    }.orShowIf(not(fellesUtbetalt) and saerkullInnvilget and fellesInnvilget) {
                        text(
// TBU1286.2
                            bokmal {
                                +"Barnetillegget for " +
                                        ifElse(harFlereFellesBarn, "barna", "barnet") +
                                        " som bor med begge sine foreldre, blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " +
                                        inntektstakFellesbarn.format() + ". De samlede inntektene er over grensen for å få utbetalt barnetillegg."
                            },

                            nynorsk {
                                +"Barnetillegget for " +
                                        ifElse(harFlereFellesBarn, "barna", "barnet") +
                                        " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " +
                                        inntektstakFellesbarn.format() + ". Dei samla inntektene er over grensa for å få utbetalt barnetillegg."
                            },
                        )
                    }
                }
            }
        }
    }

    data class BarnetilleggOvergangsregler2016(
        val pe: Expression<PEgruppe10>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            //[TBU3802, TBU3802_NN]

            paragraph {
                text(
                    bokmal { +"I perioden fra 1. januar 2016 til 31. desember 2020 er det egne regler for hvordan barnetillegget ditt blir justert. " },
                    nynorsk { +"I perioden frå 1. januar 2016 til 31. desember 2020 er det eigne reglar for korleis barnetillegget ditt blir justert. " },
                )
                text(
                    bokmal { +"Fram til 31. desember " + pe.ut_virkningstidpunktar().format() + " kan ikke uføretrygden og barnetillegget ditt til sammen være høyere enn " + pe.virkningstidspunktTilProsent().format() + " prosent av inntekten din før du ble ufør. Uføretrygden og barnetillegget ditt utgjør " + pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu().format() + " prosent av det som var inntekten din før du ble ufør." },
                    nynorsk { +"Fram til 31. desember " + pe.ut_virkningstidpunktar().format() + " kan ikkje uføretrygda di og barnetillegget ditt til saman vere høgare enn " + pe.virkningstidspunktTilProsent().format() + " prosent av inntekta di før du blei ufør. Uføretrygda og barnetillegget ditt utgjer " + pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu().format() + " prosent av det som var inntekta di før du blei ufør." },
                )
                text(
                    bokmal { +pe.virkningstidspunktTilProsent().format() + " prosent av den inntekten du hadde før du ble ufør tilsvarer i dag en inntekt på " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + ". Uføretrygden og barnetillegget ditt er til sammen " + hoyereLavere(pe.sumUtBtErHoyereEnnGradertOIFU()) + " enn dette." },
                    nynorsk { +pe.virkningstidspunktTilProsent().format() + " prosent av den inntekta du hadde før du blei ufør, tilsvarer i dag ei inntekt på " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + ". Uføretrygda di og barnetillegget ditt er til saman " + hoyereLavere(pe.sumUtBtErHoyereEnnGradertOIFU()) + " enn dette." },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0) THEN     INCLUDE END IF
                showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0))) {
                    text(
                        bokmal { +" Barnetillegget blir derfor ikke utbetalt." },
                        nynorsk { +" Barnetillegget blir derfor ikkje utbetalt." },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumUTBT > PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU  AND NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumutbt().greaterThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))) {
                    text(
                        bokmal { +" Årlig barnetillegg før reduksjon ut fra inntekt er derfor redusert til " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().format() + "." },
                        nynorsk { +" Brutto årleg barnetillegg før reduksjon ut frå inntekt er derfor redusert til " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().format() + "." },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumUTBT < PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU  AND NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumutbt().lessThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))) {
                    text(
                        bokmal { +" Barnetillegget er derfor fastsatt ut fra 40 prosent av folketrygdens grunnbeløp for hvert barn du får barnetillegg for." },
                        nynorsk { +" Barnetillegget er derfor fastsett ut frå 40 prosent av grunnbeløpet i folketrygda for kvart barn du får barnetillegg for." },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid < 40  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumUTBT < PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU  AND NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().lessThan(40) and pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumutbt().lessThan(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu()) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))) {
                    text(
                        bokmal { +" Fordi du har mindre enn 40 års trygdetid er barnetillegget ditt justert ned." },
                        nynorsk { +" Fordi du har mindre enn 40 års trygdetid, er barnetillegget ditt justert ned." },
                    )
                }

                //IF(NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))) {
                    text(
                        bokmal { +" Har du " },
                        nynorsk { +" Har du " },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))) {
                    text(
                        bokmal { +"og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " },
                        nynorsk { +"og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
                    )
                }

                //IF(NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoforreduksjonbt().greaterThan(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_sumbruttoetterreduksjonbt().equalTo(0)))) {
                    text(
                        bokmal { +"inntekt ved siden av uføretrygden vil dette også kunne ha betydning for størrelsen på barnetillegget ditt. " },
                        nynorsk { +"inntekt ved sida av uføretrygda, kan dette også ha noko å seie for storleiken på barnetillegget ditt. " },
                    )
                }
            }
        }
    }

    data class BarnetilleggOvergangsregler2016_Ny(
        val virkningstidpunktAr: Expression<Int>,
        val andelYtelseAvOifu: Expression<Double>,
        val gradertOppjustertIfu: Expression<Kroner>,
        val sumUtBtHoyereLavere: Expression<Boolean>,
        val sumBruttoForReduksjonBt: Expression<Kroner>,
        val sumBruttoEtterReduksjonBt: Expression<Kroner>,
        val sumUtBt: Expression<Kroner>,
        val anvendtTrygdetid: Expression<Int>,
        val btfbInnvilget: Expression<Boolean>,
        val epsBorMed: Expression<Grunnlag.Eps?>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal { +"I perioden fra 1. januar 2016 til 31. desember 2020 er det egne regler for hvordan barnetillegget ditt blir justert. " },
                    nynorsk { +"I perioden frå 1. januar 2016 til 31. desember 2020 er det eigne reglar for korleis barnetillegget ditt blir justert. " },
                )
                text(
                    bokmal { +"Fram til 31. desember " + virkningstidpunktAr.format() + " kan ikke uføretrygden og barnetillegget ditt til sammen være høyere enn " + virkningstidpunktAr.prosentBT().format() + " prosent av inntekten din før du ble ufør. Uføretrygden og barnetillegget ditt utgjør " + andelYtelseAvOifu.format() + " prosent av det som var inntekten din før du ble ufør." },
                    nynorsk { +"Fram til 31. desember " + virkningstidpunktAr.format() + " kan ikkje uføretrygda di og barnetillegget ditt til saman vere høgare enn " + virkningstidpunktAr.prosentBT().format() + " prosent av inntekta di før du blei ufør. Uføretrygda og barnetillegget ditt utgjer " + andelYtelseAvOifu.format() + " prosent av det som var inntekta di før du blei ufør." },
                )
                text(
                    bokmal { +virkningstidpunktAr.prosentBT().format() + " prosent av den inntekten du hadde før du ble ufør tilsvarer i dag en inntekt på " + gradertOppjustertIfu.format() + ". Uføretrygden og barnetillegget ditt er til sammen " + hoyereLavere(sumUtBtHoyereLavere) + " enn dette." },
                    nynorsk { +virkningstidpunktAr.prosentBT().format() + " prosent av den inntekta du hadde før du blei ufør, tilsvarer i dag ei inntekt på " + gradertOppjustertIfu.format() + ". Uføretrygda di og barnetillegget ditt er til saman " + hoyereLavere(sumUtBtHoyereLavere) + " enn dette." },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0) THEN     INCLUDE END IF
                showIf((sumBruttoForReduksjonBt.greaterThan(0) and sumBruttoEtterReduksjonBt.equalTo(0))) {
                    text(
                        bokmal { +" Barnetillegget blir derfor ikke utbetalt." },
                        nynorsk { +" Barnetillegget blir derfor ikkje utbetalt." },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumUTBT > PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU  AND NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
                showIf((sumUtBt.greaterThan(gradertOppjustertIfu) and not(sumBruttoForReduksjonBt.greaterThan(0) and sumBruttoEtterReduksjonBt.equalTo(0)))) {
                    text(
                        bokmal { +" Årlig barnetillegg før reduksjon ut fra inntekt er derfor redusert til " + sumBruttoEtterReduksjonBt.format() + "." },
                        nynorsk { +" Brutto årleg barnetillegg før reduksjon ut frå inntekt er derfor redusert til " + sumBruttoEtterReduksjonBt.format() + "." },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumUTBT < PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU  AND NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
                showIf((sumUtBt.lessThan(gradertOppjustertIfu) and not(sumBruttoForReduksjonBt.greaterThan(0) and sumBruttoEtterReduksjonBt.equalTo(0)))) {
                    text(
                        bokmal { +" Barnetillegget er derfor fastsatt ut fra 40 prosent av folketrygdens grunnbeløp for hvert barn du får barnetillegg for." },
                        nynorsk { +" Barnetillegget er derfor fastsett ut frå 40 prosent av grunnbeløpet i folketrygda for kvart barn du får barnetillegg for." },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid < 40  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumUTBT < PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU  AND NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
                showIf((anvendtTrygdetid.lessThan(40) and sumUtBt.lessThan(gradertOppjustertIfu) and not(sumBruttoForReduksjonBt.greaterThan(0) and sumBruttoEtterReduksjonBt.equalTo(0)))) {
                    text(
                        bokmal { +" Fordi du har mindre enn 40 års trygdetid er barnetillegget ditt justert ned." },
                        nynorsk { +" Fordi du har mindre enn 40 års trygdetid, er barnetillegget ditt justert ned." },
                    )
                }

                //IF(NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
                showIf((not(sumBruttoForReduksjonBt.greaterThan(0) and sumBruttoEtterReduksjonBt.equalTo(0)))) {
                    text(
                        bokmal { +" Har du " },
                        nynorsk { +" Har du " },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
                showIf((btfbInnvilget and not(sumBruttoForReduksjonBt.greaterThan(0) and sumBruttoEtterReduksjonBt.equalTo(0)))) {
                    text(
                        bokmal { +"og din " + epsBorMed.safe { bokmalUbestemtEntall() }.ifNull("") + " " },
                        nynorsk { +"og " + epsBorMed.safe { nynorskBestemtEntall() }.ifNull("") + " din " },
                    )
                }

                //IF(NOT(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT > 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT = 0)) THEN      INCLUDE ENDIF
                showIf((not(sumBruttoForReduksjonBt.greaterThan(0) and sumBruttoEtterReduksjonBt.equalTo(0)))) {
                    text(
                        bokmal { +"inntekt ved siden av uføretrygden vil dette også kunne ha betydning for størrelsen på barnetillegget ditt. " },
                        nynorsk { +"inntekt ved sida av uføretrygda, kan dette også ha noko å seie for storleiken på barnetillegget ditt. " },
                    )
                }
            }
        }
    }
}

private fun Expression<PEgruppe10>.virkningstidspunktTilProsent(): Expression<Int> {
    return vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().ifNull(LocalDate.of(1000, 1, 1)).year.prosentBT()
}

private fun Expression<Int>.prosentBT(): Expression<Int> {
    return ifElse(
        this.equalTo(2017), 107.expr(),
        ifElse(
            this.equalTo(2018), 104.expr(),
            ifElse(
                this.equalTo(2019), 101.expr(),
                ifElse(this.equalTo(2020), 98.expr(), 110.expr())
            )
        )
    )
}

