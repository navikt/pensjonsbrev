package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

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
import no.nav.pensjon.brev.template.dsl.textExpr

data class TBU052V_TBU073V_SlikBeregnerViStoerrelsenPaaBarnetilleggetDitt(
    val pe: Expression<PE>
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //IF(PE_UT_TBU069V() = true) THEN      INCLUDE ENDIF
        showIf(pe.ut_tbu069v()) {
            //[TBU052V-TBU073V]

            title1 {
                text(
                    Bokmal to "Slik beregner vi størrelsen på barnetillegget",
                    Nynorsk to "Slik bereknar vi storleiken på barnetillegget",
                    English to "How we calculate the amount of child supplement",
                )
            }
        }

        //IF(PE_UT_TBU069V() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu069v())) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    Bokmal to "Størrelsen på barnetillegget er avhengig av samlet inntekt. ",
                    Nynorsk to "Storleiken på barnetillegget er avhengig av samla inntekt.",
                    English to "The amount of child supplement is dependent on your total income. ",
                )
            }
        }

        //IF(PE_UT_TBU069V() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu069v())) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    Bokmal to "Barnetillegget kan bli redusert ut fra:",
                    Nynorsk to "Barnetillegget kan bli redusert ut frå:",
                    English to "Child supplement can be reduced based on:",
                )
                list {
                    item {
                        text(
                            Bokmal to "uføretrygd",
                            Nynorsk to "uføretrygd",
                            English to "disability benefits",
                        )
                    }
                    item {
                        text(
                            Bokmal to "arbeidsinntekt",
                            Nynorsk to "arbeidsinntekt",
                            English to "income from employment",
                        )
                    }
                    item {
                        text(
                            Bokmal to "næringsinntekt",
                            Nynorsk to "næringsinntekt",
                            English to "income from self-employment",
                        )
                    }

                    item {
                        text(
                            Bokmal to "inntekt fra utlandet",
                            Nynorsk to "inntekt frå utlandet",
                            English to "income from overseas",
                        )
                    }
                    item {
                        text(
                            Bokmal to "ytelser/pensjon fra Norge",
                            Nynorsk to "ytingar/pensjon frå Noreg",
                            English to "payments/pensions from Norway",
                        )
                    }
                    item {
                        text(
                            Bokmal to "pensjon fra utlandet",
                            Nynorsk to "pensjon frå utlandet",
                            English to "pensions from overseas",
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
                    Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra den samlede inntekten til begge foreldrene.",
                    Nynorsk to "Vi fastset storleiken på barnetillegget ut frå den samla inntekta til begge foreldra.",
                    English to "We determine the amount of child supplement based on the total income of both parents.",
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
                    Bokmal to "Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn er 4,6 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. ",
                    Nynorsk to "Barnetillegget blir redusert dersom den samla inntekta er høgare enn fribeløpet. Fribeløpet for eit barn er 4,6 gonger grunnbeløpet i folketrygda, og det aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. ",
                    English to "The child supplement will be reduced if the total income is greater than the exemption amount. The exemption amount is 4.6 x the national insurance basic amount and it increases with 40 percent of the national insurance basic amount for each extra child. ",
                )

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid < 40 AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) <> "oppfylt") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid()
                        .lessThan(40) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().notEqualTo("oppfylt"))
                ) {
                    text(
                        Bokmal to "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har.",
                        Nynorsk to "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har.",
                        English to "As your period of national insurance cover is less than 40 years, the exemption amount is reduced based on the period of national insurance that you have.",
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
                    Bokmal to "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har rett til det høyeste tillegget. Dette gjelder også dersom den ene forelderen mottar alderspensjon. ",
                    Nynorsk to "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har rett til det høgaste tillegget. Dette gjeld også dersom den eine forelderen får alderspensjon.",
                    English to "If both parents receive disability benefits, child supplement is paid to the parent who has the right to the highest supplement. This also applies if one of the parents receives a retirement pension.",
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu069v() and (not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
            //[TBU052V-TBU073V]

            paragraph {
                textExpr(
                    Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget. ".expr(),
                    Nynorsk to "Vi fastset storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget.".expr(),
                    English to "We determine the amount of child supplement based on your income. The income of a  ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + "who is not a parent of the child does not affect the size of the child supplement.",
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu069v() and (not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    Bokmal to "Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. ",
                    Nynorsk to "Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda, og det aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn.",
                    English to "The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 3.1 x the national insurance basic amount and it increases with 40 percent of the national insurance basic amount for each extra child. ",
                )

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid < 40 AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) <> "oppfylt") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid()
                        .lessThan(40) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().notEqualTo("oppfylt"))
                ) {
                    text(
                        Bokmal to "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har.",
                        Nynorsk to "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har.",
                        English to "As your period of national insurance cover is less than 40 years, the exemption amount is reduced based on the period of national insurance that you have.",
                    )
                }
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu069v() and (not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    Bokmal to "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har den daglige omsorgen for barnet. Dette gjelder også dersom den ene forelderen mottar alderspensjon. Har foreldrene delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bor på samme folkeregistrerte adresse som barnet. ",
                    Nynorsk to "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har den daglege omsorga for barnet. Dette gjeld også dersom den eine forelderen får alderspensjon. Har foreldra delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bur på same folkeregistrerte adresse som barnet.",
                    English to "If both parents receive disability benefits, child supplement is paid to the parent who has the daily care of the child. This also applies if one of the parents receives a retirement pension. If the parents have joint daily care of the child, the child supplement will be paid to the parent who lives at the same address, registered in the population register, as the child.",
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu069v() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
            //[TBU052V-TBU073V]

            paragraph {
                textExpr(
                    Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra inntekten til deg og din ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre. Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn som bor med begge foreldrene er 4,6 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. ",
                    Nynorsk to "Vi fastset storleiken på barnetillegget ut frå inntekta til deg og ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din for " + pe.ut_barnet_barna_felles() + " som bur med begge foreldra sine. Barnetillegget blir redusert dersom den samla inntekta er høgare enn fribeløpet. Fribeløpet for eit barn som bur med begge foreldra, er 4,6 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. ",
                    English to "We determine the amount of the child supplement based on your and your ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en() + "'s income for the " + pe.ut_barnet_barna_felles() + " that ",
                )

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles = 1
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().equalTo(1)) {
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "lives",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles > 1) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().greaterThan(1))) {
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "live",
                    )
                }
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to " with both of their parents. The child supplement will be reduced if the total income is greater than the exemption amount. The exemption amount for a child that lives with both parents is 4.6 x the national insurance basic amount and it increases with 40 percent of the national insurance basic amount for each extra child.",
                )
            }
        }

        //IF(PE_UT_TBU069V() = true AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true   AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu069v() and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()))) {
            //[TBU052V-TBU073V]

            paragraph {
                textExpr(
                    Bokmal to "For ".expr() + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldre, fastsetter vi størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget. Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn som ikke bor sammen med begge foreldrene er 3,1 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. ",
                    Nynorsk to "For ".expr() + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra, fastset vi storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget. Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn som ikkje bur saman med begge foreldra, er 3,1 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. ",
                    English to "For ".expr(),
                )

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull = 1
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().equalTo(1)) {
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "a ",
                    )
                }
                textExpr(
                    Bokmal to "".expr(),
                    Nynorsk to "".expr(),
                    English to pe.ut_barnet_barna_serkull() + " that ",
                )

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull = 1
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().equalTo(1)) {
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "does",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull > 1) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().greaterThan(1))) {
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "do",
                    )
                }
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to " not live with both parents, we determine the amount of child supplement based on your income. The income of a spouse/partner/cohabiting partner who is not a parent of the child does not affect the size of the child supplement. The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount for a child that does not live with both parents is 3.1 x the national insurance basic amount and this increases by 40 percent of the national insurance basic amount for each extra child. ",
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
                    Bokmal to "Siden trygdetiden din er kortere enn 40 år, blir fribeløpene redusert ut fra den trygdetiden du har.",
                    Nynorsk to "Sidan trygdetida di er kortare enn 40 år, blir fribeløpa reduserte ut frå den trygdetida du har.",
                    English to "As your period of national insurance cover is less than 40 years, the exemption amounts are reduced based on the period of national insurance that you have.",
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
                        Bokmal to "",
                        Nynorsk to "Har det vore ei endring i inntekta ",
                        English to "",
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
                    textExpr(
                        Bokmal to "".expr(),
                        Nynorsk to "til deg eller ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " ",
                        English to "".expr(),
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((pe.ut_tbu605v_eller_til_din()))) {
                    text(
                        Bokmal to "",
                        Nynorsk to "di",
                        English to "",
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
                        Bokmal to "",
                        Nynorsk to "n",
                        English to "",
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((pe.ut_tbu605v_eller_til_din()))) {
                    text(
                        Bokmal to "",
                        Nynorsk to ",",
                        English to "",
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        Bokmal to "",
                        Nynorsk to "Når inntekta di ",
                        English to "",
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
                    textExpr(
                        Bokmal to "".expr(),
                        Nynorsk to "eller til ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din ",
                        English to "".expr(),
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        Bokmal to "",
                        Nynorsk to "endrar seg" + ",",
                        English to "",
                    )
                }
                text(
                    Bokmal to "",
                    Nynorsk to " blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av inntekta som overstig fribeløpet ",
                    English to "",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektperiodisert())) {
                    text(
                        Bokmal to "",
                        Nynorsk to "blir rekna om til et årleg beløp som svarer til ",
                        English to "",
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
                        Bokmal to "",
                        Nynorsk to "er ",
                        English to "",
                    )
                }

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                    textExpr(
                        Bokmal to "".expr(),
                        Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                            .format(),
                        English to "".expr(),
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))) {
                    textExpr(
                        Bokmal to "".expr(),
                        Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                            .format(),
                        English to "".expr(),
                    )
                }
                text(
                    Bokmal to "",
                    Nynorsk to " kroner. ",
                    English to "",
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
                        Bokmal to "",
                        Nynorsk to "Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året. ",
                        English to "",
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
                        Bokmal to "Har det vært en endring i inntekten din",
                        Nynorsk to "",
                        English to "",
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
                        Bokmal to " eller til din ",
                        Nynorsk to "",
                        English to "",
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() AND  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true )) THEN    INCLUDE ENDIF
                showIf(((pe.ut_tbu605v_eller_til_din() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))) {
                    textExpr(
                        Bokmal to pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + ",",
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        Bokmal to "Når inntekten din ",
                        Nynorsk to "",
                        English to "",
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
                    textExpr(
                        Bokmal to "eller til din ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " ",
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        Bokmal to "endrer seg,",
                        Nynorsk to "",
                        English to "",
                    )
                }
                text(
                    Bokmal to " blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av den inntekten som overstiger fribeløpet ",
                    Nynorsk to "",
                    English to "",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektperiodisert())) {
                    text(
                        Bokmal to "blir omregnet til et årlig beløp som tilsvarer ",
                        Nynorsk to "",
                        English to "",
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
                        Bokmal to "er ",
                        Nynorsk to "",
                        English to "",
                    )
                }

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                    textExpr(
                        Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                            .format(),
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))) {
                    textExpr(
                        Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                            .format(),
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                }
                text(
                    Bokmal to " kroner. ",
                    Nynorsk to "",
                    English to "",
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
                        Bokmal to "Dette beløpet bruker vi til å redusere barnetillegget ditt for hele året. ",
                        Nynorsk to "",
                        English to "",
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
                        Bokmal to "",
                        Nynorsk to "",
                        English to "If",

                        )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "When",
                    )
                }
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to " your ",
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
                    textExpr(
                        Bokmal to "".expr(),
                        Nynorsk to "".expr(),
                        English to "or your ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + "'s ",
                    )
                }
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to "income has been changed, your child supplement will be recalculated. 50 percent of income that exceeds the exemption amount ",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektperiodisert() or pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektperiodisert())) {
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "is recalculated to an annual amount of ",
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
                        Bokmal to "",
                        Nynorsk to "",
                        English to "is ",
                    )
                }
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to "NOK ",
                )

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                    textExpr(
                        Bokmal to "".expr(),
                        Nynorsk to "".expr(),
                        English to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                            .format(),
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())) {
                    textExpr(
                        Bokmal to "".expr(),
                        Nynorsk to "".expr(),
                        English to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                            .format(),
                    )
                }
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to ". ",
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
                        Bokmal to "",
                        Nynorsk to "",
                        English to "This amount will be used to reduce your child supplement during the calendar year.",
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
                    Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor ",
                    Nynorsk to "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor  ",
                    English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been ",
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
                        Bokmal to "lagt til",
                        Nynorsk to "lagt til",
                        English to "increased",
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
                        Bokmal to "redusert",
                        Nynorsk to "trekt frå",
                        English to "reduced",
                    )
                }
                text(
                    Bokmal to " ",
                    Nynorsk to " ",
                    English to " with NOK ",
                )

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                    textExpr(
                        Bokmal to pe.barnetilleggfelles_justeringsbelopperarutenminus().format(),
                        Nynorsk to pe.barnetilleggfelles_justeringsbelopperarutenminus().format(),
                        English to pe.barnetilleggfelles_justeringsbelopperarutenminus().format(),
                    )
                }

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) {
                    textExpr(
                        Bokmal to pe.barnetilleggserkull_justeringsbelopperarutenminus().format(),
                        Nynorsk to pe.barnetilleggserkull_justeringsbelopperarutenminus().format(),
                        English to pe.barnetilleggserkull_justeringsbelopperarutenminus().format(),
                    )
                }
                text(
                    Bokmal to " kroner i beløpet vi reduserer barnetillegget med for resten av året. ",
                    Nynorsk to " kroner i beløpet vi reduserer barnetillegget med for resten av året. ",
                    English to ". ",
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
                        Bokmal to "",
                        Nynorsk to "Har det vore ei endring i inntekta ",
                        English to "",
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
                    textExpr(
                        Bokmal to "".expr(),
                        Nynorsk to "til deg eller ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " ",
                        English to "".expr(),
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt"))) {
                    text(
                        Bokmal to "",
                        Nynorsk to "di",
                        English to "",
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
                        Bokmal to "",
                        Nynorsk to "n",
                        English to "",
                    )
                }

                //IF(PE_Vedtaksdata_Kravhode_KravArsakType <> "endret_inntekt") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt"))) {
                    text(
                        Bokmal to "",
                        Nynorsk to ",",
                        English to "",
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        Bokmal to "",
                        Nynorsk to "Når inntekta di ",
                        English to "",
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
                    textExpr(
                        Bokmal to "".expr(),
                        Nynorsk to "eller til ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din ",
                        English to "".expr(),
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        Bokmal to "",
                        Nynorsk to "endrar seg,",
                        English to "",
                    )
                }
                text(
                    Bokmal to "",
                    Nynorsk to " blir reduksjonen av barnetilleggene vurdert på nytt.",
                    English to "",
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
                        Bokmal to "Har det vært en endring i inntekten din",
                        Nynorsk to "",
                        English to "",
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
                        Bokmal to " eller til din",
                        Nynorsk to "",
                        English to "",
                    )
                }

                //IF(( PE_UT_TBU605V_eller_til_din() )) THEN    INCLUDE ENDIF
                showIf(((pe.ut_tbu605v_eller_til_din()))) {
                    textExpr(
                        Bokmal to " ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + ",",
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        Bokmal to "Når inntekten din ",
                        Nynorsk to "",
                        English to "",
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
                    textExpr(
                        Bokmal to "eller til din ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " ",
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        Bokmal to "endrer seg,",
                        Nynorsk to "",
                        English to "",
                    )
                }
                text(
                    Bokmal to " blir reduksjonen av barnetilleggene vurdert på nytt. ",
                    Nynorsk to "",
                    English to "",
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
                        Bokmal to "",
                        Nynorsk to "",
                        English to "If",
                    )
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "When",
                    )
                }
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to " your ",
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
                    textExpr(
                        Bokmal to "".expr(),
                        Nynorsk to "".expr(),
                        English to "or your ".expr() + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + "'s ",
                    )
                }
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to "income has been changed, your child supplement will be recalculated.",
                )
            }
        }
        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_1_3() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_1_3())) {
            //[TBU052V-TBU073V]

            paragraph {
                textExpr(
                    Bokmal to "50 prosent av den inntekten som overstiger fribeløpet for ".expr() + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre ",
                    Nynorsk to "50 prosent av inntekta som overstig fribeløpet for ".expr() + pe.ut_barnet_barna_felles() + " som bur med begge foreldra ",
                    English to "50 percent of the income that exceeds the exemption amount for the ".expr() + pe.ut_barnet_barna_felles() + " that ",
                )

//PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles = 1
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().equalTo(1)) {
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "lives",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles > 1) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().greaterThan(1))) {
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "live",
                    )
                }
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to " with both of their parents, ",
                )
                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true
                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert()) {
                    text(
                        Bokmal to "blir omregnet til et årlig beløp som tilsvarer ",
                        Nynorsk to "sine blir rekna om til et årleg beløp som svarer til ",
                        English to "is recalculated to an annual amount of",
                    )
                }

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false
                showIf(not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert())) {
                    text(
                        Bokmal to "er ",
                        Nynorsk to "er ",
                        English to "is",
                    )
                }
                textExpr(
                    Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + " kroner. ",
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + " kroner. ",
                    English to " NOK ".expr() + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". ",
                )

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0
                showIf(
                    pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(0)
                ) {
                    text(
                        Bokmal to "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året.",
                        Nynorsk to "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året.",
                        English to "This amount will be used to reduce this child supplement during the calendar year.",
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
                    Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor ",
                    Nynorsk to "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor ",
                    English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been ",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr > 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .greaterThan(0))
                ) {
                    text(
                        Bokmal to "lagt til",
                        Nynorsk to "lagt til",
                        English to "increased",
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .lessThan(0))
                ) {
                    text(
                        Bokmal to "trukket fra",
                        Nynorsk to "trekt frå",
                        English to "reduced",
                    )
                }
                textExpr(
                    Bokmal to " ".expr() + pe.barnetilleggfelles_justeringsbelopperarutenminus()
                        .format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                    Nynorsk to " ".expr() + pe.barnetilleggfelles_justeringsbelopperarutenminus()
                        .format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                    English to " with NOK ".expr() + pe.barnetilleggfelles_justeringsbelopperarutenminus().format() + ".",
                )
            }
        }

        //IF(PE_UT_TBU613V() = true AND PE_UT_TBU613V_4_5() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_4_5())) {
            //[TBU052V-TBU073V]

            paragraph {
                textExpr(
                    Bokmal to "For ".expr() + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene ",
                    Nynorsk to "For ".expr() + pe.ut_barnet_barna_serkull() + " som ikkje bur med begge foreldra ",
                    English to "For ".expr(),
                )
                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().equalTo(1)) {
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "a ",
                    )
                }
                textExpr(
                    Bokmal to "".expr(),
                    Nynorsk to "".expr(),
                    English to pe.ut_barnet_barna_serkull() + " that ",
                )

                showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().equalTo(1)) {
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "does",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull > 1) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_antallbarnserkull().greaterThan(1))) {
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "do",
                    )
                }
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to " not live with both parents ",
                )
                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = false
                showIf(not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert())) {
                    text(
                        Bokmal to "er 50 prosent av den inntekten som overstiger fribeløpet ",
                        Nynorsk to "er 50 prosent av den inntekta som overstig fribeløpet ",
                        English to "50 percent of the income that exceeds the exemption amount ",
                    )
                }

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = true
                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert()) {
                    text(
                        Bokmal to "blir 50 prosent av den inntekten som overstiger fribeløpet omregnet til et årlig beløp som tilsvarer ",
                        Nynorsk to "blir 50 prosent av den inntekta som overstig fribeløpet regna om til et årleg beløp som svarer til ",
                        English to "is recalculated to an annual amount of",

                        )
                }
                showIf(not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert())) {
                    text(
                        Bokmal to "",
                        Nynorsk to "",
                        English to "is",
                    )
                }

                textExpr(
                    Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + " kroner. ",
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + " kroner. ",
                    English to " NOK ".expr() + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". ",
                )

                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0
                showIf(
                    pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(0)
                ) {
                    text(
                        Bokmal to "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året. ",
                        Nynorsk to "Dette beløpet bruker vi til å redusere dette barnetillegget for heile året. ",
                        English to "This amount will be used to reduce this child supplement during the calendar year.",
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
                    Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor ",
                    Nynorsk to "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor ",
                    English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been ",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr > 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .greaterThan(0))
                ) {
                    text(
                        Bokmal to "lagt til",
                        Nynorsk to "lagt til",
                        English to "increased",
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .lessThan(0))
                ) {
                    text(
                        Bokmal to "trukket fra",
                        Nynorsk to "trekt frå",
                        English to "reduced",
                    )
                }
                textExpr(
                    Bokmal to " ".expr() + pe.barnetilleggserkull_justeringsbelopperarutenminus()
                        .format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                    Nynorsk to " ".expr() + pe.barnetilleggserkull_justeringsbelopperarutenminus()
                        .format() + " kroner i beløpet vi reduserer barnetillegget med for resten av året.",
                    English to " with NOK ".expr() + pe.barnetilleggserkull_justeringsbelopperarutenminus().format() + ".",
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
                            Bokmal to "Reduksjon av barnetillegg for fellesbarn før skatt ",
                            Nynorsk to "Reduksjon av barnetillegg for fellesbarn før skatt ",
                            English to "Reduction of child supplement payment for joint children before tax,",
                            FontType.BOLD
                        )

                        //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM(1)) = false) THEN      INCLUDE ENDIF
                        showIf((not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkarvirkningfom())))) {
                            text(
                                Bokmal to "i år",
                                Nynorsk to "i år",
                                English to " for this year",
                                FontType.BOLD
                            )
                        }

                        //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM(1)) = true) THEN      INCLUDE ENDIF
                        showIf((FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkarvirkningfom()))) {
                            text(
                                Bokmal to "for neste år",
                                Nynorsk to "for neste år",
                                English to " for next year",
                                FontType.BOLD
                            )
                        }
                    }
                    column(columnSpan = 1,alignment = ColumnAlignment.RIGHT) {}
                }) {
                    row {
                        cell {
                            text(
                                Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                                Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                                English to "Yearly child supplement before income reduction  ",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbruttoperar()
                                    .format() + " kr",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbruttoperar()
                                    .format() + " kr",
                                English to "NOK ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbruttoperar()
                                    .format(),
                            )
                        }
                    }
                    row {
                        cell {
                            textExpr(
                                Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning()
                                    .format() + " kr",
                                Nynorsk to "Samla inntekt brukt i fastsetting av barnetillegget er ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning()
                                    .format() + " kr",
                                English to "Total income applied in calculation of reduction in child supplement for joint children are NOK ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning()
                                    .format(),
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
                                textExpr(
                                    Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                        .format() + " kr",
                                    Nynorsk to "Fribeløp brukt i fastsetting av barnetillegget er ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                        .format() + " kr",
                                    English to "Exemption amount applied in calculation of reduction in child supplement are NOK ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
                                        .format(),
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
                                textExpr(
                                    Bokmal to "Inntekt over fribeløpet er ".expr() + pe.ut_btfbinntektbruktiavkortningminusbtfbfribelop()
                                        .format() + " kr",
                                    Nynorsk to "Inntekt over fribeløpet er ".expr() + pe.ut_btfbinntektbruktiavkortningminusbtfbfribelop()
                                        .format() + " kr",
                                    English to "Income exceeding the exemption amount NOK ".expr() + pe.ut_btfbinntektbruktiavkortningminusbtfbfribelop()
                                        .format(),
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
                                    Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet",
                                    Nynorsk to "- 50 prosent av inntekta som overstig fribeløpet",
                                    English to "- 50 percent of income exceeding the allowance amount ",
                                    FontType.BOLD
                                )

                                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true
                                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert()) {
                                    text(
                                        Bokmal to "(oppgitt som et årlig beløp)",
                                        Nynorsk to "(oppgitt som eit årleg beløp)",
                                        English to "(calculated to an annual amount)",
                                        FontType.BOLD
                                    )
                                }
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                                        .format() + " kr",
                                    Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                                        .format() + " kr",
                                    English to "NOK ".expr() + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                                        .format(),
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
                                        Bokmal to "-",
                                        Nynorsk to "-",
                                        English to "-",
                                    )
                                }

                                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                                showIf(
                                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                                        .lessThan(0))
                                ) {
                                    text(
                                        Bokmal to "+",
                                        Nynorsk to "+",
                                        English to "+",
                                    )
                                }
                                text(
                                    Bokmal to " Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                    Nynorsk to " Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                    English to " Amount which is used to adjust the reduction of child supplement",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar_utenminus()
                                        .format() + " kr",
                                    Nynorsk to pe.barnetilleggfelles_justeringsbelopperarutenminus().format() + " kr",
                                    English to "NOK ".expr() + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar_utenminus()
                                        .format(),
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
                                    Bokmal to "= Årlig barnetillegg etter reduksjon ut fra inntekt",
                                    Nynorsk to "= Årleg barnetillegg etter reduksjon ut frå inntekt",
                                    English to "= Calculation of the yearly reduction of the child supplement",
                                    FontType.BOLD
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbnettoperar().format() + " kr",
                                    Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbnettoperar().format() + " kr",
                                    English to "NOK ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbnettoperar().format(),
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
                                    Bokmal to "Utbetaling av barnetillegg per måned ",
                                    Nynorsk to "Utbetaling av barnetillegg per månad ",
                                    English to "Child supplement payment for the remaining months of the year",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                                        .format() + " kr",
                                    Nynorsk to pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                                        .format() + " kr",
                                    English to "NOK ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                                        .format(),
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
                                    Bokmal to "Grensen for å få utbetalt barnetillegg",
                                    Nynorsk to "Grensa for å få utbetalt barnetillegg",
                                    English to "The income limit for receiving child supplement",
                                    FontType.BOLD
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + " kr",
                                    Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format() + " kr",
                                    English to "NOK ".expr() + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak().format(),
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
                textExpr (
                    Bokmal to "Du vil få utbetalt ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                        .format() + " kroner i måneden før skatt i barnetillegg",
                    Nynorsk to "Du vil få utbetalt ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                        .format() + " kroner i månaden før skatt i barnetillegg",
                    English to "You will receive a monthly child supplement payment of NOK ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                        .format(),
                )

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_UT_Etteroppgjor_BT_Utbetalt() = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.ut_etteroppgjor_bt_utbetalt())){
                    textExpr (
                        Bokmal to " for ".expr() + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre",
                        Nynorsk to " for ".expr() + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine",
                        English to " for the ".expr() + pe.ut_barnet_barna_felles() + " who live together with both parents",
                    )
                }
                text (
                    Bokmal to ". ",
                    Nynorsk to ".",
                    English to ".",
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
                        Bokmal to "Du får ikke utbetalt barnetillegget ",
                        Nynorsk to "Du får ikkje utbetalt barnetillegget ",
                        English to "You will not receive a child supplement ",
                    )
                }

                //IF(PE_UT_TBU608_Far_Ikke() = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_tbu608_far_ikke() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())){
                    textExpr (
                        Bokmal to "for ".expr() + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre ",
                        Nynorsk to "for ".expr() + pe.ut_barnet_barna_felles() + " som bur saman med begge foreldra sine ",
                        English to "for the ".expr() + pe.ut_barnet_barna_felles() + " who live together with both parents ",
                    )
                }

                //IF(PE_UT_TBU608_Far_Ikke() = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_tbu608_far_ikke())){
                    text (
                        Bokmal to "fordi samlet inntekt er over grensen for å få utbetalt barnetillegg. ",
                        Nynorsk to "fordi samla inntekt er over grensa for å få utbetalt barnetillegg. ",
                        English to "because your income is over the income limit for receiving a child supplement. ",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                        .equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .notEqualTo(0))){
                    text (
                        Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. ",
                        Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. ",
                        English to "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year.",
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
                                Bokmal to "Reduksjon av barnetillegg for særkullsbarn før skatt ",
                                Nynorsk to "Reduksjon av barnetillegg for særkullsbarn før skatt ",
                                English to "Reduction of child supplement payment for children from a previous relationship before tax, ",
                                FontType.BOLD
                            )

                            //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM(1)) = false) THEN      INCLUDE ENDIF
                            showIf((not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkarvirkningfom())))){
                                text (
                                    Bokmal to "i år",
                                    Nynorsk to "i år",
                                    English to "for this year",
                                    FontType.BOLD
                                )
                            }

                            //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVirkningFOM(1)) = true) THEN      INCLUDE ENDIF
                            showIf((FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkarvirkningfom()))){
                                text (
                                    Bokmal to "for neste år",
                                    Nynorsk to "for neste år",
                                    English to "for next year",
                                    FontType.BOLD
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
                                Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                                Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                                English to "Yearly child supplement before income reduction  ",
                                FontType.BOLD
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbbruttoperar().format() + " kr",
                                Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbbruttoperar().format() + " kr",
                                English to "NOK ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbbruttoperar().format(),
                                FontType.BOLD
                            )
                        }
                    }
                    row {
                        cell {
                            textExpr (
                                Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning()
                                    .format() + " kr",
                                Nynorsk to "Samla inntekt brukt i fastsetting av barnetillegget er ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning()
                                    .format() + " kr",
                                English to "Total income applied in calculation of reduction in child supplement from a previous relationship NOK ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning()
                                    .format(),
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
                                textExpr(
                                    Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kr",
                                    Nynorsk to "Fribeløp brukt i fastsetting av barnetillegget er ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format() + " kr",
                                    English to "Exemption amount applied in calculation of reduction in child supplement are NOK ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop().format(),
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
                                textExpr(
                                    Bokmal to "Inntekt over fribeløpet er ".expr() + pe.ut_btsbinntektbruktiavkortningminusbtsbfribelop().format() + " kr",
                                    Nynorsk to "Inntekt over fribeløpet er ".expr() + pe.ut_btsbinntektbruktiavkortningminusbtsbfribelop().format() + " kr",
                                    English to "Income exceeding the exemption amount NOK ".expr() + pe.ut_btsbinntektbruktiavkortningminusbtsbfribelop().format(),
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
                                    Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet",
                                    Nynorsk to "- 50 prosent av inntekta som overstig fribeløpet",
                                    English to "- 50 percent of income exceeding the allowance amount ",
                                    FontType.BOLD
                                )

                                //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true
                                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert()) {
                                    text(
                                        Bokmal to "(oppgitt som et årlig beløp)",
                                        Nynorsk to "(oppgitt som eit årleg beløp)",
                                        English to "(calculated to an annual amount)",
                                        FontType.BOLD
                                    )
                                }
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar().format() + " kr",
                                    Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar().format() + " kr",
                                    English to "NOK ".expr() + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar().format(),
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
                                        Bokmal to "-",
                                        Nynorsk to "-",
                                        English to "-",
                                    )
                                }

                                //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr < 0) THEN      INCLUDE ENDIF
                                showIf(
                                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                                        .lessThan(0))
                                ) {
                                    text(
                                        Bokmal to "+",
                                        Nynorsk to "+",
                                        English to "+",
                                    )
                                }
                                text(
                                    Bokmal to " Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                    Nynorsk to " Beløp som er brukt for å justere reduksjonen av barnetillegget",
                                    English to " Amount which is used to adjust the reduction of child supplement",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar_utenminus()
                                        .format() + " kr",
                                    Nynorsk to pe.barnetilleggserkull_justeringsbelopperarutenminus().format() + " kr",
                                    English to "NOK ".expr() + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar_utenminus()
                                        .format(),
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
                                    Bokmal to "= Årlig barnetillegg etter reduksjon ut fra inntekt",
                                    Nynorsk to "= Årleg barnetillegg etter reduksjon ut frå inntekt",
                                    English to "= Calculation of the yearly reduction of the child supplement",
                                    FontType.BOLD
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbnettoperar().format() + " kr",
                                    Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbnettoperar().format() + " kr",
                                    English to "NOK ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_btsbnettoperar().format(),
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
                                    Bokmal to "Utbetaling av barnetillegg per måned ",
                                    Nynorsk to "Utbetaling av barnetillegg per månad ",
                                    English to "Child supplement payment for the remaining months of the year",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().format() + " kr",
                                    Nynorsk to pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().format() + " kr",
                                    English to "NOK ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().format(),
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
                                    Bokmal to "Grensen for å få utbetalt barnetillegg",
                                    Nynorsk to "Grensa for å få utbetalt barnetillegg",
                                    English to "The income limit for receiving child supplement ",
                                    FontType.BOLD
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kr",
                                    Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format() + " kr",
                                    English to "NOK ".expr() + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak().format(),
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
                textExpr (
                    Bokmal to "Du vil få utbetalt ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
                        .format() + " kroner i måneden før skatt i barnetillegg",
                    Nynorsk to "Du vil få utbetalt ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
                        .format() + " kroner i månaden før skatt i barnetillegg",
                    English to "You will receive a monthly child supplement payment of NOK ".expr() + pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
                        .format(),
                )

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_UT_Etteroppgjor_BT_Utbetalt() = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and pe.ut_etteroppgjor_bt_utbetalt())){
                    textExpr (
                        Bokmal to " for ".expr() + pe.ut_barnet_barna_serkull() + " som ikke bor med begge sine foreldre",
                        Nynorsk to " for ".expr() + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra",
                        English to " for the ".expr() + pe.ut_barnet_barna_serkull() + " who do not live together with both parents",
                    )
                }
                text (
                    Bokmal to ". ",
                    Nynorsk to ". ",
                    English to ".",
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
                        Bokmal to "Du får ikke utbetalt barnetillegget ",
                        Nynorsk to "Du får ikkje utbetalt barnetillegget ",
                        English to "You will not receive a child supplement ",
                    )
                }

                //IF(PE_UT_TBU611_Far_Ikke() = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_tbu611_far_ikke() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget())){
                    textExpr (
                        Bokmal to "for ".expr() + pe.ut_barnet_barna_serkull() + " som ikke bor med begge sine foreldre ",
                        Nynorsk to "for ".expr() + pe.ut_barnet_barna_serkull() + " som ikkje bur saman med begge foreldra ",
                        English to "for the ".expr() + pe.ut_barnet_barna_serkull() + " who do not live together with both parents ",
                    )
                }

                //IF(PE_UT_TBU611_Far_Ikke() = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_tbu611_far_ikke())){
                    text (
                        Bokmal to "fordi samlet inntekt er over grensen for å få utbetalt barnetillegg. ",
                        Nynorsk to "fordi samla inntekt er over grensa for å få utbetalt barnetillegg. ",
                        English to "because your income is over the income limit for receiving a child supplement. ",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
                        .equalTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .notEqualTo(0))){
                    text (
                        Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                        Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. ",
                        English to "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year. ",
                    )
                }
            }
        }
    }
}