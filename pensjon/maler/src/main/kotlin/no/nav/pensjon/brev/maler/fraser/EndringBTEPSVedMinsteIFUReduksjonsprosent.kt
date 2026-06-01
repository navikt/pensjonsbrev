package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfore
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

object EndringBTEPSVedMinsteIFUReduksjonsprosent {

    data class Brevdata(
        val nettoUforetrygdUtenTillegg: Expression<Kroner>,
        val nettoBarnetilleggFB: Expression<Kroner>,
        val totalbelop: Expression<Kroner>,

        val inntektBruker: Expression<Kroner>,
        val inntektEPS: Expression<Kroner>,

        val redusertBarnetillegg: Expression<Boolean>,
        val nullBarnetillegg: Expression<Boolean>,

        val gInntil: Expression<Kroner>,
        val samletInntekt: Expression<Kroner>,
        val grenseUtbetaltBarnetillegg: Expression<Kroner>,
        val nyttBarnetillegg: Expression<Kroner>,
        val fribelop: Expression<Kroner>,
        val arligUtbetalingBarnetilleggFB: Expression<Kroner>,
        val utbetaltBarnetilleggHittilIAr: Expression<Kroner>,
        val utbetalingBarnetilleggResten: Expression<Kroner>,
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
            paragraph {
                text(
                    bokmal { +"Dersom du også mottar barnetillegg for et barn som ikke bor med begge foreldre, påvirkes ikke dette av lovendringene. " },
                    nynorsk { +"Dersom du også mottar barnetillegg for eit barn som ikkje bur med begge foreldre, påverkes ikkje dette av lovendringane. " },
                )
            }

            title1 {
                text(
                    bokmal { +"Slik har vi beregnet barnetillegg" },
                    nynorsk { +"Slik har vi berekna barnetillegg" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Vi har beregnet barnetillegg på nytt ut fra inntekten din på " + data.inntektBruker.format() + " og inntekten til din EPS på " + data.inntektEPS.format() + ". " +
                            "Folketrygdens grunnbeløp på inntil " + data.gInntil.format() + " er holdt utenfor den andre forelderens inntekt. Til sammen utgjør disse inntektene " + data.samletInntekt.format() + " . " },
                    nynorsk { +"Vi har berekna barnetillegg på nytt ut fra inntekta di på " + data.inntektBruker.format() + " og inntekta til din EPS på " + data.inntektEPS.format() + ". " +
                            "Folketrygdas grunnbeløp på inntil " + data.gInntil.format() + " er halde utanfor den andre forelderen sin inntekt. Til saman utgjer desse inntektene " + data.samletInntekt.format() + " . " },
                )
            }

            showIf (data.nullBarnetillegg) {
                paragraph {
                    text(
                        bokmal { +"Barnetillegg for fellesbarn blir ikke utbetalt fordi den samlede inntekten til deg og barnets andre forelder er høyere enn " + data.grenseUtbetaltBarnetillegg.format() +", som er grensen for å få utbetalt barnetillegg. Får dere lavere inntekt i fremtiden, kan du få utbetalt barnetillegg igjen. " },
                        nynorsk { +"Barnetillegg for fellesbarn blir ikkje utbetalt fordi den samla inntekta til deg og barnets andre forelder er høgare enn " + data.grenseUtbetaltBarnetillegg.format() +", som er grensa for å få utbetalt barnetillegg. Får dere lågare inntekt i framtida, kan du få utbetalt barnetillegg igjen. " },
                    )
                }
            }.orShowIf(data.redusertBarnetillegg) {
                paragraph {
                    text(
                        bokmal { +"Barnetillegg for fellesbarn blir redusert til " + data.nyttBarnetillegg.format() + " per måned, fordi den samlede inntekten til deg og barnets andre forelder er høyere enn fribeløpet på " + data.fribelop.format() + ". " },
                        nynorsk { +"Barnetillegg for fellesbarn blir redusert til " + data.nyttBarnetillegg.format() + " per månad, fordi den samla inntekta til deg og barnets andre forelder er høgare enn fribeløpet på " + data.fribelop.format() + ". " },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Du har rett til en årlig utbetaling av barnetillegg for fellesbarn på " + data.nettoBarnetilleggFB.format() + ". Siden du allerede har fått utbetalt " + data.utbetaltBarnetilleggHittilIAr.format() + " i barnetillegg hittil i år, har du rett til utbetaling på " + data.utbetalingBarnetilleggResten.format() + " resten av året. Derfor får du " + data.nyttBarnetillegg.format() + " i måneden før skatt i barnetillegg resten av året. " },
                        nynorsk { +"Du har rett til ei årleg utbetaling av barnetillegg for fellesbarn på " + data.nettoBarnetilleggFB.format() + ". Sidan du allereie har fått utbetalt " + data.utbetaltBarnetilleggHittilIAr.format() + " i barnetillegg hittil i år, har du rett til utbetaling på " + data.utbetalingBarnetilleggResten.format() + " resten av året. Derfor får du " + data.nyttBarnetillegg.format() + " i månaden før skatt i barnetillegg resten av året. " },
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

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Ufoeretrygd.Etteroppgjor)
            includePhrase(Ufoeretrygd.RettTilAKlage(vedleggDineRettigheterOgPlikterUfore))
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
    }
}
