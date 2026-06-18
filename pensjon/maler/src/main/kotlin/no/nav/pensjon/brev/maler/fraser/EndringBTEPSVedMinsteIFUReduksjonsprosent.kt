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
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

object EndringBTEPSVedMinsteIFUReduksjonsprosent {

    data class Brevdata(
        val nettoUforetrygdUtenTillegg: Expression<Kroner>,
        val nettoBarnetilleggFB: Expression<Kroner>,
        val nettoBarnetilleggSB: Expression<Kroner>,
        val totalbelop: Expression<Kroner>,
        val samletInntektsgrenseBarnetillegg: Expression<Kroner>,
        val fribelop: Expression<Kroner>,

        val barnetilleggSB: Expression<Boolean>,
        val opphortUforetrygdEllerBTFB: Expression<Boolean>,
    )

    data class Outline(val data: Brevdata) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

            showIf(not(data.opphortUforetrygdEllerBTFB)) {
                paragraph {
                    text(
                        bokmal { +"Vi har endret barnetillegget i uføretrygden din. " },
                        nynorsk { +"Vi har endra barnetillegget i uføretrygda di. " },
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
                        showIf(data.barnetilleggSB) {
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
            }

            showIf(data.opphortUforetrygdEllerBTFB) {
                paragraph {
                    text(
                        bokmal { +"Vi endrer barnetillegget i uføretrygden din på grunn av lovendringer Stortinget har vedtatt. Endringene trer i kraft 1. juli 2026, men gjelder fra 1. januar 2026. " },
                        nynorsk { +"Vi endrar barnetillegget i uføretrygda di på grunn av lovendringar Stortinget har vedteke. Endringane trer i kraft 1. juli 2026, men gjeld frå 1. januar 2026. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du har fått barnetillegg for fellesbarn fordi du bor sammen med barnets andre forelder. For fellesbarn, bruker vi begge foreldrenes inntekt (inkludert uføretrygd) når vi beregner størrelsen på barnetillegg. Lovendringen har ført til en endring i den andre forelderens uføretrygd. Dette påvirker bare barnetillegg for fellesbarn, og ikke uføretrygden din. " },
                        nynorsk { +"Du har fått barnetillegg for fellesbarn fordi du bur saman med barnets andre forelder. For fellesbarn, brukar vi begge foreldrenes inntekt (inkludert uføretrygd) når vi bereknar storleiken på barnetillegg. Lovendringa har ført til ein endring i den andre forelderens uføretrygd. Dette påverkar berre barnetillegg for fellesbarn, og ikkje uføretrygda di. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Dersom du også mottar barnetillegg for et barn som ikke bor med begge foreldre, påvirkes ikke dette av lovendringene. " },
                        nynorsk { +"Dersom du også mottar barnetillegg for eit barn som ikkje bur med begge foreldre, påverkas ikkje dette av lovendringane. " },
                    )
                }
            }.orShow {
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
                showIf(data.barnetilleggSB) {
                    paragraph {
                        text(
                            bokmal { +"Barnetillegg du mottar for barn som ikke bor med begge foreldre, påvirkes ikke av lovendringene. " },
                            nynorsk { +"Barnetillegg du mottar for barn som ikkje bur med begge foreldre, påverkas ikkje av lovendringane. " },
                        )
                    }
                }
            }

            title1 {
                text(
                    bokmal { +"Endring i barnetillegg" },
                    nynorsk { +"Endring i barnetillegg" },
                )
            }

            showIf(data.opphortUforetrygdEllerBTFB) {
                paragraph {
                    text(
                        bokmal { +"Regelverksendringene fører til at barnetillegg for fellesbarn har vært for høyt i perioden du har mottatt barnetillegg i år. For mye utbetalt i denne perioden vil bli regulert i etteroppgjøret neste år. " },
                        nynorsk { +"Regelverksendringane fører til at barnetillegg for fellesbarn har vore for høgt i perioden du har mottatt barnetillegg i år. For mykje utbetalt i denne perioden vil bli regulert i etteroppgjeret neste år. " },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"Regelverksendringene fører til at beregnet barnetillegg for fellesbarn i én eller flere perioder blir endret, fordi den samlede inntekten til deg og barnets andre forelder er høyere enn fribeløpet på " + data.fribelop.format() + ". " },
                        nynorsk { +"Regelverksendringane fører til at berekna barnetillegg for fellesbarn i éin eller fleire periodar blir endra, fordi den samla inntekta til deg og barnets andre forelder er høgare enn fribeløpet på " + data.fribelop.format() + ". " },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Ny beregning av barnetillegg fra 1. juli (før skatt) er " + data.nettoBarnetilleggFB.format() + ". " },
                        nynorsk { +"Ny berekning av barnetillegg frå 1. juli (før skatt) er " + data.nettoBarnetilleggFB.format() + ". " },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Dersom du i perioden 1. januar til 1. juli 2026, har fått for mye utbetalt barnetillegg, vil dette bli regulert i etteroppgjøret neste år. " },
                        nynorsk { +"Dersom du i perioden 1. januar til 1. juli 2026, har fått for mykje utbetalt barnetillegg, vil dette bli regulert i etteroppgjeret neste år. " },
                    )
                }
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

            showIf(not(data.opphortUforetrygdEllerBTFB)) {
                includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            }
            includePhrase(Ufoeretrygd.Etteroppgjor)
            includePhrase(Ufoeretrygd.RettTilAKlage)
            includePhrase(Ufoeretrygd.RettTilInnsyn)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
    }
}
