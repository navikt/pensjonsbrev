package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year


//TBU3323
object HarDuSpoersmaalEtteroppgjoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Har du spørsmål?",
                Nynorsk to "Har du spørsmål?",
                English to "Do you have questions?"
            )
        }
        paragraph {
            text(
                Bokmal to "Du finner mer informasjon på ${Constants.ETTEROPPGJOR_URL}. På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss. Hvis du ikke finner svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON}, hverdager ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                Nynorsk to "Du finn meir informasjon på ${Constants.ETTEROPPGJOR_URL}. Du kan chatte med eller skrive til oss på ${Constants.KONTAKT_URL}. Dersom du ikkje finn svar på ${Constants.NAV_URL}, kan du ringje oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON}, kvardagar ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                English to "You can find more information at ${Constants.ETTEROPPGJOR_URL}. At ${Constants.KONTAKT_URL} you can chat or write to us. If you cannot find answers at ${Constants.NAV_URL}, you can call us at: +47 ${Constants.NAV_KONTAKTSENTER_TELEFON}, weekdays ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}."
            )
        }
    }
}

