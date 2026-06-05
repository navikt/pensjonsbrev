package no.nav.pensjon.brev.maler.legacy.vedlegg

import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto.Beregningsmetode
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto.Poengtallstype.FRAMTIDIG_POENG
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto.Poengtallstype.OMSORGSPOENG_K
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto.Poengtallstype.OMSORGSPOENG_L
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.AvdoedSelectors.avtaleland
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.AvdoedSelectors.doedsdato
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.AvdoedSelectors.doedsfallSkyldesYrkesskade
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.AvdoedSelectors.flyktning as avdoedFlyktning
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.AvdoedSelectors.fnr
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.AvdoedSelectors.skadetidspunktYrkesskade
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.AvdoedSelectors.trygdetidsgrunnlagBilateral
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.AvdoedSelectors.trygdetidsgrunnlagEos
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.AvdoedSelectors.trygdetidsgrunnlagNorge
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.AvdoedSelectors.ttNordiskAar
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.AvdoedSelectors.ttNordiskMnd
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.AvdoedSelectors.ungUfoer
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.BeregningSelectors.beregningsmetode
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.BeregningSelectors.poengrekke
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.BeregningSelectors.sluttpoengtall
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.BeregningSelectors.tpInnvilget
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.BeregningSelectors.trygdetid
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.BeregningSelectors.ttAnv
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.BeregningSelectors.ttAnvBest
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.BeregningSelectors.yrke
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.BeregningSelectors.yug
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.BrukerSelectors.flyktning as brukerFlyktning
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.BrukerSelectors.forventetInntekt
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PesysDataSelectors.avdoed
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PesysDataSelectors.beregning
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PesysDataSelectors.bruker
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PesysDataSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengAarSelectors.aarstall
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengAarSelectors.grunnbeloepVeiet
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengAarSelectors.pensjonsgivendeInntekt
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengAarSelectors.pensjonspoeng
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengAarSelectors.poengtallstype
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.aar
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.framtidigPoengaarNordenBrutto
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.framtidigPoengaarNordenNetto
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.framtidigPoengtall
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.poengaarNevnerEos
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.poengaarTellerEos
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.poengaarUtenOk
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.poengaarUtenOkE91
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.poengaarUtenOkF92
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.poengaarUtenOkFaktiskNorgePlusEos2
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.poengaarUtenOkFaktiskNorgePlusFaktiskeNorden2
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.poengaarUtenOkFaktiskNorgePlusFramtidigPoengaarNordenNetto2
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.poengaarUtenOkFaktiskeEos
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.poengaarUtenOkFaktiskeNorden
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.poengaarUtenOkFaktiskeNorge
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.poengaarUtenOkTeoretiskEos
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.PoengrekkeSelectors.populert
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.SluttpoengtallSelectors.optMedOk
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.SluttpoengtallSelectors.optMedOkEos
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.SluttpoengtallSelectors.optMedOkNordisk
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.SluttpoengtallSelectors.sptUtenOk
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.SluttpoengtallSelectors.sptUtenOkEos
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.SluttpoengtallSelectors.sptUtenOkEosMinusOptMedOkEos2
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.SluttpoengtallSelectors.sptUtenOkMinusOptMedOkAvdoed
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.SluttpoengtallSelectors.sptUtenOkNordisk
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.SluttpoengtallSelectors.sptUtenOkNordiskMinusOptMedOkNordisk2
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.faTTEos
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.faTTBilateral
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.faTTNorge
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.faTTNorgePlusFaTTA10Netto
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.faTTNorgePlusFaTTBilateral
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.faTTNorgePlusFaTTEos
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.framtidigTTAvtaleland
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.framtidigTTEos
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.framtidigTTNorsk
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.ttNevnerBilateral
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.ttNevnerEos
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.ttNevnerNordisk
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.ttNordisk
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.ttTellerBilateral
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.ttTellerEos
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidScalarsSelectors.ttTellerNordisk
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidsgrunnlagBilateralPeriodeSelectors.fom as bilFom
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidsgrunnlagBilateralPeriodeSelectors.land as bilLand
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidsgrunnlagBilateralPeriodeSelectors.tom as bilTom
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidsgrunnlagEosPeriodeSelectors.fom as eosFom
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidsgrunnlagEosPeriodeSelectors.land as eosLand
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidsgrunnlagEosPeriodeSelectors.tom as eosTom
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidsgrunnlagPeriodeSelectors.fom as norFom
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.TrygdetidsgrunnlagPeriodeSelectors.tom as norTom
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.YrkesskadeBeregningSelectors.poengaarYrke
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.YrkesskadeBeregningSelectors.poengaarYrkeE91
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.YrkesskadeBeregningSelectors.poengaarYrkeF92
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.YrkesskadeBeregningSelectors.sluttpoengtallYrke
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.ITALIC
import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.includePhrase

/**
 * Vedlegg "Opplysninger om beregningen" som henges på vedtak om gjenlevendepensjon (utland).
 *
 * Konvertert fra Exstream-malen `PE_GP_Utland_opplysninger_om_beregningen_HORISONT`.
 * Per dato kun knyttet til [VedtakEndringGjenlevendepensjonBosattUtland]; samme Exstream-vedlegg
 * ble historisk inkludert i flere GP-utland-vedtak (se PR-beskrivelsen).
 */
@TemplateModelHelpers
val vedleggOpplysningerOmBeregningenGPUtlandLegacy =
    createAttachment<LangBokmalEnglish, OpplysningerOmBeregningenGPUtlandDto>(
        title = {
            text(
                bokmal { +"Opplysninger om beregningen" },
                english { +"Information about the calculation" },
            )
        },
        includeSakspart = false,
    ) {
        // ---- Innledning ----
        title2 {
            text(
                bokmal { +"Opplysninger som ligger til grunn for beregningen fra " + pesysData.virkDatoFom.format() },
                english { +"Information that provides the basis for the calculation starting " + pesysData.virkDatoFom.format() },
            )
        }
        // ---- Opplysninger om deg ----
        paragraph {
            table(
                header = {
                    column(columnSpan = 1, alignment = LEFT) {}
                    column(columnSpan = 1, alignment = LEFT) {}
                },
            ) {
                row {
                    cell {
                        text(
                            bokmal { +"Opplysninger om deg" },
                            english { +"Information about you" }, ITALIC
                        )
                    }
                    cell { }
                }
                showIf(pesysData.bruker.brukerFlyktning) {
                    row {
                        cell {
                            text(
                                bokmal { +"Du er registrert med flyktningstatus: Ja" },
                                english { +"You are registered as having refugee status: Yes" },
                            )
                        }
                        cell { }
                    }
                }
                row {
                    cell {
                        text(
                            bokmal { +"Forventet inntekt: " + pesysData.bruker.forventetInntekt.format() },
                            english { +"Future income: NOK" + pesysData.bruker.forventetInntekt.format() },
                        )
                    }
                    cell { }
                }

                // ---- Opplysninger om avdøde ----
                row {
                    cell {
                        text(
                            bokmal { +"Opplysninger om avdøde" },
                            english { +"Information about the deceased" }, ITALIC
                        )
                    }
                    cell { }
                }
                row {
                    cell {
                        text(
                            bokmal { +"Avdødes fødselsnummer" },
                            english { +"Deceased's personal identification number" },
                        )
                    }
                    cell {
                        text(
                            bokmal { +pesysData.avdoed.fnr.format() },
                            english { +pesysData.avdoed.fnr.format() },
                        )
                    }
                }
                showIf(pesysData.avdoed.avdoedFlyktning) {
                    row {
                        cell {
                            text(
                                bokmal { +"Avdøde er registrert med flyktningstatus" },
                                english { +"The deceased is registered with the status of a refugee" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +"Ja" },
                                english { +"Yes" },
                            )
                        }
                    }
                }
                row {
                    cell {
                        text(
                            bokmal { +"Dato for dødsfallet" },
                            english { +"Date of decease" },
                        )
                    }
                    cell {
                        text(
                            bokmal { +pesysData.avdoed.doedsdato.format() },
                            english { +pesysData.avdoed.doedsdato.format() },
                        )
                    }
                }
                showIf(not(pesysData.avdoed.avdoedFlyktning)) {
                    row {
                        cell {
                            text(
                                bokmal { +"Trygdetid anvendt i beregningen" },
                                english { +"Insurance period" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +pesysData.beregning.ttAnvBest.format() + " år" },
                                english { +pesysData.beregning.ttAnvBest.format() + " year(s)" },
                            )
                        }
                    }
                }

               // Seksjonen om samboer 3-2 er fjernet, da dette er redigerbar og bør plasseres i brevet istedenfor.

                // ---- Beregningsmetode-spesifikke opplysninger ----
                // De fire Exstream `Beregningsmetode2`-grenene; innholdet er stort sett identisk på tvers av
                // metodene, forskjellene ligger i hvilke felt som har verdi og hvilke ord/etiketter som brukes.
                showIf(pesysData.beregning.beregningsmetode.equalTo(Beregningsmetode.FOLKETRYGD)) {
                    showIf(
                        pesysData.beregning.sluttpoengtall.sptUtenOk.notEqualTo(0.0)
                                and pesysData.beregning.sluttpoengtall.optMedOk.equalTo(0.0),
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Sluttpoengtall" },
                                    english { +"Final pension point score" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.sluttpoengtall.sptUtenOk.format() },
                                    english { +pesysData.beregning.sluttpoengtall.sptUtenOk.format() },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.sluttpoengtall.optMedOk.notEqualTo(0.0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Sluttpoengtall med overkompensasjon" },
                                    english { +"Final pension point score with over-compensation" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.sluttpoengtall.optMedOk.format() },
                                    english { +pesysData.beregning.sluttpoengtall.optMedOk.format() },
                                )
                            }
                        }
                        showIf(pesysData.beregning.sluttpoengtall.sptUtenOk.notEqualTo(0.0)) {
                            row {
                                cell {
                                    text(
                                        bokmal { +"Sluttpoengtall uten overkompensasjon" },
                                        english { +"Final pension point score without over-compensation" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +pesysData.beregning.sluttpoengtall.sptUtenOkMinusOptMedOkAvdoed.format() },
                                        english { +pesysData.beregning.sluttpoengtall.sptUtenOkMinusOptMedOkAvdoed.format() },
                                    )
                                }
                            }
                        }
                    }
                    showIf(pesysData.beregning.tpInnvilget) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall poengår brukt i beregning" },
                                    english { +"Number of pension point earning years" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.poengrekke.poengaarUtenOk.format() + " år" },
                                    english { +pesysData.beregning.poengrekke.poengaarUtenOk.format() + " year(s)" },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.poengaarUtenOkF92.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall år med pensjonsprosent 45" },
                                    english { +"Number of years with a 45 per cent pension" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.poengrekke.poengaarUtenOkF92.format() + " år" },
                                    english { +pesysData.beregning.poengrekke.poengaarUtenOkF92.format() + " year(s)" },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.poengaarUtenOkE91.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall år med pensjonsprosent 42" },
                                    english { +"Number of years with a 42 per cent pension" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.poengrekke.poengaarUtenOkE91.format() + " år" },
                                    english { +pesysData.beregning.poengrekke.poengaarUtenOkE91.format() + " year(s)" },
                                )
                            }
                        }
                    }
                    showIf(pesysData.avdoed.ungUfoer) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Ung ufør" },
                                    english { +"Young person with disabilities" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +"Ja" },
                                    english { +"Yes" },
                                )
                            }
                        }
                    }
                    showIf(pesysData.avdoed.doedsfallSkyldesYrkesskade) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Dødsfall skyldes yrkesskade" },
                                    english { +"Death due to occupational injury" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +"Ja" },
                                    english { +"Yes" },
                                )
                            }
                        }
                    }
                    ifNotNull(pesysData.avdoed.skadetidspunktYrkesskade) { skadetidspunkt ->
                        row {
                            cell {
                                text(
                                    bokmal { +"Yrkesskadetidspunkt" },
                                    english { +"Date/time of occupational injury" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +skadetidspunkt.format() },
                                    english { +skadetidspunkt.format() },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.yug.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Avdøde har tidligere godkjent yrkesskade med en uføregrad på" },
                                    english { +"The deceased has had an approved occupational injury with a degree of disability of" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.yug.format() + " %" },
                                    english { +pesysData.beregning.yug.format() + " %" },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.yrke.sluttpoengtallYrke.notEqualTo(0.0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Sluttpoengtall ved yrkesskade" },
                                    english { +"Final points figure on occupational injury" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.yrke.sluttpoengtallYrke.format() },
                                    english { +pesysData.beregning.yrke.sluttpoengtallYrke.format() },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.yrke.poengaarYrke.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall poengår benyttet ved yrkesskadeberegningen" },
                                    english { +"Number of point-earning year(s) applied when calculating occupational injury" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.yrke.poengaarYrke.format() + " år" },
                                    english { +pesysData.beregning.yrke.poengaarYrke.format() + " year(s)" },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.yrke.poengaarYrkeF92.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall år med pensjonsprosent 45 (yrkesskade)" },
                                    english { +"Number of years with a 45 per cent pension (occupational injury)" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.yrke.poengaarYrkeF92))
                            }
                        }
                    }
                    showIf(pesysData.beregning.yrke.poengaarYrkeE91.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall år med pensjonsprosent 42 (yrkesskade)" },
                                    english { +"Number of years with a 42 per cent pension (occupational injury)" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.yrke.poengaarYrkeE91))
                            }
                        }
                    }
                }.orShowIf(pesysData.beregning.beregningsmetode.equalTo(Beregningsmetode.EOS)) {
                    showIf(pesysData.beregning.trygdetid.faTTNorge.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Faktisk trygdetid i Norge" },
                                    english { +"Actual period of national insurance cover in Norway" },
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(pesysData.beregning.trygdetid.faTTNorge))
                            }
                        }
                    }
                    showIf(pesysData.beregning.trygdetid.faTTEos.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Faktisk trygdetid i andre EØS-land" },
                                    english { +"Actual period of national insurance cover in other EEA country" },
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(pesysData.beregning.trygdetid.faTTEos))
                            }
                        }
                    }
                    showIf(pesysData.beregning.trygdetid.framtidigTTEos.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Framtidig trygdetid" },
                                    english { +"Period of future national insurance cover" },
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(pesysData.beregning.trygdetid.framtidigTTEos))
                            }
                        }
                    }
                    showIf(pesysData.beregning.trygdetid.faTTNorge.notEqualTo(0) and pesysData.beregning.trygdetid.faTTEos.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)" },
                                    english { +"Actual period of national insurance cover in Norway and the other EEA country (max 40 years)" },
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(pesysData.beregning.trygdetid.faTTNorgePlusFaTTEos))
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.poengaarUtenOkF92.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall år med pensjonsprosent 45 (EØS)" },
                                    english { +"Number of years with a 45 per cent pension (EEA)" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOkF92))
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.poengaarUtenOkE91.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall år med pensjonsprosent 42 (EØS)" },
                                    english { +"Number of years with a 42 per cent pension (EEA)" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOkE91))
                            }
                        }
                    }
                    showIf(
                        pesysData.beregning.trygdetid.ttNevnerEos.notEqualTo(0)
                                and pesysData.beregning.trygdetid.ttTellerEos.notEqualTo(0),
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Forholdstallet brukt ved beregning av grunnpensjon" },
                                    english { +"The ratio on which the basic pension is calculated" },
                                )
                            }
                            cell {
                                text(
                                    bokmal {
                                        +pesysData.beregning.trygdetid.ttTellerEos.format() + "/" +
                                                pesysData.beregning.trygdetid.ttNevnerEos.format()
                                    },
                                    english {
                                        +pesysData.beregning.trygdetid.ttTellerEos.format() + "/" +
                                                pesysData.beregning.trygdetid.ttNevnerEos.format()
                                    },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorge.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall faktiske poengår i Norge" },
                                    english { +"Number of point earning years in Norway" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorge))
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeEos.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall poengår i andre EØS-land" },
                                    english { +"Number of point earning years in other EEA country" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeEos))
                            }
                        }
                    }
                    showIf(
                        pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorge.notEqualTo(0)
                                and pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeEos.notEqualTo(0),
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall poengår i Norge og EØS-land (maksimalt 40 år)" },
                                    english { +"Number of point earning years in Norway and other EEA country (max 40 years)" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOkFaktiskNorgePlusEos2))
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.poengaarUtenOkTeoretiskEos.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Samlet antall poengår i Norge og avtaleland (inkludert framtidig)" },
                                    english { +"Total number of point years in Norway and other EEA countries (future years included)" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOkTeoretiskEos))
                            }
                        }
                    }
                    showIf(
                        pesysData.beregning.poengrekke.poengaarTellerEos.notEqualTo(0)
                                and pesysData.beregning.poengrekke.poengaarNevnerEos.notEqualTo(0),
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Forholdstallet brukt ved beregning av tilleggspensjon" },
                                    english { +"The ratio on which the supplementary pension is calculated" },
                                )
                            }
                            cell {
                                text(
                                    bokmal {
                                        +pesysData.beregning.poengrekke.poengaarTellerEos.format() + "/" +
                                                pesysData.beregning.poengrekke.poengaarNevnerEos.format()
                                    },
                                    english {
                                        +pesysData.beregning.poengrekke.poengaarTellerEos.format() + "/" +
                                                pesysData.beregning.poengrekke.poengaarNevnerEos.format()
                                    },
                                )
                            }
                        }
                    }
                    showIf(
                        pesysData.beregning.tpInnvilget
                                and pesysData.beregning.sluttpoengtall.optMedOkEos.equalTo(0.0),
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Sluttpoengtall (EØS)" },
                                    english { +"Final pension point score (EEA)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.sluttpoengtall.sptUtenOkEos.format() },
                                    english { +pesysData.beregning.sluttpoengtall.sptUtenOkEos.format() },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.sluttpoengtall.optMedOkEos.notEqualTo(0.0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Sluttpoengtall med overkompensasjon (EØS)" },
                                    english { +"Final points with over-compensation (EEA)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.sluttpoengtall.optMedOkEos.format() },
                                    english { +pesysData.beregning.sluttpoengtall.optMedOkEos.format() },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.sluttpoengtall.sptUtenOkEos.notEqualTo(0.0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Sluttpoengtall uten overkompensasjon (EØS)" },
                                    english { +"Final points without over-compensation (EEA)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.sluttpoengtall.sptUtenOkEosMinusOptMedOkEos2.format(denominator = false) },
                                    english { +pesysData.beregning.sluttpoengtall.sptUtenOkEosMinusOptMedOkEos2.format(denominator = false) },
                                )
                            }
                        }
                    }
                }.orShowIf(pesysData.beregning.beregningsmetode.equalTo(Beregningsmetode.NORDISK)) {
                    showIf(
                        pesysData.beregning.sluttpoengtall.sptUtenOkNordisk.notEqualTo(0.0)
                                and pesysData.beregning.sluttpoengtall.optMedOkNordisk.equalTo(0.0),
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Sluttpoengtall (nordisk)" },
                                    english { +"Final pension point score (Nordic)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.sluttpoengtall.sptUtenOkNordisk.format() },
                                    english { +pesysData.beregning.sluttpoengtall.sptUtenOkNordisk.format() },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.sluttpoengtall.optMedOkNordisk.notEqualTo(0.0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Sluttpoengtall med overkompensasjon (nordisk)" },
                                    english { +"Final points with over-compensation (Nordic)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.sluttpoengtall.optMedOkNordisk.format() },
                                    english { +pesysData.beregning.sluttpoengtall.optMedOkNordisk.format() },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.sluttpoengtall.sptUtenOkNordisk.notEqualTo(0.0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Sluttpoengtall uten overkompensasjon (nordisk)" },
                                    english { +"Final points without over-compensation (Nordic)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal {
                                        +pesysData.beregning.sluttpoengtall.sptUtenOkNordiskMinusOptMedOkNordisk2.format(
                                            denominator = false
                                        )
                                    },
                                    english {
                                        +pesysData.beregning.sluttpoengtall.sptUtenOkNordiskMinusOptMedOkNordisk2.format(
                                            denominator = false
                                        )
                                    },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.tpInnvilget) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall poengår brukt i beregning" },
                                    english { +"Number of point earning years" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOk))
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.poengaarUtenOkF92.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall år med pensjonsprosent 45" },
                                    english { +"Number of years with a 45 per cent pension" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOkF92))
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.poengaarUtenOkE91.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall år med pensjonsprosent 42" },
                                    english { +"Number of years with a 42 per cent pension" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOkE91))
                            }
                        }
                    }
                    showIf(pesysData.beregning.trygdetid.ttNordisk.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Faktisk trygdetid i annet nordisk land som beregner framtidig trygdetid" },
                                    english { +"Actual period of national insurance cover in other Nordic country that calculates future national insurance cover" },
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(pesysData.beregning.trygdetid.ttNordisk))
                            }
                        }
                    }
                    showIf(pesysData.beregning.trygdetid.framtidigTTNorsk.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Norsk framtidig trygdetid" },
                                    english { +"Period of future national insurance cover in Norway" },
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(pesysData.beregning.trygdetid.framtidigTTNorsk))
                            }
                        }
                    }
                    showIf(
                        pesysData.beregning.trygdetid.ttNevnerNordisk.notEqualTo(0)
                                and pesysData.beregning.trygdetid.ttTellerNordisk.notEqualTo(0),
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Forholdstallet brukt ved avkorting av norsk framtidig trygdetid" },
                                    english { +"The ratio on which the future national insurance cover in Norway is reduced" },
                                )
                            }
                            cell {
                                text(
                                    bokmal {
                                        +pesysData.beregning.trygdetid.ttTellerNordisk.format() + "/" +
                                                pesysData.beregning.trygdetid.ttNevnerNordisk.format()
                                    },
                                    english {
                                        +pesysData.beregning.trygdetid.ttTellerNordisk.format() + "/" +
                                                pesysData.beregning.trygdetid.ttNevnerNordisk.format()
                                    },
                                )
                            }
                        }
                    }
                    showIf(
                        pesysData.beregning.trygdetid.faTTNorge.notEqualTo(0)
                                and pesysData.beregning.trygdetid.faTTNorgePlusFaTTA10Netto.notEqualTo(0),
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Samlet trygdetid brukt ved beregning av grunnpensjon etter avkorting av framtidig tid" },
                                    english { +"Total period of national insurance cover on which the basic pension is calculated after reduction of future time" },
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(pesysData.beregning.trygdetid.faTTNorgePlusFaTTA10Netto))
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorge.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall faktiske poengår i Norge" },
                                    english { +"Actual point earning years in Norway" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorge))
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorden.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Poengår i annet nordisk land som beregner framtidige år" },
                                    english { +"Point earning years in other Nordic country that calculates future years" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorden))
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.framtidigPoengaarNordenBrutto.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Norske framtidige poengår" },
                                    english { +"Nordic actual point earning years" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.framtidigPoengaarNordenBrutto))
                            }
                        }
                    }
                    showIf(
                        pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorge.notEqualTo(0)
                                and pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorden.notEqualTo(0),
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Forholdstallet brukt ved avkorting av norske framtidige poengår" },
                                    english { +"The ratio on which the reduction of future point earning years in Norway is based" },
                                )
                            }
                            cell {
                                text(
                                    bokmal {
                                        +pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorge.format() + "/" +
                                                pesysData.beregning.poengrekke.poengaarUtenOkFaktiskNorgePlusFaktiskeNorden2.format()
                                    },
                                    english {
                                        +pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorge.format() + "/" +
                                                pesysData.beregning.poengrekke.poengaarUtenOkFaktiskNorgePlusFaktiskeNorden2.format()
                                    },
                                )
                            }
                        }
                    }
                    showIf(
                        pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorge.notEqualTo(0)
                                and pesysData.beregning.poengrekke.framtidigPoengaarNordenNetto.notEqualTo(0),
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Samlet antall poengår for beregning av tilleggspensjon etter avkorting av framtidig poengår" },
                                    english { +"Total number of point earning years on which the supplementary pension is based after reduction of future point earning years" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.poengrekke.poengaarUtenOkFaktiskNorgePlusFramtidigPoengaarNordenNetto2.format() },
                                    english { +pesysData.beregning.poengrekke.poengaarUtenOkFaktiskNorgePlusFramtidigPoengaarNordenNetto2.format() },
                                )
                            }
                        }
                    }
                }.orShow {
                    showIf(pesysData.beregning.trygdetid.faTTNorge.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Faktisk trygdetid i Norge" },
                                    english { +"Actual period of national insurance cover in Norway" },
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(pesysData.beregning.trygdetid.faTTNorge))
                            }
                        }
                    }
                    showIf(pesysData.beregning.trygdetid.faTTBilateral.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Faktisk trygdetid i andre avtaleland" },
                                    english { +"Actual period of national insurance cover in other country with social security agreement" },
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(pesysData.beregning.trygdetid.faTTBilateral))
                            }
                        }
                    }
                    showIf(pesysData.beregning.trygdetid.framtidigTTAvtaleland.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Framtidig trygdetid" },
                                    english { +"Period of future national insurance cover" },
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(pesysData.beregning.trygdetid.framtidigTTAvtaleland))
                            }
                        }
                    }
                    showIf(
                        pesysData.beregning.trygdetid.faTTNorge.notEqualTo(0)
                                and pesysData.beregning.trygdetid.faTTBilateral.notEqualTo(0),
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)" },
                                    english { +"Actual period of national insurance cover in Norway and country with social security agreement (max 40 years)" },
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(pesysData.beregning.trygdetid.faTTNorgePlusFaTTBilateral))
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.poengaarUtenOkF92.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall år med pensjonsprosent 45 (avtaleland)" },
                                    english { +"Number of years with a 45 per cent pension (other country)" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOkF92))
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.poengaarUtenOkE91.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall år med pensjonsprosent 42 (avtaleland)" },
                                    english { +"Number of years with a 42 per cent pension (other country)" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOkE91))
                            }
                        }
                    }
                    showIf(
                        pesysData.beregning.trygdetid.ttNevnerBilateral.notEqualTo(0)
                                and pesysData.beregning.trygdetid.ttTellerBilateral.notEqualTo(0),
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Forholdstallet brukt ved beregning av grunnpensjon" },
                                    english { +"The ratio on which the basic pension is calculated" },
                                )
                            }
                            cell {
                                text(
                                    bokmal {
                                        +pesysData.beregning.trygdetid.ttTellerBilateral.format() + "/" +
                                                pesysData.beregning.trygdetid.ttNevnerBilateral.format()
                                    },
                                    english {
                                        +pesysData.beregning.trygdetid.ttTellerBilateral.format() + "/" +
                                                pesysData.beregning.trygdetid.ttNevnerBilateral.format()
                                    },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorge.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall faktiske poengår i Norge" },
                                    english { +"Number of point earning years in Norway" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorge))
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeEos.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall poengår i andre avtaleland" },
                                    english { +"Number of point earning years in the other country" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeEos))
                            }
                        }
                    }
                    showIf(
                        pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorge.notEqualTo(0)
                                and pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeEos.notEqualTo(0),
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall poengår i Norge og avtaleland (maksimalt 40 år)" },
                                    english { +"Number of point earning years in Norway and other country (max 40 years)" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.poengaarUtenOkFaktiskNorgePlusEos2))
                            }
                        }
                    }
                    showIf(pesysData.beregning.poengrekke.framtidigPoengtall.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall framtidige poengår" },
                                    english { +"Period of future point earning years" },
                                )
                            }
                            cell {
                                includePhrase(Felles.AarText(pesysData.beregning.poengrekke.framtidigPoengtall))
                            }
                        }
                    }
                    showIf(
                        pesysData.beregning.poengrekke.poengaarTellerEos.notEqualTo(0)
                                and pesysData.beregning.poengrekke.poengaarNevnerEos.notEqualTo(0),
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Forholdstallet brukt ved beregning av tilleggspensjon" },
                                    english { +"Ratio used when calculating pro rata supplementary pension" },
                                )
                            }
                            cell {
                                text(
                                    bokmal {
                                        +pesysData.beregning.poengrekke.poengaarTellerEos.format() + "/" +
                                                pesysData.beregning.poengrekke.poengaarNevnerEos.format()
                                    },
                                    english {
                                        +pesysData.beregning.poengrekke.poengaarTellerEos.format() + "/" +
                                                pesysData.beregning.poengrekke.poengaarNevnerEos.format()
                                    },
                                )
                            }
                        }
                    }
                    showIf(
                        pesysData.beregning.tpInnvilget
                                and pesysData.beregning.sluttpoengtall.optMedOkEos.equalTo(0.0),
                    ) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Sluttpoengtall (avtaleland)" },
                                    english { +"Final points figure (Other country)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.sluttpoengtall.sptUtenOkEos.format() },
                                    english { +pesysData.beregning.sluttpoengtall.sptUtenOkEos.format() },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.sluttpoengtall.optMedOkEos.notEqualTo(0.0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Sluttpoengtall med overkompensasjon (avtaleland)" },
                                    english { +"Final points figure with over-compensation (Other country)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.sluttpoengtall.optMedOkEos.format() },
                                    english { +pesysData.beregning.sluttpoengtall.optMedOkEos.format() },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.sluttpoengtall.sptUtenOkEos.notEqualTo(0.0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Sluttpoengtall uten overkompensasjon (avtaleland)" },
                                    english { +"Final points figure without over-compensation (Other country)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.sluttpoengtall.sptUtenOkEosMinusOptMedOkEos2.format(denominator = false) },
                                    english { +pesysData.beregning.sluttpoengtall.sptUtenOkEosMinusOptMedOkEos2.format(denominator = false) },
                                )
                            }
                        }
                    }
                }
            }
        }

        // ---- Trygdetid-info / forklaring ----
        paragraph {
            text(
                bokmal {
                    +"Trygdetid er en faktor som benyttes til å beregne pensjon. Ved beregningen får man godskrevet trygdetid i de årene man har bodd og/eller arbeidet i Norge fra fylte 16 år og frem til og med det året vedkommende fylte 66 år." +
                            " År med pensjonspoeng regnes som et helt års trygdetid. Ved beregning av gjenlevendepensjon regnes framtidig trygdetid fra dødsfallet eller avdødes uføretidspunkt fram til det året avdøde ville fylt 66 år."
                },
                english {
                    +"The period of national insurance cover is one of the factors used to calculate your pension. In the pension calculation you are credited with periods of national insurance coverage for the years during which you have lived and/or worked in Norway from the age of 16 and up to and including the year you turn 66." +
                            " Years of earned pension points are considered full years of national insurance coverage. When survivor's pension is calculated, a future period of national insurance coverage from the time of death or from the time from which the deceased was disabled and until the year the deceased would have turned 66 is included."
                },
            )
        }
        showIf(pesysData.beregning.beregningsmetode.equalTo(Beregningsmetode.NORDISK)) {
            paragraph {
                text(
                    bokmal {
                        +"Avdødes framtidige trygdetid er redusert etter artikkel 10 i nordisk konvensjon om trygd. Det innebærer at den framtidige trygdetiden er fordelt forholdsmessig mellom Norge og øvrige nordiske land hvor avdøde har fått beregnet pensjon med framtidig trygdetid." +
                                " Den framtidige trygdetiden er beregnet etter forholdet mellom norsk faktisk trygdetid og samlet faktisk trygdetid i Norge og de øvrige aktuelle land."
                    },
                    english {
                        +"The deceased's future period of national insurance cover has been reduced in accordance with section 10 of the Nordic Convention on Social Security. This means that the future period of national insurance cover has been proportionately distributed between Norway and the other Nordic countries in which the deceased has had a pension that was calculated to include a future period of national insurance cover." +
                                " The future period of national insurance cover is calculated in accordance with the ratio of the actual period of national insurance coverage in Norway and the total period of national insurance cover in Norway and the other relevant countries."
                    },
                )
            }
        }
        // ---- Trygdetidsgrunnlag-tabeller ----
        // Norsk perioder (vises når TTanv < 40)
        showIf(pesysData.beregning.ttAnv.lessThan(40)) {
            paragraph {
                text(
                    bokmal { +"Avdødes faktiske norske trygdetid er fastsatt på grunnlag av følgende perioder:" },
                    english { +"The deceased's period of national insurance cover in Norway is based on the following periods:" },
                )
            }
            paragraph {
                table(
                    header = {
                        column(columnSpan = 1, alignment = LEFT) {
                            text(bokmal { +"Fra og med" }, english { +"Start date" })
                        }
                        column(columnSpan = 1, alignment = LEFT) {
                            text(bokmal { +"Til og med" }, english { +"End date" })
                        }
                    },
                ) {
                    forEach(pesysData.avdoed.trygdetidsgrunnlagNorge) { periode ->
                        row {
                            cell { text(bokmal { +periode.norFom.format() }, english { +periode.norFom.format() }) }
                            cell { text(bokmal { +periode.norTom.format() }, english { +periode.norTom.format() }) }
                        }
                    }
                }
            }
        }

        // EOS perioder
        showIf(pesysData.beregning.beregningsmetode.equalTo(Beregningsmetode.EOS)) {
            paragraph {
                text(
                    bokmal { +"Avdødes trygdetid i øvrige EØS-land er fastsatt på grunnlag av følgende perioder:" },
                    english { +"The deceased's period of national insurance cover in other EEA countries is based on the following periods:" },
                )
            }
            paragraph {
                table(
                    header = {
                        column(columnSpan = 1, alignment = LEFT) {
                            text(bokmal { +"Land" }, english { +"Country" })
                        }
                        column(columnSpan = 1, alignment = LEFT) {
                            text(bokmal { +"Fra og med" }, english { +"Start date" })
                        }
                        column(columnSpan = 1, alignment = LEFT) {
                            text(bokmal { +"Til og med" }, english { +"End date" })
                        }
                    },
                ) {
                    forEach(pesysData.avdoed.trygdetidsgrunnlagEos) { periode ->
                        showIf(periode.eosLand.notEqualTo("Norge")) {
                            row {
                                cell { text(bokmal { +periode.eosLand }, english { +periode.eosLand }) }
                                cell { text(bokmal { +periode.eosFom.format() }, english { +periode.eosFom.format() }) }
                                cell { text(bokmal { +periode.eosTom.format() }, english { +periode.eosTom.format() }) }
                            }
                        }
                    }
                }
            }
        }

        // Bilateral perioder
        showIf(
            pesysData.beregning.beregningsmetode.notEqualTo(Beregningsmetode.EOS)
                    and pesysData.beregning.beregningsmetode.notEqualTo(Beregningsmetode.NORDISK)
                    and pesysData.beregning.beregningsmetode.notEqualTo(Beregningsmetode.FOLKETRYGD),
        ) {
            paragraph {
                text(
                    bokmal {
                        +"Avdødes trygdetid i " + pesysData.avdoed.avtaleland +
                                " er fastsatt på grunnlag av følgende perioder:"
                    },
                    english {
                        +"The deceased's period of national insurance cover in " + pesysData.avdoed.avtaleland +
                                " is based on the following periods:"
                    },
                )
            }
            paragraph {
                table(
                    header = {
                        column(columnSpan = 1, alignment = LEFT) {
                            text(bokmal { +"Land" }, english { +"Country" })
                        }
                        column(columnSpan = 1, alignment = LEFT) {
                            text(bokmal { +"Fra og med" }, english { +"Start date" })
                        }
                        column(columnSpan = 1, alignment = LEFT) {
                            text(bokmal { +"Til og med" }, english { +"End date" })
                        }
                    },
                ) {
                    forEach(pesysData.avdoed.trygdetidsgrunnlagBilateral) { periode ->
                        row {
                            cell { text(bokmal { +periode.bilLand }, english { +periode.bilLand }) }
                            cell { text(bokmal { +periode.bilFom.format() }, english { +periode.bilFom.format() }) }
                            cell { text(bokmal { +periode.bilTom.format() }, english { +periode.bilTom.format() }) }
                        }
                    }
                }
            }
        }

        // Nordisk: tekst om trygdetid i andre nordiske land
        showIf(pesysData.beregning.beregningsmetode.equalTo(Beregningsmetode.NORDISK)) {
            paragraph {
                text(
                    bokmal {
                        +"Avdødes trygdetid i øvrige land som anvender artikkel 10 i nordisk konvensjon om trygd ved beregning av grunnpensjon, er fastsatt til " +
                                pesysData.avdoed.ttNordiskAar.format() + " år og " +
                                pesysData.avdoed.ttNordiskMnd.format() + " måneder."
                    },
                    english {
                        +"The deceased's period of national insurance cover in other countries that use section 10 of the Nordic Convention on Social Security has been determined to be " +
                                pesysData.avdoed.ttNordiskAar.format() + " years and " +
                                pesysData.avdoed.ttNordiskMnd.format() + " months."
                    },
                )
            }
        }

        // ---- Poengrekka ----
        showIf(pesysData.beregning.poengrekke.populert) {
            paragraph {
                text(
                    bokmal {
                        +"Pensjonspoengene til avdøde. Nedenfor følger oversikt over pensjonsgivende inntekt og poengtall for de enkelte år." +
                                " Pensjonsgivende inntekt og pensjonspoeng blir fastsatt fra 1967 da folketrygden ble innført." +
                                " Det kreves minst 40 poengår for full opptjening av tilleggspensjon. Nav mottar opplysninger om pensjonsgivende inntekt fra Skattedirektoratet." +
                                " Hvis du mener at denne inntekten er feil, må du ta kontakt med skattekontoret."
                    },
                    english {
                        +"The deceased's pension points. Below is an overview of pensionable income and pension points per year." +
                                " Pensionable income and pension points can be accumulated from 1967, when the National Insurance Scheme was introduced." +
                                " At least 40 pension point earning years are required to earn a full supplementary pension. Nav receives information about pensionable income from the Norwegian Tax Administration." +
                                " If you believe the income information presented below is wrong, you must contact the Tax Office."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Sluttpoengtallet er beregnet som gjennomsnittet av de 20 beste poengårene, eller eventuelt alle poengår hvis det er færre enn 20 år." +
                                " Det er bare de årene da poengtallene er større enn null som regnes som poengår." +
                                " For år der egen opptjening er høyere enn omsorgspoengene, vil egen opptjening være gjeldende i beregningen av pensjonen."
                    },
                    english {
                        +"The final pension point score is calculated as the average of the 20 best pension point earning years or of all pension point earning years if there are less than 20 years." +
                                " When calculating the pension, your own pension points will be used in years when they are higher than the points credited for care work."
                    },
                )
            }
            paragraph {
                table(
                    header = {
                        column(columnSpan = 1, alignment = LEFT) {
                            text(bokmal { +"År" }, english { +"Year" })
                        }
                        column(columnSpan = 1, alignment = RIGHT) {
                            text(
                                bokmal { +"Pensjonsgivende inntekt" },
                                english { +"Pensionable income" },
                            )
                        }
                        column(columnSpan = 1, alignment = RIGHT) {
                            text(bokmal { +"Gj.snittlig G" }, english { +"Average G" })
                        }
                        column(columnSpan = 1, alignment = RIGHT) {
                            text(bokmal { +"Pensjonspoeng" }, english { +"Pension points" })
                        }
                        column(columnSpan = 2, alignment = LEFT) {
                            text(bokmal { +"Merknad" }, english { +"Notes" })
                        }
                    },
                ) {
                    forEach(pesysData.beregning.poengrekke.aar) { ar ->
                        row {
                            cell { text(bokmal { +ar.aarstall.format() }, english { +ar.aarstall.format() }) }
                            cell {
                                text(
                                    bokmal { +ar.pensjonsgivendeInntekt.format(denominator = false) + " kr" },
                                    english { +"NOK " + ar.pensjonsgivendeInntekt.format(denominator = false) },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +ar.grunnbeloepVeiet.format(denominator = false) + " kr" },
                                    english { +"NOK " + ar.grunnbeloepVeiet.format(denominator = false) },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +ar.pensjonspoeng.format() },
                                    english { +ar.pensjonspoeng.format() },
                                )
                            }
                            cell {
                                showIf(
                                    ar.poengtallstype.equalTo(OpplysningerOmBeregningenGPUtlandDto.Poengtallstype.OMSORGSPOENG_J)
                                            or ar.poengtallstype.equalTo(OMSORGSPOENG_K)
                                            or ar.poengtallstype.equalTo(OMSORGSPOENG_L),
                                ) {
                                    text(
                                        bokmal { +"Omsorgspoeng godskrevet" },
                                        english { +"Credited acquired rights for care work" },
                                    )
                                }.orShowIf(ar.poengtallstype.equalTo(FRAMTIDIG_POENG)) {
                                    text(
                                        bokmal { +"Framtidig pensjonspoeng" },
                                        english { +"Future pension point score" },
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Oppsummering av godskrevne poengår per metode
            showIf(pesysData.beregning.beregningsmetode.equalTo(Beregningsmetode.EOS)) {
                paragraph {
                    text(
                        bokmal {
                            +"Avdøde har fått godskrevet " +
                                    pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeEos.format() +
                                    " poengår i øvrige EØS-land ved beregning av tilleggspensjon."
                        },
                        english {
                            +"The deceased has been credited " +
                                    pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeEos.format() +
                                    " pension point earning years for their years of pensionable income in other EEA countries."
                        },
                    )
                }
            }.orShowIf(pesysData.beregning.beregningsmetode.equalTo(Beregningsmetode.NORDISK)) {
                paragraph {
                    text(
                        bokmal {
                            +"Avdøde har fått godskrevet " +
                                    pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorden.format() +
                                    " poengår i øvrige land som anvender artikkel 10 i nordisk konvensjon om trygd ved beregning av tilleggspensjon."
                        },
                        english {
                            +"The deceased has been credited " +
                                    pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeNorden.format() +
                                    " pension point earning years in other countries that use article 10 of the Nordic Convention on Social Security in the determination of supplementary pensions."
                        },
                    )
                }
            }.orShowIf(pesysData.beregning.beregningsmetode.equalTo(Beregningsmetode.BILATERAL)) {
                paragraph {
                    text(
                        bokmal {
                            +"Avdøde har fått godskrevet " +
                                    pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeEos.format() +
                                    " poengår i " + pesysData.avdoed.avtaleland +
                                    " ved beregning av tilleggspensjon."
                        },
                        english {
                            +"The deceased has been credited " +
                                    pesysData.beregning.poengrekke.poengaarUtenOkFaktiskeEos.format() +
                                    " pension point earning years in " + pesysData.avdoed.avtaleland +
                                    " in the calculation of your supplementary pension."
                        },
                    )
                }
            }
        }

        // ---- Avsluttende kontaktinformasjon ----
        paragraph {
            text(
                bokmal {
                    +"Hvis du mener at opplysningene vi har lagt til grunn ved beregningen inneholder feil, ber vi deg ta kontakt på telefon " +
                            Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON + "."
                },
                english {
                    +"If you think that the information we have taken as the base for the calculation contains errors, please contact us by phone +47 " +
                            Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON + "."
                },
            )
        }
    }



