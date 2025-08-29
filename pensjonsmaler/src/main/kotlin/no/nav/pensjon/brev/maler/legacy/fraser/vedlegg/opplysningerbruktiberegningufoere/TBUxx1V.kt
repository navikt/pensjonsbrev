package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.fraser.common.BroekText
import no.nav.pensjon.brev.maler.fraser.common.Ja
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
import no.nav.pensjon.brev.template.dsl.text

data class TBUxx1V (val pe: Expression<PE>) : OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt"  AND PE_pebrevkode <> "PE_UT_07_100"  AND PE_pebrevkode <> "PE_UT_05_100"  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd"  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg = true  AND PE_pebrevkode <> "PE_UT_04_108"  AND PE_pebrevkode <> "PE_UT_04_109"  AND PE_pebrevkode <> "PE_UT_04_500" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300" AND (PE_pebrevkode <> "PE_UT_04_102"      OR (PE_pebrevkode = "PE_UT_04_102"     AND PE_Vedtaksdata_Kravhode_KravArsakType <> "tilst_dod"))) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.pebrevkode().notEqualTo("PE_UT_07_100") and pe.pebrevkode().notEqualTo("PE_UT_05_100") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd") and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_nyttgjenlevendetillegg() and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_04_500") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode().notEqualTo("PE_UT_06_300") and (pe.pebrevkode().notEqualTo("PE_UT_04_102") or (pe.pebrevkode().equalTo("PE_UT_04_102") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod"))))){

            ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom()) { virkFom ->
                title1 {
                    text(
                        bokmal { + "Opplysninger om avdøde som ligger til grunn for beregningen av gjenlevendetillegget ditt fra " + virkFom.format() },
                        nynorsk { + "I berekninga av attlevandetillegget har vi brukt desse opplysningane om den avdøde frå " + virkFom.format() },
                        english { + "In calculating your survivor's benefit, we have applied the following data relating to the decedent of " + virkFom.format() },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Folketrygdens grunnbeløp (G) benyttet i beregningen er " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + "." },
                        nynorsk { + "Folketrygdas grunnbeløp (G) nytta i berekninga er " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + "." },
                        english { + "The national insurance basic amount (G) used in the calculation is " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + "." },
                    )
                }
            }

            paragraph {
                table(
                    header = {
                        column(4) {
                            text(
                                bokmal { + "Opplysning" },
                                nynorsk { + "Opplysning" },
                                english { + "Information" },
                            )
                        }
                        column(columnSpan = 1, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {}
                    }
                ) {
                    row {
                        cell {
                            text(
                                bokmal { + "Avdødes fødselsnummer" },
                                nynorsk { + "Avdødes fødselsnummer" },
                                english { + "The decedent's national identity number" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.grunnlag_persongrunnlagavdod_fodselsnummer() },
                                nynorsk { + pe.grunnlag_persongrunnlagavdod_fodselsnummer() },
                                english { + pe.grunnlag_persongrunnlagavdod_fodselsnummer() },
                            )
                        }
                    }

                    //IF(IsNull(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_Uforetidspunkt) = false) THEN      INCLUDE ENDIF
                    ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_uforetidspunkt()){uforetidspunkt ->
                        row {
                            cell {
                                text(
                                    bokmal { + "Uføretidspunkt" },
                                    nynorsk { + "Uføretidspunkt" },
                                    english { + "Date of disability" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + uforetidspunkt.format() },
                                    nynorsk { + uforetidspunkt.format() },
                                    english { + uforetidspunkt.format() },
                                )
                            }
                        }
                    }

                    row {
                        cell {
                            text(
                                bokmal { + "Beregningsgrunnlag" },
                                nynorsk { + "Berekningsgrunnlag" },
                                english { + "Basis for calculation" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodordiner_arsbelop()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodordiner_arsbelop()
                                    .format(false) + " kr" },
                                english { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodordiner_arsbelop()
                                    .format(false) + " NOK" },
                            )
                        }
                    }

                    row {
                        cell {
                            text(
                                bokmal { + "Uføregrad" },
                                nynorsk { + "Uføregrad" },
                                english { + "Degree of disability" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + "100 %" },
                                nynorsk { + "100 %" },
                                english { + "100 %" },
                            )
                        }
                    }

                    //IF(PE_Grunnlag_PersongrunnlagAvdod_BrukerFlyktning = true) THEN      INCLUDE ENDIF
                    showIf((pe.grunnlag_persongrunnlagavdod_brukerflyktning())){

                        row {
                            cell {
                                text(
                                    bokmal { + "Avdøde var innvilget flyktningstatus fra UDI" },
                                    nynorsk { + "Avdøde var innvilga flyktningstatus frå UDI" },
                                    english { + "The decedent had been granted status as a refugee by the Norwegian Directorate of Immigration (UDI)" },
                                )
                            }
                            cell {
                                includePhrase(Ja)
                            }
                        }
                    }

                    //IF(PE_Grunnlag_PersongrunnlagAvdod_BrukerFlyktning = false) THEN      INCLUDE ENDIF
                    showIf((not(pe.grunnlag_persongrunnlagavdod_brukerflyktning()))){

                        row {
                            cell {
                                text(
                                    bokmal { + "Trygdetid" },
                                    nynorsk { + "Trygdetid" },
                                    english { + "Insurance period" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_anvendttrygdetid()
                                        .format() + " år" },
                                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_anvendttrygdetid()
                                        .format() + " år" },
                                    english { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_anvendttrygdetid()
                                        .format() + " years" },
                                )
                            }
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_MinsteYtelseBenyttetUngUfor = true) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_minsteytelsebenyttetungufor())){

                        row {
                            cell {
                                text(
                                    bokmal { + "Ung ufør" },
                                    nynorsk { + "Ung ufør" },
                                    english { + "Young disabled" },
                                )
                            }
                            cell {
                                includePhrase(Ja)
                            }
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_Yrkesskadegrad <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_yrkesskadegrad().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    bokmal { + "Yrkesskadegrad" },
                                    nynorsk { + "Yrkesskadegrad" },
                                    english { + "Degree of disability due to occupational injury" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_yrkesskadegrad()
                                        .format() + " %" },
                                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_yrkesskadegrad()
                                        .format() + " %" },
                                    english { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_yrkesskadegrad()
                                        .format() + " %" },
                                )
                            }
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodYrkesskadeArsbelop > 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodyrkesskadearsbelop().greaterThan(0))){

                        row {
                            cell {
                                text(
                                    bokmal { + "Beregningsgrunnlag yrkesskade" },
                                    nynorsk { + "Beregningsgrunnlag yrkesskade" },
                                    english { + "Basis for calculation due to occupational injury" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodyrkesskadearsbelop()
                                        .format(false) + " kr" },
                                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodyrkesskadearsbelop()
                                        .format(false) + " kr" },
                                    english { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_beregningsgrunnlagavdodyrkesskadearsbelop()
                                        .format(false) + " NOK" },
                                )
                            }
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_InntektVedSkadetidspunktet > 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_inntektvedskadetidspunktet().greaterThan(0))){

                        row {
                            cell {
                                text(
                                    bokmal { + "Årlig arbeidsinntekt på skadetidspunktet" },
                                    nynorsk { + "Årleg arbeidsinntekt på skadetidspunktet" },
                                    english { + "Annual income at the date of injury" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_inntektvedskadetidspunktet()
                                        .format(false) + " kr" },
                                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_inntektvedskadetidspunktet()
                                        .format(false) + " kr" },
                                    english { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gjenlevendetillegginformasjon_inntektvedskadetidspunktet()
                                        .format(false) + " NOK" },
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FaTTNorge,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_fattnorge().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    bokmal { + "Faktisk trygdetid i Norge" },
                                    nynorsk { + "Faktisk trygdetid i Noreg" },
                                    english { + "Actual insurance period in Norway" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksdata_trygdetidavdod_fattnorge().format() + " måneder" },
                                    nynorsk { + pe.vedtaksdata_trygdetidavdod_fattnorge().format() + " månader" },
                                    english { + pe.vedtaksdata_trygdetidavdod_fattnorge().format() + " months" },
                                )
                            }
                        }
                    }

                    //IF( FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FaTTEOS, 1) <> 0 ) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_fatteos().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    bokmal { + "Faktisk trygdetid i andre EØS-land" },
                                    nynorsk { + "Faktisk trygdetid i andre EØS-land" },
                                    english { + "Actual insurance period(s) in other EEA countries " },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksdata_trygdetidavdod_fatteos().format() + " måneder" },
                                    nynorsk { + pe.vedtaksdata_trygdetidavdod_fatteos().format() + " månader" },
                                    english { + pe.vedtaksdata_trygdetidavdod_fatteos().format() + " months" },
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FramtidigTTEOS,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_framtidigtteos().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    bokmal { + "Framtidig trygdetid i Norge og andre EØS-land" },
                                    nynorsk { + "Framtidig trygdetid i Noreg og andre EØS-land" },
                                    english { + "Future insurance period(s) in Norway and other EEA countries" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksdata_trygdetidavdod_framtidigtteos().format() + " måneder" },
                                    nynorsk { + pe.vedtaksdata_trygdetidavdod_framtidigtteos().format() + " månader" },
                                    english { + pe.vedtaksdata_trygdetidavdod_framtidigtteos().format() + " months" },
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FaTTNorge,1) <> 0 AND FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FaTTEOS,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_fattnorge().notEqualTo(0) and pe.vedtaksdata_trygdetidavdod_fatteos().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    bokmal { + "Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)" },
                                    nynorsk { + "Faktisk trygdetid i Noreg og avtaleland (maksimalt 40 år)" },
                                    english { + "Actual insurance period in Norway and partner countries (maximum 40 years)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.ut_fattnorgeplusfatteos_avdod().format() + " måneder" },
                                    nynorsk { + pe.ut_fattnorgeplusfatteos_avdod().format() + " månader" },
                                    english { + pe.ut_fattnorgeplusfatteos_avdod().format() + " months" },
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTTellerEOS,1) <> 0 AND FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTNevnerEOS,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_tttellereos().notEqualTo(0) and pe.vedtaksdata_trygdetidavdod_ttnevnereos().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    bokmal { + "Forholdstallet brukt ved beregning av trygdetid" },
                                    nynorsk { + "Forholdstalet brukt ved berekning av trygdetid" },
                                    english { + "Ratio applied in calculation of insurance period" },
                                )
                            }
                            cell {
                                includePhrase(
                                    BroekText(
                                        pe.vedtaksdata_trygdetidavdod_tttellereos(),
                                        pe.vedtaksdata_trygdetidavdod_ttnevnereos(),
                                    )
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTNordisk,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_ttnordisk().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    bokmal { + "Faktisk trygdetid i annet nordisk land som beregner framtidig trygdetid" },
                                    nynorsk { + "Faktisk trygdetid i anna nordisk land som blir brukt ved berekning av framtidig trygdetid" },
                                    english { + "Actual insurance period in another Nordic country, applied in calculation of future insurance period(s)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksdata_trygdetidavdod_ttnordisk().format() + " måneder" },
                                    nynorsk { + pe.vedtaksdata_trygdetidavdod_ttnordisk().format() + " månader" },
                                    english { + pe.vedtaksdata_trygdetidavdod_ttnordisk().format() + " months" },
                                )
                            }
                        }
                    }

                    showIf(pe.avdod_fremtidig_trygdetid_under_40_aar()){
                        row {
                            cell {
                                text(
                                    bokmal { + "Norsk framtidig trygdetid" },
                                    nynorsk { + "Norsk framtidig trygdetid" },
                                    english { + "Future insurance period in Norway" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksdata_trygdetidavdod_framtidigttnorsk().format() + " måneder" },
                                    nynorsk { + pe.vedtaksdata_trygdetidavdod_framtidigttnorsk().format() + " månader" },
                                    english { + pe.vedtaksdata_trygdetidavdod_framtidigttnorsk().format() + " months" },
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTTellerNordisk,1) <> 0 AND FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTNevnerNordisk,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_tttellernordisk().notEqualTo(0) and pe.vedtaksdata_trygdetidavdod_ttnevnernordisk().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    bokmal { + "Forholdstallet brukt ved avkorting av norsk framtidig trygdetid" },
                                    nynorsk { +  "Forholdstalet brukt ved reduksjon av norsk framtidig trygdetid" },
                                    english { +  "Ratio applied in reduction of future Norwegian insurance period" },
                                )
                            }
                            cell {
                                includePhrase(
                                    BroekText(
                                        pe.vedtaksdata_trygdetidavdod_tttellernordisk(),
                                        pe.vedtaksdata_trygdetidavdod_ttnevnernordisk(),
                                    )
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FaTTNorge,1) <> 0 AND FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FaTT_A10_netto,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_fattnorge().notEqualTo(0) and pe.vedtaksdata_trygdetidavdod_fatt_a10_netto().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    bokmal { +  "Samlet trygdetid brukt ved beregning av uføretrygd etter avkorting av framtidig tid" },
                                    nynorsk { +  "Samla trygdetid brukt ved berekning av uføretrygd etter reduksjon av framtidig tid" },
                                    english { +  "Total insurance period applied in calculating disability benefit after reduction of future insurance period(s)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +  pe.ut_fattnorgeplusfatta10netto_avdod().format() + " måneder" },
                                    nynorsk { +  pe.ut_fattnorgeplusfatta10netto_avdod().format() + " månader" },
                                    english { +  pe.ut_fattnorgeplusfatta10netto_avdod().format() + " months" },
                                )
                            }
                        }
                    }

                    //IF( FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_FaTTBilateral, 1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_fattbilateral().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    bokmal { +  "Faktisk trygdetid i andre avtaleland" },
                                    nynorsk { +  "Faktisk trygdetid i andre avtaleland" },
                                    english { +  "Actual insurance period(s) in other partner countries" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +  pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_fattbilateral().format() + " måneder" },
                                    nynorsk { +  pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_fattbilateral().format() + " månader" },
                                    english { +  pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_fattbilateral().format() + " months" },
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_FramtidigTTAvtaleland,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_framtidigttavtaleland().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    bokmal { +  "Framtidig trygdetid" },
                                    nynorsk { +  "Framtidig trygdetid" },
                                    english { +  "Future insurance period(s)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +  pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_framtidigttavtaleland().format() + " måneder" },
                                    nynorsk { +  pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_framtidigttavtaleland().format() + " månader" },
                                    english { +  pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_framtidigttavtaleland().format() + " months" },
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_FaTTNorge,1) <> 0 AND FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_FaTTBilateral,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_fattnorge().notEqualTo(0) and pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_fattbilateral().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    bokmal { +  "Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)" },
                                    nynorsk { +  "Faktisk trygdetid i Noreg og avtaleland (maksimalt 40 år)" },
                                    english { +  "Actual insurance period in Norway and partner countries (maximum 40 years)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +  pe.ut_fattnorgeplusfattbilateral_avdod().format() + " måneder" },
                                    nynorsk { +  pe.ut_fattnorgeplusfattbilateral_avdod().format() + " månader" },
                                    english { +  pe.ut_fattnorgeplusfattbilateral_avdod().format() + " months" },
                                )
                            }
                        }
                    }

                    //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_TTTellerBilateral,1) <> 0 AND FF_GetArrayElement_Float(PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_TTNevnerBilateral,1) <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_tttellerbilateral().notEqualTo(0) and pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_ttnevnerbilateral().notEqualTo(0))){

                        row {
                            cell {
                                text(
                                    bokmal { +  "Forholdstallet brukt ved beregning av uføretrygd" },
                                    nynorsk { +  "Forholdstalet brukt ved berekning av uføretrygd" },
                                    english { +  "Ratio applied in calculation of insurance period" },
                                )
                            }
                            cell {
                                includePhrase(
                                    BroekText(
                                        pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_tttellerbilateral(),
                                        pe.vedtaksdata_trygdetidavdod_ttutlandtrygdeavtale_ttnevnerbilateral()
                                    )
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}