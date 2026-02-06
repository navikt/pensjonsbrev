package no.nav.pensjon.brev.ufore.maler.fraser

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brevbaker.api.model.Telefonnummer

object Constants {
    const val NAV_URL = "nav.no"
    const val DITT_NAV = "$NAV_URL/dittnav"
    const val KLAGE_URL = "$NAV_URL/klage"
    const val KLAGERETTIGHETER_URL = "$NAV_URL/klagerettigheter"
    const val UFORE_URL = "$NAV_URL/uforetrygd"
    const val NAV_KONTAKTSENTER_AAPNINGSTID = "09.00-15.00"
    const val KONTAKT_URL = "$NAV_URL/kontakt"
    const val FULLMAKT_URL = "$NAV_URL/fullmakt"
    const val SOKNAD_URL = "$NAV_URL/soknader"
    const val ETTERSENDE_URL = "$NAV_URL/ettersende"
    const val SKATTEETATEN_MELD_FLYTTING = "https://www.skatteetaten.no/person/folkeregister/flytte/"
    private val navKontaktsenterUfore = Telefonnummer("55553333")
    val NAV_KONTAKTSENTER_TELEFON_UFORE = navKontaktsenterUfore.format()
}