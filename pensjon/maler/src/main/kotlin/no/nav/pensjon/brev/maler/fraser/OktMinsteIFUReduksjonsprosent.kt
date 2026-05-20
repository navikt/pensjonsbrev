package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.UTTillegg
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.HjemmelFormatter
import no.nav.pensjon.brev.maler.legacy.UTOgTilleggMapper
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

object OktMinsteIFUReduksjonsprosent {

    data class Brevdata(
        val totalbelop: Expression<Kroner>,
        val nettoUforetrygdUtenTillegg: Expression<Kroner>,
        val nettoBarnetillegg: Expression<Kroner?>,
        val etterbetalingJuli: Expression<Kroner>,
        val reduksjonsprosent: Expression<Double>,
        val inntektstak: Expression<Kroner>,
        val ifu: Expression<Kroner>,
        val endringNettoUforetrygdUtenTillegg: Expression<Boolean>,
        val endringNettoBarnetillegg: Expression<Boolean>,
        val endringInntektstak: Expression<Boolean>,
        val erInntektsavkortet: Expression<Boolean>,
        val tillegg: Expression<Collection<UTTillegg>>,
        val hjemler: Expression<Set<String>>,
        val visOktMinsteIFU: Expression<Boolean>,
        val visReduksjonsprosent: Expression<Boolean>,
    )

    data class Outline(val data: Brevdata) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

            title1 {
                text(
                    bokmal { +"Dette er dine endringer fra 1. januar 2026" },
                    nynorsk { +"Dette er endringane dine frå 1. januar 2026" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Vi endrer uføretrygden din fordi Stortinget har vedtatt lovendringer som trer i kraft 1. juli 2026, men gjelder fra 1. januar 2026. " },
                    nynorsk { +"Vi endrar uføretrygda di fordi Stortinget har vedteke lovendringar som trer i kraft 1. juli 2026, men gjeld frå 1. januar 2026. " },
                )
                table(header = {
                    // TODO: Hva skal kolonnene hete?
                    column { text(bokmal { +"" }, nynorsk { +"" }) }
                    column(alignment = RIGHT) {}
                }) {
                    showIf(data.endringNettoUforetrygdUtenTillegg) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Ny beregnet uføretrygd" },
                                    nynorsk { +"Ny berekna uføretrygd" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +data.nettoUforetrygdUtenTillegg.format() },
                                    nynorsk { +data.nettoUforetrygdUtenTillegg.format() },
                                )
                            }
                        }
                    }
                    showIf(data.endringNettoBarnetillegg) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Nytt barnetillegg" },
                                    nynorsk { +"Nytt barnetillegg" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +data.nettoBarnetillegg.ifNull(Kroner(0)).format() },
                                    nynorsk { +data.nettoBarnetillegg.ifNull(Kroner(0)).format() },
                                )
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"Ny reduksjonsprosent" },
                                nynorsk { +"Ny reduksjonsprosent" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +data.reduksjonsprosent.format() + " prosent" },
                                nynorsk { +data.reduksjonsprosent.format() + " prosent" },
                            )
                        }
                    }
                    showIf(data.endringInntektstak) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Nytt inntektstak" },
                                    nynorsk { +"Nytt inntektstak" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +data.inntektstak.format() },
                                    nynorsk { +data.inntektstak.format() },
                                )
                            }
                        }
                    }
                    showIf(data.visOktMinsteIFU) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Ny inntekt før uførhet (IFU)" },
                                    nynorsk { +"Ny inntekt før uførhet (IFU)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +data.ifu.format() },
                                    nynorsk { +data.ifu.format() },
                                )
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"Etterbetaling i juli" },
                                nynorsk { +"Etterbetaling i juli" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +data.etterbetalingJuli.format() },
                                nynorsk { +data.etterbetalingJuli.format() },
                            )
                        }
                    }
                }
            }
            paragraph {
                text(
                    bokmal {
                        +"Du får " + data.totalbelop.format() + " i " + data.tillegg.format(UTOgTilleggMapper)
                        +" per måned før skatt fra 1. juli 2026."
                    },
                    nynorsk {
                        +"Du får " + data.totalbelop.format() + " i " + data.tillegg.format(UTOgTilleggMapper)
                        +" per månad før skatt frå 1. juli 2026."
                    },
                )
            }
            paragraph {
                text(
                    bokmal { +"Uføretrygden blir utbetalt senest den 20. hver måned." },
                    nynorsk { +"Uføretrygda blir utbetalt seinast den 20. kvar månad." },
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

            title1 {
                text(
                    bokmal { +"Dette endres for deg" },
                    nynorsk { +"Dette endres for deg" },
                )
            }

            showIf(data.visOktMinsteIFU) {
                title2 {
                    text(
                        bokmal { +"Økt minste inntekt før uførhet (IFU) " },
                        nynorsk { +"Auka minste inntekt før uførhet (IFU) " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Minste IFU økes, og skal ikke være lavere enn 3,5 ganger grunnbeløpet (G). " },
                        nynorsk { +"Minste IFU aukas, og skal ikkje vere lågare enn 3,5 gonger grunnbeløpet (G). " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Fram til 1. juli i år har vi brukt din gamle IFU i beregningene av uføretrygd. Når lovendringen trer i kraft, skal den ha virkning tilbake i tid fra 1. januar i år. Det kan føre til at du har fått utbetalt for lite uføretrygd. Har du fått for lite utbetalt frem til 1. juli, vil du få etterbetalt i juli. " },
                        nynorsk { +"Fram til 1. juli i år har vi brukt din gamle IFU i berekningane av uføretrygd. Når lovendringa trer i kraft, skal den ha verknad tilbake i tid frå 1. januar i år. Det kan føre til at du har fått utbetalt for lite uføretrygd. Har du fått for lite utbetalt fram til 1. juli, vil du få etterbetalt i juli. " },
                    )
                    list {
                        item {
                            text(
                                bokmal { +"Hvis du får en etterbetaling i juli, vil beløpet stå i tabellen øverst i dette brevet. " },
                                nynorsk { +"Om du får ein etterbetaling i juli, vil beløpet stå i tabellen øverst i dette brevet. " },
                            )
                        }
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Minste IFU bruker vi for å sikre et inntektsgrunnlag for deg som har hatt lite eller ingen inntekt før uførhet. " },
                        nynorsk { +"Minste IFU brukar vi for å sikre eit inntektsgrunnlag for deg som har hatt lite eller ingen inntekt før uførhet. " },
                    )
                }
            }
            showIf(data.visReduksjonsprosent) {
                title2 {
                    text(
                        bokmal { +"Nye regler for reduksjonsprosent " },
                        nynorsk { +"Nye reglar for reduksjonsprosent " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Stortinget har vedtatt at: " },
                        nynorsk { +"Stortinget har vedteke at: " },
                    )
                    list {
                        item {
                            text(
                                bokmal { +"Kompensasjonsgrad endrer navn til reduksjonsprosent. " },
                                nynorsk { +"Kompensasjonsgrad endrar namn til reduksjonsprosent. " },
                            )
                        }
                        item {
                            text(
                                bokmal { +"Vi skal ikke lenger redusere uføretrygden din med mer enn 70 prosent når du har inntekt over inntektsgrensen din. " },
                                nynorsk { +"Vi skal ikkje lenger redusere uføretrygda di med meir enn 70 prosent når du har inntekt over inntektsgrensa di. " },
                            )
                        }
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Slik påvirkes du av endringen: " },
                        nynorsk { +"Slik vert du påverka av endringa: " },
                    )
                    list {
                        item {
                            text(
                                bokmal { +"Du får lavere reduksjonsprosent. Din nye reduksjonsprosent blir " + data.reduksjonsprosent.format() + " prosent. " },
                                nynorsk { +"Du får lågare reduksjonsprosent. Din nye reduksjonsprosent blir " + data.reduksjonsprosent.format() + " prosent. " },
                            )
                        }
                        item {
                            text(
                                bokmal { +"Reduksjonsprosent har bare betydning for deg som har inntekt over inntektsgrensen ved siden av uføretrygden. " },
                                nynorsk { +"Reduksjonsprosent har berre tyding for deg som har inntekt over inntektsgrensa ved sida av uføretrygda. " },
                            )
                        }
                        item {
                            text(
                                bokmal { +"Endringen kan føre til at det blir mer lønnsomt å jobbe ved siden av uføretrygden. " },
                                nynorsk { +"Endringa kan føre til at det blir meir lønsamt å jobbe ved sida av uføretrygda. " },
                            )
                        }
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Du finner mer informasjon om reduksjonsprosent i vedlegget " },
                        nynorsk { +"Du finn meir informasjon om reduksjonsprosent i vedlegget " },
                    )
                    namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                    text(
                        bokmal { +"." },
                        nynorsk { +"." },
                    )
                }

                // TODO: Logikk for når dette skal vises - dersom bruker har inntekt og reduksjonsprosent er endret
                title2 {
                    text(
                        bokmal { +"For deg som har hatt inntekt over inntektsgrensen " },
                        nynorsk { +"For deg som har hatt inntekt over inntektsgrensa " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Fram til 1. juli i år har vi brukt din gamle reduksjonsprosent i beregningene av uføretrygden din. Når lovendringen trer i kraft, skal den ha virkning tilbake i tid fra 1. januar i år. " },
                        nynorsk { +"Fram til 1. juli i år har vi brukt din gamle reduksjonsprosent i berekningane av uføretrygda di. Når lovendringa trer i kraft, skal den ha verknad tilbake i tid frå 1. januar i år. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Det kan føre til at vi har trukket for mye av uføretrygden din, og at du har fått utbetalt for lite uføretrygd. " },
                        nynorsk { +"Det kan føre til at vi har trekt for mykje av uføretrygda di, og at du har fått utbetalt for lite uføretrygd. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Har du fått for lite utbetalt frem til 1. juli, vil du få etterbetalt i juli. Hvis du får en etterbetaling i juli, vil beløpet stå i tabellen øverst i dette brevet. " },
                        nynorsk { +"Har du fått for lite utbetalt fram til 1. juli, vil du få etterbetalt i juli. Om du får ein etterbetaling i juli, vil beløpet stå i tabellen øverst i dette brevet. " },
                    )
                }
            }

            showIf(data.endringNettoBarnetillegg) {
                title1 {
                    text(
                        bokmal { +"Endring i barnetillegg" },
                        nynorsk { +"Endring i barnetillegg" },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Regelverksendringene fører til at du får en endret utbetaling av uføretrygd. Uføretrygden regnes med som inntekt når vi beregner barnetillegg. " +
                                    "Derfor får du en endret utbetaling av barnetillegg. Ny beregning av barnetillegg (før skatt) er " + data.nettoBarnetillegg.ifNull(Kroner(0)).format() + "."
                        },
                        nynorsk {
                            +"Regelverksendringane fører til at du får ei endra utbetaling av uføretrygd. Uføretrygda vert rekna med som inntekt når vi reknar ut barnetillegg. " +
                                    "Difor får du ei endra utbetaling av barnetillegg. Ny berekning av barnetillegg (før skatt) er " + data.nettoBarnetillegg.ifNull(Kroner(0)).format() + "."
                        },
                    )
                }
            }
            paragraph {
                text(
                    bokmal { +"Vedtaket har vi gjort etter " + data.hjemler.format(HjemmelFormatter(true)) + "." },
                    nynorsk { +"Vedtaket har vi gjort etter " + data.hjemler.format(HjemmelFormatter(true)) + "." },
                )
            }

            title1 {
                text(
                    bokmal { +"Du har rett til å klage" },
                    nynorsk { +"Du har rett til å klage" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen vedtaket har kommet fram til deg. Du finner skjema og informasjon på " +
                            "${Constants.KLAGE_URL}." },
                    nynorsk { +"Om du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen vedtaket har kome fram til deg. Du finn skjema og informasjon på " +
                            "${Constants.KLAGE_URL}." },
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
