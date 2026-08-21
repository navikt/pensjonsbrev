package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktBunnfradragData
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.okningGrad2026.dato
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.okningGrad2026.gammelUforegrad
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.antallMnd1g
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.barnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.bunnfradrag
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.bunnfradrag2027
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.datoOkningBunnfradrag
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.fribelop
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.ieu
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.manedligOkningUforetrygdUtAret
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.okningGrad2026
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.okningUt
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.redusertBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.toArFor2026
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.toArI2026ForForsteOktober
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.uforegrad
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.uforetrygd
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
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.ITALIC
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.minus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference

object OktBunnfradrag {

    data class Outline(val data: Expression<VedtakOmOktBunnfradragData>) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

            paragraph {
                text(
                    bokmal { +"Fra " + data.datoOkningBunnfradrag.format() + " øker fribeløpet ditt til 1 ganger folketrygdens grunnbeløp (G), Dette er per i dag 136 549 kroner. Det fører til at bunnfradraget ditt øker. Du kan ha inntekt på opptil ditt nye bunnfradrag før vi begynner å redusere uføretrygden din. " },
                    nynorsk { +"Frå " + data.datoOkningBunnfradrag.format() + " aukar fribeløpet ditt til 1 gonger folketrygdens grunnbeløp (G), Dette er per i dag 136 549 kroner. Det fører til at botnfrådraget ditt aukar. Du kan ha inntekt på opptil ditt nye botnfrådrag før vi byrjar å redusere uføretrygda di. " },
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
                    bokmal { +"Bunnfradrag er hvor mye inntekt du kan ha før vi begynner å redusere uføretrygden din. Bunnfradraget består av fribeløpet (0,4 G eller 1G) pluss inntekt etter uførhet. Dette ble tidligere omtalt som inntektsgrense. " },
                    nynorsk { +"Botnfrådrag er kor mykje inntekt du kan ha før vi byrjar å redusere uføretrygda di. Botnfrådraget består av fribeløpet (0,4 G eller 1G) pluss inntekt etter uførleik. Dette vart tidlegare omtala som inntektsgrense. " },
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
                    column { text(bokmal { +"Beregning" }, nynorsk { +"" }) }
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
                    ifNotNull(data.manedligOkningUforetrygdUtAret) { manedligOkningUforetrygdUtAret ->
                        row {
                            cell {
                                text(
                                    bokmal { +"Månedlig økning i uføretrygd ut 2026" },
                                    nynorsk { +"Månadleg auke i uføretrygd ut 2026" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +manedligOkningUforetrygdUtAret.format() },
                                    nynorsk { +manedligOkningUforetrygdUtAret.format() },
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
                    bokmal { +"Derfor får du høyere fribeløp " },
                    nynorsk { +"Derfor får du høgare fribeløp " },
                )
            }

            paragraph {
                text(
                    bokmal { +"Stortinget har vedtatt en lovendring som trer i kraft 1. oktober 2026 med virkning fra 1. januar 2026. Lovendringen sier at fribeløpet øker fra 0,4 G til 1 G for de som har hatt uføretrygd i 2 år eller mer, uten økning i uføregraden. " },
                    nynorsk { +"Stortinget har vedteke ei lovendring som trer i kraft 1. oktober 2026 med verknad frå 1. januar 2026. Lovendringen seier at fribeløpet aukar frå 0,4 G til 1 G for dei som har hatt uføretrygd i 2 år eller meir, utan auke i uføregraden. " },
                )
            }

            title1 {
                text(
                    bokmal { +"Slik beregner vi fribeløp og bunnfradrag " },
                    nynorsk { +"Slik bereknar vi fribeløp og botnfrådrag " },
                )
            }
            ifNotNull(data.okningGrad2026) { okningGrad2026 ->
                paragraph {
                    text(
                        bokmal { +"Regelendringen for økt fribeløp trer i kraft 1. oktober 2026, med tilbakevirkende kraft til 1. januar 2026. " },
                        nynorsk { +"Regelendringa for auka fribeløp trer i kraft 1. oktober 2026, med tilbakeverkande kraft til 1. januar 2026. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du endret uføregrad fra " + okningGrad2026.gammelUforegrad.format() + " prosent til " + data.uforegrad.format() + " prosent " + okningGrad2026.dato.format() + ". Fra 1. januar 2026 frem til " + okningGrad2026.dato.format() + ", er ditt fribeløp 1 G. Etter " + okningGrad2026.dato.format() + " er ditt fribeløp 0,4 G, fordi økning i uføregrad utløser ny periode på 2 år hvor fribeløpet er 0,4 G. " },
                        nynorsk { +"Du endra uføregrad frå " + okningGrad2026.gammelUforegrad.format() + " prosent til " + data.uforegrad.format() + " prosent " + okningGrad2026.dato.format() + ". Frå 1. januar 2026 fram til " + okningGrad2026.dato.format() + ", er ditt fribeløp 1 G. Etter " + okningGrad2026.dato.format() + " er ditt fribeløp 0,4 G, fordi auke i uføregrad utløyser ny periode på 2 år der fribeløpet er 0,4 G. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Beregningen av bunnfradraget for hele året vil derfor se slik ut: " },
                        nynorsk { +"Berekninga av botnfrådraget for heile året vil derfor sjå slik ut: " },
                    )
                }
                paragraph {//TODO Nytt regnestykke vektet
                    text(
                        bokmal { +data.ieu.format() + " (inntekt etter uførhet) + 0,4 G * " + 12.expr().minus(data.antallMnd1g).format() + "/12 (mnd med gammelt fribeløp) + 1 G * " + data.antallMnd1g.format() + "/12 (mnd med nytt fribeløp)" },
                        nynorsk { +data.ieu.format() + " (inntekt etter uførleik) + 0,4 G * " + 12.expr().minus(data.antallMnd1g).format() + "/12 (mnd med gamalt fribeløp) + 1 G * " + data.antallMnd1g.format() + "/12 (mnd med nytt fribeløp)" },
                        fontType = ITALIC
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Neste år: " }, nynorsk { +"Neste år: " }, fontType = FontType.BOLD
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Fra 2027 vil du få nytt bunnfradrag med 0,4 G som fribeløp hele året. Bunnfradraget ditt i 2027 blir " + data.bunnfradrag2027.format() + ". " },
                        nynorsk { +"Frå 2027 vil du få nytt botnfrådrag med 0,4 G som fribeløp heile året. Botnfrådraget ditt i 2027 blir " + data.bunnfradrag2027.format() + ". " },
                    )
                }

            }.orShowIf(data.toArI2026ForForsteOktober) {
                paragraph {
                    text(
                        bokmal { +"Fra og med " + data.datoOkningBunnfradrag.format() + " har du hatt " + data.uforegrad.format() + " prosent uføretrygd i 2 år, og fribeløpet skal øke til 1G fra 1. oktober. Før 1. oktober var fribeløpet ditt 0,4 G" },
                        nynorsk { +"Frå og med " + data.datoOkningBunnfradrag.format() + " har du hatt " + data.uforegrad.format() + " prosent uføretrygd i 2 år, og fribeløpet skal auke til 1G frå 1. oktober. Før 1. oktober var fribeløpet ditt 0,4 G" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Beregningen av bunnfradraget for hele året vil derfor se slik ut: " },
                        nynorsk { +"Berekninga av botnfrådraget for heile året vil derfor sjå slik ut: " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +data.ieu.format() + " (inntekt etter uførhet) + 0,4 G * " + 12.expr().minus(data.antallMnd1g).format() + "/12 (mnd med gammelt fribeløp) + 1 G * " + data.antallMnd1g.format() + "/12 (mnd med nytt fribeløp)" },
                        nynorsk { +data.ieu.format() + " (inntekt etter uførleik) + 0,4 G * " + 12.expr().minus(data.antallMnd1g).format() + "/12 (mnd med gamalt fribeløp) + 1 G * " + data.antallMnd1g.format() + "/12 (mnd med nytt fribeløp)" },
                        fontType = ITALIC
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Neste år: " }, nynorsk { +"Neste år: " }, fontType = FontType.BOLD
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Fra 2027 vil du få nytt bunnfradrag med 1 G som fribeløp hele året. Bunnfradraget ditt i 2027 blir " + data.bunnfradrag2027.format() + ". " },
                        nynorsk { +"Frå 2027 vil du få nytt botnfrådrag med 1 G som fribeløp heile året. Botnfrådraget ditt i 2027 blir " + data.bunnfradrag2027.format() + ". " },
                    )
                }

            }.orShowIf(data.toArFor2026) {
                paragraph {
                    text(
                        bokmal { +"Du har hatt uføretrygd i 2 år eller lenger før 1. januar 2026, og derfor øker fribeløpet ditt til 1 G for hele 2026. " },
                        nynorsk { +"Du har hatt uføretrygd i 2 år eller lenger før 1. januar 2026, og derfor aukar fribeløpet ditt til 1 G for heile 2026. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis du har fått for lite utbetalt mellom 1. januar og 1. oktober, gjør vi følgende: " },
                        nynorsk { +"Viss du har fått for lite utbetalt mellom 1. januar og 1. oktober, gjer vi følgjande: " },
                    )
                }
                paragraph {
                    list {
                        item {
                            text(
                                bokmal { +"Resten av året:" }, nynorsk { +"Resten av året:" }, FontType.BOLD
                            )
                            text(
                                bokmal { +"Vi øker de månedlige utbetalingene dine ut 2026. Vi kan ikke utbetale mer enn XX kroner i måneden før skatt. Dette er uføretrygden din før inntektsavkorting. " },
                                nynorsk { +"Vi aukar dei månadlege utbetalingane dine ut 2026. Vi kan ikkje utbetale meir enn XX kroner i månaden før skatt. Dette er uføretrygda di før inntektsavkorting. " },
                            )
                        }
                        item {
                            text(
                                bokmal { +"Vi vet ikke hvor mye dette blir før etteroppgjøret er klart. Grunnen til det, er at endringer i inntekt, sivilstatus, uføregrad, barnetillegg og andre endringer i din situasjon kan påvirke etteroppgjøret. Derfor er det viktig at du sier ifra til oss om endringer i din inntekt og situasjon. " },
                                nynorsk { +"Vi veit ikkje kor mykje dette blir før etteroppgjeret er klart. Grunnen til det, er at endringar i inntekt, sivilstatus, uføregrad, barnetillegg og andre endringar i di situasjon kan påverke etteroppgjeret. Derfor er det viktig at du seier ifrå til oss om endringar i di inntekt og situasjon. " },
                            )
                        }
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
                showIf(data.okningUt) {
                    title1 {
                        text(
                            bokmal { +"Endring i barnetillegg" },
                            nynorsk { +"Endring i barnetillegg" },
                        )
                    }
                    showIf(data.redusertBarnetillegg) {
                        paragraph {
                            text(
                                bokmal { +"Regelverksendringene fører til at du får en høyere utbetaling av uføretrygd. Uføretrygden regnes med som inntekt når vi beregner barnetillegg. Derfor får du en lavere utbetaling av barnetillegg. Ny beregning av barnetillegg (før skatt) er: " + bt.format() },
                                nynorsk { +"Regelverksendringane fører til at du får ei høgare utbetaling av uføretrygd. Uføretrygda vert rekna med som inntekt når vi bereknar barnetillegg. Difor får du ei lågare utbetaling av barnetillegg. Ny berekning av barnetillegg (før skatt) er: " + bt.format() },
                            )
                        }
                    }.orShow {
                        paragraph {
                            text(bokmal { +"Regelverksendringene fører til at barnetillegg for fellesbarn endres fordi begge foreldres inntekt regnes med. Derfor får du en lavere utbetaling av barnetillegg. Ny beregning av barnetillegg (før skatt) er:" + bt.format() }, nynorsk { +"Regelverksendringane fører til at barnetillegg for fellesbarn endrar seg fordi begge foreldra sine inntekter vert rekna med. Difor får du ei lågare utbetaling av barnetillegg. Ny berekning av barnetillegg (før skatt) er:" + bt.format() })
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