package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1154_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1154EN, TBU1154NN, TBU1154]

		paragraph {
			text (
				Bokmal to "For å bli innvilget rettighet som ung ufør er det et krav at du ble ufør før du fylte 26 år på grunn av en alvorlig og varig sykdom eller skade, som er klart dokumentert. Dersom du har vært mer enn 50 prosent yrkesaktiv etter at du fylte 26 år, må du ha søkt om uføretrygd før du fyller 36 år.",
				Nynorsk to "For å bli innvilga rett som ung ufør er det eit krav at du blei ufør før du fylte 26 år på grunn av ein alvorleg og varig sjukdom eller skade som er klart dokumentert. Dersom du har vore meir enn 50 prosent yrkesaktiv etter at du fylte 26 år, må du ha søkt om uføretrygd før du fyller 36 år.",
				English to "To be eligible for special rights as a young disabled person, you must have become disabled before turning 26 due to a serious and permanent illness or injury, which has clearly been documented. If you have been working more than 50 percent after turning 26, you must have applied for disability benefit before turning 36.",
			)
		}
    }
}
