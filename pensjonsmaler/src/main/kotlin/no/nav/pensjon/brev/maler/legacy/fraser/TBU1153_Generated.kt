package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1153_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // [TBU1153EN, TBU1153NN, TBU1153]

        paragraph {
            text(
                Bokmal to "Du var over 26 år på uføretidspunktet og kan derfor ikke innvilges rettighet som ung ufør.",
                Nynorsk to "Du var over 26 år på uføretidspunktet, og vi kan derfor ikkje innvilge deg rett som ung ufør.",
                English to "You were over the age of 26 years old at the time of becoming disabled and therefore are not eligible for rights as a young disabled person.",
            )
        }
    }
}
