

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU2038_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2038EN, TBU2038, TBU2038NN]

		paragraph {
			text (
				bokmal { + "Slik har vi fastsatt uføregraden din" },
				nynorsk { + "Slik har vi fastsett uføregraden din" },
				english { + "This is how we have determined your level of disability" },
			)
		}
    }
}
