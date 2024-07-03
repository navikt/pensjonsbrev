

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU2275_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2275NN, TBU2275, TBU2275EN]

		paragraph {
			text (
				Bokmal to "For deg som mottar ektefelletillegg",
				Nynorsk to "For deg som får ektefelletillegg",
				English to "For those receiving a spouse supplement",
			)
		}
    }
}
