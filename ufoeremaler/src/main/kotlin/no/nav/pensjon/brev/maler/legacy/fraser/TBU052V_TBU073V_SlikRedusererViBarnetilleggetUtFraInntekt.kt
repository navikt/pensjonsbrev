package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_URL
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class TBU052V_TBU073V_SlikRedusererViBarnetilleggetUtFraInntekt(
    val pe: Expression<PE>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        title1 {
            text(
                bokmal { + "Slik reduserer vi barnetillegget ut fra inntekt" },
                nynorsk { + "Slik reduserer vi barnetillegget ut frå inntekt" },
                english { + "This is how the reduction of your child supplement is calculated " },
            )
        }

        paragraph {
            text(
                bokmal { + "Størrelsen på barnetillegget er avhengig av inntekt." },
                nynorsk { + "Storleiken på barnetillegget er avhengig av inntekt." },
                english { + "The amount of child supplement depends on your income." },
            )
        }
        paragraph {
            text(
                bokmal { + "Barnetillegget blir redusert ut fra personinntekt. Dette kan for eksempel være:" },
                nynorsk { + "Barnetillegget kan bli redusert ut frå personinntekt. Dette kan til dømes være:" },
                english { + "The child supplement is reduced on the basis of personal income. This can be for example:" },
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
            text(
                bokmal { + "Du kan lese mer om personinntekt på $SKATTEETATEN_URL." },
                nynorsk { + "Du kan lese meir om personinntekt på $SKATTEETATEN_URL." },
                english { + "You can read more about personal income at $SKATTEETATEN_URL" },
            )
        }

        paragraph {
            text(
                bokmal { + "Det er inntekten " },
                nynorsk { + "Det er inntekta " },
                english { + "How much child supplement you receive during the year depends on the incomes of you and your " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + ". If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced. " },
            )

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                text(
                    bokmal { + "til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " },
                    nynorsk { + "til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
                    english { + "" },
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) THEN      INCLUDE ENDIF
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget())) {
                text(
                    bokmal { + "din " },
                    nynorsk { + "di " },
                    english { + "" },
                )
            }
            text(
                bokmal { + "som avgjør hva du får utbetalt i barnetillegg i løpet av året. Er inntekten høyere enn fribeløpet blir barnetillegget redusert. " },
                nynorsk { + "som avgjer kva du får utbetalt i barnetillegg i løpet av året. Er inntekta høgare enn fribeløpet blir barnetillegget redusert. " },
                english { + "" },
            )
        }

        paragraph {

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()) {
                text(
                    bokmal { + "For barn som bor sammen med begge sine foreldre, er fribeløpet 4,6 ganger folketrygdens grunnbeløp. " },
                    nynorsk { + "For barn som bur saman med begge foreldra sine, er fribeløpet 4,6 gonger grunnbeløpet i folketrygda. " },
                    english { + "For the child/children who live together with both parents, the exemption amount is 4,6 times the National Insurance basic amount. " },
                )
            }

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true
            showIf(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()) {
                text(
                    bokmal { + "For barn som ikke bor sammen med begge sine foreldre, er fribeløpet 3,1 ganger folketrygdens grunnbeløp. " },
                    nynorsk { + "For barn som ikkje bur saman med begge foreldra, er fribeløpet 3,1 gonger grunnbeløpet i folketrygda. " },
                    english { + "For the child/children who do not live together with both parents, the exemption amount is 3,1 times the National Insurance basic amount. " },
                )
            }
            text(
                bokmal { + "Fribeløpet øker med 0,4 ganger folketrygdens grunnbeløp for hvert ekstra barn. " },
                nynorsk { + "Fribeløpet aukar med 0,4 gonger grunnbeløpet i folketrygda for kvart ekstra barn. " },
                english { + "The exemption amount is increased by 0,4 times the National Insurance basic amount for each additional child. " },
            )
        }


        //IF(pe_ut_tbu601v_tbu604v() = true AND ((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)  OR (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0))  ) THEN      INCLUDE ENDIF
        showIf(
            (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                .equalTo(
                    0
                )
                    and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
                    and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto().equalTo(0))
                or (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                .equalTo(
                    0
                )
                    and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
                .equalTo(
                    0
                ))
        ) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    bokmal { + "Barnetillegget blir redusert med 50 prosent av inntekten som overstiger fribeløpet. Er inntekten " },
                    nynorsk { + "Barnetillegget blir redusert med 50 prosent av inntekta som overstig fribeløpet. Er inntekta " },
                    english { + "The child supplement will be reduced by 50 percent of any income exceeding the allowance amount. You will not receive child supplement if the income " },
                )

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(
                            0
                        ))
                ) {
                    text(
                        bokmal { + "til deg og din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " },
                        nynorsk { + "til deg og " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
                        english { + "of you and your " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_en_cohabiting_partner() + " " },
                    )
                }

                //IF(
                // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false
                // OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr > 0)) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget() and not(pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()))
                            or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
                            and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()
                            and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(
                            0
                        )
                            and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .greaterThan(
                            0
                        ))) {
                    text(
                        bokmal { + "din " },
                        nynorsk { + "di " },
                        english { + "" },
                    )
                }
                text(
                    bokmal { + "over grensen for å få utbetalt barnetillegg, blir ikke barnetillegget utbetalt. " },
                    nynorsk { + "over grensa for å få utbetalt barnetillegg, blir ikkje barnetillegget utbetalt." },
                    english { + "exceeds the income limit." },
                )
            }
        }

    }
}