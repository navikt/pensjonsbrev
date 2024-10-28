

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text


data class TBU3309_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU3309_NN, TBU3309]

		paragraph {
			text (
				Bokmal to "I etterbetalingen trekker vi skatt etter standardsatser fra Skatteetaten. På utbetalingsmeldingen ser du hvilken skattesats som er brukt. Nav kan redusere etterbetalingen din dersom du har:",
				Nynorsk to "I etterbetalinga trekkjer vi skatt etter standardsatsar frå Skatteetaten. På utbetalingsmeldinga ser du kva skattesats som er brukt. Nav kan redusere etterbetalinga di dersom du har:",
			)
			list {
				item {
					text(
						Bokmal to "Feilutbetalingsgjeld hos Nav",
						Nynorsk to "feilutbetalingsgjeld hos Nav",
					)
				}

				item {
					text(
						Bokmal to "Ettergitt feilutbetaling av uføretrygd ",
						Nynorsk to "ettergitt feilutbetaling av uføretrygd ",
					)

					//IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true) THEN      INCLUDE ENDIF
					showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())) {
						text(
							Bokmal to "og barnetillegg ",
							Nynorsk to "og barnetillegg ",
						)
					}
					text(
						Bokmal to "for samme år",
						Nynorsk to "for same år",
					)
				}
				item {
					text(
						Bokmal to "Bidragsgjeld hos Nav",
						Nynorsk to "bidragsgjeld hos Nav",
					)
				}
				item {
					text(
						Bokmal to "Utbetaling fra en tjenestepensjonsordning som krever refusjon",
						Nynorsk to "utbetaling frå ei tenestepensjonsordning som krev refusjon",
					)
				}
			}
			text (
				Bokmal to "Dersom vi reduserer etterbetalingsbeløpet fordi du har gjeld, mottar du et eget brev om dette. Refusjon fra en tjenestepensjonsordning ser du på utbetalingsmeldingen.",
				Nynorsk to "Dersom vi reduserer etterbetalingsbeløpet fordi du har gjeld, får du eit eige brev om dette. Refusjon frå ei tenestepensjonsordning ser du på utbetalingsmeldinga.",
			)
		}
    }
}
        