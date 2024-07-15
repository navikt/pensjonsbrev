package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy

import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_URL
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner

data class TBU052V_TBU073V_Del_6_SlikRedusererViBarnetilleggetUtFraInntekt(
    val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget: Expression<Boolean>,
    val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget: Expression<Boolean>,
    val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT: Expression<String>,
    val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN_cohabiting_partner: Expression<String>,
    val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall: Expression<String>,
    val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto: Expression<Kroner>,
    val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Slik reduserer vi barnetillegget ut fra inntekt",
                Nynorsk to "Slik reduserer vi barnetillegget ut frå inntekt",
                English to "This is how the reduction of your child supplement is calculated ",
            )
        }

        paragraph {
            text(
                Bokmal to "Størrelsen på barnetillegget er avhengig av inntekt. Barnetillegget blir redusert ut fra personinntekt. Dette kan for eksempel være:",
                Nynorsk to "Storleiken på barnetillegget er avhengig av inntekt.Barnetillegget kan bli redusert ut frå personinntekt. Dette kan til dømes være:",
                English to "The amount of child supplement depends on your income. The child supplement is reduced on the basis of personal income. This can be for example:",
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
            text(
                Bokmal to "Du kan lese mer om personinntekt på $SKATTEETATEN_URL.",
                Nynorsk to "Du kan lese meir om personinntekt på $SKATTEETATEN_URL.",
                English to "You can read more about personal income at $SKATTEETATEN_URL",
            )
        }

        paragraph {
            textExpr(
                Bokmal to "Det er inntekten ".expr(),
                Nynorsk to "Det er inntekta ".expr(),
                English to "How much child supplement you receive during the year depends on the incomes of you and your ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN_cohabiting_partner + ". If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced. ",
            )

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
            showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) {
                textExpr(
                    Bokmal to "til deg og din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " ",
                    Nynorsk to "til deg og ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din ",
                    English to "".expr(),
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) THEN      INCLUDE ENDIF
            showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget)) {
                text(
                    Bokmal to "din ",
                    Nynorsk to "di ",
                    English to "",
                )
            }
            text(
                Bokmal to "som avgjør hva du får utbetalt i barnetillegg i løpet av året. Er inntekten høyere enn fribeløpet blir barnetillegget redusert. ",
                Nynorsk to "som avgjer kva du får utbetalt i barnetillegg i løpet av året. Er inntekta høgare enn fribeløpet blir barnetillegget redusert. ",
                English to "",
            )
        }

        paragraph {

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
            showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) {
                text(
                    Bokmal to "For barn som bor sammen med begge sine foreldre, er fribeløpet 4,6 ganger folketrygdens grunnbeløp. ",
                    Nynorsk to "For barn som bur saman med begge foreldra sine, er fribeløpet 4,6 gonger grunnbeløpet i folketrygda. ",
                    English to "For the child/children who live together with both parents, the exemption amount is 4,6 times the National Insurance basic amount. ",
                )
            }

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true
            showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget) {
                text(
                    Bokmal to "For barn som ikke bor sammen med begge sine foreldre, er fribeløpet 3,1 ganger folketrygdens grunnbeløp. ",
                    Nynorsk to "For barn som ikkje bur saman med begge foreldra, er fribeløpet 3,1 gonger grunnbeløpet i folketrygda. ",
                    English to "For the child/children who do not live together with both parents, the exemption amount is 3,1 times the National Insurance basic amount. ",
                )
            }
            text(
                Bokmal to "Fribeløpet øker med 0,4 ganger folketrygdens grunnbeløp for hvert ekstra barn. ",
                Nynorsk to "Fribeløpet aukar med 0,4 gonger grunnbeløpet i folketrygda for kvart ekstra barn. ",
                English to "The exemption amount is increased by 0,4 times the National Insurance basic amount for each additional child. ",
            )
        }


        //IF(PE_UT_TBU601V_TBU604V() = true AND ((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0)  OR (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0))  ) THEN      INCLUDE ENDIF
        showIf(
            (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0)
                    and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget
                    and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.equalTo(0))
                or (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0)
                    and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.equalTo(0))
        ) {
            //[TBU052V-TBU073V]

            paragraph {
                text(
                    Bokmal to "Barnetillegget blir redusert med 50 prosent av inntekten som overstiger fribeløpet. Er inntekten ",
                    Nynorsk to "Barnetillegget blir redusert med 50 prosent av inntekta som overstig fribeløpet. Er inntekta ",
                    English to "The child supplement will be reduced by 50 percent of any income exceeding the allowance amount. You will not receive child supplement if the income ",
                )

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                showIf(
                    (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(
                        0
                    ))
                ) {
                    textExpr(
                        Bokmal to "til deg og din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " ",
                        Nynorsk to "til deg og ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din ",
                        English to "of you and your ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN_cohabiting_partner + " ",
                    )
                }

                //IF(
                // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false
                // OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0  AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr > 0)) THEN      INCLUDE ENDIF
                showIf(
                    (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget))
                            or (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget
                                and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget
                                and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0)
                            and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.greaterThan(0))) {
                    text(
                        Bokmal to "din ",
                        Nynorsk to "di ",
                        English to "",
                    )
                }
                text(
                    Bokmal to "over grensen for å få utbetalt barnetillegg, blir ikke barnetillegget utbetalt. ",
                    Nynorsk to "over grensa for å få utbetalt barnetillegg, blir ikkje barnetillegget utbetalt.",
                    English to "exceeds the income limit.",
                )
            }
        }

    }
}