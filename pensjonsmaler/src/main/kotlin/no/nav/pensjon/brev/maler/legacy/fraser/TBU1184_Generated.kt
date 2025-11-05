

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1184_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1184EN, TBU1184, TBU1184NN]

		paragraph {
			text (
				bokmal { + "Dette er uføretidspunktet ditt, og avgjør hvilke inntektsår vi legger til grunn for beregningen din." },
				nynorsk { + "Dette er uføretidspunktet ditt, og det avgjer kva inntektsår vi legg til grunn for berekninga di." },
				english { + "This is your date of disability, and determines the income years we use to determine your disability benefit." },
			)
		}
    }
}
