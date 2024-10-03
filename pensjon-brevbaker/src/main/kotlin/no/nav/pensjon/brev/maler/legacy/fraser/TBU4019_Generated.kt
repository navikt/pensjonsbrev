

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU4019_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU4019_NN, TBU4019]

		paragraph {
			text (
				Bokmal to "Vi gjør først en vurdering av om du har fått for mye eller for lite i uføretrygd. Uføretrygden regnes med som personinntekt, og har derfor betydning for om du har fått for mye eller for lite i barnetillegg. ",
				Nynorsk to "Vi vurderer først om du har fått for mykje eller for lite i uføretrygd. Uføretrygda blir rekna med som personinntekt og har derfor betydning for om du har fått for mykje eller for lite i barnetillegg. ",
			)

			//IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true) THEN      INCLUDE ENDIF
			showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())){
				text (
					Bokmal to "Fordi du mottok barnetillegg både for barn som bodde sammen med begge sine foreldre og for barn som ikke bodde sammen med begge foreldrene, har vi for begge barnetilleggene vurdert om du har fått for mye eller for lite. ",
					Nynorsk to "Fordi du fekk barnetillegg både for barn som budde saman med begge foreldra sine, og for barn som ikkje budde saman med begge foreldra, har vi for begge barnetillegga vurdert om du har fått for mykje eller for lite. ",
				)
			}
		}
    }
}
        