package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.quoted
import no.nav.pensjon.brev.template.dsl.textExpr

object TBU1133_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1133NN, TBU1133, TBU1133EN]

		paragraph {
			textExpr (
				Bokmal to "Du kan lese mer om dette i vedlegget ".expr() + quoted("Opplysninger om beregningen") + ".",
				Nynorsk to "Du kan lese meir om dette i vedlegget ".expr() + quoted("Opplysningar om berekninga") +".",
				English to "You can read more about this in attachment ".expr() + quoted("Information about calculations") + ".",
			)
		}
    }
}
