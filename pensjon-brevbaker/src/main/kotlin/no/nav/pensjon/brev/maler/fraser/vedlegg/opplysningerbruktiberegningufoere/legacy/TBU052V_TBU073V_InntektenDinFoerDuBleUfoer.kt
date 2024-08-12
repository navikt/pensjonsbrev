package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text

data class TBU052V_TBU073V_InntektenDinFoerDuBleUfoer(
    val PE_pebrevkode: Expression<String>,
    val FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt_LIK_3_3_G: Expression<Boolean>,
    val FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt_LIK_3_5_G: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        //IF(  ( PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt(1) = FF_Round(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop * 3.3) OR PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt(1) = FF_Round(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop * 3.5) )  AND  ( PE_pebrevkode = "PE_UT_14_300" OR PE_pebrevkode = "PE_UT_04_300" )  ) THEN      INCLUDE ENDIF
        showIf((FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt_LIK_3_3_G or FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt_LIK_3_5_G) and (PE_pebrevkode.equalTo("PE_UT_14_300") or PE_pebrevkode.equalTo("PE_UT_04_300"))){
            //[TBU052V-TBU073V]

            title1 {
                text (
                    Bokmal to "Inntekten din før du ble ufør",
                    Nynorsk to "Inntekta di før du blei ufør",
                    English to "This is how we establish your income prior to your disability",
                )
            }

            paragraph {
                text (
                    Bokmal to "Du hadde begrenset yrkesaktivitet og inntekt før du ble ufør. Ved overgangen til uføretrygd, skal inntekten din før du ble ufør derfor settes til minst ",
                    Nynorsk to "Du hadde avgrensa yrkesaktivitet og inntekt før du blei ufør. Ved overgangen til uføretrygd skal inntekta di før du blei ufør, derfor setjast til minst ",
                    English to "You had limited employment and income before you became disabled. At the time of converting, the minimum level of income prior to the time of disability will be equivalent to ",
                )

                //IF(  PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt(1)  =  FF_Round(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop * 3.5)  ) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt_LIK_3_5_G)){
                    text (
                        Bokmal to "3,5 ",
                        Nynorsk to "3,5 ",
                        English to "3,5 ",
                    )
                }

                //IF(  PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt(1)  =  FF_Round(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop * 3.3)  ) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt_LIK_3_3_G)){
                    text (
                        Bokmal to "3,3 ",
                        Nynorsk to "3,3 ",
                        English to "3,3 ",
                    )
                }
                text (
                    Bokmal to "ganger folketrygdens grunnbeløp.",
                    Nynorsk to "gonger grunnbeløpet i folketrygda.",
                    English to "times the national insurance basic amount.",
                )
            }
        }
    }
}

