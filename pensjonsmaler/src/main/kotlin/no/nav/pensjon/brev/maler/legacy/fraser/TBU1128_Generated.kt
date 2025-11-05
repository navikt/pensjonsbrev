

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1128_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1128, TBU1128NN, TBU1128EN]

		paragraph {
			text (
				bokmal { + "I dette brevet forklarer vi hvilke rettigheter og plikter du har. Det er derfor viktig at du leser hele brevet." },
				nynorsk { + "I dette brevet forklarer vi kva rettar og plikter du har. Det er derfor viktig at du les heile brevet." },
				english { + "In this letter we will explain your rights and obligations. Therefore, it is important that you read the whole letter." },
			)
		}
    }
}
