

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU3401_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU3401NN, TBU3401EN, TBU3401]

		paragraph {
			text (
				bokmal { + "Vi har redusert utbetalingen av uføretrygden din, fordi du er under straffegjennomføring." },
				nynorsk { + "Vi har redusert utbetalinga av uføretrygda di, fordi du er under straffegjennomføring." },
				english { + "We have reduced your disability benefit, because you are serving a sentence." },
			)
		}
    }
}
