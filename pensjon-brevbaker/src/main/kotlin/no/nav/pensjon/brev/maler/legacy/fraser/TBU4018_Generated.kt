

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU4018_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU4018_NN, TBU4018]

		paragraph {

			//IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
				text (
					Bokmal to "Det er din pensjonsgivende inntekt som avgjør hvor mye du skulle ha fått i uføretrygd",
					Nynorsk to "Det er den pensjonsgivande inntekta som avgjer kor mykje du skulle ha fått i uføretrygd",
				)
			}

			//IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget() and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
				text (
					Bokmal to " og gjenlevendetillegg",
					Nynorsk to " og attlevandetillegg",
				)
			}

			//IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))){
				text (
					Bokmal to ". ",
					Nynorsk to ". ",
				)
			}

			//IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true AND PE_UT_EtteroppgjorAvviksbelopTSBogTFBulikNull() = true) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb() and pe.ut_etteroppgjoravviksbeloptsbogtfbuliknull())){
				text (
					Bokmal to "For barn som ikke bor med begge foreldre er det ",
					Nynorsk to "For barn som ikkje bur saman med begge foreldra sine er det ",
				)
			}

			//IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = false AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true AND PE_UT_EtteroppgjorAvviksbelopTSBogTFBulikNull() = true) THEN      INCLUDE ENDIF
			showIf((not(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb()) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb() and pe.ut_etteroppgjoravviksbeloptsbogtfbuliknull())){
				text (
					Bokmal to "Det er ",
					Nynorsk to "Det er ",
				)
			}

			//IF((PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true) OR(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = false AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true)) AND PE_UT_EtteroppgjorAvviksbelopTSBogTFBulikNull() = true THEN      INCLUDE ENDIF
			showIf(((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb()) or (not(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb()) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())) and pe.ut_etteroppgjoravviksbeloptsbogtfbuliknull()){
				text (
					Bokmal to "personinntekten din som har betydning for størrelsen på barnetillegget. ",
					Nynorsk to "personinntekta di som har noko å seie for storleiken på barnetillegget ditt. ",
				)
			}

			//IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true AND PE_UT_EtteroppgjorAvviksbelopTSBogTFBulikNull() = true) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb() and pe.ut_etteroppgjoravviksbeloptsbogtfbuliknull())){
				text (
					Bokmal to "For barn som bor sammen med begge foreldre er det ",
					Nynorsk to "For barn som bur saman med begge foreldra sine er det ",
				)
			}

			//IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = false AND PE_UT_EtteroppgjorAvviksbelopTSBogTFBulikNull() = true) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and not(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb()) and pe.ut_etteroppgjoravviksbeloptsbogtfbuliknull())){
				text (
					Bokmal to "Det er ",
					Nynorsk to "Det er ",
				)
			}

			//IF((PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true) OR(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = false))  AND PE_UT_EtteroppgjorAvviksbelopTSBogTFBulikNull() = true THEN      INCLUDE ENDIF
			showIf(((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb()) or (pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and not(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb()))) and pe.ut_etteroppgjoravviksbeloptsbogtfbuliknull()){
				text (
					Bokmal to "personinntekten til deg og annen forelder som har betydning for størrelsen på barnetillegget.",
					Nynorsk to "personinntekta di og personinntekta til den andre forelderen som har noko å seie for storleiken på barnetillegget ditt.",
				)
			}
		}
    }
}
        