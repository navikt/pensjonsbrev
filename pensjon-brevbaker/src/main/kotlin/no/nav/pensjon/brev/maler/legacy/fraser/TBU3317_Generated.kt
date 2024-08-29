

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU3317_Generated : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU3317_NN, TBU3317]

		paragraph {
			text (
				Bokmal to "Hvert år når skatteoppgjøret er klart gjør vi et etteroppgjør av uføretrygden. Da kontrollerer vi om utbetalingen for året er riktig.",
				Nynorsk to "Kvart år når skatteoppgjeret er klart, gjer vi eit etteroppgjer av uføretrygda. Etteroppgjeret kontrollerer om du fekk rett utbetalt i fjor. Då kontrollerer vi om utbetalinga for året er riktig.",
			)
		}
    }
}
