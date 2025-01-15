package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1214_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1214NN, TBU1214, TBU1214EN]

		title1 {
			text (
				Bokmal to "For deg som mottar gjenlevendetillegg",
				Nynorsk to "For deg som mottar tillegg for attlevande ektefelle",
				English to "For those receiving a surivor’s supplement",
			)
		}
    }
}
