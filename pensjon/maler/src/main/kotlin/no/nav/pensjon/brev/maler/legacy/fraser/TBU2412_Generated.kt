package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

data class TBU2412_Generated(val uniqueness: String? = null) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2412NN, TBU2412EN, TBU2412]

		paragraph(uniqueness = uniqueness) {
			text (
				bokmal { + "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd." },
				nynorsk { + "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om uføretrygd." },
				english { + "You do not meet these requirements, and your application for disability benefit is thus denied." },
			)
		}
    }
}
