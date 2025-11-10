package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1220_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1220NN, TBU1220EN, TBU1220]

		paragraph {
			text (
				bokmal { + "<STRYK TEKSTEN UNDER DERSOM DEN IKKE ER AKTUELL>" },
				nynorsk { + "<STRYK TEKSTEN UNDER DERSOM DEN IKKE ER AKTUELL>" },
				english { + "<STRYK TEKSTEN UNDER DERSOM DEN IKKE ER AKTUELL>" },
			)
			text (
				bokmal { + "En utenlandsk myndighet krever refusjon" },
				nynorsk { + "Eit utanlandsk organ krev refusjon" },
				english { + "A foreign authority demands a refund" },
			)
			text (
				bokmal { + "<FRITEKST: land> har varslet NAV at de kan ha utbetalt for mye penger til deg. De har mulighet til å kreve dette tilbake i etterbetalingen av den norske uføretrygden din. Vi vil holde tilbake etterbetalingen inntil vi har fått svar fra <FRITEKST: land>. Har du spørsmål om dette, kan du ta kontakt med <FRITEKST: nasjonalitet> myndigheter." },
				nynorsk { + "<FRITEKST: Land> har varsla NAV at dei kan ha betalt ut for mykje pengar til deg. Dei har høve til å krevje dette tilbake i etterbetalinga av den norske uføretrygda di. Vi vil halde tilbake etterbetalinga inntil vi har fått svar frå <FRITEKST: Land>. Har du spørsmål om dette, kan du ta kontakt med <FRITEKST: Nasjonalitet> styresmakter." },
				english { + "<FRITEKST: Land> has notified NAV that they may have paid you too much money. They are allowed to claim this back from the back payment of your Norwegian disability benefit. We will withhold the back payment until we have received a reply from<FRITEKST: Land>. If you have any questions about this, you can contact <FRITEKST: Nasjonalitet> authorities." },
			)
		}
    }
}
