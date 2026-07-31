package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktBunnfradragData
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.antallMnd1g
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.barnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.bunnfradrag
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.datoOkning
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.ekstraManedligUfoeretrygdUtAret
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.fribelop
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.ieu
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.oktFribelopHeleAret
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
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

object OktBunnfradrag {

    data class Outline(val data: Expression<VedtakOmOktBunnfradragData>) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

            paragraph {
                text(
                    bokmal { +"Stortinget har vedtatt en lovendring fra 1. oktober 2026, som har tilbakevirkende kraft fra 1. januar 2026. Fribeløpet økes til 136 549 kroner (1G) når du har hatt uføretrygd i 2 år eller mer, uten endring i uføregraden. Det betyr at du kan ha en høyere inntekt ved siden av uføretrygden din før den blir redusert." },
                    nynorsk { +"Stortinget har vedteke ei lovendring frå 1. oktober 2026, som har tilbakeverkande kraft frå 1. januar 2026. Fribeløpet vert auka til 136 549 kroner (1G) når du har hatt uføretrygd i 2 år eller meir, utan endring i uføregraden. Det betyr at du kan ha ei høgare inntekt ved sida av uføretrygda di før ho vert redusert." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Det at du får et høyere bunnfradrag påvirker deg bare hvis du har inntekt over inntektsgrensen ved siden av uføretrygden. Det kan føre til at det blir mer lønnsomt for deg å kombinere jobb og uføretrygd." },
                    nynorsk { +"Det at du får eit høgare botnfrådrag påverkar deg berre viss du har inntekt over inntektsgrensa ved sida av uføretrygda. Det kan føre til at det blir meir lønsamt for deg å kombinere jobb og uføretrygd." },
                )
            }

            title1 {
                text(
                    bokmal { +"Din beregning fra 1. oktober 2026" },
                    nynorsk { +"Di berekning frå 1. oktober 2026" },
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
                    showIf(!data.barnetillegg.isNull()) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Barnetillegg" },
                                    nynorsk { +"Barnetillegg" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +data.barnetillegg.ifNull(Kroner(0)).format() },
                                    nynorsk { +data.barnetillegg.ifNull(Kroner(0)).format() },
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
                    ifNotNull(data.ekstraManedligUfoeretrygdUtAret) { ekstraManedligUfoeretrygdUtAret ->
                        row {
                            cell {
                                text(
                                    bokmal { +"Ekstra månedlig uføretrygd ut året" },
                                    nynorsk { +"Ekstra månadleg uføretrygd ut året" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +ekstraManedligUfoeretrygdUtAret.format() },
                                    nynorsk { +ekstraManedligUfoeretrygdUtAret.format() },
                                )
                            }
                        }
                    }
                }
            }

            paragraph {
                text(
                    bokmal { +"Hvis du har fått for lite utbetalt mellom 1. januar og 1. oktober 2026, gjør vi følgende:" },
                    nynorsk { +"Viss du har fått for lite utbetalt mellom 1. januar og 1. oktober 2026, gjer vi følgjande:" },
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            bokmal { +"Resten av året: Vi øker de månedlige utbetalingene dine ut 2026. Du kan likevel ikke få mer enn uføretrygden din før inntektsavkorting." },
                            nynorsk { +"Resten av året: Vi aukar dei månadlege utbetalingane dine ut 2026. Du kan likevel ikkje få meir enn uføretrygda di før inntektsavkorting." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"Neste år: Hvis du fortsatt har penger til gode etter at året er omme, får du resten utbetalt i etteroppgjøret neste år." },
                            nynorsk { +"Neste år: Viss du framleis har pengar til gode etter at året er omme, får du resten utbetalt i etteroppgjeret neste år." },
                        )
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
                    bokmal { +"To viktige begrep" },
                    nynorsk { +"To viktige omgrep" },
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            bokmal { +"Bunnfradrag (tidligere inntektsgrense) - Bunnfradraget består av inntekt etter uførhet (IEU) og et fribeløp. Bunnfradraget er den årlige inntekten du kan ha, før vi reduserer uføretrygden din." },
                            nynorsk { +"Botnfrådrag (tidlegare inntektsgrense) - Botnfrådraget består av inntekt etter uførleik (IEU) og eit fribeløp. Botnfrådraget er den årlege inntekta du kan ha, før vi reduserer uføretrygda di." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"Fribeløp i dag er 0,4 G, dette endres til 1 G med virkning fra 1. januar 2026." },
                            nynorsk { +"Fribeløp i dag er 0,4 G, dette vert endra til 1 G med verknad frå 1. januar 2026." },
                        )
                    }
                }
            }

            title1 {
                text(
                    bokmal { +"Slik påvirker endringen deg" },
                    nynorsk { +"Slik påverkar endringa deg" },
                )
            }
            showIf(!data.oktFribelopHeleAret) {
                // Deler av året
                ifNotNull(data.datoOkning) { datoOkning ->
                    ifNotNull(data.antallMnd1g) { antallMnd1g ->
                        paragraph {
                            text(
                                bokmal { +"Fra og med " + datoOkning.format() + " har du hatt uføretrygd i 2 år og fribeløpet ditt skal øke til 1G. Før denne datoen var fribeløpet ditt 0,4 G, og beregningen av bunnfradraget for hele året vil derfor se slik ut:" },
                                nynorsk { +"Frå og med " + datoOkning.format() + " har du hatt uføretrygd i 2 år og fribeløpet ditt skal auke til 1G. Før denne datoen var fribeløpet ditt 0,4 G, og berekninga av botnfrådraget for heile året vil derfor sjå slik ut:" },
                            )
                        }
                        paragraph {
                            //TODO: Hva er dette, legge inn alle disse tallene? Jeg gjorde det. Burde det evt presiseres hva tallene er? Og hva g-beløpene er?
                            text(
                                //TODO 12- antall mnd
                                bokmal { +data.ieu.format() + " + " + antallMnd1g.format() + " * 0,4G + " + antallMnd1g.format() + " * 1G" },
                                nynorsk { +data.ieu.format() + " + " + antallMnd1g.format() + " * 0,4G + " + antallMnd1g.format() + " * 1G" },
                            )
                        }
                        paragraph {
                            text(
                                bokmal { +"Fra 2027 vil du få nytt bunnfradrag med 1 G som fribeløp hele året." },
                                nynorsk { +"Frå 2027 vil du få nytt botnfrådrag med 1 G som fribeløp heile året." },
                            )
                        }
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Du har " + data.uforegrad.format() + " prosent uføretrygd og fribeløpet ditt har økt til 1 (G). Ditt nye bunnfradrag er derfor " + data.bunnfradrag.format() + ". Når du får økt bunnfradrag, kan du ha mer inntekt ved siden av uføretrygden, før vi reduserer uføretrygden din." },
                    nynorsk { +"Du har " + data.uforegrad.format() + " prosent uføretrygd og fribeløpet ditt har auka til 1 (G). Ditt nye botnfrådrag er derfor " + data.bunnfradrag.format() + ". Når du får auka botnfrådrag, kan du ha meir inntekt ved sida av uføretrygda, før vi reduserer uføretrygda di." },
                )
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

            showIf(!data.barnetillegg.isNull()) {
                title1 {
                    text(
                        bokmal { +"Endring i barnetillegg" },
                        nynorsk { +"Endring i barnetillegg" },
                    )
                }
                // TODO: Får man høyere? Hva skal styre om det dukker opp?
                paragraph {
                    text(
                        bokmal { +"Regelverksendringene fører til at du får en høyere utbetaling av uføretrygd. Uføretrygden regnes med som inntekt når vi beregner barnetillegg. Derfor får du en lavere utbetaling av barnetillegg. Ny beregning av barnetillegg (før skatt) er: " + data.barnetillegg.ifNull(Kroner(0)).format() },
                        nynorsk { +"Regelverksendringane fører til at du får ei høgare utbetaling av uføretrygd. Uføretrygda vert rekna med som inntekt når vi reknar ut barnetillegg. Derfor får du ei lågare utbetaling av barnetillegg. Ny berekning av barnetillegg (før skatt) er: " + data.barnetillegg.ifNull(Kroner(0)).format() },
                    )
                }
                //TODO: Får man lavere?
                paragraph {
                    text(
                        bokmal { +"Regelverksendringene fører til at du får en lavere utbetaling av uføretrygd. Uføretrygden regnes med som inntekt når vi beregner barnetillegg. Derfor får du en høyere utbetaling av barnetillegg. Ny beregning av barnetillegg (før skatt) er: " + data.barnetillegg.ifNull(Kroner(0)).format() },
                        nynorsk { +"Regelverksendringane fører til at du får ei lågare utbetaling av uføretrygd. Uføretrygda vert rekna med som inntekt når vi reknar ut barnetillegg. Derfor får du ei høgare utbetaling av barnetillegg. Ny berekning av barnetillegg (før skatt) er: " + data.barnetillegg.ifNull(Kroner(0)).format() },
                    )
                }
            }



            title1 {
                text(
                    bokmal { +"Du har rett til å klage" },
                    nynorsk { +"Du har rett til å klage" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen vedtaket har kommet fram til deg. Du finner skjema og informasjon på " +
                                "${Constants.KLAGE_URL}."
                    },
                    nynorsk {
                        +"Om du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen vedtaket har kome fram til deg. Du finn skjema og informasjon på " +
                                "${Constants.KLAGE_URL}."
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
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
    }
}
