

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate


data class TBU1100_1_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1100.1EN, TBU1100.1NN, TBU1100_MedMargin, TBU1100.1]

		paragraph {
			text (
				bokmal { + "Du har rett til å klage" },
				nynorsk { + "Viss du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. I vedlegget «" + pe.ut_orienteringomrettighetogplikt_eller_rettigheterogmulighetforklage() + "» får du vite meir om korleis du går fram. Du finn skjema og informasjon på $KLAGE_URL." },
				english { + "If you believe the decision is wrong, you may appeal. The deadline for appeal is six weeks from the date you received the decision. In the attachment “" + pe.ut_orienteringomrettighetogplikt_eller_rettigheterogmulighetforklage() + "” you will find more about how to proceed. You will find forms and information at $KLAGE_URL." },
			)
			text (
				bokmal { + "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du mottok vedtaket. I vedlegget «Orientering om rettigheter og plikter» får du vite mer om hvordan du går fram. Du finner skjema og informasjon på $KLAGE_URL." },
			)
		}
    }
}
        