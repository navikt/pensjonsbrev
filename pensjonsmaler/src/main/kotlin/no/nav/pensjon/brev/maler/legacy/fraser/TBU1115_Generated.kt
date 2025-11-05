

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1115_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1115NN, TBU1115EN, TBU1115]

		paragraph {
			text (
				bokmal { + "Vi har kommet fram til at uførheten din ikke skyldes en godkjent yrkesskade eller yrkessykdom." },
				nynorsk { + "Vi har kome fram til at uførleiken din ikkje kjem av ein godkjend yrkesskade eller yrkessjukdom." },
				english { + "We have concluded that your disability is not due to an occupational injury or illness." },
			)
		}
    }
}
