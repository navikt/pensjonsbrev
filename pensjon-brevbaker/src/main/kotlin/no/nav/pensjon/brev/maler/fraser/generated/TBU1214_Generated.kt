

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1214_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1214NN, TBU1214, TBU1214EN]

		paragraph {
			text (
				Bokmal to "For deg som mottar gjenlevendetillegg",
				Nynorsk to "For deg som mottar tillegg for attlevande ektefelle",
				English to "For those receiving a surivor’s supplement",
			)
		}
    }
}
