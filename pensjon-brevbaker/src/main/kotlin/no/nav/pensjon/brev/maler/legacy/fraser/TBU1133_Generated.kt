package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1133_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1133NN, TBU1133, TBU1133EN]

		paragraph {
			text (
				Bokmal to "Du kan lese mer om dette i vedlegget «Opplysninger om beregningen».",
				Nynorsk to "Du kan lese meir om dette i vedlegget «Opplysningar om berekninga».",
				English to "You can read more about this in attachment “Information about calculations”.",
			)
		}
    }
}
