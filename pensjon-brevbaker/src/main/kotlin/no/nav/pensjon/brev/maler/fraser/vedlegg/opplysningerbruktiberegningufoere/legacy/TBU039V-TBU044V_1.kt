package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy

import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy.functions.FUNKSJON_FF_GetArrayElement_Date_Boolean
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

data class TBU039V_TBU044V_1(
    val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk: Expression<Int>,
    val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTEOS: Expression<Int>,
    val PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12: Expression<Int>,
    val PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning: Expression<Boolean>,
    val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom: Expression<LocalDate?>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad: Expression<Int>,
    val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat: Expression<String>,
    val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad: Expression<Int>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //[TBU039V-TBU044V_1]

        paragraph {
            text(
                Bokmal to "Dette er trygdetiden din",
                Nynorsk to "Dette er trygdetida di",
                English to "This is your period of national insurance coverage",
            )
        }
        //[TBU039V-TBU044V_1]

        paragraph {
            text(
                Bokmal to "Vi fastsetter trygdetiden din ut fra faktisk trygdetid",
                Nynorsk to "Vi fastset trygdetida di ut frå faktisk trygdetid",
                English to "Your period of national insurance coverage has been established on the basis of your actual ",
            )

            //IF(FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk) <> 0 OR FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTEOS) <> 0) THEN      INCLUDE ENDIF
            showIf(
                (PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk.notEqualTo(0) or PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTEOS.notEqualTo(
                    0
                ))
            ) {
                text(
                    Bokmal to " og framtidig trygdetid",
                    Nynorsk to " og framtidig trygdetid",
                    English to "and future ",
                )
            }
            text(
                Bokmal to ". Den faktiske trygdetiden din er perioder du har vært medlem av folketrygden fra fylte 16 år og fram til uføretidspunktet. ",
                Nynorsk to ". Den faktiske trygdetida di er periodar du har vore medlem av folketrygda frå fylte 16 år og fram til uføretidspunktet. ",
                English to "periods of national insurance coverage. Your actual period of national insurance coverage is the period of coverage by the National Insurance scheme, from the time you turned 16 years old to the date of your disability. ",
            )

            //IF(FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk) <> 0 OR FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTEOS) <> 0) THEN      INCLUDE ENDIF
            showIf(
                (PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk.notEqualTo(0) or PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTEOS.notEqualTo(
                    0
                ))
            ) {
                text(
                    Bokmal to "Den framtidige trygdetiden er perioden fra uføretidspunktet ditt og fram til og med det året du fyller 66 år.",
                    Nynorsk to "Den framtidige trygdetida er perioden frå uføretidspunktet ditt og fram til og med det året du fyller 66 år.",
                    English to "Your future period of national insurance coverage is the time from the date of your disability up to and including the year you turn 66 years old.",
                )
            }
        }
        //[TBU039V-TBU044V_1]

        paragraph {
            text(
                Bokmal to "Størrelsen på uføretrygden din er avhengig av hvor lenge du har vært medlem av folketrygden. Full trygdetid er 40 år. Dersom trygdetiden er kortere enn 40 år, blir uføretrygden redusert.",
                Nynorsk to "Storleiken på uføretrygda di er avhengig av kor lenge du har vore medlem av folketrygda. Full trygdetid er 40 år. Dersom trygdetida er kortare enn 40 år, blir uføretrygda redusert.",
                English to "The size of your disability benefit is dependent on how long you have had national insurance coverage. The full period of national insurance coverage is 40 years. If your period of national insurance coverage is shorter than 40 years, your disability benefit will be reduced.",
            )
        }
        //Failed to convert with error: Unexpected character: + at line 3 : FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk))/12) >= 40

        //IF(  ((FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge)+ FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk))/12) >= 40
        // AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) <> true
        // AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) <> "oppfylt" OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad) ) THEN    INCLUDE ENDIF

        //[TBU039V-TBU044V_1]
        // Manuelt konvertert
        showIf(
            PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12.greaterThanOrEqual(40)
                    and not(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning)
                    and (PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat.notEqualTo("oppfylt")
                    or PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad.lessThan(
                PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad
            )
                    )
        ) {

        }
        paragraph {
            text(
                Bokmal to "Trygdetiden din er fastsatt til 40 år.",
                Nynorsk to "Trygdetida di er fastsett til 40 år.",
                English to "Your period of national insurance coverage has been set to 40 years. ",
            )
        }

        //IF(FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = true) THEN      INCLUDE ENDIF
        showIf(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) {
            //[TBU039V-TBU044V_1]

            paragraph {
                text(
                    Bokmal to "Trygdetiden din er fastsatt til 40 år, fordi du er innvilget flyktningstatus fra Utlendingsdirektoratet. Du beholder uføretrygden beregnet med 40 års trygdetid så lenge du er bosatt i Norge.",
                    Nynorsk to "Trygdetida di er fastsett til 40 år fordi du er innvilga flyktningstatus frå Utlendingsdirektoratet. Du beheld uføretrygda berekna med 40 års trygdetid så lenge du er busett i Noreg.",
                    English to "Your period of national insurance coverage has been set to 40 years, because you have been granted refugee status by the Directorate of Immigration. You will retain a period of national insurance coverage of 40 years for as long as you live in Norway.",
                )
            }
        }
        //Failed to convert with error: Unexpected character: + at line 3 : FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk))/12) < 40

        //IF(
        // ((FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge)+ FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk))/12) < 40
        // AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad  ) THEN      INCLUDE ENDIF

        //[TBU039V-TBU044V_1]

        paragraph {
            text(
                Bokmal to "Trygdetiden din er fastsatt til 40 år, for den delen du er innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom.",
                Nynorsk to "Trygdetida di er fastsett til 40 år for den delen du er innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom.",
                English to "Your period of national insurance coverage has been set to 40 years, for the part you have been granted disability benefit pursuant to special rules relating to occupational injury or occupational illness.",
            )
        }
        //Failed to convert with error: Unexpected character: + at line 3 : FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk))/12) < 40

        //IF(  ((FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge)+ FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk))/12) < 40  AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" ) THEN      INCLUDE ENDIF

        //[TBU039V-TBU044V_1]

        paragraph {
            textExpr(
                Bokmal to "Trygdetiden i folketrygden er fastsatt til ".expr() + PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12.format() + " år for den delen av uførheten din som ikke skyldes en godkjent yrkesskade eller yrkessykdom.",
                Nynorsk to "Trygdetida i folketrygda er fastsett til ".expr() + PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12.format() + " år for den delen av uføretrygda di som ikkje skuldas ein godkjend yrkesskade eller yrkessjukdom.",
                English to "The period of national insurance coverage has been set to ".expr() + PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12.format() + " years for the part of your disability that is not caused by an approved occupational injury or occupational illness.",
            )

            //IF(FF_GetArrayElement_Date_Boolean(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom) = true) THEN      INCLUDE ENDIF
            showIf(
                (FUNKSJON_FF_GetArrayElement_Date_Boolean(
                    PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom
                ))
            ) {
                text(
                    Bokmal to " Den faktiske trygdetiden din i denne perioden er fastsatt på grunnlag av følgende perioder:",
                    Nynorsk to " Den faktiske trygdetida di i denne perioden er fastsett på grunnlag av følgjande periodar:",
                    English to " Your actual period of national insurance coverage in this period has been determined on the basis of the following periods of coverage:",
                )
            }
        }

        //IF(FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) <> true AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad) THEN      INCLUDE ENDIF
        showIf(
            (PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning.notEqualTo(true) and PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat.equalTo(
                "oppfylt"
            ) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad.equalTo(
                PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad
            ))
        ) {
            //[TBU039V-TBU044V_1]

            paragraph {
                text(
                    Bokmal to "Trygdetiden din er fastsatt til 40 år, fordi du er innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom.",
                    Nynorsk to "Trygdetida di er fastsett til 40 år fordi du er innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom.",
                    English to "Your period of national insurance coverage has been set to 40 years, because you have been granted disability benefit pursuant to special rules relating to occupational injury or occupational illness.",
                )
            }
        }
        //Failed to convert with error: Unexpected character: + at line 3 : FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk)) / 12 < 40

        //IF(  (FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge) +  FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk)) / 12 < 40 AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) <> true AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = ""   ) THEN      INCLUDE ENDIF

        //[TBU039V-TBU044V_1]

        paragraph {
            textExpr(
                Bokmal to "Trygdetiden din i folketrygden er fastsatt til ".expr() + PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12.format() + " år. ",
                Nynorsk to "Trygdetida di i folketrygda er fastsett til ".expr() + PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12.format() + " år.",
                English to "Your period of national insurance coverage has been set to ".expr() + PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12.format() + " years.",
            )

            //IF(FF_GetArrayElement_Date_Boolean(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom) = true) THEN      INCLUDE ENDIF
            showIf(
                (FUNKSJON_FF_GetArrayElement_Date_Boolean(
                    PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom
                ))
            ) {
                text(
                    Bokmal to "Den faktiske trygdetiden din er fastsatt på grunnlag av følgende perioder:",
                    Nynorsk to " Den faktiske trygdetida di er fastsett på grunnlag av følgjande periodar:",
                    English to " Your actual period of national insurance coverage has been determined on the basis of the following periods of coverage:",
                )
            }
        }
        //Failed to convert with error: Unexpected character: + at line 3 : FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk)) / 12) < 40

        //IF((  (((FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge) +  FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk)) / 12) < 40 AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false )  OR  (PE_Vedtaksdata_Kravhode_BoddArbeidUtland = true AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false  ))  AND  FF_GetArrayElement_Date_Boolean(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom) = true )  THEN      INCLUDE ENDIF

        //[TBU039V-TBU044V_1]

        paragraph {
            text(
                Bokmal to "Trygdetiden din i Norge",
                Nynorsk to "Trygdetida di i Noreg",
                English to "Period of national insurance coverage in Norway",
            )
        }
    }
}