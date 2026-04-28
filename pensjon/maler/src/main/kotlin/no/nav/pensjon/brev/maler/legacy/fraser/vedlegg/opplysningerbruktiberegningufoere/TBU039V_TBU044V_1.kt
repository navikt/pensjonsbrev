package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class TBU039V_TBU044V_1(
    val pe: Expression<PEgruppe10>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                bokmal { + "Dette er trygdetiden din" },
                nynorsk { + "Dette er trygdetida di" },
            )
        }

        paragraph {
            text(
                bokmal { + "Vi fastsetter trygdetiden din ut fra faktisk trygdetid" },
                nynorsk { + "Vi fastset trygdetida di ut frå faktisk trygdetid" },
            )

            //IF(FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk) <> 0 OR FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTEOS) <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigttnorsk().notEqualTo(0) or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigtteos().notEqualTo(0))) {
                text(
                    bokmal { + " og framtidig trygdetid" },
                    nynorsk { + " og framtidig trygdetid" },
                )
            }
            text(
                bokmal { + ". Den faktiske trygdetiden din er perioder du har vært medlem av folketrygden fra fylte 16 år og fram til uføretidspunktet. " },
                nynorsk { + ". Den faktiske trygdetida di er periodar du har vore medlem av folketrygda frå fylte 16 år og fram til uføretidspunktet. " },
            )

            //IF(FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk) <> 0 OR FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTEOS) <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigttnorsk().notEqualTo(0) or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_framtidigtteos().notEqualTo(0))) {
                text(
                    bokmal { + "Den framtidige trygdetiden er perioden fra uføretidspunktet ditt og fram til og med det året du fyller 66 år." },
                    nynorsk { + "Den framtidige trygdetida er perioden frå uføretidspunktet ditt og fram til og med det året du fyller 66 år." },
                )
            }
        }
        //[TBU039V-TBU044V_1]

        paragraph {
            text(
                bokmal { + "Størrelsen på uføretrygden din er avhengig av hvor lenge du har vært medlem av folketrygden. Full trygdetid er 40 år. Dersom trygdetiden er kortere enn 40 år, blir uføretrygden redusert." },
                nynorsk { + "Storleiken på uføretrygda di er avhengig av kor lenge du har vore medlem av folketrygda. Full trygdetid er 40 år. Dersom trygdetida er kortare enn 40 år, blir uføretrygda redusert." },
            )
        }
        //IF(  ((FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge)+ FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk))/12) >= 40 AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) <> true AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) <> "oppfylt" OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad) ) THEN    INCLUDE ENDIF
        // manuellt konvertert logikk
        showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().greaterThanOrEqual(40)
                and not(pe.grunnlag_persongrunnlagsliste_brukerflyktning())
                and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().notEqualTo("oppfylt") or pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad()))
        ) {
            paragraph {
                text(
                    bokmal { + "Trygdetiden din er fastsatt til 40 år." },
                    nynorsk { + "Trygdetida di er fastsett til 40 år." },
                )
            }
        }

        //IF(FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = true) THEN      INCLUDE ENDIF
        showIf(pe.grunnlag_persongrunnlagsliste_brukerflyktning()) {
            //[TBU039V-TBU044V_1]

            paragraph {
                text(
                    bokmal { + "Trygdetiden din er fastsatt til 40 år, fordi du er innvilget flyktningstatus fra Utlendingsdirektoratet. Du beholder uføretrygden beregnet med 40 års trygdetid så lenge du er bosatt i Norge." },
                    nynorsk { + "Trygdetida di er fastsett til 40 år fordi du er innvilga flyktningstatus frå Utlendingsdirektoratet. Du beheld uføretrygda berekna med 40 års trygdetid så lenge du er busett i Noreg." },
                )
            }
        }

        //IF(((FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge)+ FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk))/12) < 40 AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad  ) THEN      INCLUDE ENDIF
        // manuellt konvertert logikk

        //[TBU039V-TBU044V_1]
        showIf(
            pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().lessThan(40)
                    and not(pe.grunnlag_persongrunnlagsliste_brukerflyktning())
                    and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().equalTo("oppfylt")
                    and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().notEqualTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad())) {
            paragraph {
                text(
                    bokmal { + "Trygdetiden din er fastsatt til 40 år, for den delen du er innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom." },
                    nynorsk { + "Trygdetida di er fastsett til 40 år for den delen du er innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Trygdetiden i folketrygden er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid()
                        .format() + " år for den delen av uførheten din som ikke skyldes en godkjent yrkesskade eller yrkessykdom." },

                    nynorsk { + "Trygdetida i folketrygda er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid()
                        .format() + " år for den delen av uføretrygda di som ikkje skuldas ein godkjend yrkesskade eller yrkessjukdom." },
                )

                //IF(FF_GetArrayElement_Date_Boolean(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom) = true) THEN      INCLUDE ENDIF
                showIf(pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().notNull()) {
                    text(
                        bokmal { + " Den faktiske trygdetiden din i denne perioden er fastsatt på grunnlag av følgende perioder:" },
                        nynorsk { + " Den faktiske trygdetida di i denne perioden er fastsett på grunnlag av følgjande periodar:" },
                    )
                }
            }
        }

        // manuellt konvertert logikk
        //IF(FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) <> true AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad) THEN      INCLUDE ENDIF
        showIf((pe.grunnlag_persongrunnlagsliste_brukerflyktning().notEqualTo(true) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().equalTo("oppfylt") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().equalTo(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad()))) {
            //[TBU039V-TBU044V_1]

            paragraph {
                text(
                    bokmal { + "Trygdetiden din er fastsatt til 40 år, fordi du er innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom." },
                    nynorsk { + "Trygdetida di er fastsett til 40 år fordi du er innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom." },
                )
            }
        }

        // manuellt konvertert logikk
        //IF(  (FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge) +  FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk)) / 12 < 40 AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) <> true AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = ""   ) THEN      INCLUDE ENDIF
        //[TBU039V-TBU044V_1]

        showIf(
            pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().lessThan(40)
            and not(pe.grunnlag_persongrunnlagsliste_brukerflyktning())
            and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().notEqualTo("oppfylt")
        ){
            paragraph {
                text(
                    bokmal { + "Trygdetiden din i folketrygden er fastsatt til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år. " },
                    nynorsk { + "Trygdetida di i folketrygda er fastsett til " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().format() + " år." },
                )

                //IF(FF_GetArrayElement_Date_Boolean(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom) = true) THEN      INCLUDE ENDIF
                showIf(pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().notNull()) {
                    text(
                        bokmal { + "Den faktiske trygdetiden din er fastsatt på grunnlag av følgende perioder:" },
                        nynorsk { + " Den faktiske trygdetida di er fastsett på grunnlag av følgjande periodar:" },
                    )
                }
            }
        }

        // manuellt konvertert logikk
        //IF((  (((FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge) +  FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk)) / 12) < 40 AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false )  OR  (PE_Vedtaksdata_Kravhode_BoddArbeidUtland = true AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false  ))  AND  FF_GetArrayElement_Date_Boolean(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom) = true )  THEN      INCLUDE ENDIF
        showIf(
            pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().notNull()
                and not(pe.grunnlag_persongrunnlagsliste_brukerflyktning())
                    and (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().lessThan(40) or pe.vedtaksdata_kravhode_boddarbeidutland())){
            paragraph {
                text(
                    bokmal { + "Trygdetiden din i Norge" },
                    nynorsk { + "Trygdetida di i Noreg" },
                )
            }
        }
    }
}