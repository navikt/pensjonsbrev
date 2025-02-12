package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU2364_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // [TBU2364NN, TBU2364, TBU2364EN]

        title1 {
            text(
                Bokmal to "Du må melde fra om eventuell inntekt",
                Nynorsk to "Du må melde frå om eventuell inntekt",
                English to "Report any income",
            )
        }
    }
}
