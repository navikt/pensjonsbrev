

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.fraser.common.Constants.ETTEROPPGJOR_URL
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU3370_Generated : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU3370_NN, TBU3370]

		paragraph {
			text (
				Bokmal to "Du finner mer informasjon på $ETTEROPPGJOR_URL. Hvis du ikke finner svar på spørsmålet ditt, kan du kontakte oss på nav.no/kontakt",
				Nynorsk to "Du finn meir informasjon på $ETTEROPPGJOR_URL. Om du ikkje finn svar på spørsmålet ditt, kan du kontakte oss på nav.no/kontakt",
			)
		}
    }
}
