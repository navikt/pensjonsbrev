package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr


data class TBU4030_Generated(
    val pe: Expression<PE>
): OutlinePhrase<LangBokmalNynorsk>(){
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {

            //IF(PE_UT_AvvikBTIkkeUT() = true) THEN      INCLUDE ENDIF
            showIf((pe.ut_avvikbtikkeut())){
                text (
                    Bokmal to "Personinntekten din har betydning for størrelsen på barnetillegget",
                    Nynorsk to "Personinntekta di har betydning for storleiken på barnetillegget",
                )
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0     AND PE_UT_AvvikBTIkkeUT()) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0) and pe.ut_avvikbtikkeut())){
                text (
                    Bokmal to " for barn som ikke bor sammen med begge foreldrene",
                    Nynorsk to " for barn som ikkje bur saman med begge foreldra sine",
                )
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB = 0 AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0      AND PE_UT_AvvikBTIkkeUT()) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().equalTo(0) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) and pe.ut_avvikbtikkeut())){
                text (
                    Bokmal to " ditt",
                    Nynorsk to "",
                )
            }

            //IF(PE_UT_AvvikBTIkkeUT() = true) THEN      INCLUDE ENDIF
            showIf((pe.ut_avvikbtikkeut())){
                text (
                    Bokmal to ". " + "Uføretrygden blir regnet med som personinntekt. ",
                    Nynorsk to ". " + "Uføretrygda blir rekna med som personinntekt. ",
                )
            }

            //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0))){
                text (
                    Bokmal to "Vi har mottatt inntektsopplysninger for deg. ",
                    Nynorsk to "Vi har fått inntektsopplysningar for deg. ",
                )
            }

            //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0 AND PE_UT_PeriodeFomStorre0101PeriodeTomLik3112() = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0) and pe.ut_periodefomstorre0101periodetomlik3112())){
                text (
                    Bokmal to "Inntekt du hadde før du ble innvilget uføretrygd trekkes fra dette beløpet. ",
                    Nynorsk to "Inntekt du hadde før du fekk innvilga uføretrygd, blir trekt frå dette beløpet. ",
                )
            }

            //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0 AND PE_UT_PeriodeTomMindre3112PeriodeFomLik0101() = true) THEN      INCLUDE ENDIF
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

            //IF(Contains(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon, "erstatning_innttap_erstoppgj") > 0) THEN      INCLUDE ENDIF
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
                    Bokmal to "Etterbetaling du har fått fra NAV trekkes fra dette beløpet. ",
                    Nynorsk to "Etterbetalinga du fekk frå NAV blir trekt frå dette beløpet. ",
                )
            }
            textExpr (
                Bokmal to "Opplysninger om inntekten din viser at du skulle ha fått ".expr() + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptsb().format() + " kroner i barnetillegg i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Du fikk imidlertid " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptsb().format() + " kroner. Du har derfor fått " + pe.ut_avviksbeloptsbutenminus().format() + " kroner for ",
                Nynorsk to "Opplysningane om inntekta di viser at du skulle ha fått ".expr() + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloptsb().format() + " kroner i barnetillegg i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Du fekk derimot " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloptsb().format() + " kroner. Du har derfor fått " + pe.ut_avviksbeloptsbutenminus().format() + " kroner for ",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB > 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().greaterThan(0))){
                text (
                    Bokmal to "lite",
                    Nynorsk to "lite",
                )
            }

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB < 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().lessThan(0))){
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
                    Bokmal to " for barn som ikke bor med begge foreldre",
                    Nynorsk to " for barn som ikkje bur med begge foreldra",
                )
            }
            text (
                Bokmal to ". ",
                Nynorsk to ". ",
            )
        }

    }

}