package no.nav.pensjon.brev.maler.legacy.vedlegg

import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDto
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDto.EndringAarsak
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningPeriodeSelectors.aarsaker
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningPeriodeSelectors.forventetAarligInntekt
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningPeriodeSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningPeriodeSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningPeriodeSelectors.virkDatoTom
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningPeriodeSelectors.ytelser
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningSelectors.aarsaker
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningSelectors.forventetAarligInntekt
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningSelectors.ytelser
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningYtelserSelectors.brutto
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningYtelserSelectors.familietillegg
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningYtelserSelectors.fasteUtgifter
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningYtelserSelectors.grunnpensjon
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningYtelserSelectors.netto
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningYtelserSelectors.saertillegg
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.BeregningYtelserSelectors.tilleggspensjon
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.KomponentSelectors.brutto
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.KomponentSelectors.netto
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.KomponentValgfriSelectors.brutto
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.KomponentValgfriSelectors.innvilget
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.KomponentValgfriSelectors.netto
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.PesysDataSelectors.beregning
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.PesysDataSelectors.beregningPerioder
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.PesysDataSelectors.virkningFom
import no.nav.pensjon.brev.api.model.maler.legacy.OversiktOverPensjonensStoerrelseGjenlevendepensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.isNotEmpty
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.size
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

/**
 * Vedlegg "Oversikt over pensjonens stoerrelse" som henges paa vedtak om gjenlevendepensjon.
 *
 * Konvertert fra Exstream-malen PE_GP_Nasj_Utland_oversikt_over_pensjonen_pensjon_HORISONT.
 *
 * Vedlegget er IKKE redigerbart. Exstream-kilden hadde flere FRITEKST-markoerer for fri
 * saksbehandlerforklaring per periodeaarsak; disse er bevisst utelatt - kun de tre periode-
 * aarsakene som hadde fast tekst beholdes (se EndringAarsak).
 */
@TemplateModelHelpers
val vedleggOversiktOverPensjonensStoerrelseGjenlevendepensjonLegacy =
    createAttachment<LangBokmalEnglish, OversiktOverPensjonensStoerrelseGjenlevendepensjonDto>(
        title = {
            text(
                bokmal { +"Oversikt over pensjonens størrelse" },
                english { +"Summary of the pension's size" },
            )
        },
        includeSakspart = false,
    ) {
        paragraph {
            text(
                bokmal { +"Oversikt over pensjonens størrelse fra " + pesysData.virkningFom.format() },
                english { +"Summary of the pension's size from " + pesysData.virkningFom.format() },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Nedenfor følger en oversikt over de månedlige pensjonsbeløpene. " +
                        "Hvis det har vært endringer i grunnbeløpet eller i noen av opplysningene som ligger til grunn for beregningen i perioden, " +
                        "kan dette føre til en endring i størrelsen på pensjonen. Årsaken til endringen vil framgå i tabellen."
                },
                english {
                    +"A summary of the monthly pension amounts follows below. " +
                        "If there have been changes in the basic amount or in some of the information on which the calculation for the period is based, " +
                        "this may result in a change in the amount of pension. The reason for the change will appear in the table."
                },
            )
        }

        showIf(pesysData.beregningPerioder.size().greaterThan(1)) {
            forEach(pesysData.beregningPerioder) { periode ->
                paragraph {
                    text(
                        bokmal { +"Den månedlige pensjonen i perioden " + periode.virkDatoFom.format() + " - " + periode.virkDatoTom.format() },
                        english { +"The monthly pension from " + periode.virkDatoFom.format() + " to " + periode.virkDatoTom.format() },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Folketrygdens grunnbeløp (G) benyttet i beregningen er " + periode.grunnbeloep.format(denominator = false) +
                                " kroner. Forventet årlig inntekt benyttet i beregningen er " + periode.forventetAarligInntekt.format(denominator = false) + " kroner."
                        },
                        english {
                            +"The National Insurance basic amount (G) used in the calculation is " + periode.grunnbeloep.format(denominator = false) +
                                " NOK. Future annual income used in the calculation is " + periode.forventetAarligInntekt.format(denominator = false) + " NOK."
                        },
                    )
                }

                showIf(periode.aarsaker.isNotEmpty()) {
                    paragraph {
                        text(
                            bokmal { +"Pensjonen din er endret fordi" },
                            english { +"Your pension is changed due to" },
                        )
                    }
                    forEach(periode.aarsaker) { aarsak ->
                        showIf(aarsak.equalTo(EndringAarsak.FASTE_UTGIFTER_INSTITUSJONSOPPHOLD)) {
                            paragraph {
                                text(
                                    bokmal { +"- faste utgifter ved institusjonsopphold er endret" },
                                    english { +"- a change in fixed costs when institutionalised" },
                                )
                            }
                        }
                        showIf(aarsak.equalTo(EndringAarsak.UTTAKSGRAD)) {
                            paragraph {
                                text(
                                    bokmal { +"- uttaksgraden er endret" },
                                    english { +"- a change in the pension level" },
                                )
                            }
                        }
                        showIf(aarsak.equalTo(EndringAarsak.OPPTJENING)) {
                            paragraph {
                                text(
                                    bokmal { +"- opptjeningsgrunnlaget er endret" },
                                    english { +"- a change in the accumulated pension rights" },
                                )
                            }
                        }
                    }
                }

                showIf(periode.ytelser.brutto.notEqualTo(periode.ytelser.netto)) {
                    paragraph {
                        table(
                            header = {
                                column(columnSpan = 2, alignment = LEFT) { text(bokmal { +"" }, english { +"" }) }
                                column(columnSpan = 1, alignment = RIGHT) {
                                    text(
                                        bokmal { +"Pensjon per måned før fradrag for inntekt" },
                                        english { +"Pension per month before deduction for income" },
                                    )
                                }
                                column(columnSpan = 1, alignment = RIGHT) {
                                    text(
                                        bokmal { +"Pensjon per måned etter fradrag for inntekt" },
                                        english { +"Pension per month after deduction for income" },
                                    )
                                }
                            },
                        ) {
                            row {
                                cell { text(bokmal { +"Grunnpensjon" }, english { +"Basic pension" }) }
                                cell {
                                    text(
                                        bokmal { +periode.ytelser.grunnpensjon.brutto.format(denominator = false) + " kr" },
                                        english { +periode.ytelser.grunnpensjon.brutto.format(denominator = false) + " NOK" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +periode.ytelser.grunnpensjon.netto.format(denominator = false) + " kr" },
                                        english { +periode.ytelser.grunnpensjon.netto.format(denominator = false) + " NOK" },
                                    )
                                }
                            }
                            showIf(periode.ytelser.tilleggspensjon.innvilget) {
                                row {
                                    cell { text(bokmal { +"Tilleggspensjon" }, english { +"Supplementary pension" }) }
                                    cell {
                                        text(
                                            bokmal { +periode.ytelser.tilleggspensjon.brutto.format(denominator = false) + " kr" },
                                            english { +periode.ytelser.tilleggspensjon.brutto.format(denominator = false) + " NOK" },
                                        )
                                    }
                                    cell {
                                        text(
                                            bokmal { +periode.ytelser.tilleggspensjon.netto.format(denominator = false) + " kr" },
                                            english { +periode.ytelser.tilleggspensjon.netto.format(denominator = false) + " NOK" },
                                        )
                                    }
                                }
                            }
                            showIf(periode.ytelser.saertillegg.innvilget) {
                                row {
                                    cell { text(bokmal { +"Særtillegg" }, english { +"Special supplement" }) }
                                    cell {
                                        text(
                                            bokmal { +periode.ytelser.saertillegg.brutto.format(denominator = false) + " kr" },
                                            english { +periode.ytelser.saertillegg.brutto.format(denominator = false) + " NOK" },
                                        )
                                    }
                                    cell {
                                        text(
                                            bokmal { +periode.ytelser.saertillegg.netto.format(denominator = false) + " kr" },
                                            english { +periode.ytelser.saertillegg.netto.format(denominator = false) + " NOK" },
                                        )
                                    }
                                }
                            }
                            showIf(periode.ytelser.fasteUtgifter.innvilget) {
                                row {
                                    cell {
                                        text(
                                            bokmal { +"Faste utgifter ved institusjonsopphold" },
                                            english { +"Fixed costs when institutionalised" },
                                        )
                                    }
                                    cell {
                                        text(
                                            bokmal { +periode.ytelser.fasteUtgifter.brutto.format(denominator = false) + " kr" },
                                            english { +periode.ytelser.fasteUtgifter.brutto.format(denominator = false) + " NOK" },
                                        )
                                    }
                                    cell {
                                        text(
                                            bokmal { +periode.ytelser.fasteUtgifter.netto.format(denominator = false) + " kr" },
                                            english { +periode.ytelser.fasteUtgifter.netto.format(denominator = false) + " NOK" },
                                        )
                                    }
                                }
                            }
                            showIf(periode.ytelser.familietillegg.innvilget) {
                                row {
                                    cell { text(bokmal { +"Familietillegg" }, english { +"Family supplement" }) }
                                    cell {
                                        text(
                                            bokmal { +periode.ytelser.familietillegg.brutto.format(denominator = false) + " kr" },
                                            english { +periode.ytelser.familietillegg.brutto.format(denominator = false) + " NOK" },
                                        )
                                    }
                                    cell {
                                        text(
                                            bokmal { +periode.ytelser.familietillegg.netto.format(denominator = false) + " kr" },
                                            english { +periode.ytelser.familietillegg.netto.format(denominator = false) + " NOK" },
                                        )
                                    }
                                }
                            }
                            row {
                                cell { text(bokmal { +"Sum pensjon før skatt" }, english { +"Total pension before tax" }) }
                                cell {
                                    text(
                                        bokmal { +periode.ytelser.brutto.format(denominator = false) + " kr" },
                                        english { +periode.ytelser.brutto.format(denominator = false) + " NOK" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +periode.ytelser.netto.format(denominator = false) + " kr" },
                                        english { +periode.ytelser.netto.format(denominator = false) + " NOK" },
                                    )
                                }
                            }
                        }
                    }
                }

                showIf(periode.ytelser.brutto.equalTo(periode.ytelser.netto)) {
                    paragraph {
                        table(
                            header = {
                                column(columnSpan = 2, alignment = LEFT) { text(bokmal { +"" }, english { +"" }) }
                                column(columnSpan = 1, alignment = RIGHT) {
                                    text(
                                        bokmal { +"Pensjon per måned" },
                                        english { +"Pension per month" },
                                    )
                                }
                            },
                        ) {
                            row {
                                cell { text(bokmal { +"Grunnpensjon" }, english { +"Basic pension" }) }
                                cell {
                                    text(
                                        bokmal { +periode.ytelser.grunnpensjon.netto.format(denominator = false) + " kr" },
                                        english { +periode.ytelser.grunnpensjon.netto.format(denominator = false) + " NOK" },
                                    )
                                }
                            }
                            showIf(periode.ytelser.tilleggspensjon.innvilget) {
                                row {
                                    cell { text(bokmal { +"Tilleggspensjon" }, english { +"Supplementary pension" }) }
                                    cell {
                                        text(
                                            bokmal { +periode.ytelser.tilleggspensjon.netto.format(denominator = false) + " kr" },
                                            english { +periode.ytelser.tilleggspensjon.netto.format(denominator = false) + " NOK" },
                                        )
                                    }
                                }
                            }
                            showIf(periode.ytelser.saertillegg.innvilget) {
                                row {
                                    cell { text(bokmal { +"Særtillegg" }, english { +"Special supplement" }) }
                                    cell {
                                        text(
                                            bokmal { +periode.ytelser.saertillegg.netto.format(denominator = false) + " kr" },
                                            english { +periode.ytelser.saertillegg.netto.format(denominator = false) + " NOK" },
                                        )
                                    }
                                }
                            }
                            showIf(periode.ytelser.fasteUtgifter.innvilget) {
                                row {
                                    cell {
                                        text(
                                            bokmal { +"Faste utgifter ved institusjonsopphold" },
                                            english { +"Fixed costs when institutionalised" },
                                        )
                                    }
                                    cell {
                                        text(
                                            bokmal { +periode.ytelser.fasteUtgifter.netto.format(denominator = false) + " kr" },
                                            english { +periode.ytelser.fasteUtgifter.netto.format(denominator = false) + " NOK" },
                                        )
                                    }
                                }
                            }
                            showIf(periode.ytelser.familietillegg.innvilget) {
                                row {
                                    cell { text(bokmal { +"Familietillegg" }, english { +"Family supplement" }) }
                                    cell {
                                        text(
                                            bokmal { +periode.ytelser.familietillegg.netto.format(denominator = false) + " kr" },
                                            english { +periode.ytelser.familietillegg.netto.format(denominator = false) + " NOK" },
                                        )
                                    }
                                }
                            }
                            row {
                                cell { text(bokmal { +"Sum pensjon før skatt" }, english { +"Total pension before tax" }) }
                                cell {
                                    text(
                                        bokmal { +periode.ytelser.netto.format(denominator = false) + " kr" },
                                        english { +periode.ytelser.netto.format(denominator = false) + " NOK" },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        paragraph {
            text(
                bokmal { +"Den månedlige pensjonen fra " + pesysData.beregning.virkDatoFom.format() },
                english { +"The monthly pension from " + pesysData.beregning.virkDatoFom.format() },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Folketrygdens grunnbeløp (G) benyttet i beregningen er " + pesysData.beregning.grunnbeloep.format(denominator = false) +
                        " kroner. Forventet årlig inntekt benyttet i beregningen er " + pesysData.beregning.forventetAarligInntekt.format(denominator = false) + " kroner."
                },
                english {
                    +"The National Insurance basic amount (G) used in the calculation is " + pesysData.beregning.grunnbeloep.format(denominator = false) +
                        " NOK. Future annual income used in the calculation is " + pesysData.beregning.forventetAarligInntekt.format(denominator = false) + " NOK."
                },
            )
        }
        showIf(pesysData.beregning.aarsaker.isNotEmpty()) {
            paragraph {
                text(
                    bokmal { +"Pensjonen din er endret fordi" },
                    english { +"Your pension is changed due to" },
                )
            }
            forEach(pesysData.beregning.aarsaker) { aarsak ->
                showIf(aarsak.equalTo(EndringAarsak.FASTE_UTGIFTER_INSTITUSJONSOPPHOLD)) {
                    paragraph {
                        text(
                            bokmal { +"- faste utgifter ved institusjonsopphold er endret" },
                            english { +"- a change in fixed costs when institutionalised" },
                        )
                    }
                }
                showIf(aarsak.equalTo(EndringAarsak.UTTAKSGRAD)) {
                    paragraph {
                        text(
                            bokmal { +"- uttaksgraden er endret" },
                            english { +"- a change in the pension level" },
                        )
                    }
                }
                showIf(aarsak.equalTo(EndringAarsak.OPPTJENING)) {
                    paragraph {
                        text(
                            bokmal { +"- opptjeningsgrunnlaget er endret" },
                            english { +"- a change in the accumulated pension rights" },
                        )
                    }
                }
            }
        }

        showIf(pesysData.beregning.ytelser.brutto.notEqualTo(pesysData.beregning.ytelser.netto)) {
            paragraph {
                table(
                    header = {
                        column(columnSpan = 2, alignment = LEFT) { text(bokmal { +"" }, english { +"" }) }
                        column(columnSpan = 1, alignment = RIGHT) {
                            text(
                                bokmal { +"Pensjon per måned før fradrag for inntekt" },
                                english { +"Pension per month before deduction for income" },
                            )
                        }
                        column(columnSpan = 1, alignment = RIGHT) {
                            text(
                                bokmal { +"Pensjon per måned etter fradrag for inntekt" },
                                english { +"Pension per month after deduction for income" },
                            )
                        }
                    },
                ) {
                    row {
                        cell { text(bokmal { +"Grunnpensjon" }, english { +"Basic pension" }) }
                        cell {
                            text(
                                bokmal { +pesysData.beregning.ytelser.grunnpensjon.brutto.format(denominator = false) + " kr" },
                                english { +pesysData.beregning.ytelser.grunnpensjon.brutto.format(denominator = false) + " NOK" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +pesysData.beregning.ytelser.grunnpensjon.netto.format(denominator = false) + " kr" },
                                english { +pesysData.beregning.ytelser.grunnpensjon.netto.format(denominator = false) + " NOK" },
                            )
                        }
                    }
                    showIf(pesysData.beregning.ytelser.tilleggspensjon.innvilget) {
                        row {
                            cell { text(bokmal { +"Tilleggspensjon" }, english { +"Supplementary pension" }) }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.ytelser.tilleggspensjon.brutto.format(denominator = false) + " kr" },
                                    english { +pesysData.beregning.ytelser.tilleggspensjon.brutto.format(denominator = false) + " NOK" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.ytelser.tilleggspensjon.netto.format(denominator = false) + " kr" },
                                    english { +pesysData.beregning.ytelser.tilleggspensjon.netto.format(denominator = false) + " NOK" },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.ytelser.saertillegg.innvilget) {
                        row {
                            cell { text(bokmal { +"Særtillegg" }, english { +"Special supplement" }) }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.ytelser.saertillegg.brutto.format(denominator = false) + " kr" },
                                    english { +pesysData.beregning.ytelser.saertillegg.brutto.format(denominator = false) + " NOK" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.ytelser.saertillegg.netto.format(denominator = false) + " kr" },
                                    english { +pesysData.beregning.ytelser.saertillegg.netto.format(denominator = false) + " NOK" },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.ytelser.fasteUtgifter.innvilget) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Faste utgifter ved institusjonsopphold" },
                                    english { +"Fixed costs when institutionalised" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.ytelser.fasteUtgifter.brutto.format(denominator = false) + " kr" },
                                    english { +pesysData.beregning.ytelser.fasteUtgifter.brutto.format(denominator = false) + " NOK" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.ytelser.fasteUtgifter.netto.format(denominator = false) + " kr" },
                                    english { +pesysData.beregning.ytelser.fasteUtgifter.netto.format(denominator = false) + " NOK" },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.ytelser.familietillegg.innvilget) {
                        row {
                            cell { text(bokmal { +"Familietillegg" }, english { +"Family supplement" }) }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.ytelser.familietillegg.brutto.format(denominator = false) + " kr" },
                                    english { +pesysData.beregning.ytelser.familietillegg.brutto.format(denominator = false) + " NOK" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.ytelser.familietillegg.netto.format(denominator = false) + " kr" },
                                    english { +pesysData.beregning.ytelser.familietillegg.netto.format(denominator = false) + " NOK" },
                                )
                            }
                        }
                    }
                    row {
                        cell { text(bokmal { +"Sum pensjon før skatt" }, english { +"Total pension before tax" }) }
                        cell {
                            text(
                                bokmal { +pesysData.beregning.ytelser.brutto.format(denominator = false) + " kr" },
                                english { +pesysData.beregning.ytelser.brutto.format(denominator = false) + " NOK" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +pesysData.beregning.ytelser.netto.format(denominator = false) + " kr" },
                                english { +pesysData.beregning.ytelser.netto.format(denominator = false) + " NOK" },
                            )
                        }
                    }
                }
            }
        }

        showIf(pesysData.beregning.ytelser.brutto.equalTo(pesysData.beregning.ytelser.netto)) {
            paragraph {
                table(
                    header = {
                        column(columnSpan = 2, alignment = LEFT) { text(bokmal { +"" }, english { +"" }) }
                        column(columnSpan = 1, alignment = RIGHT) {
                            text(
                                bokmal { +"Pensjon per måned" },
                                english { +"Pension per month" },
                            )
                        }
                    },
                ) {
                    row {
                        cell { text(bokmal { +"Grunnpensjon" }, english { +"Basic pension" }) }
                        cell {
                            text(
                                bokmal { +pesysData.beregning.ytelser.grunnpensjon.netto.format(denominator = false) + " kr" },
                                english { +pesysData.beregning.ytelser.grunnpensjon.netto.format(denominator = false) + " NOK" },
                            )
                        }
                    }
                    showIf(pesysData.beregning.ytelser.tilleggspensjon.innvilget) {
                        row {
                            cell { text(bokmal { +"Tilleggspensjon" }, english { +"Supplementary pension" }) }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.ytelser.tilleggspensjon.netto.format(denominator = false) + " kr" },
                                    english { +pesysData.beregning.ytelser.tilleggspensjon.netto.format(denominator = false) + " NOK" },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.ytelser.saertillegg.innvilget) {
                        row {
                            cell { text(bokmal { +"Særtillegg" }, english { +"Special supplement" }) }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.ytelser.saertillegg.netto.format(denominator = false) + " kr" },
                                    english { +pesysData.beregning.ytelser.saertillegg.netto.format(denominator = false) + " NOK" },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.ytelser.fasteUtgifter.innvilget) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Faste utgifter ved institusjonsopphold" },
                                    english { +"Fixed costs when institutionalised" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.ytelser.fasteUtgifter.netto.format(denominator = false) + " kr" },
                                    english { +pesysData.beregning.ytelser.fasteUtgifter.netto.format(denominator = false) + " NOK" },
                                )
                            }
                        }
                    }
                    showIf(pesysData.beregning.ytelser.familietillegg.innvilget) {
                        row {
                            cell { text(bokmal { +"Familietillegg" }, english { +"Family supplement" }) }
                            cell {
                                text(
                                    bokmal { +pesysData.beregning.ytelser.familietillegg.netto.format(denominator = false) + " kr" },
                                    english { +pesysData.beregning.ytelser.familietillegg.netto.format(denominator = false) + " NOK" },
                                )
                            }
                        }
                    }
                    row {
                        cell { text(bokmal { +"Sum pensjon før skatt" }, english { +"Total pension before tax" }) }
                        cell {
                            text(
                                bokmal { +pesysData.beregning.ytelser.netto.format(denominator = false) + " kr" },
                                english { +pesysData.beregning.ytelser.netto.format(denominator = false) + " NOK" },
                            )
                        }
                    }
                }
            }
        }
    }

