package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.ut_uforetrygdetteroppgjor_periodefom_year
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU4024_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU4024_NN, TBU4024]

		paragraph {
			text (
				bokmal { + "Uføretrygden din har vært riktig beregnet ut fra inntekt i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". " },
				nynorsk { + "Uføretrygda di har vore rett berekna ut frå inntekta di i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ". " },
			)
		}
    }
}
