package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

data class TBU4029_Generated(
    val pe: Expression<PE>
) : OutlinePhrase<LangBokmalNynorsk>(){
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))){
            //[TBU4029]
            paragraph {

                //IF(PE_UT_AvvikBTIkkeUT() = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_avvikbtikkeut())){
                    text (
                        bokmal { + "Personinntekten til både deg og annen forelder har betydning for størrelsen på barnetillegget " },
                        nynorsk { + "Både personinntekta di og personinntekta til den andre forelderen har betydning for storleiken på barnetillegget" },
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0    AND PE_UT_AvvikBTIkkeUT()) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0) and pe.ut_avvikbtikkeut())){
                    text (
                        bokmal { + "for barn som bor med begge sine foreldre" },
                        nynorsk { + " for barn som bur saman med begge foreldra sine" },
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB = 0 AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0    AND PE_UT_AvvikBTIkkeUT()) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().equalTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0) and pe.ut_avvikbtikkeut())){
                    text (
                        bokmal { + "ditt" },
                        nynorsk { + "" },
                    )
                }

                //IF(PE_UT_AvvikBTIkkeUT() = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_avvikbtikkeut())){
                    text (
                        bokmal { + ". Uføretrygden blir regnet med som personinntekt. " },
                        nynorsk { + ". Uføretrygda blir rekna med som personinntekt. " },
                    )
                }

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0))){
                    text (
                        bokmal { + "Vi har mottatt inntektsopplysninger for deg og annen forelder. " },
                        nynorsk { + "Vi har fått inntektsopplysningar for deg og for den andre forelderen. " },
                    )
                }

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0   AND PE_UT_PeriodeFomStorre0101PeriodeTomLik3112()) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0) and pe.ut_periodefomstorre0101periodetomlik3112())){
                    text (
                        bokmal { + "Inntekt du hadde før du ble innvilget uføretrygd trekkes fra dette beløpet. " },
                        nynorsk { + "Inntekt du hadde før du fekk innvilga uføretrygd, blir trekt frå dette beløpet. " },
                    )
                }

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0   AND PE_UT_PeriodeTomMindre3112PeriodeFomLik0101()) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0) and pe.ut_periodetommindre3112periodefomlik0101())){
                    text (
                        bokmal { + "Inntekt du hadde etter at uføretrygden din opphørte trekkes fra dette beløpet. " },
                        nynorsk { + "Inntekt du hadde etter at uføretrygda di tok slutt, blir trekt frå dette beløpet. " },
                    )
                }

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0   AND PE_UT_PeriodeTomMindre3112PeriodeFomStorre0101()) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0) and pe.ut_periodetommindre3112periodefomstorre0101())){
                    text (
                        bokmal { + "Inntekt du hadde før du ble innvilget uføretrygd og etter at uføretrygden din opphørte trekkes fra dette beløpet. " },
                        nynorsk { + "Inntekt du hadde før du fekk innvilga uføretrygd og etter at uføretrygda di tok slutt, blir trekt frå dette beløpet. " },
                    )
                }

                //IF(Contains(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon, "erstatning_innttap_erstoppgj") > 0 ) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_grunnikkereduksjon_har_erstatning_innttap_erstoppgj())){
                    text (
                        bokmal { + "Erstatning for inntektstap ved erstatningsoppgjør trekkes fra dette beløpet. " },
                        nynorsk { + "Erstatning for inntektstap ved erstatningsoppgjer blir trekt frå dette beløpet. " },
                    )
                }

                //IF(Contains(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon, "etterslepsinnt_avslt_akt") > 0 ) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_grunnikkereduksjon_har_etterslepsinnt_avslt_akt())){
                    text (
                        bokmal { + "Inntekt fra helt avsluttet arbeid eller virksomhet trekkes fra dette beløpet. " },
                        nynorsk { + "Inntekt frå heilt avslutta arbeid eller verksemd blir trekt frå dette beløpet. " },
                    )
                }

                //IF(PE_UT_EtteroppgjorFratrekkListeBrukerEtterbetaling("etterbetaling") = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_etteroppgjorfratrekklistebrukeretterbetaling())){
                    text (
                        bokmal { + "Etterbetaling du har fått fra Nav trekkes fra dette beløpet. " },
                        nynorsk { + "Etterbetalinga du fekk frå Nav blir trekt frå dette beløpet. " },
                    )
                }

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true AND PE_UT_GrunnIkkeReduksjon_lik_erstatning_innttap_ertstoppgj_finnes = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and pe.ut_grunnikkereduksjon_lik_erstatning_innttap_ertstoppgj_finnes())){
                    text (
                        bokmal { + "Erstatning for inntektstap ved erstatningsoppgjør for annen forelder trekkes fra dette beløpet." },
                        nynorsk { + "Erstatning for inntektstap ved erstatningsoppgjer til den andre forelderen blir trekt frå dette beløpet." },
                    )
                }

                text (
                    bokmal { + "Opplysningene om inntekt for deg og annen forelder viser at du skulle ha fått " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptfb().format() + " i barnetillegg i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Du fikk imidlertid " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptfb().format() + ". Du har derfor fått " + pe.ut_avviksbeloptfbutenminus().format() + " for " },
                    nynorsk { + "Opplysningane om inntekta til deg og den andre forelderen viser at du skulle ha fått " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptfb().format() + " i barnetillegg i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Du fekk derimot " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptfb().format() + ". Du har derfor fått " + pe.ut_avviksbeloptfbutenminus().format() + " for " },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB > 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().greaterThan(0))){
                    text (
                        bokmal { + "lite" },
                        nynorsk { + "lite" },
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB < 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().lessThan(0))){
                    text (
                        bokmal { + "mye" },
                        nynorsk { + "mykje" },
                    )
                }
                text (
                    bokmal { + " i barnetillegg" },
                    nynorsk { + " i barnetillegg" },
                )

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())){
                    text (
                        bokmal { + " for barn som bor med begge foreldre" },
                        nynorsk { + " for barnet/barna som bur saman med begge foreldra sine" },
                    )
                }
                text (
                    bokmal { + ". " },
                    nynorsk { + ". " },
                )
            }
        }
    }

}

