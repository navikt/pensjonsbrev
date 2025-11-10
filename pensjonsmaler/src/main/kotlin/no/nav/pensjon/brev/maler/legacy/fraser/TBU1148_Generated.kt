package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1148_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1148EN, TBU1148, TBU1148NN]

		paragraph {
			text (
				bokmal { + "Du er innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom. Det er derfor tilstrekkelig at inntektsevnen din er varig nedsatt med minst 30 prosent." },
				nynorsk { + "Du er innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom. Det er derfor tilstrekkeleg at inntektsevna di er varig sett ned med minst 30 prosent." },
				english { + "You have been granted disability benefit under special provisions for occupational injury and illness. It is therefore sufficient that your earning ability has been permanently reduced by at least 30 percent." },
			)
		}
    }
}
