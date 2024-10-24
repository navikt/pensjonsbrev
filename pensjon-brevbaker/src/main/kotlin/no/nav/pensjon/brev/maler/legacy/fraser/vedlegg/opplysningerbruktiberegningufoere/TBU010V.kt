package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

data class TBU010V(val pe: Expression<PE>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
                //IF(isEmpty(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforetidspunkt) = false) THEN      INCLUDE ENDIF
                ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforetidspunkt()){ uforetidspunkt ->
                    //[TBU010V]

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

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_BeregningsgrunnlagOrdinerArsbelop <> 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_beregningsgrunnlagordinerarsbelop().notEqualTo(0))){
                    //[TBU010V]

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
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_beregningsgrunnlagordinerarsbelop()
                                    .format() + " kr",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_beregningsgrunnlagordinerarsbelop()
                                    .format() + " kr",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_beregningsgrunnlagordinerarsbelop()
                                    .format() + " NOK",
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
                                Bokmal to "Beregningsgrunnlag yrkesskade",
                                Nynorsk to "Berekningsgrunnlag yrkesskade",
                                English to "Basis for calculation due to occupational injury ",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadearsbelop()
                                    .format() + " kr",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadearsbelop()
                                    .format() + " kr",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadearsbelop()
                                    .format() + " NOK",
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
                                Bokmal to "Inntekt før uførhet",
                                Nynorsk to "Inntekt før uførleik",
                                English to "Income prior to disability",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kr",
                                Nynorsk to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " kr",
                                English to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt().format() + " NOK",
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
                                Bokmal to "Inntekt etter uførhet",
                                Nynorsk to "Inntekt etter uførleik",
                                English to "Income after disability",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kr",
                                Nynorsk to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " kr",
                                English to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt().format() + " NOK",
                            )
                        }

                    }
                }
                //[TBU010V]

                row {
                    cell {
                        text(
                            Bokmal to "Uføregrad",
                            Nynorsk to "Uføregrad",
                            English to "Degree of disability",
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " %",
                            Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " %",
                            English to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().format() + " %",
                        )
                    }

                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense > 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().greaterThan(0))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                Bokmal to "Inntektsgrense",
                                Nynorsk to "Inntektsgrense",
                                English to "Income cap",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.ut_inntektsgrense_faktisk().format() + " kr",
                                Nynorsk to pe.ut_inntektsgrense_faktisk().format() + " kr",
                                English to pe.ut_inntektsgrense_faktisk().format() + " NOK",
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
                                Bokmal to "Forventet inntekt",
                                Nynorsk to "Forventa inntekt",
                                English to "Expected income",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt()
                                    .format() + " kr",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt()
                                    .format() + " kr",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt()
                                    .format() + " NOK",
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
                                Bokmal to "Kompensasjonsgrad",
                                Nynorsk to "Kompensasjonsgrad",
                                English to "Percentage of compensation",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                                    .format() + " %",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                                    .format() + " %",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                                    .format() + " %",
                            )
                        }

                    }
                }
                //[TBU010V]

                row {
                    cell {
                        text(
                            Bokmal to "Inntekt som medfører at uføretrygden ikke blir utbetalt",
                            Nynorsk to "Inntekt som fører til at uføretrygda ikkje blir utbetalt",
                            English to "Income that will lead to no payment of your disability benefit",
                        )
                    }
                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
                    cell {
                        showIf(
                            (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()
                                .lessThan(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()))
                        ) {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak().format() + " kr",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak().format() + " kr",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak().format() + " NOK",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense >= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
                        showIf(
                            (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()
                                .greaterThanOrEqual(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()))
                        ) {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format() + " kr",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format() + " kr",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().format() + " NOK",
                            )
                        }
                    }

                }

                //IF( (PE_pebrevkode = "PE_UT_04_300" OR PE_pebrevkode = "PE_UT_14_300")  OR  (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_Sats <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget <> true)   ) THEN      INCLUDE ENDIF
                showIf(((pe.pebrevkode().equalTo("PE_UT_04_300") or pe.pebrevkode().equalTo("PE_UT_14_300")) or (pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_minsteytelse_sats().notEqualTo(0) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget().notEqualTo(true)))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                Bokmal to "Sivilstatus lagt til grunn ved beregningen",
                                Nynorsk to "Sivilstatus lagd til grunn ved berekninga",
                                English to "Marital status applied to calculation",
                            )
                        }

                        cell {//IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "gift men lever adskilt" AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand = "gift") OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "separert bor med ektefelle") THEN      INCLUDE ENDIF
                            showIf(
                                ((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("gift men lever adskilt") and pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("gift")) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("separert bor med ektefelle"))
                            ) {
                                text(
                                    Bokmal to "Gift",
                                    Nynorsk to "Gift",
                                    English to "Married",
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "registrert partner men lever adskilt" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "separert bormed partner") THEN      INCLUDE ENDIF
                            showIf(
                                (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("registrert partner men lever adskilt") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("separert bormed partner"))
                            ) {
                                text(
                                    Bokmal to "Partner",
                                    Nynorsk to "Partnar",
                                    English to "Partnership",
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "separert bormed 3-2" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "gift ektefelle bormed 3-2" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "registrert partner bormed 3-2" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "separert partner bormed 3-2") THEN      INCLUDE ENDIF
                            showIf(
                                (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("separert bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("gift ektefelle bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("registrert partner bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("separert partner bormed 3-2"))
                            ) {
                                text(
                                    Bokmal to "Samboer (jf. folketrygdloven § 12-13)",
                                    Nynorsk to "Sambuar (jf. folketrygdlova § 12-13)",
                                    English to "Cohabitation (cf. Section 12-13 of the National Insurance Act)",
                                )
                            }

                            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 1-5")) {
                                text(
                                    Bokmal to "Samboer (jf. folketrygdloven § 1-5)",
                                    Nynorsk to "Sambuar (jf. folketrygdlova § 1-5)",
                                    English to "Cohabitation (cf. Section 1-5 of the National Insurance Act)",
                                )
                            }

                            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand = "enke"
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("enke")) {
                                text(
                                    Bokmal to "Enke/Enkemann ",
                                    Nynorsk to "Enkje/Enkjemann ",
                                    English to "Widow/widower ",
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "enslig" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "enslig separert" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "enslig separert partner"  OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "gift men lever adskilt" AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand = "sepr") ) THEN      INCLUDE ENDIF
                            showIf(
                                (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("enslig") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("enslig separert") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("enslig separert partner") or (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("gift men lever adskilt") and pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("sepr")))
                            ) {
                                text(
                                    Bokmal to "Enslig",
                                    Nynorsk to "Einsleg",
                                    English to "Single",
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
                                Bokmal to "Du eller partneren er registrert med annet bosted, eller er på institusjon",
                                Nynorsk to "Du eller partnaren er registrert med annan bustad, eller er på institusjon",
                                English to "You have, or your partner has, been registered as having a different address, or as living in an institution",
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

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_gift_men_lever_adskilt AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand = "gift") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("gift men lever adskilt") and pe.vedtaksdata_beregningsdata_beregning_beregningbrukersivilstand().equalTo("gift"))){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                Bokmal to "Du eller ektefellen er registrert med annet bosted, eller er på institusjon",
                                Nynorsk to "Du eller ektefellen er registrert med annan bustad, eller er på institusjon",
                                English to "You have, or your spouse has, been registered as having a different address, or as living in an institution",
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

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat) = "oppfylt") THEN      INCLUDE ENDIF
                showIf(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat().equalTo("oppfylt")){
                    //[TBU010V]

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

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad <> 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().notEqualTo(0))) {
                    //[TBU010V]

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
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " %",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " %",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().format() + " %",
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
                                    Bokmal to "Skadetidspunktet for yrkesskaden",
                                    Nynorsk to "Skadetidspunktet for yrkesskaden",
                                    English to "Date of injury",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to skadetidspunkt.format(),
                                    Nynorsk to skadetidspunkt.format(),
                                    English to skadetidspunkt.format(),
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
                                Bokmal to "Årlig arbeidsinntekt på skadetidspunktet",
                                Nynorsk to "Årleg arbeidsinntekt på skadetidspunktet",
                                English to "Annual income at the date of injury",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kr",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " kr",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet().format() + " NOK",
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
                                Bokmal to "Du er innvilget flyktningstatus fra UDI",
                                Nynorsk to "Du er innvilga flyktningstatus frå UDI",
                                English to "You have been granted status as a refugee by the Norwegian Directorate of Immigration (UDI)",
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

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd"
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd")){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                Bokmal to "Trygdetid (maksimalt 40 år)",
                                Nynorsk to "Trygdetid (maksimalt 40 år)",
                                English to "Insurance period (maximum 40 years)",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " years",
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
                                Bokmal to "Teoretisk trygdetid i Norge og andre EØS-land som er brukt i beregningen (maksimalt 40 år)",
                                Nynorsk to "Teoretisk trygdetid i Noreg og andre EØS-land som er brukt i berekninga (maksimalt 40 år)",
                                English to "Theoretical insurance period in Norway and other EEA countries used in the calculation (maximum 40 years)",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " years",
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
                                Bokmal to "Teoretisk trygdetid i Norge og andre avtaleland som er brukt i beregningen (maksimalt 40 år)",
                                Nynorsk to "Teoretisk trygdetid i Noreg og andre avtaleland som er brukt i berekninga (maksimalt 40 år)",
                                English to "Theoretical insurance period in Norway and other partner countries used in the calculation (maximum 40 years)",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " years",
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
                                Bokmal to "Faktisk trygdetid i Norge",
                                Nynorsk to "Faktisk trygdetid i Noreg",
                                English to "Actual insurance period in Norway",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fattnorge().format() + " måneder",
                                Nynorsk to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fattnorge().format() + " månader",
                                English to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fattnorge().format() + " months",
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
                                Bokmal to "Faktisk trygdetid i andre EØS-land",
                                Nynorsk to "Faktisk trygdetid i andre EØS-land",
                                English to "Actual insurance period(s) in other EEA countries",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fatteos().format() + " måneder",
                                Nynorsk to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fatteos().format() + " månader",
                                English to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fatteos().format() + " months",
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
                                Bokmal to "Faktisk trygdetid i Norge og EØS-land (maksimalt 40 år)",
                                Nynorsk to "Faktisk trygdetid i Noreg og EØS-land (maksimalt 40 år)",
                                English to "Actual insurance period in Norway and partner countries (maximum 40 years)",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.ut_sum_fattnorge_fatteos().format() + " måneder",
                                Nynorsk to pe.ut_sum_fattnorge_fatteos().format() + " månader",
                                English to pe.ut_sum_fattnorge_fatteos().format() + " months",
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
                                Bokmal to "Forholdstallet brukt i beregning av trygdetid",
                                Nynorsk to "Forholdstalet brukt ved berekning av trygdetid",
                                English to "Ratio applied in calculation of insurance period",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_tttellereos().format() + "/" + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnevnereos().format(),
                                Nynorsk to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_tttellereos().format() + "/" + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnevnereos().format(),
                                English to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_tttellereos().format() + "/" + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnevnereos().format(),
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
                                Bokmal to "Faktisk trygdetid i annet nordisk land som brukes i beregning av framtidig trygdetid",
                                Nynorsk to "Faktisk trygdetid i anna nordisk land som blir brukt ved berekning av framtidig trygdetid",
                                English to "Actual insurance period in another Nordic country, applied in calculation of future insurance period(s)",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnordisk().format() + " måneder",
                                Nynorsk to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnordisk().format() + " månader",
                                English to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnordisk().format() + " months",
                            )
                        }
                    }
                }
                //Failed to convert with error: Expected token of type CLOSE_PAREN but found NUMBER

                //IF(  (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd" OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "nordisk")  AND  (FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk)/12<40)  )  THEN      INCLUDE ENDIF

                //[TBU010V]

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
                            Bokmal to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigttnorsk().format() + " måneder",
                            Nynorsk to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigttnorsk().format() + " månader",
                            English to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigttnorsk().format() + " months",
                        )
                    }
                }

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "nordisk"
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("nordisk")){
                    //[TBU010V]

                    row {
                        cell {
                            text(
                                Bokmal to "Forholdstallet brukt i reduksjon av norsk framtidig trygdetid",
                                Nynorsk to "Forholdstalet brukt ved reduksjon av norsk framtidig trygdetid",
                                English to "Ratio applied in reduction of future Norwegian insurance period",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_tttellernordisk().format() + "/" + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnevnernordisk().format(),
                                Nynorsk to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_tttellernordisk().format() + "/" + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnevnernordisk().format(),
                                English to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_tttellernordisk().format() + "/" + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_ttnevnernordisk().format(),
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
                                Bokmal to "Samlet trygdetid brukt i beregning av uføretrygd etter reduksjon av framtidig trygdetid",
                                Nynorsk to "Samla trygdetid brukt ved berekning av uføretrygd etter reduksjon av framtidig trygdetid",
                                English to "Total insurance period applied in calculating disability benefit after reduction of future insurance period(s)",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.ut_sum_fattnorge_fatt_a10_netto().format() + " måneder",
                                Nynorsk to pe.ut_sum_fattnorge_fatt_a10_netto().format() + " månader",
                                English to pe.ut_sum_fattnorge_fatt_a10_netto().format() + " months",
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
                                Bokmal to "Faktisk trygdetid i annet avtaleland ",
                                Nynorsk to "Faktisk trygdetid i anna avtaleland",
                                English to "Actual insurance period(s) in another partner country",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ttutlandtrygdeavtaleliste_ttutlandtrygdeavtale_fattbilateral().format() + " måneder",
                                Nynorsk to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ttutlandtrygdeavtaleliste_ttutlandtrygdeavtale_fattbilateral().format() + " månader",
                                English to pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ttutlandtrygdeavtaleliste_ttutlandtrygdeavtale_fattbilateral().format() + " months",
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
                                Bokmal to "Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)",
                                Nynorsk to "Faktisk trygdetid i Noreg og avtaleland (maksimalt 40 år)",
                                English to "Actual insurance period in Norway and partner countries (maximum 40 years)",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.ut_sum_fattnorge_fattbilateral().format() + " måneder",
                                Nynorsk to pe.ut_sum_fattnorge_fattbilateral().format() + " månader",
                                English to pe.ut_sum_fattnorge_fattbilateral().format() + " months",
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
                                Bokmal to "Forholdstallet brukt i beregning av uføretrygd",
                                Nynorsk to "Forholdstalet brukt i berekning av uføretrygd",
                                English to "Ratio applied in calculation of insurance",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_proratabrokteller()
                                    .format() + "/" + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_proratabroknevner().format(),
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_proratabrokteller()
                                    .format() + "/" + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_proratabroknevner().format(),
                                English to pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_proratabrokteller()
                                    .format() + "/" + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_proratabroknevner().format(),
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
                                Bokmal to "År med inntekt over folketrygdens grunnbeløp før uføretidspunktet",
                                Nynorsk to "År med inntekt over grunnbeløpet i folketrygda før uføretidspunktet",
                                English to "Years of income exceeding the National Insurance basic amount at date of disability",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarover1g()
                                    .format() + " år",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarover1g()
                                    .format() + " år",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarover1g()
                                    .format() + " years",
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
                                Bokmal to "År med inntekt i utlandet brukt i beregningen",
                                Nynorsk to "År med inntekt i utlandet",
                                English to "Years with income abroad",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarinntektiavtaleland()
                                    .format() + " år",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarinntektiavtaleland()
                                    .format() + " år",
                                English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_antallarinntektiavtaleland()
                                    .format() + " years",
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
                                Bokmal to "Totalt antall barn du har barnetillegg for",
                                Nynorsk to "Totalt tal barn du har barnetillegg for",
                                English to "Total number of children for whom you receive child supplement",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.ut_antallbarnserkullogfelles().format(),
                                Nynorsk to pe.ut_antallbarnserkullogfelles().format(),
                                English to pe.ut_antallbarnserkullogfelles().format(),
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_AndelYtelseAvOIFU > 95 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom >= DateValue("01/01/2016")) THEN      INCLUDE ENDIF
                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().greaterThan(0) and pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu().greaterThan(95.0) and pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom().ifNull(LocalDate.of(2000,1,1)).greaterThanOrEqual(LocalDate.of(2016,1,1))){
                    //[TBU010V]

                    row {
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_prosentsatsoifufortak().format() + " % av inntekt før uførhet (justert for endringer i grunnbeløpet)",
                                Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_prosentsatsoifufortak().format() + " % av inntekt før uførleik (justert for endringar i grunnbeløpet)",
                                English to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_prosentsatsoifufortak().format() + " % of income before disability, adjusted for changes in the basic amount",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + " kr",
                                Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + " kr",
                                English to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_gradertoppjustertifu().format() + " NOK",
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
                                Bokmal to "Fribeløp for særkullsbarn",
                                Nynorsk to "Fribeløp for særkullsbarn",
                                English to "Exemption amount for children from a previous relationship",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop()
                                    .format() + " kr",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop()
                                    .format() + " kr",
                                English to pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop()
                                    .format() + " NOK",
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
                                Bokmal to "Fribeløp for fellesbarn",
                                Nynorsk to "Fribeløp for fellesbarn",
                                English to "Exemption amount for joint children",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                    .format() + " kr",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                    .format() + " kr",
                                English to pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                    .format() + " NOK",
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
                                Bokmal to "Samlet inntekt som er brukt i fastsettelse av barnetillegg",
                                Nynorsk to "Inntekt for deg som er brukt i berekning av barnetillegg",
                                English to "Your income, which is used to calculate child supplement",
                            )
                        }

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false
                        cell {
                            showIf(not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format(),
                                    Nynorsk to pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format(),
                                    English to pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning().format(),
                                )
                            }

                            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning().format(),
                                    Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format(),
                                    English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning().format(),
                                )
                            }
                            text(
                                Bokmal to " kr",
                                Nynorsk to " kr",
                                English to " NOK",
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
                                Bokmal to "Samlet inntekt til annen forelder som er brukt i fastsettelse av barnetillegg",
                                Nynorsk to "Inntekt til annan forelder som er brukt i berekning av barnetillegg",
                                English to "Income of the other parent, which is used to calculate child supplement",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + " kr",
                                Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + " kr",
                                English to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder().format() + " NOK",
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
                                Bokmal to "Beløp som er trukket fra annen forelders inntekt (inntil 1G)",
                                Nynorsk to "Beløp som er trekt frå inntekta til ein annan forelder (inntil 1G)",
                                English to "Amount deducted from the other parent's income (up to 1G)",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt()
                                    .format() + " kr",
                                Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt()
                                    .format() + " kr",
                                English to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt()
                                    .format() + " NOK",
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
                                Bokmal to "Samlet inntekt for deg som gjør at barnetillegget ikke blir utbetalt",
                                Nynorsk to "Samla inntekt som gjer at barnetillegget ikkje blir utbetalt",
                                English to "Your income which means that no child supplement is received",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak()
                                    .format() + " kr",
                                Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak()
                                    .format() + " kr",
                                English to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak()
                                    .format() + " NOK",
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
                                Bokmal to "Samlet inntekt for deg og annen forelder som gjør at barnetillegget ikke blir utbetalt",
                                Nynorsk to "Samla inntekt for deg og annan forelder som gjer at barnetillegget ikkje blir utbetalt",
                                English to "Total income which means that no child supplement is received",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak()
                                    .format() + " kr",
                                Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak()
                                    .format() + " kr",
                                English to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak()
                                    .format() + " NOK",
                            )
                        }
                    }
                }
            }
        }
    }
}