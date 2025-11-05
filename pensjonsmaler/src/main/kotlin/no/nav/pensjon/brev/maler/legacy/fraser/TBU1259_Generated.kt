

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1259_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1259NN, TBU1259EN, TBU1259]

		paragraph {
			text (
				bokmal { + "Dette betyr at uføretrygden din vil bli beregnet etter særbestemmelser som gir deg en høyere uføretrygd." },
				nynorsk { + "Dette betyr at uføretrygda di blir rekna ut etter særreglar som gir deg ei høgare uføretrygd." },
				english { + "This means that your disability benefit will be calculated according to special provisions, which will result in a higher payment for you." },
			)
		}
    }
}
