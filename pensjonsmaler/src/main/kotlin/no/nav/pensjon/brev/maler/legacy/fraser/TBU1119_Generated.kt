

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1119_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1119NN, TBU1119, TBU1119EN]

		paragraph {
			text (
				bokmal { + "Du er innvilget gjenlevendetillegg i uføretrygden din." },
				nynorsk { + "Du er innvilga attlevandetillegg i uføretrygda di." },
				english { + "You have been granted survivor’s supplement with your disability benefit." },
			)
		}
    }
}
