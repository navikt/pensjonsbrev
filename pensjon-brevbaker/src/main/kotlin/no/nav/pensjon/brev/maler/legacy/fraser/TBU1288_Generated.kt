

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1288_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1288NN, TBU1288, TBU1288EN]

		paragraph {
			text (
				Bokmal to "Du kan lese mer om beregningen av barnetillegg i vedlegget «Opplysninger om beregningen».",
				Nynorsk to "Du kan lese meir om berekninga av barnetillegg i vedlegget «Opplysningar om berekninga».",
				English to "Read more about how child supplements are calculated in the attachment called \"Information about calculations\".",
			)
		}
    }
}
