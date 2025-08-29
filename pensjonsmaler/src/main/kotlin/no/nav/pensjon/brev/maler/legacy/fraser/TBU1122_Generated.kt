

package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_totalnetto
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.formatMonthYear
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text


data class TBU1122_Generated(
	val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1122NN, TBU1122, TBU1122EN]

		paragraph {
			text (
				bokmal { + "Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd og gjenlevendetillegg per måned før skatt" },
				nynorsk { + "Du får " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " i uføretrygd og attlevandetillegg per månad før skatt" },
				english { + "Your monthly disability benefit and survivor's supplement payment will be " + pe.vedtaksdata_beregningsdata_beregningufore_totalnetto().format() + " before tax" },
			)
			ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom()) {
				text (
					bokmal { + " fra " + it.formatMonthYear() },
					nynorsk { + " frå " + it.formatMonthYear() } ,
					english { + " starting " + it.formatMonthYear() },
				)
			}
			text(
				bokmal { + "." },
				nynorsk { + "." },
				english { + "." },
			)
		}
    }
}
        