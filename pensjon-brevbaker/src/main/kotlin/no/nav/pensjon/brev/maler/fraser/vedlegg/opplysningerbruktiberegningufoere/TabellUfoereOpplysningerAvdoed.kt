package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.vedlegg.GjenlevendetilleggInformasjonSelectors.beregningsgrunnlagBeloepAarYrkesskade_safe
import no.nav.pensjon.brev.api.model.vedlegg.GjenlevendetilleggInformasjonSelectors.beregningsgrunnlagBeloepAar_safe
import no.nav.pensjon.brev.api.model.vedlegg.GjenlevendetilleggInformasjonSelectors.erUngUfoer_safe
import no.nav.pensjon.brev.api.model.vedlegg.GjenlevendetilleggInformasjonSelectors.inntektVedSkadetidspunkt_safe
import no.nav.pensjon.brev.api.model.vedlegg.GjenlevendetilleggInformasjonSelectors.ufoeretidspunkt_safe
import no.nav.pensjon.brev.api.model.vedlegg.GjenlevendetilleggInformasjonSelectors.yrkesskadegrad_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.PersonGrunnlagSelectors.avdoedeErFlyktning_safe
import no.nav.pensjon.brev.api.model.vedlegg.PersonGrunnlagSelectors.avdoedesnavn
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldeneAvdoedSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldeneAvdoedSelectors.faktiskTTBilateral
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldeneAvdoedSelectors.faktiskTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldeneAvdoedSelectors.faktiskTTNordiskKonv
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldeneAvdoedSelectors.faktiskTTNorge
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldeneAvdoedSelectors.framtidigTTAvtaleland
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldeneAvdoedSelectors.framtidigTTEOS_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldeneAvdoedSelectors.framtidigTTNorsk
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldeneAvdoedSelectors.nevnerTTBilateralProRata
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldeneAvdoedSelectors.nevnerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldeneAvdoedSelectors.nevnerTTNordiskKonv
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldeneAvdoedSelectors.samletTTNordiskKonv
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldeneAvdoedSelectors.tellerTTBilateralProRata
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldeneAvdoedSelectors.tellerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldeneAvdoedSelectors.tellerTTNordiskKonv
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

/*
IF (GTInnvilget = true
AND KravArsakType <> SOKNAD_BT
AND not(brevkode PE_UT_07_100, PE_UT_05_100)
AND BeregnoingsMetode = FOLKETRYGD
AND NyttGjenlevendetillegg = true
AND not(brevkode PE_UT_04_108, PE_UT_04_109, PE_UT_04_500, PE_UT_07_200, PE_UT_06_300)
AND (brevkode <> PE_UT_04_102
OR (brevkode = PE_UT_04_102
AND KravArsakType <> TILST_DOD)))
THEN INCLUDE
 */

data class TabellUfoereOpplysningerAvdoed(
    val gjenlevendetilleggInformasjon: Expression<OpplysningerBruktIBeregningUTDto.GjenlevendetilleggInformasjon?>,
    val persjonGrunnlag: Expression<OpplysningerBruktIBeregningUTDto.PersonGrunnlag>,
    val trygdetidsdetaljerGjeldeneAvdoed: Expression<OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldeneAvdoed>,


    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table(
                header = {
                    column(3) {
                        text(
                            Language.Bokmal to "Opplysning",
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
                            Language.Bokmal to "Avdødes navn",
                            Language.Nynorsk to "Avdødes navn",
                            Language.English to "The deceased's name"
                        )
                    }
                    cell {
                        val navn = persjonGrunnlag.avdoedesnavn
                        textExpr(
                            Language.Bokmal to navn,
                            Language.Nynorsk to navn,
                            Language.English to navn,
                        )
                    }

                    cell {
                        text(
                            Language.Bokmal to "Uføretidspunkt",
                            Language.Nynorsk to "Uføretidspunkt",
                            Language.English to "Date of disability"
                        )
                    }
                    cell {
                        ifNotNull(gjenlevendetilleggInformasjon.ufoeretidspunkt_safe) {ufoeretidspunkt ->
                            textExpr(
                                Language.Bokmal to ufoeretidspunkt.format(),
                                Language.Nynorsk to ufoeretidspunkt.format(),
                                Language.English to ufoeretidspunkt.format()
                            )
                        }
                    }
                }
                ifNotNull(gjenlevendetilleggInformasjon.beregningsgrunnlagBeloepAar_safe) { beloepAar ->
                    showIf(beloepAar.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Beregningsgrunnlag",
                                    Language.Nynorsk to "Utrekningsgrunnlag",
                                    Language.English to "Basis for calculation"
                                )
                            }
                            cell {
                                includePhrase(Felles.KronerText(beloepAar))
                            }
                        }
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
                showIf(persjonGrunnlag.avdoedeErFlyktning_safe.ifNull(then = false)) {
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
                showIf(not(persjonGrunnlag.avdoedeErFlyktning_safe.ifNull(then = false))) {
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Trygdetid (maksimalt 40 år)",
                                Language.Nynorsk to "Trygdetid (maksimalt 40 år)",
                                Language.English to "Insurance period (maximum 40 years)"
                            )
                        }
                        cell {
                            val anvendtTT = trygdetidsdetaljerGjeldeneAvdoed.anvendtTT.format()
                            textExpr(
                                Language.Bokmal to anvendtTT + " år",
                                Language.Nynorsk to anvendtTT + " år",
                                Language.English to anvendtTT + " years"
                            )
                        }
                    }
                }
                showIf(gjenlevendetilleggInformasjon.erUngUfoer_safe.ifNull(then = false)) {
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
                ifNotNull(gjenlevendetilleggInformasjon.yrkesskadegrad_safe) { yrkesskadegrad ->
                    showIf(yrkesskadegrad.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Yrkesskadegrad",
                                    Language.Nynorsk to "Yrkesskadegrad",
                                    Language.English to "Degree of disability due to occupational injury"
                                )
                            }
                            cell {
                                textExpr(
                                    Language.Bokmal to yrkesskadegrad.format() + " %",
                                    Language.Nynorsk to yrkesskadegrad.format() + " %",
                                    Language.English to yrkesskadegrad.format() + " %"
                                )
                            }
                        }
                    }
                    ifNotNull(gjenlevendetilleggInformasjon.beregningsgrunnlagBeloepAarYrkesskade_safe) { beloep ->
                        showIf(beloep.greaterThan(0)) {
                            row {
                                cell {
                                    text(
                                        Language.Bokmal to "Beregningsgrunnlag yrkesskade",
                                        Language.Nynorsk to "Utrekningsgrunnlag yrkesskade",
                                        Language.English to "Basis for calculation due to occupational injury"
                                    )
                                }
                                cell {
                                    includePhrase(Felles.KronerText(beloep))
                                }
                            }
                        }
                    }
                    ifNotNull(gjenlevendetilleggInformasjon.inntektVedSkadetidspunkt_safe) { inntekt ->
                        showIf(inntekt.greaterThan(0)) {
                            row {
                                cell {
                                    text(
                                        Language.Bokmal to "Årlig arbeidsinntekt på skadetidspunktet",
                                        Language.Nynorsk to "Årleg arbeidsinntekt på skadetidspunktet",
                                        Language.English to "Annual income at the date of injury"
                                    )
                                }
                                cell {
                                    includePhrase(Felles.KronerText(inntekt))
                                }
                            }
                        }
                    }
                }

                ifNotNull(trygdetidsdetaljerGjeldeneAvdoed.faktiskTTNorge) { faktiskTTNorge ->
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
                }

                ifNotNull(trygdetidsdetaljerGjeldeneAvdoed.faktiskTTEOS) { faktiskTTEOS ->
                    showIf(faktiskTTEOS.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Faktisk trygdetid i andre EØS-land",
                                    Language.Nynorsk to "Faktisk trygdetid i andre EØS-land",
                                    Language.English to "Actual insurance period(s) in other EEA countries"
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(faktiskTTEOS))
                            }
                        }
                    }

                    ifNotNull(trygdetidsdetaljerGjeldeneAvdoed.framtidigTTEOS_safe) { framtidigTTEOS ->
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
                    // TODO: Summeres i Exstream!
                    ifNotNull(
                        trygdetidsdetaljerGjeldeneAvdoed.faktiskTTNorge,
                        trygdetidsdetaljerGjeldeneAvdoed.faktiskTTEOS
                    ) { faktiskTTNorge, faktiskTTEOS ->
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
                                    textExpr(
                                        Language.Bokmal to faktiskTTNorge.format() + " + " + faktiskTTEOS.format() + " måneder",
                                        Language.Nynorsk to faktiskTTNorge.format() + " + " + faktiskTTEOS.format() + " månader",
                                        Language.English to faktiskTTNorge.format() + " + " + faktiskTTEOS.format() + " months"
                                    )
                                }
                            }
                        }
                    }

                    ifNotNull(
                        trygdetidsdetaljerGjeldeneAvdoed.tellerTTEOS,
                        trygdetidsdetaljerGjeldeneAvdoed.nevnerTTEOS
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
                }

                ifNotNull(trygdetidsdetaljerGjeldeneAvdoed.faktiskTTNordiskKonv) { faktiskTTNordiskKonv ->
                    row {
                        cell {
                            text(
                                Language.Bokmal to "Faktisk trygdetid i annet nordisk land som brukes i beregning av framtidig trygdetid",
                                Language.Nynorsk to "Faktisk trygdetid i anna nordisk land som blir brukt i utrekning av framtidig trygdetid",
                                Language.English to "Actual insurance period in another Nordic country, applied in calculation of future insurance period(s)"
                            )
                        }
                        cell { includePhrase(Felles.MaanederText(faktiskTTNordiskKonv)) }
                    }
                }

                ifNotNull(trygdetidsdetaljerGjeldeneAvdoed.framtidigTTNorsk) { framtidigTTNorsk ->
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
                            cell {
                                includePhrase(Felles.MaanederText(framtidigTTNorsk))
                            }
                        }
                    }
                }

                ifNotNull(
                    trygdetidsdetaljerGjeldeneAvdoed.tellerTTNordiskKonv,
                    trygdetidsdetaljerGjeldeneAvdoed.nevnerTTNordiskKonv
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

                ifNotNull(trygdetidsdetaljerGjeldeneAvdoed.samletTTNordiskKonv) { samletTTNordiskKonv ->
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

                ifNotNull(trygdetidsdetaljerGjeldeneAvdoed.faktiskTTBilateral) { faktiskTTBilateral ->
                    showIf(faktiskTTBilateral.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Faktisk trygdetid i annet avtaleland",
                                    Language.Nynorsk to "Faktisk trygdetid i anna avtaleland",
                                    Language.English to "Actual insurance period(s) in another partner country"
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(faktiskTTBilateral))
                            }
                        }
                    }
                }

                ifNotNull(trygdetidsdetaljerGjeldeneAvdoed.framtidigTTAvtaleland) { framtidigTTAvtaleland ->
                    showIf(framtidigTTAvtaleland.greaterThan(0)) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "Framtidig trygdetid",
                                    Language.Nynorsk to "Framtidig trygdetid",
                                    Language.English to "Future insurance period in Norway"
                                )
                            }
                            cell {
                                includePhrase(Felles.MaanederText(framtidigTTAvtaleland))
                            }
                        }
                    }
                }

                // TODO: Summeres i Exstream!
                ifNotNull(
                    trygdetidsdetaljerGjeldeneAvdoed.faktiskTTNorge,
                    trygdetidsdetaljerGjeldeneAvdoed.faktiskTTBilateral
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
                                textExpr(
                                    Language.Bokmal to faktiskTTNorge.format() + " + " + faktiskTTBilateral.format() + " måneder",
                                    Language.Nynorsk to faktiskTTNorge.format() + " + " + faktiskTTBilateral.format() + " månader",
                                    Language.English to faktiskTTNorge.format() + " + " + faktiskTTBilateral.format() + " months",
                                )
                            }
                        }
                    }
                }
                ifNotNull(
                    trygdetidsdetaljerGjeldeneAvdoed.tellerTTBilateralProRata,
                    trygdetidsdetaljerGjeldeneAvdoed.nevnerTTBilateralProRata
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
