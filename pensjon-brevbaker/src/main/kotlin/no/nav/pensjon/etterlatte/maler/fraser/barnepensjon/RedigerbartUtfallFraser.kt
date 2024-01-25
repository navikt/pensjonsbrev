package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object RedigerbartUtfallFraser {

    object FyllInn : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "(utfall jamfør tekstbibliotek)",
                    Language.Nynorsk to "(utfall jamfør tekstbibliotek)",
                    Language.English to "(utfall jamfør tekstbibliotek)",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven § <riktig paragrafhenvisning>.",
                    Language.Nynorsk to "Vedtaket er fatta etter føresegnene om barnepensjon i folketrygdlova § <riktig paragrafhenvisning>.",
                    Language.English to "This decision has been made pursuant to the provisions regarding children's pensions in the National Insurance Act – sections § <riktig paragrafhenvisning>.",
                )
            }
        }
    }

    object Feilutbetaling : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Skal med hvis det er feilutbetaling",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Feilutbetaling",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Siden pensjonen din er opphørt tilbake i tid, medfører dette at du har fått utbetalt for mye i pensjon i denne perioden. Du vil få eget forhåndsvarsel om eventuell tilbakekreving av det feilutbetalte beløpet.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
        }
    }
}