package no.nav.pensjon.brev.maler.fraser.vedtak

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createPhrase
import no.nav.pensjon.brev.template.dsl.text

object Vedtak {
    val overskrift = createPhrase<LangBokmalNynorskEnglish, Unit> {
        title1 {
            text(
                Bokmal to "Vedtak",
                Nynorsk to "Vedtak",
                English to "Decision",
            )
        }
    }
}