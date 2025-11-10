package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1147_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1147EN, TBU1147, TBU1147NN]

		paragraph {
			text (
				bokmal { + "Du mottok arbeidsavklaringspenger da du søkte om uføretrygd. Det er derfor tilstrekkelig at inntektsevnen din er varig nedsatt med minst 40 prosent." },
				nynorsk { + "Du fekk arbeidsavklaringspengar då du søkte om uføretrygd. Det er derfor tilstrekkeleg at inntektsevna di er varig sett ned med minst 40 prosent." },
				english { + "You received work assessment allowance when you applied for disability benefit. It is therefore sufficient that your earning ability has been permanently reduced by at least 40 percent." },
			)
		}
    }
}
