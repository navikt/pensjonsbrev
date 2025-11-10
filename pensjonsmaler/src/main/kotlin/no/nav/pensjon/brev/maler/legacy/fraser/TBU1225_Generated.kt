package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1225_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1225NN, TBU1225EN, TBU1225]

		paragraph {
			text (
				bokmal { + "Du kan sende klagen direkte til NAV eller gjennom <FRITEKST: utenlandsk trygdemyndighet>." },
				nynorsk { + "Du kan sende klaga direkte til NAV eller gjennom <FRITEKST: Utenlandsk trygdemyndighet>." },
				english { + "You may send your appeal directly to NAV or through <FRITEKST: Utenlandsk trygdemyndighet>." },
			)
		}
    }
}
