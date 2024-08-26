

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU4028_Generated : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU4028_NN, TBU4028]

		paragraph {
			text (
				Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14, 12-16 og 12-18. ",
				Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14, 12-16 og 12-18. ",
			)
		}
    }
}
