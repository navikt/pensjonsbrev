package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
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
                        Bokmal to "Personinntekten til både deg og annen forelder har betydning for størrelsen på barnetillegget ",
                        Nynorsk to "Både personinntekta di og personinntekta til den andre forelderen har betydning for storleiken på barnetillegget",
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0    AND PE_UT_AvvikBTIkkeUT()) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0) and pe.ut_avvikbtikkeut())){
                    text (
                        Bokmal to "for barn som bor med begge sine foreldre",
                        Nynorsk to " for barn som bur saman med begge foreldra sine",
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB = 0 AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0    AND PE_UT_AvvikBTIkkeUT()) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().equalTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0) and pe.ut_avvikbtikkeut())){
                    text (
                        Bokmal to "ditt",
                        Nynorsk to "",
                    )
                }

                //IF(PE_UT_AvvikBTIkkeUT() = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_avvikbtikkeut())){
                    text (
                        Bokmal to ". Uføretrygden blir regnet med som personinntekt. ",
                        Nynorsk to ". Uføretrygda blir rekna med som personinntekt. ",
                    )
                }

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0))){
                    text (
                        Bokmal to "Vi har mottatt inntektsopplysninger for deg og annen forelder. ",
                        Nynorsk to "Vi har fått inntektsopplysningar for deg og for den andre forelderen. ",
                    )
                }

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0   AND PE_UT_PeriodeFomStorre0101PeriodeTomLik3112()) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0) and pe.ut_periodefomstorre0101periodetomlik3112())){
                    text (
                        Bokmal to "Inntekt du hadde før du ble innvilget uføretrygd trekkes fra dette beløpet. ",
                        Nynorsk to "Inntekt du hadde før du fekk innvilga uføretrygd, blir trekt frå dette beløpet. ",
                    )
                }

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0   AND PE_UT_PeriodeTomMindre3112PeriodeFomLik0101()) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0) and pe.ut_periodetommindre3112periodefomlik0101())){
                    text (
                        Bokmal to "Inntekt du hadde etter at uføretrygden din opphørte trekkes fra dette beløpet. ",
                        Nynorsk to "Inntekt du hadde etter at uføretrygda di tok slutt, blir trekt frå dette beløpet. ",
                    )
                }

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0   AND PE_UT_PeriodeTomMindre3112PeriodeFomStorre0101()) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0) and pe.ut_periodetommindre3112periodefomstorre0101())){
                    text (
                        Bokmal to "Inntekt du hadde før du ble innvilget uføretrygd og etter at uføretrygden din opphørte trekkes fra dette beløpet. ",
                        Nynorsk to "Inntekt du hadde før du fekk innvilga uføretrygd og etter at uføretrygda di tok slutt, blir trekt frå dette beløpet. ",
                    )
                }

                //IF(Contains(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon, "erstatning_innttap_erstoppgj") > 0 ) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_grunnikkereduksjon_har_erstatning_innttap_erstoppgj())){
                    text (
                        Bokmal to "Erstatning for inntektstap ved erstatningsoppgjør trekkes fra dette beløpet. ",
                        Nynorsk to "Erstatning for inntektstap ved erstatningsoppgjer blir trekt frå dette beløpet. ",
                    )
                }

                //IF(Contains(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon, "etterslepsinnt_avslt_akt") > 0 ) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_grunnikkereduksjon_har_etterslepsinnt_avslt_akt())){
                    text (
                        Bokmal to "Inntekt fra helt avsluttet arbeid eller virksomhet trekkes fra dette beløpet. ",
                        Nynorsk to "Inntekt frå heilt avslutta arbeid eller verksemd blir trekt frå dette beløpet. ",
                    )
                }

                //IF(PE_UT_EtteroppgjorFratrekkListeBrukerEtterbetaling("etterbetaling") = true) THEN      INCLUDE ENDIF
                showIf((pe.ut_etteroppgjorfratrekklistebrukeretterbetaling())){
                    text (
                        Bokmal to "Etterbetaling du har fått fra Nav trekkes fra dette beløpet. ",
                        Nynorsk to "Etterbetalinga du fekk frå Nav blir trekt frå dette beløpet. ",
                    )
                }

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true AND PE_UT_GrunnIkkeReduksjon_lik_erstatning_innttap_ertstoppgj_finnes = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and pe.ut_grunnikkereduksjon_lik_erstatning_innttap_ertstoppgj_finnes())){
                    text (
                        Bokmal to "Erstatning for inntektstap ved erstatningsoppgjør for annen forelder trekkes fra dette beløpet.",
                        Nynorsk to "Erstatning for inntektstap ved erstatningsoppgjer til den andre forelderen blir trekt frå dette beløpet.",
                    )
                }

                textExpr (
                    Bokmal to "Opplysningene om inntekt for deg og annen forelder viser at du skulle ha fått ".expr() + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptfb().format() + " kroner i barnetillegg i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Du fikk imidlertid " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptfb().format() + " kroner. Du har derfor fått " + pe.ut_avviksbeloptfbutenminus().format() + " kroner for ",
                    Nynorsk to "Opplysningane om inntekta til deg og den andre forelderen viser at du skulle ha fått ".expr() + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptfb().format() + " kroner i barnetillegg i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Du fekk derimot " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptfb().format() + " kroner. Du har derfor fått " + pe.ut_avviksbeloptfbutenminus().format() + " kroner for ",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB > 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().greaterThan(0))){
                    text (
                        Bokmal to "lite",
                        Nynorsk to "lite",
                    )
                }

                //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB < 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().lessThan(0))){
                    text (
                        Bokmal to "mye",
                        Nynorsk to "mykje",
                    )
                }
                text (
                    Bokmal to " i barnetillegg",
                    Nynorsk to " i barnetillegg",
                )

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())){
                    text (
                        Bokmal to " for barn som bor med begge foreldre",
                        Nynorsk to " for barnet/barna som bur saman med begge foreldra sine",
                    )
                }
                text (
                    Bokmal to ". ",
                    Nynorsk to ". ",
                )
            }
        }
    }

}

