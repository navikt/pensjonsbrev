

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU3301_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU3301_NN, TBU3301]

		paragraph {
			text (
				Bokmal to "Vi har mottatt ",
				Nynorsk to "Vi har fått ",
			)

			//IF(PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_TidligereEOIverksatt = true     AND (PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_ResultatForrigeEO = 'etterbet'            OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_ResultatForrigeEO = 'tilbakekr'            )    AND (PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringBruker_EndretPGI = true              OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringEPS_EndretPGI = true              OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringBruker_EndretPensjonOgAndreYtelser = true              OR PE_Vedtaksbrev_Vedtaksdata_ForrigeEtteroppgjor_eoEndringEPS_EndretPensjonOgAndreYtelser = true            ) ) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_tidligereeoiverksatt() and (pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_resultatforrigeeo().equalTo("etterbet") or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_resultatforrigeeo().equalTo("tilbakekr")) and (pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringbruker_endretpgi() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringeps_endretpgi() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringbruker_endretpensjonogandreytelser() or pe.vedtaksbrev_vedtaksdata_forrigeetteroppgjor_eoendringeps_endretpensjonogandreytelser()))){
				text (
					Bokmal to "nye ",
					Nynorsk to "nye ",
				)
			}
			text (
				Bokmal to "inntektsopplysninger fra Skatteetaten. Vi har ut fra disse opplysningene vurdert hva du skulle hatt i uføretrygd ",
				Nynorsk to "inntektsopplysningar frå Skatteetaten. Vi har ut frå desse opplysningane vurdert kva du skulle hatt i uføretrygd ",
			)

			//IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())){
				text (
					Bokmal to "og barnetillegg ",
					Nynorsk to "og barnetillegg ",
				)
			}
			textExpr (
				Bokmal to "i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Resultatet av etteroppgjøret viser at du har fått " + pe.ut_avviksbeloputenminus().format() + " kroner for lite i ",
				Nynorsk to "i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". Resultatet av etteroppgjeret viser at du har fått " + pe.ut_avviksbeloputenminus().format() + " kroner for lite i ",
			)

			//IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
				text (
					Bokmal to "uføretrygd",
					Nynorsk to "uføretrygd",
				)
			}

			//IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0  AND (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0)) THEN      INCLUDE ENDIF
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
			text (
				Bokmal to ". Dette beløpet får du normalt utbetalt innen 7 dager.",
				Nynorsk to ". Dette beløpet får du normalt utbetalt innan sju dagar.",
			)
		}
    }
}
        