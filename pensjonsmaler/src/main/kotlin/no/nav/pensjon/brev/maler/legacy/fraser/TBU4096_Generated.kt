package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.ut_uforetrygdetteroppgjor_periodefom_year
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


data class TBU4096_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU4096_NN, TBU4096]

		title1 {
			text (
				Bokmal to "For deg som har fått flere vedtak om etteroppgjør for samme år",
				Nynorsk to "For deg som har fått fleire vedtak om etteroppgjer for same år",
			)
			textExpr (
				Bokmal to "Vi har mottatt nye inntektsopplysninger fra Skatteetaten for året ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + " og vi har derfor gjort et nytt etteroppgjør. Ditt nye etteroppgjør erstatter ikke tidligere etteroppgjør for samme år. Det betyr at alle vedtak om etteroppgjør er gjeldende. ",
				Nynorsk to "Vi har fått nye inntektsopplysningar frå Skatteetaten for året ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + " og vi har derfor gjort eit nytt etteroppgjer. Det nye etteroppgjeret ditt erstattar ikkje tidlegare etteroppgjer for same år. Det betyr at alle vedtak om etteroppgjer gjeld. ",
			)
		}
    }
}
        