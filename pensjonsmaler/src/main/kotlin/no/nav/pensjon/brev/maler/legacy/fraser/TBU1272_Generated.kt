

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1272_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1272NN, TBU1272EN, TBU1272]

		paragraph {
			text (
				bokmal { + "Du har ikke en godkjent yrkesskade eller yrkessykdom. Uføretrygden din vil derfor ikke bli beregnet etter særbestemmelser for yrkesskade eller yrkessykdom. <FRITEKST: Konkret begrunnelse>." },
				nynorsk { + "Du har ikkje ein godkjend yrkesskade eller yrkessjukdom. Uføretrygda di blir derfor ikkje innvilga etter reglar for yrkesskade eller yrkessjukdom. <Fritekst: Konkret begrunnelse>." },
				english { + "You do not have a certified occupational injury or occupational illness. Your disability benefit will not be calculated in accordance with the special rules for occupational injury or occupational illness.<Fritekst: Konkret begrunnelse>." },
			)
		}
    }
}
