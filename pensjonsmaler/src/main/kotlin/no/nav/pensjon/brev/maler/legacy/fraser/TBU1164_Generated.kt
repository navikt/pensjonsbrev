

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1164_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1164NN, TBU1164EN, TBU1164]

		paragraph {
			text (
				bokmal { + "Uførhet som skyldes yrkesskade eller yrkessykdom" },
				nynorsk { + "Uførleik som kjem av yrkesskade eller yrkessjukdom" },
				english { + "Disability due to occupational injury or illness" },
			)
		}
    }
}
