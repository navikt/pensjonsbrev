package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU3305_Generated : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU3305_NN, TBU3305]

		paragraph {
			text (
				Bokmal to "Du kan lese mer om etteroppgjør i vedlegget «Opplysninger om etteroppgjøret».",
				Nynorsk to "Du kan lese meir om etteroppgjer i vedlegget «Opplysningar om etteroppgjeret». ",
			)
		}
    }
}
