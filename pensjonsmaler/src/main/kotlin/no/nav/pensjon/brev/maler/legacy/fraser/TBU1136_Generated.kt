package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1136_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1136EN, TBU1136, TBU1136NN, TBU3362NN]

		paragraph {
			text (
				bokmal { + "Søknaden din er innvilget etter klage og vi anser klagen som ferdig behandlet. Dersom du ønsker å opprettholde klagen, må du gi tilbakemelding til NAV innen 3 uker." },
				nynorsk { + "Søknaden din er innvilga etter klage, og vi ser det slik at klaga er ferdig behandla. Dersom du ønskjer å halde fast på klaga, må du melde dette tilbake til NAV innan 3 veker." },
				english { + "Your application has been granted following an appeal and we consider the processing of the appeal to have been concluded. If you want to uphold your appeal, you must notify NAV of this within 3 weeks." },
			)
		}
    }
}
