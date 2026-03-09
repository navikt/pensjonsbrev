

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU2370_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2370EN, TBU2370, TBU2370NN]

		title1 {
			text (
				bokmal { + "For deg som kombinerer uføretrygd og alderspensjon" },
				nynorsk { + "For deg som kombinerer uføretrygd og alderspensjon" },
				english { + "For those combining the disability benefit with retirement pension" },
			)
		}
    }
}
