package no.nav.pensjon.brev.maler.ufore.endring.bunnfradragNesteAr

import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.BunnfradragVedAldersovergangNesteArDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.barnetilleggFellesbarn.inntektAnnenForelder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.barnetilleggFellesbarn.inntektBruker
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.barnetilleggFellesbarn.netto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.barnetilleggSaerkullsbarn.inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.barnetilleggSaerkullsbarn.netto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.btfb
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.btfbEndret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.btsb
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.btsbEndret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.datoForNormertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.datoFyller67
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.bunnfradrag
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.fribelop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.nettoBarnetillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.nettoGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.nettoTotal
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.nettoUforetrygd
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.vedtakVirkFom
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.gradertUforetrygd
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.bunnfradragVedAldersovergangNesteArDto.inntektEtterUforhet
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.year
import no.nav.pensjon.brev.template.dsl.text

object BunnfradragVedAldersovergangNesteAr {

    data class Outline(val data: Expression<BunnfradragVedAldersovergangNesteArDto>) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            with(data) {
                paragraph {
                    text(
                        bokmal { +"Du fyller 67 år den " + datoFyller67.format() + ". Fordi du får alderspensjon fra " + datoForNormertPensjonsalder.format() + ", er bunnfradraget justert ut fra antall måneder du får uføretrygd i " + vedtakVirkFom.year.format() + ". Dette har kun betydning for deg dersom du skal ha inntekt ved siden av uføretrygden i " + vedtakVirkFom.year.format() + ". Du kan ha en inntekt på " + bunnfradrag.format() + " før " + datoForNormertPensjonsalder.format() + " før vi reduserer uføretrygden din. Dette beløpet er bunnfradraget ditt." },
                        nynorsk { +"Du fyller 67 år den " + datoFyller67.format() + ". Fordi du får alderspensjon frå " + datoForNormertPensjonsalder.format() + ", er botnfrådraget justert ut frå talet på månader du får uføretrygd i " + vedtakVirkFom.year.format() + ". Dette har berre betydning for deg dersom du skal ha inntekt ved sida av uføretrygda i " + vedtakVirkFom.year.format() + ". Du kan ha ei inntekt på " + bunnfradrag.format() + " før " + datoForNormertPensjonsalder.format() + " før vi reduserer uføretrygda di. Dette beløpet er botnfrådraget ditt." },
                    )
                }
                title1 {
                    text(
                        bokmal { +"Din beregning for " + vedtakVirkFom.year.format() },
                        nynorsk { +"Berekninga di for " + vedtakVirkFom.year.format() },
                    )
                }
                paragraph {
                    table(
                        header = {
                            column { text(bokmal { +"Din beregning" }, nynorsk { +"Berekninga di" }) }
                            column { text(bokmal { +"Beløp" }, nynorsk { +"Beløp" }) }
                        },
                    ) {
                        row {
                            cell { text(bokmal { +"Uføretrygd" }, nynorsk { +"Uføretrygd" }) }
                            cell { text(bokmal { +nettoUforetrygd.format() }, nynorsk { +nettoUforetrygd.format() }) }
                        }
                        ifNotNull(nettoBarnetillegg) { nettoBt ->
                            row {
                                cell { text(bokmal { +"Barnetillegg" }, nynorsk { +"Barnetillegg" }) }
                                cell { text(bokmal { +nettoBt.format() }, nynorsk { +nettoBt.format() }) }
                            }
                        }
                        ifNotNull(nettoGjenlevendetillegg) { nettoGjt ->
                            row {
                                cell { text(bokmal { +"Gjenlevendetillegg" }, nynorsk { +"Attlevandetillegg" }) }
                                cell { text(bokmal { +nettoGjt.format() }, nynorsk { +nettoGjt.format() }) }
                            }
                        }
                        row {
                            cell { text(bokmal { +"Bunnfradrag" }, nynorsk { +"Botnfrådrag" }) }
                            cell { text(bokmal { +bunnfradrag.format() }, nynorsk { +bunnfradrag.format() }) }
                        }
                        row {
                            cell { text(bokmal { +"Fribeløp" }, nynorsk { +"Fribeløp" }) }
                            cell { text(bokmal { +fribelop.format() }, nynorsk { +fribelop.format() }) }
                        }
                        row {
                            cell { text(bokmal { +"Månedlig uføretrygd og barnetillegg" }, nynorsk { +"Månadleg uføretrygd og barnetillegg" }) }
                            cell { text(bokmal { +nettoTotal.format() }, nynorsk { +nettoTotal.format() }) }
                        }
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Uføretrygden blir fortsatt utbetalt senest den 20. hver måned." },
                        nynorsk { +"Uføretrygda blir framleis utbetalt seinast den 20. kvar månad." },
                    )
                }
                title1 {
                    text(
                        bokmal { +"Slik beregner vi bunnfradrag" },
                        nynorsk { +"Slik bereknar vi botnfrådrag" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Fordi du får alderspensjon fra " + datoForNormertPensjonsalder.format() + ", er bunnfradraget justert ut fra antall måneder du får uføretrygd i " + vedtakVirkFom.year.format() + "." },
                        nynorsk { +"Fordi du får alderspensjon frå " + datoForNormertPensjonsalder.format() + ", er botnfrådraget justert ut frå talet på månader du får uføretrygd i " + vedtakVirkFom.year.format() + "." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Ditt bunnfradrag i " + vedtakVirkFom.year.format() + " er " + bunnfradrag.format() + "." },
                        nynorsk { +"Botnfrådraget ditt i " + vedtakVirkFom.year.format() + " er " + bunnfradrag.format() + "." },
                    )
                }
                title1 {
                    text(
                        bokmal { +"Hva er fribeløp og bunnfradrag?" },
                        nynorsk { +"Kva er fribeløp og botnfrådrag?" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Bunnfradrag er hvor mye inntekt du kan ha før vi begynner å redusere uføretrygden din. Bunnfradraget består av fribeløpet pluss inntekt etter uførhet. Dette ble tidligere omtalt som inntektsgrense." },
                        nynorsk { +"Botnfrådrag er kor mykje inntekt du kan ha før vi byrjar å redusere uføretrygda di. Botnfrådraget består av fribeløpet pluss inntekt etter uførleik. Dette blei tidlegare omtalt som inntektsgrense." },
                    )
                }
                showIf(gradertUforetrygd) {
                    paragraph {
                        text(
                            bokmal { +"Siden du har gradert uføretrygd, har du fortsatt inntekt etter uførhet." },
                            nynorsk { +"Sidan du har gradert uføretrygd, har du framleis inntekt etter uførleik." },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +ifElse(inntektEtterUforhet.equalTo(0), "Siden du har 100 prosent uføretrygd, har du 0 kroner i inntekt etter uførhet. ".expr(), "Du har 100 prosent uføretrygd, og din inntekt etter uførhet er ".expr() + inntektEtterUforhet.format() + ".") },
                            nynorsk { +ifElse(inntektEtterUforhet.equalTo(0), "Sidan du har 100 prosent uføretrygd, har du 0 kroner i inntekt etter uførleik. ".expr(), "Du har 100 prosent uføretrygd, og inntekta di etter uførleik er ".expr() + inntektEtterUforhet.format() + ".") },
                        )
                    }
                }
                includePhrase(Ufoeretrygd.MeldeFraOmEndringer(vedleggDineRettigheterOgPlikterUfoere))

                showIf(nettoBarnetillegg.notNull()) {
                    title1 {
                        text(bokmal { +"Barnetillegg" }, nynorsk { +"Barnetillegg" })
                    }
                    paragraph {
                        text(
                            bokmal { +"Fordi du har barnetillegg som opphører i løpet av neste år, er inntektene og fribeløpet for neste år justert slik at de kun gjelder for perioden du mottar barnetillegg." },
                            nynorsk { +"Fordi du har barnetillegg som fell bort i løpet av neste år, er inntektene og fribeløpet for neste år justerte slik at dei berre gjeld for perioden du får barnetillegg." },
                        )
                    }

                    showIf(btfbEndret and btsbEndret) {
                        ifNotNull(btsb, btfb) { barnetilleggSB, barnetilleggFB ->
                            title2 {
                                text(
                                    bokmal { +"Dine barnetillegg" },
                                    nynorsk { +"Barnetillegga dine" },
                                )
                            }
                            paragraph {
                                text(
                                    bokmal { +"Du får barnetillegg for særkullsbarn og fellesbarn. Du får barnetillegg for fellesbarn når du bor sammen med barnets andre forelder. Du får barnetillegg for særkullsbarn når du ikke bor sammen med barnets andre forelder." },
                                    nynorsk { +"Du får barnetillegg for særkullsbarn og fellesbarn. Du får barnetillegg for fellesbarn når du bur saman med den andre forelderen til barnet. Du får barnetillegg for særkullsbarn når du ikkje bur saman med den andre forelderen til barnet." },
                                )
                            }
                            paragraph {
                                text(
                                    bokmal {
                                        +"Barnetillegg for særkullsbarn er beregnet ut fra din inntekt på " + barnetilleggSB.inntektBruktIAvkortning.format() + ". Barnetillegg for " +
                                                "fellesbarn er i tillegg beregnet ut fra den andre forelderens inntekt på " + barnetilleggFB.inntektAnnenForelder.format() +
                                                ". Du får derfor en utbetaling av barnetillegg på " + barnetilleggSB.netto.plus(barnetilleggFB.netto).format() + " per måned fra neste år."
                                    },
                                    nynorsk {
                                        +"Barnetillegg for særkullsbarn er berekna ut frå inntekta di på " + barnetilleggSB.inntektBruktIAvkortning.format() + ". Barnetillegg for " +
                                                "fellesbarn er i tillegg berekna ut frå inntekta til den andre forelderen på " + barnetilleggFB.inntektAnnenForelder.format() +
                                                ". Du får difor ei utbetaling av barnetillegg på " + barnetilleggSB.netto.plus(barnetilleggFB.netto).format() + " per månad frå neste år."
                                    },
                                )
                            }
                        }
                    }.orShowIf(btfbEndret) {
                        ifNotNull(btfb) { barnetilleggFB ->
                            title2 {
                                text(
                                    bokmal { +"Barnetillegg for fellesbarn" },
                                    nynorsk { +"Barnetillegg for fellesbarn" },
                                )
                            }
                            paragraph {
                                text(
                                    bokmal {
                                        +"Du får barnetillegg for fellesbarn fordi du bor sammen med barnets andre forelder. Vi har endret barnetillegget ut fra din personinntekt på " +
                                                barnetilleggFB.inntektBruker.format() + " og personinntekten til barnets andre forelder på " + barnetilleggFB.inntektAnnenForelder.format() +
                                                ". Du får derfor en utbetaling av barnetillegg på " + barnetilleggFB.netto.format() + " per måned fra neste år."
                                    },
                                    nynorsk {
                                        +"Du får barnetillegg for fellesbarn fordi du bur saman med den andre forelderen til barnet. Vi har endra barnetillegget ut frå personinntekta di på " +
                                                barnetilleggFB.inntektBruker.format() + " og personinntekta til den andre forelderen til barnet på " + barnetilleggFB.inntektAnnenForelder.format() +
                                                ". Du får difor ei utbetaling av barnetillegg på " + barnetilleggFB.netto.format() + " per månad frå neste år."
                                    },
                                )
                            }
                        }
                    }.orShowIf(btsbEndret) {
                        ifNotNull(btsb) { barnetilleggSB ->
                            title2 {
                                text(
                                    bokmal { +"Barnetillegg for særkullsbarn" },
                                    nynorsk { +"Barnetillegg for særkullsbarn" },
                                )
                            }
                            paragraph {
                                text(
                                    bokmal {
                                        +"Du får barnetillegg for særkullsbarn fordi du ikke bor sammen med barnets andre forelder. " +
                                                "Vi har endret barnetillegget ut fra din personinntekt på " + barnetilleggSB.inntektBruktIAvkortning.format() +
                                                ". Du får derfor en utbetaling av barnetillegg på " + barnetilleggSB.netto.format() + " per måned fra neste år."
                                    },
                                    nynorsk {
                                        +"Du får barnetillegg for særkullsbarn fordi du ikkje bur saman med den andre forelderen til barnet. " +
                                                "Vi har endra barnetillegget ut frå personinntekta di på " + barnetilleggSB.inntektBruktIAvkortning.format() +
                                                ". Du får difor ei utbetaling av barnetillegg på " + barnetilleggSB.netto.format() + " per månad frå neste år."
                                    },
                                )
                            }
                        }
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Du finner fullstendige beregninger i vedlegget «Opplysninger om beregningen»." },
                        nynorsk { +"Du finn fullstendige berekningar i vedlegget «Opplysningar om utrekninga»." },
                    )
                }
            }
        }
    }
}