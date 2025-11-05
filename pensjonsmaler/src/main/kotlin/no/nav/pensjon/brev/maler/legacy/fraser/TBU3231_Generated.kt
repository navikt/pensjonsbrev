

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU3231_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU3231NN, TBU3231EN, TBU3231]

		paragraph {
			text (
				bokmal { + "Du er innlagt på institusjon, men vi kommer likevel ikke til å redusere utbetalingen av uføretrygden din." },
				nynorsk { + "Du er innlagd på institusjon, men vi kjem likevel ikkje til å redusere utbetalinga av uføretrygda di." },
				english { + "You have been admitted to an institution, but we will not reduce your disability benefit." },
			)
		}
    }
}
