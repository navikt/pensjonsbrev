

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate


data class TBU1075_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1075NN, TBU1075EN, TBU1075]

		paragraph {
			text (
				bokmal { + "Du har rett til å se dokumentene i saken din. Se vedlegg «" + pe.ut_orienteringomrettighetogplikt_eller_rettigheterogmulighetforklage() + "» for informasjon om hvordan du går fram." },
				nynorsk { + "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg «" + pe.ut_orienteringomrettighetogplikt_eller_rettigheterogmulighetforklage() + "» for informasjon om korleis du går fram." },
				english { + "You have the right to see your case documents. Refer to the attachment “" + pe.ut_orienteringomrettighetogplikt_eller_rettigheterogmulighetforklage() + "” for information on how to proceed." },
			)
		}
    }
}
        