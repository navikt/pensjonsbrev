

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1264_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1264EN, TBU1264NN, TBU1264]

		paragraph {
			text (
				bokmal { + "Denne delen av uføretrygden din vil bli beregnet etter særbestemmelser, dersom dette er til fordel for deg." },
				nynorsk { + "Denne delen av uføretrygda di blir rekna ut etter særreglar dersom det er til fordel for deg." },
				english { + "This means that this portion of your disability benefit will be calculated on the basis of special rules, provided this is to your benefit." },
			)
		}
    }
}
