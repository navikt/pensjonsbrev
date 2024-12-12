package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU3325_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU3325_NN, TBU3325]

		paragraph {
			textExpr (
				Bokmal to "Skatteetaten har opplyst at du hadde en pensjonsgivende inntekt på ".expr() + pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut().format() + " kroner i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". ",
				Nynorsk to "Skatteetaten har opplyst at du hadde ei pensjonsgivande inntekt på ".expr() + pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_suminntekterut().format() + " kroner i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". ",
			)

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

			//IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljBruker_SumFratrekkUT > 0 AND PE_UT_PeriodeTomMindre3112PeriodeFomStorre0101() = true) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljbruker_sumfratrekkut().greaterThan(0) and pe.ut_periodetommindre3112periodefomstorre0101())){
				text (
					Bokmal to "Inntekt du hadde før du ble innvilget uføretrygd og etter at uføretrygden din opphørte trekkes fra dette beløpet. ",
					Nynorsk to "Inntekt du hadde før du fekk innvilga uføretrygd, og etter at uføretrygda di tok slutt, blir trekt frå dette beløpet. ",
				)
			}

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
				Bokmal to "Vi har derfor brukt ".expr() + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_inntektut().format() + " kroner i vurderingen av om du har fått for mye eller for lite i uføretrygd.",
				Nynorsk to "Vi har derfor brukt ".expr() + pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_inntektut().format() + " kroner når vi har vurdert om du har fått for mykje eller for lite i uføretrygd.",
			)
		}
    }
}
        