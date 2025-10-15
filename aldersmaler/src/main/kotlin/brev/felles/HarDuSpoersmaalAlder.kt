package brev.felles

import brev.felles.Constants.FAMILIEPLEIER_URL
import brev.felles.Constants.GJENLEVENDEPENSJON_URL
import brev.felles.Constants.KONTAKT_URL
import brev.felles.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import brev.felles.Constants.NAV_KONTAKTSENTER_OPEN_HOURS
import brev.felles.Constants.NAV_KONTAKTSENTER_TELEFON
import brev.felles.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import brev.felles.Constants.NAV_URL
import brev.felles.Constants.OMSORGSOPPTJENING_URL
import brev.felles.Constants.PENSJON_URL
import brev.felles.Constants.UFOERETRYGD_URL
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

class HarDuSpoersmaalAlder : OutlinePhrase<LangBokmalNynorskEnglish>() {
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
                    +"Du finner mer informasjon på $PENSJON_URL." +
                            " På $KONTAKT_URL kan du chatte eller skrive til oss." +
                            " Hvis du ikke finner svar på $NAV_URL, kan du ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON" +
                            " hverdager kl. $NAV_KONTAKTSENTER_AAPNINGSTID."
                },
                nynorsk {
                    +"Du finn meir informasjon på $PENSJON_URL." +
                            " På $KONTAKT_URL kan du chatte eller skrive til oss." +
                            " Om du ikkje finn svar på $NAV_URL, kan du ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON," +
                            " kvardagar kl. $NAV_KONTAKTSENTER_AAPNINGSTID."
                },
                english {
                    +"You can find more information at $PENSJON_URL." +
                            " At $KONTAKT_URL, you can chat or write to us." +
                            " If you do not find the answer at $NAV_URL, you can call us at: +47 $NAV_KONTAKTSENTER_TELEFON_PENSJON," +
                            " weekdays $NAV_KONTAKTSENTER_OPEN_HOURS."
                },
            )
        }
    }
}

data class HarDuSpoersmaal(
    val merInformasjonUrl: String,
    val telefonnummer: String,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Har du spørsmål?" },
                nynorsk { + "Har du spørsmål?" },
                english { + "Do you have questions?" },
            )
        }
        paragraph {
            text(
                bokmal { + "Du finner mer informasjon på $merInformasjonUrl." +
                        " På $KONTAKT_URL kan du chatte eller skrive til oss." +
                        " Hvis du ikke finner svar på ${NAV_URL}, kan du ringe oss på telefon $telefonnummer" +
                        " hverdager kl. ${NAV_KONTAKTSENTER_AAPNINGSTID}." },
                nynorsk { + "Du finn meir informasjon på $merInformasjonUrl." +
                        " På $KONTAKT_URL kan du chatte eller skrive til oss." +
                        " Om du ikkje finn svar på ${NAV_URL}, kan du ringe oss på telefon $telefonnummer," +
                        " kvardagar kl. ${NAV_KONTAKTSENTER_AAPNINGSTID}." },
                english { + "You can find more information at $merInformasjonUrl." +
                        " At ${KONTAKT_URL}, you can chat or write to us." +
                        " If you do not find the answer at ${NAV_URL}, you can call us at: +47 $telefonnummer," +
                        " weekdays ${NAV_KONTAKTSENTER_OPEN_HOURS}." },
            )
        }
    }

    companion object {
        val familiepleie =
            HarDuSpoersmaal(FAMILIEPLEIER_URL, NAV_KONTAKTSENTER_TELEFON)
        val gjenlevende =
            HarDuSpoersmaal(
                GJENLEVENDEPENSJON_URL,
                NAV_KONTAKTSENTER_TELEFON,
            )
        val ufoeretrygd =
            HarDuSpoersmaal(UFOERETRYGD_URL, NAV_KONTAKTSENTER_TELEFON)
        val omsorg =
            HarDuSpoersmaal(
                OMSORGSOPPTJENING_URL,
                NAV_KONTAKTSENTER_TELEFON_PENSJON,
            )
        val alder =
            HarDuSpoersmaal(PENSJON_URL, NAV_KONTAKTSENTER_TELEFON_PENSJON)
    }
}