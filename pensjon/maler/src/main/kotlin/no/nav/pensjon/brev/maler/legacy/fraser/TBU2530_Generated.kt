package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU2530_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2530NN, TBU2530, TBU2530EN]
		title1 {
			text (
				bokmal { + "Tilbakekreving av uføretrygd" },
				nynorsk { + "Tilbakekrevjing av uføretrygd" },
				english { + "Repayment of disability benefit" },
			)
		}
		paragraph {
			text (
				bokmal { + "Fordi uføretrygden din er redusert tilbake i tid, betyr dette at du har fått utbetalt for mye i uføretrygd. Du får eget brev med varsel om eventuell tilbakekreving av det feilutbetalte beløpet." },
				nynorsk { + "Fordi uføretrygda di er redusert tilbake i tid, betyr dette at du har fått utbetalt for mykje i uføretrygd. Du får eit eiga brev med varsel om ei eventuell tilbakekrevjing av det feilutbetalte beløpet." },
				english { + "You have been paid too much disability because your disability benefit has reduced with an effective date back in time. You will receive a letter about a possible repayment." },
			)
		}
    }
}
