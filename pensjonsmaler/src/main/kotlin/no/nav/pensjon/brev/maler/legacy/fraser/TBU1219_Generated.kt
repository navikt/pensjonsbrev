

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1219_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1219NN, TBU1219EN, TBU1219]

		paragraph {
			text (
				bokmal { + "Beregningen din kan bli endret" },
				nynorsk { + "Berekninga di kan bli endra" },
				english { + "Your calculation may be altered" },
			)
			text (
				bokmal { + "Når vi mottar vedtak fra utenlandske trygdemyndigheter, fatter vi et nytt vedtak der vi gjør en endelig beregning av uføretrygden din. Hvis det viser seg at du har fått utbetalt mer enn du skulle, kan vi kreve at du betaler tilbake det du skylder. Hvis du får etterbetalt penger fra en utenlandsk trygdemyndighet, kan vi trekke det du skylder fra etterbetalingen." },
				nynorsk { + "Når vi får vedtak frå utanlandske trygdeorgan, fattar vi eit nytt vedtak der vi reknar ut uføretrygda di endeleg. Viss det viser seg at du har fått betalt ut meir enn du skulle ha, kan vi krevje at du betaler tilbake det du skuldar. Viss du får etterbetalt pengar frå eit utanlandsk trygdeorgan, kan vi trekkje det du skuldar frå etterbetalinga." },
				english { + "When we receive a decision from the foreign social security authorities, we make a new decision where we make a final determination of your disability benefit. If you have received more than you should have, we may require that you pay back what you owe. If you receive back payment from a foreign social security authority, we may deduct what you owe from the back payment." },
			)
		}
    }
}
