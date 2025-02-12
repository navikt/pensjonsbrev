package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU1092_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // [TBU1092NN, TBU1092EN, TBU1092]

        title1 {
            text(
                Bokmal to "Begrunnelse for vedtaket",
                Nynorsk to "Grunngiving for vedtaket",
                English to "Grounds for the decision",
            )
        }
    }
}
