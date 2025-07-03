package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.maler.legacy.vedlegg.opplysningerOmETteroppgjoeretUTLegacy
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus

object TBU3305_Generated : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU3305_NN, TBU3305]

		paragraph {
			text (
				Bokmal to "Du kan lese mer om etteroppgjør i vedlegget ",
				Nynorsk to "Du kan lese meir om etteroppgjer i vedlegget ",
			)
			namedReference(opplysningerOmETteroppgjoeretUTLegacy)
			text(Bokmal to ".", Nynorsk to ".")
		}
    }
}
