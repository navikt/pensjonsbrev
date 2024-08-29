

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate


data class TBU2283_Generated(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2283NN, TBU2283, TBU2283_EN]

		paragraph {
			text (
				Bokmal to "Med vennlig hilsen",
				Nynorsk to "Med vennleg helsing",
				English to "Yours sincerely",
			)
			textExpr (
				Bokmal to pe.kontaktinformasjon_navnavsenderenhet(),
				Nynorsk to pe.kontaktinformasjon_navnavsenderenhet(),
				English to pe.kontaktinformasjon_navnavsenderenhet(),
			)
		}
    }
}
        