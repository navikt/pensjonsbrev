

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.fraser.common.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.legacy.ut_orienteringomrettighetogplikt_eller_rettigheterogmulighetforklage
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate


data class TBU1100_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1100, TBU1100NN, TBU1100EN]

		paragraph {
			text (
				Bokmal to "Du har rett til å klage ",
				Nynorsk to "Du har rett til å klage",
				English to "You have the right of appeal",
			)
			textExpr (
				Bokmal to "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du mottok vedtaket. I vedlegget «".expr() + pe.ut_orienteringomrettighetogplikt_eller_rettigheterogmulighetforklage() + "» får du vite mer om hvordan du går fram. Du finner skjema og informasjon på $KLAGE_URL.",
				)
		}
    }
}
        