package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

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
