package no.nav.pensjon.brev.alder.maler.felles

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object HarDuSpoersmaalAlder : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Har du spørsmål?" },
                nynorsk { +"Har du spørsmål?" },
                english { +"Do you have questions?" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Du finner mer informasjon på ${Constants.PENSJON_URL}." +
                            " På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss." +
                            " Hvis du ikke finner svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}" +
                            " hverdager kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}."
                },
                nynorsk {
                    +"Du finn meir informasjon på ${Constants.PENSJON_URL}." +
                            " På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss." +
                            " Om du ikkje finn svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}," +
                            " kvardagar kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}."
                },
                english {
                    +"You can find more information at ${Constants.PENSJON_URL}." +
                            " At ${Constants.KONTAKT_URL}, you can chat or write to us." +
                            " If you do not find the answer at ${Constants.NAV_URL}, you can call us at: +47 ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}," +
                            " weekdays ${Constants.NAV_KONTAKTSENTER_OPEN_HOURS}."
                },
            )
        }
    }
}