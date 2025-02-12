package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU2222_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // [TBU2222NN, TBU2222, TBU2222EN]

        paragraph {
            text(
                Bokmal to "Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før.",
                Nynorsk to "Dette får ikkje noko å seie for uføretrygda di, og du får utbetalt det same som før.",
                English to "This will not affect your disability benefit, and you will be paid the same amount.",
            )
        }
    }
}
