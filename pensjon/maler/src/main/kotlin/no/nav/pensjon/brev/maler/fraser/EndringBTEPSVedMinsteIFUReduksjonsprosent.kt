package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

object EndringBTEPSVedMinsteIFUReduksjonsprosent {

    data class Brevdata(
        val nettoUforetrygdUtenTillegg: Expression<Kroner>,
        val nettoBarnetilleggFB: Expression<Kroner>,
        val nettoBarnetilleggSB: Expression<Kroner>,
        val barnetilleggSB: Expression<Boolean>,
        val totalbelop: Expression<Kroner>,

        val samletInntektsgrenseBarnetillegg: Expression<Kroner>,
        val fribelop: Expression<Kroner>,

        val epsBokmalTxt: Expression<String>,
        val epsNynorskTxt: Expression<String>,
    )

    data class Outline(val data: Brevdata) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

            paragraph {
                text(
                    bokmal { +"Vi har endret barnetillegg i uføretrygden din. " },
                    nynorsk { +"Vi har endra barnetillegg i uføretrygda di. " },
                )
                table(header = {
                    column { text(bokmal { +"Du får per måned før skatt:" }, nynorsk { +"Du får per månad før skatt:" }) }
                    column(alignment = RIGHT) { text(bokmal { +"Kroner" }, nynorsk { +"Kroner" }) }
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
                                bokmal { +data.nettoUforetrygdUtenTillegg.format(false) },
                                nynorsk { +data.nettoUforetrygdUtenTillegg.format(false) },
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"Barnetillegg fellesbarn" },
                                nynorsk { +"Barnetillegg fellesbarn" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +data.nettoBarnetilleggFB.format(false) },
                                nynorsk { +data.nettoBarnetilleggFB.format(false) },
                            )
                        }
                    }
                    showIf (data.barnetilleggSB) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Barnetillegg særkullsbarn" },
                                    nynorsk { +"Barnetillegg særkullsbarn" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +data.nettoBarnetilleggSB.format(false) },
                                    nynorsk { +data.nettoBarnetilleggSB.format(false) },
                                )
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"Totalt" },
                                nynorsk { +"Totalt" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +data.totalbelop.format(false) },
                                nynorsk { +data.totalbelop.format(false) },
                            )
                        }
                    }
                }
            }

            paragraph {
                text(
                    bokmal { +"Uføretrygden blir fortsatt utbetalt senest den 20. hver måned." },
                    nynorsk { +"Uføretrygda blir fortsatt utbetalt seinast den 20. kvar månad." },
                )
            }

            title1 {
                text(
                    bokmal { +"Derfor har vi endret barnetillegget ditt " },
                    nynorsk { +"Derfor har vi endra barnetillegget ditt " },
                )
            }

            paragraph {
                text(
                    bokmal { +"Vi endrer barnetillegget i uføretrygden din på grunn av lovendringer Stortinget har vedtatt. Endringene trer i kraft 1. juli 2026, men gjelder fra 1. januar 2026. " },
                    nynorsk { +"Vi endrar barnetillegget i uføretrygda di på grunn av lovendringar Stortinget har vedteke. Endringane trer i kraft 1. juli 2026, men gjeld frå 1. januar 2026. " },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du får barnetillegg for fellesbarn fordi du bor sammen med barnets andre forelder. For fellesbarn bruker vi begge foreldrenes inntekt (inkludert uføretrygd) når vi beregner størrelsen på barnetillegg. Lovendringen har ført til en endring i den andre forelderens uføretrygd. Dette påvirker bare barnetillegg for fellesbarn, og ikke uføretrygden din. " },
                    nynorsk { +"Du får barnetillegg for fellesbarn fordi du bur saman med barnets andre forelder. For fellesbarn brukar vi begge foreldrenes inntekt (inkludert uføretrygd) når vi reknar ut storleiken på barnetillegg. Lovendringa har ført til ei endring i den andre forelderens uføretrygd. Dette påverkar berre barnetillegg for fellesbarn, og ikkje uføretrygda di. " },
                )
            }
            showIf (data.barnetilleggSB) {
                paragraph {
                    text(
                        bokmal { +"Barnetillegg du mottar for barn som ikke bor med begge foreldre, påvirkes ikke av lovendringene. " },
                        nynorsk { +"Barnetillegg du mottar for barn som ikkje bur med begge foreldre, påverkas ikkje av lovendringane. " },
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"Endring i barnetillegg" },
                    nynorsk { +"Endring i barnetillegg" },
                )
            }

            showIf (data.nettoBarnetilleggFB.equalTo(Kroner(0))) {
                paragraph {
                    text(
                        bokmal { +"Regelverksendringene fører til at barnetillegg for fellesbarn ikke blir utbetalt fordi den samlede inntekten til deg og barnets andre forelder er høyere enn " + data.samletInntektsgrenseBarnetillegg.format() + ", som er grensen for å få utbetalt barnetillegg. Får dere lavere inntekt i fremtiden, kan du få utbetalt barnetillegg igjen. " },
                        nynorsk { +"Regelverksendringane fører til at barnetillegg for fellesbarn ikkje blir utbetalt fordi den samla inntekta til deg og barnets andre forelder er høgare enn " + data.samletInntektsgrenseBarnetillegg.format() + ", som er grensa for å få utbetalt barnetillegg. Får dere lågare inntekt i framtida, kan du få utbetalt barnetillegg igjen. " },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"Regelverksendringene fører til at barnetillegg for fellesbarn blir redusert til " + data.nettoBarnetilleggFB.format() + " per måned, fordi den samlede inntekten til deg og barnets andre forelder er høyere enn fribeløpet på " + data.fribelop.format() + ". " },
                        nynorsk { +"Regelverksendringane fører til at barnetillegg for fellesbarn blir redusert til " + data.nettoBarnetilleggFB.format() + " per månad, fordi den samla inntekta til deg og barnets andre forelder er høgare enn fribeløpet på " + data.fribelop.format() + ". " },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Ny beregning av barnetillegg fra 1. juli (før skatt) er " + data.nettoBarnetilleggFB.format() + ". " },
                        nynorsk { +"Ny berekning av barnetillegg frå 1. juli (før skatt) er " + data.nettoBarnetilleggFB.format() + ". " },
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"Dersom du i perioden 1. januar til 1. juli 2026, har fått for mye utbetalt barnetillegg, vil dette bli regulert i etteroppgjøret neste år. " },
                    nynorsk { +"Dersom du i perioden 1. januar til 1. juli 2026, har fått for mykje utbetalt barnetillegg, vil dette bli regulert i etteroppgjeret neste år. " },
                )
            }

            paragraph {
                text(
                    bokmal { +"Du kan lese mer om dette i vedlegget " },
                    nynorsk { +"Du kan lese meir om dette i vedlegget " },
                )
                namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                text(
                    bokmal { +"." },
                    nynorsk { +"." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Vedtaket har vi gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12. " },
                    nynorsk { +"Vedtaket har vi gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12. " },
                )
            }

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Ufoeretrygd.Etteroppgjor)
            includePhrase(Ufoeretrygd.RettTilAKlage)
            includePhrase(Ufoeretrygd.RettTilInnsyn)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
    }
}
