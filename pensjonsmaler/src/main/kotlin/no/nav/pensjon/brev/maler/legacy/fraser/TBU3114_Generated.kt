package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU3114_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU3114EN, TBU3114NN, TBU3114]

		paragraph {
			text (
				bokmal { + "Utbetaling av uføretrygd for deg som er under straffegjennomføring" },
				nynorsk { + "Utbetaling av uføretrygd når du er under straffegjennomføring" },
				english { + "Payment of disability benefit while you are serving a sentence" },
			)
		}
    }
}
