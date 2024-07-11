package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy.functions

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


fun FUNKSJON_PE_UT_Trygdetid(
    PE_pebrevkode: Expression<String>,
    PE_Vedtaksdata_Kravhode_KravArsakType: Expression<String>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid: Expression<Int>,
): Expression<Boolean> =
    PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt") and
            PE_pebrevkode.notEqualTo("PE_UT_04_108") and
            PE_pebrevkode.notEqualTo("PE_UT_04_109") and
            PE_pebrevkode.notEqualTo("PE_UT_07_200") and
            PE_pebrevkode.notEqualTo("PE_UT_06_300") and
            (
                    (PE_pebrevkode.equalTo("PE_UT_04_101") or PE_pebrevkode.equalTo("PE_UT_04_114")) or
                            (PE_pebrevkode.notEqualTo("PE_UT_05_100") and PE_pebrevkode.notEqualTo("PE_UT_07_100")
                                    and PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid.lessThan(40))
                    )

//doInclude = false
//IF(
//PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
//AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt"
//AND PE_pebrevkode <> "PE_UT_07_100"
//AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd"
//AND PE_pebrevkode <> "PE_UT_05_100"
//AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg =  true
//AND PE_pebrevkode <> "PE_UT_04_108"-
//AND PE_pebrevkode <> "PE_UT_04_109"
//AND PE_pebrevkode <> "PE_UT_04_500"
//AND PE_pebrevkode <> "PE_UT_07_200"
//AND PE_pebrevkode <> "PE_UT_06_300"
//) THEN
//-
// doInclude = true
//ENDIF

fun FUNKSJON_PE_UT_Trygdetid_Avdod(
    PE_pebrevkode: Expression<String>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode: Expression<String>,
    PE_Vedtaksdata_Kravhode_KravArsakType: Expression<String>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget: Expression<Boolean>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg: Expression<Boolean>,
) = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget and
        PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt") and
        PE_pebrevkode.notEqualTo("PE_UT_07_100") and
        PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode.equalTo("folketrygd") and
        PE_pebrevkode.notEqualTo("PE_UT_05_100") and
        PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg and
        PE_pebrevkode.notEqualTo("PE_UT_04_108") and
        PE_pebrevkode.notEqualTo("PE_UT_04_109") and
        PE_pebrevkode.notEqualTo("PE_UT_04_500") and
        PE_pebrevkode.notEqualTo("PE_UT_07_200") and
        PE_pebrevkode.notEqualTo("PE_UT_06_300")


// Sjekker om localDate eksisterer. Original:
// 	IF IsDate(Array_Variabel(Array_Element)) THEN
//		Value = true
//	ELSE
//		Value = false
//	ENDIF
//ELSE Value = false
//ENDIFÿÿÿc4WðBÿÿÿz4WðBÿÿÿ4WðB4ÿÿÿÒ2>ô2Æä@T110988pHËø2Æä@T110988WðBÿÿÿSer om det eksisterer en datoüÿÿÈApprove HL3-2016T133804Íä@T133804Ü£Íä@üÿÿüÿÿüÿÿWðBh4FF_GetArrayElement_Date_BooleanfghWðBgÈeArray_Variabel
fun FUNKSJON_FF_GetArrayElement_Date_Boolean(localDate: Expression<LocalDate?>) = localDate.notNull()





// Boolean doInclude
//
//doInclude = false
//IF(
//(PE_pebrevkode = "PE_UT_04_102"
//OR
//PE_pebrevkode = "PE_UT_04_116"
//OR
//PE_pebrevkode = "PE_UT_04_101"
//OR
//PE_pebrevkode = "PE_UT_04_114"
//OR
//PE_pebrevkode = "PE_UT_04_300"
//OR
//PE_pebrevkode = "PE_UT_14_300"
//OR
// PE_pebrevkode = "PE_UT_04_500"
// OR
//(PE_Vedtaksdata_Kravhode_KravArsakType = "endret_inntekt"
//   AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT
//   ))
// AND
//   PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt"
// AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak
//) THEN
//     doInclude = true
//ENDIF
//
//value = doInclude

fun FUNKSJON_PE_UT_TBU056V(
    PE_pebrevkode: Expression<String>,
    PE_Vedtaksdata_Kravhode_KravArsakType: Expression<String>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT: Expression<Kroner>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT: Expression<Kroner>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense: Expression<Double>,
    PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak: Expression<Double>,
) =(
        PE_pebrevkode.equalTo("PE_UT_04_102")
        or PE_pebrevkode.equalTo("PE_UT_04_116")
        or PE_pebrevkode.equalTo("PE_UT_04_101")
        or PE_pebrevkode.equalTo("PE_UT_04_114")
        or PE_pebrevkode.equalTo("PE_UT_04_300")
        or PE_pebrevkode.equalTo("PE_UT_14_300")
        or PE_pebrevkode.equalTo("PE_UT_04_500")
        or (PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endret_inntekt")
                and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT)
                )
    ) and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt") and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak)
