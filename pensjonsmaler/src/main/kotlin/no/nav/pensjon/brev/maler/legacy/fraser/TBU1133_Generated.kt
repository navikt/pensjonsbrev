package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference

object TBU1133_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1133NN, TBU1133, TBU1133EN]

		paragraph {
			text (
				bokmal { + "Du kan lese mer om dette i vedlegget " },
				nynorsk { + "Du kan lese meir om dette i vedlegget " },
				english { + "You can read more about this in attachment " },
			)
			namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
			text(bokmal { + "." }, nynorsk { + "." }, english { + "." })
		}
    }
}
