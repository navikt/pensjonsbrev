

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1077_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1077, TBU1077NN, TBU1077EN, TBU1077_EN]

		paragraph {
			text (
				bokmal { + "Du finner mer informasjon på nav.no. Hvis du ikke finner svar på spørsmålet ditt, kontakt oss på nav.no/kontakt" },
				nynorsk { + "Du finn meir informasjon på nav.no. Om du ikkje finn svar på spørsmålet ditt, kontakt oss på nav.no/kontakt." },
				english { + "You can find more information at nav.no. If you do not find the answer to your question, contact us at nav.no/kontakt" },
			)
		}
    }
}
