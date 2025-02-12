package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class TBU039V_TBU044V_1(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Dette er trygdetiden din",
                Nynorsk to "Dette er trygdetida di",
                English to "This is your period of national insurance coverage",
            )
        }

        paragraph {
            text(
                Bokmal to "Vi fastsetter trygdetiden din ut fra faktisk trygdetid",
                Nynorsk to "Vi fastset trygdetida di ut frå faktisk trygdetid",
                English to "Your period of national insurance coverage has been established on the basis of your actual ",
            )

            // IF(FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk) <> 0 OR FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTEOS) <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigttnorsk().notEqualTo(0) or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigtteos().notEqualTo(0))) {
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

            // IF(FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk) <> 0 OR FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTEOS) <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigttnorsk().notEqualTo(0) or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigtteos().notEqualTo(0))) {
                text(
                    Bokmal to "Den framtidige trygdetiden er perioden fra uføretidspunktet ditt og fram til og med det året du fyller 66 år.",
                    Nynorsk to "Den framtidige trygdetida er perioden frå uføretidspunktet ditt og fram til og med det året du fyller 66 år.",
                    English to "Your future period of national insurance coverage is the time from the date of your disability up to and including the year you turn 66 years old.",
                )
            }
        }
        // [TBU039V-TBU044V_1]

        paragraph {
            text(
                Bokmal to "Størrelsen på uføretrygden din er avhengig av hvor lenge du har vært medlem av folketrygden. Full trygdetid er 40 år. Dersom trygdetiden er kortere enn 40 år, blir uføretrygden redusert.",
                Nynorsk to "Storleiken på uføretrygda di er avhengig av kor lenge du har vore medlem av folketrygda. Full trygdetid er 40 år. Dersom trygdetida er kortare enn 40 år, blir uføretrygda redusert.",
                English to "The size of your disability benefit is dependent on how long you have had national insurance coverage. The full period of national insurance coverage is 40 years. If your period of national insurance coverage is shorter than 40 years, your disability benefit will be reduced.",
            )
        }
        // IF(  ((FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge)+ FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk))/12) >= 40 AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) <> true AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) <> "oppfylt" OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad) ) THEN    INCLUDE ENDIF
        // manuellt konvertert logikk
        showIf(
            pe.ut_sum_fattnorge_framtidigttnorge_div_12().greaterThanOrEqual(40) and not(pe.grunnlag_persongrunnlagsliste_brukerflyktning()) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().notEqualTo("oppfylt") or pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad())),
        ) {
            paragraph {
                text(
                    Bokmal to "Trygdetiden din er fastsatt til 40 år.",
                    Nynorsk to "Trygdetida di er fastsett til 40 år.",
                    English to "Your period of national insurance coverage has been set to 40 years. ",
                )
            }
        }

        // IF(FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = true) THEN      INCLUDE ENDIF
        showIf(pe.grunnlag_persongrunnlagsliste_brukerflyktning()) {
            // [TBU039V-TBU044V_1]

            paragraph {
                text(
                    Bokmal to "Trygdetiden din er fastsatt til 40 år, fordi du er innvilget flyktningstatus fra Utlendingsdirektoratet. Du beholder uføretrygden beregnet med 40 års trygdetid så lenge du er bosatt i Norge.",
                    Nynorsk to "Trygdetida di er fastsett til 40 år fordi du er innvilga flyktningstatus frå Utlendingsdirektoratet. Du beheld uføretrygda berekna med 40 års trygdetid så lenge du er busett i Noreg.",
                    English to "Your period of national insurance coverage has been set to 40 years, because you have been granted refugee status by the Directorate of Immigration. You will retain a period of national insurance coverage of 40 years for as long as you live in Norway.",
                )
            }
        }

        // IF(((FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge)+ FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk))/12) < 40 AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad  ) THEN      INCLUDE ENDIF
        // manuellt konvertert logikk

        // [TBU039V-TBU044V_1]
        showIf(pe.ut_sum_fattnorge_framtidigttnorge_div_12().lessThan(40) and not(pe.grunnlag_persongrunnlagsliste_brukerflyktning()) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().equalTo("oppfylt") or pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad()))) {
            paragraph {
                text(
                    Bokmal to "Trygdetiden din er fastsatt til 40 år, for den delen du er innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom.",
                    Nynorsk to "Trygdetida di er fastsett til 40 år for den delen du er innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom.",
                    English to "Your period of national insurance coverage has been set to 40 years, for the part you have been granted disability benefit pursuant to special rules relating to occupational injury or occupational illness.",
                )
            }
        }

        // IF(  ((FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge)+ FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk))/12) < 40  AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" ) THEN      INCLUDE ENDIF
        // [TBU039V-TBU044V_1]
        // manuellt konvertert logikk
        showIf(
            pe.ut_sum_fattnorge_framtidigttnorge_div_12().lessThan(40)
                and not(pe.grunnlag_persongrunnlagsliste_brukerflyktning())
                and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad())
                and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().equalTo("oppfylt"),
        ) {
            paragraph {
                textExpr(
                    Bokmal to "Trygdetiden i folketrygden er fastsatt til ".expr() +
                        pe.ut_sum_fattnorge_framtidigttnorge_div_12()
                            .format() + " år for den delen av uførheten din som ikke skyldes en godkjent yrkesskade eller yrkessykdom.",
                    Nynorsk to "Trygdetida i folketrygda er fastsett til ".expr() +
                        pe.ut_sum_fattnorge_framtidigttnorge_div_12()
                            .format() + " år for den delen av uføretrygda di som ikkje skuldas ein godkjend yrkesskade eller yrkessjukdom.",
                    English to "The period of national insurance coverage has been set to ".expr() +
                        pe.ut_sum_fattnorge_framtidigttnorge_div_12()
                            .format() + " years for the part of your disability that is not caused by an approved occupational injury or occupational illness.",
                )

                // IF(FF_GetArrayElement_Date_Boolean(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom) = true) THEN      INCLUDE ENDIF
                showIf(pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().notNull()) {
                    text(
                        Bokmal to " Den faktiske trygdetiden din i denne perioden er fastsatt på grunnlag av følgende perioder:",
                        Nynorsk to " Den faktiske trygdetida di i denne perioden er fastsett på grunnlag av følgjande periodar:",
                        English to " Your actual period of national insurance coverage in this period has been determined on the basis of the following periods of coverage:",
                    )
                }
            }
        }

        // manuellt konvertert logikk
        // IF(FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) <> true AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad) THEN      INCLUDE ENDIF
        showIf((pe.grunnlag_persongrunnlagsliste_brukerflyktning().notEqualTo(true) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().equalTo("oppfylt") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad()))) {
            // [TBU039V-TBU044V_1]

            paragraph {
                text(
                    Bokmal to "Trygdetiden din er fastsatt til 40 år, fordi du er innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom.",
                    Nynorsk to "Trygdetida di er fastsett til 40 år fordi du er innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom.",
                    English to "Your period of national insurance coverage has been set to 40 years, because you have been granted disability benefit pursuant to special rules relating to occupational injury or occupational illness.",
                )
            }
        }

        // manuellt konvertert logikk
        // IF(  (FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge) +  FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk)) / 12 < 40 AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) <> true AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = ""   ) THEN      INCLUDE ENDIF
        // [TBU039V-TBU044V_1]

        showIf(
            pe.ut_sum_fattnorge_framtidigttnorge_div_12().lessThan(40)
                and not(pe.grunnlag_persongrunnlagsliste_brukerflyktning())
                and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().equalTo("oppfylt"),
        ) {
            paragraph {
                textExpr(
                    Bokmal to "Trygdetiden din i folketrygden er fastsatt til ".expr() + pe.ut_sum_fattnorge_framtidigttnorge_div_12().format() + " år. ",
                    Nynorsk to "Trygdetida di i folketrygda er fastsett til ".expr() + pe.ut_sum_fattnorge_framtidigttnorge_div_12().format() + " år.",
                    English to "Your period of national insurance coverage has been set to ".expr() +
                        pe.ut_sum_fattnorge_framtidigttnorge_div_12()
                            .format() + " years.",
                )

                // IF(FF_GetArrayElement_Date_Boolean(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom) = true) THEN      INCLUDE ENDIF
                showIf(pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().notNull()) {
                    text(
                        Bokmal to "Den faktiske trygdetiden din er fastsatt på grunnlag av følgende perioder:",
                        Nynorsk to " Den faktiske trygdetida di er fastsett på grunnlag av følgjande periodar:",
                        English to " Your actual period of national insurance coverage has been determined on the basis of the following periods of coverage:",
                    )
                }
            }
        }

        // manuellt konvertert logikk
        // IF((  (((FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge) +  FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk)) / 12) < 40 AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false )  OR  (PE_Vedtaksdata_Kravhode_BoddArbeidUtland = true AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false  ))  AND  FF_GetArrayElement_Date_Boolean(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom) = true )  THEN      INCLUDE ENDIF
        showIf(pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().notNull() and not(pe.grunnlag_persongrunnlagsliste_brukerflyktning()) and (pe.ut_sum_fattnorge_framtidigttnorge_div_12().lessThan(40) or pe.vedtaksdata_kravhode_boddarbeidutland())) {
            paragraph {
                text(
                    Bokmal to "Trygdetiden din i Norge",
                    Nynorsk to "Trygdetida di i Noreg",
                    English to "Period of national insurance coverage in Norway",
                )
            }
        }
    }
}
