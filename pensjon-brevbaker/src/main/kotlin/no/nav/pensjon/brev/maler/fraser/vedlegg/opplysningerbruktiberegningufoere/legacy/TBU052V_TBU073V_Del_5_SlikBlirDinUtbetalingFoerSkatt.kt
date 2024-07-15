package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner

data class TBU052V_TBU073V_Del_5_SlikBlirDinUtbetalingFoerSkatt(
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad: Expression<Int>,
    val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad: Expression<Int>,
    val PE_Vedtaksdata_Kravhode_KravArsakType: Expression<String>,
    val PE_pebrevkode: Expression<String>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad: Expression<Int>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense: Expression<Kroner>,
    val PE_UT_NettoAkk_pluss_NettoRestAr: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert: Expression<Boolean>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Fradrag: Expression<Kroner>,
    val PE_UT_NettoAkk_pluss_NettoRestAr_pluss_ForventetInntekt: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
//IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300") THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.lessThan(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt") and PE_pebrevkode.notEqualTo("PE_UT_04_108") and PE_pebrevkode.notEqualTo("PE_UT_04_109") and PE_pebrevkode.notEqualTo("PE_UT_07_200") and PE_pebrevkode.notEqualTo("PE_UT_06_300"))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Slik blir din utbetaling før skatt",
                    Nynorsk to "Slik blir den månadlege utbetalinga di før skatt",
                    English to "This is your monthly payment before tax",
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_06_300" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt") and PE_pebrevkode.notEqualTo("PE_UT_04_108") and PE_pebrevkode.notEqualTo("PE_UT_04_109") and PE_pebrevkode.notEqualTo("PE_UT_06_300") and PE_pebrevkode.notEqualTo("PE_UT_07_200") and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.greaterThan(0))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Brutto beregnet uføretrygd som følge av innmeldt inntekt:",
                    Nynorsk to "Brutto berekna uføretrygd som følgje av innmeld inntekt:",
                    English to "Gross estimated disability benefit corresponding to a reported income:",
                )
                textExpr (
                    Bokmal to PE_UT_NettoAkk_pluss_NettoRestAr.format() + " kr",
                    Nynorsk to PE_UT_NettoAkk_pluss_NettoRestAr.format() + " kr",
                    English to PE_UT_NettoAkk_pluss_NettoRestAr.format() + " NOK",
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_06_300" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt") and PE_pebrevkode.notEqualTo("PE_UT_04_108") and PE_pebrevkode.notEqualTo("PE_UT_04_109") and PE_pebrevkode.notEqualTo("PE_UT_06_300") and PE_pebrevkode.notEqualTo("PE_UT_07_200") and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.greaterThan(0))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "- Utbetalt uføretrygd hittil i år:",
                    Nynorsk to "- Utbetalt uføretrygd hittil i år:",
                    English to "- Disability benefit payments so far this year:",
                )
                textExpr (
                    Bokmal to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk.format() + " kr",
                    Nynorsk to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk.format() + " kr",
                    English to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk.format() + " NOK",
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_06_300" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt") and PE_pebrevkode.notEqualTo("PE_UT_04_108") and PE_pebrevkode.notEqualTo("PE_UT_04_109") and PE_pebrevkode.notEqualTo("PE_UT_06_300") and PE_pebrevkode.notEqualTo("PE_UT_07_200") and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.greaterThan(0))){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "= Utbetaling av uføretrygd for resterende måneder i året:",
                    Nynorsk to "= Utbetaling av uføretrygd for resterande månader i året:",
                    English to "= Disability benefit payments for the remaining months of the year:",
                )
                textExpr (
                    Bokmal to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr.format() + " kr",
                    Nynorsk to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr.format() + " kr",
                    English to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr.format() + " NOK",
                )
            }
        }


        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_06_300" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert and PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto.greaterThan(0) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt") and PE_pebrevkode.notEqualTo("PE_UT_04_108") and PE_pebrevkode.notEqualTo("PE_UT_06_300") and PE_pebrevkode.notEqualTo("PE_UT_04_109") and PE_pebrevkode.notEqualTo("PE_UT_07_200") and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.greaterThan(0))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Du vil få en månedlig reduksjon i uføretrygden din på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Fradrag.format() + " kroner i resterende måneder i kalenderåret.",
                    Nynorsk to "Du får ein månadleg reduksjon i uføretrygda di på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Fradrag.format() + " kroner i resterande månader i kalenderåret.",
                    English to "Your monthly disability benefit payments will be reduced by NOK ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Fradrag.format() + " for the remaining months of the calendar year.",
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto > 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_06_300" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) and PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto.greaterThan(0) and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt") and PE_pebrevkode.notEqualTo("PE_UT_04_108") and PE_pebrevkode.notEqualTo("PE_UT_04_109") and PE_pebrevkode.notEqualTo("PE_UT_06_300") and PE_pebrevkode.notEqualTo("PE_UT_07_200") and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.greaterThan(0))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Uføretrygden og inntekten din vil ut fra dette til sammen utgjøre ".expr() + PE_UT_NettoAkk_pluss_NettoRestAr_pluss_ForventetInntekt.format() + " kroner for dette året.",
                    Nynorsk to "Uføretrygda di og inntekta di utgjer til saman ".expr() + PE_UT_NettoAkk_pluss_NettoRestAr_pluss_ForventetInntekt.format() + " kroner i dette året.",
                    English to "Your disability benefit and income together will total NOK ".expr() + PE_UT_NettoAkk_pluss_NettoRestAr_pluss_ForventetInntekt.format() + " for this year.",
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense >= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = 0) THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.greaterThanOrEqual(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt") and PE_pebrevkode.notEqualTo("PE_UT_04_108") and PE_pebrevkode.notEqualTo("PE_UT_04_109") and PE_pebrevkode.notEqualTo("PE_UT_07_200") and PE_pebrevkode.notEqualTo("PE_UT_06_300") and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.equalTo(0))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Utbetalingen av uføretrygden din er redusert, fordi du har inntekt. Den innmeldte inntekten er høyere enn inntektsgrensen din på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.format() + " kroner og uføretrygden blir derfor ikke utbetalt. ",
                    Nynorsk to "Utbetalinga av uføretrygda di er redusert, fordi du har inntekt. Den innmelde inntekta er høgare enn inntektsgrensa di på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.format() + " kroner og uføretrygda blir derfor ikkje utbetalt.",
                    English to "Your payment have been reduced because you have an income. The reported income is higher then your income cap of NOK ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.format() + ". Your disability benefit will therefore not be paid.",
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.equalTo(0) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.greaterThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Du får ikke utbetalt uføretrygd siden inntekten din er høyere enn 80 prosent av inntekten du hadde før du ble ufør, det vil si ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak.format() + " kroner.",
                    Nynorsk to "Du får ikkje utbetalt uføretrygd fordi inntekta di er høgare enn 80 prosent av inntekta du hadde før du blei ufør, det vil si ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak.format() + " kroner.",
                    English to "You will not receive disability benefit because your income exceeds 80 percent of your income prior to disability, which is NOK ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak.format() + ". ",
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300") THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt") and PE_pebrevkode.notEqualTo("PE_UT_04_108") and PE_pebrevkode.notEqualTo("PE_UT_04_109") and PE_pebrevkode.notEqualTo("PE_UT_07_200") and PE_pebrevkode.notEqualTo("PE_UT_06_300"))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Du vil få tilbake ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad.format() + " prosent uføretrygd uten søknad, dersom du tjener mindre enn inntektsgrensen din. Hvis du allerede har fått utbetalt det du har rett til i uføretrygd for kalenderåret, vil du ikke få utbetalt uføretrygd med den opprinnelige uføregraden din før neste kalenderår.",
                    Nynorsk to "Du får tilbake ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad.format() + " prosent uføretrygd utan søknad dersom du tener mindre enn inntektsgrensa di. Dersom du allereie har fått utbetalt det du har rett til i uføretrygd for kalenderåret, får du ikkje utbetalt uføretrygd med den opphavlege uføregraden din før neste kalenderår.",
                    English to "You will get payed the full amount of your disability benefit if your income falls below your income limit. If you already have been paid what you are entitled to in disability benefits this calendar year, you will not receive any disability benefit payments at your original degree of disability until the next calendar year.".expr(),
                )
            }
        }
    }

}
