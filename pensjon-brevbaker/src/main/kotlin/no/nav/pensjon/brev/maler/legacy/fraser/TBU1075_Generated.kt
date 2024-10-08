

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
		//[TBU1075, TBU1075NN, TBU1075EN]

		paragraph {
			textExpr (
				Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg «".expr() + pe.ut_orienteringomrettighetogplikt_eller_rettigheterogmulighetforklage() + "» for informasjon om hvordan du går fram.",
				Nynorsk to "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg «".expr() + pe.ut_orienteringomrettighetogplikt_eller_rettigheterogmulighetforklage() + "» for informasjon om korleis du går fram.",
				English to "You have the right to see your case documents. Refer to the attachment “".expr() + pe.ut_orienteringomrettighetogplikt_eller_rettigheterogmulighetforklage() + "” for information on how to proceed.",
			)
		}
    }
}
        