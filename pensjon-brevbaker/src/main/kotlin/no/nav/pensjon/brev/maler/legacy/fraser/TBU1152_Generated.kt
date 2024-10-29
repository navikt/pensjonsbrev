package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1152_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1152EN, TBU1152NN, TBU1152]

		paragraph {
			text (
				Bokmal to "For å bli innvilget rettighet som ung ufør er det et krav at du ble ufør før du fylte 26 år på grunn av en alvorlig og varig sykdom eller skade, som er klart dokumentert.",
				Nynorsk to "For å bli innvilga rett som ung ufør er det eit krav at du blei ufør før du fylte 26 år på grunn av ein alvorleg og varig sjukdom eller skade, som er klart dokumentert.",
				English to "To be eligible for rights as a young disabled person, you must have been disabled before turning 26, due to a serious and permanent illness or injury, which has been clearly documented.",
			)
		}
    }
}
