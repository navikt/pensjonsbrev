

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.fraser.common.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU2213_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2213, TBU2213NN, TBU2213EN]

		title1 {
            text(
                bokmal { +"Du har rett til å klage" },
                nynorsk { +"Du har rett til å klage" },
                english { +"You have the right of appeal" },
            )
        }
            paragraph {
			text (
				bokmal { + "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du mottok vedtaket. I vedlegget «Orientering om rettigheter og plikter» får du vite mer om hvordan du går fram. Du finner skjema og informasjon på $KLAGE_URL." },
				nynorsk { + "Viss du meiner at vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. I vedlegget «Orientering om rettar og plikter» får du vite meir om korleis du går fram. Du finn skjema og informasjon på $KLAGE_URL." },
				english { + "If you think the decision is wrong, you may appeal the decision within six weeks of the date on which you received notice of the decision. Your appeal must be made in writing. In the attachment “Information about rights and obligations”, you can find out more about how to proceed. You will find a form you can use and more information about appeals at $NAV_URL." },
			)
		}
    }
}
