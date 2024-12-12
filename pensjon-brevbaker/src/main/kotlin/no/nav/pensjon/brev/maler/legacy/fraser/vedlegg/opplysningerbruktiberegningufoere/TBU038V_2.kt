package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.harOpptjeningUTMedFoerstegangstjenesteOgIkkeOmsorg
import no.nav.pensjon.brev.maler.legacy.harOpptjeningUTMedFoerstegangstjenesteOgOmsorg
import no.nav.pensjon.brev.maler.legacy.harOpptjeningUTMedOmsorgOgIkkeFoerstegangstjeneste
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text

data class TBU038V_2(
    val pe: Expression<PE>
): OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        {

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd"  AND PE_pebrevkode <> "PE_UT_05_100"  AND PE_pebrevkode <> "PE_UT_14_300"  AND PE_pebrevkode <> "PE_UT_07_100"  AND PE_pebrevkode <> "PE_UT_04_300"  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt"  AND PE_pebrevkode <> "PE_UT_04_108"  AND PE_pebrevkode <> "PE_UT_04_109"  AND PE_pebrevkode <> "PE_UT_07_200"   AND PE_pebrevkode <> "PE_UT_06_300" AND PE_pebrevkode <> "PE_UT_04_500" AND (PE_pebrevkode <> "PE_UT_04_102"      OR (PE_pebrevkode = "PE_UT_04_102"     AND PE_Vedtaksdata_Kravhode_KravArsakType <> "tilst_dod"))) THEN      INCLUDE ENDIF
			showIf((pe.pebrevkode().notEqualTo("PE_UT_07_100") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().notEqualTo("folketrygd") and pe.pebrevkode().notEqualTo("PE_UT_05_100") and pe.pebrevkode().notEqualTo("PE_UT_14_300") and pe.pebrevkode().notEqualTo("PE_UT_04_300") and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(60000) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode().notEqualTo("PE_UT_06_300") and pe.pebrevkode().notEqualTo("PE_UT_04_500") and (pe.pebrevkode().notEqualTo("PE_UT_04_102") or (pe.pebrevkode().equalTo("PE_UT_04_102") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod"))))){
                paragraph {
                    text (
                        Bokmal to "Inntektene som er uthevet er valgt siden dette gir det beste resultatet for deg.",
                        Nynorsk to "Inntektene som er utheva, er valde sidan dette gir det beste resultatet for deg.",
                        English to "The emphasized income has been selected, because this will yield a higher payout for you.",
                    )
                }

                //Integer i i = 1
                // FOR i = 1 TO Count(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar)
                //      IF( FF_GetArrayElement_Float(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste,i) <> 0  AND FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar,i) = true ) THEN      INCLUDE ENDIF
                //      NEXT

                showIf(pe.harOpptjeningUTMedFoerstegangstjenesteOgOmsorg()) {
                    paragraph {
                        text(
                            Bokmal to "*) Markerer år med omsorgsopptjening og militær eller sivil førstegangstjeneste. Det skal ses bort fra år med pensjonsopptjening på grunnlag av omsorgsarbeid dersom dette er en fordel. Dersom inntekten i året før militær eller sivil førstegangstjeneste tok til er høyere, benyttes denne inntekten.",
                            Nynorsk to "*) Markerer år med omsorgsopptening og militær eller sivil førstegongsteneste. Ein skal sjå bort frå år med pensjonsopptening på grunnlag av omsorgsarbeid dersom dette er ein fordel. Dersom inntekta i året før militær eller sivil førstegongsteneste tok til, er høgare, blir denne inntekta brukt.",
                            English to "*) Indicates a year when you earned pension points for care work or initial service, either military or civilian. If you stand to benefit from excluding years when you have earned pension points from care work, these years will be excluded. If the income in the year before your military or civilian initial service started is higher, this income will be used as the basis for calculation.",
                        )
                    }
                }
                //Integer i i = 1
                // FOR i = 1 TO Count(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar)
                //      IF(  FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar,i) = true AND FF_GetArrayElement_Float(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste,i) = 0  ) THEN      INCLUDE ENDIF NEXT


                showIf(pe.harOpptjeningUTMedOmsorgOgIkkeFoerstegangstjeneste()) {
                    paragraph {
                        text(
                            Bokmal to "*) Markerer år med omsorgsopptjening. Det skal ses bort fra år med pensjonsopptjening på grunnlag av omsorgsarbeid dersom dette er en fordel.",
                            Nynorsk to "*) Markerer år med omsorgsopptening. Ein skal sjå bort frå år med pensjonsopptening på grunnlag av omsorgsarbeid dersom dette er ein fordel.",
                            English to "*) Indicates a year when you earned pension points for care work. If you stand to benefit from excluding years when you have earned pension points from care work, these years will be excluded.",
                        )
                    }
                }
                //Integer i i = 1 FOR i = 1 TO Count(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste)  IF(  FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar,i) = false AND FF_GetArrayElement_Float(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste,i) <> 0    ) THEN      INCLUDE ENDIF NEXT
                showIf(pe.harOpptjeningUTMedFoerstegangstjenesteOgIkkeOmsorg()) {
                    paragraph {
                        text(
                            Bokmal to "*) Markerer år med militær eller sivil førstegangstjeneste. Dersom inntekten i året før tjenesten tok til er høyere, benyttes denne inntekten.",
                            Nynorsk to "*) Markerer år med militær eller sivil førstegongsteneste. Dersom inntekta i året før tenesta tok til, er høgare, blir denne inntekta brukt.",
                            English to "*)Indicates a year when you earned pension points for military or civilian initial service. If the income in the year before your service started is higher, this income will be used as a basis for calculation.",
                        )
                    }
                }
            }
            paragraph {
                text (
                    Bokmal to "**) Inntekten er justert etter endringer i folketrygdens grunnbeløp.",
                    Nynorsk to "**) Gjennomsnittleg norsk inntekt justert etter endringar i grunnbeløpet i folketrygda.",
                    English to "**) Average Norwegian income adjusted in accordance with changes in the National Insurance basic amount.",
                )
            }
        }
    }

}
