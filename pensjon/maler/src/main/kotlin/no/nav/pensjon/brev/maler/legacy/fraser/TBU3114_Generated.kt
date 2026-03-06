

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU3114_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU3114EN, TBU3114NN, TBU3114]

		paragraph {
			text (
				bokmal { + "Utbetaling av uføretrygd for deg som er under straffegjennomføring" },
				nynorsk { + "Utbetaling av uføretrygd når du er under straffegjennomføring" },
				english { + "Payment of disability benefit while you are serving a sentence" },
			)
		}
    }
}
