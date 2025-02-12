package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU2251_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // [TBU2251NN, TBU2251, TBU2251EN]

        paragraph {
            text(
                Bokmal to "Utbetalingen av uføretrygden din er redusert fordi du har inntekt utover inntektsgrensen. Det lønner seg likevel å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene.",
                Nynorsk to "Utbetalinga av uføretrygda di er redusert fordi du har inntekt utover inntektsgrensa. Det lønner seg å jobbe, fordi inntekt og uføretrygd alltid er høgare enn uføretrygd åleine.",
                English to "Your disability benefit payment has been reduced, because your income exceeds the income limit. It pays to work, because an income and disability benefit combined will always be higher than disability benefit alone.",
            )
        }
    }
}
