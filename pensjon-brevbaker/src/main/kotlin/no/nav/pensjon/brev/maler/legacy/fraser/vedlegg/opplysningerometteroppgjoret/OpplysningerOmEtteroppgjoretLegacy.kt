package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerometteroppgjoret

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.Inntektsgrunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.InntektsgrunnlagSelectors.belop_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.InntektsgrunnlagSelectors.grunnikkereduksjon_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.InntektsgrunnlagSelectors.inntekttype_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.InntektsgrunnlagSelectors.registerkilde_safe
import no.nav.pensjon.brev.maler.legacy.ut_inntekt_trukket_fra_personinntekt
import no.nav.pensjon.brev.maler.legacy.ut_sum_inntekterbt_totalbeloput
import no.nav.pensjon.brev.maler.legacy.ut_uforetrygdetteroppgjor_periodefom_year
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_inntektliste_inntektsgrunnlag
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkbt
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_fratrekkliste_inntektsgrunnlag
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_inntektliste_inntektsgrunnlag
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_suminntekterbt
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptfb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptsb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloput
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptfb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptsb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.TextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNotEmpty
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner

object OpplysningerOmEtteroppgjoretLegacy {



//PE_UT_Etteroppgjor_DetaljEPS_InntektListe_InntektTypeKode
/*String returnVal

IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_arb') THEN
   IF UCase(PE_XML_brev_spraak) ='NN'  THEN
     returnVal =  'Arbeidsinntekt'
   ELSE
	 returnVal =  'Arbeidsinntekt'
   END IF

ELSEIF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl') THEN
   IF UCase(PE_XML_brev_spraak) ='NN'  THEN
     returnVal = 'Utenlandsinntekt'
   ELSE
	 returnVal = 'Utlandsinntekt'
   END IF

ELSEIF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar') THEN
   IF UCase(PE_XML_brev_spraak) ='NN'  THEN
     returnVal = 'Næringsinntekt'
   ELSE
	 returnVal = 'Næringsinntekt'
   END IF

ELSEIF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_and') THEN
   IF UCase(PE_XML_brev_spraak) ='NN'  THEN
     returnVal = 'Pensjon frå andre enn NAV'
   ELSE
	 returnVal = 'Pensjoner fra andre enn NAV'
   END IF

ELSEIF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forpenutl') THEN
   IF UCase(PE_XML_brev_spraak) ='NN'  THEN
     returnVal = 'Pensjon frå utlandet'
   ELSE
	 returnVal = 'Pensjon fra utlandet'
   END IF

ELSEIF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'ikke_red') THEN
   IF UCase(PE_XML_brev_spraak) ='NN'  THEN
     returnVal = 'Inntekt som kan bli trekt frå'
   ELSE
	 returnVal = 'Inntekt som kan trekkes fra'
   END IF

ENDIF

value =returnVal*/
    data class PE_UT_Etteroppgjor_DetaljEPS_InntektListe_InntektTypeKode(
        val inntektType: Expression<String?>
    ): TextOnlyPhrase<LangBokmalNynorsk>(){
    override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(inntektType.equalTo("rap_arb")) {
            text(
                Bokmal to "Arbeidsinntekt",
                Nynorsk to "Arbeidsinntekt",
            )
        }.orShowIf(inntektType.equalTo("forintutl")) {
            text(
                Bokmal to "Utlandsinntekt",
                Nynorsk to "Utenlandsinntekt",
            )
        }.orShowIf(inntektType.equalTo("rap_nar")) {
            text(
                Bokmal to "Næringsinntekt",
                Nynorsk to "Næringsinntekt",
            )
        }.orShowIf(inntektType.equalTo("rap_and")) {
            text(
                Bokmal to "Pensjoner fra andre enn NAV",
                Nynorsk to "Pensjon frå andre enn NAV",
            )
        }.orShowIf(inntektType.equalTo("forpenutl")) {
            text(
                Bokmal to "Pensjon fra utlandet",
                Nynorsk to "Pensjon frå utlandet",
            )
        }.orShowIf(inntektType.equalTo("ikke_red")) {
            text(
                Bokmal to "Inntekt som kan trekkes fra",
                Nynorsk to "Inntekt som kan bli trekt frå",
            )
        }
    }
}


//PE_UT_Etteroppgjor_DetaljEPS_InntektListe_RegisterKildeKode
//String returnVal
//
//IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_RegisterKilde(SYS_TableRow) = 'a_ordning') THEN
//   IF UCase(PE_XML_brev_spraak) ='NN'  THEN
//     returnVal = 'Elektronisk innmeld frå arbeidsgivar'
//   ELSE
//	 returnVal = 'Elektronisk innmeldt fra arbeidsgiver'
//   END IF
//
//ELSEIF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_RegisterKilde(SYS_TableRow) = 'skd') THEN
//   IF UCase(PE_XML_brev_spraak) ='NN'  THEN
//     returnVal = 'Opplyst av Skatteetaten'
//   ELSE
//	 returnVal = 'Oppgitt av Skatteetaten'
//   END IF
//
//ELSE
//   IF UCase(PE_XML_brev_spraak) ='NN'  THEN
//     returnVal = 'Opplyst av deg'
//   ELSE
//	 returnVal = 'Oppgitt av deg'
//   END IF
//
//ENDIF
//
//value =returnVal

    data class PE_UT_Etteroppgjor_DetaljEPS_InntektListe_RegisterKildeKode(
        val registerkilde: Expression<String?>,
    ): TextOnlyPhrase<LangBokmalNynorsk>(){
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(registerkilde.equalTo("a_ordning")){
                text(
                    Bokmal to "Elektronisk innmeldt fra arbeidsgiver",
                    Nynorsk to "Elektronisk innmeld frå arbeidsgivar",
                )
            }.orShowIf(registerkilde.equalTo("skd")){
                text(
                    Bokmal to "Oppgitt av Skatteetaten",
                    Nynorsk to "Opplyst av Skatteetaten",
                )
            }.orShow {
                text(
                    Bokmal to "Oppgitt av deg",
                    Nynorsk to "Opplyst av deg",
                )
            }
        }

    }
    data class PE_UT_Etteroppgjor_DetaljBruker_FratrekkListe_InntektTypeKode(
        val inntektsgrunnlag: Expression<Inntektsgrunnlag>,
    ): TextOnlyPhrase<LangBokmalNynorsk>() {
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(inntektsgrunnlag.inntekttype_safe.equalTo("rap_arb")){
                text(
                    Bokmal to "Arbeidsinntekt",
                    Nynorsk to "Arbeidsinntekt",
                )
            }.orShowIf(inntektsgrunnlag.inntekttype_safe.equalTo("forintutl")){
                text(
                    Bokmal to "Utlandsinntekt",
                    Nynorsk to "Utanlandsinntekt",
                )
            }.orShowIf(inntektsgrunnlag.inntekttype_safe.equalTo("rap_nar")){
                text(
                    Bokmal to "Næringsinntekt",
                    Nynorsk to "Næringsinntekt",
                )
            }.orShowIf(inntektsgrunnlag.inntekttype_safe.equalTo("rap_and")){
                text(
                    Bokmal to "Pensjoner fra andre enn NAV",
                    Nynorsk to "Pensjon frå andre enn NAV",
                )
            }.orShowIf(inntektsgrunnlag.inntekttype_safe.equalTo("forpenutl")){
                text(
                    Bokmal to "Pensjon fra utlandet",
                    Nynorsk to "Pensjon frå utlandet",
                )
            }.orShowIf(inntektsgrunnlag.inntekttype_safe.equalTo("ikke_red")){
                showIf(inntektsgrunnlag.grunnikkereduksjon_safe.equalTo("etterbetaling")){
                    text(
                        Bokmal to "Inntekt",
                        Nynorsk to "Inntekt",
                    )
                }.orShow {
                    text(
                        Bokmal to "Inntekt som kan trekkes fra",
                        Nynorsk to "Inntekt som kan bli trekt frå",
                    )
                }
            }
        }
    }

    data class PE_UT_Etteroppgjor_DetaljBruker_FratrekkListe_RegisterKildeKode(
        val inntektsgrunnlag: Expression<Inntektsgrunnlag>,
    ): TextOnlyPhrase<LangBokmalNynorsk>(){
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(inntektsgrunnlag.registerkilde_safe.equalTo("a_ordning")){
                text(
                    Bokmal to "Elektronisk innmeldt fra arbeidsgiver",
                    Nynorsk to "Elektronisk innmeld frå arbeidsgivar",
                )
            }.orShowIf(inntektsgrunnlag.registerkilde_safe.equalTo("skd")){
                text(
                    Bokmal to "Oppgitt av Skatteetaten",
                    Nynorsk to "Opplyst av Skatteetaten",
                )
            }.orShowIf(inntektsgrunnlag.grunnikkereduksjon_safe.equalTo("etterbetaling")){
                text(
                    Bokmal to "NAV",
                    Nynorsk to "NAV",
                )
            }.orShow {
                text(
                    Bokmal to "Oppgitt av deg",
                    Nynorsk to "Opplyst av deg",
                )
            }
        }
    }

    data class PE_UT_Etteroppgjor_DetaljBruker_FratrekkListe_GrunnIkkeReduksjonKode(
        val grunnIkkeReduksjonKode: Expression<String?>,
    ): TextOnlyPhrase<LangBokmalNynorsk>(){
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(grunnIkkeReduksjonKode.equalTo("opptjent_for_innv_ut")){
                text(
                    Bokmal to "Inntekt før uføretrygden ble innvilget",
                    Nynorsk to "Inntekt før du fekk innvilga uføretrygd",
                )
            }
            showIf(grunnIkkeReduksjonKode.equalTo("opptjent_etter_innv_ut")){
                text(
                    Bokmal to "Inntekt etter at uføretrygden opphørte",
                    Nynorsk to "Inntekt etter at uføretrygda di tok slutt",
                )
            }
            showIf(grunnIkkeReduksjonKode.equalTo("erstatning_innttap_erstoppgj")){
                text(
                    Bokmal to "Erstatning for inntektstap ved erstatningsoppgjør",
                    Nynorsk to "Erstatning for inntektstap ved erstatningsoppgjer",
                )
            }
            showIf(grunnIkkeReduksjonKode.equalTo("etterslepsinnt_avslt_akt")){
                text(
                    Bokmal to "Inntekt fra helt avsluttet arbeid eller virksomhet",
                    Nynorsk to "Inntekt frå heilt avslutta arbeid eller verksemd",
                )
            }
            showIf(grunnIkkeReduksjonKode.equalTo("ikke_red_annet")){
                text(
                    Bokmal to "Annet",
                    Nynorsk to "Anna",
                )
            }
            showIf(grunnIkkeReduksjonKode.equalTo("etterbetaling")){
                text(
                    Bokmal to "Etterbetaling fra NAV",
                    Nynorsk to "Etterbetaling frå NAV",
                )
            }
        }
    }

    data class PE_UT_Etteroppgjor_DetaljEPS_FratrekkListe_InntektTypeKode(
        val inntektstypeKode: Expression<String?>
    ): TextOnlyPhrase<LangBokmalNynorsk>(){
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(inntektstypeKode.equalTo("rap_arb")){
                text(
                    Bokmal to "Arbeidsinntekt",
                    Nynorsk to "Arbeidsinntekt",
                )
            }.orShowIf(inntektstypeKode.equalTo("forintutl")){
                text(
                    Bokmal to "Utlandsinntekt",
                    Nynorsk to "Utanlandsinntekt",
                )
            }.orShowIf(inntektstypeKode.equalTo("rap_nar")){
                text(
                    Bokmal to "Næringsinntekt",
                    Nynorsk to "Næringsinntekt",
                )
            }.orShowIf(inntektstypeKode.equalTo("rap_and")){
                text(
                    Bokmal to "Pensjoner fra andre enn NAV",
                    Nynorsk to "Pensjon frå andre enn NAV",
                )
            }.orShowIf(inntektstypeKode.equalTo("forpenutl")){
                text(
                    Bokmal to "Pensjon fra utlandet",
                    Nynorsk to "Pensjon frå utlandet",
                )
            }.orShowIf(inntektstypeKode.equalTo("ikke_red")){
                text(
                    Bokmal to "Inntekt som kan trekkes fra",
                    Nynorsk to "Inntekt som kan bli trekt frå",
                )
            }
        }
    }

    //String returnVal
    //
    //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon(SYS_TableRow) = 'opptjent_for_innv_ut') THEN
    //   IF UCase(PE_XML_brev_spraak) ='NN'  THEN
    //     returnVal =  'Inntekt før du fekk innvilga uføretrygd'
    //   ELSE
    //	 returnVal =  'Inntekt før uføretrygden ble innvilget'
    //   END IF
    //
    //ELSEIF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon(SYS_TableRow) = 'opptjent_etter_innv_ut') THEN
    //   IF UCase(PE_XML_brev_spraak) ='NN'  THEN
    //     returnVal =  'Inntekt etter at uføretrygda di tok slutt'
    //   ELSE
    //	 returnVal = 'Inntekt etter at uføretrygden opphørte'
    //   END IF
    //
    //ELSEIF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon(SYS_TableRow) = 'erstatning_innttap_erstoppgj') THEN
    //   IF UCase(PE_XML_brev_spraak) ='NN'  THEN
    //     returnVal = 'Erstatning for inntektstap ved erstatningsoppgjer'
    //   ELSE
    //	 returnVal = 'Erstatning for inntektstap ved erstatningsoppgjør'
    //   END IF
    //
    //ELSEIF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon(SYS_TableRow) = 'etterslepsinnt_avslt_akt') THEN
    //   IF UCase(PE_XML_brev_spraak) ='NN'  THEN
    //     returnVal = 'Inntekt frå heilt avslutta arbeid eller verksemd'
    //   ELSE
    //	 returnVal = 'Inntekt fra helt avsluttet arbeid eller virksomhet'
    //   END IF
    //
    //ELSEIF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon(SYS_TableRow) = 'ikke_red_annet') THEN
    //   IF UCase(PE_XML_brev_spraak) ='NN'  THEN
    //     returnVal = 'Anna'
    //   ELSE
    //	 returnVal = 'Annet'
    //   END IF
    //
    //ENDIF
    //
    //value =returnVal

    data class PE_UT_Etteroppgjor_DetaljEPS_FratrekkListe_GrunnIkkeReduksjonKode(
        val grunnIkkeReduksjonKode: Expression<String?>
    ): TextOnlyPhrase<LangBokmalNynorsk>(){
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(grunnIkkeReduksjonKode.equalTo("opptjent_for_innv_ut")){
                text(
                    Bokmal to "Inntekt før uføretrygden ble innvilget",
                    Nynorsk to "Inntekt før du fekk innvilga uføretrygd",
                )
            }.orShowIf(grunnIkkeReduksjonKode.equalTo("opptjent_etter_innv_ut")) {
                text(
                    Bokmal to "Inntekt etter at uføretrygden opphørte",
                    Nynorsk to "Inntekt etter at uføretrygda di tok slutt",
                )
            }.orShowIf(grunnIkkeReduksjonKode.equalTo("erstatning_innttap_erstoppgj")) {
                text(
                    Bokmal to "Erstatning for inntektstap ved erstatningsoppgjør",
                    Nynorsk to "Erstatning for inntektstap ved erstatningsoppgjer",
                )
            }.orShowIf(grunnIkkeReduksjonKode.equalTo("etterslepsinnt_avslt_akt")) {
                text(
                    Bokmal to "Inntekt fra helt avsluttet arbeid eller virksomhet",
                    Nynorsk to "Inntekt frå heilt avslutta arbeid eller verksemd",
                )
            }.orShowIf(grunnIkkeReduksjonKode.equalTo("ikke_red_annet")) {
                text(
                    Bokmal to "Annet",
                    Nynorsk to "Anna",
                )
            }
        }

    }

    data class PE_UT_Etteroppgjor_DetaljBruker_InntektListe_InntektTypeKode(
        val inntektTypeKode: Expression<String?>
    ): TextOnlyPhrase<LangBokmalNynorsk>() {
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(inntektTypeKode.equalTo("rap_arb")) {
                text(
                    Bokmal to "Arbeidsinntekt",
                    Nynorsk to "Arbeidsinntekt",
                )
            }.orShowIf(inntektTypeKode.equalTo("forintutl")){
                text(
                    Bokmal to "Utlandsinntekt",
                    Nynorsk to "Utanlandsinntekt",
                )
            }.orShowIf(inntektTypeKode.equalTo("rap_nar")){
                text(
                    Bokmal to "Næringsinntekt",
                    Nynorsk to "Næringsinntekt",
                )
            }.orShowIf(inntektTypeKode.equalTo("rap_and")){
                text(
                    Bokmal to "Pensjoner fra andre enn NAV",
                    Nynorsk to "Pensjon frå andre enn NAV",
                )
            }.orShowIf(inntektTypeKode.equalTo("forpenutl")){
                text(
                    Bokmal to "Pensjon fra utlandet",
                    Nynorsk to "Pensjon frå utlandet",
                )
            }
        }
    }

    data class PE_UT_Etteroppgjor_DetaljEPS_FratrekkListe_RegisterKildeKode(
        val registerkilde: Expression<String?>,
    ): TextOnlyPhrase<LangBokmalNynorsk>(){
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(registerkilde.equalTo("a_ordning")){
                text(
                    Bokmal to "Elektronisk innmeldt fra arbeidsgiver",
                    Nynorsk to "Elektronisk innmeld frå arbeidsgivar",
                )
            }.orShowIf(registerkilde.equalTo("skd")){
                text(
                    Bokmal to "Oppgitt av Skatteetaten",
                    Nynorsk to "Opplyst av Skatteetaten",
                )
            }.orShow {
                text(
                    Bokmal to "Oppgitt av deg",
                    Nynorsk to "Opplyst av deg",
                )
            }
        }
    }

    data class PE_UT_Etteroppgjor_DetaljBruker_InntektListe_RegisterKildeKode(
        val registerkilde: Expression<String?>
    ): TextOnlyPhrase<LangBokmalNynorsk>() {
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(registerkilde.equalTo("a_ordning")) {
                text(
                    Bokmal to "Elektronisk innmeldt fra arbeidsgiver",
                    Nynorsk to "Elektronisk innmeld frå arbeidsgivar",
                )
            }.orShowIf(registerkilde.equalTo("skd")) {
                text(
                    Bokmal to "Oppgitt av Skatteetaten",
                    Nynorsk to "Opplyst av Skatteetaten",
                )
            }.orShow {
                text(
                    Bokmal to "Oppgitt av deg",
                    Nynorsk to "Opplyst av deg",
                )
            }
        }
    }

    data class TabellTrukketFraInntekt(val pe: Expression<PE>): OutlinePhrase<LangBokmalNynorsk>(){
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().notEqualTo(0)) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag().isNotEmpty()){
                title1 {
                    text (
                        Bokmal to "Beløp som er trukket fra inntekten din",
                        Nynorsk to "Beløp som er trekt frå inntekta di",
                    )
                }

                paragraph {
                    table(header = {
                        column {
                            text(
                                Bokmal to "Inntektstype",
                                Nynorsk to "Inntektstype",
                            )
                        }
                        column {
                            text(
                                Bokmal to "Årsak til at inntekt er trukket fra",
                                Nynorsk to "Årsak til at inntekt er trekt frå",
                            )
                        }
                        column {
                            text(
                                Bokmal to "Mottatt av",
                                Nynorsk to "Motteken av",
                            )
                        }
                        column (alignment = RIGHT) {
                            text(
                                Bokmal to "Beløp",
                                Nynorsk to "Beløp",
                            )
                        }
                    }) {
                        forEach(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag()){ inntektsgrunnlag ->

                            //IF( ( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'ikke_red' ) ) THEN      INCLUDE ENDIF
                            showIf(((inntektsgrunnlag.inntekttype_safe.equalTo("rap_nar") or inntektsgrunnlag.inntekttype_safe.equalTo("forintutl") or inntektsgrunnlag.inntekttype_safe.equalTo("rap_arb") or inntektsgrunnlag.inntekttype_safe.equalTo("ikke_red")))){
                                //[Table 3, Table 4, Table 5]

                                row {
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljBruker_FratrekkListe_InntektTypeKode(inntektsgrunnlag))
                                    }
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljBruker_FratrekkListe_GrunnIkkeReduksjonKode(inntektsgrunnlag.grunnikkereduksjon_safe))
                                    }
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljBruker_FratrekkListe_RegisterKildeKode(inntektsgrunnlag))
                                    }
                                    cell {
                                        textExpr (
                                            Bokmal to inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format() + " kr",
                                            Nynorsk to inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format() + " kr",
                                        )
                                    }
                                }
                            }
                        }
                        row {
                            cell {
                                text (
                                    Bokmal to "Inntekt trukket fra pensjonsgivende inntekt",
                                    Nynorsk to "Inntekt trekt frå pensjonsgivende inntekt",
                                    BOLD
                                )
                            }
                            cell {

                            }
                            cell {

                            }
                            cell {
                                textExpr (
                                    Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().format() + " kr",
                                    Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().format() + " kr",
                                    BOLD
                                )
                            }
                        }

                        forEach(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag()){inntektsgrunnlag ->

                            //IF((PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) AND( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_and' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forpenutl'  ) ) THEN      INCLUDE ENDIF
                            showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (inntektsgrunnlag.inntekttype_safe.equalTo("rap_and") or inntektsgrunnlag.inntekttype_safe.equalTo("forpenutl")))){
                                //[Table 3, Table 4, Table 5]
                                row {
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljBruker_FratrekkListe_InntektTypeKode(inntektsgrunnlag))
                                    }
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljBruker_FratrekkListe_GrunnIkkeReduksjonKode(inntektsgrunnlag.grunnikkereduksjon_safe))
                                    }
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljBruker_FratrekkListe_RegisterKildeKode(inntektsgrunnlag))
                                    }
                                    cell {
                                        textExpr (
                                            Bokmal to inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format() + " kr",
                                            Nynorsk to inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format() + " kr",
                                        )
                                    }
                                }
                            }
                        }
                        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                            //[Table 3, Table 4, Table 5]
                            row {
                                cell {
                                    text (
                                        Bokmal to "Inntekt trukket fra personinntekt",
                                        Nynorsk to "Inntekt trekt frå personinntekt",
                                        BOLD
                                    )
                                }
                                cell {}
                                cell {}
                                cell {
                                    textExpr (
                                        Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkbt().format() + " kr",
                                        Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkbt().format() + " kr",
                                        BOLD
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    data class TabellOversiktForskjellBetaling(
        val pe: Expression<PE>,
    ): OutlinePhrase<LangBokmalNynorsk>(){
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_Avviksbelop <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().notEqualTo(0))){
                //[Table 7, Table 8, Table 6]

                title1 {
                    text (
                        Bokmal to "Oversikt over om du har fått for mye eller for lite i ",
                        Nynorsk to "Oversikt over om du har fått for mykje eller for lite i ",
                    )

                    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
                        text (
                            Bokmal to "uføretrygd",
                            Nynorsk to "uføretrygd",
                        )
                    }

                    //IF (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0    AND (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0    OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0)    ) THEN    INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                        text (
                            Bokmal to " og ",
                            Nynorsk to " og ",
                        )
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                        text (
                            Bokmal to "barnetillegg",
                            Nynorsk to "barnetillegg",
                        )
                    }
                }

                paragraph {
                    table(header = {
                        column( columnSpan = 5 ){
                            text (
                                Bokmal to "Type stønad",
                                Nynorsk to "Type stønad",
                            )
                        }
                        column (alignment = RIGHT, columnSpan = 6){
                            text (
                                Bokmal to "Dette skulle du ha fått ut fra inntektsopplysninger fra Skatteetaten",
                                Nynorsk to "Dette skulle du ha fått ut frå inntektsopplysningar frå Skatteetaten",
                            )
                        }
                        column (alignment = RIGHT, columnSpan = 4 ){
                            text (
                                Bokmal to "Dette fikk du i ",
                                Nynorsk to "Dette fekk du i ",
                            )

                            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
                                text (
                                    Bokmal to "uføretrygd",
                                    Nynorsk to "uføretrygd",
                                )
                            }

                            //IF (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0    AND    (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0    OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0    )    ) THEN    INCLUDE ENDIF
                            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                                text (
                                    Bokmal to " og ",
                                    Nynorsk to " og ",
                                )
                            }

                            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                                text (
                                    Bokmal to "barnetillegg",
                                    Nynorsk to "barnetillegg",
                                )
                            }
                        }
                        column( alignment = RIGHT, columnSpan = 4){
                            text (
                                Bokmal to "Beløp du har fått for mye eller for lite",
                                Nynorsk to "Beløp du har fått for mykje eller for lite",
                            )
                        }
                    }) {
                        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
                            row {
                                cell {
                                    text (
                                        Bokmal to "Uføretrygd",
                                        Nynorsk to "Uføretrygd",
                                    )

                                    //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                                    showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()){
                                        text (
                                            Bokmal to " og gjenlevendetillegg",
                                            Nynorsk to " og attlevandetillegg",
                                        )
                                    }
                                }
                                cell {
                                    textExpr(
                                        Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " kr",
                                        Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " kr",
                                    )
                                }
                                cell {
                                    textExpr(
                                        Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloput().format() + " kr",
                                        Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloput().format() + " kr",
                                    )
                                }
                                cell {
                                    textExpr(
                                        Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().format() + " kr",
                                        Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().format() + " kr",
                                    )
                                }
                            }

                        }
                        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                            row {
                                cell {
                                    text(
                                        Bokmal to "Barnetillegg fellesbarn",
                                        Nynorsk to "Barnetillegg fellesbarn",
                                    )
                                }
                                cell {
                                    textExpr(
                                        Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptfb().format() + " kr",
                                        Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptfb().format() + " kr",
                                    )
                                }
                                cell {
                                    textExpr(
                                        Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptfb().format() + " kr",
                                        Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptfb().format() + " kr",
                                    )
                                }
                                cell {
                                    textExpr(
                                        Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().format() + " kr",
                                        Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().format() + " kr",
                                    )
                                }
                            }
                        }
                        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0))) {
                            //[Table 6, Table 7, Table 5]
                            row {
                                cell {
                                    text(
                                        Bokmal to "Barnetillegg særkullsbarn",
                                        Nynorsk to "Barnetillegg særkullsbarn",
                                    )
                                }
                                cell {
                                    textExpr(
                                        Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptsb().format() + " kr",
                                        Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptsb().format() + " kr",
                                    )

                                }
                                cell {
                                    textExpr(
                                        Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptsb().format() + " kr",
                                        Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptsb().format() + " kr",
                                    )

                                }
                                cell {
                                    textExpr(
                                        Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().format() + " kr",
                                        Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().format() + " kr",
                                    )

                                }
                            }
                        }
                        //PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"
                        showIf(pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr")){
                            //[Table 6, Table 7, Table 5]

                            row {
                                cell {
                                    textExpr (
                                        Bokmal to "Beløpet du har fått for mye i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ":",
                                        Nynorsk to "Beløpet du har fått for mykje i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ":",
                                        BOLD
                                    )
                                }
                                cell {  }
                                cell {  }
                                cell {
                                    textExpr(
                                        Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format() + " kr",
                                        Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format() + " kr",
                                        BOLD
                                    )
                                }
                            }
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "etterbet"
                        showIf(pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("etterbet")){
                            //[Table 6, Table 7, Table 5]

                            row {
                                cell {
                                    textExpr (
                                        Bokmal to "Beløpet du har fått for lite i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ":",
                                        Nynorsk to "Beløpet du har fått for lite i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ":",
                                        BOLD
                                    )
                                }
                                cell {  }
                                cell {  }
                                cell {
                                    textExpr(
                                        Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format() + " kr",
                                        Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format() + " kr",
                                        BOLD
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    data class TabellBeloepFratrukketInntektAnnenForelder(
        val pe: Expression<PE>,
    ): OutlinePhrase<LangBokmalNynorsk>(){
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().notEqualTo(0))){
                //[TBU705V_Overskrift, Text 4]
                title1 {
                    text (
                        Bokmal to "Beløp som er trukket fra inntekten til annen forelder",
                        Nynorsk to "Beløp som er trekt frå inntekta til den andre forelderen",
                    )
                }
                paragraph {
                    table(header = {
                        column(columnSpan = 3) {
                            text(
                                Bokmal to "Fradragstype",
                                Nynorsk to "Frådragstype",
                            )
                        }
                        column(columnSpan = 5) {
                            text(
                                Bokmal to "Årsak til at inntekt er trukket fra",
                                Nynorsk to "Årsak til at inntekt er trekt frå",
                            )
                        }
                        column(columnSpan = 2) {
                            text(
                                Bokmal to "Mottatt av",
                                Nynorsk to "Motteken av",
                            )
                        }
                        column(columnSpan = 2 ,alignment = RIGHT) {
                            text(
                                Bokmal to "Beløp",
                                Nynorsk to "Beløp",
                            )
                        }
                    }){
                        forEach(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_fratrekkliste_inntektsgrunnlag()){ inntektsgrunnlag ->
                            //IF(( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_and' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'forpenutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'ikke_red' ) ) THEN      INCLUDE ENDIF
                            showIf(((inntektsgrunnlag.inntekttype_safe.equalTo("rap_nar")
                                    or inntektsgrunnlag.inntekttype_safe.equalTo("forintutl")
                                    or inntektsgrunnlag.inntekttype_safe.equalTo("rap_arb")
                                    or inntektsgrunnlag.inntekttype_safe.equalTo("rap_and")
                                    or inntektsgrunnlag.inntekttype_safe.equalTo("forpenutl")
                                    or inntektsgrunnlag.inntekttype_safe.equalTo("ikke_red")))){
                                //[TBU705V_Tabell, Table 11]

                                row {
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljEPS_FratrekkListe_InntektTypeKode(inntektsgrunnlag.inntekttype_safe))
                                    }
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljEPS_FratrekkListe_GrunnIkkeReduksjonKode(inntektsgrunnlag.grunnikkereduksjon_safe))
                                    }
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljEPS_FratrekkListe_RegisterKildeKode(inntektsgrunnlag.registerkilde_safe))
                                    }
                                    cell {
                                        textExpr (
                                            Bokmal to inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format() + " kr",
                                            Nynorsk to inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format() + " kr",
                                        )
                                    }
                                }
                            }
                        }

                        row {
                            cell {
                                text (
                                    Bokmal to "Inntekt",
                                    Nynorsk to "Inntekt",
                                )
                            }
                            cell {
                                text (
                                    Bokmal to "Inntekt inntil ett grunnbeløp",
                                    Nynorsk to "Inntekt inntil eit grunnbeløp",
                                )
                            }
                            cell {}
                            cell {
                                textExpr (
                                    Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().format() + " kr",
                                    Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().format() + " kr",
                                )
                            }
                        }

                        row {
                            cell {
                                text (
                                    Bokmal to "Inntekt trukket fra personinntekt",
                                    Nynorsk to "Inntekt trekt frå personinntekt",
                                    BOLD
                                )
                            }
                            cell {  }
                            cell {  }
                            cell {
                                textExpr (
                                    Bokmal to pe.ut_inntekt_trukket_fra_personinntekt().format() + " kr",
                                    Nynorsk to pe.ut_inntekt_trukket_fra_personinntekt().format() + " kr",
                                    BOLD
                                )
                            }
                        }
                    }
                }
            }
        }

    }
    data class TabellInntekterEPS(
        val pe: Expression<PE>,
    ): OutlinePhrase<LangBokmalNynorsk>(){
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0 ) AND Count(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType) <> 0  THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_inntektliste_inntektsgrunnlag().isNotEmpty()){
                //[Text 3, Text 4, Text 2]
                title1 {
                    text(
                        Bokmal to "Inntekten til annen forelder",
                        Nynorsk to "Inntekta til den andre forelderen",
                    )
                }

                paragraph {
                    table(header = {
                        column(columnSpan = 3) {
                            text(
                                Bokmal to "Inntektstyper",
                                Nynorsk to "Inntektstypar",
                            )
                        }
                        column(columnSpan = 3) {
                            text(
                                Bokmal to "Mottatt av",
                                Nynorsk to "Motteken av",
                            )
                        }
                        column(columnSpan = 2, alignment = RIGHT) {
                            text(
                                Bokmal to "Registrert inntekt",
                                Nynorsk to "Registrert inntekt",
                            )
                        }
                    }) {

                        forEach(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_inntektliste_inntektsgrunnlag()) { inntektsGrunnlag ->
                            //IF(( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_and' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'forpenutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'ikke_red' ) ) THEN      INCLUDE ENDIF
                            showIf(((inntektsGrunnlag.inntekttype_safe.equalTo("rap_nar")
                                    or inntektsGrunnlag.inntekttype_safe.equalTo("forintutl")
                                    or inntektsGrunnlag.inntekttype_safe.equalTo("rap_arb")
                                    or inntektsGrunnlag.inntekttype_safe.equalTo("rap_and")
                                    or inntektsGrunnlag.inntekttype_safe.equalTo("forpenutl")
                                    or inntektsGrunnlag.inntekttype_safe.equalTo("ikke_red")))){
                                //[Table 10, Table 12]

                                row {
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljEPS_InntektListe_InntektTypeKode(inntektsGrunnlag.inntekttype_safe))
                                    }
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljEPS_InntektListe_RegisterKildeKode(inntektsGrunnlag.registerkilde_safe))
                                    }
                                    cell {
                                        textExpr(
                                            Bokmal to inntektsGrunnlag.belop_safe.ifNull(Kroner(0)).format() + " kr",
                                            Nynorsk to inntektsGrunnlag.belop_safe.ifNull(Kroner(0)).format() + " kr",
                                        )
                                    }
                                }
                            }
                        }

                        row {
                            cell {
                                text (
                                    Bokmal to "Sum personinntekt",
                                    Nynorsk to "Sum personinntekt",
                                    BOLD
                                )
                            }
                            cell { }
                            cell {
                                textExpr (
                                    Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_suminntekterbt().format() + " kr",
                                    Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_suminntekterbt().format() + " kr",
                                    BOLD
                                )
                            }
                        }
                    }
                }
            }
        }

    }

    data class TabellInntektenDin(
        val pe: Expression<PE>
    ): OutlinePhrase<LangBokmalNynorsk>(){
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            //[Text 2, Text 3, Text 4]
            //IF((PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0  OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0  OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0))  THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                title1 {
                    text (
                        Bokmal to "Inntekten din",
                        Nynorsk to "Inntekta di",
                    )
                }
            }


            //IF((PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0  OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0  OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0))  THEN      INCLUDE ENDIF
            showIf(pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) {
                //[Table 3, Table 4, Table 5]
                paragraph {
                    table(
                        header = {
                            column(columnSpan = 3) {
                                text(
                                    Bokmal to "Inntektstyper",
                                    Nynorsk to "Inntektstypar",
                                )
                            }

                            column(columnSpan = 3) {
                                text(
                                    Bokmal to "Mottatt av",
                                    Nynorsk to "Motteken av",
                                )
                            }
                            column(columnSpan = 2, alignment = RIGHT) {
                                text(
                                    Bokmal to "Registrert inntekt",
                                    Nynorsk to "Registrert inntekt",
                                )
                            }
                        }
                    ) {
                        forEach(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_inntektliste_inntektsgrunnlag()) { inntektsgrunnlag ->
                            //IF( ( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' ) ) THEN      INCLUDE ENDIF
                            showIf(
                                inntektsgrunnlag.inntekttype_safe.equalTo("rap_nar")
                                        or inntektsgrunnlag.inntekttype_safe.equalTo("forintutl")
                                        or inntektsgrunnlag.inntekttype_safe.equalTo("rap_arb")
                            ) {
                                //[Table 3, Table 4, Table 5]
                                row {
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljBruker_InntektListe_InntektTypeKode(inntektsgrunnlag.inntekttype_safe))
                                    }
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljBruker_InntektListe_RegisterKildeKode(inntektsgrunnlag.registerkilde_safe))
                                    }
                                    cell {
                                        textExpr(
                                            Bokmal to inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format() + " kr",
                                            Nynorsk to inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format() + " kr",
                                        )
                                    }
                                }
                            }
                        }

                        //PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumInntekterUT = 0
                        showIf(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut().equalTo(0)){
                            //[Table 3, Table 4, Table 5]

                            row {
                                cell {}
                                cell {}
                                cell {
                                    text (
                                        Bokmal to "Skatteetaten opplyser at du ikke hadde pensjonsgivende inntekt",
                                        Nynorsk to "Skatteetaten opplyser at du ikkje hadde pensjonsgivande inntekt",
                                    )
                                }
                            }
                        }
                        row {
                            cell {
                                text (
                                    Bokmal to "Sum pensjonsgivende inntekt",
                                    Nynorsk to "Sum pensjonsgivande inntekt",
                                    BOLD
                                )
                            }
                            cell {}
                            cell {
                                textExpr (
                                    Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut().format() + " kr",
                                    Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut().format() + " kr",
                                    BOLD
                                )
                            }
                        }

                        forEach(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_inntektliste_inntektsgrunnlag()) { inntektsgrunnlag ->
                            //IF( ( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' ) ) THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0)
                                    or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
                                    and (inntektsgrunnlag.inntekttype_safe.equalTo("rap_and") or inntektsgrunnlag.inntekttype_safe.equalTo("forpenutl"))
                            ) {
                                //[Table 3, Table 4, Table 5]
                                row {
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljBruker_InntektListe_InntektTypeKode(inntektsgrunnlag.inntekttype_safe))
                                    }
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljBruker_InntektListe_RegisterKildeKode(inntektsgrunnlag.registerkilde_safe))
                                    }
                                    cell {
                                        textExpr(
                                            Bokmal to inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format() + " kr",
                                            Nynorsk to inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format() + " kr",
                                        )
                                    }
                                }
                            }
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                            //[Table 3, Table 4, Table 5]

                            row {
                                cell {
                                    text(
                                        Bokmal to "Uføretrygd",
                                        Nynorsk to "Uføretrygd",
                                    )
                                }
                                cell {
                                    text(
                                        Bokmal to "Beregnet av NAV",
                                        Nynorsk to "Berekna av NAV",
                                    )
                                }
                                cell {
                                    textExpr(
                                        Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " kr",
                                        Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " kr",
                                    )
                                }
                            }
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                            //[Table 3, Table 4, Table 5]

                            row {
                                cell {
                                    text(
                                        Bokmal to "Sum personinntekt",
                                        Nynorsk to "Sum personinntekt",
                                        BOLD
                                    )
                                }
                                cell {}
                                cell {
                                    textExpr(
                                        Bokmal to pe.ut_sum_inntekterbt_totalbeloput().format() + " kr",
                                        Nynorsk to pe.ut_sum_inntekterbt_totalbeloput().format() + " kr",
                                        BOLD
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
