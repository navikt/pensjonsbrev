package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.maler.legacy.PE
import java.time.LocalDate

data class TBU010V(val pe: Expression<PE>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
                //IF(isEmpty(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforetidspunkt) = false) THEN      INCLUDE ENDIF
                ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforetidspunkt()){ uforetidspunkt ->
                    //[TBU010V]

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

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_BeregningsgrunnlagOrdinerArsbelop <> 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_beregningsgrunnlagordinerarsbelop().notEqualTo(0))){
                    //[TBU010V]

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
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_beregningsgrunnlagordinerarsbelop()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_beregningsgrunnlagordinerarsbelop()
                                    .format(false) + " kr" },
                                english { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_beregningsgrunnlagordinerarsbelop()
                                    .format(false) + " NOK" },
                            )
                        }

                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeArsbelop > 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadearsbelop().greaterThan(0))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Beregningsgrunnlag yrkesskade" },
                                nynorsk { + "Berekningsgrunnlag yrkesskade" },
                                english { + "Basis for calculation due to occupational injury " },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadearsbelop()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadearsbelop()
                                    .format(false) + " kr" },
                                english { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadearsbelop()
                                    .format(false) + " NOK" },
                            )
                        }

                    }
                }

                //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt) > 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().greaterThan(0))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Inntekt før uførhet" },
                                nynorsk { + "Inntekt før uførleik" },
                                english { + "Income prior to disability" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format(false) + " kr" },
                                english { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format(false) + " NOK" },
                            )
                        }

                    }
                }

                //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt) > 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().greaterThan(0))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Inntekt etter uførhet" },
                                nynorsk { + "Inntekt etter uførleik" },
                                english { + "Income after disability" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format(false) + " kr" },
                                english { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format(false) + " NOK" },
                            )
                        }

                    }
                }
                //[TBU010V]

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
                            bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " %" },
                            nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " %" },
                            english { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " %" },
                        )
                    }

                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense > 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().greaterThan(0))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Inntektsgrense" },
                                nynorsk { + "Inntektsgrense" },
                                english { + "Income cap" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.ut_inntektsgrense_faktisk().format(false) + " kr" },
                                nynorsk { + pe.ut_inntektsgrense_faktisk().format(false) + " kr" },
                                english { + pe.ut_inntektsgrense_faktisk().format(false) + " NOK" },
                            )
                        }

                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt().greaterThan(0))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Forventet inntekt" },
                                nynorsk { + "Forventa inntekt" },
                                english { + "Expected income" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt()
                                    .format(false) + " kr" },
                                english { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt()
                                    .format(false) + " NOK" },
                            )
                        }

                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad().greaterThan(0.0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Kompensasjonsgrad" },
                                nynorsk { + "Kompensasjonsgrad" },
                                english { + "Percentage of compensation" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                                    .format() + " %" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                                    .format() + " %" },
                                english { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                                    .format() + " %" },
                            )
                        }

                    }
                }
                //[TBU010V]

                row {
                    cell {
                        text(
                            bokmal { + "Inntekt som medfører at uføretrygden ikke blir utbetalt" },
                            nynorsk { + "Inntekt som fører til at uføretrygda ikkje blir utbetalt" },
                            english { + "Income that will lead to no payment of your disability benefit" },
                        )
                    }
                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
                    cell {
                        showIf(
                            (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()
                                .lessThan(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()))
                        ) {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak().format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak().format(false) + " kr" },
                                english { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak().format(false) + " NOK" },
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense >= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
                        showIf(
                            (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()
                                .greaterThanOrEqual(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()))
                        ) {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format(false) + " kr" },
                                english { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format(false) + " NOK" },
                            )
                        }
                    }

                }

                //IF( (PE_pebrevkode = "PE_UT_04_300" OR PE_pebrevkode = "PE_UT_14_300")  OR  (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_Sats <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget <> true)   ) THEN      INCLUDE ENDIF
                showIf(((pe.pebrevkode().equalTo("PE_UT_04_300") or pe.pebrevkode().equalTo("PE_UT_14_300")) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().notEqualTo(0.0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget().not()))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Sivilstatus lagt til grunn ved beregningen" },
                                nynorsk { + "Sivilstatus lagd til grunn ved berekninga" },
                                english { + "Marital status applied to calculation" },
                            )
                        }

                        cell {//IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "gift men lever adskilt" AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand = "gift") OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "separert bor med ektefelle") THEN      INCLUDE ENDIF
                            showIf(
                                ((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("gift men lever adskilt") and pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("gift")) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("separert bor med ektefelle"))
                            ) {
                                text(
                                    bokmal { + "Gift" },
                                    nynorsk { + "Gift" },
                                    english { + "Married" },
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "registrert partner men lever adskilt" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "separert bormed partner") THEN      INCLUDE ENDIF
                            showIf(
                                (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("registrert partner men lever adskilt") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("separert bormed partner"))
                            ) {
                                text(
                                    bokmal { + "Partner" },
                                    nynorsk { + "Partnar" },
                                    english { + "Partnership" },
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "separert bormed 3-2" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "gift ektefelle bormed 3-2" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "registrert partner bormed 3-2" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "separert partner bormed 3-2") THEN      INCLUDE ENDIF
                            showIf(
                                (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("separert bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("gift ektefelle bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("registrert partner bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("separert partner bormed 3-2"))
                            ) {
                                text(
                                    bokmal { + "Samboer (jf. folketrygdloven § 12-13)" },
                                    nynorsk { + "Sambuar (jf. folketrygdlova § 12-13)" },
                                    english { + "Cohabitation (cf. Section 12-13 of the National Insurance Act)" },
                                )
                            }

                            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 1-5")) {
                                text(
                                    bokmal { + "Samboer (jf. folketrygdloven § 1-5)" },
                                    nynorsk { + "Sambuar (jf. folketrygdlova § 1-5)" },
                                    english { + "Cohabitation (cf. Section 1-5 of the National Insurance Act)" },
                                )
                            }

                            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand = "enke"
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("enke")) {
                                text(
                                    bokmal { + "Enke/Enkemann " },
                                    nynorsk { + "Enkje/Enkjemann " },
                                    english { + "Widow/widower " },
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "enslig" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "enslig separert" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "enslig separert partner"  OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "gift men lever adskilt" AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand = "sepr") ) THEN      INCLUDE ENDIF
                            showIf(
                                (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("enslig") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("enslig separert") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("enslig separert partner") or (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("gift men lever adskilt") and pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("sepr")))
                            ) {
                                text(
                                    bokmal { + "Enslig" },
                                    nynorsk { + "Einsleg" },
                                    english { + "Single" },
                                )
                            }
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_registrert_partner_men_lever_adskilt) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("registrert partner men lever adskilt".expr()))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Du eller partneren er registrert med annet bosted, eller er på institusjon" },
                                nynorsk { + "Du eller partnaren er registrert med annan bustad, eller er på institusjon" },
                                english { + "You have, or your partner has, been registered as having a different address, or as living in an institution" },
                            )
                        }
                        cell {
                            includePhrase(Ja)
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_gift_men_lever_adskilt AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand = "gift") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("gift men lever adskilt") and pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("gift"))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Du eller ektefellen er registrert med annet bosted, eller er på institusjon" },
                                nynorsk { + "Du eller ektefellen er registrert med annan bustad, eller er på institusjon" },
                                english { + "You have, or your spouse has, been registered as having a different address, or as living in an institution" },
                            )
                        }
                        cell {
                            includePhrase(Ja)
                        }
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat) = "oppfylt") THEN      INCLUDE ENDIF
                showIf(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat().equalTo("oppfylt")){
                    //[TBU010V]

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

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad <> 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().notEqualTo(0))) {
                    //[TBU010V]

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
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " %" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " %" },
                                english { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " %" },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad <> 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().notEqualTo(0))){
                    ifNotNull(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_skadetidspunkt()) { skadetidspunkt ->
                        //[TBU010V]

                        row {
                            cell {
                                text(
                                    bokmal { + "Skadetidspunktet for yrkesskaden" },
                                    nynorsk { + "Skadetidspunktet for yrkesskaden" },
                                    english { + "Date of injury" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + skadetidspunkt.format() },
                                    nynorsk { + skadetidspunkt.format() },
                                    english { + skadetidspunkt.format() },
                                )
                            }
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad <> 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().notEqualTo(0))) {
                    //[TBU010V]

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
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format(false) + " kr" },
                                english { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format(false) + " NOK" },
                            )
                        }
                    }
                }

                //IF(FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = true) THEN      INCLUDE ENDIF
                showIf(pe.grunnlag_persongrunnlagsliste_brukerflyktning()) {
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Du er innvilget flyktningstatus fra UDI" },
                                nynorsk { + "Du er innvilga flyktningstatus frå UDI" },
                                english { + "You have been granted status as a refugee by the Norwegian Directorate of Immigration (UDI)" },
                            )
                        }
                        cell {
                            includePhrase(Ja)
                        }
                    }
                }

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd"
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd")){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Trygdetid (maksimalt 40 år)" },
                                nynorsk { + "Trygdetid (maksimalt 40 år)" },
                                english { + "Insurance period (maximum 40 years)" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år" },
                                english { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " years" },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "eos" OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "nordisk") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos") or pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("nordisk"))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Teoretisk trygdetid i Norge og andre EØS-land som er brukt i beregningen (maksimalt 40 år)" },
                                nynorsk { + "Teoretisk trygdetid i Noreg og andre EØS-land som er brukt i berekninga (maksimalt 40 år)" },
                                english { + "Theoretical insurance period in Norway and other EEA countries used in the calculation (maximum 40 years)" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år" },
                                english { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " years" },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "eos" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "nordisk" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "folketrygd") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("eos") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("nordisk") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("folketrygd"))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Teoretisk trygdetid i Norge og andre avtaleland som er brukt i beregningen (maksimalt 40 år)" },
                                nynorsk { + "Teoretisk trygdetid i Noreg og andre avtaleland som er brukt i berekninga (maksimalt 40 år)" },
                                english { + "Theoretical insurance period in Norway and other partner countries used in the calculation (maximum 40 years)" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år" },
                                english { + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " years" },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "folketrygd") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("folketrygd"))){
                    //[TBU010V]

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
                                bokmal { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fattnorge().format() + " måneder" },
                                nynorsk { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fattnorge().format() + " månader" },
                                english { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fattnorge().format() + " months" },
                            )
                        }
                    }
                }

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "eos"
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos")){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Faktisk trygdetid i andre EØS-land" },
                                nynorsk { + "Faktisk trygdetid i andre EØS-land" },
                                english { + "Actual insurance period(s) in other EEA countries" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fatteos().format() + " måneder" },
                                nynorsk { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fatteos().format() + " månader" },
                                english { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fatteos().format() + " months" },
                            )
                        }
                    }
                }

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "eos"
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos")){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Faktisk trygdetid i Norge og EØS-land (maksimalt 40 år)" },
                                nynorsk { + "Faktisk trygdetid i Noreg og EØS-land (maksimalt 40 år)" },
                                english { + "Actual insurance period in Norway and partner countries (maximum 40 years)" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.ut_sum_fattnorge_fatteos().format() + " måneder" },
                                nynorsk { + pe.ut_sum_fattnorge_fatteos().format() + " månader" },
                                english { + pe.ut_sum_fattnorge_fatteos().format() + " months" },
                            )
                        }
                    }
                }

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "eos"
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos")){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Forholdstallet brukt i beregning av trygdetid" },
                                nynorsk { + "Forholdstalet brukt ved berekning av trygdetid" },
                                english { + "Ratio applied in calculation of insurance period" },
                            )
                        }
                        cell {
                            includePhrase(
                                BroekText(
                                    pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_tttellereos(),
                                    pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnevnereos()
                                )
                            )
                        }
                    }
                }

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "nordisk"
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("nordisk")){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Faktisk trygdetid i annet nordisk land som brukes i beregning av framtidig trygdetid" },
                                nynorsk { + "Faktisk trygdetid i anna nordisk land som blir brukt ved berekning av framtidig trygdetid" },
                                english { + "Actual insurance period in another Nordic country, applied in calculation of future insurance period(s)" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnordisk().format() + " måneder" },
                                nynorsk { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnordisk().format() + " månader" },
                                english { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnordisk().format() + " months" },
                            )
                        }
                    }
                }

                //IF(  (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd" OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "nordisk")  AND  (FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk)/12<40)  )  THEN      INCLUDE ENDIF

                //[TBU010V]
                showIf(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigttnorsk().lessThan(480)
                        and (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd") or pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("nordisk"))) {
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
                                bokmal { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigttnorsk().format() + " måneder" },
                                nynorsk { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigttnorsk().format() + " månader" },
                                english { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigttnorsk().format() + " months" },
                            )
                        }
                    }
                }

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "nordisk"
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("nordisk")){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Forholdstallet brukt i reduksjon av norsk framtidig trygdetid" },
                                nynorsk { + "Forholdstalet brukt ved reduksjon av norsk framtidig trygdetid" },
                                english { + "Ratio applied in reduction of future Norwegian insurance period" },
                            )
                        }
                        cell {
                            includePhrase(
                                BroekText(
                                    pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_tttellernordisk(),
                                    pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnevnernordisk()
                                )
                            )
                        }
                    }
                }

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "nordisk"
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("nordisk")){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Samlet trygdetid brukt i beregning av uføretrygd etter reduksjon av framtidig trygdetid" },
                                nynorsk { + "Samla trygdetid brukt ved berekning av uføretrygd etter reduksjon av framtidig trygdetid" },
                                english { + "Total insurance period applied in calculating disability benefit after reduction of future insurance period(s)" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.ut_sum_fattnorge_fatt_a10_netto().format() + " måneder" },
                                nynorsk { + pe.ut_sum_fattnorge_fatt_a10_netto().format() + " månader" },
                                english { + pe.ut_sum_fattnorge_fatt_a10_netto().format() + " months" },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "folketrygd" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "eos" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "nordisk") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("folketrygd") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("eos") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("nordisk"))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Faktisk trygdetid i annet avtaleland " },
                                nynorsk { + "Faktisk trygdetid i anna avtaleland" },
                                english { + "Actual insurance period(s) in another partner country" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ttutlandtrygdeavtaleliste_ttutlandtrygdeavtale_fattbilateral().format() + " måneder" },
                                nynorsk { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ttutlandtrygdeavtaleliste_ttutlandtrygdeavtale_fattbilateral().format() + " månader" },
                                english { + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ttutlandtrygdeavtaleliste_ttutlandtrygdeavtale_fattbilateral().format() + " months" },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "folketrygd" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "eos" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "nordisk") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("folketrygd") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("eos") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("nordisk"))){
                    //[TBU010V]

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
                                bokmal { + pe.ut_sum_fattnorge_fattbilateral().format() + " måneder" },
                                nynorsk { + pe.ut_sum_fattnorge_fattbilateral().format() + " månader" },
                                english { + pe.ut_sum_fattnorge_fattbilateral().format() + " months" },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "folketrygd" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "eos" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode <> "nordisk") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("folketrygd") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("eos") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("nordisk"))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Forholdstallet brukt i beregning av uføretrygd" },
                                nynorsk { + "Forholdstalet brukt i berekning av uføretrygd" },
                                english { + "Ratio applied in calculation of insurance" },
                            )
                        }
                        cell {
                            includePhrase(
                                BroekText(
                                    pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_proratabrokteller(),
                                    pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_proratabroknevner()
                                )
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_Kravhode_KravGjelder = "f_bh_bo_utl" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravgjelder().equalTo("f_bh_bo_utl") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "År med inntekt over folketrygdens grunnbeløp før uføretidspunktet" },
                                nynorsk { + "År med inntekt over grunnbeløpet i folketrygda før uføretidspunktet" },
                                english { + "Years of income exceeding the National Insurance basic amount at date of disability" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarover1g()
                                    .format() + " år" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarover1g()
                                    .format() + " år" },
                                english { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarover1g()
                                    .format() + " years" },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_AntallArInntektIAvtaleland <> 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarinntektiavtaleland().notEqualTo(0))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "År med inntekt i utlandet brukt i beregningen" },
                                nynorsk { + "År med inntekt i utlandet" },
                                english { + "Years with income abroad" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarinntektiavtaleland()
                                    .format() + " år" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarinntektiavtaleland()
                                    .format() + " år" },
                                english { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarinntektiavtaleland()
                                    .format() + " years" },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Totalt antall barn du har barnetillegg for" },
                                nynorsk { + "Totalt tal barn du har barnetillegg for" },
                                english { + "Total number of children for whom you receive child supplement" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.ut_antallbarnserkullogfelles().format() },
                                nynorsk { + pe.ut_antallbarnserkullogfelles().format() },
                                english { + pe.ut_antallbarnserkullogfelles().format() },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_AndelYtelseAvOIFU > 95 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom >= DateValue("01/01/2016")) THEN      INCLUDE ENDIF
                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu().greaterThan(95.0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().ifNull(LocalDate.of(2000,1,1)).greaterThanOrEqual(LocalDate.of(2016,1,1))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_prosentsatsoifufortak().format() + " % av inntekt før uførhet (justert for endringer i grunnbeløpet)" },
                                nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_prosentsatsoifufortak().format() + " % av inntekt før uførleik (justert for endringar i grunnbeløpet)" },
                                english { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_prosentsatsoifufortak().format() + " % of income before disability, adjusted for changes in the basic amount" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format(false) + " kr" },
                                nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format(false) + " kr" },
                                english { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format(false) + " NOK" },
                            )
                        }
                    }
                }

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Fribeløp for særkullsbarn" },
                                nynorsk { + "Fribeløp for særkullsbarn" },
                                english { + "Exemption amount for children from a previous relationship" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop()
                                    .format(false) + " kr" },
                                english { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop()
                                    .format(false) + " NOK" },
                            )
                        }
                    }
                }

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Fribeløp for fellesbarn" },
                                nynorsk { + "Fribeløp for fellesbarn" },
                                english { + "Exemption amount for joint children" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                    .format(false) + " kr" },
                                english { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                    .format(false) + " NOK" },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() or pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Samlet inntekt som er brukt i fastsettelse av barnetillegg" },
                                nynorsk { + "Inntekt for deg som er brukt i berekning av barnetillegg" },
                                english { + "Your income, which is used to calculate child supplement" },
                            )
                        }

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false
                        cell {
                            showIf(not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) {
                                text(
                                    bokmal { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format(false) },
                                    nynorsk { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format(false) },
                                    english { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format(false) },
                                )
                            }

                            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                                text(
                                    bokmal { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning().format(false) },
                                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format(false) },
                                    english { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format(false) },
                                )
                            }
                            text(
                                bokmal { + " kr" },
                                nynorsk { + " kr" },
                                english { + " NOK" },
                            )
                        }
                    }
                }

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Samlet inntekt til annen forelder som er brukt i fastsettelse av barnetillegg" },
                                nynorsk { + "Inntekt til annan forelder som er brukt i berekning av barnetillegg" },
                                english { + "Income of the other parent, which is used to calculate child supplement" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format(false) + " kr" },
                                nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format(false) + " kr" },
                                english { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format(false) + " NOK" },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt > 0) THEN      INCLUDE ENDIF
                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().greaterThan(0)){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Beløp som er trukket fra annen forelders inntekt (inntil 1G)" },
                                nynorsk { + "Beløp som er trekt frå inntekta til ein annan forelder (inntil 1G)" },
                                english { + "Amount deducted from the other parent's income (up to 1G)" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt()
                                    .format(false) + " kr" },
                                english { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt()
                                    .format(false) + " NOK" },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom >= DateValue("01/01/2016")) THEN      INCLUDE ENDIF
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().ifNull(LocalDate.of(2000,1,1)).greaterThanOrEqual(LocalDate.of(2016,1,1).expr())){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Samlet inntekt for deg som gjør at barnetillegget ikke blir utbetalt" },
                                nynorsk { + "Samla inntekt som gjer at barnetillegget ikkje blir utbetalt" },
                                english { + "Your income which means that no child supplement is received" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak()
                                    .format(false) + " kr" },
                                english { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak()
                                    .format(false) + " NOK" },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom >= DateValue("01/01/2016")) THEN      INCLUDE ENDIF
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().ifNull(LocalDate.of(2000,1,1)).greaterThanOrEqual(LocalDate.of(2016,1,1))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                bokmal { + "Samlet inntekt for deg og annen forelder som gjør at barnetillegget ikke blir utbetalt" },
                                nynorsk { + "Samla inntekt for deg og annan forelder som gjer at barnetillegget ikkje blir utbetalt" },
                                english { + "Total income which means that no child supplement is received" },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak()
                                    .format(false) + " kr" },
                                english { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak()
                                    .format(false) + " NOK" },
                            )
                        }
                    }
                }
            }
        }
    }
}