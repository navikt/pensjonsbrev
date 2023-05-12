package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.faktiskTTBilateral_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.faktiskTTEOS_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.faktiskTTNordiskKonv_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.faktiskTTNorgePlusFaktiskBilateral_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.faktiskTTNorgePlusfaktiskTTEOS_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.faktiskTTNorge_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.framtidigTTAvtaleland_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.framtidigTTEOS_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.framtidigTTNorsk_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.nevnerTTBilateralProRata_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.nevnerTTEOS_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.nevnerTTNordiskKonv_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.samletTTNordiskKonv_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.tellerTTBilateralProRata_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.tellerTTEOS_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.tellerTTNordiskKonv_safe
import no.nav.pensjon.brev.api.model.vedlegg.Ufoeretrygd1Selectors.beregningsgrunnlagBeloepAar
import no.nav.pensjon.brev.api.model.vedlegg.Ufoeretrygd1Selectors.ufoeretidspunkt
import no.nav.pensjon.brev.api.model.vedlegg.Yrkesskade1Selectors.beregningsgrunnlagBeloepAarYrkesskade_safe
import no.nav.pensjon.brev.api.model.vedlegg.Yrkesskade1Selectors.inntektVedSkadetidspunkt_safe
import no.nav.pensjon.brev.api.model.vedlegg.Yrkesskade1Selectors.yrkesskadegrad_safe
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer

// <> brevkode PE_UT_04_108, PE_UT_04_109, PE_UT_04_500, PE_UT_05_100, PE_UT_06_300, PE_UT_07_100, PE_UT_07_200,


data class TabellUfoereOpplysningerAvdoed(
    val beregnetUTPerManed: Expression<OpplysningerBruktIBeregningUTDto.BeregnetUTPerManed>,
    val erFlyktning: Expression<Boolean>,
    val erUngUfoer: Expression<Boolean>,
    val foedselsnummer: Expression<Foedselsnummer>,
    val avdoed: Expression<OpplysningerBruktIBeregningUTDto.OpplysningerBruktIBeregningUTAvdoed?>,
    val trygdetidsdetaljer: Expression<OpplysningerBruktIBeregningUTDto.OpplysningerBruktIBeregningUTAvdoed.TrygdetidsdetaljerAvdoed>,
    val ufoeretrygd: Expression<OpplysningerBruktIBeregningUTDto.OpplysningerBruktIBeregningUTAvdoed.Ufoeretrygd1>,
    val yrkesskade: Expression<OpplysningerBruktIBeregningUTDto.OpplysningerBruktIBeregningUTAvdoed.Yrkesskade1?>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Language.Bokmal to "Opplysninger om avdøde",
                Language.Nynorsk to "Opplysningar om avdøde",
                Language.English to "Opplysningar om avdøde"
            )
        }
        paragraph {
            val grunnbeloep = beregnetUTPerManed.grunnbeloep.format()
            val virkDatoFom = beregnetUTPerManed.virkDatoFom.format()
            textExpr(
                Language.Bokmal to "Opplysninger om avdøde som ligger til grunn for beregningen av gjenlevendetillegget ditt fra ".expr() + virkDatoFom + ". Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + grunnbeloep + " kroner.",
                Language.Nynorsk to "I berekninga av attlevandetillegget har vi brukt desse opplysningane om den avdøde frå ".expr() + virkDatoFom + ". Folketrygdas grunnbeløp (G) nytta i berekninga er ".expr() + grunnbeloep + " kroner.",
                Language.English to "In calculating your survivor's benefit, we have applied the following data relating to the deceased of ".expr() + virkDatoFom + ". The National Insurance basic amount (G) used in the calculation is NOK ".expr() + grunnbeloep + ".",
            )
        }
        paragraph {
            table(
                header = {
                    column(2) {
                        text(
                            Language.Bokmal to "Opplysninger",
                            Language.Nynorsk to "Opplysning",
                            Language.English to "Information",
                            Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                        )
                    }
                    column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                    }
                }
            ) {
                row {
                    cell {
                        text(
                            Language.Bokmal to "Avdødes fødselsnummer",
                            Language.Nynorsk to "Avdødes fødselsnummer",
                            Language.English to "The deceased's national identity number"
                        )
                    }
                    cell {
                            textExpr(
                                Language.Bokmal to foedselsnummer.format(),
                                Language.Nynorsk to foedselsnummer.format(),
                                Language.English to foedselsnummer.format(),
                            )
                        }
                    }
                row {
                    cell {
                        text(
                            Language.Bokmal to "Uføretidspunkt",
                            Language.Nynorsk to "Uføretidspunkt",
                            Language.English to "Date of disability"
                        )
                    }
                    cell {
                        textExpr(
                            Language.Bokmal to ufoeretrygd.ufoeretidspunkt.format(),
                            Language.Nynorsk to ufoeretrygd.ufoeretidspunkt.format(),
                            Language.English to ufoeretrygd.ufoeretidspunkt.format()
                        )
                    }
                }
                showIf(ufoeretrygd.beregningsgrunnlagBeloepAar.greaterThan(0)) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Beregningsgrunnlag",
                                Language.Nynorsk to "Utrekningsgrunnlag",
                                Language.English to "Basis for calculation"
                            )
                        }
                        cell { includePhrase(Felles.KronerText(ufoeretrygd.beregningsgrunnlagBeloepAar)) }
                    }
                }
                row {
                    cell {
                        text(
                            Language.Bokmal to "Uføregrad",
                            Language.Nynorsk to "Uføregrad",
                            Language.English to "Degree of disability"
                        )
                    }
                    cell {
                        text(
                            Language.Bokmal to "100 %",
                            Language.Nynorsk to "100 %",
                            Language.English to "100 %"
                        )
                    }
                }
                showIf(erFlyktning) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Avdøde var innvilget flyktningstatus fra UDI",
                                Language.Nynorsk to "Avdøde var innvilga flyktningstatus frå UDI",
                                Language.English to "Deceased was granted refugee status by the Norwegian Directorate of Immigration (UDI)"
                            )
                        }
                        cell {
                            text(
                                Language.Bokmal to "Ja",
                                Language.Nynorsk to "Ja",
                                Language.English to "Yes"
                            )
                        }
                    }
                }
                showIf(not(erFlyktning)) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Trygdetid (maksimalt 40 år)",
                                Language.Nynorsk to "Trygdetid (maksimalt 40 år)",
                                Language.English to "Insurance period (maximum 40 years)"
                            )
                        }
                        cell {
                            val anvendtTT = trygdetidsdetaljer.anvendtTT
                            includePhrase(Felles.AarText(anvendtTT.ifNull(0)))
                        }
                    }
                }
                showIf(erUngUfoer) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Ung ufør",
                                Language.Nynorsk to "Ung ufør",
                                Language.English to "Young disabled"
                            )
                        }
                        cell {
                            text(
                                Language.Bokmal to "Ja",
                                Language.Nynorsk to "Ja",
                                Language.English to "Yes"
                            )
                        }
                    }
                }
                ifNotNull(yrkesskade.yrkesskadegrad_safe) { yrkesskadegrad ->
                    showIf(yrkesskadegrad.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Yrkesskadegrad",
                                    Language.Nynorsk to "Yrkesskadegrad",
                                    Language.English to "Degree of disability due to occupational injury"
                                )
                            }
                            cell { includePhrase((Felles.ProsentText(yrkesskadegrad))) }
                        }
                    }
                    ifNotNull(yrkesskade.beregningsgrunnlagBeloepAarYrkesskade_safe) { beloep ->
                        showIf(beloep.greaterThan(0)) {
                            row {
                                cell {
                                    text(
                                        Language.Bokmal to "Beregningsgrunnlag yrkesskade",
                                        Language.Nynorsk to "Utrekningsgrunnlag yrkesskade",
                                        Language.English to "Basis for calculation due to occupational injury"
                                    )
                                }
                                cell { includePhrase(Felles.KronerText(beloep)) }
                            }
                        }
                    }
                    ifNotNull(yrkesskade.inntektVedSkadetidspunkt_safe) { inntekt ->
                        showIf(inntekt.greaterThan(0)) {
                            row {
                                cell {
                                    text(
                                        Language.Bokmal to "Årlig arbeidsinntekt på skadetidspunktet",
                                        Language.Nynorsk to "Årleg arbeidsinntekt på skadetidspunktet",
                                        Language.English to "Annual income at the date of injury"
                                    )
                                }
                                cell { includePhrase(Felles.KronerText(inntekt)) }
                            }
                        }
                    }
                }
                ifNotNull(
                    trygdetidsdetaljer.faktiskTTNorge_safe,
                    trygdetidsdetaljer.faktiskTTEOS_safe
                ) { faktiskTTNorge, faktiskTTEOS ->
                    showIf(faktiskTTNorge.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Faktisk trygdetid i Norge",
                                    Language.Nynorsk to "Faktisk trygdetid i Noreg",
                                    Language.English to "Actual insurance period in Norway"
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(faktiskTTNorge))
                            }
                        }
                    }

                    showIf(faktiskTTEOS.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Faktisk trygdetid i andre EØS-land",
                                    Language.Nynorsk to "Faktisk trygdetid i andre EØS-land",
                                    Language.English to "Actual insurance period(s) in other EEA countries"
                                )
                            }
                            cell { includePhrase(Felles.MaanederText(faktiskTTEOS)) }
                        }
                    }

                    ifNotNull(trygdetidsdetaljer.framtidigTTEOS_safe) { framtidigTTEOS ->
                        showIf(framtidigTTEOS.greaterThan(0)) {
                            row {
                                cell {
                                    text(
                                        Language.Bokmal to "Faktisk trygdetid i Norge og EØS-land (maksimalt 40 år)",
                                        Language.Nynorsk to "Faktisk trygdetid i Noreg og EØS-land (maksimalt 40 år)",
                                        Language.English to "Actual insurance period in Norway and EEA countries (maximum 40 years)"
                                    )
                                }
                                cell { includePhrase(Felles.MaanederText(framtidigTTEOS)) }
                            }
                        }
                    }

                    showIf(faktiskTTNorge.greaterThan(0) and faktiskTTEOS.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)",
                                    Language.Nynorsk to "Faktisk trygdetid i Noreg og avtaleland (maksimalt 40 år)",
                                    Language.English to "Actual insurance period in Norway and partner countries (maximum 40 years)"
                                )
                            }
                            cell {
                                val faktiskTrygdetid =
                                    trygdetidsdetaljer.faktiskTTNorgePlusfaktiskTTEOS_safe
                                includePhrase(Felles.MaanederText(faktiskTrygdetid.ifNull(then = 0)))
                            }
                        }
                    }
                }
                ifNotNull(
                    trygdetidsdetaljer.tellerTTEOS_safe,
                    trygdetidsdetaljer.nevnerTTEOS_safe
                ) { tellerTTEOS, nevnerTTEOS ->
                    showIf(tellerTTEOS.greaterThan(0) and nevnerTTEOS.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Forholdstallet brukt i beregning av trygdetid",
                                    Language.Nynorsk to "Forholdstalet brukt i utrekning av trygdetid",
                                    Language.English to "Ratio applied in calculation of insurance period"
                                )
                            }
                            cell {
                                textExpr(
                                    Language.Bokmal to tellerTTEOS.format() + " / " + nevnerTTEOS.format(),
                                    Language.Nynorsk to tellerTTEOS.format() + " / " + nevnerTTEOS.format(),
                                    Language.English to tellerTTEOS.format() + " / " + nevnerTTEOS.format()
                                )
                            }
                        }
                    }
                }
                ifNotNull(trygdetidsdetaljer.faktiskTTNordiskKonv_safe) { faktiskTTNordiskKonv ->
                    // showIf?
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Faktisk trygdetid i annet nordisk land som brukes i beregning av framtidig trygdetid",
                                Language.Nynorsk to "Faktisk trygdetid i anna nordisk land som blir brukt i utrekning av framtidig trygdetid",
                                Language.English to "Actual insurance period in another Nordic country, applied in calculation of future insurance period(s)"
                            )
                        }
                        cell { includePhrase(Felles.MaanederText(faktiskTTNordiskKonv.ifNull(then = 0))) }
                    }
                }
                ifNotNull(trygdetidsdetaljer.framtidigTTNorsk_safe) { framtidigTTNorsk ->
                    showIf(
                        framtidigTTNorsk.greaterThan(0) and framtidigTTNorsk.lessThan(480)
                    ) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Norsk framtidig trygdetid",
                                    Language.Nynorsk to "Norsk framtidig trygdetid",
                                    Language.English to "Future insurance period in Norway"
                                )
                            }
                            cell { includePhrase(Felles.MaanederText(framtidigTTNorsk)) }
                        }
                    }
                }
                ifNotNull(
                    trygdetidsdetaljer.tellerTTNordiskKonv_safe,
                    trygdetidsdetaljer.nevnerTTNordiskKonv_safe
                ) { tellerTTNordiskKonv, nevnerTTNordiskKonv ->
                    showIf(tellerTTNordiskKonv.greaterThan(0) and nevnerTTNordiskKonv.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Forholdstallet brukt i reduksjon av norsk framtidig trygdetid",
                                    Language.Nynorsk to "Forholdstalet brukt i reduksjon av norsk framtidig trygdetid",
                                    Language.English to "Ratio applied in reduction of future Norwegian insurance period"
                                )
                            }
                            cell {
                                textExpr(
                                    Language.Bokmal to tellerTTNordiskKonv.format() + " / " + nevnerTTNordiskKonv.format(),
                                    Language.Nynorsk to tellerTTNordiskKonv.format() + " / " + nevnerTTNordiskKonv.format(),
                                    Language.English to tellerTTNordiskKonv.format() + " / " + nevnerTTNordiskKonv.format()
                                )
                            }
                        }
                    }
                }
                ifNotNull(trygdetidsdetaljer.samletTTNordiskKonv_safe) { samletTTNordiskKonv ->
                    showIf(samletTTNordiskKonv.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Samlet trygdetid brukt i beregning av uføretrygd etter reduksjon av framtidig trygdetid",
                                    Language.Nynorsk to "Samla trygdetid brukt i utrekning av uføretrygd etter reduksjon av framtidig trygdetid",
                                    Language.English to "Total insurance period applied in calculating disability benefit after reduction of future insurance period(s"
                                )
                            }
                            cell { includePhrase(Felles.MaanederText(samletTTNordiskKonv)) }
                        }
                    }
                }
                ifNotNull(trygdetidsdetaljer.faktiskTTBilateral_safe) { faktiskTTBilateral ->
                    showIf(faktiskTTBilateral.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Faktisk trygdetid i annet avtaleland",
                                    Language.Nynorsk to "Faktisk trygdetid i anna avtaleland",
                                    Language.English to "Actual insurance period(s) in another partner country"
                                )
                            }
                            cell { includePhrase(Felles.MaanederText(faktiskTTBilateral)) }
                        }
                    }
                }
                ifNotNull(trygdetidsdetaljer.framtidigTTAvtaleland_safe) { framtidigTTAvtaleland ->
                    showIf(framtidigTTAvtaleland.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Framtidig trygdetid",
                                    Language.Nynorsk to "Framtidig trygdetid",
                                    Language.English to "Future insurance period in Norway"
                                )
                            }
                            cell { includePhrase(Felles.MaanederText(framtidigTTAvtaleland)) }
                        }
                    }
                }
                ifNotNull(
                    trygdetidsdetaljer.faktiskTTNorge_safe,
                    trygdetidsdetaljer.faktiskTTBilateral_safe
                ) { faktiskTTNorge, faktiskTTBilateral ->
                    showIf(faktiskTTNorge.greaterThan(0) and faktiskTTBilateral.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)",
                                    Language.Nynorsk to "Faktisk trygdetid i Noreg og avtaleland (maksimalt 40 år)",
                                    Language.English to "Actual insurance period in Norway and partner countries (maximum 40 years)"
                                )
                            }
                            cell {
                                val faktiskTrygdetidINorgePlusAvtaleland =
                                    trygdetidsdetaljer.faktiskTTNorgePlusFaktiskBilateral_safe
                                includePhrase(
                                    Felles.MaanederText(
                                        faktiskTrygdetidINorgePlusAvtaleland.ifNull(
                                            then = 0
                                        )
                                    )
                                )
                            }
                        }
                    }
                }
                ifNotNull(
                    trygdetidsdetaljer.tellerTTBilateralProRata_safe,
                    trygdetidsdetaljer.nevnerTTBilateralProRata_safe
                ) { tellerProRata, nevnerProRata ->
                    showIf(tellerProRata.greaterThan(0) and nevnerProRata.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Forholdstallet brukt i beregning av uføretrygd",
                                    Language.Nynorsk to "Forholdstalet brukt i utrekning av uføretrygd",
                                    Language.English to "Ratio applied in calculation of insurance period"
                                )
                            }
                            cell {
                                textExpr(
                                    Language.Bokmal to tellerProRata.format() + " / " + nevnerProRata.format(),
                                    Language.Nynorsk to tellerProRata.format() + " / " + nevnerProRata.format(),
                                    Language.English to tellerProRata.format() + " / " + nevnerProRata.format()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

