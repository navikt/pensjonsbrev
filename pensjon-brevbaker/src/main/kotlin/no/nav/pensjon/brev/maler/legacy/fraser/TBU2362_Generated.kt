package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU2362_Generated : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU2362NN, TBU2362]

		paragraph {
			text (
				Bokmal to "Vi har derfor redusert utbetalingen av uføretrygden din for resten av kalenderåret.",
				Nynorsk to "Vi har derfor redusert utbetaling av uføretrygda di for resten av kalenderåret.",
			)
		}
    }
}
