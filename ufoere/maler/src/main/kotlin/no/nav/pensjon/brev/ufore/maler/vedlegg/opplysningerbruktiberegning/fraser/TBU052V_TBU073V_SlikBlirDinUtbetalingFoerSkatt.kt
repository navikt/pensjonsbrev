package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Avkortning
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Beregning
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Visningsflagg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.avkortning.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.beregning.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

/**
 * Portert fra legacy TBU052V_TBU073V_SlikBlirDinUtbetalingFoerSkatt. Tabell og forklaringer for
 * utbetaling før skatt ved endret inntekt. Brevkode-/kravårsak-/beløpsbetingelsene er trukket ut
 * til [Visningsflagg]-flaggene.
 */
data class TBU052V_TBU073V_SlikBlirDinUtbetalingFoerSkatt(
    val flagg: Expression<Visningsflagg>,
    val beregning: Expression<Beregning>,
    val avkortning: Expression<Avkortning>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(flagg.visUtbetalingFoerSkattTabell) {
            title1 {
                text(
                    bokmal { + "Slik blir din utbetaling før skatt" },
                    nynorsk { + "Slik blir utbetalinga di før skatt" },
                )
            }
            paragraph {
                table(
                    header = {
                        column(columnSpan = 3) {
                            text(bokmal { + "Utbetaling før skatt" }, nynorsk { + "Utbetalinga før skatt" })
                        }
                        column(columnSpan = 1, ColumnAlignment.RIGHT) {
                            text(bokmal { + "" }, nynorsk { + "" })
                        }
                    }
                ) {
                    row {
                        cell {
                            text(
                                bokmal { + "Brutto beregnet uføretrygd som følge av innmeldt inntekt" },
                                nynorsk { + "Brutto berekna uføretrygd som følgje av innmeld inntekt" },
                            )
                        }
                        cell {
                            ifNotNull(avkortning.nettoAkkPlussNettoRestAar) { brutto ->
                                text(bokmal { + brutto.format(false) + " kr" }, nynorsk { + brutto.format(false) + " kr" })
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "- Utbetalt uføretrygd hittil i år" },
                                nynorsk { + "- Utbetalt uføretrygd hittil i år" },
                            )
                        }
                        cell {
                            ifNotNull(avkortning.nettoAkk) { nettoAkk ->
                                text(bokmal { + nettoAkk.format(false) + " kr" }, nynorsk { + nettoAkk.format(false) + " kr" })
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "= Utbetaling av uføretrygd for resterende måneder i året" },
                                nynorsk { + "= Utbetaling av uføretrygd for resterande månader i året" },
                            )
                        }
                        cell {
                            ifNotNull(avkortning.nettoRestAar) { nettoRest ->
                                text(bokmal { + nettoRest.format(false) + " kr" }, nynorsk { + nettoRest.format(false) + " kr" })
                            }
                        }
                    }
                }
            }
        }.orShowIf(flagg.visUtbetalingFoerSkattTittelKun) {
            title1 {
                text(
                    bokmal { + "Slik blir din utbetaling før skatt" },
                    nynorsk { + "Slik blir utbetalinga di før skatt" },
                )
            }
        }

        showIf(flagg.visMaanedligReduksjon) {
            ifNotNull(avkortning.fradrag) { fradrag ->
                paragraph {
                    text(
                        bokmal { + "Du vil få en månedlig reduksjon i uføretrygden din på " + fradrag.format() + " i resterende måneder i kalenderåret." },
                        nynorsk { + "Du får ein månadleg reduksjon i uføretrygda di på " + fradrag.format() + " i resterande månader i kalenderåret." },
                    )
                }
            }
        }

        showIf(flagg.visUforetrygdInntektSum) {
            ifNotNull(avkortning.nettoAkkPlussNettoRestAarPlussForventetInntekt) { sum ->
                paragraph {
                    text(
                        bokmal { + "Uføretrygden og inntekten din vil ut fra dette til sammen utgjøre " + sum.format() + " for dette året." },
                        nynorsk { + "Uføretrygda di og inntekta di utgjer til saman " + sum.format() + " i dette året." },
                    )
                }
            }
        }

        showIf(flagg.visUtbetalingRedusertIkkeUtbetalt) {
            paragraph {
                text(
                    bokmal { + "Utbetalingen av uføretrygden din er redusert, fordi du har inntekt. Den innmeldte inntekten er høyere enn inntektsgrensen din på " + avkortning.inntektsgrenseAar.format() + " og uføretrygden blir derfor ikke utbetalt. " },
                    nynorsk { + "Utbetalinga av uføretrygda di er redusert, fordi du har inntekt. Den innmelde inntekta er høgare enn inntektsgrensa di på " + avkortning.inntektsgrenseAar.format() + " og uføretrygda blir derfor ikkje utbetalt." },
                )
            }
        }

        showIf(flagg.visIkkeUtbetaltOver80Prosent) {
            paragraph {
                text(
                    bokmal { + "Du får ikke utbetalt uføretrygd siden inntekten din er høyere enn 80 prosent av inntekten du hadde før du ble ufør, det vil si " + avkortning.inntektstak.format() + "." },
                    nynorsk { + "Du får ikkje utbetalt uføretrygd fordi inntekta di er høgare enn 80 prosent av inntekta du hadde før du blei ufør, det vil si " + avkortning.inntektstak.format() + "." },
                )
            }
        }

        showIf(flagg.visFaarTilbakeUtenSoknad) {
            paragraph {
                text(
                    bokmal { + "Du vil få tilbake " + beregning.ufoeregrad.format() + " prosent uføretrygd uten søknad, dersom du tjener mindre enn inntektsgrensen din. Hvis du allerede har fått utbetalt det du har rett til i uføretrygd for kalenderåret, vil du ikke få utbetalt uføretrygd med den opprinnelige uføregraden din før neste kalenderår." },
                    nynorsk { + "Du får tilbake " + beregning.ufoeregrad.format() + " prosent uføretrygd utan søknad dersom du tener mindre enn inntektsgrensa di. Dersom du allereie har fått utbetalt det du har rett til i uføretrygd for kalenderåret, får du ikkje utbetalt uføretrygd med den opphavlege uføregraden din før neste kalenderår." },
                )
            }
        }
    }
}
