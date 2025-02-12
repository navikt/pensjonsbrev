package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU2367_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // [TBU2367NN, TBU2367, TBU2367EN]

        paragraph {
            text(
                Bokmal to "Du kan melde fra om inntektsendringer under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL. Her kan du legge inn endringer i den forventede årlige inntekten, og se hva dette betyr for utbetalingen av uføretrygden din. For at du skal få en jevn utbetaling av uføretrygden, er det viktig at du melder fra om inntektsendringer så tidlig som mulig.",
                Nynorsk to "Du kan melde frå om inntektsendringar under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL. Her kan du leggje inn endringar i den forventa årlege inntekta og sjå kva dette har å seie for utbetalinga av uføretrygda di. For at du skal få ei jamn utbetaling av uføretrygda er det viktig at du melder frå om inntektsendringar så tidleg som mogleg.",
                English to "You can register your change in income under the option “uføretrygd” at $NAV_URL. Here, you can enter changes in your anticipated annual income, and see how this will affect your disability benefit payments. In order to ensure that your disability benefit payments do not vary dramatically from one month to the next, it is important that you report changes in income as soon as possible.",
            )
        }
    }
}
