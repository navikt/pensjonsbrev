package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_ENG_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.PENSJON_URL
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
                    Bokmal to "Du finner mer informasjon på $PENSJON_URL. På $KONTAKT_URL kan du chatte eller skrive til oss. Hvis du ikke finner svar på $NAV_URL, kan du ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON, hverdager 09.00-15.00.",
                    English to "You can find more information at $PENSJON_URL. At $KONTAKT_ENG_URL you can chat or write to us. If you do not find the answer at $NAV_URL, you can call us at: +47 $NAV_KONTAKTSENTER_TELEFON_PENSJON, weekdays 09:00-15:00.",
                )
            }
        }
    }

    object Returadresse : OutlinePhrase<LangBokmalEnglish>() {
        override fun OutlineOnlyScope<LangBokmalEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Nav Familie- og pensjonsytelser",
                    English to "Nav Familie- og pensjonsytelser",
                )
            }
            paragraph {
                text(
                    Bokmal to "Postboks 6600, Etterstad",
                    English to "Postboks 6600, Etterstad",
                )
            }
            paragraph {
                text(
                    Bokmal to "",
                    English to "NORWAY",
                )
            }
        }

    }

}