

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1113_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1113NN, TBU1113EN, TBU1113]

		paragraph {
			text (
				bokmal { + "Vi har kommet fram til at hele uførheten din skyldes en godkjent yrkesskade eller yrkessykdom." },
				nynorsk { + "Vi har kome fram til at heile uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom." },
				english { + "We have concluded that the whole of your disability is due to an approved occupational injury or illness." },
			)
		}
    }
}
