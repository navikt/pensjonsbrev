

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate

object TBU1146_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1146EN, TBU1146, TBU1146NN]

		paragraph {
			text (
				bokmal { + "Du oppfyller unntaksregel om nedsatt inntektsevne" },
				nynorsk { + "Du oppfyller unntaksregel om nedsett inntektsevne" },
				english { + "You meet the exemption criteria relating to reduced earning ability" },
			)
		}
    }
}
