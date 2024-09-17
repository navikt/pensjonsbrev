package no.nav.pensjon.brev.maler.legacy.vedlegg

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.InntektsgrunnlagSelectors.belop_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.InntektsgrunnlagSelectors.inntekttype_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.InntektsgrunnlagSelectors.registerkilde_safe
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerometteroppgjoret.OpplysningerOmEtteroppgjoretLegacy.PE_UT_Etteroppgjor_DetaljBruker_InntektListe_InntektTypeKode
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerometteroppgjoret.OpplysningerOmEtteroppgjoretLegacy.PE_UT_Etteroppgjor_DetaljBruker_InntektListe_RegisterKildeKode
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.maler.legacy.ut_periodefomstorre0101
import no.nav.pensjon.brev.maler.legacy.ut_periodetommindre3112
import no.nav.pensjon.brev.maler.legacy.ut_sum_inntekterbt_totalbeloput
import no.nav.pensjon.brev.maler.legacy.ut_uforetrygdetteroppgjor_periodefom_year
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_inntektliste_inntektsgrunnlag
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_inntekttype
import vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop
import kotlin.and
import kotlin.text.Typography.paragraph

@TemplateModelHelpers
val opplysningerOmETteroppgjoeretUTLegacy = createAttachment<LangBokmalNynorsk, PE>(
    title = newText(
        Bokmal to "Opplysninger om etteroppgjøret",
        Nynorsk to "Opplysningar om etteroppgjeret",
    ),
    includeSakspart = false,
) {
    val pe = argument
    //[PE_UT_orientering_TBU076V_veiledning]

    paragraph {
        text(
            Bokmal to "Vi bruker inntektsopplysninger fra Skatteetaten når vi vurderer om du har fått for mye eller for lite i uføretrygd",
            Nynorsk to "Vi bruker inntektsopplysningar frå Skatteetaten når vi vurderer om du har fått for mykje eller for lite i uføretrygd ",
        )

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
        showIf(
            (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
        ) {
            text(
                Bokmal to " og barnetillegg",
                Nynorsk to "og barnetillegg ",
            )
        }
        textExpr(
            Bokmal to " i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ".",
            Nynorsk to "i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ".",
        )
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
    ) {
        //[PE_UT_orientering_TBU076V_veiledning]

        paragraph {
            text(
                Bokmal to "I tabellen(e) under kan du se hva som er din pensjonsgivende inntekt",
                Nynorsk to "I tabellan(e) under kan du sjå kva som er den pensjonsgivande inntekta di",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                    .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
            ) {
                text(
                    Bokmal to ", din personinntekt",
                    Nynorsk to ", personinntekta di",
                )
            }
            text(
                Bokmal to " og hvilke inntekter som blir brukt til å vurdere om du har fått for mye eller for lite i uføretrygd",
                Nynorsk to " og kva inntekter vi bruker til å vurdere om du har fått for mykje eller for lite i uføretrygd",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                    .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
            ) {
                text(
                    Bokmal to " og barnetillegg",
                    Nynorsk to " og barnetillegg",
                )
            }
            text(
                Bokmal to ".",
                Nynorsk to ".",
            )
        }
    }

    //[Text 2, Text 3, Text 4]
    title1 {
        text (
            Bokmal to "Inntekten din",
            Nynorsk to "Inntekta di",
        )
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


//IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_Avviksbelop <> 0 ) AND Count(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType) <> 0  THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().notEqualTo(0)) and FUNKSJON_Count(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_inntekttype()).notEqualTo(0)){
        //[Text 2, Text 3, Text]

        title1 {
            text (
                Bokmal to "Beløp som er trukket fra inntekten din",
                Nynorsk to "Beløp som er trekt frå inntekta di",
            )
        }
    }
    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_Avviksbelop <> 0 ) AND Count(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType) <> 0  THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().notEqualTo(0)) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag().isNotEmpty()){
        //[Table 3, Table 4, Table 5]

        paragraph {
            text (
                Bokmal to "Inntektstype",
                Nynorsk to "Inntektstype",
            )
            text (
                Bokmal to "Årsak til at inntekt er trukket fra",
                Nynorsk to "Årsak til at inntekt er trekt frå",
            )
            text (
                Bokmal to "Mottatt av",
                Nynorsk to "Motteken av",
            )
            text (
                Bokmal to "Beløp",
                Nynorsk to "Beløp",
            )
        }

        //IF( ( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'ikke_red' ) ) THEN      INCLUDE ENDIF
        showIf(((FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_nar") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forintutl") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_arb") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("ikke_red")))){
            //[Table 3, Table 4, Table 5]

            paragraph {
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_inntekttypekode(),
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_inntekttypekode(),
                )
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_grunnikkereduksjonkode(),
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_grunnikkereduksjonkode(),
                )
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_registerkildekode(),
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_registerkildekode(),
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_belop().format() + " kr",
                    Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_belop().format() + " kr",
                )
            }
        }
        //[Table 3, Table 4, Table 5]

        paragraph {
            text (
                Bokmal to "Inntekt trukket fra pensjonsgivende inntekt",
                Nynorsk to "Inntekt trekt frå pensjonsgivende inntekt",
            )
            textExpr (
                Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().format() + " kr",
                Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().format() + " kr",
            )
        }

        //IF((PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) AND( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_and' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forpenutl'  ) ) THEN      INCLUDE ENDIF
        showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_and") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forpenutl")))){
            //[Table 3, Table 4, Table 5]

            paragraph {
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_inntekttypekode(),
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_inntekttypekode(),
                )
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_grunnikkereduksjonkode(),
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_grunnikkereduksjonkode(),
                )
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_registerkildekode(),
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_registerkildekode(),
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_belop().format() + " kr",
                    Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_belop().format() + " kr",
                )
            }
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
            //[Table 3, Table 4, Table 5]

            paragraph {
                text (
                    Bokmal to "Inntekt trukket fra personinntekt",
                    Nynorsk to "Inntekt trekt frå personinntekt",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkbt().format() + " kr",
                    Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkbt().format() + " kr",
                )
            }
        }
    }

    /*
    //[Text, Text 2, Text 3]



    //IF( PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND  (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402")  ) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402")))){
        //[Table 2, Table 3, Table 4]

        paragraph {
            text (
                Bokmal to "Vi kan i enkelte tilfeller se bort fra inntekt i etteroppgjøret",
                Nynorsk to "Vi kan i enkelte tilfelle sjå bort frå inntekt i etteroppgjeret",
            )
        }
        //[Table 2, Table 3, Table 4]

        paragraph {
            text (
                Bokmal to "Dette gjelder kun dersom du hadde en slik inntekt.",
                Nynorsk to "Dette gjeld berre dersom du hadde ei slik inntekt.",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                text (
                    Bokmal to " Det gjelder ikke for inntekten til annen forelder.",
                    Nynorsk to " Det gjeld ikkje for inntekta til den andre forelderen.",
                )
            }
            text (
                Bokmal to "Vi kan trekke fra:",
                Nynorsk to "Vi kan trekkje frå:",
            )

            //IF(PE_UT_PeriodeFomStorre0101() = true) THEN      INCLUDE ENDIF
            showIf((pe.ut_periodefomstorre0101())){
                text (
                    Bokmal to " Inntekt før du ble innvilget uføretrygd",
                    Nynorsk to " inntekt før du fekk innvilga uføretrygd",
                )
            }
            text (
                Bokmal to " Erstatning for inntektstap ved erstatningsoppgjør etterSkadeerstatningsloven § 3-1Yrkesskadeforsikringsloven § 13Pasientskadeloven § 4 første ledd",
                Nynorsk to " erstatning for inntektstap ved erstatningsoppgjer etterskadeerstatningslova § 3-1yrkesskadeforsikringslova § 13pasientskadelova § 4 første ledd",
            )

            //IF(PE_UT_PeriodeTomMindre3112() = true) THEN      INCLUDE ENDIF
            showIf((pe.ut_periodetommindre3112())){
                text (
                    Bokmal to " Inntekt etter at uføretrygden din opphørte",
                    Nynorsk to " inntekt etter at uføretrygda di tok slutt",
                )
            }
            textExpr (
                Bokmal to " Inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel:Utbetalte feriepenger for et arbeidsforhold som er avsluttetInntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomhetenProduksjonstillegg og andre overføringer til gårdbrukereHadde du en slik inntekt i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ", må du dokumentere dette innen 3 uker fra " + pe.datecurrent_formatert().format() + ". Du trenger ikke sende inn ny dokumentasjon om vi allerede har trukket dette fra. Inntekter som er trukket fra ser du i tabellen ovenfor.",
                Nynorsk to " inntekt frå arbeid eller verksemd som blei heilt avslutta før du fekk innvilga uføretrygd, for eksempel:utbetalte feriepengar for et arbeidsforhold som er avsluttainntekter frå sal av produksjonsmiddel i samband med at verksemda blei avsluttaproduksjonstillegg og andre overføringar til gardbrukararHadde du ei slik inntekt i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ", må du dokumentere dette innan tre veker frå " + pe.datecurrent_formatert().format() + ". Du treng ikkje sende inn ny dokumentasjon om vi allereie har trekt dette frå. Inntekter som er trekte frå, ser du i tabellen ovanfor.",
            )
        }
    }

    //IF((PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0  OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0  OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0))  THEN      INCLUDE ENDIF
    showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
        //[Table 2, Table 3, Table 4]

        paragraph {
            text (
                Bokmal to "Inntektstyper",
                Nynorsk to "Inntektstypar",
            )
            text (
                Bokmal to "Mottatt av",
                Nynorsk to "Motteken av",
            )
            text (
                Bokmal to "Registrert inntekt",
                Nynorsk to "Registrert inntekt",
            )
        }

        //IF( ( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' ) ) THEN      INCLUDE ENDIF
        showIf(((FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_nar") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forintutl") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_arb")))){
            //[Table 2, Table 3, Table 4]

            paragraph {
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_inntektliste_inntekttypekode(),
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_inntektliste_inntekttypekode(),
                )
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_inntektliste_registerkildekode(),
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_inntektliste_registerkildekode(),
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_inntektliste_inntektsgrunnlag_belop().format() + " kr",
                    Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_inntektliste_inntektsgrunnlag_belop().format() + " kr",
                )
            }
        }

        //PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumInntekterUT = 0
        showIf(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut().equalTo(0)){
            //[Table 2, Table 3, Table 4]

            paragraph {
                text (
                    Bokmal to "Skatteetaten opplyser at du ikke hadde pensjonsgivende inntekt",
                    Nynorsk to "Skatteetaten opplyser at du ikkje hadde pensjonsgivande inntekt",
                )
            }
        }
        //[Table 2, Table 3, Table 4]

        paragraph {
            text (
                Bokmal to "Sum pensjonsgivende inntekt",
                Nynorsk to "Sum pensjonsgivande inntekt",
            )
            textExpr (
                Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut().format() + " kr",
                Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut().format() + " kr",
            )
        }

        //IF( (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) AND( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_and' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forpenutl'  ) ) THEN      INCLUDE ENDIF
        showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_and") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forpenutl")))){
            //[Table 2, Table 3, Table 4]

            paragraph {
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_inntektliste_inntekttypekode(),
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_inntektliste_inntekttypekode(),
                )
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_inntektliste_registerkildekode(),
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_inntektliste_registerkildekode(),
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_inntektliste_inntektsgrunnlag_belop().format() + " kr",
                    Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_inntektliste_inntektsgrunnlag_belop().format() + " kr",
                )
            }
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
            //[Table 2, Table 3, Table 4]

            paragraph {
                text (
                    Bokmal to "Uføretrygd",
                    Nynorsk to "Uføretrygd",
                )
                text (
                    Bokmal to "Beregnet av NAV",
                    Nynorsk to "Berekna av NAV",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " kr",
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " kr",
                )
            }
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
            //[Table 2, Table 3, Table 4]

            paragraph {
                text (
                    Bokmal to "Sum personinntekt",
                    Nynorsk to "Sum personinntekt",
                )
                textExpr (
                    Bokmal to pe.ut_sum_inntekterbt_totalbeloput().format() + " kr",
                    Nynorsk to pe.ut_sum_inntekterbt_totalbeloput().format() + " kr",
                )
            }
        }
    }


    /*


    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
        //[Table, Table 2, Table 3]

        paragraph {
            text (
                Nynorsk to "Kva inntekter bruker vi når vi justerer uføretrygda?",
            )
        }
        //[Table, Table 2, Table 3]

        paragraph {
            text (
                Nynorsk to "Uføretrygda blir justert ut frå pensjonsgivande inntekt. Pensjonsgivande inntekt er for eksempel arbeidsinntekt, næringsinntekt og utanlandsinntekt. Ytingar frå NAV som erstattar arbeidsinntekt, er også pensjonsgivande inntekt. Dette står i § 3-15 i folketrygdlova. ",
            )
        }
    }

    //IF( PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND  (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402")  ) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402")))){
        //[Table, Table 2, Table 3]

        paragraph {
            text (
                Nynorsk to "Vi kan i enkelte tilfelle sjå bort frå inntekt i etteroppgjeret",
            )
        }
        //[Table, Table 2, Table 3]

        paragraph {
            text (
                Nynorsk to "Dette gjeld berre dersom du hadde ei slik inntekt.",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                text (
                    Nynorsk to " Det gjeld ikkje for inntekta til den andre forelderen.",
                )
            }
            text (
                Nynorsk to "Vi kan trekkje frå:",
            )

            //IF(PE_UT_PeriodeFomStorre0101() = true) THEN      INCLUDE ENDIF
            showIf((pe.ut_periodefomstorre0101())){
                text (
                    Nynorsk to " inntekt før du fekk innvilga uføretrygd",
                )
            }
            text (
                Nynorsk to " erstatning for inntektstap ved erstatningsoppgjer etterskadeerstatningslova § 3-1yrkesskadeforsikringslova § 13pasientskadelova § 4 første ledd",
            )

            //IF(PE_UT_PeriodeTomMindre3112() = true) THEN      INCLUDE ENDIF
            showIf((pe.ut_periodetommindre3112())){
                text (
                    Nynorsk to " inntekt etter at uføretrygda di tok slutt",
                )
            }
            textExpr (
                Nynorsk to " inntekt frå arbeid eller verksemd som blei heilt avslutta før du fekk innvilga uføretrygd, for eksempel:utbetalte feriepengar for et arbeidsforhold som er avsluttainntekter frå sal av produksjonsmiddel i samband med at verksemda blei avsluttaproduksjonstillegg og andre overføringar til gardbrukararHadde du ei slik inntekt i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ", må du dokumentere dette innan tre veker frå " + pe.datecurrent_formatert().format() + ". Du treng ikkje sende inn ny dokumentasjon om vi allereie har trekt dette frå. Inntekter som er trekte frå, ser du i tabellen ovanfor.",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
        //[Table, Table 2, Table 3]

        paragraph {
            text (
                Bokmal to "Hvilke inntekter bruker vi i justeringen av uføretrygden?",
            )
        }
        //[Table, Table 2, Table 3]

        paragraph {
            text (
                Bokmal to "Uføretrygden blir justert ut fra pensjonsgivende inntekt. Pensjonsgivende inntekt er for eksempel arbeidsinntekt, næringsinntekt og utlandsinntekt. Ytelser fra NAV som erstatter arbeidsinntekt er også pensjonsgivende inntekt. Dette står i § 3-15 i folketrygdloven. ",
            )
        }
    }

    //IF( PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND  (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402")  ) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402")))){
        //[Table, Table 2, Table 3]

        paragraph {
            text (
                Bokmal to "Vi kan i enkelte tilfeller se bort fra inntekt i etteroppgjøret",
            )
        }
        //[Table, Table 2, Table 3]

        paragraph {
            text (
                Bokmal to "Dette gjelder kun dersom du hadde en slik inntekt.",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                text (
                    Bokmal to " Det gjelder ikke for inntekten til annen forelder.",
                )
            }
            text (
                Bokmal to "Vi kan trekke fra:",
            )

            //IF(PE_UT_PeriodeFomStorre0101() = true) THEN      INCLUDE ENDIF
            showIf((pe.ut_periodefomstorre0101())){
                text (
                    Bokmal to " Inntekt før du ble innvilget uføretrygd",
                )
            }
            text (
                Bokmal to " Erstatning for inntektstap ved erstatningsoppgjør etterSkadeerstatningsloven § 3-1Yrkesskadeforsikringsloven § 13Pasientskadeloven § 4 første ledd",
            )

            //IF(PE_UT_PeriodeTomMindre3112() = true) THEN      INCLUDE ENDIF
            showIf((pe.ut_periodetommindre3112())){
                text (
                    Bokmal to " Inntekt etter at uføretrygden din opphørte",
                )
            }
            textExpr (
                Bokmal to " Inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel:Utbetalte feriepenger for et arbeidsforhold som er avsluttetInntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomhetenProduksjonstillegg og andre overføringer til gårdbrukereHadde du en slik inntekt i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ", må du dokumentere dette innen 3 uker fra " + pe.datecurrent_formatert().format() + ". Du trenger ikke sende inn ny dokumentasjon om vi allerede har trukket dette fra. Inntekter som er trukket fra ser du i tabellen ovenfor.",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
        //[Table, Table 2]

        paragraph {
            text (
                Bokmal to "Hvilke inntekter bruker vi i justeringen av uføretrygden?",
                Nynorsk to "Kva inntekter bruker vi når vi justerer uføretrygda?",
            )
        }
        //[Table, Table 2]

        paragraph {
            text (
                Bokmal to "Uføretrygden blir justert ut fra pensjonsgivende inntekt. Pensjonsgivende inntekt er for eksempel arbeidsinntekt, næringsinntekt og utlandsinntekt. Ytelser fra NAV som erstatter arbeidsinntekt er også pensjonsgivende inntekt. Dette står i § 3-15 i folketrygdloven. ",
                Nynorsk to "Uføretrygda blir justert ut frå pensjonsgivande inntekt. Pensjonsgivande inntekt er for eksempel arbeidsinntekt, næringsinntekt og utanlandsinntekt. Ytingar frå NAV som erstattar arbeidsinntekt, er også pensjonsgivande inntekt. Dette står i § 3-15 i folketrygdlova. ",
            )
        }
    }

    //[Text, Text 2]

    paragraph {
        text (
            Bokmal to "Beløp som er trukket fra inntekten din",
            Nynorsk to "Beløp som er trekt frå inntekta di",
        )
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_Avviksbelop <> 0 ) AND Count(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType) <> 0  THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().notEqualTo(0)) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag().isNotEmpty()){
        //[Table 4, Table 5, Table 6]

        paragraph {
            text (
                Nynorsk to "Inntektstype",
            )
            text (
                Nynorsk to "Årsak til at inntekt er trekt frå",
            )
            text (
                Nynorsk to "Motteken av",
            )
            text (
                Nynorsk to "Beløp",
            )
        }

        //IF( ( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'ikke_red' ) ) THEN      INCLUDE ENDIF
        showIf(((FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_nar") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forintutl") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_arb") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("ikke_red")))){
            //[Table 4, Table 5, Table 6]

            paragraph {
                textExpr (
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_inntekttypekode(),
                )
                textExpr (
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_grunnikkereduksjonkode(),
                )
                textExpr (
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_registerkildekode(),
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_belop().format() + " kr",
                )
            }
        }
        //[Table 4, Table 5, Table 6]

        paragraph {
            text (
                Nynorsk to "Inntekt trekt frå pensjonsgivende inntekt",
            )
            textExpr (
                Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().format() + " kr",
            )
        }

        //IF((PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) AND( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_and' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forpenutl'  ) ) THEN      INCLUDE ENDIF
        showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_and") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forpenutl")))){
            //[Table 4, Table 5, Table 6]

            paragraph {
                textExpr (
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_inntekttypekode(),
                )
                textExpr (
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_grunnikkereduksjonkode(),
                )
                textExpr (
                    Nynorsk to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_registerkildekode(),
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_belop().format() + " kr",
                )
            }
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
            //[Table 4, Table 5, Table 6]

            paragraph {
                text (
                    Nynorsk to "Inntekt trekt frå personinntekt",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkbt().format() + " kr",
                )
            }
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
        //[Table 4, Table 5, Table 6]

        paragraph {
            text (
                Nynorsk to "Dersom du ikkje har rett til barnetillegg heile året, er inntektene korta ned for berre å gjelde den perioden du får barnetillegg.",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_Avviksbelop <> 0 ) AND Count(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType) <> 0  THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().notEqualTo(0)) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag().isNotEmpty()){
        //[Table 4, Table 5, Table 6]

        paragraph {
            text (
                Bokmal to "Inntektstype",
            )
            text (
                Bokmal to "Årsak til at inntekt er trukket fra",
            )
            text (
                Bokmal to "Mottatt av",
            )
            text (
                Bokmal to "Beløp",
            )
        }

        //IF( ( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'ikke_red' ) ) THEN      INCLUDE ENDIF
        showIf(((FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_nar") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forintutl") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_arb") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("ikke_red")))){
            //[Table 4, Table 5, Table 6]

            paragraph {
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_inntekttypekode(),
                )
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_grunnikkereduksjonkode(),
                )
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_registerkildekode(),
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_belop().format() + " kr",
                )
            }
        }
        //[Table 4, Table 5, Table 6]

        paragraph {
            text (
                Bokmal to "Inntekt trukket fra pensjonsgivende inntekt",
            )
            textExpr (
                Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().format() + " kr",
            )
        }

        //IF((PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) AND( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_and' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forpenutl'  ) ) THEN      INCLUDE ENDIF
        showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_and") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forpenutl")))){
            //[Table 4, Table 5, Table 6]

            paragraph {
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_inntekttypekode(),
                )
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_grunnikkereduksjonkode(),
                )
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljbruker_fratrekkliste_registerkildekode(),
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_belop().format() + " kr",
                )
            }
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
            //[Table 4, Table 5, Table 6]

            paragraph {
                text (
                    Bokmal to "Inntekt trukket fra personinntekt",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkbt().format() + " kr",
                )
            }
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
        //[Table 4, Table 5, Table 6]

        paragraph {
            text (
                Bokmal to "Dersom du ikke har rett til barnetillegg hele året er inntektene kortet ned for kun å gjelde den perioden du mottar barnetillegg.",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
        //[Table 4, Table 5, Table 6]

        paragraph {
            text (
                Bokmal to "Mottar annen forelder uføretrygd eller alderspensjon fra NAV regnes dette også med som personinntekt.",
                Nynorsk to "Dersom den andre forelderen får uføretrygd eller alderspensjon frå NAV, blir også dette rekna med som personinntekt.",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0 AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_SumInntekterBT > PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_SumFratrekkBT) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_suminntekterbt().greaterThan(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_sumfratrekkbt()))){
        //[Table 4, Table 5, Table 6]

        paragraph {
            textExpr (
                Bokmal to "Folketrygdens grunnbeløp på inntil ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner er holdt utenfor inntekten til annen forelder.",
                Nynorsk to "Grunnbeløpet i folketrygda på inntil ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner er halde utanfor inntekta til den andre forelderen.",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
        //[Table 5, Table 6, Table 7]

        paragraph {
            text (
                Nynorsk to "Dersom du ikkje har rett til barnetillegg heile året, er inntektene korta ned for berre å gjelde den perioden du får barnetillegg.",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_Avviksbelop <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().notEqualTo(0))){
        //[Table 5, Table 6, Table 7]

        paragraph {
            text (
                Nynorsk to "Type stønad",
            )
            text (
                Nynorsk to "Dette skulle du ha fått ut frå inntektsopplysningar frå Skatteetaten",
            )
            text (
                Nynorsk to "Dette fekk du i ",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
                text (
                    Nynorsk to "uføretrygd",
                )
            }

            //IF (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0    AND    (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0    OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0    )    ) THEN    INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                text (
                    Nynorsk to " og ",
                )
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                text (
                    Nynorsk to "barnetillegg",
                )
            }
            text (
                Nynorsk to "Beløp du har fått for mykje eller for lite",
            )
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
            //[Table 5, Table 6, Table 7]

            paragraph {
                text (
                    Nynorsk to "Uføretrygd",
                )

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()){
                    text (
                        Nynorsk to " og attlevandetillegg",
                    )
                }
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " kr",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloput().format() + " kr",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().format() + " kr",
                )
            }
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
            //[Table 5, Table 6, Table 7]

            paragraph {
                text (
                    Nynorsk to "Barnetillegg fellesbarn",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptfb().format() + " kr",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptfb().format() + " kr",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().format() + " kr",
                )
            }
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0))){
            //[Table 5, Table 6, Table 7]

            paragraph {
                text (
                    Nynorsk to "Barnetillegg særkullsbarn",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptsb().format() + " kr",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptsb().format() + " kr",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().format() + " kr",
                )
            }
        }

        //PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"
        showIf(pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr")){
            //[Table 5, Table 6, Table 7]

            paragraph {
                textExpr (
                    Nynorsk to "Beløpet du har fått for mykje i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ":",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format() + " kr",
                )
            }
        }

        //PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "etterbet"
        showIf(pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("etterbet")){
            //[Table 5, Table 6, Table 7]

            paragraph {
                textExpr (
                    Nynorsk to "Beløpet du har fått for lite i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ":",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format() + " kr",
                )
            }
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
        //[Table 5, Table 6, Table 7]

        paragraph {
            text (
                Bokmal to "Dersom du ikke har rett til barnetillegg hele året er inntektene kortet ned for kun å gjelde den perioden du mottar barnetillegg.",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_Avviksbelop <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().notEqualTo(0))){
        //[Table 5, Table 6, Table 7]

        paragraph {
            text (
                Bokmal to "Type stønad",
            )
            text (
                Bokmal to "Dette skulle du ha fått ut fra inntektsopplysninger fra Skatteetaten",
            )
            text (
                Bokmal to "Dette fikk du i ",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
                text (
                    Bokmal to "uføretrygd",
                )
            }

            //IF (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0    AND    (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0    OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0    )    ) THEN    INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                text (
                    Bokmal to " og ",
                )
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                text (
                    Bokmal to "barnetillegg",
                )
            }
            text (
                Bokmal to "Beløp du har fått for mye eller for lite",
            )
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
            //[Table 5, Table 6, Table 7]

            paragraph {
                text (
                    Bokmal to "Uføretrygd",
                )

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()){
                    text (
                        Bokmal to " og gjenlevendetillegg",
                    )
                }
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " kr",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloput().format() + " kr",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().format() + " kr",
                )
            }
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
            //[Table 5, Table 6, Table 7]

            paragraph {
                text (
                    Bokmal to "Barnetillegg fellesbarn",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptfb().format() + " kr",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptfb().format() + " kr",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().format() + " kr",
                )
            }
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0))){
            //[Table 5, Table 6, Table 7]

            paragraph {
                text (
                    Bokmal to "Barnetillegg særkullsbarn",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptsb().format() + " kr",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptsb().format() + " kr",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().format() + " kr",
                )
            }
        }

        //PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"
        showIf(pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr")){
            //[Table 5, Table 6, Table 7]

            paragraph {
                textExpr (
                    Bokmal to "Beløpet du har fått for mye i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ":",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format() + " kr",
                )
            }
        }

        //PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "etterbet"
        showIf(pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("etterbet")){
            //[Table 5, Table 6, Table 7]

            paragraph {
                textExpr (
                    Bokmal to "Beløpet du har fått for lite i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ":",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format() + " kr",
                )
            }
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
        //[Table 5, Table 6, Table 7]

        paragraph {
            text (
                Bokmal to "Mottar annen forelder uføretrygd eller alderspensjon fra NAV regnes dette også med som personinntekt.",
                Nynorsk to "Dersom den andre forelderen får uføretrygd eller alderspensjon frå NAV, blir også dette rekna med som personinntekt.",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0 AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_SumInntekterBT > PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_SumFratrekkBT) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_suminntekterbt().greaterThan(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_sumfratrekkbt()))){
        //[Table 5, Table 6, Table 7]

        paragraph {
            textExpr (
                Bokmal to "Folketrygdens grunnbeløp på inntil ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner er holdt utenfor inntekten til annen forelder.",
                Nynorsk to "Grunnbeløpet i folketrygda på inntil ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner er halde utanfor inntekta til den andre forelderen.",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_Avviksbelop <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().notEqualTo(0))){
        //[Table 6, Table 7, Table 8]

        paragraph {
            text (
                Nynorsk to "Type stønad",
            )
            text (
                Nynorsk to "Dette skulle du ha fått ut frå inntektsopplysningar frå Skatteetaten",
            )
            text (
                Nynorsk to "Dette fekk du i ",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
                text (
                    Nynorsk to "uføretrygd",
                )
            }

            //IF (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0    AND    (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0    OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0    )    ) THEN    INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                text (
                    Nynorsk to " og ",
                )
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                text (
                    Nynorsk to "barnetillegg",
                )
            }
            text (
                Nynorsk to "Beløp du har fått for mykje eller for lite",
            )
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
            //[Table 6, Table 7, Table 8]

            paragraph {
                text (
                    Nynorsk to "Uføretrygd",
                )

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()){
                    text (
                        Nynorsk to " og attlevandetillegg",
                    )
                }
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " kr",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloput().format() + " kr",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().format() + " kr",
                )
            }
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
            //[Table 6, Table 7, Table 8]

            paragraph {
                text (
                    Nynorsk to "Barnetillegg fellesbarn",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptfb().format() + " kr",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptfb().format() + " kr",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().format() + " kr",
                )
            }
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0))){
            //[Table 6, Table 7, Table 8]

            paragraph {
                text (
                    Nynorsk to "Barnetillegg særkullsbarn",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptsb().format() + " kr",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptsb().format() + " kr",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().format() + " kr",
                )
            }
        }

        //PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"
        showIf(pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr")){
            //[Table 6, Table 7, Table 8]

            paragraph {
                textExpr (
                    Nynorsk to "Beløpet du har fått for mykje i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ":",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format() + " kr",
                )
            }
        }

        //PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "etterbet"
        showIf(pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("etterbet")){
            //[Table 6, Table 7, Table 8]

            paragraph {
                textExpr (
                    Nynorsk to "Beløpet du har fått for lite i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ":",
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format() + " kr",
                )
            }
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().notEqualTo(0))){
        //[Table 6, Table 7, Table 8]

        paragraph {
            text (
                Nynorsk to "Oversikt over om du har fått for mykje eller for lite i ",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
                text (
                    Nynorsk to "uføretrygd",
                )
            }

            //IF (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0    AND (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0    OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0)    ) THEN    INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                text (
                    Nynorsk to " og ",
                )
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                text (
                    Nynorsk to "barnetillegg",
                )
            }
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_Avviksbelop <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().notEqualTo(0))){
        //[Table 6, Table 7, Table 8]

        paragraph {
            text (
                Bokmal to "Type stønad",
            )
            text (
                Bokmal to "Dette skulle du ha fått ut fra inntektsopplysninger fra Skatteetaten",
            )
            text (
                Bokmal to "Dette fikk du i ",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
                text (
                    Bokmal to "uføretrygd",
                )
            }

            //IF (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0    AND    (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0    OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0    )    ) THEN    INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                text (
                    Bokmal to " og ",
                )
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                text (
                    Bokmal to "barnetillegg",
                )
            }
            text (
                Bokmal to "Beløp du har fått for mye eller for lite",
            )
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
            //[Table 6, Table 7, Table 8]

            paragraph {
                text (
                    Bokmal to "Uføretrygd",
                )

                //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()){
                    text (
                        Bokmal to " og gjenlevendetillegg",
                    )
                }
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " kr",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloput().format() + " kr",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().format() + " kr",
                )
            }
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
            //[Table 6, Table 7, Table 8]

            paragraph {
                text (
                    Bokmal to "Barnetillegg fellesbarn",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptfb().format() + " kr",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptfb().format() + " kr",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().format() + " kr",
                )
            }
        }

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0))){
            //[Table 6, Table 7, Table 8]

            paragraph {
                text (
                    Bokmal to "Barnetillegg særkullsbarn",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptsb().format() + " kr",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptsb().format() + " kr",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().format() + " kr",
                )
            }
        }

        //PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"
        showIf(pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr")){
            //[Table 6, Table 7, Table 8]

            paragraph {
                textExpr (
                    Bokmal to "Beløpet du har fått for mye i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ":",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format() + " kr",
                )
            }
        }

        //PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "etterbet"
        showIf(pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("etterbet")){
            //[Table 6, Table 7, Table 8]

            paragraph {
                textExpr (
                    Bokmal to "Beløpet du har fått for lite i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ":",
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format() + " kr",
                )
            }
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_Avviksbelop <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().notEqualTo(0))){
        //[Table 6, Table 7, Table 8]

        paragraph {
            text (
                Bokmal to "Oversikt over om du har fått for mye eller for lite i ",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
                text (
                    Bokmal to "uføretrygd",
                )
            }

            //IF (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0    AND (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0    OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0)    ) THEN    INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                text (
                    Bokmal to " og ",
                )
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                text (
                    Bokmal to "barnetillegg",
                )
            }
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().notEqualTo(0))){
        //[Table 7, Table 8, Table 9]

        paragraph {
            text (
                Nynorsk to "Oversikt over om du har fått for mykje eller for lite i ",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
                text (
                    Nynorsk to "uføretrygd",
                )
            }

            //IF (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0    AND (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0    OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0)    ) THEN    INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                text (
                    Nynorsk to " og ",
                )
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                text (
                    Nynorsk to "barnetillegg",
                )
            }
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
        //[Table 7, Table 8, Table 9]

        paragraph {
            text (
                Nynorsk to "Kva inntekter bruker vi når vi fastset kor stort barnetillegget er?",
            )
        }
        //[Table 7, Table 8, Table 9]

        paragraph {
            text (
                Nynorsk to "Barnetillegg blir berekna ut frå personinntekta. Dette står i §12-2 i skattelova. Personinntekt er den skattepliktige inntekta di før skatt. ",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                text (
                    Nynorsk to "Bur barnet/barna saman med begge foreldra sine, reknar vi også med personinntekta til den andre forelderen. ",
                )
            }
            text (
                Nynorsk to "Personinntekt er for eksempel: lønn, inkludert bonus og overtid frå arbeidsgivar, næringsinntekt, utanlandsinntekt, uføretrygd og andre ytingar frå NAV.",
            )
        }
        //[Table 7, Table 8, Table 9]

        paragraph {
            text (
                Nynorsk to "Du kan lese meir om personinntekt på $SKATTEETATEN_URL. ",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_Avviksbelop <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().notEqualTo(0))){
        //[Table 7, Table 8, Table 9]

        paragraph {
            text (
                Bokmal to "Oversikt over om du har fått for mye eller for lite i ",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
                text (
                    Bokmal to "uføretrygd",
                )
            }

            //IF (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0    AND (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0    OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0)    ) THEN    INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0) and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))){
                text (
                    Bokmal to " og ",
                )
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                text (
                    Bokmal to "barnetillegg",
                )
            }
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
        //[Table 7, Table 8, Table 9]

        paragraph {
            text (
                Bokmal to "Hvilke inntekter bruker vi i fastsettelsen av størrelsen på barnetillegget?",
            )
        }
        //[Table 7, Table 8, Table 9]

        paragraph {
            text (
                Bokmal to "Barnetillegg blir beregnet ut fra personinntekt. Dette står i §12-2 i skatteloven. Personinntekt er den skattepliktige inntekten din før skatt. ",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                text (
                    Bokmal to "Bor barnet/barna sammen med begge sine foreldre så regner vi også med personinntekt til annen forelder. ",
                )
            }
            text (
                Bokmal to "Personinntekt er for eksempel: lønn, inkludert bonus og overtid fra arbeidsgiver, næringsinntekt, utlandsinntekt, uføretrygd og andre ytelser fra NAV.",
            )
        }
        //[Table 7, Table 8, Table 9]

        paragraph {
            text (
                Bokmal to "Du kan lese mer om personinntekt på $SKATTEETATEN_URL. ",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
        //[Table 8, Table 9, Table 10]

        paragraph {
            text (
                Nynorsk to "Kva inntekter bruker vi når vi fastset kor stort barnetillegget er?",
            )
        }
        //[Table 8, Table 9, Table 10]

        paragraph {
            text (
                Nynorsk to "Barnetillegg blir berekna ut frå personinntekta. Dette står i §12-2 i skattelova. Personinntekt er den skattepliktige inntekta di før skatt. ",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                text (
                    Nynorsk to "Bur barnet/barna saman med begge foreldra sine, reknar vi også med personinntekta til den andre forelderen. ",
                )
            }
            text (
                Nynorsk to "Personinntekt er for eksempel: lønn, inkludert bonus og overtid frå arbeidsgivar, næringsinntekt, utanlandsinntekt, uføretrygd og andre ytingar frå NAV.",
            )
        }
        //[Table 8, Table 9, Table 10]

        paragraph {
            text (
                Nynorsk to "Du kan lese meir om personinntekt på $SKATTEETATEN_URL. ",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = 'tilbakekr'  AND (PE_pebrevkode = 'PE_UT_23_001' OR PE_pebrevkode = 'PE_UT_04_402')) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402")))){

        //IF( (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0        OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0      ) AND      (PE_UT_PeriodeFomStorre0101() = true      OR PE_UT_PeriodeTomMindre3112() = true)      ) THEN      INCLUDE ENDIF
        showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (pe.ut_periodefomstorre0101() or pe.ut_periodetommindre3112()))){
            //[Table 8, Table 9, Table 10]

            paragraph {
                text (
                    Nynorsk to "Inntekt i periodar før du fekk innvilga uføretrygd og etter at uføretrygda tok slutt, skal ikkje vere med i vurderinga av om du har fått for mykje eller for lite i barnetillegg. ",
                )

                //PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true
                showIf(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb()){
                    text (
                        Nynorsk to "Dersom du får barnetillegg for barn som bur saman med begge foreldra sine, held vi også slik inntekt utanfor inntekta som er oppgitt for den andre forelderen. ",
                    )
                }
            }
        }

        //IF( (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0        OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0      ) AND      (PE_UT_PeriodeFomStorre0101() = true      OR PE_UT_PeriodeTomMindre3112() = true)      ) THEN      INCLUDE ENDIF
        showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (pe.ut_periodefomstorre0101() or pe.ut_periodetommindre3112()))){
            //[Table 8, Table 9, Table 10]

            paragraph {
                text (
                    Nynorsk to "Vi kan sjå bort frå næringsinntekt ",
                )

                //PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true
                showIf(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb()){
                    text (
                        Nynorsk to "til deg eller den andre forelderen ",
                    )
                }
                text (
                    Nynorsk to "frå ei næringsverksemd som blei avslutta før uføretrygda blei innvilga, eller pensjon frå utlandet i perioden før uføretrygda blei innvilga, eller etter at ho tok slutt.",
                )
            }
        }
        //[Table 8, Table 9, Table 10]

        paragraph {
            text (
                Nynorsk to "Dersom vi får dokumentasjon frå deg som har noko å seie for etteroppgjeret, gjer vi eit nytt etteroppgjer og du får eit nytt brev. Vi gjer også eit nytt etteroppgjer dersom Skatteetaten endrar på fastsettinga av skatten din, og det har betydning for uføretrygda di. ",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
        //[Table 8, Table 9, Table 10]

        paragraph {
            text (
                Bokmal to "Hvilke inntekter bruker vi i fastsettelsen av størrelsen på barnetillegget?",
            )
        }
        //[Table 8, Table 9, Table 10]

        paragraph {
            text (
                Bokmal to "Barnetillegg blir beregnet ut fra personinntekt. Dette står i §12-2 i skatteloven. Personinntekt er den skattepliktige inntekten din før skatt. ",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
                text (
                    Bokmal to "Bor barnet/barna sammen med begge sine foreldre så regner vi også med personinntekt til annen forelder. ",
                )
            }
            text (
                Bokmal to "Personinntekt er for eksempel: lønn, inkludert bonus og overtid fra arbeidsgiver, næringsinntekt, utlandsinntekt, uføretrygd og andre ytelser fra NAV.",
            )
        }
        //[Table 8, Table 9, Table 10]

        paragraph {
            text (
                Bokmal to "Du kan lese mer om personinntekt på $SKATTEETATEN_URL. ",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = 'tilbakekr'  AND (PE_pebrevkode = 'PE_UT_23_001' OR PE_pebrevkode = 'PE_UT_04_402')) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402")))){

        //IF( (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0        OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0      ) AND      (PE_UT_PeriodeFomStorre0101() = true      OR PE_UT_PeriodeTomMindre3112() = true)      ) THEN      INCLUDE ENDIF
        showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (pe.ut_periodefomstorre0101() or pe.ut_periodetommindre3112()))){
            //[Table 8, Table 9, Table 10]

            paragraph {
                text (
                    Bokmal to "Inntekt i perioder før innvilgelse av uføretrygd og etter at uføretrygden opphørte, skal ikke være med i vurderingen om du har fått for mye eller for lite i barnetillegg. ",
                )

                //PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true
                showIf(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb()){
                    text (
                        Bokmal to "Dersom du mottar barnetillegg for barn som bor sammen med begge sine foreldre, vil vi også holde slik inntekt utenfor inntekt som er oppgitt for annen forelder. ",
                    )
                }
            }
        }

        //IF( (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0        OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0      ) AND      (PE_UT_PeriodeFomStorre0101() = true      OR PE_UT_PeriodeTomMindre3112() = true)      ) THEN      INCLUDE ENDIF
        showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (pe.ut_periodefomstorre0101() or pe.ut_periodetommindre3112()))){
            //[Table 8, Table 9, Table 10]

            paragraph {
                text (
                    Bokmal to "Vi kan se bort fra næringsinntekt ",
                )

                //PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true
                showIf(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb()){
                    text (
                        Bokmal to "til deg eller annen forelder ",
                    )
                }
                text (
                    Bokmal to "fra en næringsvirksomhet som ble avsluttet før innvilgelse av uføretrygd, eller pensjon fra utland i perioden før uføretrygden ble innvilget eller at den opphørte.",
                )
            }
        }
        //[Table 8, Table 9, Table 10]

        paragraph {
            text (
                Bokmal to "Hvis vi mottar dokumentasjon fra deg som har betydning for etteroppgjøret, vil vi gjøre et nytt etteroppgjør og du får et nytt brev. Vi vil også gjøre et nytt etteroppgjør dersom Skatteetaten endrer fastsetting av skatten din og det har betydning for uføretrygden din. ",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = 'tilbakekr'  AND (PE_pebrevkode = 'PE_UT_23_001' OR PE_pebrevkode = 'PE_UT_04_402')) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402")))){

        //IF( (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0        OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0      ) AND      (PE_UT_PeriodeFomStorre0101() = true      OR PE_UT_PeriodeTomMindre3112() = true)      ) THEN      INCLUDE ENDIF
        showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (pe.ut_periodefomstorre0101() or pe.ut_periodetommindre3112()))){
            //[Table 9, Table 10, Table 12]

            paragraph {
                text (
                    Nynorsk to "Inntekt i periodar før du fekk innvilga uføretrygd og etter at uføretrygda tok slutt, skal ikkje vere med i vurderinga av om du har fått for mykje eller for lite i barnetillegg. ",
                )

                //PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true
                showIf(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb()){
                    text (
                        Nynorsk to "Dersom du får barnetillegg for barn som bur saman med begge foreldra sine, held vi også slik inntekt utanfor inntekta som er oppgitt for den andre forelderen. ",
                    )
                }
            }
        }

        //IF( (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0        OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0      ) AND      (PE_UT_PeriodeFomStorre0101() = true      OR PE_UT_PeriodeTomMindre3112() = true)      ) THEN      INCLUDE ENDIF
        showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (pe.ut_periodefomstorre0101() or pe.ut_periodetommindre3112()))){
            //[Table 9, Table 10, Table 12]

            paragraph {
                text (
                    Nynorsk to "Vi kan sjå bort frå næringsinntekt ",
                )

                //PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true
                showIf(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb()){
                    text (
                        Nynorsk to "til deg eller den andre forelderen ",
                    )
                }
                text (
                    Nynorsk to "frå ei næringsverksemd som blei avslutta før uføretrygda blei innvilga, eller pensjon frå utlandet i perioden før uføretrygda blei innvilga, eller etter at ho tok slutt.",
                )
            }
        }
        //[Table 9, Table 10, Table 12]

        paragraph {
            text (
                Nynorsk to "Dersom vi får dokumentasjon frå deg som har noko å seie for etteroppgjeret, gjer vi eit nytt etteroppgjer og du får eit nytt brev. Vi gjer også eit nytt etteroppgjer dersom Skatteetaten endrar på fastsettinga av skatten din, og det har betydning for uføretrygda di. ",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0 ) AND Count(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType) <> 0  THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and FUNKSJON_Count(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_inntektliste_inntektsgrunnlag_inntekttype()).notEqualTo(0)){
        //[Table 9, Table 10, Table 12]

        paragraph {
            text (
                Nynorsk to "Inntektstypar",
            )
            text (
                Nynorsk to "Motteken av",
            )
            text (
                Nynorsk to "Registrert inntekt",
            )
        }

        //IF(( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_and' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'forpenutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'ikke_red' ) ) THEN      INCLUDE ENDIF
        showIf(((FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_nar") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forintutl") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_arb") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_and") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forpenutl") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("ikke_red")))){
            //[Table 9, Table 10, Table 12]

            paragraph {
                textExpr (
                    Nynorsk to pe.ut_etteroppgjor_detaljeps_inntektliste_inntekttypekode(),
                )
                textExpr (
                    Nynorsk to pe.ut_etteroppgjor_detaljeps_inntektliste_registerkildekode(),
                )
                textExpr (
                    Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_inntektliste_inntektsgrunnlag_belop().format() + " kr",
                )
            }
        }
        //[Table 9, Table 10, Table 12]

        paragraph {
            text (
                Nynorsk to "Sum personinntekt",
            )
            textExpr (
                Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_suminntekterbt().format() + " kr",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = 'tilbakekr'  AND (PE_pebrevkode = 'PE_UT_23_001' OR PE_pebrevkode = 'PE_UT_04_402')) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402")))){

        //IF( (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0        OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0      ) AND      (PE_UT_PeriodeFomStorre0101() = true      OR PE_UT_PeriodeTomMindre3112() = true)      ) THEN      INCLUDE ENDIF
        showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (pe.ut_periodefomstorre0101() or pe.ut_periodetommindre3112()))){
            //[Table 9, Table 10, Table 12]

            paragraph {
                text (
                    Bokmal to "Inntekt i perioder før innvilgelse av uføretrygd og etter at uføretrygden opphørte, skal ikke være med i vurderingen om du har fått for mye eller for lite i barnetillegg. ",
                )

                //PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true
                showIf(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb()){
                    text (
                        Bokmal to "Dersom du mottar barnetillegg for barn som bor sammen med begge sine foreldre, vil vi også holde slik inntekt utenfor inntekt som er oppgitt for annen forelder. ",
                    )
                }
            }
        }

        //IF( (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0        OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0      ) AND      (PE_UT_PeriodeFomStorre0101() = true      OR PE_UT_PeriodeTomMindre3112() = true)      ) THEN      INCLUDE ENDIF
        showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (pe.ut_periodefomstorre0101() or pe.ut_periodetommindre3112()))){
            //[Table 9, Table 10, Table 12]

            paragraph {
                text (
                    Bokmal to "Vi kan se bort fra næringsinntekt ",
                )

                //PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true
                showIf(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb()){
                    text (
                        Bokmal to "til deg eller annen forelder ",
                    )
                }
                text (
                    Bokmal to "fra en næringsvirksomhet som ble avsluttet før innvilgelse av uføretrygd, eller pensjon fra utland i perioden før uføretrygden ble innvilget eller at den opphørte.",
                )
            }
        }
        //[Table 9, Table 10, Table 12]

        paragraph {
            text (
                Bokmal to "Hvis vi mottar dokumentasjon fra deg som har betydning for etteroppgjøret, vil vi gjøre et nytt etteroppgjør og du får et nytt brev. Vi vil også gjøre et nytt etteroppgjør dersom Skatteetaten endrer fastsetting av skatten din og det har betydning for uføretrygden din. ",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0 ) AND Count(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType) <> 0  THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and FUNKSJON_Count(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_inntektliste_inntektsgrunnlag_inntekttype()).notEqualTo(0)){
        //[Table 9, Table 10, Table 12]

        paragraph {
            text (
                Bokmal to "Inntektstyper",
            )
            text (
                Bokmal to "Mottatt av",
            )
            text (
                Bokmal to "Registrert inntekt",
            )
        }

        //IF(( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_and' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'forpenutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'ikke_red' ) ) THEN      INCLUDE ENDIF
        showIf(((FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_nar") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forintutl") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_arb") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_and") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forpenutl") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("ikke_red")))){
            //[Table 9, Table 10, Table 12]

            paragraph {
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljeps_inntektliste_inntekttypekode(),
                )
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljeps_inntektliste_registerkildekode(),
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_inntektliste_inntektsgrunnlag_belop().format() + " kr",
                )
            }
        }
        //[Table 9, Table 10, Table 12]

        paragraph {
            text (
                Bokmal to "Sum personinntekt",
            )
            textExpr (
                Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_suminntekterbt().format() + " kr",
            )
        }
    }
    //[Text 3, Text 4, TBU705V_Overskrift]

    paragraph {
        text (
            Bokmal to "Inntekten til annen forelder",
            Nynorsk to "Inntekta til den andre forelderen",
        )
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0 ) AND Count(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType) <> 0  THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and FUNKSJON_Count(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_inntektliste_inntektsgrunnlag_inntekttype()).notEqualTo(0)){
        //[Table 10, Table 12]

        paragraph {
            text (
                Bokmal to "Inntektstyper",
                Nynorsk to "Inntektstypar",
            )
            text (
                Bokmal to "Mottatt av",
                Nynorsk to "Motteken av",
            )
            text (
                Bokmal to "Registrert inntekt",
                Nynorsk to "Registrert inntekt",
            )
        }

        //IF(( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_and' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'forpenutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'ikke_red' ) ) THEN      INCLUDE ENDIF
        showIf(((FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_nar") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forintutl") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_arb") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_and") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forpenutl") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("ikke_red")))){
            //[Table 10, Table 12]

            paragraph {
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljeps_inntektliste_inntekttypekode(),
                    Nynorsk to pe.ut_etteroppgjor_detaljeps_inntektliste_inntekttypekode(),
                )
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljeps_inntektliste_registerkildekode(),
                    Nynorsk to pe.ut_etteroppgjor_detaljeps_inntektliste_registerkildekode(),
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_inntektliste_inntektsgrunnlag_belop().format() + " kr",
                    Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_inntektliste_inntektsgrunnlag_belop().format() + " kr",
                )
            }
        }
        //[Table 10, Table 12]

        paragraph {
            text (
                Bokmal to "Sum personinntekt",
                Nynorsk to "Sum personinntekt",
            )
            textExpr (
                Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_suminntekterbt().format() + " kr",
                Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_suminntekterbt().format() + " kr",
            )
        }
    }
    //[Text 4, TBU705V_Overskrift]

    paragraph {
        text (
            Bokmal to "Beløp som er trukket fra inntekten til annen forelder",
            Nynorsk to "Beløp som er trekt frå inntekta til den andre forelderen",
        )
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().notEqualTo(0))){
        //[Table 11, TBU705V_Tabell]

        paragraph {
            text (
                Bokmal to "Fradragstype",
                Nynorsk to "Frådragstype",
            )
            text (
                Bokmal to "Årsak til at inntekt er trukket fra",
                Nynorsk to "Årsak til at inntekt er trekt frå",
            )
            text (
                Bokmal to "Mottatt av",
                Nynorsk to "Motteken av",
            )
            text (
                Bokmal to "Beløp",
                Nynorsk to "Beløp",
            )
        }

        //IF(( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_and' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'forpenutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'ikke_red' ) ) THEN      INCLUDE ENDIF
        showIf(((FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_nar") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forintutl") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_arb") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("rap_and") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("forpenutl") or FUNKSJON_PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(sys_tablerow()).equalTo("ikke_red")))){
            //[Table 11, TBU705V_Tabell]

            paragraph {
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljeps_fratrekkliste_inntekttypekode(),
                    Nynorsk to pe.ut_etteroppgjor_detaljeps_fratrekkliste_inntekttypekode(),
                )
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljeps_fratrekkliste_grunnikkereduksjonkode(),
                    Nynorsk to pe.ut_etteroppgjor_detaljeps_fratrekkliste_grunnikkereduksjonkode(),
                )
                textExpr (
                    Bokmal to pe.ut_etteroppgjor_detaljeps_fratrekkliste_registerkildekode(),
                    Nynorsk to pe.ut_etteroppgjor_detaljeps_fratrekkliste_registerkildekode(),
                )
                textExpr (
                    Bokmal to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_fratrekkliste_inntektsgrunnlag_belop().format() + " kr",
                    Nynorsk to pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_fratrekkliste_inntektsgrunnlag_belop().format() + " kr",
                )
            }
        }
        //[Table 11, TBU705V_Tabell]

        paragraph {
            text (
                Bokmal to "Inntekt",
                Nynorsk to "Inntekt",
            )
            text (
                Bokmal to "Inntekt inntil ett grunnbeløp",
                Nynorsk to "Inntekt inntil eit grunnbeløp",
            )
            textExpr (
                Bokmal to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().format() + " kr",
                Nynorsk to pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt().format() + " kr",
            )
        }
        //[Table 11, TBU705V_Tabell]

        paragraph {
            text (
                Bokmal to "Inntekt trukket fra personinntekt",
                Nynorsk to "Inntekt trekt frå personinntekt",
            )
            textExpr (
                Bokmal to pe.ut_inntekt_trukket_fra_personinntekt().format() + " kr",
                Nynorsk to pe.ut_inntekt_trukket_fra_personinntekt().format() + " kr",
            )
        }
    }
}
