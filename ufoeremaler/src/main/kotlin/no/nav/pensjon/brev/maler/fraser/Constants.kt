package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brevbaker.api.model.Telefonnummer

object Constants {
    const val NAV_URL = "nav.no"
    const val KLAGE_URL = "$NAV_URL/klage"
    const val UFORE_URL = "$NAV_URL/uforetrygd"
    const val NAV_KONTAKTSENTER_AAPNINGSTID = "09.00-15.00"
    const val KONTAKT_URL = "$NAV_URL/kontakt"
    private val navKontaktsenterUfore = Telefonnummer("55553333")
    val NAV_KONTAKTSENTER_TELEFON_UFORE = navKontaktsenterUfore.format()
}