package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.maler.fraser.common.Constants.UFOERETRYGD_URL
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1227_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1227, TBU1227EN, TBU1227NN, TBU1227NNx]

		paragraph {
			text (
				bokmal { + "Sjekk utbetalingene dine" },
				nynorsk { + "Sjekk utbetalingane dine" },
				english { + "Check your disability benefit payments" },
			)
			text (
				bokmal { + "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. Se alle utbetalinger du har mottatt: $UFOERETRYGD_URL. Her kan du også endre kontonummer." },
				nynorsk { + "Du får uføretrygd betalt ut den 20. kvar månad eller seinast siste vyrkedag før denne datoen. Sjå alle utbetalingar du har fått: $UFOERETRYGD_URL. Her kan du også endre kontonummer." },
				english { + "Your disability benefit will be paid on the 20th of each month or no later than the last business day before this date. To see all the payments you have received, go to: $UFOERETRYGD_URL. You may also change your account number here." },
			)
		}
    }
}
