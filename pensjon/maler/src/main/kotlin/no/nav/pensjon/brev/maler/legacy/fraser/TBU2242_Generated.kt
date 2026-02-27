

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU2242_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2242, TBU2242NN, TBU2242EN]

		paragraph {
			text (
				bokmal { + "Du har rett til å se dokumentene i saken din. Se vedlegg «Orientering om rettigheter og plikter» for informasjon om hvordan du går fram." },
				nynorsk { + "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg «Orientering om rettar og plikter» for informasjon om korleis du går fram." },
				english { + "You are entitled to see your case documents. Refer to the attachment “Rights and obligations” for information about how to proceed. " },
			)
		}
    }
}
