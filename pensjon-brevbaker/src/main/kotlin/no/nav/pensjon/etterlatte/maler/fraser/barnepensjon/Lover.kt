package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object Lover {

    object Folketrygdloven18ogforvaltningsloven35_1_c : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Vedtaket er gjort etter bestemmelsene i folketrygdloven kapittel 18 og forvaltningsloven § 35 første ledd bokstav c.",
                    Language.Nynorsk to "",
                    Language.English to ""
                )
            }
        }
    }

    object Folketrygdloven18_7og22_12 : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Vedtaket er gjort etter bestemmelsene i folketrygdloven § 18-7 og § 22-12.",
                )
            }
        }
    }

    object Folketrygdloven18og22 : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Vedtaket er gjort etter folketrygdloven kapittel 18 og 22.",
                )
            }
        }
    }
}
