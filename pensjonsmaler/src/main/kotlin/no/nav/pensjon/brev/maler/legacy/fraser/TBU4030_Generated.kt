package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text


data class TBU4030_Generated(
    val pe: Expression<PE>
): OutlinePhrase<LangBokmalNynorsk>(){
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {

            //IF(PE_UT_AvvikBTIkkeUT() = true) THEN      INCLUDE ENDIF
            showIf((pe.ut_avvikbtikkeut())){
                text (
                    bokmal { + "Personinntekten din har betydning for størrelsen på barnetillegget" },
                    nynorsk { + "Personinntekta di har betydning for storleiken på barnetillegget" },
                )
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0     AND PE_UT_AvvikBTIkkeUT()) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0) and pe.ut_avvikbtikkeut())){
                text (
                    bokmal { + " for barn som ikke bor sammen med begge foreldrene" },
                    nynorsk { + " for barn som ikkje bur saman med begge foreldra sine" },
                )
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB = 0 AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0      AND PE_UT_AvvikBTIkkeUT()) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().equalTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) and pe.ut_avvikbtikkeut())){
                text (
                    bokmal { + " ditt" },
                    nynorsk { + "" },
                )
            }

            //IF(PE_UT_AvvikBTIkkeUT() = true) THEN      INCLUDE ENDIF
            showIf((pe.ut_avvikbtikkeut())){
                text (
                    bokmal { + ". " + "Uføretrygden blir regnet med som personinntekt. " },
                    nynorsk { + ". " + "Uføretrygda blir rekna med som personinntekt. " },
                )
            }

            //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0))){
                text (
                    bokmal { + "Vi har mottatt inntektsopplysninger for deg. " },
                    nynorsk { + "Vi har fått inntektsopplysningar for deg. " },
                )
            }

            //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0 AND PE_UT_PeriodeFomStorre0101PeriodeTomLik3112() = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0) and pe.ut_periodefomstorre0101periodetomlik3112())){
                text (
                    bokmal { + "Inntekt du hadde før du ble innvilget uføretrygd trekkes fra dette beløpet. " },
                    nynorsk { + "Inntekt du hadde før du fekk innvilga uføretrygd, blir trekt frå dette beløpet. " },
                )
            }

            //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0 AND PE_UT_PeriodeTomMindre3112PeriodeFomLik0101() = true) THEN      INCLUDE ENDIF
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

            //IF(Contains(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon, "erstatning_innttap_erstoppgj") > 0) THEN      INCLUDE ENDIF
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
            text (
                bokmal { + "Opplysninger om inntekten din viser at du skulle ha fått " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptsb().format() + " i barnetillegg i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Du fikk imidlertid " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptsb().format() + ". Du har derfor fått " + pe.ut_avviksbeloptsbutenminus().format() + " for " },
                nynorsk { + "Opplysningane om inntekta di viser at du skulle ha fått " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptsb().format() + " i barnetillegg i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Du fekk derimot " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptsb().format() + ". Du har derfor fått " + pe.ut_avviksbeloptsbutenminus().format() + " for " },
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB > 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().greaterThan(0))){
                text (
                    bokmal { + "lite" },
                    nynorsk { + "lite" },
                )
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB < 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().lessThan(0))){
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
                    bokmal { + " for barn som ikke bor med begge foreldre" },
                    nynorsk { + " for barn som ikkje bur med begge foreldra" },
                )
            }
            text (
                bokmal { + ". " },
                nynorsk { + ". " },
            )
        }

    }

}