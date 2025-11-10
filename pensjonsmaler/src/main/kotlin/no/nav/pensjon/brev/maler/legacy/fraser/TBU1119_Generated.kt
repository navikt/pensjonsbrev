package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

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
