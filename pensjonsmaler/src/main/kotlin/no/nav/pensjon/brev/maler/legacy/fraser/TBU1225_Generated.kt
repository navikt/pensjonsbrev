

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1225_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1225NN, TBU1225EN, TBU1225]

		paragraph {
			text (
				bokmal { + "Du kan sende klagen direkte til NAV eller gjennom <FRITEKST: utenlandsk trygdemyndighet>." },
				nynorsk { + "Du kan sende klaga direkte til NAV eller gjennom <FRITEKST: Utenlandsk trygdemyndighet>." },
				english { + "You may send your appeal directly to NAV or through <FRITEKST: Utenlandsk trygdemyndighet>." },
			)
		}
    }
}
