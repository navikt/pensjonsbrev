

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1260_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1260NN, TBU1260EN, TBU1260]

		paragraph {
			text (
				bokmal { + "Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
				nynorsk { + "Dette betyr at uføretrygda di blir rekna ut etter særreglar, dersom dette er til fordel for deg." },
				english { + "This means that your disability benefit will be calculated according to special provisions, provided this is to your benefit." },
			)
		}
    }
}
