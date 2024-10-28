package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU3308_Generated : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU3308_NN, TBU3308]

		title1 {
			text (
				Bokmal to "Etterbetaling av beløpet",
				Nynorsk to "Etterbetaling av beløpet",
			)
		}
    }
}
