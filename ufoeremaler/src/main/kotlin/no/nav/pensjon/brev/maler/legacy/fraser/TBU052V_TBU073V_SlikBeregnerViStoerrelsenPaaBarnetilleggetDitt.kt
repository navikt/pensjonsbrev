package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class TBU052V_TBU073V_SlikBeregnerViStoerrelsenPaaBarnetilleggetDitt(
    val pe: Expression<PE>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //IF(PE_UT_TBU069V() = true) THEN      INCLUDE ENDIF
        showIf(pe.ut_tbu069v()) {
            //[TBU052V-TBU073V]

            title1 {
                text(
                    bokmal { + "Slik beregner vi størrelsen på barnetillegget" },
                    nynorsk { + "Slik bereknar vi storleiken på barnetillegget" },
                    english { + "How we calculate the amount of child supplement" },
                )
            }
        }

        //IF(PE_UT_TBU069V() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu069v())) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Størrelsen på barnetillegget er avhengig av samlet inntekt. " },
                    nynorsk { + "Storleiken på barnetillegget er avhengig av samla inntekt." },
                    english { + "The amount of child supplement is dependent on your total income. " },
                )
            }
        }

        //IF(PE_UT_TBU069V() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu069v())) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Barnetillegget kan bli redusert ut fra:" },
                    nynorsk { + "Barnetillegget kan bli redusert ut frå:" },
                    english { + "Child supplement can be reduced based on:" },
                )
                list {
                    item {
                        text(
                            bokmal { + "uføretrygd" },
                            nynorsk { + "uføretrygd" },
                            english { + "disability benefits" },
                        )
                    }
                    item {
                        text(
                            bokmal { + "arbeidsinntekt" },
                            nynorsk { + "arbeidsinntekt" },
                            english { + "income from employment" },
                        )
                    }
                    item {
                        text(
                            bokmal { + "næringsinntekt" },
                            nynorsk { + "næringsinntekt" },
                            english { + "income from self-employment" },
                        )
                    }

                    item {
                        text(
                            bokmal { + "inntekt fra utlandet" },
                            nynorsk { + "inntekt frå utlandet" },
                            english { + "income from overseas" },
                        )
                    }
                    item {
                        text(
                            bokmal { + "ytelser/pensjon fra Norge" },
                            nynorsk { + "ytingar/pensjon frå Noreg" },
                            english { + "payments/pensions from Norway" },
                        )
                    }
                    item {
                        text(
                            bokmal { + "pensjon fra utlandet" },
                            nynorsk { + "pensjon frå utlandet" },
                            english { + "pensions from overseas" },
                        )
                    }
                }
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false ) ) THEN      INCLUDE ENDIF
        showIf(
            (pe.ut_tbu069v() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and not(
                pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
            )))
        ) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Vi fastsetter størrelsen på barnetillegget ut fra den samlede inntekten til begge foreldrene." },
                    nynorsk { + "Vi fastset storleiken på barnetillegget ut frå den samla inntekta til begge foreldra." },
                    english { + "We determine the amount of child supplement based on the total income of both parents." },
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false ) ) THEN      INCLUDE ENDIF
        showIf(
            (pe.ut_tbu069v() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and not(
                pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
            )))
        ) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn er 4,6 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. " },
                    nynorsk { + "Barnetillegget blir redusert dersom den samla inntekta er høgare enn fribeløpet. Fribeløpet for eit barn er 4,6 gonger grunnbeløpet i folketrygda, og det aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. " },
                    english { + "The child supplement will be reduced if the total income is greater than the exemption amount. The exemption amount is 4.6 x the national insurance basic amount and it increases with 40 percent of the national insurance basic amount for each extra child. " },
                )

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid < 40 AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) <> "oppfylt") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid()
                        .lessThan(40) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().notEqualTo("oppfylt"))
                ) {
                    text(
                        bokmal { + "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har." },
                        nynorsk { + "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har." },
                        english { + "As your period of national insurance cover is less than 40 years, the exemption amount is reduced based on the period of national insurance that you have." },
                    )
                }
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false ) ) THEN      INCLUDE ENDIF
        showIf(
            (pe.ut_tbu069v() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and not(
                pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
            )))
        ) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har rett til det høyeste tillegget. Dette gjelder også dersom den ene forelderen mottar alderspensjon. " },
                    nynorsk { + "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har rett til det høgaste tillegget. Dette gjeld også dersom den eine forelderen får alderspensjon." },
                    english { + "If both parents receive disability benefits, child supplement is paid to the parent who has the right to the highest supplement. This also applies if one of the parents receives a retirement pension." },
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu069v() and (not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Vi fastsetter størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget. " },
                    nynorsk { + "Vi fastset storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget." },
                    english { + "We determine the amount of child supplement based on your income. The income of a  " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + "who is not a parent of the child does not affect the size of the child supplement." },
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu069v() and (not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. " },
                    nynorsk { + "Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda, og det aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn." },
                    english { + "The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 3.1 x the national insurance basic amount and it increases with 40 percent of the national insurance basic amount for each extra child. " },
                )

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid < 40 AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) <> "oppfylt") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid()
                        .lessThan(40) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().notEqualTo("oppfylt"))
                ) {
                    text(
                        bokmal { + "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har." },
                        nynorsk { + "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har." },
                        english { + "As your period of national insurance cover is less than 40 years, the exemption amount is reduced based on the period of national insurance that you have." },
                    )
                }
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu069v() and (not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har den daglige omsorgen for barnet. Dette gjelder også dersom den ene forelderen mottar alderspensjon. Har foreldrene delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bor på samme folkeregistrerte adresse som barnet. " },
                    nynorsk { + "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har den daglege omsorga for barnet. Dette gjeld også dersom den eine forelderen får alderspensjon. Har foreldra delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bur på same folkeregistrerte adresse som barnet." },
                    english { + "If both parents receive disability benefits, child supplement is paid to the parent who has the daily care of the child. This also applies if one of the parents receives a retirement pension. If the parents have joint daily care of the child, the child supplement will be paid to the parent who lives at the same address, registered in the population register, as the child." },
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu069v() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Vi fastsetter størrelsen på barnetillegget ut fra inntekten til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn som bor med begge foreldrene er 4,6 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. " },
                    nynorsk { + "Vi fastset storleiken på barnetillegget ut frå inntekta til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din for " + pe.ut_barnet_barna_felles() + " som bur med begge foreldra sine. Barnetillegget blir redusert dersom den samla inntekta er høgare enn fribeløpet. Fribeløpet for eit barn som bur med begge foreldra, er 4,6 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. " },
                    english { + "We determine the amount of the child supplement based on your and your " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en() + "'s income for the " + pe.ut_barnet_barna_felles() + " that " },
                )

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles = 1
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().equalTo(1)) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "lives" },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles > 1) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().greaterThan(1))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "live" },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                    english { + " with both of their parents. The child supplement will be reduced if the total income is greater than the exemption amount. The exemption amount for a child that lives with both parents is 4.6 x the national insurance basic amount and it increases with 40 percent of the national insurance basic amount for each extra child." },
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu069v() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "For " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldre, fastsetter vi størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget. Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn som ikke bor sammen med begge foreldrene er 3,1 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. " },
                    nynorsk { + "For " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra, fastset vi storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget. Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn som ikkje bur saman med begge foreldra, er 3,1 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. " },
                    english { + "For " },
                )

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull = 1
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().equalTo(1)) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "a " },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                    english { + pe.ut_barnet_barna_serkull() + " that " },
                )

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull = 1
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().equalTo(1)) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "does" },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull > 1) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().greaterThan(1))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "do" },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                    english { + " not live with both parents, we determine the amount of child supplement based on your income. The income of a spouse/partner/cohabiting partner who is not a parent of the child does not affect the size of the child supplement. The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount for a child that does not live with both parents is 3.1 x the national insurance basic amount and this increases by 40 percent of the national insurance basic amount for each extra child. " },
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid < 40 AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) <> "oppfylt") AND   (PE_UT_VirkningstidpunktStorreEnn01012016() = true) ) THEN      INCLUDE ENDIF
        showIf(
            (pe.ut_tbu069v() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) and (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid()
                .lessThan(
                    40
                ) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()
                .notEqualTo("oppfylt")) and (pe.ut_virkningstidpunktstorreenn01012016()))
        ) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Siden trygdetiden din er kortere enn 40 år, blir fribeløpene redusert ut fra den trygdetiden du har." },
                    nynorsk { + "Sidan trygdetida di er kortare enn 40 år, blir fribeløpa reduserte ut frå den trygdetida du har." },
                    english { + "As your period of national insurance cover is less than 40 years, the exemption amounts are reduced based on the period of national insurance that you have." },
                )
            }
        }

        // Velger å ikke slå sammen denne teksten ettersom det er vesentlige forskjeller i styringen på de ulike språklagene.
        //IF(PE_UT_TBU605() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu605())) {
            //[TBU052V-TBU073V]

            paragraph {

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((pe.ut_tbu605v_eller_til_din()))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "Har det vore ei endring i inntekta " },
                        english { + "" },
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN      INCLUDE ENDIF
                showIf(
                    ((pe.ut_tbu605v_eller_til_din() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo(
                            "bormed ektefelle"
                        ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 3-2"
                    ))))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "til deg eller " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " " },
                        english { + "" },
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((pe.ut_tbu605v_eller_til_din()))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "di" },
                        english { + "" },
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN      INCLUDE ENDIF
                showIf(
                    ((pe.ut_tbu605v_eller_til_din() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo(
                            "bormed ektefelle"
                        ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 3-2"
                    ))))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "n" },
                        english { + "" },
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((pe.ut_tbu605v_eller_til_din()))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "," },
                        english { + "" },
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "Når inntekta di " },
                        english { + "" },
                    )
                }

                //IF(( PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN      INCLUDE ENDIF
                showIf(
                    ((pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("endret_inntekt") and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2"))))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "eller til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
                        english { + "" },
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "endrar seg" + "," },
                        english { + "" },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + " blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av inntekta som overstig fribeløpet " },
                    english { + "" },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektperiodisert())) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "blir rekna om til et årleg beløp som svarer til " },
                        english { + "" },
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false) THEN      INCLUDE ENDIF
                showIf(
                    (not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert()) and not(
                        pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert()
                    ) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektperiodisert()) and not(
                        pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektperiodisert()
                    ))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "er " },
                        english { + "" },
                    )
                }

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                    text(
                        bokmal { + "" },
                        nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                            .format(false) },
                        english { + "" },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                            .format(false) },
                        english { + "" },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + " kroner. " },
                    english { + "" },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(
                            0
                        ))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året. " },
                        english { + "" },
                    )
                }
            }
        }

        // Velger å ikke slå sammen denne teksten ettersom det er vesentlige forskjeller i styringen på de ulike språklagene.
        //IF(PE_UT_TBU605() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu605())) {
            //[TBU052V-TBU073V]

            paragraph {

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((pe.ut_tbu605v_eller_til_din()))) {
                    text(
                        bokmal { + "Har det vært en endring i inntekten din" },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN      INCLUDE ENDIF
                showIf(
                    ((pe.ut_tbu605v_eller_til_din() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo(
                            "bormed ektefelle"
                        ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 3-2"
                    ))))
                ) {
                    text(
                        bokmal { + " eller til din " },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true )) THEN    INCLUDE ENDIF
                showIf(((pe.ut_tbu605v_eller_til_din() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))) {
                    text(
                        bokmal { + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + "," },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "Når inntekten din " },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }

                //IF(( PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN      INCLUDE ENDIF
                showIf(
                    ((pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("endret_inntekt") and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2"))))
                ) {
                    text(
                        bokmal { + "eller til din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "endrer seg," },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }
                text(
                    bokmal { + " blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av den inntekten som overstiger fribeløpet " },
                    nynorsk { + "" },
                    english { + "" },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektperiodisert())) {
                    text(
                        bokmal { + "blir omregnet til et årlig beløp som tilsvarer " },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false) THEN      INCLUDE ENDIF
                showIf(
                    (not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert()) and not(
                        pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert()
                    ) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektperiodisert()) and not(
                        pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektperiodisert()
                    ))
                ) {
                    text(
                        bokmal { + "er " },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                    text(
                        bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                            .format(false) },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))) {
                    text(
                        bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                            .format(false) },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }
                text(
                    bokmal { + " kroner. " },
                    nynorsk { + "" },
                    english { + "" },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(
                            0
                        ))
                ) {
                    text(
                        bokmal { + "Dette beløpet bruker vi til å redusere barnetillegget ditt for hele året. " },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }
            }
        }

        // Velger å ikke slå sammen denne teksten ettersom det er vesentlige forskjeller i styringen på de ulike språklagene.
        //IF(PE_UT_TBU605() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu605())) {
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt"))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "If" },

                        )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "When" },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                    english { + " your " },
                )

                //IF(( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN      INCLUDE ENDIF
                showIf(
                    ((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2"))))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "or your " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + "'s " },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                    english { + "income has been changed, your child supplement will be recalculated. 50 percent of income that exceeds the exemption amount " },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektperiodisert())) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "is recalculated to an annual amount of " },
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false) THEN      INCLUDE ENDIF
                showIf(
                    (not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert()) and not(
                        pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert()
                    ) and not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektperiodisert()) and not(
                        pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektperiodisert()
                    ))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "is " },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                    english { + "NOK " },
                )

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                            .format(false) },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                            .format(false) },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                    english { + ". " },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(
                            0
                        ))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "This amount will be used to reduce your child supplement during the calendar year." },
                    )
                }
            }
        }

        //IF (PE_UT_TBU605() = true      AND (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0     OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0)    ) THEN   INCLUDE ENDIF
        showIf(
            (pe.ut_tbu605() and (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                .notEqualTo(
                    0
                ) or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                .notEqualTo(
                    0
                )))
        ) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor " },
                    nynorsk { + "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor  " },
                    english { + "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been " },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr > 0 OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr > 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .greaterThan(0) or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .greaterThan(
                            0
                        ))
                ) {
                    text(
                        bokmal { + "lagt til" },
                        nynorsk { + "lagt til" },
                        english { + "increased" },
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr < 0 OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .lessThan(0) or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .lessThan(
                            0
                        ))
                ) {
                    text(
                        bokmal { + "redusert" },
                        nynorsk { + "trekt frå" },
                        english { + "reduced" },
                    )
                }
                text(
                    bokmal { + " " },
                    nynorsk { + " " },
                    english { + " with " },
                )

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                    text(
                        bokmal { + pe.barnetilleggfelles_justeringsbelopperarutenminus().format() },
                        nynorsk { + pe.barnetilleggfelles_justeringsbelopperarutenminus().format() },
                        english { + pe.barnetilleggfelles_justeringsbelopperarutenminus().format() },
                    )
                }

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) {
                    text(
                        bokmal { + pe.barnetilleggserkull_justeringsbelopperarutenminus().format() },
                        nynorsk { + pe.barnetilleggserkull_justeringsbelopperarutenminus().format() },
                        english { + pe.barnetilleggserkull_justeringsbelopperarutenminus().format() },
                    )
                }
                text(
                    bokmal { + " i beløpet vi reduserer barnetillegget med for resten av året. " },
                    nynorsk { + " i beløpet vi reduserer barnetillegget med for resten av året. " },
                    english { + ". " },
                )
            }
        }

        // Velger å ikke slå sammen denne teksten ettersom det er vesentlige forskjeller i styringen på de ulike språklagene.
        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_1_3())) {
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt"))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "Har det vore ei endring i inntekta " },
                        english { + "" },
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt"  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2")) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("endret_inntekt") and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2")))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "til deg eller " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " " },
                        english { + "" },
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt"))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "di" },
                        english { + "" },
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt"  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2")) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("endret_inntekt") and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2")))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "n" },
                        english { + "" },
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt"))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "," },
                        english { + "" },
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "Når inntekta di " },
                        english { + "" },
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2")) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("endret_inntekt") and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2")))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "eller til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
                        english { + "" },
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "endrar seg," },
                        english { + "" },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + " blir reduksjonen av barnetilleggene vurdert på nytt." },
                    english { + "" },
                )
            }
        }

        // Velger å ikke slå sammen denne teksten ettersom det er vesentlige forskjeller i styringen på de ulike språklagene.
        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_1_3())) {
            //[TBU052V-TBU073V]

            paragraph {

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((pe.ut_tbu605v_eller_til_din()))) {
                    text(
                        bokmal { + "Har det vært en endring i inntekten din" },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2") )) THEN      INCLUDE ENDIF
                showIf(
                    ((pe.ut_tbu605v_eller_til_din() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo(
                            "bormed ektefelle"
                        ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 3-2"
                    ))))
                ) {
                    text(
                        bokmal { + " eller til din" },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((pe.ut_tbu605v_eller_til_din()))) {
                    text(
                        bokmal { + " " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + "," },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "Når inntekten din " },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5"  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2")) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("endret_inntekt") and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2")))
                ) {
                    text(
                        bokmal { + "eller til din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "endrer seg," },
                        nynorsk { + "" },
                        english { + "" },
                    )
                }
                text(
                    bokmal { + " blir reduksjonen av barnetilleggene vurdert på nytt. " },
                    nynorsk { + "" },
                    english { + "" },
                )
            }
        }

        // Velger å ikke slå sammen denne teksten ettersom det er vesentlige forskjeller i styringen på de ulike språklagene.
        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_1_3())) {
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt"))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "If" },
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "When" },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                    english { + " your " },
                )

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed ektefelle" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed registrert partner" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1-5" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 3-2" OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = "bormed 1_5") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 3-2") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 1_5"))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "or your " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + "'s " },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                    english { + "income has been changed, your child supplement will be recalculated." },
                )
            }
        }
        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_1_3())) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "50 prosent av den inntekten som overstiger fribeløpet for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre " },
                    nynorsk { + "50 prosent av inntekta som overstig fribeløpet for " + pe.ut_barnet_barna_felles() + " som bur med begge foreldra " },
                    english { + "50 percent of the income that exceeds the exemption amount for the " + pe.ut_barnet_barna_felles() + " that " },
                )

//PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles = 1
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().equalTo(1)) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "lives" },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles > 1) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().greaterThan(1))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "live" },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                    english { + " with both of their parents, " },
                )
                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true
                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert()) {
                    text(
                        bokmal { + "blir omregnet til et årlig beløp som tilsvarer " },
                        nynorsk { + "sine blir rekna om til et årleg beløp som svarer til " },
                        english { + "is recalculated to an annual amount of" },
                    )
                }

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false
                showIf(not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert())) {
                    text(
                        bokmal { + "er " },
                        nynorsk { + "er " },
                        english { + "is" },
                    )
                }
                text(
                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". " },
                    nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". " },
                    english { + " " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". " },
                )

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0
                showIf(
                    pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(0)
                ) {
                    text(
                        bokmal { + "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året." },
                        nynorsk { + "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året." },
                        english { + "This amount will be used to reduce this child supplement during the calendar year." },
                    )
                }
            }
        }

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
        showIf(
            (pe.ut_tbu613v() and pe.ut_tbu613v_1_3() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                .notEqualTo(
                    0
                ))
        ) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor " },
                    nynorsk { + "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor " },
                    english { + "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been " },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr > 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .greaterThan(0))
                ) {
                    text(
                        bokmal { + "lagt til" },
                        nynorsk { + "lagt til" },
                        english { + "increased" },
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .lessThan(0))
                ) {
                    text(
                        bokmal { + "trukket fra" },
                        nynorsk { + "trekt frå" },
                        english { + "reduced" },
                    )
                }
                text(
                    bokmal { + " " + pe.barnetilleggfelles_justeringsbelopperarutenminus()
                        .format() + " i beløpet vi reduserer barnetillegget med for resten av året." },
                    nynorsk { + " " + pe.barnetilleggfelles_justeringsbelopperarutenminus()
                        .format() + " i beløpet vi reduserer barnetillegget med for resten av året." },
                    english { + " with " + pe.barnetilleggfelles_justeringsbelopperarutenminus().format() + "." },
                )
            }
        }

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_4_5() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_4_5())) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "For " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene " },
                    nynorsk { + "For " + pe.ut_barnet_barna_serkull() + " som ikkje bur med begge foreldra " },
                    english { + "For " },
                )
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().equalTo(1)) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "a " },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                    english { + pe.ut_barnet_barna_serkull() + " that " },
                )

                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().equalTo(1)) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "does" },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull > 1) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().greaterThan(1))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "do" },
                    )
                }
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                    english { + " not live with both parents " },
                )
                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = false
                showIf(not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert())) {
                    text(
                        bokmal { + "er 50 prosent av den inntekten som overstiger fribeløpet " },
                        nynorsk { + "er 50 prosent av den inntekta som overstig fribeløpet " },
                        english { + "50 percent of the income that exceeds the exemption amount " },
                    )
                }

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = true
                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert()) {
                    text(
                        bokmal { + "blir 50 prosent av den inntekten som overstiger fribeløpet omregnet til et årlig beløp som tilsvarer " },
                        nynorsk { + "blir 50 prosent av den inntekta som overstig fribeløpet regna om til et årleg beløp som svarer til " },
                        english { + "is recalculated to an annual amount of" },

                        )
                }
                showIf(not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert())) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "" },
                        english { + "is" },
                    )
                }

                text(
                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". " },
                    nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". " },
                    english { + " " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". " },
                )

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0
                showIf(
                    pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(0)
                ) {
                    text(
                        bokmal { + "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året. " },
                        nynorsk { + "Dette beløpet bruker vi til å redusere dette barnetillegget for heile året. " },
                        english { + "This amount will be used to reduce this child supplement during the calendar year." },
                    )
                }
            }
        }

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_4_5() = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
        showIf(
            (pe.ut_tbu613v() and pe.ut_tbu613v_4_5() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                .notEqualTo(
                    0
                ))
        ) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor " },
                    nynorsk { + "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor " },
                    english { + "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been " },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr > 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .greaterThan(0))
                ) {
                    text(
                        bokmal { + "lagt til" },
                        nynorsk { + "lagt til" },
                        english { + "increased" },
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .lessThan(0))
                ) {
                    text(
                        bokmal { + "trukket fra" },
                        nynorsk { + "trekt frå" },
                        english { + "reduced" },
                    )
                }
                text(
                    bokmal { + " " + pe.barnetilleggserkull_justeringsbelopperarutenminus()
                        .format() + " i beløpet vi reduserer barnetillegget med for resten av året." },
                    nynorsk { + " " + pe.barnetilleggserkull_justeringsbelopperarutenminus()
                        .format() + " i beløpet vi reduserer barnetillegget med for resten av året." },
                    english { + " with " + pe.barnetilleggserkull_justeringsbelopperarutenminus().format() + "." },
                )
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU606V_TBU608V() = true) THEN    INCLUDE END IF
        showIf((pe.ut_tbu606v_tbu611v() and pe.ut_tbu606v_tbu608v())) {

            //[TBU052V-TBU073V]
            paragraph {
                table(header = {
                    column(columnSpan = 4) {
                        text(
                            bokmal { + "Reduksjon av barnetillegg for fellesbarn før skatt " },
                            nynorsk { + "Reduksjon av barnetillegg for fellesbarn før skatt " },
                            english { + "Reduction of child supplement payment for joint children before tax," },
                        )

                        //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM(1)) = false) THEN      INCLUDE ENDIF
                        showIf((not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkarvirkningfom())))) {
                            text(
                                bokmal { + "i år" },
                                nynorsk { + "i år" },
                                english { + " for this year" },
                            )
                        }

                        //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM(1)) = true) THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkarvirkningfom()))) {
                            text(
                                bokmal { + "for neste år" },
                                nynorsk { + "for neste år" },
                                english { + " for next year" },
                            )
                        }
                    }
                    column(columnSpan = 1,alignment = ColumnAlignment.RIGHT) {}
                }) {
                    row {
                        cell {
                            text(
                                bokmal { + "Årlig barnetillegg før reduksjon ut fra inntekt" },
                                nynorsk { + "Årleg barnetillegg før reduksjon ut frå inntekt" },
                                english { + "Yearly child supplement before income reduction  " },
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbruttoperar()
                                    .format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbruttoperar()
                                    .format(false) + " kr" },
                                english { + "NOK " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbruttoperar()
                                    .format(false) },
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "Samlet inntekt brukt i fastsettelse av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning()
                                    .format(false) + " kr" },
                                nynorsk { + "Samla inntekt brukt i fastsetting av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning()
                                    .format(false) + " kr" },
                                english { + "Total income applied in calculation of reduction in child supplement for joint children are NOK " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning()
                                    .format(false) },
                            )
                        }
                        cell {

                        }
                    }
                    showIf(
                        (pe.ut_tbu606v_tbu611v() and pe.ut_tbu606v_tbu608v() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                            .greaterThan(
                                0
                            ) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                            .equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                            .notEqualTo(
                                0
                            ))))
                    ) {
                        //[TBU052V-TBU073V]

                        row {
                            cell {
                                text(
                                    bokmal { + "Fribeløp brukt i fastsettelsen av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                        .format(false) + " kr" },
                                    nynorsk { + "Fribeløp brukt i fastsetting av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                        .format(false) + " kr" },
                                    english { + "Exemption amount applied in calculation of reduction in child supplement are NOK " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                        .format(false) },
                                )
                            }
                            cell { }
                        }
                    }

                    showIf(
                        pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                            .notEqualTo(0)
                                or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)
                                and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                            .notEqualTo(0))
                    ) {
                        //[TBU052V-TBU073V]
                        row {
                            cell {
                                text(
                                    bokmal { + "Inntekt over fribeløpet er " + pe.ut_btfbinntektbruktiavkortningminusbtfbfribelop()
                                        .format(false) + " kr" },
                                    nynorsk { + "Inntekt over fribeløpet er " + pe.ut_btfbinntektbruktiavkortningminusbtfbfribelop()
                                        .format(false) + " kr" },
                                    english { + "Income exceeding the exemption amount NOK " + pe.ut_btfbinntektbruktiavkortningminusbtfbfribelop()
                                        .format(false) },
                                )
                            }
                            cell {}
                        }
                    }

                    showIf(
                        ((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().notEqualTo(0)
                                or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)
                                and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                            .notEqualTo(0)))
                                and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                            .greaterThan(0))
                    ) {
                        //[TBU052V-TBU073V]

                        row {
                            cell {
                                text(
                                    bokmal { + "- 50 prosent av inntekt som overstiger fribeløpet" },
                                    nynorsk { + "- 50 prosent av inntekta som overstig fribeløpet" },
                                    english { + "- 50 percent of income exceeding the allowance amount " },
                                    FontType.BOLD
                                )

                                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true
                                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert()) {
                                    text(
                                        bokmal { + "(oppgitt som et årlig beløp)" },
                                        nynorsk { + "(oppgitt som eit årleg beløp)" },
                                        english { + "(calculated to an annual amount)" },
                                        FontType.BOLD
                                    )
                                }
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                                        .format(false) + " kr" },
                                    nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                                        .format(false) + " kr" },
                                    english { + "NOK " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                                        .format(false) },
                                    FontType.BOLD
                                )
                            }
                        }
                    }

                    showIf(
                        (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                            .notEqualTo(0))
                    ) {
                        //[TBU052V-TBU073V]

                        row {
                            cell {
                                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr > 0) THEN      INCLUDE ENDIF
                                showIf(
                                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                                        .greaterThan(0))
                                ) {
                                    text(
                                        bokmal { + "-" },
                                        nynorsk { + "-" },
                                        english { + "-" },
                                    )
                                }

                                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                                showIf(
                                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                                        .lessThan(0))
                                ) {
                                    text(
                                        bokmal { + "+" },
                                        nynorsk { + "+" },
                                        english { + "+" },
                                    )
                                }
                                text(
                                    bokmal { + " Beløp som er brukt for å justere reduksjonen av barnetillegget" },
                                    nynorsk { + " Beløp som er brukt for å justere reduksjonen av barnetillegget" },
                                    english { + " Amount which is used to adjust the reduction of child supplement" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar_utenminus()
                                        .format(false) + " kr" },
                                    nynorsk { + pe.barnetilleggfelles_justeringsbelopperarutenminus().format(false) + " kr" },
                                    english { + "NOK " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar_utenminus()
                                        .format(false) },
                                )
                            }
                        }
                    }

                    showIf(
                        (pe.ut_tbu606v_tbu611v() and pe.ut_tbu606v_tbu608v() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                            .notEqualTo(0)
                                or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)
                                and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0))))
                    ) {
                        //[TBU052V-TBU073V]
                        row {
                            cell {
                                text(
                                    bokmal { + "= Årlig barnetillegg etter reduksjon ut fra inntekt" },
                                    nynorsk { + "= Årleg barnetillegg etter reduksjon ut frå inntekt" },
                                    english { + "= Calculation of the yearly reduction of the child supplement" },
                                    FontType.BOLD
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbnettoperar().format(false) + " kr" },
                                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbnettoperar().format(false) + " kr" },
                                    english { + "NOK " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbnettoperar().format(false) },
                                    FontType.BOLD
                                )
                            }
                        }
                    }



                    showIf(
                        (pe.ut_tbu606v_tbu611v() and pe.ut_tbu606v_tbu608v() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                            .notEqualTo(0)
                                or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)
                                and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0))))
                    ) {
                        //[TBU052V-TBU073V]

                        row {
                            cell {
                                text(
                                    bokmal { + "Utbetaling av barnetillegg per måned " },
                                    nynorsk { + "Utbetaling av barnetillegg per månad " },
                                    english { + "Child supplement payment for the remaining months of the year" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                                        .format(false) + " kr" },
                                    nynorsk { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                                        .format(false) + " kr" },
                                    english { + "NOK " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                                        .format(false) },
                                )
                            }
                        }
                    }

                    showIf((pe.ut_tbu606v_tbu611v() and pe.ut_tbu606v_tbu608v() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto().equalTo(0)
                            and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                        //[TBU052V-TBU073V]

                        row {
                            cell {
                                text(
                                    bokmal { + "Grensen for å få utbetalt barnetillegg" },
                                    nynorsk { + "Grensa for å få utbetalt barnetillegg" },
                                    english { + "The income limit for receiving child supplement" },
                                    FontType.BOLD
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format(false) + " kr" },
                                    nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format(false) + " kr" },
                                    english { + "NOK " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format(false) },
                                    FontType.BOLD
                                )
                            }
                        }
                    }
                }
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0 AND PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU606V_TBU608V() = true) THEN      INCLUDE ENDIF
        showIf(
            (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                .greaterThan(
                    0
                ) and pe.ut_tbu606v_tbu611v() and pe.ut_tbu606v_tbu608v())){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    bokmal { + "Du vil få utbetalt " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                        .format() + " i måneden før skatt i barnetillegg" },
                    nynorsk { + "Du vil få utbetalt " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                        .format() + " i månaden før skatt i barnetillegg" },
                    english { + "You will receive a monthly child supplement payment of NOK " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                        .format(false) },
                )

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_UT_Etteroppgjor_BT_Utbetalt() = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.ut_etteroppgjor_bt_utbetalt())){
                    text (
                        bokmal { + " for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre" },
                        nynorsk { + " for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine" },
                        english { + " for the " + pe.ut_barnet_barna_felles() + " who live together with both parents" },
                    )
                }
                text (
                    bokmal { + ". " },
                    nynorsk { + "." },
                    english { + "." },
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU606V_TBU608V() = true) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
            .equalTo(0) and pe.ut_tbu606v_tbu611v() and pe.ut_tbu606v_tbu608v())){
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_UT_TBU608_Far_Ikke() = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_tbu608_far_ikke())){
                    text (
                        bokmal { + "Du får ikke utbetalt barnetillegget " },
                        nynorsk { + "Du får ikkje utbetalt barnetillegget " },
                        english { + "You will not receive a child supplement " },
                    )
                }

                //IF(PE_UT_TBU608_Far_Ikke() = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_tbu608_far_ikke() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())){
                    text (
                        bokmal { + "for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre " },
                        nynorsk { + "for " + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine " },
                        english { + "for the " + pe.ut_barnet_barna_felles() + " who live together with both parents " },
                    )
                }

                //IF(PE_UT_TBU608_Far_Ikke() = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_tbu608_far_ikke())){
                    text (
                        bokmal { + "fordi samlet inntekt er over grensen for å få utbetalt barnetillegg. " },
                        nynorsk { + "fordi samla inntekt er over grensa for å få utbetalt barnetillegg. " },
                        english { + "because your income is over the income limit for receiving a child supplement. " },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                        .equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .notEqualTo(0))){
                    text (
                        bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                        nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                        english { + "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year." },
                    )
                }
            }
        }

        //IF (PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU609V_TBU611V() = true) THEN    INCLUDE END IF
        showIf((pe.ut_tbu606v_tbu611v() and pe.ut_tbu609v_tbu611v())){
            //[TBU052V-TBU073V]

            paragraph {
                table(
                    header = {
                        column(columnSpan = 4) {
                            text (
                                bokmal { + "Reduksjon av barnetillegg for særkullsbarn før skatt " },
                                nynorsk { + "Reduksjon av barnetillegg for særkullsbarn før skatt " },
                                english { + "Reduction of child supplement payment for children from a previous relationship before tax, " },
                            )

                            //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM(1)) = false) THEN      INCLUDE ENDIF
                            showIf((not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkarvirkningfom())))){
                                text (
                                    bokmal { + "i år" },
                                    nynorsk { + "i år" },
                                    english { + "for this year" },
                                )
                            }

                            //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM(1)) = true) THEN      INCLUDE ENDIF
                            showIf((FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkarvirkningfom()))){
                                text (
                                    bokmal { + "for neste år" },
                                    nynorsk { + "for neste år" },
                                    english { + "for next year" },
                                )
                            }
                        }
                        column(columnSpan = 1,alignment = ColumnAlignment.RIGHT) {

                        }
                    }
                ) {

                    row {
                        cell {
                            text(
                                bokmal { + "Årlig barnetillegg før reduksjon ut fra inntekt" },
                                nynorsk { + "Årleg barnetillegg før reduksjon ut frå inntekt" },
                                english { + "Yearly child supplement before income reduction  " },
                                FontType.BOLD
                            )
                        }
                        cell {
                            text(
                                bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbbruttoperar().format(false) + " kr" },
                                nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbbruttoperar().format(false) + " kr" },
                                english { + "NOK " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbbruttoperar().format(false) },
                                FontType.BOLD
                            )
                        }
                    }
                    row {
                        cell {
                            text (
                                bokmal { + "Samlet inntekt brukt i fastsettelse av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning()
                                    .format(false) + " kr" },
                                nynorsk { + "Samla inntekt brukt i fastsetting av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning()
                                    .format(false) + " kr" },
                                english { + "Total income applied in calculation of reduction in child supplement from a previous relationship NOK " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning()
                                    .format(false) },
                            )
                        }
                        cell {  }
                    }


                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().greaterThan(0)
                                or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)
                                    and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0))
                    ){
                        //[TBU052V-TBU073V]

                        row {
                            cell {
                                text(
                                    bokmal { + "Fribeløp brukt i fastsettelsen av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format(false) + " kr" },
                                    nynorsk { + "Fribeløp brukt i fastsetting av barnetillegget er " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format(false) + " kr" },
                                    english { + "Exemption amount applied in calculation of reduction in child supplement are NOK " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format(false) },
                                )
                            }
                            cell {  }
                        }
                    }

                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0)
                                or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)
                            and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0))
                    ){
                        row {
                            cell {
                                text(
                                    bokmal { + "Inntekt over fribeløpet er " + pe.ut_btsbinntektbruktiavkortningminusbtsbfribelop().format(false) + " kr" },
                                    nynorsk { + "Inntekt over fribeløpet er " + pe.ut_btsbinntektbruktiavkortningminusbtsbfribelop().format(false) + " kr" },
                                    english { + "Income exceeding the exemption amount NOK " + pe.ut_btsbinntektbruktiavkortningminusbtsbfribelop().format(false) },
                                )
                            }
                            cell {  }
                        }
                    }

                    showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0)
                            or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)
                            and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0))
                            and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar().greaterThan(0)
                    ){
                        //[TBU052V-TBU073V]

                        row {
                            cell {
                                text(
                                    bokmal { + "- 50 prosent av inntekt som overstiger fribeløpet" },
                                    nynorsk { + "- 50 prosent av inntekta som overstig fribeløpet" },
                                    english { + "- 50 percent of income exceeding the allowance amount " },
                                    FontType.BOLD
                                )

                                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true
                                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert()) {
                                    text(
                                        bokmal { + "(oppgitt som et årlig beløp)" },
                                        nynorsk { + "(oppgitt som eit årleg beløp)" },
                                        english { + "(calculated to an annual amount)" },
                                        FontType.BOLD
                                    )
                                }
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar().format(false) + " kr" },
                                    nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar().format(false) + " kr" },
                                    english { + "NOK " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar().format(false) },
                                    FontType.BOLD
                                )
                            }
                        }
                    }

                    showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0)){
                        //[TBU052V-TBU073V]

                        row {

                            cell {//IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr > 0) THEN      INCLUDE ENDIF
                                showIf(
                                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().greaterThan(0))
                                ) {
                                    text(
                                        bokmal { + "-" },
                                        nynorsk { + "-" },
                                        english { + "-" },
                                    )
                                }

                                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                                showIf(
                                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                                        .lessThan(0))
                                ) {
                                    text(
                                        bokmal { + "+" },
                                        nynorsk { + "+" },
                                        english { + "+" },
                                    )
                                }
                                text(
                                    bokmal { + " Beløp som er brukt for å justere reduksjonen av barnetillegget" },
                                    nynorsk { + " Beløp som er brukt for å justere reduksjonen av barnetillegget" },
                                    english { + " Amount which is used to adjust the reduction of child supplement" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar_utenminus().format(false) + " kr" },
                                    nynorsk { + pe.barnetilleggserkull_justeringsbelopperarutenminus().format(false) + " kr" },
                                    english { + "NOK " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar_utenminus().format(false) },
                                )
                            }
                        }
                    }

                    showIf(
                        pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0)
                                or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)
                                    and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0))){
                        //[TBU052V-TBU073V]

                        row {
                            cell {
                                text(
                                    bokmal { + "= Årlig barnetillegg etter reduksjon ut fra inntekt" },
                                    nynorsk { + "= Årleg barnetillegg etter reduksjon ut frå inntekt" },
                                    english { + "= Calculation of the yearly reduction of the child supplement" },
                                    FontType.BOLD
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbnettoperar().format(false)  + " kr" },
                                    nynorsk { + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbnettoperar().format(false) + " kr" },
                                    english { + "NOK " + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbnettoperar().format(false) },
                                    FontType.BOLD
                                )
                            }
                        }
                    }

                    showIf(
                        pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().notEqualTo(0)
                                or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)
                                    and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().notEqualTo(0))
                    ){
                        //[TBU052V-TBU073V]

                        row {
                            cell {
                                text(
                                    bokmal { + "Utbetaling av barnetillegg per måned " },
                                    nynorsk { + "Utbetaling av barnetillegg per månad " },
                                    english { + "Child supplement payment for the remaining months of the year" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().format(false) + " kr" },
                                    nynorsk { + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().format(false) + " kr" },
                                    english { + "NOK " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().format(false) },
                                )
                            }
                        }
                    }

                    showIf(
                        (pe.ut_tbu606v_tbu611v() and pe.ut_tbu609v_tbu611v() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0)
                                and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar().equalTo(0))){
                        //[TBU052V-TBU073V]

                        row {
                            cell {
                                text(
                                    bokmal { + "Grensen for å få utbetalt barnetillegg" },
                                    nynorsk { + "Grensa for å få utbetalt barnetillegg" },
                                    english { + "The income limit for receiving child supplement " },
                                    FontType.BOLD
                                )
                            }
                            cell {
                                text(
                                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format(false) + " kr" },
                                    nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format(false) + " kr" },
                                    english { + "NOK " + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format(false) },
                                    FontType.BOLD
                                )
                            }
                        }
                    }
                }
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU609V_TBU611V() = true) THEN      INCLUDE ENDIF
        showIf(
            (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
                .greaterThan(
                    0
                ) and pe.ut_tbu606v_tbu611v() and pe.ut_tbu609v_tbu611v())){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    bokmal { + "Du vil få utbetalt " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
                        .format() + " i måneden før skatt i barnetillegg" },
                    nynorsk { + "Du vil få utbetalt " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
                        .format() + " i månaden før skatt i barnetillegg" },
                    english { + "You will receive a monthly child supplement payment of " + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().format() },
                )

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_UT_Etteroppgjor_BT_Utbetalt() = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.ut_etteroppgjor_bt_utbetalt())){
                    text (
                        bokmal { + " for " + pe.ut_barnet_barna_serkull() + " som ikke bor med begge sine foreldre" },
                        nynorsk { + " for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra" },
                        english { + " for the " + pe.ut_barnet_barna_serkull() + " who do not live together with both parents" },
                    )
                }
                text (
                    bokmal { + ". " },
                    nynorsk { + ". " },
                    english { + "." },
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_UT_TBU606V_TBU611V() = true AND PE_UT_TBU609V_TBU611V() = true) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
            .equalTo(0) and pe.ut_tbu606v_tbu611v() and pe.ut_tbu609v_tbu611v())){
            //[TBU052V-TBU073V]

            paragraph {

                //IF(PE_UT_TBU611_Far_Ikke() = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_tbu611_far_ikke())){
                    text (
                        bokmal { + "Du får ikke utbetalt barnetillegget " },
                        nynorsk { + "Du får ikkje utbetalt barnetillegget " },
                        english { + "You will not receive a child supplement " },
                    )
                }

                //IF(PE_UT_TBU611_Far_Ikke() = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_tbu611_far_ikke() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())){
                    text (
                        bokmal { + "for " + pe.ut_barnet_barna_serkull() + " som ikke bor med begge sine foreldre " },
                        nynorsk { + "for " + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra " },
                        english { + "for the " + pe.ut_barnet_barna_serkull() + " who do not live together with both parents " },
                    )
                }

                //IF(PE_UT_TBU611_Far_Ikke() = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_tbu611_far_ikke())){
                    text (
                        bokmal { + "fordi samlet inntekt er over grensen for å få utbetalt barnetillegg. " },
                        nynorsk { + "fordi samla inntekt er over grensa for å få utbetalt barnetillegg. " },
                        english { + "because your income is over the income limit for receiving a child supplement. " },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
                        .equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .notEqualTo(0))){
                    text (
                        bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                        nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                        english { + "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year. " },
                    )
                }
            }
        }
    }
}