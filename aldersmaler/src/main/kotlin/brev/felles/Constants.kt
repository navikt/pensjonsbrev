package brev.felles

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brevbaker.api.model.Telefonnummer

object Constants {
    const val NAV_URL = "nav.no"
    const val KONTAKT_URL = "$NAV_URL/kontakt"
    const val PENSJON_URL = "$NAV_URL/pensjon"
    const val SKRIVTILOSS_URL = "$NAV_URL/skriv-til-oss"
    const val NAV_KONTAKTSENTER_AAPNINGSTID = "09.00-15.00"
    const val NAV_KONTAKTSENTER_OPEN_HOURS = "09:00-15:00"
    private val navKontaktsenterPensjon = Telefonnummer("55553334")
    val NAV_KONTAKTSENTER_TELEFON_PENSJON = navKontaktsenterPensjon.format()
}