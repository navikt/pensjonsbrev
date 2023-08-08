package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object Lover {

    object Folketrygdloven18ogforvaltningsloven35_1_c : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter bestemmelsene i folketrygdloven kapittel 18 og forvaltningsloven § 35 første ledd bokstav c.",
                    Nynorsk to "",
                    English to ""
                )
            }
        }
    }

    object Folketrygdloven18_7og22_12 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter bestemmelsene i folketrygdloven § 18-7 og § 22-12.",
                    Nynorsk to "",
                    English to ""
                )
            }
        }
    }

    object Folketrygdloven18og22 : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven kapittel 18 og 22.",
                )
            }
        }
    }

    data class MuligEtterbetaling(val paragraf: Expression<String>, val erEtterbetaling: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(erEtterbetaling) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven § $paragraf, § 22-12 og § 22-13.",
                        Nynorsk to "",
                        English to ""
                    )
                }
            } orShow {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven § $paragraf og § 22-12.",
                        Nynorsk to "",
                        English to ""
                    )
                }
            }
        }

    }
}
