package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

/*
data class TBU037V(
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom: Expression<LocalDate>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense: Expression<Kroner>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Ar: Expression<Int>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Brukt: Expression<Boolean>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste: Expression<Boolean>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_JustertBelop: Expression<Double>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar: Expression<Boolean>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Pgi: Expression<Double>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode: Expression<String>,
    val PE_Vedtaksdata_Kravhode_KravArsakType: Expression<String>,
    val PE_pebrevkode: Expression<String>,
): OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd"  AND PE_pebrevkode <> "PE_UT_05_100"  AND PE_pebrevkode <> "PE_UT_14_300"  AND PE_pebrevkode <> "PE_UT_07_100"  AND PE_pebrevkode <> "PE_UT_04_300"  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt"  AND PE_pebrevkode <> "PE_UT_04_108"  AND PE_pebrevkode <> "PE_UT_04_109"  AND PE_pebrevkode <> "PE_UT_07_200"   AND PE_pebrevkode <> "PE_UT_06_300" AND PE_pebrevkode <> "PE_UT_04_500" AND (PE_pebrevkode <> "PE_UT_04_102"      OR (PE_pebrevkode = "PE_UT_04_102"     AND PE_Vedtaksdata_Kravhode_KravArsakType <> "tilst_dod"))) THEN      INCLUDE ENDIF
        showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode.equalTo("folketrygd") and PE_pebrevkode.notEqualTo("PE_UT_05_100") and PE_pebrevkode.notEqualTo("PE_UT_14_300") and PE_pebrevkode.notEqualTo("PE_UT_07_100") and PE_pebrevkode.notEqualTo("PE_UT_04_300") and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense.notEqualTo(60000) and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt") and PE_pebrevkode.notEqualTo("PE_UT_04_108") and PE_pebrevkode.notEqualTo("PE_UT_04_109") and PE_pebrevkode.notEqualTo("PE_UT_07_200") and PE_pebrevkode.notEqualTo("PE_UT_06_300") and PE_pebrevkode.notEqualTo("PE_UT_04_500") and (PE_pebrevkode.notEqualTo("PE_UT_04_102") or (PE_pebrevkode.equalTo("PE_UT_04_102") and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("tilst_dod"))))){
            //[TBU037V_1]

            paragraph {
                textExpr (
                    Bokmal to "Inntekt lagt til grunn for beregning av uføretrygden din fra ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom.format(),
                    Nynorsk to "Inntekt lagd til grunn for berekning av uføretrygda di frå ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom.format(),
                    English to "Income on which to calculate your disability benefit of ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom.format(),
                )
            }
            //[TBU037V_1]

            paragraph {
                text (
                    Bokmal to "År",
                    Nynorsk to "År",
                    English to "Year",
                )
                text (
                    Bokmal to "Pensjonsgivende inntekt",
                    Nynorsk to "Pensjonsgivande inntekt",
                    English to "Pensionable income",
                )
                text (
                    Bokmal to "Inntekt justert med folketrygdens grunnbeløp",
                    Nynorsk to "Inntekt justert med grunnbeløpet i folketrygda",
                    English to "Income adjusted in accordance with the National Insurance basic amount",
                )
                text (
                    Bokmal to "Merknad",
                    Nynorsk to "Merknad",
                    English to "Comments",
                )
            }
            //[TBU037V_1]

            paragraph {

                //IF(FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Brukt,SYS_TableRow) = false) THEN      INCLUDE ENDIF
                showIf((not(FUNKSJON_FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Brukt,SYS_TableRow)))){
                    textExpr (
                        Bokmal to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Ar.format(),
                        Nynorsk to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Ar.format(),
                        English to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Ar.format(),
                    )
                }

                //IF(FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Brukt,SYS_TableRow) = true) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Brukt,SYS_TableRow))){
                    textExpr (
                        Bokmal to "".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Ar.format(),
                        Nynorsk to "".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Ar.format(),
                        English to "".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Ar.format(),
                    )
                }
                textExpr (
                    Bokmal to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Pgi.format() + " kr",
                    Nynorsk to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Pgi.format() + " kr",
                    English to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Pgi.format() + " NOK",
                )

                //IF(FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Brukt,SYS_TableRow) = false) THEN      INCLUDE ENDIF
                showIf((not(FUNKSJON_FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Brukt,SYS_TableRow)))){
                    textExpr (
                        Bokmal to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_JustertBelop.format() + " kr",
                        Nynorsk to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_JustertBelop.format() + " kr",
                        English to PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_JustertBelop.format() + " NOK",
                    )
                }

                //IF(FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Brukt,SYS_TableRow) = true) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Brukt,SYS_TableRow))){
                    textExpr (
                        Bokmal to "".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_JustertBelop.format() + " kr",
                        Nynorsk to "".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_JustertBelop.format() + " kr",
                        English to "".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_JustertBelop.format() + " NOK",
                    )
                }

                //IF(FF_GetArrayElement_Float(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste,SYS_TableRow) <> 0) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_FF_GetArrayElement_Float(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste,SYS_TableRow).notEqualTo(0))){
                    text (
                        Bokmal to "Førstegangsteneste * ",
                        Nynorsk to "Førstegongsteneste * ",
                        English to "Initial service * ",
                    )
                }

                //IF(FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar,SYS_TableRow) = true) THEN      INCLUDE ENDIF
                showIf((FUNKSJON_FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar,SYS_TableRow))){
                    text (
                        Bokmal to "Omsorgsår *",
                        Nynorsk to "Omsorgsår *",
                        English to "Care Work *",
                    )
                }
            }
        }
    }
}

 */