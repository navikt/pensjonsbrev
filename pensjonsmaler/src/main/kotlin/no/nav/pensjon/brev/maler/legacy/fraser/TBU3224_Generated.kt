package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_virkningfom
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU3224_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
		//[TBU3224]

		title1 {
			text (
				Bokmal to "Etterbetaling av uføretrygd",
			)
		}
		paragraph {
			ifNotNull(pe.vedtaksdata_virkningfom()){ virkFom ->
				textExpr (
					Bokmal to "Du får etterbetalt uføretrygd fra ".expr() + virkFom.format() + ". Beløpet blir vanligvis utbetalt i løpet av sju virkedager. Det kan bli beregnet fradrag i etterbetalingen for skatt og ytelser du har mottatt fra NAV eller andre, som for eksempel tjenestepensjonsordninger. I disse tilfellene kan etterbetalingen bli forsinket med inntil ni uker. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen.",
				)
			}
		}
    }
}
        