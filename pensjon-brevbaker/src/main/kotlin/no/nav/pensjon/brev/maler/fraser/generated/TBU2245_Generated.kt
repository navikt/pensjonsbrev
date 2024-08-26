

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU2245_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2245NN, TBU2245, TBU2245EN]

		paragraph {
			text (
				Bokmal to "Saken har blitt automatisk saksbehandlet av vårt fagsystem. Vedtaksbrevet er derfor ikke underskrevet av saksbehandler.",
				Nynorsk to "Saka er automatisk saksbehandla av fagsystemet vårt. Vedtaksbrevet er derfor ikkje skrive under av saksbehandlar.",
				English to "This case has been electronically processed by our administration system, which means that this letter has not been signed by a case worker.",
			)
		}
    }
}
