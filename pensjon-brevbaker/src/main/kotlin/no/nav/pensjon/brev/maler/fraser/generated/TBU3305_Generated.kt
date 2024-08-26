

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

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
