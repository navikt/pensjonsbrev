package no.nav.pensjon.brev.maler.ufore.lovendringer2026.oktbunnfradrag

import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktBunnfradragData
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.scenario2_1G_04G.dato04G
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.scenario4_04G_1G_04G.dato04G
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

object OktBunnfradragInst {

    data class Outline(val data: Expression<VedtakOmOktBunnfradragData>) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

            paragraph {
                text(
                    bokmal { +"Fra " + data.datoOkningBunnfradrag.format() + " øker fribeløpet ditt til 1 ganger folketrygdens grunnbeløp (G). Dette er per i dag 136 549 kroner. Det fører til at bunnfradraget ditt øker. " },
                    nynorsk { +"Frå " + data.datoOkningBunnfradrag.format() + " aukar fribeløpet ditt til 1 gonger folketrygdens grunnbeløp (G). Dette er per i dag 136 549 kroner. Det fører til at botnfrådraget ditt aukar. " }
                )
                showIf(data.scenario1_1G) {
                    text(
                        bokmal { +"Du kan ha inntekt på opptil ditt nye bunnfradrag før vi begynner å redusere uføretrygden din. " },
                        nynorsk { +"Du kan ha inntekt på opptil ditt nye botnfrådrag før vi byrjar å redusere uføretrygda di. " }
                    )
                }.orShow {
                    text(
                        bokmal { +"Fordi fribeløpet er endret i løpet av året, beregnes et gjennomsnitt. Se mer om beregningen av fribeløp og bunnfradraget ditt nedenfor. " },
                        nynorsk { +"Fordi fribeløpet er endra i løpet av året, vert eit gjennomsnitt berekna. Sjå meir om berekninga av fribeløp og botnfrådraget ditt nedanfor. " }
                    )
                }
            }
            paragraph {
                text(
                    bokmal { +"Et høyere fribeløp kan føre til at det blir mer lønnsomt for deg å kombinere jobb og uføretrygd. " },
                    nynorsk { +"Eit høgare fribeløp kan føre til at det vert meir lønsamt for deg å kombinere jobb og uføretrygd. " },
                )
            }

            title1 {
                text(
                    bokmal { +"Hva er fribeløp og bunnfradrag? " },
                    nynorsk { +"Kva er fribeløp og botnfrådrag? " },
                )
            }

            paragraph {
                text(
                    bokmal { +"Bunnfradrag er hvor mye inntekt du kan ha før vi begynner å redusere uføretrygden din. Bunnfradraget består av fribeløpet pluss inntekt etter uførhet. Dette ble tidligere omtalt som inntektsgrense. " },
                    nynorsk { +"Botnfrådrag er kor mykje inntekt du kan ha før vi byrjar å redusere uføretrygda di. Botnfrådraget består av fribeløpet pluss inntekt etter uførleik. Dette vart tidlegare omtala som inntektsgrense. " },
                )
            }

            paragraph {
                text(
                    bokmal { +"Per nå er grunnbeløpet (G) 136 549 kroner. Grunnbeløpet justeres i mai hvert år. " },
                    nynorsk { +"Per no er grunnbeløpet (G) 136 549 kroner. Grunnbeløpet vert justert i mai kvart år. " },
                )
            }

            title1 {
                text(
                    bokmal { +"Din beregning fra 1. oktober 2026 " },
                    nynorsk { +"Din berekning frå 1. oktober 2026 " },
                )
            }

            paragraph {
                table(header = {
                    column { text(bokmal { +"Beregning" }, nynorsk { +"Berekning" }) }
                    column(alignment = RIGHT) { text(bokmal { +"" }, nynorsk { +"" }) }
                }) {
                    row {
                        cell {
                            text(
                                bokmal { +"Uføretrygd" },
                                nynorsk { +"Uføretrygd" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +data.uforetrygd.format() },
                                nynorsk { +data.uforetrygd.format() },
                            )
                        }
                    }
                    ifNotNull(data.barnetillegg) { bt ->
                        row {
                            cell {
                                text(
                                    bokmal { +"Barnetillegg" },
                                    nynorsk { +"Barnetillegg" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +bt.format() },
                                    nynorsk { +bt.format() },
                                )
                            }
                        }
                    }
                    ifNotNull(data.gjenlevendetillegg) { gjt ->
                        row {
                            cell {
                                text(
                                    bokmal { +"Gjenlevendetillegg" },
                                    nynorsk { +"Attlevandetillegg" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +gjt.format() },
                                    nynorsk { +gjt.format() },
                                )
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"Bunnfradrag" },
                                nynorsk { +"Botnfrådrag" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +data.bunnfradrag.format() },
                                nynorsk { +data.bunnfradrag.format() },
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"Fribeløp" },
                                nynorsk { +"Fribeløp" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +data.fribelop.format() },
                                nynorsk { +data.fribelop.format() },
                            )
                        }
                    }
                    showIf(data.manedligOkningUforetrygdInklTilleggUtAret.notEqualTo(0)) {
                        row {
                            cell {
                                showIf(data.manedligOkningUforetrygdInklTilleggUtAret.greaterThan(0)) {
                                    text(
                                        bokmal { +"Månedlig økning " },
                                        nynorsk { +"Månadleg auke " },
                                    )
                                }.orShow {
                                    text(
                                        bokmal { +"Månedlig reduksjon " },
                                        nynorsk { +"Månadleg reduksjon " },
                                    )
                                }
                                showIf(data.barnetillegg.isNull()) {
                                    text(
                                        bokmal { +"i uføretrygd ut 2026 " },
                                        nynorsk { +"i uføretrygd ut 2026 " },
                                    )
                                }.orShow {
                                    text(
                                        bokmal { +"i uføretrygd og barnetillegg ut 2026 " },
                                        nynorsk { +"i uføretrygd og barnetillegg ut 2026 " },
                                    )
                                }
                            }
                            cell {
                                text(
                                    bokmal { +data.manedligOkningUforetrygdInklTilleggUtAret.format() },
                                    nynorsk { +data.manedligOkningUforetrygdInklTilleggUtAret.format() },
                                )
                            }
                        }
                    }
                }
            }

            paragraph {
                text(
                    bokmal { +"Uføretrygden blir fortsatt utbetalt senest den 20. hver måned. " },
                    nynorsk { +"Uføretrygda blir framleis utbetalt seinast den 20. kvar månad. " },
                )
            }

            title1 {
                text(
                    bokmal { +"Derfor får du høyere fribeløp og bunnfradrag " },
                    nynorsk { +"Derfor får du høgare fribeløp og botnfrådrag " },
                )
            }

            paragraph {
                text(
                    bokmal { +"Stortinget har vedtatt en lovendring som trer i kraft 1. oktober 2026 med virkning fra 1. januar 2026. Lovendringen sier at fribeløpet øker fra 0,4 G til 1 G for de som har hatt uføretrygd i 2 år eller mer, uten økning i uføregraden. " },
                    nynorsk { +"Stortinget har vedteke ei lovendring som trer i kraft 1. oktober 2026 med verknad frå 1. januar 2026. Lovendringa seier at fribeløpet aukar frå 0,4 G til 1 G for dei som har hatt uføretrygd i 2 år eller meir, utan auke i uføregraden. " },
                )
            }


            //SCENARIER
            title1 {
                text(
                    bokmal { +"Slik beregner vi fribeløp og bunnfradrag " },
                    nynorsk { +"Slik bereknar vi fribeløp og botnfrådrag " },
                )
            }
            showIf(data.scenario1_1G) {
                paragraph {
                    showIf(data.venteperiodeEtterGradsokning) {
                        text(
                            bokmal { +"1. januar 2026 er det 2 år eller lenger siden din uføregrad økte. Derfor øker fribeløpet ditt til 1 G for hele 2026. " },
                            nynorsk { +"1. januar 2026 er det 2 år eller lenger sidan di uføregrad auka. Derfor aukar fribeløpet ditt til 1 G for heile 2026. " },
                        )
                    }.orShow {
                        text(
                            bokmal { +"1. januar 2026 har du hatt uføretrygd i 2 år eller lenger. Derfor øker fribeløpet ditt til 1 G for hele 2026. " },
                            nynorsk { +"1. januar 2026 har du hatt uføretrygd i 2 år eller lenger. Derfor aukar fribeløpet ditt til 1 G for heile 2026. " },
                        )
                    }
                }
                showIf(data.manedligOkningUforetrygdInklTilleggUtAret.notEqualTo(0)) {
                    includePhrase(PengerTilGode(data.uforetrygd, data.nettoUtHarBlittLikBrutto, data.btHarBlitt0))
                }
            }.orIfNotNull(data.scenario2_1G_04G) { scenario2 ->
                paragraph {
                    text(
                        bokmal { +"Din uføregrad økte den " + scenario2.dato04G.format() + ". Fra 1. januar 2026 frem til " + scenario2.dato04G.format() + ", er ditt fribeløp 1 G. Etter " + scenario2.dato04G.format() + " er ditt fribeløp 0,4 G, fordi økning i uføregrad utløser ny periode på 2 år hvor fribeløpet er 0,4 G. " },
                        nynorsk { +"Di uføregrad auka den " + scenario2.dato04G.format() + ". Frå 1. januar 2026 fram til " + scenario2.dato04G.format() + ", er ditt fribeløp 1 G. Etter " + scenario2.dato04G.format() + " er ditt fribeløp 0,4 G, fordi auke i uføregrad utløyser ny periode på 2 år der fribeløpet er 0,4 G. " }
                    )
                }

                includePhrase(Fribelopperioder(data.fribelopPerioder, data.vektetFribelop, data.vektetFribelopKr))
                showIf(data.manedligOkningUforetrygdInklTilleggUtAret.notEqualTo(0)) {
                    includePhrase(PengerTilGode(data.uforetrygd, data.nettoUtHarBlittLikBrutto, data.btHarBlitt0))
                }
                showIf(not(data.normertPensjonsdatoFor2028)) {
                    paragraph {
                        text(
                            bokmal { +"Fra 2027 vil du få nytt bunnfradrag med 0,4 G som fribeløp hele året. Bunnfradraget ditt i 2027 blir " + data.bunnfradrag2027.format() + ". " },
                            nynorsk { +"Frå 2027 vil du få nytt botnfrådrag med 0,4 G som fribeløp heile året. Botnfrådraget ditt i 2027 blir " + data.bunnfradrag2027.format() + ". " },
                        )
                    }
                }

            }.orShowIf(data.scenario3_04G_1G) {
                paragraph {
                    showIf(data.venteperiodeEtterGradsokning) {
                        text(
                            bokmal { +"Før " + data.datoOkningBunnfradrag.format() + " var fribeløpet ditt 0,4 G. Fra og med " + data.datoOkningBunnfradrag.format() + " er det 2 år eller lenger siden din uføregrad økte, og fribeløpet skal øke til 1 G." },
                            nynorsk { +"Før " + data.datoOkningBunnfradrag.format() + " var fribeløpet ditt 0,4 G. Frå og med " + data.datoOkningBunnfradrag.format() + " er det 2 år eller lenger sidan di uføregrad auka, og fribeløpet skal auke til 1 G." }
                        )
                    }.orShow {
                        text(
                            bokmal { +"Før " + data.datoOkningBunnfradrag.format() + " var fribeløpet ditt 0,4 G. Fra og med " + data.datoOkningBunnfradrag.format() + " har du hatt uføretrygd i 2 år eller lenger, og fribeløpet skal øke til 1G." },
                            nynorsk { +"Før " + data.datoOkningBunnfradrag.format() + " var fribeløpet ditt 0,4 G. Frå og med " + data.datoOkningBunnfradrag.format() + " har du hatt uføretrygd i 2 år eller lenger, og fribeløpet skal auke til 1G." }
                        )

                    }
                }
                includePhrase(Fribelopperioder(data.fribelopPerioder, data.vektetFribelop, data.vektetFribelopKr))
                showIf(data.manedligOkningUforetrygdInklTilleggUtAret.notEqualTo(0)) {
                    includePhrase(PengerTilGode(data.uforetrygd, data.nettoUtHarBlittLikBrutto, data.btHarBlitt0))
                }
                showIf(not(data.normertPensjonsdatoFor2028)) {
                    paragraph {
                        text(
                            bokmal { +"Fra 2027 vil du få nytt bunnfradrag med 1 G som fribeløp hele året. Bunnfradraget ditt i 2027 blir " + data.bunnfradrag2027.format() + ". " },
                            nynorsk { +"Frå 2027 vil du få nytt botnfrådrag med 1 G som fribeløp heile året. Botnfrådraget ditt i 2027 blir " + data.bunnfradrag2027.format() + ". " },
                        )
                    }
                }
            }.orIfNotNull(data.scenario4_04G_1G_04G) { scenario4 ->
                paragraph {
                    showIf(data.venteperiodeEtterGradsokning) {
                        text(
                            bokmal { +"Før " + data.datoOkningBunnfradrag.format() + " var fribeløpet ditt 0,4 G. Fra og med " + data.datoOkningBunnfradrag.format() + " er det 2 år eller lenger siden din uføregrad økte, og fribeløpet ditt øker til 1 G. Siden du igjen har fått økt uføregrad fra " + scenario4.dato04G.format() + ", endres fribeløpet ditt igjen til 0,4 G. " },
                            nynorsk { +"Før " + data.datoOkningBunnfradrag.format() + " var fribeløpet ditt 0,4 G. Frå og med " + data.datoOkningBunnfradrag.format() + " er det 2 år eller lenger sidan di uføregrad auka, og fribeløpet ditt aukar til 1 G. Sidan du igjen har fått auka uføregrad frå " + scenario4.dato04G.format() + ", endrar fribeløpet ditt seg igjen til 0,4 G. " },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Før " + data.datoOkningBunnfradrag.format() + " var fribeløpet ditt 0,4 G. Fra og med " + data.datoOkningBunnfradrag.format() + " har du hatt uføretrygd i 2 år eller lenger, og fribeløpet ditt øker til 1 G. Siden du igjen har fått økt uføregrad fra " + scenario4.dato04G.format() + ", endres fribeløpet ditt igjen til 0,4 G. " },
                            nynorsk { +"Før " + data.datoOkningBunnfradrag.format() + " var fribeløpet ditt 0,4 G. Frå og med " + data.datoOkningBunnfradrag.format() + " har du hatt uføretrygd i 2 år eller lenger, og fribeløpet ditt aukar til 1 G. Sidan du igjen har fått auka uføregrad frå " + scenario4.dato04G.format() + ", endrar fribeløpet ditt seg igjen til 0,4 G. " },
                        )
                    }
                }
                includePhrase(Fribelopperioder(data.fribelopPerioder, data.vektetFribelop, data.vektetFribelopKr))
                showIf(data.manedligOkningUforetrygdInklTilleggUtAret.notEqualTo(0)) {
                    includePhrase(PengerTilGode(data.uforetrygd, data.nettoUtHarBlittLikBrutto, data.btHarBlitt0))
                }
                showIf(not(data.normertPensjonsdatoFor2028)) {
                    paragraph {
                        text(
                            bokmal { +"Fra 2027 vil du få nytt bunnfradrag med 0,4 G som fribeløp hele året. Bunnfradraget ditt i 2027 blir " + data.bunnfradrag2027.format() + ". " },
                            nynorsk { +"Frå 2027 vil du få nytt botnfrådrag med 0,4 G som fribeløp heile året. Botnfrådraget ditt i 2027 blir " + data.bunnfradrag2027.format() + ". " },
                        )
                    }
                }
            }

            //BARNETILLEGG
            ifNotNull(data.barnetillegg) { bt ->
                showIf(data.redusertBtfb or data.redusertBtsb) {
                    title1 {
                        text(
                            bokmal { +"Endring i barnetillegg" },
                            nynorsk { +"Endring i barnetillegg" },
                        )
                    }
                    showIf(data.redusertBtsb or data.okningUt) {
                        paragraph {
                            text(
                                bokmal { +"Regelverksendringene fører til at du får en høyere utbetaling av uføretrygd. Uføretrygden regnes med som inntekt når vi beregner barnetillegg. Derfor får du en lavere utbetaling av barnetillegg. Ny beregning av barnetillegg (før skatt) er " + bt.format() + ". " },
                                nynorsk { +"Regelverksendringane fører til at du får ei høgare utbetaling av uføretrygd. Uføretrygda vert rekna med som inntekt når vi bereknar barnetillegg. Derfor får du ei lågare utbetaling av barnetillegg. Ny berekning av barnetillegg (før skatt) er " + bt.format() + ". " },
                            )
                        }
                    }.orShow {//redusert fb uten økning i ut betyr eps sin sak har økt ut
                        paragraph {
                            text(
                                bokmal { +"Regelverksendringene fører til at barnetillegg for fellesbarn endres fordi begge foreldres inntekt regnes med. Derfor får du en lavere utbetaling av barnetillegg. Ny beregning av barnetillegg (før skatt) er " + bt.format() + ". " },
                                nynorsk { +"Regelverksendringane fører til at barnetillegg for fellesbarn endrar seg fordi begge foreldra sine inntekter vert rekna med. Derfor får du ei lågare utbetaling av barnetillegg. Ny berekning av barnetillegg (før skatt) er " + bt.format() + ". " })
                        }
                    }
                    showIf(data.btHarBlitt0) {
                        paragraph {
                            text(
                                bokmal { +"Dersom vi i år allerede har utbetalt for mye barnetillegg, vil dette bli regulert i etteroppgjøret neste år. " },
                                nynorsk { +"Dersom vi i år allereie har utbetalt for mykje barnetillegg, vil dette bli regulert i etteroppgjeret neste år. " },
                            )
                        }
                    }
                }
            }
        }
    }
}
