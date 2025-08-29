package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU2223_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2223NN, TBU2223, TBU2223EN]

		paragraph {
			text (
				bokmal { + "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned." },
				nynorsk { + "Uføretrygda blir framleis utbetalt seinast den 20. i kvar månad." },
				english { + "Your disability benefit will still be paid no later than the 20th of every month." },
			)
		}
    }
}
