package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

class Alderspensjon {
    object HarDuSpoersmaal : OutlinePhrase<LangBokmalEnglish>() {
        override fun OutlineOnlyScope<LangBokmalEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Har du spørsmål?",
                    English to "Do you have questions?",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på nav.no/pensjon. På nav.no/kontakt kan du chatte eller skrive til oss. Hvis du ikke finner svar på nav.no, kan du ringe oss på telefon 55 55 33 34, hverdager 09.00-15.00.",
                    English to "You can find more information at nav.no/pensjon. At nav.no/contact you can chat or write to us. If you do not find the answer at nav.no, you can call us at: +47 55 55 33 34, weekdays 09:00-15:00.",
                )
            }
        }

    }
}