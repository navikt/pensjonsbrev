package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU3306_Generated : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        // [TBU3306_NN, TBU3306]

        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-14.",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-14.",
            )
        }
    }
}
