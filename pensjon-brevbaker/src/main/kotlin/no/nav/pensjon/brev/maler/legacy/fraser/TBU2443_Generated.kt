package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU2443_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2443NN, TBU2443EN, TBU2443]

		paragraph {
			text (
				Bokmal to "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om økt uføretrygd.",
				Nynorsk to "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om auka uføretrygd.",
				English to "You do not meet the requirements, and your application for increased disability benefit is thus denied.",
			)
		}
    }
}
