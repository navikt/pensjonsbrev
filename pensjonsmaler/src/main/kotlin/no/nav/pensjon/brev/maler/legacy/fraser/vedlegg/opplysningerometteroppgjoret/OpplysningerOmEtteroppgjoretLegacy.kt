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
import no.nav.pensjon.brevbaker.api.model.Kroner

object OpplysningerOmEtteroppgjoretLegacy {

    data class PE_UT_Etteroppgjor_DetaljEPS_InntektListe_InntektTypeKode(
        val inntektType: Expression<String?>
    ) : TextOnlyPhrase<LangBokmalNynorsk>() {
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(inntektType.equalTo("rap_arb")) {
                text(
                    bokmal { + "Arbeidsinntekt" },
                    nynorsk { + "Arbeidsinntekt" },
                )
            }.orShowIf(inntektType.equalTo("forintutl")) {
                text(
                    bokmal { + "Utlandsinntekt" },
                    nynorsk { + "Utenlandsinntekt" },
                )
            }.orShowIf(inntektType.equalTo("rap_nar")) {
                text(
                    bokmal { + "Næringsinntekt" },
                    nynorsk { + "Næringsinntekt" },
                )
            }.orShowIf(inntektType.equalTo("rap_and")) {
                text(
                    bokmal { + "Pensjoner fra andre enn Nav" },
                    nynorsk { + "Pensjon frå andre enn Nav" },
                )
            }.orShowIf(inntektType.equalTo("forpenutl")) {
                text(
                    bokmal { + "Pensjon fra utlandet" },
                    nynorsk { + "Pensjon frå utlandet" },
                )
            }.orShowIf(inntektType.equalTo("ikke_red")) {
                text(
                    bokmal { + "Inntekt som kan trekkes fra" },
                    nynorsk { + "Inntekt som kan bli trekt frå" },
                )
            }
        }
    }

    data class PE_UT_Etteroppgjor_DetaljEPS_InntektListe_RegisterKildeKode(
        val registerkilde: Expression<String?>,
    ) : TextOnlyPhrase<LangBokmalNynorsk>() {
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(registerkilde.equalTo("a_ordning")) {
                text(
                    bokmal { + "Elektronisk innmeldt fra arbeidsgiver" },
                    nynorsk { + "Elektronisk innmeld frå arbeidsgivar" },
                )
            }.orShowIf(registerkilde.equalTo("skd")) {
                text(
                    bokmal { + "Oppgitt av Skatteetaten" },
                    nynorsk { + "Opplyst av Skatteetaten" },
                )
            }.orShow {
                text(
                    bokmal { + "Oppgitt av deg" },
                    nynorsk { + "Opplyst av deg" },
                )
            }
        }

    }

    data class PE_UT_Etteroppgjor_DetaljBruker_FratrekkListe_InntektTypeKode(
        val inntektsgrunnlag: Expression<Inntektsgrunnlag>,
    ) : TextOnlyPhrase<LangBokmalNynorsk>() {
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(inntektsgrunnlag.inntekttype_safe.equalTo("rap_arb")) {
                text(
                    bokmal { + "Arbeidsinntekt" },
                    nynorsk { + "Arbeidsinntekt" },
                )
            }.orShowIf(inntektsgrunnlag.inntekttype_safe.equalTo("forintutl")) {
                text(
                    bokmal { + "Utlandsinntekt" },
                    nynorsk { + "Utanlandsinntekt" },
                )
            }.orShowIf(inntektsgrunnlag.inntekttype_safe.equalTo("rap_nar")) {
                text(
                    bokmal { + "Næringsinntekt" },
                    nynorsk { + "Næringsinntekt" },
                )
            }.orShowIf(inntektsgrunnlag.inntekttype_safe.equalTo("rap_and")) {
                text(
                    bokmal { + "Pensjoner fra andre enn Nav" },
                    nynorsk { + "Pensjon frå andre enn Nav" },
                )
            }.orShowIf(inntektsgrunnlag.inntekttype_safe.equalTo("forpenutl")) {
                text(
                    bokmal { + "Pensjon fra utlandet" },
                    nynorsk { + "Pensjon frå utlandet" },
                )
            }.orShowIf(inntektsgrunnlag.inntekttype_safe.equalTo("ikke_red")) {
                showIf(inntektsgrunnlag.grunnikkereduksjon_safe.equalTo("etterbetaling")) {
                    text(
                        bokmal { + "Inntekt" },
                        nynorsk { + "Inntekt" },
                    )
                }.orShow {
                    text(
                        bokmal { + "Inntekt som kan trekkes fra" },
                        nynorsk { + "Inntekt som kan bli trekt frå" },
                    )
                }
            }
        }
    }

    data class PE_UT_Etteroppgjor_DetaljBruker_FratrekkListe_RegisterKildeKode(
        val inntektsgrunnlag: Expression<Inntektsgrunnlag>,
    ) : TextOnlyPhrase<LangBokmalNynorsk>() {
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(inntektsgrunnlag.registerkilde_safe.equalTo("a_ordning")) {
                text(
                    bokmal { + "Elektronisk innmeldt fra arbeidsgiver" },
                    nynorsk { +  "Elektronisk innmeld frå arbeidsgivar" },
                )
            }.orShowIf(inntektsgrunnlag.registerkilde_safe.equalTo("skd")) {
                text(
                    bokmal { +  "Oppgitt av Skatteetaten" },
                    nynorsk { +  "Opplyst av Skatteetaten" },
                )
            }.orShowIf(inntektsgrunnlag.grunnikkereduksjon_safe.equalTo("etterbetaling")) {
                text(
                    bokmal { +  "Nav" },
                    nynorsk { +  "Nav" },
                )
            }.orShow {
                text(
                    bokmal { +  "Oppgitt av deg" },
                    nynorsk { +  "Opplyst av deg" },
                )
            }
        }
    }

    data class PE_UT_Etteroppgjor_DetaljBruker_FratrekkListe_GrunnIkkeReduksjonKode(
        val grunnIkkeReduksjonKode: Expression<String?>,
    ) : TextOnlyPhrase<LangBokmalNynorsk>() {
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(grunnIkkeReduksjonKode.equalTo("opptjent_for_innv_ut")) {
                text(
                    bokmal { +  "Inntekt før uføretrygden ble innvilget" },
                    nynorsk { +  "Inntekt før du fekk innvilga uføretrygd" },
                )
            }
            showIf(grunnIkkeReduksjonKode.equalTo("opptjent_etter_innv_ut")) {
                text(
                    bokmal { +  "Inntekt etter at uføretrygden opphørte" },
                    nynorsk { +  "Inntekt etter at uføretrygda di tok slutt" },
                )
            }
            showIf(grunnIkkeReduksjonKode.equalTo("erstatning_innttap_erstoppgj")) {
                text(
                    bokmal { +  "Erstatning for inntektstap ved erstatningsoppgjør" },
                    nynorsk { +  "Erstatning for inntektstap ved erstatningsoppgjer" },
                )
            }
            showIf(grunnIkkeReduksjonKode.equalTo("etterslepsinnt_avslt_akt")) {
                text(
                    bokmal { +  "Inntekt fra helt avsluttet arbeid eller virksomhet" },
                    nynorsk { +  "Inntekt frå heilt avslutta arbeid eller verksemd" },
                )
            }
            showIf(grunnIkkeReduksjonKode.equalTo("ikke_red_annet")) {
                text(
                    bokmal { +  "Annet" },
                    nynorsk { +  "Anna" },
                )
            }
            showIf(grunnIkkeReduksjonKode.equalTo("etterbetaling")) {
                text(
                    bokmal { +  "Etterbetaling fra Nav" },
                    nynorsk { +  "Etterbetaling frå Nav" },
                )
            }
        }
    }

    data class PE_UT_Etteroppgjor_DetaljEPS_FratrekkListe_InntektTypeKode(
        val inntektstypeKode: Expression<String?>
    ) : TextOnlyPhrase<LangBokmalNynorsk>() {
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(inntektstypeKode.equalTo("rap_arb")) {
                text(
                    bokmal { +  "Arbeidsinntekt" },
                    nynorsk { +  "Arbeidsinntekt" },
                )
            }.orShowIf(inntektstypeKode.equalTo("forintutl")) {
                text(
                    bokmal { +  "Utlandsinntekt" },
                    nynorsk { +  "Utanlandsinntekt" },
                )
            }.orShowIf(inntektstypeKode.equalTo("rap_nar")) {
                text(
                    bokmal { +  "Næringsinntekt" },
                    nynorsk { +  "Næringsinntekt" },
                )
            }.orShowIf(inntektstypeKode.equalTo("rap_and")) {
                text(
                    bokmal { +  "Pensjoner fra andre enn Nav" },
                    nynorsk { +  "Pensjon frå andre enn Nav" },
                )
            }.orShowIf(inntektstypeKode.equalTo("forpenutl")) {
                text(
                    bokmal { +  "Pensjon fra utlandet" },
                    nynorsk { +  "Pensjon frå utlandet" },
                )
            }.orShowIf(inntektstypeKode.equalTo("ikke_red")) {
                text(
                    bokmal { +  "Inntekt som kan trekkes fra" },
                    nynorsk { +  "Inntekt som kan bli trekt frå" },
                )
            }
        }
    }

    data class PE_UT_Etteroppgjor_DetaljEPS_FratrekkListe_GrunnIkkeReduksjonKode(
        val grunnIkkeReduksjonKode: Expression<String?>
    ) : TextOnlyPhrase<LangBokmalNynorsk>() {
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(grunnIkkeReduksjonKode.equalTo("opptjent_for_innv_ut")) {
                text(
                    bokmal { +  "Inntekt før uføretrygden ble innvilget" },
                    nynorsk { +  "Inntekt før du fekk innvilga uføretrygd" },
                )
            }.orShowIf(grunnIkkeReduksjonKode.equalTo("opptjent_etter_innv_ut")) {
                text(
                    bokmal { +  "Inntekt etter at uføretrygden opphørte" },
                    nynorsk { +  "Inntekt etter at uføretrygda di tok slutt" },
                )
            }.orShowIf(grunnIkkeReduksjonKode.equalTo("erstatning_innttap_erstoppgj")) {
                text(
                    bokmal { +  "Erstatning for inntektstap ved erstatningsoppgjør" },
                    nynorsk { +  "Erstatning for inntektstap ved erstatningsoppgjer" },
                )
            }.orShowIf(grunnIkkeReduksjonKode.equalTo("etterslepsinnt_avslt_akt")) {
                text(
                    bokmal { +  "Inntekt fra helt avsluttet arbeid eller virksomhet" },
                    nynorsk { +  "Inntekt frå heilt avslutta arbeid eller verksemd" },
                )
            }.orShowIf(grunnIkkeReduksjonKode.equalTo("ikke_red_annet")) {
                text(
                    bokmal { +  "Annet" },
                    nynorsk { +  "Anna" },
                )
            }
        }

    }

    data class PE_UT_Etteroppgjor_DetaljBruker_InntektListe_InntektTypeKode(
        val inntektTypeKode: Expression<String?>
    ) : TextOnlyPhrase<LangBokmalNynorsk>() {
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(inntektTypeKode.equalTo("rap_arb")) {
                text(
                    bokmal { +  "Arbeidsinntekt" },
                    nynorsk { +  "Arbeidsinntekt" },
                )
            }.orShowIf(inntektTypeKode.equalTo("forintutl")) {
                text(
                    bokmal { +  "Utlandsinntekt" },
                    nynorsk { +  "Utanlandsinntekt" },
                )
            }.orShowIf(inntektTypeKode.equalTo("rap_nar")) {
                text(
                    bokmal { +  "Næringsinntekt" },
                    nynorsk { +  "Næringsinntekt" },
                )
            }.orShowIf(inntektTypeKode.equalTo("rap_and")) {
                text(
                    bokmal { +  "Pensjoner fra andre enn Nav" },
                    nynorsk { +  "Pensjon frå andre enn Nav" },
                )
            }.orShowIf(inntektTypeKode.equalTo("forpenutl")) {
                text(
                    bokmal { +  "Pensjon fra utlandet" },
                    nynorsk { +  "Pensjon frå utlandet" },
                )
            }
        }
    }

    data class PE_UT_Etteroppgjor_DetaljEPS_FratrekkListe_RegisterKildeKode(
        val registerkilde: Expression<String?>,
    ) : TextOnlyPhrase<LangBokmalNynorsk>() {
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(registerkilde.equalTo("a_ordning")) {
                text(
                    bokmal { +  "Elektronisk innmeldt fra arbeidsgiver" },
                    nynorsk { +  "Elektronisk innmeld frå arbeidsgivar" },
                )
            }.orShowIf(registerkilde.equalTo("skd")) {
                text(
                    bokmal { +  "Oppgitt av Skatteetaten" },
                    nynorsk { +  "Opplyst av Skatteetaten" },
                )
            }.orShow {
                text(
                    bokmal { +  "Oppgitt av deg" },
                    nynorsk { +  "Opplyst av deg" },
                )
            }
        }
    }

    data class PE_UT_Etteroppgjor_DetaljBruker_InntektListe_RegisterKildeKode(
        val registerkilde: Expression<String?>
    ) : TextOnlyPhrase<LangBokmalNynorsk>() {
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(registerkilde.equalTo("a_ordning")) {
                text(
                    bokmal { +  "Elektronisk innmeldt fra arbeidsgiver" },
                    nynorsk { +  "Elektronisk innmeld frå arbeidsgivar" },
                )
            }.orShowIf(registerkilde.equalTo("skd")) {
                text(
                    bokmal { +  "Oppgitt av Skatteetaten" },
                    nynorsk { +  "Opplyst av Skatteetaten" },
                )
            }.orShow {
                text(
                    bokmal { +  "Oppgitt av deg" },
                    nynorsk { +  "Opplyst av deg" },
                )
            }
        }
    }

    data class TabellTrukketFraInntekt(val pe: Expression<PE>) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(
                (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop()
                    .notEqualTo(0)) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag()
                    .isNotEmpty()
            ) {
                title1 {
                    text(
                        bokmal { +  "Beløp som er trukket fra inntekten din" },
                        nynorsk { +  "Beløp som er trekt frå inntekta di" },
                    )
                }

                paragraph {
                    table(header = {
                        column {
                            text(
                                bokmal { +  "Inntektstype" },
                                nynorsk { +  "Inntektstype" },
                            )
                        }
                        column {
                            text(
                                bokmal { +  "Årsak til at inntekt er trukket fra" },
                                nynorsk { +  "Årsak til at inntekt er trekt frå" },
                            )
                        }
                        column {
                            text(
                                bokmal { +  "Mottatt av" },
                                nynorsk { +  "Motteken av" },
                            )
                        }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +  "Beløp" },
                                nynorsk { +  "Beløp" },
                            )
                        }
                    }) {
                        forEach(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag()) { inntektsgrunnlag ->

                            //IF( ( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'ikke_red' ) ) THEN      INCLUDE ENDIF
                            showIf(
                                ((inntektsgrunnlag.inntekttype_safe.equalTo("rap_nar") or inntektsgrunnlag.inntekttype_safe.equalTo("forintutl") or inntektsgrunnlag.inntekttype_safe.equalTo(
                                    "rap_arb"
                                ) or inntektsgrunnlag.inntekttype_safe.equalTo("ikke_red")))
                            ) {
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
                                        text(
                                            bokmal { +  inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                            nynorsk { +  inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                        )
                                    }
                                }
                            }
                        }
                        row {
                            cell {
                                text(
                                    bokmal { +  "Inntekt trukket fra pensjonsgivende inntekt" },
                                    nynorsk { +  "Inntekt trekt frå pensjonsgivende inntekt" },
                                    BOLD
                                )
                            }
                            cell {

                            }
                            cell {

                            }
                            cell {
                                text(
                                    bokmal { +  pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut()
                                        .format(false) + " kr" },
                                    nynorsk { +  pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut()
                                        .format(false) + " kr" },
                                    BOLD
                                )
                            }
                        }

                        forEach(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag()) { inntektsgrunnlag ->

                            //IF((PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) AND( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_and' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forpenutl'  ) ) THEN      INCLUDE ENDIF
                            showIf(
                                ((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                                    .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb()
                                    .notEqualTo(0)) and (inntektsgrunnlag.inntekttype_safe.equalTo("rap_and") or inntektsgrunnlag.inntekttype_safe.equalTo(
                                    "forpenutl"
                                )))
                            ) {
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
                                        text(
                                            bokmal { +  inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                            nynorsk { +  inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                        )
                                    }
                                }
                            }
                        }
                        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
                        showIf(
                            (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                                .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
                        ) {
                            //[Table 3, Table 4, Table 5]
                            row {
                                cell {
                                    text(
                                        bokmal { +  "Inntekt trukket fra personinntekt" },
                                        nynorsk { +  "Inntekt trekt frå personinntekt" },
                                        BOLD
                                    )
                                }
                                cell {}
                                cell {}
                                cell {
                                    text(
                                        bokmal { +  pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkbt()
                                            .format(false) + " kr" },
                                        nynorsk { +  pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkbt()
                                            .format(false) + " kr" },
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
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_Avviksbelop <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().notEqualTo(0))) {
                //[Table 7, Table 8, Table 6]

                title1 {
                    text(
                        bokmal { +  "Oversikt over om du har fått for mye eller for lite i " },
                        nynorsk { +  "Oversikt over om du har fått for mykje eller for lite i " },
                    )

                    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))) {
                        text(
                            bokmal { +  "uføretrygd" },
                            nynorsk { +  "uføretrygd" },
                        )
                    }

                    //IF (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0    AND (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0    OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0)    ) THEN    INCLUDE ENDIF
                    showIf(
                        (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput()
                            .notEqualTo(0) and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                            .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))
                    ) {
                        text(
                            bokmal { +  " og " },
                            nynorsk { +  " og " },
                        )
                    }

                    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
                    showIf(
                        (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                            .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
                    ) {
                        text(
                            bokmal { +  "barnetillegg" },
                            nynorsk { +  "barnetillegg" },
                        )
                    }
                }

                paragraph {
                    table(header = {
                        column(columnSpan = 5) {
                            text(
                                bokmal { +  "Type stønad" },
                                nynorsk { +  "Type stønad" },
                            )
                        }
                        column(alignment = RIGHT, columnSpan = 6) {
                            text(
                                bokmal { +  "Dette skulle du ha fått ut fra inntektsopplysninger fra Skatteetaten" },
                                nynorsk { +  "Dette skulle du ha fått ut frå inntektsopplysningar frå Skatteetaten" },
                            )
                        }
                        column(alignment = RIGHT, columnSpan = 4) {
                            text(
                                bokmal { +  "Dette fikk du i " },
                                nynorsk { +  "Dette fekk du i " },
                            )

                            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
                            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))) {
                                text(
                                    bokmal { +  "uføretrygd" },
                                    nynorsk { +  "uføretrygd" },
                                )
                            }

                            //IF (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0    AND    (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0    OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0    )    ) THEN    INCLUDE ENDIF
                            showIf(
                                (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput()
                                    .notEqualTo(0) and (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                                    .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))
                            ) {
                                text(
                                    bokmal { +  " og " },
                                    nynorsk { +  " og " },
                                )
                            }

                            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
                            showIf(
                                (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                                    .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
                            ) {
                                text(
                                    bokmal { +  "barnetillegg" },
                                    nynorsk { +  "barnetillegg" },
                                )
                            }
                        }
                        column(alignment = RIGHT, columnSpan = 4) {
                            text(
                                bokmal { +  "Beløp du har fått for mye eller for lite" },
                                nynorsk { +  "Beløp du har fått for mykje eller for lite" },
                            )
                        }
                    }) {
                        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))) {
                            row {
                                cell {
                                    text(
                                        bokmal { +  "Uføretrygd" },
                                        nynorsk { +  "Uføretrygd" },
                                    )

                                    //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                                    showIf(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()) {
                                        text(
                                            bokmal { +  " og gjenlevendetillegg" },
                                            nynorsk { +  " og attlevandetillegg" },
                                        )
                                    }
                                }
                                cell {
                                    text(
                                        bokmal { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format(false) + " kr" },
                                        nynorsk { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format(false) + " kr" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloput().format(false) + " kr" },
                                        nynorsk { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloput().format(false) + " kr" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().format(false) + " kr" },
                                        nynorsk { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().format(false) + " kr" },
                                    )
                                }
                            }

                        }
                        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))) {
                            row {
                                cell {
                                    text(
                                        bokmal { +  "Barnetillegg fellesbarn" },
                                        nynorsk { +  "Barnetillegg fellesbarn" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptfb().format(false) + " kr" },
                                        nynorsk { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptfb().format(false) + " kr" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptfb().format(false) + " kr" },
                                        nynorsk { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptfb().format(false) + " kr" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().format(false) + " kr" },
                                        nynorsk { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().format(false) + " kr" },
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
                                        bokmal { +  "Barnetillegg særkullsbarn" },
                                        nynorsk { +  "Barnetillegg særkullsbarn" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptsb().format(false) + " kr" },
                                        nynorsk { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptsb().format(false) + " kr" },
                                    )

                                }
                                cell {
                                    text(
                                        bokmal { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptsb().format(false) + " kr" },
                                        nynorsk { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptsb().format(false) + " kr" },
                                    )

                                }
                                cell {
                                    text(
                                        bokmal { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().format(false) + " kr" },
                                        nynorsk { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().format(false) + " kr" },
                                    )

                                }
                            }
                        }
                        //PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"
                        showIf(pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr")) {
                            //[Table 6, Table 7, Table 5]

                            row {
                                cell {
                                    text(
                                        bokmal { +  "Beløpet du har fått for mye i " + pe.ut_uforetrygdetteroppgjor_periodefom_year()
                                            .format() + ":" },
                                        nynorsk { +  "Beløpet du har fått for mykje i " + pe.ut_uforetrygdetteroppgjor_periodefom_year()
                                            .format() + ":" },
                                        BOLD
                                    )
                                }
                                cell { }
                                cell { }
                                cell {
                                    text(
                                        bokmal { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format(false) + " kr" },
                                        nynorsk { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format(false) + " kr" },
                                        BOLD
                                    )
                                }
                            }
                        }

                        //PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "etterbet"
                        showIf(pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("etterbet")) {
                            //[Table 6, Table 7, Table 5]

                            row {
                                cell {
                                    text(
                                        bokmal { +  "Beløpet du har fått for lite i " + pe.ut_uforetrygdetteroppgjor_periodefom_year()
                                            .format() + ":" },
                                        nynorsk { +  "Beløpet du har fått for lite i " + pe.ut_uforetrygdetteroppgjor_periodefom_year()
                                            .format() + ":" },
                                        BOLD
                                    )
                                }
                                cell { }
                                cell { }
                                cell {
                                    text(
                                        bokmal { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format(false) + " kr" },
                                        nynorsk { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbelop().format(false) + " kr" },
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
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt <> 0) THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb()
                    .notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt()
                    .notEqualTo(0))
            ) {
                //[TBU705V_Overskrift, Text 4]
                title1 {
                    text(
                        bokmal { +  "Beløp som er trukket fra inntekten til annen forelder" },
                        nynorsk { +  "Beløp som er trekt frå inntekta til den andre forelderen" },
                    )
                }
                paragraph {
                    table(header = {
                        column(columnSpan = 3) {
                            text(
                                bokmal { +  "Fradragstype" },
                                nynorsk { +  "Frådragstype" },
                            )
                        }
                        column(columnSpan = 5) {
                            text(
                                bokmal { +  "Årsak til at inntekt er trukket fra" },
                                nynorsk { +  "Årsak til at inntekt er trekt frå" },
                            )
                        }
                        column(columnSpan = 2) {
                            text(
                                bokmal { +  "Mottatt av" },
                                nynorsk { +  "Motteken av" },
                            )
                        }
                        column(columnSpan = 2, alignment = RIGHT) {
                            text(
                                bokmal { +  "Beløp" },
                                nynorsk { +  "Beløp" },
                            )
                        }
                    }) {
                        forEach(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_fratrekkliste_inntektsgrunnlag()) { inntektsgrunnlag ->
                            //IF(( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_and' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'forpenutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_FratrekkListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'ikke_red' ) ) THEN      INCLUDE ENDIF
                            showIf(
                                ((inntektsgrunnlag.inntekttype_safe.equalTo("rap_nar")
                                        or inntektsgrunnlag.inntekttype_safe.equalTo("forintutl")
                                        or inntektsgrunnlag.inntekttype_safe.equalTo("rap_arb")
                                        or inntektsgrunnlag.inntekttype_safe.equalTo("rap_and")
                                        or inntektsgrunnlag.inntekttype_safe.equalTo("forpenutl")
                                        or inntektsgrunnlag.inntekttype_safe.equalTo("ikke_red")))
                            ) {
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
                                        text(
                                            bokmal { +  inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                            nynorsk { +  inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                        )
                                    }
                                }
                            }
                        }

                        row {
                            cell {
                                text(
                                    bokmal { +  "Inntekt" },
                                    nynorsk { +  "Inntekt" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +  "Inntekt inntil ett grunnbeløp" },
                                    nynorsk { +  "Inntekt inntil eit grunnbeløp" },
                                )
                            }
                            cell {}
                            cell {
                                text(
                                    bokmal { +  pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt()
                                        .format(false) + " kr" },
                                    nynorsk { +  pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt()
                                        .format(false) + " kr" },
                                )
                            }
                        }

                        row {
                            cell {
                                text(
                                    bokmal { +  "Inntekt trukket fra personinntekt" },
                                    nynorsk { +  "Inntekt trekt frå personinntekt" },
                                    BOLD
                                )
                            }
                            cell { }
                            cell { }
                            cell {
                                text(
                                    bokmal { +  pe.ut_inntekt_trukket_fra_personinntekt().format(false) + " kr" },
                                    nynorsk { +  pe.ut_inntekt_trukket_fra_personinntekt().format(false) + " kr" },
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
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0 ) AND Count(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType) <> 0  THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_inntektliste_inntektsgrunnlag().isNotEmpty()) {
                //[Text 3, Text 4, Text 2]
                title1 {
                    text(
                        bokmal { +  "Inntekten til annen forelder" },
                        nynorsk { +  "Inntekta til den andre forelderen" },
                    )
                }

                paragraph {
                    table(header = {
                        column(columnSpan = 3) {
                            text(
                                bokmal { +  "Inntektstyper" },
                                nynorsk { +  "Inntektstypar" },
                            )
                        }
                        column(columnSpan = 3) {
                            text(
                                bokmal { +  "Mottatt av" },
                                nynorsk { +  "Motteken av" },
                            )
                        }
                        column(columnSpan = 2, alignment = RIGHT) {
                            text(
                                bokmal { +  "Registrert inntekt" },
                                nynorsk { +  "Registrert inntekt" },
                            )
                        }
                    }) {

                        forEach(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_inntektliste_inntektsgrunnlag()) { inntektsGrunnlag ->
                            //IF(( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_and' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'forpenutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'ikke_red' ) ) THEN      INCLUDE ENDIF
                            showIf(
                                ((inntektsGrunnlag.inntekttype_safe.equalTo("rap_nar")
                                        or inntektsGrunnlag.inntekttype_safe.equalTo("forintutl")
                                        or inntektsGrunnlag.inntekttype_safe.equalTo("rap_arb")
                                        or inntektsGrunnlag.inntekttype_safe.equalTo("rap_and")
                                        or inntektsGrunnlag.inntekttype_safe.equalTo("forpenutl")
                                        or inntektsGrunnlag.inntekttype_safe.equalTo("ikke_red")))
                            ) {
                                //[Table 10, Table 12]

                                row {
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljEPS_InntektListe_InntektTypeKode(inntektsGrunnlag.inntekttype_safe))
                                    }
                                    cell {
                                        includePhrase(PE_UT_Etteroppgjor_DetaljEPS_InntektListe_RegisterKildeKode(inntektsGrunnlag.registerkilde_safe))
                                    }
                                    cell {
                                        text(
                                            bokmal { +  inntektsGrunnlag.belop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                            nynorsk { +  inntektsGrunnlag.belop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                        )
                                    }
                                }
                            }
                        }

                        row {
                            cell {
                                text(
                                    bokmal { +  "Sum personinntekt" },
                                    nynorsk { +  "Sum personinntekt" },
                                    BOLD
                                )
                            }
                            cell { }
                            cell {
                                text(
                                    bokmal { +  pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_suminntekterbt()
                                        .format(false) + " kr" },
                                    nynorsk { +  pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_suminntekterbt()
                                        .format(false) + " kr" },
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
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            //[Text 2, Text 3, Text 4]
            //IF((PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0  OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0  OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0))  THEN      INCLUDE ENDIF
            showIf(
                ((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput()
                    .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                    .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)))
            ) {
                title1 {
                    text(
                        bokmal { +  "Inntekten din" },
                        nynorsk { +  "Inntekta di" },
                    )
                }
            }


            //IF((PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0  OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0  OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0))  THEN      INCLUDE ENDIF
            showIf(
                pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput()
                    .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                    .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)
            ) {
                //[Table 3, Table 4, Table 5]
                paragraph {
                    table(
                        header = {
                            column(columnSpan = 3) {
                                text(
                                    bokmal { +  "Inntektstyper" },
                                    nynorsk { +  "Inntektstypar" },
                                )
                            }

                            column(columnSpan = 3) {
                                text(
                                    bokmal { +  "Mottatt av" },
                                    nynorsk { +  "Motteken av" },
                                )
                            }
                            column(columnSpan = 2, alignment = RIGHT) {
                                text(
                                    bokmal { +  "Registrert inntekt" },
                                    nynorsk { +  "Registrert inntekt" },
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
                                        text(
                                            bokmal { +  inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                            nynorsk { +  inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                        )
                                    }
                                }
                            }
                        }

                        //PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumInntekterUT = 0
                        showIf(
                            pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut()
                                .equalTo(0)
                        ) {
                            //[Table 3, Table 4, Table 5]

                            row {
                                cell {}
                                cell {}
                                cell {
                                    text(
                                        bokmal { +  "Skatteetaten opplyser at du ikke hadde pensjonsgivende inntekt" },
                                        nynorsk { +  "Skatteetaten opplyser at du ikkje hadde pensjonsgivande inntekt" },
                                    )
                                }
                            }
                        }
                        row {
                            cell {
                                text(
                                    bokmal { +  "Sum pensjonsgivende inntekt" },
                                    nynorsk { +  "Sum pensjonsgivande inntekt" },
                                    BOLD
                                )
                            }
                            cell {}
                            cell {
                                text(
                                    bokmal { +  pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut()
                                        .format(false) + " kr" },
                                    nynorsk { +  pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut()
                                        .format(false) + " kr" },
                                    BOLD
                                )
                            }
                        }

                        forEach(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_inntektliste_inntektsgrunnlag()) { inntektsgrunnlag ->
                            //IF( ( PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'rap_nar' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) = 'forintutl' OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_InntektListe_Inntektsgrunnlag_InntektType(SYS_TableRow) =  'rap_arb' ) ) THEN      INCLUDE ENDIF
                            showIf(
                                (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0)
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
                                        text(
                                            bokmal { +  inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                            nynorsk { +  inntektsgrunnlag.belop_safe.ifNull(Kroner(0)).format(false) + " kr" },
                                        )
                                    }
                                }
                            }
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
                        showIf(
                            (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                                .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
                        ) {
                            //[Table 3, Table 4, Table 5]

                            row {
                                cell {
                                    text(
                                        bokmal { +  "Uføretrygd" },
                                        nynorsk { +  "Uføretrygd" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +  "Beregnet av Nav" },
                                        nynorsk { +  "Berekna av Nav" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format(false) + " kr" },
                                        nynorsk { +  pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format(false) + " kr" },
                                    )
                                }
                            }
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
                        showIf(
                            (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                                .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
                        ) {
                            //[Table 3, Table 4, Table 5]

                            row {
                                cell {
                                    text(
                                        bokmal { +  "Sum personinntekt" },
                                        nynorsk { +  "Sum personinntekt" },
                                        BOLD
                                    )
                                }
                                cell {}
                                cell {
                                    text(
                                        bokmal { +  pe.ut_sum_inntekterbt_totalbeloput().format(false) + " kr" },
                                        nynorsk { +  pe.ut_sum_inntekterbt_totalbeloput().format(false) + " kr" },
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
