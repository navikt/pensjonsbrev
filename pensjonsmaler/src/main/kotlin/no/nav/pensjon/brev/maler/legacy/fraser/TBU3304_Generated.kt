package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU3304_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU3304_NN, TBU3304]

		paragraph {
			textExpr (
				Bokmal to "Skatteetaten har opplyst at du hadde en pensjonsgivende inntekt på ".expr() + pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut().format() + " kroner. ",
				Nynorsk to "Skatteetaten har opplyst at du hadde ei pensjonsgivande inntekt på ".expr() + pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut().format() + " kroner. ",
			)

			//IF(Contains(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon, "erstatning_innttap_erstoppgj") > 0) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_fratrekkliste_inntektsgrunnlag_grunnikkereduksjon_har_erstatning_innttap_erstoppgj())){
				text (
					Bokmal to "Erstatning for inntektstap ved erstatningsoppgjør trekkes fra dette beløpet. ",
					Nynorsk to "Erstatning for inntektstap ved erstatningsoppgjer blir trekt frå dette beløpet. ",
				)
			}

			//IF(Contains(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_FratrekkListe_Inntektsgrunnlag_GrunnIkkeReduksjon, "etterslepsinnt_avslt_akt") > 0) THEN      INCLUDE ENDIF
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
			textExpr (
				Bokmal to "Disse opplysningene viser at du skulle ha fått ".expr() + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " kroner i uføretrygd i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Du fikk imidlertid " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloput().format() + " kroner. Du har derfor fått " + pe.ut_avviksbelopututenminus().format() + " kroner for ",
				Nynorsk to "Desse opplysningane viser at du skulle ha fått ".expr() + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_totalbeloput().format() + " kroner i uføretrygd i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Du fekk derimot " + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_tidligerebeloput().format() + " kroner. Du har derfor fått " + pe.ut_avviksbelopututenminus().format() + " kroner for ",
			)

			//IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT < 0) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().lessThan(0))){
				text (
					Bokmal to "mye",
					Nynorsk to "mykje",
				)
			}

			//IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT > 0) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().greaterThan(0))){
				text (
					Bokmal to "lite",
					Nynorsk to "lite",
				)
			}
			text (
				Bokmal to " i uføretrygd. ",
				Nynorsk to " i uføretrygd. ",
			)
		}
    }
}
        