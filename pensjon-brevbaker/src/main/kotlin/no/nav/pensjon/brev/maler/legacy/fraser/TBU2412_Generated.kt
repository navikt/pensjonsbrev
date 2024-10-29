package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU2412_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2412NN, TBU2412EN, TBU2412]

		paragraph {
			text (
				Bokmal to "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd.",
				Nynorsk to "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om uføretrygd.",
				English to "You do not meet these requirements, and your application for disability benefit is thus denied.",
			)
		}
    }
}
