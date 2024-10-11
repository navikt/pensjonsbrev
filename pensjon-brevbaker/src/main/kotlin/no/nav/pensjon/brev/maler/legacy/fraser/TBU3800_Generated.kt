package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU3800_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //[TBU3800_NN, TBU3800, TBU3800_EN]

        paragraph {
            text(
                Bokmal to "Slik påvirker inntekt barnetillegget ditt ",
                Nynorsk to "Slik verkar inntekt inn på barnetillegget ditt",
                English to "Income will affect your child supplement",
            )
        }
    }
}
