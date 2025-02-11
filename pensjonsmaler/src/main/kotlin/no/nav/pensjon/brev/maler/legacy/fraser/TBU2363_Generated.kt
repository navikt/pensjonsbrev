package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU2363_Generated : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        // [TBU2363NN, TBU2363]

        paragraph {
            text(
                Bokmal to "Vi har derfor økt utbetalingen av uføretrygden din for resten av kalenderåret.",
                Nynorsk to "Vi har derfor auka utbetaling av uføretrygda di for resten av kalenderåret.",
            )
        }
    }
}
