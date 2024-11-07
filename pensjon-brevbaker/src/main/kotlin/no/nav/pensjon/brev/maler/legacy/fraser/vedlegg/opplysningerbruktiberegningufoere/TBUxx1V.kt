package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.avdod_fremtidig_trygdetid_under_40_aar
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagavdod_brukerflyktning
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagavdod_fodselsnummer
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.maler.legacy.ut_fattnorgeplusfatta10netto_avdod
import no.nav.pensjon.brev.maler.legacy.ut_fattnorgeplusfattbilateral_avdod
import no.nav.pensjon.brev.maler.legacy.ut_fattnorgeplusfatteos_avdod
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_anvendttrygdetid
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodordiner_arsbelop
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodyrkesskadearsbelop
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_inntektvedskadetidspunktet
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_minsteytelsebenyttetungufor
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_uforetidspunkt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_yrkesskadegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_trygdetidavdod_fatt_a10_netto
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_trygdetidavdod_fatteos
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_trygdetidavdod_fattnorge
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_trygdetidavdod_framtidigtteos
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_trygdetidavdod_framtidigttnorsk
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_trygdetidavdod_ttnevnereos
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_trygdetidavdod_ttnevnernordisk
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_trygdetidavdod_ttnordisk
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_trygdetidavdod_tttellereos
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_trygdetidavdod_tttellernordisk
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_fattbilateral
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_framtidigttavtaleland
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_ttnevnerbilateral
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_tttellerbilateral
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class TBUxx1V (val pe: Expression<PE>) : OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt"  AND PE_pebrevkode <> "PE_UT_07_100"  AND PE_pebrevkode <> "PE_UT_05_100"  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd"  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg = true  AND PE_pebrevkode <> "PE_UT_04_108"  AND PE_pebrevkode <> "PE_UT_04_109"  AND PE_pebrevkode <> "PE_UT_04_500" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300" AND (PE_pebrevkode <> "PE_UT_04_102"      OR (PE_pebrevkode = "PE_UT_04_102"     AND PE_Vedtaksdata_Kravhode_KravArsakType <> "tilst_dod"))) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.pebrevkode().notEqualTo("PE_UT_07_100") and pe.pebrevkode().notEqualTo("PE_UT_05_100") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd") and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg() and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_04_500") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode().notEqualTo("PE_UT_06_300") and (pe.pebrevkode().notEqualTo("PE_UT_04_102") or (pe.pebrevkode().equalTo("PE_UT_04_102") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod"))))){

            ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom()) { virkFom ->
                title1 {
                    textExpr(
                        Bokmal to "Opplysninger om avdøde som ligger til grunn for beregningen av gjenlevendetillegget ditt fra ".expr() + virkFom.format(),
                        Nynorsk to "I berekninga av attlevandetillegget har vi brukt desse opplysningane om den avdøde frå ".expr() + virkFom.format(),
                        English to "In calculating your survivor's benefit, we have applied the following data relating to the decedent of ".expr() + virkFom.format(),
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner.",
                        Nynorsk to "Folketrygdas grunnbeløp (G) nytta i berekninga er ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner.",
                        English to "The national insurance basic amount (G) used in the calculation is NOK ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + ".",
                    )
                }
            }

            paragraph {
                table(
                    header = {
                        column(4) {
                            text(
                                Bokmal to "Opplysning",
                                Nynorsk to "Opplysning",
                                English to "Information",
                                Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
                            )
                        }
                        column(columnSpan = 1, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {}
                    }
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "Avdødes fødselsnummer",
                                Nynorsk to "Avdødes fødselsnummer",
                                English to "The decedent's national identity number",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.grunnlag_persongrunnlagavdod_fodselsnummer(),
                                Nynorsk to pe.grunnlag_persongrunnlagavdod_fodselsnummer(),
                                English to pe.grunnlag_persongrunnlagavdod_fodselsnummer(),
                            )
                        }
                    }

                    //IF(IsNull(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_Uforetidspunkt) = false) THEN      INCLUDE ENDIF
                    ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_uforetidspunkt()){uforetidspunkt ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Uføretidspunkt",
                                    Nynorsk to "Uføretidspunkt",
                                    English to "Date of disability",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to uforetidspunkt.format(),
                                    Nynorsk to uforetidspunkt.format(),
                                    English to uforetidspunkt.format(),
                                )
                            }
                        }
                    }

                    row {
                        cell {
                            text(
                                Bokmal to "Beregningsgrunnlag",
                                Nynorsk to "Berekningsgrunnlag",
                                English to "Basis for calculation",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodordiner_arsbelop()
                                    .format() + " kr",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodordiner_arsbelop()
                                    .format() + " kr",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodordiner_arsbelop()
                                    .format() + " NOK",
                            )
                        }
                    }

                    row {
                        cell {
                            text(
                                Bokmal to "Uføregrad",
                                Nynorsk to "Uføregrad",
                                English to "Degree of disability",
                            )
                        }
                        cell {
                            text(
                                Bokmal to "100 %",
                                Nynorsk to "100 %",
                                English to "100 %",
                            )
                        }
                    }

                    //IF(PE_Grunnlag_PersongrunnlagAvdod_BrukerFlyktning = true) THEN      INCLUDE ENDIF
                    showIf((pe.grunnlag_persongrunnlagavdod_brukerflyktning())){

                        row {
                            cell {
                                text(
                                    Bokmal to "Avdøde var innvilget flyktningstatus fra UDI",
                                    Nynorsk to "Avdøde var innvilga flyktningstatus frå UDI",
                                    English to "The decedent had been granted status as a refugee by the Norwegian Directorate of Immigration (UDI)",
                                )
                            }
                            cell {
                                text(
                                    Bokmal to "Ja",
                                    Nynorsk to "Ja",
                                    English to "Yes",
                                )
                            }
                        }
                    }

                    //IF(PE_Grunnlag_PersongrunnlagAvdod_BrukerFlyktning = false) THEN      INCLUDE ENDIF
                    showIf((not(pe.grunnlag_persongrunnlagavdod_brukerflyktning()))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Trygdetid",
                                    Nynorsk to "Trygdetid",
                                    English to "Insurance period",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_anvendttrygdetid()
                                        .format() + " år",
                                    Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_anvendttrygdetid()
                                        .format() + " år",
                                    English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_anvendttrygdetid()
                                        .format() + " years",
                                )
                            }
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_MinsteYtelseBenyttetUngUfor = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_minsteytelsebenyttetungufor())){

                        row {
                            cell {
                                text(
                                    Bokmal to "Ung ufør",
                                    Nynorsk to "Ung ufør",
                                    English to "Young disabled",
                                )
                            }
                            cell {
                                text(
                                    Bokmal to "Ja",
                                    Nynorsk to "Ja",
                                    English to "Yes",
                                )
                            }
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_Yrkesskadegrad <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_yrkesskadegrad().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Yrkesskadegrad",
                                    Nynorsk to "Yrkesskadegrad",
                                    English to "Degree of disability due to occupational injury",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_yrkesskadegrad()
                                        .format() + " %",
                                    Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_yrkesskadegrad()
                                        .format() + " %",
                                    English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_yrkesskadegrad()
                                        .format() + " %",
                                )
                            }
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodYrkesskadeArsbelop > 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodyrkesskadearsbelop().greaterThan(0))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Beregningsgrunnlag yrkesskade",
                                    Nynorsk to "Beregningsgrunnlag yrkesskade",
                                    English to "Basis for calculation due to occupational injury",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodyrkesskadearsbelop()
                                        .format() + " kr",
                                    Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodyrkesskadearsbelop()
                                        .format() + " kr",
                                    English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodyrkesskadearsbelop()
                                        .format() + " NOK",
                                )
                            }
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_InntektVedSkadetidspunktet > 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_inntektvedskadetidspunktet().greaterThan(0))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Årlig arbeidsinntekt på skadetidspunktet",
                                    Nynorsk to "Årleg arbeidsinntekt på skadetidspunktet",
                                    English to "Annual income at the date of injury",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_inntektvedskadetidspunktet()
                                        .format() + " kr",
                                    Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_inntektvedskadetidspunktet()
                                        .format() + " kr",
                                    English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_inntektvedskadetidspunktet()
                                        .format() + " NOK",
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FaTTNorge,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_fattnorge().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Faktisk trygdetid i Norge",
                                    Nynorsk to "Faktisk trygdetid i Noreg",
                                    English to "Actual insurance period in Norway",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_trygdetidavdod_fattnorge().format() + " måneder",
                                    Nynorsk to pe.vedtaksdata_trygdetidavdod_fattnorge().format() + " månader",
                                    English to pe.vedtaksdata_trygdetidavdod_fattnorge().format() + " months",
                                )
                            }
                        }
                    }

                    //IF( FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FaTTEOS, 1) <> 0 ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_fatteos().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Faktisk trygdetid i andre EØS-land",
                                    Nynorsk to "Faktisk trygdetid i andre EØS-land",
                                    English to "Actual insurance period(s) in other EEA countries ",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_trygdetidavdod_fatteos().format() + " måneder",
                                    Nynorsk to pe.vedtaksdata_trygdetidavdod_fatteos().format() + " månader",
                                    English to pe.vedtaksdata_trygdetidavdod_fatteos().format() + " months",
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FramtidigTTEOS,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_framtidigtteos().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Framtidig trygdetid i Norge og andre EØS-land",
                                    Nynorsk to "Framtidig trygdetid i Noreg og andre EØS-land",
                                    English to "Future insurance period(s) in Norway and other EEA countries",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_trygdetidavdod_framtidigtteos().format() + " måneder",
                                    Nynorsk to pe.vedtaksdata_trygdetidavdod_framtidigtteos().format() + " månader",
                                    English to pe.vedtaksdata_trygdetidavdod_framtidigtteos().format() + " months",
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FaTTNorge,1) <> 0 AND FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FaTTEOS,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_fattnorge().notEqualTo(0) and pe.vedtaksdata_trygdetidavdod_fatteos().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)",
                                    Nynorsk to "Faktisk trygdetid i Noreg og avtaleland (maksimalt 40 år)",
                                    English to "Actual insurance period in Norway and partner countries (maximum 40 years)",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.ut_fattnorgeplusfatteos_avdod().format() + " måneder",
                                    Nynorsk to pe.ut_fattnorgeplusfatteos_avdod().format() + " månader",
                                    English to pe.ut_fattnorgeplusfatteos_avdod().format() + " months",
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTTellerEOS,1) <> 0 AND FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTNevnerEOS,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_tttellereos().notEqualTo(0) and pe.vedtaksdata_trygdetidavdod_ttnevnereos().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Forholdstallet brukt ved beregning av trygdetid",
                                    Nynorsk to "Forholdstalet brukt ved berekning av trygdetid",
                                    English to "Ratio applied in calculation of insurance period",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_trygdetidavdod_tttellereos().format() + "/" + pe.vedtaksdata_trygdetidavdod_ttnevnereos()
                                        .format(),
                                    Nynorsk to pe.vedtaksdata_trygdetidavdod_tttellereos()
                                        .format() + "/" + pe.vedtaksdata_trygdetidavdod_ttnevnereos().format(),
                                    English to pe.vedtaksdata_trygdetidavdod_tttellereos()
                                        .format() + "/" + pe.vedtaksdata_trygdetidavdod_ttnevnereos().format(),
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTNordisk,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_ttnordisk().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Faktisk trygdetid i annet nordisk land som beregner framtidig trygdetid",
                                    Nynorsk to "Faktisk trygdetid i anna nordisk land som blir brukt ved berekning av framtidig trygdetid",
                                    English to "Actual insurance period in another Nordic country, applied in calculation of future insurance period(s)",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_trygdetidavdod_ttnordisk().format() + " måneder",
                                    Nynorsk to pe.vedtaksdata_trygdetidavdod_ttnordisk().format() + " månader",
                                    English to pe.vedtaksdata_trygdetidavdod_ttnordisk().format() + " months",
                                )
                            }
                        }
                    }

                    showIf(pe.avdod_fremtidig_trygdetid_under_40_aar()){
                        row {
                            cell {
                                text(
                                    Bokmal to "Norsk framtidig trygdetid",
                                    Nynorsk to "Norsk framtidig trygdetid",
                                    English to "Future insurance period in Norway",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_trygdetidavdod_framtidigttnorsk().format() + " måneder",
                                    Nynorsk to pe.vedtaksdata_trygdetidavdod_framtidigttnorsk().format() + " månader",
                                    English to pe.vedtaksdata_trygdetidavdod_framtidigttnorsk().format() + " months",
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTTellerNordisk,1) <> 0 AND FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTNevnerNordisk,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_tttellernordisk().notEqualTo(0) and pe.vedtaksdata_trygdetidavdod_ttnevnernordisk().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Forholdstallet brukt ved avkorting av norsk framtidig trygdetid",
                                    Nynorsk to "Forholdstalet brukt ved reduksjon av norsk framtidig trygdetid",
                                    English to "Ratio applied in reduction of future Norwegian insurance period",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_trygdetidavdod_tttellernordisk()
                                        .format() + "/" + pe.vedtaksdata_trygdetidavdod_ttnevnernordisk().format(),
                                    Nynorsk to pe.vedtaksdata_trygdetidavdod_tttellernordisk()
                                        .format() + "/" + pe.vedtaksdata_trygdetidavdod_ttnevnernordisk().format(),
                                    English to pe.vedtaksdata_trygdetidavdod_tttellernordisk()
                                        .format() + "/" + pe.vedtaksdata_trygdetidavdod_ttnevnernordisk().format(),
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FaTTNorge,1) <> 0 AND FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FaTT_A10_netto,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_fattnorge().notEqualTo(0) and pe.vedtaksdata_trygdetidavdod_fatt_a10_netto().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Samlet trygdetid brukt ved beregning av uføretrygd etter avkorting av framtidig tid",
                                    Nynorsk to "Samla trygdetid brukt ved berekning av uføretrygd etter reduksjon av framtidig tid",
                                    English to "Total insurance period applied in calculating disability benefit after reduction of future insurance period(s)",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.ut_fattnorgeplusfatta10netto_avdod().format() + " måneder",
                                    Nynorsk to pe.ut_fattnorgeplusfatta10netto_avdod().format() + " månader",
                                    English to pe.ut_fattnorgeplusfatta10netto_avdod().format() + " months",
                                )
                            }
                        }
                    }

                    //IF( FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_FaTTBilateral, 1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_fattbilateral().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Faktisk trygdetid i andre avtaleland",
                                    Nynorsk to "Faktisk trygdetid i andre avtaleland",
                                    English to "Actual insurance period(s) in other partner countries",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_fattbilateral().format() + " måneder",
                                    Nynorsk to pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_fattbilateral().format() + " månader",
                                    English to pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_fattbilateral().format() + " months",
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_FramtidigTTAvtaleland,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_framtidigttavtaleland().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Framtidig trygdetid",
                                    Nynorsk to "Framtidig trygdetid",
                                    English to "Future insurance period(s)",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_framtidigttavtaleland().format() + " måneder",
                                    Nynorsk to pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_framtidigttavtaleland().format() + " månader",
                                    English to pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_framtidigttavtaleland().format() + " months",
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FaTTNorge,1) <> 0 AND FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_FaTTBilateral,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_fattnorge().notEqualTo(0) and pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_fattbilateral().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)",
                                    Nynorsk to "Faktisk trygdetid i Noreg og avtaleland (maksimalt 40 år)",
                                    English to "Actual insurance period in Norway and partner countries (maximum 40 years)",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.ut_fattnorgeplusfattbilateral_avdod().format() + " måneder",
                                    Nynorsk to pe.ut_fattnorgeplusfattbilateral_avdod().format() + " månader",
                                    English to pe.ut_fattnorgeplusfattbilateral_avdod().format() + " months",
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_TTTellerBilateral,1) <> 0 AND FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_TTNevnerBilateral,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_tttellerbilateral().notEqualTo(0) and pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_ttnevnerbilateral().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    Bokmal to "Forholdstallet brukt ved beregning av uføretrygd",
                                    Nynorsk to "Forholdstalet brukt ved berekning av uføretrygd",
                                    English to "Ratio applied in calculation of insurance period",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_tttellerbilateral()
                                        .format() + "/" + pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_ttnevnerbilateral().format(),
                                    Nynorsk to pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_tttellerbilateral()
                                        .format() + "/" + pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_ttnevnerbilateral().format(),
                                    English to pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_tttellerbilateral()
                                        .format() + "/" + pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_ttnevnerbilateral().format(),
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}