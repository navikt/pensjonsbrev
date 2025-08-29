package no.nav.pensjon.brev.maler.legacy.fraser

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
				bokmal { + "I etterbetalingen trekker vi skatt etter standardsatser fra Skatteetaten. På utbetalingsmeldingen ser du hvilken skattesats som er brukt. Nav kan redusere etterbetalingen din dersom du har:" },
				nynorsk { + "I etterbetalinga trekkjer vi skatt etter standardsatsar frå Skatteetaten. På utbetalingsmeldinga ser du kva skattesats som er brukt. Nav kan redusere etterbetalinga di dersom du har:" },
			)
			list {
				item {
					text(
						bokmal { + "Feilutbetalingsgjeld hos Nav" },
						nynorsk { + "feilutbetalingsgjeld hos Nav" },
					)
				}

				item {
					text(
						bokmal { + "Ettergitt feilutbetaling av uføretrygd " },
						nynorsk { + "ettergitt feilutbetaling av uføretrygd " },
					)

					//IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true OR PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggSB = true) THEN      INCLUDE ENDIF
					showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb() or pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggsb())) {
						text(
							bokmal { + "og barnetillegg " },
							nynorsk { + "og barnetillegg " },
						)
					}
					text(
						bokmal { + "for samme år" },
						nynorsk { + "for same år" },
					)
				}
				item {
					text(
						bokmal { + "Bidragsgjeld hos Nav" },
						nynorsk { + "bidragsgjeld hos Nav" },
					)
				}
				item {
					text(
						bokmal { + "Utbetaling fra en tjenestepensjonsordning som krever refusjon" },
						nynorsk { + "utbetaling frå ei tenestepensjonsordning som krev refusjon" },
					)
				}
			}
			text (
				bokmal { + "Dersom vi reduserer etterbetalingsbeløpet fordi du har gjeld, mottar du et eget brev om dette. Refusjon fra en tjenestepensjonsordning ser du på utbetalingsmeldingen." },
				nynorsk { + "Dersom vi reduserer etterbetalingsbeløpet fordi du har gjeld, får du eit eige brev om dette. Refusjon frå ei tenestepensjonsordning ser du på utbetalingsmeldinga." },
			)
		}
    }
}
        