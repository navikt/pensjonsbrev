

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate


data class TBU3740_Generated(
	val uforetrygdOrdiner_inntektsgrense: Expression<Double>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
		//[TBU3740NN, TBU3740]

		paragraph {
			textExpr (
				Bokmal to "Vi gjør oppmerksom på at det ikke utbetales uføretrygd når inntekten din utgjør mer enn inntektsgrensen din, det vil si ".expr() + uforetrygdOrdiner_inntektsgrense.format() + " kroner per år. ",
				Nynorsk to "Vi gjer merksam på at det ikkje blir utbetalt uføretrygd når inntekta di utgjer meir enn inntektsgrensa di, det vil seie ".expr() + uforetrygdOrdiner_inntektsgrense.format() + " kroner per år. ",
			)
		}
    }
}
        