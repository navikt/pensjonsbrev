

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU3103_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU3103EN, TBU3103NN, TBU3103]

		paragraph {
			text (
				bokmal { + "Utbetaling av uføretrygd for deg som er innlagt på institusjon" },
				nynorsk { + "Utbetaling av uføretrygd når du er innlagd på institusjon" },
				english { + "Payment of disability benefit when you have been admitted to an institution" },
			)
		}
    }
}
