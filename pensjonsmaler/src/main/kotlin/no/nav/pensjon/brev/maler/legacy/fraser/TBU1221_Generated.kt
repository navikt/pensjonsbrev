

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1221_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1221NN, TBU1221EN, TBU1221]

		paragraph {
			text (
				bokmal { + "Denne retten har <FRITEKST: land> etter EØS-forordningen 987/2009 artikkel 72." },
				nynorsk { + "Denne retten har <FRITEKST: Land> etter EØS-forordninga 987/2009 artikkel 72." },
				english { + "<FRITEKST: Land> has this right pursuant to Council Regulation 987/2009 article 72." },
			)
		}
    }
}
