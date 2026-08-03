package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

data class TBU2427_Generated(val uniqueness: String? = null) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2427NN, TBU2427EN, TBU2427]

		paragraph(uniqueness = uniqueness) {
			text (
				bokmal { + "<FRITEKST: konkret begrunnelse der det er nødvendig>" },
				nynorsk { + "<Fritekst: konkret begrunnelse der det er nødvendig>" },
				english { + "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>" },
			)
		}
    }
}
