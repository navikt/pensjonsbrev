package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.PESelectors.ExstreamFunctionsSelectors.pe_ut_opplyningerombergeningen_nettoperar
import no.nav.pensjon.brev.api.model.maler.legacy.PESelectors.ExstreamFunctionsSelectors.pe_ut_overskytende
import no.nav.pensjon.brev.api.model.maler.legacy.PESelectors.functions
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class TBU052V_TBU073V_SlikBeregnerViReduksjonenAvUfoeretrygden(
    val pe: Expression<PE>
) : OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //IF(PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt >= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt") and pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt().greaterThanOrEqual(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()) and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut().greaterThan(0))){
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
        showIf((pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt") and pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt().greaterThanOrEqual(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense().lessThan(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()) and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut().greaterThan(0))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to pe.functions.pe_ut_overskytende.format() + " kr",
                    Nynorsk to pe.functions.pe_ut_overskytende.format() + " kr",
                    English to "NOK ".expr() + pe.functions.pe_ut_overskytende.format(),
                )
                text (
                    Bokmal to " x ",
                    Nynorsk to " x ",
                    English to " x ",
                )
                textExpr (
                    Bokmal to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                        .formatTwoDecimals() + " %",
                    Nynorsk to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                        .formatTwoDecimals() + " %",
                    English to pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad()
                        .formatTwoDecimals() + " %",
                )
                text (
                    Bokmal to " = ",
                    Nynorsk to " = ",
                    English to " = ",
                )
                textExpr (
                    Bokmal to pe.functions.pe_ut_opplyningerombergeningen_nettoperar.format(),
                    Nynorsk to pe.functions.pe_ut_opplyningerombergeningen_nettoperar.format(),
                    English to "NOK ".expr() + pe.functions.pe_ut_opplyningerombergeningen_nettoperar.format(),
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
