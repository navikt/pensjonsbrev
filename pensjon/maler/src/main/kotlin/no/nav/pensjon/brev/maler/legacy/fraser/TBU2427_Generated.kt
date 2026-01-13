package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU2427_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2427NN, TBU2427EN, TBU2427]

		paragraph {
			text (
				bokmal { + "<FRITEKST: konkret begrunnelse der det er nødvendig>" },
				nynorsk { + "<Fritekst: konkret begrunnelse der det er nødvendig>" },
				english { + "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>" },
			)
		}
    }
}
