

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU2223_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2223NN, TBU2223, TBU2223EN]

		paragraph {
			text (
				Bokmal to "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned.",
				Nynorsk to "Uføretrygda blir framleis utbetalt seinast den 20. i kvar månad.",
				English to "Your disability benefit will still be paid no later than the 20th of every month.",
			)
		}
    }
}
