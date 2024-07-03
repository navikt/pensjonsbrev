

package no.nav.pensjon.brev.maler.fraser.generated

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.*
import java.time.LocalDate


data class TBU2279_Generated(
	val uforetrygdOrdiner_inntektstak: Expression<Double>,
	val uforetrygdOrdiner_inntektstak: Expression<Double>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU2279NN, TBU2279, TBU2279EN]

		paragraph {
			textExpr (
				Bokmal to "Vi gjør oppmerksom på at det ikke utbetales uføretrygd når inntekten din utgjør mer enn 80 prosent av inntekten du hadde før du ble ufør, det vil si ".expr() + uforetrygdOrdiner_inntektstak.format() + " kroner per år. Inntekten er justert opp til dagens verdi.",
				Nynorsk to "Vi gjer merksam på at det ikkje blir utbetalt uføretrygd når inntekta di utgjer meir enn 80 prosent av inntekta du hadde før du blei ufør, det vil seie ".expr() + uforetrygdOrdiner_inntektstak.format() + " kroner per år. Inntekta er justert opp til dagens verdi.",
				English to "Please be aware that disability benefit is not paid if your income exceeds 80 percent of the income you had prior to your disability, adjusted for inflation, i.e. NOK ".expr() + uforetrygdOrdiner_inntektstak.format() + " per year.",
			)
		}
    }
}
        