package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.api.model.maler.legacy.FribelopPeriode
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktBunnfradragData
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.fribelopPeriode.faktor
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.fribelopPeriode.fom
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.fribelopPeriode.tom
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.fribelopPeriode.uforegrad
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.scenario2_1G_04G.dato04G
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.scenario4_04G_1G_04G.dato04G
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.barnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.btHarBlitt0
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.bunnfradrag
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.bunnfradrag2027
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.datoOkningBunnfradrag
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.fribelop
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.fribelopPerioder
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.gjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.manedligOkningUforetrygdInklTilleggUtAret
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.nettoUtHarBlittLikBrutto
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.normertPensjonsdatoFor2028
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.okningUt
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.redusertBtsb
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.redusertBtfb
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.scenario1_1G
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.scenario2_1G_04G
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.scenario3_04G_1G
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.scenario4_04G_1G_04G
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.uforetrygd
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.vektetFribelop
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.vektetFribelopKr
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.venteperiodeEtterGradsokning
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.formatMonthYear
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

object OktBunnfradrag {

    data class Outline(val data: Expression<VedtakOmOktBunnfradragData>) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

            paragraph {
                text(
                    bokmal { +"Fra " + data.datoOkningBunnfradrag.format() + " øker fribeløpet ditt til 1 ganger folketrygdens grunnbeløp (G). Dette er per i dag 136 549 kroner. Det fører til at bunnfradraget ditt øker. Du kan ha inntekt på opptil ditt nye bunnfradrag før vi begynner å redusere uføretrygden din. " },
                    nynorsk { +"Frå " + data.datoOkningBunnfradrag.format() + " aukar fribeløpet ditt til 1 gonger folketrygdens grunnbeløp (G). Dette er per i dag 136 549 kroner. Det fører til at botnfrådraget ditt aukar. Du kan ha inntekt på opptil ditt nye botnfrådrag før vi byrjar å redusere uføretrygda di. " }
                )
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
                    bokmal { +"Bunnfradrag er hvor mye inntekt du kan ha før vi begynner å redusere uføretrygden din. Bunnfradraget består av fribeløpet pluss  inntekt etter uførhet. Dette ble tidligere omtalt som inntektsgrense. " },
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
                            bokmal { +"Før " + data.datoOkningBunnfradrag.format() + " var fribeløpet ditt 0,4 G. Fra og med " + data.datoOkningBunnfradrag.format() + " er det 2 år eller lenger siden din uføregrad økte, og fribeløpet ditt øker til 1G. Siden du igjen har fått økt uføregrad fra " + scenario4.dato04G.format() + ", endres fribeløpet ditt igjen til 0,4G. " },
                            nynorsk { +"Før " + data.datoOkningBunnfradrag.format() + " var fribeløpet ditt 0,4 G. Frå og med " + data.datoOkningBunnfradrag.format() + " er det 2 år eller lenger sidan di uføregrad auka, og fribeløpet ditt aukar til 1G. Sidan du igjen har fått auka uføregrad frå " + scenario4.dato04G.format() + ", endrar fribeløpet ditt seg igjen til 0,4G. " },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Før " + data.datoOkningBunnfradrag.format() + " var fribeløpet ditt 0,4 G. Fra og med " + data.datoOkningBunnfradrag.format() + " har du hatt uføretrygd i 2 år eller lenger, og fribeløpet ditt øker til 1G. Siden du igjen har fått økt uføregrad fra " + scenario4.dato04G.format() + ", endres fribeløpet ditt igjen til 0,4G. " },
                            nynorsk { +"Før " + data.datoOkningBunnfradrag.format() + " var fribeløpet ditt 0,4 G. Frå og med " + data.datoOkningBunnfradrag.format() + " har du hatt uføretrygd i 2 år eller lenger, og fribeløpet ditt aukar til 1G. Sidan du igjen har fått auka uføregrad frå " + scenario4.dato04G.format() + ", endrar fribeløpet ditt seg igjen til 0,4G. " },
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


            paragraph {
                text(
                    bokmal { +"I vedlegget " },
                    nynorsk { +"I vedlegget " },
                )
                namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                text(
                    bokmal { +" kan du se hvordan vi har beregnet uføretrygden din." },
                    nynorsk { +" kan du sjå korleis vi har berekna uføretrygda di." },
                )
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
                    paragraph {
                        text(
                            bokmal { +"Dersom vi i år allerede har utbetalt for mye barnetillegg, vil dette bli regulert i etteroppgjøret neste år. " },
                            nynorsk { +"Dersom vi i år allereie har utbetalt for mykje barnetillegg, vil dette bli regulert i etteroppgjeret neste år. " },
                        )
                    }
                    paragraph {
                        text(bokmal { +"Du kan lese mer om dette i vedlegget " }, nynorsk { +"Du kan lese meir om dette i vedlegget " })
                        namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                        text(
                            bokmal { +"." },
                            nynorsk { +"." },
                        )
                    }
                }
            }


            //KLAGE ETC
            title1 {
                text(
                    bokmal { +"Du har rett til å klage" },
                    nynorsk { +"Du har rett til å klage" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen vedtaket har kommet fram til deg. Du finner skjema og informasjon på " + "${Constants.KLAGE_URL}."
                    },
                    nynorsk {
                        +"Om du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen vedtaket har kome fram til deg. Du finn skjema og informasjon på " + "${Constants.KLAGE_URL}."
                    },
                )
            }
            paragraph {
                text(
                    bokmal { +"I vedlegget " },
                    nynorsk { +"I vedlegget " },
                )
                namedReference(vedleggDineRettigheterOgPlikterUfoere)
                text(
                    bokmal { +" får du vite mer om hvordan du går fram for å klage." },
                    nynorsk { +" får du vite meir om korleis du går fram for å klage." },
                )
            }
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)//TODO: ny tekst her?
        }
    }
}

class PengerTilGode(private val nettoUt: Expression<Kroner>, private val nettoUtHarBlittLikBrutto: Expression<Boolean>, private val btHarBlitt0: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { +"Dersom du har penger til gode eller har fått for mye utbetalt, gjør vi følgende: " },
                nynorsk { +"Dersom du har pengar til gode eller har fått for mykje utbetalt, gjer vi følgjande: " },
            )
        }
        paragraph {
            list {
                item {
                    text(
                        bokmal { +"Resten av året: " }, nynorsk { +"Resten av året: " }, FontType.BOLD
                    )
                    text(
                        bokmal { +"Vi justerer de månedlige utbetalingene dine ut 2026. Vi kan ikke utbetale mer enn " + nettoUt.format() + " i uføretrygd i måneden før skatt. Dette er uføretrygden din før inntektsavkorting. " },
                        nynorsk { +"Vi justerer dei månadlege utbetalingane dine ut 2026. Vi kan ikkje utbetale meir enn " + nettoUt.format() + " i uføretrygd i månaden før skatt. Dette er uføretrygda di før inntektsavkorting. " },
                    )
                }
                showIf(nettoUtHarBlittLikBrutto or btHarBlitt0) {
                    item {
                        text(
                            bokmal { +"Hvis du fortsatt har penger til gode eller har fått for mye utbetalt, vil dette bli justert i etteroppgjøret for 2026. " },
                            nynorsk { +"Om du framleis har pengar til gode eller har fått for mykje utbetalt, vil dette bli justert i etteroppgjeret for 2026. " },
                        )
                        text(
                            bokmal { +"Vi vet ikke hvor mye dette blir før etteroppgjøret er klart. Grunnen til det, er at endringer i inntekt, sivilstatus, uføregrad, barnetillegg og andre endringer i din situasjon kan påvirke etteroppgjøret. Etteroppgjøret gjennomføres alltid på høsten, etter at alle inntektsopplysningene for kalenderåret foreligger fra Skatteetaten. " },
                            nynorsk { +"Vi veit ikkje kor mykje dette blir før etteroppgjeret er klart. Grunnen til det, er at endringar i inntekt, sivilstatus, uføregrad, barnetillegg og andre endringar i di situasjon kan påverke etteroppgjeret. Etteroppgjeret gjennomførast alltid på hausten, etter at alle inntektsopplysningane for kalenderåret ligg føre frå Skatteetaten. " },
                        )
                    }
                }
            }
        }
    }
}


class Fribelopperioder(private val perioder: Expression<List<FribelopPeriode>>, private val vektetFribelop: Expression<Double>, private val vektetFribelopKr: Expression<Kroner>) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            table(header = {
                column { text(bokmal { +"Fra" }, nynorsk { +"Frå" }) }
                column { text(bokmal { +"Til" }, nynorsk { +"Til" }) }
                column { text(bokmal { +"Uføregrad" }, nynorsk { +"Uføregrad" }) }
                column { text(bokmal { +"Fribeløp" }, nynorsk { +"Fribeløp" }) }
            }) {
                forEach(perioder) { periode ->
                    row {
                        cell {
                            text(
                                bokmal { +periode.fom.formatMonthYear() },
                                nynorsk { +"" + periode.fom.formatMonthYear() }
                            )
                        }
                        cell {
                            text(
                                bokmal { +periode.tom.formatMonthYear() },
                                nynorsk { +"" + periode.tom.formatMonthYear() }
                            )
                        }
                        cell {
                            text(
                                bokmal { +periode.uforegrad.format() + " prosent" },
                                nynorsk { +periode.uforegrad.format() + " prosent" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +periode.faktor.format() + " G" },
                                nynorsk { +periode.faktor.format() + " G" }
                            )
                        }
                    }
                }
            }
        }
        paragraph {
            text(
                bokmal { +"Når fribeløpet endres i løpet av året, beregnes et gjennomsnitt av periodene du har hatt med ulikt fribeløp. Gjennomsnittlig fribeløp i år blir " + vektetFribelop.format() + " G, som er " + vektetFribelopKr.format() + "." },
                nynorsk { +"Når fribeløpet endrar seg i løpet av året, vert det rekna ut eit gjennomsnitt av periodane du har hatt med ulikt fribeløp. Gjennomsnittleg fribeløp i år vert " + vektetFribelop.format() + " G, som er " + vektetFribelopKr.format() + "." },
            )
        }
    }
}