package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Beregning
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Gjenlevendetillegg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.TrygdetidAvdoed
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.beregning.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.gjenlevendetillegg.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.trygdetidAvdoed.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

/**
 * Portert fra legacy TBUxx1V. Tabell med opplysninger om avdoede som ligger til grunn for
 * gjenlevendetillegget. Inkluderes naar [visAvdoedOpplysningerTabell] (gating i vedlegget).
 * Tomme/0-verdier representeres som null i DTO-en (radene gates med ifNotNull).
 */
data class TBUxx1V(
    val beregning: Expression<Beregning>,
    val gjenlevendetillegg: Expression<Gjenlevendetillegg>,
    val trygdetidAvdoed: Expression<TrygdetidAvdoed>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                bokmal { + "Opplysninger om avdøde som ligger til grunn for beregningen av gjenlevendetillegget ditt fra " + beregning.beregningVirkningDatoFom.format() },
                nynorsk { + "I berekninga av attlevandetillegget har vi brukt desse opplysningane om den avdøde frå " + beregning.beregningVirkningDatoFom.format() },
            )
        }
        paragraph {
            text(
                bokmal { + "Folketrygdens grunnbeløp (G) benyttet i beregningen er " + beregning.grunnbeloep.format() + "." },
                nynorsk { + "Folketrygdas grunnbeløp (G) nytta i berekninga er " + beregning.grunnbeloep.format() + "." },
            )
        }

        paragraph {
            table(
                header = {
                    column(4) {
                        text(bokmal { + "Opplysning" }, nynorsk { + "Opplysning" })
                    }
                    column(columnSpan = 1, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {}
                }
            ) {
                row {
                    cell {
                        text(bokmal { + "Avdødes fødselsnummer" }, nynorsk { + "Avdødes fødselsnummer" })
                    }
                    cell {
                        text(bokmal { + gjenlevendetillegg.avdoedFoedselsnummer }, nynorsk { + gjenlevendetillegg.avdoedFoedselsnummer })
                    }
                }

                ifNotNull(gjenlevendetillegg.ufoeretidspunkt) { uforetidspunkt ->
                    row {
                        cell { text(bokmal { + "Uføretidspunkt" }, nynorsk { + "Uføretidspunkt" }) }
                        cell { text(bokmal { + uforetidspunkt.format() }, nynorsk { + uforetidspunkt.format() }) }
                    }
                }

                ifNotNull(gjenlevendetillegg.beregningsgrunnlagAvdoedAarsbeloep) { aarsbeloep ->
                    row {
                        cell { text(bokmal { + "Beregningsgrunnlag" }, nynorsk { + "Berekningsgrunnlag" }) }
                        cell { text(bokmal { + aarsbeloep.format(false) + " kr" }, nynorsk { + aarsbeloep.format(false) + " kr" }) }
                    }
                }

                row {
                    cell { text(bokmal { + "Uføregrad" }, nynorsk { + "Uføregrad" }) }
                    cell { text(bokmal { + "100 %" }, nynorsk { + "100 %" }) }
                }

                showIf(gjenlevendetillegg.avdoedErFlyktning) {
                    row {
                        cell {
                            text(
                                bokmal { + "Avdøde var innvilget flyktningstatus fra UDI" },
                                nynorsk { + "Avdøde var innvilga flyktningstatus frå UDI" },
                            )
                        }
                        cell { text(bokmal { + "Ja" }, nynorsk { + "Ja" }) }
                    }
                }.orShow {
                    ifNotNull(gjenlevendetillegg.anvendtTrygdetid) { anvendtTrygdetid ->
                        row {
                            cell { text(bokmal { + "Trygdetid" }, nynorsk { + "Trygdetid" }) }
                            cell { text(bokmal { + anvendtTrygdetid.format() + " år" }, nynorsk { + anvendtTrygdetid.format() + " år" }) }
                        }
                    }
                }

                showIf(gjenlevendetillegg.minsteytelseBenyttetUngUfoer) {
                    row {
                        cell { text(bokmal { + "Ung ufør" }, nynorsk { + "Ung ufør" }) }
                        cell { text(bokmal { + "Ja" }, nynorsk { + "Ja" }) }
                    }
                }

                ifNotNull(gjenlevendetillegg.yrkesskadegrad) { yrkesskadegrad ->
                    row {
                        cell { text(bokmal { + "Yrkesskadegrad" }, nynorsk { + "Yrkesskadegrad" }) }
                        cell { text(bokmal { + yrkesskadegrad.format() + " %" }, nynorsk { + yrkesskadegrad.format() + " %" }) }
                    }
                }

                ifNotNull(gjenlevendetillegg.yrkesskadeAarsbeloep) { yrkesskadeAarsbeloep ->
                    row {
                        cell { text(bokmal { + "Beregningsgrunnlag yrkesskade" }, nynorsk { + "Beregningsgrunnlag yrkesskade" }) }
                        cell { text(bokmal { + yrkesskadeAarsbeloep.format(false) + " kr" }, nynorsk { + yrkesskadeAarsbeloep.format(false) + " kr" }) }
                    }
                }

                ifNotNull(gjenlevendetillegg.inntektVedSkadetidspunkt) { inntekt ->
                    row {
                        cell { text(bokmal { + "Årlig arbeidsinntekt på skadetidspunktet" }, nynorsk { + "Årleg arbeidsinntekt på skadetidspunktet" }) }
                        cell { text(bokmal { + inntekt.format(false) + " kr" }, nynorsk { + inntekt.format(false) + " kr" }) }
                    }
                }

                ifNotNull(trygdetidAvdoed.faktiskTTNorge) { fattNorge ->
                    row {
                        cell { text(bokmal { + "Faktisk trygdetid i Norge" }, nynorsk { + "Faktisk trygdetid i Noreg" }) }
                        cell { text(bokmal { + fattNorge.format() + " måneder" }, nynorsk { + fattNorge.format() + " månader" }) }
                    }
                }

                ifNotNull(trygdetidAvdoed.faktiskTTEOS) { fattEOS ->
                    row {
                        cell { text(bokmal { + "Faktisk trygdetid i andre EØS-land" }, nynorsk { + "Faktisk trygdetid i andre EØS-land" }) }
                        cell { text(bokmal { + fattEOS.format() + " måneder" }, nynorsk { + fattEOS.format() + " månader" }) }
                    }
                }

                ifNotNull(trygdetidAvdoed.framtidigTTEOS) { framtidigEOS ->
                    row {
                        cell { text(bokmal { + "Framtidig trygdetid i Norge og andre EØS-land" }, nynorsk { + "Framtidig trygdetid i Noreg og andre EØS-land" }) }
                        cell { text(bokmal { + framtidigEOS.format() + " måneder" }, nynorsk { + framtidigEOS.format() + " månader" }) }
                    }
                }

                ifNotNull(trygdetidAvdoed.fattNorgePlussFattEOSAvdoed) { sum ->
                    row {
                        cell { text(bokmal { + "Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)" }, nynorsk { + "Faktisk trygdetid i Noreg og avtaleland (maksimalt 40 år)" }) }
                        cell { text(bokmal { + sum.format() + " måneder" }, nynorsk { + sum.format() + " månader" }) }
                    }
                }

                ifNotNull(trygdetidAvdoed.tellerTTEOS, trygdetidAvdoed.nevnerTTEOS) { teller, nevner ->
                    row {
                        cell { text(bokmal { + "Forholdstallet brukt ved beregning av trygdetid" }, nynorsk { + "Forholdstalet brukt ved berekning av trygdetid" }) }
                        cell { text(bokmal { + teller.format() + "/" + nevner.format() }, nynorsk { + teller.format() + "/" + nevner.format() }) }
                    }
                }

                ifNotNull(trygdetidAvdoed.faktiskTTNordisk) { ttNordisk ->
                    row {
                        cell { text(bokmal { + "Faktisk trygdetid i annet nordisk land som beregner framtidig trygdetid" }, nynorsk { + "Faktisk trygdetid i anna nordisk land som blir brukt ved berekning av framtidig trygdetid" }) }
                        cell { text(bokmal { + ttNordisk.format() + " måneder" }, nynorsk { + ttNordisk.format() + " månader" }) }
                    }
                }

                showIf(trygdetidAvdoed.framtidigTrygdetidUnder40Aar) {
                    ifNotNull(trygdetidAvdoed.framtidigTTNorsk) { framtidigNorsk ->
                        row {
                            cell { text(bokmal { + "Norsk framtidig trygdetid" }, nynorsk { + "Norsk framtidig trygdetid" }) }
                            cell { text(bokmal { + framtidigNorsk.format() + " måneder" }, nynorsk { + framtidigNorsk.format() + " månader" }) }
                        }
                    }
                }

                ifNotNull(trygdetidAvdoed.tellerTTNordisk, trygdetidAvdoed.nevnerTTNordisk) { teller, nevner ->
                    row {
                        cell { text(bokmal { + "Forholdstallet brukt ved avkorting av norsk framtidig trygdetid" }, nynorsk { + "Forholdstalet brukt ved reduksjon av norsk framtidig trygdetid" }) }
                        cell { text(bokmal { + teller.format() + "/" + nevner.format() }, nynorsk { + teller.format() + "/" + nevner.format() }) }
                    }
                }

                ifNotNull(trygdetidAvdoed.fattNorgePlussFattA10NettoAvdoed) { sum ->
                    row {
                        cell { text(bokmal { + "Samlet trygdetid brukt ved beregning av uføretrygd etter avkorting av framtidig tid" }, nynorsk { + "Samla trygdetid brukt ved berekning av uføretrygd etter reduksjon av framtidig tid" }) }
                        cell { text(bokmal { + sum.format() + " måneder" }, nynorsk { + sum.format() + " månader" }) }
                    }
                }

                ifNotNull(trygdetidAvdoed.faktiskTTBilateral) { fattBilateral ->
                    row {
                        cell { text(bokmal { + "Faktisk trygdetid i andre avtaleland" }, nynorsk { + "Faktisk trygdetid i andre avtaleland" }) }
                        cell { text(bokmal { + fattBilateral.format() + " måneder" }, nynorsk { + fattBilateral.format() + " månader" }) }
                    }
                }

                ifNotNull(trygdetidAvdoed.framtidigTTAvtaleland) { framtidigAvtaleland ->
                    row {
                        cell { text(bokmal { + "Framtidig trygdetid" }, nynorsk { + "Framtidig trygdetid" }) }
                        cell { text(bokmal { + framtidigAvtaleland.format() + " måneder" }, nynorsk { + framtidigAvtaleland.format() + " månader" }) }
                    }
                }

                ifNotNull(trygdetidAvdoed.fattNorgePlussFattBilateralAvdoed) { sum ->
                    row {
                        cell { text(bokmal { + "Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)" }, nynorsk { + "Faktisk trygdetid i Noreg og avtaleland (maksimalt 40 år)" }) }
                        cell { text(bokmal { + sum.format() + " måneder" }, nynorsk { + sum.format() + " månader" }) }
                    }
                }

                ifNotNull(trygdetidAvdoed.tellerTTBilateral, trygdetidAvdoed.nevnerTTBilateral) { teller, nevner ->
                    row {
                        cell { text(bokmal { + "Forholdstallet brukt ved beregning av uføretrygd" }, nynorsk { + "Forholdstalet brukt ved berekning av uføretrygd" }) }
                        cell { text(bokmal { + teller.format() + "/" + nevner.format() }, nynorsk { + teller.format() + "/" + nevner.format() }) }
                    }
                }
            }
        }
    }
}
