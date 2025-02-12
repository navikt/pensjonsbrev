package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU1201_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // [TBU1201NN, TBU1201, TBU1201EN]

        title1 {
            text(
                Bokmal to "Skal du kombinere uføretrygd og inntekt?",
                Nynorsk to "Skal du kombinere uføretrygd og inntekt?",
                English to "Are you combining disability benefit and income?",
            )
        }
    }
}
