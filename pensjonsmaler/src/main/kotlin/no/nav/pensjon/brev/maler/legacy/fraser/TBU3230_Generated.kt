

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU3230_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU3230NN, TBU3230EN, TBU3230]

		paragraph {
			text (
				bokmal { + "Vi har redusert utbetalingen av uføretrygden din, fordi du er innlagt på institusjon." },
				nynorsk { + "Vi har redusert utbetalinga av uføretrygda di, fordi du er innlagd på institusjon." },
				english { + "We have reduced your disability benefit, because you have been admitted to an institution." },
			)
		}
    }
}
