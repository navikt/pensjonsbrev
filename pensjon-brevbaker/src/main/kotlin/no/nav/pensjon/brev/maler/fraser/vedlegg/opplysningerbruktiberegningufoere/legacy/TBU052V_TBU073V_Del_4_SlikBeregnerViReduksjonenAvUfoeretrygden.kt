package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner

data class TBU052V_TBU073V_Del_4_SlikBeregnerViReduksjonenAvUfoeretrygden(
    val PE_Vedtaksdata_Kravhode_KravArsakType: Expression<String>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt: Expression<Double>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense: Expression<Double>,
    val PE_UT_Overskytende: Expression<Double>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak: Expression<Double>,
    val PE_pebrevkode: Expression<String>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad: Expression<Int>,
    val PE_UT_OpplyningerOmBergeningen_NettoPerAr: Expression<Double>,
) : OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
//IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt >= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt") and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.greaterThanOrEqual(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) and PE_pebrevkode.notEqualTo("PE_UT_04_108") and PE_pebrevkode.notEqualTo("PE_UT_04_109") and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.greaterThan(0))){
            //[TBU052V-TBU073V]

            title1 {
                text (
                    Bokmal to "Slik beregner vi reduksjonen av uføretrygden",
                    Nynorsk to "Slik bereknar vi reduksjonen av uføretrygda",
                    English to "This is how the reduction in your disability benefit is calculated",
                )
            }
        }

        //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt >= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt") and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.greaterThanOrEqual(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) and PE_pebrevkode.notEqualTo("PE_UT_04_108") and PE_pebrevkode.notEqualTo("PE_UT_04_109") and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.greaterThan(0))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to PE_UT_Overskytende.format() + " kr",
                    Nynorsk to PE_UT_Overskytende.format() + " kr",
                    English to "NOK ".expr() + PE_UT_Overskytende.format(),
                )
                text (
                    Bokmal to " x ",
                    Nynorsk to " x ",
                    English to " x ",
                )
                textExpr (
                    Bokmal to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad.format() + " %",
                    Nynorsk to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad.format() + " %",
                    English to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad.format() + " %",
                )
                text (
                    Bokmal to " = ",
                    Nynorsk to " = ",
                    English to " = ",
                )
                textExpr (
                    Bokmal to PE_UT_OpplyningerOmBergeningen_NettoPerAr.format(),
                    Nynorsk to PE_UT_OpplyningerOmBergeningen_NettoPerAr.format(),
                    English to "NOK ".expr() + PE_UT_OpplyningerOmBergeningen_NettoPerAr.format(),
                )
                text (
                    Bokmal to " kroner i reduksjon for året",
                    Nynorsk to " kroner i reduksjon for året",
                    English to " in reductions for the year",
                )
            }
        }
    }

}
