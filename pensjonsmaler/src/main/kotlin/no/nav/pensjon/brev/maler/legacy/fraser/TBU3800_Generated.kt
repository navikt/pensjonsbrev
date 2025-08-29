package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object TBU3800_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //[TBU3800_NN, TBU3800, TBU3800_EN]

        title1 {
            text(
                bokmal { + "Slik påvirker inntekt barnetillegget ditt " },
                nynorsk { + "Slik verkar inntekt inn på barnetillegget ditt" },
                english { + "Income will affect your child supplement" },
            )
        }
    }
}
