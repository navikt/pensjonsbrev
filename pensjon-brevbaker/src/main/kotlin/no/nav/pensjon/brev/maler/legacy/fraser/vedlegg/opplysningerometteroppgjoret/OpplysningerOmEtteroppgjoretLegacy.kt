package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerometteroppgjoret

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.Inntektsgrunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.InntektsgrunnlagSelectors.belop_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.InntektsgrunnlagSelectors.grunnikkereduksjon_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.InntektsgrunnlagSelectors.inntekttype_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.InntektsgrunnlagSelectors.registerkilde_safe
import no.nav.pensjon.brev.maler.legacy.ut_sum_inntekterbt_totalbeloput
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_inntektliste_inntektsgrunnlag
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkbt
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput
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
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNotEmpty
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner

object OpplysningerOmEtteroppgjoretLegacy {


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
    data class TabellTrukketFraInntekt(
        val pe: Expression<PE>
    ): OutlinePhrase<LangBokmalNynorsk>(){
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
                            column {
                                text(
                                    Bokmal to "Inntektstyper",
                                    Nynorsk to "Inntektstypar",
                                )
                            }

                            column {
                                text(
                                    Bokmal to "Mottatt av",
                                    Nynorsk to "Motteken av",
                                )
                            }
                            column {
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
                                    )
                                }
                                cell {
                                    textExpr(
                                        Bokmal to pe.ut_sum_inntekterbt_totalbeloput().format() + " kr",
                                        Nynorsk to pe.ut_sum_inntekterbt_totalbeloput().format() + " kr",
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
