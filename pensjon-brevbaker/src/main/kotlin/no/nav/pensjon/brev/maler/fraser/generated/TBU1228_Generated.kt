

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_URL
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1228_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1228NN, TBU1228, TBU1228EN]

		paragraph {
			text (
				Bokmal to "Skattekort",
				Nynorsk to "Skattekort",
				English to "Tax card",
			)
			text (
				Bokmal to "Uføretrygd skattlegges som lønnsinntekt.Du trenger ikke levere skattekortet ditt til NAV fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten. Du bør likevel sjekke at du har riktig skattekort. Skattekortet kan du endre på $SKATTEETATEN_URL. Under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL, kan du se hvilket skattetrekk som er registrert hos NAV.",
				Nynorsk to "Uføretrygd blir skattlagd som lønsinntekt. Du treng ikkje levere skattekortet ditt til NAV, fordi skatteopplysningane dine blir sende elektronisk frå Skatteetaten. Du bør likevel sjekke at du har rett skattekort. Skattekortet kan du endre på $SKATTEETATEN_URL. Under menyvalet «uføretrygd» når du logger deg inn på $NAV_URL, kan du sjå kvaskattetrekk som er registrert hos NAV.",
				English to "You do not need to submit your tax card to NAV because your tax details are sent electronically from the Norwegian Tax Administration. However, you should check that you have the correct tax card. You may change your tax card at $SKATTEETATEN_URL. You may see your registered income tax rate under the option “uføretrygd” at $NAV_URL.",
			)
		}
    }
}
