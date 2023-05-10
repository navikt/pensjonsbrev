package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*

object Vedtak {

    object Overskrift : OutlinePhrase<LangBokmalNynorskEnglish>() {

        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
            title1 {
                text(
                    Bokmal to "Vedtak",
                    Nynorsk to "Vedtak",
                    English to "Decision",
                )
            }
    }
}